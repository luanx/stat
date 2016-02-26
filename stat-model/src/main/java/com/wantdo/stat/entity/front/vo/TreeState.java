package com.wantdo.stat.entity.front.vo;

/**
 * @ Date : 2015-9-15
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class TreeState {

    private Boolean checked = true;
    private Boolean disabled = true;
    private Boolean expanded = true;
    private Boolean selected = true;

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
