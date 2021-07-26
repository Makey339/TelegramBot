package com.makey.telegram;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.makey.telegram.models.Domain;
import com.makey.telegram.models.Message;
import com.makey.telegram.models.User;
import com.makey.telegram.services.DomainService;
import com.makey.telegram.services.MessageService;
import com.makey.telegram.services.UserService;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private DomainService domainService;

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
                user.setLastmsg_date(new Date((long)update.getMessage().getDate()*1000));
                userService.save(user);

                message = SendMessage.builder()
                        .chatId(String.valueOf(update.getMessage().getChatId()))
                        .text("Вы зарегестрировались")
                        .build();
            }

            Date time=new Date((long)update.getMessage().getDate()*1000);

            User user = userService.getById(update.getMessage().getFrom().getId());
            user.setLastmsg_date(time);
            userService.save(user);

            Message msg = new Message();
            msg.setText(update.getMessage().getText());
            msg.setDate(time);
            msg.setUser(user);
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
            catch (Exception e) {
                LOGGER.error("Ошибка отправки сообщения ",e);
            }
        }
    }

    @Scheduled(cron = "0 12 * * * *")
    public void DomainHandler() throws Exception {
        domainService.deleteAll();
        domainService.saveAll(getDomains());
        sendSuccessMessage();
    }

    private List<Domain> getDomains() {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("https://backorder.ru/json/?order=desc&expired=1&by=hotness&page=1&items=50");
            HttpResponse httpResponse = httpClient.execute(httpGet);
            String body = EntityUtils.toString(httpResponse.getEntity());
            return OBJECT_MAPPER.readValue(body , new TypeReference<List<Domain>>(){});
        } catch (Exception e) {
            LOGGER.warn("Ошибка при получении доменов",e);
            return Lists.newArrayList();
        }
    }

    private void sendSuccessMessage() {
        long count = domainService.count();
        List<User> users = userService.getAll();
        for (User i:users) {
            SendMessage message = SendMessage.builder()
                    .chatId(String.valueOf(i.getId()))
                    .text("Количество собранных доменов - " + count)
                    .build();
            try {
                execute(message);
                LOGGER.info("Сообщение о сборе доменов отправлено");

                Message sendmsg = new Message();
                sendmsg.setText(message.getText());
                sendmsg.setDate(new Date(System.currentTimeMillis()));
                sendmsg.setUser(null);
                sendmsg.setChat_id(i.getId());
                messageService.save(sendmsg);
            } catch (Exception e) {
                LOGGER.error("Ошибка отправки сообщения",e);
            }
        }
    }
}
