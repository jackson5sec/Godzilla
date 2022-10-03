/*    */ package com.kichik.pecoff4j.asm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CALL
/*    */   extends AbstractInstruction
/*    */ {
/*    */   private int imm32;
/*    */   
/*    */   public CALL(ModRM modrm, int imm32) {
/* 16 */     this.imm32 = imm32;
/* 17 */     this.code = toCode(255, modrm, imm32);
/*    */   }
/*    */   
/*    */   public CALL(int opcode, int imm32) {
/* 21 */     this.imm32 = imm32;
/* 22 */     this.code = toCode(opcode, imm32);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toIntelAssembly() {
/* 27 */     switch (getOpCode()) {
/*    */       case 232:
/* 29 */         return "call " + toHexString(this.imm32, false) + " (" + 
/* 30 */           toHexString(this.offset + this.imm32 + size(), false) + ")";
/*    */     } 
/* 32 */     return "call " + toHexString(this.imm32, false);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\CALL.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */