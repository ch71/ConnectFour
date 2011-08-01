/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.ConnectFour;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkitcontrib.event.inventory.InventoryClickEvent;
import org.bukkitcontrib.event.inventory.InventoryCloseEvent;
import org.bukkitcontrib.event.inventory.InventoryListener;
import wut.cholo71796.ConnectFour.variables.ConnectFourGame;

/**
 *
 * @author Cole Erickson
 */
public class IListener extends InventoryListener {
    public static ConnectFour plugin;
    public IListener(ConnectFour instance) {
        plugin = instance;
    }
    
    private ConnectFourGame game;
    
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        Player player = event.getPlayer();
        if (ConnectFour.games.containsKey(player))
            event.setCancelled(true);
        else
            return;
        int slot = event.getSlot();
        if (slot == -999)
            return;
        if (event.isShiftClick()) {
            return;
        }
        game = ConnectFour.games.get(player);
        if (game.isWon()) {
            return;
        }
        if (game.isPlayersTurn(player)) {
            if (inventory.getName().equals("Connect Four")) {
                int i = slot % 9 + 45;
                while (!inventory.getItem(i).getType().equals(Material.AIR) && i > slot)
                    i -= 9;
                if (inventory.getItem(i).getType().equals(Material.AIR)) {
                    if (game.getPlayerOne() == player)
                        inventory.setItem(i, new ItemStack(Material.WOOL, 1, DyeColor.RED.getData()));
                    else if (game.getPlayerTwo() == player)
                        inventory.setItem(i, new ItemStack(Material.WOOL, 1, DyeColor.YELLOW.getData()));
                    game.nextTurn(i);
                }
            }
        }
    }
    
    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getName().equals("Connect Four")) {
            game = ConnectFour.games.get(event.getPlayer());
            Player playerOne = game.getPlayerOne();
            Player playerTwo = game.getPlayerTwo();
            ((CraftPlayer)playerOne).getHandle().y();
            ((CraftPlayer)playerTwo).getHandle().y();
            ConnectFour.games.remove(playerOne);
            ConnectFour.games.remove(playerTwo);
        }
    }
}
