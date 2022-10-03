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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XmlMemberGet
/*    */   extends InfixExpression
/*    */ {
/*    */   public XmlMemberGet() {}
/*    */   
/*    */   public XmlMemberGet(int pos) {
/* 29 */     super(pos);
/*    */   }
/*    */   
/*    */   public XmlMemberGet(int pos, int len) {
/* 33 */     super(pos, len);
/*    */   }
/*    */   
/*    */   public XmlMemberGet(int pos, int len, AstNode target, XmlRef ref) {
/* 37 */     super(pos, len, target, ref);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XmlMemberGet(AstNode target, XmlRef ref) {
/* 45 */     super(target, ref);
/*    */   }
/*    */   
/*    */   public XmlMemberGet(AstNode target, XmlRef ref, int opPos) {
/* 49 */     super(143, target, ref, opPos);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AstNode getTarget() {
/* 57 */     return getLeft();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTarget(AstNode target) {
/* 65 */     setLeft(target);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XmlRef getMemberRef() {
/* 73 */     return (XmlRef)getRight();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setProperty(XmlRef ref) {
/* 82 */     setRight(ref);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 87 */     StringBuilder sb = new StringBuilder();
/* 88 */     sb.append(makeIndent(depth));
/* 89 */     sb.append(getLeft().toSource(0));
/* 90 */     sb.append(operatorToString(getType()));
/* 91 */     sb.append(getRight().toSource(0));
/* 92 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\XmlMemberGet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */