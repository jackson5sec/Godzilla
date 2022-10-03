/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.util.Iterator;
/*     */ import javax.swing.Icon;
/*     */ import org.fife.rsta.ac.java.buildpath.SourceLocation;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.java.classreader.Util;
/*     */ import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;
/*     */ import org.fife.rsta.ac.java.rjc.ast.TypeDeclaration;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ClassCompletion
/*     */   extends AbstractJavaSourceCompletion
/*     */ {
/*     */   private ClassFile cf;
/*     */   
/*     */   public ClassCompletion(CompletionProvider provider, ClassFile cf) {
/*  40 */     super(provider, cf.getClassName(false));
/*  41 */     this.cf = cf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Completion c2) {
/*  52 */     if (c2 == this) {
/*  53 */       return 0;
/*     */     }
/*     */     
/*  56 */     if (c2.toString().equalsIgnoreCase(toString()) && 
/*  57 */       c2 instanceof ClassCompletion) {
/*  58 */       ClassCompletion cc2 = (ClassCompletion)c2;
/*  59 */       return getClassName(true).compareTo(cc2.getClassName(true));
/*     */     } 
/*     */     
/*  62 */     return super.compareTo(c2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  68 */     return (obj instanceof ClassCompletion && ((ClassCompletion)obj)
/*  69 */       .getReplacementText().equals(getReplacementText()));
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
/*     */   public String getClassName(boolean fullyQualified) {
/*  82 */     return this.cf.getClassName(fullyQualified);
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
/*     */   public Icon getIcon() {
/*  94 */     boolean isInterface = false;
/*  95 */     boolean isPublic = false;
/*     */ 
/*     */     
/*  98 */     boolean isDefault = false;
/*     */     
/* 100 */     int access = this.cf.getAccessFlags();
/* 101 */     if ((access & 0x200) > 0) {
/* 102 */       isInterface = true;
/*     */     
/*     */     }
/* 105 */     else if (Util.isPublic(access)) {
/* 106 */       isPublic = true;
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 115 */       isDefault = true;
/*     */     } 
/*     */     
/* 118 */     IconFactory fact = IconFactory.get();
/* 119 */     String key = null;
/*     */     
/* 121 */     if (isInterface) {
/* 122 */       if (isDefault) {
/* 123 */         key = "defaultInterfaceIcon";
/*     */       } else {
/*     */         
/* 126 */         key = "interfaceIcon";
/*     */       }
/*     */     
/*     */     }
/* 130 */     else if (isDefault) {
/* 131 */       key = "defaultClassIcon";
/*     */     }
/* 133 */     else if (isPublic) {
/* 134 */       key = "classIcon";
/*     */     } 
/*     */ 
/*     */     
/* 138 */     return fact.getIcon(key, this.cf.isDeprecated());
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
/*     */   public String getPackageName() {
/* 150 */     return this.cf.getPackageName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/* 157 */     SourceCompletionProvider scp = (SourceCompletionProvider)getProvider();
/* 158 */     SourceLocation loc = scp.getSourceLocForClass(this.cf.getClassName(true));
/*     */     
/* 160 */     if (loc != null) {
/*     */       
/* 162 */       CompilationUnit cu = Util.getCompilationUnitFromDisk(loc, this.cf);
/* 163 */       if (cu != null) {
/* 164 */         Iterator<TypeDeclaration> i = cu.getTypeDeclarationIterator();
/* 165 */         while (i.hasNext()) {
/* 166 */           TypeDeclaration td = i.next();
/* 167 */           String typeName = td.getName();
/*     */           
/* 169 */           if (typeName.equals(this.cf.getClassName(false))) {
/* 170 */             String summary = td.getDocComment();
/*     */             
/* 172 */             if (summary != null && summary.startsWith("/**")) {
/* 173 */               return Util.docCommentToHtml(summary);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 182 */     return this.cf.getClassName(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getToolTipText() {
/* 189 */     return "class " + getReplacementText();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 195 */     return getReplacementText().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rendererText(Graphics g, int x, int y, boolean selected) {
/* 202 */     String s = this.cf.getClassName(false);
/* 203 */     g.drawString(s, x, y);
/* 204 */     FontMetrics fm = g.getFontMetrics();
/* 205 */     int newX = x + fm.stringWidth(s);
/* 206 */     if (this.cf.isDeprecated()) {
/* 207 */       int midY = y + fm.getDescent() - fm.getHeight() / 2;
/* 208 */       g.drawLine(x, midY, newX, midY);
/*     */     } 
/* 210 */     x = newX;
/*     */     
/* 212 */     s = " - ";
/* 213 */     g.drawString(s, x, y);
/* 214 */     x += fm.stringWidth(s);
/*     */     
/* 216 */     String pkgName = this.cf.getClassName(true);
/* 217 */     int lastIndexOf = pkgName.lastIndexOf('.');
/* 218 */     if (lastIndexOf != -1) {
/* 219 */       pkgName = pkgName.substring(0, lastIndexOf);
/* 220 */       Color origColor = g.getColor();
/* 221 */       if (!selected) {
/* 222 */         g.setColor(Color.GRAY);
/*     */       }
/* 224 */       g.drawString(pkgName, x, y);
/* 225 */       if (!selected)
/* 226 */         g.setColor(origColor); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\ClassCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */