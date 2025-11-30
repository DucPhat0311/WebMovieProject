package example.model;

public enum Format {
	_2D_SUB("2D Phụ đề"), _2D_DUB("2D Lồng tiếng"), _3D_SUB("3D Phụ đề"), _3D_DUB("3D Lồng tiếng"), _2D("2D"),
	_3D("3D"), IMAX("IMAX");

	private final String displayName;

	Format(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static Format fromCode(String code) {
		for (Format format : values()) {
			if (format.name().equals(code)) {
				return format;
			}
		}
		return _2D_SUB; // default
	}
}