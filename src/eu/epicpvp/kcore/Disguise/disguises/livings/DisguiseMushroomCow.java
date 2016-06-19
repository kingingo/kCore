package eu.epicpvp.kcore.Disguise.disguises.livings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DisguiseMushroomCow extends DisguiseCow
{
  public DisguiseMushroomCow(Entity entity)
  {
    super(entity);
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.MUSHROOM_COW;
  }

  public String getHurtSound()
  {
    return "mob.cow.hurt";
  }
}