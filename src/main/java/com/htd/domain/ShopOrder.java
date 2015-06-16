package com.htd.domain;

import java.math.BigDecimal;

import org.joda.time.LocalDate;

public class ShopOrder<po_due_date> {

    private long po_id;
    private String po_number;
    private LocalDate po_due_date;
    private long po_part_id;
    private int part_quantity;
    private long part_id;
    private String part_number;
    private String part_decription;
    private BigDecimal plasma_hrs;
    private BigDecimal grind_hours;
    private BigDecimal mill_hrs;
    private BigDecimal breakpress_hrs;
    private String machine_number;


    public ShopOrder() {

    }

    public ShopOrder(long po_id, String po_number, LocalDate po_due_date,
                     long po_part_id, int part_quantity, long part_id,
                     String part_number, String part_decription, BigDecimal plasma_hrs,
                     BigDecimal grind_hours, BigDecimal mill_hrs,
                     BigDecimal breakpress_hrs) {

        this.po_id = po_id;
        this.po_number = po_number;
        this.po_due_date = po_due_date;
        this.po_part_id = po_part_id;
        this.part_quantity = part_quantity;
        this.part_id = part_id;
        this.part_number = part_number;
        this.part_decription = part_decription;
        this.plasma_hrs = plasma_hrs;
        this.grind_hours = grind_hours;
        this.mill_hrs = mill_hrs;
        this.breakpress_hrs = breakpress_hrs;

    }

    public long getPo_id() {
        return po_id;
    }

    public void setPo_id(long po_id) {
        this.po_id = po_id;
    }

    public String getPo_number() {
        return po_number;
    }

    public void setPo_number(String po_number) {
        this.po_number = po_number;
    }

    public LocalDate getPo_due_date() {
        return po_due_date;
    }

    public void setPo_due_date(LocalDate po_due_date) {
        this.po_due_date = po_due_date;
    }

    public long getPo_part_id() {
        return po_part_id;
    }

    public void setPo_part_id(long po_part_id) {
        this.po_part_id = po_part_id;
    }

    public int getPart_quantity() {
        return part_quantity;
    }

    public void setPart_quantity(int part_quantity) {
        this.part_quantity = part_quantity;
    }

    public long getPart_id() {
        return part_id;
    }

    public void setPart_id(long part_id) {
        this.part_id = part_id;
    }

    public String getPart_number() {
        return part_number;
    }

    public void setPart_number(String part_number) {
        this.part_number = part_number;
    }

    public String getPart_decription() {
        return part_decription;
    }

    public void setPart_decription(String part_decription) {
        this.part_decription = part_decription;
    }

    public BigDecimal getPlasma_hrs() {
        return plasma_hrs;
    }

    public void setPlasma_hrs(BigDecimal plasma_hrs) {
        this.plasma_hrs = plasma_hrs;
    }

    public BigDecimal getGrind_hours() {
        return grind_hours;
    }

    public void setGrind_hours(BigDecimal grind_hours) {
        this.grind_hours = grind_hours;
    }

    public BigDecimal getMill_hrs() {
        return mill_hrs;
    }

    public void setMill_hrs(BigDecimal mill_hrs) {
        this.mill_hrs = mill_hrs;
    }

    public BigDecimal getBreakpress_hrs() {
        return breakpress_hrs;
    }

    public void setBreakpress_hrs(BigDecimal breakpress_hrs) {
        this.breakpress_hrs = breakpress_hrs;
    }

    public String getMachine_number() {
        return machine_number;
    }

    public void setMachine_number(String machine_number) {
        this.machine_number = machine_number;
    }

    @Override
    public String toString() {
        return "ShopOrder [po_id=" + po_id + ", po_number=" + po_number
                + ", po_due_date=" + po_due_date + ", po_part_id=" + po_part_id
                + ", part_quantity=" + part_quantity + ", part_id=" + part_id
                + ", part_number=" + part_number + ", part_decription="
                + part_decription + ", plasma_hrs=" + plasma_hrs
                + ", gring_hours=" + grind_hours + ", mill_hrs=" + mill_hrs
                + ", breakpress_hrs=" + breakpress_hrs + "]";
    }

}
