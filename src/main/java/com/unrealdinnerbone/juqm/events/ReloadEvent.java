package com.unrealdinnerbone.juqm.events;

import com.unrealdinnerbone.juqm.JAQM;
import net.minecraftforge.client.event.RecipesUpdatedEvent;

public class ReloadEvent
{
    public static void onReload(RecipesUpdatedEvent event) {
        JAQM.ORE_DISTRIBUTIONS.calculateChance();
    }
}
