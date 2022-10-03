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
/*    */ public class XmlString
/*    */   extends XmlFragment
/*    */ {
/*    */   private String xml;
/*    */   
/*    */   public XmlString() {}
/*    */   
/*    */   public XmlString(int pos) {
/* 22 */     super(pos);
/*    */   }
/*    */   
/*    */   public XmlString(int pos, String s) {
/* 26 */     super(pos);
/* 27 */     setXml(s);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setXml(String s) {
/* 37 */     assertNotNull(s);
/* 38 */     this.xml = s;
/* 39 */     setLength(s.length());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getXml() {
/* 47 */     return this.xml;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 52 */     return makeIndent(depth) + this.xml;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 60 */     v.visit(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\XmlString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */