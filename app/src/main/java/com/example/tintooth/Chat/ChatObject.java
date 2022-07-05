package com.example.tintooth.Chat;

public class ChatObject {
    private String message;
    private Boolean currentUser;//userID
    private Boolean isSeen;

    public ChatObject(String message, Boolean currentUser, Boolean isSeen) {
        this.message = message;
        this.currentUser = currentUser;
        this.isSeen = isSeen;
    }

    public ChatObject(String message, Boolean currentUser) {
        this.message = message;
        this.currentUser = currentUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Boolean currentUser) {
        this.currentUser = currentUser;
    }

    public Boolean getSeen() {
        return isSeen;
    }

    public void setSeen(Boolean seen) {
        isSeen = seen;
    }
}
