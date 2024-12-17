package DAO;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.*;

public class MessageDAO {

    public Message addMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            
            if (pkeyResultSet.next()) {
                int messageId = (int) pkeyResultSet.getLong(1);
                return new Message(messageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
    
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();

        List<Message> messages = new ArrayList<Message>();

        try {
            String sql = "SELECT * FROM message";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public List<Message> getAllMessages(int accountId) {
        Connection connection = ConnectionUtil.getConnection();

        List<Message> messages = new ArrayList<Message>();

        try {
            String sql = "SELECT * FROM message WHERE posted_by=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public Message getMessage(int id) {
        Connection connection = ConnectionUtil.getConnection();

        Message message = null;

        try {
            String sql = "SELECT * FROM message WHERE message_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            
            if (rs.next()) {
                message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
            
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return message;
    }

    public Message deleteMessage(int id) {
        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "DELETE FROM message WHERE message_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();

            if (pkeyResultSet.next()) {
                int messageId = (int) pkeyResultSet.getLong("message_id");
                return new Message(messageId, pkeyResultSet.getInt("posted_by"), pkeyResultSet.getString("message_text"), pkeyResultSet.getLong("time_posted_epoch"));
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message updateMessage(int id, String text) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE message SET message_text=? WHERE message_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, text);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();

            if (pkeyResultSet.next()) {
                int messageId = (int) pkeyResultSet.getLong("message_id");
                return getMessage(messageId);
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
