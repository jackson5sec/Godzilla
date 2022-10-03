/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ @Deprecated
/*     */ abstract class AbstractRecursiveAnnotationVisitor
/*     */   extends AnnotationVisitor
/*     */ {
/*  48 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   protected final AnnotationAttributes attributes;
/*     */   
/*     */   @Nullable
/*     */   protected final ClassLoader classLoader;
/*     */ 
/*     */   
/*     */   public AbstractRecursiveAnnotationVisitor(@Nullable ClassLoader classLoader, AnnotationAttributes attributes) {
/*  57 */     super(17432576);
/*  58 */     this.classLoader = classLoader;
/*  59 */     this.attributes = attributes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(String attributeName, Object attributeValue) {
/*  65 */     this.attributes.put(attributeName, attributeValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String attributeName, String asmTypeDescriptor) {
/*  70 */     String annotationType = Type.getType(asmTypeDescriptor).getClassName();
/*  71 */     AnnotationAttributes nestedAttributes = new AnnotationAttributes(annotationType, this.classLoader);
/*  72 */     this.attributes.put(attributeName, nestedAttributes);
/*  73 */     return new RecursiveAnnotationAttributesVisitor(annotationType, nestedAttributes, this.classLoader);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitArray(String attributeName) {
/*  78 */     return new RecursiveAnnotationArrayVisitor(attributeName, this.attributes, this.classLoader);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnum(String attributeName, String asmTypeDescriptor, String attributeValue) {
/*  83 */     Object newValue = getEnumValue(asmTypeDescriptor, attributeValue);
/*  84 */     visit(attributeName, newValue);
/*     */   }
/*     */   
/*     */   protected Object getEnumValue(String asmTypeDescriptor, String attributeValue) {
/*  88 */     Object valueToUse = attributeValue;
/*     */     try {
/*  90 */       Class<?> enumType = ClassUtils.forName(Type.getType(asmTypeDescriptor).getClassName(), this.classLoader);
/*  91 */       Field enumConstant = ReflectionUtils.findField(enumType, attributeValue);
/*  92 */       if (enumConstant != null) {
/*  93 */         ReflectionUtils.makeAccessible(enumConstant);
/*  94 */         valueToUse = enumConstant.get(null);
/*     */       }
/*     */     
/*  97 */     } catch (ClassNotFoundException|NoClassDefFoundError ex) {
/*  98 */       this.logger.debug("Failed to classload enum type while reading annotation metadata", ex);
/*     */     }
/* 100 */     catch (IllegalAccessException|java.security.AccessControlException ex) {
/* 101 */       this.logger.debug("Could not access enum value while reading annotation metadata", ex);
/*     */     } 
/* 103 */     return valueToUse;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\AbstractRecursiveAnnotationVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */