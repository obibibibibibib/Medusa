//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.8.9"!

package keystrokesmod.module.modules.player;

import keystrokesmod.module.Module;
import keystrokesmod.ay;
import keystrokesmod.module.ModuleSettingTick;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AutoJump extends Module {
   public static ModuleSettingTick b;
   private boolean c = false;

   public AutoJump() {
      super("AutoJump", Module.category.player, 0);
      this.registerSetting(b = new ModuleSettingTick("Cancel when shifting", true));
   }

   public void onDisable() {
      this.ju(this.c = false);
   }

   @SubscribeEvent
   public void p(PlayerTickEvent e) {
      if (ay.isPlayerInGame()) {
         if (mc.thePlayer.onGround && (!b.isToggled() || !mc.thePlayer.isSneaking())) {
            if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(mc.thePlayer.motionX / 3.0D, -1.0D, mc.thePlayer.motionZ / 3.0D)).isEmpty()) {
               this.ju(this.c = true);
            } else if (this.c) {
               this.ju(this.c = false);
            }
         } else if (this.c) {
            this.ju(this.c = false);
         }

      }
   }

   private void ju(boolean ju) {
      KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), ju);
   }
}
