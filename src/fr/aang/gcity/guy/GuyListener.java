package fr.aang.gcity.guy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aang.gcity.Main;
import fr.aang.gcity.utils.Utils;

public class GuyListener implements Listener {
	
	private Main	_main;
	private CityGuy	_guy;

	public GuyListener(Main main) {
		_main = main;
		_guy = new CityGuy(main);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		
		Player player = (Player) event.getWhoClicked();
		ItemStack current = event.getCurrentItem();
		
		if (current != null && current.getType() != null) {
			
			// City's view
			
			if (event.getView().getTitle().substring(2).equals("Les villes")) {
				
				event.setCancelled(true);
				player.closeInventory();
				player.updateInventory();
				
				String city_name = current.getItemMeta().getDisplayName();
				
				if (current.getItemMeta().getDisplayName().equals("§e(●) §cRetour")) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "menu " + player.getName());
					return ;
				}
				
				int i = 0;
				while (i < _main.citys.size()) {
					if (_main.citys.get(i).getName().length() >= 2 && _main.citys.get(i).getName().equals(city_name.substring(2))) {
						Location loc = _main.citys.get(i).getSpawn();
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "menu " + player.getName() + " tp " + loc.getWorld().getName() + " " + Utils.parseLocToString(loc) + " -1 &9" + _main.citys.get(i).getName().replace("§", "&"));
						return ;
					}
					i++;
				}
			}
			
			// My Homes
			
			else if (event.getView().getTitle().substring(2).equals("Mes maisons")) {
					
				event.setCancelled(true);
				player.closeInventory();
				player.updateInventory();
				
				if (current.getItemMeta().getDisplayName().equals("§e(●) §cRetour")) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "menu " + player.getName());
					return ;
				}
				
				String[] title = current.getItemMeta().getDisplayName().split(" ");
				
				if (title[0].length() >= 2) {
					String city_name = title[0].substring(2, title[0].length());
					int plot_number = Integer.parseInt(title[1]);
					
					_guy.openPlotSettings(player, city_name, plot_number);
				}
			}
			else {
				
				for (int i = 0; i < _main.citys.size(); i++) {
					
					// Plots list view
					
					if (event.getView().getTitle().substring(2).equals(_main.citys.get(i).getName() + " parcelles")) {
						
						event.setCancelled(true);
						player.closeInventory();
						player.updateInventory();
						
						int plot_number = Integer.parseInt(current.getItemMeta().getDisplayName().split(" ")[1]);
						
						_main.citys.get(i).buyPlot(_main.citys.get(i), plot_number, player);
					}
					
					// Plot Setting view
					
					else if (event.getView().getTitle().length() >= _main.citys.get(i).getName().length() + 3
							&& event.getView().getTitle().substring(0, _main.citys.get(i).getName().length() + 3).equals(("§7" + _main.citys.get(i).getName() + " "))) {
						
						player.closeInventory();
						player.updateInventory();
						event.setCancelled(true);
						
						String[] title = event.getView().getTitle().split(" ");
						
						String city_name = title[0].substring(2);
						int plot_number = Integer.parseInt(title[1]);
						
						if (current.getItemMeta().getDisplayName().equals("§e(●) §cRetour")) {
							_guy.openMyPlots(player);
						}
						else if (event.getRawSlot() == 8) {
							if (event.getClick().isRightClick()) {
								_main.region.sellPlot(player, _main.citys.get(i), plot_number - 1);
							}
							else {
								player.sendMessage("§c[✖] §cUtilisez §e(→) §cpour vendre");
							}
						}
						else if (current.getItemMeta().getDisplayName().substring(2).equals("Spawn")) {
							Location loc = _main.citys.get(i).getPlot(plot_number - 1).getSpawn();
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "menu " + player.getName() + " tp " + loc.getWorld().getName() + " " + Utils.parseLocToString(loc) + " -1 " + "&9Maison");
						}
						else if (current.getItemMeta().getDisplayName().equals(player.getName())) {
							_guy.openPlotSettings(player, city_name, plot_number);
						}
						else {
							String upgrade = current.getItemMeta().getDisplayName().substring(2);
							_main.region.upgrade(player, city_name, plot_number - 1, upgrade);
						}
					}
				}
			}
		}
	}
}
