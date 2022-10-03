/*    */ package javassist.expr;
/*    */ 
/*    */ import javassist.CtClass;
/*    */ import javassist.CtConstructor;
/*    */ import javassist.CtMethod;
/*    */ import javassist.NotFoundException;
/*    */ import javassist.bytecode.CodeIterator;
/*    */ import javassist.bytecode.MethodInfo;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConstructorCall
/*    */   extends MethodCall
/*    */ {
/*    */   protected ConstructorCall(int pos, CodeIterator i, CtClass decl, MethodInfo m) {
/* 37 */     super(pos, i, decl, m);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMethodName() {
/* 45 */     return isSuper() ? "super" : "this";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CtMethod getMethod() throws NotFoundException {
/* 55 */     throw new NotFoundException("this is a constructor call.  Call getConstructor().");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CtConstructor getConstructor() throws NotFoundException {
/* 62 */     return getCtClass().getConstructor(getSignature());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSuper() {
/* 71 */     return super.isSuper();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\expr\ConstructorCall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */