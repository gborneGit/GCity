package fr.aang.gcity.region;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
 
import java.util.HashMap;
import java.util.Map;
 
public class GamePlayer {
 
    private Player		_player;
 
    private Location	_pos1;
    private Location	_pos2;
 
    public static Map<String, GamePlayer> gamePlayers = new HashMap<>();
 
    public GamePlayer(String playerName) {
    	
        this._player = Bukkit.getPlayer(playerName);
 
        gamePlayers.put(_player.getName(), this);
    }
    
    public static GamePlayer getGamePlayer(String playerName) {
    	
    	GamePlayer gp = gamePlayers.get(playerName);
    	if (gp == null) {
    		new GamePlayer(playerName);
    		gp = gamePlayers.get(playerName);
    	}
    	return gp;
    }
 
    public Location getPos1() {
        return _pos1;
    }
 
    public Location getPos2() {
        return _pos2;
    }
 
    public void setPos1(Location pos1) {
        this._pos1 = pos1;
    }
 
    public void setPos2(Location pos2) {
        this._pos2 = pos2;
    }
}
