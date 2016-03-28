package eu.epicpvp.kcore.Permission;

import java.util.UUID;

import dev.wolveringer.dataserver.protocoll.DataBuffer;

public interface PermissionChannelListener {
	public void handle(UUID fromPacket,DataBuffer buffer);
}
