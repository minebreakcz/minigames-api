package net.graymadness.minigame_api.helper;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum ChatInfo
{

    GENERAL_INFO("§6§l[*] §e", ComponentBuilder.text("[*] ").bold(true).color(ChatColor.GOLD).build(), ChatColor.YELLOW),
    UNAVAILABLE("§8§l> §7", ComponentBuilder.text("> ").bold(true).color(ChatColor.DARK_GRAY).build(), ChatColor.GRAY),
    SUCCESS("§2§l[#] §a", ComponentBuilder.text("[#] ").bold(true).color(ChatColor.DARK_GREEN).build(), ChatColor.GREEN),
    WARNING("§c§l[!] §e", ComponentBuilder.text("[!] ").bold(true).color(ChatColor.RED).build(), ChatColor.YELLOW),
    ERROR("§4§l[!!] §c", ComponentBuilder.text("[!!] ").bold(true).color(ChatColor.DARK_RED).build(), ChatColor.RED),
    CRIME("§4§l[!!!] §c", ComponentBuilder.text("[*] ").bold(true).color(ChatColor.DARK_RED).build(), ChatColor.RED),
    NULL("", null, null)
    ;

    private final String prefix;
    private final BaseComponent component;
    private final ChatColor messageColor;

    ChatInfo(String prefix, @Nullable BaseComponent component, @Nullable ChatColor messageColor) {
        this.prefix = prefix;
        this.component = component;
        this.messageColor = messageColor;
    }

    @Deprecated
    public String getPrefix() {
        return prefix;
    }

    @Nullable
    public BaseComponent getComponentPrefix() {
        return component;
    }

    @Nullable
    public ChatColor getMessageColor() {
        return messageColor;
    }

    @NotNull
    public BaseComponent formatMessage(@NotNull BaseComponent component) {
        BaseComponent message = this.component == null ? null : this.component.duplicate();
        if(message == null) {
            message = component.duplicate();
            if(messageColor != null)
                message.setColor(messageColor.asBungee());
        } else {
            BaseComponent msg = component.duplicate();
            if(messageColor != null)
                msg.setColor(messageColor.asBungee());
            if(msg.isBoldRaw() == null)
                msg.setBold(false);

            message.addExtra(msg);
        }

        return message;
    }

    @Deprecated
    public void send(Player player, String message) {
        player.sendMessage(prefix + message);
    }

    @Deprecated
    public void send(CommandSender sender, String message) {
        sender.sendMessage(prefix + message);
    }

    @Deprecated
    public String format(String message) {
        return this.prefix + message;
    }

    public void send(@NotNull Player player, @NotNull BaseComponent component) {
        BaseComponent translated = Translate.t(formatMessage(component), player);
        player.spigot().sendMessage(translated);
    }
    public void send(@NotNull CommandSender sender, @NotNull BaseComponent component) {
        if(sender instanceof Player) {
            send((Player) sender, component);
            return;
        }

        sender.sendMessage(formatMessage(component).toLegacyText());
    }
}
