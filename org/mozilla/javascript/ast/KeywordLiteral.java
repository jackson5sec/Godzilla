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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KeywordLiteral
/*     */   extends AstNode
/*     */ {
/*     */   public KeywordLiteral() {}
/*     */   
/*     */   public KeywordLiteral(int pos) {
/*  27 */     super(pos);
/*     */   }
/*     */   
/*     */   public KeywordLiteral(int pos, int len) {
/*  31 */     super(pos, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeywordLiteral(int pos, int len, int nodeType) {
/*  39 */     super(pos, len);
/*  40 */     setType(nodeType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeywordLiteral setType(int nodeType) {
/*  49 */     if (nodeType != 43 && nodeType != 42 && nodeType != 45 && nodeType != 44 && nodeType != 160)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*  54 */       throw new IllegalArgumentException("Invalid node type: " + nodeType);
/*     */     }
/*  56 */     this.type = nodeType;
/*  57 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBooleanLiteral() {
/*  65 */     return (this.type == 45 || this.type == 44);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSource(int depth) {
/*  70 */     StringBuilder sb = new StringBuilder();
/*  71 */     sb.append(makeIndent(depth));
/*  72 */     switch (getType()) {
/*     */       case 43:
/*  74 */         sb.append("this");
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
/*  92 */         return sb.toString();case 42: sb.append("null"); return sb.toString();case 45: sb.append("true"); return sb.toString();case 44: sb.append("false"); return sb.toString();case 160: sb.append("debugger;\n"); return sb.toString();
/*     */     } 
/*     */     throw new IllegalStateException("Invalid keyword literal type: " + getType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(NodeVisitor v) {
/* 100 */     v.visit(this);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\KeywordLiteral.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */