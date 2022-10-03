/*     */ package org.mozilla.javascript.ast;
/*     */ 
/*     */ import org.mozilla.javascript.Token;
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
/*     */ public class Comment
/*     */   extends AstNode
/*     */ {
/*     */   private String value;
/*     */   private Token.CommentType commentType;
/*     */   
/*     */   public Comment(int pos, int len, Token.CommentType type, String value) {
/*  58 */     super(pos, len);
/*  59 */     this.commentType = type;
/*  60 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Token.CommentType getCommentType() {
/*  67 */     return this.commentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommentType(Token.CommentType type) {
/*  76 */     this.commentType = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  83 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/*  88 */     StringBuilder sb = new StringBuilder(getLength() + 10);
/*  89 */     sb.append(makeIndent(depth));
/*  90 */     sb.append(this.value);
/*  91 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 100 */     v.visit(this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\Comment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */