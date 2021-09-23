package club.dnd5.portal.model.background;

public enum LifeStyle {
	ANYTHING("Никудышное"),
	BEGGARLY("Нищенское"),
	POOR("Бедное"),
	MODEST("Скромное"),
	COMFORTABLE("Комфортное"),
	RICH("Богатое"),
	ARISTOCRATIC("Аристократическое");
	
	private String cyrilicName;
	
	LifeStyle(String cyrilicName){
		this.cyrilicName = cyrilicName;
	}
	
	public String getCyrilicName() {
		return cyrilicName;
	}
}