package com.makey.telegram;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "MakeyBot";
    }
    @Override
    public String getBotToken() {
        return "1649497702:AAEgKVYqKpx9fpGBEUzU2uFExwQEdc9wTlM";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()&& update.getMessage().hasText()) {
            var message = SendMessage.builder()
                    .chatId(String.valueOf(update.getMessage().getChatId()))
                    .text(update.getMessage().getText())
                    .build();
            try {
                execute(message);
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
