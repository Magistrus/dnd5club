package club.dnd5.portal.dto.api.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.spell.SpellApi;
import club.dnd5.portal.model.items.MagicItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class MagicItemApi extends ItemApi {
	private String rarity;
	private Integer cost;
	private SpellApi spell;
	
	public MagicItemApi(MagicItem item) {
		super(item);
		type = new TypeApi(item.getType().getCyrilicName(), item.getType().ordinal());
		rarity = item.getRarity().getCyrilicName();
	}
}