/*    */ package com.kichik.pecoff4j.asm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SHL
/*    */   extends AbstractInstruction
/*    */ {
/*    */   private ModRM modrm;
/*    */   private byte imm8;
/*    */   
/*    */   public SHL(ModRM modrm, byte imm8) {
/* 17 */     this.modrm = modrm;
/* 18 */     this.imm8 = imm8;
/* 19 */     this.code = toCode(193, modrm, imm8);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toIntelAssembly() {
/* 24 */     return "shl  " + Register.to32(this.modrm.reg1) + ", " + 
/* 25 */       toHexString(this.imm8, false);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\SHL.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */