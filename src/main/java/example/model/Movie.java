package example.model;

import java.time.LocalDate;
import java.util.List;

public class Movie {

    private int id;
    private String title;
    private String genre;
    private int duration;
    private String country;
    private String producer;
    private List<String> directors;
    private List<String> actors;
    private LocalDate releaseDate;
    private String content;
    private String posterUrl;
    private String videoUrl;
    private double rating;
    private String ageWarning;

    public Movie() {}

    public Movie(int id, String title, String genre, int duration, String country,
                 String producer, List<String> directors, List<String> actors,
                 LocalDate releaseDate, String content, String posterUrl,
                 String videoUrl, double rating, String ageWarning) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.country = country;
        this.producer = producer;
        this.directors = directors;
        this.actors = actors;
        this.releaseDate = releaseDate;
        this.content = content;
        this.posterUrl = posterUrl;
        this.videoUrl = videoUrl;
        this.rating = rating;
        this.ageWarning = ageWarning;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getProducer() { return producer; }
    public void setProducer(String producer) { this.producer = producer; }

    public List<String> getDirectors() { return directors; }
    public void setDirectors(List<String> directors) { this.directors = directors; }

    public List<String> getActors() { return actors; }
    public void setActors(List<String> actors) { this.actors = actors; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getAgeWarning() { return ageWarning; }
    public void setAgeWarning(String ageWarning) { this.ageWarning = ageWarning; }
}
