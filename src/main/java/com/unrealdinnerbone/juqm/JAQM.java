package com.unrealdinnerbone.juqm;

import com.unrealdinnerbone.juqm.events.DataGenerator;
import com.unrealdinnerbone.juqm.events.RegisteryEvents;
import com.unrealdinnerbone.juqm.events.ReloadEvent;
import com.unrealdinnerbone.juqm.util.OreDistributions;
import com.unrealdinnerbone.juqm.util.config.JAQMConfig;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(JAQM.MOD_ID)
@Mod.EventBusSubscriber
public class JAQM
{

    public static final String MOD_ID = "jaqm";

    private static final Logger LOGGER = LogManager.getLogger();
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final JAQMConfig.BlockConfig QUARRY = JAQMConfig.createBlockConfig(BUILDER, "quarry", 1000000, 1000000, 10000);
    public static final JAQMConfig.UpgradeConfig CHEST_UPGRADE = JAQMConfig.createUpgradeConfig(BUILDER, "chest",  10000);
    public static final JAQMConfig.UpgradeConfig BLOCK_UPGRADE = JAQMConfig.createUpgradeConfig(BUILDER, "block",  2500);

    public static final OreDistributions ORE_DISTRIBUTIONS = new OreDistributions();
    public static final ItemGroup ITEM_GROUP = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(JAQMBlocks.QUARRY.getBlock());
        }
    };

    public JAQM() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataGenerator::onDataGen);
        RegisteryEvents.init();
        MinecraftForge.EVENT_BUS.addListener(ReloadEvent::onReload);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, BUILDER.build());
    }

}
