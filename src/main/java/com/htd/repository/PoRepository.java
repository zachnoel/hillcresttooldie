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

     @Query("SELECT p FROM Po p WHERE p.id LIKE :materialNumber")
	 List<Po> getPo(@Param("materialNumber")BigDecimal materialNumber);

    @Query("SELECT p FROM Po p WHERE p.due_date >= :startDate AND p.due_date <= :endDate")
	List<Po> findByFilter(@Param("startDate")String startDate, @Param("endDate")String endDate);

    @Query("SELECT NEW com.htd.domain.ShopOrder(po.id, po.po_number, "
        + "po.due_date, po.customer.customer_name, po_part.id, po_part.part_quantity, "
        + "parts.id, parts.part_number, parts.part_description, "
        + "parts.plasma_hrs_per_part, parts.grind_hrs_per_part, "
        + "parts.mill_hrs_per_part, parts.brakepress_hrs_per_part, "
        + "materials.material_number, materials.material_size, pmaterials.parts_per_material) "
        + "FROM Po po "
        + "JOIN po.partList po_part "
        + "JOIN po_part.part parts "
        + "JOIN parts.materialList pmaterials "
        + "JOIN pmaterials.material materials "
        + "WHERE po.id = :id")
    List<ShopOrder> getShopOrder(@Param("id") Long id);

}
