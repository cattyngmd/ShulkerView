package dev.cattyn.shulkerview.mixin;

import dev.cattyn.shulkerview.ShulkerViewEntrypoint;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public class MixinHandledScreen extends Screen {
    protected MixinHandledScreen(Text title) {
        super(title);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        ShulkerViewEntrypoint.getInstance().getUpdateHandler().tick();

    }

    @Inject(method = "drawMouseoverTooltip", at = @At("HEAD"))
    private void renderHook(DrawContext context, int x, int y, CallbackInfo ci) {
        ShulkerViewEntrypoint.getInstance().getRenderHandler().render(context, x, y);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void mouseClickedHook(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        ShulkerViewEntrypoint.getInstance().getRenderHandler().mouseClick(mouseX, mouseY, button);
    }

    @Override public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        ShulkerViewEntrypoint.getInstance().getRenderHandler().mouseScroll(mouseX, mouseY, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
