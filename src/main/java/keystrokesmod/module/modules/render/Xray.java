//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.8.9"!

package keystrokesmod.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import keystrokesmod.*;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleSettingTick;
import keystrokesmod.module.ModuleSettingSlider;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Xray extends Module {
   public static ModuleSettingSlider r;
   public static ModuleSettingTick a;
   public static ModuleSettingTick b;
   public static ModuleSettingTick c;
   public static ModuleSettingTick d;
   public static ModuleSettingTick e;
   public static ModuleSettingTick f;
   public static ModuleSettingTick g;
   public static ModuleSettingTick h;
   private java.util.Timer t;
   private List<BlockPos> ren;
   private final long per = 200L;

   public Xray() {
      super("Xray", Module.category.render, 0);
      this.registerSetting(r = new ModuleSettingSlider("Range", 20.0D, 5.0D, 50.0D, 1.0D));
      this.registerSetting(a = new ModuleSettingTick("Iron", true));
      this.registerSetting(b = new ModuleSettingTick("Gold", true));
      this.registerSetting(c = new ModuleSettingTick("Diamond", true));
      this.registerSetting(d = new ModuleSettingTick("Emerald", true));
      this.registerSetting(e = new ModuleSettingTick("Lapis", true));
      this.registerSetting(f = new ModuleSettingTick("Redstone", true));
      this.registerSetting(g = new ModuleSettingTick("Coal", true));
      this.registerSetting(h = new ModuleSettingTick("Spawner", true));
   }

   public void onEnable() {
      this.ren = new ArrayList();
      (this.t = new java.util.Timer()).scheduleAtFixedRate(this.t(), 0L, 200L);
   }

   public void onDisable() {
      if (this.t != null) {
         this.t.cancel();
         this.t.purge();
         this.t = null;
      }

   }

   private TimerTask t() {
      TimerTask t = new TimerTask() {
         public void run() {
            Xray.this.ren.clear();
            int ra = (int)Xray.r.getInput();

            for(int y = ra; y >= -ra; --y) {
               for(int x = -ra; x <= ra; ++x) {
                  for(int z = -ra; z <= ra; ++z) {
                     BlockPos p = new BlockPos(Module.mc.thePlayer.posX + (double)x, Module.mc.thePlayer.posY + (double)y, Module.mc.thePlayer.posZ + (double)z);
                     Block bl = Module.mc.theWorld.getBlockState(p).getBlock();
                     if (Xray.a.isToggled() && bl.equals(Blocks.iron_ore) || Xray.b.isToggled() && bl.equals(Blocks.gold_ore) || Xray.c.isToggled() && bl.equals(Blocks.diamond_ore) || Xray.d.isToggled() && bl.equals(Blocks.emerald_ore) || Xray.e.isToggled() && bl.equals(Blocks.lapis_ore) || Xray.f.isToggled() && bl.equals(Blocks.redstone_ore) || Xray.g.isToggled() && bl.equals(Blocks.coal_ore) || Xray.h.isToggled() && bl.equals(Blocks.mob_spawner)) {
                        Xray.this.ren.add(p);
                     }
                  }
               }
            }

         }
      };
      return t;
   }

   @SubscribeEvent
   public void orl(RenderWorldLastEvent ev) {
      if (ay.isPlayerInGame() && !this.ren.isEmpty()) {
         List<BlockPos> tRen = new ArrayList(this.ren);
         Iterator var3 = tRen.iterator();

         while(var3.hasNext()) {
            BlockPos p = (BlockPos)var3.next();
            this.dr(p);
         }
      }

   }

   private void dr(BlockPos p) {
      int[] rgb = this.c(mc.theWorld.getBlockState(p).getBlock());
      if (rgb[0] + rgb[1] + rgb[2] != 0) {
         ru.re(p, (new Color(rgb[0], rgb[1], rgb[2])).getRGB(), true);
      }

   }

   private int[] c(Block b) {
      int red = 0;
      int green = 0;
      int blue = 0;
      if (b.equals(Blocks.iron_ore)) {
         red = 255;
         green = 255;
         blue = 255;
      } else if (b.equals(Blocks.gold_ore)) {
         red = 255;
         green = 255;
      } else if (b.equals(Blocks.diamond_ore)) {
         green = 220;
         blue = 255;
      } else if (b.equals(Blocks.emerald_ore)) {
         red = 35;
         green = 255;
      } else if (b.equals(Blocks.lapis_ore)) {
         green = 50;
         blue = 255;
      } else if (b.equals(Blocks.redstone_ore)) {
         red = 255;
      } else if (b.equals(Blocks.mob_spawner)) {
         red = 30;
         blue = 135;
      }

      return new int[]{red, green, blue};
   }
}
