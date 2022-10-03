/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Map;
/*     */ import javassist.CannotCompileException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StackMap
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String tag = "StackMap";
/*     */   public static final int TOP = 0;
/*     */   public static final int INTEGER = 1;
/*     */   public static final int FLOAT = 2;
/*     */   public static final int DOUBLE = 3;
/*     */   public static final int LONG = 4;
/*     */   public static final int NULL = 5;
/*     */   public static final int THIS = 6;
/*     */   public static final int OBJECT = 7;
/*     */   public static final int UNINIT = 8;
/*     */   
/*     */   StackMap(ConstPool cp, byte[] newInfo) {
/*  52 */     super(cp, "StackMap", newInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   StackMap(ConstPool cp, int name_id, DataInputStream in) throws IOException {
/*  58 */     super(cp, name_id, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int numOfEntries() {
/*  65 */     return ByteArray.readU16bit(this.info, 0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
/* 118 */     Copier copier = new Copier(this, newCp, classnames);
/* 119 */     copier.visit();
/* 120 */     return copier.getStackMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Walker
/*     */   {
/*     */     byte[] info;
/*     */ 
/*     */ 
/*     */     
/*     */     public Walker(StackMap sm) {
/* 133 */       this.info = sm.get();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void visit() {
/* 140 */       int num = ByteArray.readU16bit(this.info, 0);
/* 141 */       int pos = 2;
/* 142 */       for (int i = 0; i < num; i++) {
/* 143 */         int offset = ByteArray.readU16bit(this.info, pos);
/* 144 */         int numLoc = ByteArray.readU16bit(this.info, pos + 2);
/* 145 */         pos = locals(pos + 4, offset, numLoc);
/* 146 */         int numStack = ByteArray.readU16bit(this.info, pos);
/* 147 */         pos = stack(pos + 2, offset, numStack);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int locals(int pos, int offset, int num) {
/* 156 */       return typeInfoArray(pos, offset, num, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int stack(int pos, int offset, int num) {
/* 164 */       return typeInfoArray(pos, offset, num, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int typeInfoArray(int pos, int offset, int num, boolean isLocals) {
/* 176 */       for (int k = 0; k < num; k++) {
/* 177 */         pos = typeInfoArray2(k, pos);
/*     */       }
/* 179 */       return pos;
/*     */     }
/*     */     
/*     */     int typeInfoArray2(int k, int pos) {
/* 183 */       byte tag = this.info[pos];
/* 184 */       if (tag == 7) {
/* 185 */         int clazz = ByteArray.readU16bit(this.info, pos + 1);
/* 186 */         objectVariable(pos, clazz);
/* 187 */         pos += 3;
/*     */       }
/* 189 */       else if (tag == 8) {
/* 190 */         int offsetOfNew = ByteArray.readU16bit(this.info, pos + 1);
/* 191 */         uninitialized(pos, offsetOfNew);
/* 192 */         pos += 3;
/*     */       } else {
/*     */         
/* 195 */         typeInfo(pos, tag);
/* 196 */         pos++;
/*     */       } 
/*     */       
/* 199 */       return pos;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void typeInfo(int pos, byte tag) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void objectVariable(int pos, int clazz) {}
/*     */ 
/*     */     
/*     */     public void uninitialized(int pos, int offset) {}
/*     */   }
/*     */ 
/*     */   
/*     */   static class Copier
/*     */     extends Walker
/*     */   {
/*     */     byte[] dest;
/*     */     
/*     */     ConstPool srcCp;
/*     */     
/*     */     ConstPool destCp;
/*     */     
/*     */     Map<String, String> classnames;
/*     */ 
/*     */     
/*     */     Copier(StackMap map, ConstPool newCp, Map<String, String> classnames) {
/* 228 */       super(map);
/* 229 */       this.srcCp = map.getConstPool();
/* 230 */       this.dest = new byte[this.info.length];
/* 231 */       this.destCp = newCp;
/* 232 */       this.classnames = classnames;
/*     */     }
/*     */     
/*     */     public void visit() {
/* 236 */       int num = ByteArray.readU16bit(this.info, 0);
/* 237 */       ByteArray.write16bit(num, this.dest, 0);
/* 238 */       super.visit();
/*     */     }
/*     */ 
/*     */     
/*     */     public int locals(int pos, int offset, int num) {
/* 243 */       ByteArray.write16bit(offset, this.dest, pos - 4);
/* 244 */       return super.locals(pos, offset, num);
/*     */     }
/*     */ 
/*     */     
/*     */     public int typeInfoArray(int pos, int offset, int num, boolean isLocals) {
/* 249 */       ByteArray.write16bit(num, this.dest, pos - 2);
/* 250 */       return super.typeInfoArray(pos, offset, num, isLocals);
/*     */     }
/*     */ 
/*     */     
/*     */     public void typeInfo(int pos, byte tag) {
/* 255 */       this.dest[pos] = tag;
/*     */     }
/*     */ 
/*     */     
/*     */     public void objectVariable(int pos, int clazz) {
/* 260 */       this.dest[pos] = 7;
/* 261 */       int newClazz = this.srcCp.copy(clazz, this.destCp, this.classnames);
/* 262 */       ByteArray.write16bit(newClazz, this.dest, pos + 1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void uninitialized(int pos, int offset) {
/* 267 */       this.dest[pos] = 8;
/* 268 */       ByteArray.write16bit(offset, this.dest, pos + 1);
/*     */     }
/*     */     
/*     */     public StackMap getStackMap() {
/* 272 */       return new StackMap(this.destCp, this.dest);
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertLocal(int index, int tag, int classInfo) throws BadBytecode {
/* 294 */     byte[] data = (new InsertLocal(this, index, tag, classInfo)).doit();
/* 295 */     set(data);
/*     */   }
/*     */   
/*     */   static class SimpleCopy extends Walker {
/*     */     StackMap.Writer writer;
/*     */     
/*     */     SimpleCopy(StackMap map) {
/* 302 */       super(map);
/* 303 */       this.writer = new StackMap.Writer();
/*     */     }
/*     */     
/*     */     byte[] doit() {
/* 307 */       visit();
/* 308 */       return this.writer.toByteArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public void visit() {
/* 313 */       int num = ByteArray.readU16bit(this.info, 0);
/* 314 */       this.writer.write16bit(num);
/* 315 */       super.visit();
/*     */     }
/*     */ 
/*     */     
/*     */     public int locals(int pos, int offset, int num) {
/* 320 */       this.writer.write16bit(offset);
/* 321 */       return super.locals(pos, offset, num);
/*     */     }
/*     */ 
/*     */     
/*     */     public int typeInfoArray(int pos, int offset, int num, boolean isLocals) {
/* 326 */       this.writer.write16bit(num);
/* 327 */       return super.typeInfoArray(pos, offset, num, isLocals);
/*     */     }
/*     */ 
/*     */     
/*     */     public void typeInfo(int pos, byte tag) {
/* 332 */       this.writer.writeVerifyTypeInfo(tag, 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void objectVariable(int pos, int clazz) {
/* 337 */       this.writer.writeVerifyTypeInfo(7, clazz);
/*     */     }
/*     */ 
/*     */     
/*     */     public void uninitialized(int pos, int offset) {
/* 342 */       this.writer.writeVerifyTypeInfo(8, offset);
/*     */     } }
/*     */   
/*     */   static class InsertLocal extends SimpleCopy {
/*     */     private int varIndex;
/*     */     private int varTag;
/*     */     private int varData;
/*     */     
/*     */     InsertLocal(StackMap map, int varIndex, int varTag, int varData) {
/* 351 */       super(map);
/* 352 */       this.varIndex = varIndex;
/* 353 */       this.varTag = varTag;
/* 354 */       this.varData = varData;
/*     */     }
/*     */ 
/*     */     
/*     */     public int typeInfoArray(int pos, int offset, int num, boolean isLocals) {
/* 359 */       if (!isLocals || num < this.varIndex) {
/* 360 */         return super.typeInfoArray(pos, offset, num, isLocals);
/*     */       }
/* 362 */       this.writer.write16bit(num + 1);
/* 363 */       for (int k = 0; k < num; k++) {
/* 364 */         if (k == this.varIndex) {
/* 365 */           writeVarTypeInfo();
/*     */         }
/* 367 */         pos = typeInfoArray2(k, pos);
/*     */       } 
/*     */       
/* 370 */       if (num == this.varIndex) {
/* 371 */         writeVarTypeInfo();
/*     */       }
/* 373 */       return pos;
/*     */     }
/*     */     
/*     */     private void writeVarTypeInfo() {
/* 377 */       if (this.varTag == 7) {
/* 378 */         this.writer.writeVerifyTypeInfo(7, this.varData);
/* 379 */       } else if (this.varTag == 8) {
/* 380 */         this.writer.writeVerifyTypeInfo(8, this.varData);
/*     */       } else {
/* 382 */         this.writer.writeVerifyTypeInfo(this.varTag, 0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void shiftPc(int where, int gapSize, boolean exclusive) throws BadBytecode {
/* 389 */     (new Shifter(this, where, gapSize, exclusive)).visit();
/*     */   }
/*     */   
/*     */   static class Shifter
/*     */     extends Walker {
/*     */     private int where;
/*     */     
/*     */     public Shifter(StackMap smt, int where, int gap, boolean exclusive) {
/* 397 */       super(smt);
/* 398 */       this.where = where;
/* 399 */       this.gap = gap;
/* 400 */       this.exclusive = exclusive;
/*     */     }
/*     */     private int gap; private boolean exclusive;
/*     */     
/*     */     public int locals(int pos, int offset, int num) {
/* 405 */       if (this.exclusive ? (this.where <= offset) : (this.where < offset)) {
/* 406 */         ByteArray.write16bit(offset + this.gap, this.info, pos - 4);
/*     */       }
/* 408 */       return super.locals(pos, offset, num);
/*     */     }
/*     */ 
/*     */     
/*     */     public void uninitialized(int pos, int offset) {
/* 413 */       if (this.where <= offset) {
/* 414 */         ByteArray.write16bit(offset + this.gap, this.info, pos + 1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void shiftForSwitch(int where, int gapSize) throws BadBytecode {
/* 422 */     (new SwitchShifter(this, where, gapSize)).visit();
/*     */   }
/*     */   
/*     */   static class SwitchShifter extends Walker { private int where;
/*     */     private int gap;
/*     */     
/*     */     public SwitchShifter(StackMap smt, int where, int gap) {
/* 429 */       super(smt);
/* 430 */       this.where = where;
/* 431 */       this.gap = gap;
/*     */     }
/*     */ 
/*     */     
/*     */     public int locals(int pos, int offset, int num) {
/* 436 */       if (this.where == pos + offset) {
/* 437 */         ByteArray.write16bit(offset - this.gap, this.info, pos - 4);
/* 438 */       } else if (this.where == pos) {
/* 439 */         ByteArray.write16bit(offset + this.gap, this.info, pos - 4);
/*     */       } 
/* 441 */       return super.locals(pos, offset, num);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeNew(int where) throws CannotCompileException {
/* 455 */     byte[] data = (new NewRemover(this, where)).doit();
/* 456 */     set(data);
/*     */   }
/*     */   
/*     */   static class NewRemover extends SimpleCopy {
/*     */     int posOfNew;
/*     */     
/*     */     NewRemover(StackMap map, int where) {
/* 463 */       super(map);
/* 464 */       this.posOfNew = where;
/*     */     }
/*     */ 
/*     */     
/*     */     public int stack(int pos, int offset, int num) {
/* 469 */       return stackTypeInfoArray(pos, offset, num);
/*     */     }
/*     */     
/*     */     private int stackTypeInfoArray(int pos, int offset, int num) {
/* 473 */       int p = pos;
/* 474 */       int count = 0; int k;
/* 475 */       for (k = 0; k < num; k++) {
/* 476 */         byte tag = this.info[p];
/* 477 */         if (tag == 7) {
/* 478 */           p += 3;
/* 479 */         } else if (tag == 8) {
/* 480 */           int offsetOfNew = ByteArray.readU16bit(this.info, p + 1);
/* 481 */           if (offsetOfNew == this.posOfNew) {
/* 482 */             count++;
/*     */           }
/* 484 */           p += 3;
/*     */         } else {
/*     */           
/* 487 */           p++;
/*     */         } 
/*     */       } 
/* 490 */       this.writer.write16bit(num - count);
/* 491 */       for (k = 0; k < num; k++) {
/* 492 */         byte tag = this.info[pos];
/* 493 */         if (tag == 7) {
/* 494 */           int clazz = ByteArray.readU16bit(this.info, pos + 1);
/* 495 */           objectVariable(pos, clazz);
/* 496 */           pos += 3;
/*     */         }
/* 498 */         else if (tag == 8) {
/* 499 */           int offsetOfNew = ByteArray.readU16bit(this.info, pos + 1);
/* 500 */           if (offsetOfNew != this.posOfNew) {
/* 501 */             uninitialized(pos, offsetOfNew);
/*     */           }
/* 503 */           pos += 3;
/*     */         } else {
/*     */           
/* 506 */           typeInfo(pos, tag);
/* 507 */           pos++;
/*     */         } 
/*     */       } 
/*     */       
/* 511 */       return pos;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintWriter out) {
/* 519 */     (new Printer(this, out)).print();
/*     */   }
/*     */   
/*     */   static class Printer extends Walker {
/*     */     private PrintWriter writer;
/*     */     
/*     */     public Printer(StackMap map, PrintWriter out) {
/* 526 */       super(map);
/* 527 */       this.writer = out;
/*     */     }
/*     */     
/*     */     public void print() {
/* 531 */       int num = ByteArray.readU16bit(this.info, 0);
/* 532 */       this.writer.println(num + " entries");
/* 533 */       visit();
/*     */     }
/*     */ 
/*     */     
/*     */     public int locals(int pos, int offset, int num) {
/* 538 */       this.writer.println("  * offset " + offset);
/* 539 */       return super.locals(pos, offset, num);
/*     */     }
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
/*     */   public static class Writer
/*     */   {
/* 555 */     private ByteArrayOutputStream output = new ByteArrayOutputStream();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte[] toByteArray() {
/* 562 */       return this.output.toByteArray();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StackMap toStackMap(ConstPool cp) {
/* 569 */       return new StackMap(cp, this.output.toByteArray());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void writeVerifyTypeInfo(int tag, int data) {
/* 578 */       this.output.write(tag);
/* 579 */       if (tag == 7 || tag == 8) {
/* 580 */         write16bit(data);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void write16bit(int value) {
/* 587 */       this.output.write(value >>> 8 & 0xFF);
/* 588 */       this.output.write(value & 0xFF);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\StackMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */