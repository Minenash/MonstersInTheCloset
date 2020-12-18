package net.fabricmc.example;

import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MonstersInTheCloset {

    // Server Side
    public static World world = null;

    // Client Side
    public static int duration = -1;
    public static List<HostileEntity> list = new ArrayList<>();

}
