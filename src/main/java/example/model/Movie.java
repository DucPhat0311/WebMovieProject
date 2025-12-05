package example.model;

import java.sql.Date;

public class Movie {
	private int movieId;
    private String title;
    private String description;
    private int duration;
    private Date releaseDate;
    private String ageWarning; // P, T13, T16, T18
    private String posterUrl;
    private String trailerUrl;
    private boolean isActive;
    private String status; // Coming Soon, Now Showing
    
    
    
	public Movie(int movieId, String title, String description, int duration, Date releaseDate, String ageWarning,
			String posterUrl, String trailerUrl, boolean isActive, String status) {
		super();
		this.movieId = movieId;
		this.title = title;
		this.description = description;
		this.duration = duration;
		this.releaseDate = releaseDate;
		this.ageWarning = ageWarning;
		this.posterUrl = posterUrl;
		this.trailerUrl = trailerUrl;
		this.isActive = true;
		this.status = status;
	}
    
    
}
