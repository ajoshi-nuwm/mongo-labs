package edu.nuwm.mongolabs.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.nuwm.mongolabs.service.TelegramUpdateListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL;

@Configuration
public class TelegramConfiguration {

    private final TelegramUpdateListener telegramUpdateListener;

    public TelegramConfiguration(TelegramUpdateListener telegramUpdateListener) {
        this.telegramUpdateListener = telegramUpdateListener;
    }

    @Bean
    public TelegramBot telegramBot(@Value("${telegram.bot.token}") final String botToken) {
        final TelegramBot telegramBot = new TelegramBot(botToken);
        telegramBot.setUpdatesListener(updates -> {
            telegramUpdateListener.onUpdates(updates);
            return CONFIRMED_UPDATES_ALL;
        });
        return telegramBot;
    }
}
