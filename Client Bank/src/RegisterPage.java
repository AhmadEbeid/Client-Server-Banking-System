import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class RegisterPage {

	private JFrame frmRegister;
	private JTextField textFieldName;
	private JPasswordField textFieldPass;
	private JTextField textFieldAmount;
	socketCommunication socketClass;

	/**
	 * Launch the application.
	 * @param point 
	 */
	public static void main(socketCommunication socket, Point point, String ip, int port) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegisterPage window = new RegisterPage(socket, ip, port);
					window.frmRegister.setVisible(true);
					window.frmRegister.setLocation(point);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RegisterPage(socketCommunication socket, String ip, int port) {
		initialize(socket, ip, port);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(socketCommunication socket, String ip, int port) {
		socketClass = socket;
		frmRegister = new JFrame();
		frmRegister.setResizable(false);
		frmRegister.setTitle("Register");
		ImageIcon im = new ImageIcon(getClass().getResource("/images.png"));
		frmRegister.setIconImage(im.getImage());
		frmRegister.setBounds(100, 100, 300, 210);
//		frmRegister.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frmRegister.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		frmRegister.addWindowListener(new WindowAdapter() {
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
//	            System.exit(0);
		    	frmRegister.dispose();
	        	MainPage.main(frmRegister.getLocation());
		    	
		    	
//		        String ObjButtons[] = {"Yes","No"};
//		        int PromptResult = JOptionPane.showOptionDialog(frmRegister,"Are you sure you want to exit?","Banking System",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
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
//		            System.exit(0);
////		        	frmClient.dispose();
//		        }
		    }
		});
		
		frmRegister.getContentPane().setLayout(null);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(10, 50, 79, 14);
		frmRegister.getContentPane().add(lblName);
		
		textFieldName = new JTextField();
		textFieldName.setColumns(10);
		textFieldName.setBounds(99, 47, 175, 20);
		frmRegister.getContentPane().add(textFieldName);
		
		JLabel label_1 = new JLabel("Password");
		label_1.setBounds(10, 75, 79, 14);
		frmRegister.getContentPane().add(label_1);
		
		textFieldPass = new JPasswordField();
		textFieldPass.setColumns(10);
		textFieldPass.setBounds(99, 72, 175, 20);
		frmRegister.getContentPane().add(textFieldPass);
		
		JLabel lblRegister = new JLabel("Register");
		lblRegister.setHorizontalAlignment(SwingConstants.CENTER);
		lblRegister.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		lblRegister.setBounds(10, 11, 264, 28);
		frmRegister.getContentPane().add(lblRegister);
		
		JButton button_1 = new JButton("Register");
		
		button_1.setBounds(10, 133, 264, 23);
		frmRegister.getContentPane().add(button_1);
		
		JLabel lblAmount = new JLabel("Amount");
		lblAmount.setBounds(10, 103, 79, 14);
		frmRegister.getContentPane().add(lblAmount);
		
		textFieldAmount = new JTextField();
		textFieldAmount.setColumns(10);
		textFieldAmount.setBounds(99, 100, 175, 20);
		frmRegister.getContentPane().add(textFieldAmount);
		
		button_1.addActionListener(new ActionListener() {
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
		        d.setLocationRelativeTo(frmRegister);
		        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		        d.setModal(true);
		        
		        final Thread t = new Thread(){
		        	public void run(){
		        		String accountName = textFieldName.getText();
						String password = String.valueOf(textFieldPass.getPassword());
						double amount;
						try {
							amount = Double.parseDouble(textFieldAmount.getText());
							socketClass = new socketCommunication(ip, port);
							BankAccount bank = socketClass.register(accountName, password, amount);
							if(bank == null) {
								JOptionPane.showMessageDialog(frmRegister, "Error Occured, Try again later");
								d.dispose();
							}else {
								d.dispose();
								frmRegister.dispose();
								MainMenu.main(socketClass, bank, frmRegister.getLocation());
							}
						}catch(IOException ex) {
							d.dispose();
							JOptionPane.showMessageDialog(frmRegister, "Connection refused, Bank is not online");
						}catch(Exception ex) {
							d.dispose();
							JOptionPane.showMessageDialog(frmRegister, "Only Numbers Allowed for amount");
							textFieldAmount.setText("");
						}
		            }
		        };
		        
				t.start();
				d.setVisible(true);
				
			}
		});
	}

}
