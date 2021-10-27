package net.morimori0317.simpletranslation;


import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.morimori0317.simpletranslation.impl.SimpleTranslationExpectPlatform;
import net.morimori0317.simpletranslation.translation.TranslationManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClientHandler {
    public static boolean enableTranslate;
    private static boolean spushFlg;
    private static boolean pushFlg;

    public static void init() {
        ClientTooltipEvent.ITEM.register(ClientHandler::onTooltip);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(ClientHandler::quit);
    }

    public static void quit(@Nullable LocalPlayer localPlayer) {
        TranslationManager.getInstance().writeCash();
    }

    public static void onTooltip(ItemStack itemStack, List<Component> list, TooltipFlag tooltipFlag) {
        boolean press = SimpleTranslationExpectPlatform.isKeyPress(SimpleTranslation.TRANSLATE_KEY);
        if (SimpleTranslation.CONFIG.enableToggleMode) {
            if (press && !spushFlg) pushFlg = true;
            if (pushFlg) {
                enableTranslate = !enableTranslate;
            }
            spushFlg = true;
            pushFlg = false;
            if (!press) {
                spushFlg = false;
            }
        }

        if ((enableTranslate && SimpleTranslation.CONFIG.enableToggleMode) || (press && !SimpleTranslation.CONFIG.enableToggleMode)) {
            for (Component component : TranslationManager.getInstance().createToolTip(itemStack.getHoverName())) {
                list.add(1, component);
            }
        }

    }
}
