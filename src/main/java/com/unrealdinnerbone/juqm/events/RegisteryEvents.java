package com.unrealdinnerbone.juqm.events;

import com.unrealdinnerbone.juqm.JAQMBlocks;
import com.unrealdinnerbone.juqm.JAQMItems;
import com.unrealdinnerbone.juqm.JAQMTileEntities;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Arrays;

public class RegisteryEvents
{

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, RegisteryEvents::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, RegisteryEvents::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, RegisteryEvents::registerTiles);
    }

    private static void registerBlocks(RegistryEvent.Register<Block> event) {
        Arrays.stream(JAQMBlocks.ALL).forEach(value -> event.getRegistry().register(value.getBlock()));
    }

    private static void registerTiles(RegistryEvent.Register<TileEntityType<?>> event) {
        Arrays.stream(JAQMTileEntities.ALL).forEach(value -> event.getRegistry().register(value.getTileEntityType()));
    }

    private static void registerItems(RegistryEvent.Register<Item> event) {
        Arrays.stream(JAQMBlocks.ALL).filter(tobrirsBlocks -> tobrirsBlocks.getBlockItem() != null).forEach(value -> event.getRegistry().register(value.getBlockItem()));
        Arrays.stream(JAQMItems.ALL).filter(tobrirsBlocks -> tobrirsBlocks.getItem() != null).forEach(value -> event.getRegistry().register(value.getItem()));

    }
}
