package fr.aang.gcity.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.aang.gcity.Main;
import fr.aang.gcity.region.City;
import fr.aang.gcity.region.Plot;

public class PlotsFile {

	private Main				_main;
	private File				_file;
	private YamlConfiguration	_config;
	
	
	public PlotsFile(Main main, String file_name) {
		_main = main;
		_config = loadConfig(file_name);
		reloadConfig();
	}
	
	private YamlConfiguration loadConfig(String file_name) {
		
		if(!_main.getDirectory().exists()) {
			_main.getDirectory().mkdir();
		}
		
		_file = new File(_main.getDataFolder(), file_name);
		
		if (!_file.exists()) {
			_main.saveResource(file_name, false);
		}
		
		return YamlConfiguration.loadConfiguration(_file);
	}
	
	public void save() {
        try {
        	_config.save(_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void reloadConfig() {
		
		_main.citys.clear();
		
		ConfigurationSection	section;
		
		int	i = 1;
		while ((section = _config.getConfigurationSection("city" + i)) != null) {
			fillCity(section);
			i++;
		}
	}
	
	private ConfigurationSection getPlotSection(String city_name, int plot_id) {
		
		ConfigurationSection	section;
		
		int	i = 1;
		while ((section = _config.getConfigurationSection("city" + i)) != null) {
			if (section.get("name").equals(city_name)) {
				if (section.isSet("plots." + plot_id))
					return section.getConfigurationSection("plots." + plot_id);
				else
					return null;
			}
			i++;
		}
		return null;
	}
	
	private ConfigurationSection getCitySection(String city_name) {
		
		ConfigurationSection	section;
		
		int	i = 1;
		while ((section = _config.getConfigurationSection("city" + i)) != null) {
			if (section.get("name").equals(city_name)) {
				return section;
			}
			i++;
		}
		return null;
	}
	
	public void addPlot(String city_name, Plot plot) {
		
		ConfigurationSection	section;
		
		int	i = 1;
		while ((section = _config.getConfigurationSection("city" + i)) != null) {
			
			if (section.get("name").equals(city_name)) {

				int o = 1;
				while (section.getConfigurationSection("plots." + o) != null)
					o++;
				
				section.set("plots." + o + ".player", null);
				section.set("plots." + o + ".cord1", parseLocToString(plot.getMinLoc()));
				section.set("plots." + o + ".cord2", parseLocToString(plot.getMaxLoc()));
				section.set("plots." + o + ".collaborators", null);
				section.set("plots." + o + ".upgrade.chest", 0);
				section.set("plots." + o + ".upgrade.height", 0);
				section.set("plots." + o + ".upgrade.armor_stand", 0);
				save();
				return ;
			}
			i++;
		}
	}
	
	public void upgrade(String city_name, int plot_id, String upgrade_name, int level) {
		ConfigurationSection section = getPlotSection(city_name, plot_id + 1);
		section.set("upgrade." + upgrade_name, level);
		save();
	}
	
	public void resetPlot(String city_name, int plot_id) {
		ConfigurationSection section = getPlotSection(city_name, plot_id + 1);
		section.set(".player", null);
		section.set(".collaborators", null);
		section.set(".upgrade.chest", 0);
		section.set(".upgrade.height", 0);
		section.set(".upgrade.armor_stand", 0);
		save();
	}
	
	public void removePlot(String city_name, int plot_id) {
		ConfigurationSection section = getCitySection(city_name);
		int i = plot_id + 1;
		while (section.isSet(".plots." + (i + 1))) {
			section.set(".plots." + (i), section.get(".plots." + (i + 1)));
			i++;
		}
		section.set(".plots." + (i), null);
		save();
		reloadConfig();
	}
	
	private void fillCity(ConfigurationSection section) {
		
		City city = new City(_main);
		
		city.setName(section.getString("name"));
		city.setMaterial(section.getString("icon"));
		city.setPrice(section.getInt("price"));
		city.setLoc(parseStringToLoc(section.getString("cord1")), parseStringToLoc(section.getString("cord2")));
		city.setSpawn(parseStringToLoc(section.getString("spawn")));
		
		ConfigurationSection plots;
		
		int i = 1;
		while ((plots = section.getConfigurationSection("plots." + i)) != null) {
			
			Plot plot = new Plot(_main);
			
			plot.setPlayer(plots.getString("player"));
			plot.setLoc(parseStringToLoc(plots.getString("cord1")), parseStringToLoc(plots.getString("cord2")));
			for (String player : plots.getStringList("collaborators"))
				plot.addCollaborators(player);
			plot.setLevelChest(plots.getInt("upgrade.chest"));
			plot.setLevelHeight(plots.getInt("upgrade.height"));
			plot.setLevelArmorStand(plots.getInt("upgrade.armor_stand"));
			
			city.addPlot(plot);
			i++;
		}
		_main.citys.add(city);
	}
	
	private String parseLocToString(Location loc) {
		
		String string = loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
		return string;
	}
	
	private Location parseStringToLoc(String string) {
		
		String[] parseLoc = string.split(",");
		
		double x = Double.valueOf(parseLoc[0]);
		double y = Double.valueOf(parseLoc[1]);
		double z = Double.valueOf(parseLoc[2]);
		
		Location loc = new Location(Bukkit.getWorld(_main.config.getWorld()), x, y, z);
		
		return loc;
	}
	
	public void addPlayer(String city_name, int plot_number, String player_name) {
		ConfigurationSection	section;
		
		int	i = 1;
		while ((section = _config.getConfigurationSection("city" + i)) != null) {
			
			if (section.get("name") == city_name) {
				if (section.isSet("plots." + plot_number)) {
					section.set("plots." + plot_number + ".player", player_name);
					save();
				}
				else
					System.out.println("Le plot " + city_name + ".plots." + plot_number + " n'existe pas");
				return ;
			}
			i++;
		}
	}
}
