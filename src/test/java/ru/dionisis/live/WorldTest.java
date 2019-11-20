package ru.dionisis.live;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

    private static final int QUANTITY_STEPS = 1000;
    private static final File CONFIG_FILE = new File("target/classes/config.txt");

    @Test
    void test1() {

        World world1 = new World(CONFIG_FILE);
        world1.executeGeneration(5);

        World world2 = new World(CONFIG_FILE);
        world2.executeGeneration(5, 3);

        assertEquals(world1.toString(), world2.toString());
    }

    @Test
    void executeGeneration1thread() {
        World world = new World(CONFIG_FILE);
        world.executeGeneration(QUANTITY_STEPS);
    }

    @Test
    void executeGeneration2thread() {
        World world = new World(CONFIG_FILE);
        world.executeGeneration(QUANTITY_STEPS, 2);
    }

    @Test
    void executeGeneration3thread() {
        World world = new World(CONFIG_FILE);
        world.executeGeneration(QUANTITY_STEPS, 3);
    }

    @Test
    void executeGeneration4thread() {
        World world = new World(CONFIG_FILE);
        world.executeGeneration(QUANTITY_STEPS, 4);
    }

}