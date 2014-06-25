package me.kingingo.kcore.Util;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.util.org.apache.commons.lang3.ArrayUtils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CustomCommandExecutor
  implements CommandExecutor
{
  public static final String version = "2.1.2";
  final int commandIndex;
  static final boolean DEBUG = false;
  private HashMap<String, TreeMap<Integer, MethodWrapper>> methods = new HashMap();

  private HashMap<String, Map<String, TreeMap<Integer, MethodWrapper>>> subCmdMethods = new HashMap();
  final Plugin plugin;
  final Logger log;
  int useAlias = -1;

  protected PriorityQueue<MethodWrapper> usage = new PriorityQueue(2, new Comparator()
  {
    public int compare(CustomCommandExecutor.MethodWrapper mw1, CustomCommandExecutor.MethodWrapper mw2) {
      CustomCommandExecutor.MCCommand cmd1 = mw1.getCommand();
      CustomCommandExecutor.MCCommand cmd2 = mw2.getCommand();

      int c = new Float(mw1.getHelpOrder()).compareTo(Float.valueOf(mw2.getHelpOrder()));
      if (c != 0) return c;
      c = new Integer(cmd1.order()).compareTo(Integer.valueOf(cmd2.order()));
      return c != 0 ? c : new Integer(cmd1.hashCode()).compareTo(Integer.valueOf(cmd2.hashCode()));
    }

	@Override
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
  });
  static final String DEFAULT_CMD = "_dcmd_";
  public static final String ONLY_INGAME = ChatColor.RED + "You need to be in game to use this command";
  static final int LINES_PER_PAGE = 8;

  protected CustomCommandExecutor(Plugin plugin)
  {
    this(plugin, 0);
  }

  protected CustomCommandExecutor(Plugin plugin, int commandIndex) {
    this.plugin = plugin;
    this.log = plugin.getLogger();
    this.commandIndex = commandIndex;
    addMethods(this, getClass().getMethods());
  }

  protected void showHelp(CommandSender sender, Command command)
  {
    showHelp(sender, command, null);
  }

  protected void showHelp(CommandSender sender, Command command, String[] args) {
    help(sender, command, args);
  }

  protected boolean validCommandSenderClass(Class<?> clazz) {
    return (clazz != CommandSender.class) || (clazz != Player.class);
  }

  public void addMethods(Object obj, Method[] methodArray)
  {
    for (Method method : methodArray) {
      MCCommand mc = (MCCommand)method.getAnnotation(MCCommand.class);
      if (mc != null)
      {
        Class[] types = method.getParameterTypes();
        if ((types.length == 0) || (!validCommandSenderClass(types[0]))) {
          System.err.println("MCCommands must start with a CommandSender,Player, or ArenaPlayer");
        }
        else if (mc.cmds().length == 0) {
          addMethod(obj, method, mc, "_dcmd_");
        }
        else
          for (String cmd : mc.cmds())
            addMethod(obj, method, mc, cmd.toLowerCase());
      }
    }
  }

  private void addMethod(Object obj, Method method, MCCommand mc, String cmd)
  {
    int ml = method.getParameterTypes().length;
    if (mc.subCmds().length == 0) {
      TreeMap mthds = (TreeMap)this.methods.get(cmd);
      if (mthds == null) {
        mthds = new TreeMap();
      }
      int order = (mc.order() != -1 ? mc.order() * 100000 : 2147483647) - ml * 100 - mthds.size();
      MethodWrapper mw = new MethodWrapper(obj, method);
      mthds.put(Integer.valueOf(order), mw);
      this.methods.put(cmd, mthds);
      addUsage(mw, mc);
    } else {
      Map basemthds = (Map)this.subCmdMethods.get(cmd);
      if (basemthds == null) {
        basemthds = new HashMap();
        this.subCmdMethods.put(cmd, basemthds);
      }
      for (String subcmd : mc.subCmds()) {
        TreeMap mthds = (TreeMap)basemthds.get(subcmd);
        if (mthds == null) {
          mthds = new TreeMap();
          basemthds.put(subcmd, mthds);
        }
        int order = (mc.order() != -1 ? mc.order() * 100000 : 2147483647) - ml * 100 - mthds.size();
        MethodWrapper mw = new MethodWrapper(obj, method);

        if (mc.helpOrder() == 2.147484E+009F) {
          mw.helpOrder = Float.valueOf(2147483647 - this.usage.size());
        }
        mthds.put(Integer.valueOf(order), mw);
        addUsage(mw, mc);
      }
    }
  }

  private void addUsage(MethodWrapper method, MCCommand mc) {
    if (!mc.usage().isEmpty())
      method.usage = mc.usage();
    else {
      method.usage = createUsage(method.method);
    }
    this.usage.add(method);
  }

  private String createUsage(Method method) {
    MCCommand cmd = (MCCommand)method.getAnnotation(MCCommand.class);
    List str = new ArrayList();
    String thecmd = cmd.cmds().length > 0 ? cmd.cmds()[0] : "";
    String thesubcmd = cmd.subCmds().length > 0 ? cmd.subCmds()[0] : null;

    Class[] types = method.getParameterTypes();
    if (this.commandIndex == 0) {
      str.add(thecmd);
      if (thesubcmd != null) {
        str.add(thesubcmd);
      }
    }
    for (int i = 1; i < types.length; i++) {
      Class theclass = types[i];
      str.add(getUsageString(theclass));
      if (i == this.commandIndex) {
        str.add(thecmd);
        if (thesubcmd != null) {
          str.add(thesubcmd);
        }
      }
    }
    return StringUtils.join(str, " ");
  }

  protected String getUsageString(Class<?> clazz) {
    if (Player.class == clazz)
      return "<player>";
    if (OfflinePlayer.class == clazz)
      return "<player>";
    if (Location.class == clazz)
      return "<location>";
    if (String.class == clazz)
      return "<string>";
    if ((Integer.class == clazz) || (Integer.TYPE == clazz))
      return "<int>";
    if ((Float.class == clazz) || (Float.TYPE == clazz))
      return "<number>";
    if ((Double.class == clazz) || (Double.TYPE == clazz))
      return "<number>";
    if ((Short.class == clazz) || (Short.TYPE == clazz))
      return "<int>";
    if ((Boolean.class == clazz) || (Boolean.TYPE == clazz))
      return "<true|false>";
    if ((String.class == clazz) || (Object.class == clazz))
      return "[string ...]";
    if (GameMode.class == clazz)
      return "<gamemode>";
    if (ItemStack.class == clazz) {
      return "<item>";
    }

    return "<string> ";
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    TreeMap<String,MethodWrapper> methodmap = null;

    if (((args.length == 0) && (!this.methods.containsKey("_dcmd_"))) || ((args.length > 0) && ((args[0].equals("?")) || (args[0].equals("help")))))
    {
      showHelp(sender, command, args);
      return true;
    }
    int length = args.length;
    String cmd = length > this.commandIndex ? args[this.commandIndex].toLowerCase() : null;
    String subcmd = length > this.commandIndex + 1 ? args[(this.commandIndex + 1)].toLowerCase() : null;
    boolean hasCmd = false;
    boolean hasSubCmd = false;

    if ((subcmd != null) && (this.subCmdMethods.containsKey(cmd)) && (((Map)this.subCmdMethods.get(cmd)).containsKey(subcmd))) {
      methodmap = (TreeMap)((Map)this.subCmdMethods.get(cmd)).get(subcmd);
      hasSubCmd = true;
    }
    if ((methodmap == null) && (cmd != null)) {
      methodmap = (TreeMap)this.methods.get(cmd);
      if (methodmap != null) {
        hasCmd = true;
      }
    }
    if (methodmap == null) {
      methodmap = (TreeMap)this.methods.get("_dcmd_");
    }

    if ((methodmap == null) || (methodmap.isEmpty())) {
      return sendMessage(sender, "&cThat command does not exist!&6 /" + command.getLabel() + " help &c for help");
    }

    List<CommandException> errs = null;
    boolean success = false;
    for (MethodWrapper mwrapper : methodmap.values()) {
      MCCommand mccmd = (MCCommand)mwrapper.method.getAnnotation(MCCommand.class);
      boolean isOp = (sender == null) || (sender.isOp()) || ((sender instanceof ConsoleCommandSender));

      if (((!mccmd.op()) || (isOp)) && ((!mccmd.admin()) || (hasAdminPerms(sender))))
      {
        Arguments newArgs = null;
        try {
          newArgs = verifyArgs(mwrapper, mccmd, sender, command, label, args, hasCmd, hasSubCmd);

          Object completed = invoke(mwrapper, newArgs);
          if ((completed != null) && ((completed instanceof Boolean))) {
            success = ((Boolean)completed).booleanValue();
            if (!success) {
              String usage = mwrapper.usage;
              if ((usage != null) && (!usage.isEmpty()))
                sendMessage(sender, usage);
            }
          } else {
            success = true;
          }
        }
        catch (IllegalArgumentException e) {
          if (errs == null)
            errs = new ArrayList();
          errs.add(new CommandException(e, mwrapper));
        } catch (Exception e) {
          logInvocationError(e, mwrapper, newArgs);
        }
      }
    }
    if ((!success) && (errs != null) && (!errs.isEmpty())) {
      HashSet<String> usages = new HashSet();
      for (CommandException e : errs) {
        usages.add(ChatColor.GOLD + e.mw.usage + " &c:" + e.err.getMessage());
      }
      for (String msg : usages)
        sendMessage(sender, msg);
    }
    return true;
  }

  protected Object invoke(MethodWrapper mwrapper, Arguments args) throws InvocationTargetException, IllegalAccessException
  {
    return mwrapper.method.invoke(mwrapper.obj, args.args);
  }

  private void logInvocationError(Exception e, MethodWrapper mwrapper, Arguments newArgs) {
    System.err.println("[" + this.plugin.getName() + " Error] " + mwrapper.method + " : " + mwrapper.obj + "  : " + newArgs);
    if ((newArgs != null) && (newArgs.args != null)) {
      for (Object o : newArgs.args)
        System.err.println("[Error] object=" + o);
    }
    System.err.println("[Error] Cause=" + e.getCause());
    if (e.getCause() != null) {
      e.getCause().printStackTrace();
      this.log.log(Level.SEVERE, null, e.getCause());
    }
    System.err.println("[Error] Trace Continued ");
    this.log.log(Level.SEVERE, null, e);
  }

  protected Arguments verifyArgs(MethodWrapper mwrapper, MCCommand cmd, CommandSender sender, Command command, String label, String[] args, boolean hasCmd, boolean hasSubCmd)
    throws IllegalArgumentException
  {
    int paramLength = mwrapper.method.getParameterTypes().length;

    if ((!cmd.perm().isEmpty()) && (!sender.hasPermission(cmd.perm())) && ((!cmd.admin()) || (!hasAdminPerms(sender)))) {
      throw new IllegalArgumentException("You don't have permission to use this command");
    }

    if (args.length < cmd.min()) {
      throw new IllegalArgumentException("You need at least " + cmd.min() + " arguments");
    }

    if (args.length > cmd.max()) {
      throw new IllegalArgumentException("You need less than " + cmd.max() + " arguments");
    }

    if ((cmd.exact() != -1) && (args.length != cmd.exact())) {
      throw new IllegalArgumentException("You need exactly " + cmd.exact() + " arguments");
    }
    boolean isPlayer = sender instanceof Player;
    boolean isOp = ((isPlayer) && (sender.isOp())) || (sender == null) || ((sender instanceof ConsoleCommandSender));

    if ((cmd.op()) && (!isOp)) {
      throw new IllegalArgumentException("You need to be op to use this command");
    }
    if ((cmd.admin()) && (!isOp) && (isPlayer) && (!hasAdminPerms(sender))) {
      throw new IllegalArgumentException("You need to be an Admin to use this command");
    }
    Class[] types = mwrapper.method.getParameterTypes();

    if ((types[0] == Player.class) && (!isPlayer)) {
      throw new IllegalArgumentException(ONLY_INGAME);
    }
    int strIndex = 0;
    int objIndex = 1;

    Arguments newArgs = new Arguments();
    Object[] objs = new Object[paramLength];

    newArgs.args = objs;
    objs[0] = verifySender(sender, types[0]);
    AtomicInteger numUsedStrings = new AtomicInteger(0);
    for (int i = 1; i < types.length; i++) {
      Class clazz = types[i];
      try {
        if (CommandSender.class == clazz) {
          objs[objIndex] = sender;
        } else if (String.class == clazz) {
          objs[objIndex] = Arrays.copyOfRange(args, strIndex, args.length);
        } else if (Object.class == clazz) {
          objs[objIndex] = args;
        } else {
          objs[objIndex] = verifyArg(sender, clazz, command, args, strIndex, numUsedStrings);
          if (objs[objIndex] == null) {
            throw new IllegalArgumentException("Argument " + args[strIndex] + " can not be null");
          }

        }

        if (numUsedStrings.get() > 0)
          strIndex += numUsedStrings.get();
      } catch (ArrayIndexOutOfBoundsException e) {
        throw new IllegalArgumentException("You didnt supply enough arguments for this method");
      }
      objIndex++;
      if (i == this.commandIndex) {
        if (hasCmd)
          strIndex++;
        if (hasSubCmd) {
          strIndex++;
        }
      }
    }

    if (cmd.alphanum().length > 0) {
      for (int index : cmd.alphanum()) {
        if (index >= args.length)
          throw new IllegalArgumentException("String Index out of range. ");
        if (!args[index].matches("[a-zA-Z0-9_]*")) {
          throw new IllegalArgumentException("argument '" + args[index] + "' can only be alphanumeric with underscores");
        }
      }
    }
    return newArgs;
  }

  protected Object verifySender(CommandSender sender, Class<?> clazz) {
    if (!clazz.isAssignableFrom(sender.getClass()))
      throw new IllegalArgumentException("sender must be a " + clazz.getSimpleName());
    return sender;
  }

  protected Object verifyArg(CommandSender sender, Class<?> clazz, Command command, String[] args, int curIndex, AtomicInteger numUsedStrings)
  {
    numUsedStrings.set(0);
    if (Command.class == clazz) {
      return command;
    }
    String string = args[curIndex];
    if (string == null)
      throw new ArrayIndexOutOfBoundsException();
    numUsedStrings.set(1);
    if (Player.class == clazz)
      return verifyPlayer(string);
    if (OfflinePlayer.class == clazz)
      return verifyOfflinePlayer(string);
    if (String.class == clazz)
      return string;
    if (Location.class == clazz)
      return verifyLocation(string);
    if ((Integer.class == clazz) || (Integer.TYPE == clazz))
      return verifyInteger(string);
    if ((Boolean.class == clazz) || (Boolean.TYPE == clazz))
      return Boolean.valueOf(Boolean.parseBoolean(string));
    if (Object.class == clazz)
      return string;
    if ((Float.class == clazz) || (Float.TYPE == clazz))
      return verifyFloat(string);
    if ((Double.class == clazz) || (Double.TYPE == clazz))
      return verifyDouble(string);
    if (GameMode.class == clazz)
      return verifyGameMode(string);
    if (ItemStack.class == clazz) {
      return verifyItemStack(args, curIndex, numUsedStrings);
    }
    return null;
  }

  private ItemStack verifyItemStack(String[] args, int curIndex, AtomicInteger numUsedStrings) {
    ItemStack is = InventoryUtil.getItemStack(args[curIndex]);
    if (is == null)
      throw new IllegalArgumentException("Error parsing item " + args[curIndex]);
    numUsedStrings.set(1);
    return is;
  }

  private Location verifyLocation(String string) {
    String[] split = string.split(",");
    String w = split[0];
    float x = Float.valueOf(split[1]).floatValue();
    float y = Float.valueOf(split[2]).floatValue();
    float z = Float.valueOf(split[3]).floatValue();
    float yaw = 0.0F; float pitch = 0.0F;
    if (split.length > 4) yaw = Float.valueOf(split[4]).floatValue();
    if (split.length > 5) pitch = Float.valueOf(split[5]).floatValue();
    World world = null;
    if (w != null)
      world = Bukkit.getWorld(w);
    if (world == null)
      throw new IllegalArgumentException("Error parsing location, World '" + string + "' does not exist");
    return new Location(world, x, y, z, yaw, pitch);
  }

  private GameMode verifyGameMode(String string) {
    GameMode gm = null;
    try { gm = GameMode.valueOf(string); } catch (Exception e) {
    }if (gm == null) {
      try {
        gm = GameMode.getByValue(Integer.valueOf(string).intValue()); } catch (Exception e) {
      }
      if (gm == null) {
        throw new IllegalArgumentException("&cGamemode " + string + " not found");
      }
    }
    return gm;
  }

  private OfflinePlayer verifyOfflinePlayer(String name) throws IllegalArgumentException {
    OfflinePlayer p = findOfflinePlayer(name);
    if (p == null)
      throw new IllegalArgumentException("Player " + name + " can not be found");
    return p;
  }

  private Player verifyPlayer(String name) throws IllegalArgumentException
  {
    Player p = findPlayer(name);
    if ((p == null) || (!p.isOnline()))
      throw new IllegalArgumentException(name + " is not online ");
    return p;
  }

  private Integer verifyInteger(Object object) throws IllegalArgumentException
  {
    try {
      return Integer.valueOf(Integer.parseInt(object.toString())); } catch (NumberFormatException e) {
    }
    throw new IllegalArgumentException(ChatColor.RED + (String)object + " is not a valid integer.");
  }

  private Float verifyFloat(Object object) throws IllegalArgumentException
  {
    try {
      return Float.valueOf(Float.parseFloat(object.toString())); } catch (NumberFormatException e) {
    }
    throw new IllegalArgumentException(ChatColor.RED + (String)object + " is not a valid float.");
  }

  private Double verifyDouble(Object object) throws IllegalArgumentException
  {
    try {
      return Double.valueOf(Double.parseDouble(object.toString())); } catch (NumberFormatException e) {
    }
    throw new IllegalArgumentException(ChatColor.RED + (String)object + " is not a valid double.");
  }

  protected boolean hasAdminPerms(CommandSender sender)
  {
    return sender.isOp();
  }

  public void help(CommandSender sender, Command command, String[] args)
  {
    Integer page = Integer.valueOf(1);

    if ((args != null) && (args.length > 1)) {
      try {
        page = Integer.valueOf(args[1]);
      } catch (Exception e) {
        sendMessage(sender, ChatColor.RED + " " + args[1] + " is not a number, showing help for page 1.");
      }
    }

    List<String> available = new ArrayList();
    List<String> unavailable = new ArrayList();
    List<String> onlyop = new ArrayList();
    Set dups = new HashSet();
    String theCommand = command.getName();
    if ((this.useAlias != -1) && (this.useAlias < command.getAliases().size())) {
      theCommand = (String)command.getAliases().get(this.useAlias);
    }
    for (MethodWrapper mw : this.usage)
      if (dups.add(mw.method))
      {
        MCCommand cmd = mw.getCommand();
        String use = "&6/" + theCommand + " " + mw.usage;
        if ((cmd.op()) && (!sender.isOp()))
          onlyop.add(use);
        else if ((!cmd.admin()) || (hasAdminPerms(sender)))
        {
          if ((!cmd.perm().isEmpty()) && (!sender.hasPermission(cmd.perm())))
            unavailable.add(use);
          else
            available.add(use); 
        }
      }
    int npages = available.size() + unavailable.size();
    if (sender.isOp())
      npages += onlyop.size();
    npages = (int)Math.ceil(npages / 8.0F);
    if ((page.intValue() > npages) || (page.intValue() <= 0)) {
      if (npages <= 0)
        sendMessage(sender, "&4There are no methods for this command");
      else {
        sendMessage(sender, "&4That page doesnt exist, try 1-" + npages);
      }
      return;
    }
    if ((command != null) && (command.getAliases() != null) && (!command.getAliases().isEmpty())) {
      String aliases = StringUtils.join(command.getAliases(), ", ");
      sendMessage(sender, "&eShowing page &6" + page + "/" + npages + "&6 : /" + command.getName() + " help <page number>");
      sendMessage(sender, "&e    command &6" + command.getName() + "&e has aliases: &6" + aliases);
    } else {
      sendMessage(sender, "&eShowing page &6" + page + "/" + npages + "&6 : /cmd help <page number>");
    }
    int i = 0;
    for (String use : available) {
      i++;
      if ((i >= (page.intValue() - 1) * 8) && (i < page.intValue() * 8))
      {
        sendMessage(sender, use);
      }
    }
    for (String use : unavailable) {
      i++;
      if ((i >= (page.intValue() - 1) * 8) && (i < page.intValue() * 8))
      {
        sendMessage(sender, ChatColor.RED + "[Insufficient Perms] " + use);
      }
    }
    if (sender.isOp())
      for (String use : onlyop) {
        i++;
        if ((i >= (page.intValue() - 1) * 8) && (i < page.intValue() * 8))
        {
          sendMessage(sender, ChatColor.AQUA + "[OP only] &6" + use);
        }
      }
  }

  public boolean sendMessage(CommandSender p, String message) {
    if ((message == null) || (message.isEmpty())) return true;
    if (message.contains("\n"))
      return sendMultilineMessage(p, message);
    if ((p instanceof Player)) {
      if (((Player)p).isOnline())
        p.sendMessage(colorChat(message));
    }
    else p.sendMessage(colorChat(message));

    return true;
  }

  public boolean sendMultilineMessage(CommandSender p, String message) {
    if ((message == null) || (message.isEmpty())) return true;
    String[] msgs = message.split("\n");
    for (String msg : msgs) {
      if ((p instanceof Player)) {
        if (((Player)p).isOnline())
          p.sendMessage(colorChat(msg));
      }
      else p.sendMessage(colorChat(msg));
    }

    return true;
  }
  public static String colorChat(String msg) {
    return msg.replace('&', '§');
  }
  public static Player findPlayer(String name) {
//    if (name == null)
//      return null;
//    Player foundPlayer = Bukkit.getPlayer(name);
//    if (foundPlayer != null)
//      return foundPlayer;
//    foundPlayer = VirtualPlayers.getPlayer(name);
//    if (foundPlayer != null)
//      return foundPlayer;
//    Player[] online = getOnlinePlayers();
//
//    for (Player player : online) {
//      String playerName = player.getName();
//
//      if (playerName.equalsIgnoreCase(name)) {
//        foundPlayer = player;
//        break;
//      }
//      if (playerName.toLowerCase().indexOf(name.toLowerCase(), 0) != -1) {
//        if (foundPlayer != null) {
//          return null;
//        }
//        foundPlayer = player;
//      }
//    }

    return null;
  }


  public static OfflinePlayer findOfflinePlayer(String name) {
    OfflinePlayer p = findPlayer(name);
    if (p != null) {
      return p;
    }

    for (World w : Bukkit.getWorlds()) {
      File f = new File(w.getName() + "/players/" + name + ".dat");
      if (f.exists()) {
        return Bukkit.getOfflinePlayer(name);
      }
    }
    return null;
  }

  protected void useAliasIndex(int index) {
    this.useAlias = index;
  }

  public class CommandException
  {
    final IllegalArgumentException err;
    final CustomCommandExecutor.MethodWrapper mw;

    public CommandException(IllegalArgumentException err, CustomCommandExecutor.MethodWrapper mw)
    {
      this.err = err; this.mw = mw;
    }
  }

  class MethodWrapper
  {
    public Object obj;
    public Method method;
    public String usage;
    Float helpOrder = null;

    public MethodWrapper(Object obj, Method method)
    {
      this.obj = obj; this.method = method;
    }

    public CustomCommandExecutor.MCCommand getCommand()
    {
      return (CustomCommandExecutor.MCCommand)this.method.getAnnotation(CustomCommandExecutor.MCCommand.class);
    }
    public float getHelpOrder() {
      return this.helpOrder != null ? this.helpOrder.floatValue() : ((CustomCommandExecutor.MCCommand)this.method.getAnnotation(CustomCommandExecutor.MCCommand.class)).helpOrder();
    }
  }

  protected class Arguments
  {
    public Object[] args;

    protected Arguments()
    {
    }
  }

  @Retention(RetentionPolicy.RUNTIME)
  public static @interface MCCommand
  {
    public abstract String[] cmds();

    public abstract String[] subCmds();

    public abstract int min();

    public abstract int max();

    public abstract int exact();

    public abstract int order();

    public abstract float helpOrder();

    public abstract boolean admin();

    public abstract boolean op();

    public abstract String usage();

    public abstract String usageNode();

    public abstract String perm();

    public abstract int[] alphanum();
  }
}