/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.annotation.MergedAnnotation;
/*     */ import org.springframework.core.annotation.MergedAnnotations;
/*     */ import org.springframework.core.type.MethodMetadata;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ final class SimpleAnnotationMetadataReadingVisitor
/*     */   extends ClassVisitor
/*     */ {
/*     */   @Nullable
/*     */   private final ClassLoader classLoader;
/*  48 */   private String className = "";
/*     */   
/*     */   private int access;
/*     */   
/*     */   @Nullable
/*     */   private String superClassName;
/*     */   
/*  55 */   private String[] interfaceNames = new String[0];
/*     */   
/*     */   @Nullable
/*     */   private String enclosingClassName;
/*     */   
/*     */   private boolean independentInnerClass;
/*     */   
/*  62 */   private Set<String> memberClassNames = new LinkedHashSet<>(4);
/*     */   
/*  64 */   private List<MergedAnnotation<?>> annotations = new ArrayList<>();
/*     */   
/*  66 */   private List<SimpleMethodMetadata> annotatedMethods = new ArrayList<>();
/*     */   
/*     */   @Nullable
/*     */   private SimpleAnnotationMetadata metadata;
/*     */   
/*     */   @Nullable
/*     */   private Source source;
/*     */ 
/*     */   
/*     */   SimpleAnnotationMetadataReadingVisitor(@Nullable ClassLoader classLoader) {
/*  76 */     super(17432576);
/*  77 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(int version, int access, String name, String signature, @Nullable String supername, String[] interfaces) {
/*  85 */     this.className = toClassName(name);
/*  86 */     this.access = access;
/*  87 */     if (supername != null && !isInterface(access)) {
/*  88 */       this.superClassName = toClassName(supername);
/*     */     }
/*  90 */     this.interfaceNames = new String[interfaces.length];
/*  91 */     for (int i = 0; i < interfaces.length; i++) {
/*  92 */       this.interfaceNames[i] = toClassName(interfaces[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitOuterClass(String owner, String name, String desc) {
/*  98 */     this.enclosingClassName = toClassName(owner);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitInnerClass(String name, @Nullable String outerName, String innerName, int access) {
/* 104 */     if (outerName != null) {
/* 105 */       String className = toClassName(name);
/* 106 */       String outerClassName = toClassName(outerName);
/* 107 */       if (this.className.equals(className)) {
/* 108 */         this.enclosingClassName = outerClassName;
/* 109 */         this.independentInnerClass = ((access & 0x8) != 0);
/*     */       }
/* 111 */       else if (this.className.equals(outerClassName)) {
/* 112 */         this.memberClassNames.add(className);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
/* 120 */     return MergedAnnotationReadingVisitor.get(this.classLoader, getSource(), descriptor, visible, this.annotations::add);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
/* 132 */     if (isBridge(access)) {
/* 133 */       return null;
/*     */     }
/* 135 */     return new SimpleMethodMetadataReadingVisitor(this.classLoader, this.className, access, name, descriptor, this.annotatedMethods::add);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 141 */     String[] memberClassNames = StringUtils.toStringArray(this.memberClassNames);
/* 142 */     MethodMetadata[] annotatedMethods = this.annotatedMethods.<MethodMetadata>toArray(new MethodMetadata[0]);
/* 143 */     MergedAnnotations annotations = MergedAnnotations.of(this.annotations);
/* 144 */     this.metadata = new SimpleAnnotationMetadata(this.className, this.access, this.enclosingClassName, this.superClassName, this.independentInnerClass, this.interfaceNames, memberClassNames, annotatedMethods, annotations);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleAnnotationMetadata getMetadata() {
/* 150 */     Assert.state((this.metadata != null), "AnnotationMetadata not initialized");
/* 151 */     return this.metadata;
/*     */   }
/*     */   
/*     */   private Source getSource() {
/* 155 */     Source source = this.source;
/* 156 */     if (source == null) {
/* 157 */       source = new Source(this.className);
/* 158 */       this.source = source;
/*     */     } 
/* 160 */     return source;
/*     */   }
/*     */   
/*     */   private String toClassName(String name) {
/* 164 */     return ClassUtils.convertResourcePathToClassName(name);
/*     */   }
/*     */   
/*     */   private boolean isBridge(int access) {
/* 168 */     return ((access & 0x40) != 0);
/*     */   }
/*     */   
/*     */   private boolean isInterface(int access) {
/* 172 */     return ((access & 0x200) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Source
/*     */   {
/*     */     private final String className;
/*     */ 
/*     */     
/*     */     Source(String className) {
/* 183 */       this.className = className;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 188 */       return this.className.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 193 */       if (this == obj) {
/* 194 */         return true;
/*     */       }
/* 196 */       if (obj == null || getClass() != obj.getClass()) {
/* 197 */         return false;
/*     */       }
/* 199 */       return this.className.equals(((Source)obj).className);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 204 */       return this.className;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\SimpleAnnotationMetadataReadingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */