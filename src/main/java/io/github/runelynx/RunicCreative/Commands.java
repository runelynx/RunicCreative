package io.github.runelynx.RunicCreative;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.UNDERLINE;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.bukkit.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import mkremins.fanciful.FancyMessage;

/**
 * 
 * @author Andrewxwsaa
 */
public class Commands implements CommandExecutor {

	// pointer to your main class, not required if you don't need methods fromfg
	// the main class
	private Plugin instance = RunicCreative.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		// TODO Auto-generated method stub

		switch (cmd.getName()) {
		case "migrategroups":
			fixGroupManager();
			
			break;
		default:
			break;
		}

		return false;
	}
	
	public static void fixGroupManager() {

		// save any pending changes

		Bukkit.getLogger().log(Level.INFO, "Loading file");
		File userFile = new File("/home/AMP/.ampdata/instances/Survival/Minecraft/plugins/GroupManager/worlds/runicrealm",
				"groups.yml");

		FileConfiguration userConfig = YamlConfiguration.loadConfiguration(userFile);
		Bukkit.getLogger().log(Level.INFO, "Working file");
	
		Bukkit.getLogger().log(Level.INFO, "Getting groups");
			for (String cs : userConfig.getConfigurationSection("groups").getKeys(false)) {
				Bukkit.getLogger().log(Level.INFO, "Checking " + cs);		
				
				Boolean groupFound = false;
				
				for (String s : RunicCreative.perms.getGroups()) {
					
					if (s.equalsIgnoreCase(cs)) {
						//group already exists!
						groupFound = true;
						Bukkit.getLogger().log(Level.INFO, "Matched " + cs);
					} 
					
					
				}
				
				if (groupFound) {
					//group found, now populate the permissions
					for (String cs2 : userConfig.getStringList("groups."+ cs +".permissions")) {
						
						if (!RunicCreative.perms.groupHas("RunicRealm", cs, cs2)) {
							RunicCreative.perms.groupAdd("RunicRealm", cs, cs2);
							Bukkit.getLogger().log(Level.INFO, "Adding " + cs2 + " to " + cs);	
						}
					
					}
					
					
				} else {
					//group not found, add it, then populate permissions
					
					Bukkit.getLogger().log(Level.INFO, "GROUP NOT FOUND!!!!!!! " + cs);

				}
				
				
				
			}
			
			
			
			/*

			if (userConfig.contains("users." + p.getUniqueId().toString())) {
				// user has a UUID branch
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
						"sc Found " + ChatColor.GREEN + "good" + ChatColor.AQUA + " record for " + p.getDisplayName());

				if (userConfig.contains("users." + p.getName())) {
					// user has a name branch too!
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sc Also found " + ChatColor.RED + "bad"
							+ ChatColor.AQUA + " record for " + p.getDisplayName());
					try {
						userConfig.getConfigurationSection("users").set(p.getName(), null);
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sc Removed " + ChatColor.RED + "bad"
								+ ChatColor.AQUA + " record for " + p.getDisplayName());

					} catch (Exception e) {
						e.printStackTrace();
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sc Failed to remove " + ChatColor.RED + "bad"
								+ ChatColor.AQUA + " record for " + p.getDisplayName());
					}

				}

			} else if (userConfig.contains("users." + p.getName())) {				
					// user only has a name branch too!
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sc Found " + ChatColor.GOLD + "OK"
							+ ChatColor.AQUA + " record for " + p.getDisplayName());
			}
*/
		
		

	}
	
	

	public void goToServer(String serverName, Player player) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(serverName);

		// If you don't care about the player
		// Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(),
		// null);
		// Else, specify them

		player.sendPluginMessage(instance, "BungeeCord", out.toByteArray());
	}

}
