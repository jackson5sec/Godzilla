/*     */ package javassist.bytecode.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import javassist.ClassPool;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.SignatureAttribute;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassMemberValue
/*     */   extends MemberValue
/*     */ {
/*     */   int valueIndex;
/*     */   
/*     */   public ClassMemberValue(int index, ConstPool cp) {
/*  44 */     super('c', cp);
/*  45 */     this.valueIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassMemberValue(String className, ConstPool cp) {
/*  54 */     super('c', cp);
/*  55 */     setValue(className);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassMemberValue(ConstPool cp) {
/*  63 */     super('c', cp);
/*  64 */     setValue("java.lang.Class");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object getValue(ClassLoader cl, ClassPool cp, Method m) throws ClassNotFoundException {
/*  70 */     String classname = getValue();
/*  71 */     if (classname.equals("void"))
/*  72 */       return void.class; 
/*  73 */     if (classname.equals("int"))
/*  74 */       return int.class; 
/*  75 */     if (classname.equals("byte"))
/*  76 */       return byte.class; 
/*  77 */     if (classname.equals("long"))
/*  78 */       return long.class; 
/*  79 */     if (classname.equals("double"))
/*  80 */       return double.class; 
/*  81 */     if (classname.equals("float"))
/*  82 */       return float.class; 
/*  83 */     if (classname.equals("char"))
/*  84 */       return char.class; 
/*  85 */     if (classname.equals("short"))
/*  86 */       return short.class; 
/*  87 */     if (classname.equals("boolean")) {
/*  88 */       return boolean.class;
/*     */     }
/*  90 */     return loadClass(cl, classname);
/*     */   }
/*     */ 
/*     */   
/*     */   Class<?> getType(ClassLoader cl) throws ClassNotFoundException {
/*  95 */     return loadClass(cl, "java.lang.Class");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/* 104 */     String v = this.cp.getUtf8Info(this.valueIndex);
/*     */     try {
/* 106 */       return SignatureAttribute.toTypeSignature(v).jvmTypeName();
/* 107 */     } catch (BadBytecode e) {
/* 108 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String newClassName) {
/* 118 */     String setTo = Descriptor.of(newClassName);
/* 119 */     this.valueIndex = this.cp.addUtf8Info(setTo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 127 */     return getValue().replace('$', '.') + ".class";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(AnnotationsWriter writer) throws IOException {
/* 135 */     writer.classInfoIndex(this.cp.getUtf8Info(this.valueIndex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(MemberValueVisitor visitor) {
/* 143 */     visitor.visitClassMemberValue(this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\annotation\ClassMemberValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */