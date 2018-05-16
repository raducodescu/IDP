package idp.cs.pub.ro.proiect;

import java.util.Date;

public class Message {
    private String messageText;
    private String messageUser;
    private Date messageTime;
    private String sender;
    private String receiver;

    public Message(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        messageTime = new Date();
    }

    public Message(String messageText, String messageUser, String sender, String receiver) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.sender = sender;
        this.receiver = receiver;
        messageTime = new Date();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Message() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }
}
