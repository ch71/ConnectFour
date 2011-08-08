/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.ConnectFour;

import java.io.File;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

/**
 *
 * @author Cole Erickson
 */
public class Config {
    
    private static Configuration config;
    private static File configfile;
    
    // CONFIGURATION VARIABLES //
    //messages
    private static String connectFourName;
    private static String ticTacToeName;
    private static String gameWin;
    private static String gameWithStakesWin;
    private static String gameForfeit;
    private static String gameWithStakesForfeit;
    private static String closeOnReload;
    private static String acceptError;
    private static String backPrompt;
    private static String acceptReceiver;
    private static String acceptSender;
    private static String rejectError;
    private static String rejectReceiver;
    private static String rejectSender;
    private static String requestNoPlayer;
    private static String requestNoStart;
    private static String requestNoOther;
    private static String requestNoSelf;
    private static String requestAlreadyPlaying;
    private static String requestAlreadyRequested;
    private static String requestSenderMsg;
    private static String requestReceiverMsg;
    private static String requestInvalidNumber;
    private static String requestStakesSender;
    private static String requestStakesReceiver;
    private static String requestFundsSender;
    private static String requestFundsReceiver;
    
    public Config() {
        configfile = new File(ConnectFour.dataFolder, "config.yml");
        
        //horrendous message handling...
        
        if (!configfile.exists()){
            ConnectFour.dataFolder.mkdirs();
            config = new Configuration(configfile);
            //messages
            config.setProperty("messages.game.connectfour.name", "Connect Four");
            config.setProperty("messages.game.tictactoe.name", "tic-tac-toe");
            config.setProperty("messages.game.win", "&f@winner &6beat &f@loser &6in a game of &f@gameName&6.");
            config.setProperty("messages.game.with stakes.win", "&f@winner &6beat &f@loser &6in a game of &f@gameName &6and got &f@stakes&6.");
            config.setProperty("messages.game.forfeit", "&f@loser &6forfeited to &f@winner &6in a game of &f@gameName&6.");
            config.setProperty("messages.game.with stakes.forfeit", "&f@loser &6forfeited to &f@winner &6in a game of &f@gameName &6and lost &f@stakes&6.");
            config.setProperty("messages.closeOnReload", "&6Game closed due to server stop/reload.");
            config.setProperty("messages.commands.accept.error", "&6No request detected.");
            config.setProperty("messages.commands.accept.success.request receiver", "&6Game against &f@requestSender &6starting...");
            config.setProperty("messages.commands.accept.success.request sender", "&6Game against &f@requestReceiver &6starting...");
            config.setProperty("messages.commands.reject.error", "&6No request detected.");
            config.setProperty("messages.commands.reject.success.request receiver", "&6Rejected request from &f@requestSender&6.");
            config.setProperty("messages.commands.reject.success.request sender", "&f@requestReceiver &6rejected your request.");
            config.setProperty("messages.commands.request.error.no such player", "&f@commandArgs &6is not a valid argument.");
            config.setProperty("messages.commands.request.error.no start perms", "&6You don't have permission to start &f@gameName &6games.");
            config.setProperty("messages.commands.request.error.other cant play", "&f@otherPlayer &6doesn't have permission to play &f@gameName&6.");
            config.setProperty("messages.commands.request.error.player is self", "&6You can't play against yourself!");
            config.setProperty("messages.commands.request.error.already playing", "&f@otherPlayer &6is already in a game.");
            config.setProperty("messages.commands.request.error.already requested", "&f@otherPlayer &6already has (or sent) a &f@gameName &6request.");
            config.setProperty("messages.commands.request.success.request sender", "&f@requestReceiver &6has received your request.");
            config.setProperty("messages.commands.request.success.request receiver", "&f@requestSender &6would like to play a game of &f@gameName&6!"
                    + "@newLine  &f@commandBase accept &6to play;"
                    + "@newLine  &f@commandBase reject &6if you don't want to.");
            config.setProperty("messages.commands.back.prompt", "&6To prevent forfeiture, type &f@command&6.");
            config.setProperty("messages.commands.request.with stakes.error.invalid number", "&f@number &6is not a valid number.");
            config.setProperty("messages.commands.request.with stakes.success.request sender", "&f@requestReceiver &6has received your request.");
            config.setProperty("messages.commands.request.with stakes.success.request receiver", "&f@requestSender &6would like to play a game of &f@gameName&6!"
                    + "@newLine  &f@commandBase accept &6to play;"
                    + "@newLine  &f@commandBase reject &6if you don't want to."
                    + "@newLine&6The loser will pay the winner &f@stakes&6.");
            config.setProperty("messages.commands.request.with stakes.error.sender lacks fund", "&6You don't have &f@funds&6.");
            config.setProperty("messages.commands.request.with stakes.error.receiver lacks fund", "&f@requestReceiver &6doesn't have &f@funds&6.");
            config.save();
        } else
            config = new Configuration(configfile);
        config.load();
        connectFourName = convert(config.getString("messages.game.connectfour.name", "Connect Four"));
        ticTacToeName = convert(config.getString("messages.game.tictactoe.name", "tic-tac-toe"));
        gameWin = convert(config.getString("messages.game.win", "&f@winner &6beat &f@loser &6in a game of &f@gameName&6."));
        gameWithStakesWin = convert(config.getString("messages.game.with stakes.win", "&f@winner &6beat &f@loser &6in a game of &f@gameName &6and got &f@stakes&6."));
        gameForfeit = convert(config.getString("messages.game.forfeit", "&f@loser &6forfeited to &f@winner &6in a game of &f@gameName&6."));
        gameWithStakesForfeit = convert(config.getString("messages.game.with stakes.forfeit", "&f@loser &6forfeited to &f@winner &6in a game of &f@gameName &6and lost &f@stakes&6."));
        closeOnReload = convert(config.getString("messages.closeOnReload", "&6Game closed due to server stop/reload."));
        acceptError = convert(config.getString("messages.commands.accept.error", "&6No request detected."));
        acceptReceiver = convert(config.getString("messages.commands.accept.success.request receiver", "&6Game against &f@requestSender &6starting..."));
        acceptSender = convert(config.getString("messages.commands.accept.success.request sender", "&6Game against &f@requestReceiver &6starting..."));
        rejectError = convert(config.getString("messages.commands.reject.error", "&6No request detected."));
        rejectReceiver = convert(config.getString("messages.commands.reject.success.request receiver", "&6Rejected request from &f@requestSender&6."));
        rejectSender = convert(config.getString("messages.commands.reject.success.request sender", "&f@requestReceiver &6rejected your request."));
        requestNoPlayer = convert(config.getString("messages.commands.request.error.no such player", "&f@commandArgs &6is not a valid argument."));
        requestNoStart = convert(config.getString("messages.commands.request.error.no start perms", "&6You don't have permission to start &f@gameName &6games."));
        requestNoOther = convert(config.getString("messages.commands.request.error.other cant play", "&f@otherPlayer &6doesn't have permission to play &f@gameName&6."));
        requestNoSelf = convert(config.getString("messages.commands.request.error.player is self", "&6You can't play against yourself!"));
        requestAlreadyPlaying = convert(config.getString("messages.commands.request.error.already playing", "&f@otherPlayer &6is already in a game."));
        requestAlreadyRequested = convert(config.getString("messages.commands.request.error.already requested", "&f@otherPlayer &6already has (or sent) a &f@gameName &6request.")); //a vs. an if add API
        requestSenderMsg = convert(config.getString("messages.commands.request.success.request sender", "&f@requestReceiver &6has received your request."));
        requestReceiverMsg = convert(config.getString("messages.commands.request.success.request receiver", "&f@requestSender &6would like to play a game of &f@gameName&6!"
                + "@newLine  &f@commandBase accept &6to play;"
                + "@newLine  &f@commandBase reject &6if you don't want to."));
        backPrompt = convert(config.getString("messages.commands.back.prompt", "&6To prevent forfeiture, type &f@command&6."));
        requestInvalidNumber = convert(config.getString("messages.commands.request.with stakes.error.invalid number", "&f@number &6is not a valid number."));
        requestStakesSender = convert(config.getString("messages.commands.request.with stakes.success.request sender", "&f@requestReceiver &6has received your request."));
        requestStakesReceiver = convert(config.getString("messages.commands.request.with stakes.success.request receiver", "&f@requestSender &6would like to play a game of &f@gameName&6!"
                + "@newLine  &f@commandBase accept &6to play;"
                + "@newLine  &f@commandBase reject &6if you don't want to."
                + "@newLine&6The loser will pay the winner &f@stakes&6."));
        requestFundsSender = convert(config.getString("messages.commands.request.with stakes.error.sender lacks fund", "&6You don't have &f@funds&6."));
        requestFundsReceiver = convert(config.getString("messages.commands.request.with stakes.error.receiver lacks fund", "&f@requestReceiver &6doesn't have &f@funds&6."));
    }
    
    public static void sendAcceptError(Player player) {
        player.sendMessage(acceptError);
    }
    
    public static void sendAcceptReceiver(Player player, Player requestSender, Player requestReceiver) {
        player.sendMessage(acceptReceiver.replaceAll("@requestSender", ChatColor.stripColor(requestSender.getDisplayName())).replaceAll("@requestReceiver", ChatColor.stripColor(requestReceiver.getDisplayName())));
    }
    
    public static void sendAcceptSender(Player player, Player requestSender, Player requestReceiver) {
        player.sendMessage(acceptSender.replaceAll("@requestSender", ChatColor.stripColor(requestSender.getDisplayName())).replaceAll("@requestReceiver", ChatColor.stripColor(requestReceiver.getDisplayName())));
    }
    
    public static void sendBackPrompt(Player player, String commandPrefix) {
        player.sendMessage(backPrompt.replaceAll("@command", "/" + commandPrefix + " back"));
    }
    
    public static void sendCloseOnReload(Player player) {
        player.sendMessage(closeOnReload);
    }
    
    public static void sendGameWin(Player player, Player winner, Player loser, String gameType) {
        player.sendMessage(gameWin.replaceAll("@winner", ChatColor.stripColor(winner.getDisplayName())).replaceAll("@loser", ChatColor.stripColor(loser.getDisplayName())).replaceAll("@gameName", gameType));
    }
    
    public static void sendGameWithStakesWin(Player player, Player winner, Player loser, String gameType, double stakes) {
        player.sendMessage(gameWithStakesWin.replaceAll("@winner", ChatColor.stripColor(winner.getDisplayName())).replaceAll("@loser", ChatColor.stripColor(loser.getDisplayName())).replaceAll("@gameName", gameType).replaceAll("@stakes", ConnectFour.Method.format(stakes)));
    }
    
    public static void sendGameForfeit(Player player, Player winner, Player loser, String gameType) {
        player.sendMessage(gameForfeit.replaceAll("@winner", ChatColor.stripColor(winner.getDisplayName())).replaceAll("@loser", ChatColor.stripColor(loser.getDisplayName())).replaceAll("@gameName", gameType));
    }
    
    public static void sendGameWithStakesForfeit(Player player, Player winner, Player loser, String gameType, double stakes) {
        player.sendMessage(gameWithStakesForfeit.replaceAll("@winner", ChatColor.stripColor(winner.getDisplayName())).replaceAll("@loser", ChatColor.stripColor(loser.getDisplayName())).replaceAll("@gameName", gameType).replaceAll("@stakes", ConnectFour.Method.format(stakes)));
    }
    
    public static void sendRejectError(Player player) {
        player.sendMessage(rejectError);
    }
    
    public static void sendRejectReceiver(Player requestReceiver, Player requestSender) {
        requestReceiver.sendMessage(rejectReceiver.replaceAll("@requestSender", ChatColor.stripColor(requestSender.getDisplayName())).replaceAll("@requestReceiver", ChatColor.stripColor(requestReceiver.getDisplayName())));
    }
    
    public static void sendRejectSender(Player requestSender, Player requestReceiver) {
        requestSender.sendMessage(rejectSender.replaceAll("@requestSender", ChatColor.stripColor(requestSender.getDisplayName())).replaceAll("@requestReceiver", ChatColor.stripColor(requestReceiver.getDisplayName())));
    }
    
    public static void sendRequestAlreadyPlaying(Player player, Player otherPlayer, String gameName) {
        player.sendMessage(requestAlreadyPlaying.replaceAll("@otherPlayer", ChatColor.stripColor(otherPlayer.getDisplayName())).replaceAll("@gameName", gameName));
    }
    
    public static void sendRequestAlreadyRequested(Player player, Player otherPlayer, String gameName) {
        player.sendMessage(requestAlreadyRequested.replaceAll("@otherPlayer", ChatColor.stripColor(otherPlayer.getDisplayName())).replaceAll("@gameName", gameName));
    }
    
    public static void sendRequestNoOther(Player player, Player otherPlayer, String gameName) {
        player.sendMessage(requestNoOther.replaceAll("@otherPlayer", ChatColor.stripColor(otherPlayer.getDisplayName())).replaceAll("@gameName", gameName));
    }
    
    public static void sendRequestNoPlayer(Player player, String commandArgs) {
        player.sendMessage(requestNoPlayer.replaceAll("@commandArgs", commandArgs));
    }
    
    public static void sendRequestNoSelf(Player player) {
        player.sendMessage(requestNoSelf);
    }
    
    public static void sendRequestNoStart(Player player, String gameName) {
        player.sendMessage(requestNoStart.replaceAll("@gameName", gameName));
    }
    
    public static void sendRequestReceiver(Player player, Player requestSender, Player requestReceiver, String gameType, String commandBase) {
        for (String part : requestReceiverMsg.split("@newLine"))
            player.sendMessage(part.replaceAll("@requestSender", ChatColor.stripColor(requestSender.getDisplayName())).replaceAll("@requestReceiver", ChatColor.stripColor(requestReceiver.getDisplayName())).replaceAll("@gameName", gameType).replaceAll("@commandBase", commandBase));
    }
    
    public static void sendRequestSender(Player player, Player requestSender, Player requestReceiver, String gameType) {
        player.sendMessage(requestSenderMsg.replaceAll("@requestSender", ChatColor.stripColor(requestSender.getDisplayName())).replaceAll("@requestReceiver", ChatColor.stripColor(requestReceiver.getDisplayName())).replaceAll("@gameName", gameType));
    }
    
    public static String getConnectFourName() {
        return connectFourName;
    }
    
    public static String getTicTacToeName() {
        return ticTacToeName;
    }
    
    public static void sendRequestStakesReceiver(Player player, Player requestSender, Player requestReceiver, String gameType, String commandBase, double stakes) {
        for (String part : requestStakesReceiver.split("@newLine")) {
            player.sendMessage(part.replaceAll("@requestSender", ChatColor.stripColor(requestSender.getDisplayName())).replaceAll("@requestReceiver", ChatColor.stripColor(requestReceiver.getDisplayName())).replaceAll("@gameName", gameType).replaceAll("@commandBase", commandBase).replaceAll("@stakes", "\\" + ConnectFour.Method.format(stakes)));
        }
    }
    public static void sendRequestStakesSender(Player player, Player requestSender, Player requestReceiver, String gameType) {
        player.sendMessage(requestStakesSender.replaceAll("@requestSender", ChatColor.stripColor(requestSender.getDisplayName())).replaceAll("@requestReceiver", ChatColor.stripColor(requestReceiver.getDisplayName())).replaceAll("@gameName", gameType));
    }
    
    public static void sendInvalidNumber(Player player, String number) {
        player.sendMessage(requestInvalidNumber.replaceAll("@number", number));
    }
    
    public static void sendRequestFundsSender(Player requestSender, double funds) {
        requestSender.sendMessage(requestFundsSender.replaceAll("@funds", ConnectFour.Method.format(funds)));
    }
    
    public static void sendRequestFundsReceiver(Player requestSender, Player requestReceiver, double funds) {
        requestSender.sendMessage(requestFundsReceiver.replaceAll("@funds", ConnectFour.Method.format(funds)).replaceAll("@requestReceiver", ChatColor.stripColor(requestReceiver.getDisplayName())));
    }
    
    private static String convert(String s){
        if(s == null)
            return null;
        s = s.replaceAll("&([0-9a-f])", "\u00A7$1");
        return s;
    }
}