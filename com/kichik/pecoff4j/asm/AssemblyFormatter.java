/*    */ package com.kichik.pecoff4j.asm;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
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
/*    */ public class AssemblyFormatter
/*    */ {
/*    */   public static void format(AbstractInstruction[] instructions, PrintStream out) throws IOException {
/* 18 */     for (AbstractInstruction ai : instructions) {
/* 19 */       out.print(AbstractInstruction.toHexString(ai.getOffset(), false));
/* 20 */       out.print("   ");
/* 21 */       out.print(toHexString(ai.toCode(), 30));
/* 22 */       out.println(ai.toIntelAssembly());
/*    */     } 
/*    */   }
/*    */   
/*    */   public static String toHexString(byte[] bytes, int pad) {
/* 27 */     StringBuilder sb = new StringBuilder();
/* 28 */     for (byte b : bytes) {
/* 29 */       sb.append(toHexString(b));
/*    */     }
/* 31 */     for (int i = pad - bytes.length * 2; i > 0; i--)
/* 32 */       sb.append(' '); 
/* 33 */     return sb.toString();
/*    */   }
/*    */   
/*    */   private static String toHexString(byte b) {
/* 37 */     String s = Integer.toHexString(b & 0xFF);
/* 38 */     if (s.length() == 1) {
/* 39 */       return "0" + s;
/*    */     }
/* 41 */     return s;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\AssemblyFormatter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */