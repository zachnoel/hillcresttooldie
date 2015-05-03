package com.htd.repository;

import com.htd.domain.Po;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Spring Data JPA repository for the Po entity.
 */
public interface PoRepository extends JpaRepository<Po,Long> {

    @Query("select po from Po po left join fetch po.parts left join fetch po.customers where po.id =:id")
    Po findOneWithEagerRelationships(@Param("id") Long id);
    
	 
	 @Query(value="SELECT u FROM T_PO u WHERE u.id LIKE %?1", nativeQuery = true)
	 List<Po> getPo(BigDecimal material_number);
    

}
