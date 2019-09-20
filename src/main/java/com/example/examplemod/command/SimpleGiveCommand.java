package com.example.examplemod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.stream.Stream;


public final class SimpleGiveCommand {

    private static final SuggestionProvider<CommandSource> ITEM_ID_SUGGESTIONS = (context, builder) ->
            ISuggestionProvider.func_212476_a(ForgeRegistries.ITEMS.getKeys().stream(), builder);

    private SimpleGiveCommand() {}

static void register(CommandDispatcher<CommandSource> dispatcher) {
        // User types "/sgive"
        dispatcher.register(Commands.literal("sgive")
                // Needs permission level 2
                .requires(source -> source.hasPermissionLevel(2))
                // The target players (required argument)
                .then(Commands.argument("targets", EntityArgument.multiplePlayers())
                        // The item ID (required argument)
                        .then(Commands.argument("itemID", ResourceLocationArgument.resourceLocation())
                                // Make suggestions for the item IDs
                                .suggests(ITEM_ID_SUGGESTIONS)
                                // If no further arguments, give one of the item to all targets
                                .executes(context -> giveItem(
                                        context.getSource(),
                                        ResourceLocationArgument.getResourceLocation(context, "itemID"),
                                        EntityArgument.getPlayers(context, "targets"),
                                        1
                                ))
                                // Or we can optionally specify the number of the item to give
                                .then(Commands.argument("count", IntegerArgumentType.integer())
                                        // Which ends with giving the target players the given number of the item
                                        .executes(context -> giveItem(
                                                context.getSource(),
                                                ResourceLocationArgument.getResourceLocation(context, "itemID"),
                                                EntityArgument.getPlayers(context, "targets"),
                                                IntegerArgumentType.getInteger(context, "count")
                                        ))
                                )
                        )
                )
        );
    }

    private static int giveItem(CommandSource source, ResourceLocation itemId, Collection<EntityPlayerMP> targets, int count) {
        Item item = ForgeRegistries.ITEMS.getValue(itemId);
        if (item == null) {
            source.sendErrorMessage(new TextComponentString("Item '" + itemId + "' does not exist?"));
            return 0;
        }

        for (EntityPlayerMP player : targets) {
            int remainingCount = count;

            while (remainingCount > 0) {
                @SuppressWarnings("deprecation") int stackCount = Math.min(item.getMaxStackSize(), remainingCount);
                remainingCount -= stackCount;
                ItemStack stack = new ItemStack(item, stackCount);
                boolean addedToInventory = player.inventory.addItemStackToInventory(stack);
                if (addedToInventory && stack.isEmpty()) {
                    stack.setCount(1);
                    EntityItem entityItem = player.dropItem(stack, false);
                    if (entityItem != null) {
                        entityItem.makeFakeItem();
                    }

                    player.world.playSound(
                            null, player.posX, player.posY, player.posZ,
                            SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
                            0.2F,
                            ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    player.inventoryContainer.detectAndSendChanges();
                } else {
                    EntityItem entityItem = player.dropItem(stack, false);
                    if (entityItem != null) {
                        entityItem.setNoPickupDelay();
                        entityItem.setOwnerId(player.getUniqueID());
                    }
                }
            }
        }

        ITextComponent itemText = new ItemStack(item, count).getTextComponent();
        if (targets.size() == 1) {
            source.sendFeedback(new TextComponentTranslation("commands.give.success.single", count, itemText, targets.iterator().next().getDisplayName()), true);
        } else {
            source.sendFeedback(new TextComponentTranslation("commands.give.success.single", count, itemText, targets.size()), true);
        }

        return targets.size();
    }
}
