/*    */ package com.kichik.pecoff4j.asm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JumpIfInstruction
/*    */   extends AbstractInstruction
/*    */ {
/*    */   private int op;
/*    */   private int imm32;
/*    */   
/*    */   public JumpIfInstruction(int op, int imm32) {
/* 17 */     this.op = op;
/* 18 */     this.imm32 = imm32;
/* 19 */     this.code = toCode(15, new ModRM(op), imm32);
/*    */   }
/*    */   
/*    */   public String getOp() {
/* 23 */     switch (this.op) {
/*    */       case 133:
/* 25 */         return "jnz";
/*    */       case 141:
/* 27 */         return "jge";
/*    */     } 
/* 29 */     return "???";
/*    */   }
/*    */ 
/*    */   
/*    */   public String toIntelAssembly() {
/* 34 */     return getOp() + "  " + toHexString(this.imm32, false) + " (" + 
/* 35 */       toHexString(this.offset + this.imm32 + size(), false) + ")";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\JumpIfInstruction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */