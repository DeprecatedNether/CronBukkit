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

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CronTask extends BukkitRunnable {
    @Override
    public void run() {
        long now = System.currentTimeMillis();
        CronBukkit.lastrun = now;
        Calendar cal = new GregorianCalendar();
        int minute = (int)(now / 1000 / 60 % 60);
        int hour = (int)(now / 1000 / 60 / 60 % 24);
        try {
            BufferedReader br = new BufferedReader(new FileReader(CronBukkit.cronFile));
            String ln;
            while ((ln = br.readLine()) != null) {
                Cron cron = new Cron(ln);
                if (cron.getPredefined() != null) {
                    break; // We don't want to run predefineds in the scheduler.
                }
                // Check if it needs to be run now
                boolean canRun = true;

                // First, check minute
                String cMinute = cron.getMinute();

                if (cMinute.contains("/")) {
                    // Get the part after the /, so we get "5" from */5.
                    int min = Integer.parseInt(cMinute.split("/")[1]);
                    if (minute % min != 0) {
                        canRun = false;
                    }
                } else if (cMinute.equals("*")) {
                    // Keep it at its current value. Don't set to false because it CAN run as far as this is concerned,
                    // but don't change it to true because previous checks may have made it not run.
                } else if (minute != Integer.parseInt(cMinute)) {
                    canRun = false;
                }

                // Check the hour
                String cHour = cron.getHour();

                if (cHour.contains("/")) {
                    // Get the part after the /, so we get "5" from */5.
                    int h = Integer.parseInt(cMinute.split("/")[1]);
                    if (hour % h != 0) {
                        canRun = false;
                    }
                } else if (cHour.equals("*")) {
                    // Keep it at its current value. Don't set to false because it CAN run as far as this is concerned,
                    // but don't change it to true because previous checks may have made it not run.
                } else if (hour != Integer.parseInt(cHour)) {
                    canRun = false;
                }

                // TODO Date checking

                if (canRun) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cron.getCommand());
                }
            }
            br.close();
        } catch (IOException ioe) {
            CronBukkit.log.warning("An error occurred during Crontab execution");
            ioe.printStackTrace();
        }
    }
}
