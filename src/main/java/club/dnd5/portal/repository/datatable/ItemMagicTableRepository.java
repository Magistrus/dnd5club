package club.dnd5.portal.repository.datatable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.items.MagicThingTable;

@Repository
public interface ItemMagicTableRepository extends JpaRepository<MagicThingTable, Integer> {
	@Query("SELECT mt FROM MagicThingTable mt WHERE mt.start <= :result AND mt.end >=:result AND mt.nameTable = :table")
	public MagicThingTable findOne(@Param("result") Integer result, @Param("table")  String table);
}