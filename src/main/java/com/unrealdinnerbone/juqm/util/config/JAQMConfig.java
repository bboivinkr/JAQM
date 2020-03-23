package com.unrealdinnerbone.juqm.util.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class JAQMConfig
{

    public static BlockConfig createBlockConfig(ForgeConfigSpec.Builder builder, String name, int maxStorage, int maxReceive, int operation) {
        return new BlockConfig(builder, name, maxStorage, maxReceive, operation);
    }

    public static UpgradeConfig createUpgradeConfig(ForgeConfigSpec.Builder builder, String name, int operation) {
        return new UpgradeConfig(builder, name, operation);
    }

    public static class BlockConfig {
        private final ForgeConfigSpec.IntValue maxStorage;
        private final ForgeConfigSpec.IntValue maxReceive;
        private final ForgeConfigSpec.IntValue usagePerOperation;


        private BlockConfig(ForgeConfigSpec.Builder builder, String name, int maxStorage, int usage, int operation) {
            builder.push("blocks");
            builder.push(name);
            this.maxStorage = builder.comment("The amount of FE the " + name + " receive per tick").defineInRange("maxReceive", maxStorage, 1, Integer.MAX_VALUE);
            this.maxReceive = builder.comment("The amount of FE the " + name + " receive per tick").defineInRange("maxReceive", usage, 1, Integer.MAX_VALUE);
            this.usagePerOperation = builder.comment("The amount of FE the " + name + " uses per operation").defineInRange("usagePerOperation", operation, 1, Integer.MAX_VALUE);
            builder.pop(2);
        }

        public int getMaxReceive() {
            return maxReceive.get();
        }

        public int getMaxStorage() {
            return maxStorage.get();
        }

        public int getUsagePerOperation() {
            return usagePerOperation.get();
        }
    }


    public static class UpgradeConfig {
        private final ForgeConfigSpec.IntValue usagePerOperation;


        private UpgradeConfig(ForgeConfigSpec.Builder builder, String name, int operation) {
            builder.push("blocks." + name);
            this.usagePerOperation = builder.comment("The amount of FE the " + name + " uses per operation").defineInRange("usagePerOperation", operation, 1, Integer.MAX_VALUE);
            builder.pop(2);
        }

        public int getUsagePerOperation() {
            return usagePerOperation.get();
        }
    }
}
