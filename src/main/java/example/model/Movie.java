package example.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Movie {

	
	private int movieId;
    private String title;
    private String description;
    private int genreId;
    private int duration;
    private Date releaseDate;
    private String ageWarning;
    private String posterUrl;
    private String trailerUrl;
    private String status;
    
    public Movie() {}

   
    
	public Movie(int movieId, String title, String description, int genreId, int duration, Date releaseDate,
			String ageWarning, String posterUrl, String trailerUrl, String status) {
		super();
		this.movieId = movieId;
		this.title = title;
		this.description = description;
		this.genreId = genreId;
		this.duration = duration;
		this.releaseDate = releaseDate;
		this.ageWarning = ageWarning;
		this.posterUrl = posterUrl;
		this.trailerUrl = trailerUrl;
		this.status = status;
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

	public int getGenreId() {
		return genreId;
	}

	public void setGenreId(int genreId) {
		this.genreId = genreId;
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


	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getAgeRatingCode() {
		return ageRatingCode;
	}

	public void setAgeRatingCode(String code) {
		this.ageRatingCode = code;
	}


	public String getTrailerUrl() {
		return trailerUrl;
	}

	public void setTrailerUrl(String trailerUrl) {
		this.trailerUrl = trailerUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}      
    
    
}

