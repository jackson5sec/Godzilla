/*     */ package org.springframework.cglib.transform;
/*     */ 
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.FieldVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.TypePath;
/*     */ import org.springframework.cglib.core.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassVisitorTee
/*     */   extends ClassVisitor
/*     */ {
/*     */   private ClassVisitor cv1;
/*     */   private ClassVisitor cv2;
/*     */   
/*     */   public ClassVisitorTee(ClassVisitor cv1, ClassVisitor cv2) {
/*  25 */     super(Constants.ASM_API);
/*  26 */     this.cv1 = cv1;
/*  27 */     this.cv2 = cv2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
/*  36 */     this.cv1.visit(version, access, name, signature, superName, interfaces);
/*  37 */     this.cv2.visit(version, access, name, signature, superName, interfaces);
/*     */   }
/*     */   
/*     */   public void visitEnd() {
/*  41 */     this.cv1.visitEnd();
/*  42 */     this.cv2.visitEnd();
/*  43 */     this.cv1 = this.cv2 = null;
/*     */   }
/*     */   
/*     */   public void visitInnerClass(String name, String outerName, String innerName, int access) {
/*  47 */     this.cv1.visitInnerClass(name, outerName, innerName, access);
/*  48 */     this.cv2.visitInnerClass(name, outerName, innerName, access);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
/*  56 */     FieldVisitor fv1 = this.cv1.visitField(access, name, desc, signature, value);
/*  57 */     FieldVisitor fv2 = this.cv2.visitField(access, name, desc, signature, value);
/*  58 */     if (fv1 == null)
/*  59 */       return fv2; 
/*  60 */     if (fv2 == null)
/*  61 */       return fv1; 
/*  62 */     return new FieldVisitorTee(fv1, fv2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/*  71 */     MethodVisitor mv1 = this.cv1.visitMethod(access, name, desc, signature, exceptions);
/*  72 */     MethodVisitor mv2 = this.cv2.visitMethod(access, name, desc, signature, exceptions);
/*  73 */     if (mv1 == null)
/*  74 */       return mv2; 
/*  75 */     if (mv2 == null)
/*  76 */       return mv1; 
/*  77 */     return new MethodVisitorTee(mv1, mv2);
/*     */   }
/*     */   
/*     */   public void visitSource(String source, String debug) {
/*  81 */     this.cv1.visitSource(source, debug);
/*  82 */     this.cv2.visitSource(source, debug);
/*     */   }
/*     */   
/*     */   public void visitOuterClass(String owner, String name, String desc) {
/*  86 */     this.cv1.visitOuterClass(owner, name, desc);
/*  87 */     this.cv2.visitOuterClass(owner, name, desc);
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/*  91 */     return AnnotationVisitorTee.getInstance(this.cv1.visitAnnotation(desc, visible), this.cv2
/*  92 */         .visitAnnotation(desc, visible));
/*     */   }
/*     */   
/*     */   public void visitAttribute(Attribute attrs) {
/*  96 */     this.cv1.visitAttribute(attrs);
/*  97 */     this.cv2.visitAttribute(attrs);
/*     */   }
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
/* 101 */     return AnnotationVisitorTee.getInstance(this.cv1.visitTypeAnnotation(typeRef, typePath, desc, visible), this.cv2
/* 102 */         .visitTypeAnnotation(typeRef, typePath, desc, visible));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\ClassVisitorTee.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */