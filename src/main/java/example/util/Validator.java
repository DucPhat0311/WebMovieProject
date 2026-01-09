package example.util;

import java.util.regex.Pattern;

public class Validator {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

	private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9\\s\\-\\(\\)\\+]{10,15}$");

	private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L} .'-]{2,50}$");

	private static final Pattern PASSWORD_PATTERN = Pattern.compile("^.{6,100}$");

	public static String hashPassword(String plainPassword) {
		return org.mindrot.jbcrypt.BCrypt.hashpw(plainPassword, org.mindrot.jbcrypt.BCrypt.gensalt(10));
	}

	public static boolean checkPassword(String plainPassword, String hashedPassword) {
		if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
			return false;
		}
		return org.mindrot.jbcrypt.BCrypt.checkpw(plainPassword, hashedPassword);
	}

	public static boolean isValidEmail(String email) {
		if (email == null || email.trim().isEmpty()) {
			return false;
		}
		String trimmedEmail = email.trim();

		return trimmedEmail.contains("@") && trimmedEmail.indexOf('@') > 0
				&& trimmedEmail.indexOf('@') < trimmedEmail.length() - 1;
	}

	public static boolean isValidName(String name) {
		return name != null && NAME_PATTERN.matcher(name).matches();
	}

	public static boolean isValidPhone(String phone) {
		if (phone == null || phone.trim().isEmpty()) {
			return true;
		}

		String cleanedPhone = phone.trim().replace(" ", "").replace("-", "").replace("(", "").replace(")", "")
				.replace("+", "").replace(".", "");

		if (cleanedPhone.matches("[0-9]{9,12}")) {
			return true;
		}

		return cleanedPhone.matches("[0-9]+");
	}

	public static boolean isValidPassword(String password) {
		return password != null && PASSWORD_PATTERN.matcher(password).matches();
	}

	public static String escapeHtml(String input) {
		if (input == null)
			return null;

		return input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")
				.replace("'", "&#39;");
	}

	public static String sanitize(String input) {
		if (input == null)
			return null;
		return escapeHtml(input.trim());
	}

	public static boolean isPasswordMatch(String password, String confirmPassword) {
		if (password == null || confirmPassword == null)
			return false;
		return password.equals(confirmPassword);
	}

	public static boolean isVerySimpleEmail(String email) {
		if (email == null || email.trim().isEmpty()) {
			return false;
		}
		return email.contains("@");
	}
}