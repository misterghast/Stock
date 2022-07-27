package org.hypbase.stock.event;

import io.papermc.paper.text.PaperComponents;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.hypbase.stock.block.StockTierRegistry;
import org.hypbase.stock.item.DebugExplosionStick;
import org.hypbase.stock.item.StockItem;
import org.hypbase.stock.item.StockItemUtil;
import org.hypbase.stock.item.StockItemsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockListener implements Listener {
    StockItemUtil utils;
    private static List<Player> playersDigging;
    private static final PotionEffectType MINING_FATIGUE = PotionEffectType.SLOW_DIGGING;

    public StockListener(StockItemUtil utils) {

        this.utils = utils;
        this.playersDigging = new ArrayList<Player>();
    }
    public static DebugExplosionStick DEBUG_ExplosionStick = new DebugExplosionStick("cbt.stick", StockItemUtil.UNCOMMON_COLOR, NamespacedKey.minecraft("wooden_sword"), 2, 250);

    //this event is used for debugging + setting block and pickaxe material tiers
    @EventHandler
    public void onRegister(StockRegisterEvent event) {
        playersDigging = new ArrayList<Player>();
        System.out.println("Registration works!");
        StockItemsRegistry registry = event.getRegistry();
        event.getModelRegistry().registerRange("stock", 2);
        registry.register("stock", "debug_item", new StockItem("cbt.lol", StockItemUtil.RARE_COLOR, NamespacedKey.minecraft("clay"), 1));
        registry.register("stock", "debug_explosion_stick", DEBUG_ExplosionStick);
        event.getTierRegistry().register(Material.OBSIDIAN.getKey(), StockItem.StockTiers.VANILLA_REDSTONE, false);
        event.getTierRegistry().register(Material.IRON_PICKAXE.getKey(), StockItem.StockTiers.VANILLA_REDSTONE, true);
        event.getTierRegistry().iterator().forEachRemaining(entry -> {
            System.out.println(((Map.Entry<NamespacedKey, StockItem.StockTiers>)entry).getValue());
        });

    }

    @EventHandler
    public void onStopBreaking(BlockDamageAbortEvent event) {
        if(playersDigging.contains(event.getPlayer())) {
            if(StockTierRegistry.getInstance().canMine(event.getItemInHand(), event.getBlock())) {
                adjustPotionEffect(event.getPlayer(), MINING_FATIGUE, 2, false);
            } else {
                adjustPotionEffect(event.getPlayer(), MINING_FATIGUE, -1, false);
            }
        }

        playersDigging.remove(event.getPlayer());
    }

    /*@EventHandler
    public void swapTool(PlayerSwapHandItemsEvent event) {
            if(playersDigging.contains(event.getPlayer())) {
                refreshDiggingEffects(event.getPlayer(), event.getMainHandItem(), event.getPlayer().getTargetBlock(4));
            }
    }*/

    @EventHandler
    public void onStartBreaking(
        BlockDamageEvent event
    ) {
        if(!StockTierRegistry.getInstance().canMine(event.getItemInHand(), event.getBlock())) {
            adjustPotionEffect(event.getPlayer(), MINING_FATIGUE, 1, true);
            playersDigging.add(event.getPlayer());
        } else if(!event.getBlock().isPreferredTool(event.getItemInHand())) {
            adjustPotionEffect(event.getPlayer(), MINING_FATIGUE, -2, true);;
            playersDigging.add(event.getPlayer());
        }
    }

    public void refreshDiggingEffects(Player player, ItemStack heldItem, Block attacked) {
        if(StockTierRegistry.getInstance().canMine(heldItem, attacked)) {
            adjustPotionEffect(player, MINING_FATIGUE, -2, false);
        } else {
            adjustPotionEffect(player, MINING_FATIGUE, 1, false);
        }

        if(!StockTierRegistry.getInstance().canMine(heldItem, attacked)) {
            adjustPotionEffect(player, MINING_FATIGUE, 1, true);
        } else if(!attacked.isPreferredTool(heldItem)) {
            adjustPotionEffect(player, MINING_FATIGUE, -2, true);
        }
    }

    private void adjustPotionEffect(Player player, PotionEffectType type, int amount, boolean giveEffect) {
        PotionEffect effect = player.getPotionEffect(type);
        if(effect != null) {
            if(player.getPotionEffect(type).getAmplifier() + amount == 0) {
                //do nothing
            } else {
                player.addPotionEffect(effect.withAmplifier(effect.getAmplifier() + amount), true);
            }
            player.removePotionEffect(type);
        } else if(giveEffect) {
            player.addPotionEffect(new PotionEffect(type, 1200, amount, true));
        }
    }

    @EventHandler
    public void onBreakingBlock(BlockBreakEvent event) {
        if(event.getPlayer() != null) {
            event.setDropItems(StockTierRegistry.getInstance().canMine(event.getPlayer().getInventory().getItemInMainHand(), event.getBlock()));
        }
    }

    /*private callSpecialItemEvent(ItemStack stack, Runnable methodToCall) {
            NamespacedKey key = utils.getNamespacedKeyFromStack(stack);
            if(utils.itemExists(key, true)) {
                ((StockItem) StockSpecialItemsRegistry.STOCK_SPECIAL_ITEMS.get(key))::method;
            }
    }*/
}
