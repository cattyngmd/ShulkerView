package dev.cattyn.shulkerview;

import dev.cattyn.shulkerview.config.ShulkerViewConfig;
import dev.cattyn.shulkerview.handler.RenderHandler;
import dev.cattyn.shulkerview.handler.UpdateHandler;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class ShulkerViewEntrypoint implements ModInitializer, ClientModInitializer {
    private static ShulkerViewEntrypoint INSTANCE;
    private RenderHandler renderHandler;
    private UpdateHandler updateHandler;
    private ShulkerViewConfig config;

    @Override public void onInitializeClient() {
        INSTANCE = this;
        this.config = AutoConfig.getConfigHolder(ShulkerViewConfig.class).getConfig();
        this.updateHandler = new UpdateHandler();
        this.renderHandler = new RenderHandler();
    }

    @Override public void onInitialize() {
        AutoConfig.register(ShulkerViewConfig.class, Toml4jConfigSerializer::new);
    }

    public RenderHandler getRenderHandler() {
        return renderHandler;
    }

    public UpdateHandler getUpdateHandler() {
        return updateHandler;
    }

    public ShulkerViewConfig getConfig() {
        return config;
    }

    public static ShulkerViewEntrypoint getInstance() {
        return INSTANCE;
    }
}
