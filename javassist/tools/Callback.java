/*     */ package javassist.tools;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CtBehavior;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Callback
/*     */ {
/*  53 */   public static Map<String, Callback> callbacks = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String sourceCode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Callback(String src) {
/*  65 */     String uuid = UUID.randomUUID().toString();
/*  66 */     callbacks.put(uuid, this);
/*  67 */     this.sourceCode = "((javassist.tools.Callback) javassist.tools.Callback.callbacks.get(\"" + uuid + "\")).result(new Object[]{" + src + "});";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void result(Object[] paramArrayOfObject);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  79 */     return sourceCode();
/*     */   }
/*     */   
/*     */   public String sourceCode() {
/*  83 */     return this.sourceCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void insertBefore(CtBehavior behavior, Callback callback) throws CannotCompileException {
/*  96 */     behavior.insertBefore(callback.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void insertAfter(CtBehavior behavior, Callback callback) throws CannotCompileException {
/* 112 */     behavior.insertAfter(callback.toString(), false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void insertAfter(CtBehavior behavior, Callback callback, boolean asFinally) throws CannotCompileException {
/* 133 */     behavior.insertAfter(callback.toString(), asFinally);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int insertAt(CtBehavior behavior, Callback callback, int lineNum) throws CannotCompileException {
/* 152 */     return behavior.insertAt(lineNum, callback.toString());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\Callback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */