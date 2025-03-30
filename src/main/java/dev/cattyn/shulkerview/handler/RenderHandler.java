package dev.cattyn.shulkerview.handler;

import dev.cattyn.shulkerview.Globals;
import dev.cattyn.shulkerview.ShulkerViewEntrypoint;
import dev.cattyn.shulkerview.utils.ShulkerInfo;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.MathHelper;
import org.joml.Math;
import org.joml.Vector2d;

public class RenderHandler implements Globals {
    private static final int MARGIN = 2;
    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 18;

    private final Vector2d clicked = new Vector2d();
    private int height, offset;
    private int rows, cols;

    private int startX;
    private int currentY;

    public void render(DrawContext context, double mouseX, double mouseY) {
        boolean right = false;
        startX = MARGIN;
        currentY = usesBoth() ? MARGIN : MARGIN + offset;

        for (ShulkerInfo shulkerInfo : ShulkerViewEntrypoint.getInstance().getUpdateHandler().getShulkerList()) {
            updateShulkerInfo(shulkerInfo);

            if (currentY >= context.getScaledWindowHeight() && usesBoth() && !right) {
                right = true;
                currentY = MARGIN + offset;
            }

            if (right) {
                startX = context.getScaledWindowWidth() - cols * GRID_WIDTH - MARGIN;
            }
            handleShulkerInfo(context, shulkerInfo);
        }

        height = currentY - offset;
        clicked.set(0);
    }

    private void handleShulkerInfo(DrawContext context, ShulkerInfo info) {
        int cols = this.cols * GRID_HEIGHT;
        int rows = this.rows * GRID_WIDTH;
        int width = cols + MARGIN * this.cols;

        int x = startX;
        int y = currentY;
        int count = 0;

        if (currentY < context.getScaledWindowHeight()) {
            for (ItemStack stack : info.stacks()) {
                if (info.compact() && stack.isEmpty()) break;
                int column = x + (count % 9) * GRID_WIDTH + MARGIN;
                int row = y + count / 9 * GRID_HEIGHT + MARGIN;
                drawStack(context, stack, column, row);
                count++;
            }

            if (count == 0 && info.compact()) {
                context.drawItem(info.shulker(), x + MARGIN, currentY + MARGIN);
            }
            if (clicked.lengthSquared() != 0 && isHovered(clicked.x, clicked.y, width, rows)) {
                int id = mc.player.currentScreenHandler.syncId;
                mc.interactionManager.clickSlot(id, info.slot(), 0, SlotActionType.PICKUP, mc.player);
                clicked.set(0);
            }
            int background = ShulkerViewEntrypoint.getInstance().getConfig().getBackground();
            context.fill(x, y, x + width, y + rows, background);
            context.fill(x, y - 1, x + width, y, info.color());
        }
        currentY += rows + MARGIN;
    }

    public void mouseClick(double x, double y, int button) {
        if (button != 0) return;
        clicked.set(x, y);
    }

    public void mouseScroll(double x, double y, double amount) {
        if (amount == 0) return;
        this.offset = MathHelper.clamp((int) (offset + Math.ceil(amount) * 10), -height + mc.getWindow().getScaledHeight(), 0);
    }

    private boolean isHovered(double x, double y, float cols, float rows) {
        return x >= startX && x <= startX + cols && y >= currentY && y <= currentY + rows;
    }

    private void drawStack(DrawContext ctx, ItemStack stack, int x, int y) {
        ctx.drawItem(stack, x, y);
        if (stack.getCount() > 999) {
            String text = "%.1fk".formatted(stack.getCount() / 1000f);
            ctx.drawStackOverlay(mc.textRenderer, stack, x, y, text);
        } else {
            ctx.drawStackOverlay(mc.textRenderer, stack, x, y);
        }
    }

    private void updateShulkerInfo(ShulkerInfo info) {
        int size = info.stacks().size();

        if (info.compact()) {
            size = 0;
            for (ItemStack s : info.stacks()) {
                if (s.isEmpty()) break;
                size++;
            }
            size = Math.max(1, size);
        }

        rows = (int) Math.ceil(size / 9f);
        cols = MathHelper.clamp(size, 1, 9);
    }

    private boolean usesBoth() {
        return ShulkerViewEntrypoint.getInstance().getConfig().isBothSides();
    }
}