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
/*     */ public class SwitchStatement
/*     */   extends Jump
/*     */ {
/*  34 */   private static final List<SwitchCase> NO_CASES = Collections.unmodifiableList(new ArrayList<SwitchCase>());
/*     */   
/*     */   private AstNode expression;
/*     */   
/*     */   private List<SwitchCase> cases;
/*  39 */   private int lp = -1;
/*  40 */   private int rp = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SwitchStatement() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SwitchStatement(int pos) {
/*  51 */     this.position = pos;
/*     */   }
/*     */   
/*     */   public SwitchStatement(int pos, int len) {
/*  55 */     this.position = pos;
/*  56 */     this.length = len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getExpression() {
/*  63 */     return this.expression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpression(AstNode expression) {
/*  72 */     assertNotNull(expression);
/*  73 */     this.expression = expression;
/*  74 */     expression.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<SwitchCase> getCases() {
/*  82 */     return (this.cases != null) ? this.cases : NO_CASES;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCases(List<SwitchCase> cases) {
/*  91 */     if (cases == null) {
/*  92 */       this.cases = null;
/*     */     } else {
/*  94 */       if (this.cases != null)
/*  95 */         this.cases.clear(); 
/*  96 */       for (SwitchCase sc : cases) {
/*  97 */         addCase(sc);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCase(SwitchCase switchCase) {
/* 106 */     assertNotNull(switchCase);
/* 107 */     if (this.cases == null) {
/* 108 */       this.cases = new ArrayList<SwitchCase>();
/*     */     }
/* 110 */     this.cases.add(switchCase);
/* 111 */     switchCase.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLp() {
/* 118 */     return this.lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLp(int lp) {
/* 125 */     this.lp = lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRp() {
/* 132 */     return this.rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRp(int rp) {
/* 139 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParens(int lp, int rp) {
/* 146 */     this.lp = lp;
/* 147 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 152 */     String pad = makeIndent(depth);
/* 153 */     StringBuilder sb = new StringBuilder();
/* 154 */     sb.append(pad);
/* 155 */     sb.append("switch (");
/* 156 */     sb.append(this.expression.toSource(0));
/* 157 */     sb.append(") {\n");
/* 158 */     if (this.cases != null) {
/* 159 */       for (SwitchCase sc : this.cases) {
/* 160 */         sb.append(sc.toSource(depth + 1));
/*     */       }
/*     */     }
/* 163 */     sb.append(pad);
/* 164 */     sb.append("}\n");
/* 165 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 174 */     if (v.visit(this)) {
/* 175 */       this.expression.visit(v);
/* 176 */       for (SwitchCase sc : getCases())
/* 177 */         sc.visit(v); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\SwitchStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */