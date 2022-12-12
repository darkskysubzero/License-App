package com.example.licenseapp.Messages;

public class MessagesModel  {
    private String messageID;
    private String messageText;
    private String messageDescription;

    public String getMessageDescription() {
        return messageDescription;
    }

    public void setMessageDescription(String messageDescription) {
        this.messageDescription = messageDescription;
    }

    public MessagesModel(String messageID, String messageText, String messageDescription) {
        this.messageID = messageID;
        this.messageText = messageText;
        this.messageDescription = messageDescription;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
