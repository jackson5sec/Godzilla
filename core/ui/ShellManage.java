/*     */ package core.ui;
/*     */ import core.ApplicationContext;
/*     */ import core.EasyI18N;
/*     */ import core.annotation.DisplayName;
/*     */ import core.annotation.PluginAnnotation;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.component.ShellBasicsInfo;
/*     */ import core.ui.component.ShellCopyTab;
/*     */ import core.ui.component.ShellDatabasePanel;
/*     */ import core.ui.component.ShellExecCommandPanel;
/*     */ import core.ui.component.ShellFileManager;
/*     */ import core.ui.component.ShellNote;
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTabbedPane;
/*     */ import util.Log;
/*     */ import util.functions;
/*     */ 
/*     */ public class ShellManage extends JFrame {
/*     */   private JTabbedPane tabbedPane;
/*     */   private ShellEntity shellEntity;
/*     */   private ShellExecCommandPanel shellExecCommandPanel;
/*  30 */   private LinkedHashMap<String, Plugin> pluginMap = new LinkedHashMap<>(); private ShellBasicsInfo shellBasicsInfo; private ShellFileManager shellFileManager; private ShellDatabasePanel shellDatabasePanel;
/*  31 */   private LinkedHashMap<String, JPanel> globalComponent = new LinkedHashMap<>();
/*  32 */   private ArrayList<JPanel> allViews = new ArrayList<>();
/*     */   private Payload payload;
/*     */   private ShellCopyTab shellCopyTab;
/*  35 */   private JLabel loadLabel = new JLabel("loading......");
/*  36 */   private static final HashMap<String, String> CN_HASH_MAP = new HashMap<>();
/*     */   
/*     */   static {
/*  39 */     CN_HASH_MAP.put("payload", "有效载荷");
/*  40 */     CN_HASH_MAP.put("secretKey", "密钥");
/*  41 */     CN_HASH_MAP.put("password", "密码");
/*  42 */     CN_HASH_MAP.put("cryption", "加密器");
/*  43 */     CN_HASH_MAP.put("PROXYHOST", "代理主机");
/*  44 */     CN_HASH_MAP.put("PROXYPORT", "代理端口");
/*  45 */     CN_HASH_MAP.put("CONNTIMEOUT", "连接超时");
/*  46 */     CN_HASH_MAP.put("READTIMEOUT", "读取超时");
/*  47 */     CN_HASH_MAP.put("PROXY", "代理类型");
/*  48 */     CN_HASH_MAP.put("REMARK", "备注");
/*  49 */     CN_HASH_MAP.put("ENCODING", "编码");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ShellManage(ShellEntity shellEntity) {
/*  55 */     this.shellEntity = shellEntity;
/*  56 */     this.tabbedPane = (JTabbedPane)new RTabbedPane();
/*  57 */     String titleString = String.format("Url:%s Payload:%s Cryption:%s openCache:%s useCache:%s", new Object[] { this.shellEntity.getUrl(), this.shellEntity.getPayload(), this.shellEntity.getCryption(), Boolean.valueOf(shellEntity.isUseCache() ? false : ApplicationContext.isOpenCache()), Boolean.valueOf(shellEntity.isUseCache()) });
/*  58 */     setTitle(titleString);
/*  59 */     boolean state = this.shellEntity.initShellOpertion();
/*  60 */     if (state) {
/*  61 */       init();
/*     */     } else {
/*  63 */       setTitle("初始化失败");
/*  64 */       GOptionPane.showMessageDialog(this, "初始化失败", "提示", 2);
/*  65 */       dispose();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void init() {
/*  71 */     this.shellEntity.setFrame(this);
/*  72 */     this.payload = this.shellEntity.getPayloadModule();
/*     */     
/*  74 */     add(this.loadLabel);
/*     */     
/*  76 */     functions.setWindowSize(this, 1690, 680);
/*  77 */     setLocationRelativeTo(MainActivity.getFrame());
/*  78 */     setVisible(true);
/*  79 */     setDefaultCloseOperation(2);
/*     */ 
/*     */     
/*  82 */     initComponent();
/*     */   }
/*     */   private void initComponent() {
/*  85 */     remove(this.loadLabel);
/*  86 */     add(this.tabbedPane);
/*  87 */     loadGlobalComponent();
/*  88 */     if (!this.shellEntity.isUseCache()) {
/*  89 */       loadPlugins();
/*     */     }
/*  91 */     loadView();
/*  92 */     this.shellCopyTab.scan();
/*     */   }
/*     */   private void loadView() {
/*  95 */     this.allViews.addAll(this.globalComponent.values());
/*  96 */     for (String key : this.globalComponent.keySet()) {
/*  97 */       JPanel panel = this.globalComponent.get(key);
/*  98 */       EasyI18N.installObject(panel);
/*  99 */       String name = panel.getClass().getSimpleName();
/* 100 */       DisplayName displayName = panel.getClass().<DisplayName>getAnnotation(DisplayName.class);
/* 101 */       if (displayName != null) {
/* 102 */         name = EasyI18N.getI18nString(displayName.DisplayName());
/*     */       }
/* 104 */       EasyI18N.installObject(panel);
/* 105 */       this.tabbedPane.addTab(name, this.globalComponent.get(key));
/*     */     } 
/* 107 */     for (String key : this.pluginMap.keySet()) {
/* 108 */       Plugin plugin = this.pluginMap.get(key);
/* 109 */       JPanel panel = plugin.getView();
/* 110 */       PluginAnnotation pluginAnnotation = plugin.getClass().<PluginAnnotation>getAnnotation(PluginAnnotation.class);
/* 111 */       if (panel != null) {
/* 112 */         EasyI18N.installObject(plugin);
/* 113 */         EasyI18N.installObject(panel);
/* 114 */         this.tabbedPane.addTab(pluginAnnotation.Name(), panel);
/* 115 */         this.allViews.add(panel);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public static String getCNName(String name) {
/* 120 */     for (String key : CN_HASH_MAP.keySet()) {
/* 121 */       if (key.toUpperCase().equals(name.toUpperCase())) {
/* 122 */         return CN_HASH_MAP.get(key);
/*     */       }
/*     */     } 
/* 125 */     return name;
/*     */   }
/*     */   private void loadGlobalComponent() {
/* 128 */     this.shellCopyTab = new ShellCopyTab(this.shellEntity);
/* 129 */     this.globalComponent.put("BasicsInfo", this.shellBasicsInfo = new ShellBasicsInfo(this.shellEntity));
/* 130 */     this.globalComponent.put("ExecCommand", this.shellExecCommandPanel = new ShellExecCommandPanel(this.shellEntity));
/* 131 */     this.globalComponent.put("FileManage", this.shellFileManager = new ShellFileManager(this.shellEntity));
/* 132 */     this.globalComponent.put("DatabaseManage", this.shellDatabasePanel = new ShellDatabasePanel(this.shellEntity));
/* 133 */     this.globalComponent.put("Note", new ShellNote(this.shellEntity));
/* 134 */     this.globalComponent.put("Netstat", new ShellNetstat(this.shellEntity));
/* 135 */     this.globalComponent.put("CopyTab", this.shellCopyTab);
/*     */   }
/*     */   private String getPluginName(Plugin p) {
/* 138 */     PluginAnnotation pluginAnnotation = p.getClass().<PluginAnnotation>getAnnotation(PluginAnnotation.class);
/* 139 */     return pluginAnnotation.Name();
/*     */   }
/*     */   
/*     */   public Plugin createPlugin(String pluginName) {
/*     */     try {
/* 144 */       Plugin plugin = this.pluginMap.get(pluginName);
/* 145 */       if (plugin != null) {
/* 146 */         plugin = (Plugin)plugin.getClass().newInstance();
/* 147 */         plugin.init(this.shellEntity);
/* 148 */         plugin.getView();
/* 149 */         return plugin;
/*     */       } 
/* 151 */     } catch (Exception e) {
/* 152 */       Log.error(e);
/*     */     } 
/* 154 */     return null;
/*     */   }
/*     */   public ShellFileManager getShellFileManager() {
/* 157 */     return this.shellFileManager;
/*     */   }
/*     */   
/*     */   private void loadPlugins() {
/* 161 */     Plugin[] plugins = ApplicationContext.getAllPlugin(this.shellEntity.getPayload());
/*     */     int i;
/* 163 */     for (i = 0; i < plugins.length; i++) {
/*     */       try {
/* 165 */         Plugin plugin = plugins[i];
/* 166 */         this.pluginMap.put(getPluginName(plugin), plugin);
/* 167 */       } catch (Exception e) {
/* 168 */         Log.error(e);
/*     */       } 
/*     */     } 
/* 171 */     for (i = 0; i < plugins.length; i++) {
/*     */       try {
/* 173 */         Plugin plugin = plugins[i];
/* 174 */         plugin.init(this.shellEntity);
/* 175 */       } catch (Exception e) {
/* 176 */         Log.error(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public Plugin getPlugin(String pluginName) {
/* 181 */     return this.pluginMap.get(pluginName);
/*     */   }
/*     */   
/*     */   public void dispose() {
/*     */     try {
/* 186 */       this.tabbedPane.disable();
/* 187 */       for (JPanel jPanel : this.allViews) {
/* 188 */         if (jPanel.isEnabled()) {
/* 189 */           jPanel.disable();
/*     */         }
/*     */       } 
/* 192 */     } catch (Exception e) {
/* 193 */       Log.error(e);
/*     */     } 
/* 195 */     close();
/* 196 */     if (this.payload != null && ApplicationContext.isOpenC("isAutoCloseShell")) {
/*     */       try {
/* 198 */         Log.log(String.format("CloseShellState: %s\tShellId: %s\tShellHash: %s", new Object[] { Boolean.valueOf(this.shellEntity.getPayloadModule().close()), this.shellEntity.getId(), Integer.valueOf(this.shellEntity.hashCode()) }), new Object[0]);
/* 199 */       } catch (Exception e) {
/* 200 */         Log.error(e);
/*     */       } 
/*     */     }
/* 203 */     super.dispose();
/* 204 */     System.gc();
/*     */   }
/*     */   
/*     */   public void close() {
/* 208 */     this.pluginMap.keySet().forEach(key -> {
/*     */           Plugin plugin = this.pluginMap.get(key);
/*     */           try {
/*     */             Method method = functions.getMethodByClass(plugin.getClass(), "closePlugin", null);
/*     */             if (method != null) {
/*     */               method.invoke(plugin, null);
/*     */             }
/* 215 */           } catch (Exception e) {
/*     */             Log.error(e);
/*     */           } 
/*     */         });
/* 219 */     this.globalComponent.keySet().forEach(key -> {
/*     */           JPanel plugin = this.globalComponent.get(key);
/*     */           try {
/*     */             Method method = functions.getMethodByClass(plugin.getClass(), "closePlugin", null);
/*     */             if (method != null) {
/*     */               method.invoke(plugin, null);
/*     */             }
/* 226 */           } catch (Exception e) {
/*     */             Log.error(e);
/*     */           } 
/*     */         });
/* 230 */     this.pluginMap.clear();
/* 231 */     this.globalComponent.clear();
/*     */   }
/*     */   
/*     */   public LinkedHashMap<String, Plugin> getPluginMap() {
/* 235 */     return this.pluginMap;
/*     */   }
/*     */   
/*     */   public LinkedHashMap<String, JPanel> getGlobalComponent() {
/* 239 */     return this.globalComponent;
/*     */   }
/*     */   
/*     */   public JTabbedPane getTabbedPane() {
/* 243 */     return this.tabbedPane;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\ShellManage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */