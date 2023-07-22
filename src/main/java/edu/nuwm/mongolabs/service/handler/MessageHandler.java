package edu.nuwm.mongolabs.service.handler;

import com.pengrad.telegrambot.model.Update;
import edu.nuwm.mongolabs.persistence.entity.UserState;

public interface MessageHandler {
    UserState getState();

    void handleUpdate(long telegramUserId, Update update);

}
