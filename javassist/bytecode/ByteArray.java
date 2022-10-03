/*    */ package javassist.bytecode;
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
/*    */ 
/*    */ 
/*    */ public class ByteArray
/*    */ {
/*    */   public static int readU16bit(byte[] code, int index) {
/* 27 */     return (code[index] & 0xFF) << 8 | code[index + 1] & 0xFF;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int readS16bit(byte[] code, int index) {
/* 34 */     return code[index] << 8 | code[index + 1] & 0xFF;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void write16bit(int value, byte[] code, int index) {
/* 41 */     code[index] = (byte)(value >>> 8);
/* 42 */     code[index + 1] = (byte)value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int read32bit(byte[] code, int index) {
/* 49 */     return code[index] << 24 | (code[index + 1] & 0xFF) << 16 | (code[index + 2] & 0xFF) << 8 | code[index + 3] & 0xFF;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void write32bit(int value, byte[] code, int index) {
/* 57 */     code[index] = (byte)(value >>> 24);
/* 58 */     code[index + 1] = (byte)(value >>> 16);
/* 59 */     code[index + 2] = (byte)(value >>> 8);
/* 60 */     code[index + 3] = (byte)value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void copy32bit(byte[] src, int isrc, byte[] dest, int idest) {
/* 72 */     dest[idest] = src[isrc];
/* 73 */     dest[idest + 1] = src[isrc + 1];
/* 74 */     dest[idest + 2] = src[isrc + 2];
/* 75 */     dest[idest + 3] = src[isrc + 3];
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\ByteArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */