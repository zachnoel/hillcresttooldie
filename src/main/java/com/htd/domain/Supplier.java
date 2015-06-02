package com.htd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Supplier.
 */
@Entity
@Table(name = "T_SUPPLIER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Supplier implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "supplier_name", nullable = false)
    private String supplier_name;

    @Column(name = "supplier_address")
    private String supplier_address;

    @Column(name = "supplier_phone")
    private String supplier_phone;

    @OneToMany(mappedBy = "supplier")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Material> materials = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getSupplier_address() {
        return supplier_address;
    }

    public void setSupplier_address(String supplier_address) {
        this.supplier_address = supplier_address;
    }

    public String getSupplier_phone() {
        return supplier_phone;
    }

    public void setSupplier_phone(String supplier_phone) {
        this.supplier_phone = supplier_phone;
    }

    public Set<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(Set<Material> materials) {
        this.materials = materials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Supplier supplier = (Supplier) o;

        if ( ! Objects.equals(id, supplier.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "id=" + id +
                ", supplier_name='" + supplier_name + "'" +
                ", supplier_address='" + supplier_address + "'" +
                ", supplier_phone='" + supplier_phone + "'" +
                '}';
    }
}
