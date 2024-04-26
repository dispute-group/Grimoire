package grimoire.grimoire.gui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
import net.minecraft.network.chat.Component;

public class GrimoireMultiLineText extends FittingMultiLineTextWidget {
    public GrimoireMultiLineText(int x, int y, int width, int height, Component massage, Font font) {
        super(x, y, width, height, massage, font);


    }
}
