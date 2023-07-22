package edu.nuwm.mongolabs.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private long telegramUserId;
    private UserState userState;

    public User(long telegramUserId) {
        this.telegramUserId = telegramUserId;
        this.userState = UserState.DEFAULT;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(long telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }
}
