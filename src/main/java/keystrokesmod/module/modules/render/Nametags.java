//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.8.9"!

package keystrokesmod.module.modules.render;

import keystrokesmod.module.Module;
import keystrokesmod.module.ModuleSettingSlider;
import keystrokesmod.ay;
import keystrokesmod.module.ModuleSettingTick;
import keystrokesmod.module.modules.world.AntiBot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent.Specials.Pre;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Nametags extends Module {
   public static ModuleSettingSlider a;
   public static ModuleSettingTick b;
   public static ModuleSettingTick c;
   public static ModuleSettingTick d;
   public static ModuleSettingTick rm;
   public static ModuleSettingTick e;

   public Nametags() {
      super("Nametags", Module.category.render, 0);
      this.registerSetting(a = new ModuleSettingSlider("Offset", 0.0D, -40.0D, 40.0D, 1.0D));
      this.registerSetting(b = new ModuleSettingTick("Rect", true));
      this.registerSetting(c = new ModuleSettingTick("Show health", true));
      this.registerSetting(d = new ModuleSettingTick("Show invis", true));
      this.registerSetting(rm = new ModuleSettingTick("Remove tags", false));
   }

   @SubscribeEvent
   public void r(Pre e) {
      if (rm.isToggled()) {
         e.setCanceled(true);
      } else {
         if (e.entity instanceof EntityPlayer && e.entity != mc.thePlayer && e.entity.deathTime == 0) {
            EntityPlayer en = (EntityPlayer)e.entity;
            if (!d.isToggled() && en.isInvisible()) {
               return;
            }

            if (AntiBot.bot(en) || en.getDisplayNameString().isEmpty()) {
               return;
            }

            e.setCanceled(true);
            String str = en.getDisplayName().getFormattedText();
            if (c.isToggled()) {
               double r = (double)(en.getHealth() / en.getMaxHealth());
               String h = (r < 0.3D ? "§c" : (r < 0.5D ? "§6" : (r < 0.7D ? "§e" : "§a"))) + ay.round((double)en.getHealth(), 1);
               str = str + " " + h;
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate((float)e.x + 0.0F, (float)e.y + en.height + 0.5F, (float)e.z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
            float f1 = 0.02666667F;
            GlStateManager.scale(-f1, -f1, f1);
            if (en.isSneaking()) {
               GlStateManager.translate(0.0F, 9.374999F, 0.0F);
            }

            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = (int)(-a.getInput());
            int j = mc.fontRendererObj.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            if (b.isToggled()) {
               worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
               worldrenderer.pos((double)(-j - 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
               worldrenderer.pos((double)(-j - 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
               worldrenderer.pos((double)(j + 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
               worldrenderer.pos((double)(j + 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
               tessellator.draw();
            }

            GlStateManager.enableTexture2D();
            mc.fontRendererObj.drawString(str, -mc.fontRendererObj.getStringWidth(str) / 2, i, -1);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
         }

      }
   }
}
