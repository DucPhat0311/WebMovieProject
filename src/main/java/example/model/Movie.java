package example.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	private String ageRatingCode;

	private List<Genre> genres = new ArrayList<>();
	private List<Director> directors = new ArrayList<>();
	private List<MovieActor> actors = new ArrayList<>();

	public Movie() {
	}

	// === GETTER & SETTER CƠ BẢN ===
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

	public void setAgeRatingCode(String code) {
		this.ageRatingCode = code;
	}

	// === QUAN HỆ N-N ===
	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public void addGenre(Genre g) {
		genres.add(g);
	}

	public List<Director> getDirectors() {
		return directors;
	}

	public void setDirectors(List<Director> directors) {
		this.directors = directors;
	}

	public void addDirector(Director d) {
		directors.add(d);
	}

	public List<MovieActor> getActors() {
		return actors;
	}

	public void setActors(List<MovieActor> actors) {
		this.actors = actors;
	}

	public void addActor(MovieActor ma) {
		actors.add(ma);
	}

	// === HELPER CHO JSP ===
	public String getGenreNames() {
		if (genres.isEmpty())
			return "Chưa xác định";
		return genres.stream().map(Genre::getName).collect(Collectors.joining(", "));
	}

	public String getDirectorNames() {
		if (directors.isEmpty())
			return "Chưa xác định";
		return directors.stream().map(Director::getName).collect(Collectors.joining(", "));
	}

	public String getActorWithRoles() {
		if (actors.isEmpty())
			return "Chưa có thông tin";
		return actors.stream().map(MovieActor::getDisplay).collect(Collectors.joining(", "));
	}
	
}