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
/*     */ public abstract class ModuleVisitor
/*     */ {
/*     */   protected final int api;
/*     */   protected ModuleVisitor mv;
/*     */   
/*     */   public ModuleVisitor(int api) {
/*  57 */     this(api, null);
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
/*     */   public ModuleVisitor(int api, ModuleVisitor moduleVisitor) {
/*  69 */     if (api != 589824 && api != 524288 && api != 458752 && api != 393216 && api != 327680 && api != 262144 && api != 17432576)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  76 */       throw new IllegalArgumentException("Unsupported api " + api);
/*     */     }
/*     */     
/*  79 */     this.api = api;
/*  80 */     this.mv = moduleVisitor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitMainClass(String mainClass) {
/*  89 */     if (this.mv != null) {
/*  90 */       this.mv.visitMainClass(mainClass);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitPackage(String packaze) {
/* 100 */     if (this.mv != null) {
/* 101 */       this.mv.visitPackage(packaze);
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
/*     */   public void visitRequire(String module, int access, String version) {
/* 114 */     if (this.mv != null) {
/* 115 */       this.mv.visitRequire(module, access, version);
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
/*     */   public void visitExport(String packaze, int access, String... modules) {
/* 129 */     if (this.mv != null) {
/* 130 */       this.mv.visitExport(packaze, access, modules);
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
/*     */   public void visitOpen(String packaze, int access, String... modules) {
/* 144 */     if (this.mv != null) {
/* 145 */       this.mv.visitOpen(packaze, access, modules);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitUse(String service) {
/* 156 */     if (this.mv != null) {
/* 157 */       this.mv.visitUse(service);
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
/*     */   public void visitProvide(String service, String... providers) {
/* 169 */     if (this.mv != null) {
/* 170 */       this.mv.visitProvide(service, providers);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 179 */     if (this.mv != null)
/* 180 */       this.mv.visitEnd(); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\asm\ModuleVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */