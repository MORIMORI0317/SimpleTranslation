package net.morimori0317.simpletranslation;


import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.events.client.ClientChatEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.morimori0317.simpletranslation.impl.SimpleTranslationExpectPlatform;
import net.morimori0317.simpletranslation.translation.TranslationManager;
import net.morimori0317.simpletranslation.util.LangUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ClientHandler {
    private static final Minecraft mc = Minecraft.getInstance();
    public static boolean enableTranslate;
    private static boolean spushFlg;
    private static boolean pushFlg;

    public static void init() {
        ClientTooltipEvent.ITEM.register(ClientHandler::onTooltip);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(ClientHandler::quit);
        ClientChatEvent.RECEIVED.register(ClientHandler::received);
    }

    public static CompoundEventResult<Component> received(ChatType chatType, Component component, @Nullable UUID uuid) {
        var tx = component.getString();
        if (!tx.startsWith("<" + LangUtils.getLangByGoogleCodeName(TranslationManager.getInstance().currentLang()) + ">")) {
          /*  var ts = TranslationManager.getInstance().getChatTranslation(component, ClientHandler::sendTranslateChat);
            if (ts != null)
                sendTranslateChat(ts);+*/
            Thread th = new Thread(() -> {
                try {
                    var str = TranslationManager.getInstance().translator.translate(tx, "", TranslationManager.getInstance().currentLang());
                    if (!tx.equals(str))
                        sendTranslateChat(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            th.start();
        }
        return CompoundEventResult.interruptDefault(component);
    }

    private static void sendTranslateChat(String text) {
        if (mc.player != null) {
            mc.player.displayClientMessage(new TextComponent("<" + LangUtils.getLangByGoogleCodeName(TranslationManager.getInstance().currentLang()) + ">" + text), false);
        }
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
