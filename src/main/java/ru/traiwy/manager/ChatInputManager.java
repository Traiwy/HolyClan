package ru.traiwy.manager;

import lombok.AllArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.traiwy.util.ClanPromptText;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@AllArgsConstructor
public class ChatInputManager implements Listener {

    private static final Map<UUID, Consumer<String>> waitingPlayers = new HashMap<>();
    private final Map<UUID, BukkitTask> repeatingTasks = new HashMap<>();

    private final JavaPlugin plugin;


    public void waitForInput(Player player, String prompt, Consumer<String> callback) {
        UUID uuid = player.getUniqueId();
        waitingPlayers.put(uuid, callback);

        player.sendMessage(prompt);

        BukkitTask task = new BukkitRunnable() {
            int seconds = 0;

            @Override
            public void run() {
                if (!waitingPlayers.containsKey(uuid)) {
                    cancel();
                    return;
                }

                if (seconds++ >= 5 * 60) {
                    player.sendMessage("Время ожидания истекло!");
                    waitingPlayers.remove(uuid);
                    cancel();
                    return;
                }

                player.sendMessage(prompt);

            }
        }.runTaskTimer(plugin, 20L, 20L);

        repeatingTasks.put(uuid, task);
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!waitingPlayers.containsKey(uuid)) return;

        event.setCancelled(true);
        String message = event.getMessage();
        Consumer<String> callback = waitingPlayers.remove(uuid);

        BukkitTask task = repeatingTasks.remove(uuid);
        if (task != null) task.cancel();

        Bukkit.getScheduler().runTask(plugin, () -> callback.accept(message));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        waitingPlayers.remove(uuid);
        BukkitTask task = repeatingTasks.remove(uuid);
        if (task != null) task.cancel();
    }

    public void cancelPrompt(Player player) {
        UUID uuid = player.getUniqueId();

        waitingPlayers.remove(uuid);

        BukkitTask task = repeatingTasks.remove(uuid);
        if (task != null) task.cancel();

    }
}