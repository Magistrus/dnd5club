package club.dnd5.portal.repository.datatable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.races.RaceNickname;


@Repository
public interface NicknameRepository extends JpaRepository<RaceNickname, Integer> {

}