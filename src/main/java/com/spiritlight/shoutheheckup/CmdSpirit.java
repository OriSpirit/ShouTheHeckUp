package com.spiritlight.shoutheheckup;


import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
// import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Arrays;

@MethodsReturnNonnullByDefault @ParametersAreNonnullByDefault
public class CmdSpirit extends CommandBase {
    @Override
    public String getName() {
        return "sthu";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "/sthu help";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender sender, String[] args) { // yeet CommandException
        if (args.length == 0 || args[0].equals("help")) {
            getHelp(sender);
        }
        else {
            switch(args[0]) {
                case "add":
                    if(args.length < 2) {
                        sender.sendMessage(new TextComponentString(STHU.prefix + " §cYou need to supply a player!"));
                    } else
                    add(args[1], sender);
                    break;
                case "remove":
                    if(args.length < 2) {
                        sender.sendMessage(new TextComponentString(STHU.prefix + " §cYou need to supply a player!"));
                    } else
                    remove(args[1], sender);
                    break;
                case "addtext":
                    if(args.length < 2) {
                        sender.sendMessage(new TextComponentString(STHU.prefix + " §cYou need to supply a text!"));
                    } else {
                        String[] sa = Arrays.copyOfRange(args, 1, args.length);
                        String s = String.join(" ", sa);
                        addtext(s, sender);
                    }
                    break;
                case "removetext":
                    if(args.length < 2) {
                        sender.sendMessage(new TextComponentString(STHU.prefix + " §cYou need to supply a text!"));
                    } else {
                        String[] sa = Arrays.copyOfRange(args, 1, args.length);
                        String s = String.join(" ", sa);
                        removetext(s, sender);
                    }
                    break;
                case "list":
                    sender.sendMessage(new TextComponentString(STHU.prefix + " Current ignored players: " + STHU.players)); // Add something
                    break;
                case "listtext":
                    sender.sendMessage(new TextComponentString(STHU.prefix + " Current banned words: " + STHU.bannedWords)); // Add something
                    break;
                case "sethidelevel":
                    if(args.length < 2) {
                        sender.sendMessage(new TextComponentString(STHU.prefix + " §cYou need to supply a hide level!"));
                    } else
                    setHidelevel(args[1], sender);
                    break;
                case "hidelevel":
                    sender.sendMessage(new TextComponentString(STHU.prefix + " all - Suppresses ALL shout message"));
                    sender.sendMessage(new TextComponentString(STHU.prefix + " high - Suppresses shout message"));
                    sender.sendMessage(new TextComponentString(STHU.prefix + " medium - Replaces shout message"));
                    sender.sendMessage(new TextComponentString(STHU.prefix + " low - Replaces shout message with sender name"));
                    break;
                case "toggle":
                    toggle(sender);
                    break;
                case "banchat":
                    STHU.filterChat = !STHU.filterChat;
                    sender.sendMessage(new TextComponentString(STHU.prefix + " OK, blocked words also filter chat: " + STHU.filterChat));
                    refreshConfig();
                    break;
                default:
                    getHelp(sender);
            }
        }
    }
    private void getHelp(ICommandSender sender) {
        sender.sendMessage(new TextComponentString(STHU.prefix + " --------------- Help Page ---------------"));
        sender.sendMessage(new TextComponentString(STHU.prefix + " /sthu add - Adds a player to ignore shout"));
        sender.sendMessage(new TextComponentString(STHU.prefix + " /sthu addtext - Ignores a line from shout"));
        sender.sendMessage(new TextComponentString(STHU.prefix + " /sthu remove - Removes a player from list"));
        sender.sendMessage(new TextComponentString(STHU.prefix + " /sthu removetext - Removes line from list"));
        sender.sendMessage(new TextComponentString(STHU.prefix + " /sthu list - Lists players ignored so far"));
        sender.sendMessage(new TextComponentString(STHU.prefix + " /sthu listtext - Lists banned shout lines"));
        sender.sendMessage(new TextComponentString(STHU.prefix + " /sthu toggle - Toggles on/off mod feature"));
        sender.sendMessage(new TextComponentString(STHU.prefix + " /sthu sethidelevel - Set hide shout level"));
        sender.sendMessage(new TextComponentString(STHU.prefix + " /sthu hidelevel - Shows hide lv behaviors"));
        sender.sendMessage(new TextComponentString(STHU.prefix + " /sthu banchat - Also blocks chat messages"));
        sender.sendMessage(new TextComponentString(STHU.prefix + " Current Hide Level: " + (STHU.hidelevel == 3 ? "§4HIGHEST" : (STHU.hidelevel == 2 ? "§cHIGH" : (STHU.hidelevel == 1 ? "§eMedium" : (STHU.hidelevel == 0 ? "§aLow" : "§7Failed to get"))))));
        sender.sendMessage(new TextComponentString(STHU.prefix + " -----------------------------------------"));
    }

    // cc §
    private void add(String s, ICommandSender sender) {
        // add player to list
        if (STHU.players.contains(s)) {
            sender.sendMessage(new TextComponentString(STHU.prefix + " §cThis player already exists."));
        } else {
            sender.sendMessage(new TextComponentString(STHU.prefix + " §aAdded player §b" + s + "§a to the ignore list!"));
            STHU.players.add(s);
            refreshConfig();
        }
    }

    private void remove(String s, ICommandSender sender) {
        // remove player from list
        if (STHU.players.contains(s)) {
            STHU.players.remove(s);
            sender.sendMessage(new TextComponentString(STHU.prefix + " §aRemoved player §b" + s + "§a from the ignore list!"));
        } else
            sender.sendMessage(new TextComponentString(STHU.prefix + " §cThe player does not exist from the list."));
        refreshConfig();
    }
    private void addtext(String s, ICommandSender sender) {
        // add player to list
        if (STHU.bannedWords.contains(s)) {
            sender.sendMessage(new TextComponentString(STHU.prefix + " §cThis text already exists."));
        } else {
            sender.sendMessage(new TextComponentString(STHU.prefix + " §aAdded text §b" + s + "§a to the ignore list!"));
            STHU.bannedWords.add(s);
            refreshConfig();
        }
    }

    private void removetext(String s, ICommandSender sender) {
        // remove player from list
        if (STHU.bannedWords.contains(s)) {
            STHU.bannedWords.remove(s);
            sender.sendMessage(new TextComponentString(STHU.prefix + " §aRemoved text §b" + s + "§a from the ignore list!"));
        } else
            sender.sendMessage(new TextComponentString(STHU.prefix + " §cThe text does not exist from the list."));
        refreshConfig();
    }
    private void toggle(ICommandSender sender) {
        STHU.active = !STHU.active;
        sender.sendMessage(new TextComponentString(STHU.prefix + " Mod is now " + (STHU.active ? "§aenabled" : "§cdisabled") + "§r."));
    }
    private void setHidelevel(String level, ICommandSender sender) {
        switch(level) {
            case "all":
                STHU.hidelevel = 3;
                break;
            case "high":
                STHU.hidelevel = 2; // Entirely hide
                break;
            case "medium":
                STHU.hidelevel = 1; // Shows [Blocked Shout Message]
                break;
            case "low":
                STHU.hidelevel = 0; // Shows [Blocked Shout Message by player]
                break;
        }
        sender.sendMessage(new TextComponentString(STHU.prefix + " §aHideLevel has been changed to §b" + level + "§a."));
        refreshConfig();
    }
    private void refreshConfig() {
        try {
            ConfigSpirit.writeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(STHU.bannedWords.contains("")) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(STHU.prefix + " Found blank messages in mod. It'll be removed."));
            STHU.bannedWords.remove("");
        }
    }
}
