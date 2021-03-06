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
@Table(name = "T_PO_PART")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Po_part implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "part_quantity")
    private Integer part_quantity;

    @ManyToOne
    private Part part;

    @ManyToOne
    private Po po;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPart_quantity() {
        return part_quantity;
    }

    public void setPart_quantity(Integer part_quantity) {
        this.part_quantity = part_quantity;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Po getPo() {
        return po;
    }

    public void setPo(Po po) {
        this.po = po;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Po_part po_part = (Po_part) o;

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
            ", part_quantity='" + part_quantity + "'" +
            '}';
    }
}
