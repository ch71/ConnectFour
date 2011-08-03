/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.ConnectFour;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.getspout.spoutapi.event.inventory.InventoryClickEvent;
import org.getspout.spoutapi.event.inventory.InventoryCloseEvent;
import org.getspout.spoutapi.event.inventory.InventoryListener;
import wut.cholo71796.ConnectFour.utilities.Log;
import wut.cholo71796.ConnectFour.variables.Game;

/**
 *
 * @author Cole Erickson
 */
public class IListener extends InventoryListener {
    public static ConnectFour plugin;
    public IListener(ConnectFour instance) {
        plugin = instance;
    }
    
    private Game game;
    private Inventory inventory;
    private String invName;
    private String commandPrefix;
    
    private Player player;
    private Player playerOne;
    private Player playerTwo;
    
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        player = event.getPlayer();
        Log.info(ConnectFour.games.toString());
        if (!ConnectFour.games.containsKey(player))
            return;
        
        game = ConnectFour.games.get(player);
        inventory = event.getInventory();
        invName = inventory.getName();

        
        event.setCancelled(true);
        int slot = event.getSlot();
        if (slot == -999)
            return;
        if (event.isShiftClick())
            return;
        if (game.isWon())
            return;
        if (game.isPlayersTurn(player)) {
            if (invName.equals("Tic-tac-toe") || invName.equals("Connect Four"))
                game.onClick(player, slot, inventory);
        }
    }
    
    @Override
    public void onInventoryClose(final InventoryCloseEvent event) {       
        player = event.getPlayer();
        if (!ConnectFour.games.containsKey(player))
            return;
        
        invName = event.getInventory().getName();
        game = ConnectFour.games.get(player);
        playerOne = game.getPlayerOne();
        playerTwo = game.getPlayerTwo();
        
        if (invName.equals("Connect Four"))
            commandPrefix = "cf";
        else if (invName.equals("Tic-tac-toe"))
            commandPrefix = "tic";
        

        if (player.equals(playerOne))
            game.setPlayerOneClosed(true);
        else if (player.equals(playerTwo))
            game.setPlayerTwoClosed(true);
        else
            Log.severe("Player considered in game s/he should not be in.");
        
        if (!game.isWon()) {
            player.sendMessage(ChatColor.GOLD + "Type "
                    + ChatColor.WHITE + "/" + commandPrefix + " back" + ChatColor.GOLD +
                    " within the next ten seconds to return.");
            
            ConnectFour.plugin.getServer().getScheduler().scheduleSyncDelayedTask(ConnectFour.plugin, new Runnable(){
                @Override
                public void run(){
                    if (player.equals(playerOne) ? game.isPlayerOneClosed() : game.isPlayerTwoClosed()) {
                        ((CraftPlayer)playerOne).getHandle().y();
                        ((CraftPlayer)playerTwo).getHandle().y();
                        ConnectFour.games.remove(playerOne);
                        ConnectFour.games.remove(playerTwo);
                        player.sendMessage(ChatColor.GOLD + "You have forfeited the game.");
                    }
                }}, 200L);}
        else {
            ((CraftPlayer)playerOne).getHandle().y();
            ((CraftPlayer)playerTwo).getHandle().y();
            ConnectFour.games.remove(playerOne);
            ConnectFour.games.remove(playerTwo);
        }
    }
}
