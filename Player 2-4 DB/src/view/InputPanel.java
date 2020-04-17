package view;

/**
 * A class that represent left side of graphic design of application
 * Also it's the part of the application through which the user enters the data
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import model.League;
import model.Player;
import model.Salary;

public class InputPanel extends JPanel {

	private JPanel mainPanel, upperPanel, centerPanel, bottomPanel;

	private JTextField nameField, surnameField, ageField;

	private JComboBox<League> leagueCombo;
	private JTextField clubField;

	private JRadioButton year1, year1to3, year3to5, year5;
	private ButtonGroup contractGroup;
	private JList<Salary> salaryList;
	private JScrollPane scroll;

	private JButton submitBtn;

	private InputPanelListener inputPanelListener;
	private Player player;

	public InputPanel() {

		setLayout(new BorderLayout());

		initAll();
		setBorders();
		layoutComps();
		activateInputPanel();

	}

	/**
	 *  component initialization
	 */
	private void initAll() {

		mainPanel = new JPanel();
		upperPanel = new JPanel();
		centerPanel = new JPanel();
		bottomPanel = new JPanel();

		nameField = new JTextField(10);
		surnameField = new JTextField(10);
		ageField = new JTextField(10);
		
		// provjera jeli unesen broj u polje za godine
		ageField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				
				char input = e.getKeyChar();
				if((input < '0' || input > '9') && input != '\b') {
					e.consume();
					System.out.println("Invalid input. Enter number!");
				}
			}
			
		});

		DefaultComboBoxModel<League> leagueModel = new DefaultComboBoxModel<>();
		leagueModel.addElement(League.HNL);
		leagueModel.addElement(League.PremierLeague);
		leagueModel.addElement(League.Bundesliga);
		leagueModel.addElement(League.SeriaA);
		leagueModel.addElement(League.LaLiga);
		leagueModel.addElement(League.Ligue1);
		leagueModel.addElement(League.JupilerLeague);
		leagueModel.addElement(League.PrimeiraLiga);
		leagueModel.addElement(League.Championship);
		leagueModel.addElement(League.Bundesliga2);
		leagueModel.addElement(League.SeriaB);
		leagueModel.addElement(League.LaLiga2);
		leagueModel.addElement(League.Ligue2);
		leagueModel.addElement(League.someOtherLeague);
		leagueCombo = new JComboBox<>();
		leagueCombo.setModel(leagueModel);
		leagueCombo.setSelectedIndex(0);

		clubField = new JTextField(10);

		year1 = new JRadioButton("1 year");
		year1.setActionCommand("1 year");
		year1to3 = new JRadioButton("1 - 3 years");
		year1to3.setActionCommand("1 - 3 years");
		year3to5 = new JRadioButton("3 - 5 years");
		year3to5.setActionCommand("3 - 5 years");
		year5 = new JRadioButton("5 years");
		year5.setActionCommand("5 years");
		contractGroup = new ButtonGroup();
		contractGroup.add(year1);
		contractGroup.add(year1to3);
		contractGroup.add(year3to5);
		contractGroup.add(year5);
		year1.setSelected(true);

		DefaultListModel<Salary> salaryModel = new DefaultListModel<>();
		salaryModel.addElement(Salary.lessThan100k€pW);
		salaryModel.addElement(Salary.between100kAnd250k€pW);
		salaryModel.addElement(Salary.between250kAnd400k€pW);
		salaryModel.addElement(Salary.moreThan400k€pW);
		salaryList = new JList<>();
		salaryList.setModel(salaryModel);
		salaryList.setVisibleRowCount(3);
		salaryList.setBorder(BorderFactory.createEtchedBorder());
		salaryList.setSelectedIndex(0);
		scroll = new JScrollPane(salaryList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		submitBtn = new JButton("Submit");

	}

	/**
	 *  method of making design
	 */
	private void layoutComps() {
		
		mainPanel.setLayout(new GridLayout(3,1));

		upperPanel.setLayout(new GridBagLayout());
		centerPanel.setLayout(new GridBagLayout());
		bottomPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		Insets defaultInsets = new Insets(2,2,2,2);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.insets = defaultInsets;
		upperPanel.add(new JLabel("Name: "), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		upperPanel.add(nameField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.insets = defaultInsets;
		upperPanel.add(new JLabel("Surname: "), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		upperPanel.add(surnameField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.insets = defaultInsets;
		upperPanel.add(new JLabel("Age: "), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		upperPanel.add(ageField, gbc);
		

		add(upperPanel, BorderLayout.NORTH);

		// center panel - club info
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.insets = defaultInsets;
		centerPanel.add(new JLabel("League: "), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		centerPanel.add(leagueCombo, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		centerPanel.add(new JLabel("Club: "), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		centerPanel.add(clubField, gbc);

		add(centerPanel, BorderLayout.CENTER);

		// bottom panel - contract info
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.insets = defaultInsets;
		bottomPanel.add(new JLabel("Contract length: "), gbc);
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		bottomPanel.add(year1, gbc);
		gbc.gridx = 1;
		bottomPanel.add(year1to3, gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		bottomPanel.add(year3to5, gbc);
		gbc.gridx = 1;
		bottomPanel.add(year5, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		bottomPanel.add(new JLabel("Salary: "), gbc);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		bottomPanel.add(scroll, gbc);

		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.CENTER;
		bottomPanel.add(submitBtn, gbc);

		add(bottomPanel, BorderLayout.SOUTH);

	}

	/**
	 *  method that activate components of InputPanel
	 *  activating submit button
	 */
	private void activateInputPanel() {
		
		submitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(inputPanelListener != null) {
					
					
					if(nameField.getText().isEmpty() && surnameField.getText().isEmpty() && ageField.getText().isEmpty() && clubField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Niste popunili namefield, surnamefield, agefield i clubfield",
								"**Error**", JOptionPane.ERROR_MESSAGE);
					} else if(nameField.getText().isEmpty() && surnameField.getText().isEmpty() && ageField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Niste popunili namefield, surnamefield i agefield",
								"**Error**", JOptionPane.ERROR_MESSAGE);
					} else if(nameField.getText().isEmpty() && surnameField.getText().isEmpty() && clubField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Niste popunili namefield, surnamefield i clubfield",
								"**Error**", JOptionPane.ERROR_MESSAGE);
					} else if(nameField.getText().isEmpty() && ageField.getText().isEmpty() && clubField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Niste popunili namefield, agefield i clubfield",
								"**Error**", JOptionPane.ERROR_MESSAGE);
					} else if(surnameField.getText().isEmpty() && ageField.getText().isEmpty() && clubField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Niste popunili surnamefield, agefield i clubfield",
								"**Error**", JOptionPane.ERROR_MESSAGE);
					} else if(nameField.getText().isEmpty() && surnameField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Niste popunili namefield, surnamefield",
								"**Error**", JOptionPane.ERROR_MESSAGE);
					} else if(nameField.getText().isEmpty() && ageField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Niste popunili namefield, agefield",
								"**Error**", JOptionPane.ERROR_MESSAGE);
					} else if(nameField.getText().isEmpty() && clubField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Niste popunili namefield, clubfield",
								"**Error**", JOptionPane.ERROR_MESSAGE);
					} else if(surnameField.getText().isEmpty() && ageField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Niste popunili surnamefield, agefield",
								"**Error**", JOptionPane.ERROR_MESSAGE);
					} else if(surnameField.getText().isEmpty() && clubField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Niste popunili surnamefield, clubfield",
								"**Error**", JOptionPane.ERROR_MESSAGE);
					} else if(clubField.getText().isEmpty() && ageField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Niste popunili clubfield i agefield",
								"**Error**", JOptionPane.ERROR_MESSAGE);
					} else if(nameField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Niste popunili namefield",
								"**Error**", JOptionPane.ERROR_MESSAGE);
					}  else if(surnameField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Niste popunili surnamefield",
								"**Error**", JOptionPane.ERROR_MESSAGE);
					} else if(ageField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Niste popunili agefield",
								"**Error**", JOptionPane.ERROR_MESSAGE);
					} else if(clubField.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Niste popunili clubfield",
								"**Error**", JOptionPane.ERROR_MESSAGE);
					} else if(!nameField.getText().isEmpty() && !surnameField.getText().isEmpty() && !ageField.getText().isEmpty() && !clubField.getText().isEmpty()) {
						
						player = new Player(nameField.getText(), 
								surnameField.getText(), Integer.parseInt(ageField.getText()),
								(League) leagueCombo.getSelectedItem(), 
								clubField.getText(), 
								contractGroup.getSelection().getActionCommand(), 
								salaryList.getSelectedValue());
						
						InputPanelEvent ipe = new InputPanelEvent(InputPanel.this, player);
						inputPanelListener.inputPanelEventOccured(ipe);
						player.description();
						
					}
				}
				
				resetInputPanel();
			}
		});
		
	}

	/**
	 *  setting borders of InputPanel
	 */
	private void setBorders() {

		Border innerPLayer = BorderFactory.createTitledBorder("Player info");
		Border innerClub = BorderFactory.createTitledBorder("Club info");
		Border innerContract = BorderFactory.createTitledBorder("Contract info");
		Border outter = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border player = BorderFactory.createCompoundBorder(outter, innerPLayer);
		Border club = BorderFactory.createCompoundBorder(outter, innerClub);
		Border contract = BorderFactory.createCompoundBorder(outter, innerContract);

		upperPanel.setBorder(player);
		centerPanel.setBorder(club);
		bottomPanel.setBorder(contract);

	}

	/**
	 *  a method that returns components of InputPanel to their initial startup
	 */
	public void resetInputPanel() {

		nameField.setText("");
		surnameField.setText("");
		ageField.setText("");
		leagueCombo.setSelectedIndex(0);
		clubField.setText("");
		contractGroup.clearSelection();
		year1.setSelected(true);
		salaryList.setSelectedIndex(0);

	}

	public Player getPlayer() {
		return player;
	}

	public void setInputPanelListener(InputPanelListener inputPanelListener) {
		this.inputPanelListener = inputPanelListener;
	}

}