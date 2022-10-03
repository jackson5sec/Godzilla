/*     */ package org.fife.rsta.ac.js.completion;
/*     */ 
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
/*     */ import org.fife.rsta.ac.js.ast.JavaScriptVariableDeclaration;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.VariableCompletion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSVariableCompletion
/*     */   extends VariableCompletion
/*     */   implements JSCompletionUI
/*     */ {
/*     */   private JavaScriptVariableDeclaration dec;
/*     */   private boolean localVariable;
/*     */   
/*     */   public JSVariableCompletion(CompletionProvider provider, JavaScriptVariableDeclaration dec) {
/*  41 */     this(provider, dec, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JSVariableCompletion(CompletionProvider provider, JavaScriptVariableDeclaration dec, boolean localVariable) {
/*  47 */     super(provider, dec.getName(), dec.getJavaScriptTypeName());
/*  48 */     this.dec = dec;
/*  49 */     this.localVariable = localVariable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/*  58 */     return getType(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType(boolean qualified) {
/*  67 */     return ((SourceCompletionProvider)getProvider()).getTypesFactory().convertJavaScriptType(this.dec.getJavaScriptTypeName(), qualified);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlreadyEntered(JTextComponent comp) {
/*  74 */     String temp = getProvider().getAlreadyEnteredText(comp);
/*     */     
/*  76 */     int lastDot = JavaScriptHelper.findLastIndexOfJavaScriptIdentifier(temp);
/*  77 */     if (lastDot > -1) {
/*  78 */       temp = temp.substring(lastDot + 1);
/*     */     }
/*  80 */     return temp;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getIcon() {
/*  86 */     return 
/*  87 */       IconFactory.getIcon(this.localVariable ? "local_variable" : "global_variable");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRelevance() {
/*  94 */     return this.localVariable ? 9 : 8;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 100 */     if (obj == this) {
/* 101 */       return true;
/*     */     }
/* 103 */     if (obj instanceof VariableCompletion) {
/* 104 */       VariableCompletion comp = (VariableCompletion)obj;
/* 105 */       return getName().equals(comp.getName());
/*     */     } 
/*     */     
/* 108 */     return super.equals(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Completion c2) {
/* 113 */     if (c2 == this) {
/* 114 */       return 0;
/*     */     }
/* 116 */     return super.compareTo(c2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 122 */     return getName().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/* 128 */     SourceCompletionProvider scp = (SourceCompletionProvider)getProvider();
/* 129 */     ClassFile cf = scp.getJavaScriptTypesFactory().getClassFile(scp.getJarManager(), JavaScriptHelper.createNewTypeDeclaration(getType(true)));
/* 130 */     if (cf != null) {
/*     */       
/* 132 */       SourceLocation loc = scp.getSourceLocForClass(cf.getClassName(true));
/*     */       
/* 134 */       if (loc != null) {
/*     */         
/* 136 */         CompilationUnit cu = Util.getCompilationUnitFromDisk(loc, cf);
/* 137 */         if (cu != null) {
/* 138 */           Iterator<TypeDeclaration> i = cu.getTypeDeclarationIterator();
/* 139 */           while (i.hasNext()) {
/* 140 */             TypeDeclaration td = i.next();
/* 141 */             String typeName = td.getName();
/*     */             
/* 143 */             if (typeName.equals(cf.getClassName(false))) {
/* 144 */               String summary = td.getDocComment();
/*     */               
/* 146 */               if (summary != null && summary.startsWith("/**")) {
/* 147 */                 return Util.docCommentToHtml(summary);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 154 */       return cf.getClassName(true);
/*     */     } 
/*     */     
/* 157 */     return super.getSummary();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\completion\JSVariableCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */