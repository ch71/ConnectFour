/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.ConnectFour.utilities;

import org.bukkit.entity.Player;

/**
 *
 * @author Cole Erickson
 */
public class Request {
    //todo pass this into game?
    private Player sender;
    private Player receiver;
    private String type;
    private double stakes;
    
    public Request(Player sender, Player receiver, String type) {
        this(sender, receiver, type, 0);
    }
    
    public Request(Player sender, Player receiver, String type, double stakes) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.stakes = stakes;
    }
    
    public Player getReceiver() {
        return receiver;
    }

    public Player getSender() {
        return sender;
    }

    public double getStakes() {
        return stakes;
    }
    
    public String getType() {
        return type;
    }
}
