package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseAgeable;
import me.kingingo.kcore.Disguise.disguises.DisguiseAnimal;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager.Profession;

public class DisguiseVillager extends DisguiseAgeable
{
  public DisguiseVillager(Entity entity)
  {
    super(entity);
  }

  protected EntityType GetEntityTypeId()
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