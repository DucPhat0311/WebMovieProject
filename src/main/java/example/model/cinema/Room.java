package example.model.cinema;

public class Room {
	private int roomId;
    private int cinemaId;
    private String roomName;
    
    private Cinema cinema;
    
    public Room() {}
    
    public Room(int cinemaId, String roomName) {
		this.cinemaId = cinemaId;
		this.roomName = roomName;
	}
    
	public Room(int roomId, int cinemaId, String roomName) {
		super();
		this.roomId = roomId;
		this.cinemaId = cinemaId;
		this.roomName = roomName;
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
	
	public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }
	
    
    
}
