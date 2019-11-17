package ru.dionisis.live.impementations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

    private static final int STEPS_QUANTITY = 100;

    @Test
    void test1() {

        World world1 = new World(Config.CONFIG_FILE);
        world1.executeGeneration(5);

        World world2 = new World((Config.CONFIG_FILE));
        world2.executeGeneration(5, 2);

        assertEquals(world1.toString(), world2.toString());
    }

    @Test
    void executeGeneration1thread() {
        World world = new World(Config.CONFIG_FILE);
        world.executeGeneration(STEPS_QUANTITY);
    }

    @Test
    void executeGeneration2thread() {
        World world = new World((Config.CONFIG_FILE));
        world.executeGeneration(STEPS_QUANTITY, 2);
    }

    @Test
    void executeGeneration3thread() {
        World world = new World((Config.CONFIG_FILE));
        world.executeGeneration(STEPS_QUANTITY, 3);
    }

    @Test
    void executeGeneration4thread() {
        World world = new World((Config.CONFIG_FILE));
        world.executeGeneration(STEPS_QUANTITY, 4);
    }

}