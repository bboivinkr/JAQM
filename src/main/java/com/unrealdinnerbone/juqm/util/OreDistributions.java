package com.unrealdinnerbone.juqm.util;

import com.unrealdinnerbone.juqm.JAQM;
import com.unrealdinnerbone.juqm.JAQMTags;
import net.minecraft.block.BlockState;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.BiFunction;

public class OreDistributions
{
    private final static Logger LOGGER = LogManager.getLogger();
    private final HashMap<Biome, RandomCollection<OreDistribution>> biomeOresMap;
    private final HashMap<Biome, Map<Double, List<ITextComponent>>> oreChanceChache;
    private final HashMap<Feature<?>, BiFunction<DecoratedFeatureConfig, IFeatureConfig, OreDistribution>> handles;

    public OreDistributions() {
        biomeOresMap = new HashMap<>();
        handles = new HashMap<>();
        oreChanceChache = new HashMap<>();
        handles.put(Feature.ORE, (decoratedFeatureConfig, iFeatureConfig) -> {
            if(decoratedFeatureConfig.decorator.config instanceof CountRangeConfig) {
                CountRangeConfig countRangeConfig = (CountRangeConfig) decoratedFeatureConfig.decorator.config;
                if (iFeatureConfig instanceof OreFeatureConfig) {
                    OreFeatureConfig oreFeatureConfig = (OreFeatureConfig) iFeatureConfig;
                    return new OreDistribution(oreFeatureConfig.state, oreFeatureConfig.size * countRangeConfig.count);
                }
            }
            return null;
        });
        handles.put(Feature.EMERALD_ORE, (decoratedFeatureConfig, iFeatureConfig) -> iFeatureConfig instanceof ReplaceBlockConfig ? new OreDistribution(((ReplaceBlockConfig) iFeatureConfig).state, 1) : null);
    }

    public void calculateChance() {
        biomeOresMap.clear();
        for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
            biomeOresMap.put(biome, new RandomCollection<>());
            for (ConfiguredFeature<?, ?> configuredFeature : biome.getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES)) {
                if (configuredFeature.config instanceof DecoratedFeatureConfig) {
                    DecoratedFeatureConfig decoratedFeatureConfig = (DecoratedFeatureConfig) configuredFeature.config;
                    if (handles.containsKey(decoratedFeatureConfig.feature.feature)) {
                        OreDistribution oreDistribution = handles.get(decoratedFeatureConfig.feature.feature).apply(decoratedFeatureConfig, decoratedFeatureConfig.feature.config);
                        if (oreDistribution != null) {
                            if (!oreDistribution.getBlockState().getBlock().isIn(JAQMTags.BLACKLIST.getBlockTag())) {
                                biomeOresMap.get(biome).add(oreDistribution.getAmount(), oreDistribution);
                            }
                        }
                    }

                }
            }
        }
        updateChanceChache();
        LOGGER.info("Loaded Ore Distributions for {} biomes", biomeOresMap.size());
    }

    public void updateChanceChache() {
        getBiomeOresMap().forEach((biome, oreDistributionRandomCollection) -> {
            Map<OreDistribution, Double> map =oreDistributionRandomCollection.getMap();
            Map<Double, List<ITextComponent>> doubleListMap = new HashMap<>();
            map.forEach((key, value) -> {
                double count = value;
                double total = oreDistributionRandomCollection.getTotal();
                double chance = count / total;
                if(!doubleListMap.containsKey(chance)) {
                    doubleListMap.put(chance, new ArrayList<>());
                }
                DecimalFormat df = new DecimalFormat();
                doubleListMap.get(chance).add(new StringTextComponent(key.getBlockState().getBlock().getNameTextComponent().getFormattedText() + " @ " + df.format(chance * 100) + "%"));
            });

            List<Double> sortedKeys= new ArrayList<>(doubleListMap.keySet());
            Collections.sort(sortedKeys);
            Map<Double, List<ITextComponent>> values = new HashMap<>();
            for (Double sortedKey : sortedKeys) {
                values.put(sortedKey, doubleListMap.get(sortedKey));
            }

            oreChanceChache.put(biome, values);
        });
    }

    public HashMap<Biome, Map<Double, List<ITextComponent>>> getOreChanceChache() {
        return oreChanceChache;
    }

    public BlockState getOre(Biome biome) {
        OreDistribution blockState = biomeOresMap.get(biome).next();
        return blockState != null ? blockState.getBlockState() : null;
    }

    public HashMap<Biome, RandomCollection<OreDistribution>> getBiomeOresMap() {
        return biomeOresMap;
    }
}
