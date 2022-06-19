package club.dnd5.portal.controller.api.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.api.item.ItemApi;
import club.dnd5.portal.dto.api.item.MagicItemApi;
import club.dnd5.portal.dto.api.spell.SpellApi;
import club.dnd5.portal.dto.api.tools.CoinsApi;
import club.dnd5.portal.dto.api.tools.RequestTreasuryApi;
import club.dnd5.portal.dto.api.tools.TreasuryApi;
import club.dnd5.portal.model.Alignment;
import club.dnd5.portal.model.Dice;
import club.dnd5.portal.model.book.TypeBook;
import club.dnd5.portal.model.items.MagicThingTable;
import club.dnd5.portal.model.items.Rarity;
import club.dnd5.portal.model.items.Treasure;
import club.dnd5.portal.model.items.TreasureType;
import club.dnd5.portal.model.items.Weapon;
import club.dnd5.portal.model.splells.Spell;
import club.dnd5.portal.repository.datatable.ItemMagicTableRepository;
import club.dnd5.portal.repository.datatable.MagicItemDatatableRepository;
import club.dnd5.portal.repository.datatable.SpellDatatableRepository;
import club.dnd5.portal.repository.datatable.TreasureDatatableRepository;
import club.dnd5.portal.repository.datatable.WeaponDatatableRepository;

@RestController
public class TreasuryApiController {
	public static final Random rnd = new Random();
	@Autowired
	private MagicItemDatatableRepository magicItemRepo;
	@Autowired
	private SpellDatatableRepository spellRepo;
	@Autowired
	private WeaponDatatableRepository weaponRepo;
	@Autowired
	private ItemMagicTableRepository mtRepo;
	@Autowired
	private TreasureDatatableRepository treasureRepo;

