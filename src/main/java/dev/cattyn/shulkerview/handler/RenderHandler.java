package dev.cattyn.shulkerview.handler;

import dev.cattyn.shulkerview.Globals;
import dev.cattyn.shulkerview.ShulkerViewEntrypoint;
import dev.cattyn.shulkerview.utils.ShulkerInfo;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2d;

public class RenderHandler implements Globals {
    private final Vector2d clicked = new Vector2d();
    private int height, offset;

    public void render(DrawContext context, double mouseX, double mouseY) {
        int y = 3 + offset;
        for (ShulkerInfo shulkerInfo : ShulkerViewEntrypoint.getInstance().getUpdateHandler().getShulkerList()) {
            int count = 0, x = 2, startY = y, maxX = 22;
            for (ItemStack stack : shulkerInfo.stacks()) {
                if (shulkerInfo.compact() && stack.isEmpty()) break;
                if (count > 0 && count % 9 == 0) {
                    x = 2;
                    y += 18;
                }
                context.drawItem(stack, x + 2, y);
                if (stack.getCount() > 999) {
                    String text = "%.1fk".formatted(stack.getCount() / 1000f);
                    context.drawItemInSlot(mc.textRenderer, stack, x + 2, y, text);
                } else {
                    context.drawItemInSlot(mc.textRenderer, stack, x + 2, y);
                }
                x += 20;
                count++;
                if (x > maxX) maxX = x;
            }
            if (count == 0 && shulkerInfo.compact()) {
                context.drawItem(shulkerInfo.shulker(), x + 2, y + 1);
            }
            y += 18;
            if (clicked.lengthSquared() != 0
                    && clicked.x >= 2 && clicked.x <= maxX
                    && clicked.y >= startY && clicked.y <= y) {
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, shulkerInfo.slot(), 0, SlotActionType.PICKUP, mc.player);
                clicked.set(0);
            }
            context.fill(2, startY, maxX, y, ShulkerViewEntrypoint.getInstance().getConfig().getBackground());
            context.fill(2, startY - 1, maxX, startY, shulkerInfo.color());
            y += 2;
        }

        height = y - offset;
        clicked.set(0);
    }

    public void mouseClick(double x, double y, int button) {
        if (button != 0) return;
        clicked.set(x, y);
    }

    public void mouseScroll(double x, double y, double amount) {
        if (amount == 0) return;
        this.offset = MathHelper.clamp((int) (offset + Math.ceil(amount) * 10), -height + mc.getWindow().getScaledHeight(), 0);
    }
}
