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
/*    */ public class XmlExpression
/*    */   extends XmlFragment
/*    */ {
/*    */   private AstNode expression;
/*    */   private boolean isXmlAttribute;
/*    */   
/*    */   public XmlExpression() {}
/*    */   
/*    */   public XmlExpression(int pos) {
/* 25 */     super(pos);
/*    */   }
/*    */   
/*    */   public XmlExpression(int pos, int len) {
/* 29 */     super(pos, len);
/*    */   }
/*    */   
/*    */   public XmlExpression(int pos, AstNode expr) {
/* 33 */     super(pos);
/* 34 */     setExpression(expr);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AstNode getExpression() {
/* 41 */     return this.expression;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setExpression(AstNode expression) {
/* 49 */     assertNotNull(expression);
/* 50 */     this.expression = expression;
/* 51 */     expression.setParent(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isXmlAttribute() {
/* 58 */     return this.isXmlAttribute;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setIsXmlAttribute(boolean isXmlAttribute) {
/* 65 */     this.isXmlAttribute = isXmlAttribute;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 70 */     return makeIndent(depth) + "{" + this.expression.toSource(depth) + "}";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 78 */     if (v.visit(this))
/* 79 */       this.expression.visit(v); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\XmlExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */