package example.model;

public class Cinema {
    private int cinemaId;
    private String cinemaName;
    private int cityId;
    private String address;
    
    public Cinema() {}
    
    public Cinema(String cinemaName, int cityId, String address) {
        this.cinemaName = cinemaName;
        this.cityId = cityId;
        this.address = address;
    }
    
	public Cinema(int cinemaId, String cinemaName, int cityId, String address) {
		super();
		this.cinemaId = cinemaId;
		this.cinemaName = cinemaName;
		this.cityId = cityId;
		this.address = address;
	}

	public int getCinemaId() {
		return cinemaId;
	}

	public void setCinemaId(int cinemaId) {
		this.cinemaId = cinemaId;
	}

	public String getCinemaName() {
		return cinemaName;
	}

	public void setCinemaName(String cinemaName) {
		this.cinemaName = cinemaName;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
    
	
    
}