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
/*     */ public class ObjectLiteral
/*     */   extends AstNode
/*     */   implements DestructuringForm
/*     */ {
/*  35 */   private static final List<ObjectProperty> NO_ELEMS = Collections.unmodifiableList(new ArrayList<ObjectProperty>());
/*     */ 
/*     */   
/*     */   private List<ObjectProperty> elements;
/*     */ 
/*     */   
/*     */   boolean isDestructuring;
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectLiteral() {}
/*     */ 
/*     */   
/*     */   public ObjectLiteral(int pos) {
/*  49 */     super(pos);
/*     */   }
/*     */   
/*     */   public ObjectLiteral(int pos, int len) {
/*  53 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ObjectProperty> getElements() {
/*  61 */     return (this.elements != null) ? this.elements : NO_ELEMS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setElements(List<ObjectProperty> elements) {
/*  70 */     if (elements == null) {
/*  71 */       this.elements = null;
/*     */     } else {
/*  73 */       if (this.elements != null)
/*  74 */         this.elements.clear(); 
/*  75 */       for (ObjectProperty o : elements) {
/*  76 */         addElement(o);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addElement(ObjectProperty element) {
/*  86 */     assertNotNull(element);
/*  87 */     if (this.elements == null) {
/*  88 */       this.elements = new ArrayList<ObjectProperty>();
/*     */     }
/*  90 */     this.elements.add(element);
/*  91 */     element.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIsDestructuring(boolean destructuring) {
/* 100 */     this.isDestructuring = destructuring;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDestructuring() {
/* 109 */     return this.isDestructuring;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 114 */     StringBuilder sb = new StringBuilder();
/* 115 */     sb.append(makeIndent(depth));
/* 116 */     sb.append("{");
/* 117 */     if (this.elements != null) {
/* 118 */       printList(this.elements, sb);
/*     */     }
/* 120 */     sb.append("}");
/* 121 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 130 */     if (v.visit(this))
/* 131 */       for (ObjectProperty prop : getElements())
/* 132 */         prop.visit(v);  
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ObjectLiteral.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */