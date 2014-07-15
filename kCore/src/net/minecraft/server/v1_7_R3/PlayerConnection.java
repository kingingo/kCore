package net.minecraft.server.v1_7_R3;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.logging.Level;

import net.minecraft.util.com.google.common.base.Charsets;
import net.minecraft.util.com.google.common.collect.Lists;
import net.minecraft.util.io.netty.buffer.Unpooled;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelConfig;
import net.minecraft.util.io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_7_R3.CraftServer;
import org.bukkit.craftbukkit.v1_7_R3.SpigotTimings;
import org.bukkit.craftbukkit.v1_7_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_7_R3.block.CraftSign;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R3.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_7_R3.util.CraftChatMessage;
import org.bukkit.craftbukkit.v1_7_R3.util.LazyPlayerSet;
import org.bukkit.craftbukkit.v1_7_R3.util.Waitable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Result;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.util.NumberConversions;
import org.spigotmc.CustomTimingsHandler;
import org.spigotmc.SpigotConfig;

public class PlayerConnection
  implements PacketPlayInListener
{
  private static final org.apache.logging.log4j.Logger c = LogManager.getLogger();
  public final NetworkManager networkManager;
  private final MinecraftServer minecraftServer;
  public EntityPlayer player;
  private int e;
  private int f;
  private boolean g;
  private int h;
  private long i;
  private static Random j = new Random();
  private long k;
  private volatile int chatThrottle;
  private static final AtomicIntegerFieldUpdater chatSpamField = AtomicIntegerFieldUpdater.newUpdater(PlayerConnection.class, "chatThrottle");
  private int x;
  private IntHashMap n = new IntHashMap();
  private double y;
  private double z;
  private double q;
  public boolean checkMovement = true;
  private boolean processedDisconnect;
  private final CraftServer server;
  private int lastTick = MinecraftServer.currentTick;
  private int lastDropTick = MinecraftServer.currentTick;
  private int dropCount = 0;
  private static final int SURVIVAL_PLACE_DISTANCE_SQUARED = 36;
  private static final int CREATIVE_PLACE_DISTANCE_SQUARED = 49;
  private double lastPosX = 1.7976931348623157E+308D;
  private double lastPosY = 1.7976931348623157E+308D;
  private double lastPosZ = 1.7976931348623157E+308D;
  private float lastPitch = 3.4028235E+38F;
  private float lastYaw = 3.4028235E+38F;
  public boolean justTeleported = false;
  private boolean hasMoved;
  Long lastPacket;
  private Item lastMaterial;
  private static final HashSet<Integer> invalidItems = new HashSet(Arrays.asList(new Integer[] { Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(26), Integer.valueOf(34), Integer.valueOf(36), Integer.valueOf(43), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(55), Integer.valueOf(59), Integer.valueOf(60), Integer.valueOf(62), Integer.valueOf(63), Integer.valueOf(64), Integer.valueOf(68), Integer.valueOf(71), Integer.valueOf(74), Integer.valueOf(75), Integer.valueOf(83), Integer.valueOf(90), Integer.valueOf(92), Integer.valueOf(93), Integer.valueOf(94), Integer.valueOf(104), Integer.valueOf(105), Integer.valueOf(115), Integer.valueOf(117), Integer.valueOf(118), Integer.valueOf(119), Integer.valueOf(125), Integer.valueOf(127), Integer.valueOf(132), Integer.valueOf(137), Integer.valueOf(140), Integer.valueOf(141), Integer.valueOf(142), Integer.valueOf(144) }));

  private int lastPlace = -1;

  public PlayerConnection(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer)
  {
    this.minecraftServer = minecraftserver;
    this.networkManager = networkmanager;
    networkmanager.a(this);
    this.player = entityplayer;
    entityplayer.playerConnection = this;

    this.server = minecraftserver.server;
  }

  public CraftPlayer getPlayer()
  {
    return this.player == null ? null : this.player.getBukkitEntity();
  }

  public void a()
  {
    this.g = false;
    this.e += 1;
    this.minecraftServer.methodProfiler.a("keepAlive");
    if (this.e - this.k > 40L) {
      this.k = this.e;
      this.i = d();
      this.h = ((int)this.i);
      sendPacket(new PacketPlayOutKeepAlive(this.h));
    }
    int spam;
    while (((spam = this.chatThrottle) > 0) && (!chatSpamField.compareAndSet(this, spam, spam - 1)));
    if (this.x > 0) {
      this.x -= 1;
    }

    this.minecraftServer.methodProfiler.c("playerTick");
    this.minecraftServer.methodProfiler.b();
  }

  public NetworkManager b() {
    return this.networkManager;
  }

  public void disconnect(String s)
  {
    String leaveMessage = EnumChatFormat.YELLOW + this.player.getName() + " left the game.";

    PlayerKickEvent event = new PlayerKickEvent(this.server.getPlayer(this.player), s, leaveMessage);

    if (this.server.getServer().isRunning()) {
      this.server.getPluginManager().callEvent(event);
    }

    if (event.isCancelled())
    {
      return;
    }

    s = event.getReason();

    ChatComponentText chatcomponenttext = new ChatComponentText(s);

    this.networkManager.handle(new PacketPlayOutKickDisconnect(chatcomponenttext), new GenericFutureListener[] { new PlayerConnectionFuture(this, chatcomponenttext) });
    a(chatcomponenttext);
    this.networkManager.g();
  }

  public void a(PacketPlayInSteerVehicle packetplayinsteervehicle) {
    this.player.a(packetplayinsteervehicle.c(), packetplayinsteervehicle.d(), packetplayinsteervehicle.e(), packetplayinsteervehicle.f());
  }

  public void a(PacketPlayInFlying packetplayinflying)
  {
    if ((Double.isNaN(packetplayinflying.x)) || (Double.isNaN(packetplayinflying.y)) || (Double.isNaN(packetplayinflying.z)) || (Double.isNaN(packetplayinflying.stance)))
    {
      c.warn(this.player.getName() + " was caught trying to crash the server with an invalid position.");
      getPlayer().kickPlayer("NaN in position (Hacking?)");
      return;
    }

    if (packetplayinflying.hasPos)
    {
      if ((Math.abs(packetplayinflying.c()) > 32000000.0D) || (Math.abs(packetplayinflying.e()) > 32000000.0D)) {
        disconnect("Illegal position");
        return;
      }
      this.player.locX = packetplayinflying.x;
      this.player.locY = packetplayinflying.y;
      this.player.stance = packetplayinflying.stance;
      this.player.locZ = packetplayinflying.z;
    }
    if (packetplayinflying.hasLook)
    {
      this.player.yaw = packetplayinflying.yaw;
      this.player.pitch = packetplayinflying.pitch;
    }

    double d4 = this.player.lastX - this.player.locX;
    double d5 = this.player.lastY - this.player.locY;
    double d6 = this.player.lastZ - this.player.locZ;

    double d7 = Math.max(Math.abs(d4), Math.abs(this.player.motX));
    double d8 = Math.max(Math.abs(d5), Math.abs(this.player.motY));
    double d9 = Math.max(Math.abs(d6), Math.abs(this.player.motZ));

    double d10 = d7 * d7 + d8 * d8 + d9 * d9;

    double limit = 100.0D * Math.max(1.0D, (System.nanoTime() - this.player.lastMovementTick) / 50000000.0D);

    if ((d10 > limit) && (this.checkMovement) && ((!this.minecraftServer.N()) || (!this.minecraftServer.M().equals(this.player.getName())))) {
      return;
    }

    Player player = getPlayer();

    if (!this.hasMoved)
    {
      Location curPos = player.getLocation();
      this.lastPosX = curPos.getX();
      this.lastPosY = curPos.getY();
      this.lastPosZ = curPos.getZ();
      this.lastYaw = curPos.getYaw();
      this.lastPitch = curPos.getPitch();
      this.hasMoved = true;
    }

    Location from = new Location(player.getWorld(), this.lastPosX, this.lastPosY, this.lastPosZ, this.lastYaw, this.lastPitch);
    Location to = player.getLocation().clone();

    if ((packetplayinflying.hasPos) && ((!packetplayinflying.hasPos) || (packetplayinflying.y != -999.0D) || (packetplayinflying.stance != -999.0D)))
    {
      to.setX(packetplayinflying.x);
      to.setY(packetplayinflying.y);
      to.setZ(packetplayinflying.z);
    }

    if (packetplayinflying.hasLook)
    {
      to.setYaw(packetplayinflying.yaw);
      to.setPitch(packetplayinflying.pitch);
    }

    double delta = Math.pow(this.lastPosX - to.getX(), 2.0D) + Math.pow(this.lastPosY - to.getY(), 2.0D) + Math.pow(this.lastPosZ - to.getZ(), 2.0D);
    float deltaAngle = Math.abs(this.lastYaw - to.getYaw()) + Math.abs(this.lastPitch - to.getPitch());

    if (((delta > 0.00390625D) || (deltaAngle > 10.0F)) && (this.checkMovement) && (!this.player.dead))
    {
      this.lastPosX = to.getX();
      this.lastPosY = to.getY();
      this.lastPosZ = to.getZ();
      this.lastYaw = to.getYaw();
      this.lastPitch = to.getPitch();

      PlayerMoveEvent event = new PlayerMoveEvent(player, from, to);
      this.server.getPluginManager().callEvent(event);

      if (event.isCancelled())
      {
        this.player.playerConnection.sendPacket(new PacketPlayOutPosition(from.getX(), from.getY() + 1.620000004768372D, from.getZ(), from.getYaw(), from.getPitch(), false));
        return;
      }

      if ((!to.equals(event.getTo())) && (!event.isCancelled()))
      {
        this.player.getBukkitEntity().teleport(event.getTo(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        return;
      }

      if ((!from.equals(getPlayer().getLocation())) && (this.justTeleported))
      {
        this.justTeleported = false;
        return;
      }
    }
  }

  private void noop(PacketPlayInFlying packetplayinflying)
  {
    WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);

    this.g = true;
    if (!this.player.viewingCredits)
    {
      if (!this.checkMovement) {
        double d0 = packetplayinflying.d() - this.z;
        if ((packetplayinflying.c() == this.y) && (d0 * d0 < 0.01D) && (packetplayinflying.e() == this.q)) {
          this.checkMovement = true;
        }

      }

      Player player = getPlayer();

      if (!this.hasMoved)
      {
        Location curPos = player.getLocation();
        this.lastPosX = curPos.getX();
        this.lastPosY = curPos.getY();
        this.lastPosZ = curPos.getZ();
        this.lastYaw = curPos.getYaw();
        this.lastPitch = curPos.getPitch();
        this.hasMoved = true;
      }

      Location from = new Location(player.getWorld(), this.lastPosX, this.lastPosY, this.lastPosZ, this.lastYaw, this.lastPitch);
      Location to = player.getLocation().clone();

      if ((packetplayinflying.hasPos) && ((!packetplayinflying.hasPos) || (packetplayinflying.y != -999.0D) || (packetplayinflying.stance != -999.0D))) {
        to.setX(packetplayinflying.x);
        to.setY(packetplayinflying.y);
        to.setZ(packetplayinflying.z);
      }

      if (packetplayinflying.hasLook) {
        to.setYaw(packetplayinflying.yaw);
        to.setPitch(packetplayinflying.pitch);
      }

      double delta = Math.pow(this.lastPosX - to.getX(), 2.0D) + Math.pow(this.lastPosY - to.getY(), 2.0D) + Math.pow(this.lastPosZ - to.getZ(), 2.0D);
      float deltaAngle = Math.abs(this.lastYaw - to.getYaw()) + Math.abs(this.lastPitch - to.getPitch());

      if (((delta > 0.00390625D) || (deltaAngle > 10.0F)) && (this.checkMovement) && (!this.player.dead)) {
        this.lastPosX = to.getX();
        this.lastPosY = to.getY();
        this.lastPosZ = to.getZ();
        this.lastYaw = to.getYaw();
        this.lastPitch = to.getPitch();

        PlayerMoveEvent event = new PlayerMoveEvent(player, from, to);
        this.server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
          this.player.playerConnection.sendPacket(new PacketPlayOutPosition(from.getX(), from.getY() + 1.620000004768372D, from.getZ(), from.getYaw(), from.getPitch(), false));
          return;
        }

        if ((!to.equals(event.getTo())) && (!event.isCancelled())) {
          this.player.getBukkitEntity().teleport(event.getTo(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
          return;
        }

        if ((!from.equals(getPlayer().getLocation())) && (this.justTeleported)) {
          this.justTeleported = false;
          return;
        }

      }

      if ((this.checkMovement) && (!this.player.dead))
      {
        if (this.player.vehicle != null) {
          float f = this.player.yaw;
          float f1 = this.player.pitch;

          this.player.vehicle.ab();
          double d1 = this.player.locX;
          double d2 = this.player.locY;
          double d3 = this.player.locZ;
          if (packetplayinflying.k()) {
            f = packetplayinflying.g();
            f1 = packetplayinflying.h();
          }

          this.player.onGround = packetplayinflying.i();
          this.player.i();
          this.player.V = 0.0F;
          this.player.setLocation(d1, d2, d3, f, f1);
          if (this.player.vehicle != null) {
            this.player.vehicle.ab();
          }

          this.minecraftServer.getPlayerList().d(this.player);
          if (this.checkMovement) {
            this.y = this.player.locX;
            this.z = this.player.locY;
            this.q = this.player.locZ;
          }

          worldserver.playerJoinedWorld(this.player);
          return;
        }

        if (this.player.isSleeping()) {
          this.player.i();
          this.player.setLocation(this.y, this.z, this.q, this.player.yaw, this.player.pitch);
          worldserver.playerJoinedWorld(this.player);
          return;
        }

        double d0 = this.player.locY;
        this.y = this.player.locX;
        this.z = this.player.locY;
        this.q = this.player.locZ;
        double d1 = this.player.locX;
        double d2 = this.player.locY;
        double d3 = this.player.locZ;
        float f2 = this.player.yaw;
        float f3 = this.player.pitch;

        if ((packetplayinflying.j()) && (packetplayinflying.d() == -999.0D) && (packetplayinflying.f() == -999.0D)) {
          packetplayinflying.a(false);
        }

        if (packetplayinflying.j()) {
          d1 = packetplayinflying.c();
          d2 = packetplayinflying.d();
          d3 = packetplayinflying.e();
          double d4 = packetplayinflying.f() - packetplayinflying.d();
          if ((!this.player.isSleeping()) && ((d4 > 1.65D) || (d4 < 0.1D))) {
            disconnect("Illegal stance");
            c.warn(this.player.getName() + " had an illegal stance: " + d4);
            return;
          }

          if ((Math.abs(packetplayinflying.c()) > 32000000.0D) || (Math.abs(packetplayinflying.e()) > 32000000.0D)) {
            disconnect("Illegal position");
            return;
          }
        }

        if (packetplayinflying.k()) {
          f2 = packetplayinflying.g();
          f3 = packetplayinflying.h();
        }

        this.player.i();
        this.player.V = 0.0F;
        this.player.setLocation(this.y, this.z, this.q, f2, f3);
        if (!this.checkMovement) {
          return;
        }

        double d4 = d1 - this.player.locX;
        double d5 = d2 - this.player.locY;
        double d6 = d3 - this.player.locZ;

        double d7 = Math.max(Math.abs(d4), Math.abs(this.player.motX));
        double d8 = Math.max(Math.abs(d5), Math.abs(this.player.motY));
        double d9 = Math.max(Math.abs(d6), Math.abs(this.player.motZ));

        double d10 = d7 * d7 + d8 * d8 + d9 * d9;

        if ((d10 > 100.0D) && (this.checkMovement) && ((!this.minecraftServer.N()) || (!this.minecraftServer.M().equals(this.player.getName())))) {
          c.warn(this.player.getName() + " moved too quickly! " + d4 + "," + d5 + "," + d6 + " (" + d7 + ", " + d8 + ", " + d9 + ")");
          a(this.y, this.z, this.q, this.player.yaw, this.player.pitch);
          return;
        }

        float f4 = 0.0625F;
        boolean flag = worldserver.getCubes(this.player, this.player.boundingBox.clone().shrink(f4, f4, f4)).isEmpty();

        if ((this.player.onGround) && (!packetplayinflying.i()) && (d5 > 0.0D)) {
          this.player.bi();
        }

        this.player.move(d4, d5, d6);
        this.player.onGround = packetplayinflying.i();
        this.player.checkMovement(d4, d5, d6);
        double d11 = d5;

        d4 = d1 - this.player.locX;
        d5 = d2 - this.player.locY;
        if ((d5 > -0.5D) || (d5 < 0.5D)) {
          d5 = 0.0D;
        }

        d6 = d3 - this.player.locZ;
        d10 = d4 * d4 + d5 * d5 + d6 * d6;
        boolean flag1 = false;

        if ((d10 > 0.0625D) && (!this.player.isSleeping()) && (!this.player.playerInteractManager.isCreative())) {
          flag1 = true;
          c.warn(this.player.getName() + " moved wrongly!");
        }

        this.player.setLocation(d1, d2, d3, f2, f3);
        boolean flag2 = worldserver.getCubes(this.player, this.player.boundingBox.clone().shrink(f4, f4, f4)).isEmpty();

        if ((flag) && ((flag1) || (!flag2)) && (!this.player.isSleeping())) {
          a(this.y, this.z, this.q, f2, f3);
          return;
        }

        AxisAlignedBB axisalignedbb = this.player.boundingBox.clone().grow(f4, f4, f4).a(0.0D, -0.55D, 0.0D);

        if ((!this.minecraftServer.getAllowFlight()) && (!this.player.abilities.canFly) && (!worldserver.c(axisalignedbb))) {
          if (d11 >= -0.03125D) {
            this.f += 1;
            if (this.f > 80) {
              c.warn(this.player.getName() + " was kicked for floating too long!");
              disconnect("Flying is not enabled on this server");
            }
          }
        }
        else {
          this.f = 0;
        }

        this.player.onGround = packetplayinflying.i();
        this.minecraftServer.getPlayerList().d(this.player);
        this.player.b(this.player.locY - d0, packetplayinflying.i());
      } else if (this.e % 20 == 0) {
        a(this.y, this.z, this.q, this.player.yaw, this.player.pitch);
      }
    }
  }

  public void a(double d0, double d1, double d2, float f, float f1)
  {
    Player player = getPlayer();
    Location from = player.getLocation();
    Location to = new Location(getPlayer().getWorld(), d0, d1, d2, f, f1);
    PlayerTeleportEvent event = new PlayerTeleportEvent(player, from, to, PlayerTeleportEvent.TeleportCause.UNKNOWN);
    this.server.getPluginManager().callEvent(event);

    from = event.getFrom();
    to = event.isCancelled() ? from : event.getTo();

    teleport(to);
  }

  public void teleport(Location dest)
  {
    double d0 = dest.getX();
    double d1 = dest.getY();
    double d2 = dest.getZ();
    float f = dest.getYaw();
    float f1 = dest.getPitch();

    if (Float.isNaN(f)) {
      f = 0.0F;
    }

    if (Float.isNaN(f1)) {
      f1 = 0.0F;
    }

    this.lastPosX = d0;
    this.lastPosY = d1;
    this.lastPosZ = d2;
    this.lastYaw = f;
    this.lastPitch = f1;
    this.justTeleported = true;

    this.checkMovement = false;
    this.y = d0;
    this.z = d1;
    this.q = d2;
    this.player.setLocation(d0, d1, d2, f, f1);
    this.player.playerConnection.sendPacket(new PacketPlayOutPosition(d0, d1 + 1.620000004768372D, d2, f, f1, false));
  }

  public void a(PacketPlayInBlockDig packetplayinblockdig) {
    if (this.player.dead) return;
    WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);

    this.player.v();
    if (packetplayinblockdig.g() == 4)
    {
      if (this.lastDropTick != MinecraftServer.currentTick) {
        this.dropCount = 0;
        this.lastDropTick = MinecraftServer.currentTick;
      }
      else {
        this.dropCount += 1;
        if (this.dropCount >= 20) {
          c.warn(this.player.getName() + " dropped their items too quickly!");
          disconnect("You dropped your items too quickly (Hacking?)");
          return;
        }
      }

      this.player.a(false);
    } else if (packetplayinblockdig.g() == 3) {
      this.player.a(true);
    } else if (packetplayinblockdig.g() == 5) {
      this.player.bz();
    } else {
      boolean flag = false;

      if (packetplayinblockdig.g() == 0) {
        flag = true;
      }

      if (packetplayinblockdig.g() == 1) {
        flag = true;
      }

      if (packetplayinblockdig.g() == 2) {
        flag = true;
      }

      int i = packetplayinblockdig.c();
      int j = packetplayinblockdig.d();
      int k = packetplayinblockdig.e();

      if (flag) {
        double d0 = this.player.locX - (i + 0.5D);
        double d1 = this.player.locY - (j + 0.5D) + 1.5D;
        double d2 = this.player.locZ - (k + 0.5D);
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;

        if (d3 > 36.0D) {
          return;
        }

        if (j >= this.minecraftServer.getMaxBuildHeight()) {
          return;
        }
      }

      if (packetplayinblockdig.g() == 0) {
        if (!this.minecraftServer.a(worldserver, i, j, k, this.player)) {
          this.player.playerInteractManager.dig(i, j, k, packetplayinblockdig.f());
        }
        else {
          CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, i, j, k, packetplayinblockdig.f(), this.player.inventory.getItemInHand());
          this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, worldserver));

          TileEntity tileentity = worldserver.getTileEntity(i, j, k);
          if (tileentity != null) {
            this.player.playerConnection.sendPacket(tileentity.getUpdatePacket());
          }
        }
      }
      else if (packetplayinblockdig.g() == 2) {
        this.player.playerInteractManager.a(i, j, k);
        if (worldserver.getType(i, j, k).getMaterial() != Material.AIR)
          this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, worldserver));
      }
      else if (packetplayinblockdig.g() == 1) {
        this.player.playerInteractManager.c(i, j, k);
        if (worldserver.getType(i, j, k).getMaterial() != Material.AIR)
          this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, worldserver));
      }
    }
  }

  public void a(PacketPlayInBlockPlace packetplayinblockplace)
  {
    boolean throttled = false;
    if ((this.lastPlace != -1) && (MinecraftServer.currentTick - this.lastPlace < 2)) {
      throttled = true;
    }
    else {
      this.lastPlace = MinecraftServer.currentTick;
    }

    WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);

    if (this.player.dead) return;

    if (packetplayinblockplace.getFace() == 255) {
      if ((packetplayinblockplace.getItemStack() != null) && (packetplayinblockplace.getItemStack().getItem() == this.lastMaterial) && (this.lastPacket != null) && (packetplayinblockplace.timestamp - this.lastPacket.longValue() < 100L))
        this.lastPacket = null;
    }
    else
    {
      this.lastMaterial = (packetplayinblockplace.getItemStack() == null ? null : packetplayinblockplace.getItemStack().getItem());
      this.lastPacket = Long.valueOf(packetplayinblockplace.timestamp);
    }

    boolean always = false;

    ItemStack itemstack = this.player.inventory.getItemInHand();
    boolean flag = false;
    int i = packetplayinblockplace.c();
    int j = packetplayinblockplace.d();
    int k = packetplayinblockplace.e();
    int l = packetplayinblockplace.getFace();

    this.player.v();
    if (packetplayinblockplace.getFace() == 255) {
      if (itemstack == null) {
        return;
      }

      int itemstackAmount = itemstack.count;

      if (!throttled) {
        PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.RIGHT_CLICK_AIR, itemstack);
        if (event.useItemInHand() != Event.Result.DENY) {
          this.player.playerInteractManager.useItem(this.player, this.player.world, itemstack);
        }

      }

      always = (itemstack.count != itemstackAmount) || (itemstack.getItem() == Item.getItemOf(Blocks.WATER_LILY));
    }
    else if ((packetplayinblockplace.d() >= this.minecraftServer.getMaxBuildHeight() - 1) && ((packetplayinblockplace.getFace() == 1) || (packetplayinblockplace.d() >= this.minecraftServer.getMaxBuildHeight()))) {
      ChatMessage chatmessage = new ChatMessage("build.tooHigh", new Object[] { Integer.valueOf(this.minecraftServer.getMaxBuildHeight()) });

      chatmessage.getChatModifier().setColor(EnumChatFormat.RED);
      this.player.playerConnection.sendPacket(new PacketPlayOutChat(chatmessage));
      flag = true;
    }
    else {
      Location eyeLoc = getPlayer().getEyeLocation();
      double reachDistance = NumberConversions.square(eyeLoc.getX() - i) + NumberConversions.square(eyeLoc.getY() - j) + NumberConversions.square(eyeLoc.getZ() - k);
      if (reachDistance > (getPlayer().getGameMode() == GameMode.CREATIVE ? 49 : 36)) {
        return;
      }

      if ((throttled) || (!this.player.playerInteractManager.interact(this.player, worldserver, itemstack, i, j, k, l, packetplayinblockplace.h(), packetplayinblockplace.i(), packetplayinblockplace.j()))) {
        always = true;
      }

      flag = true;
    }

    if (flag) {
      this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, worldserver));
      if (l == 0) {
        j--;
      }

      if (l == 1) {
        j++;
      }

      if (l == 2) {
        k--;
      }

      if (l == 3) {
        k++;
      }

      if (l == 4) {
        i--;
      }

      if (l == 5) {
        i++;
      }

      this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(i, j, k, worldserver));
    }

    itemstack = this.player.inventory.getItemInHand();
    if ((itemstack != null) && (itemstack.count == 0)) {
      this.player.inventory.items[this.player.inventory.itemInHandIndex] = null;
      itemstack = null;
    }

    if ((itemstack == null) || (itemstack.n() == 0)) {
      this.player.g = true;
      this.player.inventory.items[this.player.inventory.itemInHandIndex] = ItemStack.b(this.player.inventory.items[this.player.inventory.itemInHandIndex]);
      Slot slot = this.player.activeContainer.a(this.player.inventory, this.player.inventory.itemInHandIndex);

      this.player.activeContainer.b();
      this.player.g = false;

      if ((!ItemStack.matches(this.player.inventory.getItemInHand(), packetplayinblockplace.getItemStack())) || (always))
        sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, slot.rawSlotIndex, this.player.inventory.getItemInHand()));
    }
  }

  public void a(IChatBaseComponent ichatbasecomponent)
  {
    if (this.processedDisconnect) {
      return;
    }
    this.processedDisconnect = true;

    c.info(this.player.getName() + " lost connection: " + ichatbasecomponent.c());
    this.minecraftServer.az();

    this.player.n();
    String quitMessage = this.minecraftServer.getPlayerList().disconnect(this.player);
    if ((quitMessage != null) && (quitMessage.length() > 0)) {
      this.minecraftServer.getPlayerList().sendMessage(CraftChatMessage.fromString(quitMessage));
    }

    if ((this.minecraftServer.N()) && (this.player.getName().equals(this.minecraftServer.M()))) {
      c.info("Stopping singleplayer server as player logged out");
      this.minecraftServer.safeShutdown();
    }
  }

  public void sendPacket(Packet packet) {
    if ((packet instanceof PacketPlayOutChat)) {
      PacketPlayOutChat packetplayoutchat = (PacketPlayOutChat)packet;
      EnumChatVisibility enumchatvisibility = this.player.getChatFlags();

      if (enumchatvisibility == EnumChatVisibility.HIDDEN) {
        return;
      }

      if ((enumchatvisibility == EnumChatVisibility.SYSTEM) && (!packetplayoutchat.d())) {
        return;
      }

    }

    if (packet == null)
      return;
    if ((packet instanceof PacketPlayOutSpawnPosition)) {
      PacketPlayOutSpawnPosition packet6 = (PacketPlayOutSpawnPosition)packet;
      this.player.compassTarget = new Location(getPlayer().getWorld(), packet6.x, packet6.y, packet6.z);
    }

    try
    {
      this.networkManager.handle(packet, new GenericFutureListener[0]);
    } catch (Throwable throwable) {
      CrashReport crashreport = CrashReport.a(throwable, "Sending packet");
      CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Packet being sent");

      crashreportsystemdetails.a("Packet class", new CrashReportConnectionPacketClass(this, packet));
      throw new ReportedException(crashreport);
    }
  }

  public void a(PacketPlayInHeldItemSlot packetplayinhelditemslot)
  {
    if (this.player.dead) return;

    if ((packetplayinhelditemslot.c() >= 0) && (packetplayinhelditemslot.c() < PlayerInventory.getHotbarSize())) {
      PlayerItemHeldEvent event = new PlayerItemHeldEvent(getPlayer(), this.player.inventory.itemInHandIndex, packetplayinhelditemslot.c());
      this.server.getPluginManager().callEvent(event);
      if (event.isCancelled()) {
        sendPacket(new PacketPlayOutHeldItemSlot(this.player.inventory.itemInHandIndex));
        this.player.v();
        return;
      }

      this.player.inventory.itemInHandIndex = packetplayinhelditemslot.c();
      this.player.v();
    } else {
      c.warn(this.player.getName() + " tried to set an invalid carried item");
      disconnect("Invalid hotbar selection (Hacking?)");
    }
  }

  public void a(PacketPlayInChat packetplayinchat) {
    if ((this.player.dead) || (this.player.getChatFlags() == EnumChatVisibility.HIDDEN)) {
      ChatMessage chatmessage = new ChatMessage("chat.cannotSend", new Object[0]);

      chatmessage.getChatModifier().setColor(EnumChatFormat.RED);
      sendPacket(new PacketPlayOutChat(chatmessage));
    } else {
      this.player.v();
      String s = packetplayinchat.c();

      s = StringUtils.normalizeSpace(s);

      for (int i = 0; i < s.length(); i++) {
        if (!SharedConstants.isAllowedChatCharacter(s.charAt(i)))
        {
          if (packetplayinchat.a()) {
            Waitable waitable = new Waitable()
            {
              protected Object evaluate() {
                PlayerConnection.this.disconnect("Illegal characters in chat");
                return null;
              }
            };
            this.minecraftServer.processQueue.add(waitable);
            try
            {
              waitable.get();
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
              throw new RuntimeException(e);
            }
          } else {
            disconnect("Illegal characters in chat");
          }

          return;
        }

      }

      if (!packetplayinchat.a()) {
        try {
          this.minecraftServer.server.playerCommandState = true;
          handleCommand(s);
        } finally {
          this.minecraftServer.server.playerCommandState = false;
        }
      } else if (s.isEmpty()) {
        c.warn(this.player.getName() + " tried to send an empty message");
      } else if (getPlayer().isConversing()) {
        final String message = s;

        Waitable waitable = new Waitable()
        {
          protected Object evaluate() {
            PlayerConnection.this.getPlayer().acceptConversationInput(message);
            return null;
          }
        };
        this.minecraftServer.processQueue.add(waitable);
        try
        {
          waitable.get();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
          throw new RuntimeException(e);
        }
      } else if (this.player.getChatFlags() == EnumChatVisibility.SYSTEM) {
        ChatMessage chatmessage = new ChatMessage("chat.cannotSend", new Object[0]);

        chatmessage.getChatModifier().setColor(EnumChatFormat.RED);
        sendPacket(new PacketPlayOutChat(chatmessage));
      } else {
        chat(s, true);
      }

      boolean counted = true;
      for (String exclude : SpigotConfig.spamExclusions)
      {
        if ((exclude != null) && (s.startsWith(exclude)))
        {
          counted = false;
          break;
        }

      }

      if ((counted) && (chatSpamField.addAndGet(this, 20) > 200) && (!this.minecraftServer.getPlayerList().isOp(this.player.getProfile())))
        if (packetplayinchat.a()) {
          Waitable waitable = new Waitable()
          {
            protected Object evaluate() {
              PlayerConnection.this.disconnect("disconnect.spam");
              return null;
            }
          };
          this.minecraftServer.processQueue.add(waitable);
          try
          {
            waitable.get();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          } catch (ExecutionException e) {
            throw new RuntimeException(e);
          }
        } else {
          disconnect("disconnect.spam");
        }
    }
  }

  public void chat(String s, boolean async)
  {
    if ((s.isEmpty()) || (this.player.getChatFlags() == EnumChatVisibility.HIDDEN)) {
      return;
    }

    if ((!async) && (s.startsWith("/"))) {
      handleCommand(s);
    } else if (this.player.getChatFlags() != EnumChatVisibility.SYSTEM)
    {
      Player player = getPlayer();
      AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(async, player, s, new LazyPlayerSet());
      this.server.getPluginManager().callEvent(event);

      if (PlayerChatEvent.getHandlerList().getRegisteredListeners().length != 0)
      {
        final PlayerChatEvent queueEvent = new PlayerChatEvent(player, event.getMessage(), event.getFormat(), event.getRecipients());
        queueEvent.setCancelled(event.isCancelled());
        Waitable waitable = new Waitable()
        {
          protected Object evaluate() {
            Bukkit.getPluginManager().callEvent(queueEvent);

            if (queueEvent.isCancelled()) {
              return null;
            }

            String message = String.format(queueEvent.getFormat(), new Object[] { queueEvent.getPlayer().getDisplayName(), queueEvent.getMessage() });
            PlayerConnection.this.minecraftServer.console.sendMessage(message);
            Iterator i$;
            if (((LazyPlayerSet)queueEvent.getRecipients()).isLazy())
              for (i$ = PlayerConnection.this.minecraftServer.getPlayerList().players.iterator(); i$.hasNext(); ) { Object player = i$.next();
                ((EntityPlayer)player).sendMessage(CraftChatMessage.fromString(message));
              }
            else {
              for (Player player : queueEvent.getRecipients()) {
                player.sendMessage(message);
              }
            }
            return null;
          }
        };
        if (async)
          this.minecraftServer.processQueue.add(waitable);
        else
          waitable.run();
        try
        {
          waitable.get();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
          throw new RuntimeException("Exception processing chat event", e.getCause());
        }
      } else {
        if (event.isCancelled()) {
          return;
        }

        s = String.format(event.getFormat(), new Object[] { event.getPlayer().getDisplayName(), event.getMessage() });
        this.minecraftServer.console.sendMessage(s);
        Iterator i$;
        if (((LazyPlayerSet)event.getRecipients()).isLazy())
          for (i$ = this.minecraftServer.getPlayerList().players.iterator(); i$.hasNext(); ) { Object recipient = i$.next();
            ((EntityPlayer)recipient).sendMessage(CraftChatMessage.fromString(s));
          }
        else
          for (Player recipient : event.getRecipients())
            recipient.sendMessage(s);
      }
    }
  }

  private void handleCommand(String s)
  {
    SpigotTimings.playerCommandTimer.startTiming();

    if (SpigotConfig.logCommands) c.info(this.player.getName() + " issued server command: " + s);

    CraftPlayer player = getPlayer();

    PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, s, new LazyPlayerSet());
    this.server.getPluginManager().callEvent(event);

    if (event.isCancelled()) {
      SpigotTimings.playerCommandTimer.stopTiming();
      return;
    }
    try
    {
      if (this.server.dispatchCommand(event.getPlayer(), event.getMessage().substring(1))) {
        SpigotTimings.playerCommandTimer.stopTiming();
        return;
      }
    } catch (CommandException ex) {
      player.sendMessage(ChatColor.RED + "An internal error occurred while attempting to perform this command");
      java.util.logging.Logger.getLogger(PlayerConnection.class.getName()).log(Level.SEVERE, null, ex);
      SpigotTimings.playerCommandTimer.stopTiming();
      return;
    }
    SpigotTimings.playerCommandTimer.stopTiming();
  }

  public void a(PacketPlayInArmAnimation packetplayinarmanimation)
  {
    if (this.player.dead) return;
    this.player.v();
    if (packetplayinarmanimation.d() == 1)
    {
      float f = 1.0F;
      float f1 = this.player.lastPitch + (this.player.pitch - this.player.lastPitch) * f;
      float f2 = this.player.lastYaw + (this.player.yaw - this.player.lastYaw) * f;
      double d0 = this.player.lastX + (this.player.locX - this.player.lastX) * f;
      double d1 = this.player.lastY + (this.player.locY - this.player.lastY) * f + 1.62D - this.player.height;
      double d2 = this.player.lastZ + (this.player.locZ - this.player.lastZ) * f;
      Vec3D vec3d = Vec3D.a(d0, d1, d2);

      float f3 = MathHelper.cos(-f2 * 0.01745329F - 3.141593F);
      float f4 = MathHelper.sin(-f2 * 0.01745329F - 3.141593F);
      float f5 = -MathHelper.cos(-f1 * 0.01745329F);
      float f6 = MathHelper.sin(-f1 * 0.01745329F);
      float f7 = f4 * f5;
      float f8 = f3 * f5;
      double d3 = 5.0D;
      Vec3D vec3d1 = vec3d.add(f7 * d3, f6 * d3, f8 * d3);
      MovingObjectPosition movingobjectposition = this.player.world.rayTrace(vec3d, vec3d1, false);

      if ((movingobjectposition == null) || (movingobjectposition.type != EnumMovingObjectType.BLOCK)) {
        CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_AIR, this.player.inventory.getItemInHand());
      }

      PlayerAnimationEvent event = new PlayerAnimationEvent(getPlayer());
      this.server.getPluginManager().callEvent(event);

      if (event.isCancelled()) return;

      this.player.aZ();
    }
  }

  public void a(PacketPlayInEntityAction packetplayinentityaction)
  {
    if (this.player.dead) return;

    this.player.v();
    if ((packetplayinentityaction.d() == 1) || (packetplayinentityaction.d() == 2)) {
      PlayerToggleSneakEvent event = new PlayerToggleSneakEvent(getPlayer(), packetplayinentityaction.d() == 1);
      this.server.getPluginManager().callEvent(event);

      if (event.isCancelled()) {
        return;
      }
    }

    if ((packetplayinentityaction.d() == 4) || (packetplayinentityaction.d() == 5)) {
      PlayerToggleSprintEvent event = new PlayerToggleSprintEvent(getPlayer(), packetplayinentityaction.d() == 4);
      this.server.getPluginManager().callEvent(event);

      if (event.isCancelled()) {
        return;
      }

    }

    if (packetplayinentityaction.d() == 1)
      this.player.setSneaking(true);
    else if (packetplayinentityaction.d() == 2)
      this.player.setSneaking(false);
    else if (packetplayinentityaction.d() == 4)
      this.player.setSprinting(true);
    else if (packetplayinentityaction.d() == 5)
      this.player.setSprinting(false);
    else if (packetplayinentityaction.d() == 3) {
      this.player.a(false, true, true);
    }
    else if (packetplayinentityaction.d() == 6) {
      if ((this.player.vehicle != null) && ((this.player.vehicle instanceof EntityHorse)))
        ((EntityHorse)this.player.vehicle).w(packetplayinentityaction.e());
    }
    else if ((packetplayinentityaction.d() == 7) && (this.player.vehicle != null) && ((this.player.vehicle instanceof EntityHorse)))
      ((EntityHorse)this.player.vehicle).g(this.player);
  }

  public void a(PacketPlayInUseEntity packetplayinuseentity)
  {
    if (this.player.dead) return;
    WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);
    Entity entity = packetplayinuseentity.a(worldserver);

    System.out.println("NAME:"+entity.getId());
    
    if (entity == this.player)
    {
      disconnect("Cannot interact with self!");
      return;
    }

    this.player.v();
    if (entity != null) {
      boolean flag = this.player.p(entity);
      double d0 = 36.0D;

      if (!flag) {
        d0 = 9.0D;
      }

      if (this.player.f(entity) < d0) {
        ItemStack itemInHand = this.player.inventory.getItemInHand();
        if (packetplayinuseentity.c() == EnumEntityUseAction.INTERACT)
        {
          boolean triggerTagUpdate = (itemInHand != null) && (itemInHand.getItem() == Items.NAME_TAG) && ((entity instanceof EntityInsentient));
          boolean triggerChestUpdate = (itemInHand != null) && (itemInHand.getItem() == Item.getItemOf(Blocks.CHEST)) && ((entity instanceof EntityHorse));
          boolean triggerLeashUpdate = (itemInHand != null) && (itemInHand.getItem() == Items.LEASH) && ((entity instanceof EntityInsentient));
          PlayerInteractEntityEvent event = new PlayerInteractEntityEvent(getPlayer(), entity.getBukkitEntity());
          this.server.getPluginManager().callEvent(event);

          if ((triggerLeashUpdate) && ((event.isCancelled()) || (this.player.inventory.getItemInHand() == null) || (this.player.inventory.getItemInHand().getItem() != Items.LEASH)))
          {
            sendPacket(new PacketPlayOutAttachEntity(1, entity, ((EntityInsentient)entity).getLeashHolder()));
          }

          if ((triggerTagUpdate) && ((event.isCancelled()) || (this.player.inventory.getItemInHand() == null) || (this.player.inventory.getItemInHand().getItem() != Items.NAME_TAG)))
          {
            sendPacket(new PacketPlayOutEntityMetadata(entity.getId(), entity.datawatcher, true));
          }
          if ((triggerChestUpdate) && ((event.isCancelled()) || (this.player.inventory.getItemInHand() == null) || (this.player.inventory.getItemInHand().getItem() != Item.getItemOf(Blocks.CHEST)))) {
            sendPacket(new PacketPlayOutEntityMetadata(entity.getId(), entity.datawatcher, true));
          }

          if (event.isCancelled()) {
            return;
          }

          this.player.q(entity);

          if ((itemInHand != null) && (itemInHand.count <= -1)) {
            this.player.updateInventory(this.player.activeContainer);
          }
        }
        else if (packetplayinuseentity.c() == EnumEntityUseAction.ATTACK) {
          if (((entity instanceof EntityItem)) || ((entity instanceof EntityExperienceOrb)) || ((entity instanceof EntityArrow)) || (entity == this.player)) {
            disconnect("Attempting to attack an invalid entity");
            this.minecraftServer.warning("Player " + this.player.getName() + " tried to attack an invalid entity");
            return;
          }

          this.player.attack(entity);

          if ((itemInHand != null) && (itemInHand.count <= -1))
            this.player.updateInventory(this.player.activeContainer);
        }
      }
    }
  }

  public void a(PacketPlayInClientCommand packetplayinclientcommand)
  {
    this.player.v();
    EnumClientCommand enumclientcommand = packetplayinclientcommand.c();

    switch (ClientCommandOrdinalWrapper.a[enumclientcommand.ordinal()]) {
    case 1:
      if (this.player.viewingCredits) {
        this.minecraftServer.getPlayerList().changeDimension(this.player, 0, PlayerTeleportEvent.TeleportCause.END_PORTAL);
      } else if (this.player.r().getWorldData().isHardcore()) {
        if ((this.minecraftServer.N()) && (this.player.getName().equals(this.minecraftServer.M()))) {
          this.player.playerConnection.disconnect("You have died. Game over, man, it's game over!");
          this.minecraftServer.U();
        } else {
          GameProfileBanEntry gameprofilebanentry = new GameProfileBanEntry(this.player.getProfile(), (Date)null, "(You just lost the game)", (Date)null, "Death in Hardcore");

          this.minecraftServer.getPlayerList().getProfileBans().add(gameprofilebanentry);
          this.player.playerConnection.disconnect("You have died. Game over, man, it's game over!");
        }
      } else {
        if (this.player.getHealth() > 0.0F) {
          return;
        }

        this.player = this.minecraftServer.getPlayerList().moveToWorld(this.player, 0, false);
      }
      break;
    case 2:
      this.player.getStatisticManager().a(this.player);
      break;
    case 3:
      this.player.a(AchievementList.f);
    }
  }

  public void a(PacketPlayInCloseWindow packetplayinclosewindow) {
    if (this.player.dead) return;

    CraftEventFactory.handleInventoryCloseEvent(this.player);

    this.player.m();
  }

  public void a(PacketPlayInWindowClick packetplayinwindowclick) {
    if (this.player.dead) return;

    this.player.v();
    if ((this.player.activeContainer.windowId == packetplayinwindowclick.c()) && (this.player.activeContainer.c(this.player)))
    {
      if ((packetplayinwindowclick.d() < -1) && (packetplayinwindowclick.d() != -999)) {
        return;
      }

      InventoryView inventory = this.player.activeContainer.getBukkitView();
      InventoryType.SlotType type = CraftInventoryView.getSlotType(inventory, packetplayinwindowclick.d());

      InventoryClickEvent event = null;
      ClickType click = ClickType.UNKNOWN;
      InventoryAction action = InventoryAction.UNKNOWN;

      ItemStack itemstack = null;

      if (packetplayinwindowclick.d() == -1) {
        type = InventoryType.SlotType.OUTSIDE;
        click = packetplayinwindowclick.e() == 0 ? ClickType.WINDOW_BORDER_LEFT : ClickType.WINDOW_BORDER_RIGHT;
        action = InventoryAction.NOTHING;
      } else if (packetplayinwindowclick.h() == 0) {
        if (packetplayinwindowclick.e() == 0)
          click = ClickType.LEFT;
        else if (packetplayinwindowclick.e() == 1) {
          click = ClickType.RIGHT;
        }
        if ((packetplayinwindowclick.e() == 0) || (packetplayinwindowclick.e() == 1)) {
          action = InventoryAction.NOTHING;
          if (packetplayinwindowclick.d() == -999) {
            if (this.player.inventory.getCarried() != null)
              action = packetplayinwindowclick.e() == 0 ? InventoryAction.DROP_ALL_CURSOR : InventoryAction.DROP_ONE_CURSOR;
          }
          else {
            Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.d());
            if (slot != null) {
              ItemStack clickedItem = slot.getItem();
              ItemStack cursor = this.player.inventory.getCarried();
              if (clickedItem == null) {
                if (cursor != null)
                  action = packetplayinwindowclick.e() == 0 ? InventoryAction.PLACE_ALL : InventoryAction.PLACE_ONE;
              }
              else if (slot.isAllowed(this.player)) {
                if (cursor == null)
                  action = packetplayinwindowclick.e() == 0 ? InventoryAction.PICKUP_ALL : InventoryAction.PICKUP_HALF;
                else if (slot.isAllowed(cursor)) {
                  if ((clickedItem.doMaterialsMatch(cursor)) && (ItemStack.equals(clickedItem, cursor))) {
                    int toPlace = packetplayinwindowclick.e() == 0 ? cursor.count : 1;
                    toPlace = Math.min(toPlace, clickedItem.getMaxStackSize() - clickedItem.count);
                    toPlace = Math.min(toPlace, slot.inventory.getMaxStackSize() - clickedItem.count);
                    if (toPlace == 1)
                      action = InventoryAction.PLACE_ONE;
                    else if (toPlace == cursor.count)
                      action = InventoryAction.PLACE_ALL;
                    else if (toPlace < 0)
                      action = toPlace != -1 ? InventoryAction.PICKUP_SOME : InventoryAction.PICKUP_ONE;
                    else if (toPlace != 0)
                      action = InventoryAction.PLACE_SOME;
                  }
                  else if (cursor.count <= slot.getMaxStackSize()) {
                    action = InventoryAction.SWAP_WITH_CURSOR;
                  }
                } else if ((cursor.getItem() == clickedItem.getItem()) && ((!cursor.usesData()) || (cursor.getData() == clickedItem.getData())) && (ItemStack.equals(cursor, clickedItem)) && 
                  (clickedItem.count >= 0) && 
                  (clickedItem.count + cursor.count <= cursor.getMaxStackSize()))
                {
                  action = InventoryAction.PICKUP_ALL;
                }
              }
            }
          }
        }

      }
      else if (packetplayinwindowclick.h() == 1) {
        if (packetplayinwindowclick.e() == 0)
          click = ClickType.SHIFT_LEFT;
        else if (packetplayinwindowclick.e() == 1) {
          click = ClickType.SHIFT_RIGHT;
        }
        if ((packetplayinwindowclick.e() == 0) || (packetplayinwindowclick.e() == 1)) {
          if (packetplayinwindowclick.d() < 0) {
            action = InventoryAction.NOTHING;
          } else {
            Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.d());
            if ((slot != null) && (slot.isAllowed(this.player)) && (slot.hasItem()))
              action = InventoryAction.MOVE_TO_OTHER_INVENTORY;
            else
              action = InventoryAction.NOTHING;
          }
        }
      }
      else if (packetplayinwindowclick.h() == 2) {
        if ((packetplayinwindowclick.e() >= 0) && (packetplayinwindowclick.e() < 9)) {
          click = ClickType.NUMBER_KEY;
          Slot clickedSlot = this.player.activeContainer.getSlot(packetplayinwindowclick.d());
          if (clickedSlot.isAllowed(this.player)) {
            ItemStack hotbar = this.player.inventory.getItem(packetplayinwindowclick.e());
            boolean canCleanSwap = (hotbar == null) || ((clickedSlot.inventory == this.player.inventory) && (clickedSlot.isAllowed(hotbar)));
            if (clickedSlot.hasItem()) {
              if (canCleanSwap) {
                action = InventoryAction.HOTBAR_SWAP;
              } else {
                int firstEmptySlot = this.player.inventory.getFirstEmptySlotIndex();
                if (firstEmptySlot > -1)
                  action = InventoryAction.HOTBAR_MOVE_AND_READD;
                else
                  action = InventoryAction.NOTHING;
              }
            }
            else if ((!clickedSlot.hasItem()) && (hotbar != null) && (clickedSlot.isAllowed(hotbar)))
              action = InventoryAction.HOTBAR_SWAP;
            else
              action = InventoryAction.NOTHING;
          }
          else {
            action = InventoryAction.NOTHING;
          }

          event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.d(), click, action, packetplayinwindowclick.e());
        }
      } else if (packetplayinwindowclick.h() == 3) {
        if (packetplayinwindowclick.e() == 2) {
          click = ClickType.MIDDLE;
          if (packetplayinwindowclick.d() == -999) {
            action = InventoryAction.NOTHING;
          } else {
            Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.d());
            if ((slot != null) && (slot.hasItem()) && (this.player.abilities.canInstantlyBuild) && (this.player.inventory.getCarried() == null))
              action = InventoryAction.CLONE_STACK;
            else
              action = InventoryAction.NOTHING;
          }
        }
        else {
          click = ClickType.UNKNOWN;
          action = InventoryAction.UNKNOWN;
        }
      } else if (packetplayinwindowclick.h() == 4) {
        if (packetplayinwindowclick.d() >= 0) {
          if (packetplayinwindowclick.e() == 0) {
            click = ClickType.DROP;
            Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.d());
            if ((slot != null) && (slot.hasItem()) && (slot.isAllowed(this.player)) && (slot.getItem() != null) && (slot.getItem().getItem() != Item.getItemOf(Blocks.AIR)))
              action = InventoryAction.DROP_ONE_SLOT;
            else
              action = InventoryAction.NOTHING;
          }
          else if (packetplayinwindowclick.e() == 1) {
            click = ClickType.CONTROL_DROP;
            Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.d());
            if ((slot != null) && (slot.hasItem()) && (slot.isAllowed(this.player)) && (slot.getItem() != null) && (slot.getItem().getItem() != Item.getItemOf(Blocks.AIR)))
              action = InventoryAction.DROP_ALL_SLOT;
            else
              action = InventoryAction.NOTHING;
          }
        }
        else
        {
          click = ClickType.LEFT;
          if (packetplayinwindowclick.e() == 1) {
            click = ClickType.RIGHT;
          }
          action = InventoryAction.NOTHING;
        }
      } else if (packetplayinwindowclick.h() == 5) {
        itemstack = this.player.activeContainer.clickItem(packetplayinwindowclick.d(), packetplayinwindowclick.e(), 5, this.player);
      } else if (packetplayinwindowclick.h() == 6) {
        click = ClickType.DOUBLE_CLICK;
        action = InventoryAction.NOTHING;
        if ((packetplayinwindowclick.d() >= 0) && (this.player.inventory.getCarried() != null)) {
          ItemStack cursor = this.player.inventory.getCarried();
          action = InventoryAction.NOTHING;

          if ((inventory.getTopInventory().contains(org.bukkit.Material.getMaterial(Item.b(cursor.getItem())))) || (inventory.getBottomInventory().contains(org.bukkit.Material.getMaterial(Item.b(cursor.getItem()))))) {
            action = InventoryAction.COLLECT_TO_CURSOR;
          }
        }

      }

      if (packetplayinwindowclick.h() != 5) {
        if (click == ClickType.NUMBER_KEY)
          event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.d(), click, action, packetplayinwindowclick.e());
        else {
          event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.d(), click, action);
        }

        Inventory top = inventory.getTopInventory();
        if ((packetplayinwindowclick.d() == 0) && ((top instanceof CraftingInventory))) {
          Recipe recipe = ((CraftingInventory)top).getRecipe();
          if (recipe != null) {
            if (click == ClickType.NUMBER_KEY)
              event = new CraftItemEvent(recipe, inventory, type, packetplayinwindowclick.d(), click, action, packetplayinwindowclick.e());
            else {
              event = new CraftItemEvent(recipe, inventory, type, packetplayinwindowclick.d(), click, action);
            }
          }
        }

        this.server.getPluginManager().callEvent(event);

        switch (event.getResult().ordinal()) {
        case 1:
        case 2:
          itemstack = this.player.activeContainer.clickItem(packetplayinwindowclick.d(), packetplayinwindowclick.e(), packetplayinwindowclick.h(), this.player);
          break;
        case 3:
          switch (action.ordinal())
          {
          case 1:
          case 2:
          case 3:
          case 4:
          case 5:
          case 6:
            this.player.updateInventory(this.player.activeContainer);
            break;
          case 7:
          case 8:
          case 9:
          case 10:
          case 11:
          case 12:
          case 13:
            this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.player.inventory.getCarried()));
            this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, packetplayinwindowclick.d(), this.player.activeContainer.getSlot(packetplayinwindowclick.d()).getItem()));
            break;
          case 14:
          case 15:
            this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, packetplayinwindowclick.d(), this.player.activeContainer.getSlot(packetplayinwindowclick.d()).getItem()));
            break;
          case 16:
          case 17:
          case 18:
            this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.player.inventory.getCarried()));
            break;
          case 19:
          }

          return;
        }

      }

      if (ItemStack.matches(packetplayinwindowclick.g(), itemstack)) {
        this.player.playerConnection.sendPacket(new PacketPlayOutTransaction(packetplayinwindowclick.c(), packetplayinwindowclick.f(), true));
        this.player.g = true;
        this.player.activeContainer.b();
        this.player.broadcastCarriedItem();
        this.player.g = false;
      } else {
        this.n.a(this.player.activeContainer.windowId, Short.valueOf(packetplayinwindowclick.f()));
        this.player.playerConnection.sendPacket(new PacketPlayOutTransaction(packetplayinwindowclick.c(), packetplayinwindowclick.f(), false));
        this.player.activeContainer.a(this.player, false);
        ArrayList arraylist = new ArrayList();

        for (int i = 0; i < this.player.activeContainer.c.size(); i++) {
          arraylist.add(((Slot)this.player.activeContainer.c.get(i)).getItem());
        }

        this.player.a(this.player.activeContainer, arraylist);

        if ((type == InventoryType.SlotType.RESULT) && (itemstack != null))
          this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, 0, itemstack));
      }
    }
  }

  public void a(PacketPlayInEnchantItem packetplayinenchantitem)
  {
    this.player.v();
    if ((this.player.activeContainer.windowId == packetplayinenchantitem.c()) && (this.player.activeContainer.c(this.player))) {
      this.player.activeContainer.a(this.player, packetplayinenchantitem.d());
      this.player.activeContainer.b();
    }
  }

  public void a(PacketPlayInSetCreativeSlot packetplayinsetcreativeslot) {
    if (this.player.playerInteractManager.isCreative()) {
      boolean flag = packetplayinsetcreativeslot.c() < 0;
      ItemStack itemstack = packetplayinsetcreativeslot.getItemStack();
      boolean flag1 = (packetplayinsetcreativeslot.c() >= 1) && (packetplayinsetcreativeslot.c() < 36 + PlayerInventory.getHotbarSize());

      boolean flag2 = (itemstack == null) || ((itemstack.getItem() != null) && ((!invalidItems.contains(Integer.valueOf(Item.b(itemstack.getItem())))) || (!SpigotConfig.filterCreativeItems)));
      boolean flag3 = (itemstack == null) || ((itemstack.getData() >= 0) && (itemstack.count <= 64) && (itemstack.count > 0));

      if ((flag) || ((flag1) && (!ItemStack.matches(this.player.defaultContainer.getSlot(packetplayinsetcreativeslot.c()).getItem(), packetplayinsetcreativeslot.getItemStack()))))
      {
        HumanEntity player = this.player.getBukkitEntity();
        InventoryView inventory = new CraftInventoryView(player, player.getInventory(), this.player.defaultContainer);
        org.bukkit.inventory.ItemStack item = CraftItemStack.asBukkitCopy(packetplayinsetcreativeslot.getItemStack());

        InventoryType.SlotType type = InventoryType.SlotType.QUICKBAR;
        if (flag)
          type = InventoryType.SlotType.OUTSIDE;
        else if (packetplayinsetcreativeslot.c() < 36) {
          if ((packetplayinsetcreativeslot.c() >= 5) && (packetplayinsetcreativeslot.c() < 9))
            type = InventoryType.SlotType.ARMOR;
          else {
            type = InventoryType.SlotType.CONTAINER;
          }
        }
        InventoryCreativeEvent event = new InventoryCreativeEvent(inventory, type, flag ? -999 : packetplayinsetcreativeslot.c(), item);
        this.server.getPluginManager().callEvent(event);

        itemstack = CraftItemStack.asNMSCopy(event.getCursor());

        switch (event.getResult().ordinal())
        {
        case 1:
          flag2 = flag3 = true;
          break;
        case 2:
          break;
        case 3:
          if (packetplayinsetcreativeslot.c() >= 0) {
            this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.defaultContainer.windowId, packetplayinsetcreativeslot.c(), this.player.defaultContainer.getSlot(packetplayinsetcreativeslot.c()).getItem()));
            this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, null));
          }
          return;
        }

      }

      if ((flag1) && (flag2) && (flag3)) {
        if (itemstack == null)
          this.player.defaultContainer.setItem(packetplayinsetcreativeslot.c(), (ItemStack)null);
        else {
          this.player.defaultContainer.setItem(packetplayinsetcreativeslot.c(), itemstack);
        }

        this.player.defaultContainer.a(this.player, true);
      } else if ((flag) && (flag2) && (flag3) && (this.x < 200)) {
        this.x += 20;
        EntityItem entityitem = this.player.drop(itemstack, true);

        if (entityitem != null)
          entityitem.e();
      }
    }
  }

  public void a(PacketPlayInTransaction packetplayintransaction)
  {
    if (this.player.dead) return;
    Short oshort = (Short)this.n.get(this.player.activeContainer.windowId);

    if ((oshort != null) && (packetplayintransaction.d() == oshort.shortValue()) && (this.player.activeContainer.windowId == packetplayintransaction.c()) && (!this.player.activeContainer.c(this.player)))
      this.player.activeContainer.a(this.player, true);
  }

  public void a(PacketPlayInUpdateSign packetplayinupdatesign)
  {
    if (this.player.dead) return;

    this.player.v();
    WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);

    if (worldserver.isLoaded(packetplayinupdatesign.c(), packetplayinupdatesign.d(), packetplayinupdatesign.e())) {
      TileEntity tileentity = worldserver.getTileEntity(packetplayinupdatesign.c(), packetplayinupdatesign.d(), packetplayinupdatesign.e());

      if ((tileentity instanceof TileEntitySign)) {
        TileEntitySign tileentitysign = (TileEntitySign)tileentity;

        if ((!tileentitysign.a()) || (tileentitysign.b() != this.player)) {
          this.minecraftServer.warning("Player " + this.player.getName() + " just tried to change non-editable sign");
          sendPacket(new PacketPlayOutUpdateSign(packetplayinupdatesign.c(), packetplayinupdatesign.d(), packetplayinupdatesign.e(), tileentitysign.lines));
          return;
        }

      }

      for (int j = 0; j < 4; j++) {
        boolean flag = true;
        packetplayinupdatesign.f()[j] = packetplayinupdatesign.f()[j].replaceAll("?", "").replaceAll("?", "");

        if (packetplayinupdatesign.f()[j].length() > 15)
          flag = false;
        else {
          for (int i = 0; i < packetplayinupdatesign.f()[j].length(); i++) {
            if (!SharedConstants.isAllowedChatCharacter(packetplayinupdatesign.f()[j].charAt(i))) {
              flag = false;
            }
          }
        }

        if (!flag) {
          packetplayinupdatesign.f()[j] = "!?";
        }
      }

      if ((tileentity instanceof TileEntitySign)) {
        int j = packetplayinupdatesign.c();
        int k = packetplayinupdatesign.d();

        int i = packetplayinupdatesign.e();
        TileEntitySign tileentitysign1 = (TileEntitySign)tileentity;

        Player player = this.server.getPlayer(this.player);
        SignChangeEvent event = new SignChangeEvent((CraftBlock)player.getWorld().getBlockAt(j, k, i), this.server.getPlayer(this.player), packetplayinupdatesign.f());
        this.server.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
          tileentitysign1.lines = CraftSign.sanitizeLines(event.getLines());
          tileentitysign1.isEditable = false;
        }

        tileentitysign1.update();
        worldserver.notify(j, k, i);
      }
    }
  }

  public void a(PacketPlayInKeepAlive packetplayinkeepalive) {
    if (packetplayinkeepalive.c() == this.h) {
      int i = (int)(d() - this.i);

      this.player.ping = ((this.player.ping * 3 + i) / 4);
    }
  }

  private long d() {
    return System.nanoTime() / 1000000L;
  }

  public void a(PacketPlayInAbilities packetplayinabilities)
  {
    if ((this.player.abilities.canFly) && (this.player.abilities.isFlying != packetplayinabilities.isFlying())) {
      PlayerToggleFlightEvent event = new PlayerToggleFlightEvent(this.server.getPlayer(this.player), packetplayinabilities.isFlying());
      this.server.getPluginManager().callEvent(event);
      if (!event.isCancelled())
        this.player.abilities.isFlying = packetplayinabilities.isFlying();
      else
        this.player.updateAbilities();
    }
  }

  public void a(PacketPlayInTabComplete packetplayintabcomplete)
  {
    ArrayList arraylist = Lists.newArrayList();
    Iterator iterator = this.minecraftServer.a(this.player, packetplayintabcomplete.c()).iterator();

    while (iterator.hasNext()) {
      String s = (String)iterator.next();

      arraylist.add(s);
    }

    this.player.playerConnection.sendPacket(new PacketPlayOutTabComplete((String[])arraylist.toArray(new String[arraylist.size()])));
  }

  public void a(PacketPlayInSettings packetplayinsettings) {
    this.player.a(packetplayinsettings);
  }

  public void a(PacketPlayInCustomPayload packetplayincustompayload)
  {
    if (packetplayincustompayload.length <= 0) {
      return;
    }

    if ("MC|BEdit".equals(packetplayincustompayload.c())) {
      PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.wrappedBuffer(packetplayincustompayload.e()));
      try
      {
        ItemStack itemstack = packetdataserializer.c();
        if (itemstack == null)
        {
          return;
        }
        if (!ItemBookAndQuill.a(itemstack.getTag())) {
          throw new IOException("Invalid book tag!");
        }

        ItemStack itemstack1 = this.player.inventory.getItemInHand();
        if (itemstack1 != null) {
          if ((itemstack.getItem() == Items.BOOK_AND_QUILL) && (itemstack.getItem() == itemstack1.getItem())) {
            CraftEventFactory.handleEditBookEvent(this.player, itemstack);
          }
          return;
        }
      } catch (Exception exception) {
        c.error("Couldn't handle book info", exception);
        disconnect("Invalid book data!");
        return;
      }
      finally {
        packetdataserializer.release();
      }

      return;
    }if ("MC|BSign".equals(packetplayincustompayload.c())) {
      PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.wrappedBuffer(packetplayincustompayload.e()));
      try
      {
        ItemStack itemstack = packetdataserializer.c();
        if (itemstack != null) {
          if (!ItemWrittenBook.a(itemstack.getTag())) {
            throw new IOException("Invalid book tag!");
          }

          ItemStack itemstack1 = this.player.inventory.getItemInHand();
          if (itemstack1 == null)
          {
            return;
          }
          if ((itemstack.getItem() == Items.WRITTEN_BOOK) && (itemstack1.getItem() == Items.BOOK_AND_QUILL)) {
            CraftEventFactory.handleEditBookEvent(this.player, itemstack);
          }
          return;
        }
      } catch (Throwable exception1) {
        c.error("Couldn't sign book", exception1);
        disconnect("Invalid book data!");
        return;
      }
      finally {
        packetdataserializer.release();
      }
      return;
    }
    DataInputStream datainputstream;
    int i;
    if ("MC|TrSel".equals(packetplayincustompayload.c())) {
      try {
        datainputstream = new DataInputStream(new ByteArrayInputStream(packetplayincustompayload.e()));
        i = datainputstream.readInt();
        Container container = this.player.activeContainer;

        if ((container instanceof ContainerMerchant))
          ((ContainerMerchant)container).e(i);
      }
      catch (Throwable exception2)
      {
        c.error("Couldn't select trade", exception2);
        disconnect("Invalid trade data!");
      }
    }
    else if ("MC|AdvCdm".equals(packetplayincustompayload.c())) {
      if (!this.minecraftServer.getEnableCommandBlock()) {
        this.player.sendMessage(new ChatMessage("advMode.notEnabled", new Object[0]));
      } else if ((this.player.a(2, "")) && (this.player.abilities.canInstantlyBuild)) {
        PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.wrappedBuffer(packetplayincustompayload.e()));
        try
        {
          byte b0 = packetdataserializer.readByte();
          CommandBlockListenerAbstract commandblocklistenerabstract = null;

          if (b0 == 0) {
            TileEntity tileentity = this.player.world.getTileEntity(packetdataserializer.readInt(), packetdataserializer.readInt(), packetdataserializer.readInt());

            if ((tileentity instanceof TileEntityCommand))
              commandblocklistenerabstract = ((TileEntityCommand)tileentity).a();
          }
          else if (b0 == 1) {
            Entity entity = this.player.world.getEntity(packetdataserializer.readInt());

            if ((entity instanceof EntityMinecartCommandBlock)) {
              commandblocklistenerabstract = ((EntityMinecartCommandBlock)entity).e();
            }
          }

          String s = packetdataserializer.c(packetdataserializer.readableBytes());

          if (commandblocklistenerabstract != null) {
            commandblocklistenerabstract.a(s);
            commandblocklistenerabstract.e();
            this.player.sendMessage(new ChatMessage("advMode.setCommand.success", new Object[] { s }));
          }
        }
        catch (Throwable exception3) {
          c.error("Couldn't set command block", exception3);
          disconnect("Invalid CommandBlock data!");
        }
        finally {
          packetdataserializer.release();
        }
      } else {
        this.player.sendMessage(new ChatMessage("advMode.notAllowed", new Object[0]));
      }
    } else if ("MC|Beacon".equals(packetplayincustompayload.c())) {
      if ((this.player.activeContainer instanceof ContainerBeacon)) {
        try {
          datainputstream = new DataInputStream(new ByteArrayInputStream(packetplayincustompayload.e()));
          i = datainputstream.readInt();
          int j = datainputstream.readInt();
          ContainerBeacon containerbeacon = (ContainerBeacon)this.player.activeContainer;
          Slot slot = containerbeacon.getSlot(0);

          if (slot.hasItem()) {
            slot.a(1);
            TileEntityBeacon tileentitybeacon = containerbeacon.e();

            tileentitybeacon.d(i);
            tileentitybeacon.e(j);
            tileentitybeacon.update();
          }
        }
        catch (Throwable exception4) {
          c.error("Couldn't set beacon", exception4);
          disconnect("Invalid beacon data!");
        }
      }
    }
    else if (("MC|ItemName".equals(packetplayincustompayload.c())) && ((this.player.activeContainer instanceof ContainerAnvil))) {
      ContainerAnvil containeranvil = (ContainerAnvil)this.player.activeContainer;

      if ((packetplayincustompayload.e() != null) && (packetplayincustompayload.e().length >= 1)) {
        String s1 = SharedConstants.a(new String(packetplayincustompayload.e(), Charsets.UTF_8));

        if (s1.length() <= 30)
          containeranvil.a(s1);
      }
      else {
        containeranvil.a("");
      }

    }
    else if (packetplayincustompayload.c().equals("REGISTER")) {
      try {
        String channels = new String(packetplayincustompayload.e(), "UTF8");
        for (String channel : channels.split(""))
          getPlayer().addChannel(channel);
      }
      catch (UnsupportedEncodingException ex) {
        throw new AssertionError(ex);
      }
    } else if (packetplayincustompayload.c().equals("UNREGISTER")) {
      try {
        String channels = new String(packetplayincustompayload.e(), "UTF8");
        for (String channel : channels.split(""))
          getPlayer().removeChannel(channel);
      }
      catch (UnsupportedEncodingException ex) {
        throw new AssertionError(ex);
      }
    } else {
      this.server.getMessenger().dispatchIncomingMessage(this.player.getBukkitEntity(), packetplayincustompayload.c(), packetplayincustompayload.e());
    }
  }

  public void a(EnumProtocol enumprotocol, EnumProtocol enumprotocol1)
  {
    if (enumprotocol1 != EnumProtocol.PLAY)
      throw new IllegalStateException("Unexpected change in protocol!");
  }

  public boolean isDisconnected()
  {
    return (!this.player.joining) && (!NetworkManager.a(this.networkManager).config().isAutoRead());
  }
}