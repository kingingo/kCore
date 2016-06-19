package eu.epicpvp.kcore.LagMeter.Chunks;
public class TickLimiter
{
  private long startTime;
  private final int maxTick;

  public TickLimiter(int maxTick)
  {
    this.maxTick = maxTick;
  }

  public void initTick() {
    this.startTime = System.currentTimeMillis();
  }

  public boolean shouldContinue() {
    return elapsedTime() < this.maxTick;
  }

  public long elapsedTime() {
    return System.currentTimeMillis() - this.startTime;
  }
}