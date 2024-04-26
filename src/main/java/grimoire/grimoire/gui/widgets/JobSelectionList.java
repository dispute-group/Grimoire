package grimoire.grimoire.gui.widgets;

import grimoire.grimoire.gui.ScreensPlus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntUnaryOperator;

import static grimoire.grimoire.data.LoadFile.*;

public class JobSelectionList extends AbstractContainerEventHandler implements Renderable, NarratableEntry {
    private final ScreensPlus mainScreen;
    private final Minecraft client;
    private final int x,y;
    private final List<GrimoireAllWidgets> GrimoireWidgets = new ArrayList<>();
    private final List<GuiEventListener> elements = new ArrayList<>();
    private final Scrollbar scrollbar = new Scrollbar();

    private int width, height;
    private int scrollY;
    private int scrollSpeedFactor;
    private int JobImagePerRow;
    private int spacing, childSize;


    public JobSelectionList(ScreensPlus mainScreen, int x, int y, int width, int height) {
        this.mainScreen = mainScreen;
        this.client = mainScreen.client();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scrollSpeedFactor = 16;
        this.JobImagePerRow = 9;
        updateVariables();
    }


    public void init() {
        clearChildren();
        ArrayList<String> AllJobName = new ArrayList<>();
        for(int i = 2; i < AllName.size(); i++){
            ArrayList<String> A1 = AllName.get(i);
            AllJobName.addAll(A1);
        }
        updateVariables();
        final int maxXOff = JobImagePerRow - 1;
        int childX = x + spacing;
        int childY = y + spacing;
        int xOff = 0;

        for (String JobName : AllJobName) {
            GrimoireAllWidgets widget = new GrimoireAllWidgets(mainScreen, childX, childY, childSize, childSize,0,JobName,AllJobPath.get(JobName),AllIntroduction.get(JobName));
                this.GrimoireWidgets.add(widget);
                this.elements.add(widget);

                if (xOff == maxXOff) {
                    xOff = 0;
                    childX = x + spacing;
                    childY += childSize + spacing;
                } else {
                    xOff++;
                    childX += childSize + spacing;
                }
        }
        scrollbar.repositionScrollbar(x, y, width, height, spacing, getTotalHeightOfChildren());
    }




    private void updateVariables() {
        final int scrollbarWidth = 6;
        final int scrollbarSpacing = 2;
        spacing = (width-childSize*JobImagePerRow)/(JobImagePerRow+1);
        if (client.screen != null) {
            childSize = client.screen.height / 8;
        }
    }

    private void clearChildren() {
        close();
        GrimoireWidgets.clear();
        elements.clear();
    }
    public void close() {
        GrimoireWidgets.forEach(GrimoireAllWidgets::close);
    }
    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta, boolean updateHoverState) {
//        graphics.fill(x, y, x + width, y + height, FastColor.ARGB32.color((int) (0.7f * 255), 0, 0, 0));
        if (GrimoireWidgets.isEmpty()) {
            graphics.drawCenteredString(client.font, "请先选择剧本", (x + width) / 2, (y + height + 8) / 2, 0xFFFFFF);
        }
        for (GrimoireAllWidgets screenshotWidget : GrimoireWidgets) {
//            screenshotWidget.updateY(scrollY);
            int viewportY = y + spacing;
            int viewportBottom = y + height - spacing;
            screenshotWidget.updateHoverState(mouseX, mouseY,/* viewportY, viewportBottom, */updateHoverState);

            // skips rendering the widget if it is not at all in the render area
            if (screenshotWidget.getY() + screenshotWidget.getHeight() < y || screenshotWidget.getY() > y + height) {
                continue;
            }
            screenshotWidget.render(graphics);

        }
        for (GrimoireAllWidgets screenshotWidget : GrimoireWidgets) {
            screenshotWidget.renderIntroduction(graphics, mouseX, mouseY);
        }
        if (canScroll()) {
            scrollbar.render(graphics, mouseX, mouseY, scrollY, scrollbarClicked);
        }
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return elements;
    }

    private boolean canScroll() {
        final int totalHeightOfTheChildrens = getTotalHeightOfChildren();
        final int viewHeight = height - 2 * spacing;

        return totalHeightOfTheChildrens > viewHeight;
    }

    private boolean canScrollDown() {
        final int totalHeightOfTheChildrens = getTotalHeightOfChildren();
        final int viewHeight = height - 2 * spacing;
        // Maximum offset from the top
        final int leftOver = totalHeightOfTheChildrens - viewHeight;

        return scrollY < leftOver;
    }

    private int getTotalHeightOfChildren() {
        int rows = Mth.ceil(GrimoireWidgets.size() / (float) JobImagePerRow);
        return rows * childSize + spacing * (rows - 1);
    }
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (canScroll()) {
            final int scrollSpeed = Math.abs((int) (scrollSpeedFactor * (6.0f / JobImagePerRow) * amount));
            if (scrollY > 0 && amount > 0) {
                scrollY = Math.max(0, scrollY - scrollSpeed);
            }
            if (canScrollDown() && amount < 0) {
                final int totalHeightOfTheChildrens = getTotalHeightOfChildren();
                final int viewHeight = height - 2 * spacing;
                // Maximum offset from the top
                final int leftOver = totalHeightOfTheChildrens - viewHeight;

                scrollY = Math.min(leftOver, scrollY + scrollSpeed);
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    private boolean scrollbarClicked;

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        scrollbarClicked = false;
        if(canScroll() && scrollbar.mouseClicked(mouseX, mouseY, button, scrollY)) {
            scrollbarClicked = true;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        scrollbarClicked = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(scrollbarClicked && canScroll()) {
            final int totalHeightOfTheChildrens = getTotalHeightOfChildren();
            int scrollDelta = scrollbar.getScrollOffsetDelta(deltaY, totalHeightOfTheChildrens);
            if (scrollY > 0 && scrollDelta > 0) {
                scrollY = Math.max(0, scrollY - scrollDelta);
            }
            if (canScrollDown() && scrollDelta < 0) {
                final int viewHeight = height - 2 * spacing;
                // Maximum offset from the top
                final int leftOver = totalHeightOfTheChildrens - viewHeight;

                scrollY = Math.min(leftOver, scrollY - scrollDelta);
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput builder) {
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    private static class Scrollbar {
        private final int spacing = 2;
        private final int width = 6;
        @SuppressWarnings("FieldCanBeLocal")
        private final int trackWidth = 2;
        private int x, height;
        private int trackX, trackY, trackHeight;
        private IntUnaryOperator scrollbarYGetter;

        void repositionScrollbar(int listX, int listY, int listWith, int listHeight, int listSpacing, int totalHeightOfTheChildrens) {
            this.x = listX + listWith - spacing - width;
            this.trackX = x + spacing;
            this.trackY = listY + listSpacing;
            this.trackHeight = listHeight - 2 * listSpacing;
            // Takes into account the fact that the scrollbar is offset from the track
            int scrollbarSpacedTrackHeight = trackHeight + 2 * spacing;

            this.scrollbarYGetter = scrollOffset -> Mth.ceil(scrollOffset * scrollbarSpacedTrackHeight / (float) totalHeightOfTheChildrens) + listY + spacing;
            this.height = (trackHeight * scrollbarSpacedTrackHeight) / totalHeightOfTheChildrens;
        }

        void render(GuiGraphics graphics, double mouseX, double mouseY, int scrollOffset, boolean clicked) {
            int y = scrollbarYGetter.applyAsInt(scrollOffset);
            graphics.fill(trackX, trackY, trackX + trackWidth, trackY + trackHeight, 0xFFFFFFFF);
            graphics.fill(x, y, x + width, y + height, isHovered(mouseX, mouseY, y) || clicked ? 0xFF6D6D6D : 0xFF1E1E1E);
        }

        boolean mouseClicked(double mouseX, double mouseY, double button, int scrollOffset) {
            return button == GLFW.GLFW_MOUSE_BUTTON_LEFT && isHovered(mouseX, mouseY, scrollbarYGetter.applyAsInt(scrollOffset));
        }

        int getScrollOffsetDelta(double scrollbarDelta, double totalHeightOfTheChildrens) {
            int scrollbarSpacedTrackHeight = trackHeight + 2 * spacing;
            return Mth.ceil(-scrollbarDelta * totalHeightOfTheChildrens / (float) scrollbarSpacedTrackHeight);
        }

        private boolean isHovered(double mouseX, double mouseY, int y) {
            return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        }
    }



    }
