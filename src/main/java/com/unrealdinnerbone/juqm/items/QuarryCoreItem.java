package com.unrealdinnerbone.juqm.items;

import com.unrealdinnerbone.juqm.JAQM;
import com.unrealdinnerbone.juqm.JAQMItems;
import com.unrealdinnerbone.juqm.events.DataGenerator;
import com.unrealdinnerbone.juqm.util.IItemModelProvider;
import com.unrealdinnerbone.juqm.util.IRecipeCreater;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Consumer;

public class QuarryCoreItem extends Item implements IRecipeCreater, IItemModelProvider {

    public QuarryCoreItem() {
        super(new Properties().group(JAQM.ITEM_GROUP));
    }


    @Override
    public void create(Item item, DataGenerator.RecipeProvider recipeProvider, Consumer<IFinishedRecipe> iFinishedRecipeConsumer) {
        ShapedRecipeBuilder.shapedRecipe(item)
                .patternLine("OSO")
                .patternLine("OEO")
                .patternLine("OOO")
                .key('O', Tags.Items.OBSIDIAN)
                .key('S', JAQMItems.BIOME_MARKER.getItem())
                .key('E', Tags.Items.NETHER_STARS)
                .addCriterion("has_obsidian", recipeProvider.hasItem(Tags.Items.OBSIDIAN))
                .addCriterion("has_nether_star", recipeProvider.hasItem(JAQMItems.BIOME_MARKER.getItem()))
                .addCriterion("has_eye_ender", recipeProvider.hasItem(Tags.Items.NETHER_STARS))
                .setGroup(JAQM.MOD_ID).build(iFinishedRecipeConsumer);
    }

    @Override
    public void registerModel(Item item, DataGenerator.ItemModelProvider itemModelProvider) {
        itemModelProvider.itemGenerated(item, new ResourceLocation(JAQM.MOD_ID, "block/quarry/front"));
    }
}
