package wut.cholo71796.ConnectFour;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import wut.cholo71796.ConnectFour.commands.ConnectFourCommand;
import wut.cholo71796.ConnectFour.utilities.Log;
import wut.cholo71796.ConnectFour.variables.ConnectFourGame;

/**
 *
 * @author Cole Erickson
 */
public class ConnectFour extends JavaPlugin {
    public static Plugin plugin;
    public static File dataFolder;
    
    public static Map<Player, ConnectFourGame> games = new HashMap<Player, ConnectFourGame>();
    public static Map<Player, Player> requests = new HashMap<Player, Player>();
    
    private static PluginDescriptionFile pdfFile;
    
    @Override
    public void onDisable() {}
    
    @Override
    public void onEnable() {
        plugin = this;
        pdfFile = this.getDescription();
        
        new Log(pdfFile.getName());
        
        getCommand("connectfour").setExecutor(new ConnectFourCommand());
        
        final PluginManager pluginManager = getServer().getPluginManager();
        
        if (pluginManager.getPlugin("BukkitContrib") == null)
            try {
                download(new URL("http://bit.ly/autoupdateBukkitContrib"), new File("plugins/BukkitContrib.jar"));
                pluginManager.loadPlugin(new File("plugins" + File.separator + "BukkitContrib.jar"));
                pluginManager.enablePlugin(pluginManager.getPlugin("BukkitContrib"));
            } catch (final Exception ex) {
                Log.warning("Failed to install BukkitContrib. " + pdfFile.getName() + " will break.");
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
        Log.info("BukkitContrib download finished.");}
}
