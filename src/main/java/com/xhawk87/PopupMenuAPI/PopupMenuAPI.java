/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xhawk87.PopupMenuAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * PopupMenuAPI
 *
 * @author XHawk87
 */
public class PopupMenuAPI extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    /**
     * Create a new pop-up menu and stores it for later use
     *
     * @param title The menu title
     * @param rows The number of rows on the menu
     * @return The menu
     */
    public static PopupMenu createMenu(String title, int rows) {
        return new PopupMenu(title, rows);
    }

    /**
     * Creates an exact copy of an existing pop-up menu. This is intended to be
     * used for creating dynamic pop-up menus for individual players. Be sure to
     * call destroyMenu for menus that are no longer needed.
     *
     * @param menu The menu to clone
     * @return The cloned copy
     */
    public static PopupMenu cloneMenu(PopupMenu menu) {
        return menu.clone();
    }

    /**
     * Destroys an existing menu, and closes it for any viewers
     *
     * Please note: you should not store any references to destroyed menus
     *
     * @param menu The menu to destroy
     */
    public static void removeMenu(PopupMenu menu) {
        for (HumanEntity viewer : menu.getInventory().getViewers()) {
            if (viewer instanceof Player) {
                menu.closeMenu((Player) viewer);
            } else {
                viewer.closeInventory();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMenuItemClicked(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder() instanceof PopupMenu) {
            PopupMenu menu = (PopupMenu) inventory.getHolder();
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
                    // Quick exit for a menu, click outside of it
                    if (menu.exitOnClickOutside()) {
                        menu.closeMenu(player);
                    }
                } else {
                    int index = event.getRawSlot();
                    if (index < inventory.getSize()) {
                        menu.selectMenuItem(player, index);
                    } else {
                        // If they want to mess with their inventory they don't need to do so in a menu
                        if (menu.exitOnClickOutside()) {
                            menu.closeMenu(player);
                            player.openInventory(player.getInventory());
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMenuClosed(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Inventory inventory = event.getInventory();
            if (inventory.getHolder() instanceof PopupMenu) {
                PopupMenu menu = (PopupMenu) inventory.getHolder();
                MenuCloseBehaviour menuCloseBehaviour = menu.getMenuCloseBehaviour();
                if (menuCloseBehaviour != null) {
                    menuCloseBehaviour.onClose((Player) event.getPlayer());
                }
            }
        }
    }
}
