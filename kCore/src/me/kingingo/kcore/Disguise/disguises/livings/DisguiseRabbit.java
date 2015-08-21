package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseAnimal;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;

public class DisguiseRabbit extends DisguiseAnimal
{
  public DisguiseRabbit(Entity entity)
  {
    super(entity);
    this.DataWatcher.a(18, Byte.valueOf((byte)0));

  }

  public Type getRabbitType() {
	  return Type.values()[this.DataWatcher.getByte(18)];
  }
  
  public void setRabbitType(Type type){
	  this.DataWatcher.watch(18, Byte.valueOf((byte)type.ordinal()));
  }
  
  public EntityType GetEntityTypeId()
  {
    return EntityType.RABBIT;
  }

  public String getHurtSound()
  {
    return "mob.rabbit.hurt";
  }
}