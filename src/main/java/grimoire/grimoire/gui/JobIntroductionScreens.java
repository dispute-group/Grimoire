package grimoire.grimoire.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import grimoire.grimoire.Grimoire;
import grimoire.grimoire.data.LoadFile;
import grimoire.grimoire.gui.widgets.GrimoireButton;
import grimoire.grimoire.gui.widgets.GrimoireMultiLineText;
import grimoire.grimoire.gui.widgets.GrimoireText;
import grimoire.grimoire.gui.widgets.newJobImageLoad;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class JobIntroductionScreens extends Screen  {
    private final ArrayList<ArrayList<String>> AllJobName  = LoadFile.AllName;
    private final HashMap<String,String> AllIntroduction = LoadFile.AllIntroduction;
    private final HashMap<String,File> jobPath = LoadFile.AllJobPath;
    private final HashMap<String,newJobImageLoad> jobImagePath = LoadFile.AllLoadImage;
    private final double ScrolledAmount = 24.0d;
    private double Scrolled = 0d;
    private double Max_Scrolled;
    private GrimoireButton ReturnButton;

    private final Screen parent;
    public JobIntroductionScreens() {
        super(Component.literal("Grimoire"));
        parent= null;
    }
    public JobIntroductionScreens(Screen parent) {
        super(Component.literal("Grimoire"));
        this.parent = parent;
    }



    @Override
    public void render(@NotNull GuiGraphics guigraphics, int mouseX, int mouseY, float partialTicks) {

        renderBackground(guigraphics);

        int row = 0;
        int CreateNum = 0;
        int image_size = height / 7;
        int fillGradientColor = 0x00000000;
        int num = 0;
        super.render(guigraphics, mouseX, mouseY, partialTicks);
    if(AllJobName!=null){
        for (ArrayList<String> strings : AllJobName) {
            fillGradientColor = switch (num) {
                case 1 -> 0xffff00ff;//粉色
                case 2 -> 0xff0000ff;//蓝色
                case 3 -> 0xff00ffff;//青色
                case 4 -> 0xffff5500;//橙色
                case 5 -> 0xffff0000;//红色
                case 6 -> 0xffffffff;//白色
                default -> fillGradientColor;
            };
            num++;
            guigraphics.fillGradient(0, getCreateY(row - 1) + font.lineHeight / 2, width, getCreateY(row), 0, 0x000000, fillGradientColor);
            guigraphics.fill(0, getCreateY(row), width, getCreateY(row) + font.lineHeight, fillGradientColor);
            row++;
            while (CreateNum < strings.size()) {
                for (int j = 0; j < 3 && CreateNum < strings.size(); j++) {
                    if (getCreateY(row) + image_size < 0 || getCreateY(row) - image_size > height) {
                        CreateNum++;
                        continue;
                    }
                    String jobName = strings.get(CreateNum);
                    if (jobImagePath.get(jobName) != null) {
                        newJobImageLoad jobImageLoad = jobImagePath.get(jobName);
                        if (jobImageLoad != null/* && jobImageLoad.getPixels() != null*/) {
                            RenderSystem.setShader(GameRenderer::getPositionTexShader);
                            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                            RenderSystem.setShaderTexture(0, jobImageLoad.imageId());
                            RenderSystem.enableBlend();
                            newJobImageLoad.drawTexture(guigraphics, getCreateX(j) + height / 68 - 5, getCreateY(row), image_size, image_size, 0, 0, image_size, image_size, image_size, image_size);
                            RenderSystem.disableBlend();
                        }
                    }
                    CreateNum++;
                }
                row += 2;
            }

            CreateNum = 0;
        }
    }
        guigraphics.fillGradient(0,0,width, height / 40+font.lineHeight,1, 0xFF000000, 0x00000000);
        guigraphics.fillGradient(0,height-(height / 40+font.lineHeight),width, height+height / 40+font.lineHeight,1,  0x00000000,0xFF000000);
        ReturnButton.render(guigraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void init() {//初始化屏幕状态
        LoadFile.load(Grimoire.GrimoireFile);
        setupWidgets();
    }

    private void setupWidgets() {
        clearWidgets();
        int row =0;
        int CreateNum = 0;

        addRenderableWidget(new GrimoireText(0,getCreateY(0),width, height/100,Component.literal("角色技能表"),font));

        if(AllJobName!=null) {
            for (int i = 0; i < AllJobName.size(); i++) {
                String Class = "无";
                int ClassColor = 0xFFFFFF;
                if (i > 0) {
                    switch (i) {
                        case 1:
                            Class = "状 态";
                            break;
                        case 2:
                            Class = "镇 民";
                            break;
                        case 3:
                            Class = "外来者";
                            break;
                        case 4:
                            Class = "爪 牙";
                            break;
                        case 5:
                            Class = "恶 魔";
                            break;
                        case 6:
                            Class = "旅行者";
                            ClassColor = 0x000000;
                            break;
                    }
                    GrimoireText JobClass = addRenderableWidget(new GrimoireText(0, getCreateY(row), width, font.lineHeight, Component.literal(Class), font));
                    JobClass.setColor(ClassColor);
                }
                row++;
                while (CreateNum < AllJobName.get(i).size()) {
                    for (int j = 0; j < 3; j++) {
                        if (getCreateY(row) + (height / 7 + font.lineHeight) < 0 || getCreateY(row) - (height / 7 + font.lineHeight) > height) {
                            CreateNum++;
                            continue;
                        }
                        if (CreateNum < AllJobName.get(i).size()) {
                            if (AllJobName.get(i).get(CreateNum) != null) {
                                GrimoireText Job = addRenderableWidget(new GrimoireText(getCreateX(j) + height * 5 / 34, (int) getCreateY(row) - font.lineHeight, width * 7 / 34, height / 100, Component.literal(AllJobName.get(i).get(CreateNum)), font));

                                Job.alignLeft();
                                switch (i) {
                                    case 1:
                                        Job.setColor(0xff00ff);
                                        break;//粉色
                                    case 2:
                                        Job.setColor(0x0000ff);
                                        break;//蓝色
                                    case 3:
                                        Job.setColor(0x00ffff);
                                        break;//青色
                                    case 4:
                                        Job.setColor(0xff5500);
                                        break;//橙色
                                    case 5:
                                        Job.setColor(0xff0000);
                                        break;//红色
                                    case 6:
                                        Job.setColor(0xffffff);
                                        break;//白色
                                }
                            }
                            if (AllIntroduction.get(AllJobName.get(i).get(CreateNum)) != null) {
                                GrimoireMultiLineText introduction = addRenderableWidget(new GrimoireMultiLineText(getCreateX(j) + height * 5 / 34, getCreateY(row), width * 7 / 34, height / 7, Component.literal(AllIntroduction.get(AllJobName.get(i).get(CreateNum))), font));
                            }
                            CreateNum++;
                        } else {
                            break;
                        }
                    }
                    row += 2;
                }
                CreateNum = 0;
            }
        }
        setMaxScrolled(row);
        ReturnButton = addRenderableWidget(new GrimoireButton(3,15,40, 20,parent==null?Component.literal("退出"):Component.literal("返回"),(onPress)->{
            if(parent == null){
                Objects.requireNonNull(Objects.requireNonNull(minecraft).screen).onClose();
            }else{
                Objects.requireNonNull(minecraft).setScreen(parent);
            }
        }));
    }

    @Override
    public boolean mouseScrolled(double scroll1, double scroll2, double scroll3) {
        return Scrolled(scroll1, scroll2, scroll3);
    }

    private boolean Scrolled(double scroll1, double scroll2, double scroll3) {
        double Chang = scroll3 * ScrolledAmount;
        if(Scrolled + Chang <=0 && Scrolled + Chang>-Max_Scrolled) {
            Scrolled += Chang;
        }
        setupWidgets();
        return true;
    }
    private int getCreateX(int Col){
        return height/34+width*11/34*Col;
    }
    private int getCreateY(int Row){
        return (int) (height/10+Scrolled)+(height/10*Row);
    }
    private void setMaxScrolled(int Row){
        Max_Scrolled=((double) height /10)*(Row+2)-height;
    }


}
