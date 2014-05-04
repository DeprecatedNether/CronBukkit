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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class CronBukkit extends JavaPlugin {

    public static Logger log;
    private BukkitTask crontask;
    public static long lastrun;
    public static File cronFile;

    public void onEnable() {
        log = getLogger();
        cronFile = new File(getDataFolder(), "crontab");
        if (!cronFile.exists()) {
            getDataFolder().mkdirs();
            try {
                cronFile.createNewFile();
                log.info("Created empty crontab file.");
            } catch (IOException ioe) {
                log.severe("An error occurred while creating an empty crontab file.");
                ioe.printStackTrace();
            }
        }

        // Run the @reboot crons
        try {
            BufferedReader br = new BufferedReader(new FileReader(cronFile));
            String ln;
            while ((ln = br.readLine()) != null) { // http://stackoverflow.com/a/5868528/3551129 <3
                Cron cron = new Cron(ln);
                String predefined = cron.getPredefined();
                if (predefined != null) {
                    if (predefined.equals("@reboot")) {
                        getServer().dispatchCommand(getServer().getConsoleSender(), cron.getCommand());
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // Initialize the cron daemon
        crontask = new CronTask().runTaskTimer(this, 0, 1200); // todo add some sort of a handler so tasks still get run if the server is lagging (if the tps dropped to 10, this will get called every 2 minutes instead of 1!)
    }

    public void onDisable() {
        crontask.cancel();
        try {
            BufferedReader br = new BufferedReader(new FileReader(cronFile));
            String ln;
            while ((ln = br.readLine()) != null) {
                Cron cron = new Cron(ln);
                String predefined = cron.getPredefined();
                if (predefined != null) {
                    if (predefined.equals("@shutdown")) {
                        getServer().dispatchCommand(getServer().getConsoleSender(), cron.getCommand());
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
