package serverBank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

class ClientHandler implements Runnable 
{ 
    final DataInputStream dis; 
    final DataOutputStream dos; 
    final Socket s; 
    DatabaseHandler db;
    Transfer transfer;
    Withdraw withdraw;
    Deposit deposit;
    SignUp signUp;
    SignIn signIn;
    TransferOut transferOut;
    boolean running;
      
  
    // Constructor 
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)  
    { 
        this.s = s; 
        this.dis = dis; 
        this.dos = dos; 
        this.running = true;
    } 
    
    public void terminate() throws IOException {
    	if(this.dis != null)
    		this.dis.close();
    	if(this.dos != null)
    		this.dos.close();
    	if(this.s != null)
    		this.s.close();
    	if(this.db != null)
    		this.db.close();
        running = false;
    }
  
    private void sendOut(String name, String data) throws IOException {
    	Gson gson = new Gson();
    	dos.writeUTF(Encryptor.encrypt(gson.toJson(new SendData(name, data))));
    }
    
    @Override
    public void run()  
    { 
    	String received; 
    	int res;
        while (running)  
        { 
            try { 
  
            	received = Encryptor.decrypt(dis.readUTF());
                Gson gson = new Gson();
                SendData data = gson.fromJson(received, SendData.class);
                
                if(data.getProperty().equals("Exit")) 
                {   
                    this.s.close(); 
                    break; 
                } 
                synchronized (this)
                {
                	switch (data.getProperty()) { 
                    
                    case "currentBalance":
                    	db.updateData();
                    	// dos.writeUTF(gson.toJson(new SendData("currentBalance", gson.toJson(db.bankAccount))));
						sendOut("currentBalance", gson.toJson(db.bankAccount));
                        break; 
                          
                    case "withdraw":
                    	withdraw = gson.fromJson(data.getGsonData(), Withdraw.class);
                    	res = db.withdrawMoney(withdraw.amount);
                    	if(res == 1) {
							sendOut("withdraw", gson.toJson(db.bankAccount));
                    		// dos.writeUTF(gson.toJson(new SendData("withdraw", gson.toJson(db.bankAccount))));
                    	}else if(res == 0) {
							sendOut("invalid Amount", null);
                    		// dos.writeUTF(gson.toJson(new SendData("invalid Amount", null))); 
                    	}else {
							sendOut("invalid", null);
                    		// dos.writeUTF(gson.toJson(new SendData("invalid", null))); 
                    	}
                        break;
                    
                    case "transfer":
                    	transfer = gson.fromJson(data.getGsonData(), Transfer.class);
                    	Socket c1 = null;
                    	DataOutputStream dos1 = null;
                    	DataInputStream dis1 = null;
                    	if(String.valueOf(transfer.otherBankAccountID).length() > 3 && !String.valueOf(transfer.otherBankAccountID).substring(0, 3).equals(String.valueOf(ServerSide.bankNumber))) {
                			
                			try {
                				c1 = new Socket(ServerSide.otherBankIP, ServerSide.OtherBankPort);
                				dos1 = new DataOutputStream(c1.getOutputStream());
                				dis1 = new DataInputStream(c1.getInputStream());
                				
                				transferOut = new TransferOut();
                				transferOut.amount = transfer.amount;
                				transferOut.currentBankAccountID = transfer.otherBankAccountID;
                				transferOut.otherBankAccountID = String.valueOf(db.bankAccount.getBankAccountID());
                				
                				String a = gson.toJson(new SendData("transferOut", gson.toJson(transferOut)));
                				dos1.writeUTF(Encryptor.encrypt(a));
                				
                				received = Encryptor.decrypt(dis1.readUTF());
                		        data = gson.fromJson(received, SendData.class);
                		        
                		        if(data.getProperty().equals("transferOut")) {
                		        	res = db.transferMoneyOut(transfer.amount, transfer.otherBankAccountID);
                                	if(res == 1) {
										sendOut("transfer", gson.toJson(db.bankAccount));
                                		// dos.writeUTF(gson.toJson(new SendData("transfer", gson.toJson(db.bankAccount))));
                                	}else{
										sendOut("invalid Amount", null);
                                		// dos.writeUTF(gson.toJson(new SendData("invalid Amount", null))); 
                                	}
                		        }else if(data.getProperty().equals("invalid Amount")) {
									sendOut("invalid Amount", null);
                		        	// dos.writeUTF(gson.toJson(new SendData("invalid Amount", null))); 
                		        }else {
									sendOut("invalid Account", null);
                            		// dos.writeUTF(gson.toJson(new SendData("invalid Account", null))); 
                            	}
                		        dos1.writeUTF(Encryptor.encrypt(gson.toJson(new SendData("Exit", null))));
                		        dos1.close();
                		        dis1.close();
                		        c1.close();
                			} catch (IOException e) {
								sendOut("invalid", null);
                				// dos.writeUTF(gson.toJson(new SendData("invalid", null))); 
                			}
                		}else {
                			res = db.transferMoney(transfer.amount, transfer.otherBankAccountID);
                        	if(res == 1) {
								sendOut("transfer", gson.toJson(db.bankAccount));
                        		// dos.writeUTF(gson.toJson(new SendData("transfer", gson.toJson(db.bankAccount))));
                        	}else if(res == 0) {
								sendOut("invalid Amount", null);
                        		// dos.writeUTF(gson.toJson(new SendData("invalid Amount", null))); 
                        	}else {
								sendOut("invalid Account", null);
                        		// dos.writeUTF(gson.toJson(new SendData("invalid Account", null))); 
                        	}
                		}
                    	
                        break; 
                        
                    case "transferOut":
                    	transferOut = gson.fromJson(data.getGsonData(), TransferOut.class);
                    	res = (new DatabaseHandler(transferOut.currentBankAccountID)).transferMoneyIn(transferOut.amount, transferOut.otherBankAccountID);
                    	if(res == 1) {
							sendOut("transferOut", null);
                    		// dos.writeUTF(gson.toJson(new SendData("transferOut", null)));
                    	}else if(res == 0) {
							sendOut("invalid Amount", null);
                    		// dos.writeUTF(gson.toJson(new SendData("invalid Amount", null))); 
                    	}else {
							sendOut("invalid Account", null);
                    		// dos.writeUTF(gson.toJson(new SendData("invalid Account", null))); 
                    	}
                    	
                    	break;
                        
                    case "deposit":
                    	deposit = gson.fromJson(data.getGsonData(), Deposit.class);
                    	res = db.depositMoney(deposit.amount);
                    	if(res == 1) {
							sendOut("deposit", gson.toJson(db.bankAccount));
                    		// dos.writeUTF(gson.toJson(new SendData("deposit", gson.toJson(db.bankAccount))));
                    	}else if(res == 0) {
							sendOut("invalid Amount", null);
                    		// dos.writeUTF(gson.toJson(new SendData("invalid Amount", null))); 
                    	}else {
							sendOut("invalid", null);
                    		// dos.writeUTF(gson.toJson(new SendData("invalid", null))); 
                    	}
                        break; 
                        
                    case "view_Transaction":
                    	List<Transaction> trans = db.getTransactions();
                    	if(trans != null) {
							sendOut("view_Transaction", gson.toJson(trans));
                    		// dos.writeUTF(gson.toJson(new SendData("view_Transaction", gson.toJson(trans))));
                    	}else {
							sendOut("invalid", null);
                    		// dos.writeUTF(gson.toJson(new SendData("invalid", null))); 
                    	}
                        break; 
                        
                    case "SignIn":
                    	signIn = gson.fromJson(data.getGsonData(), SignIn.class);
                    	res = (new DatabaseHandler()).checkLogin(signIn);
                    	if(res == 1) {
                    		db = new DatabaseHandler(String.valueOf(signIn.bankAccountID));
							sendOut("SignIn", gson.toJson(db.bankAccount));
                    		// dos.writeUTF(gson.toJson(new SendData("SignIn", gson.toJson(db.bankAccount))));
                    	}else {
							sendOut("invalid", null);
                    		// dos.writeUTF(gson.toJson(new SendData("invalid", null))); 
                    	}
                        break; 
                    
                    case "SignUp":
                    	signUp = gson.fromJson(data.getGsonData(), SignUp.class);
                    	db = new DatabaseHandler();
                    	db.createNewAccount(signUp.name, signUp.password);
                    	db.createNewBankAccount(signUp.amount);
						sendOut("SignUp", gson.toJson(db.bankAccount));
                    	// dos.writeUTF(gson.toJson(new SendData("SignUp", gson.toJson(db.bankAccount))));
                        break;
                        
                    default:
						sendOut("invalid", null);
                        // dos.writeUTF(gson.toJson(new SendData("invalid", null))); 
                        break; 
                	}
                }
                 
            } catch (Exception e) { 
                e.printStackTrace(); 
                try {
					terminate();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } 
        } 
          
        try
        { 
            // closing resources 
            this.dis.close(); 
            this.dos.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
} 

class RunningThread implements Runnable {

	Socket s = null;
	ArrayList<Thread> thread;
	ArrayList<ClientHandler> clientHandler;
	boolean running = true;
	ServerSocket serverSocket;
	
	public RunningThread(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		thread = new ArrayList<>();
		clientHandler = new ArrayList<>();
	}
	
	public void terminate() throws IOException, InterruptedException {
		running = false;
		if(s != null) {
			s.close();
		}
		for(int i = 0; i < clientHandler.size(); i++) {
			clientHandler.get(i).terminate();
			thread.get(i).join();
		}
	}
	
	@Override
	public void run() {
		while (running)  
        { 
            try 
            { 
                // socket object to receive incoming client requests 
                s = serverSocket.accept();
                DataInputStream dis = new DataInputStream(s.getInputStream()); 
                DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
                  
                System.out.println("Assigning new thread for this client"); 
  
                // create a new thread object 
                int threadNum = thread.size();
				
                clientHandler.add(new ClientHandler(s, dis, dos));
				thread.add(new Thread(clientHandler.get(threadNum)));
				thread.get(threadNum).start();
				
                  
            } 
            catch (Exception e){ 
                try {
					if(s != null) {
						s.close();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
            } 
        }
	}
	
}