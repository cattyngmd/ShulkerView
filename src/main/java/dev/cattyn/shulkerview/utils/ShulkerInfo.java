package dev.cattyn.shulkerview.utils;

import dev.cattyn.shulkerview.ShulkerViewEntrypoint;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.ColorHelper;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ShulkerInfo(ItemStack shulker, boolean compact, int color, int slot, List<ItemStack> stacks) {

    public static ShulkerInfo create(ItemStack stack, int slot) {
        if (!(stack.getItem() instanceof BlockItem) || !(((BlockItem) stack.getItem()).getBlock() instanceof ShulkerBoxBlock)) return null;
        ShulkerBoxBlock block = (ShulkerBoxBlock) ((BlockItem) stack.getItem()).getBlock();
        List<ItemStack> items = DefaultedList.ofSize(27, ItemStack.EMPTY);

        ContainerComponent component = stack.getComponents().get(DataComponentTypes.CONTAINER);

        boolean compact = ShulkerViewEntrypoint.getInstance().getConfig().isCompact();

        if (component != null) {
            Item unstackable = null;

            List<ItemStack> list = component.stream().toList();
            for (int i = 0; i < list.size(); i++) {
                ItemStack item = list.get(i);
                items.set(i, item);
                if (item.getMaxCount() == 1) {
                    if (unstackable != null && !item.getItem().equals(unstackable)) compact = false;
                    unstackable = item.getItem();
                }
            }

        }

        if (compact) {
            shrinkToCompact(items);
        }

        int color = 0xff9953b0;
        if (block.getColor() != null) {
            color = ColorHelper.Argb.withAlpha(255, block.getColor().getMapColor().color);
        }

        return new ShulkerInfo(stack, compact, color, slot, items);
    }

    private static void shrinkToCompact(List<ItemStack> items) {
        Map<Item, Integer> map = new HashMap<>();
        for (ItemStack item : items) {
            if (item.isEmpty()) continue;

            int initial = map.getOrDefault(item.getItem(), 0);
            map.put(item.getItem(), item.getCount() + initial);
        }
        items.clear();
        int k = 0;
        for (Map.Entry<Item, Integer> entry : map.entrySet()) {
            items.set(k, new ItemStack(entry.getKey(), entry.getValue()));
            k++;
        }
    }

}