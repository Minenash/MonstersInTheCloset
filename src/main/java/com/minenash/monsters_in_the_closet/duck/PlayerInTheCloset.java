package com.minenash.monsters_in_the_closet.duck;


import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;


public interface PlayerInTheCloset {
    @Nullable
    BlockPos getLastSleepPos();
    
    void setLastSleepPos(@Nullable BlockPos lastSleepPos);
}
