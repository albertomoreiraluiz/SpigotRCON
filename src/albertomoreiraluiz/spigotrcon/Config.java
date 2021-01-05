package albertomoreiraluiz.spigotrcon;

import java.util.List;
//import java.net.*;

public class Config {
	private String prefix;
    private int port;
    private String password;
    private List<String> blockedCmds;
    private List<String> allowedIps;

    public Config(SpigotRCON main) {
    	prefix = main.getConfig().getString("settings.prefix");
    	port = main.getConfig().getInt("settings.port");
        password = main.getConfig().getString("settings.password");
        blockedCmds = main.getConfig().getStringList("settings.blockedCmds");
        allowedIps = main.getConfig().getStringList("settings.allowedIps");
        
        /*for(String ip : this.allowedIps)
        {
        	if(!ip.matches("^(?:(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])$")) 
        	{
        		try 
        		{
        			InetAddress resolve = InetAddress.getByName(ip);
        			this.allowedIps.add(resolve.getHostAddress().replace("/", ""));
        		}
        		catch(Exception ex) 
        		{
        			return;
        		}
        	}
        }*/  
    }

    public String getPrefix() {
        return prefix;
    }
    
    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getBlockedCmds() {
        return blockedCmds;
    }

    public List<String> getAllowedIps() {
        return allowedIps;
    }
}
