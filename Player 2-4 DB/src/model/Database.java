package model;

/**
 * Class Database is used to communicate with database
 * @author Niko
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Database {

	private List<Player> players;
	private Connection conn;
	private Player playerPos;
	private int pos = 0;

	public Database() {
		players = new LinkedList<Player>();
		conn = null;
	}

	/** method for connecting to a database
	 * 
	 * @throws SQLException
	 */
	public void connect() throws SQLException {

		System.out.println("Connecting to database..");

		try {
			// load driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			// obtain connection
			String url = "jdbc:mysql://db4free.net:3306/noop2020n"; // your database
			String user = "********"; // your user name
			String password = "********"; // your password
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Connected to -> " + conn.toString());
		} catch (ClassNotFoundException e) {
			System.out.println("Could not load driver!!!");
		}

	}

	/** method for disconnecting from database
	 * 
	 * @throws SQLException
	 */
	public void disconnect() throws SQLException {
		conn.close();
		System.out.println("Disconnected from DB....");
	}

	public void setPlayer2DB(Player player) {
		players.add(player);
	}

	/** method for saving and updating data in a database
	 * 
	 * @throws SQLException
	 */
	public void save2DB() throws SQLException {

		if (conn != null) {
			// SQL queries
			String cntSql = "SELECT COUNT(*) as count from PlayerTable where id=?";
			String insSql = "INSERT INTO PlayerTable(id, name, surrname, age, league, club, contractLength, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			String updSql = "UPDATE PlayerTable set name = ?, surrname = ?, age = ?, league = ?, club = ?, contractLength = ?, salary = ? where id = ?";
			// statements
			PreparedStatement cntStm = conn.prepareStatement(cntSql);
			PreparedStatement insrStm = conn.prepareStatement(insSql);
			PreparedStatement updStm = conn.prepareStatement(updSql);

			// checking all players
			for (Player player : players) {

				int id = player.getId();
				String name = player.getName();
				String surname = player.getSurname();
				int age = player.getAge();
				League league = player.getLeague();
				String club = player.getClub();
				String contract = player.getContract();
				Salary salary = player.getSalary();

				cntStm.setInt(1, id);
				ResultSet result = cntStm.executeQuery();
				result.next();

				int cnt = result.getInt(1);
				System.out.println("Cnt -> " + cnt);

				if (cnt == 0) {
					System.out.println("Inserting new player: " + id);
					
					// insert commands
					int col = 1;
					insrStm.setInt(col++, id);
					insrStm.setString(col++, name);
					insrStm.setString(col++, surname);
					insrStm.setInt(col++, age);
					insrStm.setString(col++, league.name());
					insrStm.setString(col++, club);
					insrStm.setString(col++, contract);
					insrStm.setString(col++, salary.name());
					
					insrStm.executeUpdate();

				} else {
					System.out.println("Updating player: " + id);
					
					// update commands
					int col = 1;
					updStm.setString(col++, name);
					updStm.setString(col++, surname);
					updStm.setInt(col++, age);
					updStm.setString(col++, league.name());
					updStm.setString(col++, club);
					updStm.setString(col++, contract);
					updStm.setString(col++, salary.name());
					
					updStm.setInt(col++, id);
					
					updStm.executeUpdate();
					
				}
			}
			
			cntStm.close();
			insrStm.close();
			updStm.close();
			
		}

	}
	
	/** method for loading data from a database
	 * 
	 * @throws SQLException
	 */
	public void load4DB() throws SQLException {

		if (conn != null) {
			System.out.println("Loading from DB..");
			String selectSQL = "SELECT id, name, surrname, age, league, club, contractLength, salary FROM PlayerTable ORDER BY id";
			PreparedStatement slctStm = conn.prepareStatement(selectSQL);

			ResultSet slcResult = slctStm.executeQuery();
			players.clear();
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
				players.add(player);

			}

			slcResult.close();
			slctStm.close();

		}
	}

	/**
	 * method used for listing all players from database after connection
	 * 
	 * @see Player
	 */
	public void listAll4DB() {
		System.out.println("<<<<<<<<<<<<< Listing all from DB >>>>>>>>>>>>>");
		for(Player player: players) {
			player.description();
		}
		
	}

	/**
	 * method used for getting all players from database-
	 * 
	 * @see Player
	 */
	public List<Player> getAll4DB(){
		return players;
	}

	/** the method we use when saving data to a file
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void saveDB2File(File file) throws IOException {
		
		Player[] plr = players.toArray(new Player[players.size()]);
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject(plr);
		oos.close();
		
	}
	
	/** the method we use when load data from a file
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void readData4File(File file) throws IOException {
	
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		
		try {
			Player[] plr = (Player[]) ois.readObject();
			players.clear();
			players.addAll(Arrays.asList(plr));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		ois.close();
		
		
	}
	
}