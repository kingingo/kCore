package me.kingingo.kcore.Disguise.disguises;
import org.bukkit.entity.Entity;

public abstract class DisguiseHuman extends DisguiseLiving
{
  public DisguiseHuman(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(16, Byte.valueOf((byte)0));
    this.DataWatcher.a(17, Float.valueOf(0.0F));
    this.DataWatcher.a(18, Integer.valueOf(0));
    this.DataWatcher.a(10, Byte.valueOf((byte)0));
  }
  
  public void setAbsorptionHearts(float f) {
	    if (f < 0.0F) {
	      f = 0.0F;
	    }
	    
	    this.DataWatcher.watch(17, Float.valueOf(f));
  }

  public float getAbsorptionHearts() {
	  return this.DataWatcher.getFloat(17);
  }
  
  public int getScore() {
	  return this.DataWatcher.getInt(18);
  }

  public void setScore(int i) {
	  this.DataWatcher.watch(18, Integer.valueOf(i));
  }
  
  public void addScore(int i) {
	  int j = getScore();
	  this.DataWatcher.watch(18, Integer.valueOf(j + i));
  }
}