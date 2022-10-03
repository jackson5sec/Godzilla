/*    */ package com.kichik.pecoff4j.asm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Register
/*    */ {
/*    */   public static String to32(int register) {
/* 14 */     switch (register) {
/*    */       case 0:
/* 16 */         return "eax";
/*    */       case 1:
/* 18 */         return "ecx";
/*    */       case 2:
/* 20 */         return "edx";
/*    */       case 3:
/* 22 */         return "ebx";
/*    */       case 4:
/* 24 */         return "esp";
/*    */       case 5:
/* 26 */         return "ebp";
/*    */       case 6:
/* 28 */         return "esi";
/*    */       case 7:
/* 30 */         return "edi";
/*    */     } 
/* 32 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\Register.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */