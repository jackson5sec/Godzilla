/*     */ package org.fife.rsta.ac.java.tree;
/*     */ 
/*     */ import javax.swing.Icon;
/*     */ import org.fife.rsta.ac.SourceTreeNode;
/*     */ import org.fife.rsta.ac.java.IconFactory;
/*     */ import org.fife.rsta.ac.java.rjc.ast.ASTNode;
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
/*     */ class JavaTreeNode
/*     */   extends SourceTreeNode
/*     */ {
/*     */   private ASTNode astNode;
/*     */   private Icon icon;
/*     */   protected static final int PRIORITY_TYPE = 0;
/*     */   protected static final int PRIORITY_FIELD = 1;
/*     */   protected static final int PRIORITY_CONSTRUCTOR = 2;
/*     */   protected static final int PRIORITY_METHOD = 3;
/*     */   protected static final int PRIORITY_LOCAL_VAR = 4;
/*     */   protected static final int PRIORITY_BOOST_STATIC = -16;
/*     */   
/*     */   protected JavaTreeNode(ASTNode node) {
/*  40 */     this(node, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected JavaTreeNode(ASTNode node, String iconName) {
/*  45 */     this(node, iconName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected JavaTreeNode(ASTNode node, String iconName, boolean sorted) {
/*  50 */     super(node, sorted);
/*  51 */     this.astNode = node;
/*  52 */     if (iconName != null) {
/*  53 */       setIcon(IconFactory.get().getIcon(iconName));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaTreeNode(String text, String iconName) {
/*  59 */     this(text, iconName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaTreeNode(String text, String iconName, boolean sorted) {
/*  64 */     super(text, sorted);
/*  65 */     if (iconName != null) {
/*  66 */       this.icon = IconFactory.get().getIcon(iconName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(SourceTreeNode obj) {
/*  76 */     int res = -1;
/*  77 */     if (obj instanceof JavaTreeNode) {
/*  78 */       JavaTreeNode jtn2 = (JavaTreeNode)obj;
/*  79 */       res = getSortPriority() - jtn2.getSortPriority();
/*  80 */       if (res == 0 && ((SourceTreeNode)getParent()).isSorted()) {
/*  81 */         res = getText(false).compareToIgnoreCase(jtn2.getText(false));
/*     */       }
/*     */     } 
/*  84 */     return res;
/*     */   }
/*     */ 
/*     */   
/*     */   public ASTNode getASTNode() {
/*  89 */     return this.astNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public Icon getIcon() {
/*  94 */     return this.icon;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getText(boolean selected) {
/*  99 */     Object obj = getUserObject();
/* 100 */     return (obj != null) ? obj.toString() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIcon(Icon icon) {
/* 105 */     this.icon = icon;
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
/* 117 */     return getText(false);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\tree\JavaTreeNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */