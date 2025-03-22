package croissantnova.sanitydim.config.registry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Package-Private
enum DefaultConfig {

    PASSIVE_SANITY_ENTITIES(
            "minecraft:enderman;-0.025;4",
            "minecraft:villager;0.025;16"
    ),
    PASSIVE_BLOCKS(
            "minecraft:campfire[lit=true];0.1;4;false",
            "minecraft:soul_campfire[lit=true];-0.1;4;false"
    ),
    ITEMS(
            "minecraft:pufferfish;-5;0",
            "minecraft:poisonous_potato;-5;0",
            "minecraft:spider_eye;-5;0",
            "minecraft:rotten_flesh;-5;0",
            "minecraft:chorus_fruit;-3;0",
            "minecraft:ender_pearl;-1;0",
            "minecraft:honey_bottle;6;1",
            "minecraft:golden_carrot;7;1",
            "minecraft:golden_apple;8;1",
            "minecraft:enchanted_golden_apple;13;1"
    ),
    ITEM_CATEGORY_COOLDOWNS(
            "0;0",
            "1;800.0"
    ),
    STATUS_EFFECTS(
            "minecraft:blindness;-0.5;0",
            "minecraft:hunger;-0.25;0",
            "minecraft:hunger;-0.50;1",
            "minecraft:hunger;-0.75;2",
            "minecraft:hunger;-1.00;3",
            "legendarysurvivaloverhaul:thirst;-1.0;0"
    ),
    BROKEN_BLOCKS(
            "minecraft:infested_stone;-8;0;false;false",
            "minecraft:infested_cobblestone;-8;0;false;false",
            "minecraft:infested_stone_bricks;-8;0;false;false",
            "minecraft:infested_cracked_stone_bricks;-8;0;false;false",
            "minecraft:infested_mossy_stone_bricks;-8;0;false;false",
            "minecraft:infested_chiseled_stone_bricks;-8;0;false;false",
            "minecraft:infested_deepslate;-8;0;false;false"
    ),
    BROKEN_BLOCK_CATEGORY_COOLDOWNS(
            "0;0"
    );

    private final List<String> elements;

    DefaultConfig(String... elements) {
        this.elements = Collections.unmodifiableList(Arrays.asList(elements));
    }

    public List<String> getElements() {
        return elements;
    }
}

