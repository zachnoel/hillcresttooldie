package com.htd.repository;

import com.htd.domain.Part;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Part entity.
 */
public interface PartRepository extends JpaRepository<Part,Long> {

    @Query("select part from Part part left join fetch part.materials where part.id =:id")
    Part findOneWithEagerRelationships(@Param("id") Long id);

}
