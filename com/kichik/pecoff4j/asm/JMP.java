/*    */ package com.kichik.pecoff4j.asm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JMP
/*    */   extends AbstractInstruction
/*    */ {
/*    */   private byte imm8;
/*    */   private int imm32;
/*    */   
/*    */   public JMP(byte imm8) {
/* 17 */     this.imm8 = imm8;
/* 18 */     this.code = toCode(235, imm8);
/*    */   }
/*    */   
/*    */   public JMP(int imm32) {
/* 22 */     this.imm32 = imm32;
/* 23 */     this.code = toCode(233, imm32);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toIntelAssembly() {
/* 28 */     switch (getOpCode()) {
/*    */       case 233:
/* 30 */         return "jmp  " + toHexString(this.imm32, false);
/*    */     } 
/* 32 */     return "jmp  " + toHexString(this.imm8, false);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\JMP.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */