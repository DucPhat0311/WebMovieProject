package example.model;


public class Seat {
	private int id;
	private Room room;
	private char row;
	private int number;
	private SeatType type;

	public Seat() {
	}

	public Seat(int id, Room room, char row, int number, String typeStr) {
		this.id = id;
		this.room = room;
		this.row = row;
		this.number = number;
		this.type = SeatType.valueOf(typeStr.toLowerCase());
	}

	// Getters & Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public char getRow() {
		return row;
	}

	public void setRow(char row) {
		this.row = row;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public SeatType getType() {
		return type;
	}

	public void setType(SeatType type) {
		this.type = type;
	}

	public void setType(String type) {
		this.type = SeatType.valueOf(type.toLowerCase());
	}

	public String getLabel() {
		return row + String.valueOf(number);
	}

}