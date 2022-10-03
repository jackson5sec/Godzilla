/*     */ package core.ui.component;
/*     */ 
/*     */ import core.Encoding;
/*     */ import core.annotation.DisplayName;
/*     */ import core.imp.Payload;
/*     */ import core.shell.ShellEntity;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Vector;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
/*     */ 
/*     */ 
/*     */ @DisplayName(DisplayName = "网络详情")
/*     */ public class ShellNetstat
/*     */   extends JPanel
/*     */ {
/*  28 */   private static final Vector COLUMNS_VECTOR = new Vector(new CopyOnWriteArrayList((Object[])new String[] { "Proto", "Local Address", "Remote Address", "State" }));
/*     */ 
/*     */   
/*  31 */   private static final HashMap<String, String> LINUX_INET_FILE_MAPPING = new HashMap<>();
/*  32 */   private static final HashMap<String, String> LINUX_TCP_STATUS_MAPPING = new HashMap<>();
/*     */   
/*     */   private final DataView dataView;
/*     */   private final JButton getButton;
/*     */   private final JSplitPane portScanSplitPane;
/*     */   private final ShellEntity shellEntity;
/*     */   private final Payload payload;
/*     */   private Encoding encoding;
/*     */   
/*     */   static {
/*  42 */     LINUX_INET_FILE_MAPPING.put("tcp4", "/proc/net/tcp");
/*     */     
/*  44 */     LINUX_INET_FILE_MAPPING.put("udp4", "/proc/net/udp");
/*     */ 
/*     */     
/*  47 */     LINUX_TCP_STATUS_MAPPING.put("01", "ESTABLISHED");
/*  48 */     LINUX_TCP_STATUS_MAPPING.put("02", "SYN_SENT");
/*  49 */     LINUX_TCP_STATUS_MAPPING.put("03", "SYN_RECV");
/*  50 */     LINUX_TCP_STATUS_MAPPING.put("04", "FIN_WAIT1");
/*  51 */     LINUX_TCP_STATUS_MAPPING.put("05", "FIN_WAIT2");
/*  52 */     LINUX_TCP_STATUS_MAPPING.put("06", "TIME_WAIT");
/*  53 */     LINUX_TCP_STATUS_MAPPING.put("07", "CLOSE");
/*  54 */     LINUX_TCP_STATUS_MAPPING.put("08", "CLOSE_WAIT");
/*  55 */     LINUX_TCP_STATUS_MAPPING.put("09", "LAST_ACK");
/*  56 */     LINUX_TCP_STATUS_MAPPING.put("0A", "LISTEN");
/*  57 */     LINUX_TCP_STATUS_MAPPING.put("0B", "CLOSING");
/*     */   }
/*     */   
/*     */   public ShellNetstat(ShellEntity shellEntity) {
/*  61 */     this.shellEntity = shellEntity;
/*  62 */     this.payload = shellEntity.getPayloadModule();
/*     */     
/*  64 */     this.getButton = new JButton("scan");
/*  65 */     this.dataView = new DataView(null, COLUMNS_VECTOR, -1, -1);
/*  66 */     this.portScanSplitPane = new JSplitPane();
/*     */     
/*  68 */     this.portScanSplitPane.setOrientation(0);
/*  69 */     this.portScanSplitPane.setDividerSize(0);
/*     */     
/*  71 */     JPanel topPanel = new JPanel();
/*  72 */     topPanel.add(this.getButton);
/*     */     
/*  74 */     this.portScanSplitPane.setTopComponent(topPanel);
/*  75 */     this.portScanSplitPane.setBottomComponent(new JScrollPane(this.dataView));
/*     */     
/*  77 */     setLayout(new BorderLayout());
/*  78 */     add(this.portScanSplitPane);
/*  79 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */ 
/*     */   
/*     */   private void getButtonClick(ActionEvent actionEvent) {
/*     */     try {
/*  85 */       Vector<Vector<String>> rowsVector = null;
/*  86 */       if (!this.payload.isWindows()) {
/*  87 */         rowsVector = getLinuxNet();
/*     */       } else {
/*  89 */         rowsVector = getWinNet();
/*     */       } 
/*  91 */       this.dataView.AddRows(rowsVector);
/*  92 */     } catch (Exception e) {
/*  93 */       Log.error(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Vector<Vector<String>> getLinuxNet() {
/*  98 */     Vector<Vector<String>> rows = new Vector<>();
/*  99 */     for (String protoType : (String[])LINUX_INET_FILE_MAPPING.keySet().toArray((Object[])new String[0])) {
/* 100 */       String resultString = new String(this.payload.downloadFile(LINUX_INET_FILE_MAPPING.get(protoType)));
/* 101 */       String[] lines = resultString.split("\n");
/* 102 */       Log.log(resultString, new Object[0]);
/* 103 */       for (String line : lines) {
/*     */         try {
/* 105 */           if (line.indexOf("local_address") == -1) {
/* 106 */             String[] infos = line.trim().split("\\s+");
/* 107 */             if (infos.length > 10) {
/* 108 */               Vector<String> oneRow = new Vector<>();
/* 109 */               oneRow.add(protoType);
/* 110 */               oneRow.add(Inet4Addr(infos[1]));
/* 111 */               oneRow.add(Inet4Addr(infos[2]));
/* 112 */               oneRow.add(LINUX_TCP_STATUS_MAPPING.get(infos[3]));
/* 113 */               rows.add(oneRow);
/*     */             } 
/*     */           } 
/* 116 */         } catch (Exception e) {
/* 117 */           Log.error(line);
/* 118 */           Log.error(e);
/*     */         } 
/*     */       } 
/*     */     } 
/* 122 */     return rows;
/*     */   }
/*     */   private Vector<Vector<String>> getWinNet() {
/* 125 */     Vector<Vector<String>> rows = new Vector<>();
/* 126 */     String cmdResult = this.payload.execCommand("netstat -an");
/* 127 */     String[] lines = cmdResult.replace("\r", "").split("\n");
/* 128 */     for (String line : lines) {
/* 129 */       if (line.indexOf("TCP") != -1 || line.indexOf("UDP") != -1) {
/* 130 */         String[] infos = line.split("\\s+");
/* 131 */         Vector<String> oneRow = new Vector<>();
/* 132 */         int pt = -1;
/* 133 */         for (int i = 0; i < infos.length; i++) {
/* 134 */           if (infos[i].indexOf("TCP") != -1 || infos[i].indexOf("UDP") != -1) {
/* 135 */             pt = i;
/*     */             break;
/*     */           } 
/*     */         } 
/* 139 */         if (pt != -1) {
/* 140 */           oneRow.addAll(new CopyOnWriteArrayList<>(Arrays.copyOfRange(infos, pt, infos.length)));
/*     */         }
/* 142 */         rows.add(oneRow);
/*     */       } 
/*     */     } 
/* 145 */     return rows;
/*     */   }
/*     */   
/*     */   private String Inet4Addr(String hex) {
/* 149 */     String[] strings = hex.split(":");
/* 150 */     String ip = linuxHexToIP(strings[0]);
/* 151 */     int port = functions.byteToInt2(functions.hexToByte(strings[1]));
/* 152 */     return ip + ":" + port;
/*     */   }
/*     */   
/*     */   public static String linuxHexToIP(String hexString) {
/* 156 */     ArrayList<String> arrayList = new ArrayList<>();
/* 157 */     byte[] bs = functions.hexToByte(hexString);
/* 158 */     for (byte b : bs) {
/* 159 */       arrayList.add(Integer.toString(b & 0xFF));
/*     */     }
/* 161 */     Collections.reverse(arrayList);
/* 162 */     return Arrays.toString(arrayList.toArray()).replace(" ", "").replace("[", "").replace("]", "").replace(",", ".")
/* 163 */       .trim();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\ShellNetstat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */