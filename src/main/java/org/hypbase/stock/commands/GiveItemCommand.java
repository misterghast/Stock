package org.hypbase.stock.commands;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hypbase.stock.item.StockItem;
import org.hypbase.stock.item.StockItemUtil;
import org.jetbrains.annotations.NotNull;

public class GiveItemCommand implements CommandExecutor {
    private StockItemUtil utils;

    public GiveItemCommand(StockItemUtil utils) {
        this.utils = utils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = Bukkit.getPlayer(strings[0]);
        if(player != null) {
            player.getInventory().addItem(utils.getNewStack(NamespacedKey.fromString(strings[1]), Integer.parseInt(strings[2])));
            return true;
        }
        return false;
    }
}
