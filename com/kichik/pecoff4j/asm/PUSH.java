/*    */ package com.kichik.pecoff4j.asm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PUSH
/*    */   extends AbstractInstruction
/*    */ {
/*    */   private int register;
/*    */   private byte imm8;
/*    */   private int imm32;
/*    */   
/*    */   public PUSH(int register) {
/* 18 */     this.register = register;
/* 19 */     this.code = toCode(0x50 | register);
/*    */   }
/*    */   
/*    */   public PUSH(byte imm8) {
/* 23 */     this.imm8 = imm8;
/* 24 */     this.code = toCode(106, imm8);
/*    */   }
/*    */   
/*    */   public PUSH(int opcode, int imm32) {
/* 28 */     this.imm32 = imm32;
/* 29 */     this.code = toCode(opcode, imm32);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toIntelAssembly() {
/* 34 */     switch (getOpCode()) {
/*    */       case 106:
/* 36 */         return "push " + toHexString(this.imm8, false);
/*    */       case 104:
/* 38 */         return "push " + toHexString(this.imm32, false);
/*    */     } 
/* 40 */     return "push " + Register.to32(this.register);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\PUSH.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */