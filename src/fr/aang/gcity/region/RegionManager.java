package fr.aang.gcity.region;

import org.bukkit.entity.Player;

import fr.aang.gcity.Main;

public class RegionManager {
	
	private Main _main;
	
	public RegionManager(Main main) {
		_main = main;
	}
	
	public City getCity(String city_name) {

		for (int i = 0; i < _main.citys.size(); i++) {
			if (_main.citys.get(i).getName().equals(city_name))
				return _main.citys.get(i);
		}
		return null;
	}
	
	public Plot getPlot(String city_name, int plot_number) {
		
		City city = getCity(city_name);
		return city.getPlot(plot_number);
	}
	
	public void sellPlot(Player player, City city, int plot_id) {
		city.getPlot(plot_id).resetPlot();
		_main.plots.resetPlot(city.getName(), plot_id);
		double price = city.getPrice() * city.getPlot(plot_id).getLenght() * city.getPlot(plot_id).getWidth() * _main.config.getSellCoef();
		_main.eco.depositPlayer(player, price);
		player.sendMessage("§a[⛃] §aVous venez de §cvendre §9" + city.getName() + " " + (plot_id + 1) + " §apour §6" + price + "⛃");
	}
	
	public void	upgrade(Player player, String city_name, int plot_number, String upgrade) {
		
		Plot plot = getPlot(city_name, plot_number);
		
		if (upgrade.equals("Coffres")) {
			if (plot.getLevelChest() >= _main.config.getUpgradeChestSize() - 1)
				player.sendMessage("§9[☂] §cCette amélioration est déjà au maximum");
			else {
				int price = _main.config.getUpgradeChest(plot.getLevelChest() + 1).price();
				if (_main.eco.getBalance(player) >= price) {
					_main.eco.withdrawPlayer(player, price);
					plot.setLevelChest(plot.getLevelChest() + 1);
					_main.plots.upgrade(city_name, plot_number, "chest", plot.getLevelChest());
					player.sendMessage("§9[☂] §aVous venez d'améliorer §e" + upgrade + " §apour §6" + price + "⛃");
				}
				else {
					player.sendMessage("§c[⛃] §cVous n'avez pas assez d'argent");
				}
			}
		}
		else if (upgrade.equals("Hauteur")) {
			if (plot.getLevelHeight() >= _main.config.getUpgradeHeightSize() - 1)
				player.sendMessage("§9[☂] §cCette amélioration est déjà au maximum");
			else {
				int price = _main.config.getUpgradeHeight(plot.getLevelHeight() + 1).price();
				if (_main.eco.getBalance(player) >= price) {
					_main.eco.withdrawPlayer(player, price);
					plot.setLevelHeight(plot.getLevelHeight() + 1);
					_main.plots.upgrade(city_name, plot_number, "height", plot.getLevelHeight());
					player.sendMessage("§9[☂] §aVous venez d'améliorer §e" + upgrade + " §apour §6" + price + "⛃");
			
				}
				else {
					player.sendMessage("§c[⛃] §cVous n'avez pas assez d'argent");
				}
			}
		}
		else if (upgrade.equals("Portes-armures")) {
			if (plot.getLevelArmorStand() >= _main.config.getUpgradeArmorStandSize() - 1)
				player.sendMessage("§9[☂] §cCette amélioration est déjà au maximum");
			else {
				int price = _main.config.getUpgradeArmorStand(plot.getLevelArmorStand() + 1).price();
				if (_main.eco.getBalance(player) >= price) {
					_main.eco.withdrawPlayer(player, price);
					plot.setLevelArmorStand(plot.getLevelArmorStand() + 1);
					_main.plots.upgrade(city_name, plot_number, "armor_stand", plot.getLevelArmorStand());
					player.sendMessage("§9[☂] §aVous venez d'améliorer §e" + upgrade + " §apour §6" + price + "⛃");
				}
				else
					player.sendMessage("§c[⛃] §cVous n'avez pas assez d'argent");
			}
		}
		else
			player.sendMessage("§c[✖] §cCette amélioration n'existe pas");
	}

}
