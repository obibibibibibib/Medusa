//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.8.9"!

package keystrokesmod.module.modules.world;

import java.util.HashMap;

import keystrokesmod.*;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleManager;
import keystrokesmod.module.ModuleSettingTick;
import keystrokesmod.module.modules.player.Freecam;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiBot extends Module {
   private static final HashMap<EntityPlayer, Long> newEnt = new HashMap();
   private final long ms = 4000L;
   public static ModuleSettingTick a;

   public AntiBot() {
      super("AntiBot", Module.category.world, 0);
      this.registerSetting(a = new ModuleSettingTick("Wait 80 ticks", false));
   }

   public void onDisable() {
      newEnt.clear();
   }

   @SubscribeEvent
   public void w(EntityJoinWorldEvent j) {
      if (a.isToggled() && j.entity instanceof EntityPlayer && j.entity != mc.thePlayer) {
         newEnt.put((EntityPlayer)j.entity, System.currentTimeMillis());
      }

   }

   public void update() {
      if (a.isToggled() && !newEnt.isEmpty()) {
         long now = System.currentTimeMillis();
         newEnt.values().removeIf((e) -> {
            return e < now - 4000L;
         });
      }

   }

   public static boolean bot(Entity en) {
      if (Freecam.en != null && Freecam.en == en) {
         return true;
      } else if (!ModuleManager.antiBot.isEnabled()) {
         return false;
      } else if (!ay.isHyp()) {
         return false;
      } else if (a.isToggled() && !newEnt.isEmpty() && newEnt.containsKey(en)) {
         return true;
      } else if (en.getName().startsWith("§c")) {
         return true;
      } else {
         String n = en.getDisplayName().getUnformattedText();
         if (n.contains("§")) {
            return n.contains("[NPC] ");
         } else {
            if (n.isEmpty() && en.getName().isEmpty()) {
               return true;
            }

            if (n.length() == 10) {
               int num = 0;
               int let = 0;
               char[] var4 = n.toCharArray();

               for (char c : var4) {
                  if (Character.isLetter(c)) {
                     if (Character.isUpperCase(c)) {
                        return false;
                     }

                     ++let;
                  } else {
                     if (!Character.isDigit(c)) {
                        return false;
                     }

                     ++num;
                  }
               }

               return num >= 2 && let >= 2;
            }
         }

         return false;
      }
   }
}
