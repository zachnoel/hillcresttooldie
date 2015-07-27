package com.htd.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Po_part.
 */
@Entity
@Table(name = "T_PART_MATERIAL")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Part_material implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "material_quantity")
    private Integer material_quantity;
    
    @Column(name = "parts_per_material")
    private Integer parts_per_material;

    @ManyToOne
    private Part part;

    @ManyToOne
    private Material material;

	public Integer getMaterial_quantity() {
		return material_quantity;
	}

	public void setMaterial_quantity(Integer material_quantity) {
		this.material_quantity = material_quantity;
	}

	public Integer getParts_per_material() {
		return parts_per_material;
	}

	public void setParts_per_material(Integer parts_per_material) {
		this.parts_per_material = parts_per_material;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Part_material po_part = (Part_material) o;

        if ( ! Objects.equals(id, po_part.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Po_part{" +
            "id=" + id +
            ", material_quantity='" + material_quantity + "'" +
            ", parts_per_material='" + parts_per_material + "'" +
            '}';
    }

}
