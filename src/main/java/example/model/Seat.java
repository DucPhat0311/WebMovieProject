package example.model;

public class Seat {
	private int seatId;
    private int roomId;
    private int seatTypeId;
    private String seatRow;
    private int seatNumber;
    
    
	public Seat() {}
	
	public Seat(int seatTypeId, String seatRow, int seatNumber) {
		this.seatTypeId = seatTypeId;
		this.seatRow = seatRow;
		this.seatNumber = seatNumber;
	}
    
	public Seat(int seatId, int roomId, int seatTypeId, String seatRow, int seatNumber) {
		super();
		this.seatId = seatId;
		this.roomId = roomId;
		this.seatTypeId = seatTypeId;
		this.seatRow = seatRow;
		this.seatNumber = seatNumber;
	}

	public int getSeatId() {
		return seatId;
	}

	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getSeatTypeId() {
		return seatTypeId;
	}

	public void setSeatTypeId(int seatTypeId) {
		this.seatTypeId = seatTypeId;
	}

	public String getSeatRow() {
		return seatRow;
	}

	public void setSeatRow(String seatRow) {
		this.seatRow = seatRow;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}
	
	
    
    
}
