package com.unrealdinnerbone.juqm.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class JAQMEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT> {

    public JAQMEnergyStorage(int capacity, int maxReceive) {
        super(capacity, maxReceive, 0);
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int removeEnergy(int amount) {
        if(amount > this.energy) {
            this.energy = 0;
            return amount - this.energy;
        }else {
            this.energy -= amount;
            return 0;
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("energy", getEnergyStored());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setEnergy(nbt.getInt("energy"));
    }
}
