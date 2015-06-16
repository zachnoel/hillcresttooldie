package com.htd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.htd.domain.Po_part;
import com.htd.domain.ShopOrder;

/**
 * Spring Data JPA repository for the Po_part entity.
 */
public interface Po_partRepository extends JpaRepository<Po_part,Long> {

	@Query(value="SELECT * FROM T_PO_PART u WHERE u.po_id LIKE %?1", nativeQuery = true)
	List<Po_part> findByPoId(Long poId);




}
