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
    private String status; // Coming Soon, Now Showing, Ended
    
    public Movie() {}
    
	public Movie(int movieId, String title, String description, int duration, Date releaseDate, String ageWarning,
			String posterUrl, String trailerUrl, boolean isActive) {
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
		this.status = Constant.MOVIE_COMING_SOON;
	}



	public int getMovieId() {
		return movieId;
	}



	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public int getDuration() {
		return duration;
	}



	public void setDuration(int duration) {
		this.duration = duration;
	}



	public Date getReleaseDate() {
		return releaseDate;
	}



	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}



	public String getAgeWarning() {
		return ageWarning;
	}



	public void setAgeWarning(String ageWarning) {
		this.ageWarning = ageWarning;
	}



	public String getPosterUrl() {
		return posterUrl;
	}



	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}



	public String getTrailerUrl() {
		return trailerUrl;
	}



	public void setTrailerUrl(String trailerUrl) {
		this.trailerUrl = trailerUrl;
	}



	public boolean isActive() {
		return isActive;
	}



	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}
	
	
    
    
}
