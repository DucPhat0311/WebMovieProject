package example.model;

public class Room {
    private int roomId;
    private int cinemaId;
    private String roomName;
    private int totalSeats;
    
    public Room() {}

    public Room(int cinemaId, String roomName, int totalSeats) {
        this.cinemaId = cinemaId;
        this.roomName = roomName;
        this.totalSeats = totalSeats;
    }
    
	public Room(int roomId, int cinemaId, String roomName, int totalSeats) {
		super();
		this.roomId = roomId;
		this.cinemaId = cinemaId;
		this.roomName = roomName;
		this.totalSeats = totalSeats;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getCinemaId() {
		return cinemaId;
	}

	public void setCinemaId(int cinemaId) {
		this.cinemaId = cinemaId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}


}