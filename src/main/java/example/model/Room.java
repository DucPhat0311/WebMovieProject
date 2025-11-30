package example.model;

public class Room {

	private int id;
	private Cinema cinema;
	private String name;
	private int totalSeats;

	public Room() {
	}

	public Room(int id, Cinema cinema, String name, int totalSeats) {
		this.id = id;
		this.cinema = cinema;
		this.name = name;
		this.totalSeats = totalSeats;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}
}