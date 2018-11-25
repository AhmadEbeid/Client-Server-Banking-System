package serverBank;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.awt.event.ActionEvent;

public class ServerSide {

	private JFrame frmBankServer;
	public static int port = 5678;
	public static int OtherBankPort = 5680;
	public static String otherBankIP = "127.0.0.1";
	public static int bankNumber = 250;
	public static int otherBankNumber = 300;
	private boolean status = false;
	private Thread thread;
	private RunningThread runningThread;
	private ServerSocket serverSocket;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerSide window = new ServerSide();
					window.frmBankServer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerSide() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBankServer = new JFrame();
		frmBankServer.setTitle("Bank Server");
		frmBankServer.setResizable(false);
		frmBankServer.setBounds(100, 100, 226, 170);
		frmBankServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBankServer.getContentPane().setLayout(null);
		
		ImageIcon im = new ImageIcon(getClass().getResource("/images.png"));
		frmBankServer.setIconImage(im.getImage());
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(10, 44, 95, 14);
		frmBankServer.getContentPane().add(lblStatus);
		
		JLabel lblRunning = new JLabel("Offline");
		lblRunning.setBounds(115, 44, 85, 14);
		frmBankServer.getContentPane().add(lblRunning);
		
		JLabel label_1 = new JLabel("250");
		label_1.setBounds(115, 11, 85, 14);
		frmBankServer.getContentPane().add(label_1);
		
		JMenuBar menuBar;
		JMenu mnSettings;
		JMenuItem mntmBankLocation;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
//		mntmBankLocation = new JMenuItem("Bank Settings", new ImageIcon("settings.gif"));
//		Image myImage = null;
//		try {
//			myImage = ImageIO.read(getClass().getResourceAsStream("../settings.gif"));
//			
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		mntmBankLocation = new JMenuItem(new AbstractAction("Bank Settings", new ImageIcon(getClass().getResource("/settings.gif"))) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				JTextField bankIP = new JTextField();
				bankIP.setText(otherBankIP);
				JTextField bank2Num = new JTextField();
				bank2Num.setText(String.valueOf(otherBankNumber));
				JTextField bankNum = new JTextField();
				bankNum.setText(String.valueOf(bankNumber));
				JTextField bankPort = new JTextField();
				bankPort.setText(String.valueOf(port));
				JTextField bank2Port = new JTextField();
				bank2Port.setText(String.valueOf(OtherBankPort));
				
				Object[] message = {
					"Current Bank Number", bankNum,
				    "Current Bank Port", bankPort,
				    "Other Bank Number", bank2Num,
				    "Other Bank Port", bank2Port,
				    "Other Bank IP", bankIP
				};

				int option = JOptionPane.showConfirmDialog(frmBankServer, message, "Bank Settings", JOptionPane.OK_CANCEL_OPTION);
				
				if (option == JOptionPane.OK_OPTION) {
					try {
						port = Integer.parseInt(bankPort.getText());
						OtherBankPort = Integer.parseInt(bank2Port.getText());
						otherBankNumber = Integer.parseInt(bank2Num.getText());
						bankNumber = Integer.parseInt(bankNum.getText());
						String[] IPs = bankIP.getText().trim().split("\\.");
						Integer.parseInt(IPs[0]);
						Integer.parseInt(IPs[1]);
						Integer.parseInt(IPs[2]);
						Integer.parseInt(IPs[3]);
						otherBankIP = bankIP.getText();
						label_1.setText(bankNum.getText());
					}catch( Exception ex) {
						port = 5678;
						OtherBankPort = 5680;
						otherBankIP = "127.0.0.1";
						otherBankNumber = 300;
						bankNumber = 250;
						label_1.setText(String.valueOf(bankNumber));
						JOptionPane.showMessageDialog(frmBankServer, "Error in input data");
					}
				}
				
		    }
		});
		mnSettings.add(mntmBankLocation);
		
		mntmBankLocation = new JMenuItem(new AbstractAction("Accounts Registered", new ImageIcon(getClass().getResource("/settings.gif"))) {
			private static final long serialVersionUID = 1L;
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
		        d.setLocationRelativeTo(frmBankServer);
		        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		        d.setModal(true);
				
		        final Thread t = new Thread(){
		        	public void run(){
		        		List<BankAccount> listAccounts = DatabaseHandler.getBankAccounts();
		        		d.dispose();
		        		registeredAccounts.main(listAccounts, frmBankServer.getLocation());
		            }
		        };
		        
				t.start();
				d.setVisible(true);
		    }
		});
		mnSettings.add(mntmBankLocation);
		
		frmBankServer.setJMenuBar(menuBar);
		
		JButton btnServer = new JButton("Start Server");
		btnServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
								
				if(status) {
					try {
						status = false;
						lblRunning.setText("Offline");
						btnServer.setText("Start Server");
						runningThread.terminate();
						serverSocket.close();
						thread.join();
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					try {
						serverSocket = new ServerSocket(port);
						runningThread = new RunningThread(serverSocket);
						thread = new Thread(runningThread);
						thread.start();
						status = true;
						lblRunning.setText("Online");
						btnServer.setText("Stop Server");
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				
			}
		});
		btnServer.setBounds(10, 69, 190, 23);
		frmBankServer.getContentPane().add(btnServer);
		
		JLabel lblBankName = new JLabel("Bank No.");
		lblBankName.setBounds(10, 11, 95, 14);
		frmBankServer.getContentPane().add(lblBankName);
		
		
	}
}
