package fr.aang.gcity.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aang.gcity.Main;
import fr.aang.gcity.guy.CityGuy;
import fr.aang.gcity.item.PlotItem;
import fr.aang.gcity.region.City;

public class CityCommands implements CommandExecutor {
	
	private Main 	_main;
	private	CityGuy	_guy;

	public CityCommands(Main main) {
		_main = main;
		_guy = new CityGuy(main);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (label.equalsIgnoreCase("city")) {
			
			// remove & baton
			if (sender instanceof Player) {
				
				Player player = (Player)sender;
				
				if (player.hasPermission("gcity.use")) {
					
					if (args.length == 1) {
					
						if (args[0].equalsIgnoreCase("admin")) {
							player.getInventory().addItem(PlotItem.get());
							return true;
						}
					}
					else if (args.length == 3) {
					
						if (args[0].equalsIgnoreCase("remove")) {
							
							if (_main.region.getCity(args[1]) != null) {
								
								City city = _main.region.getCity(args[1]);
								
								try {
									int plot_number = Integer.parseInt(args[2]);
									
									if (city.getPlot(plot_number - 1) != null) {
										_main.plots.removePlot(args[1], plot_number - 1);
										city.removePlot(plot_number - 1);
										player.sendMessage("§aPlot §e" + args[1] + " " + args[2] + " §aas remove");
										return true;
									}
								} catch (Exception e) {
									player.sendMessage("§cPlot '" + args[2] + "' doesn't exist in city " + args[1]);
								}
							}
							else {
								player.sendMessage("§c'" + args[1] + "' is not a city");
							}
						}
						else {
							player.sendMessage("§cWrong city command");
						}
					}
					else {
						player.sendMessage("§cWrong city command");
					}
				}
			}
			else {
				
				if (args.length >= 1) {
					
					Player player = Bukkit.getPlayer(args[0]);
					
					if (player == null)
						return false;
					else {
						
						if (args.length == 1) {
							_guy.open(player);
							return true;
						}
						else if (args.length == 2) {
							
							if (args[1].equalsIgnoreCase("myplots")) {
								_guy.openMyPlots(player);
								return true;
							}
							else {
								int i = 0;
								while (i < _main.citys.size()) {
									if (args[1].equalsIgnoreCase(_main.citys.get(i).getName())) {
										_guy.openCity(player, _main.citys.get(i));
										return true;
									}
									i++;
								}
								return false;
							}
						}
						return false;
					}
				}
				return false;
			}
		}
		return false;
	}
}
