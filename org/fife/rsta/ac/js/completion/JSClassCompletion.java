/*     */ package org.fife.rsta.ac.js.completion;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.util.Iterator;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.java.Util;
/*     */ import org.fife.rsta.ac.java.buildpath.SourceLocation;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;
/*     */ import org.fife.rsta.ac.java.rjc.ast.TypeDeclaration;
/*     */ import org.fife.rsta.ac.js.IconFactory;
/*     */ import org.fife.rsta.ac.js.JavaScriptHelper;
/*     */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*     */ import org.fife.ui.autocomplete.BasicCompletion;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ 
/*     */ 
/*     */ public class JSClassCompletion
/*     */   extends BasicCompletion
/*     */   implements JSCompletion
/*     */ {
/*     */   private ClassFile cf;
/*     */   private boolean qualified;
/*     */   
/*     */   public JSClassCompletion(CompletionProvider provider, ClassFile cf, boolean qualified) {
/*  30 */     super(provider, ((SourceCompletionProvider)provider).getTypesFactory().convertJavaScriptType(cf.getClassName(true), qualified));
/*  31 */     this.cf = cf;
/*  32 */     this.qualified = qualified;
/*  33 */     setRelevance(2);
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
/*  44 */     if (c2 == this) {
/*  45 */       return 0;
/*     */     }
/*     */     
/*  48 */     if (c2.toString().equalsIgnoreCase(toString()) && 
/*  49 */       c2 instanceof JSClassCompletion) {
/*  50 */       JSClassCompletion jsc2 = (JSClassCompletion)c2;
/*  51 */       return getReplacementText().compareTo(jsc2.getReplacementText());
/*     */     } 
/*     */     
/*  54 */     return super.compareTo(c2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  60 */     return (obj instanceof JSClassCompletion && ((JSClassCompletion)obj)
/*  61 */       .getReplacementText().equals(getReplacementText()));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlreadyEntered(JTextComponent comp) {
/*  66 */     String temp = getProvider().getAlreadyEnteredText(comp);
/*     */     
/*  68 */     int lastDot = JavaScriptHelper.findLastIndexOfJavaScriptIdentifier(temp);
/*  69 */     if (lastDot > -1) {
/*  70 */       return temp.substring(lastDot + 1);
/*     */     }
/*  72 */     if (temp.contains("new"))
/*     */     {
/*  74 */       return "";
/*     */     }
/*     */     
/*  77 */     return temp;
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
/*     */   public String getClassName(boolean fullyQualified) {
/*  89 */     return ((SourceCompletionProvider)getProvider()).getTypesFactory().convertJavaScriptType(this.cf.getClassName(fullyQualified), fullyQualified);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getIcon() {
/*  95 */     return IconFactory.getIcon("default_class");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPackageName() {
/* 106 */     return this.cf.getPackageName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/* 113 */     SourceCompletionProvider scp = (SourceCompletionProvider)getProvider();
/* 114 */     SourceLocation loc = scp.getSourceLocForClass(this.cf.getClassName(true));
/*     */     
/* 116 */     if (loc != null) {
/*     */       
/* 118 */       CompilationUnit cu = Util.getCompilationUnitFromDisk(loc, this.cf);
/* 119 */       if (cu != null) {
/* 120 */         Iterator<TypeDeclaration> i = cu.getTypeDeclarationIterator();
/* 121 */         while (i.hasNext()) {
/* 122 */           TypeDeclaration td = i.next();
/* 123 */           String typeName = td.getName();
/*     */           
/* 125 */           if (typeName.equals(this.cf.getClassName(false))) {
/* 126 */             String summary = td.getDocComment();
/*     */             
/* 128 */             if (summary != null && summary.startsWith("/**")) {
/* 129 */               return Util.docCommentToHtml(summary);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 138 */     return ((SourceCompletionProvider)getProvider()).getTypesFactory().convertJavaScriptType(this.cf.getClassName(true), this.qualified);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getToolTipText() {
/* 145 */     return "type " + getReplacementText();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 151 */     return getReplacementText().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public void rendererText(Graphics g, int x, int y, boolean selected) {
/* 156 */     String s = this.cf.getClassName(false);
/* 157 */     g.drawString(s, x, y);
/* 158 */     FontMetrics fm = g.getFontMetrics();
/* 159 */     int newX = x + fm.stringWidth(s);
/* 160 */     if (this.cf.isDeprecated()) {
/* 161 */       int midY = y + fm.getDescent() - fm.getHeight() / 2;
/* 162 */       g.drawLine(x, midY, newX, midY);
/*     */     } 
/* 164 */     x = newX;
/*     */     
/* 166 */     if (this.qualified) {
/*     */       
/* 168 */       s = " - ";
/* 169 */       g.drawString(s, x, y);
/* 170 */       x += fm.stringWidth(s);
/*     */       
/* 172 */       String pkgName = this.cf.getClassName(true);
/* 173 */       int lastIndexOf = pkgName.lastIndexOf('.');
/* 174 */       if (lastIndexOf != -1) {
/* 175 */         pkgName = pkgName.substring(0, lastIndexOf);
/* 176 */         Color origColor = g.getColor();
/* 177 */         if (!selected) {
/* 178 */           g.setColor(Color.GRAY);
/*     */         }
/* 180 */         g.drawString(pkgName, x, y);
/* 181 */         if (!selected) {
/* 182 */           g.setColor(origColor);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnclosingClassName(boolean fullyQualified) {
/* 192 */     return this.cf.getClassName(fullyQualified);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLookupName() {
/* 198 */     return getReplacementText();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType(boolean qualified) {
/* 204 */     return getClassName(qualified);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\completion\JSClassCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */