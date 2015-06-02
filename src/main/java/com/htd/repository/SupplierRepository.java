package com.htd.repository;

import com.htd.domain.Supplier;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Supplier entity.
 */
public interface SupplierRepository extends JpaRepository<Supplier,Long> {

}
