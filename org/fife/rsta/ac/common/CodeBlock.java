/*     */ package org.fife.rsta.ac.common;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class CodeBlock
/*     */ {
/*     */   private int start;
/*     */   private int end;
/*     */   private CodeBlock parent;
/*     */   private List<CodeBlock> children;
/*     */   private List<VariableDeclaration> varDecs;
/*     */   
/*     */   public CodeBlock(int start) {
/*  42 */     this.start = start;
/*  43 */     this.end = Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CodeBlock addChildCodeBlock(int start) {
/*  54 */     CodeBlock child = new CodeBlock(start);
/*  55 */     child.parent = this;
/*  56 */     if (this.children == null) {
/*  57 */       this.children = new ArrayList<>();
/*     */     }
/*  59 */     this.children.add(child);
/*  60 */     return child;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addVariable(VariableDeclaration varDec) {
/*  70 */     if (this.varDecs == null) {
/*  71 */       this.varDecs = new ArrayList<>();
/*     */     }
/*  73 */     this.varDecs.add(varDec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(int offset) {
/*  84 */     return (offset >= this.start && offset < this.end);
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
/*     */   public CodeBlock getChildCodeBlock(int index) {
/*  96 */     return this.children.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getChildCodeBlockCount() {
/* 107 */     return (this.children == null) ? 0 : this.children.size();
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
/*     */   public CodeBlock getDeepestCodeBlockContaining(int offs) {
/* 121 */     if (!contains(offs)) {
/* 122 */       return null;
/*     */     }
/* 124 */     for (int i = 0; i < getChildCodeBlockCount(); i++) {
/* 125 */       CodeBlock child = getChildCodeBlock(i);
/* 126 */       if (child.contains(offs)) {
/* 127 */         return child.getDeepestCodeBlockContaining(offs);
/*     */       }
/*     */     } 
/* 130 */     return this;
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
/*     */   public int getEndOffset() {
/* 142 */     return this.end;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CodeBlock getParent() {
/* 152 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStartOffset() {
/* 163 */     return this.start;
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
/*     */   public VariableDeclaration getVariableDeclaration(int index) {
/* 175 */     return this.varDecs.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVariableDeclarationCount() {
/* 186 */     return (this.varDecs == null) ? 0 : this.varDecs.size();
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
/*     */   public List<VariableDeclaration> getVariableDeclarationsBefore(int offs) {
/* 200 */     List<VariableDeclaration> vars = new ArrayList<>();
/*     */     
/* 202 */     int varCount = getVariableDeclarationCount();
/* 203 */     for (int i = 0; i < varCount; ) {
/* 204 */       VariableDeclaration localVar = getVariableDeclaration(i);
/* 205 */       if (localVar.getOffset() < offs) {
/* 206 */         vars.add(localVar);
/*     */ 
/*     */         
/*     */         i++;
/*     */       } 
/*     */     } 
/*     */     
/* 213 */     if (this.parent != null) {
/* 214 */       vars.addAll(this.parent.getVariableDeclarationsBefore(offs));
/*     */     }
/*     */     
/* 217 */     return vars;
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
/*     */   public void setEndOffset(int end) {
/* 229 */     this.end = end;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\common\CodeBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */