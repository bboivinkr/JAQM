package com.unrealdinnerbone.juqm.util;

import com.unrealdinnerbone.juqm.JAQMTags;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.function.BiFunction;

public class OreDistributions
{
    private final HashMap<Biome, RandomCollection<OreDistribution>> biomeOresMap;
    private final HashMap<Feature<?>, BiFunction<DecoratedFeatureConfig, IFeatureConfig, OreDistribution>> handles;

    public OreDistributions() {
        biomeOresMap = new HashMap<>();
        handles = new HashMap<>();
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
                            if(!oreDistribution.getBlockState().getBlock().isIn(JAQMTags.BLACKLIST.getBlockTag())) {
                                biomeOresMap.get(biome).add(oreDistribution.getAmount(), oreDistribution);
                            }
                        }
                    }

                }
            }
        }
    }

    public BlockState getOre(Biome biome) {
        OreDistribution blockState = biomeOresMap.get(biome).next();
        if(blockState != null) {
            return blockState.getBlockState();
        }else {
            return null;
        }
    }

    public HashMap<Biome, RandomCollection<OreDistribution>> getBiomeOresMap() {
        return biomeOresMap;
    }
}
