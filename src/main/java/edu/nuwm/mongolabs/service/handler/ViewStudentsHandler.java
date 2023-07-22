package edu.nuwm.mongolabs.service.handler;

import com.pengrad.telegrambot.model.Update;
import edu.nuwm.mongolabs.persistence.entity.UserState;
import edu.nuwm.mongolabs.service.TelegramService;
import org.springframework.stereotype.Component;

@Component
public class ViewStudentsHandler implements MessageHandler {

    private final TelegramService telegramService;
    private final DefaultMessageHandler defaultMessageHandler;

    public ViewStudentsHandler(TelegramService telegramService,
                               DefaultMessageHandler defaultMessageHandler) {
        this.telegramService = telegramService;
        this.defaultMessageHandler = defaultMessageHandler;
    }

    @Override
    public UserState getState() {
        return UserState.VIEW_STUDENTS;
    }

    @Override
    public void handleUpdate(long telegramUserId, Update update) {
        telegramService.sendMessage(telegramUserId, "view stundes");
        defaultMessageHandler.handleUpdate(telegramUserId, null);
    }
}
