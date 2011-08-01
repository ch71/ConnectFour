/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.ConnectFour.commands;

import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wut.cholo71796.ConnectFour.ConnectFour;
import wut.cholo71796.ConnectFour.variables.ConnectFourGame;

/**
 *
 * @author Cole Erickson
 */
public class ConnectFourCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        
        if (args.length == 0) {
            return false;
        }
        
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("accept")) {
                for (Entry entry : ConnectFour.requests.entrySet()) {
                    if (entry.getValue().equals(player)) {
                        final Player requestSender = (Player) entry.getKey();
                        final Player requestRecipient = (Player) entry.getValue();
                        
                        requestSender.sendMessage(requestRecipient.getName() + ChatColor.GOLD + " has accepted your request-- game starting...");
                        requestRecipient.sendMessage(ChatColor.GOLD + "Game starting...");
                        ConnectFour.requests.remove(requestSender);
                        
                        ConnectFour.plugin.getServer().getScheduler().scheduleSyncDelayedTask(ConnectFour.plugin, new Runnable(){
                            @Override
                            public void run(){
                                new ConnectFourGame(requestSender, requestRecipient);
                            }}, 15L);                        
                        return true; //only one entry, don't waste checks
                    }
                }               
                player.sendMessage(ChatColor.GOLD + "No request detected.");
            } else if (args[0].equalsIgnoreCase("reject")) {
                for (Entry entry : ConnectFour.requests.entrySet()) {
                    if (entry.getValue().equals(player)) {
                        Player requestRecipient = (Player) entry.getKey();
                        Player requestSender = (Player) entry.getValue();
                        ConnectFour.requests.remove((Player) entry.getKey());
                        requestSender.sendMessage(ChatColor.GOLD + "Rejected request from " + ChatColor.WHITE + requestRecipient.getDisplayName() + ".");
                        requestRecipient.sendMessage(requestSender.getName() + ChatColor.GOLD + " has rejected your request.");
                        return true; //only one entry, don't waste checks
                    }
                }
                player.sendMessage(ChatColor.GOLD + "No request detected.");
            } else {
                Player playerTwo = ConnectFour.plugin.getServer().getPlayer(args[0]);
                if (playerTwo == null) {
                    player.sendMessage(args[0] + ChatColor.GOLD + " is not a valid argument.");
                    return false;
                } if (playerTwo.equals(player)) {
                    player.sendMessage(ChatColor.GOLD + "You can't play against yourself!");
                    return false;
                } if (ConnectFour.games.containsKey(playerTwo)) {
                    player.sendMessage(playerTwo.getDisplayName() + ChatColor.GOLD + " is already in a game.");
                    return false;
                } if (ConnectFour.requests.containsKey(playerTwo)) {
                    player.sendMessage(playerTwo.getDisplayName() + ChatColor.GOLD + " already has (or sent) a " + ChatColor.WHITE + "Connect Four " + ChatColor.GOLD + "request.");
                    return false;
                } if (ConnectFour.requests.containsKey(player))
                    ConnectFour.requests.remove(player);//deny the old request to this player so two games don't occur at once
                ConnectFour.requests.put(player, playerTwo);
                player.sendMessage(playerTwo.getDisplayName() + ChatColor.GOLD + " has received your request.");
                playerTwo.sendMessage(player.getName() + ChatColor.GOLD + " would like to play a game of Connect Four!");
                playerTwo.sendMessage("  /cf accept" + ChatColor.GOLD + " to play;");
                playerTwo.sendMessage("  /cf reject" + ChatColor.GOLD + " if you don't want to.");
                return true;
            }
        }
        return false;
    }
}