/*    */ package javassist.convert;
/*    */ 
/*    */ import javassist.CtMethod;
/*    */ import javassist.bytecode.CodeIterator;
/*    */ import javassist.bytecode.ConstPool;
/*    */ import javassist.bytecode.Descriptor;
/*    */ 
/*    */ public class TransformCallToStatic
/*    */   extends TransformCall
/*    */ {
/*    */   public TransformCallToStatic(Transformer next, CtMethod origMethod, CtMethod substMethod) {
/* 12 */     super(next, origMethod, substMethod);
/* 13 */     this.methodDescriptor = origMethod.getMethodInfo2().getDescriptor();
/*    */   }
/*    */ 
/*    */   
/*    */   protected int match(int c, int pos, CodeIterator iterator, int typedesc, ConstPool cp) {
/* 18 */     if (this.newIndex == 0) {
/* 19 */       String desc = Descriptor.insertParameter(this.classname, this.methodDescriptor);
/* 20 */       int nt = cp.addNameAndTypeInfo(this.newMethodname, desc);
/* 21 */       int ci = cp.addClassInfo(this.newClassname);
/* 22 */       this.newIndex = cp.addMethodrefInfo(ci, nt);
/* 23 */       this.constPool = cp;
/*    */     } 
/* 25 */     iterator.writeByte(184, pos);
/* 26 */     iterator.write16bit(this.newIndex, pos + 1);
/* 27 */     return pos;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\convert\TransformCallToStatic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */