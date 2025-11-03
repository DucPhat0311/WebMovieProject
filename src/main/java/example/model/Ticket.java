package example.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Corresponds to table tickets.
 * Keep seatId and optional price snapshot.
 */
public class Ticket {
    private int id;
    private int bookingId;
    private int seatId;
    private int showtimeId;
    private BigDecimal price;    // price at purchase time

    public Ticket() {}

    public Ticket(int id, int bookingId, int seatId, int showtimeId, BigDecimal price) {
        this.id = id;
        this.bookingId = bookingId;
        this.seatId = seatId;
        this.showtimeId = showtimeId;
        this.price = price;
    }

    // --- getters / setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getSeatId() { return seatId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }


    @Override
    public String toString() {
        return "Ticket{" + "id=" + id + ", bookingId=" + bookingId + ", seatId=" + seatId + ", price=" + price + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
