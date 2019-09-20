package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class ModBlocks {
    public static Block blueStone;
    public static Block myStone;

    public static void registerAll(RegistryEvent.Register<Block> event){
        if(!event.getName().equals(ForgeRegistries.BLOCKS.getRegistryName())) return;

        blueStone = register("blue_stone",new Block(Block.Properties.create(Material.ROCK)
                .hardnessAndResistance(1.5f, 6f).sound(SoundType.STONE)
                ));
        blueStone = register("my_stone",new Block(Block.Properties.create(Material.ROCK)
                .hardnessAndResistance(6f, 20f).sound(SoundType.STONE)
        ));
    }

    private static <T extends Block> T register(String name, T block){
        ItemBlock itemBlock = new ItemBlock(block, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS));
        return register(name, block , itemBlock);
    }

    private static <T extends Block> T register(String name, T block, @Nullable ItemBlock item){
        ResourceLocation id = ExampleMod.getId(name);
        block.setRegistryName(id);
        ForgeRegistries.BLOCKS.register(block);
        if(item != null){
            ModItems.BLOCKS_TO_REGISTER.put(name, item);
        }
        return block;
    }
}
