package dev.cattyn.shulkerview.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.math.Color;

@Config(name = "shulker-view")
public class ShulkerViewConfig implements ConfigData {
    @ConfigEntry.ColorPicker(allowAlpha = true)
    private int background = Color.ofRGBA(0, 0, 0, 75).hashCode();
    private boolean compact = true;
    private boolean bothSides = true;

    public boolean isCompact() {
        return compact;
    }

    public boolean isBothSides() {
        return bothSides;
    }

    public int getBackground() {
        return background;
    }
}
