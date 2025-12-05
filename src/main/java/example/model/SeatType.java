package example.model;

public class SeatType {
	private int seatTypeId;
    private String name; // Standard, VIP
    private double surcharge;
     
	public SeatType() {}
	
	public SeatType(String name, double surcharge) {
		this.name = Constant.SEAT_STANDARD;
		this.surcharge = surcharge;
	} 

	public SeatType(int seatTypeId, String name, double surcharge) {
		super();
		this.seatTypeId = seatTypeId;
		this.name = name;
		this.surcharge = surcharge;
	}

	public int getSeatTypeId() {
		return seatTypeId;
	}

	public void setSeatTypeId(int seatTypeId) {
		this.seatTypeId = seatTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(double surcharge) {
		this.surcharge = surcharge;
	} 
	
	
    
    
}
