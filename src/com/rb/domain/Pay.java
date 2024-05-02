/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rb.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LuisR
 */
public class Pay {

    private int id;
    private String codigo;
    private Date fecha;
    private long idInvoice;
    private long cycle;
    private BigDecimal value;
    private BigDecimal efecty;
    private BigDecimal transfer;
    private BigDecimal card;
    private BigDecimal cambio;
    private BigDecimal credit;
    private PayType type;
    private String bankTransfer;
    private int cardType;

    private String nota;
    private Date updateTime;
    private String user;

    public static final String CREDIT = "CREDITO";
    public static final String DEBIT = "DEBITO";

    public static final int DEBIT_TYPE = 1;
    public static final int CREDIT_TYPE = 1;

    public enum PayType {
        EFECTY(0), TRANSFER(1), CARD(2), COMBO(3);

        private static final Map<Integer, PayType> lookup
                = new HashMap<Integer, PayType>();

        static {
            for (PayType w : EnumSet.allOf(PayType.class)) {
                lookup.put(w.getCode(), w);
            }
        }

        private int code;

        private PayType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static PayType get(int code) {
            return lookup.get(code);
        }
    };

    public Pay() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public long getCycle() {
        return cycle;
    }

    public void setCycle(long cycle) {
        this.cycle = cycle;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public long getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(long idInvoice) {
        this.idInvoice = idInvoice;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getEfecty() {
        return efecty;
    }

    public void setEfecty(BigDecimal efecty) {
        this.efecty = efecty;
    }

    public BigDecimal getTransfer() {
        return transfer;
    }

    public void setTransfer(BigDecimal transfer) {
        this.transfer = transfer;
    }

    public BigDecimal getCard() {
        return card;
    }

    public void setCard(BigDecimal bank) {
        this.card = bank;
    }

    public BigDecimal getCambio() {
        return cambio;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public void setCambio(BigDecimal cambio) {
        this.cambio = cambio;
    }

    public String getBankTransfer() {
        return bankTransfer;
    }

    public void setBankTransfer(String bankTransfer) {
        this.bankTransfer = bankTransfer;
    }

    public PayType getType() {
        return type;
    }

    public void setType(int type) {
        this.type = PayType.values()[type];
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMethods() {
        StringBuilder stb = new StringBuilder();
        stb.append(efecty.signum() == 0 ? "" : "EFECTIVO,");
        stb.append(transfer.signum() == 0 ? "" : "TRANSFERENCIA,");
        stb.append(card.signum() == 0 ? "" : "TARJETA,");
        return stb.toString().substring(0, stb.length() - 1);
    }

    public BigDecimal getTotalRec() {
        return efecty.add(transfer).add(card);
    }

    @Override
    public String toString() {
        return "Pay{" + "id=" + id + ", codigo=" + codigo + ", fecha=" + fecha + ", idInvoice=" + idInvoice + ", value=" + value + ", efecty=" + efecty + ", transfer=" + transfer + ", card=" + card + ", cambio=" + cambio + ", credit=" + credit + ", type=" + type + ", bankTransfer=" + bankTransfer + ", cardType=" + cardType + ", nota=" + nota + ", updateTime=" + updateTime + ", user=" + user + '}';
    }

}
