package me.kingingo.kcore.Util;

import org.bukkit.Note;

public class UtilSound
{
  public static float GetPitch(Note note)
  {
    int o = note.getOctave();
    switch (note.getTone().ordinal()) {
    case 7:
      if (note.isSharped())
        switch (o) { case 0:
          return 0.5F;
        case 1:
          return 1.0F;
        case 2:
          return 2.0F;
        }
      else {
        switch (o) { case 0:
          return 0.943874F;
        case 1:
          return 1.887749F;
        }
      }
      break;
    case 1:
      if (note.isSharped())
        switch (o) { case 0:
          return 0.561231F;
        case 1:
          return 1.122462F;
        }
      else {
        switch (o) { case 0:
          return 0.529732F;
        case 1:
          return 1.059463F;
        }
      }
      break;
    case 2:
      if (note.isSharped())
        switch (o) { case 0:
          return 0.629961F;
        case 1:
          return 1.259921F;
        }
      else {
        switch (o) { case 0:
          return 0.594604F;
        case 1:
          return 1.189207F;
        }
      }
      break;
    case 3:
      switch (o) { case 0:
        return 0.66742F;
      case 1:
        return 1.33484F;
      }
      break;
    case 4:
      if (note.isSharped())
        switch (o) { case 0:
          return 0.749154F;
        case 1:
          return 1.498307F;
        }
      else {
        switch (o) { case 0:
          return 0.707107F;
        case 1:
          return 1.414214F;
        }
      }
      break;
    case 5:
      if (note.isSharped())
        switch (o) { case 0:
          return 0.840896F;
        case 1:
          return 1.681793F;
        }
      else {
        switch (o) { case 0:
          return 0.793701F;
        case 1:
          return 1.587401F;
        }
      }
      break;
    case 6:
      switch (o) { case 0:
        return 0.890899F;
      case 1:
        return 1.781797F; }
      break;
    }
    return -1.0F;
  }
}