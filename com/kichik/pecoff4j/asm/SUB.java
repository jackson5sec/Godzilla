/*    */ package com.kichik.pecoff4j.asm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SUB
/*    */   extends AbstractInstruction
/*    */ {
/*    */   private ModRM modrm;
/*    */   private int imm32;
/*    */   
/*    */   public SUB(ModRM modrm, int imm32) {
/* 17 */     this.modrm = modrm;
/* 18 */     this.imm32 = imm32;
/* 19 */     this.code = toCode(129, modrm, imm32);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toIntelAssembly() {
/* 24 */     return "sub  " + Register.to32(this.modrm.reg1) + ", " + 
/* 25 */       toHexString(this.imm32, false);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\SUB.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */