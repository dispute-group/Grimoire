package grimoire.grimoire.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import grimoire.grimoire.gui.GrimoireSelectionScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrimoireEntry extends ObjectSelectionList.Entry<GrimoireEntry> {

    private final Minecraft mc;
    private final GrimoireSelectionScreens grimoireScreens;
    private final String script;
    private final GrimoireList grimoireList;
    private long lastClickTime;

    public GrimoireEntry(GrimoireList grimoireList, String script) {
        this.grimoireList = grimoireList;
        this.script = script;
        grimoireScreens = grimoireList.getParentScreen();
        mc = Minecraft.getInstance();
    }

    @Override
    public boolean mouseClicked (double mouseX, double mouseY, int button) {
        if (button == 0) {
            // 选中
            grimoireList.selectGrimoire(this);
        }
        return false;
    }
    @Override
    public Component getNarration() {
        return Component.literal("hello");
    }

    @Override
    public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int par6, int par7, boolean par8, float par9) {

        guiGraphics.drawString(mc.font, Component.literal(script), left, top+height/4, 0xffffff);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void setScript() {
        grimoireScreens.setScript(script);

    }
}
