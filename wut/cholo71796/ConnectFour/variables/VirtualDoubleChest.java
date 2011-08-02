/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.ConnectFour.variables;

import net.minecraft.server.Block;
import net.minecraft.server.InventoryLargeChest;
import net.minecraft.server.ItemStack;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author Cole Erickson
 */
public class VirtualDoubleChest {
    
    private TileEntityVirtualChest chest;
    private TileEntityVirtualChest chest2;
    private InventoryLargeChest lc;
    
    public VirtualDoubleChest(String name) {
        chest = new TileEntityVirtualChest();
        chest2 = new TileEntityVirtualChest();

        lc = new InventoryLargeChest(name, chest, chest2);
    }
    
    public InventoryLargeChest getLc() {
        return lc;
    }
    
    public void showToPlayers(Player playerOne, Player playerTwo) {
        ((CraftPlayer) playerOne).getHandle().a(lc);
        ((CraftPlayer) playerTwo).getHandle().a(lc);
    }
    
    public void putConnectFourBorders() {
        for (int i = 0 ; i <= 45; i += 9)
            lc.setItem(i, new ItemStack(Block.WOOL, 1, 11));
        for (int i = 8 ; i <= 53; i += 9)
            lc.setItem(i, new ItemStack(Block.WOOL, 1, 11));
    }
    
    public void putTicTacToeBorders() {
        for (int i = 2 ; i <= 6 ; i++) //top of frame
            lc.setItem(i, new ItemStack(Block.PISTON_EXTENSION));
        for (int i = 38 ; i <= 42 ; i++) // bottom of frame
            lc.setItem(i, new ItemStack(Block.PISTON_EXTENSION));
        for (int i = 11 ; i <= 29 ; i += 9) {
            lc.setItem(i, new ItemStack(Block.PISTON_EXTENSION));
            lc.setItem(i + 4, new ItemStack(Block.PISTON_EXTENSION));
        }
        for (int i = 48 ; i <= 50 ; i++) // posts below bottom
            lc.setItem(i, new ItemStack(Block.LADDER));
    }
}