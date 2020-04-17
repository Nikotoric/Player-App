package view;

/**
 * A class used to open a new JFrame where the user can see each player separately
 * 
 * @author Niko
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import model.League;
import model.Player;
import model.Salary;

public class ViewPanel extends JFrame {

	private JPanel mainPanel, leftPanel, rightPanel;
	private JButton previousBtn, firstBtn, lastBtn, nextBtn;
	private SimpleAttributeSet attributes;
	private JTextPane txtPane;
	private JScrollPane scPanel;
	private StyledDocument doc;
	private int pos = 0;
	private Connection conn;
	private List<Player> playerUser;

	public ViewPanel() {

		setTitle("Player info");
		setLayout(new BorderLayout());
		setSize(800, 350);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);

		playerUser = new LinkedList<Player>();
		conn = null;
		initAll();
		layoutComps();

		activateComps();

	}

	/**
	 *  component initialization
	 */
	private void initAll() {

		mainPanel = new JPanel();
		leftPanel = new JPanel();
		rightPanel = new JPanel();

		previousBtn = new JButton("Previous player from DB");
		firstBtn = new JButton("First player from DB");
		lastBtn = new JButton("Last player from DB");
		nextBtn = new JButton("Next player from DB");

		txtPane = new JTextPane();
		txtPane.setEditable(false);
		doc = txtPane.getStyledDocument();
		txtPane.setBackground(Color.WHITE);
		Color fontclr = new Color(50, 15, 130);
		txtPane.setForeground(fontclr);

		attributes = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attributes, "Consolas");
		StyleConstants.setFontSize(attributes, 14);
		attributes.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.TRUE);

	}

	/**
	 *  method of making design
	 */
	private void layoutComps() {

		mainPanel.setLayout(new GridLayout(1, 3));

		leftPanel.setLayout(new GridBagLayout());
		rightPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.insets = new Insets(5, 5, 5, 5);
		leftPanel.add(previousBtn, gbc);

		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		leftPanel.add(nextBtn, gbc);

		mainPanel.add(leftPanel, BorderLayout.WEST);

		Dimension tdim = txtPane.getPreferredSize();
		txtPane.setPreferredSize(tdim);

		scPanel = new JScrollPane(txtPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainPanel.add(scPanel, BorderLayout.CENTER);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.insets = new Insets(5, 5, 5, 5);
		rightPanel.add(firstBtn, gbc);

		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		rightPanel.add(lastBtn, gbc);

		mainPanel.add(rightPanel, BorderLayout.EAST);

		add(mainPanel);

	}
	
	/**
	 *  method that activate components
	 */
	private void activateComps() {

		try {
			connect2DB();
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(ViewPanel.this, "Unable to connect to a DB server!!!", "Connection DB error",
					JOptionPane.ERROR_MESSAGE);
		}
		try {
			loadData4DB();
			// da uvijek prikaze prvog pri pokretanju showView-a
			showFirst(playerUser);

		} catch (SQLException e2) {
			JOptionPane.showMessageDialog(ViewPanel.this, "Unable to load data from a DB server!!!", "Load DB error",
					JOptionPane.ERROR_MESSAGE);
		}

		// first Button -> uvijek pokazuje prvog iz baze podatake / always show first from database
		firstBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resetPane();
				showFirst(playerUser);
			}
		});

		// next button -> pokazuje sljedeceg player-a iz baze podataka / showing next player from database
		nextBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resetPane();
				showNext(playerUser);
			}
		});

		// last button -> uvijek pokazuje zadnjeg iz baze podataka / always showing the last one from database
		lastBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resetPane();
				showLast(playerUser);
			}
		});

		// previous button -> prijasnjeg user-a iz baze podataka / showing previous player from database
		previousBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resetPane();
				showPrevious(playerUser);
			}
		});

	}

	/**
	 * method for showing results in viewPanel
	 * @param player
	 * @see Player
	 */
	public void showOnViewPanel(Player player) {
		try {
			doc.insertString(doc.getLength(), player.toString() + "\n\n", attributes);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * method for showing results from database in ViewPanel
	 * @param all4db
	 * @see Player
	 */
	public void showImportedDataInViewPanel(List<Player> all4db) {
		for (Player player : all4db) {
			showOnViewPanel(player);
		}
	}

	/**
	 *  method to clear viewPanel
	 */
	public void resetPane() {
		txtPane.selectAll();
		txtPane.setText("");
	}

	/**
	 *  connection with databse
	 * @throws SQLException
	 */
	public void connect2DB() throws SQLException {
		System.out.println("Connecting to database..");

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String url = "jdbc:mysql://db4free.net:3306/noop2020n"; // your database
			String user = "noop_niko"; // your user name
			String password = "niksa1212"; // your password
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Connected to -> " + conn.toString());
		} catch (ClassNotFoundException e) {
			System.out.println("Could not load driver!!!");
		}

	}

	/**
	 *  method for loading data from database
	 * @throws SQLException
	 */
	public void loadData4DB() throws SQLException {

		if (conn != null) {
			System.out.println("Loading from DB..");
			String selectSQL = "SELECT id, name, surrname, age, league, club, contractLength, salary FROM PlayerTable ORDER BY id";
			PreparedStatement slctStm = conn.prepareStatement(selectSQL);

			ResultSet slcResult = slctStm.executeQuery();
			playerUser.clear();
			while (slcResult.next()) {

				int id = slcResult.getInt(1);
				String name = slcResult.getString(2);
				String surname = slcResult.getString(3);
				int age = slcResult.getInt(4);
				League league = League.valueOf(slcResult.getString(5));
				String club = slcResult.getString(6);
				String contract = slcResult.getString(7);
				Salary salary = Salary.valueOf(slcResult.getString(8));

				Player player = new Player(id, name, surname, age, league, club, contract, salary);
				playerUser.add(player);

			}

			slcResult.close();
			slctStm.close();

		}
	}

	/**
	 *  method for retrieving a previous player from the database
	 * @param prev4DB
	 */
	public void showPrevious(List<Player> prev4DB) {
		pos--;
		if (pos > 0) {
			Player player = playerUser.get(pos);
			showOnViewPanel(player);
		} else {
			pos = 0;
			Player player = playerUser.get(pos);
			showOnViewPanel(player);
			JOptionPane.showMessageDialog(null, "You have reached the first user in the database.");
		}
	}

	/**
	 *  method for retrieving first player from the database
	 * @param first4DB
	 */
	public void showFirst(List<Player> first4DB) {
		pos = 0;
		Player player = playerUser.get(pos);
		showOnViewPanel(player);
	}

	/**
	 *  method for retrieving last player from the database
	 * @param last4DB
	 */
	public void showLast(List<Player> last4DB) {
		pos = playerUser.size() - 1;
		Player player = playerUser.get(pos);
		showOnViewPanel(player);
	}

	/**
	 *  method for retrieving a next player from the database
	 * @param next4DB
	 */
	public void showNext(List<Player> next4DB) {
		pos++;
		if (pos < playerUser.size()) {
			Player player = playerUser.get(pos);
			showOnViewPanel(player);
		} else {
			pos = playerUser.size() - 1;
			Player player = playerUser.get(pos);
			showOnViewPanel(player);
			JOptionPane.showMessageDialog(null, "You have reached the last user in the database.");
		}
	}
	
}