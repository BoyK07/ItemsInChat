package com.boyk07.itemsinchat;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemDisplayHandler {
    public static MutableText handleItemDisplay(String chatText, ServerPlayerEntity player) {
        ItemStack heldItem = player.getStackInHand(Hand.MAIN_HAND);

        if (!heldItem.isEmpty()) {
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(heldItem));

            MutableText itemText;
            if (heldItem.getCount() > 1) {
                itemText = Text.literal("§d" + heldItem.getName().getString() + " x" + heldItem.getCount() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));
            } else {
                itemText = Text.literal("§d" + heldItem.getName().getString() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));
            }

            MutableText finalMessage = Text.literal("");
            int start = 0;
            int index;

            Pattern pattern = Pattern.compile("\\[item\\]|\\[i\\]");
            Matcher matcher = pattern.matcher(chatText);
            while (matcher.find()) {
                finalMessage = finalMessage.append(chatText.substring(start, matcher.start()));
                finalMessage = finalMessage.append(itemText);
                start = matcher.end();
            }

            finalMessage = finalMessage.append(chatText.substring(start));
            return finalMessage;
        }

        return Text.literal(chatText);
    }

    public static MutableText handleArmorDisplay(String chatText, ServerPlayerEntity player) {
        ItemStack[] armorItems = player.getInventory().armor.toArray(new ItemStack[0]);

        MutableText armorText = Text.literal("");
        boolean hasArmor = false;

        for (int i = armorItems.length - 1; i >= 0; i--) {
            ItemStack armorPiece = armorItems[i];
            if (!armorPiece.isEmpty()) {
                hasArmor = true;
                Text armorName = armorPiece.getName();
                HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(armorPiece));

                MutableText armorDisplay = Text.literal("§d" + armorName.getString() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));

                if (i != 0) {
                    armorText = armorText.append(armorDisplay).append(", ");
                } else {
                    armorText = armorText.append(armorDisplay).append(" ");
                }
            }
        }

        if (hasArmor) {
            String[] splitText = chatText.split("\\[armor\\]|\\[a\\]");
            MutableText finalMessage = Text.literal(splitText[0]);

            for (int i = 1; i < splitText.length; i++) {
                finalMessage = finalMessage.append(armorText).append(splitText[i]);
            }

            return finalMessage;
        }

        return Text.literal(chatText);
    }

    public static MutableText handleOffhandDisplay(String chatText, ServerPlayerEntity player) {
        ItemStack heldItem = player.getStackInHand(Hand.OFF_HAND);

        if (!heldItem.isEmpty()) {
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(heldItem));

            MutableText itemText;
            if (heldItem.getCount() > 1) {
                itemText = Text.literal("§d" + heldItem.getName().getString() + " x" + heldItem.getCount() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));
            } else {
                itemText = Text.literal("§d" + heldItem.getName().getString() + "§f")
                        .styled(style -> style.withHoverEvent(hoverEvent));
            }

            String[] splitText = chatText.split("\\[offhand\\]|\\[o\\]");
            MutableText finalMessage = Text.literal(splitText[0]);

            for (int i = 1; i < splitText.length; i++) {
                finalMessage = finalMessage.append(itemText).append(splitText[i]);
            }

            return finalMessage;
        }

        return Text.literal(chatText);
    }

    public static MutableText handleEnderchestDisplay(String chatText, ServerPlayerEntity player) {
        String clickId = CommandHandler.generateEnderchestClickId(player);

        MutableText enderChestText = Text.literal("§d[Enderchest]§f").styled(style ->
                style.withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to open " + player.getDisplayName().getString() + "'s Enderchest"))
                ).withClickEvent(
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/openenderchest " + clickId)
                )
        );

        String[] splitText = chatText.split("\\[enderchest\\]|\\[ec\\]");
        MutableText finalMessage = Text.literal(splitText[0]);

        for (int i = 1; i < splitText.length; i++) {
            finalMessage = finalMessage.append(enderChestText).append(splitText[i]);
        }

        return finalMessage;
    }

    public static MutableText handleInventoryDisplay(String chatText, ServerPlayerEntity player) {
        String clickId = CommandHandler.generateInventoryClickId(player);

        MutableText inventoryText = Text.literal("§d[Inventory]§f").styled(style ->
                style.withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to open " + player.getDisplayName().getString() + "'s inventory"))
                ).withClickEvent(
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/openinventory " + clickId)
                )
        );

        String[] splitText = chatText.split("\\[inventory\\]|\\[inv\\]");
        MutableText finalMessage = Text.literal(splitText[0]);

        for (int i = 1; i < splitText.length; i++) {
            finalMessage = finalMessage.append(inventoryText).append(splitText[i]);
        }

        return finalMessage;
    }
}
