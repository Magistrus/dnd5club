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
	
	public byte getArmorClass() {
		switch (this) {
		case LEATHER:
			return 11;
		case HIDE:
			return 12;
		case SCRAPPY:
			return 11;
		case SCRAPS:
			return 12;
		case RIVETED_LEATHER:
			return 12;
		case CHAINMAIL:
			return 13;
		case CHAIN_MAIL:
			return 16;
		case RING_MAIL:
			return 14;
		case BREASTPLATE:
			return 15;
		case SCALED:
			return 14;
		case HALF_PLATE:
			return 15;
		case SPLINT:
			return 17;
		case PLATE:
			return 18;
		case SHIELD:
			return 2;
		default:
			return 0;
		}
	}
	public String getArmorType() {
		switch (this) {
		case LEATHER:
		case RIVETED_LEATHER:
		case SCRAPPY:
		case SCRAPS:
			return "light";
		case HIDE:
		case CHAINMAIL:
		case SCALED:
		case BREASTPLATE:
		case HALF_PLATE:
		case CUIRASS:
			return "medium";
		case PLATE:
		case CHAIN_MAIL:
		case RING_MAIL:
		case PLATE_HALF:
		case SPLINT:
			return "heavy";
		case SHIELD:
			return "shield";
		default:
			return null;
		}
	}

	public Byte getArmorDexBonus() {
		switch (this) {
		case HIDE:
		case CHAINMAIL:
		case SCALED:
		case BREASTPLATE:
		case HALF_PLATE:
		case CUIRASS:
			return 2;
		case PLATE:
		case CHAIN_MAIL:
		case RING_MAIL:
		case PLATE_HALF:
		case SPLINT:
			return 0;
		default:
			return null;
		}
	}

	public String getFvttIcon() {
		switch (this) {
		case LEATHER:
			return "icons/equipment/chest/breastplate-quilted-brown.webp";
		case HIDE:
			return "icons/equipment/chest/breastplate-scale-leather.webp";
		case SCRAPPY:
		case SCRAPS:
			return "icons/equipment/chest/breastplate-helmet-metal.webp";
		case RIVETED_LEATHER:
			return "icons/equipment/chest/breastplate-rivited-red.webp";
		case CHAINMAIL:
			return "icons/equipment/chest/breastplate-metal-scaled-grey.webp";
		case CHAIN_MAIL:
			return "icons/equipment/chest/breastplate-metal-scaled-grey.webp";
		case RING_MAIL:
			return "icons/equipment/chest/breastplate-metal-scaled-grey.webp";
		case BREASTPLATE:
		case CUIRASS:
			return "icons/equipment/chest/breastplate-layered-steel-black.webp";
		case SCALED:
			return "icons/equipment/chest/breastplate-metal-scaled-grey.webp";
		case HALF_PLATE:
			return "icons/equipment/chest/breastplate-metal-scaled-grey.webp";
		case SPLINT:
			return "icons/equipment/chest/breastplate-layered-steel.webp";
		case PLATE:
			return "icons/equipment/chest/breastplate-cuirass-steel-grey.webp";
		case SHIELD:
			return "icons/equipment/shield/oval-wooden-boss-steel.webp";
			default:
			return "";
		}
	}
}