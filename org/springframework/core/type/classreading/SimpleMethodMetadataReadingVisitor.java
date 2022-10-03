/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.core.annotation.MergedAnnotation;
/*     */ import org.springframework.core.annotation.MergedAnnotations;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ final class SimpleMethodMetadataReadingVisitor
/*     */   extends MethodVisitor
/*     */ {
/*     */   @Nullable
/*     */   private final ClassLoader classLoader;
/*     */   private final String declaringClassName;
/*     */   private final int access;
/*     */   private final String methodName;
/*     */   private final String descriptor;
/*  51 */   private final List<MergedAnnotation<?>> annotations = new ArrayList<>(4);
/*     */ 
/*     */   
/*     */   private final Consumer<SimpleMethodMetadata> consumer;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Source source;
/*     */ 
/*     */   
/*     */   SimpleMethodMetadataReadingVisitor(@Nullable ClassLoader classLoader, String declaringClassName, int access, String methodName, String descriptor, Consumer<SimpleMethodMetadata> consumer) {
/*  62 */     super(17432576);
/*  63 */     this.classLoader = classLoader;
/*  64 */     this.declaringClassName = declaringClassName;
/*  65 */     this.access = access;
/*  66 */     this.methodName = methodName;
/*  67 */     this.descriptor = descriptor;
/*  68 */     this.consumer = consumer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
/*  75 */     return MergedAnnotationReadingVisitor.get(this.classLoader, getSource(), descriptor, visible, this.annotations::add);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/*  81 */     if (!this.annotations.isEmpty()) {
/*  82 */       String returnTypeName = Type.getReturnType(this.descriptor).getClassName();
/*  83 */       MergedAnnotations annotations = MergedAnnotations.of(this.annotations);
/*     */       
/*  85 */       SimpleMethodMetadata metadata = new SimpleMethodMetadata(this.methodName, this.access, this.declaringClassName, returnTypeName, getSource(), annotations);
/*  86 */       this.consumer.accept(metadata);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Object getSource() {
/*  91 */     Source source = this.source;
/*  92 */     if (source == null) {
/*  93 */       source = new Source(this.declaringClassName, this.methodName, this.descriptor);
/*  94 */       this.source = source;
/*     */     } 
/*  96 */     return source;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Source
/*     */   {
/*     */     private final String declaringClassName;
/*     */ 
/*     */     
/*     */     private final String methodName;
/*     */     
/*     */     private final String descriptor;
/*     */     
/*     */     @Nullable
/*     */     private String toStringValue;
/*     */ 
/*     */     
/*     */     Source(String declaringClassName, String methodName, String descriptor) {
/* 115 */       this.declaringClassName = declaringClassName;
/* 116 */       this.methodName = methodName;
/* 117 */       this.descriptor = descriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 122 */       int result = 1;
/* 123 */       result = 31 * result + this.declaringClassName.hashCode();
/* 124 */       result = 31 * result + this.methodName.hashCode();
/* 125 */       result = 31 * result + this.descriptor.hashCode();
/* 126 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object other) {
/* 131 */       if (this == other) {
/* 132 */         return true;
/*     */       }
/* 134 */       if (other == null || getClass() != other.getClass()) {
/* 135 */         return false;
/*     */       }
/* 137 */       Source otherSource = (Source)other;
/* 138 */       return (this.declaringClassName.equals(otherSource.declaringClassName) && this.methodName
/* 139 */         .equals(otherSource.methodName) && this.descriptor.equals(otherSource.descriptor));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 144 */       String value = this.toStringValue;
/* 145 */       if (value == null) {
/* 146 */         StringBuilder builder = new StringBuilder();
/* 147 */         builder.append(this.declaringClassName);
/* 148 */         builder.append('.');
/* 149 */         builder.append(this.methodName);
/* 150 */         Type[] argumentTypes = Type.getArgumentTypes(this.descriptor);
/* 151 */         builder.append('(');
/* 152 */         for (int i = 0; i < argumentTypes.length; i++) {
/* 153 */           if (i != 0) {
/* 154 */             builder.append(',');
/*     */           }
/* 156 */           builder.append(argumentTypes[i].getClassName());
/*     */         } 
/* 158 */         builder.append(')');
/* 159 */         value = builder.toString();
/* 160 */         this.toStringValue = value;
/*     */       } 
/* 162 */       return value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\SimpleMethodMetadataReadingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */