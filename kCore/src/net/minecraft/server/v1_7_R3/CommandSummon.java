package net.minecraft.server.v1_7_R3;

import java.util.List;

import net.minecraft.server.v1_7_R3.CommandAbstract;
import net.minecraft.server.v1_7_R3.Entity;
import net.minecraft.server.v1_7_R3.EntityInsentient;
import net.minecraft.server.v1_7_R3.EntityTypes;
import net.minecraft.server.v1_7_R3.ExceptionUsage;
import net.minecraft.server.v1_7_R3.GroupDataEntity;
import net.minecraft.server.v1_7_R3.IChatBaseComponent;
import net.minecraft.server.v1_7_R3.ICommandListener;
import net.minecraft.server.v1_7_R3.MojangsonParser;
import net.minecraft.server.v1_7_R3.NBTBase;
import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R3.CraftServer;

public class CommandSummon extends CommandAbstract {

    public CommandSummon() {}

    public String getCommand() {
        return "summon";
    }

    public int a() {
        return 2;
    }

    public String c(ICommandListener icommandlistener) {
        return "commands.summon.usage";
    }

    public void execute(ICommandListener icommandlistener, String[] astring) {
        if (astring.length < 1) {
            throw new ExceptionUsage("commands.summon.usage", new Object[0]);
        } else {
            String s = astring[0];
            double d0 = (double) icommandlistener.getChunkCoordinates().x + 0.5D;
            double d1 = (double) icommandlistener.getChunkCoordinates().y;
            double d2 = (double) icommandlistener.getChunkCoordinates().z + 0.5D;

            if (astring.length >= 4) {
                d0 = a(icommandlistener, d0, astring[1]);
                d1 = a(icommandlistener, d1, astring[2]);
                d2 = a(icommandlistener, d2, astring[3]);
            }
            CraftServer cs = (CraftServer)Bukkit.getServer();
            World world=null;
            for(World w : cs.getServer().worlds){
            	if(w.getWorld().getName().equalsIgnoreCase("map")){
            		world=w;
            		break;
            	}
            }
            
            world.getWorld().loadChunk(world.getWorld().getChunkAt((int)d0, (int)d2));
            
            if (!world.isLoaded((int) d0, (int) d1, (int) d2)) {
                a(icommandlistener, this, "commands.summon.outOfWorld", new Object[0]);
            } else {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                boolean flag = false;

                if (astring.length >= 5) {
                    IChatBaseComponent ichatbasecomponent = a(icommandlistener, astring, 4);

                    NBTBase nbtbase = MojangsonParser.parse(ichatbasecomponent.c());

					if (!(nbtbase instanceof NBTTagCompound)) {
					    a(icommandlistener, this, "commands.summon.tagError", new Object[] { "Not a valid tag"});
					    return;
					}

					nbttagcompound = (NBTTagCompound) nbtbase;
					flag = true;
                }

                nbttagcompound.setString("id", s);
                Entity entity = EntityTypes.a(nbttagcompound, world);

                if (entity == null) {
                    a(icommandlistener, this, "commands.summon.failed", new Object[0]);
                } else {
                    entity.setPositionRotation(d0, d1, d2, entity.yaw, entity.pitch);
                    if (!flag && entity instanceof EntityInsentient) {
                        ((EntityInsentient) entity).a((GroupDataEntity) null);
                    }

                    world.addEntity(entity);
                    Entity entity1 = entity;

                    for (NBTTagCompound nbttagcompound1 = nbttagcompound; entity1 != null && nbttagcompound1.hasKeyOfType("Riding", 10); nbttagcompound1 = nbttagcompound1.getCompound("Riding")) {
                        Entity entity2 = EntityTypes.a(nbttagcompound1.getCompound("Riding"), world);

                        if (entity2 != null) {
                            entity2.setPositionRotation(d0, d1, d2, entity2.yaw, entity2.pitch);
                            world.addEntity(entity2);
                            entity1.mount(entity2);
                        }

                        entity1 = entity2;
                    }

                    a(icommandlistener, this, "commands.summon.success", new Object[0]);
                }
            }
        }
    }

    public List tabComplete(ICommandListener icommandlistener, String[] astring) {
        return astring.length == 1 ? a(astring, this.d()) : null;
    }

    protected String[] d() {
        return (String[]) EntityTypes.b().toArray(new String[0]);
    }

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
