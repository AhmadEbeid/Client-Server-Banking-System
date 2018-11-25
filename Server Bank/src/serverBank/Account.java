package serverBank;

import com.j256.ormlite.field.DatabaseField;

public class Account {

	@DatabaseField(id = true)
	private String accountID;
	
	@DatabaseField()
	private String name;
	
	@DatabaseField(canBeNull = false)
	private String password;
	
	public Account() {
		accountID = Encryptor.encrypt(String.valueOf(ServerSide.bankNumber) + String.valueOf(System.currentTimeMillis()));
	}
	
	public Account(String n, String p) {
		name = Encryptor.encrypt(n);
		password = Encryptor.encrypt(p);
		accountID = Encryptor.encrypt(String.valueOf(ServerSide.bankNumber) + String.valueOf(System.currentTimeMillis()));
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
