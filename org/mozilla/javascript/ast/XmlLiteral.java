/*    */ package org.mozilla.javascript.ast;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class XmlLiteral
/*    */   extends AstNode
/*    */ {
/* 23 */   private List<XmlFragment> fragments = new ArrayList<XmlFragment>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XmlLiteral() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public XmlLiteral(int pos) {
/* 33 */     super(pos);
/*    */   }
/*    */   
/*    */   public XmlLiteral(int pos, int len) {
/* 37 */     super(pos, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<XmlFragment> getFragments() {
/* 44 */     return this.fragments;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFragments(List<XmlFragment> fragments) {
/* 54 */     assertNotNull(fragments);
/* 55 */     this.fragments.clear();
/* 56 */     for (XmlFragment fragment : fragments) {
/* 57 */       addFragment(fragment);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addFragment(XmlFragment fragment) {
/* 65 */     assertNotNull(fragment);
/* 66 */     this.fragments.add(fragment);
/* 67 */     fragment.setParent(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSource(int depth) {
/* 72 */     StringBuilder sb = new StringBuilder(250);
/* 73 */     for (XmlFragment frag : this.fragments) {
/* 74 */       sb.append(frag.toSource(0));
/*    */     }
/* 76 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(NodeVisitor v) {
/* 84 */     if (v.visit(this))
/* 85 */       for (XmlFragment frag : this.fragments)
/* 86 */         frag.visit(v);  
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\XmlLiteral.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */