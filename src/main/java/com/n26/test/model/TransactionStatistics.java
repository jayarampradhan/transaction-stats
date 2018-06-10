package com.n26.test.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionStatistics implements Serializable {

    private static final long serialVersionUID = 9098681098117182010L;
    private Double sum;
    @XmlElement(name = "avg")
    private Double average;
    private Double max;
    private Double min;
    private Long count;

    public Double getSum () {
        return this.sum;
    }

    public void setSum (Double sum) {
        this.sum = sum;
    }

    public Double getAverage () {
        return this.average;
    }

    public void setAverage (Double average) {
        this.average = average;
    }

    public Double getMax () {
        return this.max;
    }

    public void setMax (Double max) {
        this.max = max;
    }

    public Double getMin () {
        return this.min;
    }

    public void setMin (Double min) {
        this.min = min;
    }

    public Long getCount () {
        return this.count;
    }

    public void setCount (Long count) {
        this.count = count;
    }
}
