package com.htd.repository;

import com.htd.domain.Po;
import com.htd.domain.Po_part;

import org.springframework.data.jpa.repository.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Spring Data JPA repository for the Po_part entity.
 */
public interface Po_partRepository extends JpaRepository<Po_part,Long> {

	@Query(value="SELECT * FROM T_PO_PART u WHERE u.po_id LIKE %?1", nativeQuery = true)
	List<Po_part> findByPoId(Long poId);
	
}
