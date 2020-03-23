package com.unrealdinnerbone.juqm.blocks.upgrades.chest;

import com.unrealdinnerbone.juqm.JAQM;
import com.unrealdinnerbone.juqm.JAQMBlocks;
import com.unrealdinnerbone.juqm.JAQMItems;
import com.unrealdinnerbone.juqm.api.IStoneSuppler;
import com.unrealdinnerbone.juqm.blocks.upgrades.BaseFacingUpgradeBlock;
import com.unrealdinnerbone.juqm.events.DataGenerator;
import com.unrealdinnerbone.juqm.util.IItemProperitesProvider;
import com.unrealdinnerbone.juqm.util.IRecipeCreater;
import com.unrealdinnerbone.juqm.util.config.JAQMConfig;
import net.minecraft.block.material.Material;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class ChestInputUpgradeBlock extends BaseFacingUpgradeBlock implements IItemProperitesProvider, IStoneSuppler, IRecipeCreater {

    public ChestInputUpgradeBlock() {
        super(Properties.create(Material.ROCK));
    }

    @Override
    public boolean supplyStone(World world, Direction direction, BlockPos blockPos) {
        BlockPos inventoryBlockPos = getFacingBlockPos(blockPos, world.getBlockState(blockPos));
        TileEntity tileEntity = world.getTileEntity(inventoryBlockPos);
        AtomicReference<Boolean> atomicReference = new AtomicReference<>(false);
        if(tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction).ifPresent(iItemHandler -> {
                for (int i = 0; i < iItemHandler.getSlots(); i++) {
                    ItemStack stack = iItemHandler.getStackInSlot(i);
                    if(stack.getItem().isIn(Tags.Items.STONE)) {
                        atomicReference.set(true);
                        stack.shrink(1);
                        break;
                    }
                }
            });
        }
        return atomicReference.get();
    }

    @Override
    public boolean hasStone(World world, Direction direction, BlockPos blockPos) {
        BlockPos inventoryBlockPos = getFacingBlockPos(blockPos, world.getBlockState(blockPos));
        TileEntity tileEntity = world.getTileEntity(inventoryBlockPos);
        AtomicReference<Boolean> atomicReference = new AtomicReference<>(false);
        if(tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction).ifPresent(iItemHandler -> {
                for (int i = 0; i < iItemHandler.getSlots(); i++) {
                    ItemStack stack = iItemHandler.getStackInSlot(i);
                    if(stack.getItem().isIn(Tags.Items.STONE)) {
                        atomicReference.set(true);
                        break;
                    }
                }
            });
        }
        return atomicReference.get();
    }

    @Override
    public JAQMConfig.UpgradeConfig getConfig() {
        return JAQM.CHEST_UPGRADE;
    }

    @Override
    public TransferType getType() {
        return TransferType.IN;
    }

    @Override
    public String getName() {
        return "chest";
    }

    @Override
    public void create(Item item, DataGenerator.RecipeProvider recipeProvider, Consumer<IFinishedRecipe> iFinishedRecipeConsumer) {
        ShapedRecipeBuilder.shapedRecipe(item)
                .patternLine("WHW")
                .patternLine("WCW")
                .patternLine("WTW")
                .key('W', ItemTags.PLANKS)
                .key('H', Items.HOPPER)
                .key('C', JAQMItems.QUARRY_CORE.getItem())
                .key('T', Tags.Items.CHESTS)
                .addCriterion("has_planks", recipeProvider.hasItem(ItemTags.PLANKS))
                .addCriterion("has_hopper", recipeProvider.hasItem(Items.HOPPER))
                .addCriterion("has_quarry_core", recipeProvider.hasItem(JAQMItems.QUARRY_CORE.getItem()))
                .addCriterion("has_chest", recipeProvider.hasItem(Tags.Items.CHESTS))
                .build(iFinishedRecipeConsumer);
        ShapelessRecipeBuilder.shapelessRecipe(item)
                .addIngredient(JAQMBlocks.CHEST_OUTPUT_UPGRADE.getBlockItem())
                .addCriterion("has_output", recipeProvider.hasItem(JAQMBlocks.CHEST_OUTPUT_UPGRADE.getBlockItem()))
                .build(iFinishedRecipeConsumer, item.getRegistryName().toString() + "_convert");
    }
}
