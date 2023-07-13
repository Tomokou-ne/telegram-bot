package pro.sky.telegrambot.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table (name = "notification_task")
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "chat_id", nullable = false)
    int chatId;
    @Column(name = "notification_text", nullable = false)
    String notificationText;
    @Column(name = "notification_time_data", nullable = false)
    LocalDateTime notificationTime;

    public NotificationTask(long id, int chatId, String notificationText, LocalDateTime notificationTime) {
        this.id = id;
        this.chatId = chatId;
        this.notificationText = notificationText;
        this.notificationTime = notificationTime;
    }

    public NotificationTask() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public LocalDateTime getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(LocalDateTime notificationTime) {
        this.notificationTime = notificationTime;
    }


}
