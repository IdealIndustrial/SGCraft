//------------------------------------------------------------------------------------------------
//
//   SG Craft - Power unit gui screen
//
//------------------------------------------------------------------------------------------------

package gcewing.sg;

import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;

import net.minecraft.client.gui.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;

public class PowerScreen extends BaseGuiContainer {

    final static int guiWidth = 128;
    final static int guiHeight = 64;
    //final static String screenTitle = "IC2 SGPU";

    PowerTE te;
    
    public static PowerScreen create(EntityPlayer player, World world, int x, int y, int z) {
        PowerContainer container = PowerContainer.create(player, world, x, y, z);
        if (container != null)
            return new PowerScreen(container);
        else
            return null;
    }
    
    public PowerScreen(PowerContainer container) {
        super(container, guiWidth, guiHeight);
        this.te = container.te;
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void drawBackgroundLayer() {
        bindTexture(SGCraft.mod.resourceLocation("textures/gui/power_gui.png"), 128, 64);
        drawTexturedRect(0, 0, guiWidth, guiHeight, 0, 0);
        int cx = xSize / 2;
        //textColor = 0x004c66;
        drawCenteredString(te.getScreenTitle(), cx, 8);
        drawRightAlignedString(te.getUnitName(), 68, 28);
        drawRightAlignedString(String.format("%,.0f", te.energyBuffer), 123, 28);
        drawRightAlignedString("Max", 68, 42);
        drawRightAlignedString(String.format("%,.0f", te.energyMax), 123, 42);
        drawPowerGauge();
    }
    
    void drawPowerGauge() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        setColor(1, 0, 0);
        drawRect(19, 27, 25 * te.energyBuffer / te.energyMax, 10);
    }

}
