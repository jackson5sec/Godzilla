/*     */ package core.ui.component.frame;
/*     */ import core.Db;
/*     */ import core.EasyI18N;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.MainActivity;
/*     */ import core.ui.component.DataView;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import core.ui.component.dialog.ShellSetting;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Vector;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ 
/*     */ public class LiveScan extends JDialog {
/*     */   private DataView shellView;
/*     */   private JButton addShellButton;
/*     */   private JButton removeFailShellButton;
/*     */   private JButton scanButton;
/*  41 */   private ComponentRenderer COMPONENT_RENDERER = new ComponentRenderer(); private JButton refreshButton;
/*     */   private Vector<String> columnVector;
/*  43 */   private static JLabel OK_LABEL = new JLabel("Succes"); private JSplitPane splitPane; private boolean isRuning; private String groupName;
/*  44 */   private static JLabel FAIL_LABEL = new JLabel("Fail");
/*  45 */   private static JLabel WAIT_LABEL = new JLabel("wait");
/*  46 */   private static JLabel DELETE_LABEL = new JLabel("deleted");
/*     */   
/*     */   static {
/*  49 */     OK_LABEL.setOpaque(true);
/*  50 */     FAIL_LABEL.setOpaque(true);
/*  51 */     WAIT_LABEL.setOpaque(true);
/*  52 */     DELETE_LABEL.setOpaque(true);
/*     */     
/*  54 */     DELETE_LABEL.setBackground(Color.DARK_GRAY);
/*  55 */     WAIT_LABEL.setBackground(Color.CYAN);
/*  56 */     OK_LABEL.setBackground(Color.GREEN);
/*  57 */     FAIL_LABEL.setBackground(Color.RED);
/*     */   }
/*     */   
/*     */   public LiveScan() {
/*  61 */     this("/");
/*     */   }
/*     */   public LiveScan(String groupId) {
/*  64 */     super((Frame)MainActivity.getFrame(), "LiveScan", true);
/*  65 */     this.groupName = groupId;
/*     */     
/*  67 */     this.addShellButton = new JButton("添加Shell");
/*  68 */     this.removeFailShellButton = new JButton("移除所有失败");
/*  69 */     this.refreshButton = new JButton("刷新");
/*  70 */     this.scanButton = new JButton("扫描");
/*  71 */     this.splitPane = new JSplitPane();
/*     */ 
/*     */ 
/*     */     
/*  75 */     Vector<Vector<String>> allShellVector = new Vector<>();
/*     */     
/*  77 */     allShellVector.addAll(Db.getAllShell(this.groupName));
/*     */     
/*  79 */     this.columnVector = allShellVector.remove(0);
/*     */     
/*  81 */     this.columnVector.add("Status");
/*     */     
/*  83 */     this.shellView = new DataView(null, this.columnVector, -1, -1);
/*     */ 
/*     */     
/*  86 */     refreshshellView();
/*     */     
/*  88 */     JPanel bottomPanel = new JPanel();
/*     */     
/*  90 */     bottomPanel.add(this.addShellButton);
/*  91 */     bottomPanel.add(this.scanButton);
/*  92 */     bottomPanel.add(this.refreshButton);
/*  93 */     bottomPanel.add(this.removeFailShellButton);
/*     */ 
/*     */     
/*  96 */     this.splitPane.setOrientation(0);
/*  97 */     this.splitPane.setTopComponent(new JScrollPane((Component)this.shellView));
/*  98 */     this.splitPane.setBottomComponent(bottomPanel);
/*     */     
/* 100 */     this.splitPane.addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentResized(ComponentEvent e) {
/* 103 */             LiveScan.this.splitPane.setDividerLocation(0.85D);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 108 */     JMenuItem removeShellMenuItem = new JMenuItem("删除");
/* 109 */     removeShellMenuItem.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 113 */             int selectedRow = LiveScan.this.shellView.getSelectedRow();
/* 114 */             int lastColumn = LiveScan.this.shellView.getColumnCount() - 1;
/* 115 */             if (selectedRow != -1) {
/* 116 */               String shellId = (String)LiveScan.this.shellView.getValueAt(selectedRow, 0);
/* 117 */               if (shellId != null) {
/* 118 */                 ShellEntity shellEntity = Db.getOneShell(shellId);
/* 119 */                 Log.log("removeShell -> " + shellEntity.toString(), new Object[0]);
/* 120 */                 if (Db.removeShell(shellId) > 0) {
/* 121 */                   GOptionPane.showMessageDialog(null, "删除成功");
/*     */                 } else {
/* 123 */                   GOptionPane.showMessageDialog(null, "删除失败");
/*     */                 } 
/* 125 */                 LiveScan.this.shellView.setValueAt(LiveScan.DELETE_LABEL, selectedRow, lastColumn);
/*     */               } 
/*     */             } 
/*     */           }
/*     */         });
/* 130 */     this.shellView.getRightClickMenu().add(removeShellMenuItem);
/*     */     
/* 132 */     automaticBindClick.bindJButtonClick(this, this);
/*     */     
/* 134 */     add(this.splitPane);
/*     */     
/* 136 */     functions.setWindowSize(this, 510, 430);
/*     */     
/* 138 */     setLocationRelativeTo((Component)MainActivity.getFrame());
/* 139 */     setDefaultCloseOperation(2);
/* 140 */     this.shellView.getColumn("Status").setCellRenderer(this.COMPONENT_RENDERER);
/* 141 */     EasyI18N.installObject(this);
/* 142 */     EasyI18N.installObject(this.shellView);
/* 143 */     setVisible(true);
/*     */   }
/*     */   protected void refreshshellView() {
/* 146 */     Vector<Vector<String>> rows = Db.getAllShell(this.groupName);
/* 147 */     rows.remove(0);
/*     */     
/* 149 */     rows.forEach(oneRow -> oneRow.add("WAIT_LABEL"));
/*     */ 
/*     */     
/* 152 */     this.shellView.AddRows(rows);
/*     */     
/* 154 */     int max = rows.size();
/* 155 */     int lastColumn = this.shellView.getColumnCount() - 1;
/*     */     
/* 157 */     for (int i = 0; i < max; i++) {
/* 158 */       this.shellView.setValueAt(WAIT_LABEL, i, lastColumn);
/*     */     }
/* 160 */     this.shellView.getModel().fireTableDataChanged();
/*     */   }
/*     */   protected void addShellButtonClick(ActionEvent actionEvent) {
/* 163 */     ShellSetting setting = new ShellSetting(null);
/* 164 */     refreshshellView();
/*     */   }
/*     */   private void removeFailShellButtonClick(ActionEvent actionEvent) {
/* 167 */     int max = this.shellView.getRowCount();
/* 168 */     int lastColumn = this.shellView.getColumnCount() - 1;
/* 169 */     Object valueObject = null;
/* 170 */     int removeNum = 0;
/* 171 */     for (int i = 0; i < max; i++) {
/* 172 */       valueObject = this.shellView.getValueAt(i, lastColumn);
/* 173 */       if (FAIL_LABEL.equals(valueObject)) {
/* 174 */         String shellId = (String)this.shellView.getValueAt(i, 0);
/* 175 */         if (shellId != null) {
/* 176 */           ShellEntity shellEntity = Db.getOneShell(shellId);
/* 177 */           Db.removeShell(shellId);
/* 178 */           Log.log("removeShell -> " + shellEntity.toString(), new Object[0]);
/* 179 */           this.shellView.setValueAt(DELETE_LABEL, i, lastColumn);
/* 180 */           removeNum++;
/*     */         } 
/*     */       } 
/*     */     } 
/* 184 */     GOptionPane.showMessageDialog(this, String.format(EasyI18N.getI18nString("共删除%s条Shell"), new Object[] { Integer.valueOf(removeNum) }));
/*     */   }
/*     */   protected synchronized void scanButtonClick(ActionEvent actionEvent) {
/* 187 */     if (!this.isRuning) {
/* 188 */       (new Thread(new Runnable()
/*     */           {
/*     */             public void run() {
/*     */               try {
/* 192 */                 LiveScan.this.scanStrart();
/* 193 */               } catch (Exception e) {
/* 194 */                 Log.error(e);
/*     */               } finally {
/* 196 */                 LiveScan.this.isRuning = false;
/*     */               } 
/*     */             }
/* 199 */           })).start();
/* 200 */       GOptionPane.showMessageDialog(this, "已开始存活检测");
/*     */     } else {
/* 202 */       GOptionPane.showMessageDialog(this, "正在检测");
/*     */     } 
/*     */   }
/*     */   protected void scanStrart() {
/* 206 */     long startTime = System.currentTimeMillis();
/* 207 */     int max = this.shellView.getRowCount();
/* 208 */     int lastColumn = this.shellView.getColumnCount() - 1;
/* 209 */     Object valueObject = null;
/* 210 */     ThreadPoolExecutor executor = new ThreadPoolExecutor(30, 50, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
/* 211 */     Log.log(String.format("LiveScanStart startTime:%s", new Object[] { (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Long.valueOf(System.currentTimeMillis())) }), new Object[0]);
/* 212 */     for (int i = 0; i < max; i++) {
/* 213 */       this.shellView.setValueAt(WAIT_LABEL, i, lastColumn);
/* 214 */       String shellId = (String)this.shellView.getValueAt(i, 0);
/* 215 */       executor.execute(new ScanShellRunnable(shellId, this.shellView, i, lastColumn));
/*     */     } 
/* 217 */     while (executor.getActiveCount() != 0);
/*     */ 
/*     */     
/* 220 */     executor.shutdown();
/* 221 */     long endTime = System.currentTimeMillis();
/* 222 */     Log.log(String.format("LiveScanComplete completeTime:%s", new Object[] { (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Long.valueOf(System.currentTimeMillis())) }), new Object[0]);
/* 223 */     int succes = 0;
/* 224 */     int fail = 0;
/* 225 */     for (int j = 0; j < max; j++) {
/* 226 */       valueObject = this.shellView.getValueAt(j, lastColumn);
/* 227 */       if (OK_LABEL.equals(valueObject)) {
/* 228 */         succes++;
/* 229 */       } else if (FAIL_LABEL.equals(valueObject)) {
/* 230 */         fail++;
/*     */       } 
/*     */     } 
/* 233 */     Log.log(String.format("LiveScanComplete: 用时:%sms", new Object[] { Long.valueOf(endTime - startTime) }), new Object[0]);
/* 234 */     setTitle(String.format("LiveScan all:%s succes:%s fail:%s", new Object[] { Integer.valueOf(max), Integer.valueOf(succes), Integer.valueOf(fail) }));
/* 235 */     GOptionPane.showMessageDialog(this, "Scan complete!");
/* 236 */     Log.log("Scan complete!", new Object[0]);
/*     */   }
/*     */   protected void refreshButtonClick(ActionEvent actionEvent) {
/* 239 */     refreshshellView();
/* 240 */     this.shellView.getColumn("Status").setCellRenderer(this.COMPONENT_RENDERER);
/*     */   }
/*     */   
/*     */   class ScanShellRunnable implements Runnable { private String shellId;
/*     */     private DataView dataView;
/*     */     private int rowId;
/*     */     private int columnId;
/*     */     
/*     */     public ScanShellRunnable(String shellId, DataView dataView, int rowId, int columnId) {
/* 249 */       this.shellId = shellId;
/* 250 */       this.dataView = dataView;
/* 251 */       this.rowId = rowId;
/* 252 */       this.columnId = columnId;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 257 */       boolean ok = false;
/*     */       try {
/* 259 */         ShellEntity shellEntity = Db.getOneShell(this.shellId);
/* 260 */         ok = shellEntity.initShellOpertion();
/*     */         try {
/* 262 */           if (ok) {
/* 263 */             shellEntity.getPayloadModule().close();
/*     */           }
/* 265 */         } catch (Exception e) {
/* 266 */           Log.error(e);
/*     */         } 
/* 268 */       } catch (Exception e) {
/* 269 */         Log.error(e);
/*     */       } 
/* 271 */       final boolean finalOk = ok;
/*     */       try {
/* 273 */         SwingUtilities.invokeAndWait(new Runnable()
/*     */             {
/*     */               public void run() {
/* 276 */                 if (finalOk) {
/* 277 */                   LiveScan.ScanShellRunnable.this.dataView.setValueAt(LiveScan.OK_LABEL, LiveScan.ScanShellRunnable.this.rowId, LiveScan.ScanShellRunnable.this.columnId);
/*     */                 } else {
/* 279 */                   LiveScan.ScanShellRunnable.this.dataView.setValueAt(LiveScan.FAIL_LABEL, LiveScan.ScanShellRunnable.this.rowId, LiveScan.ScanShellRunnable.this.columnId);
/*     */                 } 
/*     */               }
/*     */             });
/* 283 */       } catch (InterruptedException e) {
/* 284 */         e.printStackTrace();
/* 285 */       } catch (InvocationTargetException e) {
/* 286 */         e.printStackTrace();
/*     */       } 
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class ComponentRenderer
/*     */     implements TableCellRenderer
/*     */   {
/*     */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 297 */       if (Component.class.isAssignableFrom(value.getClass())) {
/* 298 */         Component component = (Component)value;
/* 299 */         if (isSelected) {
/* 300 */           component.setForeground(table.getSelectionForeground());
/*     */         } else {
/* 302 */           component.setForeground(table.getForeground());
/*     */         } 
/*     */         
/* 305 */         return component;
/*     */       } 
/* 307 */       return new JLabel(value.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\frame\LiveScan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */