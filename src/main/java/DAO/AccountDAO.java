package DAO;
import java.sql.*;
import java.util.*;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    
    public List<Account> GetAllAccounts() {
        Connection connection = ConnectionUtil.getConnection();

        List<Account> accounts = new ArrayList<Account>();

        try {
            String sql = "SELECT * FROM account";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return accounts;
    }

    public Account GetAccount(int id) {
        Connection connection = ConnectionUtil.getConnection();

        Account account = null;

        try {
            String sql = "SELECT * FROM account WHERE account_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return account;
    }

    public Account GetAccount(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();

        Account account = null;

        try {
            String sql = "SELECT * FROM account WHERE username=? AND password=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return account;
    }

    public Account AddAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            
            if(pkeyResultSet.next()){
                int accountId = (int) pkeyResultSet.getLong(1);
                return new Account(accountId, account.getUsername(), account.getPassword());
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
