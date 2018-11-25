import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainMenu {

	private JFrame frmAccount;
	socketCommunication socketClass;
	BankAccount bankAccount;
	/**
	 * Launch the application.
	 * @param point 
	 */
	public static void main(socketCommunication socket, BankAccount bank, Point point) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu window = new MainMenu(socket, bank);
					window.frmAccount.setVisible(true);
					window.frmAccount.setLocation(point);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainMenu(socketCommunication socket, BankAccount bank) {
		initialize(socket, bank);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(socketCommunication socket, BankAccount bank) {
		socketClass = socket;
		bankAccount = bank;
		
		frmAccount = new JFrame();
		frmAccount.setResizable(false);
		frmAccount.setTitle("Account");
		ImageIcon im = new ImageIcon(getClass().getResource("/images.png"));
		frmAccount.setIconImage(im.getImage());
		frmAccount.setBounds(100, 100, 300, 275);
		frmAccount.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmAccount.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent we)
		    { 
		    	
		        String ObjButtons[] = {"Yes","No"};
		        int PromptResult = JOptionPane.showOptionDialog(frmAccount,"Are you sure you want to logout?","Banking System",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
		        if(PromptResult==JOptionPane.YES_OPTION)
		        {
		        	if(socketClass != null) {
		        		try {
							socketClass.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		        	}
//		            System.exit(0);
			    	frmAccount.dispose();
		        	MainPage.main(frmAccount.getLocation());
		        }
		    }
		});
		frmAccount.getContentPane().setLayout(null);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(10, 47, 100, 14);
		frmAccount.getContentPane().add(lblName);
		
		JLabel labelAccountNumber = new JLabel("Account Number");
		labelAccountNumber.setBounds(10, 72, 100, 14);
		frmAccount.getContentPane().add(labelAccountNumber);
		
		JLabel lblCurrentBalance = new JLabel("Current Balance");
		lblCurrentBalance.setBounds(10, 97, 100, 14);
		frmAccount.getContentPane().add(lblCurrentBalance);
		
		JTextField name = new JTextField(bank.getAccountOwner().getName());
		name.setEditable(false);
		name.setBounds(120, 47, 154, 14);
		frmAccount.getContentPane().add(name);
		
		JTextField accountNumber = new JTextField(String.valueOf(bank.getBankAccountID()));
		accountNumber.setEditable(false);
		accountNumber.setBounds(120, 72, 154, 14);
		frmAccount.getContentPane().add(accountNumber);
		
		JTextField balance = new JTextField(String.valueOf(bank.getAmount()));
		balance.setEditable(false);
		balance.setBounds(120, 97, 154, 14);
		frmAccount.getContentPane().add(balance);
		
		JLabel lblBankAccount = new JLabel("Bank Account");
		lblBankAccount.setHorizontalAlignment(SwingConstants.CENTER);
		lblBankAccount.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		lblBankAccount.setBounds(10, 11, 264, 25);
		frmAccount.getContentPane().add(lblBankAccount);
		
		JButton btnRefreshBalance = new JButton("Refresh Balance");
		btnRefreshBalance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ImageIcon loading = new ImageIcon("images/loader.gif");
				ImageIcon loading = new ImageIcon(getClass().getResource("/loader.gif"));
				final JDialog d = new JDialog();
		        JPanel p1 = new JPanel(new GridBagLayout());
		        JLabel j = new JLabel("loading... ", loading, JLabel.CENTER);
		        j.setHorizontalAlignment(JLabel.CENTER);
		        j.setVerticalAlignment(JLabel.CENTER);
		        p1.add( j,new GridBagConstraints());
		        d.getContentPane().add(p1);
		        d.setSize(100,50);
		        d.setUndecorated(true);
		        d.setLocationRelativeTo(frmAccount);
		        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		        d.setModal(true);
		        
				new Thread(new Runnable() {
					@Override
					public void run() {
						BankAccount bank;
						try {
							bank = socketClass.refreshAccount();
							if(bank == null) {
								d.dispose();
								JOptionPane.showMessageDialog(frmAccount, "Error Occured, Try again later");
							}else {
								d.dispose();
								frmAccount.dispose();
								MainMenu.main(socketClass, bank, frmAccount.getLocation());
							}
						} catch (IOException e1) {
							d.dispose();
							JOptionPane.showMessageDialog(frmAccount, "Error");
						}
					}
					
				}).start();
				d.setVisible(true);
			}
		});
		btnRefreshBalance.setBounds(10, 129, 132, 23);
		frmAccount.getContentPane().add(btnRefreshBalance);
		
		JButton btnWithdrawMoney = new JButton("Withdraw Money");
		btnWithdrawMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// ImageIcon loading = new ImageIcon("images/loader.gif");
				ImageIcon loading = new ImageIcon(getClass().getResource("/loader.gif"));
				final JDialog d = new JDialog();
		        JPanel p1 = new JPanel(new GridBagLayout());
		        JLabel j = new JLabel("loading... ", loading, JLabel.CENTER);
		        j.setHorizontalAlignment(JLabel.CENTER);
		        j.setVerticalAlignment(JLabel.CENTER);
		        p1.add( j,new GridBagConstraints());
		        d.getContentPane().add(p1);
		        d.setSize(100,50);
		        d.setUndecorated(true);
		        d.setLocationRelativeTo(frmAccount);
		        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		        d.setModal(true);
		        
		        String value = JOptionPane.showInputDialog(frmAccount,"Enter withdraw amount: ");
		        new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							if(value == null) {
								d.dispose();
							}
							else if(!value.isEmpty()) {
								double amount = Double.parseDouble(value);
						    	BankAccount bank = socketClass.withdraw(amount);
						    	if(bank == null) {
						    		d.dispose();
									JOptionPane.showMessageDialog(frmAccount, "Error Occured, Try again later");
								}else if(bank.getAmount() < 0) {
									d.dispose();
									JOptionPane.showMessageDialog(frmAccount, "Error in amount entered");
								}else {
									d.dispose();
									frmAccount.dispose();
									MainMenu.main(socketClass, bank, frmAccount.getLocation());
								}
							}
							d.dispose();
					    } catch (IOException e1) {
					    	d.dispose();
					    	JOptionPane.showMessageDialog(frmAccount, "Error");
						}catch(Exception ex) {
							d.dispose();
					    	JOptionPane.showMessageDialog(frmAccount, "Only Numbers Allowed");
					    }
						
					}
		        	
		        }).start();
		        d.setVisible(true);
			}
		});
		btnWithdrawMoney.setBounds(142, 129, 132, 23);
		frmAccount.getContentPane().add(btnWithdrawMoney);
		
		JButton btnDepositMoney = new JButton("Deposit Money");
		btnDepositMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// ImageIcon loading = new ImageIcon("images/loader.gif");
				ImageIcon loading = new ImageIcon(getClass().getResource("/loader.gif"));
				final JDialog d = new JDialog();
		        JPanel p1 = new JPanel(new GridBagLayout());
		        JLabel j = new JLabel("loading... ", loading, JLabel.CENTER);
		        j.setHorizontalAlignment(JLabel.CENTER);
		        j.setVerticalAlignment(JLabel.CENTER);
		        p1.add( j,new GridBagConstraints());
		        d.getContentPane().add(p1);
		        d.setSize(100,50);
		        d.setUndecorated(true);
		        d.setLocationRelativeTo(frmAccount);
		        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		        d.setModal(true);
		        
		        
		        String value = JOptionPane.showInputDialog(frmAccount,"Enter deposit amount: ");
				
		        new Thread(new Runnable() {

					@Override
					public void run() {
						try {	
							if(value == null) {
								d.dispose();
							}
							else if(!value.isEmpty()) {
								double amount = Double.parseDouble(value);
						    	BankAccount bank = socketClass.deposit(amount);
								if(bank == null) {
									d.dispose();
									JOptionPane.showMessageDialog(frmAccount, "Error Occured, Try again later");
								}else if(bank.getAmount() < 0) {
									d.dispose();
									JOptionPane.showMessageDialog(frmAccount, "Error in amount entered");
								}else {
									d.dispose();
									frmAccount.dispose();
									MainMenu.main(socketClass, bank, frmAccount.getLocation());
								}
							}
							d.dispose();
					    } catch (IOException e1) {
					    	d.dispose();
					    	JOptionPane.showMessageDialog(frmAccount, "Error");
						}catch(Exception ex) {
							d.dispose();
					    	JOptionPane.showMessageDialog(frmAccount, "Only Numbers Allowed");
					    }
					}
		        	
		        }).start();
		        d.setVisible(true);
				
				
				
				
			}
		});
		btnDepositMoney.setBounds(10, 163, 132, 23);
		frmAccount.getContentPane().add(btnDepositMoney);
		
		JButton btnTransferMoney = new JButton("Transfer Money");
		btnTransferMoney.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// ImageIcon loading = new ImageIcon("images/loader.gif");
				ImageIcon loading = new ImageIcon(getClass().getResource("/loader.gif"));
				final JDialog d = new JDialog();
		        JPanel p1 = new JPanel(new GridBagLayout());
		        JLabel j = new JLabel("loading... ", loading, JLabel.CENTER);
		        j.setHorizontalAlignment(JLabel.CENTER);
		        j.setVerticalAlignment(JLabel.CENTER);
		        p1.add( j,new GridBagConstraints());
		        d.getContentPane().add(p1);
		        d.setSize(100,50);
		        d.setUndecorated(true);
		        d.setLocationRelativeTo(frmAccount);
		        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		        d.setModal(true);
		        
		        JTextField amount = new JTextField();
				JTextField accountNum = new JTextField();
				Object[] message = {
				    "Amount:", amount,
				    "Account Number:", accountNum
				};

				int option = JOptionPane.showConfirmDialog(frmAccount, message, "Transfere Money", JOptionPane.OK_CANCEL_OPTION);
				
		        new Thread(new Runnable() {
					@Override
					public void run() {
						if (option == JOptionPane.OK_OPTION) {
						    try {
						    	double am = Double.parseDouble(amount.getText());
						    	long accountN = Long.parseLong(accountNum.getText());
						    	if(accountN == bankAccount.getBankAccountID()) {
						    		d.dispose();
									JOptionPane.showMessageDialog(frmAccount, "Error, can't transfer money to your account");
						    	}else {
						    		BankAccount bank = socketClass.Transfer(accountN, am);
									if(bank == null) {
										d.dispose();
										JOptionPane.showMessageDialog(frmAccount, "Other bank is not online, Try again later");
									}else if(bank.getAmount() == -1) {
										d.dispose();
										JOptionPane.showMessageDialog(frmAccount, "Account not found");
									}else if(bank.getAmount() < 0) {
										d.dispose();
										JOptionPane.showMessageDialog(frmAccount, "Error in amount entered");
									}else {
										d.dispose();
										frmAccount.dispose();
										MainMenu.main(socketClass, bank, frmAccount.getLocation());
									}
						    	}
						    	
						    } catch (IOException e1) {
						    	d.dispose();
						    	JOptionPane.showMessageDialog(frmAccount, "Error");
							}catch(Exception ex) {
								d.dispose();
						    	JOptionPane.showMessageDialog(frmAccount, "Only Numbers Allowed");
						    }
						}else {
							d.dispose();
						}
					}
		        	
		        }).start();
		        d.setVisible(true);
		        
			}
		});
		btnTransferMoney.setBounds(142, 163, 132, 23);
		frmAccount.getContentPane().add(btnTransferMoney);
		
		JButton btnShowTransactions = new JButton("Show Transactions");
		btnShowTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// ImageIcon loading = new ImageIcon("images/loader.gif");
				ImageIcon loading = new ImageIcon(getClass().getResource("/loader.gif"));
				final JDialog d = new JDialog();
		        JPanel p1 = new JPanel(new GridBagLayout());
		        JLabel j = new JLabel("loading... ", loading, JLabel.CENTER);
		        j.setHorizontalAlignment(JLabel.CENTER);
		        j.setVerticalAlignment(JLabel.CENTER);
		        p1.add( j,new GridBagConstraints());
		        d.getContentPane().add(p1);
		        d.setSize(100,50);
		        d.setUndecorated(true);
		        d.setLocationRelativeTo(frmAccount);
		        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		        d.setModal(true);
		        
		        
		        new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							d.dispose();
							TransactionsPage.main(socketClass.getTransactions(), frmAccount.getLocation());
						} catch (IOException e1) {
							d.dispose();
							JOptionPane.showMessageDialog(frmAccount, "Error");
						}
						
					}
		        	
		        }).start();
		        d.setVisible(true);
				
				
			}
		});
		btnShowTransactions.setBounds(10, 197, 264, 23);
		frmAccount.getContentPane().add(btnShowTransactions);
	}

}
