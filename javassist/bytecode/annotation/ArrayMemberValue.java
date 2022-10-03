/*     */ package javassist.bytecode.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import javassist.ClassPool;
/*     */ import javassist.bytecode.ConstPool;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrayMemberValue
/*     */   extends MemberValue
/*     */ {
/*     */   MemberValue type;
/*     */   MemberValue[] values;
/*     */   
/*     */   public ArrayMemberValue(ConstPool cp) {
/*  39 */     super('[', cp);
/*  40 */     this.type = null;
/*  41 */     this.values = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayMemberValue(MemberValue t, ConstPool cp) {
/*  50 */     super('[', cp);
/*  51 */     this.type = t;
/*  52 */     this.values = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object getValue(ClassLoader cl, ClassPool cp, Method method) throws ClassNotFoundException {
/*     */     Class<?> clazz;
/*  59 */     if (this.values == null) {
/*  60 */       throw new ClassNotFoundException("no array elements found: " + method
/*  61 */           .getName());
/*     */     }
/*  63 */     int size = this.values.length;
/*     */     
/*  65 */     if (this.type == null) {
/*  66 */       clazz = method.getReturnType().getComponentType();
/*  67 */       if (clazz == null || size > 0) {
/*  68 */         throw new ClassNotFoundException("broken array type: " + method
/*  69 */             .getName());
/*     */       }
/*     */     } else {
/*  72 */       clazz = this.type.getType(cl);
/*     */     } 
/*  74 */     Object a = Array.newInstance(clazz, size);
/*  75 */     for (int i = 0; i < size; i++) {
/*  76 */       Array.set(a, i, this.values[i].getValue(cl, cp, method));
/*     */     }
/*  78 */     return a;
/*     */   }
/*     */ 
/*     */   
/*     */   Class<?> getType(ClassLoader cl) throws ClassNotFoundException {
/*  83 */     if (this.type == null) {
/*  84 */       throw new ClassNotFoundException("no array type specified");
/*     */     }
/*  86 */     Object a = Array.newInstance(this.type.getType(cl), 0);
/*  87 */     return a.getClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberValue getType() {
/*  96 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberValue[] getValue() {
/* 103 */     return this.values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(MemberValue[] elements) {
/* 110 */     this.values = elements;
/* 111 */     if (elements != null && elements.length > 0) {
/* 112 */       this.type = elements[0];
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 120 */     StringBuffer buf = new StringBuffer("{");
/* 121 */     if (this.values != null) {
/* 122 */       for (int i = 0; i < this.values.length; i++) {
/* 123 */         buf.append(this.values[i].toString());
/* 124 */         if (i + 1 < this.values.length) {
/* 125 */           buf.append(", ");
/*     */         }
/*     */       } 
/*     */     }
/* 129 */     buf.append("}");
/* 130 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(AnnotationsWriter writer) throws IOException {
/* 138 */     int num = (this.values == null) ? 0 : this.values.length;
/* 139 */     writer.arrayValue(num);
/* 140 */     for (int i = 0; i < num; i++) {
/* 141 */       this.values[i].write(writer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(MemberValueVisitor visitor) {
/* 149 */     visitor.visitArrayMemberValue(this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\annotation\ArrayMemberValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */