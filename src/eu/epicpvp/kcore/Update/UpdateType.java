package eu.epicpvp.kcore.Update;

public enum UpdateType
{
  HOUR_4(4*3840000L),
  HOUR_3(3*3840000L), 
  HOUR_2(2*3840000L), 
  MIN_64(3840000L), 
  MIN_32(1920000L), 
  MIN_16(960000L), 
  MIN_10(10*60000L), 
  MIN_08(480000L), 
  MIN_04(240000L), 
  MIN_02(120000L), 
  MIN_01(60000L), 
  MIN_005(30000L),
  SLOWEST(32000L), 
  SLOWER(16000L), 
  SLOW(4000L), 
  SEC_3(3000L),
  SEC_2(2000L),
  SEC(1000L), 
  FAST(500L), 
  FASTER(250L), 
  FASTEST(125L), 
  TICK(49L);

  private long _time;
  private long _last;
  private long _timeSpent;
  private long _timeCount;

  private UpdateType(long time) { this._time = time;
    this._last = System.currentTimeMillis();
  }

  public boolean Elapsed()
  {
	  
    if (System.currentTimeMillis() - this._last > this._time)
    {
      this._last = System.currentTimeMillis();
      return true;
    }

    return false;
  }

  public void StartTime()
  {
    this._timeCount = System.currentTimeMillis();
  }

  public void StopTime()
  {
    this._timeSpent += System.currentTimeMillis() - this._timeCount;
  }

  public void PrintAndResetTime()
  {
    System.out.println(name() + " in a second: " + this._timeSpent);
    this._timeSpent = 0L;
  }
}