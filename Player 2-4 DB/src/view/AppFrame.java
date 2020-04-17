package view;

/**
 * a class that represent final graphic design of application
 * 
 * @author Niko
 */

import java.awt.BorderLayout;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.Controller;
import model.Player;

public class AppFrame extends JFrame {

	private InputPanel inputPanel;
	private PresentationPanel presentationPanel;
	private JMenuBar menuBar;
	private JFileChooser fileChooser;
	private Controller controller;
	private ViewPanel viewPanel;

	public AppFrame() {

		super("Final project -> Player Info");
		setLayout(new BorderLayout());
		setSize(780, 520);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		initAll();
		layoutAll();
		activateApp();

	}

	/**
	 *  component initialization
	 */
	private void initAll() {

		inputPanel = new InputPanel();
		presentationPanel = new PresentationPanel();
		controller = new Controller();
		controller.setData4Table(presentationPanel);
		menuBar = createMenuBar();
		setJMenuBar(menuBar);
		fileChooser = new JFileChooser();
		setFileExtension();
	}

	/**
	 *  method of making design
	 */
	private void layoutAll() {

		add(inputPanel, BorderLayout.WEST);
		add(presentationPanel, BorderLayout.EAST);

	}

	/**
	 *  method that activate components
	 */
	private void activateApp() {

		inputPanel.setInputPanelListener(new InputPanelListener() {

			@Override
			public void inputPanelEventOccured(InputPanelEvent ipe) {
				
				try {
					
					Player plr = inputPanel.getPlayer();
					controller.addingNewPlayerAndReloadTable(plr, presentationPanel);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	/**
	 *  creating and activation menuBar 
	 * @return {@link MenuBar}
	 */
	private JMenuBar createMenuBar() {

		JMenuBar menu = new JMenuBar();

		// file menu
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveFile = new JMenuItem("Save");
		JMenuItem importFile = new JMenuItem("Import");
		JMenuItem exitFile = new JMenuItem("Exit");
		fileMenu.add(saveFile);
		fileMenu.add(importFile);
		fileMenu.addSeparator();
		fileMenu.add(exitFile);

		// aktivacija file menu-a
		// save data menu item
		saveFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				int val = fileChooser.showSaveDialog(AppFrame.this);

				if (val == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						controller.saveData2File(file);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(AppFrame.this, "Unable to save data into the file!", "Save error",
								JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		});

		// import data menu item
		importFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				int val = fileChooser.showOpenDialog(AppFrame.this);

				if (val == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						controller.importData4File(file);
						controller.showPlayerData(presentationPanel);
						// controller.showImportedDataInTxtPanel(presentationPanel);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(AppFrame.this, "Unable to read data from the file!", "Open error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		// exit menu item
		exitFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int val = JOptionPane.showConfirmDialog(AppFrame.this, "Do you realy want to exit app", "Exit dialog",
						JOptionPane.OK_CANCEL_OPTION);
				if (val == JOptionPane.OK_OPTION) {
					System.exit(0);
				} else {
					System.out.println("Exit canceled by user...");
				}
			}
		});

		// server menu
		JMenu serverMenu = new JMenu("Server");
		JMenuItem saveServer = new JMenuItem("Save to ..");
		JMenuItem importServer = new JMenuItem("Import from ..");
		JMenuItem close = new JMenuItem("Close connection");
		serverMenu.add(saveServer);
		serverMenu.add(importServer);
		serverMenu.addSeparator();
		serverMenu.add(close);

		// aktivacija server menu-a
		// save data to server
		saveServer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					controller.connect2DB();
					System.out.println("-- connect --");
					controller.save2DB();
					System.out.println("-- save --");
					controller.disconnect4DB();
					System.out.println("-- disconnect --");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(AppFrame.this, "Some DB server connection error!!!", "Save DB error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// import data from server
		importServer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					controller.connect2DB();
				} catch (SQLException e3) {
					JOptionPane.showMessageDialog(AppFrame.this, "Unable to connect to a DB server!!!",
							"Connection DB error", JOptionPane.ERROR_MESSAGE);
				}
				try {
					controller.load4DB();
					controller.showPlayerData(presentationPanel);
					// controller.showImportedDataInTxtPanel(presentationPanel);
				} catch (SQLException e2) {
					JOptionPane.showMessageDialog(AppFrame.this, "Unable to load data from a DB server!!!",
							"Load DB error", JOptionPane.ERROR_MESSAGE);
				}
				try {
					controller.disconnect4DB();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(AppFrame.this, "Unable to disconnect from a DB server!!!",
							"Disconnect DB error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// disconnect from server
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					controller.disconnect4DB();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(AppFrame.this, "Unable to disconnect from a DB server!!!",
							"Disconnect DB error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// view menu
		JMenu viewMenu = new JMenu("View");
		JMenuItem showView = new JMenuItem("Show player info");
		JMenuItem resetView = new JMenuItem("Reset view panel");
		JMenuItem closeView = new JMenuItem("Close view panel");
		viewMenu.add(showView);
		viewMenu.add(resetView);
		viewMenu.addSeparator();
		viewMenu.add(closeView);

		// aktivacija view menu-a
		// view panel
		showView.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				viewPanel = new ViewPanel();

			}
		});

		// reset View
		resetView.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				viewPanel.resetPane();
			}
		});

		// close view panel
		closeView.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int val = JOptionPane.showConfirmDialog(viewPanel, "Do you realy want to exit app", "Exit dialog",
						JOptionPane.OK_CANCEL_OPTION);
				if (val == JOptionPane.OK_OPTION) {
//					System.exit(0);
					viewPanel.dispose();
				} else {
					System.out.println("Exit canceled by user...");
				}
			}
		});

		menu.add(fileMenu);
		menu.add(serverMenu);
		menu.add(viewMenu);

		return menu;
	}

	private void setFileExtension() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Player files (*.plr)", "plr");
		fileChooser.setFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
	}

}