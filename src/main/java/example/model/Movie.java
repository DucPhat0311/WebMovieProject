package example.model;

import java.util.List;
import java.util.Objects;

public class Movie {
    private int id;
    private String title;
    private String genre;
    private int duration; 
    private String country;
    private String content;
    private int descriptionId;      
    private String posterUrl;       
    private Integer rating;         
    private String ageWarning;
    private String videoUrl;        

    public Movie() {}

    public Movie(int id, String title, String genre, int duration, String country,
                 String content, int descriptionId, String posterUrl, Integer rating,
                 String ageWarning, String videoUrl) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.country = country;
        this.content = content;
        this.descriptionId = descriptionId;
        this.posterUrl = posterUrl;
        this.rating = rating;
        this.ageWarning = ageWarning;
        this.videoUrl = videoUrl;
    }

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

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getDescriptionId() { return descriptionId; }
    public void setDescriptionId(int descriptionId) { this.descriptionId = descriptionId; }


    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getAgeWarning() { return ageWarning; }
    public void setAgeWarning(String ageWarning) { this.ageWarning = ageWarning; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    @Override
    public String toString() {
        return "Movie{" + "id=" + id + ", title=" + title + ", genre=" + genre + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;
        return id == movie.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
