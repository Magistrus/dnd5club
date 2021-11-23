package club.dnd5.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.Madness;
import club.dnd5.portal.model.MadnessType;


@Repository
public interface MadnessRepository extends JpaRepository<Madness, Integer>{
	List<Madness> findByMadnessType(MadnessType madnessType); 
}