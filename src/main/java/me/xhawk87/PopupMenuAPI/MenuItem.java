/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.xhawk87.PopupMenuAPI;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

/**
 * MenuItem
 *
 * An item in a menu. Displays like an ItemStack in an inventory, and activates
 * the onClick method when it is selected.
 *
 * @author XHawk87
 */
public abstract class MenuItem {

    private PopupMenu menu;
    private int number;
    private MaterialData icon;
    private String text;
    private List<String> descriptions = new ArrayList<>();

    /**
     * Create a new menu item with the given title text on mouse over
     *
     * Icon defaults to a piece of paper, and no number is displayed.
     *
     * @param text The title text to display on mouse over
     */
    public MenuItem(String text) {
        this(text, new MaterialData(Material.PAPER));
    }

    /**
     * Create a new menu item with the given title text on mouse over, and using
     * the given MaterialData as its icon
     *
     * @param text The title text to display on mouse over
     * @param icon The material to use as its icon
     */
    public MenuItem(String text, MaterialData icon) {
        this(text, icon, 1);
    }

    /**
     * Create a new menu item with the given title text on mouseover, using the
     * given MaterialData as its icon, and displaying the given number
     *
     * @param text The title text to display on mouse over
     * @param icon The material to use as its icon
     * @param number The number to display on the item (must be greater than 1)
     */
    public MenuItem(String text, MaterialData icon, int number) {
        this.text = text;
        this.icon = icon;
        this.number = number;
    }

    protected void addToMenu(PopupMenu menu) {
        this.menu = menu;
    }

    protected void removeFromMenu(PopupMenu menu) {
        if (this.menu == menu) {
            this.menu = null;
        }
    }

    /**
     * Get the menu on which this item resides
     *
     * @return The popup menu
     */
    public PopupMenu getMenu() {
        return menu;
    }

    /**
     * Get the number displayed on this item. 1 for no number displayed
     *
     * @return The number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Get the MaterialData used as the icon for this menu item
     *
     * @return The icon
     */
    public MaterialData getIcon() {
        return icon;
    }

    /**
     * Get the title text used as the mouse over text for this menu item
     *
     * @return The title text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the detailed description of a menu item to display on mouse over.
     *
     * Note that this does not automatically wrap text, so you must separate the
     * strings into lines
     *
     * @param lines The lines of text to display as a description
     */
    public void setDescriptions(List<String> lines) {
        descriptions = lines;
    }

    /**
     * Adds an extra line to the description of the menu item to display on
     * mouse over.
     *
     * Note that this does not automatically wrap text, so you must separate the
     * strings into multiple lines if they are too long.
     *
     * @param line The line to add to the display text description
     */
    public void addDescription(String line) {
        descriptions.add(line);
    }

    protected ItemStack getItemStack() {
        ItemStack slot = new ItemStack(getIcon().getItemType(), getNumber(), getIcon().getData());
        ItemMeta meta = slot.getItemMeta();
        meta.setLore(descriptions);
        meta.setDisplayName(getText());

        slot.setItemMeta(meta);
        return slot;
    }

    /**
     * Called when a player clicks this menu item
     * 
     * @param player The clicking player
     */
    public abstract void onClick(Player player);
}
