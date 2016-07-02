package eu.epicpvp.kcore.Enum;

import org.bukkit.ChatColor;

public enum PlayerState
  {
    INGAME("In", ChatColor.GREEN), 
    SPECTATOR("Out", ChatColor.RED),
    @Deprecated
    BOTH("Both",ChatColor.GRAY);

    private String name;
    private ChatColor color;

    private PlayerState(String name, ChatColor color) {
      this.name = name;
      this.color = color;
    }

    public String GetName()
    {
      return this.name;
    }

    public ChatColor GetColor()
    {
      return this.color;
    }
  }