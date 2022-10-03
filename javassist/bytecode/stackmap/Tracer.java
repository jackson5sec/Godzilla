/*     */ package javassist.bytecode.stackmap;
/*     */ 
/*     */ import javassist.ClassPool;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.ByteArray;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.Opcode;
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
/*     */ public abstract class Tracer
/*     */   implements TypeTag
/*     */ {
/*     */   protected ClassPool classPool;
/*     */   protected ConstPool cpool;
/*     */   protected String returnType;
/*     */   protected int stackTop;
/*     */   protected TypeData[] stackTypes;
/*     */   protected TypeData[] localsTypes;
/*     */   
/*     */   public Tracer(ClassPool classes, ConstPool cp, int maxStack, int maxLocals, String retType) {
/*  42 */     this.classPool = classes;
/*  43 */     this.cpool = cp;
/*  44 */     this.returnType = retType;
/*  45 */     this.stackTop = 0;
/*  46 */     this.stackTypes = TypeData.make(maxStack);
/*  47 */     this.localsTypes = TypeData.make(maxLocals);
/*     */   }
/*     */   
/*     */   public Tracer(Tracer t) {
/*  51 */     this.classPool = t.classPool;
/*  52 */     this.cpool = t.cpool;
/*  53 */     this.returnType = t.returnType;
/*  54 */     this.stackTop = t.stackTop;
/*  55 */     this.stackTypes = TypeData.make(t.stackTypes.length);
/*  56 */     this.localsTypes = TypeData.make(t.localsTypes.length);
/*     */   }
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
/*     */   protected int doOpcode(int pos, byte[] code) throws BadBytecode {
/*     */     try {
/*  71 */       int op = code[pos] & 0xFF;
/*  72 */       if (op < 54)
/*  73 */         return doOpcode0_53(pos, code, op); 
/*  74 */       if (op < 96)
/*  75 */         return doOpcode54_95(pos, code, op); 
/*  76 */       if (op < 148)
/*  77 */         return doOpcode96_147(pos, code, op); 
/*  78 */       return doOpcode148_201(pos, code, op);
/*     */     }
/*  80 */     catch (ArrayIndexOutOfBoundsException e) {
/*  81 */       throw new BadBytecode("inconsistent stack height " + e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitBranch(int pos, byte[] code, int offset) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitGoto(int pos, byte[] code, int offset) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitReturn(int pos, byte[] code) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitThrow(int pos, byte[] code) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitTableSwitch(int pos, byte[] code, int n, int offsetPos, int defaultOffset) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitLookupSwitch(int pos, byte[] code, int n, int pairsPos, int defaultOffset) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitJSR(int pos, byte[] code) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitRET(int pos, byte[] code) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int doOpcode0_53(int pos, byte[] code, int op) throws BadBytecode {
/*     */     int reg, s;
/* 131 */     TypeData data, stackTypes[] = this.stackTypes;
/* 132 */     switch (op)
/*     */     
/*     */     { 
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
/*     */       case 0:
/* 244 */         return 1;case 1: stackTypes[this.stackTop++] = new TypeData.NullType();case 2: case 3: case 4: case 5: case 6: case 7: case 8: stackTypes[this.stackTop++] = INTEGER;case 9: case 10: stackTypes[this.stackTop++] = LONG; stackTypes[this.stackTop++] = TOP;case 11: case 12: case 13: stackTypes[this.stackTop++] = FLOAT;case 14: case 15: stackTypes[this.stackTop++] = DOUBLE; stackTypes[this.stackTop++] = TOP;case 16: case 17: stackTypes[this.stackTop++] = INTEGER; return (op == 17) ? 3 : 2;case 18: doLDC(code[pos + 1] & 0xFF); return 2;case 19: case 20: doLDC(ByteArray.readU16bit(code, pos + 1)); return 3;case 21: return doXLOAD(INTEGER, code, pos);case 22: return doXLOAD(LONG, code, pos);case 23: return doXLOAD(FLOAT, code, pos);case 24: return doXLOAD(DOUBLE, code, pos);case 25: return doALOAD(code[pos + 1] & 0xFF);case 26: case 27: case 28: case 29: stackTypes[this.stackTop++] = INTEGER;case 30: case 31: case 32: case 33: stackTypes[this.stackTop++] = LONG; stackTypes[this.stackTop++] = TOP;case 34: case 35: case 36: case 37: stackTypes[this.stackTop++] = FLOAT;case 38: case 39: case 40: case 41: stackTypes[this.stackTop++] = DOUBLE; stackTypes[this.stackTop++] = TOP;case 42: case 43: case 44: case 45: reg = op - 42; stackTypes[this.stackTop++] = this.localsTypes[reg];case 46: stackTypes[--this.stackTop - 1] = INTEGER;case 47: stackTypes[this.stackTop - 2] = LONG; stackTypes[this.stackTop - 1] = TOP;
/*     */       case 48: stackTypes[--this.stackTop - 1] = FLOAT;
/*     */       case 49: stackTypes[this.stackTop - 2] = DOUBLE; stackTypes[this.stackTop - 1] = TOP;
/*     */       case 50: s = --this.stackTop - 1; data = stackTypes[s]; stackTypes[s] = TypeData.ArrayElement.make(data);
/* 248 */       case 51: case 52: case 53: stackTypes[--this.stackTop - 1] = INTEGER; }  throw new RuntimeException("fatal"); } private void doLDC(int index) { TypeData[] stackTypes = this.stackTypes;
/* 249 */     int tag = this.cpool.getTag(index);
/* 250 */     if (tag == 8) {
/* 251 */       stackTypes[this.stackTop++] = new TypeData.ClassName("java.lang.String");
/* 252 */     } else if (tag == 3) {
/* 253 */       stackTypes[this.stackTop++] = INTEGER;
/* 254 */     } else if (tag == 4) {
/* 255 */       stackTypes[this.stackTop++] = FLOAT;
/* 256 */     } else if (tag == 5) {
/* 257 */       stackTypes[this.stackTop++] = LONG;
/* 258 */       stackTypes[this.stackTop++] = TOP;
/*     */     }
/* 260 */     else if (tag == 6) {
/* 261 */       stackTypes[this.stackTop++] = DOUBLE;
/* 262 */       stackTypes[this.stackTop++] = TOP;
/*     */     }
/* 264 */     else if (tag == 7) {
/* 265 */       stackTypes[this.stackTop++] = new TypeData.ClassName("java.lang.Class");
/* 266 */     } else if (tag == 17) {
/* 267 */       String desc = this.cpool.getDynamicType(index);
/* 268 */       pushMemberType(desc);
/*     */     } else {
/*     */       
/* 271 */       throw new RuntimeException("bad LDC: " + tag);
/*     */     }  }
/*     */   
/*     */   private int doXLOAD(TypeData type, byte[] code, int pos) {
/* 275 */     int localVar = code[pos + 1] & 0xFF;
/* 276 */     return doXLOAD(localVar, type);
/*     */   }
/*     */   
/*     */   private int doXLOAD(int localVar, TypeData type) {
/* 280 */     this.stackTypes[this.stackTop++] = type;
/* 281 */     if (type.is2WordType()) {
/* 282 */       this.stackTypes[this.stackTop++] = TOP;
/*     */     }
/* 284 */     return 2;
/*     */   }
/*     */   
/*     */   private int doALOAD(int localVar) {
/* 288 */     this.stackTypes[this.stackTop++] = this.localsTypes[localVar];
/* 289 */     return 2; } private int doOpcode54_95(int pos, byte[] code, int op) throws BadBytecode { int var; int i; int len;
/*     */     int sp;
/*     */     int j;
/*     */     TypeData t;
/* 293 */     switch (op) {
/*     */       case 54:
/* 295 */         return doXSTORE(pos, code, INTEGER);
/*     */       case 55:
/* 297 */         return doXSTORE(pos, code, LONG);
/*     */       case 56:
/* 299 */         return doXSTORE(pos, code, FLOAT);
/*     */       case 57:
/* 301 */         return doXSTORE(pos, code, DOUBLE);
/*     */       case 58:
/* 303 */         return doASTORE(code[pos + 1] & 0xFF);
/*     */       case 59:
/*     */       case 60:
/*     */       case 61:
/*     */       case 62:
/* 308 */         var = op - 59;
/* 309 */         this.localsTypes[var] = INTEGER;
/* 310 */         this.stackTop--;
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
/* 404 */         return 1;case 63: case 64: case 65: case 66: var = op - 63; this.localsTypes[var] = LONG; this.localsTypes[var + 1] = TOP; this.stackTop -= 2; return 1;case 67: case 68: case 69: case 70: var = op - 67; this.localsTypes[var] = FLOAT; this.stackTop--; return 1;case 71: case 72: case 73: case 74: var = op - 71; this.localsTypes[var] = DOUBLE; this.localsTypes[var + 1] = TOP; this.stackTop -= 2; return 1;case 75: case 76: case 77: case 78: var = op - 75; doASTORE(var); return 1;case 79: case 80: case 81: case 82: this.stackTop -= (op == 80 || op == 82) ? 4 : 3; return 1;case 83: TypeData.ArrayElement.aastore(this.stackTypes[this.stackTop - 3], this.stackTypes[this.stackTop - 1], this.classPool); this.stackTop -= 3; return 1;case 84: case 85: case 86: this.stackTop -= 3; return 1;case 87: this.stackTop--; return 1;case 88: this.stackTop -= 2; return 1;case 89: i = this.stackTop; this.stackTypes[i] = this.stackTypes[i - 1]; this.stackTop = i + 1; return 1;case 90: case 91: len = op - 90 + 2; doDUP_XX(1, len); j = this.stackTop; this.stackTypes[j - len] = this.stackTypes[j]; this.stackTop = j + 1; return 1;case 92: doDUP_XX(2, 2); this.stackTop += 2; return 1;case 93: case 94: len = op - 93 + 3; doDUP_XX(2, len); j = this.stackTop; this.stackTypes[j - len] = this.stackTypes[j]; this.stackTypes[j - len + 1] = this.stackTypes[j + 1]; this.stackTop = j + 2; return 1;case 95: sp = this.stackTop - 1; t = this.stackTypes[sp]; this.stackTypes[sp] = this.stackTypes[sp - 1]; this.stackTypes[sp - 1] = t; return 1;
/*     */     } 
/*     */     throw new RuntimeException("fatal"); }
/*     */    private int doXSTORE(int pos, byte[] code, TypeData type) {
/* 408 */     int index = code[pos + 1] & 0xFF;
/* 409 */     return doXSTORE(index, type);
/*     */   }
/*     */   
/*     */   private int doXSTORE(int index, TypeData type) {
/* 413 */     this.stackTop--;
/* 414 */     this.localsTypes[index] = type;
/* 415 */     if (type.is2WordType()) {
/* 416 */       this.stackTop--;
/* 417 */       this.localsTypes[index + 1] = TOP;
/*     */     } 
/*     */     
/* 420 */     return 2;
/*     */   }
/*     */   
/*     */   private int doASTORE(int index) {
/* 424 */     this.stackTop--;
/*     */     
/* 426 */     this.localsTypes[index] = this.stackTypes[this.stackTop];
/* 427 */     return 2;
/*     */   }
/*     */   
/*     */   private void doDUP_XX(int delta, int len) {
/* 431 */     TypeData[] types = this.stackTypes;
/* 432 */     int sp = this.stackTop - 1;
/* 433 */     int end = sp - len;
/* 434 */     while (sp > end) {
/* 435 */       types[sp + delta] = types[sp];
/* 436 */       sp--;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int doOpcode96_147(int pos, byte[] code, int op) {
/* 441 */     if (op <= 131) {
/* 442 */       this.stackTop += Opcode.STACK_GROW[op];
/* 443 */       return 1;
/*     */     } 
/*     */     
/* 446 */     switch (op) {
/*     */       
/*     */       case 132:
/* 449 */         return 3;
/*     */       case 133:
/* 451 */         this.stackTypes[this.stackTop - 1] = LONG;
/* 452 */         this.stackTypes[this.stackTop] = TOP;
/* 453 */         this.stackTop++;
/*     */       
/*     */       case 134:
/* 456 */         this.stackTypes[this.stackTop - 1] = FLOAT;
/*     */       
/*     */       case 135:
/* 459 */         this.stackTypes[this.stackTop - 1] = DOUBLE;
/* 460 */         this.stackTypes[this.stackTop] = TOP;
/* 461 */         this.stackTop++;
/*     */       
/*     */       case 136:
/* 464 */         this.stackTypes[--this.stackTop - 1] = INTEGER;
/*     */       
/*     */       case 137:
/* 467 */         this.stackTypes[--this.stackTop - 1] = FLOAT;
/*     */       
/*     */       case 138:
/* 470 */         this.stackTypes[this.stackTop - 2] = DOUBLE;
/*     */       
/*     */       case 139:
/* 473 */         this.stackTypes[this.stackTop - 1] = INTEGER;
/*     */       
/*     */       case 140:
/* 476 */         this.stackTypes[this.stackTop - 1] = LONG;
/* 477 */         this.stackTypes[this.stackTop] = TOP;
/* 478 */         this.stackTop++;
/*     */       
/*     */       case 141:
/* 481 */         this.stackTypes[this.stackTop - 1] = DOUBLE;
/* 482 */         this.stackTypes[this.stackTop] = TOP;
/* 483 */         this.stackTop++;
/*     */       
/*     */       case 142:
/* 486 */         this.stackTypes[--this.stackTop - 1] = INTEGER;
/*     */       
/*     */       case 143:
/* 489 */         this.stackTypes[this.stackTop - 2] = LONG;
/*     */       
/*     */       case 144:
/* 492 */         this.stackTypes[--this.stackTop - 1] = FLOAT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 145:
/*     */       case 146:
/*     */       case 147:
/* 502 */         return 1;
/*     */     }  throw new RuntimeException("fatal"); } private int doOpcode148_201(int pos, byte[] code, int op) throws BadBytecode { int pos2; int i; int low; int n; String type;
/*     */     int high;
/*     */     int j;
/* 506 */     switch (op) {
/*     */       case 148:
/* 508 */         this.stackTypes[this.stackTop - 4] = INTEGER;
/* 509 */         this.stackTop -= 3;
/*     */         break;
/*     */       case 149:
/*     */       case 150:
/* 513 */         this.stackTypes[--this.stackTop - 1] = INTEGER;
/*     */         break;
/*     */       case 151:
/*     */       case 152:
/* 517 */         this.stackTypes[this.stackTop - 4] = INTEGER;
/* 518 */         this.stackTop -= 3;
/*     */         break;
/*     */       case 153:
/*     */       case 154:
/*     */       case 155:
/*     */       case 156:
/*     */       case 157:
/*     */       case 158:
/* 526 */         this.stackTop--;
/* 527 */         visitBranch(pos, code, ByteArray.readS16bit(code, pos + 1));
/* 528 */         return 3;
/*     */       case 159:
/*     */       case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 164:
/*     */       case 165:
/*     */       case 166:
/* 537 */         this.stackTop -= 2;
/* 538 */         visitBranch(pos, code, ByteArray.readS16bit(code, pos + 1));
/* 539 */         return 3;
/*     */       case 167:
/* 541 */         visitGoto(pos, code, ByteArray.readS16bit(code, pos + 1));
/* 542 */         return 3;
/*     */       case 168:
/* 544 */         visitJSR(pos, code);
/* 545 */         return 3;
/*     */       case 169:
/* 547 */         visitRET(pos, code);
/* 548 */         return 2;
/*     */       case 170:
/* 550 */         this.stackTop--;
/* 551 */         pos2 = (pos & 0xFFFFFFFC) + 8;
/* 552 */         low = ByteArray.read32bit(code, pos2);
/* 553 */         high = ByteArray.read32bit(code, pos2 + 4);
/* 554 */         j = high - low + 1;
/* 555 */         visitTableSwitch(pos, code, j, pos2 + 8, ByteArray.read32bit(code, pos2 - 4));
/* 556 */         return j * 4 + 16 - (pos & 0x3);
/*     */       case 171:
/* 558 */         this.stackTop--;
/* 559 */         pos2 = (pos & 0xFFFFFFFC) + 8;
/* 560 */         n = ByteArray.read32bit(code, pos2);
/* 561 */         visitLookupSwitch(pos, code, n, pos2 + 4, ByteArray.read32bit(code, pos2 - 4));
/* 562 */         return n * 8 + 12 - (pos & 0x3);
/*     */       case 172:
/* 564 */         this.stackTop--;
/* 565 */         visitReturn(pos, code);
/*     */         break;
/*     */       case 173:
/* 568 */         this.stackTop -= 2;
/* 569 */         visitReturn(pos, code);
/*     */         break;
/*     */       case 174:
/* 572 */         this.stackTop--;
/* 573 */         visitReturn(pos, code);
/*     */         break;
/*     */       case 175:
/* 576 */         this.stackTop -= 2;
/* 577 */         visitReturn(pos, code);
/*     */         break;
/*     */       case 176:
/* 580 */         this.stackTypes[--this.stackTop].setType(this.returnType, this.classPool);
/* 581 */         visitReturn(pos, code);
/*     */         break;
/*     */       case 177:
/* 584 */         visitReturn(pos, code);
/*     */         break;
/*     */       case 178:
/* 587 */         return doGetField(pos, code, false);
/*     */       case 179:
/* 589 */         return doPutField(pos, code, false);
/*     */       case 180:
/* 591 */         return doGetField(pos, code, true);
/*     */       case 181:
/* 593 */         return doPutField(pos, code, true);
/*     */       case 182:
/*     */       case 183:
/* 596 */         return doInvokeMethod(pos, code, true);
/*     */       case 184:
/* 598 */         return doInvokeMethod(pos, code, false);
/*     */       case 185:
/* 600 */         return doInvokeIntfMethod(pos, code);
/*     */       case 186:
/* 602 */         return doInvokeDynamic(pos, code);
/*     */       case 187:
/* 604 */         i = ByteArray.readU16bit(code, pos + 1);
/* 605 */         this.stackTypes[this.stackTop++] = new TypeData.UninitData(pos, this.cpool
/* 606 */             .getClassInfo(i));
/* 607 */         return 3;
/*     */       case 188:
/* 609 */         return doNEWARRAY(pos, code);
/*     */       case 189:
/* 611 */         i = ByteArray.readU16bit(code, pos + 1);
/* 612 */         type = this.cpool.getClassInfo(i).replace('.', '/');
/* 613 */         if (type.charAt(0) == '[') {
/* 614 */           type = "[" + type;
/*     */         } else {
/* 616 */           type = "[L" + type + ";";
/*     */         } 
/* 618 */         this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(type);
/*     */         
/* 620 */         return 3;
/*     */       case 190:
/* 622 */         this.stackTypes[this.stackTop - 1].setType("[Ljava.lang.Object;", this.classPool);
/* 623 */         this.stackTypes[this.stackTop - 1] = INTEGER;
/*     */         break;
/*     */       case 191:
/* 626 */         this.stackTypes[--this.stackTop].setType("java.lang.Throwable", this.classPool);
/* 627 */         visitThrow(pos, code);
/*     */         break;
/*     */       
/*     */       case 192:
/* 631 */         i = ByteArray.readU16bit(code, pos + 1);
/* 632 */         type = this.cpool.getClassInfo(i);
/* 633 */         if (type.charAt(0) == '[') {
/* 634 */           type = type.replace('.', '/');
/*     */         }
/* 636 */         this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(type);
/* 637 */         return 3;
/*     */       
/*     */       case 193:
/* 640 */         this.stackTypes[this.stackTop - 1] = INTEGER;
/* 641 */         return 3;
/*     */       case 194:
/*     */       case 195:
/* 644 */         this.stackTop--;
/*     */         break;
/*     */       
/*     */       case 196:
/* 648 */         return doWIDE(pos, code);
/*     */       case 197:
/* 650 */         return doMultiANewArray(pos, code);
/*     */       case 198:
/*     */       case 199:
/* 653 */         this.stackTop--;
/* 654 */         visitBranch(pos, code, ByteArray.readS16bit(code, pos + 1));
/* 655 */         return 3;
/*     */       case 200:
/* 657 */         visitGoto(pos, code, ByteArray.read32bit(code, pos + 1));
/* 658 */         return 5;
/*     */       case 201:
/* 660 */         visitJSR(pos, code);
/* 661 */         return 5;
/*     */     } 
/* 663 */     return 1; }
/*     */ 
/*     */   
/*     */   private int doWIDE(int pos, byte[] code) throws BadBytecode {
/* 667 */     int index, op = code[pos + 1] & 0xFF;
/* 668 */     switch (op) {
/*     */       case 21:
/* 670 */         doWIDE_XLOAD(pos, code, INTEGER);
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
/* 711 */         return 4;case 22: doWIDE_XLOAD(pos, code, LONG); return 4;case 23: doWIDE_XLOAD(pos, code, FLOAT); return 4;case 24: doWIDE_XLOAD(pos, code, DOUBLE); return 4;case 25: index = ByteArray.readU16bit(code, pos + 2); doALOAD(index); return 4;case 54: doWIDE_STORE(pos, code, INTEGER); return 4;case 55: doWIDE_STORE(pos, code, LONG); return 4;case 56: doWIDE_STORE(pos, code, FLOAT); return 4;case 57: doWIDE_STORE(pos, code, DOUBLE); return 4;case 58: index = ByteArray.readU16bit(code, pos + 2); doASTORE(index); return 4;case 132: return 6;case 169: visitRET(pos, code); return 4;
/*     */     } 
/*     */     throw new RuntimeException("bad WIDE instruction: " + op);
/*     */   } private void doWIDE_XLOAD(int pos, byte[] code, TypeData type) {
/* 715 */     int index = ByteArray.readU16bit(code, pos + 2);
/* 716 */     doXLOAD(index, type);
/*     */   }
/*     */   
/*     */   private void doWIDE_STORE(int pos, byte[] code, TypeData type) {
/* 720 */     int index = ByteArray.readU16bit(code, pos + 2);
/* 721 */     doXSTORE(index, type);
/*     */   }
/*     */   
/*     */   private int doPutField(int pos, byte[] code, boolean notStatic) throws BadBytecode {
/* 725 */     int index = ByteArray.readU16bit(code, pos + 1);
/* 726 */     String desc = this.cpool.getFieldrefType(index);
/* 727 */     this.stackTop -= Descriptor.dataSize(desc);
/* 728 */     char c = desc.charAt(0);
/* 729 */     if (c == 'L') {
/* 730 */       this.stackTypes[this.stackTop].setType(getFieldClassName(desc, 0), this.classPool);
/* 731 */     } else if (c == '[') {
/* 732 */       this.stackTypes[this.stackTop].setType(desc, this.classPool);
/*     */     } 
/* 734 */     setFieldTarget(notStatic, index);
/* 735 */     return 3;
/*     */   }
/*     */   
/*     */   private int doGetField(int pos, byte[] code, boolean notStatic) throws BadBytecode {
/* 739 */     int index = ByteArray.readU16bit(code, pos + 1);
/* 740 */     setFieldTarget(notStatic, index);
/* 741 */     String desc = this.cpool.getFieldrefType(index);
/* 742 */     pushMemberType(desc);
/* 743 */     return 3;
/*     */   }
/*     */   
/*     */   private void setFieldTarget(boolean notStatic, int index) throws BadBytecode {
/* 747 */     if (notStatic) {
/* 748 */       String className = this.cpool.getFieldrefClassName(index);
/* 749 */       this.stackTypes[--this.stackTop].setType(className, this.classPool);
/*     */     } 
/*     */   }
/*     */   private int doNEWARRAY(int pos, byte[] code) {
/*     */     String type;
/* 754 */     int s = this.stackTop - 1;
/*     */     
/* 756 */     switch (code[pos + 1] & 0xFF) {
/*     */       case 4:
/* 758 */         type = "[Z";
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
/* 785 */         this.stackTypes[s] = new TypeData.ClassName(type);
/* 786 */         return 2;case 5: type = "[C"; this.stackTypes[s] = new TypeData.ClassName(type); return 2;case 6: type = "[F"; this.stackTypes[s] = new TypeData.ClassName(type); return 2;case 7: type = "[D"; this.stackTypes[s] = new TypeData.ClassName(type); return 2;case 8: type = "[B"; this.stackTypes[s] = new TypeData.ClassName(type); return 2;case 9: type = "[S"; this.stackTypes[s] = new TypeData.ClassName(type); return 2;case 10: type = "[I"; this.stackTypes[s] = new TypeData.ClassName(type); return 2;case 11: type = "[J"; this.stackTypes[s] = new TypeData.ClassName(type); return 2;
/*     */     } 
/*     */     throw new RuntimeException("bad newarray");
/*     */   } private int doMultiANewArray(int pos, byte[] code) {
/* 790 */     int i = ByteArray.readU16bit(code, pos + 1);
/* 791 */     int dim = code[pos + 3] & 0xFF;
/* 792 */     this.stackTop -= dim - 1;
/*     */     
/* 794 */     String type = this.cpool.getClassInfo(i).replace('.', '/');
/* 795 */     this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(type);
/* 796 */     return 4;
/*     */   }
/*     */   
/*     */   private int doInvokeMethod(int pos, byte[] code, boolean notStatic) throws BadBytecode {
/* 800 */     int i = ByteArray.readU16bit(code, pos + 1);
/* 801 */     String desc = this.cpool.getMethodrefType(i);
/* 802 */     checkParamTypes(desc, 1);
/* 803 */     if (notStatic) {
/* 804 */       String className = this.cpool.getMethodrefClassName(i);
/* 805 */       TypeData target = this.stackTypes[--this.stackTop];
/* 806 */       if (target instanceof TypeData.UninitTypeVar && target.isUninit()) {
/* 807 */         constructorCalled(target, ((TypeData.UninitTypeVar)target).offset());
/* 808 */       } else if (target instanceof TypeData.UninitData) {
/* 809 */         constructorCalled(target, ((TypeData.UninitData)target).offset());
/*     */       } 
/* 811 */       target.setType(className, this.classPool);
/*     */     } 
/*     */     
/* 814 */     pushMemberType(desc);
/* 815 */     return 3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void constructorCalled(TypeData target, int offset) {
/* 824 */     target.constructorCalled(offset); int i;
/* 825 */     for (i = 0; i < this.stackTop; i++) {
/* 826 */       this.stackTypes[i].constructorCalled(offset);
/*     */     }
/* 828 */     for (i = 0; i < this.localsTypes.length; i++)
/* 829 */       this.localsTypes[i].constructorCalled(offset); 
/*     */   }
/*     */   
/*     */   private int doInvokeIntfMethod(int pos, byte[] code) throws BadBytecode {
/* 833 */     int i = ByteArray.readU16bit(code, pos + 1);
/* 834 */     String desc = this.cpool.getInterfaceMethodrefType(i);
/* 835 */     checkParamTypes(desc, 1);
/* 836 */     String className = this.cpool.getInterfaceMethodrefClassName(i);
/* 837 */     this.stackTypes[--this.stackTop].setType(className, this.classPool);
/* 838 */     pushMemberType(desc);
/* 839 */     return 5;
/*     */   }
/*     */   
/*     */   private int doInvokeDynamic(int pos, byte[] code) throws BadBytecode {
/* 843 */     int i = ByteArray.readU16bit(code, pos + 1);
/* 844 */     String desc = this.cpool.getInvokeDynamicType(i);
/* 845 */     checkParamTypes(desc, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 853 */     pushMemberType(desc);
/* 854 */     return 5;
/*     */   }
/*     */   
/*     */   private void pushMemberType(String descriptor) {
/* 858 */     int top = 0;
/* 859 */     if (descriptor.charAt(0) == '(') {
/* 860 */       top = descriptor.indexOf(')') + 1;
/* 861 */       if (top < 1) {
/* 862 */         throw new IndexOutOfBoundsException("bad descriptor: " + descriptor);
/*     */       }
/*     */     } 
/*     */     
/* 866 */     TypeData[] types = this.stackTypes;
/* 867 */     int index = this.stackTop;
/* 868 */     switch (descriptor.charAt(top)) {
/*     */       case '[':
/* 870 */         types[index] = new TypeData.ClassName(descriptor.substring(top));
/*     */         break;
/*     */       case 'L':
/* 873 */         types[index] = new TypeData.ClassName(getFieldClassName(descriptor, top));
/*     */         break;
/*     */       case 'J':
/* 876 */         types[index] = LONG;
/* 877 */         types[index + 1] = TOP;
/* 878 */         this.stackTop += 2;
/*     */         return;
/*     */       case 'F':
/* 881 */         types[index] = FLOAT;
/*     */         break;
/*     */       case 'D':
/* 884 */         types[index] = DOUBLE;
/* 885 */         types[index + 1] = TOP;
/* 886 */         this.stackTop += 2;
/*     */         return;
/*     */       case 'V':
/*     */         return;
/*     */       default:
/* 891 */         types[index] = INTEGER;
/*     */         break;
/*     */     } 
/*     */     
/* 895 */     this.stackTop++;
/*     */   }
/*     */   
/*     */   private static String getFieldClassName(String desc, int index) {
/* 899 */     return desc.substring(index + 1, desc.length() - 1).replace('/', '.');
/*     */   }
/*     */   
/*     */   private void checkParamTypes(String desc, int i) throws BadBytecode {
/* 903 */     char c = desc.charAt(i);
/* 904 */     if (c == ')') {
/*     */       return;
/*     */     }
/* 907 */     int k = i;
/* 908 */     boolean array = false;
/* 909 */     while (c == '[') {
/* 910 */       array = true;
/* 911 */       c = desc.charAt(++k);
/*     */     } 
/*     */     
/* 914 */     if (c == 'L') {
/* 915 */       k = desc.indexOf(';', k) + 1;
/* 916 */       if (k <= 0) {
/* 917 */         throw new IndexOutOfBoundsException("bad descriptor");
/*     */       }
/*     */     } else {
/* 920 */       k++;
/*     */     } 
/* 922 */     checkParamTypes(desc, k);
/* 923 */     if (!array && (c == 'J' || c == 'D')) {
/* 924 */       this.stackTop -= 2;
/*     */     } else {
/* 926 */       this.stackTop--;
/*     */     } 
/* 928 */     if (array) {
/* 929 */       this.stackTypes[this.stackTop].setType(desc.substring(i, k), this.classPool);
/* 930 */     } else if (c == 'L') {
/* 931 */       this.stackTypes[this.stackTop].setType(desc.substring(i + 1, k - 1).replace('/', '.'), this.classPool);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\stackmap\Tracer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */