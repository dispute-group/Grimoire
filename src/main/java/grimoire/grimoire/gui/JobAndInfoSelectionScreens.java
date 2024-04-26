package grimoire.grimoire.gui;

import ca.weblite.objc.Client;
import grimoire.grimoire.Grimoire;
import grimoire.grimoire.data.GrimoireData;
import grimoire.grimoire.data.LoadFile;
import grimoire.grimoire.gui.widgets.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static grimoire.grimoire.data.LoadFile.*;

@OnlyIn(Dist.CLIENT)

public class JobAndInfoSelectionScreens extends ScreensPlus{
    private final ArrayList<ArrayList<String>> AllName = LoadFile.AllName;
    private final File InfoFile= new File(path+"\\"+GrimoireData.Current_Script,"状态");
    private final int spacing =4;
    private final int Create_Box_Lines = 2;
    public Minecraft client;
    private int Mode;
    private int SeatNum;
    private int TotalHeight = 0;
    private int ScrolledAmount,Scrolled,Max_Scrolled;
    private GrimoireEditText EditBox ;
    private GrimoireButton EditFineButton,EditCancelBtton;

    private ArrayList<GrimoireAllWidgets> JobAndInfoWidgets = new ArrayList<>();
    protected JobAndInfoSelectionScreens(int Mode,int SeatNum) {
        super(Component.literal("JobSelection"));
        this.Mode=Mode;
        this.SeatNum=SeatNum;
    }

    @Override
    public void render(@NotNull GuiGraphics guigraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guigraphics);
            int create_Box_X = width / 10;
            int create_Box_Y = height / 10;
            int create_Box_Width = width * 8 / 10;
            int create_Box_Height = height * 8 / 10;
            guigraphics.fill(RenderType.gui(), create_Box_X, create_Box_Y, create_Box_X + create_Box_Width, create_Box_Y + Create_Box_Lines, 0, FastColor.ARGB32.color(255, 0, 0, 0));
            guigraphics.fill(RenderType.gui(), create_Box_X, create_Box_Y, create_Box_X + Create_Box_Lines, create_Box_Y + create_Box_Height, 0, FastColor.ARGB32.color(255, 0, 0, 0));
            guigraphics.fill(RenderType.gui(), create_Box_X, create_Box_Y + create_Box_Height - Create_Box_Lines, create_Box_X + create_Box_Width, create_Box_Y + create_Box_Height, 0, FastColor.ARGB32.color(255, 0, 0, 0));
            guigraphics.fill(RenderType.gui(), create_Box_X + create_Box_Width - Create_Box_Lines, create_Box_Y, create_Box_X + create_Box_Width, create_Box_Y + create_Box_Height, 0, FastColor.ARGB32.color(255, 0, 0, 0));
            guigraphics.fill(RenderType.gui(), create_Box_X + Create_Box_Lines, create_Box_Y + Create_Box_Lines, create_Box_X + create_Box_Width - Create_Box_Lines, create_Box_Y + create_Box_Height - Create_Box_Lines, 0, FastColor.ARGB32.color((int) (255 * 0.8), 0, 0, 0));
            if (Mode == 3) {
                guigraphics.drawString(font,FormattedCharSequence.forward("请输入自定义提醒：",Style.EMPTY.withColor(0xFFFFFF)),width/4,height/2-font.lineHeight*2,width/2,false);
                EditBox.render(guigraphics, mouseX, mouseY, partialTicks);
                EditCancelBtton.render(guigraphics, mouseX, mouseY, partialTicks);
                EditFineButton.render(guigraphics, mouseX, mouseY, partialTicks);
            } else {
                switch (Mode) {
                    case 0:
                        guigraphics.drawString(font, FormattedCharSequence.forward("请为" + (SeatNum + 1) + "号玩家选择身份", Style.EMPTY.withColor(0xFFFFFF)), width / 2 - font.width("请为" + (SeatNum + 1) + "号玩家选择身份") / 2, create_Box_Y + Create_Box_Lines + height / 40, font.width("请为" + (SeatNum + 1) + "号玩家选择身份"), false);
                        break;
                    case 1:
                        guigraphics.drawString(font, FormattedCharSequence.forward("请选择一个提醒标记:", Style.EMPTY.withColor(0xFFFFFF)), width / 2 - font.width("请选择一个提醒标记:") / 2, create_Box_Y + Create_Box_Lines + height / 40, font.width("请选择一个提醒标记:"), false);
                        break;
                    case 2:
                        guigraphics.drawString(font, FormattedCharSequence.forward("请选择不在场身份"+ (SeatNum - 19), Style.EMPTY.withColor(0xFFFFFF)), width / 2 - font.width("请选择不在场身份"+ (SeatNum - 19)) / 2, create_Box_Y + Create_Box_Lines + height / 40, font.width("请选择不在场身份"+ (SeatNum - 19)), false);
                        break;
                }
                if (!JobAndInfoWidgets.isEmpty()) {
                    for (GrimoireAllWidgets widget : JobAndInfoWidgets) {
                        if (widget.getY() > width / 10 + height / 8 / 2 + height * 8 / 10 - height / 8 || widget.getY() < width / 10 + height / 8 / 2 - spacing) {
                            continue;
                        }
                        widget.render(guigraphics);
                        widget.updateHoverState(mouseX, mouseY, true);
                    }
                    if (Mode == 0 || Mode == 2) {
                        for (GrimoireAllWidgets widget : JobAndInfoWidgets) {
                            if (widget.getY() > width / 10 + height / 8 / 2 + height * 8 / 10 - height / 8 || widget.getY() < width / 10 + height / 8 / 2 - spacing) {
                                continue;
                            }
                            widget.renderIntroduction(guigraphics, mouseX, mouseY);
                        }
                    }
                } else {
                    guigraphics.drawString(font, FormattedCharSequence.forward("请先选择剧本", Style.EMPTY.withColor(0xFFFFFF)), width / 2 - font.width("请先选择剧本") / 2, height / 2, font.width("请先选择剧本"), false);

                }
                super.render(guigraphics, mouseX, mouseY, partialTicks);
            }
    }
    @Override
    public void init() {//初始化屏幕状态

        int Create_List_X = width/10+height/8/2;
        int Create_List_Y =height/10+height/8/2;
        int Create_List_Width =width*8/10-height/8;
        int Create_List_Height = height*8/10-height/8;
        int Create_list_XCenter = Create_List_X + Create_List_Width/2;
        int Create_list_YCenter = Create_List_Y + Create_List_Height/2;




        switch (Mode) {
            case 0,2:
                if (JobAndInfoWidgets.isEmpty()) {
                    ArrayList<String> AllJobName = new ArrayList<>();
                    if (AllName != null) {
                        for (int i = 2; i < AllName.size(); i++) {
                            ArrayList<String> A1 = AllName.get(i);
                            AllJobName.addAll(A1);
                        }
                        int size = height / 8;
                        ScrolledAmount = size + spacing;
                        int JobImagePerRow = (Create_List_Width - spacing) / ((size) + spacing);
                        int maxXOff = JobImagePerRow - 1;
                        int childX = Create_List_X + spacing + size / 2;
                        int childY = Create_List_Y + spacing + size / 2;
                        int xOff = 0;
                        for (String JobName : AllJobName) {
                            GrimoireAllWidgets widget = new GrimoireAllWidgets(this, childX, childY, size, size, SeatNum, JobName, AllJobPath.get(JobName), AllIntroduction.get(JobName));
                            JobAndInfoWidgets.add(widget);
                            if (xOff == maxXOff) {
                                xOff = 0;
                                childX = Create_List_X + spacing + size / 2;
                                childY += size + spacing;
                            } else {
                                xOff++;
                                childX += size + spacing;
                            }
                        }
                        GrimoireAllWidgets widget = new GrimoireAllWidgets(this, childX, childY, size, size, SeatNum, null, null, null);
                        JobAndInfoWidgets.add(widget);
                        TotalHeight = Mth.ceil((float) AllJobName.size() / JobImagePerRow) * (size + spacing) + spacing;
                    }
                }
                if (TotalHeight > Create_List_Height) {
                    Max_Scrolled = TotalHeight;
                }
                break;
            case 1:
                if (JobAndInfoWidgets.isEmpty()) {
                    ArrayList<String> Infos = new ArrayList<>();
                    if(InfoFile.exists() && InfoFile.isDirectory()){
                        File[] files = InfoFile.listFiles();
                        if(files != null){
                            for(File file : files){
                                Infos.add(file.getName().substring(0, file.getName().lastIndexOf('.')));
                            }
                        }
                        int size = height / 16;
                        ScrolledAmount = size + spacing;
                        int JobImagePerRow = (Create_List_Width - spacing) / ((size) + spacing);
                        int maxXOff = JobImagePerRow - 1;
                        int childX;
                        int childY;
                        if(Infos.size()+2>JobImagePerRow) {
                            childX = Create_list_XCenter - (JobImagePerRow * (size + spacing) + spacing) / 2+size/2+spacing;
                        }else{
                            childX = Create_list_XCenter - ((Infos.size()+2)* (size + spacing) + spacing) / 2+size/2+spacing;
                        }
                        if((Infos.size()+2)/JobImagePerRow>6){
                            childY = Create_List_Y + spacing + size;
                        }else{
                            childY = Create_list_YCenter - ((Infos.size()+2)* (size + spacing) + spacing) / 2;
                        }

                        int xOff = 2;
                        JobAndInfoWidgets.add(new GrimoireAllWidgets(this, childX, childY, size, size, SeatNum,"善良",null));
                        childX += size + spacing;
                        JobAndInfoWidgets.add(new GrimoireAllWidgets(this, childX, childY, size, size, SeatNum,"邪恶",null));
                        childX += size + spacing;
                        for (String info : Infos) {
                            JobAndInfoWidgets.add(new GrimoireAllWidgets(this, childX, childY, size, size, SeatNum, info, AllJobPath.get(info)));
                            if (xOff == maxXOff) {
                                xOff = 0;
                                childX = Create_List_X + spacing + size;
                                childY += size + spacing;
                            } else {
                                xOff++;
                                childX += size + spacing;
                            }
                        }
                        childY += size + spacing;
                            JobAndInfoWidgets.add(new GrimoireAllWidgets(this, Create_list_XCenter, childY, size, size, SeatNum,"自定义",null));
                    }
                }
                break;
        }
        setupwidget();
    }
    private void setupwidget(){
        clearWidgets();
        if (JobAndInfoWidgets != null) {
            for (GrimoireAllWidgets widget : JobAndInfoWidgets) {
                addWidget(widget);
            }
        }
        addRenderableWidget(new GrimoireButton(width*9/10-40,height*9/10-20,35,15,Component.literal("取消"),(onPress)->{
            Objects.requireNonNull(minecraft).setScreen(new GrimoireScreens());
        }));
    }
    @Override
    public void onLeftClick(int Mode, int SeatNum){
        clearWidgets();
        this.Mode = Mode;
        EditBox = addRenderableWidget(new GrimoireEditText(font,width/4,height/2-font.lineHeight,width/2,font.lineHeight*2,Component.literal(" ")));
        EditFineButton = addRenderableWidget(new GrimoireButton(width*3/4-width/20,height/2+height/20,width/20,height/20,Component.literal("确定"),(onPress)->{
            EditBox.tick();
            LoadFile.save(Grimoire.GrimoireFile);
            Minecraft.getInstance().setScreen(new GrimoireScreens(SeatNum, 1, EditBox.getValue()));
        }));
        EditCancelBtton= addRenderableWidget(new GrimoireButton(width*3/4-width/10-6,height/2+height/20,width/20,height/20,Component.literal("取消"),(onPress)->{
            EditBox.setValue("");
            this.Mode = 1;
            setupwidget();
        }));

    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return Scrolled(mouseX, mouseY, amount);
    }

    private boolean Scrolled(double scroll1, double scroll2, double scroll3) {
        double Chang = scroll3 * ScrolledAmount;
        if(Scrolled + Chang <=0 && Scrolled + Chang>-Max_Scrolled) {
            Scrolled += (int) Chang;
            for(GrimoireAllWidgets widegt: JobAndInfoWidgets ){
                widegt.ChangY((int) Chang);
            }
        }
        return true;
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