	@PostMapping("/api/v1/tools/treasury")
	public TreasuryApi getItems(@RequestBody RequestTreasuryApi reques) {
		TreasuryApi treasuryApi = new TreasuryApi();
		if (reques.getCr() == null) {
			reques.setCr(Dice.d4.roll());
		}
		if (reques.getUnique() == null) {
			reques.setUnique(Boolean.FALSE);
		}
		if (reques.getCoins() != null && reques.getCoins()) {
			CoinsApi coins = new CoinsApi();
			treasuryApi.setCoins(coins);
			switch (reques.getCr()) {
			case 1:
				coins.setCopper(Dice.d6.roll(6) * 100);
				coins.setSilver(Dice.d6.roll(3) * 100);
				coins.setGold(Dice.d6.roll(2) * 10);
				break;
			case 2:
				coins.setCopper(Dice.d6.roll(2) * 100);
				coins.setSilver(Dice.d6.roll(2) * 1000);
				coins.setGold(Dice.d6.roll(6) * 100);
				coins.setPlatinum(Dice.d6.roll(3) * 10);
				break;
			case 3:
				coins.setGold(Dice.d6.roll(4) * 1000);
				coins.setPlatinum(Dice.d6.roll(5) * 100);
				break;
			case 4:
				coins.setGold(Dice.d6.roll(12) * 1000);
				coins.setPlatinum(Dice.d6.roll(8) * 1000);
				break;
			default:
				break;
			}
		}
		if (reques.getMagicItem() != null && reques.getMagicItem()) {
			List<MagicItemApi> magicItems = new ArrayList<>();
			int ri = Dice.d100.roll();
			switch (reques.getCr()) {
			case 1:
				if (ri >= 37 && ri <= 60) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "А", 6, reques.getUnique()));
				} else if (ri >= 61 && ri <= 75) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Б", 4, reques.getUnique()));
				} else if (ri >= 76 && ri <= 85) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "В", 4, reques.getUnique()));
				} else if (ri >= 86 && ri <= 97) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Е", 4, reques.getUnique()));
				} else if (ri >= 98 && ri <= 100) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Е1", 1, reques.getUnique()));
				}
				break;
			case 2:
				if (ri >= 29 && ri <= 44) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "А", 6, reques.getUnique()));
				} else if (ri >= 45 && ri <= 63) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Б", 4, reques.getUnique()));
				} else if (ri >= 64 && ri <= 74) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "В", 4, reques.getUnique()));
				} else if (ri >= 75 && ri <= 80) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Г", 1, reques.getUnique()));
				} else if (ri >= 81 && ri <= 94) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Е", 4, reques.getUnique()));
				} else if (ri >= 95 && ri <= 98) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Е1", 4, reques.getUnique()));
				} else if (ri >= 96 && ri <= 100) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Ж", 1, reques.getUnique()));
				}
				break;
			case 3:
				if (ri >= 16 && ri <= 29) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "А", 4, reques.getUnique()));
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Б", 6, reques.getUnique()));
				} else if (ri >= 30 && ri <= 50) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "В", 6, reques.getUnique()));
				} else if (ri >= 51 && ri <= 66) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Г", 4, reques.getUnique()));
				} else if (ri >= 67 && ri <= 74) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Д", 1, reques.getUnique()));
				} else if (ri >= 75 && ri <= 82) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Е", 1, reques.getUnique()));
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Е1", 4, reques.getUnique()));
				} else if (ri >= 83 && ri <= 92) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Ж", 4, reques.getUnique()));
				} else if (ri >= 93 && ri <= 100) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "З", 1, reques.getUnique()));
				}
				break;
			case 4:
				if (ri >= 3 && ri <= 14) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "В", 8, reques.getUnique()));
				} else if (ri >= 15 && ri <= 46) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Г", 6, reques.getUnique()));
				} else if (ri >= 47 && ri <= 68) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Д", 6, reques.getUnique()));
				} else if (ri >= 69 && ri <= 72) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Е1", 6, reques.getUnique()));
				} else if (ri >= 73 && ri <= 80) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "Ж", 4, reques.getUnique()));
				} else if (ri >= 81 && ri <= 100) {
					magicItems.addAll(getMagicItems(ri, 1, 1000, "З", 4, reques.getUnique()));
				}
				break;
			default:
				break;
			}
			treasuryApi.setMagicItems(magicItems);
		}
		List<ItemApi> arts = new ArrayList<>();
		List<ItemApi> gems = new ArrayList<>();
		int ri = Dice.d100.roll();
		if (reques.getCr() == 1) {
			
			if (ri >= 7 && ri <= 16) {
				gems.addAll(getTreasures(10, TreasureType.GEM, 2, Dice.d6));
			} else if (ri >= 17 && ri <= 26) {
				arts.addAll(getTreasures(25, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 27 && ri <= 36) {
				gems.addAll(getTreasures(50, TreasureType.GEM, 2, Dice.d6));
			} else if (ri >= 37 && ri <= 44) {
				gems.addAll(getTreasures(10, TreasureType.GEM, 2, Dice.d6));
			} else if (ri >= 45 && ri <= 52) {
				arts.addAll(getTreasures(25, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 53 && ri <= 60) {
				gems.addAll(getTreasures(50, TreasureType.GEM, 2, Dice.d6));
			} else if (ri >= 61 && ri <= 65) {
				gems.addAll(getTreasures(10, TreasureType.GEM, 2, Dice.d6));
			} else if (ri >= 66 && ri <= 70) {
				arts.addAll(getTreasures(25, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 71 && ri <= 75) {
				gems.addAll(getTreasures(50, TreasureType.GEM, 2, Dice.d6));
			} else if (ri >= 76 && ri <= 78) {
				gems.addAll(getTreasures(10, TreasureType.GEM, 2, Dice.d6));
			} else if (ri >= 79 && ri <= 80) {
				arts.addAll(getTreasures(25, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 81 && ri <= 85) {
				gems.addAll(getTreasures(50, TreasureType.GEM, 2, Dice.d6));
			} else if (ri >= 86 && ri <= 92) {
				arts.addAll(getTreasures(25, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 93 && ri <= 97) {
				gems.addAll(getTreasures(50, TreasureType.GEM, 2, Dice.d6));
			} else if (ri >= 98 && ri <= 99) {
				arts.addAll(getTreasures(25, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri == 100) {
				gems.addAll(getTreasures(50, TreasureType.GEM, 2, Dice.d6));
			}
		} else if (reques.getCr() == 2) {
			if (ri >= 5 && ri <= 10) {
				arts.addAll(getTreasures(25, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 11 && ri <= 16) {
				gems.addAll(getTreasures(50, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 17 && ri <= 22) {
				gems.addAll(getTreasures(100, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 23 && ri <= 28) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 29 && ri <= 32) {
				arts.addAll(getTreasures(25, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 33 && ri <= 36) {
				gems.addAll(getTreasures(50, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 37 && ri <= 40) {
				gems.addAll(getTreasures(100, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 41 && ri <= 44) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 45 && ri <= 49) {
				arts.addAll(getTreasures(25, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 50 && ri <= 54) {
				gems.addAll(getTreasures(100, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 55 && ri <= 59) {
				gems.addAll(getTreasures(100, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 60 && ri <= 63) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 64 && ri <= 66) {
				arts.addAll(getTreasures(25, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 67 && ri <= 69) {
				gems.addAll(getTreasures(50, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 70 && ri <= 72) {
				gems.addAll(getTreasures(100, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 73 && ri <= 74) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 75 && ri <= 76) {
				arts.addAll(getTreasures(25, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 77 && ri <= 78) {
				gems.addAll(getTreasures(50, TreasureType.GEM, 3, Dice.d6));
			} else if (ri == 79) {
				gems.addAll(getTreasures(100, TreasureType.GEM, 3, Dice.d6));
			} else if (ri == 80) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 81 && ri <= 84) {
				arts.addAll(getTreasures(25, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 85 && ri <= 88) {
				gems.addAll(getTreasures(50, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 89 && ri <= 91) {
				gems.addAll(getTreasures(100, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 92 && ri <= 94) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 95 && ri <= 96) {
				gems.addAll(getTreasures(100, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 97 && ri <= 98) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri == 99) {
				gems.addAll(getTreasures(100, TreasureType.GEM, 3, Dice.d6));
			} else if (ri == 100) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d6));
			}
		} else if (reques.getCr() == 3) {
			if (ri >= 4 && ri <= 6) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 7 && ri <= 9) {
				arts.addAll(getTreasures(750, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 10 && ri <= 12) {
				gems.addAll(getTreasures(500, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 13 && ri <= 15) {
				gems.addAll(getTreasures(1000, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 16 && ri <= 19) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 20 && ri <= 23) {
				arts.addAll(getTreasures(750, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 24 && ri <= 26) {
				gems.addAll(getTreasures(500, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 27 && ri <= 29) {
				gems.addAll(getTreasures(1000, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 30 && ri <= 35) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 36 && ri <= 40) {
				arts.addAll(getTreasures(750, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 41 && ri <= 45) {
				gems.addAll(getTreasures(500, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 46 && ri <= 50) {
				gems.addAll(getTreasures(1000, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 51 && ri <= 54) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 55 && ri <= 58) {
				arts.addAll(getTreasures(750, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 59 && ri <= 62) {
				gems.addAll(getTreasures(500, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 63 && ri <= 66) {
				gems.addAll(getTreasures(1000, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 67 && ri <= 68) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 69 && ri <= 70) {
				arts.addAll(getTreasures(750, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 71 && ri <= 72) {
				gems.addAll(getTreasures(500, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 73 && ri <= 74) {
				gems.addAll(getTreasures(1000, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 75 && ri <= 76) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 77 && ri <= 78) {
				arts.addAll(getTreasures(750, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 79 && ri <= 80) {
				gems.addAll(getTreasures(500, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 81 && ri <= 82) {
				gems.addAll(getTreasures(1000, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 83 && ri <= 85) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 86 && ri <= 88) {
				arts.addAll(getTreasures(750, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 89 && ri <= 90) {
				gems.addAll(getTreasures(500, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 91 && ri <= 92) {
				gems.addAll(getTreasures(1000, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 93 && ri <= 94) {
				arts.addAll(getTreasures(250, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 95 && ri <= 96) {
				arts.addAll(getTreasures(750, TreasureType.WORKS_OF_ART, 2, Dice.d4));
			} else if (ri >= 97 && ri <= 98) {
				gems.addAll(getTreasures(500, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 99 && ri <= 100) {
				gems.addAll(getTreasures(1000, TreasureType.GEM, 3, Dice.d6));
			}
		} else if (reques.getCr() == 4) {
			if (ri >= 3 && ri <= 5) {
				gems.addAll(getTreasures(1000, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 6 && ri <= 8) {
				arts.addAll(getTreasures(2500, TreasureType.WORKS_OF_ART, 1, Dice.d10));
			} else if (ri >= 9 && ri <= 11) {
				arts.addAll(getTreasures(7500, TreasureType.WORKS_OF_ART, 1, Dice.d4));
			} else if (ri >= 12 && ri <= 14) {
				gems.addAll(getTreasures(5000, TreasureType.GEM, 1, Dice.d8));
			} else if (ri >= 15 && ri <= 22) {
				gems.addAll(getTreasures(1000, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 23 && ri <= 30) {
				arts.addAll(getTreasures(2500, TreasureType.WORKS_OF_ART, 1, Dice.d10));
			} else if (ri >= 31 && ri <= 38) {
				arts.addAll(getTreasures(7500, TreasureType.WORKS_OF_ART, 1, Dice.d4));
			} else if (ri >= 39 && ri <= 46) {
				gems.addAll(getTreasures(5000, TreasureType.GEM, 1, Dice.d8));
			} else if (ri >= 47 && ri <= 52) {
				gems.addAll(getTreasures(1000, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 53 && ri <= 58) {
				arts.addAll(getTreasures(2500, TreasureType.WORKS_OF_ART, 1, Dice.d10));
			} else if (ri >= 59 && ri <= 63) {
				arts.addAll(getTreasures(7500, TreasureType.WORKS_OF_ART, 1, Dice.d4));
			} else if (ri >= 64 && ri <= 68) {
				gems.addAll(getTreasures(5000, TreasureType.GEM, 1, Dice.d8));
			} else if (ri == 69) {
				gems.addAll(getTreasures(1000, TreasureType.GEM, 3, Dice.d6));
			} else if (ri == 70) {
				arts.addAll(getTreasures(2500, TreasureType.WORKS_OF_ART, 1, Dice.d10));
			} else if (ri == 71) {
				arts.addAll(getTreasures(7500, TreasureType.WORKS_OF_ART, 1, Dice.d4));
			} else if (ri == 72) {
				gems.addAll(getTreasures(5000, TreasureType.GEM, 1, Dice.d8));
			} else if (ri >= 73 && ri <= 74) {
				gems.addAll(getTreasures(1000, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 75 && ri <= 76) {
				arts.addAll(getTreasures(2500, TreasureType.WORKS_OF_ART, 1, Dice.d10));
			} else if (ri >= 77 && ri <= 78) {
				arts.addAll(getTreasures(7500, TreasureType.WORKS_OF_ART, 1, Dice.d4));
			} else if (ri >= 79 && ri <= 80) {
				gems.addAll(getTreasures(500, TreasureType.GEM, 1, Dice.d8));
			} else if (ri >= 81 && ri <= 85) {
				gems.addAll(getTreasures(1000, TreasureType.GEM, 3, Dice.d6));
			} else if (ri >= 86 && ri <= 90) {
				arts.addAll(getTreasures(2500, TreasureType.WORKS_OF_ART, 1, Dice.d10));
			} else if (ri >= 91 && ri <= 95) {
				arts.addAll(getTreasures(7500, TreasureType.WORKS_OF_ART, 1, Dice.d4));
			} else if (ri >= 96 && ri <= 100) {
				gems.addAll(getTreasures(5000, TreasureType.GEM, 1, Dice.d8));
			}
			if (reques.getArt() != null && reques.getArt()) {
				treasuryApi.setArts(arts);
			}
			if (reques.getGem() != null && reques.getGem()) {
				treasuryApi.setGems(gems);
			}
		}
		return treasuryApi;
	}

	private List<MagicItemApi> getMagicItems(Integer result, int start, int end, String tableName, int count,
			boolean unique) {
		Set<String> names = new HashSet<>();
		List<MagicItemApi> list = new ArrayList<>();
		if (result >= start) {
			for (int i = 0; i < 1 + rnd.nextInt(count); i++) {
				int ri = Dice.roll(Dice.d100);
				// System.out.println("table= " + tableName + " in " + ri);
				MagicThingTable mt = mtRepo.findOne(ri, tableName);
				if (mt != null) {
					MagicItemApi itemApi = new MagicItemApi(mt.getMagicThing());
					if (tableName.equals("Б")) {
						if (ri == 91) {
							int zap = Dice.roll(4, Dice.d4);
							itemApi.changeName("(дополнительных заплаток " + zap + ")");
						}
					}
					if (tableName.equals("В")) {
						if (ri >= 82 && ri <= 84) {
							String effect = "";
							int er = Dice.roll(Dice.d100);
							if (er <= 15) {
								effect = "веер";
							} else if (er <= 40) {
								effect = "дерево";
							} else if (er <= 50) {
								effect = "кнут";
							} else if (er <= 65) {
								effect = "лодка-лебедь";
							} else if (er <= 80) {
								effect = "птица";
							} else {
								effect = "якорь";
							}
							itemApi.changeName("(Эффект: " + effect + ")");
						} else if (ri >= 85 && ri <= 87) {
							String effect = "";
							int er = Dice.roll(Dice.d100);
							if (er <= 10) {
								effect = "Аберрации";
							} else if (er <= 20) {
								effect = "Звери";
							} else if (er <= 45) {
								effect = "Исчадия";
							} else if (er <= 55) {
								effect = "Небожителиь";
							} else if (er <= 75) {
								effect = "Нежить";
							} else if (er <= 80) {
								effect = "Растения";
							} else if (er <= 90) {
								effect = "Феи";
							} else if (er <= 100) {
								effect = "Элементали";
							} else {
								effect = "якорь";
							}
							itemApi.changeName("(Вид существ: " + effect + ")");
						} else if (ri >= 88 && ri <= 89) {
							itemApi.changeName(String.format("(Бобов: %d)", Dice.roll(2, Dice.d4)));
						}
					}
					if (tableName.equals("E")) {
						switch (ri) {
						case 66:
							itemApi.changeName("(кольчуга)");
							itemApi.setCostDmg(Rarity.getCostDMG(mt.getMagicThing().getRarity()) + 75);
							break;
						case 67:
							itemApi.changeName("(кольчужная рубаха)");
							itemApi.setCostDmg(Rarity.getCostDMG(mt.getMagicThing().getRarity()) + 50);
							break;
						case 68:
							itemApi.changeName("(чещуйчатый доспех)");
							itemApi.setCostDmg(Rarity.getCostDMG(mt.getMagicThing().getRarity()) + 50);
							break;
						}
					}
					if (tableName.equals("E1")) {
						switch (ri) {
						case 15:
							itemApi.changeName("(кираса)");
							itemApi.setCostDmg(Rarity.getCostDMG(mt.getMagicThing().getRarity()) + 400);
							break;
						case 16:
							itemApi.changeName("(наборной доспех)");
							itemApi.setCostDmg(Rarity.getCostDMG(mt.getMagicThing().getRarity()) + 200);
							break;
						case 35:
							itemApi.changeName("(кожаный)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							break;
						case 36:
							itemApi.changeName("(кольчуга)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							break;
						case 37:
							itemApi.changeName("(кольчужная рубаха)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							break;
						case 38:
							itemApi.changeName("(чешуйчатый)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							break;
						case 60:
							itemApi.changeName(getResistenceType());
							break;
						}
					}
					if (tableName.equals("Ж")) {
						switch (ri) {
						case 55:
							itemApi.changeName("(латы)");
							itemApi.setCostDmg(Rarity.getCostDMG(mt.getMagicThing().getRarity()) + 1500);
							break;
						case 56:
							itemApi.changeName("(полулаты)");
							itemApi.setCostDmg(Rarity.getCostDMG(mt.getMagicThing().getRarity()) + 750);
							break;
						case 65:
							itemApi.changeName("(кираса)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							break;
						case 66:
							itemApi.changeName("(кираса)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							break;
						case 67:
							itemApi.changeName("(проклёпанная кожа)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							break;
						case 68:
							itemApi.changeName("(кожаный)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							break;
						case 69:
							itemApi.changeName("(кольчуга)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							break;
						case 70:
							itemApi.changeName("(кольчужная рубаха)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							break;
						case 71:
							itemApi.changeName("(чешуйчатый)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							break;
						case 89:
							String aligment = Alignment.values()[rnd.nextInt(Alignment.values().length)]
									.getCyrilicName();
							itemApi.changeName("(Мировоззрение: " + aligment + ")");
							break;
						case 91:
							int rg = Dice.roll(Dice.d20);
							String golemType = "";
							if (rg >= 1 && rg <= 5)
								golemType = "Глинянный";
							else if (rg == 6)
								golemType = "Железный";
							else if (rg >= 7 && rg <= 8)
								golemType = "Каменный";
							else if (rg >= 9 && rg <= 20)
								golemType = "Мясной (из плоти)";
							itemApi.changeName(String.format("(%s)", golemType));
							break;
						}
					}
					if (tableName.equals("З")) {
						switch (ri) {
						case 42:
						case 43:
							itemApi.changeName("(латы)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							break;
						case 44:
						case 45:
							itemApi.changeName("(полулаты)");
							itemApi.setRarity(Rarity.UNCOMMON.getCyrilicName());
							break;
						case 46:
						case 47:
							itemApi.changeName("(чешуйчатый)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							break;
						case 48:
						case 49:
							itemApi.changeName("(кираса)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							break;
						case 50:
						case 51:
							itemApi.changeName("(наборной)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							break;
						case 52:
						case 53:
							itemApi.changeName("(проклёпанная кожа)");
							itemApi.setRarity(Rarity.RARE.getCyrilicName());
							break;
						case 54:
						case 55:
							itemApi.changeName("(кожаный)");
							itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
							break;
						case 56:
						case 57:
							itemApi.changeName("(кольчуга)");
							itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
							break;
						case 58:
						case 59:
							itemApi.changeName("(кольчужная рубаха)");
							itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
							break;
						case 76:
							switch (Dice.roll(Dice.d12)) {
							case 1:
							case 2:
								itemApi.changeName("(полулаты)");
								itemApi.setRarity(Rarity.RARE.getCyrilicName());
								break;
							case 3:
							case 4:
								itemApi.changeName("(латы)");
								itemApi.setRarity(Rarity.RARE.getCyrilicName());
								break;
							case 5:
							case 6:
								itemApi.changeName("(проклёпанная кожа)");
								itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
								break;
							case 7:
							case 8:
								itemApi.changeName("(кираса)");
								itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
								break;
							case 9:
							case 10:
								itemApi.changeName("(набороной)");
								itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
								break;
							case 11:
								itemApi.changeName("(полулаты)");
								itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
								break;
							case 12:
								itemApi.changeName("(латы)");
								itemApi.setRarity(Rarity.VERY_RARE.getCyrilicName());
								break;
							}
							break;
						}
					}
					if (itemApi.getName().getRus().startsWith("Боеприпасы")) {
						int rb = Dice.roll(Dice.d12);
						if (rb <= 6) {
							itemApi.changeName("(стрелы)");
						} else if (rb < 9) {
							itemApi.changeName("(болты)");
						} else if (rb < 10) {
							itemApi.changeName("(дротики)");
						} else if (rb < 11) {
							itemApi.changeName("(снаряды для пращи)");
						}
					} else if (itemApi.getName().getRus().contains("Свиток заклинания")) {
						if (itemApi.getName().getRus().contains("заговор")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 0, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("1")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 1, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("2")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 2, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("3")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 3, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("4")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 4, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("5")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 5, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("6")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 6, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("7")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 7, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("8")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 8, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
						if (itemApi.getName().getRus().contains("9")) {
							List<Spell> spells = spellRepo.findByLevelAndBook_type((byte) 9, TypeBook.OFFICAL);
							Spell spell = spells.get(rnd.nextInt(spells.size()));
							itemApi.setSpell(new SpellApi(spell));
							itemApi.changeName(String.format("(%s)", spell.getName().toLowerCase()));
						}
					} else if (itemApi.getName().getRus().contains("Оружие")) {
						List<Weapon> weapons = weaponRepo.findAll();
						Weapon weapon = weapons.get(rnd.nextInt(weapons.size()));
						itemApi.changeName(" (" + weapon.getName().toLowerCase() + ")");
					} else if (tableName.equals("Е1") && ri >= 12 && ri <= 14) {
						switch (Dice.roll(Dice.d8)) {
						case 1:
							itemApi.changeName("(Бронзовый грифон)");
							break;
						case 2:
							itemApi.changeName("(Эбеновая муха)");
							break;
						case 3:
							itemApi.changeName("(Золотые львы)");
							break;
						case 4:
							itemApi.changeName("(Костяные козлы)");
							break;
						case 5:
							itemApi.changeName("(Мраморный слон)");
							break;
						case 6:
						case 7:
							itemApi.changeName("(Ониксовая собака)");
							break;
						case 8:
							itemApi.changeName("(Серпентиновая сова)");
							break;
						}
					} else if (itemApi.getName().getRus().contains("Зелье сопротивления")) {
						String resistType = getResistenceType();
						switch (resistType) {
						case "(звуку)":
							itemApi = new MagicItemApi(magicItemRepo.findById(86).get());
							break;
						case "(излучению)":
							itemApi = new MagicItemApi(magicItemRepo.findById(87).get());
							break;
						case "(кислоте)":
							itemApi = new MagicItemApi(magicItemRepo.findById(421).get());
							break;
						case "(огню)":
							itemApi = new MagicItemApi(magicItemRepo.findById(420).get());
							break;
						case "(некротической энергии)":
							itemApi = new MagicItemApi(magicItemRepo.findById(422).get());
							break;
						case "(психической энергии)":
							itemApi = new MagicItemApi(magicItemRepo.findById(423).get());
							break;
						case "(силовому полю)":
							itemApi = new MagicItemApi(magicItemRepo.findById(424).get());
							break;
						case "(холоду)":
							itemApi = new MagicItemApi(magicItemRepo.findById(425).get());
							break;
						case "(электричеству)":
							itemApi = new MagicItemApi(magicItemRepo.findById(426).get());
							break;
						case "(яду)":
							itemApi = new MagicItemApi(magicItemRepo.findById(896).get());
							break;
						}
					} else if (itemApi.getName().getRus().contains("Доспех cопротивления")) {
						itemApi.changeName(getResistenceType());
					} else if (itemApi.getName().getRus().contains("Камень элементаля")) {
						switch (Dice.roll(Dice.d4)) {
						case 1:
							itemApi.changeName("Изумруд	(элементаль воды)");
							break;
						case 2:
							itemApi.changeName("Синий сапфир элементаль воздуха");
							break;
						case 3:
							itemApi.changeName("Жёлтый бриллиант (элементаль земли)");
							break;
						case 4:
							itemApi.changeName("Красный корунд (элементаль огня)");
							break;
						}
					} else if (itemApi.getName().getRus().contains("Свиток защиты")) {
						// int roll = Dice.d100.roll();
					}
					if (unique) {
						if (names.contains(itemApi.getName().getRus())) {
							count++;
							continue;
						}
					}
					list.add(itemApi);
					names.add(itemApi.getName().getRus());
				}
			}
		}
		return list;
	}

	private String getResistenceType() {
		switch (Dice.roll(Dice.d10)) {
		case 1:
			return "(звуку)";
		case 2:
			return "(излучению)";
		case 3:
			return "(кислоте)";
		case 4:
			return "(некротической энергии)";
		case 5:
			return "(огню)";
		case 6:
			return "(психической энергии)";
		case 7:
			return "(силовому полю)";
		case 8:
			return "(холоду)";
		case 9:
			return "(электричеству)";
		case 10:
			return "(яду)";
		}
		return "";
	}

	private List<ItemApi> getTreasures(int cost, TreasureType type, int count, Dice dice) {
		List<ItemApi> treasures = new ArrayList<>();
		int ri = dice.roll(count);
		for (int i = 0; i < ri; i++) {
			List<Treasure> treasuresFind = treasureRepo.findAllByCostAndType(cost, type);
			treasures.add(new ItemApi(treasuresFind.get(rnd.nextInt(treasuresFind.size()))));
		}
		return treasures;
	}
}