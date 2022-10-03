/*     */ package org.fife.rsta.ac.js.tree;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.text.Position;
/*     */ import org.fife.rsta.ac.SourceTreeNode;
/*     */ import org.fife.rsta.ac.js.util.RhinoUtil;
/*     */ import org.mozilla.javascript.ast.AstNode;
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
/*     */ public class JavaScriptTreeNode
/*     */   extends SourceTreeNode
/*     */ {
/*     */   private Position pos;
/*     */   private String text;
/*     */   private Icon icon;
/*     */   
/*     */   public JavaScriptTreeNode(List<AstNode> userObject) {
/*  48 */     super(userObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaScriptTreeNode(AstNode userObject) {
/*  53 */     this(RhinoUtil.toList(new AstNode[] { userObject }));
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaScriptTreeNode(AstNode userObject, boolean sorted) {
/*  58 */     super(RhinoUtil.toList(new AstNode[] { userObject }, ), sorted);
/*     */   }
/*     */ 
/*     */   
/*     */   public Icon getIcon() {
/*  63 */     return this.icon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLength() {
/*  75 */     int length = 0;
/*  76 */     List<AstNode> nodes = (List<AstNode>)getUserObject();
/*  77 */     for (AstNode node : nodes) {
/*  78 */       length += node.getLength();
/*     */     }
/*  80 */     length += nodes.size() - 1;
/*  81 */     return length;
/*     */   }
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
/*     */   public int getOffset() {
/*  94 */     return this.pos.getOffset();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getText(boolean selected) {
/*  99 */     return this.text;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIcon(Icon icon) {
/* 104 */     this.icon = icon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOffset(Position offs) {
/* 115 */     this.pos = offs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setText(String text) {
/* 125 */     this.text = text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 137 */     return getText(false);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\tree\JavaScriptTreeNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */