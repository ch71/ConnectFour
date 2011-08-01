/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.ConnectFour.variables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.server.Block;
import net.minecraft.server.InventoryLargeChest;
import net.minecraft.server.ItemStack;
import org.bukkit.entity.Player;
import wut.cholo71796.ConnectFour.ConnectFour;

/**
 *
 * @author Cole Erickson
 */
public class ConnectFourGame {
    private VirtualDoubleChest chest;
    private InventoryLargeChest inventory;
    private Player playerOne;
    private Player playerTwo;
    private Player turn;
    private Player winner;
    private ItemStack winnerCoin;
    private boolean won = false;
    private boolean playerOneClosed;
    private boolean playerTwoClosed;
    private Set<Integer> winningSlots = new HashSet<Integer>();
    
    private static final ItemStack redCoin = new ItemStack(Block.WOOL, 1, 14);
    private static final ItemStack yellowCoin = new ItemStack(Block.WOOL, 1, 4);
    private static final ItemStack greenCoin = new ItemStack(Block.WOOL, 1, 5);
    private static final ItemStack placeholderCoin = new ItemStack(Block.COAL_ORE, 1, 15);
    
    public ConnectFourGame(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.turn = playerTwo;
        startGame();
    }
    
    private void startGame() {
        chest = new VirtualDoubleChest("Connect Four");
        inventory = chest.getLc();
        chest.showToPlayers(playerOne, playerTwo);
        ConnectFour.games.put(playerOne, this);
        ConnectFour.games.put(playerTwo, this);
    }
    
    public void nextTurn(int slot) {
        if (turn.equals(playerOne))  {
            if (checkWin(slot, redCoin)) {
                won = true;
                win();
                return;
            }
            turn = playerTwo;
        } else {
            if (checkWin(slot, yellowCoin)) {
                won = true;
                win();
                return;
            }
            turn = playerOne;
        }
    }
    
    public boolean isPlayersTurn(Player player) {
        if (player.equals(turn))
            return true;
        return false;
    }
    
