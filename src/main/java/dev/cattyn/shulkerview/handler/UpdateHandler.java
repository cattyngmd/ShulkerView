package dev.cattyn.shulkerview.handler;

import dev.cattyn.shulkerview.Globals;
import dev.cattyn.shulkerview.utils.ShulkerInfo;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;

public class UpdateHandler implements Globals {
    private final ObjectList<ShulkerInfo> shulkerList = ObjectLists.synchronize(new ObjectArrayList<>());

    public void tick() {
        if (!(mc.currentScreen instanceof HandledScreen<?>)) return;
        HandledScreen<?> screen = (HandledScreen<?>) mc.currentScreen;
        shulkerList.clear();
        for (Slot slot : screen.getScreenHandler().slots) {
            ShulkerInfo shulkerInfo = ShulkerInfo.create(slot.getStack(), slot.getIndex());
            if (shulkerInfo == null) continue;
            shulkerList.add(shulkerInfo);
        }
    }

    public ObjectList<ShulkerInfo> getShulkerList() {
        return shulkerList;
    }
}
