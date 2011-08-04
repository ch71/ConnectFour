/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.ConnectFour.variables;

import net.minecraft.server.Block;
import net.minecraft.server.InventoryLargeChest;
import net.minecraft.server.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import wut.cholo71796.ConnectFour.ConnectFour;
import wut.cholo71796.ConnectFour.utilities.Log;

/**
 *
 * @author Cole Erickson
 */
public class Game {
    private VirtualDoubleChest chest;
    
    private Player playerOne;
    private Player playerTwo;
    private Player turn;
    
    private boolean won = false;
    private boolean playerOneClosed;
    private boolean playerTwoClosed;
    
    public Player winner;
    public Player loser;
    
    public InventoryLargeChest inventory;
    public ItemStack playerOneCoin;
    public ItemStack playerTwoCoin;
    public ItemStack placeholderCoin;
    public ItemStack winnerCoin;
    
    
    public Game(Player playerOne, Player playerTwo, String name) {
        this(playerOne, playerTwo, name, new ItemStack(Block.WOOL, 1, 15), new ItemStack(Block.WOOL, 1), new ItemStack(Block.COAL_ORE, 1, 15));
    }
    
    public Game(Player playerOne, Player playerTwo, String name, ItemStack playerOneCoin, ItemStack playerTwoCoin) {
        this(playerOne, playerTwo, name, playerOneCoin, playerTwoCoin, new ItemStack(Block.COAL_ORE, 1, 15));
    }
    
    @SuppressWarnings("LeakingThisInConstructor")
    public Game(Player playerOne, Player playerTwo, String name, ItemStack playerOneCoin, ItemStack playerTwoCoin, ItemStack placeholderCoin) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.turn = playerTwo;
        
        this.playerOneCoin = playerOneCoin;
        this.playerTwoCoin = playerTwoCoin;
        this.placeholderCoin = placeholderCoin;
        
        chest = new VirtualDoubleChest(name);
        inventory = chest.getLc();
        playerOneClosed = false;
        playerTwoClosed = false;
        chest.showToPlayers(playerOne, playerTwo);
        ConnectFour.games.put(playerOne, this);
        ConnectFour.games.put(playerTwo, this);
        
        Log.info("put players");
        onStart();
    }
    
    public final void reshowPlayer(Player player) {
        if (player.equals(playerOne))
            playerOneClosed = false;
        else if (player.equals(playerTwo))
            playerTwoClosed = false;
        else
            return;
        chest.showToPlayer(player);
    }
    
    public void nextTurn(int slot) {
        if (turn.equals(playerOne))  {
            if (checkWin(slot, playerOneCoin)) {
                
                winner = playerOne;
                loser = playerTwo;
                win();
                return;
            }
            turn = playerTwo;
        } else {
            if (checkWin(slot, playerTwoCoin)) {                
                winner = playerTwo;
                loser = playerOne;
                win();
                return;
            }
            turn = playerOne;
        }
    }
    
    public void onClick(Player player, int slot, Inventory inventory) {}
    
    public boolean isPlayersTurn(Player player) {
        if (player.equals(turn))
            return true;
        return false;
    }
    
    //we <3 magic numbers
    public boolean checkWin(int slot, ItemStack coin) {
        return false;
    }
    
    public final void win() {
        won = true;
        if (winner == playerOne)
            winnerCoin = playerOneCoin;
        else if (winner == playerTwo)
            winnerCoin = playerTwoCoin;
        onWin();
    }
    
    public void onWin() {}
    public void onStart() {}
    
    public Player getPlayerOne() {
        return playerOne;
    }
    
    public Player getPlayerTwo() {
        return playerTwo;
    }
    
    public boolean isWon() {
        return won;
    }
    
    public boolean isPlayerOneClosed() {
        return playerOneClosed;
    }
    
    public boolean isPlayerTwoClosed() {
        return playerTwoClosed;
    }
    
    public void setPlayerOneClosed(boolean playerOneClosed) {
        this.playerOneClosed = playerOneClosed;
    }
    
    public void setPlayerTwoClosed(boolean playerTwoClosed) {
        this.playerTwoClosed = playerTwoClosed;
    }
}