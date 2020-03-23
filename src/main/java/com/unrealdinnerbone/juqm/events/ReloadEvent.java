package com.unrealdinnerbone.juqm.events;

import com.unrealdinnerbone.juqm.JAQM;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.TagsUpdatedEvent;

public class ReloadEvent
{
    public static void onReload(TagsUpdatedEvent event) {
        JAQM.ORE_DISTRIBUTIONS.calculateChance();
    }
}
