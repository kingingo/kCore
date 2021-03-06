package eu.epicpvp.kcore.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;
import org.bukkit.material.DirectionalContainer;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.TileEntityBeacon;

public class UtilBlock {
	public static HashSet<Byte> blockPassSet = new HashSet();

	public static HashSet<Byte> blockAirFoliageSet = new HashSet();

	public static HashSet<Byte> fullSolid = new HashSet();

	public static HashSet<Byte> blockUseSet = new HashSet();

	public static boolean solid(Block block) {
		if (block == null)
			return false;
		return solid(block.getTypeId());
	}

	public static Location getTwinLocation(Block block) {
		if (!((Bed) block.getState().getData()).isHeadOfBed()) {
			return (block.getRelative(((Bed) block.getState().getData()).getFacing())).getLocation();
		} else {
			return (block.getRelative(((Bed) block.getState().getData()).getFacing().getOppositeFace())).getLocation();
		}
	}

	public static BlockFace getSignFace(Sign sign) {
		org.bukkit.material.Sign matSign = new org.bukkit.material.Sign(Material.WALL_SIGN);
		matSign.setData(sign.getData().getData());
		return matSign.getFacing();
	}

	public static BlockFace getSignAttachedFace(Sign sign) {
		org.bukkit.material.Sign matSign = new org.bukkit.material.Sign(Material.WALL_SIGN);
		matSign.setData(sign.getData().getData());
		return matSign.getAttachedFace();
	}

