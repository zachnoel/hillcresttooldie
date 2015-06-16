package com.htd.repository;

import com.htd.domain.Po;

import com.htd.domain.ShopOrder;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Spring Data JPA repository for the Po entity.
 */
public interface PoRepository extends JpaRepository<Po,Long> {

    //@Query("select po from Po po left join fetch po.parts left join fetch po.customers where po.id =:id")
    //Po findOneWithEagerRelationships(@Param("id") Long id);
    
	 
	 @Query(value="SELECT u FROM T_PO u WHERE u.id LIKE %?1", nativeQuery = true)
	 List<Po> getPo(BigDecimal material_number);

	@Query(value="Select * FROM T_PO po WHERE po.due_date >= ?1 and po.due_date <= ?2", nativeQuery = true)
	List<Po> findByFilter(String startDate, String endDate);

	@Query("SELECT NEW com.htd.domain.ShopOrder(po.id, po.po_number, "
			+ "po.due_date, po_part.id, po_part.part_quantity, "
			+ "part.id, part.part_number, part.part_description, "
			+ "part.plasma_hrs_per_part, part.grind_hrs_per_part, "
			+ "part.mill_hrs_per_part, part.brakepress_hrs_per_part) "
			+ "FROM Po po "
			+ "JOIN po.partList po_part "
			+ "JOIN po_part.part part "
			+ "where po.id = ?1")
	List<ShopOrder> getShopOrder(Long id);

}
