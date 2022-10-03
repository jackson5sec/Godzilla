/*    */ package com.kichik.pecoff4j.asm;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModRM
/*    */ {
/*    */   public final int value;
/*    */   public final int mod;
/*    */   public final int reg1;
/*    */   public final int reg2;
/*    */   
/*    */   public ModRM(int value) {
/* 19 */     this.value = value;
/* 20 */     this.mod = value >> 6 & 0xF;
/* 21 */     this.reg2 = value >> 3 & 0x7;
/* 22 */     this.reg1 = value & 0x7;
/*    */   }
/*    */   
/*    */   public byte encode() {
/* 26 */     return (byte)(this.mod << 6 | this.reg2 << 3 | this.reg1);
/*    */   }
/*    */   
/*    */   public String toIntelAssembly(int imm32) {
/* 30 */     switch (this.mod) {
/*    */       case 0:
/* 32 */         return Register.to32(this.reg2) + ", " + Register.to32(this.reg1);
/*    */       case 1:
/* 34 */         return Register.to32(this.reg2) + ", [" + Register.to32(this.reg1) + 
/* 35 */           AbstractInstruction.toHexString((byte)imm32, true) + "]";
/*    */       case 2:
/* 37 */         return Register.to32(this.reg2) + ", [" + Register.to32(this.reg1) + 
/* 38 */           AbstractInstruction.toHexString(imm32, true) + "]";
/*    */     } 
/*    */     
/* 41 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\ModRM.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */