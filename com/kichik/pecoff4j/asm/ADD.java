/*    */ package com.kichik.pecoff4j.asm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ADD
/*    */   extends AbstractInstruction
/*    */ {
/*    */   private ModRM modrm;
/*    */   private byte imm8;
/*    */   private int imm32;
/*    */   
/*    */   public ADD(ModRM modrm, byte imm8) {
/* 18 */     this.modrm = modrm;
/* 19 */     this.imm8 = imm8;
/* 20 */     this.code = toCode(131, modrm, imm8);
/*    */   }
/*    */   
/*    */   public ADD(int opcode, ModRM modrm, int imm32) {
/* 24 */     this.modrm = modrm;
/* 25 */     this.imm32 = imm32;
/* 26 */     this.code = toCode(opcode, modrm, imm32);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toIntelAssembly() {
/* 31 */     switch (getOpCode()) {
/*    */       case 3:
/* 33 */         return "add  " + this.modrm.toIntelAssembly(this.imm32);
/*    */     } 
/* 35 */     return "add  " + Register.to32(this.modrm.reg1) + ", " + 
/* 36 */       toHexString(this.imm8, false);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\ADD.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */