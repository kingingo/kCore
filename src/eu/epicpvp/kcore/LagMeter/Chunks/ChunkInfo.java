package eu.epicpvp.kcore.LagMeter.Chunks;
import org.bukkit.Chunk;
import org.bukkit.World;

public class ChunkInfo
{
  private final World world;
  public int x;
  public int z;

  public ChunkInfo(Chunk chunk)
  {
    this.x = chunk.getX();
    this.z = chunk.getZ();
    this.world = chunk.getWorld();
  }

  public ChunkInfo(World world, int x, int z) {
    this.world = world;
    this.x = x;
    this.z = z;
  }

  public String toString()
  {
    return this.world.getName() + ":" + this.x + "," + this.z;
  }

  public Chunk get() {
    return this.world.getChunkAt(this.x, this.z);
  }

  public boolean unload(boolean save) {
    return this.world.unloadChunk(this.x, this.z, true, false);
  }

  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    ChunkInfo chunkInfo = (ChunkInfo)o;
    if (this.x != chunkInfo.x) {
      return false;
    }
    if (this.z != chunkInfo.z) {
      return false;
    }
    return chunkInfo.world == null ? true : this.world != null ? this.world.equals(chunkInfo.world) : false;
  }

  public int hashCode()
  {
    int result = this.world != null ? this.world.hashCode() : 0;
    result = 31 * result + this.x;
    result = 31 * result + this.z;
    return result;
  }
}