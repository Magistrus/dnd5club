package club.dnd5.portal.model.foundary.token;

import java.util.ArrayList;
import java.util.List;

import org.thymeleaf.util.StringUtils;

import club.dnd5.portal.model.creature.Creature;
import club.dnd5.portal.model.foundary.FEffect;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FToken {
	public String name;
	public int displayName = 0;
	public boolean actorLink;
	public Object tint;
	public float width;
	public float height;
	public int scale = 1;
	public boolean mirrorX;
	public boolean mirrorY;
	public boolean lockRotation;
	public int rotation;
	public boolean vision = true;
	public int dimSight;
	public int brightSight;
	public int dimLight;
	public int brightLight;
	public int sightAngle;
	public int lightAngle;
	public int lightAlpha = 1;
	public FLightAnimation lightAnimation;
	public int disposition = -1;
	public int displayBars = 40;
	public FBar bar1;
	public FBar bar2;
	public FFlags flags;
	public List<FEffect> effects= new ArrayList<FEffect>();
	public boolean randomImg;
	public String img;
	public String actorId;
	public int alpha = 1;

	public FToken(Creature creature) {
		name = creature.getName();
		lightAnimation = new FLightAnimation();
		bar1 = new FBar("attributes.hp");
		bar2 = new FBar();
		if (creature.getDarkvision() != null) {
			dimSight = creature.getDarkvision();
		}
		switch (creature.getSize()) {
		case TINY:
			width = 0.3f;
			height = 0.3f;
			break;
		case SMALL:
			width = 0.5f;
			height = 0.5f;
			break;
		case MEDIUM:
			width = 1;
			height = 1;
			break;
		case LARGE:
			width = 2;
			height = 2;
			break;
		case HUGE:
			width = 3;
			height = 3;
			break;
		case GARGANTUAN:
			width = 4;
			height = 4;
			break;
		default:
			width = 1;
			height = 1;
			break;
		}
		img = StringUtils.capitalizeWords(String.format("https://5e.tools/img/%s/%s.png",
				creature.getBook().getSource(), creature.getEnglishName()));
	}
}
