package grimoire.grimoire.gui;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import grimoire.grimoire.data.GrimoireData;
import grimoire.grimoire.data.LoadFile;
import grimoire.grimoire.gui.widgets.newJobImageLoad;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import static com.mojang.text2speech.Narrator.LOGGER;
import static grimoire.grimoire.data.GrimoireData.MyselfSeatNum;

public  class AbsenceAndMyselfShow  {
    public static CompletableFuture<NativeImage>[] Allimage = new CompletableFuture[4];
    public static DynamicTexture[] texture = new DynamicTexture[4];

    public static final IGuiOverlay AbsenceAndMyself =new IGuiOverlay() {
        @Nullable

        private String[] Jobs = new String[4];
        private File[] ImagePaths = new File[4];

        public static void clear(){
            Allimage = new CompletableFuture[4];
        }
        private DynamicTexture  texture(int i,File ImagePath) {
            if (texture[i] != null) {
                return texture[i];
            }
            if (Allimage[i]  == null) {
                Allimage[i] = getImage(ImagePath);
            }
            if (Allimage[i] .isDone()) {
                return texture[i] = new DynamicTexture(Allimage[i].join());

            }
            return null;
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


        @Override
        public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
            int Image_Size = height / 8;
            int spacing = 4;
            int absence_Creat_X = width/2-gui.getMinecraft().font.lineHeight*18-(height / 8+4)-Image_Size/2;
            int absence_Creat_Y = height-height / 16-spacing-Image_Size/2;
            int my_Seat_Num = MyselfSeatNum;
            int my_Seat_Creat_X =width/2+gui.getMinecraft().font.lineHeight*12-Image_Size/2;

            int my_Seat_Creat_Y = height-height / 16-4-Image_Size/2;
            for (int i = 0; i < Jobs.length-1; i++) {
                if(GrimoireData.PlayerJob[i + 20]!=null && LoadFile.AllJobPath!=null) {
                    Jobs[i] = GrimoireData.PlayerJob[i + 20];
                    ImagePaths[i] = LoadFile.AllJobPath.get(Jobs[i]);
                }else{
                    Jobs[i]=null;
                    ImagePaths[i]=null;
                }
            }
            if(GrimoireData.PlayerJob[my_Seat_Num]!=null && LoadFile.AllJobPath!=null) {
                Jobs[Jobs.length - 1] = GrimoireData.PlayerJob[my_Seat_Num];
                ImagePaths[Jobs.length - 1] = LoadFile.AllJobPath.get(Jobs[Jobs.length - 1]);
            }else{
                Jobs[Jobs.length - 1]=null;
                ImagePaths[Jobs.length - 1]=null;
            }




            double scaleFactor = ((double) gui.getMinecraft().getWindow().getGuiScaledHeight() / 400 / ((double) height / Image_Size / 8));
            if(GrimoireData.isDisplayAbsentIdentity) {
                for (int i = 0; i < 3; i++) {
                    guiGraphics.blitInscribed(new ResourceLocation("grimoire", "textures/gui/seat.png"), absence_Creat_X + i * (Image_Size + spacing), absence_Creat_Y, Image_Size, Image_Size, Image_Size, Image_Size);
                    if (Jobs[i] != null) {
                        if (ImagePaths[i] != null) {
                            DynamicTexture image = texture(i, ImagePaths[i]);
                            if (image != null && image.getPixels() != null) {
                                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                                RenderSystem.setShaderTexture(0, image.getId());
                                RenderSystem.enableBlend();
                                newJobImageLoad.drawTexture(guiGraphics, absence_Creat_X + i * (Image_Size + spacing), absence_Creat_Y, Image_Size, Image_Size, 0, 0, Image_Size, Image_Size, Image_Size, Image_Size);
                                RenderSystem.disableBlend();
                            }
                        }
                        if (gui.getMinecraft() != null) {
                            int textX = absence_Creat_X + i * (Image_Size + spacing) + Image_Size / 2;
                            int textY = absence_Creat_Y + Image_Size - Image_Size / 20;
                            PoseStack matrices = guiGraphics.pose();
                            matrices.pushPose();
                            matrices.translate(textX, textY, 0);
                            matrices.scale((float) scaleFactor, (float) scaleFactor, (float) scaleFactor);
                            guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                            guiGraphics.drawString(gui.getMinecraft().font, Component.literal(Jobs[i]), -gui.getMinecraft().font.width(Jobs[i]) / 2, -gui.getMinecraft().font.lineHeight, gui.getMinecraft().font.width(Jobs[i]), false);
                            matrices.popPose();
                        }
                    }
                }
            }

            if(GrimoireData.isDisplayMySelfSeat) {
                guiGraphics.blitInscribed(new ResourceLocation("grimoire", "textures/gui/seat.png"), my_Seat_Creat_X, my_Seat_Creat_Y, Image_Size, Image_Size, Image_Size, Image_Size);
                if (Jobs[Jobs.length - 1] != null) {
                    if (ImagePaths[ImagePaths.length - 1] != null) {
                        DynamicTexture image = texture(Jobs.length - 1, ImagePaths[ImagePaths.length - 1]);

                        if (image != null && image.getPixels() != null) {

                            RenderSystem.setShader(GameRenderer::getPositionTexShader);
                            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                            RenderSystem.setShaderTexture(0, image.getId());
                            RenderSystem.enableBlend();
                            newJobImageLoad.drawTexture(guiGraphics, my_Seat_Creat_X, my_Seat_Creat_Y, Image_Size, Image_Size, 0, 0, Image_Size, Image_Size, Image_Size, Image_Size);
                            RenderSystem.disableBlend();
                        }
                    }
                    if (gui.getMinecraft() != null) {
                        int textX = my_Seat_Creat_X + Image_Size / 2;
                        int textY = my_Seat_Creat_Y + Image_Size - Image_Size / 20;
                        PoseStack matrices = guiGraphics.pose();
                        matrices.pushPose();
                        matrices.translate(textX, textY, 0);
                        matrices.scale((float) scaleFactor, (float) scaleFactor, (float) scaleFactor);
                        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                        guiGraphics.drawString(gui.getMinecraft().font, Component.literal(Jobs[Jobs.length - 1]), -gui.getMinecraft().font.width(Jobs[Jobs.length - 1]) / 2, -gui.getMinecraft().font.lineHeight, gui.getMinecraft().font.width(Jobs[Jobs.length - 1]), false);
                        matrices.popPose();
                    }
                }
                guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                guiGraphics.drawString(gui.getMinecraft().font, FormattedCharSequence.forward(String.valueOf(my_Seat_Num + 1), Style.EMPTY.withColor(0xFFFFFF).withBold(true)), my_Seat_Creat_X - Image_Size / 20 - gui.getMinecraft().font.width(String.valueOf(my_Seat_Num + 1)) / 2, my_Seat_Creat_Y - Image_Size / 20 - gui.getMinecraft().font.lineHeight / 2, gui.getMinecraft().font.width(String.valueOf(my_Seat_Num + 1)), false);
                if (GrimoireData.PlayerDeath[my_Seat_Num]) {
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem._setShaderTexture(0, new ResourceLocation("grimoire", "textures/gui/death_mark.png"));
                    RenderSystem.enableBlend();
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                    newJobImageLoad.drawTexture(guiGraphics, my_Seat_Creat_X, my_Seat_Creat_Y, Image_Size, Image_Size, 0, 0, Image_Size, Image_Size, Image_Size, Image_Size);
                    RenderSystem.disableBlend();
                }
            }
            }
    };
};
