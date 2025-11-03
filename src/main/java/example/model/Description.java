package example.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Corresponds to table descriptions (id, producer, director, actor, release_date)
 */
public class Description {
    private int id;
    private String producer;
    private List<String> director; // or store as CSV in DB
    private List<String> actor;
    private LocalDate releaseDate;

    public Description() {}

    public Description(int id, String producer, List<String> director, List<String> actor, LocalDate releaseDate) {
        this.id = id;
        this.producer = producer;
        this.director = director;
        this.actor = actor;
        this.releaseDate = releaseDate;
    }

    // --- getters / setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getProducer() { return producer; }
    public void setProducer(String producer) { this.producer = producer; }

    public List<String> getDirector() { return director; }
    public void setDirector(List<String> director) { this.director = director; }

    public List<String> getActor() { return actor; }
    public void setActor(List<String> actor) { this.actor = actor; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    @Override
    public String toString() {
        return "Description{" +
                "id=" + id +
                ", producer='" + producer + '\'' +
                ", director=" + director +
                ", actor=" + actor +
                ", releaseDate=" + releaseDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Description that = (Description) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
