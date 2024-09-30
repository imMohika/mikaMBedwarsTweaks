package ir.mohika.mikambedwarstweaks.listeners;

import ir.mohika.mikambedwarstweaks.MikaMBedwarsTweaks;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class CarbonNPEListener implements Listener {
  private final MikaMBedwarsTweaks plugin;

  public CarbonNPEListener(MikaMBedwarsTweaks plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void playerSpawn(PlayerSpawnLocationEvent event) {
    Player player = event.getPlayer();
    if (player.getGameMode() == null) {
      GameMode defaultGamemode = GameMode.SURVIVAL;
      plugin
          .getLogger()
          .info(
              "Gamemode of "
                  + player.getName()
                  + " was null, setting it to "
                  + defaultGamemode.name()
                  + " [PSL]");
      player.setGameMode(defaultGamemode);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void playerLogin(PlayerLoginEvent event) {
    Player player = event.getPlayer();
    if (player.getGameMode() == null) {
      GameMode defaultGamemode = GameMode.SURVIVAL;
      plugin
          .getLogger()
          .info(
              "Gamemode of "
                  + player.getName()
                  + " was null, setting it to "
                  + defaultGamemode.name()
                  + " [PL]");
      player.setGameMode(defaultGamemode);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void playerLogin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (player.getGameMode() == null) {
      GameMode defaultGamemode = GameMode.SURVIVAL;
      plugin.getLogger().info("Gamemode of " + player.getName() + defaultGamemode.name() + " [PJ]");
      player.setGameMode(defaultGamemode);
    }
  }
}
