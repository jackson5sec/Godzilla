/*     */ package com.kichik.pecoff4j.asm;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MOV
/*     */   extends AbstractInstruction
/*     */ {
/*     */   private ModRM modrm;
/*     */   private SIB sib;
/*     */   private int disp32;
/*     */   private int imm32;
/*     */   
/*     */   public MOV(ModRM modrm) {
/*  19 */     this.modrm = modrm;
/*  20 */     this.code = toCode(139, modrm);
/*     */   }
/*     */   
/*     */   public MOV(ModRM modrm, byte imm8) {
/*  24 */     this.modrm = modrm;
/*  25 */     this.imm32 = imm8;
/*  26 */     this.code = toCode(139, modrm, imm8);
/*     */   }
/*     */   
/*     */   public MOV(ModRM modrm, int disp32, int imm32) {
/*  30 */     this.modrm = modrm;
/*  31 */     this.disp32 = disp32;
/*  32 */     this.imm32 = imm32;
/*  33 */     this.code = toCode(199, modrm, disp32, imm32);
/*     */   }
/*     */   
/*     */   public MOV(ModRM modrm, byte disp8, int imm32) {
/*  37 */     this.modrm = modrm;
/*  38 */     this.disp32 = disp8;
/*  39 */     this.imm32 = imm32;
/*  40 */     this.code = toCode(199, modrm, disp8, imm32);
/*     */   }
/*     */   
/*     */   public MOV(ModRM modrm, int imm32) {
/*  44 */     this.modrm = modrm;
/*  45 */     this.imm32 = imm32;
/*  46 */     this.code = toCode(137, modrm, imm32);
/*     */   }
/*     */   
/*     */   public MOV(int opcode, ModRM modrm, byte imm8) {
/*  50 */     this.modrm = modrm;
/*  51 */     this.imm32 = imm8;
/*  52 */     this.code = toCode(opcode, modrm, imm8);
/*     */   }
/*     */   
/*     */   public MOV(int opcode, ModRM modrm, int imm32) {
/*  56 */     this.modrm = modrm;
/*  57 */     this.imm32 = imm32;
/*  58 */     this.code = toCode(opcode, modrm, imm32);
/*     */   }
/*     */   
/*     */   public MOV(int opcode, int imm32) {
/*  62 */     this.imm32 = imm32;
/*  63 */     this.code = toCode(opcode, imm32);
/*     */   }
/*     */   
/*     */   public MOV(ModRM modrm, SIB sib, byte imm8) {
/*  67 */     this.modrm = modrm;
/*  68 */     this.sib = sib;
/*  69 */     this.imm32 = imm8;
/*  70 */     this.code = toCode(137, modrm, sib, imm8);
/*     */   }
/*     */   
/*     */   public MOV(int opcode, ModRM modrm, SIB sib, int imm32) {
/*  74 */     this.modrm = modrm;
/*  75 */     this.sib = sib;
/*  76 */     this.imm32 = imm32;
/*  77 */     this.code = toCode(opcode, modrm, sib, imm32);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toIntelAssembly() {
/*  82 */     switch (this.code[0] & 0xFF) {
/*     */       case 139:
/*  84 */         switch (this.modrm.mod) {
/*     */           case 0:
/*  86 */             return "mov  [" + Register.to32(this.modrm.reg2) + "], " + 
/*  87 */               Register.to32(this.modrm.reg1);
/*     */           case 1:
/*  89 */             return "mov  " + Register.to32(this.modrm.reg2) + ", [" + 
/*  90 */               Register.to32(this.modrm.reg1) + 
/*  91 */               toHexString((byte)this.imm32, true) + "]";
/*     */           case 2:
/*  93 */             return "mov  " + Register.to32(this.modrm.reg2) + ", [" + 
/*  94 */               Register.to32(this.modrm.reg1) + toHexString(this.imm32, true) + "]";
/*     */           
/*     */           case 3:
/*  97 */             return "mov  " + Register.to32(this.modrm.reg2) + ", " + 
/*  98 */               Register.to32(this.modrm.reg1);
/*     */         } 
/*     */       case 137:
/* 101 */         switch (this.modrm.mod) {
/*     */         
/*     */         } 
/*     */         
/* 105 */         return "mov  [" + Register.to32(this.modrm.reg1) + 
/* 106 */           toHexString(this.imm32, true) + "], " + 
/* 107 */           Register.to32(this.modrm.reg2);
/*     */       case 198:
/* 109 */         return "mov  byte ptr [" + Register.to32(this.modrm.reg1) + "], " + 
/* 110 */           toHexString((byte)this.imm32, false);
/*     */       case 199:
/* 112 */         switch (this.modrm.mod) {
/*     */           case 1:
/* 114 */             return "mov  dword ptr [" + Register.to32(this.modrm.reg1) + 
/* 115 */               toHexString((byte)this.disp32, true) + "], " + 
/* 116 */               toHexString(this.imm32, false);
/*     */           case 2:
/* 118 */             return "mov  dword ptr [" + Register.to32(this.modrm.reg1) + 
/* 119 */               toHexString(this.disp32, true) + "], " + 
/* 120 */               toHexString(this.imm32, false);
/*     */         } 
/*     */       case 161:
/* 123 */         return "mov  eax, [" + toHexString(this.imm32, false) + "]";
/*     */       case 163:
/* 125 */         return "mov  [" + toHexString(this.imm32, false) + "], eax";
/*     */     } 
/*     */     
/* 128 */     return "MOV: UNKNOWN";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\MOV.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */