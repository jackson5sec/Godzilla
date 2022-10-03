/*      */ package javassist.bytecode;
/*      */ 
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javassist.CtClass;
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
/*      */ public final class ConstPool
/*      */ {
/*      */   LongVector items;
/*      */   int numOfItems;
/*      */   int thisClassInfo;
/*      */   Map<ConstInfo, ConstInfo> itemsCache;
/*      */   public static final int CONST_Class = 7;
/*      */   public static final int CONST_Fieldref = 9;
/*      */   public static final int CONST_Methodref = 10;
/*      */   public static final int CONST_InterfaceMethodref = 11;
/*      */   public static final int CONST_String = 8;
/*      */   public static final int CONST_Integer = 3;
/*      */   public static final int CONST_Float = 4;
/*      */   public static final int CONST_Long = 5;
/*      */   public static final int CONST_Double = 6;
/*      */   public static final int CONST_NameAndType = 12;
/*      */   public static final int CONST_Utf8 = 1;
/*      */   public static final int CONST_MethodHandle = 15;
/*      */   public static final int CONST_MethodType = 16;
/*      */   public static final int CONST_Dynamic = 17;
/*      */   public static final int CONST_DynamicCallSite = 18;
/*      */   public static final int CONST_InvokeDynamic = 18;
/*      */   public static final int CONST_Module = 19;
/*      */   public static final int CONST_Package = 20;
/*  132 */   public static final CtClass THIS = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_getField = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_getStatic = 2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_putField = 3;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_putStatic = 4;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_invokeVirtual = 5;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_invokeStatic = 6;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_invokeSpecial = 7;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_newInvokeSpecial = 8;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_invokeInterface = 9;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConstPool(String thisclass) {
/*  187 */     this.items = new LongVector();
/*  188 */     this.itemsCache = null;
/*  189 */     this.numOfItems = 0;
/*  190 */     addItem0(null);
/*  191 */     this.thisClassInfo = addClassInfo(thisclass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConstPool(DataInputStream in) throws IOException {
/*  201 */     this.itemsCache = null;
/*  202 */     this.thisClassInfo = 0;
/*      */ 
/*      */     
/*  205 */     read(in);
/*      */   }
/*      */ 
/*      */   
/*      */   void prune() {
/*  210 */     this.itemsCache = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSize() {
/*  218 */     return this.numOfItems;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClassName() {
/*  226 */     return getClassInfo(this.thisClassInfo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getThisClassInfo() {
/*  235 */     return this.thisClassInfo;
/*      */   }
/*      */ 
/*      */   
/*      */   void setThisClassInfo(int i) {
/*  240 */     this.thisClassInfo = i;
/*      */   }
/*      */ 
/*      */   
/*      */   ConstInfo getItem(int n) {
/*  245 */     return this.items.elementAt(n);
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
/*      */   public int getTag(int index) {
/*  257 */     return getItem(index).getTag();
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
/*      */   public String getClassInfo(int index) {
/*  273 */     ClassInfo c = (ClassInfo)getItem(index);
/*  274 */     if (c == null)
/*  275 */       return null; 
/*  276 */     return Descriptor.toJavaName(getUtf8Info(c.name));
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
/*      */   public String getClassInfoByDescriptor(int index) {
/*  290 */     ClassInfo c = (ClassInfo)getItem(index);
/*  291 */     if (c == null)
/*  292 */       return null; 
/*  293 */     String className = getUtf8Info(c.name);
/*  294 */     if (className.charAt(0) == '[')
/*  295 */       return className; 
/*  296 */     return Descriptor.of(className);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNameAndTypeName(int index) {
/*  306 */     NameAndTypeInfo ntinfo = (NameAndTypeInfo)getItem(index);
/*  307 */     return ntinfo.memberName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNameAndTypeDescriptor(int index) {
/*  317 */     NameAndTypeInfo ntinfo = (NameAndTypeInfo)getItem(index);
/*  318 */     return ntinfo.typeDescriptor;
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
/*      */   public int getMemberClass(int index) {
/*  332 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  333 */     return minfo.classIndex;
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
/*      */   public int getMemberNameAndType(int index) {
/*  347 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  348 */     return minfo.nameAndTypeIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFieldrefClass(int index) {
/*  358 */     FieldrefInfo finfo = (FieldrefInfo)getItem(index);
/*  359 */     return finfo.classIndex;
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
/*      */   public String getFieldrefClassName(int index) {
/*  371 */     FieldrefInfo f = (FieldrefInfo)getItem(index);
/*  372 */     if (f == null)
/*  373 */       return null; 
/*  374 */     return getClassInfo(f.classIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFieldrefNameAndType(int index) {
/*  384 */     FieldrefInfo finfo = (FieldrefInfo)getItem(index);
/*  385 */     return finfo.nameAndTypeIndex;
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
/*      */   public String getFieldrefName(int index) {
/*  398 */     FieldrefInfo f = (FieldrefInfo)getItem(index);
/*  399 */     if (f == null)
/*  400 */       return null; 
/*  401 */     NameAndTypeInfo n = (NameAndTypeInfo)getItem(f.nameAndTypeIndex);
/*  402 */     if (n == null)
/*  403 */       return null; 
/*  404 */     return getUtf8Info(n.memberName);
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
/*      */   public String getFieldrefType(int index) {
/*  417 */     FieldrefInfo f = (FieldrefInfo)getItem(index);
/*  418 */     if (f == null)
/*  419 */       return null; 
/*  420 */     NameAndTypeInfo n = (NameAndTypeInfo)getItem(f.nameAndTypeIndex);
/*  421 */     if (n == null)
/*  422 */       return null; 
/*  423 */     return getUtf8Info(n.typeDescriptor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMethodrefClass(int index) {
/*  433 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  434 */     return minfo.classIndex;
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
/*      */   public String getMethodrefClassName(int index) {
/*  446 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  447 */     if (minfo == null)
/*  448 */       return null; 
/*  449 */     return getClassInfo(minfo.classIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMethodrefNameAndType(int index) {
/*  459 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  460 */     return minfo.nameAndTypeIndex;
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
/*      */   public String getMethodrefName(int index) {
/*  473 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  474 */     if (minfo == null) {
/*  475 */       return null;
/*      */     }
/*  477 */     NameAndTypeInfo n = (NameAndTypeInfo)getItem(minfo.nameAndTypeIndex);
/*  478 */     if (n == null)
/*  479 */       return null; 
/*  480 */     return getUtf8Info(n.memberName);
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
/*      */   public String getMethodrefType(int index) {
/*  493 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  494 */     if (minfo == null) {
/*  495 */       return null;
/*      */     }
/*  497 */     NameAndTypeInfo n = (NameAndTypeInfo)getItem(minfo.nameAndTypeIndex);
/*  498 */     if (n == null)
/*  499 */       return null; 
/*  500 */     return getUtf8Info(n.typeDescriptor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInterfaceMethodrefClass(int index) {
/*  510 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  511 */     return minfo.classIndex;
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
/*      */   public String getInterfaceMethodrefClassName(int index) {
/*  523 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  524 */     return getClassInfo(minfo.classIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInterfaceMethodrefNameAndType(int index) {
/*  534 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  535 */     return minfo.nameAndTypeIndex;
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
/*      */   public String getInterfaceMethodrefName(int index) {
/*  549 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  550 */     if (minfo == null) {
/*  551 */       return null;
/*      */     }
/*  553 */     NameAndTypeInfo n = (NameAndTypeInfo)getItem(minfo.nameAndTypeIndex);
/*  554 */     if (n == null)
/*  555 */       return null; 
/*  556 */     return getUtf8Info(n.memberName);
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
/*      */   public String getInterfaceMethodrefType(int index) {
/*  570 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  571 */     if (minfo == null) {
/*  572 */       return null;
/*      */     }
/*  574 */     NameAndTypeInfo n = (NameAndTypeInfo)getItem(minfo.nameAndTypeIndex);
/*  575 */     if (n == null)
/*  576 */       return null; 
/*  577 */     return getUtf8Info(n.typeDescriptor);
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
/*      */   public Object getLdcValue(int index) {
/*  590 */     ConstInfo constInfo = getItem(index);
/*  591 */     Object value = null;
/*  592 */     if (constInfo instanceof StringInfo) {
/*  593 */       value = getStringInfo(index);
/*  594 */     } else if (constInfo instanceof FloatInfo) {
/*  595 */       value = Float.valueOf(getFloatInfo(index));
/*  596 */     } else if (constInfo instanceof IntegerInfo) {
/*  597 */       value = Integer.valueOf(getIntegerInfo(index));
/*  598 */     } else if (constInfo instanceof LongInfo) {
/*  599 */       value = Long.valueOf(getLongInfo(index));
/*  600 */     } else if (constInfo instanceof DoubleInfo) {
/*  601 */       value = Double.valueOf(getDoubleInfo(index));
/*      */     } 
/*  603 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getIntegerInfo(int index) {
/*  614 */     IntegerInfo i = (IntegerInfo)getItem(index);
/*  615 */     return i.value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloatInfo(int index) {
/*  626 */     FloatInfo i = (FloatInfo)getItem(index);
/*  627 */     return i.value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLongInfo(int index) {
/*  638 */     LongInfo i = (LongInfo)getItem(index);
/*  639 */     return i.value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDoubleInfo(int index) {
/*  650 */     DoubleInfo i = (DoubleInfo)getItem(index);
/*  651 */     return i.value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getStringInfo(int index) {
/*  662 */     StringInfo si = (StringInfo)getItem(index);
/*  663 */     return getUtf8Info(si.string);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUtf8Info(int index) {
/*  674 */     Utf8Info utf = (Utf8Info)getItem(index);
/*  675 */     return utf.string;
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
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMethodHandleKind(int index) {
/*  696 */     MethodHandleInfo mhinfo = (MethodHandleInfo)getItem(index);
/*  697 */     return mhinfo.refKind;
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
/*      */   public int getMethodHandleIndex(int index) {
/*  709 */     MethodHandleInfo mhinfo = (MethodHandleInfo)getItem(index);
/*  710 */     return mhinfo.refIndex;
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
/*      */   public int getMethodTypeInfo(int index) {
/*  722 */     MethodTypeInfo mtinfo = (MethodTypeInfo)getItem(index);
/*  723 */     return mtinfo.descriptor;
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
/*      */   public int getInvokeDynamicBootstrap(int index) {
/*  735 */     InvokeDynamicInfo iv = (InvokeDynamicInfo)getItem(index);
/*  736 */     return iv.bootstrap;
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
/*      */   public int getInvokeDynamicNameAndType(int index) {
/*  748 */     InvokeDynamicInfo iv = (InvokeDynamicInfo)getItem(index);
/*  749 */     return iv.nameAndType;
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
/*      */   public String getInvokeDynamicType(int index) {
/*  763 */     InvokeDynamicInfo iv = (InvokeDynamicInfo)getItem(index);
/*  764 */     if (iv == null)
/*  765 */       return null; 
/*  766 */     NameAndTypeInfo n = (NameAndTypeInfo)getItem(iv.nameAndType);
/*  767 */     if (n == null)
/*  768 */       return null; 
/*  769 */     return getUtf8Info(n.typeDescriptor);
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
/*      */   public int getDynamicBootstrap(int index) {
/*  781 */     DynamicInfo iv = (DynamicInfo)getItem(index);
/*  782 */     return iv.bootstrap;
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
/*      */   public int getDynamicNameAndType(int index) {
/*  794 */     DynamicInfo iv = (DynamicInfo)getItem(index);
/*  795 */     return iv.nameAndType;
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
/*      */   public String getDynamicType(int index) {
/*  809 */     DynamicInfo iv = (DynamicInfo)getItem(index);
/*  810 */     if (iv == null)
/*  811 */       return null; 
/*  812 */     NameAndTypeInfo n = (NameAndTypeInfo)getItem(iv.nameAndType);
/*  813 */     if (n == null)
/*  814 */       return null; 
/*  815 */     return getUtf8Info(n.typeDescriptor);
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
/*      */   public String getModuleInfo(int index) {
/*  827 */     ModuleInfo mi = (ModuleInfo)getItem(index);
/*  828 */     return getUtf8Info(mi.name);
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
/*      */   public String getPackageInfo(int index) {
/*  841 */     PackageInfo mi = (PackageInfo)getItem(index);
/*  842 */     return getUtf8Info(mi.name);
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
/*      */   public int isConstructor(String classname, int index) {
/*  857 */     return isMember(classname, "<init>", index);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public int isMember(String classname, String membername, int index) {
/*  878 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  879 */     if (getClassInfo(minfo.classIndex).equals(classname)) {
/*      */       
/*  881 */       NameAndTypeInfo ntinfo = (NameAndTypeInfo)getItem(minfo.nameAndTypeIndex);
/*  882 */       if (getUtf8Info(ntinfo.memberName).equals(membername)) {
/*  883 */         return ntinfo.typeDescriptor;
/*      */       }
/*      */     } 
/*  886 */     return 0;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String eqMember(String membername, String desc, int index) {
/*  908 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*      */     
/*  910 */     NameAndTypeInfo ntinfo = (NameAndTypeInfo)getItem(minfo.nameAndTypeIndex);
/*  911 */     if (getUtf8Info(ntinfo.memberName).equals(membername) && 
/*  912 */       getUtf8Info(ntinfo.typeDescriptor).equals(desc))
/*  913 */       return getClassInfo(minfo.classIndex); 
/*  914 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private int addItem0(ConstInfo info) {
/*  919 */     this.items.addElement(info);
/*  920 */     return this.numOfItems++;
/*      */   }
/*      */ 
/*      */   
/*      */   private int addItem(ConstInfo info) {
/*  925 */     if (this.itemsCache == null) {
/*  926 */       this.itemsCache = makeItemsCache(this.items);
/*      */     }
/*  928 */     ConstInfo found = this.itemsCache.get(info);
/*  929 */     if (found != null)
/*  930 */       return found.index; 
/*  931 */     this.items.addElement(info);
/*  932 */     this.itemsCache.put(info, info);
/*  933 */     return this.numOfItems++;
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
/*      */   public int copy(int n, ConstPool dest, Map<String, String> classnames) {
/*  949 */     if (n == 0) {
/*  950 */       return 0;
/*      */     }
/*  952 */     ConstInfo info = getItem(n);
/*  953 */     return info.copy(this, dest, classnames);
/*      */   }
/*      */   
/*      */   int addConstInfoPadding() {
/*  957 */     return addItem0(new ConstInfoPadding(this.numOfItems));
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
/*      */   public int addClassInfo(CtClass c) {
/*  970 */     if (c == THIS)
/*  971 */       return this.thisClassInfo; 
/*  972 */     if (!c.isArray()) {
/*  973 */       return addClassInfo(c.getName());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  980 */     return addClassInfo(Descriptor.toJvmName(c));
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
/*      */   public int addClassInfo(String qname) {
/*  996 */     int utf8 = addUtf8Info(Descriptor.toJvmName(qname));
/*  997 */     return addItem(new ClassInfo(utf8, this.numOfItems));
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
/*      */   public int addNameAndTypeInfo(String name, String type) {
/* 1011 */     return addNameAndTypeInfo(addUtf8Info(name), addUtf8Info(type));
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
/*      */   public int addNameAndTypeInfo(int name, int type) {
/* 1023 */     return addItem(new NameAndTypeInfo(name, type, this.numOfItems));
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
/*      */   public int addFieldrefInfo(int classInfo, String name, String type) {
/* 1041 */     int nt = addNameAndTypeInfo(name, type);
/* 1042 */     return addFieldrefInfo(classInfo, nt);
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
/*      */   public int addFieldrefInfo(int classInfo, int nameAndTypeInfo) {
/* 1054 */     return addItem(new FieldrefInfo(classInfo, nameAndTypeInfo, this.numOfItems));
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
/*      */   
/*      */   public int addMethodrefInfo(int classInfo, String name, String type) {
/* 1073 */     int nt = addNameAndTypeInfo(name, type);
/* 1074 */     return addMethodrefInfo(classInfo, nt);
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
/*      */   public int addMethodrefInfo(int classInfo, int nameAndTypeInfo) {
/* 1086 */     return addItem(new MethodrefInfo(classInfo, nameAndTypeInfo, this.numOfItems));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addInterfaceMethodrefInfo(int classInfo, String name, String type) {
/* 1108 */     int nt = addNameAndTypeInfo(name, type);
/* 1109 */     return addInterfaceMethodrefInfo(classInfo, nt);
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
/*      */   public int addInterfaceMethodrefInfo(int classInfo, int nameAndTypeInfo) {
/* 1123 */     return addItem(new InterfaceMethodrefInfo(classInfo, nameAndTypeInfo, this.numOfItems));
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
/*      */   public int addStringInfo(String str) {
/* 1139 */     int utf = addUtf8Info(str);
/* 1140 */     return addItem(new StringInfo(utf, this.numOfItems));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addIntegerInfo(int i) {
/* 1151 */     return addItem(new IntegerInfo(i, this.numOfItems));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addFloatInfo(float f) {
/* 1162 */     return addItem(new FloatInfo(f, this.numOfItems));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addLongInfo(long l) {
/* 1173 */     int i = addItem(new LongInfo(l, this.numOfItems));
/* 1174 */     if (i == this.numOfItems - 1) {
/* 1175 */       addConstInfoPadding();
/*      */     }
/* 1177 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addDoubleInfo(double d) {
/* 1188 */     int i = addItem(new DoubleInfo(d, this.numOfItems));
/* 1189 */     if (i == this.numOfItems - 1) {
/* 1190 */       addConstInfoPadding();
/*      */     }
/* 1192 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addUtf8Info(String utf8) {
/* 1203 */     return addItem(new Utf8Info(utf8, this.numOfItems));
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
/*      */   public int addMethodHandleInfo(int kind, int index) {
/* 1219 */     return addItem(new MethodHandleInfo(kind, index, this.numOfItems));
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
/*      */   public int addMethodTypeInfo(int desc) {
/* 1233 */     return addItem(new MethodTypeInfo(desc, this.numOfItems));
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
/*      */   public int addInvokeDynamicInfo(int bootstrap, int nameAndType) {
/* 1248 */     return addItem(new InvokeDynamicInfo(bootstrap, nameAndType, this.numOfItems));
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
/*      */   public int addDynamicInfo(int bootstrap, int nameAndType) {
/* 1260 */     return addItem(new DynamicInfo(bootstrap, nameAndType, this.numOfItems));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addModuleInfo(int nameIndex) {
/* 1271 */     return addItem(new ModuleInfo(nameIndex, this.numOfItems));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addPackageInfo(int nameIndex) {
/* 1282 */     return addItem(new PackageInfo(nameIndex, this.numOfItems));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<String> getClassNames() {
/* 1292 */     Set<String> result = new HashSet<>();
/* 1293 */     LongVector v = this.items;
/* 1294 */     int size = this.numOfItems;
/* 1295 */     for (int i = 1; i < size; i++) {
/* 1296 */       String className = v.elementAt(i).getClassName(this);
/* 1297 */       if (className != null)
/* 1298 */         result.add(className); 
/*      */     } 
/* 1300 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void renameClass(String oldName, String newName) {
/* 1311 */     LongVector v = this.items;
/* 1312 */     int size = this.numOfItems;
/* 1313 */     for (int i = 1; i < size; i++) {
/* 1314 */       ConstInfo ci = v.elementAt(i);
/* 1315 */       ci.renameClass(this, oldName, newName, this.itemsCache);
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
/*      */   public void renameClass(Map<String, String> classnames) {
/* 1327 */     LongVector v = this.items;
/* 1328 */     int size = this.numOfItems;
/* 1329 */     for (int i = 1; i < size; i++) {
/* 1330 */       ConstInfo ci = v.elementAt(i);
/* 1331 */       ci.renameClass(this, classnames, this.itemsCache);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void read(DataInputStream in) throws IOException {
/* 1337 */     int n = in.readUnsignedShort();
/*      */     
/* 1339 */     this.items = new LongVector(n);
/* 1340 */     this.numOfItems = 0;
/* 1341 */     addItem0(null);
/*      */     
/* 1343 */     while (--n > 0) {
/* 1344 */       int tag = readOne(in);
/* 1345 */       if (tag == 5 || tag == 6) {
/* 1346 */         addConstInfoPadding();
/* 1347 */         n--;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static Map<ConstInfo, ConstInfo> makeItemsCache(LongVector items) {
/* 1354 */     Map<ConstInfo, ConstInfo> cache = new HashMap<>();
/* 1355 */     int i = 1;
/*      */     while (true) {
/* 1357 */       ConstInfo info = items.elementAt(i++);
/* 1358 */       if (info == null)
/*      */         break; 
/* 1360 */       cache.put(info, info);
/*      */     } 
/*      */     
/* 1363 */     return cache;
/*      */   }
/*      */ 
/*      */   
/*      */   private int readOne(DataInputStream in) throws IOException {
/*      */     ConstInfo info;
/* 1369 */     int tag = in.readUnsignedByte();
/* 1370 */     switch (tag) {
/*      */       case 1:
/* 1372 */         info = new Utf8Info(in, this.numOfItems);
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
/* 1427 */         addItem0(info);
/* 1428 */         return tag;case 3: info = new IntegerInfo(in, this.numOfItems); addItem0(info); return tag;case 4: info = new FloatInfo(in, this.numOfItems); addItem0(info); return tag;case 5: info = new LongInfo(in, this.numOfItems); addItem0(info); return tag;case 6: info = new DoubleInfo(in, this.numOfItems); addItem0(info); return tag;case 7: info = new ClassInfo(in, this.numOfItems); addItem0(info); return tag;case 8: info = new StringInfo(in, this.numOfItems); addItem0(info); return tag;case 9: info = new FieldrefInfo(in, this.numOfItems); addItem0(info); return tag;case 10: info = new MethodrefInfo(in, this.numOfItems); addItem0(info); return tag;case 11: info = new InterfaceMethodrefInfo(in, this.numOfItems); addItem0(info); return tag;case 12: info = new NameAndTypeInfo(in, this.numOfItems); addItem0(info); return tag;case 15: info = new MethodHandleInfo(in, this.numOfItems); addItem0(info); return tag;case 16: info = new MethodTypeInfo(in, this.numOfItems); addItem0(info); return tag;case 17: info = new DynamicInfo(in, this.numOfItems); addItem0(info); return tag;case 18: info = new InvokeDynamicInfo(in, this.numOfItems); addItem0(info); return tag;case 19: info = new ModuleInfo(in, this.numOfItems); addItem0(info); return tag;case 20: info = new PackageInfo(in, this.numOfItems); addItem0(info); return tag;
/*      */     } 
/*      */     throw new IOException("invalid constant type: " + tag + " at " + this.numOfItems);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void write(DataOutputStream out) throws IOException {
/* 1436 */     out.writeShort(this.numOfItems);
/* 1437 */     LongVector v = this.items;
/* 1438 */     int size = this.numOfItems;
/* 1439 */     for (int i = 1; i < size; i++) {
/* 1440 */       v.elementAt(i).write(out);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void print() {
/* 1448 */     print(new PrintWriter(System.out, true));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void print(PrintWriter out) {
/* 1456 */     int size = this.numOfItems;
/* 1457 */     for (int i = 1; i < size; i++) {
/* 1458 */       out.print(i);
/* 1459 */       out.print(" ");
/* 1460 */       this.items.elementAt(i).print(out);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\ConstPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */