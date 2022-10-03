/*     */ package shells.plugins.generic;
/*     */ import core.Encoding;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.DataView;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Vector;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SwingUtilities;
/*     */ import util.Log;
/*     */ import util.UiFunction;
/*     */ import util.functions;
/*     */ import util.http.ReqParameter;
/*     */ 
/*     */ public abstract class PortScan implements Plugin {
/*  26 */   private static final Vector COLUMNS_VECTOR = new Vector(new CopyOnWriteArrayList((Object[])new String[] { "IP", "Port", "Status" }));
/*     */   
/*  28 */   private static final JLabel OPEN_LABEL = new JLabel("Open");
/*  29 */   private static final JLabel CLOSED_LABEL = new JLabel("Closed");
/*  30 */   private static ComponentRenderer COMPONENT_RENDERER = null;
/*     */   
/*     */   private final JPanel panel;
/*     */   
/*     */   private final DataView dataView;
/*     */   
/*     */   private final JButton scanButton;
/*     */   private final JButton stopButton;
/*     */   private final JLabel hostLabel;
/*     */   private final JLabel portLabel;
/*     */   private final JCheckBox onlyOpenPortCheckBox;
/*     */   private final JTextField hostTextField;
/*     */   private final JTextField portTextField;
/*     */   private final JSplitPane portScanSplitPane;
/*     */   private boolean loadState;
/*     */   private ShellEntity shellEntity;
/*     */   private Payload payload;
/*     */   private Encoding encoding;
/*     */   private boolean isRunning;
/*     */   
/*     */   static {
/*  51 */     OPEN_LABEL.setOpaque(true);
/*  52 */     CLOSED_LABEL.setOpaque(true);
/*  53 */     OPEN_LABEL.setBackground(Color.GREEN);
/*  54 */     CLOSED_LABEL.setBackground(Color.RED);
/*     */   }
/*     */ 
/*     */   
/*     */   public PortScan() {
/*  59 */     this.panel = new JPanel(new BorderLayout());
/*     */     
/*  61 */     COMPONENT_RENDERER = new ComponentRenderer();
/*     */     
/*  63 */     this.hostLabel = new JLabel("host :");
/*  64 */     this.portLabel = new JLabel("ports :");
/*     */     
/*  66 */     this.scanButton = new JButton("scan");
/*  67 */     this.stopButton = new JButton("stop");
/*  68 */     this.dataView = new DataView(null, COLUMNS_VECTOR, -1, -1);
/*  69 */     this.hostTextField = new JTextField("127.0.0.1", 15);
/*  70 */     this.portTextField = new JTextField("21,22,80-81,88,443,445,873,1433,3306,3389,8080,8088,8888", 60);
/*  71 */     this.onlyOpenPortCheckBox = new JCheckBox("仅显示开放端口", false);
/*  72 */     this.portScanSplitPane = new JSplitPane();
/*     */     
/*  74 */     this.portScanSplitPane.setOrientation(0);
/*  75 */     this.portScanSplitPane.setDividerSize(0);
/*     */     
/*  77 */     JPanel topPanel = new JPanel();
/*  78 */     topPanel.add(this.hostLabel);
/*  79 */     topPanel.add(this.hostTextField);
/*  80 */     topPanel.add(this.portLabel);
/*  81 */     topPanel.add(this.portTextField);
/*  82 */     topPanel.add(this.onlyOpenPortCheckBox);
/*  83 */     topPanel.add(this.scanButton);
/*  84 */     topPanel.add(this.stopButton);
/*     */     
/*  86 */     this.portScanSplitPane.setTopComponent(topPanel);
/*  87 */     this.portScanSplitPane.setBottomComponent(new JScrollPane((Component)this.dataView));
/*     */     
/*  89 */     this.dataView.getColumn("Status").setCellRenderer(COMPONENT_RENDERER);
/*     */     
/*  91 */     this.panel.add(this.portScanSplitPane);
/*     */   }
/*     */   
/*     */   private void load() {
/*  95 */     if (!this.loadState) {
/*     */       try {
/*  97 */         byte[] data = readPlugin();
/*  98 */         if (this.loadState = this.payload.include(getClassName(), data)) {
/*  99 */           this.loadState = true;
/* 100 */           Log.log("Load success", new Object[0]);
/*     */         } else {
/* 102 */           Log.log("Load fail", new Object[0]);
/*     */         } 
/* 104 */       } catch (Exception e) {
/* 105 */         Log.error(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void scanButtonClick(ActionEvent actionEvent) {
/* 113 */     load();
/* 114 */     if (!this.isRunning) {
/* 115 */       this.isRunning = true;
/* 116 */       (new Thread(() -> {
/*     */             long startTime = System.currentTimeMillis();
/*     */ 
/*     */             
/*     */             LinkedList<String> hosts = functions.stringToIps(this.hostTextField.getText().trim());
/*     */ 
/*     */             
/*     */             String ports = formatPorts(this.portTextField.getText().trim());
/*     */ 
/*     */             
/*     */             if (ports.isEmpty() && hosts.isEmpty()) {
/*     */               this.isRunning = false;
/*     */ 
/*     */               
/*     */               GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), "host/ports 是空的");
/*     */ 
/*     */               
/*     */               return;
/*     */             } 
/*     */ 
/*     */             
/*     */             SwingUtilities.invokeLater(());
/*     */ 
/*     */             
/*     */             GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), "已开启扫描");
/*     */ 
/*     */             
/*     */             hosts.forEach(());
/*     */ 
/*     */             
/*     */             this.isRunning = false;
/*     */ 
/*     */             
/*     */             Log.log("扫描结束!!! 扫描耗时: %dms", new Object[] { Long.valueOf(System.currentTimeMillis() - startTime) });
/* 150 */           })).start();
/*     */     } else {
/* 152 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), "已有扫描线程");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void stopButtonClick(ActionEvent actionEvent) {
/* 157 */     if (this.isRunning) {
/* 158 */       this.isRunning = false;
/* 159 */       Log.log("PortScan: %s", new Object[] { "已停止扫描!" });
/* 160 */       GOptionPane.showMessageDialog(UiFunction.getParentFrame(this.panel), "已停止扫描!");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void closePlugin() {
/* 165 */     stopButtonClick(null);
/*     */   }
/*     */   
/*     */   private String formatPorts(String ports) {
/* 169 */     LinkedList<Integer> list = functions.stringToPorts(ports);
/* 170 */     StringBuilder stringBuilder = new StringBuilder();
/*     */     
/* 172 */     list.forEach(v -> stringBuilder.append(v.toString() + ","));
/*     */     
/* 174 */     if (stringBuilder.length() > 0) {
/* 175 */       return stringBuilder.substring(0, stringBuilder.length() - 1);
/*     */     }
/* 177 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private void formatResult(String resultString) {
/* 182 */     String[] lines = resultString.split("\n");
/* 183 */     String[] infos = null;
/* 184 */     Vector<Vector<String>> rowsVector = this.dataView.getDataVector();
/* 185 */     for (String line : lines) {
/* 186 */       infos = line.split("\t");
/* 187 */       if (infos.length >= 3) {
/* 188 */         boolean isOpen = "1".equals(infos[2]);
/* 189 */         if (!this.onlyOpenPortCheckBox.isSelected() || isOpen) {
/*     */ 
/*     */           
/* 192 */           Vector<String> oneRowVector = new Vector();
/* 193 */           oneRowVector.add(infos[0]);
/* 194 */           oneRowVector.add(infos[1]);
/* 195 */           oneRowVector.add(isOpen ? OPEN_LABEL : CLOSED_LABEL);
/* 196 */           rowsVector.add(oneRowVector);
/*     */         } 
/*     */       } else {
/* 199 */         Log.error(line);
/*     */       } 
/*     */     } 
/* 202 */     this.dataView.AddRows(rowsVector);
/* 203 */     this.dataView.getColumn("Status").setCellRenderer(COMPONENT_RENDERER);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ShellEntity shellEntity) {
/* 208 */     this.shellEntity = shellEntity;
/* 209 */     this.payload = this.shellEntity.getPayloadModule();
/* 210 */     this.encoding = Encoding.getEncoding(this.shellEntity);
/* 211 */     automaticBindClick.bindJButtonClick(PortScan.class, this, PortScan.class, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPanel getView() {
/* 218 */     return this.panel;
/*     */   }
/*     */   
/*     */   public abstract byte[] readPlugin() throws IOException;
/*     */   
/*     */   public abstract String getClassName();
/*     */   
/*     */   class ComponentRenderer
/*     */     implements TableCellRenderer {
/*     */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 228 */       if (Component.class.isAssignableFrom(value.getClass())) {
/* 229 */         Component component = (Component)value;
/* 230 */         if (isSelected) {
/* 231 */           component.setForeground(table.getSelectionForeground());
/*     */         } else {
/* 233 */           component.setForeground(table.getForeground());
/*     */         } 
/*     */         
/* 236 */         return component;
/*     */       } 
/* 238 */       return new JLabel(value.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\plugins\generic\PortScan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */