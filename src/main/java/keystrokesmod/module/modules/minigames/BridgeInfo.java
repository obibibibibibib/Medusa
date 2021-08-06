//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.8.9"!

package keystrokesmod.module.modules.minigames;

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;

import keystrokesmod.*;
import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleDesc;
import keystrokesmod.module.ModuleSettingTick;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class BridgeInfo extends Module {
   public static ModuleDesc a;
   public static ModuleSettingTick ep;
   private static final int rgb = (new Color(0, 200, 200)).getRGB();
   private final String bd = new String("the brid");
   private final String g1t = new String("Defend!");
   private final String g2t = new String("Jump in to score!");
   private final String qt = new String("First player to score 5 goals wins");
   private final String t1 = new String("Enemy: ");
   private final String t2 = new String("Distance to goal: ");
   private final String t3 = new String("Enemy distance to goal: ");
   private final String t4 = new String("Blocks: ");
   private static int hudX = 5;
   private static int hudY = 70;
   private String en = "";
   private BlockPos g1p = null;
   private BlockPos g2p = null;
   private boolean q = false;
   private double d1 = 0.0D;
   private double d2 = 0.0D;
   private int blc = 0;

   public BridgeInfo() {
      super("Bridge Info", Module.category.minigames, 0);
      this.registerSetting(a = new ModuleDesc(new String("Only for solos.")));
      this.registerSetting(ep = new ModuleSettingTick("Edit position", false));
   }

   public void onDisable() {
      this.rv();
   }

   public void guiButtonToggled(ModuleSettingTick b) {
      if (b == ep) {
         ep.disable();
         mc.displayGuiScreen(new BridgeInfo.eh());
      }

   }

   public void update() {
      if (!this.en.isEmpty() && this.ibd()) {
         EntityPlayer enem = null;
         Iterator var2 = mc.theWorld.loadedEntityList.iterator();

         while(var2.hasNext()) {
            Entity e = (Entity)var2.next();
            if (e instanceof EntityPlayer) {
               if (e.getName().equals(this.en)) {
                  enem = (EntityPlayer)e;
               }
            } else if (e instanceof EntityArmorStand) {
               if (e.getName().contains(this.g1t)) {
                  this.g1p = e.getPosition();
               } else if (e.getName().contains(this.g2t)) {
                  this.g2p = e.getPosition();
               }
            }
         }

         if (this.g1p != null && this.g2p != null) {
            this.d1 = ay.round(mc.thePlayer.getDistance((double)this.g2p.getX(), (double)this.g2p.getY(), (double)this.g2p.getZ()) - 1.4D, 1);
            if (this.d1 < 0.0D) {
               this.d1 = 0.0D;
            }

            this.d2 = enem == null ? 0.0D : ay.round(enem.getDistance((double)this.g1p.getX(), (double)this.g1p.getY(), (double)this.g1p.getZ()) - 1.4D, 1);
            if (this.d2 < 0.0D) {
               this.d2 = 0.0D;
            }
         }

         int blc2 = 0;

         for(int i = 0; i < 9; ++i) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBlock && ((ItemBlock)stack.getItem()).block.equals(Blocks.stained_hardened_clay)) {
               blc2 += stack.stackSize;
            }
         }

         this.blc = blc2;
      }
   }

   @SubscribeEvent
   public void a(RenderTickEvent ev) {
      if (ev.phase == Phase.END && ay.isPlayerInGame() && this.ibd()) {
         if (mc.currentScreen != null || mc.gameSettings.showDebugInfo) {
            return;
         }

         mc.fontRendererObj.drawString(this.t1 + this.en, (float)hudX, (float)hudY, rgb, true);
         mc.fontRendererObj.drawString(this.t2 + this.d1, (float)hudX, (float)(hudY + 11), rgb, true);
         mc.fontRendererObj.drawString(this.t3 + this.d2, (float)hudX, (float)(hudY + 22), rgb, true);
         mc.fontRendererObj.drawString(this.t4 + this.blc, (float)hudX, (float)(hudY + 33), rgb, true);
      }

   }

   @SubscribeEvent
   public void o(ClientChatReceivedEvent c) {
      if (ay.isPlayerInGame()) {
         String s = ay.str(c.message.getUnformattedText());
         if (s.startsWith(" ")) {
            if (s.contains(this.qt)) {
               this.q = true;
            } else if (this.q && s.contains("Opponent:")) {
               String n = s.split(":")[1].trim();
               if (n.contains("[")) {
                  n = n.split("] ")[1];
               }

               this.en = n;
               this.q = false;
            }
         }
      }

   }

   @SubscribeEvent
   public void w(EntityJoinWorldEvent j) {
      if (j.entity == mc.thePlayer) {
         this.rv();
      }

   }

   private boolean ibd() {
      if (ay.isHyp()) {
         Iterator var1 = ay.gsl().iterator();

         while(var1.hasNext()) {
            String s = (String)var1.next();
            String s2 = s.toLowerCase();
            if (s2.contains("mode") && s2.contains(this.bd)) {
               return true;
            }
         }
      }

      return false;
   }

   private void rv() {
      this.en = "";
      this.q = false;
      this.g1p = null;
      this.g2p = null;
      this.d1 = 0.0D;
      this.d2 = 0.0D;
      this.blc = 0;
   }

   static class eh extends GuiScreen {
      final String a = new String("Enemy: Player123-Distance to goal: 17.2-Enemy distance to goal: 16.3-Blocks: 98");
      GuiButtonExt rp;
      boolean d = false;
      int miX = 0;
      int miY = 0;
      int maX = 0;
      int maY = 0;
      int aX = 5;
      int aY = 70;
      int laX = 0;
      int laY = 0;
      int lmX = 0;
      int lmY = 0;

      public void initGui() {
         super.initGui();
         this.buttonList.add(this.rp = new GuiButtonExt(1, this.width - 90, 5, 85, 20, new String("Reset position")));
         this.aX = BridgeInfo.hudX;
         this.aY = BridgeInfo.hudY;
      }

      public void drawScreen(int mX, int mY, float pt) {
         drawRect(0, 0, this.width, this.height, -1308622848);
         int miX = this.aX;
         int miY = this.aY;
         int maX = miX + 140;
         int maY = miY + 41;
         this.d(this.mc.fontRendererObj, this.a);
         this.miX = miX;
         this.miY = miY;
         this.maX = maX;
         this.maY = maY;
         BridgeInfo.hudX = miX;
         BridgeInfo.hudY = miY;
         ScaledResolution res = new ScaledResolution(this.mc);
         int x = res.getScaledWidth() / 2 - 84;
         int y = res.getScaledHeight() / 2 - 20;
         ru.dct("Edit the HUD position by dragging.", '-', x, y, 2L, 0L, true, this.mc.fontRendererObj);

         try {
            this.handleInput();
         } catch (IOException var12) {
         }

         super.drawScreen(mX, mY, pt);
      }

      private void d(FontRenderer fr, String t) {
         int x = this.miX;
         int y = this.miY;
         String[] var5 = t.split("-");
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String s = var5[var7];
            fr.drawString(s, (float)x, (float)y, BridgeInfo.rgb, true);
            y += fr.FONT_HEIGHT + 2;
         }

      }

      protected void mouseClickMove(int mX, int mY, int b, long t) {
         super.mouseClickMove(mX, mY, b, t);
         if (b == 0) {
            if (this.d) {
               this.aX = this.laX + (mX - this.lmX);
               this.aY = this.laY + (mY - this.lmY);
            } else if (mX > this.miX && mX < this.maX && mY > this.miY && mY < this.maY) {
               this.d = true;
               this.lmX = mX;
               this.lmY = mY;
               this.laX = this.aX;
               this.laY = this.aY;
            }

         }
      }

      protected void mouseReleased(int mX, int mY, int s) {
         super.mouseReleased(mX, mY, s);
         if (s == 0) {
            this.d = false;
         }

      }

      public void actionPerformed(GuiButton b) {
         if (b == this.rp) {
            this.aX = BridgeInfo.hudX = 5;
            this.aY = BridgeInfo.hudY = 70;
         }

      }

      public boolean doesGuiPauseGame() {
         return false;
      }
   }
}
