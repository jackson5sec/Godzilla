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
/*     */ public class TryStatement
/*     */   extends AstNode
/*     */ {
/*  29 */   private static final List<CatchClause> NO_CATCHES = Collections.unmodifiableList(new ArrayList<CatchClause>());
/*     */   
/*     */   private AstNode tryBlock;
/*     */   
/*     */   private List<CatchClause> catchClauses;
/*     */   private AstNode finallyBlock;
/*  35 */   private int finallyPosition = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TryStatement() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public TryStatement(int pos) {
/*  45 */     super(pos);
/*     */   }
/*     */   
/*     */   public TryStatement(int pos, int len) {
/*  49 */     super(pos, len);
/*     */   }
/*     */   
/*     */   public AstNode getTryBlock() {
/*  53 */     return this.tryBlock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTryBlock(AstNode tryBlock) {
/*  61 */     assertNotNull(tryBlock);
/*  62 */     this.tryBlock = tryBlock;
/*  63 */     tryBlock.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<CatchClause> getCatchClauses() {
/*  71 */     return (this.catchClauses != null) ? this.catchClauses : NO_CATCHES;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatchClauses(List<CatchClause> catchClauses) {
/*  80 */     if (catchClauses == null) {
/*  81 */       this.catchClauses = null;
/*     */     } else {
/*  83 */       if (this.catchClauses != null)
/*  84 */         this.catchClauses.clear(); 
/*  85 */       for (CatchClause cc : catchClauses) {
/*  86 */         addCatchClause(cc);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCatchClause(CatchClause clause) {
/*  97 */     assertNotNull(clause);
/*  98 */     if (this.catchClauses == null) {
/*  99 */       this.catchClauses = new ArrayList<CatchClause>();
/*     */     }
/* 101 */     this.catchClauses.add(clause);
/* 102 */     clause.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getFinallyBlock() {
/* 109 */     return this.finallyBlock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFinallyBlock(AstNode finallyBlock) {
/* 117 */     this.finallyBlock = finallyBlock;
/* 118 */     if (finallyBlock != null) {
/* 119 */       finallyBlock.setParent(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFinallyPosition() {
/* 126 */     return this.finallyPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFinallyPosition(int finallyPosition) {
/* 133 */     this.finallyPosition = finallyPosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 138 */     StringBuilder sb = new StringBuilder(250);
/* 139 */     sb.append(makeIndent(depth));
/* 140 */     sb.append("try ");
/* 141 */     sb.append(this.tryBlock.toSource(depth).trim());
/* 142 */     for (CatchClause cc : getCatchClauses()) {
/* 143 */       sb.append(cc.toSource(depth));
/*     */     }
/* 145 */     if (this.finallyBlock != null) {
/* 146 */       sb.append(" finally ");
/* 147 */       sb.append(this.finallyBlock.toSource(depth));
/*     */     } 
/* 149 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 158 */     if (v.visit(this)) {
/* 159 */       this.tryBlock.visit(v);
/* 160 */       for (CatchClause cc : getCatchClauses()) {
/* 161 */         cc.visit(v);
/*     */       }
/* 163 */       if (this.finallyBlock != null)
/* 164 */         this.finallyBlock.visit(v); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\TryStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */