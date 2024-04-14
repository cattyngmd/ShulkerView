package dev.cattyn.shulkerview.utils;

import dev.cattyn.shulkerview.ShulkerViewEntrypoint;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ShulkerInfo(String name, boolean compact, int color, int slot, List<ItemStack> stacks) {

    public static ShulkerInfo create(ItemStack stack, int slot) {
        if (!(stack.getItem() instanceof BlockItem) || !(((BlockItem) stack.getItem()).getBlock() instanceof ShulkerBoxBlock)) return null;
        ShulkerBoxBlock block = (ShulkerBoxBlock) ((BlockItem) stack.getItem()).getBlock();
        List<ItemStack> items = DefaultedList.ofSize(27, ItemStack.EMPTY);

        NbtCompound nbt = stack.getOrCreateNbt().contains("BlockEntityTag", 10)
                ? stack.getNbt().getCompound("BlockEntityTag") : stack.getNbt();

        boolean compact = ShulkerViewEntrypoint.getInstance().getConfig().isCompact();

        if (nbt.contains("Items", 9)) {
            Item unstackable = null;
            NbtList nbt2 = nbt.getList("Items", 10);
            for (int i = 0; i < nbt2.size(); i++) {
                int slot2 = nbt2.getCompound(i).contains("Slot", 99) ? nbt2.getCompound(i).getByte("Slot") : i;
                ItemStack item = ItemStack.fromNbt(nbt2.getCompound(i));
                items.set(slot2, item);
                if (item.getMaxCount() == 1) {
                    if (unstackable != null && !item.getItem().equals(unstackable)) compact = false;
                    unstackable = item.getItem();
                }
            }
        }

        if (compact) {
            Map<Item, Integer> map = new HashMap<>();
            for (ItemStack item : items) {
                if (item.isEmpty()) continue;
                map.compute(item.getItem(), (k, v) -> {
                    if (v == null) return item.getCount();
                    return v + item.getCount();
                });
            }
            items.clear();
            int k = 0;
            for (Map.Entry<Item, Integer> entry : map.entrySet()) {
                items.set(k, new ItemStack(entry.getKey(), entry.getValue()));
                k++;
            }
        }

        int color = new Color(0x9953b0, false).hashCode();
        if (block.getColor() != null) {
            float[] components = block.getColor().getColorComponents();
            color = new Color(components[0], components[1], components[2]).hashCode();
        }

        return new ShulkerInfo(stack.getName().getString(), compact, color, slot, items);
    }

}