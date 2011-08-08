package wut.cholo71796.ConnectFour;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import wut.cholo71796.ConnectFour.utilities.Log;
import wut.cholo71796.register.payment.Methods;

public class SListener extends ServerListener {
    private Methods Methods = null;

    public SListener() {
        this.Methods = new Methods();
    }

    @Override
    public void onPluginDisable(PluginDisableEvent event) {
        // Check to see if the plugin thats being disabled is the one we are using
        if (this.Methods != null && this.Methods.hasMethod()) {
            Boolean check = this.Methods.checkDisabled(event.getPlugin());

            if(check) {
                ConnectFour.Method = null;
            }
        }
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        // Check to see if we need a payment method
        if (!this.Methods.hasMethod()) {
            if(this.Methods.setMethod(event.getPlugin())) {
                // You might want to make this a public variable inside your MAIN class public Method Method = null;
                // then reference it through this.plugin.Method so that way you can use it in the rest of your plugin ;)
                ConnectFour.Method = this.Methods.getMethod();
                Log.info("Using: " + ConnectFour.Method.getName() + " - " + ConnectFour.Method.getVersion());
            }
        }
    }
}