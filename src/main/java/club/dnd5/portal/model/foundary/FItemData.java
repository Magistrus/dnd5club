package club.dnd5.portal.model.foundary;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import club.dnd5.portal.model.ArmorType;
import club.dnd5.portal.model.creature.Action;
import club.dnd5.portal.model.creature.CreatureFeat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FItemData {
	private String name;
	private FDiscription description;
	private String source = "";
	private int quantity = 1;
	private float weight;
	private float price;
	private boolean attuned;
	private boolean equipped = true;
	private String rarity;
	private boolean  identified = true;
	private FActivation activation;
	private FDuration duration;
	private FTarget target;
	private FRange range;
	private FUses uses;
	private FConsume consume;
	private String ability = "";
	private String actionType= ""; 
    private byte attackBonus;
    private String chatFlavor = "";
    private String critical;
    private FItemDamage damage;
    private FCharge recharge;
    private String formula = "";
    private FSave save;
    private FArmor armor;
    private FIHP hp;
    private String weaponType = "natural";
    private FWeaponProperties properties;
    private boolean proficient = true;
    private String cptooltipmode = "hide";

	FItemData(CreatureFeat feat) {
		name = feat.getName();
		description = new FDiscription(feat.getDescription().replace("/hero", "http://dnd5.club/hero"));
		activation = new FActivation();
		duration = new FDuration();
		target = new FTarget();
		range = new FRange();
		uses = new FUses();
		consume = new FConsume();
		save = new FSave();
		armor = new FArmor();
		hp = new FIHP();
		properties = new FWeaponProperties();
	}
	
	public FItemData(Action action) {
		name = action.getName();
		if (name.contains("перезарядка")) {
			int value = 4;
			if (name.contains("5")) {
				value = 5;
			} else if (name.contains("6")) {
				value = 5;
			}
			recharge = new FCharge();
			recharge.setValue(value);
		}
		description = new FDiscription(action.getDescription().replace("/hero", "http://dnd5.club/hero"));
		activation = new FActivation(action.getActionType().name().toLowerCase(), (byte) 1, "");
		duration = new FDuration();
		target = new FTarget();
		range = new FRange();
		uses = new FUses();
		consume = new FConsume();
		if (action.getDescription().contains("Рукопашная атака оружием:")) {
			actionType = "mwak"; 
		} else if (action.getDescription().contains("Дальнобойная атака оружием:")) {
			actionType = "rwak";	
		} else if (action.getDescription().contains("спасброс")) {
			actionType = "save";
			save = new FSave();
			if (action.getDescription().contains("Силы")) {
				save.setAbility("str");
			} else if (action.getDescription().contains("Ловкости")) {
				save.setAbility("dex");
			} else if (action.getDescription().contains("Телосложения")) {
				save.setAbility("con");
			} else if (action.getDescription().contains("Мудрости")) {
				save.setAbility("wiz");
			} else if (action.getDescription().contains("Интеллекта")) {
				save.setAbility("int");
			} else if (action.getDescription().contains("Харизмы")) {
				save.setAbility("cha");
			}
			Pattern dcMatcher = 
					Pattern.compile("(Сл|Сложностью)\\s\\d+");
			Matcher matcher = dcMatcher.matcher(action.getDescription());
			if (matcher.find()) {
				String dc = matcher.group();
				dc = dc.replaceAll("\\D+", "");
				save.setDc(Integer.parseInt(dc.trim()));
				save.setScaling("flat");
			}
		}
		
		armor = new FArmor();
		damage = new FItemDamage();
		
		Queue<String> damageTypes = new LinkedList<>();
		Pattern patternDamageTypes = 
				Pattern.compile("колющий урон|рубящий урон|дробящий урон|урон ядом|урон электричеством|урон кислотой|урон огнём");
		Matcher matcher = patternDamageTypes.matcher(action.getDescription().toLowerCase());
		while (matcher.find()) {
			String damageType = matcher.group();
			switch (damageType) {
			case "колющий урон":
				damageTypes.add("piercing");
				break;
			case "рубящий урон":
				damageTypes.add("slashing");
				break;
			case "дробящий урон":
				damageTypes.add("bludgeoning");
				break;
			case "урон ядом":
				damageTypes.add("poison");
				break;
			case "урон электричеством":
				damageTypes.add("lightning");
				break;
			case "урон кислотой":
				damageTypes.add("acid");
				break;
			case "урон огнём":
				damageTypes.add("fire");
				break;
				
			default:
				break;
			}
		}

		Pattern patternDamageFormula = Pattern.compile("\\d+(к|d)\\d+(\\s\\+\\s\\d+){0,}");
		matcher = patternDamageFormula.matcher(action.getDescription());
		while (matcher.find()) {
			String damageFormula = matcher.group().replace("к", "d");
			if (damage.getParts().isEmpty()) {
				damage.addDamage(damageFormula, damageTypes.remove());
			} else {
				
				damage.addDamage(damageFormula, damageTypes.remove());
			}
		}
		hp = new FIHP();
		properties = new FWeaponProperties();
	}

	public FItemData(ArmorType armorType) {
		description = new FDiscription(armorType.getCyrillicName());
		activation = new FActivation();
		duration = new FDuration();
		target = new FTarget();
		range = new FRange();
		uses = new FUses();
		consume = new FConsume();
		save = new FSave();
		armor = new FArmor(armorType.getArmorClass(), armorType.getArmorType(), armorType.getArmorDexBonus());
		hp = new FIHP();
		properties = new FWeaponProperties();
	}
}