package grimoire.grimoire.gui.widgets;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrimoireButton extends Button {
    public GrimoireButton(int x, int y, int width, int height,Component label, Button.OnPress press) {
        super(x,y,width,height,label,press,DEFAULT_NARRATION);
    }
}


