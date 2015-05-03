package com.htd.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.htd.domain.Material;

/**
 * Spring Data JPA repository for the Material entity.
 */
public interface MaterialRepository extends JpaRepository<Material,Long> {
	
	 //public void uploadMaterialData(String material_number, BigDecimal material_thickness,String material_size,BigDecimal lb_per_sheet,BigDecimal dollar_per_lb);
	
	 @Query(value="SELECT u FROM T_MATERIAL u WHERE u.material_number LIKE %?1", nativeQuery = true)
	 List<Material> getMaterials(BigDecimal material_number);

}
