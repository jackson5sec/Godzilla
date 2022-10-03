/*    */ package com.kichik.pecoff4j.asm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JGE
/*    */   extends AbstractInstruction
/*    */ {
/*    */   private byte imm8;
/*    */   
/*    */   public JGE(byte imm8) {
/* 16 */     this.imm8 = imm8;
/* 17 */     this.code = toCode(125, imm8);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toIntelAssembly() {
/* 22 */     return "jge  " + toHexString(this.imm8, true);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\JGE.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */