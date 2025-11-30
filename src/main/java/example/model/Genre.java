package example.model;

public class Genre {
	private int genreId;
	private String name;
	
	public Genre() {}
	
	public Genre(String name) {
		super();
		this.name = name;
	}
	
	public Genre(int genreId, String name) {
		super();
		this.genreId = genreId;
		this.name = name;
	}

	public int getGenreId() {
		return genreId;
	}

	public void setGenreId(int genreId) {
		this.genreId = genreId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
