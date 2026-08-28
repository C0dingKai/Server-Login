package dev.kai.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * ColorUtil
 *
 * @author Kai
 * @since 8/28/2026
 */
@UtilityClass
public class ColorUtil {
    public static Component parse(String input) {
        return MiniMessage.miniMessage().deserialize(input).decoration(TextDecoration.ITALIC, false);
    }

    public static Component empty() {
        return Component.empty();
    }

}