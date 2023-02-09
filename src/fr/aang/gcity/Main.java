package fr.aang.gcity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import fr.aang.gcity.commands.CityCommands;
import fr.aang.gcity.config.ConfigFile;
import fr.aang.gcity.config.PlotsFile;
import fr.aang.gcity.guy.GuyListener;
import fr.aang.gcity.region.City;
import fr.aang.gcity.region.ClaimListener;
import fr.aang.gcity.region.RegionManager;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	public ConfigFile		config;
	public PlotsFile		plots;
	public RegionManager	region = new RegionManager(this);
	public Economy			eco;
	
	public List<City> citys = new ArrayList<>();
	
	@Override
	public void onEnable() {
		
		if (!setupEconomy()) {
			System.out.println(ChatColor.RED + "[GCity] You must have Vault");
			getServer().getPluginManager().disablePlugin(this);
			return ;
		}
		
		this.config = new ConfigFile(this, "config.yml");
		this.plots = new PlotsFile(this, "plots.yml");
		
		getCommand("city").setExecutor(new CityCommands(this));
		getServer().getPluginManager().registerEvents(new ClaimListener(this), this);
		getServer().getPluginManager().registerEvents(new GuyListener(this), this);
	
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economy = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economy != null)
			eco = economy.getProvider();
		return (economy != null);
	}
	
	public File getDirectory() {
		return getDataFolder();
	}

}
