/*     */ package javassist.bytecode;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CodeAnalyzer
/*     */   implements Opcode
/*     */ {
/*     */   private ConstPool constPool;
/*     */   private CodeAttribute codeAttr;
/*     */   
/*     */   public CodeAnalyzer(CodeAttribute ca) {
/*  27 */     this.codeAttr = ca;
/*  28 */     this.constPool = ca.getConstPool();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int computeMaxStack() throws BadBytecode {
/*     */     boolean repeat;
/*  39 */     CodeIterator ci = this.codeAttr.iterator();
/*  40 */     int length = ci.getCodeLength();
/*  41 */     int[] stack = new int[length];
/*  42 */     this.constPool = this.codeAttr.getConstPool();
/*  43 */     initStack(stack, this.codeAttr);
/*     */     
/*     */     do {
/*  46 */       repeat = false;
/*  47 */       for (int j = 0; j < length; j++) {
/*  48 */         if (stack[j] < 0)
/*  49 */         { repeat = true;
/*  50 */           visitBytecode(ci, stack, j); } 
/*     */       } 
/*  52 */     } while (repeat);
/*     */     
/*  54 */     int maxStack = 1;
/*  55 */     for (int i = 0; i < length; i++) {
/*  56 */       if (stack[i] > maxStack)
/*  57 */         maxStack = stack[i]; 
/*     */     } 
/*  59 */     return maxStack - 1;
/*     */   }
/*     */   
/*     */   private void initStack(int[] stack, CodeAttribute ca) {
/*  63 */     stack[0] = -1;
/*  64 */     ExceptionTable et = ca.getExceptionTable();
/*  65 */     if (et != null) {
/*  66 */       int size = et.size();
/*  67 */       for (int i = 0; i < size; i++) {
/*  68 */         stack[et.handlerPc(i)] = -2;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void visitBytecode(CodeIterator ci, int[] stack, int index) throws BadBytecode {
/*  75 */     int codeLength = stack.length;
/*  76 */     ci.move(index);
/*  77 */     int stackDepth = -stack[index];
/*  78 */     int[] jsrDepth = new int[1];
/*  79 */     jsrDepth[0] = -1;
/*  80 */     while (ci.hasNext()) {
/*  81 */       index = ci.next();
/*  82 */       stack[index] = stackDepth;
/*  83 */       int op = ci.byteAt(index);
/*  84 */       stackDepth = visitInst(op, ci, index, stackDepth);
/*  85 */       if (stackDepth < 1) {
/*  86 */         throw new BadBytecode("stack underflow at " + index);
/*     */       }
/*  88 */       if (processBranch(op, ci, index, codeLength, stack, stackDepth, jsrDepth)) {
/*     */         break;
/*     */       }
/*  91 */       if (isEnd(op)) {
/*     */         break;
/*     */       }
/*  94 */       if (op == 168 || op == 201) {
/*  95 */         stackDepth--;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean processBranch(int opcode, CodeIterator ci, int index, int codeLength, int[] stack, int stackDepth, int[] jsrDepth) throws BadBytecode {
/* 103 */     if ((153 <= opcode && opcode <= 166) || opcode == 198 || opcode == 199) {
/*     */       
/* 105 */       int target = index + ci.s16bitAt(index + 1);
/* 106 */       checkTarget(index, target, codeLength, stack, stackDepth);
/*     */     } else {
/*     */       int target;
/*     */       int index2;
/* 110 */       switch (opcode) {
/*     */         case 167:
/* 112 */           target = index + ci.s16bitAt(index + 1);
/* 113 */           checkTarget(index, target, codeLength, stack, stackDepth);
/* 114 */           return true;
/*     */         case 200:
/* 116 */           target = index + ci.s32bitAt(index + 1);
/* 117 */           checkTarget(index, target, codeLength, stack, stackDepth);
/* 118 */           return true;
/*     */         case 168:
/*     */         case 201:
/* 121 */           if (opcode == 168) {
/* 122 */             target = index + ci.s16bitAt(index + 1);
/*     */           } else {
/* 124 */             target = index + ci.s32bitAt(index + 1);
/*     */           } 
/* 126 */           checkTarget(index, target, codeLength, stack, stackDepth);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 134 */           if (jsrDepth[0] < 0) {
/* 135 */             jsrDepth[0] = stackDepth;
/* 136 */             return false;
/*     */           } 
/* 138 */           if (stackDepth == jsrDepth[0]) {
/* 139 */             return false;
/*     */           }
/* 141 */           throw new BadBytecode("sorry, cannot compute this data flow due to JSR: " + stackDepth + "," + jsrDepth[0]);
/*     */ 
/*     */         
/*     */         case 169:
/* 145 */           if (jsrDepth[0] < 0) {
/* 146 */             jsrDepth[0] = stackDepth + 1;
/* 147 */             return false;
/*     */           } 
/* 149 */           if (stackDepth + 1 == jsrDepth[0]) {
/* 150 */             return true;
/*     */           }
/* 152 */           throw new BadBytecode("sorry, cannot compute this data flow due to RET: " + stackDepth + "," + jsrDepth[0]);
/*     */ 
/*     */         
/*     */         case 170:
/*     */         case 171:
/* 157 */           index2 = (index & 0xFFFFFFFC) + 4;
/* 158 */           target = index + ci.s32bitAt(index2);
/* 159 */           checkTarget(index, target, codeLength, stack, stackDepth);
/* 160 */           if (opcode == 171) {
/* 161 */             int npairs = ci.s32bitAt(index2 + 4);
/* 162 */             index2 += 12;
/* 163 */             for (int i = 0; i < npairs; i++) {
/* 164 */               target = index + ci.s32bitAt(index2);
/* 165 */               checkTarget(index, target, codeLength, stack, stackDepth);
/*     */               
/* 167 */               index2 += 8;
/*     */             } 
/*     */           } else {
/*     */             
/* 171 */             int low = ci.s32bitAt(index2 + 4);
/* 172 */             int high = ci.s32bitAt(index2 + 8);
/* 173 */             int n = high - low + 1;
/* 174 */             index2 += 12;
/* 175 */             for (int i = 0; i < n; i++) {
/* 176 */               target = index + ci.s32bitAt(index2);
/* 177 */               checkTarget(index, target, codeLength, stack, stackDepth);
/*     */               
/* 179 */               index2 += 4;
/*     */             } 
/*     */           } 
/*     */           
/* 183 */           return true;
/*     */       } 
/*     */     
/*     */     } 
/* 187 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkTarget(int opIndex, int target, int codeLength, int[] stack, int stackDepth) throws BadBytecode {
/* 194 */     if (target < 0 || codeLength <= target) {
/* 195 */       throw new BadBytecode("bad branch offset at " + opIndex);
/*     */     }
/* 197 */     int d = stack[target];
/* 198 */     if (d == 0) {
/* 199 */       stack[target] = -stackDepth;
/* 200 */     } else if (d != stackDepth && d != -stackDepth) {
/* 201 */       throw new BadBytecode("verification error (" + stackDepth + "," + d + ") at " + opIndex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isEnd(int opcode) {
/* 206 */     return ((172 <= opcode && opcode <= 177) || opcode == 191);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int visitInst(int op, CodeIterator ci, int index, int stack) throws BadBytecode {
/*     */     String desc;
/* 216 */     switch (op)
/*     */     { case 180:
/* 218 */         stack += getFieldSize(ci, index) - 1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 260 */         return stack;case 181: stack -= getFieldSize(ci, index) + 1; return stack;case 178: stack += getFieldSize(ci, index); return stack;case 179: stack -= getFieldSize(ci, index); return stack;case 182: case 183: desc = this.constPool.getMethodrefType(ci.u16bitAt(index + 1)); stack += Descriptor.dataSize(desc) - 1; return stack;case 184: desc = this.constPool.getMethodrefType(ci.u16bitAt(index + 1)); stack += Descriptor.dataSize(desc); return stack;case 185: desc = this.constPool.getInterfaceMethodrefType(ci.u16bitAt(index + 1)); stack += Descriptor.dataSize(desc) - 1; return stack;case 186: desc = this.constPool.getInvokeDynamicType(ci.u16bitAt(index + 1)); stack += Descriptor.dataSize(desc); return stack;case 191: stack = 1; return stack;case 197: stack += 1 - ci.byteAt(index + 3); return stack;case 196: op = ci.byteAt(index + 1); break; }  stack += STACK_GROW[op]; return stack;
/*     */   }
/*     */   
/*     */   private int getFieldSize(CodeIterator ci, int index) {
/* 264 */     String desc = this.constPool.getFieldrefType(ci.u16bitAt(index + 1));
/* 265 */     return Descriptor.dataSize(desc);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\CodeAnalyzer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */