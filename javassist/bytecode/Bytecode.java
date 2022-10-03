/*      */ package javassist.bytecode;
/*      */ 
/*      */ import javassist.CtClass;
/*      */ import javassist.CtPrimitiveType;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Bytecode
/*      */   extends ByteVector
/*      */   implements Cloneable, Opcode
/*      */ {
/*  122 */   public static final CtClass THIS = ConstPool.THIS;
/*      */ 
/*      */ 
/*      */   
/*      */   ConstPool constPool;
/*      */ 
/*      */   
/*      */   int maxStack;
/*      */ 
/*      */   
/*      */   int maxLocals;
/*      */ 
/*      */   
/*      */   ExceptionTable tryblocks;
/*      */ 
/*      */   
/*      */   private int stackDepth;
/*      */ 
/*      */ 
/*      */   
/*      */   public Bytecode(ConstPool cp, int stacksize, int localvars) {
/*  143 */     this.constPool = cp;
/*  144 */     this.maxStack = stacksize;
/*  145 */     this.maxLocals = localvars;
/*  146 */     this.tryblocks = new ExceptionTable(cp);
/*  147 */     this.stackDepth = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Bytecode(ConstPool cp) {
/*  160 */     this(cp, 0, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object clone() {
/*      */     try {
/*  171 */       Bytecode bc = (Bytecode)super.clone();
/*  172 */       bc.tryblocks = (ExceptionTable)this.tryblocks.clone();
/*  173 */       return bc;
/*      */     }
/*  175 */     catch (CloneNotSupportedException cnse) {
/*  176 */       throw new RuntimeException(cnse);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ConstPool getConstPool() {
/*  183 */     return this.constPool;
/*      */   }
/*      */ 
/*      */   
/*      */   public ExceptionTable getExceptionTable() {
/*  188 */     return this.tryblocks;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CodeAttribute toCodeAttribute() {
/*  194 */     return new CodeAttribute(this.constPool, this.maxStack, this.maxLocals, 
/*  195 */         get(), this.tryblocks);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int length() {
/*  202 */     return getSize();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] get() {
/*  209 */     return copy();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxStack() {
/*  215 */     return this.maxStack;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxStack(int size) {
/*  232 */     this.maxStack = size;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxLocals() {
/*  238 */     return this.maxLocals;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxLocals(int size) {
/*  244 */     this.maxLocals = size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxLocals(boolean isStatic, CtClass[] params, int locals) {
/*  262 */     if (!isStatic) {
/*  263 */       locals++;
/*      */     }
/*  265 */     if (params != null) {
/*  266 */       CtClass doubleType = CtClass.doubleType;
/*  267 */       CtClass longType = CtClass.longType;
/*  268 */       int n = params.length;
/*  269 */       for (int i = 0; i < n; i++) {
/*  270 */         CtClass type = params[i];
/*  271 */         if (type == doubleType || type == longType) {
/*  272 */           locals += 2;
/*      */         } else {
/*  274 */           locals++;
/*      */         } 
/*      */       } 
/*      */     } 
/*  278 */     this.maxLocals = locals;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void incMaxLocals(int diff) {
/*  285 */     this.maxLocals += diff;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addExceptionHandler(int start, int end, int handler, CtClass type) {
/*  293 */     addExceptionHandler(start, end, handler, this.constPool
/*  294 */         .addClassInfo(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addExceptionHandler(int start, int end, int handler, String type) {
/*  304 */     addExceptionHandler(start, end, handler, this.constPool
/*  305 */         .addClassInfo(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addExceptionHandler(int start, int end, int handler, int type) {
/*  313 */     this.tryblocks.add(start, end, handler, type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int currentPc() {
/*  321 */     return getSize();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int read(int offset) {
/*  332 */     return super.read(offset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int read16bit(int offset) {
/*  340 */     int v1 = read(offset);
/*  341 */     int v2 = read(offset + 1);
/*  342 */     return (v1 << 8) + (v2 & 0xFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int read32bit(int offset) {
/*  350 */     int v1 = read16bit(offset);
/*  351 */     int v2 = read16bit(offset + 2);
/*  352 */     return (v1 << 16) + (v2 & 0xFFFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write(int offset, int value) {
/*  363 */     super.write(offset, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write16bit(int offset, int value) {
/*  371 */     write(offset, value >> 8);
/*  372 */     write(offset + 1, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write32bit(int offset, int value) {
/*  380 */     write16bit(offset, value >> 16);
/*  381 */     write16bit(offset + 2, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(int code) {
/*  389 */     super.add(code);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add32bit(int value) {
/*  396 */     add(value >> 24, value >> 16, value >> 8, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addGap(int length) {
/*  406 */     super.addGap(length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addOpcode(int code) {
/*  421 */     add(code);
/*  422 */     growStack(STACK_GROW[code]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void growStack(int diff) {
/*  433 */     setStackDepth(this.stackDepth + diff);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getStackDepth() {
/*  439 */     return this.stackDepth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStackDepth(int depth) {
/*  449 */     this.stackDepth = depth;
/*  450 */     if (this.stackDepth > this.maxStack) {
/*  451 */       this.maxStack = this.stackDepth;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addIndex(int index) {
/*  459 */     add(index >> 8, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAload(int n) {
/*  468 */     if (n < 4) {
/*  469 */       addOpcode(42 + n);
/*  470 */     } else if (n < 256) {
/*  471 */       addOpcode(25);
/*  472 */       add(n);
/*      */     } else {
/*      */       
/*  475 */       addOpcode(196);
/*  476 */       addOpcode(25);
/*  477 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAstore(int n) {
/*  487 */     if (n < 4) {
/*  488 */       addOpcode(75 + n);
/*  489 */     } else if (n < 256) {
/*  490 */       addOpcode(58);
/*  491 */       add(n);
/*      */     } else {
/*      */       
/*  494 */       addOpcode(196);
/*  495 */       addOpcode(58);
/*  496 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addIconst(int n) {
/*  506 */     if (n < 6 && -2 < n) {
/*  507 */       addOpcode(3 + n);
/*  508 */     } else if (n <= 127 && -128 <= n) {
/*  509 */       addOpcode(16);
/*  510 */       add(n);
/*      */     }
/*  512 */     else if (n <= 32767 && -32768 <= n) {
/*  513 */       addOpcode(17);
/*  514 */       add(n >> 8);
/*  515 */       add(n);
/*      */     } else {
/*      */       
/*  518 */       addLdc(this.constPool.addIntegerInfo(n));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConstZero(CtClass type) {
/*  528 */     if (type.isPrimitive()) {
/*  529 */       if (type == CtClass.longType)
/*  530 */       { addOpcode(9); }
/*  531 */       else if (type == CtClass.floatType)
/*  532 */       { addOpcode(11); }
/*  533 */       else if (type == CtClass.doubleType)
/*  534 */       { addOpcode(14); }
/*  535 */       else { if (type == CtClass.voidType) {
/*  536 */           throw new RuntimeException("void type?");
/*      */         }
/*  538 */         addOpcode(3); }
/*      */     
/*      */     } else {
/*  541 */       addOpcode(1);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addIload(int n) {
/*  550 */     if (n < 4) {
/*  551 */       addOpcode(26 + n);
/*  552 */     } else if (n < 256) {
/*  553 */       addOpcode(21);
/*  554 */       add(n);
/*      */     } else {
/*      */       
/*  557 */       addOpcode(196);
/*  558 */       addOpcode(21);
/*  559 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addIstore(int n) {
/*  569 */     if (n < 4) {
/*  570 */       addOpcode(59 + n);
/*  571 */     } else if (n < 256) {
/*  572 */       addOpcode(54);
/*  573 */       add(n);
/*      */     } else {
/*      */       
/*  576 */       addOpcode(196);
/*  577 */       addOpcode(54);
/*  578 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLconst(long n) {
/*  588 */     if (n == 0L || n == 1L) {
/*  589 */       addOpcode(9 + (int)n);
/*      */     } else {
/*  591 */       addLdc2w(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLload(int n) {
/*  600 */     if (n < 4) {
/*  601 */       addOpcode(30 + n);
/*  602 */     } else if (n < 256) {
/*  603 */       addOpcode(22);
/*  604 */       add(n);
/*      */     } else {
/*      */       
/*  607 */       addOpcode(196);
/*  608 */       addOpcode(22);
/*  609 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLstore(int n) {
/*  619 */     if (n < 4) {
/*  620 */       addOpcode(63 + n);
/*  621 */     } else if (n < 256) {
/*  622 */       addOpcode(55);
/*  623 */       add(n);
/*      */     } else {
/*      */       
/*  626 */       addOpcode(196);
/*  627 */       addOpcode(55);
/*  628 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDconst(double d) {
/*  638 */     if (d == 0.0D || d == 1.0D) {
/*  639 */       addOpcode(14 + (int)d);
/*      */     } else {
/*  641 */       addLdc2w(d);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDload(int n) {
/*  650 */     if (n < 4) {
/*  651 */       addOpcode(38 + n);
/*  652 */     } else if (n < 256) {
/*  653 */       addOpcode(24);
/*  654 */       add(n);
/*      */     } else {
/*      */       
/*  657 */       addOpcode(196);
/*  658 */       addOpcode(24);
/*  659 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDstore(int n) {
/*  669 */     if (n < 4) {
/*  670 */       addOpcode(71 + n);
/*  671 */     } else if (n < 256) {
/*  672 */       addOpcode(57);
/*  673 */       add(n);
/*      */     } else {
/*      */       
/*  676 */       addOpcode(196);
/*  677 */       addOpcode(57);
/*  678 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFconst(float f) {
/*  688 */     if (f == 0.0F || f == 1.0F || f == 2.0F) {
/*  689 */       addOpcode(11 + (int)f);
/*      */     } else {
/*  691 */       addLdc(this.constPool.addFloatInfo(f));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFload(int n) {
/*  700 */     if (n < 4) {
/*  701 */       addOpcode(34 + n);
/*  702 */     } else if (n < 256) {
/*  703 */       addOpcode(23);
/*  704 */       add(n);
/*      */     } else {
/*      */       
/*  707 */       addOpcode(196);
/*  708 */       addOpcode(23);
/*  709 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFstore(int n) {
/*  719 */     if (n < 4) {
/*  720 */       addOpcode(67 + n);
/*  721 */     } else if (n < 256) {
/*  722 */       addOpcode(56);
/*  723 */       add(n);
/*      */     } else {
/*      */       
/*  726 */       addOpcode(196);
/*  727 */       addOpcode(56);
/*  728 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addLoad(int n, CtClass type) {
/*  741 */     if (type.isPrimitive())
/*  742 */     { if (type == CtClass.booleanType || type == CtClass.charType || type == CtClass.byteType || type == CtClass.shortType || type == CtClass.intType)
/*      */       
/*      */       { 
/*  745 */         addIload(n); }
/*  746 */       else { if (type == CtClass.longType) {
/*  747 */           addLload(n);
/*  748 */           return 2;
/*      */         } 
/*  750 */         if (type == CtClass.floatType)
/*  751 */         { addFload(n); }
/*  752 */         else { if (type == CtClass.doubleType) {
/*  753 */             addDload(n);
/*  754 */             return 2;
/*      */           } 
/*      */           
/*  757 */           throw new RuntimeException("void type?"); }
/*      */          }
/*      */        }
/*  760 */     else { addAload(n); }
/*      */     
/*  762 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addStore(int n, CtClass type) {
/*  774 */     if (type.isPrimitive())
/*  775 */     { if (type == CtClass.booleanType || type == CtClass.charType || type == CtClass.byteType || type == CtClass.shortType || type == CtClass.intType)
/*      */       
/*      */       { 
/*  778 */         addIstore(n); }
/*  779 */       else { if (type == CtClass.longType) {
/*  780 */           addLstore(n);
/*  781 */           return 2;
/*      */         } 
/*  783 */         if (type == CtClass.floatType)
/*  784 */         { addFstore(n); }
/*  785 */         else { if (type == CtClass.doubleType) {
/*  786 */             addDstore(n);
/*  787 */             return 2;
/*      */           } 
/*      */           
/*  790 */           throw new RuntimeException("void type?"); }
/*      */          }
/*      */        }
/*  793 */     else { addAstore(n); }
/*      */     
/*  795 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addLoadParameters(CtClass[] params, int offset) {
/*  806 */     int stacksize = 0;
/*  807 */     if (params != null) {
/*  808 */       int n = params.length;
/*  809 */       for (int i = 0; i < n; i++) {
/*  810 */         stacksize += addLoad(stacksize + offset, params[i]);
/*      */       }
/*      */     } 
/*  813 */     return stacksize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCheckcast(CtClass c) {
/*  822 */     addOpcode(192);
/*  823 */     addIndex(this.constPool.addClassInfo(c));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCheckcast(String classname) {
/*  832 */     addOpcode(192);
/*  833 */     addIndex(this.constPool.addClassInfo(classname));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInstanceof(String classname) {
/*  842 */     addOpcode(193);
/*  843 */     addIndex(this.constPool.addClassInfo(classname));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addGetfield(CtClass c, String name, String type) {
/*  856 */     add(180);
/*  857 */     int ci = this.constPool.addClassInfo(c);
/*  858 */     addIndex(this.constPool.addFieldrefInfo(ci, name, type));
/*  859 */     growStack(Descriptor.dataSize(type) - 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addGetfield(String c, String name, String type) {
/*  872 */     add(180);
/*  873 */     int ci = this.constPool.addClassInfo(c);
/*  874 */     addIndex(this.constPool.addFieldrefInfo(ci, name, type));
/*  875 */     growStack(Descriptor.dataSize(type) - 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addGetstatic(CtClass c, String name, String type) {
/*  888 */     add(178);
/*  889 */     int ci = this.constPool.addClassInfo(c);
/*  890 */     addIndex(this.constPool.addFieldrefInfo(ci, name, type));
/*  891 */     growStack(Descriptor.dataSize(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addGetstatic(String c, String name, String type) {
/*  904 */     add(178);
/*  905 */     int ci = this.constPool.addClassInfo(c);
/*  906 */     addIndex(this.constPool.addFieldrefInfo(ci, name, type));
/*  907 */     growStack(Descriptor.dataSize(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokespecial(CtClass clazz, String name, CtClass returnType, CtClass[] paramTypes) {
/*  920 */     String desc = Descriptor.ofMethod(returnType, paramTypes);
/*  921 */     addInvokespecial(clazz, name, desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokespecial(CtClass clazz, String name, String desc) {
/*  935 */     boolean isInterface = (clazz == null) ? false : clazz.isInterface();
/*  936 */     addInvokespecial(isInterface, this.constPool
/*  937 */         .addClassInfo(clazz), name, desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokespecial(String clazz, String name, String desc) {
/*  952 */     addInvokespecial(false, this.constPool.addClassInfo(clazz), name, desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokespecial(int clazz, String name, String desc) {
/*  968 */     addInvokespecial(false, clazz, name, desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokespecial(boolean isInterface, int clazz, String name, String desc) {
/*      */     int index;
/*  986 */     if (isInterface) {
/*  987 */       index = this.constPool.addInterfaceMethodrefInfo(clazz, name, desc);
/*      */     } else {
/*  989 */       index = this.constPool.addMethodrefInfo(clazz, name, desc);
/*      */     } 
/*  991 */     addInvokespecial(index, desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokespecial(int index, String desc) {
/* 1005 */     add(183);
/* 1006 */     addIndex(index);
/* 1007 */     growStack(Descriptor.dataSize(desc) - 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokestatic(CtClass clazz, String name, CtClass returnType, CtClass[] paramTypes) {
/* 1020 */     String desc = Descriptor.ofMethod(returnType, paramTypes);
/* 1021 */     addInvokestatic(clazz, name, desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokestatic(CtClass clazz, String name, String desc) {
/*      */     boolean isInterface;
/* 1035 */     if (clazz == THIS) {
/* 1036 */       isInterface = false;
/*      */     } else {
/* 1038 */       isInterface = clazz.isInterface();
/*      */     } 
/* 1040 */     addInvokestatic(this.constPool.addClassInfo(clazz), name, desc, isInterface);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokestatic(String classname, String name, String desc) {
/* 1054 */     addInvokestatic(this.constPool.addClassInfo(classname), name, desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokestatic(int clazz, String name, String desc) {
/* 1068 */     addInvokestatic(clazz, name, desc, false);
/*      */   }
/*      */   
/*      */   private void addInvokestatic(int clazz, String name, String desc, boolean isInterface) {
/*      */     int index;
/* 1073 */     add(184);
/*      */     
/* 1075 */     if (isInterface) {
/* 1076 */       index = this.constPool.addInterfaceMethodrefInfo(clazz, name, desc);
/*      */     } else {
/* 1078 */       index = this.constPool.addMethodrefInfo(clazz, name, desc);
/*      */     } 
/* 1080 */     addIndex(index);
/* 1081 */     growStack(Descriptor.dataSize(desc));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokevirtual(CtClass clazz, String name, CtClass returnType, CtClass[] paramTypes) {
/* 1098 */     String desc = Descriptor.ofMethod(returnType, paramTypes);
/* 1099 */     addInvokevirtual(clazz, name, desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokevirtual(CtClass clazz, String name, String desc) {
/* 1116 */     addInvokevirtual(this.constPool.addClassInfo(clazz), name, desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokevirtual(String classname, String name, String desc) {
/* 1133 */     addInvokevirtual(this.constPool.addClassInfo(classname), name, desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokevirtual(int clazz, String name, String desc) {
/* 1151 */     add(182);
/* 1152 */     addIndex(this.constPool.addMethodrefInfo(clazz, name, desc));
/* 1153 */     growStack(Descriptor.dataSize(desc) - 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokeinterface(CtClass clazz, String name, CtClass returnType, CtClass[] paramTypes, int count) {
/* 1168 */     String desc = Descriptor.ofMethod(returnType, paramTypes);
/* 1169 */     addInvokeinterface(clazz, name, desc, count);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokeinterface(CtClass clazz, String name, String desc, int count) {
/* 1184 */     addInvokeinterface(this.constPool.addClassInfo(clazz), name, desc, count);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokeinterface(String classname, String name, String desc, int count) {
/* 1200 */     addInvokeinterface(this.constPool.addClassInfo(classname), name, desc, count);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokeinterface(int clazz, String name, String desc, int count) {
/* 1217 */     add(185);
/* 1218 */     addIndex(this.constPool.addInterfaceMethodrefInfo(clazz, name, desc));
/* 1219 */     add(count);
/* 1220 */     add(0);
/* 1221 */     growStack(Descriptor.dataSize(desc) - 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInvokedynamic(int bootstrap, String name, String desc) {
/* 1235 */     int nt = this.constPool.addNameAndTypeInfo(name, desc);
/* 1236 */     int dyn = this.constPool.addInvokeDynamicInfo(bootstrap, nt);
/* 1237 */     add(186);
/* 1238 */     addIndex(dyn);
/* 1239 */     add(0, 0);
/* 1240 */     growStack(Descriptor.dataSize(desc));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLdc(String s) {
/* 1250 */     addLdc(this.constPool.addStringInfo(s));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLdc(int i) {
/* 1259 */     if (i > 255) {
/* 1260 */       addOpcode(19);
/* 1261 */       addIndex(i);
/*      */     } else {
/*      */       
/* 1264 */       addOpcode(18);
/* 1265 */       add(i);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLdc2w(long l) {
/* 1273 */     addOpcode(20);
/* 1274 */     addIndex(this.constPool.addLongInfo(l));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLdc2w(double d) {
/* 1281 */     addOpcode(20);
/* 1282 */     addIndex(this.constPool.addDoubleInfo(d));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addNew(CtClass clazz) {
/* 1291 */     addOpcode(187);
/* 1292 */     addIndex(this.constPool.addClassInfo(clazz));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addNew(String classname) {
/* 1301 */     addOpcode(187);
/* 1302 */     addIndex(this.constPool.addClassInfo(classname));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAnewarray(String classname) {
/* 1311 */     addOpcode(189);
/* 1312 */     addIndex(this.constPool.addClassInfo(classname));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAnewarray(CtClass clazz, int length) {
/* 1322 */     addIconst(length);
/* 1323 */     addOpcode(189);
/* 1324 */     addIndex(this.constPool.addClassInfo(clazz));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addNewarray(int atype, int length) {
/* 1334 */     addIconst(length);
/* 1335 */     addOpcode(188);
/* 1336 */     add(atype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addMultiNewarray(CtClass clazz, int[] dimensions) {
/* 1347 */     int len = dimensions.length;
/* 1348 */     for (int i = 0; i < len; i++) {
/* 1349 */       addIconst(dimensions[i]);
/*      */     }
/* 1351 */     growStack(len);
/* 1352 */     return addMultiNewarray(clazz, len);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addMultiNewarray(CtClass clazz, int dim) {
/* 1364 */     add(197);
/* 1365 */     addIndex(this.constPool.addClassInfo(clazz));
/* 1366 */     add(dim);
/* 1367 */     growStack(1 - dim);
/* 1368 */     return dim;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addMultiNewarray(String desc, int dim) {
/* 1379 */     add(197);
/* 1380 */     addIndex(this.constPool.addClassInfo(desc));
/* 1381 */     add(dim);
/* 1382 */     growStack(1 - dim);
/* 1383 */     return dim;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPutfield(CtClass c, String name, String desc) {
/* 1394 */     addPutfield0(c, (String)null, name, desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPutfield(String classname, String name, String desc) {
/* 1406 */     addPutfield0((CtClass)null, classname, name, desc);
/*      */   }
/*      */ 
/*      */   
/*      */   private void addPutfield0(CtClass target, String classname, String name, String desc) {
/* 1411 */     add(181);
/*      */ 
/*      */     
/* 1414 */     int ci = (classname == null) ? this.constPool.addClassInfo(target) : this.constPool.addClassInfo(classname);
/* 1415 */     addIndex(this.constPool.addFieldrefInfo(ci, name, desc));
/* 1416 */     growStack(-1 - Descriptor.dataSize(desc));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPutstatic(CtClass c, String name, String desc) {
/* 1427 */     addPutstatic0(c, (String)null, name, desc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPutstatic(String classname, String fieldName, String desc) {
/* 1439 */     addPutstatic0((CtClass)null, classname, fieldName, desc);
/*      */   }
/*      */ 
/*      */   
/*      */   private void addPutstatic0(CtClass target, String classname, String fieldName, String desc) {
/* 1444 */     add(179);
/*      */ 
/*      */     
/* 1447 */     int ci = (classname == null) ? this.constPool.addClassInfo(target) : this.constPool.addClassInfo(classname);
/* 1448 */     addIndex(this.constPool.addFieldrefInfo(ci, fieldName, desc));
/* 1449 */     growStack(-Descriptor.dataSize(desc));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addReturn(CtClass type) {
/* 1458 */     if (type == null) {
/* 1459 */       addOpcode(177);
/* 1460 */     } else if (type.isPrimitive()) {
/* 1461 */       CtPrimitiveType ptype = (CtPrimitiveType)type;
/* 1462 */       addOpcode(ptype.getReturnOp());
/*      */     } else {
/*      */       
/* 1465 */       addOpcode(176);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addRet(int var) {
/* 1474 */     if (var < 256) {
/* 1475 */       addOpcode(169);
/* 1476 */       add(var);
/*      */     } else {
/*      */       
/* 1479 */       addOpcode(196);
/* 1480 */       addOpcode(169);
/* 1481 */       addIndex(var);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPrintln(String message) {
/* 1492 */     addGetstatic("java.lang.System", "err", "Ljava/io/PrintStream;");
/* 1493 */     addLdc(message);
/* 1494 */     addInvokevirtual("java.io.PrintStream", "println", "(Ljava/lang/String;)V");
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\Bytecode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */