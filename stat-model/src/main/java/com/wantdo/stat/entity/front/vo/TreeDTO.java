package com.wantdo.stat.entity.front.vo;

import java.io.Serializable;
import java.util.List;

/**
 * TreeView封装
 *
 * @ Date : 2015-9-15
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class TreeDTO implements Serializable{

    private static final long serialVersionUID = -8448506029512911667L;
    private Long id;

    /**
     * 列表树节点上的文本
     */
    private String text;

    /**
     * 列表树节点上的图标
     */
    private String icon;


    /**
     * 当某个节点被选择后显示的图标
     */
    private String selectedIcon;

    /**
     * 结合全局enableLinks选项为列表树节点指定URL
     */
    private String href;

    /**
     * 指定列表树的节点是否可选择
     */
    private Boolean selectedable = true;

    /**
     * 节点的前景色
     */
    private String color;

    /**
     * 节点的背景色
     */
    private String backColor;

    /**
     * 通过结合全局showTags选项来在列表树节点的右边添加额外的信息
     */
    private String[] tags;

    /**
     * 子节点
     */
    private List<TreeDTO> nodes;

    /**
     * 父节点Id
     */
    private Long pid;

    private TreeState state = new TreeState();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(String selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Boolean getSelectedable() {
        return selectedable;
    }

    public void setSelectedable(Boolean selectedable) {
        this.selectedable = selectedable;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public List<TreeDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeDTO> nodes) {
        this.nodes = nodes;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public TreeState getState() {
        return state;
    }

    public void setState(TreeState state) {
        this.state = state;
    }
}
