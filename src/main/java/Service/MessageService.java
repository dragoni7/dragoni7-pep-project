package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.*;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public Message addMessage(Message message) {
        return messageDAO.addMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessage(int id) {
        return messageDAO.getMessage(id);
    }

    public Message deleteMessage(int id) {
        return messageDAO.deleteMessage(id);
    }

    public Message updateMessage(int id, String text) {
        return messageDAO.updateMessage(id, text);
    }

    public List<Message> getUserMessages(int accountId) {
        return messageDAO.getAllMessages(accountId);
    }
}
