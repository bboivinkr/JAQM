package com.unrealdinnerbone.juqm.events;

import com.unrealdinnerbone.juqm.JAQM;
import com.unrealdinnerbone.juqm.JAQMBlocks;
import com.unrealdinnerbone.juqm.JAQMItems;
import com.unrealdinnerbone.juqm.util.IItemModelProvider;
import com.unrealdinnerbone.juqm.util.IItemProperitesProvider;
import com.unrealdinnerbone.juqm.util.IRecipeCreater;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.Arrays;
import java.util.function.Consumer;

public class DataGenerator
{

    @SubscribeEvent
    public static void onDataGen(GatherDataEvent event) {
        event.getGenerator().addProvider(new RecipeProvider(event.getGenerator()));
        event.getGenerator().addProvider(new BlockModelProvider(event.getGenerator(), JAQM.MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new ItemModelProvider(event.getGenerator(), JAQM.MOD_ID, event.getExistingFileHelper()));
    }

    public static class RecipeProvider extends net.minecraft.data.RecipeProvider {

        public RecipeProvider(net.minecraft.data.DataGenerator generatorIn) {
            super(generatorIn);
        }

        @Override
        protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
            Arrays.stream(JAQMBlocks.ALL).filter(tobrirsBlocks -> tobrirsBlocks.getBlockItem() != null).filter(tobrirsBlocks -> tobrirsBlocks.getBlock() instanceof IRecipeCreater).forEach(tobrirsBlocks -> ((IRecipeCreater) tobrirsBlocks.getBlock()).create(tobrirsBlocks.getBlockItem(), this, consumer));
            Arrays.stream(JAQMItems.ALL).filter(tobrirsBlocks -> tobrirsBlocks.getItem() != null).filter(tobrirsBlocks -> tobrirsBlocks.getItem() instanceof IRecipeCreater).forEach(tobrirsBlocks -> ((IRecipeCreater) tobrirsBlocks.getItem()).create(tobrirsBlocks.getItem(), this, consumer));
        }

        @Override
        public InventoryChangeTrigger.Instance hasItem(ItemPredicate... predicates) {
            return super.hasItem(predicates);
        }

        @Override
        public InventoryChangeTrigger.Instance hasItem(Tag<Item> tagIn) {
            return super.hasItem(tagIn);
        }


        @Override
        public InventoryChangeTrigger.Instance hasItem(IItemProvider itemIn) {
            return super.hasItem(itemIn);
        }
    }

    public static class BlockModelProvider extends BlockStateProvider {

        public BlockModelProvider(net.minecraft.data.DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
            super(gen, modid, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            Arrays.stream(JAQMBlocks.ALL).filter(blocks -> blocks.getBlockItem() != null).filter(blocks -> blocks.getBlock() instanceof IItemProperitesProvider).forEach(blocks -> ((IItemProperitesProvider) blocks.getBlock()).registerStatesAndModels(blocks.getBlock(), this));

        }
    }

    public static class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {

        public ItemModelProvider(net.minecraft.data.DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
            super(generator, modid, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            Arrays.stream(JAQMBlocks.ALL).filter(blocks -> blocks.getBlockItem() != null).filter(blocks -> blocks.getBlock() instanceof IItemProperitesProvider).forEach(blocks -> ((IItemProperitesProvider) blocks.getBlock()).registerModel(blocks.getBlockItem(), this));
            Arrays.stream(JAQMItems.ALL).filter(blocks -> blocks.getItem() != null).filter(blocks -> blocks.getItem() instanceof IItemModelProvider).forEach(blocks -> ((IItemModelProvider) blocks.getItem()).registerModel(blocks.getItem(), this));

        }

        public void itemGenerated(Item item, ResourceLocation texture) {
            getBuilder(item.getRegistryName().getPath()).parent(getExistingFile(mcLoc("item/generated")))
                    .texture("layer0", texture);
        }

        @Override
        public String getName() {
            return JAQM.MOD_ID;
        }
    }


}
