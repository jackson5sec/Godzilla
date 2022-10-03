/*     */ package org.mozilla.javascript.ast;
/*     */ 
/*     */ import org.mozilla.javascript.Node;
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
/*     */ public class Jump
/*     */   extends AstNode
/*     */ {
/*     */   public Node target;
/*     */   private Node target2;
/*     */   private Jump jumpNode;
/*     */   
/*     */   public Jump() {
/*  26 */     this.type = -1;
/*     */   }
/*     */   
/*     */   public Jump(int nodeType) {
/*  30 */     this.type = nodeType;
/*     */   }
/*     */   
/*     */   public Jump(int type, int lineno) {
/*  34 */     this(type);
/*  35 */     setLineno(lineno);
/*     */   }
/*     */   
/*     */   public Jump(int type, Node child) {
/*  39 */     this(type);
/*  40 */     addChildToBack(child);
/*     */   }
/*     */   
/*     */   public Jump(int type, Node child, int lineno) {
/*  44 */     this(type, child);
/*  45 */     setLineno(lineno);
/*     */   }
/*     */ 
/*     */   
/*     */   public Jump getJumpStatement() {
/*  50 */     if (this.type != 120 && this.type != 121) codeBug(); 
/*  51 */     return this.jumpNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setJumpStatement(Jump jumpStatement) {
/*  56 */     if (this.type != 120 && this.type != 121) codeBug(); 
/*  57 */     if (jumpStatement == null) codeBug(); 
/*  58 */     if (this.jumpNode != null) codeBug(); 
/*  59 */     this.jumpNode = jumpStatement;
/*     */   }
/*     */ 
/*     */   
/*     */   public Node getDefault() {
/*  64 */     if (this.type != 114) codeBug(); 
/*  65 */     return this.target2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefault(Node defaultTarget) {
/*  70 */     if (this.type != 114) codeBug(); 
/*  71 */     if (defaultTarget.getType() != 131) codeBug(); 
/*  72 */     if (this.target2 != null) codeBug(); 
/*  73 */     this.target2 = defaultTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   public Node getFinally() {
/*  78 */     if (this.type != 81) codeBug(); 
/*  79 */     return this.target2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFinally(Node finallyTarget) {
/*  84 */     if (this.type != 81) codeBug(); 
/*  85 */     if (finallyTarget.getType() != 131) codeBug(); 
/*  86 */     if (this.target2 != null) codeBug(); 
/*  87 */     this.target2 = finallyTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   public Jump getLoop() {
/*  92 */     if (this.type != 130) codeBug(); 
/*  93 */     return this.jumpNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLoop(Jump loop) {
/*  98 */     if (this.type != 130) codeBug(); 
/*  99 */     if (loop == null) codeBug(); 
/* 100 */     if (this.jumpNode != null) codeBug(); 
/* 101 */     this.jumpNode = loop;
/*     */   }
/*     */ 
/*     */   
/*     */   public Node getContinue() {
/* 106 */     if (this.type != 132) codeBug(); 
/* 107 */     return this.target2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContinue(Node continueTarget) {
/* 112 */     if (this.type != 132) codeBug(); 
/* 113 */     if (continueTarget.getType() != 131) codeBug(); 
/* 114 */     if (this.target2 != null) codeBug(); 
/* 115 */     this.target2 = continueTarget;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor visitor) {
/* 125 */     throw new UnsupportedOperationException(toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/* 130 */     throw new UnsupportedOperationException(toString());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\Jump.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */