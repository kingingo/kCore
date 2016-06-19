package eu.epicpvp.kcore.Disguise.disguises.livings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager.Profession;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseAgeable;

public class DisguiseVillager extends DisguiseAgeable
{
  public DisguiseVillager(Entity entity)
  {
    super(entity);
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.VILLAGER;
  }
  
  public void setProfession(Profession i) {
	  this.DataWatcher.watch(16, Integer.valueOf(i.getId()));
  }

  public Profession getProfession() {
	  return Profession.getProfession(Math.max(this.DataWatcher.getInt(16) % 5, 0));
  }

  public String getHurtSound()
  {
    return "mob.villager.haggle";
  }
}