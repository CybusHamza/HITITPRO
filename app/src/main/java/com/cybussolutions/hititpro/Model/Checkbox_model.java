package com.cybussolutions.hititpro.Model;

/**
 * Created by Hamza Android on 4/17/2017.
 */
public class Checkbox_model {

    private String name;
    private boolean selected;

    public Checkbox_model() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
