/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CodeAttribute
/*     */   extends AttributeInfo
/*     */   implements Opcode
/*     */ {
/*     */   public static final String tag = "Code";
/*     */   private int maxStack;
/*     */   private int maxLocals;
/*     */   private ExceptionTable exceptions;
/*     */   private List<AttributeInfo> attributes;
/*     */   
/*     */   public CodeAttribute(ConstPool cp, int stack, int locals, byte[] code, ExceptionTable etable) {
/*  61 */     super(cp, "Code");
/*  62 */     this.maxStack = stack;
/*  63 */     this.maxLocals = locals;
/*  64 */     this.info = code;
/*  65 */     this.exceptions = etable;
/*  66 */     this.attributes = new ArrayList<>();
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
/*     */   private CodeAttribute(ConstPool cp, CodeAttribute src, Map<String, String> classnames) throws BadBytecode {
/*  81 */     super(cp, "Code");
/*     */     
/*  83 */     this.maxStack = src.getMaxStack();
/*  84 */     this.maxLocals = src.getMaxLocals();
/*  85 */     this.exceptions = src.getExceptionTable().copy(cp, classnames);
/*  86 */     this.attributes = new ArrayList<>();
/*  87 */     List<AttributeInfo> src_attr = src.getAttributes();
/*  88 */     int num = src_attr.size();
/*  89 */     for (int i = 0; i < num; i++) {
/*  90 */       AttributeInfo ai = src_attr.get(i);
/*  91 */       this.attributes.add(ai.copy(cp, classnames));
/*     */     } 
/*     */     
/*  94 */     this.info = src.copyCode(cp, classnames, this.exceptions, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   CodeAttribute(ConstPool cp, int name_id, DataInputStream in) throws IOException {
/* 100 */     super(cp, name_id, (byte[])null);
/*     */     
/* 102 */     int attr_len = in.readInt();
/*     */     
/* 104 */     this.maxStack = in.readUnsignedShort();
/* 105 */     this.maxLocals = in.readUnsignedShort();
/*     */     
/* 107 */     int code_len = in.readInt();
/* 108 */     this.info = new byte[code_len];
/* 109 */     in.readFully(this.info);
/*     */     
/* 111 */     this.exceptions = new ExceptionTable(cp, in);
/*     */     
/* 113 */     this.attributes = new ArrayList<>();
/* 114 */     int num = in.readUnsignedShort();
/* 115 */     for (int i = 0; i < num; i++) {
/* 116 */       this.attributes.add(AttributeInfo.read(cp, in));
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
/*     */   public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) throws RuntimeCopyException {
/*     */     try {
/* 138 */       return new CodeAttribute(newCp, this, classnames);
/*     */     }
/* 140 */     catch (BadBytecode e) {
/* 141 */       throw new RuntimeCopyException("bad bytecode. fatal?");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class RuntimeCopyException
/*     */     extends RuntimeException
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RuntimeCopyException(String s) {
/* 157 */       super(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 168 */     return 18 + this.info.length + this.exceptions.size() * 8 + 
/* 169 */       AttributeInfo.getLength(this.attributes);
/*     */   }
/*     */ 
/*     */   
/*     */   void write(DataOutputStream out) throws IOException {
/* 174 */     out.writeShort(this.name);
/* 175 */     out.writeInt(length() - 6);
/* 176 */     out.writeShort(this.maxStack);
/* 177 */     out.writeShort(this.maxLocals);
/* 178 */     out.writeInt(this.info.length);
/* 179 */     out.write(this.info);
/* 180 */     this.exceptions.write(out);
/* 181 */     out.writeShort(this.attributes.size());
/* 182 */     AttributeInfo.writeAll(this.attributes, out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] get() {
/* 192 */     throw new UnsupportedOperationException("CodeAttribute.get()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(byte[] newinfo) {
/* 202 */     throw new UnsupportedOperationException("CodeAttribute.set()");
/*     */   }
/*     */ 
/*     */   
/*     */   void renameClass(String oldname, String newname) {
/* 207 */     AttributeInfo.renameClass(this.attributes, oldname, newname);
/*     */   }
/*     */ 
/*     */   
/*     */   void renameClass(Map<String, String> classnames) {
/* 212 */     AttributeInfo.renameClass(this.attributes, classnames);
/*     */   }
/*     */ 
/*     */   
/*     */   void getRefClasses(Map<String, String> classnames) {
/* 217 */     AttributeInfo.getRefClasses(this.attributes, classnames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDeclaringClass() {
/* 225 */     ConstPool cp = getConstPool();
/* 226 */     return cp.getClassName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxStack() {
/* 233 */     return this.maxStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxStack(int value) {
/* 240 */     this.maxStack = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int computeMaxStack() throws BadBytecode {
/* 251 */     this.maxStack = (new CodeAnalyzer(this)).computeMaxStack();
/* 252 */     return this.maxStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxLocals() {
/* 259 */     return this.maxLocals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxLocals(int value) {
/* 266 */     this.maxLocals = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCodeLength() {
/* 273 */     return this.info.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getCode() {
/* 280 */     return this.info;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void setCode(byte[] newinfo) {
/* 286 */     super.set(newinfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CodeIterator iterator() {
/* 292 */     return new CodeIterator(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ExceptionTable getExceptionTable() {
/* 298 */     return this.exceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<AttributeInfo> getAttributes() {
/* 308 */     return this.attributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo getAttribute(String name) {
/* 318 */     return AttributeInfo.lookup(this.attributes, name);
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
/*     */   public void setAttribute(StackMapTable smt) {
/* 330 */     AttributeInfo.remove(this.attributes, "StackMapTable");
/* 331 */     if (smt != null) {
/* 332 */       this.attributes.add(smt);
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
/*     */   public void setAttribute(StackMap sm) {
/* 345 */     AttributeInfo.remove(this.attributes, "StackMap");
/* 346 */     if (sm != null) {
/* 347 */       this.attributes.add(sm);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] copyCode(ConstPool destCp, Map<String, String> classnames, ExceptionTable etable, CodeAttribute destCa) throws BadBytecode {
/* 357 */     int len = getCodeLength();
/* 358 */     byte[] newCode = new byte[len];
/* 359 */     destCa.info = newCode;
/* 360 */     LdcEntry ldc = copyCode(this.info, 0, len, getConstPool(), newCode, destCp, classnames);
/*     */     
/* 362 */     return LdcEntry.doit(newCode, ldc, etable, destCa);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static LdcEntry copyCode(byte[] code, int beginPos, int endPos, ConstPool srcCp, byte[] newcode, ConstPool destCp, Map<String, String> classnameMap) throws BadBytecode {
/* 371 */     LdcEntry ldcEntry = null;
/*     */     int i;
/* 373 */     for (i = beginPos; i < endPos; i = i2) {
/* 374 */       int index; LdcEntry ldc; int i2 = CodeIterator.nextOpcode(code, i);
/* 375 */       byte c = code[i];
/* 376 */       newcode[i] = c;
/* 377 */       switch (c & 0xFF) {
/*     */         case 19:
/*     */         case 20:
/*     */         case 178:
/*     */         case 179:
/*     */         case 180:
/*     */         case 181:
/*     */         case 182:
/*     */         case 183:
/*     */         case 184:
/*     */         case 187:
/*     */         case 189:
/*     */         case 192:
/*     */         case 193:
/* 391 */           copyConstPoolInfo(i + 1, code, srcCp, newcode, destCp, classnameMap);
/*     */           break;
/*     */         
/*     */         case 18:
/* 395 */           index = code[i + 1] & 0xFF;
/* 396 */           index = srcCp.copy(index, destCp, classnameMap);
/* 397 */           if (index < 256) {
/* 398 */             newcode[i + 1] = (byte)index; break;
/*     */           } 
/* 400 */           newcode[i] = 0;
/* 401 */           newcode[i + 1] = 0;
/* 402 */           ldc = new LdcEntry();
/* 403 */           ldc.where = i;
/* 404 */           ldc.index = index;
/* 405 */           ldc.next = ldcEntry;
/* 406 */           ldcEntry = ldc;
/*     */           break;
/*     */         
/*     */         case 185:
/* 410 */           copyConstPoolInfo(i + 1, code, srcCp, newcode, destCp, classnameMap);
/*     */           
/* 412 */           newcode[i + 3] = code[i + 3];
/* 413 */           newcode[i + 4] = code[i + 4];
/*     */           break;
/*     */         case 186:
/* 416 */           copyConstPoolInfo(i + 1, code, srcCp, newcode, destCp, classnameMap);
/*     */           
/* 418 */           newcode[i + 3] = 0;
/* 419 */           newcode[i + 4] = 0;
/*     */           break;
/*     */         case 197:
/* 422 */           copyConstPoolInfo(i + 1, code, srcCp, newcode, destCp, classnameMap);
/*     */           
/* 424 */           newcode[i + 3] = code[i + 3];
/*     */           break;
/*     */         default:
/* 427 */           while (++i < i2) {
/* 428 */             newcode[i] = code[i];
/*     */           }
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 434 */     return ldcEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void copyConstPoolInfo(int i, byte[] code, ConstPool srcCp, byte[] newcode, ConstPool destCp, Map<String, String> classnameMap) {
/* 440 */     int index = (code[i] & 0xFF) << 8 | code[i + 1] & 0xFF;
/* 441 */     index = srcCp.copy(index, destCp, classnameMap);
/* 442 */     newcode[i] = (byte)(index >> 8);
/* 443 */     newcode[i + 1] = (byte)index;
/*     */   }
/*     */ 
/*     */   
/*     */   static class LdcEntry
/*     */   {
/*     */     LdcEntry next;
/*     */     
/*     */     int where;
/*     */     int index;
/*     */     
/*     */     static byte[] doit(byte[] code, LdcEntry ldc, ExceptionTable etable, CodeAttribute ca) throws BadBytecode {
/* 455 */       if (ldc != null) {
/* 456 */         code = CodeIterator.changeLdcToLdcW(code, etable, ca, ldc);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 471 */       return code;
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
/*     */   public void insertLocalVar(int where, int size) throws BadBytecode {
/* 492 */     CodeIterator ci = iterator();
/* 493 */     while (ci.hasNext()) {
/* 494 */       shiftIndex(ci, where, size);
/*     */     }
/* 496 */     setMaxLocals(getMaxLocals() + size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void shiftIndex(CodeIterator ci, int lessThan, int delta) throws BadBytecode {
/* 507 */     int index = ci.next();
/* 508 */     int opcode = ci.byteAt(index);
/* 509 */     if (opcode < 21)
/*     */       return; 
/* 511 */     if (opcode < 79) {
/* 512 */       if (opcode < 26) {
/*     */         
/* 514 */         shiftIndex8(ci, index, opcode, lessThan, delta);
/*     */       }
/* 516 */       else if (opcode < 46) {
/*     */         
/* 518 */         shiftIndex0(ci, index, opcode, lessThan, delta, 26, 21);
/*     */       } else {
/* 520 */         if (opcode < 54)
/*     */           return; 
/* 522 */         if (opcode < 59) {
/*     */           
/* 524 */           shiftIndex8(ci, index, opcode, lessThan, delta);
/*     */         }
/*     */         else {
/*     */           
/* 528 */           shiftIndex0(ci, index, opcode, lessThan, delta, 59, 54);
/*     */         } 
/*     */       } 
/* 531 */     } else if (opcode == 132) {
/* 532 */       int var = ci.byteAt(index + 1);
/* 533 */       if (var < lessThan) {
/*     */         return;
/*     */       }
/* 536 */       var += delta;
/* 537 */       if (var < 256) {
/* 538 */         ci.writeByte(var, index + 1);
/*     */       } else {
/* 540 */         int plus = (byte)ci.byteAt(index + 2);
/* 541 */         int pos = ci.insertExGap(3);
/* 542 */         ci.writeByte(196, pos - 3);
/* 543 */         ci.writeByte(132, pos - 2);
/* 544 */         ci.write16bit(var, pos - 1);
/* 545 */         ci.write16bit(plus, pos + 1);
/*     */       }
/*     */     
/* 548 */     } else if (opcode == 169) {
/* 549 */       shiftIndex8(ci, index, opcode, lessThan, delta);
/* 550 */     } else if (opcode == 196) {
/* 551 */       int var = ci.u16bitAt(index + 2);
/* 552 */       if (var < lessThan) {
/*     */         return;
/*     */       }
/* 555 */       var += delta;
/* 556 */       ci.write16bit(var, index + 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void shiftIndex8(CodeIterator ci, int index, int opcode, int lessThan, int delta) throws BadBytecode {
/* 564 */     int var = ci.byteAt(index + 1);
/* 565 */     if (var < lessThan) {
/*     */       return;
/*     */     }
/* 568 */     var += delta;
/* 569 */     if (var < 256) {
/* 570 */       ci.writeByte(var, index + 1);
/*     */     } else {
/* 572 */       int pos = ci.insertExGap(2);
/* 573 */       ci.writeByte(196, pos - 2);
/* 574 */       ci.writeByte(opcode, pos - 1);
/* 575 */       ci.write16bit(var, pos);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void shiftIndex0(CodeIterator ci, int index, int opcode, int lessThan, int delta, int opcode_i_0, int opcode_i) throws BadBytecode {
/* 584 */     int var = (opcode - opcode_i_0) % 4;
/* 585 */     if (var < lessThan) {
/*     */       return;
/*     */     }
/* 588 */     var += delta;
/* 589 */     if (var < 4) {
/* 590 */       ci.writeByte(opcode + delta, index);
/*     */     } else {
/* 592 */       opcode = (opcode - opcode_i_0) / 4 + opcode_i;
/* 593 */       if (var < 256) {
/* 594 */         int pos = ci.insertExGap(1);
/* 595 */         ci.writeByte(opcode, pos - 1);
/* 596 */         ci.writeByte(var, pos);
/*     */       } else {
/*     */         
/* 599 */         int pos = ci.insertExGap(3);
/* 600 */         ci.writeByte(196, pos - 1);
/* 601 */         ci.writeByte(opcode, pos);
/* 602 */         ci.write16bit(var, pos + 1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\CodeAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */