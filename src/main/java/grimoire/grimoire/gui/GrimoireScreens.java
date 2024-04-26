package grimoire.grimoire.gui;

import grimoire.grimoire.Grimoire;
import grimoire.grimoire.data.GrimoireData;
import grimoire.grimoire.data.LoadFile;
import grimoire.grimoire.gui.widgets.GrimoireAllWidgets;
import grimoire.grimoire.gui.widgets.GrimoireButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static grimoire.grimoire.data.GrimoireData.*;
import static grimoire.grimoire.data.LoadFile.*;


@OnlyIn(Dist.CLIENT)
public class GrimoireScreens extends ScreensPlus {
    public Minecraft client;
    private GrimoireAllWidgets MySelfSeat;
    private ArrayList<GrimoireAllWidgets> PlayerSeat;
    private ArrayList<ArrayList<GrimoireAllWidgets>> PlayerInfos;
    private ArrayList<GrimoireAllWidgets> PlayerInfo;
    private int OptionListCreatX=-width;
    private final int OptionListCreatY=0;
    private int TwoBytewidth ;
    private Boolean isOptionDisplay=false,firstLoad = false;


//    private final JobAndInfoSelectionScreens jobAndinfoselection = new JobAndInfoSelectionScreens();

    public GrimoireScreens() {
        super(Component.literal("Grimoire"));
        getNightSheet(GrimoireData.Current_Script);
        getEveryNightPlayerNum();

    }
    public GrimoireScreens(int SeatNum,int Mode,String Info){
        super(Component.literal("Grimoire"));
        switch (Mode) {
            case 0:
                GrimoireData.PlayerJobChange(SeatNum, Info);
                break;
            case 1:
                GrimoireData.PlayerDescriptionAdd(SeatNum,Info);
                break;
        }
        getNightSheet(GrimoireData.Current_Script);
        getEveryNightPlayerNum();
    }


    @Override
    public Minecraft client() {
        return minecraft;
    }

    @Override
    public void init(){//初始化屏幕状态
        if(minecraft == null) {
            return;
        }

        int creatX = width / 2;
        int creatY = height / 2;
        int creatSize = height / 8;
        int radius = (creatY-creatSize/2-font.lineHeight-4);
        if(PlayerNum<15) {
             creatSize = height / 8;
        }else{
             creatSize = height / 8-(PlayerNum-14)*2;
        }
        PlayerSeat = new ArrayList<>();
        PlayerInfos = new ArrayList<>();
        PlayerInfo = new ArrayList<>();

        for (int i = 0; i < PlayerNum; i++) {
            boolean Death = PlayerDeath[i];
            String Job = null;
            File JobFile = null;
            String Introduction = null;
            if (Current_Script != null) {
                if (PlayerJob != null) {
                    Job = PlayerJob[i];
                    if (AllJobPath != null && AllJobPath.get(Job) != null) {
                        JobFile = AllJobPath.get(Job);
                    }
                    if (AllIntroduction != null && AllIntroduction.get(Job) != null) {
                        Introduction = AllIntroduction.get(Job);
                    }
                }
            }
            PlayerSeat.add(new GrimoireAllWidgets(this, (int) (creatX + radius * Math.cos(Math.PI * ((double) (2 * i) / PlayerNum - 0.5))), (int) (creatY + radius * Math.sin(Math.PI * ((double) (2 * i) / PlayerNum - 0.5))), creatSize, creatSize, i, Death, Job, JobFile, Introduction));

            for (int j = 0; j <= PlayerDescription.get(i).size(); j++) {
                int Inforadius = radius - (height /11) * (j + 1)-height/20;
                String CreateInfo = null;
                File CreateImage = null;
                if (PlayerDescription.get(i).size() > j) {
                    CreateInfo = PlayerDescription.get(i).get(j);
                    if (AllJobPath != null && AllJobPath.get(CreateInfo) != null) {
                        CreateImage = AllJobPath.get(CreateInfo);
                    }

                }
                PlayerInfo.add(new GrimoireAllWidgets(this, (int) (creatX + Inforadius * Math.cos(Math.PI * ((double) (2 * i) / PlayerNum - 0.5))), (int) (creatY + Inforadius * Math.sin(Math.PI * ((double) (2 * i) / PlayerNum - 0.5))), creatSize * 6 / 10, creatSize * 6 / 10, i, j, CreateInfo, CreateImage));
            }
            PlayerInfos.add(PlayerInfo);
            PlayerInfo = new ArrayList<>();
        }
        for(int i = 20;i<23;i++){//不在场身份组件加载

            String Job = null;
            File JobFile = null;
            String Introduction = null;
            if (Current_Script != null) {
                if (PlayerJob != null) {
                    Job = PlayerJob[i];
                    if (AllJobPath != null && AllJobPath.get(Job) != null) {
                        JobFile = AllJobPath.get(Job);
                    }
                    if (AllIntroduction != null && AllIntroduction.get(Job) != null) {
                        Introduction = AllIntroduction.get(Job);
                    }
                }
            }
            PlayerSeat.add(new GrimoireAllWidgets(this,(height / 8+4)*(i-20)+width/2-font.lineHeight*18-(height / 8+4),height-height / 16-4,height / 8,height / 8,i,Job,JobFile,Introduction,4));


        }
        MySelfSeat = new GrimoireAllWidgets(this.client(),width/2+font.lineHeight*12,height-height / 16-4,height / 8,height / 8,MyselfSeatNum);

        /*option列表*/





       setupWidgets();
    }

    @Override
    public void resize(@NotNull Minecraft client, int width, int height) {
        super.resize(client, width, height);
    }

    @Override
    public void render(@NotNull GuiGraphics guigraphics, int mouseX, int mouseY, float partialTicks) {

        this.renderBackground(guigraphics);
        guigraphics.drawCenteredString(font,Component.literal("魔典"), width/2, height/100, 0xFFFFFF);
        for(GrimoireAllWidgets seat : PlayerSeat){
            seat.updateHoverState(mouseX, mouseY, true);
            seat.render( guigraphics);
        }

        for(ArrayList<GrimoireAllWidgets> Infos : PlayerInfos){
            for(GrimoireAllWidgets Info : Infos){
                Info.updateHoverState(mouseX, mouseY, true);
                Info.render( guigraphics);
            }
        }

        for(GrimoireAllWidgets seat : PlayerSeat){
            seat.renderIntroduction(guigraphics,  mouseX,  mouseY);
        }
        MySelfSeat.render(guigraphics);

        TwoBytewidth= font.width("一");
        if(!firstLoad){
            firstLoad = true;
            OptionListCreatX=-TwoBytewidth*12;
        }
        if(isOptionDisplay){
            if(OptionListCreatX<0) {
                OptionListCreatX += TwoBytewidth * 12 / 30;
                setupWidgets();
            }else{
                OptionListCreatX = 0;
                setupWidgets();
            }
        }else{
            if(OptionListCreatX>-TwoBytewidth * 12) {
                OptionListCreatX -= TwoBytewidth * 12 / 30;
                setupWidgets();
            }else{
                OptionListCreatX = -TwoBytewidth * 12 ;
                setupWidgets();
            }
        }

        /*option列表*/
        guigraphics.fill(OptionListCreatX,OptionListCreatY,OptionListCreatX+TwoBytewidth*12,OptionListCreatY+font.lineHeight*17,0, FastColor.ARGB32.color(180,0,0,0));
        guigraphics.drawString(font, FormattedCharSequence.forward("更改游戏人数:",Style.EMPTY.withColor(0xFFFFFF).withBold(true)),OptionListCreatX+width/10-font.width("更改游戏人数:")/2,OptionListCreatY+font.lineHeight/2,font.width("更改游戏人数:"));
        guigraphics.drawString(font, FormattedCharSequence.forward(String.valueOf(PlayerNum),Style.EMPTY.withColor(0xFFFFFF).withBold(true)),OptionListCreatX+width/10-font.width(String.valueOf(PlayerNum))/2, OptionListCreatY+font.lineHeight*2,font.width(String.valueOf(PlayerNum)));
        guigraphics.drawString(font, FormattedCharSequence.forward("更改自身座位:",Style.EMPTY.withColor(0xFFFFFF).withBold(true)),OptionListCreatX+width/10-font.width("更改自身座位:")/2, OptionListCreatY+(int) (font.lineHeight*3.5),font.width("更改自身座位:"));
        guigraphics.drawString(font, FormattedCharSequence.forward(String.valueOf(MyselfSeatNum+1),Style.EMPTY.withColor(0xFFFFFF).withBold(true)),OptionListCreatX+width/10-font.width(String.valueOf(MyselfSeatNum+1))/2, OptionListCreatY+font.lineHeight*5,font.width(String.valueOf(MyselfSeatNum)));
        guigraphics.drawString(font, FormattedCharSequence.forward("当前剧本",Style.EMPTY.withColor(0xFFFFFF).withBold(true)),OptionListCreatX+TwoBytewidth, (int) (OptionListCreatY+(font.lineHeight*6.5)),font.width("当前剧本"));
        guigraphics.drawString(font, FormattedCharSequence.forward(Objects.requireNonNullElse(Current_Script, "无"),Style.EMPTY.withColor(0xFFFFFF).withBold(true)),OptionListCreatX+TwoBytewidth, OptionListCreatY+ (font.lineHeight*8),font.width(Objects.requireNonNullElse(Current_Script, "无")));
        super.render(guigraphics, mouseX, mouseY, partialTicks);





    }
    private void setupWidgets() {//加载小组件
        clearWidgets();
        /*option列表*/
        TwoBytewidth= font.width("一");
//      玩家人数修改按钮
        addRenderableWidget(new GrimoireButton( OptionListCreatX+width/10-(int)(font.lineHeight*(0.75-3)), (int) (OptionListCreatY+font.lineHeight*1.75), (int) (font.lineHeight*1.5), (int) (font.lineHeight*1.5), Component.literal("+"), (onPress) -> {
            GrimoireData.PlayerNumChange(1);
            init();
        }));
        addRenderableWidget(new GrimoireButton(OptionListCreatX+width/10-(int)(font.lineHeight*(0.75+3)), (int) (OptionListCreatY+font.lineHeight*1.75), (int) (font.lineHeight*1.5), (int) (font.lineHeight*1.5), Component.literal("-"), (onPress) -> {
            GrimoireData.PlayerNumChange(-1);
            init();
        }));

//      自身座位修改按钮
        addRenderableWidget(new GrimoireButton(OptionListCreatX+width/10-(int)(font.lineHeight*(0.75-3)), (int) (OptionListCreatY+font.lineHeight*4.75), (int) (font.lineHeight*1.5), (int) (font.lineHeight*1.5), Component.literal("+"), (onPress) -> {
            GrimoireData.MyselfSeatNumChange(1);
            init();
        }));
        addRenderableWidget(new GrimoireButton(OptionListCreatX+width/10-(int)(font.lineHeight*(0.75+3)), (int) (OptionListCreatY+font.lineHeight*4.75), (int) (font.lineHeight*1.5), (int) (font.lineHeight*1.5), Component.literal("-"), (onPress) -> {
            GrimoireData.MyselfSeatNumChange(-1);
            init();
        } ));

//      修改剧本按钮
        addRenderableWidget(new GrimoireButton((int) OptionListCreatX+TwoBytewidth*6, (int) (OptionListCreatY+(font.lineHeight*6.25)), (int) (font.lineHeight*4.5), (int) (font.lineHeight*1.5), Component.literal("修改剧本"), (onPress) -> {
            minecraft.setScreen(new GrimoireSelectionScreens(this));
        }));

 //        打开剧本身份介绍按钮
        addRenderableWidget(new GrimoireButton((int) (OptionListCreatX+TwoBytewidth), (int) (OptionListCreatY+(font.lineHeight*9.25)), (int) (font.lineHeight*5), (int) (font.lineHeight*1.5), Component.literal("身份介绍"), (onPress) -> {
            minecraft.setScreen(new JobIntroductionScreens(this));
        }));

//        是否显示不在场身份信息按钮
            addRenderableWidget(new GrimoireButton((int) (OptionListCreatX + TwoBytewidth), (int) (OptionListCreatY + (font.lineHeight * 10.75)), (int) (font.lineHeight * 9), (int) (font.lineHeight * 1.5),isDisplayMySelfSeat?Component.literal("隐藏自身座位信息"):Component.literal("显示自身座位信息"), (onPress) -> {
                isDisplayMySelfSeat = !isDisplayMySelfSeat;
            }));


//        是否显示自身座位信息按钮
        addRenderableWidget(new GrimoireButton((int) (OptionListCreatX+TwoBytewidth), (int) (OptionListCreatY+(font.lineHeight*12.25)), (int) (font.lineHeight*8), (int) (font.lineHeight*1.5), isDisplayAbsentIdentity?Component.literal("隐藏不在场身份"):Component.literal("显示不在场身份"), (onPress) -> {
            isDisplayAbsentIdentity =!isDisplayAbsentIdentity;
        }));
//        关闭魔典按钮
        addRenderableWidget(new GrimoireButton(OptionListCreatX+TwoBytewidth*7,OptionListCreatY+font.lineHeight*15, (int) (font.lineHeight*4.5), (int) (font.lineHeight*1.5), Component.literal("退出魔典"), (onPress) -> {
            onClose();
        }));
        //        清理魔典按钮
        addRenderableWidget(new GrimoireButton(OptionListCreatX+TwoBytewidth/2,OptionListCreatY+font.lineHeight*15, (int) (font.lineHeight*4.5), (int) (font.lineHeight*1.5), Component.literal("清空魔典"), (onPress) -> {
            GrimoireData.clear();
            getEveryNightPlayerNum();
            save(Grimoire.GrimoireFile);
            load(Grimoire.GrimoireFile);
            init();
        }));
        //        打开 关闭设置按钮
        addRenderableWidget(new GrimoireButton((OptionListCreatX+TwoBytewidth*12), (int) OptionListCreatY, (int) (font.lineHeight*3), (int) (font.lineHeight*1.5), Component.literal("设置"), (onPress) -> {
            isOptionDisplay = !isOptionDisplay;
        }));


        for(GrimoireAllWidgets seat : PlayerSeat){
            addWidget(seat);
        }
        for(ArrayList<GrimoireAllWidgets> Infos : PlayerInfos){
            for(GrimoireAllWidgets Info : Infos){
                addWidget(Info);
            }
        }
                addWidget(MySelfSeat);




    }
    public void onLeftClick(int Mode,int Seat){
        switch (Mode) {
            case 0:
                LoadFile.save(Grimoire.GrimoireFile);
                Minecraft.getInstance().setScreen(new JobAndInfoSelectionScreens(0,Seat));
                break;
            case 1:
                LoadFile.save(Grimoire.GrimoireFile);
                Minecraft.getInstance().setScreen(new JobAndInfoSelectionScreens(1,Seat));
                break;
            case 4:
                LoadFile.save(Grimoire.GrimoireFile);
                Minecraft.getInstance().setScreen(new JobAndInfoSelectionScreens(2,Seat));
                break;

            default:
                break;
        }
    }
    public void onLeftClickRight(int Mode,int Seat){
        switch (Mode) {
            case 0:
                GrimoireData.PlayerDeathChange(Seat);
            case 1:
                init();
            default:
                break;
        }
    }
    @Override
    public void onClose() {
        LoadFile.save(Grimoire.GrimoireFile);
        super.onClose();
    }
    private void saveData(){//保存数据

    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
    @Override
    public boolean charTyped(char chr, int modifiers) {
        return super.charTyped(chr, modifiers);
    }
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public @NotNull Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
        return super.getChildAt(mouseX, mouseY);
    }
}
