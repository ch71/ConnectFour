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
import wut.cholo71796.ConnectFour.variables.TicTacToeGame;

/**
 *
 * @author Cole Erickson
 */
public class IListener extends InventoryListener {
    public static ConnectFour plugin;
    public IListener(ConnectFour instance) {
        plugin = instance;
    }
    
    private ConnectFourGame cfGame;
    private TicTacToeGame ticGame;
    
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (ConnectFour.cfGames.containsKey(event.getPlayer()))
            connectFourClick(event);
        else if (ConnectFour.ticGames.containsKey(event.getPlayer()))
            ticTacToeClick(event);
        else
            return;
    }
    
    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getName().equals("Connect Four")) {
            cfGame = ConnectFour.cfGames.get(event.getPlayer());
            Player playerOne = cfGame.getPlayerOne();
            Player playerTwo = cfGame.getPlayerTwo();
            ((CraftPlayer)playerOne).getHandle().y();
            ((CraftPlayer)playerTwo).getHandle().y();
            ConnectFour.cfGames.remove(playerOne);
            ConnectFour.cfGames.remove(playerTwo);
        } else if (event.getInventory().getName().equals("Tic-Tac-Toe")) {
            ticGame = ConnectFour.ticGames.get(event.getPlayer());
            Player playerOne = ticGame.getPlayerOne();
            Player playerTwo = ticGame.getPlayerTwo();
            ((CraftPlayer)playerOne).getHandle().y();
            ((CraftPlayer)playerTwo).getHandle().y();
            ConnectFour.ticGames.remove(playerOne);
            ConnectFour.ticGames.remove(playerTwo);
        }
    }
    
    private void connectFourClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        Player player = event.getPlayer();
        event.setCancelled(true);
        int slot = event.getSlot();
        if (slot == -999)
            return;
        if (event.isShiftClick())
            return;
        cfGame = ConnectFour.cfGames.get(player);
        if (cfGame.isWon())
            return;
        if (cfGame.isPlayersTurn(player)) {
            if (inventory.getName().equals("Connect Four")) {
                int i = slot % 9 + 45;
                while (!inventory.getItem(i).getType().equals(Material.AIR) && i > slot)
                    i -= 9;
                if (inventory.getItem(i).getType().equals(Material.AIR)) {
                    if (cfGame.getPlayerOne() == player)
                        inventory.setItem(i, new ItemStack(Material.WOOL, 1, DyeColor.RED.getData()));
                    else if (cfGame.getPlayerTwo() == player)
                        inventory.setItem(i, new ItemStack(Material.WOOL, 1, DyeColor.YELLOW.getData()));
                    cfGame.nextTurn(i);
                }
            }
        }
    }
    
    private void ticTacToeClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        Player player = event.getPlayer();
        event.setCancelled(true);
        int slot = event.getSlot();
        if (slot == -999)
            return;
        int column = slot % 9;
        if (event.isShiftClick())
            return;
        ticGame = ConnectFour.ticGames.get(player);
        if (ticGame.isWon())
            return;
        if (ticGame.isPlayersTurn(player)) {
            if (inventory.getName().equals("Tic-Tac-Toe")) {
                if (inventory.getItem(slot).getType().equals(Material.AIR) && column >= 3 && column <= 5) {
                    if (ticGame.getPlayerOne() == player)
                        inventory.setItem(slot, new ItemStack(Material.WOOL, 1, DyeColor.BLACK.getData()));
                    else if (ticGame.getPlayerTwo() == player)
                        inventory.setItem(slot, new ItemStack(Material.WOOL, 1, DyeColor.WHITE.getData()));
                    ticGame.nextTurn(slot);
                }
            }
        }
    }
}
