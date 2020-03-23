package com.unrealdinnerbone.juqm.tileentity;

import com.unrealdinnerbone.juqm.JAQM;
import com.unrealdinnerbone.juqm.JAQMItems;
import com.unrealdinnerbone.juqm.JAQMTileEntities;
import com.unrealdinnerbone.juqm.api.IOreGiver;
import com.unrealdinnerbone.juqm.api.IStoneSuppler;
import com.unrealdinnerbone.juqm.blocks.QuarryBlock;
import com.unrealdinnerbone.juqm.energy.JAQMEnergyStorage;
import com.unrealdinnerbone.juqm.items.BiomeMakerItem;
import com.unrealdinnerbone.juqm.util.SidedBlockHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class QuaryTileEntity extends TileEntity implements ITickableTileEntity {

    private static final Direction[] DIRECTIONS = Direction.values();

    private final LazyOptional<JAQMEnergyStorage> energyStorage;
    private final List<SidedBlockHandler<IStoneSuppler>> inputHandles;
    private final List<SidedBlockHandler<IOreGiver>> outputHandles;
    private final AtomicReference<Biome> biome;
    private BlockState oreBlockState;

    public QuaryTileEntity() {
        super(JAQMTileEntities.QUARY.getTileEntityType());
        energyStorage = LazyOptional.of(() -> new JAQMEnergyStorage(JAQM.QUARRY));
        inputHandles = new ArrayList<>();
        outputHandles = new ArrayList<>();
        biome = new AtomicReference<>();
    }

    @Override
    public void tick() {
        if (!world.isRemote()) {
            energyStorage.ifPresent(jaqmEnergyStorage -> {
                for (SidedBlockHandler<IStoneSuppler> inputHandle : inputHandles) {

                    if (jaqmEnergyStorage.getEnergyStored() >= inputHandle.get().getConfig().getUsagePerOperation()) {
                        int needEnergy = inputHandle.get().getConfig().getUsagePerOperation();
                        if (inputHandle.get().hasStone(world, inputHandle.getDirection(), pos.offset(inputHandle.getDirection()))) {
                            for (SidedBlockHandler<IOreGiver> outputHandle : outputHandles) {
                                if (outputHandle.get().getConfig().getUsagePerOperation() + JAQM.QUARRY.getUsagePerOperation() + inputHandle.get().getConfig().getUsagePerOperation() <= jaqmEnergyStorage.getEnergyStored()) {
                                    needEnergy += outputHandle.get().getConfig().getUsagePerOperation() + inputHandle.get().getConfig().getUsagePerOperation();
                                    if (outputHandle.get().hasAir(oreBlockState, world, outputHandle.getDirection(), pos.offset(outputHandle.getDirection()))) {
                                        if (inputHandle.get().supplyStone(world, inputHandle.getDirection(), pos.offset(inputHandle.getDirection()))) {
                                            if (outputHandle.get().giveOre(oreBlockState, world, outputHandle.getDirection(), pos.offset(outputHandle.getDirection()))) {
                                                updateOreBlockState();
                                                jaqmEnergyStorage.removeEnergy(needEnergy + JAQM.QUARRY.getUsagePerOperation());
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                    }

                }
            });
        }
    }


    public void updateOreBlockState() {
        this.oreBlockState = getOre();
    }


    public BlockState getOre() {
        return JAQM.ORE_DISTRIBUTIONS.getOre(biome.get());
    }


    @Override
    public void onLoad() {
        super.onLoad();
        updateUpgradesCache();
        updateOreBlockState();
    }

    public void updateUpgradesCache() {
        outputHandles.clear();
        inputHandles.clear();
        for (Direction direction : DIRECTIONS) {
            BlockPos blockPos = pos.offset(direction);
            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if(block instanceof IOreGiver) {
                outputHandles.add(new SidedBlockHandler<>((IOreGiver) block, direction));
            }
            if(block instanceof IStoneSuppler) {
                inputHandles.add(new SidedBlockHandler<>((IStoneSuppler) block, direction));
            }
        }
        Direction direction = world.getBlockState(pos).get(QuarryBlock.FACING);
        Optional<ItemFrameEntity> itemFrameEntity = world.getEntitiesWithinAABB(ItemFrameEntity.class, new AxisAlignedBB(pos.offset(direction))).stream().findFirst();
        biome.set(world.getBiome(pos));
        itemFrameEntity.filter(itemFrameEntity1 -> itemFrameEntity1.getDisplayedItem().getItem() == JAQMItems.BIOME_MARKER.getItem()).ifPresent(itemFrameEntity1 -> biome.set(BiomeMakerItem.getBiome(itemFrameEntity1.getDisplayedItem().getStack())));
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
