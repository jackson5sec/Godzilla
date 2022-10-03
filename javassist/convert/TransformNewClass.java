/*    */ package javassist.convert;
/*    */ 
/*    */ import javassist.CannotCompileException;
/*    */ import javassist.CtClass;
/*    */ import javassist.bytecode.CodeAttribute;
/*    */ import javassist.bytecode.CodeIterator;
/*    */ import javassist.bytecode.ConstPool;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TransformNewClass
/*    */   extends Transformer
/*    */ {
/*    */   private int nested;
/*    */   private String classname;
/*    */   private String newClassName;
/*    */   private int newClassIndex;
/*    */   private int newMethodNTIndex;
/*    */   private int newMethodIndex;
/*    */   
/*    */   public TransformNewClass(Transformer next, String classname, String newClassName) {
/* 32 */     super(next);
/* 33 */     this.classname = classname;
/* 34 */     this.newClassName = newClassName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void initialize(ConstPool cp, CodeAttribute attr) {
/* 39 */     this.nested = 0;
/* 40 */     this.newClassIndex = this.newMethodNTIndex = this.newMethodIndex = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int transform(CtClass clazz, int pos, CodeIterator iterator, ConstPool cp) throws CannotCompileException {
/* 55 */     int c = iterator.byteAt(pos);
/* 56 */     if (c == 187) {
/* 57 */       int index = iterator.u16bitAt(pos + 1);
/* 58 */       if (cp.getClassInfo(index).equals(this.classname)) {
/* 59 */         if (iterator.byteAt(pos + 3) != 89) {
/* 60 */           throw new CannotCompileException("NEW followed by no DUP was found");
/*    */         }
/*    */         
/* 63 */         if (this.newClassIndex == 0) {
/* 64 */           this.newClassIndex = cp.addClassInfo(this.newClassName);
/*    */         }
/* 66 */         iterator.write16bit(this.newClassIndex, pos + 1);
/* 67 */         this.nested++;
/*    */       }
/*    */     
/* 70 */     } else if (c == 183) {
/* 71 */       int index = iterator.u16bitAt(pos + 1);
/* 72 */       int typedesc = cp.isConstructor(this.classname, index);
/* 73 */       if (typedesc != 0 && this.nested > 0) {
/* 74 */         int nt = cp.getMethodrefNameAndType(index);
/* 75 */         if (this.newMethodNTIndex != nt) {
/* 76 */           this.newMethodNTIndex = nt;
/* 77 */           this.newMethodIndex = cp.addMethodrefInfo(this.newClassIndex, nt);
/*    */         } 
/*    */         
/* 80 */         iterator.write16bit(this.newMethodIndex, pos + 1);
/* 81 */         this.nested--;
/*    */       } 
/*    */     } 
/*    */     
/* 85 */     return pos;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\convert\TransformNewClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */