package grimoire.grimoire.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class ScreensPlus extends Screen {
    protected ScreensPlus(Component p_96550_) {
        super(p_96550_);
    }
    public Minecraft client() {
        return minecraft;
    }
    public void onLeftClick(int Mode,int Seat){}
    public void onLeftClickRight(int Mode,int Seat){}

}
