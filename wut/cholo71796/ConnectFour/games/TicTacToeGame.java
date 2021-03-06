/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.ConnectFour.games;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.server.Block;
import net.minecraft.server.ItemStack;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import wut.cholo71796.ConnectFour.Config;
import wut.cholo71796.ConnectFour.ConnectFour;

/**
 *
 * @author Cole Erickson
 */
public class TicTacToeGame extends Game {
    private Set<Integer> winningSlots = new HashSet<Integer>();
    
    public TicTacToeGame(Player playerOne, Player playerTwo, double stakes) {
        super(playerOne, playerTwo, Config.getTicTacToeName(), stakes);
    }
    
    @Override
    public void onStart() {
        for (int i = 2 ; i < 7 ; i++) {//top + bottom
            inventory.setItem(i, new ItemStack(Block.PISTON_EXTENSION));
            inventory.setItem(i + 36, new ItemStack(Block.PISTON_EXTENSION));
        }
        for (int i = 11 ; i <= 29 ; i += 9) {//sides
            inventory.setItem(i, new ItemStack(Block.PISTON_EXTENSION));
            inventory.setItem(i + 4, new ItemStack(Block.PISTON_EXTENSION));
        }
        for (int i = 48 ; i < 51 ; i++) //stand
            inventory.setItem(i, new ItemStack(Block.LADDER));
    }
    
    @Override
    public void onClick(Player player, int slot, Inventory friendlyInventory) {
        int column = slot % 9;
        if (friendlyInventory.getItem(slot).getType().equals(Material.AIR) && column >= 3 && column <= 5) {
            if (getPlayerOne() == player)
                friendlyInventory.setItem(slot, new org.bukkit.inventory.ItemStack(Material.WOOL, 1, DyeColor.BLACK.getData()));
            else if (getPlayerTwo() == player)
                friendlyInventory.setItem(slot, new org.bukkit.inventory.ItemStack(Material.WOOL, 1, DyeColor.WHITE.getData()));
            nextTurn(slot);
        }
    }
    
    //we <3 magic numbers
    @Override
    public boolean checkWin(int slot, ItemStack coin) {
        //vertical check
        List<ItemStack> vertical = new ArrayList<ItemStack>();
        for (int i = slot - 18; i <= slot + 18; i += 9)
            if (i <= 53 && i >= 0 && inventory.getItem(i) != null)
                vertical.add(inventory.getItem(i));
            else
                vertical.add(placeholderCoin);
        for (int i = 0 ; i <= 2 ; i++) {
            List<ItemStack> verticalCheckerList = vertical.subList(i, i + 3);
            if (verticalCheckerList.get(0).doMaterialsMatch(coin)
                    && verticalCheckerList.get(1).doMaterialsMatch(coin)
                    && verticalCheckerList.get(2).doMaterialsMatch(coin)) {
                winningSlots.add(slot + (i * 9));
                winningSlots.add(slot - 9 + (i * 9));
                winningSlots.add(slot - 18 + (i * 9));
            }
        }
        
        //horizontal check
        List<ItemStack> horizontal = new ArrayList<ItemStack>();
        for (int i = slot - 2; i <= slot + 2; i++) {
            if (i <= 53 && i >= 0 && inventory.getItem(i) != null)
                horizontal.add(inventory.getItem(i));
            else
                horizontal.add(placeholderCoin);
        }
        for (int i = 0 ; i <= 2 ; i++) {
            List<ItemStack> horizontalCheckerList = horizontal.subList(i, i + 3);
            if (horizontalCheckerList.get(0).doMaterialsMatch(coin)
                    && horizontalCheckerList.get(1).doMaterialsMatch(coin)
                    && horizontalCheckerList.get(2).doMaterialsMatch(coin)) {
                winningSlots.add(slot + i);
                winningSlots.add(slot - 1 + i);
                winningSlots.add(slot - 2 + i);
            }
        }
        
        //negative slope diagonal
        List<ItemStack> negSlopeDiagonal = new ArrayList<ItemStack>();
        for (int i = slot - 20; i <= slot + 20; i += 10) {
            if (i <= 53 && i >= 0 && inventory.getItem(i) != null)
                negSlopeDiagonal.add(inventory.getItem(i));
            else
                negSlopeDiagonal.add(placeholderCoin);
        }
        for (int i = 0 ; i <= 2 ; i++) {
            List<ItemStack> negSlopeDiagonalCheckerList = negSlopeDiagonal.subList(i, i + 3);
            if (negSlopeDiagonalCheckerList.get(0).doMaterialsMatch(coin)
                    && negSlopeDiagonalCheckerList.get(1).doMaterialsMatch(coin)
                    && negSlopeDiagonalCheckerList.get(2).doMaterialsMatch(coin)) {
                winningSlots.add(slot + (i * 10));
                winningSlots.add(slot - 10 + (i * 10));
                winningSlots.add(slot - 20 + (i * 10));
            }
        }
        
        //positive slope diagonal
        List<ItemStack> posSlopeDiagonal = new ArrayList<ItemStack>();
        for (int i = slot - 16; i <= slot + 16; i += 8) {
            if (i <= 53 && i >= 0 && inventory.getItem(i) != null)
                posSlopeDiagonal.add(inventory.getItem(i));
            else
                posSlopeDiagonal.add(placeholderCoin);
        }
        for (int i = 0 ; i <= 2 ; i++) {
            List<ItemStack> posSlopeDiagonalCheckerList = posSlopeDiagonal.subList(i, i + 3);
            if (posSlopeDiagonalCheckerList.get(0).doMaterialsMatch(coin)
                    && posSlopeDiagonalCheckerList.get(1).doMaterialsMatch(coin)
                    && posSlopeDiagonalCheckerList.get(2).doMaterialsMatch(coin)) {
                winningSlots.add(slot + (i * 8));
                winningSlots.add(slot - 8 + (i * 8));
                winningSlots.add(slot - 16 + (i * 8));
            }
        }
        if (!winningSlots.isEmpty()) {
            winnerCoin = coin;
            return true;
        }
        return false;
    }
    
    @Override
    public void onWin() {
        for (Player player : ConnectFour.plugin.getServer().getOnlinePlayers())
            Config.sendGameWin(player, winner, loser, Config.getTicTacToeName());
        ConnectFour.Method.getAccount(loser.getName()).subtract(stakes);
        ConnectFour.Method.getAccount(winner.getName()).add(stakes);
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
    
    @Override
    public void onForfeit() {
        ((CraftPlayer)getPlayerOne()).getHandle().y();
        ((CraftPlayer)getPlayerTwo()).getHandle().y(); //todo check if player should hear
        setWon(true);
        for (Player msgrecvr : ConnectFour.plugin.getServer().getOnlinePlayers()) {
            Config.sendGameForfeit(msgrecvr, winner, loser, Config.getTicTacToeName());
        }
        ConnectFour.Method.getAccount(loser.getName()).subtract(stakes);
        ConnectFour.Method.getAccount(winner.getName()).add(stakes);
        ConnectFour.games.remove(getPlayerOne());
        ConnectFour.games.remove(getPlayerTwo());
    }
    
    @Override
    public void nextTurn(int slot) {
        int iii = 0;
        for (ItemStack foo : inventory.getContents()) {
            if (foo == null) {
                iii++;
            }
        }
        if (iii == 26) { //26 is the number of blank slots there will be if there are no more open spaces
            onTie();
            return;
        }
        
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
    
    @Override
    public void onTie() {
        setWon(true);
        Config.sendGameTie(playerOne, playerTwo, name);
    }
}