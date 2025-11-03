package example.composite;

public class Seat extends CinemaComponent {
    private int row;
    private int col;
    private boolean isBooked = false;

    public Seat(int id, String seatCode, int row, int col) {
        this.id = id;
        this.name = seatCode;   
        this.row = row;
        this.col = col;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void book() {
        this.isBooked = true;
    }

    @Override
    public void displayInfo() {
        System.out.println("    Seat: " + name + " (row:" + row + ", col:" + col + ", booked:" + isBooked + ")");
    }
}
