package com.unrealdinnerbone.juqm.items;

import com.unrealdinnerbone.juqm.JAQM;
import com.unrealdinnerbone.juqm.events.DataGenerator;
import com.unrealdinnerbone.juqm.util.IItemModelProvider;
import com.unrealdinnerbone.juqm.util.IRecipeCreater;
import com.unrealdinnerbone.juqm.util.OreDistribution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
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
import net.minecraft.util.registry.Registry;
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

public class BiomeMakerItem extends Item implements IRecipeCreater, IItemModelProvider {

    private static final String BASE_KEY = "data";
    private static final String BIOME_KEY = "biome";

    public BiomeMakerItem() {
        super(new Item.Properties().group(JAQM.ITEM_GROUP));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(!worldIn.isRemote) {
            ItemStack heldItem = playerIn.getHeldItem(handIn);
            Biome currentBiome = worldIn.getBiome(playerIn.getPosition());
            Biome oldBiome = getBiome(heldItem);
            if(oldBiome != currentBiome) {
                setBiome(heldItem, currentBiome);
                playerIn.sendMessage(new TranslationTextComponent("message.setBiomeTo", currentBiome.getDisplayName()));
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if(group == JAQM.ITEM_GROUP) {
            ForgeRegistries.BIOMES.forEach(biome -> {
                ItemStack itemStack = new ItemStack(this);
                setBiome(itemStack, biome);
                items.add(itemStack);
            });
        }
        super.fillItemGroup(group, items);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Biome biome = getBiome(stack);
        tooltip.add(new TranslationTextComponent("tooltip.biome_marker.biome", biome.getDisplayName()));
        if(InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), Minecraft.getInstance().gameSettings.keyBindSneak.getKey().getKeyCode())) {
            JAQM.ORE_DISTRIBUTIONS.getOreChanceChache().get(biome).forEach((d, l) -> tooltip.addAll(l));
//            JAQM.ORE_DISTRIBUTIONS.getBiomeOresMap().get(biome).getMap().forEach((key, value) -> {
//                key.getBlockState().getBlock().getRegistryName().toString();
//                double count = value;
//                double total = JAQM.ORE_DISTRIBUTIONS.getBiomeOresMap().get(biome).getTotal();
//                double chance = count / total;
//                DecimalFormat df = new DecimalFormat();
//                tooltip.add(new StringTextComponent(key.getBlockState().getBlock().getNameTextComponent().getFormattedText() + " @ " + df.format(chance * 100) + "%"));
//            });
        }else {
            tooltip.add(new TranslationTextComponent("tooltip.biome_marker.usage"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }


    public static Biome getBiome(ItemStack stack) {
        CompoundNBT compoundNBT = stack.getOrCreateChildTag(BASE_KEY);
        Biome biome = null;
        if (compoundNBT.contains(BIOME_KEY)) {
            biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(compoundNBT.getString(BIOME_KEY)));
        }
        if(biome == null) {
            biome = Biomes.DEFAULT;
        }
        return biome;
    }

    public static void setBiome(ItemStack itemStack, Biome biome) {
        itemStack.getOrCreateChildTag(BASE_KEY).putString(BIOME_KEY, biome.getRegistryName().toString());
    }

    @Override
    public void create(Item item, DataGenerator.RecipeProvider recipeProvider, Consumer<IFinishedRecipe> iFinishedRecipeConsumer) {
        ShapedRecipeBuilder.shapedRecipe(item)
                .patternLine("GGG")
                .patternLine("GEG")
                .patternLine("GGG")
                .key('G', Tags.Items.GLASS_PANES)
                .key('E', Items.ENDER_EYE)
                .addCriterion("has_glass", recipeProvider.hasItem(Tags.Items.GLASS_PANES))
                .addCriterion("has_eye", recipeProvider.hasItem(Items.ENDER_EYE))
                .build(iFinishedRecipeConsumer);
    }

    @Override
    public void registerModel(Item item, DataGenerator.ItemModelProvider itemModelProvider) {
        itemModelProvider.itemGenerated(item, new ResourceLocation(JAQM.MOD_ID, "item/biome_finder"));
    }


}
