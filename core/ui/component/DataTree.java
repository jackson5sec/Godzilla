/*     */ package core.ui.component;
/*     */ 
/*     */ import core.ui.component.listener.ActionDblClick;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import javax.swing.tree.DefaultTreeCellRenderer;
/*     */ import javax.swing.tree.TreePath;
/*     */ 
/*     */ 
/*     */ public class DataTree
/*     */   extends JTree
/*     */ {
/*     */   private RightClickEvent rightClickEvent;
/*  24 */   private DefaultMutableTreeNode rootNode = null;
/*     */   private ImageIcon leafIcon;
/*     */   
/*     */   public DataTree(String fileString, DefaultMutableTreeNode root_Node) {
/*  28 */     super(root_Node);
/*  29 */     this.rootNode = root_Node;
/*  30 */     initJTree();
/*     */   }
/*     */   private void initJTree() {
/*  33 */     this.rightClickEvent = new RightClickEvent(this);
/*  34 */     addMouseListener(this.rightClickEvent);
/*     */     
/*  36 */     getSelectionModel().setSelectionMode(1);
/*     */   }
/*     */   public void setActionDbclick(ActionDblClick actionDblClick) {
/*  39 */     this.rightClickEvent.setActionDblClick(actionDblClick);
/*     */   }
/*     */   public void setChildPopupMenu(JPopupMenu popupMenu) {
/*  42 */     this.rightClickEvent.setChildPopupMenu(popupMenu);
/*     */   }
/*     */   public void setParentPopupMenu(JPopupMenu popupMenu) {
/*  45 */     this.rightClickEvent.setParentPopupMenu(popupMenu);
/*     */   }
/*     */   public void deleteNote(String fileString) {
/*  48 */     DefaultMutableTreeNode defaultMutableTreeNode = this.rootNode;
/*  49 */     String[] paths = parseFile(fileString);
/*  50 */     DefaultMutableTreeNode node = (DefaultMutableTreeNode)defaultMutableTreeNode.getPath()[0];
/*  51 */     DefaultMutableTreeNode lasTreeNode = null;
/*  52 */     for (int i = 0; i < paths.length; i++) {
/*  53 */       node = FindTreeNote(node, paths[i]);
/*  54 */       if (node == null)
/*     */         return; 
/*  56 */       if (i == paths.length - 2) {
/*  57 */         lasTreeNode = node;
/*     */       }
/*     */     } 
/*  60 */     if (lasTreeNode != null)
/*  61 */       lasTreeNode.remove(lasTreeNode); 
/*     */   }
/*     */   
/*     */   public void setLeafIcon(ImageIcon imageIcon) {
/*  65 */     this.leafIcon = imageIcon;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateUI() {
/*  70 */     super.updateUI();
/*  71 */     if (this.leafIcon != null) {
/*  72 */       DefaultTreeCellRenderer cellRenderer = (DefaultTreeCellRenderer)getCellRenderer();
/*  73 */       cellRenderer.setLeafIcon(cellRenderer.getClosedIcon());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAll() {
/*  81 */     super.removeAll();
/*  82 */     this.rootNode.removeAllChildren();
/*  83 */     updateUI();
/*     */   }
/*     */   public void MoveNoteName(String fileString, String rename) {
/*  86 */     DefaultMutableTreeNode defaultMutableTreeNode = this.rootNode;
/*  87 */     String[] paths = parseFile(fileString);
/*  88 */     DefaultMutableTreeNode node = (DefaultMutableTreeNode)defaultMutableTreeNode.getPath()[0];
/*  89 */     for (int i = 0; i < paths.length; i++) {
/*  90 */       node = FindTreeNote(node, paths[i]);
/*  91 */       if (node == null) {
/*     */         return;
/*     */       }
/*     */     } 
/*  95 */     node.setUserObject(rename);
/*     */   }
/*     */   public String GetSelectFile() {
/*  98 */     TreePath paths = getSelectionPath();
/*  99 */     ArrayList<String> pathList = new ArrayList<>();
/* 100 */     TreePath lastTreePath = paths;
/* 101 */     DefaultMutableTreeNode lastNode = null;
/*     */     do {
/*     */       try {
/* 104 */         lastNode = (DefaultMutableTreeNode)lastTreePath.getLastPathComponent();
/* 105 */       } catch (Exception e) {
/*     */         
/* 107 */         return "";
/*     */       } 
/* 109 */       pathList.add((String)lastNode.getUserObject());
/* 110 */       lastTreePath = lastTreePath.getParentPath();
/* 111 */     } while (lastTreePath != null);
/*     */ 
/*     */ 
/*     */     
/* 115 */     pathList.remove(pathList.size() - 1);
/* 116 */     Collections.reverse(pathList);
/* 117 */     if (pathList.size() > 0) {
/* 118 */       return parseFile(pathList);
/*     */     }
/* 120 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelectNote(String pathString) {
/* 125 */     if (pathString == null || pathString.trim().isEmpty()) {
/*     */       return;
/*     */     }
/* 128 */     DefaultMutableTreeNode defaultMutableTreeNode = this.rootNode;
/* 129 */     String[] paths = parseFile(pathString);
/* 130 */     DefaultMutableTreeNode node = (DefaultMutableTreeNode)defaultMutableTreeNode.getPath()[0];
/* 131 */     DefaultMutableTreeNode lastTreeNode = node;
/*     */     
/* 133 */     for (int i = 0; i < paths.length; i++) {
/* 134 */       node = FindTreeNote(node, paths[i]);
/* 135 */       if (node != null) {
/* 136 */         lastTreeNode = node;
/*     */       } else {
/*     */         return;
/*     */       } 
/*     */     } 
/* 141 */     if (lastTreeNode != null) {
/* 142 */       setSelectionPath(new TreePath((Object[])lastTreeNode.getPath()));
/*     */     }
/* 144 */     updateUI();
/*     */   }
/*     */ 
/*     */   
/*     */   public void AddNote(String pathString) {
/* 149 */     if (pathString == null || pathString.trim().isEmpty()) {
/*     */       return;
/*     */     }
/* 152 */     DefaultMutableTreeNode defaultMutableTreeNode = this.rootNode;
/* 153 */     String[] paths = parseFile(pathString);
/* 154 */     DefaultMutableTreeNode node = (DefaultMutableTreeNode)defaultMutableTreeNode.getPath()[0];
/* 155 */     DefaultMutableTreeNode lastTreeNode = node;
/* 156 */     boolean findSate = true;
/* 157 */     for (int i = 0; i < paths.length; i++) {
/*     */       
/* 159 */       if (findSate) {
/* 160 */         node = FindTreeNote(node, paths[i]);
/* 161 */         if (node == null) {
/* 162 */           findSate = false;
/* 163 */           DefaultMutableTreeNode _note = new DefaultMutableTreeNode(paths[i]);
/* 164 */           lastTreeNode.add(_note);
/* 165 */           lastTreeNode = _note;
/*     */         } else {
/* 167 */           lastTreeNode = node;
/*     */         } 
/*     */       } else {
/* 170 */         DefaultMutableTreeNode _note = new DefaultMutableTreeNode(paths[i]);
/* 171 */         lastTreeNode.add(_note);
/* 172 */         lastTreeNode = _note;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 177 */     if (lastTreeNode != null) {
/* 178 */       expandPath(new TreePath((Object[])((DefaultMutableTreeNode)lastTreeNode.getParent()).getPath()));
/*     */     }
/*     */     
/* 181 */     updateUI();
/*     */   }
/*     */   protected String[] parseFile(String fileString) {
/* 184 */     fileString = replaceSpecial(fileString);
/* 185 */     String[] retStrings = null;
/* 186 */     if (fileString.charAt(0) == '/') {
/* 187 */       retStrings = fileString.split("/");
/* 188 */       if (retStrings.length == 0) {
/* 189 */         retStrings = new String[] { "/" };
/*     */       } else {
/* 191 */         retStrings[0] = "/";
/*     */       } 
/*     */     } else {
/* 194 */       retStrings = fileString.split("/");
/*     */     } 
/* 196 */     return retStrings;
/*     */   }
/*     */   protected String parseFile(List<String> pathList) {
/* 199 */     StringBuilder builder = new StringBuilder();
/* 200 */     for (int i = 0; i < pathList.size(); i++) {
/* 201 */       builder.append("/");
/* 202 */       builder.append(pathList.get(i));
/*     */     } 
/* 204 */     return replaceSpecial(builder.substring(1));
/*     */   }
/*     */   
/*     */   protected String parseFile2(String file) {
/* 208 */     return parseFile(new CopyOnWriteArrayList<>(parseFile(file)));
/*     */   }
/*     */   private String replaceSpecial(String string) {
/* 211 */     return string.replaceAll("\\\\+", "/").trim().replaceAll("/+", "/").trim();
/*     */   }
/*     */   private DefaultMutableTreeNode FindTreeNote(DefaultMutableTreeNode node, String noteString) {
/* 214 */     for (Enumeration<DefaultMutableTreeNode> e = node.children(); e.hasMoreElements(); ) {
/*     */       
/* 216 */       DefaultMutableTreeNode n = e.nextElement();
/* 217 */       if (n.getUserObject().equals(noteString)) {
/* 218 */         return n;
/*     */       }
/*     */     } 
/* 221 */     return null;
/*     */   }
/*     */   
/*     */   private class RightClickEvent
/*     */     extends MouseAdapter {
/*     */     private ActionDblClick actionDblClick;
/*     */     private JPopupMenu childPopupMenu;
/*     */     private JPopupMenu parentPopupMenu;
/*     */     private final DataTree jTree;
/*     */     
/*     */     public RightClickEvent(DataTree tree) {
/* 232 */       this.jTree = tree;
/*     */     }
/*     */     
/*     */     public ActionDblClick getActionDblClick() {
/* 236 */       return this.actionDblClick;
/*     */     }
/*     */     public void setActionDblClick(ActionDblClick actionDblClick) {
/* 239 */       this.actionDblClick = actionDblClick;
/*     */     }
/*     */     public void setChildPopupMenu(JPopupMenu popupMenu) {
/* 242 */       this.childPopupMenu = popupMenu;
/*     */     }
/*     */     
/*     */     public void setParentPopupMenu(JPopupMenu parentPopupMenu) {
/* 246 */       this.parentPopupMenu = parentPopupMenu;
/*     */     }
/*     */ 
/*     */     
/*     */     public void mouseClicked(MouseEvent paramMouseEvent) {
/* 251 */       if (SwingUtilities.isRightMouseButton(paramMouseEvent)) {
/* 252 */         if ((this.childPopupMenu != null || this.parentPopupMenu != null) && 
/* 253 */           this.jTree.getSelectionPath() != null) {
/* 254 */           if (((DefaultMutableTreeNode)this.jTree.getLastSelectedPathComponent()).getChildCount() == 0 && this.childPopupMenu != null) {
/* 255 */             String selectedPath = this.jTree.GetSelectFile();
/* 256 */             if (selectedPath != null && !selectedPath.trim().isEmpty()) {
/* 257 */               this.childPopupMenu.show(this.jTree, paramMouseEvent.getX(), paramMouseEvent.getY());
/*     */             }
/* 259 */           } else if (this.parentPopupMenu != null) {
/* 260 */             String selectedPath = this.jTree.GetSelectFile();
/* 261 */             if (selectedPath != null && !selectedPath.trim().isEmpty()) {
/* 262 */               this.parentPopupMenu.show(this.jTree, paramMouseEvent.getX(), paramMouseEvent.getY());
/*     */             }
/*     */           }
/*     */         
/*     */         }
/* 267 */       } else if (paramMouseEvent.getClickCount() == 2 && 
/* 268 */         this.actionDblClick != null && !DataTree.this.GetSelectFile().trim().isEmpty()) {
/* 269 */         this.actionDblClick.dblClick(paramMouseEvent);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\DataTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */