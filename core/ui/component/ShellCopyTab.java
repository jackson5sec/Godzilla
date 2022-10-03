/*     */ package core.ui.component;
/*     */ 
/*     */ import core.EasyI18N;
/*     */ import core.annotation.DisplayName;
/*     */ import core.imp.Payload;
/*     */ import core.imp.Plugin;
/*     */ import core.shell.ShellEntity;
/*     */ import core.ui.ShellManage;
/*     */ import java.awt.Component;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTabbedPane;
/*     */ import util.Log;
/*     */ import util.automaticBindClick;
/*     */ 
/*     */ 
/*     */ @DisplayName(DisplayName = "插件标签管理")
/*     */ public class ShellCopyTab
/*     */   extends JPanel
/*     */ {
/*     */   private final ShellEntity shellEntity;
/*     */   private final Payload payload;
/*  31 */   private int currentComponent = 0;
/*     */   private final JComboBox<String> pluginComboBox;
/*     */   private final JLabel pluginLabel;
/*     */   private final JButton scanButton;
/*     */   private final JButton addButton;
/*     */   private final LinkedHashMap<String, Object> pluginMap;
/*     */   private final ArrayList pluginInstanceList;
/*     */   
/*     */   public ShellCopyTab(ShellEntity shellEntity) {
/*  40 */     super(new GridBagLayout());
/*  41 */     this.shellEntity = shellEntity;
/*  42 */     this.payload = shellEntity.getPayloadModule();
/*     */     
/*  44 */     this.pluginLabel = new JLabel("插件");
/*  45 */     this.addButton = new JButton("添加");
/*  46 */     this.scanButton = new JButton("扫描");
/*     */     
/*  48 */     this.pluginMap = new LinkedHashMap<>();
/*  49 */     this.pluginInstanceList = new ArrayList();
/*     */ 
/*     */     
/*  52 */     this.pluginComboBox = new JComboBox<>();
/*     */     
/*  54 */     addComponent(this.pluginLabel, this.pluginComboBox);
/*  55 */     addComponent(this.scanButton, this.addButton);
/*     */     
/*  57 */     automaticBindClick.bindJButtonClick(this, this);
/*     */   }
/*     */   
/*     */   public void scanButtonClick(ActionEvent e) {
/*  61 */     scan();
/*     */   }
/*     */   
/*     */   public void addButtonClick(ActionEvent e) throws Exception {
/*  65 */     String select = (String)this.pluginComboBox.getSelectedItem();
/*  66 */     if (select != null) {
/*  67 */       Object pluginObject = this.pluginMap.get(select);
/*  68 */       if (pluginObject != null) {
/*  69 */         ShellManage shellManage = this.shellEntity.getFrame();
/*  70 */         JTabbedPane tabbedPane = shellManage.getTabbedPane();
/*  71 */         Class<?> pluginClass = pluginObject.getClass();
/*  72 */         if (Plugin.class.isAssignableFrom(pluginClass)) {
/*  73 */           Plugin plugin = (Plugin)pluginClass.newInstance();
/*  74 */           plugin.init(this.shellEntity);
/*  75 */           tabbedPane.addTab(select, plugin.getView());
/*  76 */           EasyI18N.installObject(plugin);
/*  77 */           EasyI18N.installObject(plugin.getView());
/*  78 */           this.pluginInstanceList.add(plugin);
/*  79 */         } else if (JPanel.class.isAssignableFrom(pluginClass)) {
/*  80 */           JPanel panel = pluginClass.getConstructor(new Class[] { ShellEntity.class }).newInstance(new Object[] { this.shellEntity });
/*  81 */           EasyI18N.installObject(panel);
/*  82 */           tabbedPane.addTab(select, panel);
/*  83 */           this.pluginInstanceList.add(panel);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int scan() {
/*  90 */     this.pluginComboBox.removeAllItems();
/*  91 */     this.pluginMap.clear();
/*  92 */     ShellManage shellManage = this.shellEntity.getFrame();
/*  93 */     this.pluginMap.putAll(shellManage.getGlobalComponent());
/*  94 */     this.pluginMap.putAll(shellManage.getPluginMap());
/*  95 */     Iterator<String> iterato = this.pluginMap.keySet().iterator();
/*  96 */     while (iterato.hasNext()) {
/*  97 */       String keyString = iterato.next();
/*  98 */       this.pluginComboBox.addItem(keyString);
/*     */     } 
/* 100 */     return this.pluginComboBox.getItemCount();
/*     */   }
/*     */   
/*     */   void addComponent(Component label, Component component) {
/* 104 */     GBC gbcl = (new GBC(0, this.currentComponent)).setInsets(5, -40, 0, 0);
/* 105 */     GBC gbc = (new GBC(1, this.currentComponent, 3, 1)).setInsets(5, 20, 0, 0);
/* 106 */     add(label, gbcl);
/* 107 */     add(component, gbc);
/* 108 */     this.currentComponent++;
/*     */   }
/*     */   
/*     */   public void closePlugin() {
/* 112 */     this.pluginInstanceList.stream().forEach(o -> {
/*     */           try {
/*     */             Method method = o.getClass().getDeclaredMethod("closePlugin", null);
/*     */             if (method != null) {
/*     */               method.invoke(o, null);
/*     */             }
/* 118 */           } catch (Exception e) {
/*     */             Log.error(e);
/*     */           } 
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\ShellCopyTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */