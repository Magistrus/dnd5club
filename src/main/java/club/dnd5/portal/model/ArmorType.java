package club.dnd5.portal.model;

import java.util.EnumSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArmorType {
	NATURAL("природный доспех"), // 0
	LEATHER("кожанный доспех"), // 1
	HIDE("шкурный доспех"), // 2
	CHAINMAIL("кольчужная рубаха"), //3 
	RING_MAIL("колечный доспех"),
	SCRAPPY("лоскутный доспех"), // 4
	SCALED("чешуйчатый доспех"), //5 
	RIVETED_LEATHER("проклёпаная кожа"), //7
	CHAIN_MAIL("кольчуга"), // 8
	BREASTPLATE("нагрудник"), // 9
	HALF_PLATE("полулаты"), // 10
	PLATE("латный доспех"), // 11
	
	PLATE_HALF("пластинчатый доспех"), // 12 
	CUIRASS("кираса"), //13
	
	BONECRAFT("костяной доспех"), //14
	MAGE_ARMOR("с доспехами мага"), //15
	
	SHIELD("щит"), // 16
	SCRAPS("обломки доспеха"),
	SPLINT("наборный доспех"),
	; // 17;

	private String cyrillicName;

	public static Set<ArmorType> getCreatures() {
		return EnumSet.of(NATURAL, LEATHER, RIVETED_LEATHER, HIDE, CHAINMAIL, SCALED, CUIRASS, BREASTPLATE, HALF_PLATE, RING_MAIL, CHAIN_MAIL, PLATE_HALF, PLATE);
	}
	
	public String getPlutoniumTypeName() {
		switch (this) {
		case NATURAL:
			return "natural armor";
		case LEATHER:
			return "{@item leather armor|phb}";
		case HIDE:
			return "{@item hide armor|phb}";
		case SCRAPPY:
			return "patchwork armor";
		case SCRAPS:
			return "armor scraps";
		case RIVETED_LEATHER:
			return "{@item studded leather armor|phb}";
		case CHAINMAIL:
			return "{@item chain shirt|phb}";
		case CHAIN_MAIL:
			return "{@item chain mail|phb}";
		case RING_MAIL:
			return "{@item ring mail|phb}";
		case BREASTPLATE:
			return "{@item breastplate|phb}";
		case SCALED:
			return "{@item chain shirt|phb}";
		case HALF_PLATE:
			return "{@item half plate armor|phb}";
		case SPLINT:
			return "{@item splint armor|phb}";
		case PLATE:
			return "{@item plate armor|phb}";
		case SHIELD:
			return "{@item shield|phb}";
		
		default:
			return "";
		}
	}
}