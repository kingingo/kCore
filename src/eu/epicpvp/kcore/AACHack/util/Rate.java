package eu.epicpvp.kcore.AACHack.util;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import lombok.Getter;

public class Rate {

	@Getter
	private final long maxTimeMillis;
	private final Deque<Long> eventTimes = new ConcurrentLinkedDeque<>();
	@Getter
	private final long resetTime;
	private long lastResetTime = System.currentTimeMillis();

	public Rate(long maxTime, TimeUnit maxTimeUnit) {
		this (maxTime, maxTimeUnit, -1, TimeUnit.MILLISECONDS);
	}

	public Rate(long maxTime, TimeUnit maxTimeUnit, long resetTime, TimeUnit resetTimeUnit) {
		this.maxTimeMillis = maxTimeUnit.toMillis(maxTime);
		this.resetTime = resetTimeUnit.toMillis(resetTime);
	}

	public void eventTriggered() {
		eventTriggered0(System.currentTimeMillis());
	}

	private void eventTriggered0(long currentMillis) {
		cleanup(currentMillis - maxTimeMillis);
		eventTimes.add(currentMillis);
	}

	public int getOccurredEventsInMaxTime() {
		return getOccurredEventsInMaxTime0(System.currentTimeMillis());
	}

	private int getOccurredEventsInMaxTime0(long currentMillis) {
		cleanup(currentMillis - maxTimeMillis);
		return eventTimes.size();
	}

	public int getOccurredEventsInTime(long timeFrame, TimeUnit timeUnit) {
		return getOccurredEventsInTime0(timeFrame, timeUnit, System.currentTimeMillis());
	}

	private int getOccurredEventsInTime0(long timeFrame, TimeUnit timeUnit, long currentMillis) {
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

	public double getAveragePerSecondOnMaxTime() {
		return getOccurredEventsInMaxTime() / (((double) maxTimeMillis) / 1000.0);
	}

	private double getAveragePerSecond0(long currentMillis) {
		return getOccurredEventsInMaxTime0(currentMillis) / (((double) maxTimeMillis) / 1000.0);
	}

	public double getAveragePerSecond(long timeFrame, TimeUnit timeUnit) {
		return getAveragePerSecond0(timeFrame, timeUnit, System.currentTimeMillis());
	}

	private double getAveragePerSecond0(long timeFrame, TimeUnit timeUnit, long currentMillis) {
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
		if (resetTime > 0 && getNextResetTime() < removeThresholdMillis) {
			reset();
		}
	}

	public void reset() {
		eventTimes.clear();
		lastResetTime = System.currentTimeMillis();
	}

	public long getNextResetTime() {
		if (resetTime <= 0) {
			throw new UnsupportedOperationException("There was no reset");
		}
		return lastResetTime + resetTime;
	}
}
