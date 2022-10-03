/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassFileWriter
/*     */ {
/*     */   private ByteStream output;
/*     */   private ConstPoolWriter constPool;
/*     */   private FieldWriter fields;
/*     */   private MethodWriter methods;
/*     */   int thisClass;
/*     */   int superClass;
/*     */   
/*     */   public ClassFileWriter(int major, int minor) {
/*  89 */     this.output = new ByteStream(512);
/*  90 */     this.output.writeInt(-889275714);
/*  91 */     this.output.writeShort(minor);
/*  92 */     this.output.writeShort(major);
/*  93 */     this.constPool = new ConstPoolWriter(this.output);
/*  94 */     this.fields = new FieldWriter(this.constPool);
/*  95 */     this.methods = new MethodWriter(this.constPool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstPoolWriter getConstPool() {
/* 102 */     return this.constPool;
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldWriter getFieldWriter() {
/* 107 */     return this.fields;
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodWriter getMethodWriter() {
/* 112 */     return this.methods;
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
/*     */   public byte[] end(int accessFlags, int thisClass, int superClass, int[] interfaces, AttributeWriter aw) {
/* 129 */     this.constPool.end();
/* 130 */     this.output.writeShort(accessFlags);
/* 131 */     this.output.writeShort(thisClass);
/* 132 */     this.output.writeShort(superClass);
/* 133 */     if (interfaces == null) {
/* 134 */       this.output.writeShort(0);
/*     */     } else {
/* 136 */       int n = interfaces.length;
/* 137 */       this.output.writeShort(n);
/* 138 */       for (int i = 0; i < n; i++) {
/* 139 */         this.output.writeShort(interfaces[i]);
/*     */       }
/*     */     } 
/* 142 */     this.output.enlarge(this.fields.dataSize() + this.methods.dataSize() + 6);
/*     */     try {
/* 144 */       this.output.writeShort(this.fields.size());
/* 145 */       this.fields.write(this.output);
/*     */       
/* 147 */       this.output.writeShort(this.methods.numOfMethods());
/* 148 */       this.methods.write(this.output);
/*     */     }
/* 150 */     catch (IOException iOException) {}
/*     */     
/* 152 */     writeAttribute(this.output, aw, 0);
/* 153 */     return this.output.toByteArray();
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
/*     */   public void end(DataOutputStream out, int accessFlags, int thisClass, int superClass, int[] interfaces, AttributeWriter aw) throws IOException {
/* 175 */     this.constPool.end();
/* 176 */     this.output.writeTo(out);
/* 177 */     out.writeShort(accessFlags);
/* 178 */     out.writeShort(thisClass);
/* 179 */     out.writeShort(superClass);
/* 180 */     if (interfaces == null) {
/* 181 */       out.writeShort(0);
/*     */     } else {
/* 183 */       int n = interfaces.length;
/* 184 */       out.writeShort(n);
/* 185 */       for (int i = 0; i < n; i++) {
/* 186 */         out.writeShort(interfaces[i]);
/*     */       }
/*     */     } 
/* 189 */     out.writeShort(this.fields.size());
/* 190 */     this.fields.write(out);
/*     */     
/* 192 */     out.writeShort(this.methods.numOfMethods());
/* 193 */     this.methods.write(out);
/* 194 */     if (aw == null) {
/* 195 */       out.writeShort(0);
/*     */     } else {
/* 197 */       out.writeShort(aw.size());
/* 198 */       aw.write(out);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void writeAttribute(ByteStream bs, AttributeWriter aw, int attrCount) {
/* 236 */     if (aw == null) {
/* 237 */       bs.writeShort(attrCount);
/*     */       
/*     */       return;
/*     */     } 
/* 241 */     bs.writeShort(aw.size() + attrCount);
/* 242 */     DataOutputStream dos = new DataOutputStream(bs);
/*     */     try {
/* 244 */       aw.write(dos);
/* 245 */       dos.flush();
/*     */     }
/* 247 */     catch (IOException iOException) {}
/*     */   }
/*     */   
/*     */   public static interface AttributeWriter {
/*     */     int size();
/*     */     
/*     */     void write(DataOutputStream param1DataOutputStream) throws IOException; }
/*     */   
/*     */   public static final class FieldWriter {
/*     */     protected ByteStream output;
/*     */     
/*     */     FieldWriter(ClassFileWriter.ConstPoolWriter cp) {
/* 259 */       this.output = new ByteStream(128);
/* 260 */       this.constPool = cp;
/* 261 */       this.fieldCount = 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected ClassFileWriter.ConstPoolWriter constPool;
/*     */ 
/*     */     
/*     */     private int fieldCount;
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(int accessFlags, String name, String descriptor, ClassFileWriter.AttributeWriter aw) {
/* 274 */       int nameIndex = this.constPool.addUtf8Info(name);
/* 275 */       int descIndex = this.constPool.addUtf8Info(descriptor);
/* 276 */       add(accessFlags, nameIndex, descIndex, aw);
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
/*     */     
/*     */     public void add(int accessFlags, int name, int descriptor, ClassFileWriter.AttributeWriter aw) {
/* 289 */       this.fieldCount++;
/* 290 */       this.output.writeShort(accessFlags);
/* 291 */       this.output.writeShort(name);
/* 292 */       this.output.writeShort(descriptor);
/* 293 */       ClassFileWriter.writeAttribute(this.output, aw, 0);
/*     */     }
/*     */     int size() {
/* 296 */       return this.fieldCount;
/*     */     } int dataSize() {
/* 298 */       return this.output.size();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void write(OutputStream out) throws IOException {
/* 304 */       this.output.writeTo(out);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class MethodWriter
/*     */   {
/*     */     protected ByteStream output;
/*     */     
/*     */     protected ClassFileWriter.ConstPoolWriter constPool;
/*     */     
/*     */     private int methodCount;
/*     */     protected int codeIndex;
/*     */     protected int throwsIndex;
/*     */     protected int stackIndex;
/*     */     private int startPos;
/*     */     private boolean isAbstract;
/*     */     private int catchPos;
/*     */     private int catchCount;
/*     */     
/*     */     MethodWriter(ClassFileWriter.ConstPoolWriter cp) {
/* 325 */       this.output = new ByteStream(256);
/* 326 */       this.constPool = cp;
/* 327 */       this.methodCount = 0;
/* 328 */       this.codeIndex = 0;
/* 329 */       this.throwsIndex = 0;
/* 330 */       this.stackIndex = 0;
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void begin(int accessFlags, String name, String descriptor, String[] exceptions, ClassFileWriter.AttributeWriter aw) {
/* 346 */       int intfs[], nameIndex = this.constPool.addUtf8Info(name);
/* 347 */       int descIndex = this.constPool.addUtf8Info(descriptor);
/*     */       
/* 349 */       if (exceptions == null) {
/* 350 */         intfs = null;
/*     */       } else {
/* 352 */         intfs = this.constPool.addClassInfo(exceptions);
/*     */       } 
/* 354 */       begin(accessFlags, nameIndex, descIndex, intfs, aw);
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
/*     */ 
/*     */     
/*     */     public void begin(int accessFlags, int name, int descriptor, int[] exceptions, ClassFileWriter.AttributeWriter aw) {
/* 368 */       this.methodCount++;
/* 369 */       this.output.writeShort(accessFlags);
/* 370 */       this.output.writeShort(name);
/* 371 */       this.output.writeShort(descriptor);
/* 372 */       this.isAbstract = ((accessFlags & 0x400) != 0);
/*     */       
/* 374 */       int attrCount = this.isAbstract ? 0 : 1;
/* 375 */       if (exceptions != null) {
/* 376 */         attrCount++;
/*     */       }
/* 378 */       ClassFileWriter.writeAttribute(this.output, aw, attrCount);
/*     */       
/* 380 */       if (exceptions != null) {
/* 381 */         writeThrows(exceptions);
/*     */       }
/* 383 */       if (!this.isAbstract) {
/* 384 */         if (this.codeIndex == 0) {
/* 385 */           this.codeIndex = this.constPool.addUtf8Info("Code");
/*     */         }
/* 387 */         this.startPos = this.output.getPos();
/* 388 */         this.output.writeShort(this.codeIndex);
/* 389 */         this.output.writeBlank(12);
/*     */       } 
/*     */       
/* 392 */       this.catchPos = -1;
/* 393 */       this.catchCount = 0;
/*     */     }
/*     */     
/*     */     private void writeThrows(int[] exceptions) {
/* 397 */       if (this.throwsIndex == 0) {
/* 398 */         this.throwsIndex = this.constPool.addUtf8Info("Exceptions");
/*     */       }
/* 400 */       this.output.writeShort(this.throwsIndex);
/* 401 */       this.output.writeInt(exceptions.length * 2 + 2);
/* 402 */       this.output.writeShort(exceptions.length);
/* 403 */       for (int i = 0; i < exceptions.length; i++) {
/* 404 */         this.output.writeShort(exceptions[i]);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(int b) {
/* 413 */       this.output.write(b);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void add16(int b) {
/* 420 */       this.output.writeShort(b);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void add32(int b) {
/* 427 */       this.output.writeInt(b);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addInvoke(int opcode, String targetClass, String methodName, String descriptor) {
/* 437 */       int target = this.constPool.addClassInfo(targetClass);
/* 438 */       int nt = this.constPool.addNameAndTypeInfo(methodName, descriptor);
/* 439 */       int method = this.constPool.addMethodrefInfo(target, nt);
/* 440 */       add(opcode);
/* 441 */       add16(method);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void codeEnd(int maxStack, int maxLocals) {
/* 448 */       if (!this.isAbstract) {
/* 449 */         this.output.writeShort(this.startPos + 6, maxStack);
/* 450 */         this.output.writeShort(this.startPos + 8, maxLocals);
/* 451 */         this.output.writeInt(this.startPos + 10, this.output.getPos() - this.startPos - 14);
/* 452 */         this.catchPos = this.output.getPos();
/* 453 */         this.catchCount = 0;
/* 454 */         this.output.writeShort(0);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addCatch(int startPc, int endPc, int handlerPc, int catchType) {
/* 466 */       this.catchCount++;
/* 467 */       this.output.writeShort(startPc);
/* 468 */       this.output.writeShort(endPc);
/* 469 */       this.output.writeShort(handlerPc);
/* 470 */       this.output.writeShort(catchType);
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
/*     */     public void end(StackMapTable.Writer smap, ClassFileWriter.AttributeWriter aw) {
/* 482 */       if (this.isAbstract) {
/*     */         return;
/*     */       }
/*     */       
/* 486 */       this.output.writeShort(this.catchPos, this.catchCount);
/*     */       
/* 488 */       int attrCount = (smap == null) ? 0 : 1;
/* 489 */       ClassFileWriter.writeAttribute(this.output, aw, attrCount);
/*     */       
/* 491 */       if (smap != null) {
/* 492 */         if (this.stackIndex == 0) {
/* 493 */           this.stackIndex = this.constPool.addUtf8Info("StackMapTable");
/*     */         }
/* 495 */         this.output.writeShort(this.stackIndex);
/* 496 */         byte[] data = smap.toByteArray();
/* 497 */         this.output.writeInt(data.length);
/* 498 */         this.output.write(data);
/*     */       } 
/*     */ 
/*     */       
/* 502 */       this.output.writeInt(this.startPos + 2, this.output.getPos() - this.startPos - 6);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 511 */       return this.output.getPos() - this.startPos - 14;
/*     */     } int numOfMethods() {
/* 513 */       return this.methodCount;
/*     */     } int dataSize() {
/* 515 */       return this.output.size();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void write(OutputStream out) throws IOException {
/* 521 */       this.output.writeTo(out);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class ConstPoolWriter
/*     */   {
/*     */     ByteStream output;
/*     */     
/*     */     protected int startPos;
/*     */     protected int num;
/*     */     
/*     */     ConstPoolWriter(ByteStream out) {
/* 534 */       this.output = out;
/* 535 */       this.startPos = out.getPos();
/* 536 */       this.num = 1;
/* 537 */       this.output.writeShort(1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int[] addClassInfo(String[] classNames) {
/* 546 */       int n = classNames.length;
/* 547 */       int[] result = new int[n];
/* 548 */       for (int i = 0; i < n; i++) {
/* 549 */         result[i] = addClassInfo(classNames[i]);
/*     */       }
/* 551 */       return result;
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
/*     */ 
/*     */     
/*     */     public int addClassInfo(String jvmname) {
/* 565 */       int utf8 = addUtf8Info(jvmname);
/* 566 */       this.output.write(7);
/* 567 */       this.output.writeShort(utf8);
/* 568 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addClassInfo(int name) {
/* 578 */       this.output.write(7);
/* 579 */       this.output.writeShort(name);
/* 580 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addNameAndTypeInfo(String name, String type) {
/* 591 */       return addNameAndTypeInfo(addUtf8Info(name), addUtf8Info(type));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addNameAndTypeInfo(int name, int type) {
/* 602 */       this.output.write(12);
/* 603 */       this.output.writeShort(name);
/* 604 */       this.output.writeShort(type);
/* 605 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addFieldrefInfo(int classInfo, int nameAndTypeInfo) {
/* 616 */       this.output.write(9);
/* 617 */       this.output.writeShort(classInfo);
/* 618 */       this.output.writeShort(nameAndTypeInfo);
/* 619 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addMethodrefInfo(int classInfo, int nameAndTypeInfo) {
/* 630 */       this.output.write(10);
/* 631 */       this.output.writeShort(classInfo);
/* 632 */       this.output.writeShort(nameAndTypeInfo);
/* 633 */       return this.num++;
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
/*     */     
/*     */     public int addInterfaceMethodrefInfo(int classInfo, int nameAndTypeInfo) {
/* 646 */       this.output.write(11);
/* 647 */       this.output.writeShort(classInfo);
/* 648 */       this.output.writeShort(nameAndTypeInfo);
/* 649 */       return this.num++;
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
/*     */ 
/*     */ 
/*     */     
/*     */     public int addMethodHandleInfo(int kind, int index) {
/* 664 */       this.output.write(15);
/* 665 */       this.output.write(kind);
/* 666 */       this.output.writeShort(index);
/* 667 */       return this.num++;
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
/*     */     
/*     */     public int addMethodTypeInfo(int desc) {
/* 680 */       this.output.write(16);
/* 681 */       this.output.writeShort(desc);
/* 682 */       return this.num++;
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
/*     */ 
/*     */ 
/*     */     
/*     */     public int addInvokeDynamicInfo(int bootstrap, int nameAndTypeInfo) {
/* 697 */       this.output.write(18);
/* 698 */       this.output.writeShort(bootstrap);
/* 699 */       this.output.writeShort(nameAndTypeInfo);
/* 700 */       return this.num++;
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
/*     */ 
/*     */ 
/*     */     
/*     */     public int addDynamicInfo(int bootstrap, int nameAndTypeInfo) {
/* 715 */       this.output.write(17);
/* 716 */       this.output.writeShort(bootstrap);
/* 717 */       this.output.writeShort(nameAndTypeInfo);
/* 718 */       return this.num++;
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
/*     */     
/*     */     public int addStringInfo(String str) {
/* 731 */       int utf8 = addUtf8Info(str);
/* 732 */       this.output.write(8);
/* 733 */       this.output.writeShort(utf8);
/* 734 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addIntegerInfo(int i) {
/* 744 */       this.output.write(3);
/* 745 */       this.output.writeInt(i);
/* 746 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addFloatInfo(float f) {
/* 756 */       this.output.write(4);
/* 757 */       this.output.writeFloat(f);
/* 758 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addLongInfo(long l) {
/* 768 */       this.output.write(5);
/* 769 */       this.output.writeLong(l);
/* 770 */       int n = this.num;
/* 771 */       this.num += 2;
/* 772 */       return n;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addDoubleInfo(double d) {
/* 782 */       this.output.write(6);
/* 783 */       this.output.writeDouble(d);
/* 784 */       int n = this.num;
/* 785 */       this.num += 2;
/* 786 */       return n;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addUtf8Info(String utf8) {
/* 796 */       this.output.write(1);
/* 797 */       this.output.writeUTF(utf8);
/* 798 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void end() {
/* 805 */       this.output.writeShort(this.startPos, this.num);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\ClassFileWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */