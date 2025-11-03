package example.composite;

import java.util.List;

public abstract class CinemaComponent {
    protected int id;
    protected String name;
    protected CinemaComponent parent;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public CinemaComponent getParent() { return parent; }
    public void setParent(CinemaComponent parent) { this.parent = parent; }

    public void add(CinemaComponent component) {
        throw new UnsupportedOperationException();
    }

    public void remove(CinemaComponent component) {
        throw new UnsupportedOperationException();
    }

    public List<CinemaComponent> getChildren() {
        throw new UnsupportedOperationException();
    }

    public abstract void displayInfo();
}
