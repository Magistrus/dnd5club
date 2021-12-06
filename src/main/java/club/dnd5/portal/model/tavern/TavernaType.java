package club.dnd5.portal.model.tavern;

import java.util.Random;

public enum TavernaType {
	BEER("Пивная", "Кабак", "Бар", "Паб", "Дом эля", "Пивоварня"), 
	INN("Постоялый двор", "Трактир", "Таверна", "Логово"), 
	HOTEL("Гостиница", "Отель", "Дом", "Гостинный двор");
	
	private final Random rnd = new Random();

	private String[] names;
	TavernaType(String... names){
		this.names = names;
	}
	public String getName() {
		return names[rnd.nextInt(names.length)];
	}
	public String getNames() {
		return String.join(", ", names);
	}
}