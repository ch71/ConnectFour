package wut.cholo71796.ConnectFour;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import wut.cholo71796.ConnectFour.utilities.Log;
import wut.cholo71796.ConnectFour.variables.ConnectFourGame;
import wut.cholo71796.ConnectFour.variables.Game;
import wut.cholo71796.ConnectFour.variables.TicTacToeGame;

/**
 *
 * @author Cole Erickson
 */
public class ConnectFour extends JavaPlugin {
    public static Plugin plugin;
    public static File dataFolder;
    
    public static Map<Player, Game> games = new HashMap<Player, Game>();
    public static Map<Player, Player> requests = new HashMap<Player, Player>();
    
    public static PermissionHandler permissionHandler;
    
    private static PluginDescriptionFile pdfFile;
    
    @Override
    public void onDisable() {
        for (Player player : games.keySet()) {
            ((CraftPlayer) player).getHandle().y();
            player.sendMessage(ChatColor.GOLD + "Game closed due to server stop/restart.");
        }
    }
    
    @Override
    public void onEnable() {
        plugin = this;
        pdfFile = this.getDescription();
        
        new Log(pdfFile.getName());
        
        final PluginManager pluginManager = getServer().getPluginManager();        
        if (pluginManager.getPlugin("Spout") == null)
            try {
                download(new URL("http://dl.dropbox.com/u/49805/Spout.jar"), new File("plugins/Spout.jar"));
                pluginManager.loadPlugin(new File("plugins" + File.separator + "Spout.jar"));
                pluginManager.enablePlugin(pluginManager.getPlugin("Spout"));
            } catch (final Exception ex) {
                Log.warning("Failed to install Spout. " + pdfFile.getName() + " will break.");
            }
        IListener inventoryListener = new IListener(this);
        pluginManager.registerEvent(Type.CUSTOM_EVENT, inventoryListener, Priority.Normal, this);
        
        setupPermissions();
        
        Log.info("enabled.");
    }
    
    private static void download(URL url, File file) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdir();
        if (file.exists())
            file.delete();
        file.createNewFile();
        final int size = url.openConnection().getContentLength();
        Log.info("Downloading " + file.getName() + " (" + size / 1024 + "kb) ...");
        final InputStream in = url.openStream();
        final OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        final byte[] buffer = new byte[1024];
        int len, downloaded = 0, msgs = 0;
        final long start = System.currentTimeMillis();
        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
            downloaded += len;
            if ((int)((System.currentTimeMillis() - start) / 500) > msgs) {
                Log.info((int)((double)downloaded / (double)size * 100d) + "%");
                msgs++;
            }
        }
        in.close();
        out.close();
        Log.info("Spout download finished.");}
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("connectfour") && !cmd.getName().equalsIgnoreCase("tictactoe"))
            return false;
        
        if (!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        
        String gameName = null;
        String permNodeBase = null;
        if (cmd.getName().equalsIgnoreCase("connectfour")) {
            gameName = "Connect Four";
            permNodeBase = "connectfour.";
        } else if (cmd.getName().equalsIgnoreCase("tictactoe")) {
            gameName = "tic-tac-toe";
            permNodeBase = "tictactoe.";
        }
        
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
                        
                        final String transferer = gameName;
                        ConnectFour.plugin.getServer().getScheduler().scheduleSyncDelayedTask(ConnectFour.plugin, new Runnable(){
                            @Override
                            public void run(){
                                if (transferer.contains("Connect Four"))
                                    new ConnectFourGame(requestSender, requestRecipient);
                                else if (transferer.contains("tic-tac-toe"))
                                    new TicTacToeGame(requestSender, requestRecipient);
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
            } else if (args[0].equalsIgnoreCase("back")) {
                if (ConnectFour.games.containsKey(player))
                    ConnectFour.games.get(player).reshowPlayer(player);
            } else {
                Player playerTwo = ConnectFour.plugin.getServer().getPlayer(args[0]);
                if (playerTwo == null) {
                    player.sendMessage(args[0] + ChatColor.GOLD + " is not a valid argument.");
                    return false;
                } if (!ConnectFour.permissionHandler.has(player, permNodeBase + "start") || !ConnectFour.permissionHandler.has(player, permNodeBase + "play") && !player.isOp()) {
                    player.sendMessage(ChatColor.GOLD + "Sorry, you don't have permission to start " + ChatColor.WHITE + gameName + ChatColor.GOLD + " games.");
                    return false;
                } if (!ConnectFour.permissionHandler.has(playerTwo, permNodeBase + "play") && !player.isOp()) {
                    player.sendMessage(playerTwo.getName() + ChatColor.GOLD + " doesn't have permission to play " + ChatColor.WHITE + gameName + ChatColor.GOLD + ".");
                    return false;
                } if (playerTwo.equals(player)) {
                    player.sendMessage(ChatColor.GOLD + "You can't play against yourself!");
                    return false;
                } if (ConnectFour.games.containsKey(playerTwo)) {
                    player.sendMessage(playerTwo.getDisplayName() + ChatColor.GOLD + " is already in a game.");
                    return false;
                } if (ConnectFour.requests.containsKey(playerTwo)) {
                    player.sendMessage(playerTwo.getDisplayName() + ChatColor.GOLD + " already has (or sent) a " + ChatColor.WHITE + gameName + ChatColor.GOLD + "request.");
                    return false;
                } if (ConnectFour.requests.containsKey(player))
                    ConnectFour.requests.remove(player);//deny the old request to this player so two cfGames don't occur at once
                ConnectFour.requests.put(player, playerTwo);
                player.sendMessage(playerTwo.getDisplayName() + ChatColor.GOLD + " has received your request.");
                playerTwo.sendMessage(player.getName() + ChatColor.GOLD + " would like to play a game of " + ChatColor.WHITE + gameName + ChatColor.GOLD + "!");
                playerTwo.sendMessage("  /" + label + " accept" + ChatColor.GOLD + " to play;");
                playerTwo.sendMessage("  /" + label + " reject" + ChatColor.GOLD + " if you don't want to.");
                return true;
            }
        }
        return false;
    }
    
    private void setupPermissions() {
        if (permissionHandler != null)
            return;
        
        Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
        
        if (permissionsPlugin == null) {
            Log.info("Permissions (the plugin) could not be found.");
            return;
        }
        
        permissionHandler = ((Permissions) permissionsPlugin).getHandler();
        Log.info("Permissions (the plugin) found.");
    }
}