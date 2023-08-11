const mqtt = require('mqtt');

const host = "app.{TODO: tenant code here}.milkmantechnologies.com";
const topic = "mds/tenants/{TODO: tenant code here}/notifications";
const token = "TODO: Insert here a new valid IdToken";

const clientId = "client-unique-identifier";

const options = {
  keepalive: 60,
  hostname: host,
  path: "/mqtt/wsk",
  clientId: clientId,
  protocol: 'wss',
  port:443,
  reconnectPeriod: 1000,
  connectTimeout: 30 * 1000,
  username: token,
  password: "null",
  protocolVersion: 5,
}

console.log('Connecting mqtt client')
const client = mqtt.connect(host, options)

client.on('error', (err) => {
  console.error('Connection error: ', err)
  client.end()
})

client.on('reconnect', () => {
  console.log('Reconnecting...')
})

client.on('connect', (packet) => {
  console.log('Client connected:' + clientId)
  // Subscribe
  let options = {
    qos: 1,
  };
  client.subscribe(topic, options, (err, granted) => {
    if(err != null) {
      console.error('Subscription error: ', err);
      return;
    }
    granted.forEach(g => {
      console.log('Topic subscribed ' + g.topic + ' with QoS ' + g.qos);
    });
  });
});

client.on('message', (topic, payload, packet) => {

  const json = payload.toString();
  console.log('Message received, payload: ' + json);

  if(packet.properties.correlationData) {
    console.log('Message correlationData: ' + packet.properties.correlationData.toString());
  }

  if(packet.properties.userProperties) {
    for (let key in packet.properties.userProperties) {
      console.log('User Property ' + key + " = " + packet.properties.userProperties[key]);
    }
  }

  const notificationPayload = JSON.parse(json);
  const {notificationType, installationId, notificationParams} = notificationPayload;
  console.log("Notification Type: " + notificationType);
  console.log("Installation ID: " + installationId);
  for (let key in notificationParams) {
    console.log("Param " + key + " = " + notificationParams[key]);
  }
});


