package huffmanbran.treasurer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {

	public Core plugin;
	

	public Command(Core p) {

		plugin = p;
		
		
	}


	@Override
	public boolean onCommand(CommandSender sender,
			org.bukkit.command.Command cmd, String label, String[] args) {
		plugin.pdf = plugin.getDescription();

		plugin.log = Bukkit.getLogger();

		Player p = (Player) sender;

		if (label.equalsIgnoreCase("treasure")) {

			if (args.length == 0) {
				if(!p.hasPermission(plugin.treasureMenu)){
					
					p.sendMessage(plugin.settings.config.getString("prefix") 
							+ plugin.settings.config.getString("no-permission-msg")
							.replaceAll("(&([a-f0-9]))", "\u00A7$2"));
					
					return true;
					
				}
				
				p.sendMessage(ChatColor.BLUE + "[Treasure] " + ChatColor.WHITE
						+ "Purchase random treasure chests to be looted from anyone!");

				p.sendMessage(ChatColor.BLUE + "Authors: " + ChatColor.WHITE
						+ plugin.pdf.getAuthors());
				p.sendMessage(ChatColor.BLUE + "Version: " + ChatColor.WHITE
						+ plugin.pdf.getVersion());
				p.sendMessage(ChatColor.BLUE + "Commands:" + ChatColor.WHITE
						+ " /treasure deploy | buy | reload");
				
				return true;

			} else {

				if (args[0].equalsIgnoreCase("deploy")) {
					
					if(!p.hasPermission(plugin.treasureDeploy)){
						
						p.sendMessage(plugin.settings.config.getString("prefix") 
								+ plugin.settings.config.getString("no-permission-msg")
								.replaceAll("(&([a-f0-9]))", "\u00A7$2"));
						
						return true;
						
					}
					
					plugin.makeRandomChest();
					
					

					return true;

				}
				
				if(args[0].equalsIgnoreCase("reload")){
					
					if(!p.hasPermission(plugin.treasureReload)){
						
						p.sendMessage(plugin.settings.config.getString("prefix") 
								+ plugin.settings.config.getString("no-permission-msg")
								.replaceAll("(&([a-f0-9]))", "\u00A7$2"));
						
						return true;
						
					}
					
					plugin.settings.reloadConfig();
					plugin.settings.reloadData();
					
					p.sendMessage(ChatColor.GREEN + "Config and data successfully reloaded.");
					
					return true;
				}
				
				if(args[0].equalsIgnoreCase("buy")){
					
					if(!p.hasPermission(plugin.treasureBuy)){
						
						p.sendMessage(plugin.settings.config.getString("prefix") 
								+ plugin.settings.config.getString("no-permission-msg")
								.replaceAll("(&([a-f0-9]))", "\u00A7$2"));
						
						return true;
						
					}
					
					p.sendMessage(plugin.settings.config.getString("prefix") 
							+ plugin.settings.config.getString("buy-link")
							.replaceAll("(&([a-f0-9]))", "\u00A7$2"));
					
					return true;
					
				}


			}

		}

		return false;
	}
}
