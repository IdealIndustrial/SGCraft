//------------------------------------------------------------------------------------------------
//
//   Greg's Mod Base for 1.7 Version B - Utilities
//
//------------------------------------------------------------------------------------------------

package gcewing.sg;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import net.minecraft.block.Block;
//import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapStorage;

public class BaseUtils {

	public static EnumFacing[] facings = EnumFacing.values();
	public static EnumFacing[] horizontalFacings = {
	    EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.EAST}; 
	
	public static int abs(int x) {
	    return x >= 0 ? x : -x;
	}

	public static int min(int x, int y) {
		return x < y ? x : y;
	}
	
	public static int max(int x, int y) {
		return x > y ? x : y;
	}

	public static double min(double x, double y) {
		return x < y ? x : y;
	}
	
	public static double max(double x, double y) {
		return x > y ? x : y;
	}
	
	public static int ifloor(double x) {
		return (int)Math.floor(x);
	}
	
	public static int iround(double x) {
		return (int)Math.round(x);
	}
	
	public static int iceil(double x) {
		return (int)Math.ceil(x);
	}
	
	public static Object[] arrayOf(Collection c) {
		int n = c.size();
		Object[] result = new Object[n];
		int i = 0;
		for (Object item : c)
			result[i++] = item;
		return result;
	}
	
	public static Field getFieldDef(Class cls, String unobfName, String obfName) {
		try {
			Field field;
			try {
				field = cls.getDeclaredField(unobfName);
			}
			catch (NoSuchFieldException e) {
				field = cls.getDeclaredField(obfName);
			}
			field.setAccessible(true);
			return field;
		}
		catch (Exception e) {
			throw new RuntimeException(
				String.format("Cannot find field %s or %s of %s", unobfName, obfName, cls.getName()),
				e);
		}
	}
	
	public static Object getField(Object obj, String unobfName, String obfName) {
	    Field field = getFieldDef(obj.getClass(), unobfName, obfName);
	    return getField(obj, field);
	}
		
	public static Object getField(Object obj, Field field) {
		try {
			return field.get(obj);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    
	public static void setField(Object obj, String unobfName, String obfName, Object value) {
	    Field field = getFieldDef(obj.getClass(), unobfName, obfName);
		setField(obj, field, value);
	}
	
	public static void setField(Object obj, Field field, Object value) {
	    try {
            field.set(obj, value);
        }
        catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static int packedColor(double red, double green, double blue) {
		return ((int)(red * 255) << 16) | ((int)(green * 255) << 8) | (int)(blue * 255);
	}
	
	public static ItemStack blockStackWithState(IBlockState state, int size) {
		BaseBlock block = (BaseBlock)state.getBlock();
		int meta = block.getMetaFromState(state);
		return new ItemStack(block, size, meta);
	}
	
	public static ItemStack blockStackWithTileEntity(Block block, int size, BaseTileEntity te) {
		ItemStack stack = new ItemStack(block, size);
		if (te != null) {
			NBTTagCompound tag = new NBTTagCompound();
			te.writeToItemStackNBT(tag);
			stack.setTagCompound(tag);
		}
		return stack;
	}
	
	public static int turnToFace(EnumFacing local, EnumFacing global) {
		return (turnToFaceEast(local) - turnToFaceEast(global)) & 3;
	}
	
	public static int turnToFaceEast(EnumFacing f) {
		switch (f) {
			case SOUTH: return 1;
			case WEST: return 2;
			case NORTH: return 3;
			default: return 0;
		}
	}
	
	public static BlockPos readBlockPos(DataInput data) {
	    try {
            int x = data.readInt();
            int y = data.readInt();
            int z = data.readInt();
            return new BlockPos(x, y, z);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
	
	public static void writeBlockPos(DataOutput data, BlockPos pos) {
	    try {
            data.writeInt(pos.getX());
            data.writeInt(pos.getY());
            data.writeInt(pos.getZ());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static int getMetaFromState(IBlockState state) {
        return ((BaseBlock)state.getBlock()).getMetaFromState(state);
    }
    
    public static int getWorldDimensionId(World world) {
        return world.provider.dimensionId;
    }
    
    public static EnumDifficulty getWorldDifficulty(World world) {
        return world.difficultySetting;
    }
    
    public static World getChunkWorld(Chunk chunk) {
        return chunk.worldObj;
    }
    
    public static Map getChunkTileEntityMap(Chunk chunk) {
        return chunk.chunkTileEntityMap;
    }
    
    public static AxisAlignedBB newAxisAlignedBB(double x0, double y0, double z0,
        double x1, double y1, double z1)
    {
        return AxisAlignedBB.getBoundingBox(x0, y0, z0, x1, y1, z1);
    }
    
    public static boolean getGameRuleBoolean(GameRules gr, String name) {
        return gr.getGameRuleBooleanValue(name);
    }
    
    public static void scmPreparePlayer(ServerConfigurationManager scm, EntityPlayerMP player, WorldServer world) {
        scm.func_72375_a(player, world);
    }
    
    public static void setBoundingBoxOfEntity(Entity entity, AxisAlignedBB box) {
        entity.boundingBox.setBounds(box.minX, box.minY, box.minZ,
            box.maxX, box.maxY, box.maxZ);
    }
    
    public static String getEntityName(Entity entity) {
        return entity.getCommandSenderName();
    }
    
    public static MapStorage getPerWorldStorage(World world) {
        return world.perWorldStorage;
    }
    
    public static EnumFacing oppositeFacing(EnumFacing dir) {
        return facings[dir.ordinal() ^ 1];
    }
    
}
