package club.dnd5.portal.dto.api.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.spell.SpellApi;
import club.dnd5.portal.model.items.MagicItem;
import club.dnd5.portal.model.items.Rarity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class MagicItemApi extends ItemApi {
	private RarityApi rarity;
	private SpellApi spell;
	
	public MagicItemApi(MagicItem item) {
		super(item);
		type = new TypeApi(item.getType().getCyrilicName(), item.getType().ordinal());
		rarity = new RarityApi(item.getRarity().name().toLowerCase().replace('_', '-'), item.getRarity().getShort(), item.getTextRarity());
	}
	
	public void setRarity(Rarity rarity) {
		this.rarity = new RarityApi(rarity);
	}
}