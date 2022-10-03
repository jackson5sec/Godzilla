/*    */ package org.fife.rsta.ac.js.engine;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavaScriptEngineFactory
/*    */ {
/*    */   public static final String DEFAULT = "EMCA";
/* 11 */   private HashMap<String, JavaScriptEngine> supportedEngines = new HashMap<>();
/*    */ 
/*    */   
/* 14 */   private static JavaScriptEngineFactory Instance = new JavaScriptEngineFactory();
/*    */   
/*    */   static {
/* 17 */     Instance().addEngine("EMCA", new EMCAJavaScriptEngine());
/* 18 */     Instance().addEngine("JSR223", new JSR223JavaScriptEngine());
/* 19 */     Instance().addEngine("RHINO", new RhinoJavaScriptEngine());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static JavaScriptEngineFactory Instance() {
/* 28 */     return Instance;
/*    */   }
/*    */ 
/*    */   
/*    */   public JavaScriptEngine getEngineFromCache(String name) {
/* 33 */     if (name == null) {
/* 34 */       name = "EMCA";
/*    */     }
/* 36 */     return this.supportedEngines.get(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addEngine(String name, JavaScriptEngine engine) {
/* 41 */     this.supportedEngines.put(name, engine);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeEngine(String name) {
/* 46 */     this.supportedEngines.remove(name);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\engine\JavaScriptEngineFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */