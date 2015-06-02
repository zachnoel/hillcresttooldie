package com.htd.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.htd.domain.util.CustomLocalDateSerializer;
import com.htd.domain.util.ISO8601LocalDateDeserializer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Po.
 */
@Entity
@Table(name = "T_PO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Po implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "po_number")
    private String po_number;

    @Column(name = "sales_order_number")
    private String sales_order_number;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "due_date")
    private LocalDate due_date;

    @Column(name = "status")
    private String status;

    @Column(name = "total_sale", precision=10, scale=2)
    private BigDecimal total_sale;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPo_number() {
        return po_number;
    }

    public void setPo_number(String po_number) {
        this.po_number = po_number;
    }

    public String getSales_order_number() {
        return sales_order_number;
    }

    public void setSales_order_number(String sales_order_number) {
        this.sales_order_number = sales_order_number;
    }

    public LocalDate getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal_sale() {
        return total_sale;
    }

    public void setTotal_sale(BigDecimal total_sale) {
        this.total_sale = total_sale;
    }

    
    public Customer getCustomer() {
    	return customer;
    }
    public void setCustomer(Customer customer){
    	this.customer = customer;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Po po = (Po) o;

        if ( ! Objects.equals(id, po.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Po{" +
                "id=" + id +
                ", po_number='" + po_number + "'" +
                ", sales_order_number='" + sales_order_number + "'" +
                ", due_date='" + due_date + "'" +
                ", status='" + status + "'" +
                ", total_sale='" + total_sale + "'" +
                '}';
    }
}
