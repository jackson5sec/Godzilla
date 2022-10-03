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
/*     */ public class GeneratorExpression
/*     */   extends Scope
/*     */ {
/*     */   private AstNode result;
/*  18 */   private List<GeneratorExpressionLoop> loops = new ArrayList<GeneratorExpressionLoop>();
/*     */   
/*     */   private AstNode filter;
/*  21 */   private int ifPosition = -1;
/*  22 */   private int lp = -1;
/*  23 */   private int rp = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GeneratorExpression() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public GeneratorExpression(int pos) {
/*  33 */     super(pos);
/*     */   }
/*     */   
/*     */   public GeneratorExpression(int pos, int len) {
/*  37 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getResult() {
/*  44 */     return this.result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResult(AstNode result) {
/*  52 */     assertNotNull(result);
/*  53 */     this.result = result;
/*  54 */     result.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<GeneratorExpressionLoop> getLoops() {
/*  61 */     return this.loops;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoops(List<GeneratorExpressionLoop> loops) {
/*  69 */     assertNotNull(loops);
/*  70 */     this.loops.clear();
/*  71 */     for (GeneratorExpressionLoop acl : loops) {
/*  72 */       addLoop(acl);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLoop(GeneratorExpressionLoop acl) {
/*  81 */     assertNotNull(acl);
/*  82 */     this.loops.add(acl);
/*  83 */     acl.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getFilter() {
/*  90 */     return this.filter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilter(AstNode filter) {
/*  98 */     this.filter = filter;
/*  99 */     if (filter != null) {
/* 100 */       filter.setParent(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIfPosition() {
/* 107 */     return this.ifPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIfPosition(int ifPosition) {
/* 114 */     this.ifPosition = ifPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFilterLp() {
/* 121 */     return this.lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilterLp(int lp) {
/* 128 */     this.lp = lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFilterRp() {
/* 135 */     return this.rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilterRp(int rp) {
/* 142 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 147 */     StringBuilder sb = new StringBuilder(250);
/* 148 */     sb.append("(");
/* 149 */     sb.append(this.result.toSource(0));
/* 150 */     for (GeneratorExpressionLoop loop : this.loops) {
/* 151 */       sb.append(loop.toSource(0));
/*     */     }
/* 153 */     if (this.filter != null) {
/* 154 */       sb.append(" if (");
/* 155 */       sb.append(this.filter.toSource(0));
/* 156 */       sb.append(")");
/*     */     } 
/* 158 */     sb.append(")");
/* 159 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 168 */     if (!v.visit(this)) {
/*     */       return;
/*     */     }
/* 171 */     this.result.visit(v);
/* 172 */     for (GeneratorExpressionLoop loop : this.loops) {
/* 173 */       loop.visit(v);
/*     */     }
/* 175 */     if (this.filter != null)
/* 176 */       this.filter.visit(v); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\GeneratorExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */