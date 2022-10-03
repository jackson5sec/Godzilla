/*    */ package javassist.tools.reflect;
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
/*    */ public class Sample
/*    */ {
/*    */   private Metaobject _metaobject;
/*    */   private static ClassMetaobject _classobject;
/*    */   
/*    */   public Object trap(Object[] args, int identifier) throws Throwable {
/* 28 */     Metaobject mobj = this._metaobject;
/* 29 */     if (mobj == null)
/* 30 */       return ClassMetaobject.invoke(this, identifier, args); 
/* 31 */     return mobj.trapMethodcall(identifier, args);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Object trapStatic(Object[] args, int identifier) throws Throwable {
/* 37 */     return _classobject.trapMethodcall(identifier, args);
/*    */   }
/*    */   
/*    */   public static Object trapRead(Object[] args, String name) {
/* 41 */     if (args[0] == null)
/* 42 */       return _classobject.trapFieldRead(name); 
/* 43 */     return ((Metalevel)args[0])._getMetaobject().trapFieldRead(name);
/*    */   }
/*    */   
/*    */   public static Object trapWrite(Object[] args, String name) {
/* 47 */     Metalevel base = (Metalevel)args[0];
/* 48 */     if (base == null) {
/* 49 */       _classobject.trapFieldWrite(name, args[1]);
/*    */     } else {
/* 51 */       base._getMetaobject().trapFieldWrite(name, args[1]);
/*    */     } 
/* 53 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\reflect\Sample.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */