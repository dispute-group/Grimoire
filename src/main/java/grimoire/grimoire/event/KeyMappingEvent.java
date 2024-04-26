package grimoire.grimoire.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyMappingEvent {
    public static final String KEY_OPEN_GRIMOIRE = "key_open_grimoireScreens";
    public static final String KEY_CATEGORY_TUTORIAL = "key_category_tutorial";
    public static final String KEY_OPEN_INTRODUCE = "key_open_introduce";
    public static final KeyMapping Open_Grimoire = new KeyMapping(KEY_OPEN_GRIMOIRE, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R,KEY_CATEGORY_TUTORIAL);
    public static final KeyMapping Open_Introduce = new KeyMapping(KEY_OPEN_INTRODUCE, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_T,KEY_CATEGORY_TUTORIAL);
    }
