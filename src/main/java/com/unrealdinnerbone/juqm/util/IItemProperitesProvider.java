package com.unrealdinnerbone.juqm.util;

import com.unrealdinnerbone.juqm.JAQM;
import com.unrealdinnerbone.juqm.events.DataGenerator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public interface IItemProperitesProvider extends IItemModelProvider
{
    default Item.Properties getItemProperties() {
        return new Item.Properties().group(JAQM.ITEM_GROUP);
    }

    default void registerStatesAndModels(Block block, DataGenerator.BlockModelProvider blockModelProvider) {

    }

}
