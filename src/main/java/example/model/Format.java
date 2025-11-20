package example.model;

public enum Format {
	_2D_SUB("2D Phụ đề"), _2D_DUB("2D Lồng tiếng"), _3D_SUB("3D Phụ đề"), _3D_DUB("3D Lồng tiếng");

	private final String displayName;

	Format(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
