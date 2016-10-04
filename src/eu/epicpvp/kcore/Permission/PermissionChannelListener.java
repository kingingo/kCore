package eu.epicpvp.kcore.Permission;

import java.util.UUID;

import eu.epicpvp.datenserver.definitions.dataserver.protocoll.DataBuffer;

public interface PermissionChannelListener {
	public boolean handle(UUID fromPacket,DataBuffer buffer);
}
