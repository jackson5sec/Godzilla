/*     */ package core.ui;
/*     */ import core.ApplicationConfig;
/*     */ import core.ApplicationContext;
/*     */ import core.Db;
/*     */ import core.EasyI18N;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.DataView;
/*     */ import core.ui.component.ShellGroup;
/*     */ import core.ui.component.dialog.AppSeting;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import core.ui.component.dialog.GenerateShellLoder;
/*     */ import core.ui.component.dialog.PluginManage;
/*     */ import core.ui.component.frame.LiveScan;
/*     */ import core.ui.component.frame.ShellSetting;
/*     */ import java.awt.Component;
/*     */ import java.awt.KeyEventDispatcher;
/*     */ import java.awt.PopupMenu;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Vector;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ 
/*     */ public class MainActivity extends JFrame {
/*     */   private static MainActivity mainActivityFrame;
/*     */   private static JMenuBar menuBar;
/*     */   private JMenu targetMenu;
/*     */   private JMenu aboutMenu;
/*     */   private JMenu attackMenu;
/*     */   private JMenu configMenu;
/*     */   private static JMenu pluginMenu;
/*     */   
/*     */   private static void initStatic() {
/*  50 */     menuBar = new JMenuBar();
/*  51 */     pluginMenu = new JMenu("插件");
/*  52 */     shellViewPopupMenu = new JPopupMenu();
/*     */   }
/*     */   private DataView shellView; private JScrollPane shellViewScrollPane; private static JPopupMenu shellViewPopupMenu;
/*     */   private Vector<String> columnVector;
/*     */   
/*     */   public MainActivity() {
/*  58 */     ApplicationContext.init();
/*  59 */     initVariable();
/*  60 */     EasyI18N.installObject(this);
/*     */   }
/*     */   private JSplitPane splitPane; private ShellGroup shellGroupTree; private String currentGroup; private JLabel statusLabel;
/*     */   private void initVariable() {
/*  64 */     setTitle(EasyI18N.getI18nString("哥斯拉   V%s by: BeichenDream Github:https://github.com/BeichenDream/Godzilla", new Object[] { "4.01" }));
/*  65 */     setLayout(new BorderLayout(2, 2));
/*  66 */     this.currentGroup = "/";
/*  67 */     this.statusLabel = new JLabel("status");
/*     */     
/*  69 */     Vector<Vector<String>> rows = Db.getAllShell();
/*  70 */     this.columnVector = rows.get(0);
/*  71 */     rows.remove(0);
/*  72 */     this.shellView = new DataView(null, this.columnVector, -1, -1);
/*  73 */     refreshShellView();
/*  74 */     this.shellView.setSelectionMode(2);
/*     */ 
/*     */ 
/*     */     
/*  78 */     this.splitPane = new JSplitPane(1);
/*     */     
/*  80 */     this.shellGroupTree = new ShellGroup();
/*     */     
/*  82 */     this.splitPane.setLeftComponent(new JScrollPane((Component)this.shellGroupTree));
/*  83 */     this.splitPane.setRightComponent(this.shellViewScrollPane = new JScrollPane((Component)this.shellView));
/*     */ 
/*     */     
/*  86 */     add(this.splitPane);
/*  87 */     add(this.statusLabel, "South");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     this.targetMenu = new JMenu("目标");
/*  93 */     JMenuItem addShellMenuItem = new JMenuItem("添加");
/*  94 */     addShellMenuItem.setActionCommand("addShell");
/*  95 */     this.targetMenu.add(addShellMenuItem);
/*  96 */     this.attackMenu = new JMenu("管理");
/*  97 */     JMenuItem shellLiveScanMenuItem = new JMenuItem("存活扫描");
/*  98 */     shellLiveScanMenuItem.setActionCommand("shellLiveScan");
/*  99 */     JMenuItem generateShellMenuItem = new JMenuItem("生成");
/* 100 */     generateShellMenuItem.setActionCommand("generateShell");
/* 101 */     this.attackMenu.add(generateShellMenuItem);
/* 102 */     this.attackMenu.add(shellLiveScanMenuItem);
/* 103 */     this.configMenu = new JMenu("配置");
/* 104 */     JMenuItem pluginConfigMenuItem = new JMenuItem("插件配置");
/* 105 */     pluginConfigMenuItem.setActionCommand("pluginConfig");
/* 106 */     JMenuItem appConfigMenuItem = new JMenuItem("程序配置");
/* 107 */     appConfigMenuItem.setActionCommand("appConfig");
/* 108 */     this.configMenu.add(appConfigMenuItem);
/* 109 */     this.configMenu.add(pluginConfigMenuItem);
/* 110 */     this.aboutMenu = new JMenu("关于");
/* 111 */     JMenuItem aboutMenuItem = new JMenuItem("关于");
/* 112 */     aboutMenuItem.setActionCommand("about");
/* 113 */     this.aboutMenu.add(aboutMenuItem);
/*     */     
/* 115 */     automaticBindClick.bindMenuItemClick(this.targetMenu, null, this);
/* 116 */     automaticBindClick.bindMenuItemClick(this.attackMenu, null, this);
/* 117 */     automaticBindClick.bindMenuItemClick(this.configMenu, null, this);
/* 118 */     automaticBindClick.bindMenuItemClick(this.aboutMenu, null, this);
/*     */     
/* 120 */     this.shellGroupTree.setActionDbclick(e -> {
/*     */           this.currentGroup = this.shellGroupTree.GetSelectFile().trim();
/*     */           
/*     */           refreshShellView();
/*     */         });
/*     */     
/* 126 */     menuBar.add(this.targetMenu);
/* 127 */     menuBar.add(this.attackMenu);
/* 128 */     menuBar.add(this.configMenu);
/* 129 */     menuBar.add(this.aboutMenu);
/* 130 */     menuBar.add(pluginMenu);
/* 131 */     setJMenuBar(menuBar);
/*     */     
/* 133 */     JMenuItem copyselectItem = new JMenuItem("复制选中");
/* 134 */     copyselectItem.setActionCommand("copyShellViewSelected");
/* 135 */     JMenuItem interactMenuItem = new JMenuItem("进入");
/* 136 */     interactMenuItem.setActionCommand("interact");
/* 137 */     JMenuItem interactCacheMenuItem = new JMenuItem("进入缓存");
/* 138 */     interactCacheMenuItem.setActionCommand("interactCache");
/* 139 */     JMenuItem removeShell = new JMenuItem("移除");
/* 140 */     removeShell.setActionCommand("removeShell");
/* 141 */     JMenuItem editShell = new JMenuItem("编辑");
/* 142 */     editShell.setActionCommand("editShell");
/* 143 */     JMenuItem refreshShell = new JMenuItem("刷新");
/* 144 */     refreshShell.setActionCommand("refreshShellView");
/* 145 */     shellViewPopupMenu.add(interactMenuItem);
/* 146 */     shellViewPopupMenu.add(interactCacheMenuItem);
/* 147 */     shellViewPopupMenu.add(copyselectItem);
/* 148 */     shellViewPopupMenu.add(removeShell);
/* 149 */     shellViewPopupMenu.add(editShell);
/* 150 */     shellViewPopupMenu.add(refreshShell);
/* 151 */     this.shellView.setRightClickMenu(shellViewPopupMenu);
/* 152 */     automaticBindClick.bindMenuItemClick(shellViewPopupMenu, null, this);
/*     */     
/* 154 */     addEasterEgg();
/*     */     
/* 156 */     functions.setWindowSize(this, 1500, 600);
/* 157 */     setLocationRelativeTo((Component)null);
/*     */     
/* 159 */     setVisible(true);
/*     */     
/* 161 */     setDefaultCloseOperation(3);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addEasterEgg() {
/* 166 */     KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher()
/*     */         {
/*     */           public boolean dispatchKeyEvent(KeyEvent e)
/*     */           {
/* 170 */             if (e.getKeyCode() == 112 && ApplicationContext.easterEgg) {
/* 171 */               ApplicationContext.easterEgg = false;
/* 172 */               GOptionPane.showMessageDialog(MainActivity.getFrame(), EasyI18N.getI18nString("Hacker技术学的再好, 却无法入侵你的心,\n服务器入侵的再多,对你只有Guest,\n是我的DDOS造成了你的拒绝服务？\n还是我的WebShell再次被你查杀？\n你总有防火墙\n我始终停不掉\n想提权\n无奈JSP+MYSQL成为我们的障碍\n找不到你的注入点\n扫不出你的空口令\n所有对我的回应都用3DES加密\n你总是自定义文件格式\n我永远找不到你的入口点\n忽略所有异常\n却还是跟踪不到你的注册码\n是你太过完美,还是我太菜?\n虽然我们是不同的对象,都有隐私的一面,\n但我相信总有一天我会找到你的接口,把我的最真给你看!\n因为我是你的指针,在茫茫内存的堆栈中, 永远指向你那片天空,不孜不倦!\n我愿做你的内联,供你无限次的调用,直到海枯石烂!\n我愿做你的引用,和你同进退共生死,一起经受考验!\n只是我不愿苦苦地调试你的心情,最终沦为你的友元!\n如今我们已被MFC封装--事事变迁!\n如今我们已向COM走去--可想当年!\n没任何奢求,只愿做你最后的System!\n渗透玩的再强,我也不能提权进你的心\n免杀玩的再狠,我也过不了你的主防御\n外挂写的再叼,我也不能操控你对我的爱\n编程玩的再好,我也不能写出完美的爱情\n纵使我多么的不可一世,也不是你的System\n提权了再多的服务器，却永远成不了你的Root\n**But...... **\n那怕你的心再强大，我有0day在手\n主动防御再牛，我有R0\n击败你只是时间问题, 就算能操控，你的心早已经不属于我\n已被千人DownLoad\n完美的爱情写出来能怎样，终究会像游戏一样结束\n不是你的System也罢，但我有Guest用户，早晚提权进入你的管理员组\n\n也许，像你说的那样，我们是不同世界的人，因为我是乞丐而不是骑士\n人变了，是因为心跟着生活在变\n人生有梦，各自精彩\n燕雀安知鸿鹄之志!"), "提示", -1);
/* 173 */               return true;
/*     */             } 
/* 175 */             return false;
/*     */           }
/*     */         });
/*     */   }
/*     */   private void addShellMenuItemClick(ActionEvent e) {
/* 180 */     ShellSetting manage = new ShellSetting(null, this.currentGroup);
/* 181 */     refreshShellView();
/*     */   }
/*     */   
/*     */   private void generateShellMenuItemClick(ActionEvent e) {
/* 185 */     GenerateShellLoder generateShellLoder = new GenerateShellLoder();
/*     */   }
/*     */   
/*     */   private void shellLiveScanMenuItemClick(ActionEvent e) {
/* 189 */     LiveScan liveScan = new LiveScan(this.currentGroup);
/*     */   }
/*     */   private void pluginConfigMenuItemClick(ActionEvent e) {
/* 192 */     PluginManage pluginManage = new PluginManage();
/*     */   }
/*     */   private void appConfigMenuItemClick(ActionEvent e) {
/* 195 */     AppSeting appSeting = new AppSeting();
/*     */   }
/*     */   private void aboutMenuItemClick(ActionEvent e) {
/* 198 */     GOptionPane.showMessageDialog(getFrame(), EasyI18N.getI18nString("由BeichenDream强力驱动\nMail:beichendream@gmail.com"), "About", -1);
/*     */   }
/*     */   
/*     */   private void copyShellViewSelectedMenuItemClick(ActionEvent e) {
/* 202 */     int columnIndex = this.shellView.getSelectedColumn();
/* 203 */     if (columnIndex != -1) {
/* 204 */       Object o = this.shellView.getValueAt(this.shellView.getSelectedRow(), this.shellView.getSelectedColumn());
/* 205 */       if (o != null) {
/* 206 */         String value = (String)o;
/* 207 */         Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(value), null);
/* 208 */         GOptionPane.showMessageDialog(getMainActivityFrame(), "复制成功", "提示", 1);
/*     */       } else {
/* 210 */         GOptionPane.showMessageDialog(getMainActivityFrame(), "选中列是空的", "提示", 2);
/*     */       } 
/*     */     } else {
/* 213 */       GOptionPane.showMessageDialog(getMainActivityFrame(), "未选中列", "提示", 2);
/*     */     } 
/*     */   }
/*     */   private void removeShellMenuItemClick(ActionEvent e) {
/* 217 */     String[] shellIds = getSlectedShellId();
/* 218 */     if (shellIds.length > 0) {
/* 219 */       int n = GOptionPane.showConfirmDialog(getMainActivityFrame(), String.format(EasyI18N.getI18nString("确定删除id在 %s 的shell吗?"), new Object[] { Arrays.toString((Object[])shellIds) }), "警告", 0);
/* 220 */       if (n == 0) {
/* 221 */         for (int i = 0; i < shellIds.length; i++) {
/* 222 */           String shellId = shellIds[i];
/* 223 */           String shshellInfo = Db.getOneShell(shellId).toString();
/* 224 */           Log.log("removeShell status:%s  -> %s", new Object[] { Boolean.valueOf((Db.removeShell(shellId) > 0)), shshellInfo });
/*     */         } 
/* 226 */         GOptionPane.showMessageDialog(getMainActivityFrame(), "删除成功", "提示", 1);
/* 227 */         refreshShellView();
/* 228 */       } else if (n == 1) {
/* 229 */         GOptionPane.showMessageDialog(getMainActivityFrame(), "已取消");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private String[] getSlectedShellId() {
/* 235 */     int[] rows = this.shellView.getSelectedRows();
/* 236 */     String[] shellIds = new String[rows.length];
/* 237 */     for (int i = 0; i < shellIds.length; i++) {
/* 238 */       shellIds[i] = (String)this.shellView.getValueAt(rows[i], 0);
/*     */     }
/* 240 */     return shellIds;
/*     */   }
/*     */   
/*     */   private void editShellMenuItemClick(ActionEvent e) {
/* 244 */     String[] shellIds = getSlectedShellId();
/* 245 */     if (shellIds.length > 0)
/* 246 */       for (int i = 0; i < shellIds.length; i++) {
/* 247 */         String shellId = shellIds[i];
/* 248 */         ShellSetting shellSetting = new ShellSetting(shellId, this.currentGroup);
/*     */       }  
/*     */   }
/*     */   
/*     */   private void interactMenuItemClick(ActionEvent e) {
/*     */     try {
/* 254 */       String shellId = (String)this.shellView.getValueAt(this.shellView.getSelectedRow(), 0);
/* 255 */       ShellManage shellManage = new ShellManage(Db.getOneShell(shellId));
/* 256 */     } catch (Throwable err) {
/* 257 */       ByteArrayOutputStream stream = new ByteArrayOutputStream();
/* 258 */       PrintStream printStream = new PrintStream(stream);
/* 259 */       err.printStackTrace(printStream);
/* 260 */       printStream.flush();
/* 261 */       printStream.close();
/* 262 */       GOptionPane.showMessageDialog(getMainActivityFrame(), new String(stream.toByteArray()));
/*     */     } 
/*     */   }
/*     */   private void interactCacheMenuItemClick(ActionEvent e) {
/* 266 */     String shellId = (String)this.shellView.getValueAt(this.shellView.getSelectedRow(), 0);
/*     */     
/*     */     try {
/* 269 */       if ((new File(String.format("%s/%s/cache.db", new Object[] { "GodzillaCache", shellId }))).isFile()) {
/* 270 */         ShellEntity shellEntity = Db.getOneShell(shellId);
/* 271 */         shellEntity.setUseCache(true);
/* 272 */         ShellManage shellManage = new ShellManage(shellEntity);
/*     */       } else {
/* 274 */         GOptionPane.showMessageDialog(getMainActivityFrame(), "缓存文件不存在");
/*     */       } 
/* 276 */     } catch (Throwable err) {
/* 277 */       ByteArrayOutputStream stream = new ByteArrayOutputStream();
/* 278 */       PrintStream printStream = new PrintStream(stream);
/* 279 */       err.printStackTrace(printStream);
/* 280 */       printStream.flush();
/* 281 */       printStream.close();
/* 282 */       GOptionPane.showMessageDialog(getMainActivityFrame(), new String(stream.toByteArray()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void refreshShellView() {
/* 289 */     Vector<Vector<String>> rowsVector = null;
/*     */     
/* 291 */     if (this.currentGroup.equals("/")) {
/* 292 */       rowsVector = Db.getAllShell();
/*     */     } else {
/* 294 */       rowsVector = Db.getAllShell(this.currentGroup);
/*     */     } 
/* 296 */     rowsVector.remove(0);
/* 297 */     this.shellView.AddRows(rowsVector);
/* 298 */     this.shellView.getModel().fireTableDataChanged();
/*     */ 
/*     */ 
/*     */     
/* 302 */     this.statusLabel.setText(String.format(EasyI18N.getI18nString("共有%d组 所有成员数:%d 当前组是:%s 当前组成员数:%d "), new Object[] { Integer.valueOf(Db.getAllGroup().size()), Integer.valueOf(Db.getAllShell().size() - 1), this.currentGroup, Integer.valueOf(rowsVector.size()) }));
/*     */   }
/*     */   
/*     */   private void refreshShellViewMenuItemClick(ActionEvent e) {
/* 306 */     refreshShellView();
/*     */   }
/*     */   public MainActivity getJFrame() {
/* 309 */     return this;
/*     */   }
/*     */   public static MainActivity getFrame() {
/* 312 */     return mainActivityFrame;
/*     */   }
/*     */   
/*     */   public static JMenuItem registerPluginJMenuItem(JMenuItem menuItem) {
/* 316 */     return pluginMenu.add(menuItem);
/*     */   }
/*     */   public static void registerPluginPopMenu(PopupMenu popupMenu) {
/* 319 */     pluginMenu.add(popupMenu);
/*     */   }
/*     */   public static JMenu registerJMenu(JMenu menu) {
/* 322 */     return menuBar.add(menu);
/*     */   }
/*     */   public static JMenuItem registerShellViewJMenuItem(JMenuItem menuItem) {
/* 325 */     return shellViewPopupMenu.add(menuItem);
/*     */   }
/*     */   public static void registerShellViewPopupMenu(PopupMenu popupMenu) {
/* 328 */     shellViewPopupMenu.add(popupMenu);
/*     */   }
/*     */   public static MainActivity getMainActivityFrame() {
/* 331 */     return mainActivityFrame;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/* 336 */       ApplicationContext.initUi();
/* 337 */     } catch (Exception e) {
/* 338 */       Log.error(e);
/*     */     } 
/* 340 */     initStatic();
/* 341 */     ApplicationConfig.invoke();
/* 342 */     MainActivity activity = new MainActivity();
/* 343 */     mainActivityFrame = activity.getJFrame();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\MainActivity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */