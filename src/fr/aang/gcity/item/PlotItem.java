package fr.aang.gcity.item;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlotItem {
	
	public static ItemStack get() {
		ItemStack	customGuy = new ItemStack(Material.STICK, 1);
		ItemMeta	customM = customGuy.getItemMeta();
		customM.setDisplayName("§cCréateur de parcelle");
		customM.setLore(Arrays.asList("Clic gauche pour ajouter les positions", 
									"La parcelle s'ajoute à la deuxième position"));
		customM.addEnchant(Enchantment.DAMAGE_ALL, 200, true);
		customGuy.setItemMeta(customM);
		return customGuy;
	}
	
	public static boolean is(ItemStack item) {
		if (item != null
				&& item.getType() != null
				&& item.getType() == Material.STICK
				&& item.getItemMeta() != null
				&& item.getItemMeta().hasDisplayName()
				&& item.getItemMeta().getDisplayName().equals("§cCréateur de parcelle")
				&& item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 200)
		{
			return true;
		}
		return false;
	}
}
