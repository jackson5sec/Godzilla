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
/*    */ public class XmlDotQuery
/*    */   extends InfixExpression
/*    */ {
/* 25 */   private int rp = -1;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XmlDotQuery() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public XmlDotQuery(int pos) {
/* 35 */     super(pos);
/*    */   }
/*    */   
/*    */   public XmlDotQuery(int pos, int len) {
/* 39 */     super(pos, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRp() {
/* 50 */     return this.rp;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRp(int rp) {
/* 57 */     this.rp = rp;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 62 */     StringBuilder sb = new StringBuilder();
/* 63 */     sb.append(makeIndent(depth));
/* 64 */     sb.append(getLeft().toSource(0));
/* 65 */     sb.append(".(");
/* 66 */     sb.append(getRight().toSource(0));
/* 67 */     sb.append(")");
/* 68 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\XmlDotQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */