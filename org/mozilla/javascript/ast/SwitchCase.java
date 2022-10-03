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
/*     */ public class SwitchCase
/*     */   extends AstNode
/*     */ {
/*     */   private AstNode expression;
/*     */   private List<AstNode> statements;
/*     */   
/*     */   public SwitchCase() {}
/*     */   
/*     */   public SwitchCase(int pos) {
/*  43 */     super(pos);
/*     */   }
/*     */   
/*     */   public SwitchCase(int pos, int len) {
/*  47 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getExpression() {
/*  54 */     return this.expression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpression(AstNode expression) {
/*  65 */     this.expression = expression;
/*  66 */     if (expression != null) {
/*  67 */       expression.setParent(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDefault() {
/*  75 */     return (this.expression == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<AstNode> getStatements() {
/*  82 */     return this.statements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatements(List<AstNode> statements) {
/*  90 */     if (this.statements != null) {
/*  91 */       this.statements.clear();
/*     */     }
/*  93 */     for (AstNode s : statements) {
/*  94 */       addStatement(s);
/*     */     }
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
/*     */   public void addStatement(AstNode statement) {
/* 108 */     assertNotNull(statement);
/* 109 */     if (this.statements == null) {
/* 110 */       this.statements = new ArrayList<AstNode>();
/*     */     }
/* 112 */     int end = statement.getPosition() + statement.getLength();
/* 113 */     setLength(end - getPosition());
/* 114 */     this.statements.add(statement);
/* 115 */     statement.setParent(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 120 */     StringBuilder sb = new StringBuilder();
/* 121 */     sb.append(makeIndent(depth));
/* 122 */     if (this.expression == null) {
/* 123 */       sb.append("default:\n");
/*     */     } else {
/* 125 */       sb.append("case ");
/* 126 */       sb.append(this.expression.toSource(0));
/* 127 */       sb.append(":\n");
/*     */     } 
/* 129 */     if (this.statements != null) {
/* 130 */       for (AstNode s : this.statements) {
/* 131 */         sb.append(s.toSource(depth + 1));
/*     */       }
/*     */     }
/* 134 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 143 */     if (v.visit(this)) {
/* 144 */       if (this.expression != null) {
/* 145 */         this.expression.visit(v);
/*     */       }
/* 147 */       if (this.statements != null)
/* 148 */         for (AstNode s : this.statements)
/* 149 */           s.visit(v);  
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\SwitchCase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */