package club.dnd5.portal.model;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Dice {
	d4(4),
	d6(6),
	d8(8),
	d10(10),
	d12(12),
	d20(20),
	d100(100),
	d3(3),
	d2(2);

	private static Random rnd = new Random();

	private int maxValue;
	
	public int roll() {
		return 1 + rnd.nextInt(this.getMaxValue());
	}
	
	public int roll(int diceCount) {
		int result = 0;
		for (int i = 0; i < diceCount; i++) {
			result += roll();
		}
		return result;
	}
	
	public static Dice parse(int dice) {
		switch (dice) {
		case 4:
			return d4;
		case 6:
			return d6;
		case 8:
			return d8;
		case 10:
			return d10;
		case 12:
			return d12;
		case 20:
			return d20;
		case 100:
			return d100;
		}
		return null;
	}
	
	public static Dice parse(String dice) {
		switch (dice) {
		case "k4":
			return d4;
		case "k6":
			return d6;
		case "k8":
			return d8;
		case "k10":
			return d10;
		case "k12":
			return d12;
		case "k20":
			return d20;
		case "k100":
			return d100;
		}
		return null;
	}
	
	public String getName() {
		return "к" + maxValue;
	}

	public static Set<Dice> getCreatures() {
		return EnumSet.of(d4, d6, d8, d10, d12, d20);
	}

	public static int roll(Dice dice) {
		return 1 + rnd.nextInt(dice.maxValue);
	}

	public static int roll(int countDice, Dice dice) {
		int result = 0;
		for (int i = 0; i < countDice; i++) {
			result += roll(dice);
		}
		return result;
	}
}