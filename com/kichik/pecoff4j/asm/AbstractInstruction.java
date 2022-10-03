/*     */ package com.kichik.pecoff4j.asm;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractInstruction
/*     */   implements Instruction
/*     */ {
/*     */   protected byte[] code;
/*     */   protected int offset;
/*     */   protected String label;
/*     */   
/*     */   public int size() {
/*  19 */     return this.code.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] toCode() {
/*  24 */     return this.code;
/*     */   }
/*     */   
/*     */   public int getOpCode() {
/*  28 */     return this.code[0] & 0xFF;
/*     */   }
/*     */   
/*     */   public void setOffset(int offset) {
/*  32 */     this.offset = offset;
/*     */   }
/*     */   
/*     */   public int getOffset() {
/*  36 */     return this.offset;
/*     */   }
/*     */   
/*     */   public void setLabel(String label) {
/*  40 */     this.label = label;
/*     */   }
/*     */   
/*     */   public String getLabel() {
/*  44 */     return this.label;
/*     */   }
/*     */   
/*     */   protected byte[] toCode(int opcode) {
/*  48 */     return new byte[] { (byte)opcode };
/*     */   }
/*     */   
/*     */   protected byte[] toCode(int opcode, ModRM modrm) {
/*  52 */     return new byte[] { (byte)opcode, modrm.encode() };
/*     */   }
/*     */   
/*     */   protected byte[] toCode(int opcode, byte imm8) {
/*  56 */     return new byte[] { (byte)opcode, imm8 };
/*     */   }
/*     */   
/*     */   protected byte[] toCode(int opcode, int imm32) {
/*  60 */     return new byte[] { (byte)opcode, b1(imm32), b2(imm32), b3(imm32), 
/*  61 */         b4(imm32) };
/*     */   }
/*     */   
/*     */   protected byte[] toCode(int opcode, ModRM modrm, byte imm8) {
/*  65 */     return new byte[] { (byte)opcode, modrm.encode(), imm8 };
/*     */   }
/*     */   
/*     */   protected byte[] toCode(int opcode, ModRM modrm, int imm32) {
/*  69 */     return new byte[] { (byte)opcode, modrm.encode(), b1(imm32), 
/*  70 */         b2(imm32), b3(imm32), b4(imm32) };
/*     */   }
/*     */   
/*     */   protected byte[] toCode(int opcode, ModRM modrm, SIB sib, byte imm8) {
/*  74 */     return new byte[] { (byte)opcode, modrm.encode(), sib
/*  75 */         .encode(), imm8 };
/*     */   }
/*     */   
/*     */   protected byte[] toCode(int opcode, ModRM modrm, SIB sib, int imm32) {
/*  79 */     return new byte[] { (byte)opcode, modrm.encode(), sib
/*  80 */         .encode(), b1(imm32), b2(imm32), b3(imm32), b4(imm32) };
/*     */   }
/*     */   
/*     */   protected byte[] toCode(int opcode, ModRM modrm, int disp32, int imm32) {
/*  84 */     return new byte[] { (byte)opcode, modrm.encode(), b1(disp32), 
/*  85 */         b2(disp32), b3(disp32), b4(disp32), b1(imm32), b2(imm32), 
/*  86 */         b3(imm32), b4(imm32) };
/*     */   }
/*     */   
/*     */   protected byte[] toCode(int opcode, ModRM modrm, byte disp8, int imm32) {
/*  90 */     return new byte[] { (byte)opcode, modrm.encode(), disp8, 
/*  91 */         b1(imm32), b2(imm32), b3(imm32), b4(imm32) };
/*     */   }
/*     */   
/*     */   protected byte b1(int value) {
/*  95 */     return (byte)(value & 0xFF);
/*     */   }
/*     */   
/*     */   protected byte b2(int value) {
/*  99 */     return (byte)(value >> 8 & 0xFF);
/*     */   }
/*     */   
/*     */   protected byte b3(int value) {
/* 103 */     return (byte)(value >> 16 & 0xFF);
/*     */   }
/*     */   
/*     */   protected byte b4(int value) {
/* 107 */     return (byte)(value >> 24 & 0xFF);
/*     */   }
/*     */   
/*     */   public static String toHexString(int value, boolean showSign) {
/* 111 */     StringBuilder sb = new StringBuilder();
/* 112 */     if (showSign)
/* 113 */       if (value < 0) {
/* 114 */         value *= -1;
/* 115 */         sb.append('-');
/*     */       } else {
/* 117 */         sb.append('+');
/*     */       }  
/* 119 */     String s = Integer.toHexString(value);
/* 120 */     int pad = 8 - s.length();
/* 121 */     for (int i = 0; i < pad; i++)
/* 122 */       sb.append('0'); 
/* 123 */     sb.append(s);
/*     */     
/* 125 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String toHexString(byte value, boolean showSign) {
/* 129 */     StringBuilder sb = new StringBuilder();
/* 130 */     if (showSign)
/* 131 */       if (value < 0) {
/* 132 */         value = (byte)(value * -1);
/* 133 */         sb.append('-');
/*     */       } else {
/* 135 */         sb.append('+');
/*     */       }  
/* 137 */     String s = Integer.toHexString(value & 0xFF);
/* 138 */     int pad = 2 - s.length();
/* 139 */     for (int i = 0; i < pad; i++)
/* 140 */       sb.append('0'); 
/* 141 */     sb.append(s);
/*     */     
/* 143 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\AbstractInstruction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */