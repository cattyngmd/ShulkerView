package dev.cattyn.shulkerview.mixin;

import dev.cattyn.shulkerview.ShulkerViewEntrypoint;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeInventoryScreen.class)
public class MixinCreativeInventoryScreen {

    @Inject(method = "mouseScrolled", at = @At("HEAD"))
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount, CallbackInfoReturnable<Boolean> cir) {
        ShulkerViewEntrypoint.getInstance().getRenderHandler().mouseScroll(mouseX, mouseY, verticalAmount);
    }

}
