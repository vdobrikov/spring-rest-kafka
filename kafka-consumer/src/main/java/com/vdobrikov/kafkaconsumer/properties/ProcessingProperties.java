package com.vdobrikov.kafkaconsumer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Validated
@ConfigurationProperties(prefix = "processing")
public class ProcessingProperties {
    private static final float DEFAULT_TAX_PERCENT = 10f;

    @Min(0)
    @Max(100)
    private float taxPercent = DEFAULT_TAX_PERCENT;

    public float getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(float taxPercent) {
        this.taxPercent = taxPercent;
    }
}
