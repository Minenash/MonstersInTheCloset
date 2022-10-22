package com.minenash.monsters_in_the_closet;


import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MonstersInTheCloset implements ModInitializer {
    private static final Logger logger = LoggerFactory.getLogger(MonstersInTheCloset.class);
    
    @Override
    public void onInitialize() {
        logger.info("Started Monsters In The Closet");
    }
}
