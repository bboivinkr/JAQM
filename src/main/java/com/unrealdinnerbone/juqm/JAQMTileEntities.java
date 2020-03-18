package com.unrealdinnerbone.juqm;

import com.unrealdinnerbone.juqm.tileentity.QuaryTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public enum JAQMTileEntities
{
    QUARY(QuaryTileEntity::new);

    private final TileEntityType<? extends TileEntity> tileEntityType;
    private final ResourceLocation resourceLocation;


    public static final JAQMTileEntities[] ALL = values();

    JAQMTileEntities(Supplier<? extends TileEntity> suppler) {
        this.resourceLocation = new ResourceLocation(JAQM.MOD_ID, name().toLowerCase());
        this.tileEntityType = TileEntityType.Builder.create(suppler, JAQMBlocks.QUARRY.getBlock()).build(null).setRegistryName(resourceLocation);
    }

    public TileEntityType<? extends TileEntity> getTileEntityType() {
        return tileEntityType;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }
}
