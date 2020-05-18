package xyz.zeeraa.instantvoidkill;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class InstantVoidKill extends JavaPlugin implements Listener {
	private double instantKillY;
	private boolean autoRespawn;
	private boolean alwaysAutoRespawn;

	@Override
	public void onEnable() {
		saveDefaultConfig();

		instantKillY = getConfig().getDouble("y_level");
		autoRespawn = getConfig().getBoolean("auto_respawn");
		alwaysAutoRespawn = getConfig().getBoolean("always_auto_respawn");

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (p.getLocation().getY() < instantKillY) {
						if (p.getHealth() > 0) {
							if (p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE) {
								p.setHealth(0);
								if (autoRespawn && !alwaysAutoRespawn) {
									p.spigot().respawn();
								}
							}
						}
					}
				}
			}
		}, 5L, 5L);

		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll((Plugin) this);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (alwaysAutoRespawn) {
			e.getEntity().spigot().respawn();
		}
	}
}