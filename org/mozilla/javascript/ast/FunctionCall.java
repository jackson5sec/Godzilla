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
/*     */ public class FunctionCall
/*     */   extends AstNode
/*     */ {
/*  20 */   protected static final List<AstNode> NO_ARGS = Collections.unmodifiableList(new ArrayList<AstNode>());
/*     */   
/*     */   protected AstNode target;
/*     */   
/*     */   protected List<AstNode> arguments;
/*  25 */   protected int lp = -1;
/*  26 */   protected int rp = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FunctionCall() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public FunctionCall(int pos) {
/*  36 */     super(pos);
/*     */   }
/*     */   
/*     */   public FunctionCall(int pos, int len) {
/*  40 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AstNode getTarget() {
/*  47 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(AstNode target) {
/*  57 */     assertNotNull(target);
/*  58 */     this.target = target;
/*  59 */     target.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<AstNode> getArguments() {
/*  68 */     return (this.arguments != null) ? this.arguments : NO_ARGS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArguments(List<AstNode> arguments) {
/*  77 */     if (arguments == null) {
/*  78 */       this.arguments = null;
/*     */     } else {
/*  80 */       if (this.arguments != null)
/*  81 */         this.arguments.clear(); 
/*  82 */       for (AstNode arg : arguments) {
/*  83 */         addArgument(arg);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addArgument(AstNode arg) {
/*  94 */     assertNotNull(arg);
/*  95 */     if (this.arguments == null) {
/*  96 */       this.arguments = new ArrayList<AstNode>();
/*     */     }
/*  98 */     this.arguments.add(arg);
/*  99 */     arg.setParent(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLp() {
/* 106 */     return this.lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLp(int lp) {
/* 114 */     this.lp = lp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRp() {
/* 121 */     return this.rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRp(int rp) {
/* 128 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParens(int lp, int rp) {
/* 135 */     this.lp = lp;
/* 136 */     this.rp = rp;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 141 */     StringBuilder sb = new StringBuilder();
/* 142 */     sb.append(makeIndent(depth));
/* 143 */     sb.append(this.target.toSource(0));
/* 144 */     sb.append("(");
/* 145 */     if (this.arguments != null) {
/* 146 */       printList(this.arguments, sb);
/*     */     }
/* 148 */     sb.append(")");
/* 149 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 157 */     if (v.visit(this)) {
/* 158 */       this.target.visit(v);
/* 159 */       for (AstNode arg : getArguments())
/* 160 */         arg.visit(v); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\FunctionCall.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */