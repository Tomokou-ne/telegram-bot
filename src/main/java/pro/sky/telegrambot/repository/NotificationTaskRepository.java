package pro.sky.telegrambot.repository;

import pro.sky.telegrambot.entity.NotificationTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

    List<NotificationTask> findAllByNotificationDataTime(LocalDateTime dateTime);

    List<NotificationTask> findAllByNotificationDateTimeAndChatId(LocalDateTime dateTime, long chatId);


}
