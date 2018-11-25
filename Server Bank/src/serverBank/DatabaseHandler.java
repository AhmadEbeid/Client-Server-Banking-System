package serverBank;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHandler {

	ConnectionSource connection;
	Dao<Account, String> accountDao;
	Dao<Transaction, String> transactionDao;
	Dao<BankAccount, String> bankAccountDao;
	Account account;
	BankAccount bankAccount;
	
	DatabaseHandler() throws SQLException {
			connection = new JdbcConnectionSource("jdbc:sqlite:bankingSystem"+ String.valueOf(ServerSide.bankNumber) +".db");
			
			accountDao = DaoManager.createDao(connection, Account.class);
			TableUtils.createTableIfNotExists(connection, Account.class);
			
			transactionDao = DaoManager.createDao(connection, Transaction.class);
			TableUtils.createTableIfNotExists(connection, Transaction.class);
			
			bankAccountDao = DaoManager.createDao(connection, BankAccount.class);
			TableUtils.createTableIfNotExists(connection, BankAccount.class);
			bankAccount = null;
			account = null;
	}
	
	
	DatabaseHandler(String bankAccountID) {
		try {
			connection = new JdbcConnectionSource("jdbc:sqlite:bankingSystem"+ String.valueOf(ServerSide.bankNumber) +".db");
			
			accountDao = DaoManager.createDao(connection, Account.class);
			TableUtils.createTableIfNotExists(connection, Account.class);
			
			transactionDao = DaoManager.createDao(connection, Transaction.class);
			TableUtils.createTableIfNotExists(connection, Transaction.class);
			
			bankAccountDao = DaoManager.createDao(connection, BankAccount.class);
			TableUtils.createTableIfNotExists(connection, BankAccount.class);
			bankAccount = bankAccountDao.queryForId(Encryptor.encrypt(String.valueOf(bankAccountID)));
			account = bankAccount.getAccountOwner();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	

	void close() throws IOException {
		connection.close();
	}
	
	void updateData() throws SQLException {
		bankAccount = bankAccountDao.queryForId(Encryptor.encrypt(String.valueOf(bankAccount.getBankAccountID())));
		account = bankAccount.getAccountOwner();
	}
	
	boolean createNewAccount(String name, String password) {
		try {
			account = new Account(name, password);
			accountDao.create(account);
			return true;
		}catch(Exception ex) {
			return false;
		}
	}
	
	boolean createNewBankAccount(double amount) {
		try {
			bankAccount = new BankAccount(account, amount);
			bankAccountDao.create(bankAccount);
			transactionDao.create(new Transaction("Intial Amount", amount, amount, bankAccount, null));
			return true;
		}catch(Exception ex) {
			return false;
		}
	}
	
	int depositMoney(double amount) {
		try {
			updateData();
			if(bankAccount.depositMoney(transactionDao, amount, bankAccountDao)) {
				return 1;
			}
			return 0;
		}catch(Exception ex) {
			return -1;			
		}
	}
	
	int withdrawMoney(double amount) {
		try {
			updateData();
			if(bankAccount.withdrawMoney(transactionDao, amount, bankAccountDao)) {
				return 1;
			}
			return 0;
		}catch(Exception ex) {
			return -1;			
		}
	}
	
	int transferMoney(double amount, String bankAccountNumber) {
		try {
			updateData();
			if(bankAccount.transferMoney(transactionDao, amount, bankAccountNumber, bankAccountDao)) {
				return 1;
			}
			return 0; 
		}catch(Exception ex) {
			return -1;			
		}
	}
	
	int transferMoneyOut(double amount, String bankAccountNumber) {
		try {
			updateData();
			if(bankAccount.transferMoneyOut(transactionDao, amount, bankAccountNumber, bankAccountDao)) {
				return 1;
			}
			return 0; 
		}catch(Exception ex) {
			return -1;			
		}
	}
	
	int transferMoneyIn(double amount, String otherBankAccountNumber) {
		try {
			updateData();
			if(bankAccount.transferMoneyIn(transactionDao, amount, otherBankAccountNumber, bankAccountDao)) {
				return 1;
			}
			return 0; 
		}catch(Exception ex) {
			return -1;			
		}
	}
	
	int checkLogin(SignIn signIn) {
		
		try {
			BankAccount b = bankAccountDao.queryForId(Encryptor.encrypt(String.valueOf(signIn.bankAccountID)));
			if(b != null && b.getAccountOwner().getPassword().equals(signIn.password)) {
				return 1;
			}
			return 0;
		} catch (SQLException e) {
			return 0;
		}
		
	}
	
	boolean checkBankAccount(String accountNo) {
		try {
			BankAccount b = bankAccountDao.queryForId(Encryptor.encrypt(String.valueOf(accountNo)));
			if(b == null)
				return false;
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	List<Transaction> getTransactions() {
		try {
			updateData();
			return transactionDao.query(transactionDao.queryBuilder().where().like("bankAccount_id", bankAccount.getEncryptedBankAccountID()).prepare());
		} catch (SQLException e) {
			return null;
		}
	}
	
	static List<BankAccount> getBankAccounts() {
		try {
			ConnectionSource connection = new JdbcConnectionSource("jdbc:sqlite:bankingSystem"+ String.valueOf(ServerSide.bankNumber) +".db");
			Dao<BankAccount, String> bankAccountDao = DaoManager.createDao(connection, BankAccount.class);
			return bankAccountDao.queryForAll();
		} catch (SQLException e) {
			return new ArrayList<BankAccount>();
		}
	}
	
//	boolean accountExist(SignIn signIn) {
//		return false;
//	}
	
}
