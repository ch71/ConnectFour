package wut.cholo71796.ConnectFour.utilities;

import java.util.logging.Logger;

public final class Log {
    private static Logger logger;
    private static String pluginName;
    
    public Log(String pluginName) {
        Log.pluginName = pluginName;
        logger = Logger.getLogger(pluginName);
    }
    
    public static void info(String message) {
        logger.info("[".concat(pluginName).concat("] ").concat(message));
    }
    
    public static void warning(String message) {
        logger.warning("[".concat(pluginName).concat("] ").concat(message));
    }
    
    public static void severe(String message) {
        logger.severe("[".concat(pluginName).concat("] ").concat(message));
    }
}
