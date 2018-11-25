import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class LoginPage {

	private JFrame frmClient;
	private JTextField textField_No;
	private JPasswordField textField_PW;
	socketCommunication socketClass;

	/**
	 * Launch the application.
	 * @param point 
	 */
	public static void main(socketCommunication socket, Point point, String ip, int port) {
//		socketClass = socket;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPage window = new LoginPage(socket, ip, port);
					window.frmClient.setVisible(true);
					window.frmClient.setLocation(point);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginPage(socketCommunication socket, String ip, int port) {
		initialize(socket, ip, port);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(socketCommunication socket, String ip, int port) {
		socketClass = socket;
		frmClient = new JFrame();
		frmClient.setResizable(false);
		frmClient.setTitle("Login");
		ImageIcon im = new ImageIcon(getClass().getResource("/images.png"));
		frmClient.setIconImage(im.getImage());
		frmClient.setBounds(100, 100, 300, 180);
//		frmClient.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frmClient.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		frmClient.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent we)
		    { 
		    	if(socketClass != null) {
	        		try {
						socketClass.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        	frmClient.dispose();
	        	MainPage.main(frmClient.getLocation());
	        	
	        	
//		        String ObjButtons[] = {"Yes","No"};
//		        int PromptResult = JOptionPane.showOptionDialog(frmClient,"Are you sure you want to exit?","Banking System",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
//		        if(PromptResult==JOptionPane.YES_OPTION)
//		        {
//		        	if(socketClass != null) {
//		        		try {
//							socketClass.close();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//		        	}
////		            System.exit(0);
//		        	frmClient.dispose();
//		        	MainPage.main(null);
//		        }
		    }
		});
		
		frmClient.getContentPane().setLayout(null);
		
		JLabel lblWelcomeToOur = new JLabel("Login");
		lblWelcomeToOur.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeToOur.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		lblWelcomeToOur.setBounds(10, 11, 264, 28);
		frmClient.getContentPane().add(lblWelcomeToOur);
		
		JButton btnLogin = new JButton("Login");
		
		btnLogin.setBounds(10, 100, 264, 23);
		frmClient.getContentPane().add(btnLogin);
		
		JLabel lblAccountNumber = new JLabel("Account Number");
		lblAccountNumber.setBounds(10, 50, 102, 14);
		frmClient.getContentPane().add(lblAccountNumber);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 75, 102, 14);
		frmClient.getContentPane().add(lblPassword);
		
		textField_No = new JTextField();
		textField_No.setBounds(122, 47, 152, 20);
		frmClient.getContentPane().add(textField_No);
		textField_No.setColumns(10);
		
		textField_PW = new JPasswordField();
		textField_PW.setColumns(10);
		textField_PW.setBounds(122, 72, 152, 20);
		frmClient.getContentPane().add(textField_PW);
		
		btnLogin.addActionListener(new ActionListener() {
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
		        d.setLocationRelativeTo(frmClient);
		        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		        d.setModal(true);
		        
		        final Thread t = new Thread(){
		            public void run(){
		            	String accountNumber = textField_No.getText();
						String password = String.valueOf(textField_PW.getPassword());
						long num = -1;
						try {
							num = Long.parseLong(accountNumber);
							try {
								socketClass = new socketCommunication(ip, port);
								BankAccount bank = socketClass.login(num, password);
								if(bank == null) {
									d.dispose();
									JOptionPane.showMessageDialog(frmClient, "Wrong Account/Password");
								}
								else {
									d.dispose();
									frmClient.dispose();
									MainMenu.main(socketClass, bank, frmClient.getLocation());
								}
									
							} catch (IOException e) {
								// TODO Auto-generated catch block
								d.dispose();
								JOptionPane.showMessageDialog(frmClient, "Connection refused, bank is not online");
							}
						}catch(Exception e) {
							d.dispose();
							JOptionPane.showMessageDialog(frmClient, "Only Numbers Allowed for account number");
							textField_No.setText("");
							return;
						}
		            }
		        };
		        t.start();
		        d.setVisible(true);
				
			}
		});
	}
}
