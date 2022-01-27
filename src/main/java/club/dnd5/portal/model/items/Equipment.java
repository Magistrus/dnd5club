package club.dnd5.portal.model.items;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import club.dnd5.portal.model.book.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "equipments")
public class Equipment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String englishName;
	private Integer cost;
	
	@Enumerated(EnumType.ORDINAL)
	private Currency currency;
	
	private Float weight;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@ElementCollection(targetClass = EquipmentType.class)
	@JoinTable(name = "equipments_types", joinColumns = @JoinColumn(name = "equipment_id"))
	@Column(name = "type", nullable = false)
	@Enumerated(javax.persistence.EnumType.STRING)
	private Set<EquipmentType> types;
	
	@ManyToOne
	@JoinColumn(name = "source")
	private Book book;
	private Short page;
	
	public String getTextCost() {
		if (getCost() == null) {
			return "&mdash;";
		}
		else
		{
			
			switch (getCurrency()) {
			case SM:
				return String.valueOf(getCost() / 10f) + " " + getCurrency().getName();
			case GM:
				return String.valueOf(getCost() / 100f) + " " + getCurrency().getName();
			case PM:
				return String.valueOf(getCost() / 1000f) + " " + getCurrency().getName();
			default:
				return getCost() + " " + getCurrency().getName();
			}
		}
	}
}