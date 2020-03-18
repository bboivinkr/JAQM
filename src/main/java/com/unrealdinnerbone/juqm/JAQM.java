package com.unrealdinnerbone.juqm;

import com.unrealdinnerbone.juqm.events.DataGenerator;
import com.unrealdinnerbone.juqm.events.RegisteryEvents;
import com.unrealdinnerbone.juqm.util.OreDistributions;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;

@Mod(JAQM.MOD_ID)
@Mod.EventBusSubscriber
public class JAQM
{

    public static final String MOD_ID = "jaqm";

    private static final Logger LOGGER = LogManager.getLogger();
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec.IntValue maxStorage;
    public static ForgeConfigSpec.IntValue maxReceive;
    public static ForgeConfigSpec.IntValue usagePerBlock;

    public static final OreDistributions ORE_DISTRIBUTIONS = new OreDistributions();
    public static final ItemGroup ITEM_GROUP = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(JAQMBlocks.QUARRY.getBlock());
        }
    };

    public JAQM() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataGenerator::onDataGen);
        RegisteryEvents.init();
        MinecraftForge.EVENT_BUS.register(this);
        BUILDER.push("general");
        maxStorage = BUILDER.comment("The amount of FE the quarry can hold").defineInRange("maxStorage", 1000000, 1, Integer.MAX_VALUE);
        maxReceive = BUILDER.comment("The amount of FE the quarry can receive per tick").defineInRange("maxReceive", 1000000, 1, Integer.MAX_VALUE);
        usagePerBlock = BUILDER.comment("The amount of FE to use per ore change").defineInRange("usagePerBlock", 10000, 1, Integer.MAX_VALUE);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BUILDER.build());
    }

    private void setup(final FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(ORE_DISTRIBUTIONS::calculateChance).whenComplete((aVoid, throwable) -> LOGGER.info("Loaded Ore Chances for " + ForgeRegistries.BIOMES.getValues().size() + "Biomes"));
    }
}
