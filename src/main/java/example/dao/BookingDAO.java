package example.dao;

public class BookingDAO {
// sử dụng transaction trong method để kiểm tra double booking (đặt trùng ghế)
	
	// bookingId 01 có ghế A01 suất chiếu 9h trùng với bookingId 02 có ghế A01 suất chiếu 9h
	
	// khi người dùng nhấn nút thanh toán thì nếu bị trùng sẽ báo "Ghế đã có người đặt"
	
	// ví dụ một người vào trang tìm seat lúc 9h và chọn seat A01 nhưng chưa thanh toán
	
	// tới 9h5p có người khác vào trang tìm seat thì vẫn thấy seat A01 trống do người kia vẫn chưa thanh toán
	
	// ==> seat A01 bị ai chiếm phụ thuộc vào ai thanh toán trước
	
	// ==> người thanh toán muộn sẽ thấy thông báo "ghế đã có người đặt"
	
	public boolean insertBooking() {
	   return false;
	}
}
