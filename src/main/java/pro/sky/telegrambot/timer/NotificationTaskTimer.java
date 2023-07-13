package pro.sky.telegrambot.timer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class NotificationTaskTimer {

    private final NotificationTaskRepository notificationTaskRepository;

    private final TelegramBot telegramBot;

    public NotificationTaskTimer(NotificationTaskRepository notificationTaskRepository, TelegramBot telegramBot) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBot = telegramBot;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void task() {
        notificationTaskRepository.findAllByNotificationDataTime(LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES))
                .forEach(notificationTask -> {
                telegramBot.execute(
                        new SendMessage(notificationTask.getChatId(), "Напоминание о задаче " + notificationTask.getNotificationText()));
                notificationTaskRepository.delete(notificationTask);
                });
    }
}
