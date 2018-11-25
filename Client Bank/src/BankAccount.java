public class BankAccount {
	
	private String bankAccountID;
	
	private Account accountOwner;
	
	private String amount;
	
	BankAccount(){}
	
	BankAccount(boolean a){
		if(a) {
			amount = Encryptor.encrypt("-1");
		}else {
			amount = Encryptor.encrypt("-2");
		}
	}
	
	BankAccount(Account a, double am){
		accountOwner = a;
		amount = Encryptor.encrypt(String.valueOf(am));
		bankAccountID = Encryptor.encrypt(String.valueOf(System.currentTimeMillis()));
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
	
}