	public static Location getBlockCenterUP(Location loc) {
		if (loc != null) {
			return new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getY() + 0.1, loc.getZ() + 0.5);
		}
		return loc;
	}

	public static double getWidth(Location center, boolean Xline) throws NullPointerException {
		if (center.getBlock().getType() != Material.AIR) {
			Location loc1 = center.clone();

			for (int i = 0; i < 1; i++) {
				if (Xline)
					loc1 = loc1.add(1, 0, 0);
				else
					loc1 = loc1.add(0, 0, 1);

				if (loc1.getBlock().getType() == Material.AIR) {
					if (Xline)
						loc1 = loc1.add(-1, 0, 0);
					else
						loc1 = loc1.add(0, 0, -1);
					break;
				} else {
					i--;
				}
			}

			Location loc2 = center.clone();

			for (int i = 0; i < 1; i++) {
				if (Xline)
					loc1 = loc1.add(-1, 0, 0);
				else
					loc1 = loc1.add(0, 0, -1);

				if (loc2.getBlock().getType() == Material.AIR) {
					if (Xline)
						loc1 = loc1.add(1, 0, 0);
					else
						loc1 = loc1.add(0, 0, 1);
					break;
				} else {
					i--;
				}
			}

			return (loc1.distance(center) + loc2.distance(center));
		}
		throw new NullPointerException("center Block is AIR!?");
	}

	public static void setChestFace(Block block, BlockFace face) {
		if (face == null || block == null)
			return;
		if (block.getType() == Material.ENDER_CHEST || block.getType() == Material.CHEST) {
			BlockState state = block.getState();
			DirectionalContainer c = new DirectionalContainer(block.getType());
			c.setFacingDirection(face);
			state.setRawData(c.getData());
			state.update();
		}
	}

	public static BlockState placeBed(Location location, BlockFace face) {
		BlockState bedFoot = location.getBlock().getState();
		BlockState bedHead = bedFoot.getBlock().getRelative(face).getState();
		bedFoot.setType(Material.BED_BLOCK);
		bedHead.setType(Material.BED_BLOCK);

		byte flags = (byte) 8;
		byte direction = (byte) (0x0);

		switch (face) {

		case EAST:
			flags = (byte) (flags | 0x3);
			direction = (byte) (0x3);
			break;

		case SOUTH:
			flags = (byte) (flags | 0x0);
			direction = (byte) (0x0);
			break;

		case WEST:
			flags = (byte) (flags | 0x1);
			direction = (byte) (0x1);
			break;

		case NORTH:
			flags = (byte) (flags | 0x2);
			direction = (byte) (0x2);
			break;
		}

		bedFoot.setRawData((byte) direction);
		bedHead.setRawData((byte) flags);
		bedFoot.update(true, false);
		bedHead.update(true, true);

		return bedHead;
	}

	public static boolean solid(int block) {
		return solid((byte) block);
	}

	public static void setBeaconActive(Block block, boolean state) {
		block.setType(Material.BEACON);
		TileEntityBeacon beacon = (TileEntityBeacon) ((CraftWorld) block.getWorld()).getHandle()
				.getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
		UtilReflection.setValue("a", beacon, state);
	}

	public static boolean solid(byte block) {
		if (blockPassSet.isEmpty()) {
			blockPassSet.add(Byte.valueOf((byte) 0));
			blockPassSet.add(Byte.valueOf((byte) 6));
			blockPassSet.add(Byte.valueOf((byte) 8));
			blockPassSet.add(Byte.valueOf((byte) 9));
			blockPassSet.add(Byte.valueOf((byte) 10));
			blockPassSet.add(Byte.valueOf((byte) 11));
			blockPassSet.add(Byte.valueOf((byte) 26));
			blockPassSet.add(Byte.valueOf((byte) 27));
			blockPassSet.add(Byte.valueOf((byte) 28));
			blockPassSet.add(Byte.valueOf((byte) 30));
			blockPassSet.add(Byte.valueOf((byte) 31));
			blockPassSet.add(Byte.valueOf((byte) 32));
			blockPassSet.add(Byte.valueOf((byte) 37));
			blockPassSet.add(Byte.valueOf((byte) 38));
			blockPassSet.add(Byte.valueOf((byte) 39));
			blockPassSet.add(Byte.valueOf((byte) 40));
			blockPassSet.add(Byte.valueOf((byte) 50));
			blockPassSet.add(Byte.valueOf((byte) 51));
			blockPassSet.add(Byte.valueOf((byte) 55));
			blockPassSet.add(Byte.valueOf((byte) 59));
			blockPassSet.add(Byte.valueOf((byte) 63));
			blockPassSet.add(Byte.valueOf((byte) 64));
			blockPassSet.add(Byte.valueOf((byte) 65));
			blockPassSet.add(Byte.valueOf((byte) 66));
			blockPassSet.add(Byte.valueOf((byte) 68));
			blockPassSet.add(Byte.valueOf((byte) 69));
			blockPassSet.add(Byte.valueOf((byte) 70));
			blockPassSet.add(Byte.valueOf((byte) 71));
			blockPassSet.add(Byte.valueOf((byte) 72));
			blockPassSet.add(Byte.valueOf((byte) 75));
			blockPassSet.add(Byte.valueOf((byte) 76));
			blockPassSet.add(Byte.valueOf((byte) 77));
			blockPassSet.add(Byte.valueOf((byte) 78));
			blockPassSet.add(Byte.valueOf((byte) 83));
			blockPassSet.add(Byte.valueOf((byte) 90));
			blockPassSet.add(Byte.valueOf((byte) 92));
			blockPassSet.add(Byte.valueOf((byte) 93));
			blockPassSet.add(Byte.valueOf((byte) 94));
			blockPassSet.add(Byte.valueOf((byte) 96));
			blockPassSet.add(Byte.valueOf((byte) 101));
			blockPassSet.add(Byte.valueOf((byte) 102));
			blockPassSet.add(Byte.valueOf((byte) 104));
			blockPassSet.add(Byte.valueOf((byte) 105));
			blockPassSet.add(Byte.valueOf((byte) 106));
			blockPassSet.add(Byte.valueOf((byte) 107));
			blockPassSet.add(Byte.valueOf((byte) 111));
			blockPassSet.add(Byte.valueOf((byte) 115));
			blockPassSet.add(Byte.valueOf((byte) 116));
			blockPassSet.add(Byte.valueOf((byte) 117));
			blockPassSet.add(Byte.valueOf((byte) 118));
			blockPassSet.add(Byte.valueOf((byte) 119));
			blockPassSet.add(Byte.valueOf((byte) 120));
			blockPassSet.add(Byte.valueOf((byte) -85));
		}

		return !blockPassSet.contains(Byte.valueOf(block));
	}

	public static boolean airFoliage(Block block) {
		if (block == null)
			return false;
		return airFoliage(block.getTypeId());
	}

	public static boolean airFoliage(int block) {
		return airFoliage((byte) block);
	}

	public static boolean airFoliage(byte block) {
		if (blockAirFoliageSet.isEmpty()) {
			blockAirFoliageSet.add(Byte.valueOf((byte) 0));
			blockAirFoliageSet.add(Byte.valueOf((byte) 6));
			blockAirFoliageSet.add(Byte.valueOf((byte) 31));
			blockAirFoliageSet.add(Byte.valueOf((byte) 32));
			blockAirFoliageSet.add(Byte.valueOf((byte) 37));
			blockAirFoliageSet.add(Byte.valueOf((byte) 38));
			blockAirFoliageSet.add(Byte.valueOf((byte) 39));
			blockAirFoliageSet.add(Byte.valueOf((byte) 40));
			blockAirFoliageSet.add(Byte.valueOf((byte) 51));
			blockAirFoliageSet.add(Byte.valueOf((byte) 59));
			blockAirFoliageSet.add(Byte.valueOf((byte) 104));
			blockAirFoliageSet.add(Byte.valueOf((byte) 105));
			blockAirFoliageSet.add(Byte.valueOf((byte) 115));
			blockAirFoliageSet.add(Byte.valueOf((byte) -115));
			blockAirFoliageSet.add(Byte.valueOf((byte) -114));
		}

		return blockAirFoliageSet.contains(Byte.valueOf(block));
	}

	public static boolean fullSolid(Block block) {
		if (block == null) {
			return false;
		}
		return fullSolid(block.getTypeId());
	}

	public static boolean fullSolid(int block) {
		return fullSolid((byte) block);
	}

	public static boolean fullSolid(byte block) {
		if (fullSolid.isEmpty()) {
			fullSolid.add(Byte.valueOf((byte) 1));
			fullSolid.add(Byte.valueOf((byte) 2));
			fullSolid.add(Byte.valueOf((byte) 3));
			fullSolid.add(Byte.valueOf((byte) 4));
			fullSolid.add(Byte.valueOf((byte) 5));
			fullSolid.add(Byte.valueOf((byte) 7));
			fullSolid.add(Byte.valueOf((byte) 12));
			fullSolid.add(Byte.valueOf((byte) 13));
			fullSolid.add(Byte.valueOf((byte) 14));
			fullSolid.add(Byte.valueOf((byte) 15));
			fullSolid.add(Byte.valueOf((byte) 16));
			fullSolid.add(Byte.valueOf((byte) 17));
			fullSolid.add(Byte.valueOf((byte) 19));
			fullSolid.add(Byte.valueOf((byte) 20));
			fullSolid.add(Byte.valueOf((byte) 21));
			fullSolid.add(Byte.valueOf((byte) 22));
			fullSolid.add(Byte.valueOf((byte) 23));
			fullSolid.add(Byte.valueOf((byte) 24));
			fullSolid.add(Byte.valueOf((byte) 25));
			fullSolid.add(Byte.valueOf((byte) 29));
			fullSolid.add(Byte.valueOf((byte) 33));
			fullSolid.add(Byte.valueOf((byte) 35));
			fullSolid.add(Byte.valueOf((byte) 41));
			fullSolid.add(Byte.valueOf((byte) 42));
			fullSolid.add(Byte.valueOf((byte) 43));
			fullSolid.add(Byte.valueOf((byte) 44));
			fullSolid.add(Byte.valueOf((byte) 45));
			fullSolid.add(Byte.valueOf((byte) 46));
			fullSolid.add(Byte.valueOf((byte) 47));
			fullSolid.add(Byte.valueOf((byte) 48));
			fullSolid.add(Byte.valueOf((byte) 49));
			fullSolid.add(Byte.valueOf((byte) 56));
			fullSolid.add(Byte.valueOf((byte) 57));
			fullSolid.add(Byte.valueOf((byte) 58));
			fullSolid.add(Byte.valueOf((byte) 60));
			fullSolid.add(Byte.valueOf((byte) 61));
			fullSolid.add(Byte.valueOf((byte) 62));
			fullSolid.add(Byte.valueOf((byte) 73));
			fullSolid.add(Byte.valueOf((byte) 74));
			fullSolid.add(Byte.valueOf((byte) 79));
			fullSolid.add(Byte.valueOf((byte) 80));
			fullSolid.add(Byte.valueOf((byte) 82));
			fullSolid.add(Byte.valueOf((byte) 84));
			fullSolid.add(Byte.valueOf((byte) 86));
			fullSolid.add(Byte.valueOf((byte) 87));
			fullSolid.add(Byte.valueOf((byte) 88));
			fullSolid.add(Byte.valueOf((byte) 89));
			fullSolid.add(Byte.valueOf((byte) 91));
			fullSolid.add(Byte.valueOf((byte) 95));
			fullSolid.add(Byte.valueOf((byte) 97));
			fullSolid.add(Byte.valueOf((byte) 98));
			fullSolid.add(Byte.valueOf((byte) 99));
			fullSolid.add(Byte.valueOf((byte) 100));
			fullSolid.add(Byte.valueOf((byte) 103));
			fullSolid.add(Byte.valueOf((byte) 110));
			fullSolid.add(Byte.valueOf((byte) 112));
			fullSolid.add(Byte.valueOf((byte) 121));
			fullSolid.add(Byte.valueOf((byte) 123));
			fullSolid.add(Byte.valueOf((byte) 124));
			fullSolid.add(Byte.valueOf((byte) 125));
			fullSolid.add(Byte.valueOf((byte) 126));
			fullSolid.add(Byte.valueOf((byte) -127));
			fullSolid.add(Byte.valueOf((byte) -123));
			fullSolid.add(Byte.valueOf((byte) -119));
			fullSolid.add(Byte.valueOf((byte) -118));
			fullSolid.add(Byte.valueOf((byte) -104));
			fullSolid.add(Byte.valueOf((byte) -103));
			fullSolid.add(Byte.valueOf((byte) -101));
			fullSolid.add(Byte.valueOf((byte) -98));
		}

		return fullSolid.contains(Byte.valueOf(block));
	}

	public static boolean usable(Block block) {
		if (block == null) {
			return false;
		}
		return usable(block.getTypeId());
	}

	public static boolean usable(int block) {
		return usable((byte) block);
	}

	public static boolean usable(byte block) {
		if (blockUseSet.isEmpty()) {
			blockUseSet.add(Byte.valueOf((byte) 23));
			blockUseSet.add(Byte.valueOf((byte) 26));
			blockUseSet.add(Byte.valueOf((byte) 33));
			blockUseSet.add(Byte.valueOf((byte) 47));
			blockUseSet.add(Byte.valueOf((byte) 54));
			blockUseSet.add(Byte.valueOf((byte) 58));
			blockUseSet.add(Byte.valueOf((byte) 61));
			blockUseSet.add(Byte.valueOf((byte) 62));
			blockUseSet.add(Byte.valueOf((byte) 64));
			blockUseSet.add(Byte.valueOf((byte) 69));
			blockUseSet.add(Byte.valueOf((byte) 71));
			blockUseSet.add(Byte.valueOf((byte) 77));
			blockUseSet.add(Byte.valueOf((byte) 93));
			blockUseSet.add(Byte.valueOf((byte) 94));
			blockUseSet.add(Byte.valueOf((byte) 96));
			blockUseSet.add(Byte.valueOf((byte) 107));
			blockUseSet.add(Byte.valueOf((byte) 116));
			blockUseSet.add(Byte.valueOf((byte) 117));
			blockUseSet.add(Byte.valueOf((byte) -126));
			blockUseSet.add(Byte.valueOf((byte) -111));
			blockUseSet.add(Byte.valueOf((byte) -110));
			blockUseSet.add(Byte.valueOf((byte) -102));
			blockUseSet.add(Byte.valueOf((byte) -98));
		}

		return blockUseSet.contains(Byte.valueOf(block));
	}

	public static HashMap<Block, Double> getInRadius(Location loc, double dR) {
		return getInRadius(loc, dR, 999.0D);
	}

	public static HashMap<Block, Double> getInRadius(Location loc, double dR, double heightLimit) {
		HashMap blockList = new HashMap();
		int iR = (int) dR + 1;

		for (int x = -iR; x <= iR; x++)
			for (int z = -iR; z <= iR; z++)
				for (int y = -iR; y <= iR; y++) {
					if (Math.abs(y) <= heightLimit) {
						Block curBlock = loc.getWorld().getBlockAt((int) (loc.getX() + x), (int) (loc.getY() + y),
								(int) (loc.getZ() + z));

						double offset = UtilMath.offset(loc, curBlock.getLocation().add(0.5D, 0.5D, 0.5D));

						if (offset <= dR)
							blockList.put(curBlock, Double.valueOf(1.0D - offset / dR));
					}
				}
		return blockList;
	}

	public static HashMap<Block, Double> getInRadius(Block block, double dR) {
		HashMap blockList = new HashMap();
		int iR = (int) dR + 1;

		for (int x = -iR; x <= iR; x++) {
			for (int z = -iR; z <= iR; z++)
				for (int y = -iR; y <= iR; y++) {
					Block curBlock = block.getRelative(x, y, z);

					double offset = UtilMath.offset(block.getLocation(), curBlock.getLocation());

					if (offset <= dR)
						blockList.put(curBlock, Double.valueOf(1.0D - offset / dR));
				}
		}
		return blockList;
	}

	public static boolean isBlock(Material m) {
		if (m == null) {
			return false;
		}
		return (m.getId() > 0) && (m.getId() < 256);
	}

	public static Block getHighest(Location loc, ArrayList<ItemStack> ignore) {
		return getHighest(loc.getWorld(), loc.getBlockX(), loc.getBlockZ(), ignore);
	}

	public static Block getHighest(World world, int x, int z, ArrayList<ItemStack> ignore) {
		for (int y = 255; y > 0; y--) {
			Block b = world.getBlockAt(x, y, z);

			if (isBlock(b.getType())
					&& (ignore == null || !ignore.contains(new ItemStack(b.getType(), 1, b.getData())))) {
				if (b.getType() == Material.AIR || b.getTypeId() == 78 || b.getTypeId() == 31 || b.getTypeId() == 32
						|| b.getTypeId() == 37 || b.getTypeId() == 38 || b.getTypeId() == 39 || b.getTypeId() == 40
						|| b.getType().toString().contains("WATER"))
					continue;

				return b;
			}
		}

		return null;
	}

	public static ArrayList<Block> getSurrounding(Block block, boolean diagonals) {
		ArrayList blocks = new ArrayList();

		if (diagonals) {
			for (int x = -1; x <= 1; x++)
				for (int y = -1; y <= 1; y++)
					for (int z = -1; z <= 1; z++) {
						if ((x != 0) || (y != 0) || (z != 0)) {
							blocks.add(block.getRelative(x, y, z));
						}
					}
		} else {
			blocks.add(block.getRelative(BlockFace.UP));
			blocks.add(block.getRelative(BlockFace.DOWN));
			blocks.add(block.getRelative(BlockFace.NORTH));
			blocks.add(block.getRelative(BlockFace.SOUTH));
			blocks.add(block.getRelative(BlockFace.EAST));
			blocks.add(block.getRelative(BlockFace.WEST));
		}

		return blocks;
	}

	public static boolean isVisible(Block block) {
		for (Block other : getSurrounding(block, false)) {
			if (!other.getType().isOccluding()) {
				return true;
			}
		}

		return false;
	}
}