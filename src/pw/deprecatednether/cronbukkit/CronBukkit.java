/*
 * CronBukkit is a Bukkit plugin that implements a scheduler similar to cron.
 * Copyright (C) 2014  DeprecatedNether
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package pw.deprecatednether.cronbukkit;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

public class CronBukkit extends JavaPlugin {

    private BukkitTask crontask;
    public static long lastrun;
    public static File cronFile;

    public void onEnable() {
        cronFile = new File(getDataFolder(), "crontab");
        if (!cronFile.exists()) {
            getDataFolder().mkdirs();
            try {
                cronFile.createNewFile();
                getLogger().info("Created empty crontab file.");
            } catch (IOException ioe) {
                getLogger().severe("An error occurred while creating an empty crontab file.");
                ioe.printStackTrace();
            }
        }
    }

    public void onDisable() {

    }
}
