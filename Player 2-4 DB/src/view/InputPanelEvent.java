package view;

import java.util.EventObject;

import model.League;
import model.Player;
import model.Salary;

public class InputPanelEvent extends EventObject {

	private int id;
	private String name, surname;
	private int age;
	private League league;
	private String club, contract;
	private Salary salary;

	public InputPanelEvent(Object source, Player player) {
		super(source);

		id = player.getId();
		name = player.getName();
		surname = player.getSurname();
		age = player.getAge();
		league = player.getLeague();
		club = player.getClub();
		contract = player.getContract();
		salary = player.getSalary();

	}

	// getters
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public int getAge() {
		return age;
	}

	public League getLeague() {
		return league;
	}

	public String getClub() {
		return club;
	}

	public String getContract() {
		return contract;
	}

	public Salary getSalary() {
		return salary;
	}

}