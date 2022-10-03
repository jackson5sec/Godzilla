/*    */ package core;
/*    */ 
/*    */ import core.shell.ShellEntity;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import util.Log;
/*    */ 
/*    */ public class Encoding {
/*    */   private String charsetString;
/*    */   
/*    */   private Encoding(String charsetString) {
/* 11 */     this.charsetString = charsetString;
/*    */   }
/* 13 */   private static final String[] ENCODING_TYPES = new String[] { "UTF-8", "GBK", "GB2312", "BIG5", "GB18030", "ISO-8859-1", "latin1", "UTF16", "ascii", "cp850" };
/*    */   public static String[] getAllEncodingTypes() {
/* 15 */     return ENCODING_TYPES;
/*    */   }
/*    */   public byte[] Encoding(String string) {
/*    */     try {
/* 19 */       return string.getBytes(this.charsetString);
/* 20 */     } catch (UnsupportedEncodingException e) {
/* 21 */       Log.error(e);
/* 22 */       return string.getBytes();
/*    */     } 
/*    */   }
/*    */   public String Decoding(byte[] srcData) {
/* 26 */     if (srcData == null) {
/* 27 */       return "";
/*    */     }
/*    */     try {
/* 30 */       return new String(srcData, this.charsetString);
/* 31 */     } catch (UnsupportedEncodingException e) {
/* 32 */       Log.error(e);
/* 33 */       return new String(srcData);
/*    */     } 
/*    */   }
/*    */   public void setCharsetString(String charsetString) {
/* 37 */     this.charsetString = charsetString;
/*    */   }
/*    */   public String getCharsetString() {
/* 40 */     return this.charsetString;
/*    */   }
/*    */   public static Encoding getEncoding(ShellEntity entity) {
/* 43 */     return entity.getEncodingModule();
/*    */   }
/*    */   public static Encoding getEncoding(String charsetString) {
/* 46 */     return new Encoding(charsetString);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return this.charsetString;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\Encoding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */