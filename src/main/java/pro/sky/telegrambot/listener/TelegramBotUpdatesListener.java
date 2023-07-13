package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import javax.swing.plaf.nimbus.State;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final Pattern pattern = Pattern.compile(
            "[a-zA-Z]");

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final NotificationTaskService notificationTaskService;

    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(NotificationTaskService notificationTaskService, TelegramBot telegramBot) {
        this.notificationTaskService = notificationTaskService;
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.stream()
                .filter(update -> update.message() != null)
                .forEach(update -> {
            Message message = update.message();
            Long chatId = message.chat().id();
            String text = message.text();
            logger.info("Processing update: {}", update);
            if (message != null && "/start".equals(text)) {
                this.telegramBot.execute(new SendMessage(chatId, "Выполнена команда /start"));
            } else if (text != null) {
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    LocalDateTime dateTime = LocalDateTime.parse(matcher.group(1));
                    if (Objects.isNull(dateTime)) {
                        this.telegramBot.execute(new SendMessage(chatId, "Некорректный тип даты и/или формата времени"));
                    } else {
                        String txt = matcher.group(2);
                        NotificationTask notificationTask = new NotificationTask();
                        notificationTask.setId(chatId);
                        notificationTask.setNotificationText(txt);
                        notificationTask.setNotificationTime(dateTime);
                        notificationTaskService.save(notificationTask);
                        this.telegramBot.execute(new SendMessage(chatId, "Задача запланирована!"));
                    }
                } else {
                    this.telegramBot.execute(new SendMessage(chatId, "Некорректный формат сообщения"));
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Nullable
    private LocalDateTime parse(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private void sendMessage(Long chatId,String message){
        SendMessage sendMessage = new SendMessage(chatId,message);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Ошибка в процессе отправки сообщения: {}", sendResponse.description());
        }

    }
}
