package edu.nuwm.mongolabs.service.handler;

import com.pengrad.telegrambot.model.Update;
import edu.nuwm.mongolabs.persistence.entity.User;
import edu.nuwm.mongolabs.persistence.entity.UserState;
import edu.nuwm.mongolabs.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DefaultMessageKeyboardOptionChosenHandler implements MessageHandler {

    private final UserRepository userRepository;
    private final Map<UserState, MessageHandler> handlersByState;

    public DefaultMessageKeyboardOptionChosenHandler(UserRepository userRepository,
                                                     List<MessageHandler> messageHandlers) {
        this.userRepository = userRepository;
        this.handlersByState = messageHandlers.stream()
                .collect(Collectors.toMap(MessageHandler::getState, Function.identity()));
    }

    @Override
    public UserState getState() {
        return UserState.DEFAULT_KEYBOARD_OPTION_CHOSEN;
    }

    @Override
    public void handleUpdate(long telegramUserId, Update update) {
        final User user = userRepository.findByTelegramUserId(telegramUserId);
        final String data = update.callbackQuery().data();
        final UserState userState = UserState.valueOf(data);

        user.setUserState(userState);
        userRepository.save(user);

        final MessageHandler messageHandler = handlersByState.get(userState);
        if (messageHandler == null) {
            handlersByState.get(UserState.DEFAULT).handleUpdate(telegramUserId, update);
            return;
        }
        messageHandler.handleUpdate(telegramUserId, null);
    }
}
