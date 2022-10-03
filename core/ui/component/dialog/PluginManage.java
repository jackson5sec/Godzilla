/*     */ package core.ui.component.dialog;
/*     */ 
/*     */ import core.ApplicationContext;
/*     */ import core.Db;
/*     */ import core.EasyI18N;
/*     */ import core.ui.MainActivity;
/*     */ import core.ui.component.DataView;
/*     */ import java.awt.Component;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.io.File;
/*     */ import java.util.Vector;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.filechooser.FileNameExtensionFilter;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluginManage
/*     */   extends JDialog
/*     */ {
/*     */   private final DataView pluginView;
/*     */   private final JButton addPluginButton;
/*     */   private final JButton removeButton;
/*     */   private final JButton cancelButton;
/*     */   private final JButton refreshButton;
/*     */   private final Vector<String> columnVector;
/*     */   private final JSplitPane splitPane;
/*     */   
/*     */   public PluginManage() {
/*  42 */     super((Frame)MainActivity.getFrame(), "PluginManage", true);
/*     */     
/*  44 */     this.addPluginButton = new JButton("添加");
/*  45 */     this.removeButton = new JButton("移除");
/*  46 */     this.refreshButton = new JButton("刷新");
/*  47 */     this.cancelButton = new JButton("取消");
/*  48 */     this.splitPane = new JSplitPane();
/*     */ 
/*     */     
/*  51 */     this.columnVector = new Vector<>();
/*  52 */     this.columnVector.add("pluginJarFile");
/*     */     
/*  54 */     this.pluginView = new DataView(null, this.columnVector, -1, -1);
/*  55 */     refreshPluginView();
/*     */     
/*  57 */     JPanel bottomPanel = new JPanel();
/*     */     
/*  59 */     bottomPanel.add(this.addPluginButton);
/*  60 */     bottomPanel.add(this.removeButton);
/*  61 */     bottomPanel.add(this.refreshButton);
/*  62 */     bottomPanel.add(this.cancelButton);
/*     */     
/*  64 */     this.splitPane.setOrientation(0);
/*  65 */     this.splitPane.setTopComponent(new JScrollPane((Component)this.pluginView));
/*  66 */     this.splitPane.setBottomComponent(bottomPanel);
/*     */     
/*  68 */     this.splitPane.addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentResized(ComponentEvent e) {
/*  71 */             PluginManage.this.splitPane.setDividerLocation(0.85D);
/*     */           }
/*     */         });
/*     */     
/*  75 */     automaticBindClick.bindJButtonClick(this, this);
/*     */     
/*  77 */     add(this.splitPane);
/*     */     
/*  79 */     functions.setWindowSize(this, 420, 420);
/*     */     
/*  81 */     setLocationRelativeTo((Component)MainActivity.getFrame());
/*  82 */     setDefaultCloseOperation(2);
/*     */     
/*  84 */     EasyI18N.installObject(this);
/*     */     
/*  86 */     setVisible(true);
/*     */   }
/*     */   private void refreshPluginView() {
/*  89 */     String[] pluginStrings = Db.getAllPlugin();
/*  90 */     Vector<Vector<String>> rows = new Vector<>();
/*     */     
/*  92 */     for (int i = 0; i < pluginStrings.length; i++) {
/*  93 */       String string = pluginStrings[i];
/*  94 */       Vector<String> rowVector = new Vector<>();
/*  95 */       rowVector.add(string);
/*  96 */       rows.add(rowVector);
/*     */     } 
/*  98 */     this.pluginView.AddRows(rows);
/*  99 */     this.pluginView.getModel().fireTableDataChanged();
/*     */   }
/*     */   private void addPluginButtonClick(ActionEvent actionEvent) {
/* 102 */     GFileChooser chooser = new GFileChooser();
/* 103 */     chooser.setFileFilter(new FileNameExtensionFilter("*.jar", new String[] { "jar" }));
/* 104 */     chooser.setFileSelectionMode(0);
/* 105 */     boolean flag = (0 == chooser.showDialog(new JLabel(), "选择"));
/* 106 */     File selectdFile = chooser.getSelectedFile();
/* 107 */     if (flag && selectdFile != null) {
/* 108 */       if (Db.addPlugin(selectdFile.getAbsolutePath()) == 1) {
/* 109 */         ApplicationContext.init();
/* 110 */         GOptionPane.showMessageDialog(this, "添加插件成功", "提示", 1);
/*     */       } else {
/* 112 */         GOptionPane.showMessageDialog(this, "添加插件失败", "提示", 2);
/*     */       } 
/*     */     } else {
/* 115 */       Log.log("用户取消选择.....", new Object[0]);
/*     */     } 
/* 117 */     refreshPluginView();
/*     */   }
/*     */   
/*     */   private void removeButtonClick(ActionEvent actionEvent) {
/* 121 */     int rowIndex = this.pluginView.getSelectedRow();
/* 122 */     if (rowIndex != -1) {
/* 123 */       Object selectedItem = this.pluginView.getValueAt(rowIndex, 0);
/* 124 */       if (Db.removePlugin((String)selectedItem) == 1) {
/* 125 */         GOptionPane.showMessageDialog(this, "移除插件成功", "提示", 1);
/*     */       } else {
/* 127 */         GOptionPane.showMessageDialog(this, "移除插件失败", "提示", 2);
/*     */       } 
/*     */     } else {
/* 130 */       GOptionPane.showMessageDialog(this, "没有选中插件", "提示", 2);
/*     */     } 
/* 132 */     refreshPluginView();
/*     */   }
/*     */   private void cancelButtonClick(ActionEvent actionEvent) {
/* 135 */     dispose();
/*     */   }
/*     */   private void refreshButtonClick(ActionEvent actionEvent) {
/* 138 */     refreshPluginView();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\dialog\PluginManage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */