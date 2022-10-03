/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.asm.ClassReader;
/*    */ import org.springframework.asm.ClassVisitor;
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
/*    */ public class ClassNameReader
/*    */ {
/* 28 */   private static final EarlyExitException EARLY_EXIT = new EarlyExitException();
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getClassName(ClassReader r) {
/* 33 */     return getClassInfo(r)[0];
/*    */   }
/*    */   private static class EarlyExitException extends RuntimeException {
/*    */     private EarlyExitException() {} }
/*    */   public static String[] getClassInfo(ClassReader r) {
/* 38 */     final List array = new ArrayList();
/*    */     try {
/* 40 */       r.accept(new ClassVisitor(Constants.ASM_API, null)
/*    */           {
/*    */ 
/*    */ 
/*    */             
/*    */             public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
/*    */             {
/* 47 */               array.add(name.replace('/', '.'));
/* 48 */               if (superName != null) {
/* 49 */                 array.add(superName.replace('/', '.'));
/*    */               }
/* 51 */               for (int i = 0; i < interfaces.length; i++) {
/* 52 */                 array.add(interfaces[i].replace('/', '.'));
/*    */               }
/*    */               
/* 55 */               throw ClassNameReader.EARLY_EXIT;
/*    */             }
/*    */           }6);
/* 58 */     } catch (EarlyExitException earlyExitException) {}
/*    */     
/* 60 */     return (String[])array.toArray((Object[])new String[0]);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\ClassNameReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */