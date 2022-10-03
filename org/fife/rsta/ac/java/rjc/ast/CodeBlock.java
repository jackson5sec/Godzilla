/*     */ package org.fife.rsta.ac.java.rjc.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Modifiers;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Type;
/*     */ import org.fife.rsta.ac.java.rjc.lexer.Offset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   extends AbstractMember
/*     */ {
/*     */   public static final String NAME = "{...}";
/*     */   private CodeBlock parent;
/*     */   private List<CodeBlock> children;
/*     */   private List<LocalVariable> localVars;
/*     */   private boolean isStatic;
/*     */   
/*     */   public CodeBlock(boolean isStatic, Offset startOffs) {
/*  47 */     super("{...}", startOffs);
/*  48 */     this.isStatic = isStatic;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(CodeBlock child) {
/*  53 */     if (this.children == null) {
/*  54 */       this.children = new ArrayList<>();
/*     */     }
/*  56 */     this.children.add(child);
/*  57 */     child.setParent(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLocalVariable(LocalVariable localVar) {
/*  62 */     if (this.localVars == null) {
/*  63 */       this.localVars = new ArrayList<>();
/*     */     }
/*  65 */     this.localVars.add(localVar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsOffset(int offs) {
/*  72 */     return (getNameEndOffset() >= offs && getNameStartOffset() <= offs);
/*     */   }
/*     */ 
/*     */   
/*     */   public CodeBlock getChildBlock(int index) {
/*  77 */     return this.children.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getChildBlockCount() {
/*  82 */     return (this.children == null) ? 0 : this.children.size();
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
/*  96 */     if (!containsOffset(offs)) {
/*  97 */       return null;
/*     */     }
/*  99 */     for (int i = 0; i < getChildBlockCount(); i++) {
/* 100 */       CodeBlock child = getChildBlock(i);
/* 101 */       if (child.containsOffset(offs)) {
/* 102 */         return child.getDeepestCodeBlockContaining(offs);
/*     */       }
/*     */     } 
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDocComment() {
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocalVariable getLocalVar(int index) {
/* 117 */     return this.localVars.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLocalVarCount() {
/* 122 */     return (this.localVars == null) ? 0 : this.localVars.size();
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
/*     */   public List<LocalVariable> getLocalVarsBefore(int offs) {
/* 136 */     List<LocalVariable> vars = new ArrayList<>();
/*     */     
/* 138 */     if (this.localVars != null) {
/* 139 */       for (int i = 0; i < getLocalVarCount(); ) {
/* 140 */         LocalVariable localVar = getLocalVar(i);
/* 141 */         if (localVar.getNameStartOffset() < offs) {
/* 142 */           vars.add(localVar);
/*     */ 
/*     */           
/*     */           i++;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 150 */     if (this.parent != null) {
/* 151 */       vars.addAll(this.parent.getLocalVarsBefore(offs));
/*     */     }
/*     */     
/* 154 */     return vars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Modifiers getModifiers() {
/* 161 */     Modifiers modifiers = new Modifiers();
/* 162 */     if (this.isStatic) {
/* 163 */       modifiers.addModifier(65574);
/*     */     }
/* 165 */     return modifiers;
/*     */   }
/*     */ 
/*     */   
/*     */   public CodeBlock getParent() {
/* 170 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getType() {
/* 181 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeprecated() {
/* 187 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 198 */     return this.isStatic;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setParent(CodeBlock parent) {
/* 203 */     this.parent = parent;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\ast\CodeBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */