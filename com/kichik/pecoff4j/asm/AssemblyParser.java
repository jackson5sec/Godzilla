/*     */ package com.kichik.pecoff4j.asm;
/*     */ 
/*     */ import com.kichik.pecoff4j.util.Reflection;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AssemblyParser
/*     */ {
/*     */   public static AbstractInstruction[] parseAll(int offset, InputStream is) throws IOException {
/*  22 */     List<AbstractInstruction> instructions = new ArrayList<>();
/*  23 */     AbstractInstruction ins = null;
/*  24 */     while ((ins = parse(is)) != null) {
/*  25 */       ins.setOffset(offset);
/*  26 */       offset += ins.size();
/*  27 */       instructions.add(ins);
/*     */     } 
/*  29 */     return instructions
/*  30 */       .<AbstractInstruction>toArray(new AbstractInstruction[instructions.size()]);
/*     */   }
/*     */   
/*     */   public static AbstractInstruction parse(InputStream is) throws IOException {
/*  34 */     int imm32, disp32, opcode = is.read() & 0xFF;
/*  35 */     int highop = opcode & 0xF0;
/*     */ 
/*     */     
/*  38 */     ModRM modrm = null;
/*  39 */     SIB sib = null;
/*  40 */     switch (highop) {
/*     */       case 0:
/*  42 */         switch (opcode) {
/*     */           case 3:
/*  44 */             modrm = new ModRM(is.read());
/*  45 */             imm32 = readDoubleWord(is);
/*  46 */             return new ADD(opcode, modrm, imm32);
/*     */           case 15:
/*  48 */             return new JumpIfInstruction(is.read(), readDoubleWord(is));
/*     */         } 
/*     */         break;
/*     */       case 48:
/*  52 */         switch (opcode) {
/*     */           case 59:
/*  54 */             modrm = new ModRM(is.read());
/*  55 */             imm32 = is.read();
/*  56 */             return new CMP(modrm, (byte)imm32);
/*     */         } 
/*     */         break;
/*     */       case 80:
/*  60 */         if (opcode < 88) {
/*  61 */           return new PUSH(opcode & 0xF);
/*     */         }
/*  63 */         return new POP(opcode >> 4 & 0xF);
/*     */       
/*     */       case 96:
/*  66 */         switch (opcode) {
/*     */           case 104:
/*  68 */             return new PUSH(opcode, readDoubleWord(is));
/*     */           case 106:
/*  70 */             return new PUSH((byte)is.read());
/*     */         } 
/*     */         break;
/*     */       case 112:
/*  74 */         switch (opcode) {
/*     */           case 125:
/*  76 */             return new JGE((byte)is.read());
/*     */         } 
/*     */         break;
/*     */       case 128:
/*  80 */         modrm = new ModRM(is.read());
/*  81 */         switch (opcode) {
/*     */           case 139:
/*  83 */             if (modrm.mod < 3 && modrm.reg1 == 4)
/*  84 */               sib = new SIB(is.read()); 
/*  85 */             switch (modrm.mod) {
/*     */               case 0:
/*     */               case 1:
/*  88 */                 imm32 = is.read();
/*  89 */                 if (sib != null) {
/*  90 */                   return new MOV(modrm, sib, (byte)imm32);
/*     */                 }
/*  92 */                 return new MOV(modrm, (byte)imm32);
/*     */               case 2:
/*  94 */                 imm32 = readDoubleWord(is);
/*  95 */                 if (sib != null) {
/*  96 */                   return new MOV(opcode, modrm, sib, imm32);
/*     */                 }
/*  98 */                 return new MOV(opcode, modrm, imm32);
/*     */             } 
/* 100 */             return new MOV(modrm);
/*     */           case 129:
/* 102 */             imm32 = readDoubleWord(is);
/* 103 */             return new SUB(modrm, imm32);
/*     */           case 131:
/* 105 */             imm32 = is.read();
/* 106 */             return new ADD(modrm, (byte)imm32);
/*     */           case 137:
/* 108 */             switch (modrm.mod) {
/*     */               case 0:
/*     */               case 1:
/* 111 */                 imm32 = is.read();
/* 112 */                 return new MOV(opcode, modrm, (byte)imm32);
/*     */               case 2:
/* 114 */                 imm32 = readDoubleWord(is);
/* 115 */                 return new MOV(modrm, imm32);
/*     */             } 
/*     */           case 133:
/* 118 */             return new TEST(modrm);
/*     */           case 141:
/* 120 */             if (modrm.mod < 3 && modrm.reg1 == 4) {
/* 121 */               sib = new SIB(is.read());
/* 122 */               imm32 = readDoubleWord(is);
/* 123 */               return new LEA(modrm, sib, imm32);
/*     */             } 
/* 125 */             imm32 = readDoubleWord(is);
/* 126 */             return new LEA(modrm, imm32);
/*     */         } 
/* 128 */         print(modrm);
/*     */         break;
/*     */       case 160:
/* 131 */         switch (opcode) {
/*     */           case 161:
/*     */           case 163:
/* 134 */             return new MOV(opcode, readDoubleWord(is));
/*     */         } 
/*     */         break;
/*     */       case 192:
/* 138 */         switch (opcode) {
/*     */           case 193:
/* 140 */             modrm = new ModRM(is.read());
/* 141 */             imm32 = is.read();
/* 142 */             return new SHL(modrm, (byte)imm32);
/*     */           case 195:
/* 144 */             return new RET();
/*     */           case 198:
/* 146 */             modrm = new ModRM(is.read());
/* 147 */             imm32 = is.read();
/* 148 */             return new MOV(opcode, modrm, (byte)imm32);
/*     */           case 199:
/* 150 */             modrm = new ModRM(is.read());
/* 151 */             switch (modrm.mod) {
/*     */               case 1:
/* 153 */                 disp32 = is.read();
/* 154 */                 imm32 = readDoubleWord(is);
/* 155 */                 return new MOV(modrm, (byte)disp32, imm32);
/*     */             } 
/* 157 */             disp32 = readDoubleWord(is);
/* 158 */             imm32 = readDoubleWord(is);
/* 159 */             return new MOV(modrm, disp32, imm32);
/*     */         } 
/*     */         break;
/*     */       case 224:
/* 163 */         switch (opcode) {
/*     */           case 232:
/* 165 */             return new CALL(opcode, readDoubleWord(is));
/*     */           case 233:
/* 167 */             return new JMP(readDoubleWord(is));
/*     */           case 235:
/* 169 */             return new JMP((byte)is.read());
/*     */         } 
/*     */         break;
/*     */       case 240:
/* 173 */         switch (opcode) {
/*     */           case 255:
/* 175 */             modrm = new ModRM(is.read());
/* 176 */             imm32 = readDoubleWord(is);
/* 177 */             return new CALL(modrm, imm32);
/*     */         } 
/*     */         break;
/*     */     } 
/* 181 */     println(Integer.valueOf(opcode));
/* 182 */     return null;
/*     */   }
/*     */   
/*     */   public static int readDoubleWord(InputStream is) throws IOException {
/* 186 */     return is.read() | is.read() << 8 | is.read() << 16 | is.read() << 24;
/*     */   }
/*     */   
/*     */   public static void print(Object o) {
/* 190 */     System.out.print(Reflection.toString(o));
/*     */   }
/*     */   
/*     */   public static void println(Object o) {
/* 194 */     System.out.println(Reflection.toString(o));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\asm\AssemblyParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */