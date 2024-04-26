package grimoire.grimoire.gui.widgets;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.*;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class newJobImageLoad {
    private final File JobImageFile;
    private CompletableFuture<NativeImage> JobImage;
    @Nullable
    private DynamicTexture texture;

    public newJobImageLoad(File JobImageFile) {
        this.JobImageFile = JobImageFile;
        this.JobImage = getImage(JobImageFile);

    }

    public CompletableFuture<NativeImage> getImage(File file) {
        return CompletableFuture.supplyAsync(() -> {
            try (InputStream inputStream = new FileInputStream(file)) {
                return NativeImage.read(inputStream);
            } catch (Exception e) {
                LogUtils.getLogger().error("Failed to load screenshot: {}", file.getName(), e);
            }
            return null;
        }, Util.backgroundExecutor());
    }

    public int  imageId() {
        DynamicTexture texture = texture();
        return texture != null ? texture.getId() : 0;
    }
    public DynamicTexture texture() {
        if (texture != null) {
            return texture;
        }
        if (JobImage == null) {
            JobImage = getImage(JobImageFile);
        }
        if (JobImage.isDone()) {
            return texture = new DynamicTexture(JobImage.join());
        }
        return null;
    }





    public static void drawTexture(GuiGraphics graphics, int x, int y, int width, int height, int u, int v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        int x2 = x + width;
        int y2 = y + height;
        float u1 = u / (float) textureWidth;
        float u2 = (u + (float) regionWidth) / (float) textureWidth;
        float v1 = v / (float) textureHeight;
        float v2 = (v + (float) regionHeight) / (float) textureHeight;

        Matrix4f matrix4f = graphics.pose().last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix4f, x, y, 0).uv(u1, v1).endVertex();
        bufferBuilder.vertex(matrix4f, x, y2, 0).uv(u1, v2).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y2, 0).uv(u2, v2).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y, 0).uv(u2, v1).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
    }
}