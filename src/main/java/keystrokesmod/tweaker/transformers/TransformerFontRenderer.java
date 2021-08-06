package keystrokesmod.tweaker.transformers;

import java.util.Iterator;
import keystrokesmod.tweaker.ASMTransformerClass;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TransformerFontRenderer implements Transformer {
   public String[] getClassName() {
      return new String[]{"net.minecraft.client.gui.FontRenderer"};
   }

   public void transform(ClassNode classNode, String transformedName) {
      Iterator<MethodNode> var3 = classNode.methods.iterator();

      while(true) {
         MethodNode m;
         String n;
         do {
            if (!var3.hasNext()) {
               return;
            }

            m = (MethodNode)var3.next();
            n = this.mapMethodName(classNode, m);
         } while(!n.equalsIgnoreCase("renderStringAtPos") && !n.equalsIgnoreCase("func_78255_a") && !n.equalsIgnoreCase("getStringWidth") && !n.equalsIgnoreCase("func_78256_a"));

         m.instructions.insertBefore(m.instructions.getFirst(), this.getEventInsn());
      }
   }

   private InsnList getEventInsn() {
      InsnList i = new InsnList();
      i.add(new VarInsnNode(25, 1));
      i.add(new MethodInsnNode(184, ASMTransformerClass.eventHandlerClassName, "getUnformattedTextForChat", "(Ljava/lang/String;)Ljava/lang/String;", false));
      i.add(new VarInsnNode(58, 1));
      return i;
   }
}
