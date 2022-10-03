/*    */ package javassist.convert;
/*    */ 
/*    */ import javassist.CannotCompileException;
/*    */ import javassist.CtClass;
/*    */ import javassist.bytecode.BadBytecode;
/*    */ import javassist.bytecode.CodeAttribute;
/*    */ import javassist.bytecode.CodeIterator;
/*    */ import javassist.bytecode.ConstPool;
/*    */ import javassist.bytecode.MethodInfo;
/*    */ import javassist.bytecode.Opcode;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Transformer
/*    */   implements Opcode
/*    */ {
/*    */   private Transformer next;
/*    */   
/*    */   public Transformer(Transformer t) {
/* 38 */     this.next = t;
/*    */   }
/*    */   public Transformer getNext() {
/* 41 */     return this.next;
/*    */   }
/*    */   public void initialize(ConstPool cp, CodeAttribute attr) {}
/*    */   
/*    */   public void initialize(ConstPool cp, CtClass clazz, MethodInfo minfo) throws CannotCompileException {
/* 46 */     initialize(cp, minfo.getCodeAttribute());
/*    */   }
/*    */   
/*    */   public void clean() {}
/*    */   
/*    */   public abstract int transform(CtClass paramCtClass, int paramInt, CodeIterator paramCodeIterator, ConstPool paramConstPool) throws CannotCompileException, BadBytecode;
/*    */   
/*    */   public int extraLocals() {
/* 54 */     return 0;
/*    */   } public int extraStack() {
/* 56 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\convert\Transformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */