/*     */ package org.fife.rsta.ac.js.tree;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.event.TreeSelectionEvent;
/*     */ import javax.swing.event.TreeSelectionListener;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import javax.swing.tree.DefaultTreeModel;
/*     */ import javax.swing.tree.TreeNode;
/*     */ import javax.swing.tree.TreePath;
/*     */ import org.fife.rsta.ac.AbstractSourceTree;
/*     */ import org.fife.rsta.ac.LanguageSupport;
/*     */ import org.fife.rsta.ac.LanguageSupportFactory;
/*     */ import org.fife.rsta.ac.js.JavaScriptLanguageSupport;
/*     */ import org.fife.rsta.ac.js.JavaScriptParser;
/*     */ import org.fife.ui.rsyntaxtextarea.DocumentRange;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*     */ import org.mozilla.javascript.ast.AstRoot;
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
/*     */ 
/*     */ public class JavaScriptOutlineTree
/*     */   extends AbstractSourceTree
/*     */ {
/*     */   private DefaultTreeModel model;
/*     */   private RSyntaxTextArea textArea;
/*     */   private JavaScriptParser parser;
/*     */   private Listener listener;
/*     */   static final int PRIORITY_FUNCTION = 1;
/*     */   static final int PRIORITY_VARIABLE = 2;
/*     */   
/*     */   public JavaScriptOutlineTree() {
/*  65 */     this(false);
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
/*     */   public JavaScriptOutlineTree(boolean sorted) {
/*  78 */     setSorted(sorted);
/*  79 */     setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
/*  80 */     setRootVisible(false);
/*  81 */     setCellRenderer(new JavaScriptTreeCellRenderer());
/*  82 */     this.model = new DefaultTreeModel(new DefaultMutableTreeNode("Nothing"));
/*  83 */     setModel(this.model);
/*  84 */     this.listener = new Listener();
/*  85 */     addTreeSelectionListener(this.listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkForJavaScriptParsing() {
/*  96 */     if (this.parser != null) {
/*  97 */       this.parser.removePropertyChangeListener("AST", this.listener);
/*     */       
/*  99 */       this.parser = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 104 */     LanguageSupportFactory lsf = LanguageSupportFactory.get();
/* 105 */     LanguageSupport support = lsf.getSupportFor("text/javascript");
/*     */     
/* 107 */     JavaScriptLanguageSupport jls = (JavaScriptLanguageSupport)support;
/*     */ 
/*     */     
/* 110 */     this.parser = jls.getParser(this.textArea);
/* 111 */     if (this.parser != null) {
/* 112 */       this.parser.addPropertyChangeListener("AST", this.listener);
/*     */ 
/*     */       
/* 115 */       AstRoot ast = this.parser.getAstRoot();
/* 116 */       update(ast);
/*     */     } else {
/*     */       
/* 119 */       update((AstRoot)null);
/*     */     } 
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
/*     */   public void expandInitialNodes() {
/* 132 */     int j = 0;
/* 133 */     while (j < getRowCount()) {
/* 134 */       collapseRow(j++);
/*     */     }
/*     */ 
/*     */     
/* 138 */     expandRow(0);
/* 139 */     j = 1;
/* 140 */     while (j < getRowCount()) {
/* 141 */       TreePath path = getPathForRow(j);
/*     */ 
/*     */       
/* 144 */       expandPath(path);
/*     */       
/* 146 */       j++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void gotoElementAtPath(TreePath path) {
/* 153 */     Object node = path.getLastPathComponent();
/* 154 */     if (node instanceof JavaScriptTreeNode) {
/* 155 */       JavaScriptTreeNode jstn = (JavaScriptTreeNode)node;
/* 156 */       int len = jstn.getLength();
/* 157 */       if (len > -1) {
/* 158 */         int offs = jstn.getOffset();
/* 159 */         DocumentRange range = new DocumentRange(offs, offs + len);
/* 160 */         RSyntaxUtilities.selectAndPossiblyCenter((JTextArea)this.textArea, range, true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean gotoSelectedElement() {
/* 171 */     TreePath path = getLeadSelectionPath();
/* 172 */     if (path != null) {
/* 173 */       gotoElementAtPath(path);
/* 174 */       return true;
/*     */     } 
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void listenTo(RSyntaxTextArea textArea) {
/* 186 */     if (this.textArea != null) {
/* 187 */       uninstall();
/*     */     }
/*     */ 
/*     */     
/* 191 */     if (textArea == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 196 */     this.textArea = textArea;
/* 197 */     textArea.addPropertyChangeListener("RSTA.syntaxStyle", this.listener);
/*     */ 
/*     */ 
/*     */     
/* 201 */     checkForJavaScriptParsing();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void uninstall() {
/* 212 */     if (this.parser != null) {
/* 213 */       this.parser.removePropertyChangeListener("AST", this.listener);
/*     */       
/* 215 */       this.parser = null;
/*     */     } 
/*     */     
/* 218 */     if (this.textArea != null) {
/* 219 */       this.textArea.removePropertyChangeListener("RSTA.syntaxStyle", this.listener);
/*     */       
/* 221 */       this.textArea = null;
/*     */     } 
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
/*     */   private void update(AstRoot ast) {
/* 234 */     JavaScriptOutlineTreeGenerator generator = new JavaScriptOutlineTreeGenerator(this.textArea, ast);
/*     */     
/* 236 */     JavaScriptTreeNode root = generator.getTreeRoot();
/* 237 */     this.model.setRoot((TreeNode)root);
/* 238 */     root.setSorted(isSorted());
/* 239 */     refresh();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateUI() {
/* 248 */     super.updateUI();
/*     */ 
/*     */     
/* 251 */     setCellRenderer(new JavaScriptTreeCellRenderer());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class Listener
/*     */     implements PropertyChangeListener, TreeSelectionListener
/*     */   {
/*     */     private Listener() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 269 */       String name = e.getPropertyName();
/*     */ 
/*     */       
/* 272 */       if ("RSTA.syntaxStyle".equals(name)) {
/* 273 */         JavaScriptOutlineTree.this.checkForJavaScriptParsing();
/*     */       
/*     */       }
/* 276 */       else if ("AST".equals(name)) {
/* 277 */         AstRoot ast = (AstRoot)e.getNewValue();
/* 278 */         JavaScriptOutlineTree.this.update(ast);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void valueChanged(TreeSelectionEvent e) {
/* 289 */       if (JavaScriptOutlineTree.this.getGotoSelectedElementOnClick()) {
/*     */         
/* 291 */         TreePath newPath = e.getNewLeadSelectionPath();
/* 292 */         if (newPath != null)
/* 293 */           JavaScriptOutlineTree.this.gotoElementAtPath(newPath); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\tree\JavaScriptOutlineTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */