/*     */ package org.mozilla.javascript.ast;
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
/*     */ public class LabeledStatement
/*     */   extends AstNode
/*     */ {
/*  23 */   private List<Label> labels = new ArrayList<Label>();
/*     */ 
/*     */   
/*     */   private AstNode statement;
/*     */ 
/*     */ 
/*     */   
/*     */   public LabeledStatement() {}
/*     */ 
/*     */   
/*     */   public LabeledStatement(int pos) {
/*  34 */     super(pos);
/*     */   }
/*     */   
/*     */   public LabeledStatement(int pos, int len) {
/*  38 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Label> getLabels() {
/*  45 */     return this.labels;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLabels(List<Label> labels) {
/*  54 */     assertNotNull(labels);
/*  55 */     if (this.labels != null)
/*  56 */       this.labels.clear(); 
/*  57 */     for (Label l : labels) {
/*  58 */       addLabel(l);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLabel(Label label) {
/*  67 */     assertNotNull(label);
/*  68 */     this.labels.add(label);
/*  69 */     label.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getStatement() {
/*  76 */     return this.statement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Label getLabelByName(String name) {
/*  85 */     for (Label label : this.labels) {
/*  86 */       if (name.equals(label.getName())) {
/*  87 */         return label;
/*     */       }
/*     */     } 
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatement(AstNode statement) {
/*  98 */     assertNotNull(statement);
/*  99 */     this.statement = statement;
/* 100 */     statement.setParent(this);
/*     */   }
/*     */   
/*     */   public Label getFirstLabel() {
/* 104 */     return this.labels.get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSideEffects() {
/* 110 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 115 */     StringBuilder sb = new StringBuilder();
/* 116 */     for (Label label : this.labels) {
/* 117 */       sb.append(label.toSource(depth));
/*     */     }
/* 119 */     sb.append(this.statement.toSource(depth + 1));
/* 120 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 129 */     if (v.visit(this)) {
/* 130 */       for (AstNode label : this.labels) {
/* 131 */         label.visit(v);
/*     */       }
/* 133 */       this.statement.visit(v);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\LabeledStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */