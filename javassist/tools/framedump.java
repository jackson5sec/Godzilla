/*    */ package javassist.tools;
/*    */ 
/*    */ import javassist.ClassPool;
/*    */ import javassist.CtClass;
/*    */ import javassist.bytecode.analysis.FramePrinter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class framedump
/*    */ {
/*    */   public static void main(String[] args) throws Exception {
/* 38 */     if (args.length != 1) {
/* 39 */       System.err.println("Usage: java javassist.tools.framedump <fully-qualified class name>");
/*    */       
/*    */       return;
/*    */     } 
/* 43 */     ClassPool pool = ClassPool.getDefault();
/* 44 */     CtClass clazz = pool.get(args[0]);
/* 45 */     System.out.println("Frame Dump of " + clazz.getName() + ":");
/* 46 */     FramePrinter.print(clazz, System.out);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\framedump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */