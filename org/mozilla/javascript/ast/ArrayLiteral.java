/*     */ package org.mozilla.javascript.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ public class ArrayLiteral
/*     */   extends AstNode
/*     */   implements DestructuringForm
/*     */ {
/*  35 */   private static final List<AstNode> NO_ELEMS = Collections.unmodifiableList(new ArrayList<AstNode>());
/*     */ 
/*     */   
/*     */   private List<AstNode> elements;
/*     */   
/*     */   private int destructuringLength;
/*     */   
/*     */   private int skipCount;
/*     */   
/*     */   private boolean isDestructuring;
/*     */ 
/*     */   
/*     */   public ArrayLiteral() {}
/*     */ 
/*     */   
/*     */   public ArrayLiteral(int pos) {
/*  51 */     super(pos);
/*     */   }
/*     */   
/*     */   public ArrayLiteral(int pos, int len) {
/*  55 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<AstNode> getElements() {
/*  65 */     return (this.elements != null) ? this.elements : NO_ELEMS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setElements(List<AstNode> elements) {
/*  73 */     if (elements == null) {
/*  74 */       this.elements = null;
/*     */     } else {
/*  76 */       if (this.elements != null)
/*  77 */         this.elements.clear(); 
/*  78 */       for (AstNode e : elements) {
/*  79 */         addElement(e);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addElement(AstNode element) {
/*  90 */     assertNotNull(element);
/*  91 */     if (this.elements == null)
/*  92 */       this.elements = new ArrayList<AstNode>(); 
/*  93 */     this.elements.add(element);
/*  94 */     element.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 102 */     return (this.elements == null) ? 0 : this.elements.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getElement(int index) {
/* 112 */     if (this.elements == null)
/* 113 */       throw new IndexOutOfBoundsException("no elements"); 
/* 114 */     return this.elements.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDestructuringLength() {
/* 121 */     return this.destructuringLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestructuringLength(int destructuringLength) {
/* 132 */     this.destructuringLength = destructuringLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSkipCount() {
/* 140 */     return this.skipCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSkipCount(int count) {
/* 148 */     this.skipCount = count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsDestructuring(boolean destructuring) {
/* 157 */     this.isDestructuring = destructuring;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDestructuring() {
/* 166 */     return this.isDestructuring;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 171 */     StringBuilder sb = new StringBuilder();
/* 172 */     sb.append(makeIndent(depth));
/* 173 */     sb.append("[");
/* 174 */     if (this.elements != null) {
/* 175 */       printList(this.elements, sb);
/*     */     }
/* 177 */     sb.append("]");
/* 178 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 188 */     if (v.visit(this))
/* 189 */       for (AstNode e : getElements())
/* 190 */         e.visit(v);  
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ArrayLiteral.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */