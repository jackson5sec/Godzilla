/*    */ package com.formdev.flatlaf.demo;
/*    */ 
/*    */ import com.formdev.flatlaf.FlatLaf;
/*    */ import com.formdev.flatlaf.FlatLightLaf;
/*    */ import com.formdev.flatlaf.FlatPropertiesLaf;
/*    */ import com.formdev.flatlaf.IntelliJTheme;
/*    */ import com.formdev.flatlaf.demo.intellijthemes.IJThemesPanel;
/*    */ import com.formdev.flatlaf.util.StringUtils;
/*    */ import java.beans.PropertyChangeEvent;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.util.prefs.Preferences;
/*    */ import javax.swing.LookAndFeel;
/*    */ import javax.swing.UIManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DemoPrefs
/*    */ {
/*    */   public static final String KEY_LAF = "laf";
/*    */   public static final String KEY_LAF_THEME = "lafTheme";
/*    */   public static final String RESOURCE_PREFIX = "res:";
/*    */   public static final String FILE_PREFIX = "file:";
/*    */   public static final String THEME_UI_KEY = "__FlatLaf.demo.theme";
/*    */   private static Preferences state;
/*    */   
/*    */   public static Preferences getState() {
/* 46 */     return state;
/*    */   }
/*    */   
/*    */   public static void init(String rootPath) {
/* 50 */     state = Preferences.userRoot().node(rootPath);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void initLaf(String[] args) {
/*    */     try {
/* 56 */       if (args.length > 0) {
/* 57 */         UIManager.setLookAndFeel(args[0]);
/*    */       } else {
/* 59 */         String lafClassName = state.get("laf", FlatLightLaf.class.getName());
/* 60 */         if (IntelliJTheme.ThemeLaf.class.getName().equals(lafClassName)) {
/* 61 */           String theme = state.get("lafTheme", "");
/* 62 */           if (theme.startsWith("res:")) {
/* 63 */             IntelliJTheme.install(IJThemesPanel.class.getResourceAsStream("/com/formdev/flatlaf/intellijthemes/themes/" + theme.substring("res:".length())));
/* 64 */           } else if (theme.startsWith("file:")) {
/* 65 */             FlatLaf.install((LookAndFeel)IntelliJTheme.createLaf(new FileInputStream(theme.substring("file:".length()))));
/*    */           } else {
/* 67 */             FlatLightLaf.install();
/*    */           } 
/*    */           
/* 70 */           if (!theme.isEmpty()) {
/* 71 */             UIManager.getLookAndFeelDefaults().put("__FlatLaf.demo.theme", theme);
/*    */           }
/* 73 */         } else if (FlatPropertiesLaf.class.getName().equals(lafClassName)) {
/* 74 */           String theme = state.get("lafTheme", "");
/* 75 */           if (theme.startsWith("file:")) {
/* 76 */             File themeFile = new File(theme.substring("file:".length()));
/* 77 */             String themeName = StringUtils.removeTrailing(themeFile.getName(), ".properties");
/* 78 */             FlatLaf.install((LookAndFeel)new FlatPropertiesLaf(themeName, themeFile));
/*    */           } else {
/* 80 */             FlatLightLaf.install();
/*    */           } 
/*    */           
/* 83 */           if (!theme.isEmpty()) {
/* 84 */             UIManager.getLookAndFeelDefaults().put("__FlatLaf.demo.theme", theme);
/*    */           }
/*    */         } else {
/* 87 */           UIManager.setLookAndFeel(lafClassName);
/*    */         } 
/*    */       } 
/* 90 */     } catch (Throwable ex) {
/* 91 */       ex.printStackTrace();
/*    */ 
/*    */       
/* 94 */       FlatLightLaf.install();
/*    */     } 
/*    */ 
/*    */     
/* 98 */     UIManager.addPropertyChangeListener(e -> {
/*    */           if ("lookAndFeel".equals(e.getPropertyName()))
/*    */             state.put("laf", UIManager.getLookAndFeel().getClass().getName()); 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\DemoPrefs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */