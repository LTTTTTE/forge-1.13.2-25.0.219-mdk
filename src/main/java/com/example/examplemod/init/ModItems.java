package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModItems {
    static final Map<String, ItemBlock> BLOCKS_TO_REGISTER = new LinkedHashMap<>();

    public static void registerAll(RegistryEvent.Register<Item> event){
        if(!event.getName().equals(ForgeRegistries.BLOCKS.getRegistryName())) return;
        //Blocks
        BLOCKS_TO_REGISTER.forEach(ModItems::register);
        //Items
    }

    private static <T extends Item> T register(String name, T item){
        ResourceLocation id = ExampleMod.getId(name);
        item.setRegistryName(id);
        ForgeRegistries.ITEMS.register(item);
        return item;
    }
}
