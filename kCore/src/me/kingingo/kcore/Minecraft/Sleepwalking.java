package me.kingingo.kcore.Minecraft;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
 
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
 
import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.PacketConstructor;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.reflect.StructureModifier;
 
public class Sleepwalking extends JavaPlugin implements Listener {
 
    private PacketConstructor useBedConstructor;
    private PacketConstructor relativeMoveConstructor;
    
    private ProtocolManager manager;
    
    private Map<String, Integer> offsetY = new HashMap<String, Integer>();
    
    @Override
    public void onEnable() {
        manager = ProtocolLibrary.getProtocolManager();
        
        manager.addPacketListener(new PacketAdapter(this, ConnectionSide.SERVER_SIDE, 
                Packets.Server.ENTITY_TELEPORT) {
            
            public void onPacketSending(PacketEvent event) {
                
                PacketContainer packet = event.getPacket();
                Player receiver = event.getPlayer();
                
                try {
                    Entity target = packet.getEntityModifier(receiver.getWorld()).read(0);
                    int offset = getOffsetY(target);
 
                    // Only modify the value if we need to
                    if (offset != 0) {
                        StructureModifier<Integer> ints = packet.getSpecificModifier(int.class);
                        ints.write(2, ints.read(2) + offset);
                    }
                    
                } catch (FieldAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private int getOffsetY(Entity target) {
        if (target instanceof Player) {
            String name = ((Player) target).getName();
            Integer result = offsetY.get(name);
            
            if (result != null)
                return result;
        }
        
        // Default value
        return 0;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
 
        if (args.length > 0) {
            sender.sendMessage("This command doesn't accept any arguments.");
            return true;
        }
        
        if (label.equalsIgnoreCase("performaction")) {
            if (sender instanceof Player)
                performAction((Player) sender);
            else
                performAction(sender);
            return true;
        }
        
        return false;
    }
    
    private void performAction(CommandSender sender) {
        sender.sendMessage("Cannot perform command for console!");
    }
    
    private void performAction(Player player) {
        Location loc = player.getLocation();
        
        if (useBedConstructor == null) {
            useBedConstructor = manager.createPacketConstructor(Packets.Server.ENTITY_LOCATION_ACTION, 
                            player, 0, 0, 0, 0);
        }
        if (relativeMoveConstructor == null) {
            relativeMoveConstructor = manager.createPacketConstructor(
                    Packets.Server.REL_ENTITY_MOVE, (int) 0, (byte) 0, (byte) 0, (byte) 0);
        }
                
        // Make the player appear asleep
        try {
            final int OFFSET_Y = 2;
            
            PacketContainer sleepPacket = useBedConstructor.createPacket(
                    player, 0, loc.getBlockX(), loc.getBlockY() + 2, loc.getBlockZ());
            PacketContainer movePacket = relativeMoveConstructor.createPacket(
                    player.getEntityId(), (byte) 0, (byte) (OFFSET_Y * 32), (byte) 0); 
            
            // Broadcast this to every nearby player
            for (Player other : getServer().getOnlinePlayers()) {
                if (!other.equals(player)) {
                    manager.sendServerPacket(other, sleepPacket);
                    manager.sendServerPacket(other, movePacket);
                }
            }
            
            offsetY.put(player.getName(), OFFSET_Y);
            
        } catch (FieldAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}