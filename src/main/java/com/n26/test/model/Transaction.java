package com.n26.test.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 3509301540310492566L;
    private Double amount;
    @XmlElement(name = "timestamp")
    private Long timeStamp;

    public Double getAmount () {
        return this.amount;
    }

    public void setAmount (Double amount) {
        this.amount = amount;
    }

    public Long getTimeStamp () {
        return this.timeStamp;
    }

    public void setTimeStamp (Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public boolean equals (Object o) {
        if ( this == o ) return true;
        if ( !(o instanceof Transaction) ) return false;

        Transaction that = (Transaction) o;

        if ( getAmount() != null ? !getAmount().equals(that.getAmount()) : that.getAmount() != null ) return false;
        return getTimeStamp() != null ? getTimeStamp().equals(that.getTimeStamp()) : that.getTimeStamp() == null;

    }

    @Override
    public int hashCode () {
        int result = getAmount() != null ? getAmount().hashCode() : 0;
        result = 31 * result + (getTimeStamp() != null ? getTimeStamp().hashCode() : 0);
        return result;
    }
}
