package ir.mohika.mikambedwarstweaks;

import ir.mohika.mikambedwarstweaks.listeners.ArenaCloneListener;
import ir.mohika.mikambedwarstweaks.listeners.CarbonNPEListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MikaMBedwarsTweaks extends JavaPlugin {
  private static MikaMBedwarsTweaks instance;
  private TweaksAddon addon;

  @Override
  public void onEnable() {
    if (!checkMBedwars()) {
      return;
    }

    if (!registerAddon()) {
      return;
    }

    instance = this;
    registerEvents();
  }

  private boolean checkMBedwars() {
    final int supportedAPIVersion = 112;
    final String supportedVersionName = "5.4.13";

    try {
      Class<?> apiClass = Class.forName("de.marcely.bedwars.api.BedwarsAPI");
      int apiVersion = (int) apiClass.getMethod("getAPIVersion").invoke(null);

      if (apiVersion < supportedAPIVersion) throw new IllegalStateException();
    } catch (Exception e) {
      getLogger()
          .warning(
              "Sorry, your installed version of MBedwars is not supported. Please install at least v"
                  + supportedVersionName);
      Bukkit.getPluginManager().disablePlugin(this);
      return false;
    }
    return true;
  }

  private boolean registerAddon() {
    addon = new TweaksAddon(this);

    if (!addon.register()) {
      getLogger()
          .warning(
              "It seems like this addon has already been loaded. Please delete duplicates and try again.");
      Bukkit.getPluginManager().disablePlugin(this);

      return false;
    }

    return true;
  }

  private void registerEvents() {
    getServer().getPluginManager().registerEvents(new CarbonNPEListener(this), this);
    getServer().getPluginManager().registerEvents(new ArenaCloneListener(this), this);
  }
}
