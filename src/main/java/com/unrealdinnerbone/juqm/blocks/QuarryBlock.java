package com.unrealdinnerbone.juqm.blocks;

import com.unrealdinnerbone.juqm.JAQM;
import com.unrealdinnerbone.juqm.JAQMItems;
import com.unrealdinnerbone.juqm.events.DataGenerator;
import com.unrealdinnerbone.juqm.tileentity.QuaryTileEntity;
import com.unrealdinnerbone.juqm.util.IItemProperitesProvider;
import com.unrealdinnerbone.juqm.util.IRecipeCreater;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class QuarryBlock extends Block implements IItemProperitesProvider, IRecipeCreater {

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public QuarryBlock() {
        super(Block.Properties.create(Material.ROCK));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));

    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new QuaryTileEntity();
    }

    @Override
    public void registerModel(Item block, DataGenerator.ItemModelProvider itemModelProvider) {
//        itemModelProvider.si
        itemModelProvider.orientable(block.getRegistryName().toString(), new ResourceLocation(JAQM.MOD_ID, "block/quarry/side"), new ResourceLocation(JAQM.MOD_ID, "block/quarry/front"), new ResourceLocation("minecraft", "block/obsidian"));
//        itemModelProvider.cubeAll(block.getRegistryName().getPath(), new ResourceLocation("minecraft", "block/soul_sand"));
    }


    @Override
    public void registerStatesAndModels(Block block, DataGenerator.BlockModelProvider blockModelProvider) {
//        blockModelProvider.simpleBlock(block);
        blockModelProvider.horizontalBlock(block, new ResourceLocation(JAQM.MOD_ID, "block/quarry/side"), new ResourceLocation(JAQM.MOD_ID, "block/quarry/front"), new ResourceLocation("minecraft", "block/obsidian"));
    }

    @Override
    public void create(Item item, DataGenerator.RecipeProvider recipeProvider, Consumer<IFinishedRecipe> iFinishedRecipeConsumer) {
        ShapedRecipeBuilder.shapedRecipe(item)
                .patternLine("EPE")
                .patternLine("ESE")
                .patternLine("OTO")
                .key('E', Tags.Items.END_STONES)
                .key('P', Items.DIAMOND_PICKAXE)
                .key('S', Tags.Items.NETHER_STARS)
                .key('O', Tags.Items.OBSIDIAN)
                .key('T', JAQMItems.BIOME_MARKER.getItem())
                .addCriterion("has_endstone", recipeProvider.hasItem(Tags.Items.END_STONES))
                .addCriterion("has_diamond_pickaxe", recipeProvider.hasItem(Items.DIAMOND_PICKAXE))
                .addCriterion("has_nether_star", recipeProvider.hasItem(Tags.Items.NETHER_STARS))
                .addCriterion("has_obsidain", recipeProvider.hasItem(Tags.Items.OBSIDIAN))
                .addCriterion("has_eye", recipeProvider.hasItem(JAQMItems.BIOME_MARKER.getItem()))
                .setGroup(JAQM.MOD_ID)
                .build(iFinishedRecipeConsumer);
    }
}
