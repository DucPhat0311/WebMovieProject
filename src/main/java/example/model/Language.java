package example.model;

public class Language {
	private int languageId;
    private String name;
    
    public Language() {}

	public Language(int languageId, String name) {
		super();
		this.languageId = languageId;
		this.name = name;
	}
	
	public Language(String name) {
		super();
		this.name = name;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
    
    
}
