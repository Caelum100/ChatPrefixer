package net.caelum100.chatprefixer.updatechecker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class ChatPrefixerUpdater {
  private JavaPlugin plugin;
  private String currentVersion;

  private boolean newVersionFound = false;

  private static final long INTERVAL = 20 * 60 * 60; // 1 hour
  private static final String API_URL = "https://api.spigotmc.org/legacy/update.php?resource=";
  private static final String PREFIX = "[ChatPrefixer] ";
  private static final String RESOURCE_ID = "56778";

  public ChatPrefixerUpdater(JavaPlugin plugin) {
    this.plugin = plugin;
    this.currentVersion = this.plugin.getDescription().getVersion();

    Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::run, 0, INTERVAL);
  }

  public void run() {
    if (newVersionFound) return;

    HttpsURLConnection con = null;
    try {
      URL url = new URL(API_URL + RESOURCE_ID);
      con = (HttpsURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.setReadTimeout(20 * 1000);

      con.connect();
      try (BufferedReader reader =
                   new BufferedReader(new InputStreamReader(con.getInputStream()))) {
        String latestVersion = reader.readLine();

        if (latestVersion != null && !currentVersion.equals(latestVersion)) {
          Bukkit.getConsoleSender()
                  .sendMessage(
                          String.format(
                                  PREFIX
                                          + ChatColor.AQUA
                                          + "A new version (v%s) is available! Please download it from the plugin resource page; "
                                          + "you will receive no support regarding old versions of the plugin.",
                                  latestVersion));
          newVersionFound = true;
        }
      }
    } catch (Exception e) {
      if (con != null) con.disconnect();
    }
  }
}
