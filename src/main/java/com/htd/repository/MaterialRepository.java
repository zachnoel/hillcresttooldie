package com.htd.repository;

import com.htd.domain.Material;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Material entity.
 */
public interface MaterialRepository extends JpaRepository<Material,Long> {

}
