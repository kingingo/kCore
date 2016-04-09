package eu.epicpvp.kcore.Translation;

import java.util.UUID;

import dev.wolveringer.dataserver.protocoll.DataBuffer;

public interface TranslationChannelListener {
	public void handle(UUID fromPacket,DataBuffer buffer);
}
