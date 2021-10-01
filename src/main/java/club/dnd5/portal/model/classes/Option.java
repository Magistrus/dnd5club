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
		ARCANE_SHOT("Магические выстрелы: Мистический Лучник", "Fighter"),
		MANEUVER("Маневры: Мастер боевых искуств", "Fighter"),
		METAMAGIC("Метамагия", "Sorcerer"),
		ELDRITCH_INVOCATION("Таинственные воззвания","Warlock"),
		FIGHTING_STYLE("Боевые стили", "Fighter"),
		ELEMENTAL_DISCIPLINE("Стихийные практики: Путь четырех стихий", "Monk"),
		ARTIFICER_INFUSION("Инфузии","Artificer"),
		RUNE("Руны: Рунический рыцарь", "Fighter"),
		BONE("Договоры", "Warlock"),
		FIGHTING_STYLE_RANGER("Боевые стили", "Ranger"), 
		FIGHTING_STYLE_PALADIN("Боевые стили", "Paladin"),
		FIGHTING_STYLE_BARD("Боевые стили: Колллегия Мечей", "Bard"),
		FIGHTING_STYLE_BLOODHANTER("Боевые стили", "Blood Hunter"),
		BLOOD_CURSE("Проклятья крови", "Blood Hunter"),
		MUTAGEN("Мутагены: Ордена мутантов", "Blood Hunter"),
		WILD_SHAPE("Формы Дикого Облика", "Druid"), 
		PHILOSOPHICAL_SCHOOL("Философские школы: Философ Академии","Wizard");

		private String name;
		private String className;
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