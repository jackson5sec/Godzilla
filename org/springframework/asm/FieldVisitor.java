/*     */ package org.springframework.asm;
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
/*     */ public abstract class FieldVisitor
/*     */ {
/*     */   protected final int api;
/*     */   protected FieldVisitor fv;
/*     */   
/*     */   public FieldVisitor(int api) {
/*  55 */     this(api, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldVisitor(int api, FieldVisitor fieldVisitor) {
/*  67 */     if (api != 589824 && api != 524288 && api != 458752 && api != 393216 && api != 327680 && api != 262144 && api != 17432576)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  74 */       throw new IllegalArgumentException("Unsupported api " + api);
/*     */     }
/*     */     
/*  77 */     this.api = api;
/*  78 */     this.fv = fieldVisitor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
/*  90 */     if (this.fv != null) {
/*  91 */       return this.fv.visitAnnotation(descriptor, visible);
/*     */     }
/*  93 */     return null;
/*     */   }
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
/*     */   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 111 */     if (this.api < 327680) {
/* 112 */       throw new UnsupportedOperationException("This feature requires ASM5");
/*     */     }
/* 114 */     if (this.fv != null) {
/* 115 */       return this.fv.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
/*     */     }
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute attribute) {
/* 126 */     if (this.fv != null) {
/* 127 */       this.fv.visitAttribute(attribute);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 136 */     if (this.fv != null)
/* 137 */       this.fv.visitEnd(); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\asm\FieldVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */