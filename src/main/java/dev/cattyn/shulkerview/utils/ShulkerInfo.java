package dev.cattyn.shulkerview.utils;

import dev.cattyn.shulkerview.ShulkerViewEntrypoint;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record ShulkerInfo(ItemStack shulker, boolean compact, int color, int slot, List<ItemStack> stacks) {

    public static ShulkerInfo create(ItemStack stack, int slot) {
        if (!(stack.getItem() instanceof BlockItem) || !(((BlockItem) stack.getItem()).getBlock() instanceof ShulkerBoxBlock)) return null;
        ShulkerBoxBlock block = (ShulkerBoxBlock) ((BlockItem) stack.getItem()).getBlock();

        ContainerComponent blockContainer = stack.getComponents().get(DataComponentTypes.CONTAINER);
        List<ItemStack> items = DefaultedList.ofSize(27, ItemStack.EMPTY);

        IntStream.range(0, blockContainer.stream().toList().size()).forEach(i -> items.set(i, blockContainer.stream().toList().get(i)));

        boolean compact = ShulkerViewEntrypoint.getInstance().getConfig().isCompact();

        if (compact) {
            Map<Item, Integer> map = new HashMap<>();
            for (ItemStack item : items) {
                if (item.isEmpty()) continue;
                map.put(item.getItem(), item.getCount() + map.getOrDefault(item.getItem(), 0));
            }
            items.clear();
            int k = 0;
            for (Map.Entry<Item, Integer> entry : map.entrySet()) {
                items.set(k, new ItemStack(entry.getKey(), entry.getValue()));
                k++;
            }
        }

        int color = 0xff9953b0;
        if (block.getColor() != null) {

            int[] col = RGBIntToRGB(block.getColor().getSignColor());
            color = new Color(col[0], col[1], col[2]).hashCode();
        }

        return new ShulkerInfo(stack, compact, color, slot, items);
    }

    public static int[] RGBIntToRGB(int in) {
        int red = in >> 8 * 2 & 0xFF;
        int green = in >> 8 & 0xFF;
        int blue = in & 0xFF;
        return new int[]{red, green, blue};
    }

}