package com.example.examplemod;

import com.example.examplemod.init.ModBlocks;
import com.example.examplemod.init.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class SideProxy {

    SideProxy(){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::enqueueINC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SideProxy::processINC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModBlocks::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModItems::registerAll);

        MinecraftForge.EVENT_BUS.addListener(SideProxy::serverStarting);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        ExampleMod.LOGGER.debug("commonSetup for Example Mod");
    }

    private static void enqueueINC(final InterModEnqueueEvent event){

    }
    private static void processINC(final InterModEnqueueEvent event){

    }

    public static void serverStarting(FMLServerStartedEvent event){

    }

    public static class Client extends SideProxy{
        public Client(){
            FMLJavaModLoadingContext.get().getModEventBus().addListener(Client::clientSetup);
        }
        private static void clientSetup(FMLClientSetupEvent event){

        }
    }
    public static class Server extends SideProxy{
        public Server(){
            FMLJavaModLoadingContext.get().getModEventBus().addListener(Server::serverSetUp);
        }

        private static void serverSetUp(FMLDedicatedServerSetupEvent event) {
        }

    }
}
