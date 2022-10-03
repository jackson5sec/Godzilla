/*    */ package org.mozilla.javascript.ast;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Loop
/*    */   extends Scope
/*    */ {
/*    */   protected AstNode body;
/* 15 */   protected int lp = -1;
/* 16 */   protected int rp = -1;
/*    */ 
/*    */   
/*    */   public Loop() {}
/*    */   
/*    */   public Loop(int pos) {
/* 22 */     super(pos);
/*    */   }
/*    */   
/*    */   public Loop(int pos, int len) {
/* 26 */     super(pos, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AstNode getBody() {
/* 33 */     return this.body;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBody(AstNode body) {
/* 42 */     this.body = body;
/* 43 */     int end = body.getPosition() + body.getLength();
/* 44 */     setLength(end - getPosition());
/* 45 */     body.setParent(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getLp() {
/* 52 */     return this.lp;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLp(int lp) {
/* 59 */     this.lp = lp;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRp() {
/* 66 */     return this.rp;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRp(int rp) {
/* 73 */     this.rp = rp;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setParens(int lp, int rp) {
/* 80 */     this.lp = lp;
/* 81 */     this.rp = rp;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\Loop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */