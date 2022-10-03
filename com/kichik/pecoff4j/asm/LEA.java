/*    */ package com.kichik.pecoff4j.asm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LEA
/*    */   extends AbstractInstruction
/*    */ {
/*    */   private ModRM modrm;
/*    */   private SIB sib;
/*    */   private int imm32;
/*    */   
/*    */   public LEA(ModRM modrm, int imm32) {
/* 18 */     this.modrm = modrm;
/* 19 */     this.imm32 = imm32;
/* 20 */     this.code = toCode(141, modrm, imm32);
/*    */   }
/*    */   
/*    */   public LEA(ModRM modrm, SIB sib, int imm32) {
/* 24 */     this.modrm = modrm;
/* 25 */     this.sib = sib;
/* 26 */     this.imm32 = imm32;
/* 27 */     this.code = toCode(141, modrm, sib, imm32);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toIntelAssembly() {
/* 32 */     if (this.sib != null) {
/* 33 */       return "lea  " + Register.to32(this.modrm.reg2) + ", [" + this.sib
/* 34 */         .toString(this.imm32) + "]";
/*    */     }
/* 36 */     return "lea  " + Register.to32(this.modrm.reg2) + ", [" + 
/* 37 */       Register.to32(this.modrm.reg1) + toHexString(this.imm32, true) + "]";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\LEA.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */