package com.unrealdinnerbone.juqm.blocks.upgrades.block;

import com.unrealdinnerbone.juqm.JAQM;
import com.unrealdinnerbone.juqm.JAQMBlocks;
import com.unrealdinnerbone.juqm.JAQMItems;
import com.unrealdinnerbone.juqm.api.IStoneSuppler;
import com.unrealdinnerbone.juqm.blocks.upgrades.BaseFacingUpgradeBlock;
import com.unrealdinnerbone.juqm.events.DataGenerator;
import com.unrealdinnerbone.juqm.util.IRecipeCreater;
import com.unrealdinnerbone.juqm.util.config.JAQMConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class BlockInputUpgradeBlock extends BaseFacingUpgradeBlock implements IStoneSuppler, IRecipeCreater {

    public BlockInputUpgradeBlock() {
        super(Properties.create(Material.ROCK));
    }

    @Override
    public boolean supplyStone(World world, Direction direction, BlockPos blockPos) {
        BlockPos blockPos1 = getFacingBlockPos(blockPos, world.getBlockState(blockPos));
        BlockState blockState = world.getBlockState(getFacingBlockPos(blockPos, world.getBlockState(blockPos)));
        if(blockState.getBlock().isIn(Tags.Blocks.STONE)) {
            world.setBlockState(blockPos1, Blocks.AIR.getDefaultState());
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean hasStone(World world, Direction direction, BlockPos blockPos) {
        return world.getBlockState(getFacingBlockPos(blockPos, world.getBlockState(blockPos))).getBlock().isIn(Tags.Blocks.STONE);
    }

    @Override
    public TransferType getType() {
        return TransferType.IN;
    }

    @Override
    public JAQMConfig.UpgradeConfig getConfig() {
        return JAQM.BLOCK_UPGRADE;
    }

    @Override
    public String getName() {
        return "block";
    }

    @Override
    public void create(Item item, DataGenerator.RecipeProvider recipeProvider, Consumer<IFinishedRecipe> iFinishedRecipeConsumer) {
        ShapedRecipeBuilder.shapedRecipe(item)
                .patternLine("WTW")
                .patternLine("WCW")
                .patternLine("WHW")
                .key('W', Tags.Items.STONE)
                .key('H', Items.HOPPER)
                .key('C', JAQMItems.QUARRY_CORE.getItem())
                .key('T', Tags.Items.CHESTS)
                .addCriterion("has_stone", recipeProvider.hasItem(Tags.Items.STONE))
                .addCriterion("has_hopper", recipeProvider.hasItem(Items.HOPPER))
                .addCriterion("has_quarry_core", recipeProvider.hasItem(JAQMItems.QUARRY_CORE.getItem()))
                .addCriterion("has_chest", recipeProvider.hasItem(Tags.Items.CHESTS))
                .build(iFinishedRecipeConsumer);
        ShapelessRecipeBuilder.shapelessRecipe(item)
                .addIngredient(JAQMBlocks.BLOCK_OUTPUT_UPGRADE.getBlockItem())
                .addCriterion("has_input", recipeProvider.hasItem(JAQMBlocks.BLOCK_OUTPUT_UPGRADE.getBlockItem()))
                .build(iFinishedRecipeConsumer, item.getRegistryName().toString() + "_convert");
    }
}
