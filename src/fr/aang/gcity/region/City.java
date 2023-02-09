package fr.aang.gcity.region;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.aang.gcity.Main;

public class City {
	
	private Main _main;
	
	private String			_name;
	private	Material		_material;
	private int				_price;
	private Location		_min_loc;
	private Location		_max_loc;
	private Location		_spawn;
	private List<Plot>		_plots = new ArrayList<Plot>();
	
	public City(Main main) {
		_main = main;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public void setMaterial(String name) {
		_material = Material.getMaterial(name);
	}
	
	public void setPrice(int price) {
		_price = price;
	}
	
	public void setLoc(Location firstPoint, Location secondPoint) {
		_min_loc = new Location(firstPoint.getWorld(), min(firstPoint.getX(), secondPoint.getX()), min(firstPoint.getY(), secondPoint.getY()), min(firstPoint.getZ(), secondPoint.getZ()));
		_max_loc = new Location(firstPoint.getWorld(), max(firstPoint.getX(), secondPoint.getX()), max(firstPoint.getY(), secondPoint.getY()), max(firstPoint.getZ(), secondPoint.getZ()));
	}
	
	public double min(double a, double b) {
		return a < b ? a : b;
	}
 
	public double max(double a, double b) {
		return a > b ? a : b;
	}
	
	public void setSpawn(Location spawn) {
		_spawn = spawn;
	}
	
	public void addPlot(Plot plot) {
		_plots.add(plot);
	}
	
	public void removePlot(int plot_id) {
		_plots.remove(plot_id);
	}
	
	public String getName() {
		return _name;
	}
	
	public Material getMaterial() {
		return _material;
	}
	
	public int getPrice() {
		return _price;
	}
	
	public Location getMinLoc() {
		return _min_loc;
	}
	
	public Location getMaxLoc() {
		return _max_loc;
	}
	
	public Location getSpawn() {
		return _spawn;
	}
	
	public Plot getPlot(int id) {
		return _plots.get(id);
	}
	
	public int getPlotSize() {
		return _plots.size();
	}
	
	public int getEmptyPlotSize() {
		int size = 0;
		for (int i = 0; i < _plots.size(); i++) {
			if (_plots.get(i).getPlayer() == null || _plots.get(i).getPlayer().isEmpty())
				size++;
		}
		return size;
	}
	
	public int getPlayerPlotSize(Player player) {
		int size = 0;
		for (int i = 0; i < _plots.size(); i++) {
			if (_plots.get(i).getPlayer() != null && _plots.get(i).getPlayer().equals(player.getName()))
				size++;
		}
		return size;
	}
	
	public boolean isInCity(Location loc) {
		return ( _min_loc.getX() <= loc.getX()
				&& _min_loc.getZ() <= loc.getZ() 
				&& _max_loc.getX() >= loc.getX()
				&& _max_loc.getZ() >= loc.getZ());
	}
	
	public void buyPlot(City city, int plot_id, Player player) {
		
		Plot plot = _plots.get(plot_id - 1);
		
		if (plot.getPlayer() != null) {
			player.sendMessage("§9[☂] §cCette parcelle appartient à §f" + plot.getPlayer());
			return ;
		}
		int price = plot.getWidth() * plot.getLenght() * city.getPrice();
		if (_main.eco.getBalance(player) >= price) {
			_main.eco.withdrawPlayer(player, price);
			plot.setPlayer(player.getName());
			_main.plots.addPlayer(_name, plot_id, player.getName());
			player.sendMessage("§a[⛃] §aVous avez acheter une parcelle pour §e" + price + "⛃");
		}
		else {
			player.sendMessage("§c[⛃] §cVous n'avez pas assez d'argent");
		}
		
	}
}
