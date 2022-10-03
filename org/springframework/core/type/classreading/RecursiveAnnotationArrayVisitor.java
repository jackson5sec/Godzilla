/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ class RecursiveAnnotationArrayVisitor
/*     */   extends AbstractRecursiveAnnotationVisitor
/*     */ {
/*     */   private final String attributeName;
/*  45 */   private final List<AnnotationAttributes> allNestedAttributes = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecursiveAnnotationArrayVisitor(String attributeName, AnnotationAttributes attributes, @Nullable ClassLoader classLoader) {
/*  51 */     super(classLoader, attributes);
/*  52 */     this.attributeName = attributeName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(String attributeName, Object attributeValue) {
/*  58 */     Object newValue = attributeValue;
/*  59 */     Object existingValue = this.attributes.get(this.attributeName);
/*  60 */     if (existingValue != null) {
/*  61 */       newValue = ObjectUtils.addObjectToArray((Object[])existingValue, newValue);
/*     */     } else {
/*     */       
/*  64 */       Class<?> arrayClass = newValue.getClass();
/*  65 */       if (Enum.class.isAssignableFrom(arrayClass)) {
/*  66 */         while (arrayClass.getSuperclass() != null && !arrayClass.isEnum()) {
/*  67 */           arrayClass = arrayClass.getSuperclass();
/*     */         }
/*     */       }
/*  70 */       Object[] newArray = (Object[])Array.newInstance(arrayClass, 1);
/*  71 */       newArray[0] = newValue;
/*  72 */       newValue = newArray;
/*     */     } 
/*  74 */     this.attributes.put(this.attributeName, newValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String attributeName, String asmTypeDescriptor) {
/*  79 */     String annotationType = Type.getType(asmTypeDescriptor).getClassName();
/*  80 */     AnnotationAttributes nestedAttributes = new AnnotationAttributes(annotationType, this.classLoader);
/*  81 */     this.allNestedAttributes.add(nestedAttributes);
/*  82 */     return new RecursiveAnnotationAttributesVisitor(annotationType, nestedAttributes, this.classLoader);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/*  87 */     if (!this.allNestedAttributes.isEmpty()) {
/*  88 */       this.attributes.put(this.attributeName, this.allNestedAttributes.toArray(new AnnotationAttributes[0]));
/*     */     }
/*  90 */     else if (!this.attributes.containsKey(this.attributeName)) {
/*  91 */       Class<? extends Annotation> annotationType = this.attributes.annotationType();
/*  92 */       if (annotationType != null)
/*     */         try {
/*  94 */           Class<?> attributeType = annotationType.getMethod(this.attributeName, new Class[0]).getReturnType();
/*  95 */           if (attributeType.isArray()) {
/*  96 */             Class<?> elementType = attributeType.getComponentType();
/*  97 */             if (elementType.isAnnotation()) {
/*  98 */               elementType = AnnotationAttributes.class;
/*     */             }
/* 100 */             this.attributes.put(this.attributeName, Array.newInstance(elementType, 0));
/*     */           }
/*     */         
/* 103 */         } catch (NoSuchMethodException noSuchMethodException) {} 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\RecursiveAnnotationArrayVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */