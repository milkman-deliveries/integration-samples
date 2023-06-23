package com.milkmantechnologies.mqttjavaclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import model.MqttPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootApplication
public class MqttJavaClientApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MqttJavaClientApplication.class, args);
	}


	@Value("${mqttHost}")
	private String mqttHost;

	@Value("${mqttPort}")
	private int mqttPort;

	@Value("${mqttServerPath}")
	private String mqttServerPath;

	@Value("${mqttUsername}")
	private String mqttUsername;

	@Value("${mqttPassword}")
	private String mqttPassword;

	@Value("${mqttTopic}")
	private String mqttTopic;

	private final ObjectMapper mapper = new ObjectMapper();


	@Override
	public void run(String... args) throws Exception {
		Mqtt5AsyncClient client = buildClient();

		client.connect()
				.thenCompose(mqtt5ConnAck -> {
					System.out.println("Connected: " + mqtt5ConnAck.getReasonCode());
					return client.subscribeWith()
							.topicFilter(mqttTopic)
							.qos(MqttQos.AT_LEAST_ONCE)
							.callback(this::handleMessage)
							.send();
				})
				.exceptionally(throwable -> {
					System.err.println("Error: " + throwable.toString());
					return null;
				});
	}

	Mqtt5AsyncClient buildClient() {
		return Mqtt5Client.builder()
				.identifier("client-unique-identifier")
				.serverHost(mqttHost)
				.serverPort(mqttPort)
				.sslWithDefaultConfig()
				.simpleAuth()
				.username(mqttUsername)
				.password(mqttPassword.getBytes())
				.applySimpleAuth()
				.webSocketConfig()
				.serverPath(mqttServerPath)
				.subprotocol("mqtt")
				.applyWebSocketConfig()
				.buildAsync();
	}

	private void handleMessage(Mqtt5Publish publish) {
		if (publish.getCorrelationData().isPresent()) {
			System.out.println("Received publish with correlation data: " +
					UTF_8.decode(publish.getCorrelationData().get()));
		}

		publish.getUserProperties().asList().forEach(p -> {
			System.out.println("Header: " + p.getName() + " = " + p.getValue());
		});

		if (publish.getPayload().isPresent()) {
			String json = UTF_8.decode(publish.getPayload().get()).toString();
			System.out.println("Payload: " + json);

			try {
				MqttPayload payload = mapper.readValue(json, MqttPayload.class);
				System.out.println("NotificationType: " + payload.getMqttNotificationType());
				System.out.println("Object toString(): " + payload);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}
		else {
			System.err.println("No payload...");
		}
	}
}
