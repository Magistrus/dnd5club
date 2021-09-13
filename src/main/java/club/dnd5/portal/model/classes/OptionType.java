package club.dnd5.portal.model.classes;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OptionType {
	ARCANE_SHOT("Магические выстрелы: Мистический Лучник (Воин)", "МВ"),
	MANEUVER("Маневры: Мастер боевых искуств (Воин)", "М МБИ"),
	METAMAGIC("Метамагия: Чародей", "ММ"),
	ELDRITCH_INVOCATION("Таинственные воззвания: Колдун","ТВ"),
	FIGHTING_STYLE("Боевые стили: Воин", "БС В"),
	ELEMENTAL_DISCIPLINE("Стихийные практики: Путь четырех стихий (Монах)", "СП ПЧС"),
	ARTIFICER_INFUSION("Инфузии: Изобретатель","И И"),
	RUNE("Руны: Рунический рыцарь", "РР, Р"),
	BONE("Договоры: Колдун", "Д К"),
	FIGHTING_STYLE_RANGER("Боевые стили: Следопыт", "БС С"), 
	FIGHTING_STYLE_PALADIN("Боевые стили: Паладин", "БС П"),
	FIGHTING_STYLE_BARD("Боевые стили: Колллегия Мечей, Бард", "БС Б"),
	FIGHTING_STYLE_BLOODHANTER("Боевые стили: Кровавый охотник", "БС КО"),
	BLOOD_CURSE("Проклятья крови: Кровавый Охотник", "ПК"),
	MUTAGEN("Мутагены: Ордена мутантов, (Кровавый Охотник)", "М ОМ"),
	WILD_SHAPE("Формы Дикого Облика: Друид", "ДО"), 
	PHILOSOPHICAL_SCHOOL("Философские школы: Философ Академии, Волшебник","ФШ ФА");

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
