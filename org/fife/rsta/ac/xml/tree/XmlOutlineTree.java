/*     */ package org.fife.rsta.ac.xml.tree;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.event.TreeSelectionEvent;
/*     */ import javax.swing.event.TreeSelectionListener;
/*     */ import javax.swing.tree.DefaultTreeModel;
/*     */ import javax.swing.tree.TreeNode;
/*     */ import javax.swing.tree.TreePath;
/*     */ import org.fife.rsta.ac.AbstractSourceTree;
/*     */ import org.fife.rsta.ac.LanguageSupport;
/*     */ import org.fife.rsta.ac.LanguageSupportFactory;
/*     */ import org.fife.rsta.ac.xml.XmlLanguageSupport;
/*     */ import org.fife.rsta.ac.xml.XmlParser;
/*     */ import org.fife.ui.rsyntaxtextarea.DocumentRange;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
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
/*     */ 
/*     */ public class XmlOutlineTree
/*     */   extends AbstractSourceTree
/*     */ {
/*     */   private XmlParser parser;
/*     */   private XmlEditorListener listener;
/*     */   private DefaultTreeModel model;
/*     */   private XmlTreeCellRenderer xmlTreeCellRenderer;
/*     */   
/*     */   public XmlOutlineTree() {
/*  62 */     this(false);
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
/*     */   public XmlOutlineTree(boolean sorted) {
/*  75 */     setSorted(sorted);
/*  76 */     setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
/*  77 */     setRootVisible(false);
/*  78 */     this.xmlTreeCellRenderer = new XmlTreeCellRenderer();
/*  79 */     setCellRenderer(this.xmlTreeCellRenderer);
/*  80 */     this.model = new DefaultTreeModel((TreeNode)new XmlTreeNode("Nothing"));
/*  81 */     setModel(this.model);
/*  82 */     this.listener = new XmlEditorListener();
/*  83 */     addTreeSelectionListener(this.listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkForXmlParsing() {
/*  94 */     if (this.parser != null) {
/*  95 */       this.parser.removePropertyChangeListener("XmlAST", this.listener);
/*  96 */       this.parser = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 101 */     LanguageSupportFactory lsf = LanguageSupportFactory.get();
/* 102 */     LanguageSupport support = lsf.getSupportFor("text/xml");
/*     */     
/* 104 */     XmlLanguageSupport xls = (XmlLanguageSupport)support;
/*     */ 
/*     */     
/* 107 */     this.parser = xls.getParser(this.textArea);
/* 108 */     if (this.parser != null) {
/* 109 */       this.parser.addPropertyChangeListener("XmlAST", this.listener);
/*     */       
/* 111 */       XmlTreeNode root = this.parser.getAst();
/* 112 */       update(root);
/*     */     } else {
/*     */       
/* 115 */       update((XmlTreeNode)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void expandInitialNodes() {
/* 126 */     fastExpandAll(new TreePath(getModel().getRoot()), true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void gotoElementAtPath(TreePath path) {
/* 135 */     Object node = path.getLastPathComponent();
/* 136 */     if (node instanceof XmlTreeNode) {
/* 137 */       XmlTreeNode xtn = (XmlTreeNode)node;
/*     */       
/* 139 */       DocumentRange range = new DocumentRange(xtn.getStartOffset(), xtn.getEndOffset());
/* 140 */       RSyntaxUtilities.selectAndPossiblyCenter((JTextArea)this.textArea, range, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean gotoSelectedElement() {
/* 147 */     TreePath path = getLeadSelectionPath();
/* 148 */     if (path != null) {
/* 149 */       gotoElementAtPath(path);
/* 150 */       return true;
/*     */     } 
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void listenTo(RSyntaxTextArea textArea) {
/* 159 */     if (this.textArea != null) {
/* 160 */       uninstall();
/*     */     }
/*     */ 
/*     */     
/* 164 */     if (textArea == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 169 */     this.textArea = textArea;
/* 170 */     textArea.addPropertyChangeListener("RSTA.syntaxStyle", this.listener);
/*     */ 
/*     */     
/* 173 */     checkForXmlParsing();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void uninstall() {
/* 181 */     if (this.parser != null) {
/* 182 */       this.parser.removePropertyChangeListener("XmlAST", this.listener);
/* 183 */       this.parser = null;
/*     */     } 
/*     */     
/* 186 */     if (this.textArea != null) {
/* 187 */       this.textArea.removePropertyChangeListener("RSTA.syntaxStyle", this.listener);
/*     */       
/* 189 */       this.textArea = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void update(XmlTreeNode root) {
/* 196 */     if (root != null) {
/* 197 */       root = (XmlTreeNode)root.cloneWithChildren();
/*     */     }
/* 199 */     this.model.setRoot((TreeNode)root);
/* 200 */     if (root != null) {
/* 201 */       root.setSorted(isSorted());
/*     */     }
/* 203 */     refresh();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateUI() {
/* 212 */     super.updateUI();
/* 213 */     this.xmlTreeCellRenderer = new XmlTreeCellRenderer();
/* 214 */     setCellRenderer(this.xmlTreeCellRenderer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class XmlEditorListener
/*     */     implements PropertyChangeListener, TreeSelectionListener
/*     */   {
/*     */     private XmlEditorListener() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 232 */       String name = e.getPropertyName();
/*     */ 
/*     */       
/* 235 */       if ("RSTA.syntaxStyle".equals(name)) {
/* 236 */         XmlOutlineTree.this.checkForXmlParsing();
/*     */       
/*     */       }
/* 239 */       else if ("XmlAST".equals(name)) {
/* 240 */         XmlTreeNode root = (XmlTreeNode)e.getNewValue();
/* 241 */         XmlOutlineTree.this.update(root);
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
/* 252 */       if (XmlOutlineTree.this.getGotoSelectedElementOnClick()) {
/*     */         
/* 254 */         TreePath newPath = e.getNewLeadSelectionPath();
/* 255 */         if (newPath != null)
/* 256 */           XmlOutlineTree.this.gotoElementAtPath(newPath); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\xml\tree\XmlOutlineTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */