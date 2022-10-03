/*    */ package org.mozilla.javascript.serialize;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectStreamClass;
/*    */ import org.mozilla.javascript.Context;
/*    */ import org.mozilla.javascript.Scriptable;
/*    */ import org.mozilla.javascript.Undefined;
/*    */ import org.mozilla.javascript.UniqueTag;
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
/*    */ public class ScriptableInputStream
/*    */   extends ObjectInputStream
/*    */ {
/*    */   private Scriptable scope;
/*    */   private ClassLoader classLoader;
/*    */   
/*    */   public ScriptableInputStream(InputStream in, Scriptable scope) throws IOException {
/* 35 */     super(in);
/* 36 */     this.scope = scope;
/* 37 */     enableResolveObject(true);
/* 38 */     Context cx = Context.getCurrentContext();
/* 39 */     if (cx != null) {
/* 40 */       this.classLoader = cx.getApplicationClassLoader();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
/* 48 */     String name = desc.getName();
/* 49 */     if (this.classLoader != null) {
/*    */       try {
/* 51 */         return this.classLoader.loadClass(name);
/* 52 */       } catch (ClassNotFoundException ex) {}
/*    */     }
/*    */ 
/*    */     
/* 56 */     return super.resolveClass(desc);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object resolveObject(Object obj) throws IOException {
/* 63 */     if (obj instanceof ScriptableOutputStream.PendingLookup) {
/* 64 */       String name = ((ScriptableOutputStream.PendingLookup)obj).getName();
/* 65 */       obj = ScriptableOutputStream.lookupQualifiedName(this.scope, name);
/* 66 */       if (obj == Scriptable.NOT_FOUND) {
/* 67 */         throw new IOException("Object " + name + " not found upon " + "deserialization.");
/*    */       }
/*    */     }
/* 70 */     else if (obj instanceof UniqueTag) {
/* 71 */       obj = ((UniqueTag)obj).readResolve();
/* 72 */     } else if (obj instanceof Undefined) {
/* 73 */       obj = ((Undefined)obj).readResolve();
/*    */     } 
/* 75 */     return obj;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\serialize\ScriptableInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */