package example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Corresponds to table bookings.
 * Use LocalDateTime for bookingDate and BigDecimal for money.
 */
public class Booking {
    private int id;
    private int userId;
    private User user;               // optional
    private int showtimeId;
    private Showtime showtime;       // optional
    private LocalDateTime bookingDate;
    private List<Ticket> tickets = new ArrayList<>();
    private BigDecimal totalAmount;  // stored snapshot, can be computed

    public Booking() {}

    public Booking(int id, int userId, int showtimeId, LocalDateTime bookingDate) {
        this.id = id;
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.bookingDate = bookingDate;
    }

    // --- getters / setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }

    public Showtime getShowtime() { return showtime; }
    public void setShowtime(Showtime showtime) { this.showtime = showtime; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public List<Ticket> getTickets() { return tickets; }
    public void setTickets(List<Ticket> tickets) { this.tickets = tickets; }

    public BigDecimal getTotalAmount() {
        return calculateTotalAmount();

    }

    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    /**
     * Calculate total using showtime.price * number of tickets.
     * If ticket price differs per seat, use ticket.getPrice sum instead.
     */
    public BigDecimal calculateTotalAmount() {
        if (tickets == null || tickets.isEmpty()) return BigDecimal.ZERO;
        // if showtime price available and uniform pricing:
        if (showtime != null && showtime.getPrice() != null) {
            return showtime.getPrice().multiply(new BigDecimal(tickets.size()));
        }
        // else sum ticket.price
        BigDecimal sum = BigDecimal.ZERO;
        for (Ticket t : tickets) {
            if (t.getPrice() != null) sum = sum.add(t.getPrice());
        }
        return sum;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : userId) +
                ", showtime=" + (showtime != null ? showtime.getId() : showtimeId) +
                ", bookingDate=" + bookingDate +
                ", totalAmount=" + getTotalAmount() +
                ", tickets=" + tickets.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        Booking booking = (Booking) o;
        return id == booking.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
