package example.model;

public class Actor {
	private int movieId;
    private int artistId;
    
    public Actor() {}
    
    public Actor(int movieId, int artistId) {
        this.movieId = movieId;
        this.artistId = artistId;
    }

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}
    
    
}
