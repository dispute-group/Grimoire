package grimoire.grimoire.gui;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import static grimoire.grimoire.data.GrimoireData.Current_Script;
import static grimoire.grimoire.data.LoadFile.FirstNight;
import static grimoire.grimoire.data.LoadFile.getNightSheet;


public class GrimoireNightSheetScreen extends Screen {
    protected GrimoireNightSheetScreen() {
        super(Component.literal("行动次序表"));
        getNightSheet(Current_Script);
    }
    @Override
    public void render(@NotNull GuiGraphics guigraphics, int mouseX, int mouseY, float partialTicks) {
    }

}
