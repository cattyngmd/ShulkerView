package dev.cattyn.shulkerview.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)
public interface DuckHandledScreen {
    @Accessor("focusedSlot")
    Slot shulker_view$getFocused();
}
