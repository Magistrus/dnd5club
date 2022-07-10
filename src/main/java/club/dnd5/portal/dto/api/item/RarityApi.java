package club.dnd5.portal.dto.api.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import club.dnd5.portal.model.items.Rarity;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RarityApi {
	private String type;
	@JsonProperty("short")
	private String shortName;
	private String name;
	
	public RarityApi(Rarity rarity) {
		type = rarity.name().toLowerCase().replace('_', '-');
		shortName = rarity.getShort();
		name = rarity.getCyrilicName();
	}
}