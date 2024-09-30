package ir.mohika.mikambedwarstweaks;

import de.marcely.bedwars.api.BedwarsAddon;

public class TweaksAddon extends BedwarsAddon {
  private final MikaMBedwarsTweaks plugin;

  public TweaksAddon(MikaMBedwarsTweaks plugin) {
    super(plugin);
    this.plugin = plugin;
  }

  @Override
  public String getName() {
    return plugin.getName();
  }
}
