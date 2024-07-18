package me.auvq.friendsystem.utils;


import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class ItemBuilder {
    private final ItemMeta meta;

    private ItemStack base;

    public ItemBuilder(@NotNull ItemStack base, @NotNull ItemMeta meta) {
        this.base = base;
        this.meta = meta;
    }

    @NotNull
    public static ItemBuilder empty() {
        return from(Material.AIR);
    }

    @NotNull
    public static ItemBuilder from(@NotNull Material material) {
        return from(new ItemStack(material));
    }

    @NotNull
    public static ItemBuilder from(@NotNull ItemStack item) {
        return new ItemBuilder(new ItemStack(item), item.getItemMeta());
    }

    @NotNull
    public ItemStack build() {
        return this.base;
    }

    @NotNull
    public ItemBuilder skull(@NotNull UUID uuid) {
        updateItem(item -> {
            item.setType(Material.PLAYER_HEAD);
            return item;
        });
        updateFunctional(meta -> {
            SkullMeta skullMeta = (SkullMeta)meta;
            skullMeta.setOwner(Bukkit.getOfflinePlayer(uuid).getName());
            return (ItemMeta)skullMeta;
        });
        return this;
    }

    @NotNull
    public ItemBuilder spawnEgg(@NotNull EntityType type) {
        updateFunctional(meta -> {
            SpawnEggMeta spawnEggMeta = (SpawnEggMeta)meta;
            spawnEggMeta.setCustomSpawnedType(type);
            return (ItemMeta)spawnEggMeta;
        });
        return this;
    }

    @NotNull
    public ItemBuilder durability(int durability) {
        return updateItem(item -> {
            item.setDurability((short)durability);
            return item;
        });
    }

    @NotNull
    public ItemBuilder type(@NotNull Material material) {
        return updateItem(itemStack -> {
            itemStack = new ItemStack(material);
            itemStack.setItemMeta(this.meta);
            return itemStack;
        });
    }

    @NotNull
    public final ItemBuilder enchant(@NotNull Enchantment enchantment, int level) {
        return updateItem(item -> {
            item.addUnsafeEnchantment(enchantment, level);
            return item;
        });
    }

    @NotNull
    public final ItemBuilder potion(@NotNull PotionType type, boolean splash, int level) {
        return updateItem(item -> {
            Potion potion = new Potion(type, level);
            potion.setSplash(splash);
            return potion.toItemStack(item.getAmount());
        });
    }

    @NotNull
    public final ItemBuilder name(@NotNull String name) {
        return update(itemMeta -> itemMeta.setDisplayName(CC.color(name)));
    }

    @NotNull
    public final ItemBuilder updateName(@NotNull Function<String, String> function) {
        return update(itemMeta -> {
            if (itemMeta.getDisplayName() == null || !itemMeta.hasDisplayName())
                itemMeta.setDisplayName(CC.color(function.apply("")));
            itemMeta.setDisplayName(CC.color(function.apply(itemMeta.getDisplayName())));
        });
    }

    @NotNull
    public final ItemBuilder updateLore(@NotNull Function<List<String>, List<String>> function) {
        return update(itemMeta -> {
            if (!itemMeta.hasLore())
                itemMeta.setLore(CC.toColor((List<String>)function.apply(new ArrayList())));
            itemMeta.setLore(CC.toColor(function.apply(itemMeta.getLore())));
        });
    }

    @NotNull
    public final ItemBuilder lore(@NotNull List<String> lore) {
        return update(itemMeta -> itemMeta.setLore(CC.toColor(lore)));
    }

    @NotNull
    public final ItemBuilder lore(@NotNull String... lore) {
        return lore(
                Arrays.asList(lore));
    }

    @NotNull
    public final ItemBuilder lore(@NotNull String lore) {
        return update(itemMeta -> {
            if (!itemMeta.hasLore())
                itemMeta.setLore(Collections.singletonList(lore));
            List<String> itemLore = itemMeta.getLore();
            itemLore.add(lore);
            itemMeta.setLore(CC.toColor(itemLore));
        });
    }

    @NotNull
    public final ItemBuilder amount(int amount) {
        return updateItem(itemStack -> {
            itemStack.setAmount(amount);
            return itemStack;
        });
    }

    @NotNull
    public final ItemBuilder data(byte data) {
        return updateItemBuilder(itemStack -> {
            MaterialData materialData = itemStack.getData();
            materialData.setData(data);
            return data(materialData);
        });
    }

    @NotNull
    public final ItemBuilder flags(@NotNull ItemFlag... itemFlags) {
        return update(itemMeta -> itemMeta.addItemFlags(itemFlags));
    }

    @NotNull
    public final ItemBuilder unbreakable(boolean unbreakable) {
        return update(meta -> meta.setUnbreakable(unbreakable));
    }

    @NotNull
    public final ItemBuilder dye(@NotNull Color color) {
        return updateItem(item -> {
            if (item.getType().name().startsWith("LEATHER_")) {
                LeatherArmorMeta armorMeta = (LeatherArmorMeta)item.getItemMeta();
                armorMeta.setColor(color);
                item.setItemMeta((ItemMeta)armorMeta);
            }
            return item;
        });
    }

    @NotNull
    public final ItemBuilder data(@NotNull MaterialData data) {
        return updateItem(itemStack -> {
            itemStack.setData(data);
            return itemStack;
        });
    }

    @NotNull
    public final ItemBuilder damage(short damage) {
        return updateItem(itemStack -> {
            itemStack.setDurability(damage);
            return itemStack;
        });
    }

    @NotNull
    public final ItemBuilder enchantIf(@NotNull int level, @NotNull Enchantment enchantment, boolean why) {
        if (!why)
            return this;
        updateItem(item -> {
            item.addEnchantment(enchantment, level);
            return item;
        });
        return this;
    }

    @NotNull
    public ItemBuilder updateItemBuilder(@NotNull Function<ItemStack, ItemBuilder> function) {
        return function.apply(this.base);
    }

    @NotNull
    public final ItemBuilder updateItem(@NotNull Function<ItemStack, ItemStack> function) {
        this.base = function.apply(this.base);
        return this;
    }

    @NotNull
    public final ItemBuilder updateFunctional(@NotNull Function<ItemMeta, ItemMeta> function) {
        this.base.setItemMeta(function.apply(this.meta));
        return this;
    }

    @NotNull
    private final ItemBuilder update(@NotNull Consumer<ItemMeta> consumer) {
        consumer.accept(this.meta);
        this.base.setItemMeta(this.meta);
        return this;
    }
}