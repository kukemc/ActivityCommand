package su.activitycommand;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class Activitycommand extends JavaPlugin implements Listener {

    private File dataFile;
    private FileConfiguration dataConfig;

    @Override
    public void onEnable() {
        // 保存默认配置
        this.saveDefaultConfig();

        // 加载领取记录的配置文件
        createDataFile();

        // 注册事件监听器
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Activitycommand 插件已启用");
    }

    @Override
    public void onDisable() {
        saveDataConfig(); // 插件禁用时保存数据
        getLogger().info("Activitycommand 插件已禁用");
    }

    // 创建或加载 data.yml 文件
    private void createDataFile() {
        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    // 保存 data.yml
    private void saveDataConfig() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            getLogger().severe("无法保存 data.yml 文件: " + e.getMessage());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = this.getConfig();

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        String today = currentDate.toString(); // 日期格式为 yyyy-MM-dd
        UUID playerUUID = player.getUniqueId();

        // 延迟三秒执行任务
        Bukkit.getScheduler().runTaskLater(this, () -> {
            // 检查配置中的活动
            if (config.contains("events")) {
                for (String eventDate : config.getConfigurationSection("events").getKeys(false)) {
                    if (eventDate.equals(today)) {
                        // 检查玩家是否已经领取过奖励
                        if (hasPlayerClaimed(playerUUID, eventDate)) {
                            player.sendMessage("你已经领取过今天的活动奖励！");
                            return; // 玩家已经领取过奖励，不再执行指令
                        }

                        // 获取相应的指令列表
                        List<String> commands = config.getStringList("events." + eventDate + ".commands");

                        // 执行每条指令，替换占位符
                        for (String command : commands) {
                            String formattedCommand = command.replace("%player%", player.getName());
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedCommand);
                        }

                        // 标记玩家已经领取奖励
                        markPlayerClaimed(playerUUID, eventDate);
                        break;`
                    }
                }
            }
        }, 60L); // 3秒 = 60 ticks (1秒 = 20 ticks)
    }

    // 检查玩家是否已经领取过指定日期的奖励
    private boolean hasPlayerClaimed(UUID playerUUID, String eventDate) {
        return dataConfig.contains("players." + playerUUID + "." + eventDate);
    }

    // 标记玩家已经领取指定日期的奖励
    private void markPlayerClaimed(UUID playerUUID, String eventDate) {
        dataConfig.set("players." + playerUUID + "." + eventDate, true);
        saveDataConfig();
    }
}
