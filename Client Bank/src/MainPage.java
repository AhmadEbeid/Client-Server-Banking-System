import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.Point;

import javax.swing.SwingConstants;
//import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class MainPage {

	private JFrame frmClient;
	public socketCommunication socketClass = null;
	static int port = 5678;
	static String ip = "127.0.0.1";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		try {
//			  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			} catch(Exception e) {
//			  System.out.println("Error setting native LAF: " + e);
//			}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainPage window = new MainPage();
					window.frmClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void main(Point p) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainPage window = new MainPage();
					window.frmClient.setVisible(true);
					window.frmClient.setLocation(p);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmClient = new JFrame();
		frmClient.setResizable(false);
		frmClient.setTitle("Client");
		ImageIcon im = new ImageIcon(getClass().getResource("/images.png"));
		frmClient.setIconImage(im.getImage());
		frmClient.setBounds(100, 100, 300, 190);
//		frmClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmClient.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmClient.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent we)
		    { 
		        String ObjButtons[] = {"Yes","No"};
		        int PromptResult = JOptionPane.showOptionDialog(frmClient,"Are you sure you want to exit?","Banking System",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
		        if(PromptResult==JOptionPane.YES_OPTION)
		        {
		            System.exit(0);
		        }
		    }
		});
		frmClient.getContentPane().setLayout(null);
		
		JMenuBar menuBar;
		JMenu mnSettings;
		JMenuItem mntmBankLocation;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
//		mntmBankLocation = new JMenuItem("Bank Settings", new ImageIcon("settings.gif"));
		mntmBankLocation = new JMenuItem(new AbstractAction("Bank Settings", new ImageIcon(getClass().getResource("/settings.gif"))) {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				JTextField bankIP = new JTextField();
				bankIP.setText(ip);
				JTextField bankPort = new JTextField();
				bankPort.setText(String.valueOf(port));
				
				Object[] message = {
				    "bankIP", bankIP,
				    "bankPort", bankPort
				};

				int option = JOptionPane.showConfirmDialog(frmClient, message, "Bank Settings", JOptionPane.OK_CANCEL_OPTION);
				
				if (option == JOptionPane.OK_OPTION) {
					try {
						String a = bankPort.getText();
						port = Integer.parseInt(a);
						String[] IPs = bankIP.getText().trim().split("\\.");
						Integer.parseInt(IPs[0]);
						Integer.parseInt(IPs[0]);
						Integer.parseInt(IPs[0]);
						Integer.parseInt(IPs[0]);
						ip = bankIP.getText();
					}catch( Exception ex) {
						port = 5678;
						ip = "127.0.0.1";
						JOptionPane.showMessageDialog(frmClient, "Error in input data");
					}
				}
				
		    }
		});
		mnSettings.add(mntmBankLocation);
		
		frmClient.setJMenuBar(menuBar);
		
		JButton button = new JButton("Login");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmClient.dispose();
				LoginPage.main(socketClass, frmClient.getLocation(), ip, port);
			}
		});
		button.setBounds(10, 57, 264, 23);
		frmClient.getContentPane().add(button);
		
		JLabel label = new JLabel("Welcome");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		label.setBounds(10, 23, 264, 23);
		frmClient.getContentPane().add(label);
		
		JButton button_1 = new JButton("Register");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmClient.dispose();
				RegisterPage.main(socketClass, frmClient.getLocation(), ip, port);
			}
		});
		button_1.setBounds(10, 91, 264, 23);
		frmClient.getContentPane().add(button_1);
	}

}
