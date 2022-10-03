/*    */ package com.kichik.pecoff4j.asm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CMP
/*    */   extends AbstractInstruction
/*    */ {
/*    */   private ModRM modrm;
/*    */   private byte imm8;
/*    */   
/*    */   public CMP(ModRM modrm, byte imm8) {
/* 17 */     this.modrm = modrm;
/* 18 */     this.imm8 = imm8;
/* 19 */     this.code = toCode(59, modrm, imm8);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toIntelAssembly() {
/* 24 */     return "cmp  " + Register.to32(this.modrm.reg2) + ", [" + 
/* 25 */       Register.to32(this.modrm.reg1) + toHexString(this.imm8, true) + "]";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\CMP.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */