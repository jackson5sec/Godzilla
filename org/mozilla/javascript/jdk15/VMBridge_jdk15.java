/*    */ package org.mozilla.javascript.jdk15;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Member;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Iterator;
/*    */ import org.mozilla.javascript.Context;
/*    */ import org.mozilla.javascript.Scriptable;
/*    */ import org.mozilla.javascript.Wrapper;
/*    */ import org.mozilla.javascript.jdk13.VMBridge_jdk13;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VMBridge_jdk15
/*    */   extends VMBridge_jdk13
/*    */ {
/*    */   public VMBridge_jdk15() throws SecurityException, InstantiationException {
/*    */     try {
/* 22 */       Method.class.getMethod("isVarArgs", (Class[])null);
/* 23 */     } catch (NoSuchMethodException e) {
/*    */ 
/*    */       
/* 26 */       throw new InstantiationException(e.getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isVarArgs(Member member) {
/* 32 */     if (member instanceof Method)
/* 33 */       return ((Method)member).isVarArgs(); 
/* 34 */     if (member instanceof Constructor) {
/* 35 */       return ((Constructor)member).isVarArgs();
/*    */     }
/* 37 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Iterator<?> getJavaIterator(Context cx, Scriptable scope, Object obj) {
/* 47 */     if (obj instanceof Wrapper) {
/* 48 */       Object unwrapped = ((Wrapper)obj).unwrap();
/* 49 */       Iterator<?> iterator = null;
/* 50 */       if (unwrapped instanceof Iterator)
/* 51 */         iterator = (Iterator)unwrapped; 
/* 52 */       if (unwrapped instanceof Iterable)
/* 53 */         iterator = ((Iterable)unwrapped).iterator(); 
/* 54 */       return iterator;
/*    */     } 
/* 56 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\jdk15\VMBridge_jdk15.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */