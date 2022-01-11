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
	BREASTPLATE("нагрудник"),
	HALF_PLATE("полулаты"), 
	PLATE("латный доспех"), // 11
	
	PLATE_HALF("пластинчатый доспех"),
	CUIRASS("кираса"),
	
	BONECRAFT("костяной доспех"),
	MAGE_ARMOR("с доспехами мага"),
	
	SHIELD("доспех (щит)"); //9

	private String cyrillicName;

	public static Set<ArmorType> getCreatures() {
		return EnumSet.of(LEATHER, RIVETED_LEATHER, HIDE, CHAINMAIL, SCALED, CUIRASS, BREASTPLATE, HALF_PLATE, RING_MAIL, CHAIN_MAIL, PLATE_HALF, PLATE);
	}
}