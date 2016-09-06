package eu.epicpvp.kcore.AACHack.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

import lombok.Getter;

public class Rate {

	@Getter
	private final long maxTimeMillis;
	private final Deque<Long> eventTimes;

	public Rate(long maxTime, TimeUnit timeUnit) {
		this.maxTimeMillis = timeUnit.toMillis(maxTime);
		eventTimes = new ArrayDeque<>();
	}

	public void eventTriggered() {
		eventTriggered0(System.currentTimeMillis());
	}

	void eventTriggered0(long currentMillis) {
		cleanup(currentMillis - maxTimeMillis);
		eventTimes.add(currentMillis);
	}

	public int getOccurredEventsInMaxTime() {
		return getOccurredEventsInMaxTime0(System.currentTimeMillis());
	}

	int getOccurredEventsInMaxTime0(long currentMillis) {
		cleanup(currentMillis - maxTimeMillis);
		return eventTimes.size();
	}

	public int getOccurredEventsInTime(long timeFrame, TimeUnit timeUnit) {
		return getOccurredEventsInTime0(timeFrame, timeUnit, System.currentTimeMillis());
	}

	int getOccurredEventsInTime0(long timeFrame, TimeUnit timeUnit, long currentMillis) {
		cleanup(currentMillis - maxTimeMillis);
		long searchTimeCut = currentMillis - timeUnit.toMillis(timeFrame);

		int overJumped = 0;
		//the events are ordered by addition in the list, so it is possible like this
		for (long next : eventTimes) {
			if (next <= searchTimeCut) {
				overJumped++;
			}
		}
		return eventTimes.size() - overJumped;
	}

	public double getAveragePerSecond() {
		return getOccurredEventsInMaxTime() / (((double) maxTimeMillis) / 1000.0);
	}

	double getAveragePerSecond0(long currentMillis) {
		return getOccurredEventsInMaxTime0(currentMillis) / (((double) maxTimeMillis) / 1000.0);
	}

	public double getAveragePerSecond(long timeFrame, TimeUnit timeUnit) {
		return getAveragePerSecond0(timeFrame, timeUnit, System.currentTimeMillis());
	}

	public double getAveragePerSecond0(long timeFrame, TimeUnit timeUnit, long currentMillis) {
		return getOccurredEventsInTime0(timeFrame, timeUnit, currentMillis) / (((double) timeUnit.toMillis(timeFrame)) / 1000.0);
	}

	private void cleanup(long removeThresholdMillis) {
		while (true) {
			Long peek = eventTimes.peek();
			if (peek == null || peek > removeThresholdMillis) {
				break;
			}
			eventTimes.poll();
		}
	}
}
