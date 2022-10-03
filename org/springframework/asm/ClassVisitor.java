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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ClassVisitor
/*     */ {
/*     */   protected final int api;
/*     */   protected ClassVisitor cv;
/*     */   
/*     */   public ClassVisitor(int api) {
/*  58 */     this(api, null);
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
/*     */   public ClassVisitor(int api, ClassVisitor classVisitor) {
/*  70 */     if (api != 589824 && api != 524288 && api != 458752 && api != 393216 && api != 327680 && api != 262144 && api != 17432576)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  77 */       throw new IllegalArgumentException("Unsupported api " + api);
/*     */     }
/*     */     
/*  80 */     this.api = api;
/*  81 */     this.cv = classVisitor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
/* 108 */     if (this.api < 524288 && (access & 0x10000) != 0) {
/* 109 */       throw new UnsupportedOperationException("Records requires ASM8");
/*     */     }
/* 111 */     if (this.cv != null) {
/* 112 */       this.cv.visit(version, access, name, signature, superName, interfaces);
/*     */     }
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
/*     */   public void visitSource(String source, String debug) {
/* 125 */     if (this.cv != null) {
/* 126 */       this.cv.visitSource(source, debug);
/*     */     }
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
/*     */   public ModuleVisitor visitModule(String name, int access, String version) {
/* 141 */     if (this.api < 393216) {
/* 142 */       throw new UnsupportedOperationException("Module requires ASM6");
/*     */     }
/* 144 */     if (this.cv != null) {
/* 145 */       return this.cv.visitModule(name, access, version);
/*     */     }
/* 147 */     return null;
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
/*     */   public void visitNestHost(String nestHost) {
/* 161 */     if (this.api < 458752) {
/* 162 */       throw new UnsupportedOperationException("NestHost requires ASM7");
/*     */     }
/* 164 */     if (this.cv != null) {
/* 165 */       this.cv.visitNestHost(nestHost);
/*     */     }
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
/*     */   public void visitOuterClass(String owner, String name, String descriptor) {
/* 180 */     if (this.cv != null) {
/* 181 */       this.cv.visitOuterClass(owner, name, descriptor);
/*     */     }
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
/* 194 */     if (this.cv != null) {
/* 195 */       return this.cv.visitAnnotation(descriptor, visible);
/*     */     }
/* 197 */     return null;
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
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
/* 217 */     if (this.api < 327680) {
/* 218 */       throw new UnsupportedOperationException("TypeAnnotation requires ASM5");
/*     */     }
/* 220 */     if (this.cv != null) {
/* 221 */       return this.cv.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
/*     */     }
/* 223 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute attribute) {
/* 232 */     if (this.cv != null) {
/* 233 */       this.cv.visitAttribute(attribute);
/*     */     }
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
/*     */   public void visitNestMember(String nestMember) {
/* 247 */     if (this.api < 458752) {
/* 248 */       throw new UnsupportedOperationException("NestMember requires ASM7");
/*     */     }
/* 250 */     if (this.cv != null) {
/* 251 */       this.cv.visitNestMember(nestMember);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitPermittedSubclass(String permittedSubclass) {
/* 262 */     if (this.api < 589824) {
/* 263 */       throw new UnsupportedOperationException("PermittedSubclasses requires ASM9");
/*     */     }
/* 265 */     if (this.cv != null) {
/* 266 */       this.cv.visitPermittedSubclass(permittedSubclass);
/*     */     }
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
/*     */   public void visitInnerClass(String name, String outerName, String innerName, int access) {
/* 284 */     if (this.cv != null) {
/* 285 */       this.cv.visitInnerClass(name, outerName, innerName, access);
/*     */     }
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
/*     */   public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
/* 301 */     if (this.api < 524288) {
/* 302 */       throw new UnsupportedOperationException("Record requires ASM8");
/*     */     }
/* 304 */     if (this.cv != null) {
/* 305 */       return this.cv.visitRecordComponent(name, descriptor, signature);
/*     */     }
/* 307 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
/* 334 */     if (this.cv != null) {
/* 335 */       return this.cv.visitField(access, name, descriptor, signature, value);
/*     */     }
/* 337 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
/* 362 */     if (this.cv != null) {
/* 363 */       return this.cv.visitMethod(access, name, descriptor, signature, exceptions);
/*     */     }
/* 365 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 373 */     if (this.cv != null)
/* 374 */       this.cv.visitEnd(); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\asm\ClassVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */