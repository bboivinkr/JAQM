package com.unrealdinnerbone.juqm.tileentity;

import com.unrealdinnerbone.juqm.JAQM;
import com.unrealdinnerbone.juqm.JAQMItems;
import com.unrealdinnerbone.juqm.JAQMTileEntities;
import com.unrealdinnerbone.juqm.blocks.QuarryBlock;
import com.unrealdinnerbone.juqm.energy.JAQMEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class QuaryTileEntity extends TileEntity implements ITickableTileEntity {

    private final LazyOptional<JAQMEnergyStorage> energyStorage;

    public QuaryTileEntity() {
        super(JAQMTileEntities.QUARY.getTileEntityType());
        energyStorage = LazyOptional.of(() -> new JAQMEnergyStorage(JAQM.maxStorage.get(), JAQM.maxReceive.get()));
    }

    @Override
    public void tick() {
        if (!world.isRemote()) {
            energyStorage.ifPresent(jaqmEnergyStorage -> {
                int needed = JAQM.usagePerBlock.get();
                if (jaqmEnergyStorage.getEnergyStored() > needed) {
                    BlockPos aboveBlockPos = pos.up();
                    BlockState blockAbove = world.getBlockState(aboveBlockPos);
                    if(blockAbove.getBlock().isIn(Tags.Blocks.STONE)) {
                        Direction direction = world.getBlockState(pos).get(QuarryBlock.FACING);
                        List<ItemFrameEntity> itemFrameEntities = world.getEntitiesWithinAABB(ItemFrameEntity.class, new AxisAlignedBB(pos.offset(direction)));
                        Optional<ItemFrameEntity> itemFrameEntity = itemFrameEntities.stream().findFirst();
                        AtomicReference<Biome> biome = new AtomicReference<>(world.getBiome(pos));
                        itemFrameEntity.ifPresent(itemFrameEntity1 -> {
                            if(itemFrameEntity1.getDisplayedItem().getItem() == JAQMItems.BIOME_MARKER.getItem()) {
                                biome.set(ForgeRegistries.BIOMES.getValue(new ResourceLocation(itemFrameEntity1.getDisplayedItem().getOrCreateChildTag("data").getString("biome"))));
                            }
                        });
                        BlockState blockState = JAQM.ORE_DISTRIBUTIONS.getOre(biome.get());
                        if(blockState != null) {
                            world.setBlockState(aboveBlockPos, blockState);
                            jaqmEnergyStorage.removeEnergy(needed);
                            markDirty();
                        }
                    }
                }
            });
        }
    }

    private List<ItemStack> getDrops(BlockState blockState) {
        if (blockState != null) {
            if (!blockState.getBlock().hasTileEntity(blockState)) {
                return Block.getDrops(blockState, (ServerWorld) world, pos, null);
            }
        }
        return Collections.emptyList();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return energyStorage.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void read(CompoundNBT tag) {
        CompoundNBT energyTag = tag.getCompound("energy");
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(energyTag));
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        energyStorage.ifPresent(h -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            tag.put("energy", compound);
        });
        return super.write(tag);
    }

}
