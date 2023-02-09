package fr.aang.gcity.region;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import fr.aang.gcity.Main;
import fr.aang.gcity.item.PlotItem;

public class ClaimListener implements Listener {
	
	private Main _main;

	public ClaimListener(Main main) {
		_main = main;
	}

	@EventHandler
	public void onBlockBreak( BlockBreakEvent event ) {
		
		Player player = event.getPlayer();
		
		if (!player.hasPermission("gcity.use")) {
			
			if (player.getWorld().getName().equals(_main.config.getWorld())) {
				
				Location loc = event.getBlock().getLocation();
				
				for (int i = 0; i < _main.citys.size(); i++) {
					
					if (_main.citys.get(i).isInCity(loc)) {
						
						for (int o = 0; o < _main.citys.get(i).getPlotSize(); o++) {
							
							if (_main.citys.get(i).getPlot(o).isInPlot(loc)) {
								
								Plot plot = _main.citys.get(i).getPlot(o);
								
								if (plot.getPlayer() != null) {
									if(plot.getPlayer().equals(player.getName()) || plot.isCollaborator(player.getName())) {
										return ;
									}
									else {
										event.setCancelled(true);
										player.sendMessage("§9[☂] §cParcelle à §e" + plot.getPlayer());
										return ;
									}
								}
								else {
									event.setCancelled(true);
									player.sendMessage("§9[☂] §cAchetez cette parcelle");
									return ;
								}
							}
						}
						event.setCancelled(true);
						return ;
					}
				}
				event.setCancelled(true);
				return ;
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace( BlockPlaceEvent event ) {
		
		Player player = event.getPlayer();
		
		if (!player.hasPermission("gcity.use")) {
		
			if (player.getWorld().getName().equals(_main.config.getWorld())) {
				
				Location loc = event.getBlock().getLocation();
				
				for (int i = 0; i < _main.citys.size(); i++) {
					
					if (_main.citys.get(i).isInCity(loc)) {
						
						for (int o = 0; o < _main.citys.get(i).getPlotSize(); o++) {
							
							if (_main.citys.get(i).getPlot(o).isInPlot(loc)) {
								
								Plot plot = _main.citys.get(i).getPlot(o);
								
								if (plot.getPlayer() != null) {
									if (plot.getPlayer().equals(player.getName()) || plot.isCollaborator(player.getName())) {
										
										if (event.getBlock().getType().compareTo(Material.CHEST) == 0 || event.getBlock().getType().compareTo(Material.TRAPPED_CHEST) == 0) {
											if (plot.getNumberOfBlock(Material.CHEST) + plot.getNumberOfBlock(Material.TRAPPED_CHEST) > _main.config.getUpgradeChest(plot.getLevelChest()).quantity()) {
												event.setCancelled(true);
												player.sendMessage("§9[☂] §cAméliorez votre parcelle pour poser plus de coffres");
											}
										}
										return ;
									}
									else {
										event.setCancelled(true);
										player.sendMessage("§9[☂] §cParcelle à §e" + plot.getPlayer());
										return ;
									}
								}
								else {
									event.setCancelled(true);
									player.sendMessage("§9[☂] §cAchetez cette parcelle");
									return ;
								}
							}
						}
						event.setCancelled(true);
						return ;
					}
				}
				event.setCancelled(true);
				return ;
			}
		}
	}
	
	@EventHandler
	public void onArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
		
		Player player = event.getPlayer();
		
		if (!player.hasPermission("gcity.use")) {
		
			if (player.getWorld().getName().equals(_main.config.getWorld())) {
				
				Location loc = event.getRightClicked().getLocation();
				
				for (int i = 0; i < _main.citys.size(); i++) {
					
					if (_main.citys.get(i).isInCity(loc)) {
						
						for (int o = 0; o < _main.citys.get(i).getPlotSize(); o++) {
							
							if (_main.citys.get(i).getPlot(o).isInPlot(loc)) {
								
								Plot plot = _main.citys.get(i).getPlot(o);
								
								if (plot.getPlayer() != null) {
									if (plot.getPlayer().equals(player.getName()) || plot.isCollaborator(player.getName())) {
										return ;
									}
									else {
										event.setCancelled(true);
										player.sendMessage("§9[☂] §cParcelle à §e" + plot.getPlayer());
										return ;
									}
								}
								event.setCancelled(true);
								player.sendMessage("§9[☂] §cAchetez cette parcelle");
								return ;
							}
						}
						event.setCancelled(true);
						return ;
					}
				}
				event.setCancelled(true);
				return ;
			}
		}
	}
	
	@EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {

		if (event.getDamager() instanceof Player) {
			
			Player player = (Player)event.getDamager();
			
			if (!player.hasPermission("gcity.use")) {
			
				if (player.getWorld().getName().equals(_main.config.getWorld())) {
					
					Location loc = event.getEntity().getLocation();
					
					for (int i = 0; i < _main.citys.size(); i++) {
						
						if (_main.citys.get(i).isInCity(loc)) {
							
							for (int o = 0; o < _main.citys.get(i).getPlotSize(); o++) {
								
								if (_main.citys.get(i).getPlot(o).isInPlot(loc)) {
									
									Plot plot = _main.citys.get(i).getPlot(o);
									
									if (plot.getPlayer() != null) {
										if (plot.getPlayer().equals(player.getName()) || plot.isCollaborator(player.getName())) {
											return ;
										}
										else {
											event.setCancelled(true);
											player.sendMessage("§9[☂] §cParcelle à §e" + plot.getPlayer());
											return ;
										}
									}
									event.setCancelled(true);
									player.sendMessage("§9[☂] §cAchetez cette parcelle");
									return ;
								}
							}
							event.setCancelled(true);
							return ;
						}
					}
					event.setCancelled(true);
					return ;
				}
			}
        }
	}
	
	@EventHandler 
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		
		Player player = event.getPlayer();
		
		if (!player.hasPermission("gcity.use")) {
		
			// For cityzens
			if (event.getRightClicked().getType() == EntityType.PLAYER)
				return ;
	
			if (player.getWorld().getName().equals(_main.config.getWorld())) {
				
				Location loc = event.getRightClicked().getLocation();
				
				for (int i = 0; i < _main.citys.size(); i++) {
					
					if (_main.citys.get(i).isInCity(loc)) {
						
						for (int o = 0; o < _main.citys.get(i).getPlotSize(); o++) {
							
							if (_main.citys.get(i).getPlot(o).isInPlot(loc)) {
								
								Plot plot = _main.citys.get(i).getPlot(o);
								
								if (plot.getPlayer() != null) {
									if (plot.getPlayer().equals(player.getName()) || plot.isCollaborator(player.getName())) {
										return ;
									}
									else {
										event.setCancelled(true);
										player.sendMessage("§9[☂] §cParcelle à §e" + plot.getPlayer());
										return ;
									}
								}
								event.setCancelled(true);
								player.sendMessage("§9[☂] §cAchetez cette parcelle");
								return ;
							}
						}
						event.setCancelled(true);
						return ;
					}
				}
				event.setCancelled(true);
				return ;
			}
		}
    }


	@EventHandler
	public void  onInteract( PlayerInteractEvent event ) {

		Player player = event.getPlayer();
		
		if (player.getWorld().getName().equals(_main.config.getWorld())) {
			
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK
			&& !player.hasPermission("gcity.use")) {
			
				Location loc = event.getClickedBlock().getLocation();
				
				for (int i = 0; i < _main.citys.size(); i++) {
					
					if (_main.citys.get(i).isInCity(loc)) {
						
						for (int o = 0; o < _main.citys.get(i).getPlotSize(); o++) {
							
							if (_main.citys.get(i).getPlot(o).isInPlot(loc)) {
								
								Plot plot = _main.citys.get(i).getPlot(o);
								
								if (plot.getPlayer() != null) {
									if (plot.getPlayer().equals(player.getName()) || plot.isCollaborator(player.getName())) {
										if (player.getInventory().getItemInMainHand().getType().compareTo(Material.ARMOR_STAND) == 0
											||(player.getInventory().getItemInOffHand().getType().compareTo(Material.ARMOR_STAND) == 0 && (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR))) {
											if (plot.getNumberOfEntity(EntityType.ARMOR_STAND) > _main.config.getUpgradeArmorStand(plot.getLevelArmorStand()).quantity()) {
												event.setCancelled(true);
												player.sendMessage("§9[☂] §cAméliorez votre parcelle pour poser plus de porte-armure");
											}
										}
										return ;
									}
									else {
										event.setCancelled(true);
										if (event.getHand() != EquipmentSlot.OFF_HAND)
											player.sendMessage("§9[☂] §cParcelle à §e" + plot.getPlayer());
										return ;
									}
								}
								else {
									event.setCancelled(true);
									if (event.getHand() != EquipmentSlot.OFF_HAND)
										player.sendMessage("§9[☂] §cAchetez cette parcelle");
									return ;
								}
							}
						}
						event.setCancelled(true);
						return ;
					}
				}
				event.setCancelled(true);
				return ;
			}
			else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
				
				if (PlotItem.is(player.getInventory().getItemInMainHand())
				&& player.hasPermission("gcity.use")) {
					
					event.setCancelled(true);
					GamePlayer gp = GamePlayer.getGamePlayer(player.getName());
					
					if (gp.getPos1() == null) {
						
						gp.setPos1(event.getClickedBlock().getLocation());
						player.sendMessage("§aPosition 1 définie.");
						
						Bukkit.getScheduler().runTaskLater(_main, new Runnable() {
							@Override
							public void run() {
								if (gp.getPos1() != null)
									player.sendMessage("§cLe claim est annulé");
								gp.setPos1(null);
							}
						}, 5 * 60 * 20);
						return ;
					}
					else if (gp.getPos2() == null) {
						
						gp.setPos2(event.getClickedBlock().getLocation());
						
						if (gp.getPos1().distance(gp.getPos2()) > 250 
						|| !gp.getPos1().getWorld().equals(gp.getPos2().getWorld())) {
							player.sendMessage("§cVous ne pouvez pas faire une zone aussi grande");
							gp.setPos1(null);
							gp.setPos2(null);
							return ;
						}
						
						City city = null;
						
						// On check si on est bien dans une ville
						for (int i = 0; i < _main.citys.size(); i++) {
							if (_main.citys.get(i).isInCity(gp.getPos1()) && _main.citys.get(i).isInCity(gp.getPos2())) {
								city = _main.citys.get(i);
							}
						}
						if (city == null) {
							player.sendMessage("§cVous ne pouvez pas faire une zone à l'extérieur d'une île");
							gp.setPos1(null);
							gp.setPos2(null);
							return ;
						}
						
						// On créer le plot
						Plot plot = new Plot(_main);
						plot.setLevelArmorStand(0);
						plot.setLevelChest(0);
						plot.setLevelHeight(0);
						plot.setLoc(gp.getPos1(), gp.getPos2());
						
						// On check si on est pas dans un plot déja existant
						for (int i = 0; i < city.getPlotSize(); i++) {
							if (city.getPlot(i).isInArea(plot)) {
								player.sendMessage("§cCette zone est déjà utilisée par un joueur");
								gp.setPos1(null);
								gp.setPos2(null);
								return ;
							}
						}
						
						city.addPlot(plot);
						_main.plots.addPlot(city.getName(), plot);

						player.sendMessage("§aPosition 2 définie.");
						player.sendMessage("§aZone enregistrée");
						gp.setPos1(null);
						gp.setPos2(null);
					}
				}
			}
		}
	}
}
