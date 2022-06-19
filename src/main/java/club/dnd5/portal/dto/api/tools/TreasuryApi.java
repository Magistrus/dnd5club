package club.dnd5.portal.dto.api.tools;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.item.ItemApi;
import club.dnd5.portal.dto.api.item.MagicItemApi;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class TreasuryApi {
	private CoinsApi coins;
	private Collection<MagicItemApi> magicItems;
	private Collection<ItemApi> gems;
	private Collection<ItemApi> arts;
}