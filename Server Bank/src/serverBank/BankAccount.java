package serverBank;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

public class BankAccount {
	
	@DatabaseField(id = true)
	private String bankAccountID;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private Account accountOwner;
	
	@DatabaseField(defaultValue = "0")
	private String amount;
	
	
	BankAccount(){}
	
	BankAccount(Account a, double am){
		accountOwner = a;
		amount =  Encryptor.encrypt(String.valueOf(am));
		bankAccountID = Encryptor.encrypt(String.valueOf(ServerSide.bankNumber) + String.valueOf(System.currentTimeMillis()));
	}
	
	Account getAccountOwner() {
		return accountOwner;
	}
	
	double getAmount() {
		return Double.parseDouble(Encryptor.decrypt(amount));
	}
	
	long getBankAccountID() {
		return Long.parseLong(Encryptor.decrypt(bankAccountID));
	}
	
	String getEncryptedAmount() {
		return amount;
	}
	
	String getEncryptedBankAccountID() {
		return bankAccountID;
	}
	

	boolean depositMoney(Dao<Transaction, String> dao, double deposit, Dao<BankAccount, String> daoBank) throws SQLException {
		if(deposit > 0) {
			double am = Double.parseDouble(Encryptor.decrypt(amount)) + deposit;
			amount = Encryptor.encrypt(String.valueOf(am));
			dao.create(new Transaction("Deposit", deposit, am, this, null));
			daoBank.update(this);
			return true;
		}
		return false;
	}
	
	boolean withdrawMoney(Dao<Transaction, String> dao, double withdraw, Dao<BankAccount, String> daoBank) throws SQLException {
		double amm = Double.parseDouble(Encryptor.decrypt(amount));
		if(withdraw <= amm && withdraw > 0) {
//			amount -= withdraw;
			double am = amm - withdraw;
			amount = Encryptor.encrypt(String.valueOf(am));
			dao.create(new Transaction("Withdraw", withdraw, am, this, null));
			daoBank.update(this);
			return true;
		}
		return false;
	}
	
	boolean transferMoneyFrom(Dao<Transaction, String> dao, double transfer,  BankAccount otherPartyID, Dao<BankAccount, String> daoBank) throws SQLException {
		if(transfer > 0) {
//			amount += transfer;
			double am = Double.parseDouble(Encryptor.decrypt(amount)) + transfer;
			amount = Encryptor.encrypt(String.valueOf(am));
			
			dao.create(new Transaction("Transfer From", transfer, am, this, otherPartyID));
			daoBank.update(this);
			return true;
		}
		return false;
	}
	
	
	boolean transferMoney(Dao<Transaction, String> dao, double transfer, String otherPartyID, Dao<BankAccount, String> daoBank) throws SQLException {
		double amm = Double.parseDouble(Encryptor.decrypt(amount));
		if(transfer <= amm && transfer > 0 && !otherPartyID.equals(String.valueOf(this.bankAccountID))) {
			BankAccount other = daoBank.queryForId(Encryptor.encrypt(otherPartyID));
			other.transferMoneyFrom(dao, transfer, this, daoBank);
//			amount -= transfer;
			
			double am = amm - transfer;
			amount = Encryptor.encrypt(String.valueOf(am));
			
			dao.create(new Transaction("Transfer To", transfer, am, this, other));
			daoBank.update(this);
			daoBank.update(other);
			return true;
		}
		return false;
	}
	
	boolean transferMoneyOut(Dao<Transaction, String> dao, double transfer, String otherPartyID, Dao<BankAccount, String> daoBank) throws SQLException {

		double amm = Double.parseDouble(Encryptor.decrypt(amount));
		
		if(transfer <= amm && transfer > 0 && !otherPartyID.equals(String.valueOf(this.bankAccountID))) {
//			amount -= transfer;
			double am = amm - transfer;
			amount = Encryptor.encrypt(String.valueOf(am));
			
			dao.create(new Transaction("Transfer Out To " + otherPartyID, transfer, am, this, null));
			daoBank.update(this);
			return true;
		}
		return false;
	}
	
	boolean transferMoneyIn(Dao<Transaction, String> dao, double transfer, String otherPartyID, Dao<BankAccount, String> daoBank) throws SQLException {
		
//		double amm = Double.parseDouble(Encryptor.decrypt(amount));
		if(transfer > 0 && !otherPartyID.equals(String.valueOf(this.bankAccountID))) {
			
			double am = Double.parseDouble(Encryptor.decrypt(amount)) + transfer;
			amount = Encryptor.encrypt(String.valueOf(am));
			
			dao.create(new Transaction("Transfer Out From " + otherPartyID, transfer, am, this, null));
			daoBank.update(this);
			return true;
		}
		return false;
	}

	
	
	
	
	
//	boolean addTransuction(Dao<Transaction, String> dao, Transaction transaction, Dao<BankAccount, String> daoBank) throws SQLException {
//		
//	
//		if(transaction.getTransactionName().equals("withdraw")) {
//			if(withdrawMoney(transaction.getTransactionAmount())) {
//				dao.create(transaction);
//				return true;
//			}else {
//				return false;
//			}
//		}
//		else if(transaction.getTransactionName().equals("transfer")) {
//			if(withdrawMoney(transaction.getTransactionAmount())) {
//				BankAccount other = daoBank.queryForId(String.valueOf(transaction.getOtherPartyBankAccount()));
//				other.depositMoney(transaction.getTransactionAmount());
//				dao.create(transaction);
//				return true;
//			}else {
//				return false;
//			}
//		}
//		else if(transaction.getTransactionName().equals("deposit")) {
//			if(depositMoney(transaction.getTransactionAmount())) {
//				dao.create(transaction);
//				return true;
//			}else {
//				return false;
//			}
//		}
//		else {ssss
//			return false;
//		}
//	}
	
}
