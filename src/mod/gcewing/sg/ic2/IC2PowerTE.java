//------------------------------------------------------------------------------------------------
//
//   SG Craft - IC2 Stargate Power Unit Tile Entity
//
//------------------------------------------------------------------------------------------------

package gcewing.sg.ic2;

import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraftforge.common.*;
import net.minecraftforge.common.util.*;

import ic2.api.energy.event.*;
import ic2.api.energy.tile.*;

import gcewing.sg.*;
import static gcewing.sg.BaseUtils.*;
import static gcewing.sg.Utils.*;

public class IC2PowerTE extends PowerTE implements IEnergySink {

    boolean debugLoad = false;
    boolean debugInput = false;

    static int maxSafeInput = SGBaseTE.ic2Input;
    static int maxEnergyBuffer = SGBaseTE.ic2Buffer;
    static double euPerSGEnergyUnit = SGBaseTE.ic2Ratio;

    public static void SetIC2Params(int input, int buffer, double ratio) {
        maxSafeInput = input;
        maxEnergyBuffer = buffer;
        euPerSGEnergyUnit = ratio;
    }
    
    boolean loaded = false;
    
    public IC2PowerTE() {
        super(maxEnergyBuffer, euPerSGEnergyUnit);
    }
    
    @Override
    public String getScreenTitle() {
        return "IC2 SGPU";
    }
    
    @Override
    public String getUnitName() {
        return "EU";
    }
    
    @Override
    public void updateEntity() {
        load();
    }
    
    @Override
    public void invalidate() {
        unload();
        super.invalidate();
    }
    
    @Override
    public void onChunkUnload() {
        unload();
        super.onChunkUnload();
    }
    
    void load() {
        if (!worldObj.isRemote && !loaded) {
            if(debugLoad)
                System.out.printf("SGCraft: IC2PowerTE: Adding to energy network\n");
            loaded = true;
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        }
    }			
    
    void unload() {
        if (!worldObj.isRemote && loaded) {
            if(debugLoad)
                System.out.printf("SGCraft: IC2PowerTE: Removing from energy network\n");
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            loaded = false;
        }
    }
    
    //------------------------- IEnergySink -------------------------
    
    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
        return true;
    }
    
    @Override
    public double getDemandedEnergy() {
        double eu = min(maxEnergyBuffer - energyBuffer, maxSafeInput);
        if(debugInput)
            System.out.printf("SGCraft: IC2PowerTE: Demanding %s EU\n", eu);
        return eu;
    }
    
    @Override
    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
        energyBuffer += amount;
        double diff = energyBuffer - maxEnergyBuffer;
        energyBuffer = min(energyBuffer, maxEnergyBuffer);
        markDirty();
        markBlockForUpdate();
        if(debugInput)
            System.out.printf("SGCraft: IC2PowerTE: Injected %s EU giving %s\n", amount, energyBuffer);
        return max(diff, 0);
    }
    
//	@Override
//	public int getMaxSafeInput() {
//		return maxSafeInput;
//	}
    
    @Override
    public int getSinkTier() {
        return 3;
    }
    
}
