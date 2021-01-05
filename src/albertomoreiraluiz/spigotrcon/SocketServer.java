package albertomoreiraluiz.spigotrcon;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import org.bukkit.ChatColor;

public class SocketServer {

	private SpigotRCON rcon = null;
	private Config config = null;
	private ServerSocket server = null;
	private String identifier = "";
	private String password = "";
	private String command = "";

	public SocketServer(SpigotRCON main, Config cfg) {
		rcon = main;
		config = cfg;
		this.run();
	}

	private void run() {		

		try {
			server = new ServerSocket(config.getPort());
			rcon.getLogger().info(String.join("", "Started to Listening on Port: ", String.valueOf(config.getPort())));
		}
		catch(Exception ex) 
		{
			rcon.getLogger().severe("Error on starting listener!" + ex.getMessage());
		}

		try 
		{
			Thread thread = new Thread(() -> {
				while(true) {
					try 
					{
						Socket socket = server.accept();
						String incomingAddress = socket.getInetAddress().getHostAddress().replace("/", "");

						if(!config.getAllowedIps().contains(incomingAddress)) {
							socket.close();
							rcon.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', String.join("", config.getPrefix(), rcon.getConfig().getString("messages.connect.denied").replace("%remote_address%", incomingAddress))));
						}

						InputStream inputStream = socket.getInputStream();
						OutputStream outputStream = socket.getOutputStream();

						InputStreamReader streamReader = new InputStreamReader(inputStream);
						BufferedReader br = new BufferedReader(streamReader);
						PrintWriter out = new PrintWriter(outputStream);

						String line = null;
						while ((line = br.readLine()) != null) {

							identifier = line.split(";")[0];
							password = line.split(";")[1];
							command = line.split(";")[2];
							boolean blockcommand = false;

							if(!identifier.contains("PacketRCON")) {
								socket.close();
								continue;
							}

							if(!password.contentEquals(config.getPassword())) { 
								rcon.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', String.join("", config.getPrefix(), rcon.getConfig().getString("messages.connect.invalid").replace("%remote_address%", incomingAddress).replace("%password%", password))));
								socket.close();
								continue;
							}

							rcon.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', String.join("", config.getPrefix(), rcon.getConfig().getString("messages.connect.success").replace("%remote_address%", incomingAddress).replace("%password%", password))));

							for (String cmd : config.getBlockedCmds()) {
								if (!command.startsWith(cmd))
									continue;

								rcon.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', String.join("", config.getPrefix(), rcon.getConfig().getString("messages.command.blocked").replace("%remote_address%", incomingAddress).replace("%command%", cmd))));
								blockcommand = true;
								socket.close();
							}

							if(!blockcommand) {
								rcon.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', String.join("", config.getPrefix(), rcon.getConfig().getString("messages.command.success").replace("%remote_address%", incomingAddress).replace("%command%", command))));
								rcon.getServer().dispatchCommand(rcon.getServer().getConsoleSender(), command);
								out.write("Command Executed");
								out.flush();
								socket.close();
							}
						}

					}
					catch(Exception ex) 
					{
						rcon.getLogger().severe(String.join("", rcon.getConfig().getString("messages.connect.error")));
					}
				}
			});

			thread.setName("SpigotRCON SocketListener");
			thread.start();
		}
		catch(Exception ex) 
		{
			rcon.getLogger().severe(String.join("", rcon.getConfig().getString("messages.severe")));
		}
	}
}
