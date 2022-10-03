/*    */ package com.kichik.pecoff4j.asm;
/*    */ 
/*    */ public class SIB {
/*    */   public final int scale;
/*    */   public final int index;
/*    */   public final int base;
/*    */   
/*    */   public SIB(int value) {
/*  9 */     value &= 0xFF;
/* 10 */     this.scale = value >> 6;
/* 11 */     this.index = value >> 3 & 0x7;
/* 12 */     this.base = value & 0x7;
/*    */   }
/*    */   
/*    */   public byte encode() {
/* 16 */     return (byte)(this.scale << 6 | this.index << 3 | this.base);
/*    */   }
/*    */   
/*    */   public String toString(int imm32) {
/* 20 */     return Register.to32(this.index) + "*" + (this.scale * 2) + "+" + 
/* 21 */       Register.to32(this.base) + 
/* 22 */       AbstractInstruction.toHexString(imm32, true);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\SIB.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */