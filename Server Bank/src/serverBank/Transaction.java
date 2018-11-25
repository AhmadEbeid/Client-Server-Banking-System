package serverBank;

import com.j256.ormlite.field.DatabaseField;

public class Transaction {

	@DatabaseField(id = true)
	private String transactionID;
	
	@DatabaseField(canBeNull = false)
	private String name;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private BankAccount otherPartyBankAccount;
	
	@DatabaseField(canBeNull = false)
	private String amount;
	
	@DatabaseField(canBeNull = false)
	private String prevAmount;
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private BankAccount bankAccount;
	
	Transaction(){}
	
	Transaction(String n, double am, double prevAm, BankAccount account, BankAccount other){
		name = Encryptor.encrypt(n);
		amount = Encryptor.encrypt(String.valueOf(am));
		prevAmount = Encryptor.encrypt(String.valueOf(prevAm));
		otherPartyBankAccount = other;
		bankAccount = account;
		transactionID = Encryptor.encrypt(String.valueOf(ServerSide.bankNumber) + String.valueOf(System.currentTimeMillis()));
	}
	
	
	String getTransactionName() {
		return name;
	}
	
	double getTransactionAmount() {
		return Double.parseDouble(Encryptor.decrypt(amount));
	}
	
	double getPrevAmount() {
		return Double.parseDouble(Encryptor.decrypt(prevAmount));
	}
	
	BankAccount getOtherPartyBankAccount() {
		return otherPartyBankAccount;
	}
	
	BankAccount getBankAccount() {
		return bankAccount;
	}
	
	long getTransactionID() {
		return Long.parseLong(Encryptor.decrypt(transactionID));
	}
}
