package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account AddAccount(Account account) {
        return accountDAO.AddAccount(account);
    }

    public Account GetAccount(String username, String password) {
        return accountDAO.GetAccount(username, password);
    }

    public Account getAccount(int accountId) {
        return accountDAO.GetAccount(accountId);
    }

}
