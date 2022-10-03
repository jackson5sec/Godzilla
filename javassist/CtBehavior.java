/*      */ package javassist;
/*      */ 
/*      */ import javassist.bytecode.AccessFlag;
/*      */ import javassist.bytecode.AnnotationsAttribute;
/*      */ import javassist.bytecode.AttributeInfo;
/*      */ import javassist.bytecode.BadBytecode;
/*      */ import javassist.bytecode.Bytecode;
/*      */ import javassist.bytecode.CodeAttribute;
/*      */ import javassist.bytecode.CodeIterator;
/*      */ import javassist.bytecode.ConstPool;
/*      */ import javassist.bytecode.Descriptor;
/*      */ import javassist.bytecode.ExceptionsAttribute;
/*      */ import javassist.bytecode.LineNumberAttribute;
/*      */ import javassist.bytecode.LocalVariableAttribute;
/*      */ import javassist.bytecode.LocalVariableTypeAttribute;
/*      */ import javassist.bytecode.MethodInfo;
/*      */ import javassist.bytecode.ParameterAnnotationsAttribute;
/*      */ import javassist.bytecode.SignatureAttribute;
/*      */ import javassist.bytecode.StackMap;
/*      */ import javassist.bytecode.StackMapTable;
/*      */ import javassist.compiler.CompileError;
/*      */ import javassist.compiler.Javac;
/*      */ import javassist.expr.ExprEditor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class CtBehavior
/*      */   extends CtMember
/*      */ {
/*      */   protected MethodInfo methodInfo;
/*      */   
/*      */   protected CtBehavior(CtClass clazz, MethodInfo minfo) {
/*   57 */     super(clazz);
/*   58 */     this.methodInfo = minfo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void copy(CtBehavior src, boolean isCons, ClassMap map) throws CannotCompileException {
/*   67 */     CtClass declaring = this.declaringClass;
/*   68 */     MethodInfo srcInfo = src.methodInfo;
/*   69 */     CtClass srcClass = src.getDeclaringClass();
/*   70 */     ConstPool cp = declaring.getClassFile2().getConstPool();
/*      */     
/*   72 */     map = new ClassMap(map);
/*   73 */     map.put(srcClass.getName(), declaring.getName());
/*      */     try {
/*   75 */       boolean patch = false;
/*   76 */       CtClass srcSuper = srcClass.getSuperclass();
/*   77 */       CtClass destSuper = declaring.getSuperclass();
/*   78 */       String destSuperName = null;
/*   79 */       if (srcSuper != null && destSuper != null) {
/*   80 */         String srcSuperName = srcSuper.getName();
/*   81 */         destSuperName = destSuper.getName();
/*   82 */         if (!srcSuperName.equals(destSuperName)) {
/*   83 */           if (srcSuperName.equals("java.lang.Object")) {
/*   84 */             patch = true;
/*      */           } else {
/*   86 */             map.putIfNone(srcSuperName, destSuperName);
/*      */           } 
/*      */         }
/*      */       } 
/*   90 */       this.methodInfo = new MethodInfo(cp, srcInfo.getName(), srcInfo, map);
/*   91 */       if (isCons && patch) {
/*   92 */         this.methodInfo.setSuperclass(destSuperName);
/*      */       }
/*   94 */     } catch (NotFoundException e) {
/*   95 */       throw new CannotCompileException(e);
/*      */     }
/*   97 */     catch (BadBytecode e) {
/*   98 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void extendToString(StringBuffer buffer) {
/*  104 */     buffer.append(' ');
/*  105 */     buffer.append(getName());
/*  106 */     buffer.append(' ');
/*  107 */     buffer.append(this.methodInfo.getDescriptor());
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
/*      */   public abstract String getLongName();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MethodInfo getMethodInfo() {
/*  130 */     this.declaringClass.checkModify();
/*  131 */     return this.methodInfo;
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
/*      */   public MethodInfo getMethodInfo2() {
/*  153 */     return this.methodInfo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getModifiers() {
/*  164 */     return AccessFlag.toModifier(this.methodInfo.getAccessFlags());
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
/*      */   public void setModifiers(int mod) {
/*  178 */     this.declaringClass.checkModify();
/*  179 */     this.methodInfo.setAccessFlags(AccessFlag.of(mod));
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
/*      */   public boolean hasAnnotation(String typeName) {
/*  192 */     MethodInfo mi = getMethodInfo2();
/*      */     
/*  194 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)mi.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  196 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)mi.getAttribute("RuntimeVisibleAnnotations");
/*  197 */     return CtClassType.hasAnnotationType(typeName, 
/*  198 */         getDeclaringClass().getClassPool(), ainfo, ainfo2);
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
/*  215 */     MethodInfo mi = getMethodInfo2();
/*      */     
/*  217 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)mi.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  219 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)mi.getAttribute("RuntimeVisibleAnnotations");
/*  220 */     return CtClassType.getAnnotationType(clz, 
/*  221 */         getDeclaringClass().getClassPool(), ainfo, ainfo2);
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
/*  234 */     return getAnnotations(false);
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
/*  249 */       return getAnnotations(true);
/*      */     }
/*  251 */     catch (ClassNotFoundException e) {
/*  252 */       throw new RuntimeException("Unexpected exception", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Object[] getAnnotations(boolean ignoreNotFound) throws ClassNotFoundException {
/*  259 */     MethodInfo mi = getMethodInfo2();
/*      */     
/*  261 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)mi.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  263 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)mi.getAttribute("RuntimeVisibleAnnotations");
/*  264 */     return CtClassType.toAnnotationType(ignoreNotFound, 
/*  265 */         getDeclaringClass().getClassPool(), ainfo, ainfo2);
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
/*      */   public Object[][] getParameterAnnotations() throws ClassNotFoundException {
/*  281 */     return getParameterAnnotations(false);
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
/*      */   public Object[][] getAvailableParameterAnnotations() {
/*      */     try {
/*  299 */       return getParameterAnnotations(true);
/*      */     }
/*  301 */     catch (ClassNotFoundException e) {
/*  302 */       throw new RuntimeException("Unexpected exception", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   Object[][] getParameterAnnotations(boolean ignoreNotFound) throws ClassNotFoundException {
/*  309 */     MethodInfo mi = getMethodInfo2();
/*      */     
/*  311 */     ParameterAnnotationsAttribute ainfo = (ParameterAnnotationsAttribute)mi.getAttribute("RuntimeInvisibleParameterAnnotations");
/*      */     
/*  313 */     ParameterAnnotationsAttribute ainfo2 = (ParameterAnnotationsAttribute)mi.getAttribute("RuntimeVisibleParameterAnnotations");
/*  314 */     return CtClassType.toAnnotationType(ignoreNotFound, 
/*  315 */         getDeclaringClass().getClassPool(), ainfo, ainfo2, mi);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass[] getParameterTypes() throws NotFoundException {
/*  323 */     return Descriptor.getParameterTypes(this.methodInfo.getDescriptor(), this.declaringClass
/*  324 */         .getClassPool());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   CtClass getReturnType0() throws NotFoundException {
/*  331 */     return Descriptor.getReturnType(this.methodInfo.getDescriptor(), this.declaringClass
/*  332 */         .getClassPool());
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
/*      */   public String getSignature() {
/*  354 */     return this.methodInfo.getDescriptor();
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
/*  367 */     SignatureAttribute sa = (SignatureAttribute)this.methodInfo.getAttribute("Signature");
/*  368 */     return (sa == null) ? null : sa.getSignature();
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
/*  383 */     this.declaringClass.checkModify();
/*  384 */     this.methodInfo.addAttribute((AttributeInfo)new SignatureAttribute(this.methodInfo.getConstPool(), sig));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass[] getExceptionTypes() throws NotFoundException {
/*      */     String[] exceptions;
/*  394 */     ExceptionsAttribute ea = this.methodInfo.getExceptionsAttribute();
/*  395 */     if (ea == null) {
/*  396 */       exceptions = null;
/*      */     } else {
/*  398 */       exceptions = ea.getExceptions();
/*      */     } 
/*  400 */     return this.declaringClass.getClassPool().get(exceptions);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExceptionTypes(CtClass[] types) throws NotFoundException {
/*  407 */     this.declaringClass.checkModify();
/*  408 */     if (types == null || types.length == 0) {
/*  409 */       this.methodInfo.removeExceptionsAttribute();
/*      */       
/*      */       return;
/*      */     } 
/*  413 */     String[] names = new String[types.length];
/*  414 */     for (int i = 0; i < types.length; i++) {
/*  415 */       names[i] = types[i].getName();
/*      */     }
/*  417 */     ExceptionsAttribute ea = this.methodInfo.getExceptionsAttribute();
/*  418 */     if (ea == null) {
/*  419 */       ea = new ExceptionsAttribute(this.methodInfo.getConstPool());
/*  420 */       this.methodInfo.setExceptionsAttribute(ea);
/*      */     } 
/*      */     
/*  423 */     ea.setExceptions(names);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean isEmpty();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBody(String src) throws CannotCompileException {
/*  440 */     setBody(src, (String)null, (String)null);
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
/*      */   public void setBody(String src, String delegateObj, String delegateMethod) throws CannotCompileException {
/*  459 */     CtClass cc = this.declaringClass;
/*  460 */     cc.checkModify();
/*      */     try {
/*  462 */       Javac jv = new Javac(cc);
/*  463 */       if (delegateMethod != null) {
/*  464 */         jv.recordProceed(delegateObj, delegateMethod);
/*      */       }
/*  466 */       Bytecode b = jv.compileBody(this, src);
/*  467 */       this.methodInfo.setCodeAttribute(b.toCodeAttribute());
/*  468 */       this.methodInfo.setAccessFlags(this.methodInfo.getAccessFlags() & 0xFFFFFBFF);
/*      */       
/*  470 */       this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
/*  471 */       this.declaringClass.rebuildClassFile();
/*      */     }
/*  473 */     catch (CompileError e) {
/*  474 */       throw new CannotCompileException(e);
/*  475 */     } catch (BadBytecode e) {
/*  476 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void setBody0(CtClass srcClass, MethodInfo srcInfo, CtClass destClass, MethodInfo destInfo, ClassMap map) throws CannotCompileException {
/*  485 */     destClass.checkModify();
/*      */     
/*  487 */     map = new ClassMap(map);
/*  488 */     map.put(srcClass.getName(), destClass.getName());
/*      */     try {
/*  490 */       CodeAttribute cattr = srcInfo.getCodeAttribute();
/*  491 */       if (cattr != null) {
/*  492 */         ConstPool cp = destInfo.getConstPool();
/*  493 */         CodeAttribute ca = (CodeAttribute)cattr.copy(cp, map);
/*  494 */         destInfo.setCodeAttribute(ca);
/*      */       }
/*      */     
/*      */     }
/*  498 */     catch (javassist.bytecode.CodeAttribute.RuntimeCopyException e) {
/*      */ 
/*      */       
/*  501 */       throw new CannotCompileException(e);
/*      */     } 
/*      */     
/*  504 */     destInfo.setAccessFlags(destInfo.getAccessFlags() & 0xFFFFFBFF);
/*      */     
/*  506 */     destClass.rebuildClassFile();
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
/*      */   public byte[] getAttribute(String name) {
/*  523 */     AttributeInfo ai = this.methodInfo.getAttribute(name);
/*  524 */     if (ai == null)
/*  525 */       return null; 
/*  526 */     return ai.get();
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
/*      */   public void setAttribute(String name, byte[] data) {
/*  542 */     this.declaringClass.checkModify();
/*  543 */     this.methodInfo.addAttribute(new AttributeInfo(this.methodInfo.getConstPool(), name, data));
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
/*      */   public void useCflow(String name) throws CannotCompileException {
/*      */     String fname;
/*  566 */     CtClass cc = this.declaringClass;
/*  567 */     cc.checkModify();
/*  568 */     ClassPool pool = cc.getClassPool();
/*      */     
/*  570 */     int i = 0;
/*      */     while (true) {
/*  572 */       fname = "_cflow$" + i++;
/*      */       try {
/*  574 */         cc.getDeclaredField(fname);
/*      */       }
/*  576 */       catch (NotFoundException e) {
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  581 */     pool.recordCflow(name, this.declaringClass.getName(), fname);
/*      */     try {
/*  583 */       CtClass type = pool.get("javassist.runtime.Cflow");
/*  584 */       CtField field = new CtField(type, fname, cc);
/*  585 */       field.setModifiers(9);
/*  586 */       cc.addField(field, CtField.Initializer.byNew(type));
/*  587 */       insertBefore(fname + ".enter();", false);
/*  588 */       String src = fname + ".exit();";
/*  589 */       insertAfter(src, true);
/*      */     }
/*  591 */     catch (NotFoundException e) {
/*  592 */       throw new CannotCompileException(e);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLocalVariable(String name, CtClass type) throws CannotCompileException {
/*  614 */     this.declaringClass.checkModify();
/*  615 */     ConstPool cp = this.methodInfo.getConstPool();
/*  616 */     CodeAttribute ca = this.methodInfo.getCodeAttribute();
/*  617 */     if (ca == null) {
/*  618 */       throw new CannotCompileException("no method body");
/*      */     }
/*  620 */     LocalVariableAttribute va = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
/*      */     
/*  622 */     if (va == null) {
/*  623 */       va = new LocalVariableAttribute(cp);
/*  624 */       ca.getAttributes().add(va);
/*      */     } 
/*      */     
/*  627 */     int maxLocals = ca.getMaxLocals();
/*  628 */     String desc = Descriptor.of(type);
/*  629 */     va.addEntry(0, ca.getCodeLength(), cp
/*  630 */         .addUtf8Info(name), cp.addUtf8Info(desc), maxLocals);
/*  631 */     ca.setMaxLocals(maxLocals + Descriptor.dataSize(desc));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void insertParameter(CtClass type) throws CannotCompileException {
/*  640 */     this.declaringClass.checkModify();
/*  641 */     String desc = this.methodInfo.getDescriptor();
/*  642 */     String desc2 = Descriptor.insertParameter(type, desc);
/*      */     try {
/*  644 */       addParameter2(Modifier.isStatic(getModifiers()) ? 0 : 1, type, desc);
/*      */     }
/*  646 */     catch (BadBytecode e) {
/*  647 */       throw new CannotCompileException(e);
/*      */     } 
/*      */     
/*  650 */     this.methodInfo.setDescriptor(desc2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addParameter(CtClass type) throws CannotCompileException {
/*  659 */     this.declaringClass.checkModify();
/*  660 */     String desc = this.methodInfo.getDescriptor();
/*  661 */     String desc2 = Descriptor.appendParameter(type, desc);
/*  662 */     int offset = Modifier.isStatic(getModifiers()) ? 0 : 1;
/*      */     try {
/*  664 */       addParameter2(offset + Descriptor.paramSize(desc), type, desc);
/*      */     }
/*  666 */     catch (BadBytecode e) {
/*  667 */       throw new CannotCompileException(e);
/*      */     } 
/*      */     
/*  670 */     this.methodInfo.setDescriptor(desc2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addParameter2(int where, CtClass type, String desc) throws BadBytecode {
/*  676 */     CodeAttribute ca = this.methodInfo.getCodeAttribute();
/*  677 */     if (ca != null) {
/*  678 */       int size = 1;
/*  679 */       char typeDesc = 'L';
/*  680 */       int classInfo = 0;
/*  681 */       if (type.isPrimitive()) {
/*  682 */         CtPrimitiveType cpt = (CtPrimitiveType)type;
/*  683 */         size = cpt.getDataSize();
/*  684 */         typeDesc = cpt.getDescriptor();
/*      */       } else {
/*      */         
/*  687 */         classInfo = this.methodInfo.getConstPool().addClassInfo(type);
/*      */       } 
/*  689 */       ca.insertLocalVar(where, size);
/*      */       
/*  691 */       LocalVariableAttribute va = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
/*  692 */       if (va != null) {
/*  693 */         va.shiftIndex(where, size);
/*      */       }
/*      */       
/*  696 */       LocalVariableTypeAttribute lvta = (LocalVariableTypeAttribute)ca.getAttribute("LocalVariableTypeTable");
/*  697 */       if (lvta != null) {
/*  698 */         lvta.shiftIndex(where, size);
/*      */       }
/*  700 */       StackMapTable smt = (StackMapTable)ca.getAttribute("StackMapTable");
/*  701 */       if (smt != null) {
/*  702 */         smt.insertLocal(where, StackMapTable.typeTagOf(typeDesc), classInfo);
/*      */       }
/*  704 */       StackMap sm = (StackMap)ca.getAttribute("StackMap");
/*  705 */       if (sm != null) {
/*  706 */         sm.insertLocal(where, StackMapTable.typeTagOf(typeDesc), classInfo);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void instrument(CodeConverter converter) throws CannotCompileException {
/*  718 */     this.declaringClass.checkModify();
/*  719 */     ConstPool cp = this.methodInfo.getConstPool();
/*  720 */     converter.doit(getDeclaringClass(), this.methodInfo, cp);
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
/*      */   public void instrument(ExprEditor editor) throws CannotCompileException {
/*  741 */     if (this.declaringClass.isFrozen()) {
/*  742 */       this.declaringClass.checkModify();
/*      */     }
/*  744 */     if (editor.doit(this.declaringClass, this.methodInfo)) {
/*  745 */       this.declaringClass.checkModify();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void insertBefore(String src) throws CannotCompileException {
/*  766 */     insertBefore(src, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void insertBefore(String src, boolean rebuild) throws CannotCompileException {
/*  772 */     CtClass cc = this.declaringClass;
/*  773 */     cc.checkModify();
/*  774 */     CodeAttribute ca = this.methodInfo.getCodeAttribute();
/*  775 */     if (ca == null) {
/*  776 */       throw new CannotCompileException("no method body");
/*      */     }
/*  778 */     CodeIterator iterator = ca.iterator();
/*  779 */     Javac jv = new Javac(cc);
/*      */     try {
/*  781 */       int nvars = jv.recordParams(getParameterTypes(), 
/*  782 */           Modifier.isStatic(getModifiers()));
/*  783 */       jv.recordParamNames(ca, nvars);
/*  784 */       jv.recordLocalVariables(ca, 0);
/*  785 */       jv.recordReturnType(getReturnType0(), false);
/*  786 */       jv.compileStmnt(src);
/*  787 */       Bytecode b = jv.getBytecode();
/*  788 */       int stack = b.getMaxStack();
/*  789 */       int locals = b.getMaxLocals();
/*      */       
/*  791 */       if (stack > ca.getMaxStack()) {
/*  792 */         ca.setMaxStack(stack);
/*      */       }
/*  794 */       if (locals > ca.getMaxLocals()) {
/*  795 */         ca.setMaxLocals(locals);
/*      */       }
/*  797 */       int pos = iterator.insertEx(b.get());
/*  798 */       iterator.insert(b.getExceptionTable(), pos);
/*  799 */       if (rebuild) {
/*  800 */         this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
/*      */       }
/*  802 */     } catch (NotFoundException e) {
/*  803 */       throw new CannotCompileException(e);
/*      */     }
/*  805 */     catch (CompileError e) {
/*  806 */       throw new CannotCompileException(e);
/*      */     }
/*  808 */     catch (BadBytecode e) {
/*  809 */       throw new CannotCompileException(e);
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
/*      */ 
/*      */   
/*      */   public void insertAfter(String src) throws CannotCompileException {
/*  824 */     insertAfter(src, false, false);
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
/*      */   public void insertAfter(String src, boolean asFinally) throws CannotCompileException {
/*  842 */     insertAfter(src, asFinally, false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void insertAfter(String src, boolean asFinally, boolean redundant) throws CannotCompileException {
/*  871 */     CtClass cc = this.declaringClass;
/*  872 */     cc.checkModify();
/*  873 */     ConstPool pool = this.methodInfo.getConstPool();
/*  874 */     CodeAttribute ca = this.methodInfo.getCodeAttribute();
/*  875 */     if (ca == null) {
/*  876 */       throw new CannotCompileException("no method body");
/*      */     }
/*  878 */     CodeIterator iterator = ca.iterator();
/*  879 */     int retAddr = ca.getMaxLocals();
/*  880 */     Bytecode b = new Bytecode(pool, 0, retAddr + 1);
/*  881 */     b.setStackDepth(ca.getMaxStack() + 1);
/*  882 */     Javac jv = new Javac(b, cc);
/*      */     try {
/*  884 */       int nvars = jv.recordParams(getParameterTypes(), 
/*  885 */           Modifier.isStatic(getModifiers()));
/*  886 */       jv.recordParamNames(ca, nvars);
/*  887 */       CtClass rtype = getReturnType0();
/*  888 */       int varNo = jv.recordReturnType(rtype, true);
/*  889 */       jv.recordLocalVariables(ca, 0);
/*      */ 
/*      */       
/*  892 */       int handlerLen = insertAfterHandler(asFinally, b, rtype, varNo, jv, src);
/*      */       
/*  894 */       int handlerPos = iterator.getCodeLength();
/*  895 */       if (asFinally) {
/*  896 */         ca.getExceptionTable().add(getStartPosOfBody(ca), handlerPos, handlerPos, 0);
/*      */       }
/*  898 */       int adviceLen = 0;
/*  899 */       int advicePos = 0;
/*  900 */       boolean noReturn = true;
/*  901 */       while (iterator.hasNext()) {
/*  902 */         int pos = iterator.next();
/*  903 */         if (pos >= handlerPos) {
/*      */           break;
/*      */         }
/*  906 */         int c = iterator.byteAt(pos);
/*  907 */         if (c == 176 || c == 172 || c == 174 || c == 173 || c == 175 || c == 177) {
/*      */ 
/*      */           
/*  910 */           if (redundant) {
/*  911 */             Bytecode bcode; Javac jvc; int retVarNo; iterator.setMark2(handlerPos);
/*      */ 
/*      */ 
/*      */             
/*  915 */             if (noReturn) {
/*  916 */               noReturn = false;
/*  917 */               bcode = b;
/*  918 */               jvc = jv;
/*  919 */               retVarNo = varNo;
/*      */             } else {
/*      */               
/*  922 */               bcode = new Bytecode(pool, 0, retAddr + 1);
/*  923 */               bcode.setStackDepth(ca.getMaxStack() + 1);
/*  924 */               jvc = new Javac(bcode, cc);
/*  925 */               int nvars2 = jvc.recordParams(getParameterTypes(), 
/*  926 */                   Modifier.isStatic(getModifiers()));
/*  927 */               jvc.recordParamNames(ca, nvars2);
/*  928 */               retVarNo = jvc.recordReturnType(rtype, true);
/*  929 */               jvc.recordLocalVariables(ca, 0);
/*      */             } 
/*      */             
/*  932 */             int adviceLen2 = insertAfterAdvice(bcode, jvc, src, pool, rtype, retVarNo);
/*  933 */             int offset = iterator.append(bcode.get());
/*  934 */             iterator.append(bcode.getExceptionTable(), offset);
/*  935 */             int advicePos2 = iterator.getCodeLength() - adviceLen2;
/*  936 */             insertGoto(iterator, advicePos2, pos);
/*  937 */             handlerPos = iterator.getMark2();
/*      */             continue;
/*      */           } 
/*  940 */           if (noReturn) {
/*      */             
/*  942 */             adviceLen = insertAfterAdvice(b, jv, src, pool, rtype, varNo);
/*  943 */             handlerPos = iterator.append(b.get());
/*  944 */             iterator.append(b.getExceptionTable(), handlerPos);
/*  945 */             advicePos = iterator.getCodeLength() - adviceLen;
/*  946 */             handlerLen = advicePos - handlerPos;
/*  947 */             noReturn = false;
/*      */           } 
/*      */           
/*  950 */           insertGoto(iterator, advicePos, pos);
/*  951 */           advicePos = iterator.getCodeLength() - adviceLen;
/*  952 */           handlerPos = advicePos - handlerLen;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  957 */       if (noReturn) {
/*  958 */         handlerPos = iterator.append(b.get());
/*  959 */         iterator.append(b.getExceptionTable(), handlerPos);
/*      */       } 
/*      */       
/*  962 */       ca.setMaxStack(b.getMaxStack());
/*  963 */       ca.setMaxLocals(b.getMaxLocals());
/*  964 */       this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
/*      */     }
/*  966 */     catch (NotFoundException e) {
/*  967 */       throw new CannotCompileException(e);
/*      */     }
/*  969 */     catch (CompileError e) {
/*  970 */       throw new CannotCompileException(e);
/*      */     }
/*  972 */     catch (BadBytecode e) {
/*  973 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int insertAfterAdvice(Bytecode code, Javac jv, String src, ConstPool cp, CtClass rtype, int varNo) throws CompileError {
/*  981 */     int pc = code.currentPc();
/*  982 */     if (rtype == CtClass.voidType) {
/*  983 */       code.addOpcode(1);
/*  984 */       code.addAstore(varNo);
/*  985 */       jv.compileStmnt(src);
/*  986 */       code.addOpcode(177);
/*  987 */       if (code.getMaxLocals() < 1) {
/*  988 */         code.setMaxLocals(1);
/*      */       }
/*      */     } else {
/*  991 */       code.addStore(varNo, rtype);
/*  992 */       jv.compileStmnt(src);
/*  993 */       code.addLoad(varNo, rtype);
/*  994 */       if (rtype.isPrimitive()) {
/*  995 */         code.addOpcode(((CtPrimitiveType)rtype).getReturnOp());
/*      */       } else {
/*  997 */         code.addOpcode(176);
/*      */       } 
/*      */     } 
/* 1000 */     return code.currentPc() - pc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void insertGoto(CodeIterator iterator, int subr, int pos) throws BadBytecode {
/* 1009 */     iterator.setMark(subr);
/*      */     
/* 1011 */     iterator.writeByte(0, pos);
/* 1012 */     boolean wide = (subr + 2 - pos > 32767);
/* 1013 */     int len = wide ? 4 : 2;
/* 1014 */     CodeIterator.Gap gap = iterator.insertGapAt(pos, len, false);
/* 1015 */     pos = gap.position + gap.length - len;
/* 1016 */     int offset = iterator.getMark() - pos;
/* 1017 */     if (wide) {
/* 1018 */       iterator.writeByte(200, pos);
/* 1019 */       iterator.write32bit(offset, pos + 1);
/*      */     }
/* 1021 */     else if (offset <= 32767) {
/* 1022 */       iterator.writeByte(167, pos);
/* 1023 */       iterator.write16bit(offset, pos + 1);
/*      */     } else {
/*      */       
/* 1026 */       if (gap.length < 4) {
/* 1027 */         CodeIterator.Gap gap2 = iterator.insertGapAt(gap.position, 2, false);
/* 1028 */         pos = gap2.position + gap2.length + gap.length - 4;
/*      */       } 
/*      */       
/* 1031 */       iterator.writeByte(200, pos);
/* 1032 */       iterator.write32bit(iterator.getMark() - pos, pos + 1);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int insertAfterHandler(boolean asFinally, Bytecode b, CtClass rtype, int returnVarNo, Javac javac, String src) throws CompileError {
/* 1043 */     if (!asFinally) {
/* 1044 */       return 0;
/*      */     }
/* 1046 */     int var = b.getMaxLocals();
/* 1047 */     b.incMaxLocals(1);
/* 1048 */     int pc = b.currentPc();
/* 1049 */     b.addAstore(var);
/* 1050 */     if (rtype.isPrimitive()) {
/* 1051 */       char c = ((CtPrimitiveType)rtype).getDescriptor();
/* 1052 */       if (c == 'D') {
/* 1053 */         b.addDconst(0.0D);
/* 1054 */         b.addDstore(returnVarNo);
/*      */       }
/* 1056 */       else if (c == 'F') {
/* 1057 */         b.addFconst(0.0F);
/* 1058 */         b.addFstore(returnVarNo);
/*      */       }
/* 1060 */       else if (c == 'J') {
/* 1061 */         b.addLconst(0L);
/* 1062 */         b.addLstore(returnVarNo);
/*      */       }
/* 1064 */       else if (c == 'V') {
/* 1065 */         b.addOpcode(1);
/* 1066 */         b.addAstore(returnVarNo);
/*      */       } else {
/*      */         
/* 1069 */         b.addIconst(0);
/* 1070 */         b.addIstore(returnVarNo);
/*      */       } 
/*      */     } else {
/*      */       
/* 1074 */       b.addOpcode(1);
/* 1075 */       b.addAstore(returnVarNo);
/*      */     } 
/*      */     
/* 1078 */     javac.compileStmnt(src);
/* 1079 */     b.addAload(var);
/* 1080 */     b.addOpcode(191);
/* 1081 */     return b.currentPc() - pc;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCatch(String src, CtClass exceptionType) throws CannotCompileException {
/* 1147 */     addCatch(src, exceptionType, "$e");
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
/*      */   public void addCatch(String src, CtClass exceptionType, String exceptionName) throws CannotCompileException {
/* 1166 */     CtClass cc = this.declaringClass;
/* 1167 */     cc.checkModify();
/* 1168 */     ConstPool cp = this.methodInfo.getConstPool();
/* 1169 */     CodeAttribute ca = this.methodInfo.getCodeAttribute();
/* 1170 */     CodeIterator iterator = ca.iterator();
/* 1171 */     Bytecode b = new Bytecode(cp, ca.getMaxStack(), ca.getMaxLocals());
/* 1172 */     b.setStackDepth(1);
/* 1173 */     Javac jv = new Javac(b, cc);
/*      */     try {
/* 1175 */       jv.recordParams(getParameterTypes(), 
/* 1176 */           Modifier.isStatic(getModifiers()));
/* 1177 */       int var = jv.recordVariable(exceptionType, exceptionName);
/* 1178 */       b.addAstore(var);
/* 1179 */       jv.compileStmnt(src);
/*      */       
/* 1181 */       int stack = b.getMaxStack();
/* 1182 */       int locals = b.getMaxLocals();
/*      */       
/* 1184 */       if (stack > ca.getMaxStack()) {
/* 1185 */         ca.setMaxStack(stack);
/*      */       }
/* 1187 */       if (locals > ca.getMaxLocals()) {
/* 1188 */         ca.setMaxLocals(locals);
/*      */       }
/* 1190 */       int len = iterator.getCodeLength();
/* 1191 */       int pos = iterator.append(b.get());
/* 1192 */       ca.getExceptionTable().add(getStartPosOfBody(ca), len, len, cp
/* 1193 */           .addClassInfo(exceptionType));
/* 1194 */       iterator.append(b.getExceptionTable(), pos);
/* 1195 */       this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
/*      */     }
/* 1197 */     catch (NotFoundException e) {
/* 1198 */       throw new CannotCompileException(e);
/*      */     }
/* 1200 */     catch (CompileError e) {
/* 1201 */       throw new CannotCompileException(e);
/* 1202 */     } catch (BadBytecode e) {
/* 1203 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   int getStartPosOfBody(CodeAttribute ca) throws CannotCompileException {
/* 1210 */     return 0;
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
/*      */   public int insertAt(int lineNum, String src) throws CannotCompileException {
/* 1233 */     return insertAt(lineNum, true, src);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int insertAt(int lineNum, boolean modify, String src) throws CannotCompileException {
/* 1261 */     CodeAttribute ca = this.methodInfo.getCodeAttribute();
/* 1262 */     if (ca == null) {
/* 1263 */       throw new CannotCompileException("no method body");
/*      */     }
/*      */     
/* 1266 */     LineNumberAttribute ainfo = (LineNumberAttribute)ca.getAttribute("LineNumberTable");
/* 1267 */     if (ainfo == null) {
/* 1268 */       throw new CannotCompileException("no line number info");
/*      */     }
/* 1270 */     LineNumberAttribute.Pc pc = ainfo.toNearPc(lineNum);
/* 1271 */     lineNum = pc.line;
/* 1272 */     int index = pc.index;
/* 1273 */     if (!modify) {
/* 1274 */       return lineNum;
/*      */     }
/* 1276 */     CtClass cc = this.declaringClass;
/* 1277 */     cc.checkModify();
/* 1278 */     CodeIterator iterator = ca.iterator();
/* 1279 */     Javac jv = new Javac(cc);
/*      */     try {
/* 1281 */       jv.recordLocalVariables(ca, index);
/* 1282 */       jv.recordParams(getParameterTypes(), 
/* 1283 */           Modifier.isStatic(getModifiers()));
/* 1284 */       jv.setMaxLocals(ca.getMaxLocals());
/* 1285 */       jv.compileStmnt(src);
/* 1286 */       Bytecode b = jv.getBytecode();
/* 1287 */       int locals = b.getMaxLocals();
/* 1288 */       int stack = b.getMaxStack();
/* 1289 */       ca.setMaxLocals(locals);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1294 */       if (stack > ca.getMaxStack()) {
/* 1295 */         ca.setMaxStack(stack);
/*      */       }
/* 1297 */       index = iterator.insertAt(index, b.get());
/* 1298 */       iterator.insert(b.getExceptionTable(), index);
/* 1299 */       this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
/* 1300 */       return lineNum;
/*      */     }
/* 1302 */     catch (NotFoundException e) {
/* 1303 */       throw new CannotCompileException(e);
/*      */     }
/* 1305 */     catch (CompileError e) {
/* 1306 */       throw new CannotCompileException(e);
/*      */     }
/* 1308 */     catch (BadBytecode e) {
/* 1309 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CtBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */