/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.FieldVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.type.ClassMetadata;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ class ClassMetadataReadingVisitor
/*     */   extends ClassVisitor
/*     */   implements ClassMetadata
/*     */ {
/*  52 */   private String className = "";
/*     */   
/*     */   private boolean isInterface;
/*     */   
/*     */   private boolean isAnnotation;
/*     */   
/*     */   private boolean isAbstract;
/*     */   
/*     */   private boolean isFinal;
/*     */   
/*     */   @Nullable
/*     */   private String enclosingClassName;
/*     */   
/*     */   private boolean independentInnerClass;
/*     */   
/*     */   @Nullable
/*     */   private String superClassName;
/*     */   
/*  70 */   private String[] interfaces = new String[0];
/*     */   
/*  72 */   private Set<String> memberClassNames = new LinkedHashSet<>(4);
/*     */ 
/*     */   
/*     */   public ClassMetadataReadingVisitor() {
/*  76 */     super(17432576);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(int version, int access, String name, String signature, @Nullable String supername, String[] interfaces) {
/*  84 */     this.className = ClassUtils.convertResourcePathToClassName(name);
/*  85 */     this.isInterface = ((access & 0x200) != 0);
/*  86 */     this.isAnnotation = ((access & 0x2000) != 0);
/*  87 */     this.isAbstract = ((access & 0x400) != 0);
/*  88 */     this.isFinal = ((access & 0x10) != 0);
/*  89 */     if (supername != null && !this.isInterface) {
/*  90 */       this.superClassName = ClassUtils.convertResourcePathToClassName(supername);
/*     */     }
/*  92 */     this.interfaces = new String[interfaces.length];
/*  93 */     for (int i = 0; i < interfaces.length; i++) {
/*  94 */       this.interfaces[i] = ClassUtils.convertResourcePathToClassName(interfaces[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitOuterClass(String owner, String name, String desc) {
/* 100 */     this.enclosingClassName = ClassUtils.convertResourcePathToClassName(owner);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitInnerClass(String name, @Nullable String outerName, String innerName, int access) {
/* 105 */     if (outerName != null) {
/* 106 */       String fqName = ClassUtils.convertResourcePathToClassName(name);
/* 107 */       String fqOuterName = ClassUtils.convertResourcePathToClassName(outerName);
/* 108 */       if (this.className.equals(fqName)) {
/* 109 */         this.enclosingClassName = fqOuterName;
/* 110 */         this.independentInnerClass = ((access & 0x8) != 0);
/*     */       }
/* 112 */       else if (this.className.equals(fqOuterName)) {
/* 113 */         this.memberClassNames.add(fqName);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitSource(String source, String debug) {}
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 127 */     return new EmptyAnnotationVisitor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute attr) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
/* 138 */     return new EmptyFieldVisitor();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 144 */     return new EmptyMethodVisitor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 155 */     return this.className;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterface() {
/* 160 */     return this.isInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotation() {
/* 165 */     return this.isAnnotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/* 170 */     return this.isAbstract;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 175 */     return this.isFinal;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIndependent() {
/* 180 */     return (this.enclosingClassName == null || this.independentInnerClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEnclosingClass() {
/* 185 */     return (this.enclosingClassName != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getEnclosingClassName() {
/* 191 */     return this.enclosingClassName;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getSuperClassName() {
/* 197 */     return this.superClassName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getInterfaceNames() {
/* 202 */     return this.interfaces;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getMemberClassNames() {
/* 207 */     return StringUtils.toStringArray(this.memberClassNames);
/*     */   }
/*     */   
/*     */   private static class EmptyAnnotationVisitor
/*     */     extends AnnotationVisitor
/*     */   {
/*     */     public EmptyAnnotationVisitor() {
/* 214 */       super(17432576);
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitAnnotation(String name, String desc) {
/* 219 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitArray(String name) {
/* 224 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EmptyMethodVisitor
/*     */     extends MethodVisitor
/*     */   {
/*     */     public EmptyMethodVisitor() {
/* 232 */       super(17432576);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EmptyFieldVisitor
/*     */     extends FieldVisitor
/*     */   {
/*     */     public EmptyFieldVisitor() {
/* 240 */       super(17432576);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\ClassMetadataReadingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */