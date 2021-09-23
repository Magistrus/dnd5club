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
		ARCANE_SHOT("Магические выстрелы: Мистический Лучник (Воин)", "МВ"),
		MANEUVER("Маневры: Мастер боевых искуств (Воин)", "МА"),
		METAMAGIC("Метамагия: Чародей", "ММ"),
		ELDRITCH_INVOCATION("Таинственные воззвания: Колдун","ТВ"),
		FIGHTING_STYLE("Боевые стили: Воин", "БС"),
		ELEMENTAL_DISCIPLINE("Стихийные практики: Путь четырех стихий (Монах)", "СП"),
		ARTIFICER_INFUSION("Инфузии: Изобретатель","И"),
		RUNE("Руны: Рунический рыцарь", "Р"),
		BONE("Договоры: Колдун", "ДК"),
		FIGHTING_STYLE_RANGER("Боевые стили: Следопыт", "Б"), 
		FIGHTING_STYLE_PALADIN("Боевые стили: Паладин", "БС"),
		FIGHTING_STYLE_BARD("Боевые стили: Колллегия Мечей, Бард", "БС"),
		FIGHTING_STYLE_BLOODHANTER("Боевые стили: Кровавый охотник", "БС"),
		BLOOD_CURSE("Проклятья крови: Кровавый Охотник", "ПК"),
		MUTAGEN("Мутагены: Ордена мутантов, (Кровавый Охотник)", "МУ"),
		WILD_SHAPE("Формы Дикого Облика: Друид", "ДО"), 
		PHILOSOPHICAL_SCHOOL("Философские школы: Философ Академии, Волшебник","ФШ");

		private String name;
		private String shortName;
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