    public boolean checkWin(int slot, ItemStack coin) {
        //vertical check
        List<ItemStack> vertical = new ArrayList<ItemStack>();
        for (int i = slot; i <= slot + 27; i += 9) // no need to check above, there can't be coins there
            //            if (i <= 53 && i >= 0) //null  spaces cannot be problem, always will have block below
            if (i <= 53 && i >= 0 && inventory.getItem(i) != null)
                vertical.add(inventory.getItem(i));
            else
                vertical.add(placeholderCoin);
        if (vertical.get(0).doMaterialsMatch(coin)
                && vertical.get(1).doMaterialsMatch(coin)
                && vertical.get(2).doMaterialsMatch(coin)
                && vertical.get(3).doMaterialsMatch(coin)) {
            winningSlots.add(slot);
            winningSlots.add(slot + 9);
            winningSlots.add(slot + 18);
            winningSlots.add(slot + 27);
        }
        
        //horizontal check
        List<ItemStack> horizontal = new ArrayList<ItemStack>();
        for (int i = slot - 3; i <= slot + 3; i++) {
            if (i <= 53 && i >= 0 && inventory.getItem(i) != null)
                horizontal.add(inventory.getItem(i));
            else
                horizontal.add(placeholderCoin);
        }
        for (int i = 0 ; i <= 3 ; i++) {
            List<ItemStack> horizontalCheckerList = horizontal.subList(i, i + 4);
            if (horizontalCheckerList.get(0).doMaterialsMatch(coin)
                    && horizontalCheckerList.get(1).doMaterialsMatch(coin)
                    && horizontalCheckerList.get(2).doMaterialsMatch(coin)
                    && horizontalCheckerList.get(3).doMaterialsMatch(coin)) {
                winningSlots.add(slot + i);
                winningSlots.add(slot - 1 + i);
                winningSlots.add(slot - 2 + i);
                winningSlots.add(slot - 3 + i);
            }
        }
        
        //negative slope diagonal
        List<ItemStack> negSlopeDiagonal = new ArrayList<ItemStack>();
        for (int i = slot - 30; i <= slot + 30; i += 10) {
            if (i <= 53 && i >= 0 && inventory.getItem(i) != null)
                negSlopeDiagonal.add(inventory.getItem(i));
            else
                negSlopeDiagonal.add(placeholderCoin);
        }
        for (int i = 0 ; i <= 3 ; i++) {
            List<ItemStack> negSlopeDiagonalCheckerList = negSlopeDiagonal.subList(i, i + 4);
            if (negSlopeDiagonalCheckerList.get(0).doMaterialsMatch(coin)
                    && negSlopeDiagonalCheckerList.get(1).doMaterialsMatch(coin)
                    && negSlopeDiagonalCheckerList.get(2).doMaterialsMatch(coin)
                    && negSlopeDiagonalCheckerList.get(3).doMaterialsMatch(coin)) {
                winningSlots.add(slot + (i * 10));
                winningSlots.add(slot - 10 + (i * 10));
                winningSlots.add(slot - 20 + (i * 10));
                winningSlots.add(slot - 30 + (i * 10));
            }
        }
        
        //positive slope diagonal
        List<ItemStack> posSlopeDiagonal = new ArrayList<ItemStack>();
        for (int i = slot - 24; i <= slot + 24; i += 8) {
            if (i <= 53 && i >= 0 && inventory.getItem(i) != null)
                posSlopeDiagonal.add(inventory.getItem(i));
            else
                posSlopeDiagonal.add(placeholderCoin);
        }
        for (int i = 0 ; i <= 3 ; i++) {
            List<ItemStack> posSlopeDiagonalCheckerList = posSlopeDiagonal.subList(i, i + 4);
            if (posSlopeDiagonalCheckerList.get(0).doMaterialsMatch(coin)
                    && posSlopeDiagonalCheckerList.get(1).doMaterialsMatch(coin)
                    && posSlopeDiagonalCheckerList.get(2).doMaterialsMatch(coin)
                    && posSlopeDiagonalCheckerList.get(3).doMaterialsMatch(coin)) {
                winningSlots.add(slot + (i * 8));
                winningSlots.add(slot - 8 + (i * 8));
                winningSlots.add(slot - 16 + (i * 8));
                winningSlots.add(slot - 24 + (i * 8));
            }
        }
        if (!winningSlots.isEmpty())
            return true;
        return false;
    }
    
    public void win() {
        if (winner == playerOne)
            winnerCoin = redCoin;
        else
            winnerCoin = yellowCoin;
//        fancy border change
//        for (int i = 0 ; i <= 45; i += 9)
//            inventory.setItem(i, winnerCoin);
//        for (int i = 8 ; i <= 53; i += 9)
//            inventory.setItem(i, winnerCoin);
        
        ConnectFour.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(ConnectFour.plugin, new Runnable(){
            double j = 0;
            @Override
            public void run(){
                if (j % 2 == 0) {
                    for (int fooSlot : winningSlots)
                        inventory.setItem(fooSlot, null);
                } else {
                    for (int fooSlot : winningSlots)
                        inventory.setItem(fooSlot, winnerCoin);
                }
                j++;
            }}, 1L, 15L);
    }
    
    public Player getPlayerOne() {
        return playerOne;
    }
    
    public Player getPlayerTwo() {
        return playerTwo;
    }
    
    public boolean isWon() {
        return won;
    }
    
    public boolean didPlayersClose() {
        return playerOneClosed && playerTwoClosed;
    }
    
    public void setPlayerOneClosed(boolean playerOneClosed) {
        this.playerOneClosed = playerOneClosed;
    }
    
    public void setPlayerTwoClosed(boolean playerTwoClosed) {
        this.playerTwoClosed = playerTwoClosed;
    }
}