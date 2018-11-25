import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import com.google.gson.Gson;

class SignIn {
	long bankAccountID;
	String password;
}

class SignUp{
	String name;
	String password;
	double amount;
}

class Transfer{
	String otherBankAccountID;
	double amount;
}

class TransferOut{
	String otherBankAccountID;
	String currentBankAccountID;
	double amount;
}

class Deposit{
	double amount;
}


class Withdraw{
	double amount;
}

class socketCommunication{
	Socket c;
	DataOutputStream dos;
	DataInputStream dis;
	
	socketCommunication(String ip, int port) throws UnknownHostException, IOException{
		c = new Socket(ip, port);
        dos = new DataOutputStream(c.getOutputStream());
        dis = new DataInputStream(c.getInputStream());
	}
	
	BankAccount login(long accountNumber, String password) throws IOException {
		
		SignIn signIn = new SignIn();
		signIn.bankAccountID = accountNumber;
		signIn.password = password;
		
		Gson gson = new Gson();
		
		dos.writeUTF(Encryptor.encrypt(gson.toJson(new SendData("SignIn", gson.toJson(signIn)))));
		
		String received = Encryptor.decrypt(dis.readUTF());
        SendData data = gson.fromJson(received, SendData.class);
        
        if(data.getProperty().equals("SignIn")) {
        	return gson.fromJson(data.getGsonData(), BankAccount.class);
        }else {
        	return null;
        }
	}
	
	void close() throws IOException {
		String a = (new Gson()).toJson(new SendData("Exit", null));
		dos.writeUTF(Encryptor.encrypt(a));
	}
	
	BankAccount register(String accountName, String password, double amount) throws IOException {
			
			SignUp signUp = new SignUp();
			signUp.amount = amount;
			signUp.name = accountName;
			signUp.password = password;
					
			Gson gson = new Gson();
			String a = gson.toJson(new SendData("SignUp", gson.toJson(signUp)));
			dos.writeUTF(Encryptor.encrypt(a));
			
			String received = Encryptor.decrypt(dis.readUTF());
	        SendData data = gson.fromJson(received, SendData.class);
	        
	        if(data.getProperty().equals("SignUp")) {
	        	return gson.fromJson(data.getGsonData(), BankAccount.class);
	        }else {
	        	return null;
	        }
		}
	
	BankAccount withdraw(double amount) throws IOException {
		Withdraw withdraw = new Withdraw();
		withdraw.amount = amount;
		
		Gson gson = new Gson();
		String a = gson.toJson(new SendData("withdraw", gson.toJson(withdraw)));
		dos.writeUTF(Encryptor.encrypt(a));
		
		String received = Encryptor.decrypt(dis.readUTF());
        SendData data = gson.fromJson(received, SendData.class);
        
        if(data.getProperty().equals("withdraw")) {
        	return gson.fromJson(data.getGsonData(), BankAccount.class);
        }else if(data.getProperty().equals("invalid Amount")) {
        	return new BankAccount(false);
        }else {
        	return null;
        }
	}
	
	BankAccount deposit(double amount) throws IOException {
		Deposit deposit = new Deposit();
		deposit.amount = amount;
		
		Gson gson = new Gson();
		String a = gson.toJson(new SendData("deposit", gson.toJson(deposit)));
		dos.writeUTF(Encryptor.encrypt(a));
		
		String received = Encryptor.decrypt(dis.readUTF());
        SendData data = gson.fromJson(received, SendData.class);
        
        if(data.getProperty().equals("deposit")) {
        	return gson.fromJson(data.getGsonData(), BankAccount.class);
        }else if(data.getProperty().equals("invalid Amount")) {
        	return new BankAccount(false);
        }else {
        	return null;
        }
	}
	
	BankAccount Transfer(long bankAccount, double amount) throws IOException {
		Transfer transfer = new Transfer();
		transfer.amount = amount;
		transfer.otherBankAccountID = String.valueOf(bankAccount);
		
		Gson gson = new Gson();
		String a = gson.toJson(new SendData("transfer", gson.toJson(transfer)));
		dos.writeUTF(Encryptor.encrypt(a));
		
		String received = Encryptor.decrypt(dis.readUTF());
        SendData data = gson.fromJson(received, SendData.class);
        
        if(data.getProperty().equals("transfer")) {
        	return gson.fromJson(data.getGsonData(), BankAccount.class);
        }else if(data.getProperty().equals("invalid Account")) {
        	return new BankAccount(true);
        }else if(data.getProperty().equals("invalid Amount")) {
        	return new BankAccount(false);
        }else {
        	return null;
        }
	}
	
	BankAccount refreshAccount() throws IOException {
		
		Gson gson = new Gson();
		String a = gson.toJson(new SendData("currentBalance", null));
		dos.writeUTF(Encryptor.encrypt(a));
		
		String received = Encryptor.decrypt(dis.readUTF());
        SendData data = gson.fromJson(received, SendData.class);
        
        if(data.getProperty().equals("currentBalance")) {
        	return gson.fromJson(data.getGsonData(), BankAccount.class);
        }else {
        	return null;
        }
	}
	
	List<Transaction> getTransactions() throws IOException{
		Gson gson = new Gson();
		String a = gson.toJson(new SendData("view_Transaction", null));
		dos.writeUTF(Encryptor.encrypt(a));
		
		String received = Encryptor.decrypt(dis.readUTF());
        SendData data = gson.fromJson(received, SendData.class);
		
        if(data.getProperty().equals("view_Transaction")) {
        	Type listType = new TypeToken<List<Transaction>>(){}.getType();
        	return gson.fromJson(data.getGsonData(), listType);
        }else {
        	return null;
        }
	}
	
	
}

