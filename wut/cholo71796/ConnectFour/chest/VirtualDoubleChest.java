/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wut.cholo71796.ConnectFour.chest;

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
    
    public void showToPlayer(Player player) {
        ((CraftPlayer) player).getHandle().a(lc);
    }
}