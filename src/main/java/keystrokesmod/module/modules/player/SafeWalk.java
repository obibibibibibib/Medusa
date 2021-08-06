//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.8.9"!

package keystrokesmod.module.modules.player;

import keystrokesmod.module.Module;
import keystrokesmod.ay;
import keystrokesmod.module.ModuleSettingSlider;
import keystrokesmod.module.ModuleSettingTick;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.input.Keyboard;

public class SafeWalk extends Module {
   public static ModuleSettingTick doShift;
   public static ModuleSettingTick blocksOnly;
   public static ModuleSettingTick shiftOnJump;
   public static ModuleSettingTick onHold;
   public static ModuleSettingTick lookDown;
   public static ModuleSettingSlider pitchRange;
   public static ModuleSettingSlider pitchIgnorePoint;
   private static boolean shouldBridge = false;
   private static boolean isShifting = false;

   public SafeWalk() {
      super("SafeWalk", Module.category.player, 0);
      this.registerSetting(doShift = new ModuleSettingTick("Shift", false));
      this.registerSetting(shiftOnJump = new ModuleSettingTick("Shift during jumps", false));
      this.registerSetting(onHold = new ModuleSettingTick("On shift hold", false));
      this.registerSetting(blocksOnly = new ModuleSettingTick("Blocks only", true));
      this.registerSetting(lookDown = new ModuleSettingTick("Only when looking down", true));
      this.registerSetting(pitchRange = new ModuleSettingSlider("Pitch min range:", 70D, 0D, 90D, 1D));
      this.registerSetting(pitchIgnorePoint = new ModuleSettingSlider("Pitch Max range:", 85D, 0D, 90D, 1D));
   }

   public void onDisable() {
      if (doShift.isToggled() && ay.playerOverAir()) {
         this.setShift(false);
      }

      shouldBridge = false;
      isShifting = false;
   }

   public void guiUpdate() {
      ay.correctSliders(pitchRange, pitchIgnorePoint);
   }

   @SubscribeEvent
   public void p(PlayerTickEvent e) {
      if (!ay.isPlayerInGame()) {
         return;
      }
      if(doShift.isToggled()) {
         if(lookDown.isToggled()) {
            if(mc.thePlayer.rotationPitch < pitchRange.getInput() || mc.thePlayer.rotationPitch > pitchIgnorePoint.getInput())
               return;
         }
         if (onHold.isToggled()) {
            if  (!Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()))
               return;
         }
         if (mc.thePlayer.onGround) {
            if (ay.playerOverAir()) {
               if (blocksOnly.isToggled()) {
                  ItemStack i = mc.thePlayer.getHeldItem();
                  if (i == null || !(i.getItem() instanceof ItemBlock)) {
                     if (isShifting) {
                        isShifting = false;
                        this.setShift(false);
                     }

                     return;
                  }
               }

               isShifting = true;
               this.setShift(true);
               shouldBridge = true;
            }
            else if(mc.thePlayer.isSneaking()) {
               isShifting = false;
               this.setShift(false);
               shouldBridge = true;
            }
            else if (mc.thePlayer.isSneaking() && !Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
               isShifting = false;
               shouldBridge = false;
               this.setShift(false);
            }
         }

         else if (shouldBridge && mc.thePlayer.capabilities.isFlying) {
            this.setShift(false);
            shouldBridge = false;
         }

         else if (shouldBridge && ay.playerOverAir() && shiftOnJump.isToggled()) {
            isShifting = true;
            this.setShift(true);
         } else {
            // rn we are in the air and we are not flying, meaning that we are in a jump. and since shiftOnJump is turned off, we just unshift and uhh... nyoooom
            isShifting = false;
            this.setShift(false);
         }
      }
   }

   private void setShift(boolean sh) {
      KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), sh);
   }
}
