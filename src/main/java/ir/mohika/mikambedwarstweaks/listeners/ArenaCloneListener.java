package ir.mohika.mikambedwarstweaks.listeners;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaCloningManager;
import de.marcely.bedwars.api.arena.QueuedCloningArena;
import de.marcely.bedwars.api.event.arena.AsyncArenaCloneQueueEvent;
import ir.mohika.mikambedwarstweaks.MikaMBedwarsTweaks;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaCloneListener implements Listener {
  private final MikaMBedwarsTweaks plugin;

  public ArenaCloneListener(MikaMBedwarsTweaks plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onClone(AsyncArenaCloneQueueEvent event) {
    ArenaCloningManager manager = GameAPI.get().getCloningManager();
    Iterator<QueuedCloningArena> it = event.getAddingQueue().iterator();

    while (it.hasNext()) {
      QueuedCloningArena queued = it.next();
      Arena originalArena = queued.getArena();

      if (manager.getQueue().contains(queued.getArena())) {
        it.remove();
        continue;
      }

      if (!ArenaType.isValid(originalArena)) {
        continue;
      }
      ArenaType originalType = ArenaType.from(originalArena);

      // count how many players we already have per mode
      AtomicInteger originalCount = new AtomicInteger(0);
      AtomicInteger otherCount = new AtomicInteger(0);
      ArenaType otherType = originalType.other();

      Consumer<Arena> count =
          arena -> {
            ArenaType type = ArenaType.from(arena);
            if (type == originalType) {
              originalCount.incrementAndGet();
            } else if (type == otherType) {
              otherCount.incrementAndGet();
            }
          };

      count.accept(originalArena);
      Arrays.stream(originalArena.getClones()).forEach(count);

      // identify the next needed mode
      ArenaType nextType = originalCount.get() <= otherCount.get() ? originalType : otherType;

      queued.addCallback(
          result -> {
            if (result.isEmpty()) return;
            result.get().setPlayersPerTeam(nextType.playersPerTeam);
          });
    }
  }

  public enum ArenaType {
    SOLO(8, 1),
    DUO(8, 2),
    TRIO(4, 3),
    QUAD(4, 4);

    private final int teams;
    private final int playersPerTeam;

    ArenaType(int teams, int playersPerTeam) {
      this.teams = teams;
      this.playersPerTeam = playersPerTeam;
    }

    public static boolean isValid(Arena arena) {
      return isValid(arena.getEnabledTeams().size(), arena.getPlayersPerTeam());
    }

    public static boolean isValid(int teams, int playersPerTeam) {
      return Arrays.stream(ArenaType.values())
          .anyMatch(t -> t.teams == teams && t.playersPerTeam == playersPerTeam);
    }

    public static ArenaType from(Arena arena) {
      return from(arena.getEnabledTeams().size(), arena.getPlayersPerTeam());
    }

    public static ArenaType from(int teams, int playersPerTeam) {
      return Arrays.stream(ArenaType.values())
          .filter(t -> t.teams == teams && t.playersPerTeam == playersPerTeam)
          .findFirst()
          .orElseThrow(
              () ->
                  new IllegalArgumentException(
                      "No ArenaType found for teams: "
                          + teams
                          + " and playersPerTeam: "
                          + playersPerTeam));
    }

    public ArenaType other() {
      return Arrays.stream(ArenaType.values())
          .filter(t -> t != this)
          .filter(t -> t.teams == this.teams && t.playersPerTeam != this.playersPerTeam)
          .findFirst()
          .orElseThrow(
              () ->
                  new IllegalArgumentException(
                      "No other ArenaType found for teams: "
                          + teams
                          + " and playersPerTeam: "
                          + playersPerTeam));
    }
  }
}
