/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FieldInfoData
/*     */   implements MemberCompletion.Data
/*     */ {
/*     */   private FieldInfo info;
/*     */   private SourceCompletionProvider provider;
/*     */   
/*     */   public FieldInfoData(FieldInfo info, SourceCompletionProvider provider) {
/*  40 */     this.info = info;
/*  41 */     this.provider = provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnclosingClassName(boolean fullyQualified) {
/*  50 */     return this.info.getClassFile().getClassName(fullyQualified);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIcon() {
/*     */     String key;
/*  61 */     int flags = this.info.getAccessFlags();
/*     */     
/*  63 */     if (Util.isDefault(flags)) {
/*  64 */       key = "fieldDefaultIcon";
/*     */     }
/*  66 */     else if (Util.isPrivate(flags)) {
/*  67 */       key = "fieldPrivateIcon";
/*     */     }
/*  69 */     else if (Util.isProtected(flags)) {
/*  70 */       key = "fieldProtectedIcon";
/*     */     }
/*  72 */     else if (Util.isPublic(flags)) {
/*  73 */       key = "fieldPublicIcon";
/*     */     } else {
/*     */       
/*  76 */       key = "fieldDefaultIcon";
/*     */     } 
/*     */     
/*  79 */     return key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSignature() {
/*  89 */     return this.info.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/*  99 */     ClassFile cf = this.info.getClassFile();
/* 100 */     SourceLocation loc = this.provider.getSourceLocForClass(cf.getClassName(true));
/* 101 */     String summary = null;
/*     */ 
/*     */ 
/*     */     
/* 105 */     if (loc != null) {
/* 106 */       summary = getSummaryFromSourceLoc(loc, cf);
/*     */     }
/*     */ 
/*     */     
/* 110 */     if (summary == null) {
/* 111 */       summary = this.info.getName();
/*     */     }
/* 113 */     return summary;
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
/*     */   private String getSummaryFromSourceLoc(SourceLocation loc, ClassFile cf) {
/* 130 */     String summary = null;
/*     */     
/* 132 */     CompilationUnit cu = Util.getCompilationUnitFromDisk(loc, cf);
/*     */ 
/*     */ 
/*     */     
/* 136 */     if (cu != null) {
/*     */       
/* 138 */       Iterator<TypeDeclaration> i = cu.getTypeDeclarationIterator();
/* 139 */       while (i.hasNext()) {
/*     */         
/* 141 */         TypeDeclaration td = i.next();
/* 142 */         String typeName = td.getName();
/*     */ 
/*     */         
/* 145 */         if (typeName.equals(cf.getClassName(false))) {
/*     */ 
/*     */           
/* 148 */           Iterator<Member> j = td.getMemberIterator();
/* 149 */           while (j.hasNext()) {
/* 150 */             Member member = j.next();
/* 151 */             if (member instanceof Field && member
/* 152 */               .getName().equals(this.info.getName())) {
/* 153 */               Field f2 = (Field)member;
/* 154 */               summary = f2.getDocComment();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     return summary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 175 */     return this.info.getTypeString(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/* 186 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstructor() {
/* 197 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeprecated() {
/* 206 */     return this.info.isDeprecated();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 212 */     return this.info.isFinal();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 218 */     return this.info.isStatic();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\FieldInfoData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */