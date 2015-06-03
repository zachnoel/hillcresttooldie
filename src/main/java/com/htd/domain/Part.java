package com.htd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Part.
 */
@Entity
@Table(name = "T_PART")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Part implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "part_number")
    private String part_number;

    @Column(name = "part_description")
    private String part_description;

    @Column(name = "plasma_hrs_per_part", precision=12, scale=4)
    private BigDecimal plasma_hrs_per_part;
    
    @Column(name = "laser_hrs_per_part", precision=12, scale=4)
    private BigDecimal laser_hrs_per_part;

    @Column(name = "grind_hrs_per_part", precision=12, scale=4)
    private BigDecimal grind_hrs_per_part;

    @Column(name = "mill_hrs_per_part", precision=12, scale=4)
    private BigDecimal mill_hrs_per_part;

    @Column(name = "brakepress_hrs_per_part", precision=12, scale=4)
    private BigDecimal brakepress_hrs_per_part;

    @Column(name = "lb_per_part", precision=12, scale=4)
    private BigDecimal lb_per_part;

    @Column(name = "inventory_count")
    private Integer inventory_count;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "T_PART_MATERIAL",
               joinColumns = @JoinColumn(name="parts_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="materials_id", referencedColumnName="ID"))
    private Set<Material> materials = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPart_number() {
        return part_number;
    }

    public void setPart_number(String part_number) {
        this.part_number = part_number;
    }

    public String getPart_description() {
        return part_description;
    }

    public void setPart_description(String part_description) {
        this.part_description = part_description;
    }

    public BigDecimal getPlasma_hrs_per_part() {
        return plasma_hrs_per_part;
    }

    public void setPlasma_hrs_per_part(BigDecimal plasma_hrs_per_part) {
        this.plasma_hrs_per_part = plasma_hrs_per_part;
    }
    
    public BigDecimal getLaser_hrs_per_part() {
        return laser_hrs_per_part;
    }

    public void setLaser_hrs_per_part(BigDecimal laser_hrs_per_part) {
        this.laser_hrs_per_part = laser_hrs_per_part;
    }

    public BigDecimal getGrind_hrs_per_part() {
        return grind_hrs_per_part;
    }

    public void setGrind_hrs_per_part(BigDecimal grind_hrs_per_part) {
        this.grind_hrs_per_part = grind_hrs_per_part;
    }

    public BigDecimal getMill_hrs_per_part() {
        return mill_hrs_per_part;
    }

    public void setMill_hrs_per_part(BigDecimal mill_hrs_per_part) {
        this.mill_hrs_per_part = mill_hrs_per_part;
    }

    public BigDecimal getBrakepress_hrs_per_part() {
        return brakepress_hrs_per_part;
    }

    public void setBrakepress_hrs_per_part(BigDecimal brakepress_hrs_per_part) {
        this.brakepress_hrs_per_part = brakepress_hrs_per_part;
    }

    public BigDecimal getLb_per_part() {
        return lb_per_part;
    }

    public void setLb_per_part(BigDecimal lb_per_part) {
        this.lb_per_part = lb_per_part;
    }

    public Integer getInventory_count() {
        return inventory_count;
    }

    public void setInventory_count(Integer inventory_count) {
        this.inventory_count = inventory_count;
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

        Part part = (Part) o;

        if ( ! Objects.equals(id, part.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Part{" +
                "id=" + id +
                ", part_number='" + part_number + "'" +
                ", part_description='" + part_description + "'" +
                ", plasma_hrs_per_part='" + plasma_hrs_per_part + "'" +
                ", laser_hrs_per_part='" + laser_hrs_per_part + "'" +
                ", grind_hrs_per_part='" + grind_hrs_per_part + "'" +
                ", mill_hrs_per_part='" + mill_hrs_per_part + "'" +
                ", brakepress_hrs_per_part='" + brakepress_hrs_per_part + "'" +
                ", lb_per_part='" + lb_per_part + "'" +
                ", inventory_count='" + inventory_count + "'" +
                '}';
    }
}
