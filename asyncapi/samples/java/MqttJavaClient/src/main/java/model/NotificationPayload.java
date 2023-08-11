package model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Data
@Builder
@Jacksonized
public class NotificationPayload {
    NotificationType notificationType;
    String installationId;
    Map<String, Object> notificationParams;
}