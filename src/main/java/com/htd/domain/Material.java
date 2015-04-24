package com.htd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Material.
 */
@Entity
@Table(name = "T_MATERIAL")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Material implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "material_number")
    private String material_number;

    @Column(name = "material_thickness", precision=10, scale=2)
    private BigDecimal material_thickness;

    @Column(name = "material_size")
    private String material_size;

    @Column(name = "lb_per_sheet", precision=10, scale=2)
    private BigDecimal lb_per_sheet;

    @Column(name = "dollar_per_lb", precision=10, scale=2)
    private BigDecimal dollar_per_lb;

    @Column(name = "inventory_count")
    private Integer inventory_count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterial_number() {
        return material_number;
    }

    public void setMaterial_number(String material_number) {
        this.material_number = material_number;
    }

    public BigDecimal getMaterial_thickness() {
        return material_thickness;
    }

    public void setMaterial_thickness(BigDecimal material_thickness) {
        this.material_thickness = material_thickness;
    }

    public String getMaterial_size() {
        return material_size;
    }

    public void setMaterial_size(String material_size) {
        this.material_size = material_size;
    }

    public BigDecimal getLb_per_sheet() {
        return lb_per_sheet;
    }

    public void setLb_per_sheet(BigDecimal lb_per_sheet) {
        this.lb_per_sheet = lb_per_sheet;
    }

    public BigDecimal getDollar_per_lb() {
        return dollar_per_lb;
    }

    public void setDollar_per_lb(BigDecimal dollar_per_lb) {
        this.dollar_per_lb = dollar_per_lb;
    }

    public Integer getInventory_count() {
        return inventory_count;
    }

    public void setInventory_count(Integer inventory_count) {
        this.inventory_count = inventory_count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Material material = (Material) o;

        if ( ! Objects.equals(id, material.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", material_number='" + material_number + "'" +
                ", material_thickness='" + material_thickness + "'" +
                ", material_size='" + material_size + "'" +
                ", lb_per_sheet='" + lb_per_sheet + "'" +
                ", dollar_per_lb='" + dollar_per_lb + "'" +
                ", inventory_count='" + inventory_count + "'" +
                '}';
    }
}
