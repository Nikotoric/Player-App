package controller;

/**
 * A class that represents a data container for created players
 * It can be viewed as a simple database
 * 
 * @author Niko
 */

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import model.Database;
import model.Player;
import view.PresentationPanel;
import view.ViewPanel;

public class Controller {

	private Database database;

	public Controller() {
		database = new Database();
	}

	/** 
	 * method for connecting to a database
	 * 
	 * @throws SQLException
	 */
	public void connect2DB() throws SQLException {
		database.connect();
	}

	/** 
	 * method for disconnecting from database
	 * 
	 * @throws SQLException
	 */
	public void disconnect4DB() throws SQLException {
		database.disconnect();
	}

	/** 
	 * method for saving and updating data in a database
	 * 
	 * @throws SQLException
	 */
	public void save2DB() throws SQLException {
		database.save2DB();
	}

	/** 
	 * method for loading data from a database
	 * 
	 * @throws SQLException
	 */
	public void load4DB() throws SQLException {
		database.load4DB();
	}
	
	/**
	 * method for adding new player in database
	 * @param player
	 * @see Player
	 */
	public void addNewPlayer2DB(Player player) {
		database.setPlayer2DB(player);
	}

	/**
	 * method that we use if we want to get all players from database
	 * 
	 * @return {@link Player}
	 */
	public List<Player> getAllPlayers(){
		return database.getAll4DB();
	}
	
	/** method for showing player info in table on presentationPanel
	 * 
	 * @param panel
	 * @see PresentationPanel
	 */
	public void showPlayerData(PresentationPanel panel) {
		panel.showDataOnTable();
	}
	
	/**
	 * method for setting data in table
	 * 
	 * @param panel
	 * @see PresentationPanel
	 */
	public void setData4Table(PresentationPanel panel) {
		panel.setDBData(database);
	}

	/**
	 * method for showing data in ViewPanel
	 * 
	 * @param plr
	 * @param viewPanel
	 * @see ViewPanel
	 */
	public void showAllDataInViewPanel(Player plr, ViewPanel viewPanel) {
		viewPanel.showImportedDataInViewPanel(database.getAll4DB());
	}
	
	/**
	 * method for showing data in ViewPanel
	 * 
	 * @param viewPanel
	 * @see ViewPanel
	 */
	public void showImportedDataInViewPanel(ViewPanel viewPanel) {
		viewPanel.showImportedDataInViewPanel(database.getAll4DB());
	}
	
	/**
	 *  the method we use when saving data to a file
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void saveData2File(File file) throws IOException {
		database.saveDB2File(file);
	}

	/**
	 * the method we use when load data from a file
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void importData4File(File file) throws IOException {

		database.readData4File(file);
		int num = database.getAll4DB().get(database.getAll4DB().size() - 1).getId();
		Player.setCnt(num + 1);

	}

	/**
	 * method for adding new Player in database 
	 * @param plr
	 * @param presentationPanel
	 * @throws SQLException
	 * 
	 * @see Player
	 * @see PresentationPanel
	 * 
	 */
	public void addingNewPlayerAndReloadTable(Player plr, PresentationPanel presentationPanel) throws SQLException {
		
		database.connect();
		database.setPlayer2DB(plr);
		database.save2DB();
		database.load4DB();
		presentationPanel.setDBData(database);
		presentationPanel.showDataOnTable();
		database.disconnect();
		
	}
	
	/**
	 * method for reloading table after changing 
	 * @param panel
	 * @throws SQLException
	 * @see PresentationPanel
	 */
	public void reloadTable(PresentationPanel panel) throws SQLException {
		
		database.connect();
		System.out.println("Reloading table\n--->Saving data to DB");
		database.save2DB();
				
		System.out.println("---> loading data that we save to data");
		database.load4DB();
		
		panel.setDBData(database);
		panel.showDataOnTable();
		
		System.out.println("--> and after all we disconnecting from database.");
		database.disconnect();
		
	}
	
}