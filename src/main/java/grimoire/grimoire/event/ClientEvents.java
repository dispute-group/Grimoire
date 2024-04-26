package grimoire.grimoire.event;

import grimoire.grimoire.Grimoire;
import grimoire.grimoire.data.LoadFile;
import grimoire.grimoire.gui.AbsenceAndMyselfShow;
import grimoire.grimoire.gui.GrimoireScreens;
import grimoire.grimoire.gui.JobIntroductionScreens;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


public class ClientEvents {
    @Mod.EventBusSubscriber(modid = Grimoire.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (KeyMappingEvent.Open_Grimoire.consumeClick()) {
                LoadFile.load(Grimoire.GrimoireFile);
                Minecraft.getInstance().setScreen(new GrimoireScreens());
            }
            if (KeyMappingEvent.Open_Introduce.consumeClick()) {
                LoadFile.load(Grimoire.GrimoireFile);
                Minecraft.getInstance().setScreen(new JobIntroductionScreens());
            }
        }

        @Mod.EventBusSubscriber(modid = Grimoire.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
        public static class ClientModBusEvents {
            @SubscribeEvent
            public static void onKeyInput(RegisterKeyMappingsEvent event) {
                event.register(KeyMappingEvent.Open_Grimoire);
                event.register(KeyMappingEvent.Open_Introduce);
            }


            @SubscribeEvent
            public static void registerGuiOverlay(RegisterGuiOverlaysEvent event) {
                event.registerAboveAll("absence_and_myself", AbsenceAndMyselfShow.AbsenceAndMyself);
            }

        }

        @SubscribeEvent
        public static void PlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        }
    }
}