package edu.nuwm.mongolabs.service.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import edu.nuwm.mongolabs.persistence.entity.User;
import edu.nuwm.mongolabs.persistence.entity.UserState;
import edu.nuwm.mongolabs.persistence.repository.UserRepository;
import edu.nuwm.mongolabs.service.TelegramService;
import org.springframework.stereotype.Component;

@Component
public class DefaultMessageHandler implements MessageHandler {

    private final TelegramService telegramService;
    private final UserRepository userRepository;

    public DefaultMessageHandler(TelegramService telegramService, UserRepository userRepository) {
        this.telegramService = telegramService;
        this.userRepository = userRepository;
    }

    @Override
    public UserState getState() {
        return UserState.DEFAULT;
    }

    @Override
    public void handleUpdate(long telegramUserId, Update update) {
        final User user = findOrCreateUser(telegramUserId);
        final String message = getMessage(user);
        final InlineKeyboardMarkup keyboard = getKeyboard();

        user.setUserState(UserState.DEFAULT_KEYBOARD_OPTION_CHOSEN);
        userRepository.save(user);

        telegramService.sendMessage(telegramUserId, message, keyboard);
    }

    private String getMessage(final User user) {
        return user.getId() + " : " + user.getTelegramUserId() + " : " + user.getUserState();
    }

    private InlineKeyboardMarkup getKeyboard() {
        final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        final InlineKeyboardButton addStudent = new InlineKeyboardButton("Додати студента")
                .callbackData(UserState.ADD_STUDENT.name());
        final InlineKeyboardButton removeStudent = new InlineKeyboardButton("Видалити студента")
                .callbackData(UserState.REMOVE_STUDENT.name());
        final InlineKeyboardButton viewStudents = new InlineKeyboardButton("Переглянути список студентів")
                .callbackData(UserState.VIEW_STUDENTS.name());
        keyboard.addRow(addStudent);
        keyboard.addRow(removeStudent);
        keyboard.addRow(viewStudents);
        return keyboard;
    }

    private User findOrCreateUser(final long telegramUserId) {
        final User existingUser = userRepository.findByTelegramUserId(telegramUserId);
        if (existingUser != null) {
            return existingUser;
        }
        return createUser(telegramUserId);
    }

    private User createUser(final long telegramUserId) {
        final User user = new User(telegramUserId);
        return userRepository.save(user);
    }
}
