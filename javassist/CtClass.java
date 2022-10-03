/*      */ package javassist;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.lang.invoke.MethodHandles;
/*      */ import java.net.URL;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.Collection;
/*      */ import javassist.bytecode.ClassFile;
/*      */ import javassist.bytecode.Descriptor;
/*      */ import javassist.compiler.AccessorMaker;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class CtClass
/*      */ {
/*      */   protected String qualifiedName;
/*   67 */   public static String debugDump = null;
/*      */   
/*      */   public static final String version = "3.28.0-GA";
/*      */   static final String javaLangObject = "java.lang.Object";
/*      */   public static CtClass booleanType;
/*      */   public static CtClass charType;
/*      */   public static CtClass byteType;
/*      */   public static CtClass shortType;
/*      */   public static CtClass intType;
/*      */   public static CtClass longType;
/*      */   public static CtClass floatType;
/*      */   public static CtClass doubleType;
/*      */   public static CtClass voidType;
/*      */   
/*      */   public static void main(String[] args) {
/*   82 */     System.out.println("Javassist version 3.28.0-GA");
/*   83 */     System.out.println("Copyright (C) 1999-2021 Shigeru Chiba. All Rights Reserved.");
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
/*  146 */   static CtClass[] primitiveTypes = new CtClass[9];
/*      */   static {
/*  148 */     booleanType = new CtPrimitiveType("boolean", 'Z', "java.lang.Boolean", "booleanValue", "()Z", 172, 4, 1);
/*      */ 
/*      */ 
/*      */     
/*  152 */     primitiveTypes[0] = booleanType;
/*      */     
/*  154 */     charType = new CtPrimitiveType("char", 'C', "java.lang.Character", "charValue", "()C", 172, 5, 1);
/*      */ 
/*      */     
/*  157 */     primitiveTypes[1] = charType;
/*      */     
/*  159 */     byteType = new CtPrimitiveType("byte", 'B', "java.lang.Byte", "byteValue", "()B", 172, 8, 1);
/*      */ 
/*      */     
/*  162 */     primitiveTypes[2] = byteType;
/*      */     
/*  164 */     shortType = new CtPrimitiveType("short", 'S', "java.lang.Short", "shortValue", "()S", 172, 9, 1);
/*      */ 
/*      */     
/*  167 */     primitiveTypes[3] = shortType;
/*      */     
/*  169 */     intType = new CtPrimitiveType("int", 'I', "java.lang.Integer", "intValue", "()I", 172, 10, 1);
/*      */ 
/*      */     
/*  172 */     primitiveTypes[4] = intType;
/*      */     
/*  174 */     longType = new CtPrimitiveType("long", 'J', "java.lang.Long", "longValue", "()J", 173, 11, 2);
/*      */ 
/*      */     
/*  177 */     primitiveTypes[5] = longType;
/*      */     
/*  179 */     floatType = new CtPrimitiveType("float", 'F', "java.lang.Float", "floatValue", "()F", 174, 6, 1);
/*      */ 
/*      */     
/*  182 */     primitiveTypes[6] = floatType;
/*      */     
/*  184 */     doubleType = new CtPrimitiveType("double", 'D', "java.lang.Double", "doubleValue", "()D", 175, 7, 2);
/*      */ 
/*      */     
/*  187 */     primitiveTypes[7] = doubleType;
/*      */     
/*  189 */     voidType = new CtPrimitiveType("void", 'V', "java.lang.Void", null, null, 177, 0, 0);
/*      */     
/*  191 */     primitiveTypes[8] = voidType;
/*      */   }
/*      */   
/*      */   protected CtClass(String name) {
/*  195 */     this.qualifiedName = name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  203 */     StringBuffer buf = new StringBuffer(getClass().getName());
/*  204 */     buf.append("@");
/*  205 */     buf.append(Integer.toHexString(hashCode()));
/*  206 */     buf.append("[");
/*  207 */     extendToString(buf);
/*  208 */     buf.append("]");
/*  209 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void extendToString(StringBuffer buffer) {
/*  217 */     buffer.append(getName());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassPool getClassPool() {
/*  223 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassFile getClassFile() {
/*  232 */     checkModify();
/*  233 */     return getClassFile2();
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
/*      */   public ClassFile getClassFile2() {
/*  254 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public AccessorMaker getAccessorMaker() {
/*  260 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URL getURL() throws NotFoundException {
/*  267 */     throw new NotFoundException(getName());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isModified() {
/*  273 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFrozen() {
/*  282 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void freeze() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void checkModify() throws RuntimeException {
/*  296 */     if (isFrozen()) {
/*  297 */       throw new RuntimeException(getName() + " class is frozen");
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
/*      */   public void defrost() {
/*  318 */     throw new RuntimeException("cannot defrost " + getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPrimitive() {
/*  326 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isArray() {
/*  332 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isKotlin() {
/*  340 */     return hasAnnotation("kotlin.Metadata");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass getComponentType() throws NotFoundException {
/*  348 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean subtypeOf(CtClass clazz) throws NotFoundException {
/*  357 */     return (this == clazz || getName().equals(clazz.getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  363 */     return this.qualifiedName;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getSimpleName() {
/*  369 */     String qname = this.qualifiedName;
/*  370 */     int index = qname.lastIndexOf('.');
/*  371 */     if (index < 0)
/*  372 */       return qname; 
/*  373 */     return qname.substring(index + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getPackageName() {
/*  380 */     String qname = this.qualifiedName;
/*  381 */     int index = qname.lastIndexOf('.');
/*  382 */     if (index < 0)
/*  383 */       return null; 
/*  384 */     return qname.substring(0, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String name) {
/*  393 */     checkModify();
/*  394 */     if (name != null) {
/*  395 */       this.qualifiedName = name;
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
/*      */   public String getGenericSignature() {
/*  412 */     return null;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGenericSignature(String sig) {
/*  484 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void replaceClassName(String oldName, String newName) {
/*  494 */     checkModify();
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
/*      */   public void replaceClassName(ClassMap map) {
/*  515 */     checkModify();
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
/*      */   public synchronized Collection<String> getRefClasses() {
/*  528 */     ClassFile cf = getClassFile2();
/*  529 */     if (cf != null) {
/*  530 */       ClassMap cm = new ClassMap()
/*      */         {
/*      */           private static final long serialVersionUID = 1L;
/*      */           
/*      */           public String put(String oldname, String newname) {
/*  535 */             return put0(oldname, newname);
/*      */           }
/*      */           
/*      */           public String get(Object jvmClassName) {
/*  539 */             String n = toJavaName((String)jvmClassName);
/*  540 */             put0(n, n);
/*  541 */             return null;
/*      */           }
/*      */ 
/*      */           
/*      */           public void fix(String name) {}
/*      */         };
/*  547 */       cf.getRefClasses(cm);
/*  548 */       return cm.values();
/*      */     } 
/*  550 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInterface() {
/*  558 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAnnotation() {
/*  568 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnum() {
/*  578 */     return false;
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
/*      */   public int getModifiers() {
/*  591 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasAnnotation(Class<?> annotationType) {
/*  602 */     return hasAnnotation(annotationType.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasAnnotation(String annotationTypeName) {
/*  613 */     return false;
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
/*      */   public Object getAnnotation(Class<?> clz) throws ClassNotFoundException {
/*  628 */     return null;
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
/*      */   public Object[] getAnnotations() throws ClassNotFoundException {
/*  643 */     return new Object[0];
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
/*      */   public Object[] getAvailableAnnotations() {
/*  658 */     return new Object[0];
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
/*      */   public CtClass[] getDeclaredClasses() throws NotFoundException {
/*  670 */     return getNestedClasses();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass[] getNestedClasses() throws NotFoundException {
/*  681 */     return new CtClass[0];
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
/*      */   public void setModifiers(int mod) {
/*  696 */     checkModify();
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
/*      */   public boolean subclassOf(CtClass superclass) {
/*  708 */     return false;
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
/*      */   public CtClass getSuperclass() throws NotFoundException {
/*  724 */     return null;
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
/*      */   public void setSuperclass(CtClass clazz) throws CannotCompileException {
/*  741 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass[] getInterfaces() throws NotFoundException {
/*  750 */     return new CtClass[0];
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
/*      */   public void setInterfaces(CtClass[] list) {
/*  763 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInterface(CtClass anInterface) {
/*  772 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass getDeclaringClass() throws NotFoundException {
/*  782 */     return null;
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
/*      */   @Deprecated
/*      */   public final CtMethod getEnclosingMethod() throws NotFoundException {
/*  797 */     CtBehavior b = getEnclosingBehavior();
/*  798 */     if (b == null)
/*  799 */       return null; 
/*  800 */     if (b instanceof CtMethod) {
/*  801 */       return (CtMethod)b;
/*      */     }
/*  803 */     throw new NotFoundException(b.getLongName() + " is enclosing " + getName());
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
/*      */   public CtBehavior getEnclosingBehavior() throws NotFoundException {
/*  815 */     return null;
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
/*      */   public CtClass makeNestedClass(String name, boolean isStatic) {
/*  830 */     throw new RuntimeException(getName() + " is not a class");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtField[] getFields() {
/*  839 */     return new CtField[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtField getField(String name) throws NotFoundException {
/*  846 */     return getField(name, null);
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
/*      */   public CtField getField(String name, String desc) throws NotFoundException {
/*  861 */     throw new NotFoundException(name);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   CtField getField2(String name, String desc) {
/*  867 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtField[] getDeclaredFields() {
/*  875 */     return new CtField[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtField getDeclaredField(String name) throws NotFoundException {
/*  884 */     throw new NotFoundException(name);
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
/*      */   public CtField getDeclaredField(String name, String desc) throws NotFoundException {
/*  900 */     throw new NotFoundException(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtBehavior[] getDeclaredBehaviors() {
/*  907 */     return new CtBehavior[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtConstructor[] getConstructors() {
/*  915 */     return new CtConstructor[0];
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
/*      */   public CtConstructor getConstructor(String desc) throws NotFoundException {
/*  931 */     throw new NotFoundException("no such constructor");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtConstructor[] getDeclaredConstructors() {
/*  940 */     return new CtConstructor[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtConstructor getDeclaredConstructor(CtClass[] params) throws NotFoundException {
/*  951 */     String desc = Descriptor.ofConstructor(params);
/*  952 */     return getConstructor(desc);
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
/*      */   public CtConstructor getClassInitializer() {
/*  965 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtMethod[] getMethods() {
/*  975 */     return new CtMethod[0];
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
/*      */   public CtMethod getMethod(String name, String desc) throws NotFoundException {
/*  993 */     throw new NotFoundException(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtMethod[] getDeclaredMethods() {
/* 1003 */     return new CtMethod[0];
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
/*      */   public CtMethod getDeclaredMethod(String name, CtClass[] params) throws NotFoundException {
/* 1019 */     throw new NotFoundException(name);
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
/*      */   public CtMethod[] getDeclaredMethods(String name) throws NotFoundException {
/* 1033 */     throw new NotFoundException(name);
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
/*      */   public CtMethod getDeclaredMethod(String name) throws NotFoundException {
/* 1046 */     throw new NotFoundException(name);
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
/*      */   public CtConstructor makeClassInitializer() throws CannotCompileException {
/* 1059 */     throw new CannotCompileException("not a class");
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
/*      */   public void addConstructor(CtConstructor c) throws CannotCompileException {
/* 1071 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeConstructor(CtConstructor c) throws NotFoundException {
/* 1081 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addMethod(CtMethod m) throws CannotCompileException {
/* 1088 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeMethod(CtMethod m) throws NotFoundException {
/* 1098 */     checkModify();
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
/*      */   public void addField(CtField f) throws CannotCompileException {
/* 1111 */     addField(f, (CtField.Initializer)null);
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
/*      */   public void addField(CtField f, String init) throws CannotCompileException {
/* 1147 */     checkModify();
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
/*      */   public void addField(CtField f, CtField.Initializer init) throws CannotCompileException {
/* 1175 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeField(CtField f) throws NotFoundException {
/* 1185 */     checkModify();
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
/*      */   public byte[] getAttribute(String name) {
/* 1206 */     return null;
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
/*      */   public void setAttribute(String name, byte[] data) {
/* 1232 */     checkModify();
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
/*      */   public void instrument(CodeConverter converter) throws CannotCompileException {
/* 1246 */     checkModify();
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
/*      */   public void instrument(ExprEditor editor) throws CannotCompileException {
/* 1260 */     checkModify();
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
/*      */   public Class<?> toClass() throws CannotCompileException {
/* 1298 */     return getClassPool().toClass(this);
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
/*      */   public Class<?> toClass(Class<?> neighbor) throws CannotCompileException {
/* 1325 */     return getClassPool().toClass(this, neighbor);
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
/*      */   public Class<?> toClass(MethodHandles.Lookup lookup) throws CannotCompileException {
/* 1351 */     return getClassPool().toClass(this, lookup);
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
/*      */   public Class<?> toClass(ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
/* 1388 */     ClassPool cp = getClassPool();
/* 1389 */     if (loader == null) {
/* 1390 */       loader = cp.getClassLoader();
/*      */     }
/* 1392 */     return cp.toClass(this, null, loader, domain);
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
/*      */   @Deprecated
/*      */   public final Class<?> toClass(ClassLoader loader) throws CannotCompileException {
/* 1408 */     return getClassPool().toClass(this, null, loader, null);
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
/*      */   public void detach() {
/* 1424 */     ClassPool cp = getClassPool();
/* 1425 */     CtClass obj = cp.removeCached(getName());
/* 1426 */     if (obj != this) {
/* 1427 */       cp.cacheCtClass(getName(), obj, false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean stopPruning(boolean stop) {
/* 1454 */     return true;
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
/*      */   public void prune() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void incGetCounter() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rebuildClassFile() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] toBytecode() throws IOException, CannotCompileException {
/* 1516 */     ByteArrayOutputStream barray = new ByteArrayOutputStream();
/* 1517 */     DataOutputStream out = new DataOutputStream(barray);
/*      */     try {
/* 1519 */       toBytecode(out);
/*      */     } finally {
/*      */       
/* 1522 */       out.close();
/*      */     } 
/*      */     
/* 1525 */     return barray.toByteArray();
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
/*      */   public void writeFile() throws NotFoundException, IOException, CannotCompileException {
/* 1539 */     writeFile(".");
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
/*      */   public void writeFile(String directoryName) throws CannotCompileException, IOException {
/* 1554 */     DataOutputStream out = makeFileOutput(directoryName);
/*      */     try {
/* 1556 */       toBytecode(out);
/*      */     } finally {
/*      */       
/* 1559 */       out.close();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected DataOutputStream makeFileOutput(String directoryName) {
/* 1564 */     String classname = getName();
/*      */     
/* 1566 */     String filename = directoryName + File.separatorChar + classname.replace('.', File.separatorChar) + ".class";
/* 1567 */     int pos = filename.lastIndexOf(File.separatorChar);
/* 1568 */     if (pos > 0) {
/* 1569 */       String dir = filename.substring(0, pos);
/* 1570 */       if (!dir.equals(".")) {
/* 1571 */         (new File(dir)).mkdirs();
/*      */       }
/*      */     } 
/* 1574 */     return new DataOutputStream(new BufferedOutputStream(new DelayedFileOutputStream(filename)));
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
/*      */   public void debugWriteFile() {
/* 1586 */     debugWriteFile(".");
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
/*      */   public void debugWriteFile(String directoryName) {
/*      */     try {
/* 1600 */       boolean p = stopPruning(true);
/* 1601 */       writeFile(directoryName);
/* 1602 */       defrost();
/* 1603 */       stopPruning(p);
/*      */     }
/* 1605 */     catch (Exception e) {
/* 1606 */       throw new RuntimeException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   static class DelayedFileOutputStream extends OutputStream {
/*      */     private FileOutputStream file;
/*      */     private String filename;
/*      */     
/*      */     DelayedFileOutputStream(String name) {
/* 1615 */       this.file = null;
/* 1616 */       this.filename = name;
/*      */     }
/*      */     
/*      */     private void init() throws IOException {
/* 1620 */       if (this.file == null) {
/* 1621 */         this.file = new FileOutputStream(this.filename);
/*      */       }
/*      */     }
/*      */     
/*      */     public void write(int b) throws IOException {
/* 1626 */       init();
/* 1627 */       this.file.write(b);
/*      */     }
/*      */ 
/*      */     
/*      */     public void write(byte[] b) throws IOException {
/* 1632 */       init();
/* 1633 */       this.file.write(b);
/*      */     }
/*      */ 
/*      */     
/*      */     public void write(byte[] b, int off, int len) throws IOException {
/* 1638 */       init();
/* 1639 */       this.file.write(b, off, len);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void flush() throws IOException {
/* 1645 */       init();
/* 1646 */       this.file.flush();
/*      */     }
/*      */ 
/*      */     
/*      */     public void close() throws IOException {
/* 1651 */       init();
/* 1652 */       this.file.close();
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
/*      */   public void toBytecode(DataOutputStream out) throws CannotCompileException, IOException {
/* 1668 */     throw new CannotCompileException("not a class");
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
/*      */   public String makeUniqueName(String prefix) {
/* 1681 */     throw new RuntimeException("not available in " + getName());
/*      */   }
/*      */   
/*      */   void compress() {}
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CtClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */