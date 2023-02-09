package fr.aang.gcity.utils;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Utils {
	
	public static ItemStack getHeadInfo(Player player, double balance) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(player.getName());
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§7▪ §6" + balance + "⛃");
        skull.setLore(lore);
        skull.setOwnerProfile(player.getPlayerProfile());
        item.setItemMeta(skull);
        return item;
    }
	
	public static String parseLocToString(Location loc) {
		String string = loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();  
		return string;
	}
}
