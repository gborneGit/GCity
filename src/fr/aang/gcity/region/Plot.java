package fr.aang.gcity.region;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import fr.aang.gcity.Main;

public class Plot {
	
	private Main			_main;
	
	private String			_player;
	private Location		_spawn;
	private Location		_min_loc;
	private Location		_max_loc;
	private List<String>	_collaborators = new ArrayList<String>();
	private int				_level_chest;
	private int				_level_height;
	private int				_level_armor_stand;
	
	
	public Plot(Main main) {
		_main = main;
	}
	
	public void setPlayer(String player) {
		_player = player;
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
	
	public void addCollaborators(String player) {
		_collaborators.add(player);
	}
	
	public void removeCollaborators(String player) {
		_collaborators.remove(player);
	}
	
	public void setLevelChest(int level) {
		_level_chest = level;
	}
	
	public void setLevelHeight(int level) {
		_level_height = level;
	}
	
	public void setLevelArmorStand(int level) {
		_level_armor_stand = level;
	}
	
	// GETTERS
	
	public String getPlayer() {
		return _player;
	}
	
	public Location getMinLoc() {
		return _min_loc;
	}
	
	public Location getMaxLoc() {
		return _max_loc;
	}
	
	public Location getSpawn() {
		if (_spawn == null) {
			_spawn = new Location(_min_loc.getWorld(), getMiddleX(), _min_loc.getBlockY() + 1, getMiddleZ());
		}
		return _spawn;
	}
	
	public int getMiddleX() {
		return _min_loc.getBlockX() + (_max_loc.getBlockX() - _min_loc.getBlockX()) / 2 ;
	}
	
	public int getMiddleZ() {
		return _min_loc.getBlockZ() + (_max_loc.getBlockZ() - _min_loc.getBlockZ()) / 2;
	}
	
	public List<String> getCollaborators() {
		return _collaborators;
	}
	
	public boolean isCollaborator(String name) {
		if (_collaborators.indexOf(name) == -1)
			return false;
		else
			return true;
	}
	
	public int getLevelChest() {
		return _level_chest;
	}
	
	public int getLevelHeight() {
		return _level_height;
	}
	
	public int getLevelArmorStand() {
		return _level_armor_stand;
	}
	
	public int getWidth() {
		int width = _min_loc.getBlockX() - _max_loc.getBlockX();
		if (width < 0)
			return (-width) + 1;
		return width + 1;
	}
	
	public int getLenght() {
		int lenght = _min_loc.getBlockZ() - _max_loc.getBlockZ();
		if (lenght < 0)
			return (-lenght) + 1;
		return lenght + 1;
	}
	
	public int getFloor() {
		return _min_loc.getBlockY();
	}
	
	public void resetPlot() {
		_player = null;
		_collaborators.clear();
		_level_chest = 0;
		_level_height = 0;
		_level_armor_stand = 0;
	}
	
	// Check X, Y et Z (pour les interactions)
	public boolean isInPlot(Location loc) {
		return ( _min_loc.getX() <= loc.getX() 
				&& (_min_loc.getY() - _main.config.getPlotDepth()) <= loc.getY() 
				&& _min_loc.getZ() <= loc.getZ() 
				&& _max_loc.getX() >= loc.getX() 
				&& (_min_loc.getY() + _main.config.getUpgradeHeight(_level_height).quantity()) >= loc.getY() 
				&& _max_loc.getZ() >= loc.getZ());
	}
	
	// Check X et Z (pour le claim)
	public boolean isInArea(Plot plot) {
		if ((plot.getMaxLoc().getBlockX() < _min_loc.getBlockX() || plot.getMinLoc().getBlockX() > _max_loc.getBlockX())
			|| (plot.getMaxLoc().getBlockZ() < _min_loc.getBlockZ() || plot.getMinLoc().getBlockZ() > _max_loc.getBlockZ())) {
			return false;
		}
		return true;
	}
	
	public int getNumberOfBlock(Material block) {
		int n = 0;
		int ymin = _min_loc.getBlockY() - _main.config.getPlotDepth();
		int ymax =  _min_loc.getBlockY() + _main.config.getUpgradeHeight(getLevelChest()).quantity();
		int x = _min_loc.getBlockX();
		int y = ymin;
		int z = _min_loc.getBlockZ();
		World world = _min_loc.getWorld();
		while (x <= _max_loc.getBlockX()) {
			while (y <= ymax) {
				while (z <= _max_loc.getBlockZ()) {
					if (world.getBlockAt(x, y, z).getType().compareTo(block) == 0)
						n++;
					z++;
				}
				z = _min_loc.getBlockZ();
				y++;
			}
			y = ymin;
			x++;
		}
		return n;
	}
	
	public int getNumberOfEntity(EntityType entity) {
		int n = 0;

		World world = _min_loc.getWorld();
		for (int i = 0; i < world.getEntities().size(); i++) {
			if (world.getEntities().get(i).getType().compareTo(entity) == 0) {
				if (isInPlot(world.getEntities().get(i).getLocation()))
					n++;
			}
		}
		return n;
	}
}
