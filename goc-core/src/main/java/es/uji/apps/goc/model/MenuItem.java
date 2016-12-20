package es.uji.apps.goc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class MenuItem
{
    private String id;
    private String title;
    private String text;
    private String leaf;
    private String checked;

    @JsonProperty("row")
    private List<MenuItem> row;

    public MenuItem()
    {
        this.row = new ArrayList<>();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getLeaf()
    {
        return leaf;
    }

    public void setLeaf(String leaf)
    {
        this.leaf = leaf;
    }

    public String getChecked()
    {
        return checked;
    }

    public void setChecked(String checked)
    {
        this.checked = checked;
    }

    public List<MenuItem> getRow()
    {
        return row;
    }

    public void setRow(List<MenuItem> row)
    {
        this.row = row;
    }
}