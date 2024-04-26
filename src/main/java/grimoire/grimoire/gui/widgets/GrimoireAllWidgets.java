package grimoire.grimoire.gui.widgets;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import grimoire.grimoire.Grimoire;
import grimoire.grimoire.data.GrimoireData;
import grimoire.grimoire.data.LoadFile;
import grimoire.grimoire.gui.GrimoireScreens;
import grimoire.grimoire.gui.ScreensPlus;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.*;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.mojang.text2speech.Narrator.LOGGER;
import static grimoire.grimoire.data.GrimoireData.PlayerNickName;
import static grimoire.grimoire.data.LoadFile.*;

public class GrimoireAllWidgets extends AbstractWidget implements AutoCloseable {

    private final int Mode;
    private final int SeatNumber;
    private final int InfoNumber;
    private final ScreensPlus mainScreen;
    private Minecraft client;
    private final File ImagePath;
    private final String Job;
    private final String Info;
    private  List<FormattedCharSequence> Info_split = null;
    private  List<FormattedCharSequence> Info_split2 = null;
    private CompletableFuture<NativeImage> image;
    private float Death_bgOpacity = 0;
    private double Death_Pos=-6;
    private float bgOpacity = 0;
    private Boolean Death;



    @Nullable
    private DynamicTexture texture;

    public GrimoireAllWidgets(ScreensPlus mainScreen, int x, int y, int width, int height, int SeatNumber, Boolean Death, @Nullable String Job, @Nullable File ImagePath, @Nullable String Info) {
        super(x, y, width, height, Component.literal("Grimoire Seat"));
        this.mainScreen = mainScreen;
        this.client = mainScreen.client();
        this.Job= Job;
        this.ImagePath = ImagePath;
        this.image = getImage(ImagePath);
        this.Death = Death;
        this.SeatNumber = SeatNumber;
        this.Info = Info;
        this.Mode = 0;
        this.InfoNumber = -1;
    }

    public GrimoireAllWidgets(ScreensPlus mainScreen,int x, int y, int width, int height, int SeatNumber,int InfoNumber,@Nullable String Info,@Nullable File ImagePath){
        super(x, y, width, height, Component.literal("Grimoire Info"));
        this.mainScreen = mainScreen;
        this.client = mainScreen.client();
        this.ImagePath = ImagePath;
        this.image = getImage(ImagePath);
        this.Info = Info;
        this.Mode = 1;
        this.SeatNumber = SeatNumber;
        this.Job = null;
        this.InfoNumber = InfoNumber;
    }

    public GrimoireAllWidgets(ScreensPlus mainScreen, int x, int y, int width, int height,int SeatNumber, @Nullable String Job, @Nullable File ImagePath,@Nullable String Info) {
        super(x, y, width, height, Component.literal("Grimoire SelectionJob"));
        this.mainScreen = mainScreen;
        this.client = mainScreen.client();
        this.Job= Job;
        this.ImagePath = ImagePath;
        this.image = getImage(ImagePath);
        this.Death = false;
        this.SeatNumber = SeatNumber;
        this.Info = Info;
        this.Mode = 2;
        this.InfoNumber = -1;


    }

    public GrimoireAllWidgets(ScreensPlus mainScreen, int x, int y, int width, int height,int SeatNumber, @Nullable String Info, @Nullable File ImagePath) {
        super(x, y, width, height, Component.literal("Grimoire SelectionJob"));
        this.mainScreen = mainScreen;
        this.client = mainScreen.client();
        this.Job = null;
        this.ImagePath = ImagePath;
        this.image = getImage(ImagePath);
        this.Death = false;
        this.SeatNumber = SeatNumber;
        this.Info = Info;
        this.Mode = 3;
        this.InfoNumber = -1;
    }

    public GrimoireAllWidgets(ScreensPlus mainScreen, int x, int y, int width, int height,int SeatNumber, @Nullable String Job, @Nullable File ImagePath, @Nullable String Info,int Mode) {
        super(x, y, width, height, Component.literal("Grimoire Absent identity"));
        this.mainScreen = mainScreen;
        this.client = mainScreen.client();
        this.Job = Job;
        this.ImagePath = ImagePath;
        this.image = getImage(ImagePath);
        this.Death = false;
        this.SeatNumber = SeatNumber;
        this.Info = Info;
        this.Mode = Mode;
        this.InfoNumber = -1;
    }

    public GrimoireAllWidgets(Minecraft client,int x, int y, int width, int height,int SeatNumber) {
        super(x, y, width, height, Component.literal("Grimoire Non interactive"));
        this.mainScreen = null;
        this.client = client;
        this.Job = GrimoireData.PlayerJob[SeatNumber];
        if(AllJobPath != null) {
            this.ImagePath = AllJobPath.get(Job);
        }else{
            this.ImagePath = null;
        }
        this.image = getImage(ImagePath);
        if(GrimoireData.PlayerDeath.length>SeatNumber) {
            this.Death = GrimoireData.PlayerDeath[SeatNumber];
        }else{
            this.Death = false;
        }
        this.SeatNumber = SeatNumber;
        if(AllIntroduction != null) {
            this.Info = AllIntroduction.get(Job);
        }else{
            this.Info = null;
        }
        this.Mode = 5;
        this.InfoNumber = -1;
    }




    private void getInfosplit() {
        if (Info != null) {
            switch (Mode) {
                case 0, 2,4:
                    Info_split = new ArrayList<>();
                    this.Info_split = client.font.split(FormattedText.of(Info, Style.EMPTY.withColor(0x000000)), height * 3);
                    break;
                case 1:
                    Info_split = new ArrayList<>();
                    Info_split2 = new ArrayList<>();
                    this.Info_split = client.font.split(FormattedText.of(Info, Style.EMPTY.withColor(0x000000).withBold(true)),client.font.width("一")*4+4 );
                    this.Info_split2 = client.font.split(FormattedText.of(Info, Style.EMPTY.withColor(0xFFFFFF).withBold(true)),client.font.width("一")*4+4 );
                    break;
            }
        }
    }
    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics p_282139_, int p_268034_, int p_268009_, float p_268085_) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isHoveredOrFocused()) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT ) {
                super.playDownSound(this.client.getSoundManager());
            switch (Mode){
                case 0://座位左键
                    if (mouseX < getX()) {
                        Mode0_onLeftClickLeft();
                        break;
                    } else {
                        Mode0_onLeftClickRight();
                        break;
                    }
                case 1:
                    Mode1_onLeftClick();
                    break;
                case 2:
                    Mode2_onLeftClick();
                    break;
                case 3:
                    Mode3_onLeftClick();
                    break;
                case 4:
                    Mode0_onLeftClickLeft();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + Mode);
            }




            }
            return true;
        }
        return false;
    }
    private void Mode0_onLeftClickLeft() {
        LoadFile.save(Grimoire.GrimoireFile);
        mainScreen.onLeftClick(Mode,SeatNumber);
    }
    private void Mode0_onLeftClickRight() {
        Death=!Death;
        mainScreen.onLeftClickRight(Mode,SeatNumber);
        LoadFile.save(Grimoire.GrimoireFile);
    }
    private void Mode1_onLeftClick() {
        if(Info !=null ){
            GrimoireData.PlayerDescriptionDelete(SeatNumber,InfoNumber);
            LoadFile.save(Grimoire.GrimoireFile);
            mainScreen.onLeftClickRight(Mode,SeatNumber);
        }else{
            LoadFile.save(Grimoire.GrimoireFile);
            mainScreen.onLeftClick(Mode,SeatNumber);
        }
    }
    private void Mode2_onLeftClick() {
        Minecraft.getInstance().setScreen(new GrimoireScreens(SeatNumber,0,Job));
    }
    private void Mode3_onLeftClick() {
        if(Info.equals("自定义")){
            mainScreen.onLeftClick(Mode,SeatNumber);
        }else{
            Minecraft.getInstance().setScreen(new GrimoireScreens(SeatNumber,1,Info));
        }

    }





    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public boolean isHoveredOrFocused() {
        return isHovered;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return isHoveredOrFocused();
    }







    public void render(GuiGraphics graphics) {
        renderBackground(graphics);
            int CreatX = getX() - width / 2;
            int CreatY = getY() - height / 2;
            int Image_Size=width;
        switch (Mode) {
            case 0://绘制昵称和行动次序
                if(PlayerNickName[SeatNumber] != null) {



                    assert client.screen != null;
                    double scaleFactor = ((double) client.getWindow().getGuiScaledHeight() / 400 / ((double) client.screen.height / height / 6));
                    int textX = getX();
                    int textY = getY() + height / 2;

                    PoseStack matrices = graphics.pose();
                    matrices.pushPose();
                    matrices.translate(textX, textY, 0);
                    matrices.scale((float) scaleFactor, (float) scaleFactor, (float) scaleFactor);
                    graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                    RenderSystem.enableBlend();
                    int NikeNameBG = client.font.width(FormattedCharSequence.forward(PlayerNickName[SeatNumber], Style.EMPTY.withColor(0xFFFFFF).withBold(true)));
                    graphics.fill(RenderType.gui(),-(NikeNameBG+client.font.lineHeight)/2,0,(NikeNameBG+client.font.lineHeight)/2,client.font.lineHeight*3/2, 0x15BBBBBB);
                    graphics.renderOutline(-(NikeNameBG+client.font.lineHeight)/2,0,(NikeNameBG+client.font.lineHeight),client.font.lineHeight*3/2,0xFF000000);
                    graphics.drawString(client.font, FormattedCharSequence.forward(PlayerNickName[SeatNumber], Style.EMPTY.withColor(0xFFFFFF).withBold(true)), -NikeNameBG / 2, client.font.lineHeight/4, NikeNameBG, false);
                    matrices.popPose();
                    RenderSystem.disableBlend();
                }
                int Firstsum = 0;
                int Eachnum = 0;
                int FirstColor = 0x0000c8;
                int EachColor = 0xc80000;
                if(Death){
                    FirstColor = 0x3A3A3A;
                    EachColor = 0x3A3A3A;
                }
                if(FirstNightPlayerNum.get(SeatNumber)!=null) {
                    Firstsum = FirstNightPlayerNum.get(SeatNumber);
                }
                if(EachNightPlayerNum.get(SeatNumber)!=null) {
                    Eachnum = EachNightPlayerNum.get(SeatNumber);
                }
            {
                PoseStack matrices = graphics.pose();
                matrices.pushPose();
                matrices.translate(CreatX, getY(), 0);
                matrices.scale(0.65f, 0.65f, 0.65f);
                if (Firstsum != 0) {
                    graphics.drawString(client.font, FormattedCharSequence.forward(Firstsum + ".", Style.EMPTY.withColor(FirstColor).withBold(true)), -client.font.width(FormattedCharSequence.forward(Firstsum + ".", Style.EMPTY.withBold(true))) / 2 + 1, -client.font.lineHeight / 2, client.font.width(FormattedCharSequence.forward(Firstsum + ".", Style.EMPTY.withBold(true))), false);
                    graphics.drawString(client.font, FormattedCharSequence.forward(Firstsum + ".", Style.EMPTY.withColor(FirstColor).withBold(true)), -client.font.width(FormattedCharSequence.forward(Firstsum + ".", Style.EMPTY.withBold(true))) / 2 - 1, -client.font.lineHeight / 2, client.font.width(FormattedCharSequence.forward(Firstsum + ".", Style.EMPTY.withBold(true))), false);
                    graphics.drawString(client.font, FormattedCharSequence.forward(Firstsum + ".", Style.EMPTY.withColor(FirstColor).withBold(true)), -client.font.width(FormattedCharSequence.forward(Firstsum + ".", Style.EMPTY.withBold(true))) / 2, -client.font.lineHeight / 2 + 1, client.font.width(FormattedCharSequence.forward(Firstsum + ".", Style.EMPTY.withBold(true))), false);
                    graphics.drawString(client.font, FormattedCharSequence.forward(Firstsum + ".", Style.EMPTY.withColor(FirstColor).withBold(true)), -client.font.width(FormattedCharSequence.forward(Firstsum + ".", Style.EMPTY.withBold(true))) / 2, -client.font.lineHeight / 2 - 1, client.font.width(FormattedCharSequence.forward(Firstsum + ".", Style.EMPTY.withBold(true))), false);


                    graphics.drawString(client.font, FormattedCharSequence.forward(Firstsum + ".", Style.EMPTY.withColor(0xFFFFFF).withBold(true)), -client.font.width(FormattedCharSequence.forward(Firstsum + ".", Style.EMPTY.withBold(true))) / 2, -client.font.lineHeight / 2, client.font.width(FormattedCharSequence.forward(Firstsum + ".", Style.EMPTY.withColor(0xFFFFFF).withBold(true))), false);
                }
                matrices.translate(height/0.65f, 0, 0);

                if (Eachnum != 0) {
                    graphics.drawString(client.font, FormattedCharSequence.forward(Eachnum + ".", Style.EMPTY.withColor(EachColor).withBold(true)), - client.font.width(FormattedCharSequence.forward(Eachnum + ".", Style.EMPTY.withBold(true))) / 2 + 1, -client.font.lineHeight / 2, client.font.width(FormattedCharSequence.forward(Eachnum + ".", Style.EMPTY.withBold(true))), false);
                    graphics.drawString(client.font, FormattedCharSequence.forward(Eachnum + ".", Style.EMPTY.withColor(EachColor).withBold(true)), - client.font.width(FormattedCharSequence.forward(Eachnum + ".", Style.EMPTY.withBold(true))) / 2 - 1, -client.font.lineHeight / 2, client.font.width(FormattedCharSequence.forward(Eachnum + ".", Style.EMPTY.withBold(true))), false);
                    graphics.drawString(client.font, FormattedCharSequence.forward(Eachnum + ".", Style.EMPTY.withColor(EachColor).withBold(true)), - client.font.width(FormattedCharSequence.forward(Eachnum + ".", Style.EMPTY.withBold(true))) / 2, -client.font.lineHeight / 2 + 1, client.font.width(FormattedCharSequence.forward(Eachnum + ".", Style.EMPTY.withBold(true))), false);
                    graphics.drawString(client.font, FormattedCharSequence.forward(Eachnum + ".", Style.EMPTY.withColor(EachColor).withBold(true)), - client.font.width(FormattedCharSequence.forward(Eachnum + ".", Style.EMPTY.withBold(true))) / 2, -client.font.lineHeight / 2 - 1, client.font.width(FormattedCharSequence.forward(Eachnum + ".", Style.EMPTY.withBold(true))), false);


                    graphics.drawString(client.font, FormattedCharSequence.forward(Eachnum + ".", Style.EMPTY.withColor(0xFFFFFF).withBold(true)), - client.font.width(FormattedCharSequence.forward(Eachnum + ".", Style.EMPTY.withColor(0xFFFFFF).withBold(true))) / 2, -client.font.lineHeight / 2, client.font.width(FormattedCharSequence.forward(Eachnum + ".", Style.EMPTY.withColor(0xFFFFFF).withBold(true))), false);
                }
                matrices.popPose();
            }
            case 2, 4://图片内容文字内容渲染
                if (Job != null) {
                    if (ImagePath != null) {
                        DynamicTexture image = texture();
                        if (image != null && image.getPixels() != null) {
                            RenderSystem.setShader(GameRenderer::getPositionTexShader);
                            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                            RenderSystem.setShaderTexture(0, image.getId());
                            RenderSystem.enableBlend();
                            newJobImageLoad.drawTexture(graphics, CreatX, CreatY, Image_Size, Image_Size, 0, 0, Image_Size, Image_Size, Image_Size, Image_Size);
                            RenderSystem.disableBlend();
                        }
                    }

                    assert client.screen != null;
                    double scaleFactor = ((double) client.getWindow().getGuiScaledHeight() / 400 / ((double) client.screen.height / height / 8));
                    int textX = getX();
                    int textY = getY() + height / 2 - height / 20;
                    PoseStack matrices = graphics.pose();
                    matrices.pushPose();
                    matrices.translate(textX, textY, 0);
                    matrices.scale((float) scaleFactor, (float) scaleFactor, (float) scaleFactor);
                    graphics.setColor(1.0f - bgOpacity, 1.0f - bgOpacity, 1.0f - bgOpacity, 1.0f - bgOpacity);
                    graphics.drawString(client.font, FormattedCharSequence.forward(Job, Style.EMPTY.withColor(0x000000)), -client.font.width(Job) / 2, -client.font.lineHeight, client.font.width(Job), false);
                    graphics.setColor(bgOpacity, bgOpacity, bgOpacity, bgOpacity);
                    graphics.drawString(client.font, FormattedCharSequence.forward(Job, Style.EMPTY.withColor(0xFFFFFF)), -client.font.width(Job) / 2, -client.font.lineHeight, client.font.width(Job), false);
                    matrices.popPose();
                }
                break;
            case 1, 3:
                if (Info == null && ImagePath == null) {
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(bgOpacity, bgOpacity, bgOpacity, bgOpacity);
                    RenderSystem.enableBlend();
                    RenderSystem.depthMask(false);
                    RenderSystem._setShaderTexture(0, new ResourceLocation("grimoire", "textures/gui/sign.png"));
                    newJobImageLoad.drawTexture(graphics, getX() - width / 2, getY() - width / 2, width, height, 0, 0, width, height, width, height);
                    RenderSystem._setShaderTexture(0, new ResourceLocation("grimoire", "textures/gui/plus.png"));
                    newJobImageLoad.drawTexture(graphics, getX() - width / 2, getY() - width / 2, width, height, 0, 0, width, height, width, height);
                    RenderSystem.disableBlend();
                } else {

                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                    RenderSystem.enableBlend();
                    RenderSystem.enableDepthTest();
                    RenderSystem.depthFunc(515);
                    RenderSystem._setShaderTexture(0, new ResourceLocation("grimoire", "textures/gui/sign.png"));
                    newJobImageLoad.drawTexture(graphics, getX() - width / 2, getY() - width / 2, width, height, 0, 0, width, height, width, height);
                    RenderSystem.disableDepthTest();
                    RenderSystem.disableBlend();

                    if (Info != null) {
                        boolean SpecialInfo = Info.equals("善良") || Info.equals("邪恶") || Info.equals("自定义");
                        if (SpecialInfo || ImagePath != null) {
                            RenderSystem.setShader(GameRenderer::getPositionTexShader);
                            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                            if (!SpecialInfo) {
                                DynamicTexture image = texture();
                                if (image != null && image.getPixels() != null) {
                                    RenderSystem.setShaderTexture(0, image.getId());
                                }
                            } else {
                                switch (Info) {
                                    case "善良":
                                        RenderSystem._setShaderTexture(0, new ResourceLocation("grimoire", "textures/gui/good.png"));
                                        break;
                                    case "邪恶":
                                        RenderSystem._setShaderTexture(0, new ResourceLocation("grimoire", "textures/gui/evil.png"));
                                        break;
                                    case "自定义":
                                        RenderSystem._setShaderTexture(0, new ResourceLocation("grimoire", "textures/gui/custom.png"));
                                        break;
                                }
                            }
                            RenderSystem.enableBlend();
                            newJobImageLoad.drawTexture(graphics, CreatX, CreatY, Image_Size, Image_Size, 0, 0, Image_Size, Image_Size, Image_Size, Image_Size);
                            RenderSystem.disableBlend();
                            graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);

                            double scaleFactor = 0;
                            if (client.screen != null) {
                                scaleFactor = ((double) client.getWindow().getGuiScaledHeight() / 500 / ((double) client.screen.height / height * 3 / 40));
                            }
                            int textX = getX();
                            int textY = (int) (getY() + height / 2 - height / 20);
                            PoseStack matrices = graphics.pose();
                            matrices.pushPose();
                            matrices.translate(textX, textY, 0);
                            matrices.scale((float) scaleFactor, (float) scaleFactor, (float) scaleFactor);
                            graphics.drawString(client.font, FormattedCharSequence.forward(Info, Style.EMPTY.withColor(0x000000).withBold(true)), -client.font.width(Info) / 2, -client.font.lineHeight, client.font.width(Info), false);
                            matrices.popPose();


                        } else {
                            double scaleFactor = 0;
                            if (client.screen != null) {
                                scaleFactor = ((double) client.getWindow().getGuiScaledHeight() / 500 / ((double) client.screen.height / height * 3 / 40));
                            }
                            int textX = getX();
                            int textY = getY();
                            PoseStack matrices = graphics.pose();
                            matrices.pushPose();
                            matrices.translate(textX, textY, 1);
                            matrices.scale((float) scaleFactor, (float) scaleFactor, (float) scaleFactor);
                            getInfosplit();
                            if (Info_split != null) {
                                for (int i = 0; i < Info_split.size(); i++) {
                                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                                    graphics.drawString(client.font, Info_split2.get(i), -client.font.width(Info_split2.get(i)) / 2+1, -(Info_split2.size() * client.font.lineHeight) / 2 + i * client.font.lineHeight, client.font.width(Info_split2.get(i)), false);
                                    graphics.drawString(client.font, Info_split2.get(i), -client.font.width(Info_split2.get(i)) / 2-1, -(Info_split2.size() * client.font.lineHeight) / 2 + i * client.font.lineHeight, client.font.width(Info_split2.get(i)), false);
                                    graphics.drawString(client.font, Info_split2.get(i), -client.font.width(Info_split2.get(i)) / 2, -(Info_split2.size() * client.font.lineHeight) / 2 + i * client.font.lineHeight+1, client.font.width(Info_split2.get(i)), false);
                                    graphics.drawString(client.font, Info_split2.get(i), -client.font.width(Info_split2.get(i)) / 2, -(Info_split2.size() * client.font.lineHeight) / 2 + i * client.font.lineHeight-1, client.font.width(Info_split2.get(i)), false);

                                    graphics.drawString(client.font, Info_split.get(i), -client.font.width(Info_split.get(i)) / 2, -(Info_split.size() * client.font.lineHeight) / 2 + i * client.font.lineHeight, client.font.width(Info_split.get(i)), false);

                                }
                            }

                            matrices.popPose();
                        }
                    }

                    if (Mode == 1) {
                        RenderSystem.setShader(GameRenderer::getPositionTexShader);
                        RenderSystem.setShaderColor(bgOpacity, bgOpacity, bgOpacity, bgOpacity);
                        RenderSystem.depthMask(false);
                        RenderSystem.enableBlend();
                        RenderSystem._setShaderTexture(0, new ResourceLocation("grimoire", "textures/gui/x.png"));
                        newJobImageLoad.drawTexture(graphics, getX() - width / 2, getY() - width / 2, width, height, 0, 0, width, height, width, height);
                        RenderSystem.disableBlend();
                    }

                }
                break;
            case 5://图片内容文字内容渲染
                if (Job != null) {
                    if (ImagePath != null) {
                        DynamicTexture image = texture();
                        if (image != null && image.getPixels() != null) {
                            RenderSystem.setShader(GameRenderer::getPositionTexShader);
                            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                            RenderSystem.setShaderTexture(0, image.getId());
                            RenderSystem.enableBlend();
                            newJobImageLoad.drawTexture(graphics, CreatX, CreatY, Image_Size, Image_Size, 0, 0, Image_Size, Image_Size, Image_Size, Image_Size);
                            RenderSystem.disableBlend();
                        }
                    }

                    double scaleFactor;
                    if (client.screen!= null) {
                        scaleFactor = ((double) client.getWindow().getGuiScaledHeight() / 400 / ((double) client.screen.height / height / 8));
                    } else {
                        scaleFactor = 1.0f;
                    }
                    int textX = getX();
                    int textY = getY() + height / 2 - height / 20;
                    PoseStack matrices = graphics.pose();
                    matrices.pushPose();
                    matrices.translate(textX, textY, 0);
                    matrices.scale((float) scaleFactor, (float) scaleFactor, (float) scaleFactor);
                    graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                    graphics.drawString(client.font, Component.literal(Job), -client.font.width(Job) / 2, -client.font.lineHeight, client.font.width(Job), false);
                    matrices.popPose();
                }
                break;
        }
        switch (Mode) {
            case 0,5://左上角标号 和死亡标记 渲染
            graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            if(SeatNumber<20) {
                graphics.drawString(client.font, FormattedCharSequence.forward(String.valueOf(SeatNumber + 1), Style.EMPTY.withColor(0xFFFFFF).withBold(true)), CreatX - width / 20 - client.font.width(String.valueOf(SeatNumber + 1)) / 2, CreatY - height / 20 - client.font.lineHeight / 2, (height - client.font.width(String.valueOf(SeatNumber + 1))), false);
            }
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem._setShaderTexture(0, new ResourceLocation("grimoire", "textures/gui/death_mark.png"));
            RenderSystem.enableBlend();
            if (!Death) {
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, Death_bgOpacity);
                newJobImageLoad.drawTexture(graphics, getX() - height / 2, (int) (getY() - width / 2 + Death_Pos), width, height, 0, 0, width, height, width, height);
            } else {
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                newJobImageLoad.drawTexture(graphics, getX() - height / 2, getY() - width / 2, width, height, 0, 0, width, height, width, height);
            }
            RenderSystem.disableBlend();
                break;
        }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void renderIntroduction(GuiGraphics graphics, int mouseX, int mouseY) {
        switch (Mode) {
            case 0,2,4://身份说明渲染
            if (Info != null && bgOpacity > 0 ) {
                getInfosplit();
                if(Info_split!=null) {
                int Alpha = (int) (255 * bgOpacity);
                int Line_Width = client.font.lineHeight / 4;
                int Introduction_Box_Out_width = width * 3 + client.font.lineHeight;
                int Introduction_Box_Out_height = (Info_split.size() + 1) * client.font.lineHeight;
                int Introduction_Box_In_width = Introduction_Box_Out_width - Line_Width * 2;
                int Introduction_Box_In_height = Introduction_Box_Out_height - Line_Width * 2;
                int Introduction_Box_Out_CreatX;
                int Introduction_Box_Out_CreatY = 0;
                if (mouseX <= mainScreen.width / 2) {
                    Introduction_Box_Out_CreatX = mouseX + client.font.lineHeight;
                } else {
                    Introduction_Box_Out_CreatX = mouseX - Introduction_Box_Out_width - client.font.lineHeight;
                }
                if (mouseY > Introduction_Box_Out_height / 2 && mouseY < mainScreen.height - Introduction_Box_Out_height / 2) {
                    Introduction_Box_Out_CreatY = mouseY - Introduction_Box_Out_height / 2;
                } else if (mouseY >= mainScreen.height - Introduction_Box_Out_height / 2) {
                    Introduction_Box_Out_CreatY = mainScreen.height - Introduction_Box_Out_height;
                }
                int Introduction_Box_In_CreatX = Introduction_Box_Out_CreatX + Line_Width;
                int Introduction_Box_In_CreatY = Introduction_Box_Out_CreatY + Line_Width;

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                graphics.fill(RenderType.gui(), Introduction_Box_Out_CreatX, Introduction_Box_Out_CreatY, Introduction_Box_Out_CreatX + Introduction_Box_Out_width, Introduction_Box_Out_CreatY + Line_Width, 6, FastColor.ARGB32.color(Alpha, 0, 0, 0));
                graphics.fill(RenderType.gui(), Introduction_Box_Out_CreatX, Introduction_Box_Out_CreatY, Introduction_Box_Out_CreatX + Line_Width, Introduction_Box_Out_CreatY + Introduction_Box_Out_height, 6, FastColor.ARGB32.color(Alpha, 0, 0, 0));
                graphics.fill(RenderType.gui(), Introduction_Box_Out_CreatX + Introduction_Box_Out_width - Line_Width, Introduction_Box_Out_CreatY, Introduction_Box_Out_CreatX + Introduction_Box_Out_width, Introduction_Box_Out_CreatY + Introduction_Box_Out_height, 6, FastColor.ARGB32.color(Alpha, 0, 0, 0));
                graphics.fill(RenderType.gui(), Introduction_Box_Out_CreatX, Introduction_Box_Out_CreatY + Introduction_Box_Out_height - Line_Width, Introduction_Box_Out_CreatX + Introduction_Box_Out_width, Introduction_Box_Out_CreatY + Introduction_Box_Out_height, 6, FastColor.ARGB32.color(Alpha, 0, 0, 0));
                graphics.fill(RenderType.gui(), Introduction_Box_In_CreatX, Introduction_Box_In_CreatY, Introduction_Box_In_CreatX + Introduction_Box_In_width, Introduction_Box_In_CreatY + Introduction_Box_In_height, 0, FastColor.ARGB32.color((int) (Alpha * 0.9), 135, 135, 135));

                for (int i = 0; i < Info_split.size(); i++) {
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(bgOpacity, bgOpacity, bgOpacity, bgOpacity);
                    graphics.drawString(client.font, Info_split.get(i), Introduction_Box_Out_CreatX + client.font.lineHeight / 2, (int) (Introduction_Box_Out_CreatY + client.font.lineHeight * (i + 0.5)), width * 3, false);
                }
            }
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                break;
            }
        }
    }

    private void renderBackground(GuiGraphics graphics) {
        int CreatX = getX() - height / 2;
        int CreatY = getY() - height / 2;
        switch (Mode) {
            case 0, 2,4,5://背景图标渲染
                graphics.blitInscribed(new ResourceLocation("grimoire", "textures/gui/seat.png"), CreatX, CreatY, width, height, width, height);
                break;
            case 1:
                graphics.blitInscribed(new ResourceLocation("grimoire", "textures/gui/infobg.png"), CreatX, CreatY, width, height, width, height);
                break;
            case 3:
                graphics.blitInscribed(new ResourceLocation("grimoire", "textures/gui/sign.png"), CreatX, CreatY, width, height, width, height);
                break;
        }
    }

    public void ChangY(int Chang){
        int afterY = getY()+Chang;
        setY(afterY);
    }






    public void updateHoverState(int mouseX, int mouseY, boolean updateHoverState) {
        this.isHovered = updateHoverState && (mouseX >= this.getX() - this.width/2 && mouseY >= this.getY()-this.width/2 && mouseX < this.getX() + this.width/2 && mouseY < this.getY() + this.height/2);
        int maxOpacity = 60;
        int maxPos = 0;
        int maxItOpacity = 100;
        switch (Mode) {
            case 0://鼠标悬停
        if (isHovered && mouseX >= this.getX()) {
            if (Death_bgOpacity < maxOpacity / 100f) {
                Death_bgOpacity = Math.min(maxOpacity / 100f, Death_bgOpacity + 0.05F);
            }
            if(Death_Pos < maxPos){
                Death_Pos = Math.min(maxPos, Death_Pos + 0.5);
            }
        } else {
            if (Death_bgOpacity > 0) {
                Death_bgOpacity = Math.max(0, Death_bgOpacity - 0.05F);
            }
            if(Death_Pos > -6){
                Death_Pos = Math.min(maxPos, Death_Pos - 0.5);
            }
        }

        if (isHovered && mouseX < this.getX()) {
            if (bgOpacity < maxItOpacity / 100f) {
                bgOpacity = Math.min(maxItOpacity / 100f, bgOpacity + 0.05F);
            }
        } else {
            if (bgOpacity > 0) {
                bgOpacity = Math.max(0, bgOpacity - 0.05F);
            }
        }
                break;
            case 1,2,3,4:
                if (isHovered) {
                    if (bgOpacity < maxItOpacity / 100f) {
                        bgOpacity = Math.min(maxItOpacity / 100f, bgOpacity + 0.05F);
                    }
                } else {
                    if (bgOpacity > 0) {
                        bgOpacity = Math.max(0, bgOpacity - 0.05F);
                    }
                }
                break;
        }
    }

    private CompletableFuture<NativeImage> getImage(File file) {
        return CompletableFuture.supplyAsync(() -> {
            try (InputStream inputStream = new FileInputStream(file)) {
                return NativeImage.read(inputStream);
            } catch (Exception e) {
                LOGGER.error("Failed to load screenshot: {}", file.getName(), e);
            }
            return null;
        }, Util.backgroundExecutor());
    }

    @Nullable
    public DynamicTexture texture() {
        if (texture != null) {
            return texture;
        }
        if (image == null) {
            image = getImage(ImagePath);
        }
        if (image.isDone()) {
            return texture = new DynamicTexture(image.join());
        }
        return null;
    }

    @Override
    public void close() {
        if (texture != null) {
            texture.close(); // Also closes the image
        } else if(image != null) {
            image.thenAcceptAsync(image -> {
                if (image != null) {
                    image.close();
                }
            }, this.client);
        }
        image = null;
        texture = null;
    }
}
