package cf.snowberry.smartcustomer.Models;

public class ModelChat {

    String message, messageImage, sender, receiver, timeStamp;

    public ModelChat() {
    }

    public ModelChat(String message, String messageImage, String sender, String receiver, String timeStamp) {
        this.message = message;
        this.messageImage = messageImage;
        this.sender = sender;
        this.receiver = receiver;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageImage() {
        return messageImage;
    }

    public void setMessageImage(String messageImage) {
        this.messageImage = messageImage;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
