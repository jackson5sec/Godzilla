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
/*     */ public class ArrayComprehension
/*     */   extends Scope
/*     */ {
/*     */   private AstNode result;
/*  21 */   private List<ArrayComprehensionLoop> loops = new ArrayList<ArrayComprehensionLoop>();
/*     */   
/*     */   private AstNode filter;
/*  24 */   private int ifPosition = -1;
/*  25 */   private int lp = -1;
/*  26 */   private int rp = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayComprehension() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayComprehension(int pos) {
/*  36 */     super(pos);
/*     */   }
/*     */   
/*     */   public ArrayComprehension(int pos, int len) {
/*  40 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getResult() {
/*  47 */     return this.result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResult(AstNode result) {
/*  55 */     assertNotNull(result);
/*  56 */     this.result = result;
/*  57 */     result.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ArrayComprehensionLoop> getLoops() {
/*  64 */     return this.loops;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoops(List<ArrayComprehensionLoop> loops) {
/*  72 */     assertNotNull(loops);
/*  73 */     this.loops.clear();
/*  74 */     for (ArrayComprehensionLoop acl : loops) {
/*  75 */       addLoop(acl);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLoop(ArrayComprehensionLoop acl) {
/*  84 */     assertNotNull(acl);
/*  85 */     this.loops.add(acl);
/*  86 */     acl.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getFilter() {
/*  93 */     return this.filter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilter(AstNode filter) {
/* 101 */     this.filter = filter;
/* 102 */     if (filter != null) {
/* 103 */       filter.setParent(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIfPosition() {
/* 110 */     return this.ifPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIfPosition(int ifPosition) {
/* 117 */     this.ifPosition = ifPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFilterLp() {
/* 124 */     return this.lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilterLp(int lp) {
/* 131 */     this.lp = lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFilterRp() {
/* 138 */     return this.rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilterRp(int rp) {
/* 145 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 150 */     StringBuilder sb = new StringBuilder(250);
/* 151 */     sb.append("[");
/* 152 */     sb.append(this.result.toSource(0));
/* 153 */     for (ArrayComprehensionLoop loop : this.loops) {
/* 154 */       sb.append(loop.toSource(0));
/*     */     }
/* 156 */     if (this.filter != null) {
/* 157 */       sb.append(" if (");
/* 158 */       sb.append(this.filter.toSource(0));
/* 159 */       sb.append(")");
/*     */     } 
/* 161 */     sb.append("]");
/* 162 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 171 */     if (!v.visit(this)) {
/*     */       return;
/*     */     }
/* 174 */     this.result.visit(v);
/* 175 */     for (ArrayComprehensionLoop loop : this.loops) {
/* 176 */       loop.visit(v);
/*     */     }
/* 178 */     if (this.filter != null)
/* 179 */       this.filter.visit(v); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ArrayComprehension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */