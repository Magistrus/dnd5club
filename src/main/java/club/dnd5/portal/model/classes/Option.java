package club.dnd5.portal.model.classes;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import club.dnd5.portal.model.book.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "options")
public class Option {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String englishName;
	
	@ElementCollection(targetClass = OptionType.class)
	@JoinTable(name = "option_types", joinColumns = @JoinColumn(name = "option_id"))
	@Column(name = "option_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private List<OptionType> optionTypes;
	
	private String prerequisite;
	private Integer level;

	@Column(columnDefinition = "TEXT")
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "source")
	private Book book;
	private Short page;

	@AllArgsConstructor
	@Getter
	public enum OptionType {
		WILD_SHAPE("Формы Дикого Облика: Друид", "Druid", false), 
		ARCANE_SHOT("Магические выстрелы: Воин Мистический Лучник", "Fighter", "Arcane_Archer", false),
		METAMAGIC("Метамагия: Чародей", "Sorcerer", false),
		ELDRITCH_INVOCATION("Воззвания: Колдун","Warlock", false),
		BONE("Договоры: Колдун", "Warlock", false),
		ELEMENTAL_DISCIPLINE("Стихийные практики: Монах Пути четырех стихий", "Monk", "Four_Elements", false),
		ARTIFICER_INFUSION("Инфузии: Изобретатель","Artificer", false),
		RUNE("Руны: Рунический рыцарь", "Fighter", "Rune", false),
		MANEUVER("Маневры: Воин Мастер боевых искуств", "Fighter","Battle_Master", false),
		FIGHTING_STYLE("Боевые стили: Воин", "Fighter", false),
		FIGHTING_STYLE_RANGER("Боевые стили: Следопыт", "Ranger", false), 
		FIGHTING_STYLE_PALADIN("Боевые стили: Паладин", "Paladin", false),
		FIGHTING_STYLE_BARD("Боевые стили: Бард Колллегии Мечей", "Bard", "Swords", false),
		FIGHTING_STYLE_BLOODHANTER("Боевые стили: Кровавый охотник", "Blood Hunter", true),
		BLOOD_CURSE("Проклятья крови: Кровавый охотник", "Blood Hunter", true),
		MUTAGEN("Мутагены: Кровавый охотник Ордена мутантов", "Blood Hunter", true),
		PHILOSOPHICAL_SCHOOL("Философские школы: Волшебник Философ Академии","Wizard", true);
		
		private String name;
		private String className;
		private String arhetypeName;
		private boolean homebrew;
		
		OptionType(String name, String className, boolean homebrew){
			this.name = name;
			this.className = className;
			this.homebrew = homebrew;
		}
		
		public static OptionType parse(String type) {
			return Arrays.asList(values()).stream()
					.filter(t -> t.name.equals(type))
					.findFirst()
					.orElseThrow(IllegalArgumentException::new);
		}

		public String getDisplayName() {
			return name.substring(0, name.indexOf(":"));
		}
	}
}