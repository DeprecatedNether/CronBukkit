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

public class Cron {

    String line;
    String[] split;
    boolean at = false;

    public Cron(String cronEntry) {
        this.line = cronEntry;
        if (line.startsWith("@")) {
            at = true;
            split = this.line.split(" ", 2);
        } else {
            split = this.line.split(" ", 6);
        }
    }

    /**
     * Gets the cron entry passed when creating this Cron instance
     * @return The line
     */
    public String getLine() {
        return line;
    }

    /**
     * Gets the entry for minute. It will either return the minute or a special character ("*" for every minute, "*\/5" for every 5 minutes ...).
     * @return The entry for minute (0-59, special character) or 61 if the cron is "special" (such as "@reboot")
     */
    public String getMinute() {
        if (at) return "61"; return split[0];
    }

    /**
     * Gets the entry for hour. It will either return the hour or a special character ("*" for every hour, "*\/5" for every 5 hours ...).
     * @return The entry for hour (0-23, special character) or 25 if the cron is "special" (such as "@reboot")
     */
    public String getHour() {
        if (at) return "25"; return split[1];
    }

    /**
     * Gets the entry for day of month. It will either return the day of month or a special character ("*" for every day of month, "*\/5" for every 5th day ...).
     * @return The entry for hour (1-31, special character) or 32 if the cron is "special" (such as "@reboot")
     */
    public String getDoM() {
        if (at) return "32"; return split[2];
    }

    /**
     * Gets the entry for month. It will either return the month or a special character ("*" for every month, "*\/5" for every 5 months ...).
     * @return The entry for month (1-12, special character) or 13 if the cron is "special" (such as "@reboot")
     */
    public String getMonth() {
        if (at) return "13"; return split[3];
    }

    /**
     * Gets the entry for day of week. It will either return the day of week or a special character ("*" for every day, "*\/5" for every 5 days ...).
     * @return The entry for day of week (0-7, special character) or 8 if the cron is "special" (such as "@reboot")
     */
    public String getDoW() {
        if (at) return "8"; return split[4];
    }

    /**
     * Gets the predefined scheduling definition, such as @reboot (run when plugin enabled).
     * @return The predefined definition (with the preceding @) or null if the entry doesn't use a predefined definition.
     */
    public String getPredefined() {
        if (at) return split[0]; return null;
    }

    /**
     * Gets the command to be run.
     * @return The command.
     */
    public String getCommand() {
        if (at) return split[1]; return split[5];
    }
}
