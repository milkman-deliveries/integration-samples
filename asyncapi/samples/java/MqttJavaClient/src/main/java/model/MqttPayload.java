package model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Data
@Builder
@Jacksonized
public class MqttPayload {
    MqttNotificationType mqttNotificationType;
    String installationId;
    Map<String, Object> notificationParams;
}