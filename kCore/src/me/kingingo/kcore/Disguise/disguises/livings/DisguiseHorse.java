package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseAnimal;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;

public class DisguiseHorse extends DisguiseAnimal
{
  public DisguiseHorse(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(16, Integer.valueOf(0));
    this.DataWatcher.a(19, Byte.valueOf((byte)0));
    this.DataWatcher.a(20, Integer.valueOf(0));
    this.DataWatcher.a(21, String.valueOf(""));
    this.DataWatcher.a(22, Integer.valueOf(0));
  }

  protected EntityType GetEntityTypeId()
  {
    return EntityType.HORSE;
  }

  public void setType(Horse.Variant horseType)
  {
    this.DataWatcher.watch(19, Byte.valueOf((byte)horseType.ordinal()));
  }

  public Horse.Variant getType()
  {
    return Horse.Variant.values()[this.DataWatcher.getByte(19)];
  }

  public void setVariant(Horse.Color color)
  {
    this.DataWatcher.watch(20, Integer.valueOf(color.ordinal()));
  }

  public Horse.Color getVariant()
  {
    return Horse.Color.values()[this.DataWatcher.getInt(20)];
  }

  private boolean w(int i)
  {
    return (this.DataWatcher.getInt(16) & i) != 0;
  }

  public void kick()
  {
    c(32, false);
    c(64, true);
  }

  public boolean isTame() {
	return w(2);
  }
  
  public void setTame(boolean flag) {
	  c(2, flag);
  }
  
  public void stopKick()
  {
    c(64, false);
  }

  private void c(int i, boolean flag) {
	int j = this.DataWatcher.getInt(16);

	if (flag)
	this.DataWatcher.watch(16, Integer.valueOf(j | i));
	else
	this.DataWatcher.watch(16, Integer.valueOf(j & (i ^ 0xFFFFFFFF)));
	}

  public void setHasChest(boolean flag) {
	c(8, flag);
  }
  
  public String getOwnerUUID() {
	  return this.DataWatcher.getString(21);
  }

  public void setOwnerUUID(String s) {
	  this.DataWatcher.watch(21, s);
  }

  public int cx() {
	return this.DataWatcher.getInt(22);
  }

  public void e(ItemStack i){
	  e(i.getTypeId());
  }
  
  public void e(int i)
  {
    this.DataWatcher.watch(22, Integer.valueOf(i));
  }
}