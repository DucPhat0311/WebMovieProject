package example.model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Movie {
	private int id;
	private String title;
	private int duration;
	private String country;
	private String producer;
	private LocalDate releaseDate;
	private String content;
	private String posterUrl;
	private String videoUrl;
	private double rating;
	private String ageRatingCode; // Thay ageWarning bằng ageRatingCode

	// Quan hệ N-N mới
	private List<Genre> genres = new ArrayList<>();
	private List<Director> directors = new ArrayList<>();
	private List<Actor> actors = new ArrayList<>();

	public Movie() {
	}

	// Getter & Setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public void setAgeRatingCode(String ageRatingCode) {
		this.ageRatingCode = ageRatingCode;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public List<Director> getDirectors() {
		return directors;
	}

	public void setDirectors(List<Director> directors) {
		this.directors = directors;
	}

	public List<Actor> getActors() {
		return actors;
	}

	public void setActors(List<Actor> actors) {
		this.actors = actors;
	}

	// Helper methods
	public String getGenreNames() {
		if (genres == null || genres.isEmpty())
			return "";
		List<String> names = new ArrayList<>();
		for (Genre genre : genres) {
			names.add(genre.getName());
		}
		return String.join(", ", names);
	}

	public String getDirectorNames() {
		if (directors == null || directors.isEmpty())
			return "";
		List<String> names = new ArrayList<>();
		for (Director director : directors) {
			names.add(director.getName());
		}
		return String.join(", ", names);
	}
}