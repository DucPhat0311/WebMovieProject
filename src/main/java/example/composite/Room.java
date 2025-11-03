package example.composite;

import java.util.ArrayList;
import java.util.List;

public class Room extends CinemaComponent {
    private List<CinemaComponent> seats = new ArrayList<>();

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void add(CinemaComponent component) {
        component.setParent(this);
        seats.add(component);
    }

    @Override
    public void remove(CinemaComponent component) {
        seats.remove(component);
        component.setParent(null);
    }

    @Override
    public List<CinemaComponent> getChildren() {
        return seats;
    }

    @Override
    public void displayInfo() {
        System.out.println("  Room: " + name);
        for (CinemaComponent seat : seats) {
            seat.displayInfo();
        }
    }
}
