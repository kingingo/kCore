package eu.epicpvp.kcore.Disguise.disguises.livings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseMonster;

public class DisguiseGuardian extends DisguiseMonster
{
  public DisguiseGuardian(Entity entity)
  {
    super(entity);
    this.DataWatcher.a(16, Integer.valueOf(0));
    this.DataWatcher.a(17, Integer.valueOf(0));
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.GUARDIAN;
  }

  public String getHurtSound()
  {
    return "mob.guardian.land.idle";
  }
  
  private boolean a(int i) {
      return (this.DataWatcher.getInt(16) & i) != 0;
  }

  private void a(int i, boolean flag) {
      int j = this.DataWatcher.getInt(16);

      if (flag) {
          this.DataWatcher.watch(16, Integer.valueOf(j | i));
      } else {
          this.DataWatcher.watch(16, Integer.valueOf(j & ~i));
      }

  }
  
  public boolean isElder() {
      return this.a(4);
  }
  
  private void b(int i) {
      this.DataWatcher.watch(17, Integer.valueOf(i));
  }

  public boolean cp() {
      return this.DataWatcher.getInt(17) != 0;
  }
  
  public void setElder(boolean flag){
      this.a(4, flag);
  }
}