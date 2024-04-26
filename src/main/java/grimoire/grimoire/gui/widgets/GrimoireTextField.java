package grimoire.grimoire.gui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.network.chat.Component;

@OnlyIn(Dist.CLIENT)
public class GrimoireTextField extends EditBox {
    // 输入框

    private Font font;
    private Component label;
    public GrimoireTextField(Font font, int x, int y, int width, int height, Component label) {
        super(font, x, y, width, height, label);
        this.font = font;
//        font.drawInBatch()
        this.label = label;
    }

}

