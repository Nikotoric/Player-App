package view;

/**
 * A class that represent right side of graphic design of application
 * Used to view the data entered and to search the database and delete players from the database
 * 
 * @author Niko
 */

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


import controller.Controller;
import model.Database;
import model.Player;

public class PresentationPanel extends JPanel {

	private JPanel mainPanel, upperPanel;
	private JButton searchBtn, deleteBtn;
	private JTextField searchField;
	private JTable table;
	private List<Player> players;
	private JScrollPane scrollTable;
	private AbstractTableModel tableModel;
	private SimpleAttributeSet attributes;
	private Controller controller;
	private InputPanel inputPanel;

	public PresentationPanel() {

		setLayout(new BorderLayout());
		
		initAll();
		layoutComps();
		activateComps();

	}

	/**
	 *  component initialization
	 */
	private void initAll() {

		mainPanel = new JPanel();
		upperPanel = new JPanel();

		searchField = new JTextField(10);
		searchBtn = new JButton("Search");
		deleteBtn = new JButton("Delete player");

		attributes = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attributes, "Consolas");
		StyleConstants.setFontSize(attributes, 14);

		controller = new Controller();
		inputPanel = new InputPanel();

	}

	/**
	 *  method of making design
	 */
	private void layoutComps() {

		mainPanel.setLayout(new GridLayout(2, 1));

		upperPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(5, 5, 5, 5);
		upperPanel.add(new JLabel("Search player: "), gbc);

		gbc.gridx = 1;
		upperPanel.add(searchField, gbc);

		gbc.gridx = 2;
		upperPanel.add(searchBtn, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		upperPanel.add(deleteBtn, gbc);

		add(upperPanel, BorderLayout.NORTH);

		this.table = setTable();
		scrollTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollTable.setSize(getPreferredSize());

		add(scrollTable, BorderLayout.CENTER);

	}

	/**
	 *  method that activate components
	 *  acivating search button and delete button
	 */
	private void activateComps() {

		// search button - aktivacija
		searchBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					searching();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// delete button - aktivacija
		deleteBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				int row = table.getSelectedRow();
				if (row < 0) {
					JOptionPane.showMessageDialog(null, "Select player for delete");
				} else {
					int playerID = (int) table.getValueAt(row, 0);
					Object[] yesNo = { "Yes", "No" };
					int selectedOpt = JOptionPane.showOptionDialog(null, "Do you really want do delete this player?",
							"Confirm to delete", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
							yesNo, yesNo[1]);

					if (selectedOpt == 0) {
						try {
							// controller.connect2DB();
							System.out.println("--> deleting player......");
							deleting(playerID);
							System.out.println("---> player was deleted!");

							controller.reloadTable(PresentationPanel.this);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}

			}
		});

	}

	/**
	 *  method that create table
	 * @return JTable design
	 */
	private JTable setTable() {

		JTable table = new JTable();

		tableModel = new AbstractTableModel() {

			String[] colNames = { "ID", "Name", "Surname", "Age", "League", "Club", "Contract length", "Salary" };

			@Override
			public String getColumnName(int column) {
				return colNames[column];
			}

			@Override
			public Object getValueAt(int row, int column) {
				Player player = players.get(row);
				switch (column) {
				case 0:
					return player.getId();
				case 1:
					return player.getName();
				case 2:
					return player.getSurname();
				case 3:
					return player.getAge();
				case 4:
					return player.getLeague();
				case 5:
					return player.getClub();
				case 6:
					return player.getContract();
				case 7:
					return player.getSalary();
				default:
					throw new IllegalArgumentException("There is no such value for the input data!!!");
				}

			}

			@Override
			public int getRowCount() {
				return players.size();
			}

			@Override
			public int getColumnCount() {
				Player player = new Player();
				return player.getClass().getDeclaredFields().length - 1;
			}
		};

		table.setModel(tableModel);
		return table;
	}

	public void setDBData(Database db) {
		players = db.getAll4DB();
	}

	public void showDataOnTable() {
		tableModel.fireTableDataChanged();
	}

	/**
	 *  a method that does a search for players in a database
	 * @throws SQLException
	 */
	public void searching() throws SQLException {

		table.setModel(new DefaultTableModel());

		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addColumn("id");
		model.addColumn("name");
		model.addColumn("surrname");
		model.addColumn("age");
		model.addColumn("league");
		model.addColumn("club");
		model.addColumn("contractLength");
		model.addColumn("salary");

		Connection connect = null;
		Statement statement = null;

		try {
			// load driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			// obtain connection
			String url = "jdbc:mysql://db4free.net:3306/noop2020n"; // your database
			String user = "noop_niko"; // your user name
			String password = "niksa1212"; // your password
			connect = DriverManager.getConnection(url, user, password);
			System.out.println("Connected to -> " + connect.toString());

			statement = connect.createStatement();

			String sql = "SELECT * FROM PlayerTable WHERE " + "name LIKE '%" + searchField.getText() + "%' "
					+ "OR surrname LIKE '%" + searchField.getText() + "%' " + "OR age LIKE '%" + searchField.getText()
					+ "%' " + "OR league LIKE '%" + searchField.getText() + "%' " + "OR club LIKE '%"
					+ searchField.getText() + "%' " + "ORDER BY id ASC";

			ResultSet result = statement.executeQuery(sql);
			int row = 0;
			while (result != null && result.next()) {

				model.addRow(new Object[0]);
				model.setValueAt(result.getString("id"), row, 0);
				model.setValueAt(result.getString("name"), row, 1);
				model.setValueAt(result.getString("surrname"), row, 2);
				model.setValueAt(result.getString("age"), row, 3);
				model.setValueAt(result.getString("league"), row, 4);
				model.setValueAt(result.getString("club"), row, 5);
				model.setValueAt(result.getString("contractLength"), row, 6);
				model.setValueAt(result.getString("salary"), row, 7);
				row++;
			}
			result.close();

		} catch (ClassNotFoundException e) {
			System.out.println("Could not load driver!!!");
		}

		try {
			if (statement != null) {
				statement.close();
				connect.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 *  a method that delete player from database
	 * @param playerID
	 * @throws SQLException
	 */
	public void deleting(int playerID) throws SQLException {

		Connection connect = null;
		Statement statement = null;

		try {
			// load driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			// obtain connection
			String url = "jdbc:mysql://db4free.net:3306/noop2020n"; // your database
			String user = "noop_niko"; // your user name
			String password = "niksa1212"; // your password
			connect = DriverManager.getConnection(url, user, password);
			System.out.println("Connected to -> " + connect.toString());

			statement = connect.createStatement();

			String sql = "DELETE FROM PlayerTable WHERE id = '" + playerID + "' ";

			statement.executeUpdate(sql);
			JOptionPane.showMessageDialog(null, "Deleted player info!\nWait, refreshing table...");

			controller.save2DB();

		} catch (SQLException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			if (statement != null) {
				statement.close();
				connect.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}