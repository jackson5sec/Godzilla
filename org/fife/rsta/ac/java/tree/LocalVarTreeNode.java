/*    */ package org.fife.rsta.ac.java.tree;
/*    */ 
/*    */ import org.fife.rsta.ac.java.IconFactory;
/*    */ import org.fife.rsta.ac.java.rjc.ast.ASTNode;
/*    */ import org.fife.rsta.ac.java.rjc.ast.LocalVariable;
/*    */ import org.fife.ui.autocomplete.Util;
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
/*    */ class LocalVarTreeNode
/*    */   extends JavaTreeNode
/*    */ {
/*    */   private String text;
/*    */   
/*    */   public LocalVarTreeNode(LocalVariable var) {
/* 31 */     super((ASTNode)var);
/* 32 */     setIcon(IconFactory.get().getIcon("localVariableIcon"));
/* 33 */     setSortPriority(4);
/*    */     
/* 35 */     StringBuilder sb = new StringBuilder();
/* 36 */     sb.append("<html>");
/* 37 */     sb.append(var.getName());
/* 38 */     sb.append(" : ");
/* 39 */     sb.append("<font color='#888888'>");
/* 40 */     MemberTreeNode.appendType(var.getType(), sb);
/* 41 */     this.text = sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getText(boolean selected) {
/* 48 */     return selected ? Util.stripHtml(this.text)
/* 49 */       .replaceAll("&lt;", "<").replaceAll("&gt;", ">") : this.text;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\tree\LocalVarTreeNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */