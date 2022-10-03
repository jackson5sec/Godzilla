/*     */ package org.fife.rsta.ac.js.completion;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import org.fife.rsta.ac.java.JarManager;
/*     */ import org.fife.rsta.ac.java.Util;
/*     */ import org.fife.rsta.ac.java.buildpath.SourceLocation;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.java.classreader.FieldInfo;
/*     */ import org.fife.rsta.ac.java.classreader.Util;
/*     */ import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Field;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Member;
/*     */ import org.fife.rsta.ac.java.rjc.ast.TypeDeclaration;
/*     */ 
/*     */ 
/*     */ public class JSFieldData
/*     */ {
/*     */   private FieldInfo info;
/*     */   private JarManager jarManager;
/*     */   
/*     */   public JSFieldData(FieldInfo info, JarManager jarManager) {
/*  22 */     this.info = info;
/*  23 */     this.jarManager = jarManager;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Field getField() {
/*  29 */     ClassFile cf = this.info.getClassFile();
/*  30 */     SourceLocation loc = this.jarManager.getSourceLocForClass(cf
/*  31 */         .getClassName(true));
/*  32 */     return getFieldFromSourceLoc(loc, cf);
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
/*     */   private Field getFieldFromSourceLoc(SourceLocation loc, ClassFile cf) {
/*  48 */     CompilationUnit cu = Util.getCompilationUnitFromDisk(loc, cf);
/*     */ 
/*     */ 
/*     */     
/*  52 */     if (cu != null) {
/*     */       
/*  54 */       Iterator<TypeDeclaration> i = cu.getTypeDeclarationIterator();
/*  55 */       while (i.hasNext()) {
/*     */         
/*  57 */         TypeDeclaration td = i.next();
/*  58 */         String typeName = td.getName();
/*     */ 
/*     */         
/*  61 */         if (typeName.equals(cf.getClassName(false))) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  66 */           Iterator<Member> j = td.getMemberIterator();
/*  67 */           while (j.hasNext()) {
/*  68 */             Member member = j.next();
/*  69 */             if (member instanceof Field && member
/*  70 */               .getName().equals(this.info.getName())) {
/*  71 */               return (Field)member;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType(boolean qualified) {
/*  87 */     return this.info.getTypeString(qualified);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/*  92 */     return this.info.isStatic();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPublic() {
/*  97 */     int access = this.info.getAccessFlags();
/*  98 */     return Util.isPublic(access);
/*     */   }
/*     */   
/*     */   public String getEnclosingClassName(boolean fullyQualified) {
/* 102 */     return this.info.getClassFile().getClassName(fullyQualified);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\completion\JSFieldData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */