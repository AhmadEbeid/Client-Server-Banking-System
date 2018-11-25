package serverBank;

import java.awt.EventQueue;
import java.awt.Point;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class registeredAccounts {

	private JFrame frmAccounts;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(List<BankAccount> bankAccounts, Point point) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					registeredAccounts window = new registeredAccounts(bankAccounts);
					window.frmAccounts.setVisible(true);
					window.frmAccounts.setLocation(point);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public registeredAccounts(List<BankAccount> bankAccounts) {
		initialize(bankAccounts);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(List<BankAccount> bankAccounts) {
		frmAccounts = new JFrame();
		ImageIcon im = new ImageIcon(getClass().getResource("/images.png"));
		frmAccounts.setIconImage(im.getImage());
		frmAccounts.setTitle("Accounts");
		frmAccounts.setBounds(100, 100, 250, 250);
		frmAccounts.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAccounts.getContentPane().setLayout(null);
		
		String col[] = {"User", "Account Number"};
		DefaultTableModel tableModel = new DefaultTableModel(col, 0);
		table = new JTable(tableModel);
		table.setBounds(10, 11, 264, 239);
		
		
		
		JScrollPane a = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(120);
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		a.setLocation(10, 11);
		a.setSize(214, 189);
		
		frmAccounts.getContentPane().add(a);
		
		for(int i = 0; i < bankAccounts.size(); i++) {
			Object[] objs = {bankAccounts.get(i).getAccountOwner().getName(), bankAccounts.get(i).getBankAccountID()};
			tableModel.addRow(objs);
		}
	}

}
