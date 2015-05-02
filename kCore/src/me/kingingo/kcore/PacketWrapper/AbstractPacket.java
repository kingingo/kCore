/*
 *  PacketWrapper - Contains wrappers for each packet in Minecraft.
 *  Copyright (C) 2012 Kristian S. Stangeland
 *
 *  This program is free software; you can redistribute it and/or modify it under the terms of the 
 *  GNU Lesser General Public License as published by the Free Software Foundation; either version 2 of 
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program; 
 *  if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
 *  02111-1307 USA
 */

package me.kingingo.kcore.PacketWrapper;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.base.Objects;

public abstract class AbstractPacket {
	// The packet we will be modifying
	protected PacketContainer handle;

	/**
	 * Constructs a new strongly typed wrapper for the given packet.
	 * @param handle - handle to the raw packet data.
	 * @param type - the packet type.
	 */
	protected AbstractPacket(PacketContainer handle, PacketType type) {
		// Make sure we're given a valid packet
		if (handle == null)
			throw new IllegalArgumentException("Packet handle cannot be NULL.");
		if (!Objects.equal(handle.getType(), type))
			throw new IllegalArgumentException(
					handle.getHandle() + " is not a packet of type " + type);
		
		this.handle = handle;
	}

	/**
	 * Retrieve a handle to the raw packet data.
	 * @return Raw packet data.
	 */
	public PacketContainer getHandle() {
		return handle;
	}
	
	/**
	 * Send the current packet to the given receiver.
	 * @param receiver - the receiver.
	 * @throws RuntimeException If the packet cannot be sent.
	 */
	public void sendPacket(Player receiver) {
		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, getHandle());
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Cannot send packet.", e);
		}
	}
	
	/**
	 * Simulate receiving the current packet from the given sender.
	 * @param sender - the sender.
	 * @throws RuntimeException If the packet cannot be received.
	 */
	public void recievePacket(Player sender) {
		try {
			ProtocolLibrary.getProtocolManager().recieveClientPacket(sender, getHandle());
		} catch (Exception e) {
			throw new RuntimeException("Cannot recieve packet.", e);
		}
	}
}
