package albertomoreiraluiz.spigotrcon;

import org.bukkit.plugin.java.JavaPlugin;


public final class SpigotRCON extends JavaPlugin
{
    public void onEnable() {
        
    	getConfig().options().copyDefaults(false);
    	saveDefaultConfig(); 	
    	
        new SocketServer(this, new Config(this));
    }
}
