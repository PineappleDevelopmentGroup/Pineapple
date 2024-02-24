package sh.miles.pineapple.config.adapter;


import net.md_5.bungee.api.ChatColor;
import sh.miles.pineapple.config.adapter.base.TypeAdapterString;

import java.awt.Color;

class ChatColorAdapter implements TypeAdapterString<ChatColor> {

    @Override
    public Class<ChatColor> getRuntimeType() {
        return ChatColor.class;
    }

    @Override
    public String toString(ChatColor value) {
        return "#" + Integer.toHexString(value.getColor().getRGB()).substring(2);
    }

    @Override
    public ChatColor fromString(String value) {
        if (value.startsWith("#")) {
            value = value.substring(1);
        }

        return ChatColor.of(new Color(Integer.parseInt(value, 16)));
    }
}
