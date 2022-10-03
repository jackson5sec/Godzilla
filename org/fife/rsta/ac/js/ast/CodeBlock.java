/*     */ package org.fife.rsta.ac.js.ast;
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
/*     */ 
/*     */ 
/*     */ public class CodeBlock
/*     */ {
/*     */   private int start;
/*     */   private int end;
/*     */   private CodeBlock parent;
/*     */   private List<CodeBlock> children;
/*     */   private List<JavaScriptVariableDeclaration> varDecs;
/*     */   
/*     */   public CodeBlock(int start) {
/*  44 */     this.start = start;
/*  45 */     this.end = Integer.MAX_VALUE;
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
/*  56 */     CodeBlock child = new CodeBlock(start);
/*  57 */     child.parent = this;
/*  58 */     if (this.children == null) {
/*  59 */       this.children = new ArrayList<>();
/*     */     }
/*  61 */     this.children.add(child);
/*  62 */     return child;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addVariable(JavaScriptVariableDeclaration varDec) {
/*  72 */     if (this.varDecs == null) {
/*  73 */       this.varDecs = new ArrayList<>();
/*     */     }
/*  75 */     this.varDecs.add(varDec);
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
/*  86 */     return (offset >= this.start && offset < this.end);
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
/*  98 */     return this.children.get(index);
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
/* 109 */     return (this.children == null) ? 0 : this.children.size();
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
/* 121 */     return this.end;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CodeBlock getParent() {
/* 131 */     return this.parent;
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
/* 142 */     return this.start;
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
/*     */   public JavaScriptVariableDeclaration getVariableDeclaration(int index) {
/* 154 */     return this.varDecs.get(index);
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
/* 165 */     return (this.varDecs == null) ? 0 : this.varDecs.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndOffset(int end) {
/* 176 */     this.end = end;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartOffSet(int start) {
/* 187 */     this.start = start;
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug() {
/* 192 */     StringBuilder sb = new StringBuilder();
/* 193 */     outputChild(sb, this, 0);
/* 194 */     System.out.println(sb.toString());
/*     */   }
/*     */   
/*     */   private void outputChild(StringBuilder sb, CodeBlock block, int tab) {
/* 198 */     String tabs = ""; int i;
/* 199 */     for (i = 0; i < tab; i++)
/*     */     {
/* 201 */       tabs = tabs + "\t";
/*     */     }
/* 203 */     sb.append(tabs);
/* 204 */     sb.append("start: " + block.getStartOffset() + "\n");
/* 205 */     sb.append(tabs);
/* 206 */     sb.append("end: " + block.getEndOffset() + "\n");
/* 207 */     sb.append(tabs);
/* 208 */     sb.append("var count: " + block.getVariableDeclarationCount() + "\n\n");
/* 209 */     for (i = 0; i < block.getChildCodeBlockCount(); i++) {
/* 210 */       CodeBlock childBlock = block.getChildCodeBlock(i);
/* 211 */       outputChild(sb, childBlock, tab++);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\CodeBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */