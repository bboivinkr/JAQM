package com.unrealdinnerbone.juqm;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public enum JAQMTags
{
    BLACKLIST;

    private final Tag<Block> blockTag;
    private final Tag<Item> itemTag;
    private final ResourceLocation resourceLocation;

    public static final JAQMTags[] ALL = values();

    JAQMTags() {
        this.resourceLocation = new ResourceLocation(JAQM.MOD_ID, name().toLowerCase());
        blockTag = new BlockTags.Wrapper(resourceLocation);
        itemTag = new ItemTags.Wrapper(resourceLocation);
    }

    public Tag<Block> getBlockTag() {
        return blockTag;
    }

    public Tag<Item> getItemTag() {
        return itemTag;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

}
