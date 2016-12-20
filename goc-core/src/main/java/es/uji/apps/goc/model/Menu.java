package es.uji.apps.goc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Menu
{
    @JsonProperty("row")
    private List<MenuItem> menuItems;

    public List<MenuItem> getMenuItems()
    {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems)
    {
        this.menuItems = menuItems;
    }
}