package eu.epicpvp.kcore.Gilden;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GildenType {
	PVP("PvP-Server", "PvP", new StatsKey[]{StatsKey.KILLS, StatsKey.DEATHS, StatsKey.ELO, StatsKey.LOC_X, StatsKey.LOC_Y, StatsKey.LOC_Z, StatsKey.WORLD}, "Fame"),
	SKY("Sky-Server", "Sky", new StatsKey[]{StatsKey.KILLS, StatsKey.DEATHS, StatsKey.MONEY}, "Money");

	@Getter
	private final String typ;
	@Getter
	private final String Kuerzel;
	@Getter
	private final StatsKey[] stats;
	@Getter
	private final String rankWith;

}
