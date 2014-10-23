/*
 * Copyright 2014 TheJeterLP.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.thejeterlp.bukkit.viruscmd.commands.mob;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.BaseCommand;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandArgs;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Command.CommandResult;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

/**
 * @author TheJeterLP
 */
public class Command_Spawnmob extends BaseCommand {

    public Command_Spawnmob() {
        super("spawnmob");
    }

    @Override
    public CommandResult onPlayerCommand(Player p, Command cmd, CommandArgs args) throws Exception {
        if (args.getLength() == 1 && args.getString(0).equalsIgnoreCase("help")) {
            p.getInventory().addItem(getHelpBook());
            return Utils.sendMessage(MessageType.INFO, p, "You got a book with help!");
        }

        if (args.getLength() != 2) return CommandResult.ERROR;
        if (!args.isInteger(1)) return CommandResult.NOT_A_NUMBER;

        EntityType type = null;

        try {
            type = EntityType.valueOf(args.getString(0).toUpperCase());
        } catch (IllegalArgumentException e) {
            return Utils.sendMessage(MessageType.ERROR, p, "That mob is not existing.");
        }

        if (!type.isSpawnable() || !type.isAlive()) return Utils.sendMessage(MessageType.ERROR, p, "That mob is not spawnable!");

        for (int i = 0; i < args.getInt(1); i++) {
            p.getWorld().spawnEntity(Utils.getLocationLooking(p, 10), type);
        }

        return Utils.sendMessage(MessageType.INFO, p, "You spawned some entities.");

    }

    @Override
    public CommandResult onServerCommand(CommandSender sender, Command cmd, CommandArgs args) throws Exception {
        return CommandResult.ONLY_PLAYER;
    }

    @Override
    public boolean argsCheck(CommandArgs args) {
        return args.getLength() <= 2 && !args.isEmpty();
    }

    private ItemStack getHelpBook() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor("VirusCraftNetwork");
        meta.setTitle("§5/spawnmob help");
        meta.setLore(Arrays.asList("§aClick to view help."));

        List<String> mobs = new ArrayList<>();

        for (EntityType e : EntityType.values()) {
            if (!e.isAlive() || !e.isSpawnable()) continue;
            mobs.add("/spawnmob" + e.toString().toUpperCase() + " <number> - Spawns " + e.toString().toUpperCase() + " <number> times.");
        }

        meta.setPages(mobs);
        book.setItemMeta(meta);

        return book;
    }

}
