package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseAnimal;
import me.kingingo.kcore.Disguise.disguises.DisguiseCreature;
import me.kingingo.kcore.Disguise.disguises.DisguiseInsentient;
import me.kingingo.kcore.PacketAPI.kPacket;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DisguiseEnderdragon extends DisguiseCreature
{
  public DisguiseEnderdragon(Entity entity)
  {
    super(entity);
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.ENDER_DRAGON;
  }

  public String getHurtSound()
  {
    return "mob.enderdragon.growl";
  }

}