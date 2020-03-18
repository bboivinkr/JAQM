package com.unrealdinnerbone.juqm.util;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class OreDistribution
{
    private final BlockState blockState;
    private final int amount;

    public OreDistribution(BlockState blockState, int amount) {

        this.blockState = blockState;
        this.amount = amount;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public int getAmount() {
        return amount;
    }
}
