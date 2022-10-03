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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RegExpLiteral
/*    */   extends AstNode
/*    */ {
/*    */   private String value;
/*    */   private String flags;
/*    */   
/*    */   public RegExpLiteral() {}
/*    */   
/*    */   public RegExpLiteral(int pos) {
/* 28 */     super(pos);
/*    */   }
/*    */   
/*    */   public RegExpLiteral(int pos, int len) {
/* 32 */     super(pos, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 39 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(String value) {
/* 47 */     assertNotNull(value);
/* 48 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getFlags() {
/* 55 */     return this.flags;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFlags(String flags) {
/* 62 */     this.flags = flags;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 67 */     return makeIndent(depth) + "/" + this.value + "/" + ((this.flags == null) ? "" : this.flags);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 76 */     v.visit(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\RegExpLiteral.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */