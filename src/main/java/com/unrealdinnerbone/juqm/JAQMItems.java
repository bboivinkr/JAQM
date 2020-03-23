package com.unrealdinnerbone.juqm;

import com.unrealdinnerbone.juqm.items.QuarryCoreItem;
import com.unrealdinnerbone.juqm.items.BiomeMakerItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public enum JAQMItems
{
    BIOME_MARKER(BiomeMakerItem::new),
    QUARRY_CORE(QuarryCoreItem::new);

    private final Item item;
    private final ResourceLocation resourceLocation;

    public static final JAQMItems[] ALL = values();

    JAQMItems(Supplier<Item> itemSupplier) {
        this.resourceLocation = new ResourceLocation(JAQM.MOD_ID, name().toLowerCase());
        this.item = itemSupplier.get().setRegistryName(resourceLocation);
    }

    public Item getItem() {
        return item;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

}
