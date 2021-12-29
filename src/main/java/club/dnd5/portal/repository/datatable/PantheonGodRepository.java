package club.dnd5.portal.repository.datatable;

import org.springframework.data.jpa.repository.JpaRepository;

import club.dnd5.portal.model.god.Pantheon;

public interface PantheonGodRepository extends JpaRepository<Pantheon, Integer> {

}
