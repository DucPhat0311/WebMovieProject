package example.util;

import org.mindrot.jbcrypt.BCrypt;

public class SecurityUtil {
	// 1. Hàm dùng khi ĐĂNG KÝ (Tạo hash từ mật khẩu user nhập)
	public static String hashPassword(String plainPassword) {
		// Gensalt(12) là độ khó, số càng lớn càng an toàn nhưng chạy càng chậm
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
	}

	// 2. Hàm dùng khi ĐĂNG NHẬP (Kiểm tra mật khẩu)
	// plainPassword: Mật khẩu user nhập (ví dụ: 123456)
	// hashedPassword: Mật khẩu loằng ngoằng lấy từ Database
	public static boolean checkPassword(String plainPassword, String hashedPassword) {
		if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
			// Nếu hash trong DB không đúng định dạng BCrypt
			return false;
		}
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}
}
