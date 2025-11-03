package example.composite;

import java.util.ArrayList;
import java.util.List;

public class Cinema extends CinemaComponent {
    private List<CinemaComponent> rooms = new ArrayList<>();

    public Cinema(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void add(CinemaComponent component) {
        component.setParent(this);
        rooms.add(component);
    }

    @Override
    public void remove(CinemaComponent component) {
        rooms.remove(component);
        component.setParent(null);
    }

    @Override
    public List<CinemaComponent> getChildren() {
        return rooms;
    }

    @Override
    public void displayInfo() {
        System.out.println("Cinema: " + name);
        for (CinemaComponent room : rooms) {
            room.displayInfo();
        }
    }
}

