package eu.epicpvp.kcore.Disguise.disguises;
import java.util.Random;

public abstract class DisguiseLiving extends DisguiseBase
{
  private static Random _random = new Random();

  public DisguiseLiving(org.bukkit.entity.Entity entity)
  {
    super(entity);

    this.DataWatcher.a(6, Float.valueOf(1.0F));
    this.DataWatcher.a(7, Integer.valueOf(0));
    this.DataWatcher.a(8, Byte.valueOf((byte)0));
    this.DataWatcher.a(9, Byte.valueOf((byte)0));
  }

  public void UpdateDataWatcher()
  {
    super.UpdateDataWatcher();

    setHealth(Float.valueOf(this.Entity.getDataWatcher().getFloat(6)));
    this.DataWatcher.watch(7, Integer.valueOf(this.Entity.getDataWatcher().getInt(7)));
    this.DataWatcher.watch(8, Byte.valueOf(this.Entity.getDataWatcher().getByte(8)));
    this.DataWatcher.watch(9, Byte.valueOf(this.Entity.getDataWatcher().getByte(9)));
  }
  
  public float getHealth(){
	  return this.DataWatcher.getFloat(6);
  }
  
  public void setHealth(float f){
      this.DataWatcher.watch(6, Float.valueOf(f));
  }

  protected String getHurtSound()
  {
    return "damage.hit";
  }

  protected float getVolume()
  {
    return 1.0F;
  }

  protected float getPitch()
  {
    return (_random.nextFloat() - _random.nextFloat()) * 0.2F + 1.0F;
  }
}