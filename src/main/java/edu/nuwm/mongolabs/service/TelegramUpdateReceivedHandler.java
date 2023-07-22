package edu.nuwm.mongolabs.service;

import com.pengrad.telegrambot.model.Update;
import edu.nuwm.mongolabs.persistence.entity.User;
import edu.nuwm.mongolabs.persistence.entity.UserState;
import edu.nuwm.mongolabs.persistence.repository.UserRepository;
import edu.nuwm.mongolabs.service.handler.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
public class TelegramUpdateReceivedHandler implements ApplicationListener<TelegramUpdateReceived> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramUpdateReceivedHandler.class);

    private final UserRepository userRepository;
    private final Map<UserState, MessageHandler> handlersByState;

    public TelegramUpdateReceivedHandler(UserRepository userRepository, List<MessageHandler> messageHandlers) {
        this.userRepository = userRepository;
        this.handlersByState = messageHandlers.stream()
                .collect(Collectors.toMap(MessageHandler::getState, Function.identity()));
    }

    @Override
    public void onApplicationEvent(TelegramUpdateReceived event) {
        try {
            final Update update = event.getTelegramUpdate();
            if (isTextMessage(update)) {
                handleTextMessage(update);
                return;
            }
            if (isCallbackMessage(update)) {
                handleCallback(update);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void handleTextMessage(final Update update) {
        final long chatId = update.message().chat().id();

        final UserState userState = getUserState(chatId);
        handlersByState.get(userState).handleUpdate(chatId, update);
    }

    private void handleCallback(final Update update) {
        final long chatId = update.callbackQuery().message().chat().id();

        final UserState userState = getUserState(chatId);
        handlersByState.get(userState).handleUpdate(chatId, update);
    }

    private UserState getUserState(final long telegramUserId) {
        return ofNullable(userRepository.findByTelegramUserId(telegramUserId))
                .map(User::getUserState)
                .orElse(UserState.DEFAULT);
    }

    private boolean isTextMessage(final Update update) {
        return update.message() != null;
    }

    private boolean isCallbackMessage(final Update update) {
        return update.callbackQuery() != null;
    }
}
