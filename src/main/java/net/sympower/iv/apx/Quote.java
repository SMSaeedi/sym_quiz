package net.sympower.iv.apx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Quote {
    private String market;
    @JsonProperty("date_applied")
    private long dateApplied;
    private List<Value> values;

    public Quote() {
    }
}

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
class Value {
    @JsonProperty("tLabel")
    private String tLabel;

    @JsonProperty("cLabel")
    private String cLabel;

    @JsonProperty("value")
    private String value;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("position")
    private int position;

    @JsonProperty("chartShow")
    private boolean chartShow;

    @JsonProperty("chartType")
    private String chartType;

    @JsonProperty("color")
    private String color;

    @JsonProperty("precision")
    private int precision;

    public Value() {
    }

    public Value(String tLabel, String value) {
        this.tLabel = tLabel;
        this.value = value;
    }
}

@Setter
@Getter
class QuoteWrapper {
    private List<Quote> quote;

    public QuoteWrapper() {
    }

    public QuoteWrapper(List<Quote> es) {
        this.quote = es;
    }
}