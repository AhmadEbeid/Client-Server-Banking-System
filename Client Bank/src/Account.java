
public class Account {

	private String accountID;
	
	private String name;
	
	private String password;
	
	public Account() {
		
		accountID = Encryptor.encrypt(String.valueOf(System.currentTimeMillis()));;
	}
	
	public Account(String n, String p) {
		name = Encryptor.encrypt(n);
		password = Encryptor.encrypt(p);
		accountID = Encryptor.encrypt(String.valueOf(System.currentTimeMillis()));
	}
	
	public String getName() {
		return Encryptor.decrypt(name);
	}
	
	public String getPassword() {
		return Encryptor.decrypt(password);
	}
	
	public long getAccountID() {
		return Long.parseLong(Encryptor.decrypt(accountID));
	}
	

}
