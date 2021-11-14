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
		ARCANE_SHOT("Магические выстрелы: Воин Мистический Лучник", "Fighter", "Arcane_Archer"),
		MANEUVER("Маневры: Воин Мастер боевых искуств", "Fighter","Battle_Master"),
		METAMAGIC("Метамагия: Чародей", "Sorcerer"),
		ELDRITCH_INVOCATION("Таинственные воззвания: Колдун","Warlock"),
		FIGHTING_STYLE("Боевые стили: Воин", "Fighter"),
		ELEMENTAL_DISCIPLINE("Стихийные практики: Монах Пути четырех стихий", "Monk", "Four_Elements"),
		ARTIFICER_INFUSION("Инфузии: Изобретатель","Artificer"),
		RUNE("Руны: Рунический рыцарь", "Fighter", "Rune"),
		BONE("Договор: Колдун", "Warlock"),
		FIGHTING_STYLE_RANGER("Боевой стиль: Следопыт", "Ranger"), 
		FIGHTING_STYLE_PALADIN("Боевой стиль: Паладин", "Paladin"),
		FIGHTING_STYLE_BARD("Боевой стиль: Бард Колллегии Мечей", "Bard", "Swords"),
		FIGHTING_STYLE_BLOODHANTER("Боевой стиль: Кровавый охотник", "Blood Hunter"),
		BLOOD_CURSE("Проклятья крови: Кровавый охотник", "Blood Hunter"),
		MUTAGEN("Мутаген: Кровавый охотник Ордена мутантов", "Blood Hunter"),
		WILD_SHAPE("Форма Дикого Облика: Друид", "Druid"), 
		PHILOSOPHICAL_SCHOOL("Философская школа: Волшебник Философ Академии","Wizard");
		
		private String name;
		private String className;
		private String arhetypeName;
		
		OptionType(String name, String className){
			this.name = name;
			this.className = className;
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