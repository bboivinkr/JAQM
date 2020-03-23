package com.unrealdinnerbone.juqm.api;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IOreGiver extends IUpgrade {

    boolean giveOre(BlockState oreBlockState, World world, Direction direction, BlockPos blockPos);

    boolean hasAir(BlockState oreBlockState, World world, Direction direction, BlockPos blockPos);
}
