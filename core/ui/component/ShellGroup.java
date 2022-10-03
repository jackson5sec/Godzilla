/*     */ package core.ui.component;
/*     */ 
/*     */ import core.Db;
/*     */ import core.EasyI18N;
/*     */ import core.ui.MainActivity;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.Component;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.awt.event.ActionEvent;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import util.Log;
/*     */ import util.UiFunction;
/*     */ import util.automaticBindClick;
/*     */ 
/*     */ public class ShellGroup
/*     */   extends DataTree {
/*     */   public ShellGroup() {
/*  22 */     this("", new DefaultMutableTreeNode(EasyI18N.getI18nString("分组")));
/*     */   } protected JPopupMenu childPopupMenu;
/*     */   public ShellGroup(String fileString, DefaultMutableTreeNode root_Node) {
/*  25 */     super(fileString, root_Node);
/*  26 */     this.childPopupMenu = new JPopupMenu();
/*     */     
/*  28 */     JMenuItem newGroupItem = new JMenuItem("新建组");
/*  29 */     newGroupItem.setActionCommand("newGroup");
/*     */     
/*  31 */     JMenuItem renameItem = new JMenuItem("移动/重命名");
/*  32 */     renameItem.setActionCommand("rename");
/*     */     
/*  34 */     JMenuItem copyPathItem = new JMenuItem("复制当前组路径");
/*  35 */     copyPathItem.setActionCommand("copyPath");
/*     */     
/*  37 */     JMenuItem refreshItem = new JMenuItem("刷新");
/*  38 */     refreshItem.setActionCommand("refresh");
/*     */     
/*  40 */     JMenuItem deleteCurrentGroupItem = new JMenuItem("删除当前组");
/*  41 */     deleteCurrentGroupItem.setActionCommand("deleteGroup");
/*     */     
/*  43 */     JMenuItem deleteCurrentGroupAndDeleteWebshellItem = new JMenuItem("删除当前组并删除所有成员");
/*  44 */     deleteCurrentGroupAndDeleteWebshellItem.setActionCommand("deleteCurrentGroupAndDeleteWebshell");
/*     */     
/*  46 */     JMenuItem deleteCurrentGroupDontDeleteWebshellItem = new JMenuItem("删除当前组但不删除所有成员");
/*  47 */     deleteCurrentGroupDontDeleteWebshellItem.setActionCommand("deleteCurrentGroupDontDeleteWebshell");
/*     */     
/*  49 */     this.childPopupMenu.add(newGroupItem);
/*  50 */     this.childPopupMenu.add(renameItem);
/*  51 */     this.childPopupMenu.add(copyPathItem);
/*  52 */     this.childPopupMenu.add(refreshItem);
/*  53 */     this.childPopupMenu.add(deleteCurrentGroupItem);
/*  54 */     this.childPopupMenu.add(deleteCurrentGroupAndDeleteWebshellItem);
/*  55 */     this.childPopupMenu.add(deleteCurrentGroupDontDeleteWebshellItem);
/*     */     
/*  57 */     setChildPopupMenu(this.childPopupMenu);
/*  58 */     setParentPopupMenu(this.childPopupMenu);
/*     */     
/*  60 */     automaticBindClick.bindMenuItemClick(this.childPopupMenu, null, this);
/*     */     
/*  62 */     setLeafIcon(new ImageIcon(getClass().getResource("/images/folder.png")));
/*     */     
/*  64 */     refreshMenuItemClick((ActionEvent)null);
/*     */     
/*  66 */     EasyI18N.installObject(this);
/*     */   }
/*     */   
/*     */   protected void newGroupMenuItemClick(ActionEvent e) {
/*  70 */     String inputString = GOptionPane.showInputDialog("请输入新组名", "newGroup");
/*  71 */     if (inputString != null && !"/".equals(inputString.trim())) {
/*  72 */       String selectedString = GetSelectFile();
/*  73 */       String newGroup = parseFile2(selectedString + "/" + inputString);
/*  74 */       if (Db.addGroup(newGroup) > 0) {
/*  75 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "添加成功!");
/*  76 */         refreshMenuItemClick(e);
/*     */       } else {
/*  78 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "添加失败 请检查组是否存在 控制台是否有报错!");
/*     */       } 
/*     */     } else {
/*  81 */       Log.error("用户未输入数据");
/*     */     } 
/*     */   }
/*     */   protected void renameMenuItemClick(ActionEvent e) {
/*  85 */     String inputString = GOptionPane.showInputDialog("请输入新组名", GetSelectFile());
/*  86 */     if (inputString != null && !"/".equals(inputString.trim())) {
/*  87 */       String oldGroup = GetSelectFile();
/*  88 */       String newGroup = parseFile2("/" + inputString);
/*  89 */       if (Db.renameGroup(oldGroup, newGroup) > 0) {
/*  90 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "移动成功!");
/*  91 */         refreshMenuItemClick(e);
/*     */       } else {
/*  93 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "移动失败 请检查新组是否存在 控制台是否有报错!");
/*     */       } 
/*     */     } else {
/*  96 */       Log.error("用户未输入数据");
/*     */     } 
/*     */   }
/*     */   protected void refreshMenuItemClick(ActionEvent e) {
/* 100 */     removeAll();
/* 101 */     AddNote("/");
/* 102 */     Db.getAllGroup().forEach(id -> AddNote(id.toString()));
/*     */   }
/*     */   protected void deleteGroupMenuItemClick(ActionEvent e) {
/* 105 */     String groupId = GetSelectFile();
/* 106 */     if (groupId != null && !"/".equals(groupId.trim())) {
/* 107 */       if (0 != GOptionPane.showConfirmDialog((Component)MainActivity.getMainActivityFrame(), String.format("确定删除组：%s 吗?", new Object[] { groupId, "警告", Integer.valueOf(0) }))) {
/*     */         return;
/*     */       }
/* 110 */       if (Db.removeGroup(groupId, "/") > 0) {
/* 111 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "删除成功! 成员已移动到 / ");
/* 112 */         refreshMenuItemClick(e);
/*     */       } else {
/* 114 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "删除失败 请检查组是否存在 控制台是否有报错!");
/*     */       } 
/*     */     } else {
/* 117 */       Log.error("group是空的");
/*     */     } 
/*     */   }
/*     */   protected void copyPathMenuItemClick(ActionEvent e) {
/* 121 */     String groupId = GetSelectFile();
/* 122 */     if (groupId != null && !"/".equals(groupId.trim())) {
/* 123 */       Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(groupId), null);
/* 124 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "复制成功");
/*     */     } else {
/* 126 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "group是空的");
/*     */     } 
/*     */   }
/*     */   protected void deleteCurrentGroupAndDeleteWebshellMenuItemClick(ActionEvent e) {
/* 130 */     String groupId = GetSelectFile();
/* 131 */     if (groupId != null && !"/".equals(groupId.trim())) {
/* 132 */       if (0 != GOptionPane.showConfirmDialog((Component)MainActivity.getMainActivityFrame(), String.format(EasyI18N.getI18nString("确定删除组：%s 并删除所有组成员(包括子组)吗?"), new Object[] { groupId, "警告", Integer.valueOf(0) }))) {
/*     */         return;
/*     */       }
/* 135 */       Db.removeShellByGroup(groupId);
/* 136 */       if (Db.removeGroup(groupId, "/") > 0) {
/* 137 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "删除成功! 子组与成员已全部删除");
/* 138 */         refreshMenuItemClick(e);
/*     */       } else {
/* 140 */         GOptionPane.showMessageDialog(UiFunction.getParentFrame(this), "删除失败 请检查组是否存在 控制台是否有报错!");
/*     */       } 
/*     */     } else {
/* 143 */       Log.error("group是空的");
/*     */     } 
/*     */   }
/*     */   protected void deleteCurrentGroupDontDeleteWebshellMenuItemClick(ActionEvent e) {
/* 147 */     deleteGroupMenuItemClick(e);
/*     */   }
/*     */   public String getSelectedGroupName() {
/* 150 */     String groupId = GetSelectFile();
/* 151 */     return groupId;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\ShellGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */