package example.listener;

import example.dao.impl.BookingDAO;
import example.util.Constant;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class BookingCleanupListener implements ServletContextListener {

	private ScheduledExecutorService scheduler;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("--- SYSTEM: Bắt đầu tiến trình dọn dẹp Booking quá hạn ---");

		BookingDAO bookingDAO = new BookingDAO();
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(() -> {
			try {
				bookingDAO.cancelExpiredPendingBookings(Constant.BOOKING_TIMEOUT_MINUTES);
			} catch (Exception e) {
				System.err.println("Lỗi trong quá trình dọn dẹp booking: " + e.getMessage());
			}
		}, 0, 30, TimeUnit.SECONDS);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (scheduler != null) {
			scheduler.shutdownNow();
		}
		System.out.println("--- SYSTEM: Đã dừng tiến trình dọn dẹp Booking ---");
	}
}