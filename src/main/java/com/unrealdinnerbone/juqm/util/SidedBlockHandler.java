package com.unrealdinnerbone.juqm.util;

import net.minecraft.util.Direction;

public class SidedBlockHandler<T> {

    private final T t;
    private final Direction direction;

    public SidedBlockHandler(T t, Direction direction) {
        this.t = t;
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public T get() {
        return t;
    }
}
