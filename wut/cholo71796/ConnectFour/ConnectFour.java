package wut.cholo71796.ConnectFour;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import wut.cholo71796.ConnectFour.utilities.Log;
import wut.cholo71796.ConnectFour.games.ConnectFourGame;
import wut.cholo71796.ConnectFour.games.Game;
import wut.cholo71796.ConnectFour.games.TicTacToeGame;
import wut.cholo71796.ConnectFour.utilities.Request;
import wut.cholo71796.register.payment.Method;

/**
 *
 * @author Cole Erickson
 */
public class ConnectFour extends JavaPlugin {
    public static Plugin plugin;
    public static File dataFolder;
    public static PluginDescriptionFile pdfFile;
    
    public static Map<Player, Game> games = new HashMap<Player, Game>();
    public static List<Request> requests = new ArrayList<Request>();
    public static Method Method = null;
    
    private PluginManager pluginManager;
    
    @Override
    public void onDisable() {
        for (Player player : games.keySet()) {
            ((CraftPlayer) player).getHandle().y();
            Config.sendCloseOnReload(player);
        }
    }
    
    @Override
    public void onEnable() {
        plugin = this;
        pdfFile = this.getDescription();
        dataFolder = this.getDataFolder();
        
        new Log(pdfFile.getName());
        new Config();
        
        pluginManager = getServer().getPluginManager();
        pluginManager.registerEvent(Event.Type.PLUGIN_ENABLE, new SListener(), Priority.Monitor, this);
        pluginManager.registerEvent(Event.Type.PLUGIN_DISABLE, new SListener(), Priority.Monitor, this);
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
        String commandBase = null;
        if (cmd.getName().equalsIgnoreCase("connectfour")) {
            gameName = Config.getConnectFourName();
            commandBase = "/cf";
            permNodeBase = "connectfour.";
        } else if (cmd.getName().equalsIgnoreCase("tictactoe")) {
            gameName = Config.getTicTacToeName();
            commandBase = "/tic";
            permNodeBase = "tictactoe.";
        }
        
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("accept")) {
            for (final Request entry : requests) {
                if (entry.getReceiver().equals(player)) {
                    final Player requestSender = (Player) entry.getSender();
                    final Player requestReceiver = (Player) entry.getReceiver();
                    Config.sendAcceptReceiver(requestReceiver, requestSender, requestReceiver);
                    Config.sendAcceptSender(requestSender, requestSender, requestReceiver);
                    
                    final String transferer = gameName;
                    ConnectFour.plugin.getServer().getScheduler().scheduleSyncDelayedTask(ConnectFour.plugin, new Runnable(){
                        @Override
                        public void run(){
                            if (transferer.contains(Config.getConnectFourName()))
                                new ConnectFourGame(requestSender, requestReceiver, entry.getStakes());
                            else if (transferer.contains(Config.getTicTacToeName()))
                                new TicTacToeGame(requestSender, requestReceiver, entry.getStakes());
                            requests.remove(entry);
                        }}, 15L);
                    return true; //only one entry, don't waste checks
                }
            }
            Config.sendAcceptError(player);
        } else if (args[0].equalsIgnoreCase("reject")) {
            for (Request entry : requests) {
                if (entry.getReceiver().equals(player)) {
                    Player requestSender = entry.getSender();
                    Player requestReceiver = entry.getReceiver();                    
                    Config.sendRejectSender(requestSender, requestReceiver);
                    Config.sendRejectReceiver(requestReceiver, requestSender);                    
                    requests.remove(entry);
                    return true; //only one entry, don't waste checks
                }
            }
            Config.sendRejectError(player);
        } else if (args[0].equalsIgnoreCase("back")) {
            if (ConnectFour.games.containsKey(player))
                ConnectFour.games.get(player).reshowPlayer(player);
        } else {
            Player playerTwo = ConnectFour.plugin.getServer().getPlayer(args[0]);
            if (playerTwo == null) {
                Config.sendRequestNoPlayer(player, args[0]);
                return false;
            }
            if (playerTwo.equals(player)) {
                Config.sendRequestNoSelf(player);
                return false;
            } if (ConnectFour.games.containsKey(playerTwo)) {
                Config.sendRequestAlreadyPlaying(player, playerTwo, ConnectFour.games.get(playerTwo).getName());
                return false;
            } if (!player.hasPermission(permNodeBase + "play") || !player.hasPermission(permNodeBase + "start")) {
                Config.sendRequestNoStart(player, gameName);
                return false;
            } if (!playerTwo.hasPermission(permNodeBase + "play")) {
                Config.sendRequestNoOther(player, playerTwo, gameName);
                return false;
            }
            for (Request entry : requests) {
                if (entry.getReceiver().equals(playerTwo) || entry.getSender().equals(playerTwo)) {
                    Config.sendRequestAlreadyRequested(player, playerTwo, entry.getType());
                    return false;
                } else if (entry.getSender().equals(player))
                    requests.remove(entry);
            }
            if (args.length == 1) {
                ConnectFour.requests.add(new Request(player, playerTwo, gameName));
                Config.sendRequestSender(player, player, playerTwo, gameName);
                Config.sendRequestReceiver(playerTwo, player, playerTwo, gameName, commandBase);
                return true;
            } else if (args.length == 2) {
                double stakes;
                try {
                    stakes = Double.parseDouble(args[1]);
                } catch (Exception e) {
                    Config.sendInvalidNumber(player, args[1]);
                    return false;
                }
                if (!Method.hasAccount(player.getDisplayName()) || Method.getAccount(player.getName()).balance() - stakes < 0.0) {
                    Config.sendRequestFundsSender(player, stakes);
                    return false;
                } else if (!Method.hasAccount(playerTwo.getDisplayName()) ||Method.getAccount(playerTwo.getName()).balance() - stakes < 0.0) {
                    Config.sendRequestFundsReceiver(player, playerTwo, stakes);
                    return false;
                }
                ConnectFour.requests.add(new Request(player, playerTwo, gameName, stakes));
                Config.sendRequestStakesSender(player, player, playerTwo, gameName);
                Config.sendRequestStakesReceiver(playerTwo, player, playerTwo, gameName, commandBase, stakes);
                return true;
            }
        }
        return false;
    }
}