package com.unrealdinnerbone.juqm.util;

import com.unrealdinnerbone.juqm.JAQM;
import com.unrealdinnerbone.juqm.events.DataGenerator;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public interface IItemModelProvider
{

    default void registerModel(Item item, DataGenerator.ItemModelProvider itemModelProvider) {

    }

}
