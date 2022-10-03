/*      */ package javassist;
/*      */ 
/*      */ import java.util.List;
/*      */ import javassist.bytecode.AccessFlag;
/*      */ import javassist.bytecode.AnnotationsAttribute;
/*      */ import javassist.bytecode.AttributeInfo;
/*      */ import javassist.bytecode.Bytecode;
/*      */ import javassist.bytecode.ClassFile;
/*      */ import javassist.bytecode.ConstPool;
/*      */ import javassist.bytecode.Descriptor;
/*      */ import javassist.bytecode.FieldInfo;
/*      */ import javassist.bytecode.SignatureAttribute;
/*      */ import javassist.compiler.CompileError;
/*      */ import javassist.compiler.Javac;
/*      */ import javassist.compiler.SymbolTable;
/*      */ import javassist.compiler.ast.ASTree;
/*      */ import javassist.compiler.ast.DoubleConst;
/*      */ import javassist.compiler.ast.IntConst;
/*      */ import javassist.compiler.ast.StringL;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CtField
/*      */   extends CtMember
/*      */ {
/*      */   static final String javaLangString = "java.lang.String";
/*      */   protected FieldInfo fieldInfo;
/*      */   
/*      */   public CtField(CtClass type, String name, CtClass declaring) throws CannotCompileException {
/*   71 */     this(Descriptor.of(type), name, declaring);
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
/*      */   
/*      */   public CtField(CtField src, CtClass declaring) throws CannotCompileException {
/*   94 */     this(src.fieldInfo.getDescriptor(), src.fieldInfo.getName(), declaring);
/*      */     
/*   96 */     FieldInfo fi = this.fieldInfo;
/*   97 */     fi.setAccessFlags(src.fieldInfo.getAccessFlags());
/*   98 */     ConstPool cp = fi.getConstPool();
/*   99 */     List<AttributeInfo> attributes = src.fieldInfo.getAttributes();
/*  100 */     for (AttributeInfo ainfo : attributes) {
/*  101 */       fi.addAttribute(ainfo.copy(cp, null));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private CtField(String typeDesc, String name, CtClass clazz) throws CannotCompileException {
/*  107 */     super(clazz);
/*  108 */     ClassFile cf = clazz.getClassFile2();
/*  109 */     if (cf == null) {
/*  110 */       throw new CannotCompileException("bad declaring class: " + clazz
/*  111 */           .getName());
/*      */     }
/*  113 */     this.fieldInfo = new FieldInfo(cf.getConstPool(), name, typeDesc);
/*      */   }
/*      */   
/*      */   CtField(FieldInfo fi, CtClass clazz) {
/*  117 */     super(clazz);
/*  118 */     this.fieldInfo = fi;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  126 */     return getDeclaringClass().getName() + "." + getName() + ":" + this.fieldInfo
/*  127 */       .getDescriptor();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void extendToString(StringBuffer buffer) {
/*  132 */     buffer.append(' ');
/*  133 */     buffer.append(getName());
/*  134 */     buffer.append(' ');
/*  135 */     buffer.append(this.fieldInfo.getDescriptor());
/*      */   }
/*      */   
/*      */   protected ASTree getInitAST() {
/*  139 */     return null;
/*      */   }
/*      */   
/*      */   Initializer getInit() {
/*  143 */     ASTree tree = getInitAST();
/*  144 */     if (tree == null)
/*  145 */       return null; 
/*  146 */     return Initializer.byExpr(tree);
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
/*      */   public static CtField make(String src, CtClass declaring) throws CannotCompileException {
/*  166 */     Javac compiler = new Javac(declaring);
/*      */     try {
/*  168 */       CtMember obj = compiler.compile(src);
/*  169 */       if (obj instanceof CtField) {
/*  170 */         return (CtField)obj;
/*      */       }
/*  172 */     } catch (CompileError e) {
/*  173 */       throw new CannotCompileException(e);
/*      */     } 
/*      */     
/*  176 */     throw new CannotCompileException("not a field");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FieldInfo getFieldInfo() {
/*  183 */     this.declaringClass.checkModify();
/*  184 */     return this.fieldInfo;
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
/*      */   public FieldInfo getFieldInfo2() {
/*  206 */     return this.fieldInfo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass getDeclaringClass() {
/*  214 */     return super.getDeclaringClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  222 */     return this.fieldInfo.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String newName) {
/*  229 */     this.declaringClass.checkModify();
/*  230 */     this.fieldInfo.setName(newName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getModifiers() {
/*  240 */     return AccessFlag.toModifier(this.fieldInfo.getAccessFlags());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModifiers(int mod) {
/*  250 */     this.declaringClass.checkModify();
/*  251 */     this.fieldInfo.setAccessFlags(AccessFlag.of(mod));
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
/*      */   public boolean hasAnnotation(String typeName) {
/*  263 */     FieldInfo fi = getFieldInfo2();
/*      */     
/*  265 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)fi.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  267 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)fi.getAttribute("RuntimeVisibleAnnotations");
/*  268 */     return CtClassType.hasAnnotationType(typeName, getDeclaringClass().getClassPool(), ainfo, ainfo2);
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
/*      */   public Object getAnnotation(Class<?> clz) throws ClassNotFoundException {
/*  285 */     FieldInfo fi = getFieldInfo2();
/*      */     
/*  287 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)fi.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  289 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)fi.getAttribute("RuntimeVisibleAnnotations");
/*  290 */     return CtClassType.getAnnotationType(clz, getDeclaringClass().getClassPool(), ainfo, ainfo2);
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
/*      */   public Object[] getAnnotations() throws ClassNotFoundException {
/*  303 */     return getAnnotations(false);
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
/*      */   public Object[] getAvailableAnnotations() {
/*      */     try {
/*  318 */       return getAnnotations(true);
/*      */     }
/*  320 */     catch (ClassNotFoundException e) {
/*  321 */       throw new RuntimeException("Unexpected exception", e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private Object[] getAnnotations(boolean ignoreNotFound) throws ClassNotFoundException {
/*  326 */     FieldInfo fi = getFieldInfo2();
/*      */     
/*  328 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)fi.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  330 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)fi.getAttribute("RuntimeVisibleAnnotations");
/*  331 */     return CtClassType.toAnnotationType(ignoreNotFound, getDeclaringClass().getClassPool(), ainfo, ainfo2);
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
/*      */   public String getSignature() {
/*  351 */     return this.fieldInfo.getDescriptor();
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
/*      */   public String getGenericSignature() {
/*  364 */     SignatureAttribute sa = (SignatureAttribute)this.fieldInfo.getAttribute("Signature");
/*  365 */     return (sa == null) ? null : sa.getSignature();
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
/*      */   public void setGenericSignature(String sig) {
/*  380 */     this.declaringClass.checkModify();
/*  381 */     this.fieldInfo.addAttribute((AttributeInfo)new SignatureAttribute(this.fieldInfo.getConstPool(), sig));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass getType() throws NotFoundException {
/*  388 */     return Descriptor.toCtClass(this.fieldInfo.getDescriptor(), this.declaringClass
/*  389 */         .getClassPool());
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
/*      */   public void setType(CtClass clazz) {
/*  407 */     this.declaringClass.checkModify();
/*  408 */     this.fieldInfo.setDescriptor(Descriptor.of(clazz));
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
/*      */   public Object getConstantValue() {
/*  429 */     int value, index = this.fieldInfo.getConstantValue();
/*  430 */     if (index == 0) {
/*  431 */       return null;
/*      */     }
/*  433 */     ConstPool cp = this.fieldInfo.getConstPool();
/*  434 */     switch (cp.getTag(index)) {
/*      */       case 5:
/*  436 */         return Long.valueOf(cp.getLongInfo(index));
/*      */       case 4:
/*  438 */         return Float.valueOf(cp.getFloatInfo(index));
/*      */       case 6:
/*  440 */         return Double.valueOf(cp.getDoubleInfo(index));
/*      */       case 3:
/*  442 */         value = cp.getIntegerInfo(index);
/*      */         
/*  444 */         if ("Z".equals(this.fieldInfo.getDescriptor()))
/*  445 */           return Boolean.valueOf((value != 0)); 
/*  446 */         return Integer.valueOf(value);
/*      */       case 8:
/*  448 */         return cp.getStringInfo(index);
/*      */     } 
/*  450 */     throw new RuntimeException("bad tag: " + cp.getTag(index) + " at " + index);
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
/*      */   public byte[] getAttribute(String name) {
/*  468 */     AttributeInfo ai = this.fieldInfo.getAttribute(name);
/*  469 */     if (ai == null)
/*  470 */       return null; 
/*  471 */     return ai.get();
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
/*      */   public void setAttribute(String name, byte[] data) {
/*  486 */     this.declaringClass.checkModify();
/*  487 */     this.fieldInfo.addAttribute(new AttributeInfo(this.fieldInfo.getConstPool(), name, data));
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
/*      */   
/*      */   public static abstract class Initializer
/*      */   {
/*      */     public static Initializer constant(int i) {
/*  512 */       return new CtField.IntInitializer(i);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer constant(boolean b) {
/*  520 */       return new CtField.IntInitializer(b ? 1 : 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer constant(long l) {
/*  528 */       return new CtField.LongInitializer(l);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer constant(float l) {
/*  536 */       return new CtField.FloatInitializer(l);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer constant(double d) {
/*  544 */       return new CtField.DoubleInitializer(d);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer constant(String s) {
/*  552 */       return new CtField.StringInitializer(s);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byParameter(int nth) {
/*  570 */       CtField.ParamInitializer i = new CtField.ParamInitializer();
/*  571 */       i.nthParam = nth;
/*  572 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byNew(CtClass objectType) {
/*  590 */       CtField.NewInitializer i = new CtField.NewInitializer();
/*  591 */       i.objectType = objectType;
/*  592 */       i.stringParams = null;
/*  593 */       i.withConstructorParams = false;
/*  594 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byNew(CtClass objectType, String[] stringParams) {
/*  617 */       CtField.NewInitializer i = new CtField.NewInitializer();
/*  618 */       i.objectType = objectType;
/*  619 */       i.stringParams = stringParams;
/*  620 */       i.withConstructorParams = false;
/*  621 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byNewWithParams(CtClass objectType) {
/*  645 */       CtField.NewInitializer i = new CtField.NewInitializer();
/*  646 */       i.objectType = objectType;
/*  647 */       i.stringParams = null;
/*  648 */       i.withConstructorParams = true;
/*  649 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byNewWithParams(CtClass objectType, String[] stringParams) {
/*  675 */       CtField.NewInitializer i = new CtField.NewInitializer();
/*  676 */       i.objectType = objectType;
/*  677 */       i.stringParams = stringParams;
/*  678 */       i.withConstructorParams = true;
/*  679 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byCall(CtClass methodClass, String methodName) {
/*  703 */       CtField.MethodInitializer i = new CtField.MethodInitializer();
/*  704 */       i.objectType = methodClass;
/*  705 */       i.methodName = methodName;
/*  706 */       i.stringParams = null;
/*  707 */       i.withConstructorParams = false;
/*  708 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byCall(CtClass methodClass, String methodName, String[] stringParams) {
/*  737 */       CtField.MethodInitializer i = new CtField.MethodInitializer();
/*  738 */       i.objectType = methodClass;
/*  739 */       i.methodName = methodName;
/*  740 */       i.stringParams = stringParams;
/*  741 */       i.withConstructorParams = false;
/*  742 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byCallWithParams(CtClass methodClass, String methodName) {
/*  769 */       CtField.MethodInitializer i = new CtField.MethodInitializer();
/*  770 */       i.objectType = methodClass;
/*  771 */       i.methodName = methodName;
/*  772 */       i.stringParams = null;
/*  773 */       i.withConstructorParams = true;
/*  774 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byCallWithParams(CtClass methodClass, String methodName, String[] stringParams) {
/*  805 */       CtField.MethodInitializer i = new CtField.MethodInitializer();
/*  806 */       i.objectType = methodClass;
/*  807 */       i.methodName = methodName;
/*  808 */       i.stringParams = stringParams;
/*  809 */       i.withConstructorParams = true;
/*  810 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byNewArray(CtClass type, int size) throws NotFoundException {
/*  824 */       return new CtField.ArrayInitializer(type.getComponentType(), size);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byNewArray(CtClass type, int[] sizes) {
/*  837 */       return new CtField.MultiArrayInitializer(type, sizes);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byExpr(String source) {
/*  846 */       return new CtField.CodeInitializer(source);
/*      */     }
/*      */     
/*      */     static Initializer byExpr(ASTree source) {
/*  850 */       return new CtField.PtreeInitializer(source);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     void check(String desc) throws CannotCompileException {}
/*      */ 
/*      */ 
/*      */     
/*      */     abstract int compile(CtClass param1CtClass, String param1String, Bytecode param1Bytecode, CtClass[] param1ArrayOfCtClass, Javac param1Javac) throws CannotCompileException;
/*      */ 
/*      */ 
/*      */     
/*      */     abstract int compileIfStatic(CtClass param1CtClass, String param1String, Bytecode param1Bytecode, Javac param1Javac) throws CannotCompileException;
/*      */ 
/*      */ 
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/*  868 */       return 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static abstract class CodeInitializer0
/*      */     extends Initializer
/*      */   {
/*      */     abstract void compileExpr(Javac param1Javac) throws CompileError;
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/*      */       try {
/*  880 */         code.addAload(0);
/*  881 */         compileExpr(drv);
/*  882 */         code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/*  883 */         return code.getMaxStack();
/*      */       }
/*  885 */       catch (CompileError e) {
/*  886 */         throw new CannotCompileException(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/*      */       try {
/*  895 */         compileExpr(drv);
/*  896 */         code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/*  897 */         return code.getMaxStack();
/*      */       }
/*  899 */       catch (CompileError e) {
/*  900 */         throw new CannotCompileException(e);
/*      */       } 
/*      */     }
/*      */     
/*      */     int getConstantValue2(ConstPool cp, CtClass type, ASTree tree) {
/*  905 */       if (type.isPrimitive()) {
/*  906 */         if (tree instanceof IntConst) {
/*  907 */           long value = ((IntConst)tree).get();
/*  908 */           if (type == CtClass.doubleType)
/*  909 */             return cp.addDoubleInfo(value); 
/*  910 */           if (type == CtClass.floatType)
/*  911 */             return cp.addFloatInfo((float)value); 
/*  912 */           if (type == CtClass.longType)
/*  913 */             return cp.addLongInfo(value); 
/*  914 */           if (type != CtClass.voidType) {
/*  915 */             return cp.addIntegerInfo((int)value);
/*      */           }
/*  917 */         } else if (tree instanceof DoubleConst) {
/*  918 */           double value = ((DoubleConst)tree).get();
/*  919 */           if (type == CtClass.floatType)
/*  920 */             return cp.addFloatInfo((float)value); 
/*  921 */           if (type == CtClass.doubleType) {
/*  922 */             return cp.addDoubleInfo(value);
/*      */           }
/*      */         } 
/*  925 */       } else if (tree instanceof StringL && type
/*  926 */         .getName().equals("java.lang.String")) {
/*  927 */         return cp.addStringInfo(((StringL)tree).get());
/*      */       } 
/*  929 */       return 0;
/*      */     }
/*      */   }
/*      */   
/*      */   static class CodeInitializer
/*      */     extends CodeInitializer0 {
/*      */     CodeInitializer(String expr) {
/*  936 */       this.expression = expr;
/*      */     }
/*      */     private String expression;
/*      */     void compileExpr(Javac drv) throws CompileError {
/*  940 */       drv.compileExpr(this.expression);
/*      */     }
/*      */ 
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/*      */       try {
/*  946 */         ASTree t = Javac.parseExpr(this.expression, new SymbolTable());
/*  947 */         return getConstantValue2(cp, type, t);
/*      */       }
/*  949 */       catch (CompileError e) {
/*  950 */         return 0;
/*      */       } 
/*      */     } }
/*      */   
/*      */   static class PtreeInitializer extends CodeInitializer0 {
/*      */     private ASTree expression;
/*      */     
/*      */     PtreeInitializer(ASTree expr) {
/*  958 */       this.expression = expr;
/*      */     }
/*      */     
/*      */     void compileExpr(Javac drv) throws CompileError {
/*  962 */       drv.compileExpr(this.expression);
/*      */     }
/*      */ 
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/*  967 */       return getConstantValue2(cp, type, this.expression);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class ParamInitializer
/*      */     extends Initializer
/*      */   {
/*      */     int nthParam;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/*  985 */       if (parameters != null && this.nthParam < parameters.length) {
/*  986 */         code.addAload(0);
/*  987 */         int nth = nthParamToLocal(this.nthParam, parameters, false);
/*  988 */         int s = code.addLoad(nth, type) + 1;
/*  989 */         code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/*  990 */         return s;
/*      */       } 
/*  992 */       return 0;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static int nthParamToLocal(int nth, CtClass[] params, boolean isStatic) {
/*      */       int k;
/* 1005 */       CtClass longType = CtClass.longType;
/* 1006 */       CtClass doubleType = CtClass.doubleType;
/*      */       
/* 1008 */       if (isStatic) {
/* 1009 */         k = 0;
/*      */       } else {
/* 1011 */         k = 1;
/*      */       } 
/* 1013 */       for (int i = 0; i < nth; i++) {
/* 1014 */         CtClass type = params[i];
/* 1015 */         if (type == longType || type == doubleType) {
/* 1016 */           k += 2;
/*      */         } else {
/* 1018 */           k++;
/*      */         } 
/*      */       } 
/* 1021 */       return k;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1028 */       return 0;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class NewInitializer
/*      */     extends Initializer
/*      */   {
/*      */     CtClass objectType;
/*      */ 
/*      */ 
/*      */     
/*      */     String[] stringParams;
/*      */ 
/*      */ 
/*      */     
/*      */     boolean withConstructorParams;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/*      */       int stacksize;
/* 1053 */       code.addAload(0);
/* 1054 */       code.addNew(this.objectType);
/* 1055 */       code.add(89);
/* 1056 */       code.addAload(0);
/*      */       
/* 1058 */       if (this.stringParams == null) {
/* 1059 */         stacksize = 4;
/*      */       } else {
/* 1061 */         stacksize = compileStringParameter(code) + 4;
/*      */       } 
/* 1063 */       if (this.withConstructorParams) {
/* 1064 */         stacksize += CtNewWrappedMethod.compileParameterList(code, parameters, 1);
/*      */       }
/*      */       
/* 1067 */       code.addInvokespecial(this.objectType, "<init>", getDescriptor());
/* 1068 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1069 */       return stacksize;
/*      */     }
/*      */     
/*      */     private String getDescriptor() {
/* 1073 */       String desc3 = "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)V";
/*      */ 
/*      */       
/* 1076 */       if (this.stringParams == null) {
/* 1077 */         if (this.withConstructorParams) {
/* 1078 */           return "(Ljava/lang/Object;[Ljava/lang/Object;)V";
/*      */         }
/* 1080 */         return "(Ljava/lang/Object;)V";
/*      */       } 
/* 1082 */       if (this.withConstructorParams) {
/* 1083 */         return "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)V";
/*      */       }
/* 1085 */       return "(Ljava/lang/Object;[Ljava/lang/String;)V";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/*      */       String desc;
/* 1097 */       code.addNew(this.objectType);
/* 1098 */       code.add(89);
/*      */       
/* 1100 */       int stacksize = 2;
/* 1101 */       if (this.stringParams == null) {
/* 1102 */         desc = "()V";
/*      */       } else {
/* 1104 */         desc = "([Ljava/lang/String;)V";
/* 1105 */         stacksize += compileStringParameter(code);
/*      */       } 
/*      */       
/* 1108 */       code.addInvokespecial(this.objectType, "<init>", desc);
/* 1109 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1110 */       return stacksize;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected final int compileStringParameter(Bytecode code) throws CannotCompileException {
/* 1116 */       int nparam = this.stringParams.length;
/* 1117 */       code.addIconst(nparam);
/* 1118 */       code.addAnewarray("java.lang.String");
/* 1119 */       for (int j = 0; j < nparam; j++) {
/* 1120 */         code.add(89);
/* 1121 */         code.addIconst(j);
/* 1122 */         code.addLdc(this.stringParams[j]);
/* 1123 */         code.add(83);
/*      */       } 
/*      */       
/* 1126 */       return 4;
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
/*      */   static class MethodInitializer
/*      */     extends NewInitializer
/*      */   {
/*      */     String methodName;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/*      */       int stacksize;
/* 1151 */       code.addAload(0);
/* 1152 */       code.addAload(0);
/*      */       
/* 1154 */       if (this.stringParams == null) {
/* 1155 */         stacksize = 2;
/*      */       } else {
/* 1157 */         stacksize = compileStringParameter(code) + 2;
/*      */       } 
/* 1159 */       if (this.withConstructorParams) {
/* 1160 */         stacksize += CtNewWrappedMethod.compileParameterList(code, parameters, 1);
/*      */       }
/*      */       
/* 1163 */       String typeDesc = Descriptor.of(type);
/* 1164 */       String mDesc = getDescriptor() + typeDesc;
/* 1165 */       code.addInvokestatic(this.objectType, this.methodName, mDesc);
/* 1166 */       code.addPutfield(Bytecode.THIS, name, typeDesc);
/* 1167 */       return stacksize;
/*      */     }
/*      */     
/*      */     private String getDescriptor() {
/* 1171 */       String desc3 = "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)";
/*      */ 
/*      */       
/* 1174 */       if (this.stringParams == null) {
/* 1175 */         if (this.withConstructorParams) {
/* 1176 */           return "(Ljava/lang/Object;[Ljava/lang/Object;)";
/*      */         }
/* 1178 */         return "(Ljava/lang/Object;)";
/*      */       } 
/* 1180 */       if (this.withConstructorParams) {
/* 1181 */         return "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)";
/*      */       }
/* 1183 */       return "(Ljava/lang/Object;[Ljava/lang/String;)";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/*      */       String desc;
/* 1195 */       int stacksize = 1;
/* 1196 */       if (this.stringParams == null) {
/* 1197 */         desc = "()";
/*      */       } else {
/* 1199 */         desc = "([Ljava/lang/String;)";
/* 1200 */         stacksize += compileStringParameter(code);
/*      */       } 
/*      */       
/* 1203 */       String typeDesc = Descriptor.of(type);
/* 1204 */       code.addInvokestatic(this.objectType, this.methodName, desc + typeDesc);
/* 1205 */       code.addPutstatic(Bytecode.THIS, name, typeDesc);
/* 1206 */       return stacksize;
/*      */     } }
/*      */   
/*      */   static class IntInitializer extends Initializer {
/*      */     int value;
/*      */     
/*      */     IntInitializer(int v) {
/* 1213 */       this.value = v;
/*      */     }
/*      */     
/*      */     void check(String desc) throws CannotCompileException {
/* 1217 */       char c = desc.charAt(0);
/* 1218 */       if (c != 'I' && c != 'S' && c != 'B' && c != 'C' && c != 'Z') {
/* 1219 */         throw new CannotCompileException("type mismatch");
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/* 1227 */       code.addAload(0);
/* 1228 */       code.addIconst(this.value);
/* 1229 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1230 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1237 */       code.addIconst(this.value);
/* 1238 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1239 */       return 1;
/*      */     }
/*      */ 
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/* 1244 */       return cp.addIntegerInfo(this.value);
/*      */     }
/*      */   }
/*      */   
/*      */   static class LongInitializer extends Initializer { long value;
/*      */     
/*      */     LongInitializer(long v) {
/* 1251 */       this.value = v;
/*      */     }
/*      */     
/*      */     void check(String desc) throws CannotCompileException {
/* 1255 */       if (!desc.equals("J")) {
/* 1256 */         throw new CannotCompileException("type mismatch");
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/* 1264 */       code.addAload(0);
/* 1265 */       code.addLdc2w(this.value);
/* 1266 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1267 */       return 3;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1274 */       code.addLdc2w(this.value);
/* 1275 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1276 */       return 2;
/*      */     }
/*      */ 
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/* 1281 */       if (type == CtClass.longType)
/* 1282 */         return cp.addLongInfo(this.value); 
/* 1283 */       return 0;
/*      */     } }
/*      */   
/*      */   static class FloatInitializer extends Initializer {
/*      */     float value;
/*      */     
/*      */     FloatInitializer(float v) {
/* 1290 */       this.value = v;
/*      */     }
/*      */     
/*      */     void check(String desc) throws CannotCompileException {
/* 1294 */       if (!desc.equals("F")) {
/* 1295 */         throw new CannotCompileException("type mismatch");
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/* 1303 */       code.addAload(0);
/* 1304 */       code.addFconst(this.value);
/* 1305 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1306 */       return 3;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1313 */       code.addFconst(this.value);
/* 1314 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1315 */       return 2;
/*      */     }
/*      */ 
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/* 1320 */       if (type == CtClass.floatType)
/* 1321 */         return cp.addFloatInfo(this.value); 
/* 1322 */       return 0;
/*      */     }
/*      */   }
/*      */   
/*      */   static class DoubleInitializer extends Initializer { double value;
/*      */     
/*      */     DoubleInitializer(double v) {
/* 1329 */       this.value = v;
/*      */     }
/*      */     
/*      */     void check(String desc) throws CannotCompileException {
/* 1333 */       if (!desc.equals("D")) {
/* 1334 */         throw new CannotCompileException("type mismatch");
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/* 1342 */       code.addAload(0);
/* 1343 */       code.addLdc2w(this.value);
/* 1344 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1345 */       return 3;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1352 */       code.addLdc2w(this.value);
/* 1353 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1354 */       return 2;
/*      */     }
/*      */ 
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/* 1359 */       if (type == CtClass.doubleType)
/* 1360 */         return cp.addDoubleInfo(this.value); 
/* 1361 */       return 0;
/*      */     } }
/*      */   
/*      */   static class StringInitializer extends Initializer {
/*      */     String value;
/*      */     
/*      */     StringInitializer(String v) {
/* 1368 */       this.value = v;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/* 1375 */       code.addAload(0);
/* 1376 */       code.addLdc(this.value);
/* 1377 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1378 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1385 */       code.addLdc(this.value);
/* 1386 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1387 */       return 1;
/*      */     }
/*      */ 
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/* 1392 */       if (type.getName().equals("java.lang.String"))
/* 1393 */         return cp.addStringInfo(this.value); 
/* 1394 */       return 0;
/*      */     }
/*      */   }
/*      */   
/*      */   static class ArrayInitializer extends Initializer { CtClass type;
/*      */     int size;
/*      */     
/*      */     ArrayInitializer(CtClass t, int s) {
/* 1402 */       this.type = t; this.size = s;
/*      */     }
/*      */     private void addNewarray(Bytecode code) {
/* 1405 */       if (this.type.isPrimitive()) {
/* 1406 */         code.addNewarray(((CtPrimitiveType)this.type).getArrayType(), this.size);
/*      */       } else {
/*      */         
/* 1409 */         code.addAnewarray(this.type, this.size);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/* 1417 */       code.addAload(0);
/* 1418 */       addNewarray(code);
/* 1419 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1420 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1427 */       addNewarray(code);
/* 1428 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1429 */       return 1;
/*      */     } }
/*      */   
/*      */   static class MultiArrayInitializer extends Initializer {
/*      */     CtClass type;
/*      */     int[] dim;
/*      */     
/*      */     MultiArrayInitializer(CtClass t, int[] d) {
/* 1437 */       this.type = t; this.dim = d;
/*      */     }
/*      */     
/*      */     void check(String desc) throws CannotCompileException {
/* 1441 */       if (desc.charAt(0) != '[') {
/* 1442 */         throw new CannotCompileException("type mismatch");
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/* 1450 */       code.addAload(0);
/* 1451 */       int s = code.addMultiNewarray(type, this.dim);
/* 1452 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1453 */       return s + 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1460 */       int s = code.addMultiNewarray(type, this.dim);
/* 1461 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1462 */       return s;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CtField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */