package club.dnd5.portal.dto;

import java.util.stream.Collectors;

import club.dnd5.portal.model.god.Domain;
import club.dnd5.portal.model.god.God;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GodDto {
	private int id;
	private String name;
	private String englishName;
	private String aligmentShort;
	private String alignment;
	private String commitment;
	private String sex;
	private String symbol;
	private String nicknames;
	private String domains;
	private String pantheon;
	private String rank;
	private String book = "";
	private String bookshort = "";
	
	public GodDto(God god) {
		id = god.getId();
		name = god.getName();
		englishName = god.getEnglishName() == null ? "" : god.getEnglishName();
		commitment = god.getPrefixName() + " " + god.getCommitment();
		sex = god.getSex().getCyrilicName();
		aligmentShort = god.getAligment().getShortName();
		alignment = god.getAligment().getCyrilicName();
		nicknames = god.getNicknames();
		symbol = god.getSymbol();
		domains = god.getDomains().stream().map(Domain::getCyrilicName).collect(Collectors.joining(", "));
		pantheon = god.getPantheon().getName();
		rank = god.getRank().getName(god.getSex());
		if (god.getBook()!=null) {
			book = god.getBook().getName();
			bookshort = god.getBook().getSource();
		}
	}
}