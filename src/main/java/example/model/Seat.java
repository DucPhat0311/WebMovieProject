package example.model;

import java.math.BigDecimal;

public class Seat{
	private int seatId;
    private int roomId;
    private String seatRow;         
    private int seatNumber;        
    private String seatType;        
    private BigDecimal priceBasedOnSeatType;
    
    public Seat() {}
    
    public Seat(int roomId, String seatRow, int seatNumber, String seatType, BigDecimal priceBasedOnSeatType) {
        this.roomId = roomId;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.priceBasedOnSeatType = priceBasedOnSeatType;
    }
    
	public Seat(int seatId, int roomId, String seatRow, int seatNumber, String seatType,
			BigDecimal priceBasedOnSeatType) {
		super();
		this.seatId = seatId;
		this.roomId = roomId;
		this.seatRow = seatRow;
		this.seatNumber = seatNumber;
		this.seatType = seatType;
		this.priceBasedOnSeatType = priceBasedOnSeatType;
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



	public String getSeatType() {
		return seatType;
	}



	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}



	public BigDecimal getPriceBasedOnSeatType() {
		return priceBasedOnSeatType;
	}



	public void setPriceBasedOnSeatType(BigDecimal priceBasedOnSeatType) {
		this.priceBasedOnSeatType = priceBasedOnSeatType;
	}



	public String getSeatLabel() {   // A10, B05...
        return seatRow + seatNumber;
    }
	
}