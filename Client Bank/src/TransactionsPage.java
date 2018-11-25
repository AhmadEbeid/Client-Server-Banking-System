import java.awt.EventQueue;
import java.awt.Point;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TransactionsPage {

	private JFrame frmTransactions;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(List<Transaction> transactions, Point point) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TransactionsPage window = new TransactionsPage(transactions);
					window.frmTransactions.setVisible(true);
					window.frmTransactions.setLocation(point);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TransactionsPage(List<Transaction> transactions) {
		initialize(transactions);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(List<Transaction> transactions) {
		frmTransactions = new JFrame();
		frmTransactions.setResizable(false);
		frmTransactions.setTitle("Transactions");
		frmTransactions.setBounds(100, 100, 300, 250);
		ImageIcon im = new ImageIcon(getClass().getResource("/images.png"));
		frmTransactions.setIconImage(im.getImage());
		frmTransactions.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmTransactions.getContentPane().setLayout(null);
		
		String col[] = {"Date", "Operation","Amount", "Balance After"};
		DefaultTableModel tableModel = new DefaultTableModel(col, 0);
		table = new JTable(tableModel);
		table.setBounds(10, 11, 264, 239);
		
		
		JScrollPane a = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(1).setPreferredWidth(155);
		table.getColumnModel().getColumn(3).setPreferredWidth(90);
		a.setLocation(10, 11);
		a.setSize(264, 189);
		
		frmTransactions.getContentPane().add(a);
		
		for(int i = 0; i < transactions.size(); i++) {
			String trans = "";
			if(transactions.get(i).getOtherPartyBankAccount() != null) {
				trans = transactions.get(i).getTransactionName() + " " + String.valueOf(transactions.get(i).getOtherPartyBankAccount().getBankAccountID());
			}else {
				trans = transactions.get(i).getTransactionName();
			}
			
			Object[] objs = {transactions.get(i).getDate(), trans, transactions.get(i).getTransactionAmount(), transactions.get(i).getPrevAmount()};
			tableModel.addRow(objs);
		}
		
		
        // The 0 argument is number rows.

	}
}
