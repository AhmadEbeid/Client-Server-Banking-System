import java.util.Date;

public class Transaction {

	private String transactionID;
	
	private String name;
	
	private BankAccount otherPartyBankAccount;
	
	private String amount;
	
	private String prevAmount;
	
	private BankAccount bankAccount;
	
	Transaction(){}
	
	String getTransactionName() {
		return Encryptor.decrypt(name);
	}
	
	double getTransactionAmount() {
		return Double.parseDouble(Encryptor.decrypt(String.valueOf(amount)));
	}
	
	double getPrevAmount() {
		return Double.parseDouble(Encryptor.decrypt(String.valueOf(prevAmount)));
	}
	
	BankAccount getOtherPartyBankAccount() {
		return otherPartyBankAccount;
	}
	
	BankAccount getBankAccount() {
		return bankAccount;
	}
	
	long getTransactionID() {
		return Long.parseLong(Encryptor.decrypt(String.valueOf(transactionID)));
	}
	
	String getDate() {
		long a = Long.parseLong(Encryptor.decrypt(String.valueOf(transactionID)));
		Date date1 = new Date(a % (long) Math.pow(10, (int) (Math.log10(a) + 1) - 3));
		return date1.toString();
	}
	
}
