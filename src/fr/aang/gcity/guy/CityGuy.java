package fr.aang.gcity.guy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aang.gcity.Main;
import fr.aang.gcity.region.City;
import fr.aang.gcity.region.Plot;
import fr.aang.gcity.utils.Utils;

public class CityGuy {
	
	private Main _main;
	
	public CityGuy(Main main) {
		_main = main;
	}
	
	public void open(Player player) {
		
		Inventory inv = Bukkit.createInventory(null, 9, "§7Les villes");
	
		if (_main.citys.size() > 8)
			player.sendMessage("§c[✖] §cErreur: Trop d'îles à afficher)");
		else {
			int i = 0;
			while (i < _main.citys.size()) {
				List<String> lore = new ArrayList<String>();
				lore.add("§7▪ §fPrix au cube² §6" + _main.citys.get(i).getPrice() + "⛃");
				lore.add("§e(●) §aTéléportation");
				inv.setItem(i, getItem(_main.citys.get(i).getMaterial(), "§6" + _main.citys.get(i).getName(), lore));
				i++;
			}
			inv.setItem(8, getItem(Material.BARRIER, "§e(●) §cRetour", null));
			player.openInventory(inv);
		}
	}
	
	public void openCity(Player player, City city) {
		
		
		int slot = 0;
		while (slot < city.getEmptyPlotSize())
			slot += 9;
		
		if (slot > 54)
			player.sendMessage("§c[✖] §cErreur: Trop de parcelles à afficher");
		else if (slot < 1)
			player.sendMessage("§c[✖] §cAucune parcelle à vendre");
		else {
			
			Inventory inv = Bukkit.createInventory(null, slot, "§7" + city.getName() + " parcelles");
			
			int i = 0;
			slot = 0;
			while (i < city.getPlotSize()) {
				if (city.getPlot(i).getPlayer() == null || city.getPlot(i).getPlayer().isEmpty()) {
					List<String> lore = new ArrayList<String>();
					lore.add("§7▪ §fTaille §e" + city.getPlot(i).getWidth() + " x " + city.getPlot(i).getLenght());
					lore.add("§7▪ §fPosition §e" + city.getPlot(i).getMiddleX() + "x " + city.getPlot(i).getFloor() + "y " + city.getPlot(i).getMiddleZ() + "z");
					lore.add("§e(●) §aAcheter §6" + (city.getPlot(i).getWidth() * city.getPlot(i).getLenght() * city.getPrice()) + "⛃");
					inv.setItem(slot, getItem(city.getMaterial(), "§6Parcelle " + (i + 1), lore));
					slot++;
				}
				i++;
			}
			player.openInventory(inv);
		}
	}
	
	public void openMyPlots(Player player) {
		
		int	total_home = 0;
		for (int i = 0; i < _main.citys.size(); i++)
			total_home += _main.citys.get(i).getPlayerPlotSize(player);
		
		int slot_size = 0;
		while (slot_size < total_home + 1)
			slot_size += 9;
		
		if (slot_size > 54)
			player.sendMessage("§c[✖] §cErreur: Trop de parcelles à afficher");
		else if (slot_size < 1)
			player.sendMessage("§c[✖] §cAchetez une parcelle pour accéder à ce menu");
		else {
			Inventory inv = Bukkit.createInventory(null, slot_size, "§7Mes maisons");
			
			int slot = 0;
			for (int i = 0; i < _main.citys.size(); i++) {
				City city = _main.citys.get(i);
				int o = 0;
				while (o < city.getPlotSize()) {
					if (city.getPlot(o).getPlayer() != null && city.getPlot(o).getPlayer().equals(player.getName())) {
						List<String> lore = new ArrayList<String>();
						lore.add("§7▪ §fTaille §e" + city.getPlot(o).getWidth() + " x " + city.getPlot(o).getLenght());
						lore.add("§7▪ §fPosition §e" + city.getPlot(o).getMiddleX() + "x " + city.getPlot(o).getFloor() + "y " + city.getPlot(o).getMiddleZ() + "z");
						lore.add("§e(●) §aVoir");
						inv.setItem(slot, getItem(city.getMaterial(), "§6" + city.getName() + " " + (o + 1), lore));
						slot++;
					}
					o++;
				}
			}
			inv.setItem(slot_size - 1, getItem(Material.BARRIER, "§e(●) §cRetour", null));
			player.openInventory(inv);
		}
	}
	
	public void openPlotSettings (Player player, String city_name, int plot_number) {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§7" + city_name + " " + plot_number);
		
		Plot plot = _main.region.getPlot(city_name, plot_number - 1);
		
		// Sell
		List<String> sell_lore = new ArrayList<String>();
		double price = (plot.getWidth() * plot.getLenght() * _main.region.getCity(city_name).getPrice()) * _main.config.getSellCoef();
		sell_lore.add("§7▪ §cAction irréversible");
		inv.setItem(8, getItem(Material.TNT,"§e(→) §cVendre §6" + price + "⛃", sell_lore));
		
		// Collaborators
		//List<String> collaborators = plot.getCollaborators();
		List<String> collaborators = new ArrayList<String>();
		collaborators.add("§7▪ §cEn construction..");
		inv.setItem(10, getItem(Material.PLAYER_HEAD,"§6Collaborateurs", collaborators));
		
		// Chest
		List<String> chest_lore = new ArrayList<>();
		chest_lore.add("§7▪ §fNiveau §e" + plot.getLevelChest());
		chest_lore.add("§7▪ §fCapacité §e" + _main.config.getUpgradeChest(plot.getLevelChest()).quantity());
		if (plot.getLevelChest() >= _main.config.getUpgradeChestSize() - 1)
			chest_lore.add("§e(●) §aAmélioration §cMAX");
		else
			chest_lore.add("§e(●) §aAmélioration §6" + _main.config.getUpgradeChest(plot.getLevelChest() + 1).price() + "⛃");
		inv.setItem(12, getItem(Material.CHEST,"§6Coffres", chest_lore));
		
		// Height
		List<String> height_lore = new ArrayList<>();
		height_lore.add("§7▪ §fNiveau §e" + plot.getLevelHeight());
		height_lore.add("§7▪ §fCapacité §e" + _main.config.getUpgradeHeight(plot.getLevelHeight()).quantity());
		if (plot.getLevelHeight() >= _main.config.getUpgradeHeightSize() - 1)
			height_lore.add("§e(●) §aAmélioration §cMAX");
		else
			height_lore.add("§e(●) §aAmélioration §6" + _main.config.getUpgradeHeight(plot.getLevelHeight() + 1).price() + "⛃");
		inv.setItem(13, getItem(Material.LADDER,"§6Hauteur", height_lore));
		
		// Armor Stand
		List<String> armor_stand_lore = new ArrayList<>();
		armor_stand_lore.add("§7▪ §fNiveau §e" + plot.getLevelArmorStand());
		armor_stand_lore.add("§7▪ §fCapacité §e" + _main.config.getUpgradeArmorStand(plot.getLevelArmorStand()).quantity());
		if (plot.getLevelArmorStand() >= _main.config.getUpgradeArmorStandSize() - 1)
			armor_stand_lore.add("§e(●) §aAmélioration §cMAX");
		else
			armor_stand_lore.add("§e(●) §aAmélioration §6" + _main.config.getUpgradeArmorStand(plot.getLevelArmorStand() + 1).price() + "⛃");
		inv.setItem(14, getItem(Material.ARMOR_STAND,"§6Portes-armures", armor_stand_lore));
		
		// Spawn
		List<String> spawn_lore = new ArrayList<String>();
		Location spawn = plot.getSpawn();
		spawn_lore.add("§7▪ §fPosition §e" + spawn.getBlockX() + "x " + spawn.getBlockY() + "y " + spawn.getBlockZ() + "z");
		spawn_lore.add("§e(●) §aTéléportation");
		inv.setItem(16, getItem(Material.ENDER_PEARL,"§6Spawn", spawn_lore));
		
		inv.setItem(18, Utils.getHeadInfo(player, _main.eco.getBalance(player)));
		
		// Back
		inv.setItem(26, getItem(Material.BARRIER,"§e(●) §cRetour", null));
	
		player.openInventory(inv);
	}
	
	private static ItemStack getItem(Material material, String custom_name, List<String> lore) {
		ItemStack item = new ItemStack(material, 1);
		ItemMeta itemM = item.getItemMeta();
		itemM.setDisplayName(custom_name);
		if (lore != null && !lore.isEmpty())
			itemM.setLore(lore);
		item.setItemMeta(itemM);;
		return item;
	}

}
