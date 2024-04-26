package grimoire.grimoire.gui.widgets;

import grimoire.grimoire.data.LoadFile;
import grimoire.grimoire.gui.GrimoireSelectionScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.File;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class GrimoireList extends ObjectSelectionList<GrimoireEntry> {

    private final GrimoireSelectionScreens parentScreen;

    public GrimoireList(GrimoireSelectionScreens parentScreen,Minecraft mc, int width, int height, int top, int bottom, int slotHeight) {
        super(mc, width, height, top, bottom, slotHeight);
        this.parentScreen = parentScreen;
        refreshList();
    }

    public GrimoireSelectionScreens getParentScreen() {
        return this.parentScreen;
    }

    // 设置滚动的条的位置
    @Override
    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 20;
    }

    // 用于判断是否选中
    public boolean hasSelection() {
        return getSelected() != null;
    }

    // 宽度
    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 50;
    }
    // 获得最低端
    @Override
    protected int getRowBottom(int itemIndex) {
        return getRowTop(itemIndex) + itemHeight;
    }
    // 判断index是否选中
    @Override
    protected boolean isSelectedItem(int slotIndex) {
        return slotIndex >= 0 && slotIndex < children().size() ? children().get(slotIndex).equals(getSelected()) : false;
    }
    // 渲染list
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderList(guiGraphics, mouseX, mouseY, partialTicks);
    }
    // 渲染list
    @Override
    protected void renderList(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        for (int i = 0; i < getItemCount(); ++i) {
            int top = getRowTop(i);
            int bottom = getRowBottom(i);
            if (bottom >= y0 && top <= y1) {
                // 获得 第i个选项
                GrimoireEntry entry = getEntry(i);
                // 是否被选中
                if (isSelectedItem(i)) {
                    // 若被选中，则渲染颜色加深
                    final int insideLeft = x0 + width / 2 - getRowWidth() / 2 + 2;
                    guiGraphics.fill(insideLeft - 4, top - 4, insideLeft + getRowWidth() + 4, top + itemHeight, 255 / 2 << 24);
                }
                // 否则正常渲染
                entry.render(guiGraphics, i, top, getRowLeft(), getRowWidth(), itemHeight - 4, mouseX, mouseY, isMouseOver((double) mouseX, (double) mouseY) && Objects.equals(getEntryAtPosition((double) mouseX, (double) mouseY), entry), partialTicks);
            }
        }

        if (getMaxScroll() > 0) {
            int left = getScrollbarPosition();
            int right = left + 6;
            int height = (int) ((float) ((y1 - y0) * (y1 - y0)) / (float) getMaxPosition());
            height = Mth.clamp(height, 32, y1 - y0 - 8);
            int top = (int) getScrollAmount() * (y1 - y0 - height) / getMaxScroll() + y0;
            if (top < y0) {
                top = y0;
            }

            guiGraphics.fill(left, y0, right, y1, (int) (2.35F * 255.0F) / 2 << 24);
            guiGraphics.fill(left, top, right, top + height, (int) (1.9F * 255.0F) / 2 << 24);
        }
    }

    public void refreshList() {
        clearEntries();
        for (File job : LoadFile.Script) {
            addEntry(new GrimoireEntry(this, job.getName()));
        }
        // 打开Screen之前将选中的选项清空
        //selectBiome(null);

    }
    public void selectGrimoire(GrimoireEntry entry) {
        setSelected(entry);
//        GrimoireScreens.selectGrimoire(entry);
        //ExampleMod.LOGGER.info("===============ListClick");
    }


}
