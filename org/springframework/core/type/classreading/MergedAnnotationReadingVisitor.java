/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.core.annotation.AnnotationFilter;
/*     */ import org.springframework.core.annotation.MergedAnnotation;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ class MergedAnnotationReadingVisitor<A extends Annotation>
/*     */   extends AnnotationVisitor
/*     */ {
/*     */   @Nullable
/*     */   private final ClassLoader classLoader;
/*     */   @Nullable
/*     */   private final Object source;
/*     */   private final Class<A> annotationType;
/*     */   private final Consumer<MergedAnnotation<A>> consumer;
/*  55 */   private final Map<String, Object> attributes = new LinkedHashMap<>(4);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MergedAnnotationReadingVisitor(@Nullable ClassLoader classLoader, @Nullable Object source, Class<A> annotationType, Consumer<MergedAnnotation<A>> consumer) {
/*  61 */     super(17432576);
/*  62 */     this.classLoader = classLoader;
/*  63 */     this.source = source;
/*  64 */     this.annotationType = annotationType;
/*  65 */     this.consumer = consumer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(String name, Object value) {
/*  71 */     if (value instanceof Type) {
/*  72 */       value = ((Type)value).getClassName();
/*     */     }
/*  74 */     this.attributes.put(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnum(String name, String descriptor, String value) {
/*  79 */     visitEnum(descriptor, value, enumValue -> this.attributes.put(name, enumValue));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AnnotationVisitor visitAnnotation(String name, String descriptor) {
/*  85 */     return visitAnnotation(descriptor, annotation -> this.attributes.put(name, annotation));
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitArray(String name) {
/*  90 */     return new ArrayVisitor(value -> this.attributes.put(name, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/*  95 */     MergedAnnotation<A> annotation = MergedAnnotation.of(this.classLoader, this.source, this.annotationType, this.attributes);
/*     */     
/*  97 */     this.consumer.accept(annotation);
/*     */   }
/*     */ 
/*     */   
/*     */   public <E extends Enum<E>> void visitEnum(String descriptor, String value, Consumer<E> consumer) {
/* 102 */     String className = Type.getType(descriptor).getClassName();
/* 103 */     Class<E> type = ClassUtils.resolveClassName(className, this.classLoader);
/* 104 */     consumer.accept(Enum.valueOf(type, value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private <T extends Annotation> AnnotationVisitor visitAnnotation(String descriptor, Consumer<MergedAnnotation<T>> consumer) {
/* 112 */     String className = Type.getType(descriptor).getClassName();
/* 113 */     if (AnnotationFilter.PLAIN.matches(className)) {
/* 114 */       return null;
/*     */     }
/* 116 */     Class<T> type = ClassUtils.resolveClassName(className, this.classLoader);
/* 117 */     return new MergedAnnotationReadingVisitor(this.classLoader, this.source, type, consumer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   static <A extends Annotation> AnnotationVisitor get(@Nullable ClassLoader classLoader, @Nullable Object source, String descriptor, boolean visible, Consumer<MergedAnnotation<A>> consumer) {
/* 126 */     if (!visible) {
/* 127 */       return null;
/*     */     }
/*     */     
/* 130 */     String typeName = Type.getType(descriptor).getClassName();
/* 131 */     if (AnnotationFilter.PLAIN.matches(typeName)) {
/* 132 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 136 */       Class<A> annotationType = ClassUtils.forName(typeName, classLoader);
/* 137 */       return new MergedAnnotationReadingVisitor<>(classLoader, source, annotationType, consumer);
/*     */     }
/* 139 */     catch (ClassNotFoundException|LinkageError ex) {
/* 140 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ArrayVisitor
/*     */     extends AnnotationVisitor
/*     */   {
/* 150 */     private final List<Object> elements = new ArrayList();
/*     */     
/*     */     private final Consumer<Object[]> consumer;
/*     */     
/*     */     ArrayVisitor(Consumer<Object[]> consumer) {
/* 155 */       super(17432576);
/* 156 */       this.consumer = consumer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void visit(String name, Object value) {
/* 161 */       if (value instanceof Type) {
/* 162 */         value = ((Type)value).getClassName();
/*     */       }
/* 164 */       this.elements.add(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitEnum(String name, String descriptor, String value) {
/* 169 */       MergedAnnotationReadingVisitor.this.visitEnum(descriptor, value, this.elements::add);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public AnnotationVisitor visitAnnotation(String name, String descriptor) {
/* 175 */       return MergedAnnotationReadingVisitor.this.visitAnnotation(descriptor, this.elements::add);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitEnd() {
/* 180 */       Class<?> componentType = getComponentType();
/* 181 */       Object[] array = (Object[])Array.newInstance(componentType, this.elements.size());
/* 182 */       this.consumer.accept(this.elements.toArray(array));
/*     */     }
/*     */     
/*     */     private Class<?> getComponentType() {
/* 186 */       if (this.elements.isEmpty()) {
/* 187 */         return Object.class;
/*     */       }
/* 189 */       Object firstElement = this.elements.get(0);
/* 190 */       if (firstElement instanceof Enum) {
/* 191 */         return ((Enum)firstElement).getDeclaringClass();
/*     */       }
/* 193 */       return firstElement.getClass();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\MergedAnnotationReadingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */