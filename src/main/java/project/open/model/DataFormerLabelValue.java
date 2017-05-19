package project.open.model;

/**
 * Created by Errol on 17/5/17.
 */
public class DataFormerLabelValue {

    private String label;
    private String value;

    public DataFormerLabelValue(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
