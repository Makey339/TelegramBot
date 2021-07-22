package com.makey.telegram;


import com.makey.telegram.models.Message;
import com.makey.telegram.models.User;
import com.makey.telegram.services.MessageService;
import com.makey.telegram.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Date;

@Component
public class Bot extends TelegramLongPollingBot {

    private static final Logger LOGGER= LoggerFactory.getLogger(Bot.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }
    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()&& update.getMessage().hasText()) {
            LOGGER.info("UPDATE RECEIVED id=" + update.getUpdateId());
            var message = SendMessage.builder()
                    .chatId(String.valueOf(update.getMessage().getChatId()))
                    .text("Мне нечего ответить")
                    .build();


            if (update.getMessage().getText().equals("/start")) {

                User user = new User();
                user.setId(update.getMessage().getFrom().getId());
                user.setFirst_name(update.getMessage().getFrom().getFirstName());
                user.setLast_name(update.getMessage().getFrom().getLastName());
                user.setUser_name(update.getMessage().getFrom().getUserName());
                Date time=new Date((long)update.getMessage().getDate()*1000);
                user.setLastmsg_date(time);

                userService.save(user);

                message = SendMessage.builder()
                        .chatId(String.valueOf(update.getMessage().getChatId()))
                        .text("Вы зарегестрировались")
                        .build();
            }


            Message msg = new Message();
            msg.setText(update.getMessage().getText());
            Date time=new Date((long)update.getMessage().getDate()*1000);
            msg.setDate(time);
            msg.setUser(userService.getById(update.getMessage().getFrom().getId()));
            msg.setChat_id(update.getMessage().getChatId());
            messageService.save(msg);


            try {

                execute(message);

                LOGGER.info("Сообщение отправлено");
                Message sendmsg = new Message();
                sendmsg.setText(message.getText());
                sendmsg.setDate(new Date(System.currentTimeMillis()));
                sendmsg.setUser(null);
                sendmsg.setChat_id(update.getMessage().getChatId());
                messageService.save(sendmsg);

            }
            catch (TelegramApiException e) {
                LOGGER.error("Ошибка отправки сообщения ",e);
            }
        }
    }
}
