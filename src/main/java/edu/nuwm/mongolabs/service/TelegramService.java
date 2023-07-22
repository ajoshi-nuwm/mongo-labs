package edu.nuwm.mongolabs.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

@Service
public class TelegramService {

    private final TelegramBot telegramBot;

    public TelegramService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendMessage(final Long chatId, final String message) {
        final SendMessage sendMessageRequest = buildMessage(chatId, message);
        telegramBot.execute(sendMessageRequest);
    }

    public void sendMessage(final Long chatId, final String message, final InlineKeyboardMarkup keyboard) {
        final SendMessage sendMessageRequest = buildMessage(chatId, message, keyboard);
        telegramBot.execute(sendMessageRequest);
    }

    private SendMessage buildMessage(final Long chatId, final String message) {
        return new SendMessage(chatId, message).parseMode(ParseMode.HTML);
    }

    private SendMessage buildMessage(final Long chatId, final String message, final InlineKeyboardMarkup keyboard) {
        return new SendMessage(chatId, message).replyMarkup(keyboard).parseMode(ParseMode.HTML);
    }
}
