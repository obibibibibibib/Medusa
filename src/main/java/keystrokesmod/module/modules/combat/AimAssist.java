//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.8.9"!

package keystrokesmod.module.modules.combat;

import java.util.ArrayList;
import java.util.Iterator;

import keystrokesmod.*;
import keystrokesmod.main.Ravenb3;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleSettingTick;
import keystrokesmod.module.ModuleSettingSlider;
import keystrokesmod.module.modules.world.AntiBot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import javax.security.auth.login.CredentialException;
import javax.xml.stream.events.EndElement;

public class AimAssist extends Module {
   public static ModuleSettingSlider speed;
   public static ModuleSettingSlider fov;
   public static ModuleSettingSlider distance;
   public static ModuleSettingTick clickAim;
   public static ModuleSettingTick weaponOnly;
   public static ModuleSettingTick aimInvis;
   public static ModuleSettingTick breakBlocks;
   public static ModuleSettingTick blatantMode;
   public static ModuleSettingTick ignoreFriends;
   public static ArrayList<Entity> friends = new ArrayList<>();

   public AimAssist() {
      super("AimAssist", Module.category.combat, 0);
      this.registerSetting(speed = new ModuleSettingSlider("Speed", 45.0D, 1.0D, 100.0D, 1.0D));
      this.registerSetting(fov = new ModuleSettingSlider("FOV", 90.0D, 15.0D, 180.0D, 1.0D));
      this.registerSetting(distance = new ModuleSettingSlider("Distance", 4.5D, 1.0D, 10.0D, 0.5D));
      this.registerSetting(clickAim = new ModuleSettingTick("Click aim", true));
      this.registerSetting(breakBlocks = new ModuleSettingTick("Break blocks", true));
      this.registerSetting(ignoreFriends = new ModuleSettingTick("Ignore Friends", true));
      this.registerSetting(weaponOnly = new ModuleSettingTick("Weapon only", false));
      this.registerSetting(aimInvis = new ModuleSettingTick("Aim invis", false));
      this.registerSetting(blatantMode = new ModuleSettingTick("Blatant mode", false));
   }

   public void update() {
         if (breakBlocks.isToggled() && mc.objectMouseOver != null) {
            BlockPos p = mc.objectMouseOver.getBlockPos();
            if (p != null) {
               Block bl = mc.theWorld.getBlockState(p).getBlock();
               if (bl != Blocks.air && !(bl instanceof BlockLiquid) && bl instanceof  Block) {
                  return;
               }
            }
         }


         if (!weaponOnly.isToggled() || ay.wpn()) {
            if (!clickAim.isToggled() || ay.autoClickerClicking()) {
               Entity en = this.getEnemy();
               if (en != null) {
                  if (Ravenb3.debugger) {
                     ay.sendMessageToSelf(this.getName() + " &e" + en.getName());
                  }
                  if (ignoreFriends.isToggled() && isAFriend(en)){
                     return;
                  }

                  if (blatantMode.isToggled()) {
                     ay.aim(en, 0.0F, false);
                  } else {
                     double n = ay.n(en);
                     if (n > 1.0D || n < -1.0D) {
                        float val = (float)(-(n / (101.0D - speed.getInput())));
                        mc.thePlayer.rotationYaw += val;
                     }
                  }
               }

            }
         }
      }


   public static boolean isAFriend(Entity entity) {
      for (Entity wut : friends){
         if (wut.equals(entity))
            return true;
      }
      return false;
   }

   public Entity getEnemy() {
      int fov = (int) AimAssist.fov.getInput();
      Iterator var2 = mc.theWorld.playerEntities.iterator();

      EntityPlayer en;
      do {
         do {
            do {
               do {
                  do {
                     do {
                        if (!var2.hasNext()) {
                           return null;
                        }

                        en = (EntityPlayer)var2.next();
                     } while(en == mc.thePlayer);
                  } while(en.deathTime != 0);
               } while(!aimInvis.isToggled() && en.isInvisible());
            } while((double)mc.thePlayer.getDistanceToEntity(en) > distance.getInput());
         } while(AntiBot.bot(en));
      } while(!blatantMode.isToggled() && !ay.fov(en, (float)fov));

      return en;
   }

   public static void addFriend(Entity entityPlayer) {
      friends.add(entityPlayer);
   }

   public static boolean addFriend(String name) {
      boolean found = false;
      for (Entity entity:mc.theWorld.getLoadedEntityList()) {
         if (entity.getName().equalsIgnoreCase(name) || entity.getCustomNameTag().equalsIgnoreCase(name)) {
            if(!isAFriend(entity)) {
               addFriend(entity);
               found = true;
            }
         }
      }

      return found;
   }

   public static boolean removeFriend(String name) {
      boolean removed = false;
      boolean found = false;
      for (NetworkPlayerInfo networkPlayerInfo : new ArrayList<NetworkPlayerInfo>(mc.getNetHandler().getPlayerInfoMap())) {
         Entity entity = (Entity) mc.theWorld.getPlayerEntityByName(networkPlayerInfo.getDisplayName().getUnformattedText());
         if (entity.getName().equalsIgnoreCase(name) || entity.getCustomNameTag().equalsIgnoreCase(name)) {
            removed = removeFriend(entity);
            found = true;
         }
      }

      return found && removed;
   }

   public static boolean removeFriend(Entity entityPlayer) {
      try{
         friends.remove(entityPlayer);
      } catch (Exception eeeeee){
         eeeeee.printStackTrace();
         return false;
      }
      return true;
   }

   public static ArrayList<Entity> getFriends() {
      return friends;
   }
}
