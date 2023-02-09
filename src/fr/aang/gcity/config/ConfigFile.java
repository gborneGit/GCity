package fr.aang.gcity.config;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.aang.gcity.Main;

public class ConfigFile {
	
	private Main				_main;
	private File				_file;
	private YamlConfiguration	_config;

	private String				_world;
	private int					_max_collaborators;
	private int					_plot_depth;
	private double				_sell_coef;
	private List<Upgrade>		_upgrade_chest;
	private List<Upgrade>		_upgrade_height;
	private List<Upgrade>		_upgrade_armor_stand;
	

	public ConfigFile(Main main, String file_name) {
		_main = main;
		_config = loadConfig(file_name);
		reloadConfig();
	}
	
	public String getWorld() {
		return _world;
	}
	
	public int getMaxCollaborators() {
		return _max_collaborators;
	}
	
	public int getPlotDepth() {
		return _plot_depth;
	}
	
	public double getSellCoef() {
		return _sell_coef;
	}
	
	public Upgrade getUpgradeChest(int level) {
		return _upgrade_chest.get(level);
	}
	
	public int getUpgradeChestSize() {
		return _upgrade_chest.size();
	}
	
	public Upgrade getUpgradeHeight(int level) {
		return _upgrade_height.get(level);
	}
	
	public int getUpgradeHeightSize() {
		return _upgrade_height.size();
	}
	
	public Upgrade getUpgradeArmorStand(int level) {
		return _upgrade_armor_stand.get(level);
	}
	
	public int getUpgradeArmorStandSize() {
		return _upgrade_armor_stand.size();
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
	
	private List<Upgrade> fillUpgrade(String section) {

		List<Upgrade> upgrade = new ArrayList<Upgrade>();
		
		
		upgrade.add(new Upgrade(0, _config.getInt(section + ".default")));
		
		int level = 1;
		while (_config.isSet(section + "." + level) == true) {
			int price = _config.getInt(section + "." + level + ".price");
			int quantity = _config.getInt(section + "." + level + ".quantity");
			upgrade.add(new Upgrade(price, quantity));
			level++;
		}
		return upgrade;
	}

	private void reloadConfig() {
		_world = _config.getString("world");
		_max_collaborators = _config.getInt("max_collaborators");
		_plot_depth = _config.getInt("plot_depth");
		_sell_coef = _config.getDouble("sell_coef");
		_upgrade_chest = fillUpgrade("upgrade.chest");
		_upgrade_height = fillUpgrade("upgrade.height");
		_upgrade_armor_stand = fillUpgrade("upgrade.armor_stand");
	}
}
