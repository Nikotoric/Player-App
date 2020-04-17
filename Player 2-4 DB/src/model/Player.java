package model;

import java.io.Serializable;

public class Player implements Serializable {

	private static int cnt = 100;
	private int id;
	private String name, surname;
	private int age;
	private League league;
	private String club, contract;
	private Salary salary;

	public Player() {

	}

	public Player(int id, String name, String surname, int age, League league, String club, String contract,
			Salary salary) {

		this.id = id;
		this.name = name;
		this.surname = surname;
		this.age = age;
		this.league = league;
		this.club = club;
		this.contract = contract;
		this.salary = salary;
		cnt = id + 1;
	}

	public Player(String name, String surname, int age, League league, String club, String contract, Salary salary) {

		this.id = cnt;
		this.name = name;
		this.surname = surname;
		this.age = age;
		this.league = league;
		this.club = club;
		this.contract = contract;
		this.salary = salary;
		cnt++;
	}

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

	@Override
	public String toString() {
		return "Player ID: " + id + "\nName: " + name + "\nSurname: " + surname + "\nAge: " + age + "\nLeague: " + league
				+ "\nClub: " + club + "\nContract: " + contract + "\nSalary: " + salary;
	}

	public void description() {
		System.out.println(toString());
	}

	public static void setCnt(int count) {
		cnt = count;
	}

}