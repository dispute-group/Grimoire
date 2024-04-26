package grimoire.grimoire.gui;

import grimoire.grimoire.Grimoire;
import grimoire.grimoire.data.GrimoireData;
import grimoire.grimoire.data.LoadFile;
import grimoire.grimoire.gui.widgets.GrimoireButton;
import grimoire.grimoire.gui.widgets.GrimoireList;
import grimoire.grimoire.gui.widgets.GrimoireText;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;


@OnlyIn(Dist.CLIENT)
    public class GrimoireSelectionScreens extends Screen {
    private Button SelectButton;
    private Button returnButton;
    private GrimoireList ScriptSelectList;
    private StringWidget CurrentScript;
    Screen parent = null;

    public GrimoireSelectionScreens() {
        super(Component.literal("Grimoire"));
    }
    public GrimoireSelectionScreens(Screen parent) {
        super(Component.literal("Grimoire"));
        this.parent = parent;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(font, Component.literal("剧本选择"), width / 2, height / 100, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }


    @Override
    public void init() {//初始化屏幕状态
        LoadFile.load(Grimoire.GrimoireFile);
        setupWidgets();
    }


    private void setupWidgets() {
        clearWidgets();

             String CScript;
            CScript = Objects.requireNonNullElse(GrimoireData.Current_Script, "无");
                CurrentScript = addRenderableWidget(new GrimoireText(30,10,40,25,Component.literal("当前剧本为:"+CScript),font));
                CurrentScript.alignLeft();
//        if(CurrentScript == null){
//        }
//        if (returnButton == null) {
            returnButton = addRenderableWidget(new GrimoireButton(30, 30, 40, 25, Component.literal("选 择"), (onPress) -> {
                if (ScriptSelectList.hasSelection()) {
                    GrimoireData.clear();
                    Objects.requireNonNull(ScriptSelectList.getSelected()).setScript();
                    LoadFile.save(Grimoire.GrimoireFile);
                    LoadFile.load(Grimoire.GrimoireFile);
                }
            }));
//        }
//        if (SelectButton == null) {
            SelectButton = addRenderableWidget(new GrimoireButton(30, 70, 40, 25, Component.literal("返 回"), (onPress) -> {
            if(parent == null){
                minecraft.setScreen(null);
            }else{
                minecraft.setScreen(parent);

            }
            }));
//        }
//        if (ScriptSelectList == null) {
            ScriptSelectList = addRenderableWidget(new GrimoireList(this, minecraft, width + 110, height, 40, height, 18));
//        }

    }
    @Override
    public boolean mouseScrolled(double scroll1, double scroll2, double scroll3) {

        return ScriptSelectList.mouseScrolled(scroll1, scroll2, scroll3);
    }
    public void setScript(String script){
        LoadFile.load(Grimoire.GrimoireFile);
        GrimoireData.setScript(script);
        LoadFile.save(Grimoire.GrimoireFile);
        setupWidgets();
    }
}