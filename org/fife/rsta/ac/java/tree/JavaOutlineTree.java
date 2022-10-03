/*     */ package org.fife.rsta.ac.java.tree;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.Iterator;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.event.TreeSelectionEvent;
/*     */ import javax.swing.event.TreeSelectionListener;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import javax.swing.tree.DefaultTreeModel;
/*     */ import javax.swing.tree.MutableTreeNode;
/*     */ import javax.swing.tree.TreeNode;
/*     */ import javax.swing.tree.TreePath;
/*     */ import org.fife.rsta.ac.AbstractSourceTree;
/*     */ import org.fife.rsta.ac.LanguageSupport;
/*     */ import org.fife.rsta.ac.LanguageSupportFactory;
/*     */ import org.fife.rsta.ac.java.JavaLanguageSupport;
/*     */ import org.fife.rsta.ac.java.JavaParser;
/*     */ import org.fife.rsta.ac.java.rjc.ast.ASTNode;
/*     */ import org.fife.rsta.ac.java.rjc.ast.CodeBlock;
/*     */ import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Field;
/*     */ import org.fife.rsta.ac.java.rjc.ast.ImportDeclaration;
/*     */ import org.fife.rsta.ac.java.rjc.ast.LocalVariable;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Member;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Method;
/*     */ import org.fife.rsta.ac.java.rjc.ast.NormalClassDeclaration;
/*     */ import org.fife.rsta.ac.java.rjc.ast.NormalInterfaceDeclaration;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Package;
/*     */ import org.fife.rsta.ac.java.rjc.ast.TypeDeclaration;
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
/*     */ public class JavaOutlineTree
/*     */   extends AbstractSourceTree
/*     */ {
/*     */   private DefaultTreeModel model;
/*     */   private RSyntaxTextArea textArea;
/*     */   private JavaParser parser;
/*     */   private Listener listener;
/*     */   
/*     */   public JavaOutlineTree() {
/*  77 */     this(false);
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
/*     */   public JavaOutlineTree(boolean sorted) {
/*  90 */     setSorted(sorted);
/*  91 */     setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
/*  92 */     setRootVisible(false);
/*  93 */     setCellRenderer(new AstTreeCellRenderer());
/*  94 */     this.model = new DefaultTreeModel(new DefaultMutableTreeNode("Nothing"));
/*  95 */     setModel(this.model);
/*  96 */     this.listener = new Listener();
/*  97 */     addTreeSelectionListener(this.listener);
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
/*     */   private void update(CompilationUnit cu) {
/* 109 */     JavaTreeNode root = new JavaTreeNode("Remove me!", "sourceFileIcon");
/*     */     
/* 111 */     root.setSortable(false);
/* 112 */     if (cu == null) {
/* 113 */       this.model.setRoot((TreeNode)root);
/*     */       
/*     */       return;
/*     */     } 
/* 117 */     Package pkg = cu.getPackage();
/* 118 */     if (pkg != null) {
/* 119 */       String iconName = "packageIcon";
/* 120 */       root.add((MutableTreeNode)new JavaTreeNode((ASTNode)pkg, iconName, false));
/*     */     } 
/*     */     
/* 123 */     if (!getShowMajorElementsOnly()) {
/* 124 */       JavaTreeNode importNode = new JavaTreeNode("Imports", "importRootIcon");
/*     */       
/* 126 */       Iterator<ImportDeclaration> iterator = cu.getImportIterator();
/* 127 */       while (iterator.hasNext()) {
/* 128 */         ImportDeclaration idec = iterator.next();
/* 129 */         JavaTreeNode iNode = new JavaTreeNode((ASTNode)idec, "importIcon");
/*     */         
/* 131 */         importNode.add((MutableTreeNode)iNode);
/*     */       } 
/* 133 */       root.add((MutableTreeNode)importNode);
/*     */     } 
/*     */     
/* 136 */     Iterator<TypeDeclaration> i = cu.getTypeDeclarationIterator();
/* 137 */     while (i.hasNext()) {
/* 138 */       TypeDeclaration td = i.next();
/* 139 */       TypeDeclarationTreeNode dmtn = createTypeDeclarationNode(td);
/* 140 */       root.add((MutableTreeNode)dmtn);
/*     */     } 
/*     */     
/* 143 */     this.model.setRoot((TreeNode)root);
/* 144 */     root.setSorted(isSorted());
/* 145 */     refresh();
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
/*     */   private void checkForJavaParsing() {
/* 157 */     if (this.parser != null) {
/* 158 */       this.parser.removePropertyChangeListener("CompilationUnit", this.listener);
/*     */       
/* 160 */       this.parser = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 165 */     LanguageSupportFactory lsf = LanguageSupportFactory.get();
/* 166 */     LanguageSupport support = lsf.getSupportFor("text/java");
/*     */     
/* 168 */     JavaLanguageSupport jls = (JavaLanguageSupport)support;
/*     */ 
/*     */     
/* 171 */     this.parser = jls.getParser(this.textArea);
/* 172 */     if (this.parser != null) {
/* 173 */       this.parser.addPropertyChangeListener("CompilationUnit", this.listener);
/*     */ 
/*     */       
/* 176 */       CompilationUnit cu = this.parser.getCompilationUnit();
/* 177 */       update(cu);
/*     */     } else {
/*     */       
/* 180 */       update((CompilationUnit)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MemberTreeNode createMemberNode(Member member) {
/*     */     MemberTreeNode node;
/* 189 */     if (member instanceof CodeBlock) {
/* 190 */       node = new MemberTreeNode((CodeBlock)member);
/*     */     }
/* 192 */     else if (member instanceof Field) {
/* 193 */       node = new MemberTreeNode((Field)member);
/*     */     } else {
/*     */       
/* 196 */       node = new MemberTreeNode((Method)member);
/*     */     } 
/*     */     
/* 199 */     CodeBlock body = null;
/* 200 */     if (member instanceof CodeBlock) {
/* 201 */       body = (CodeBlock)member;
/*     */     }
/* 203 */     else if (member instanceof Method) {
/* 204 */       body = ((Method)member).getBody();
/*     */     } 
/*     */     
/* 207 */     if (body != null && !getShowMajorElementsOnly()) {
/* 208 */       for (int i = 0; i < body.getLocalVarCount(); i++) {
/* 209 */         LocalVariable var = body.getLocalVar(i);
/* 210 */         LocalVarTreeNode varNode = new LocalVarTreeNode(var);
/* 211 */         node.add((MutableTreeNode)varNode);
/*     */       } 
/*     */     }
/*     */     
/* 215 */     return node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TypeDeclarationTreeNode createTypeDeclarationNode(TypeDeclaration td) {
/* 223 */     TypeDeclarationTreeNode dmtn = new TypeDeclarationTreeNode(td);
/*     */     
/* 225 */     if (td instanceof NormalClassDeclaration) {
/* 226 */       NormalClassDeclaration ncd = (NormalClassDeclaration)td;
/* 227 */       for (int j = 0; j < ncd.getChildTypeCount(); j++) {
/* 228 */         TypeDeclaration td2 = ncd.getChildType(j);
/* 229 */         TypeDeclarationTreeNode tdn = createTypeDeclarationNode(td2);
/* 230 */         dmtn.add((MutableTreeNode)tdn);
/*     */       } 
/* 232 */       Iterator<Member> i = ncd.getMemberIterator();
/* 233 */       while (i.hasNext()) {
/* 234 */         dmtn.add((MutableTreeNode)createMemberNode(i.next()));
/*     */       
/*     */       }
/*     */     }
/* 238 */     else if (td instanceof NormalInterfaceDeclaration) {
/* 239 */       NormalInterfaceDeclaration nid = (NormalInterfaceDeclaration)td;
/* 240 */       for (int j = 0; j < nid.getChildTypeCount(); j++) {
/* 241 */         TypeDeclaration td2 = nid.getChildType(j);
/* 242 */         TypeDeclarationTreeNode tdn = createTypeDeclarationNode(td2);
/* 243 */         dmtn.add((MutableTreeNode)tdn);
/*     */       } 
/* 245 */       Iterator<Member> i = nid.getMemberIterator();
/* 246 */       while (i.hasNext()) {
/* 247 */         dmtn.add((MutableTreeNode)createMemberNode(i.next()));
/*     */       }
/*     */     } 
/*     */     
/* 251 */     return dmtn;
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
/* 263 */     int j = 0;
/* 264 */     while (j < getRowCount()) {
/* 265 */       collapseRow(j++);
/*     */     }
/*     */ 
/*     */     
/* 269 */     expandRow(0);
/* 270 */     j = 1;
/* 271 */     while (j < getRowCount()) {
/* 272 */       TreePath path = getPathForRow(j);
/* 273 */       Object comp = path.getLastPathComponent();
/* 274 */       if (comp instanceof TypeDeclarationTreeNode) {
/* 275 */         expandPath(path);
/*     */       }
/* 277 */       j++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void gotoElementAtPath(TreePath path) {
/* 285 */     DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
/* 286 */     Object obj = node.getUserObject();
/* 287 */     if (obj instanceof ASTNode) {
/* 288 */       ASTNode astNode = (ASTNode)obj;
/* 289 */       int start = astNode.getNameStartOffset();
/* 290 */       int end = astNode.getNameEndOffset();
/* 291 */       DocumentRange range = new DocumentRange(start, end);
/* 292 */       RSyntaxUtilities.selectAndPossiblyCenter((JTextArea)this.textArea, range, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean gotoSelectedElement() {
/* 302 */     TreePath path = getLeadSelectionPath();
/* 303 */     if (path != null) {
/* 304 */       gotoElementAtPath(path);
/* 305 */       return true;
/*     */     } 
/* 307 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void listenTo(RSyntaxTextArea textArea) {
/* 317 */     if (this.textArea != null) {
/* 318 */       uninstall();
/*     */     }
/*     */ 
/*     */     
/* 322 */     if (textArea == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 327 */     this.textArea = textArea;
/* 328 */     textArea.addPropertyChangeListener("RSTA.syntaxStyle", this.listener);
/*     */ 
/*     */ 
/*     */     
/* 332 */     checkForJavaParsing();
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
/* 343 */     if (this.parser != null) {
/* 344 */       this.parser.removePropertyChangeListener("CompilationUnit", this.listener);
/*     */       
/* 346 */       this.parser = null;
/*     */     } 
/*     */     
/* 349 */     if (this.textArea != null) {
/* 350 */       this.textArea.removePropertyChangeListener("RSTA.syntaxStyle", this.listener);
/*     */       
/* 352 */       this.textArea = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateUI() {
/* 363 */     super.updateUI();
/*     */ 
/*     */     
/* 366 */     setCellRenderer(new AstTreeCellRenderer());
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
/* 384 */       String name = e.getPropertyName();
/*     */ 
/*     */       
/* 387 */       if ("RSTA.syntaxStyle".equals(name)) {
/* 388 */         JavaOutlineTree.this.checkForJavaParsing();
/*     */       
/*     */       }
/* 391 */       else if ("CompilationUnit".equals(name)) {
/* 392 */         CompilationUnit cu = (CompilationUnit)e.getNewValue();
/* 393 */         JavaOutlineTree.this.update(cu);
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
/* 404 */       if (JavaOutlineTree.this.getGotoSelectedElementOnClick()) {
/*     */         
/* 406 */         TreePath newPath = e.getNewLeadSelectionPath();
/* 407 */         if (newPath != null)
/* 408 */           JavaOutlineTree.this.gotoElementAtPath(newPath); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\tree\JavaOutlineTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */