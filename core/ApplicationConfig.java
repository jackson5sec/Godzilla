/*     */ package core;
/*     */ 
/*     */ import core.ui.component.dialog.GOptionPane;
/*     */ import core.ui.component.dialog.ImageShowDialog;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.HashMap;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JOptionPane;
/*     */ import util.Log;
/*     */ import util.functions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ApplicationConfig
/*     */ {
/*     */   private static final String GITEE_CONFIG_URL = "https://gitee.com/beichendram/Godzilla/raw/master/%s";
/*     */   private static final String GIT_CONFIG_URL = "https://raw.githubusercontent.com/BeichenDream/Godzilla/master/%s";
/*  27 */   private static String ACCESS_URL = "https://gitee.com/beichendram/Godzilla/raw/master/%s";
/*     */   public static final String GIT = "https://github.com/BeichenDream/Godzilla";
/*  29 */   private static final HashMap<String, String> headers = new HashMap<>();
/*     */ 
/*     */   
/*     */   static {
/*  33 */     headers.put("Accept", "*/*");
/*  34 */     headers.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void invoke() {
/*  41 */     if (functions.getCurrentJarFile() == null) {
/*     */       return;
/*     */     }
/*     */     
/*  45 */     HashMap<String, String> configMap = null;
/*     */     
/*     */     try {
/*  48 */       configMap = getAppConfig(String.format("https://gitee.com/beichendram/Godzilla/raw/master/%s", new Object[] { "application.config" }));
/*  49 */       ACCESS_URL = "https://gitee.com/beichendram/Godzilla/raw/master/%s";
/*  50 */     } catch (Exception e) {
/*     */       try {
/*  52 */         configMap = getAppConfig(String.format("https://raw.githubusercontent.com/BeichenDream/Godzilla/master/%s", new Object[] { "application.config" }));
/*  53 */         ACCESS_URL = "https://raw.githubusercontent.com/BeichenDream/Godzilla/master/%s";
/*  54 */       } catch (Exception e2) {
/*  55 */         Log.error("Network connection failure");
/*     */       } 
/*     */     } 
/*     */     try {
/*  59 */       HashMap<String, String> md5SumMap = getAppConfig(String.format(ACCESS_URL, new Object[] { "hashsumJar" }));
/*  60 */       String hashString = md5SumMap.get("4.01");
/*  61 */       File jarFile = functions.getCurrentJarFile();
/*  62 */       String jarHashString = new String();
/*  63 */       if (jarFile != null) {
/*  64 */         FileInputStream inputStream = new FileInputStream(jarFile);
/*  65 */         byte[] jar = functions.readInputStream(inputStream);
/*  66 */         inputStream.close();
/*  67 */         jarHashString = functions.SHA(jar, "SHA-512");
/*     */       } 
/*  69 */       if (hashString != null) {
/*  70 */         if (jarFile != null) {
/*  71 */           if (!jarHashString.equals(hashString)) {
/*  72 */             String tipString = EasyI18N.getI18nString("??????????????????????????????????????????   ????????????????????????\r\n??????Jar??????:%s\r\n??????Jar??????:%s", new Object[] { hashString, jarHashString });
/*  73 */             GOptionPane.showMessageDialog(null, tipString, EasyI18N.getI18nString("??????\t????????????:", new Object[] { "4.01" }), 2);
/*  74 */             Log.error(String.format(tipString, new Object[] { hashString, jarHashString }));
/*  75 */             System.exit(0);
/*     */           } else {
/*  77 */             Log.error(EasyI18N.getI18nString("??????Hash??????   Hash Url:%s\r\n??????Jar??????:%s\r\n??????Jar??????:%s", new Object[] { String.format(ACCESS_URL, new Object[] { "hashsumJar" }), hashString, jarHashString }));
/*     */           } 
/*     */         } else {
/*  80 */           String tipString = EasyI18N.getI18nString("?????????Jar??????\r\n??????????????????????????????????????????   ????????????????????????");
/*  81 */           GOptionPane.showMessageDialog(null, tipString, EasyI18N.getI18nString("??????\t????????????:%s", new Object[] { "4.01", hashString }), 2);
/*  82 */           Log.error(tipString);
/*  83 */           System.exit(0);
/*     */         } 
/*     */       } else {
/*  86 */         String tipString = EasyI18N.getI18nString("?????????????????????(%s)???Hash\r\n??????Hash:%s\r\n??????????????????????????????????????????   ????????????????????????", new Object[] { "4.01", jarHashString });
/*  87 */         JOptionPane.showMessageDialog(null, tipString, EasyI18N.getI18nString("??????\t????????????:%s", new Object[] { "4.01" }), 2);
/*  88 */         Log.error(String.format(tipString, new Object[] { "4.01" }));
/*  89 */         System.exit(0);
/*     */       } 
/*  91 */     } catch (Exception e) {
/*  92 */       Log.error(e);
/*     */     } 
/*     */     
/*  95 */     if (configMap != null && configMap.size() > 0) {
/*     */ 
/*     */       
/*  98 */       String version = configMap.get("currentVersion");
/*  99 */       boolean isShowGroup = Boolean.valueOf(configMap.get("isShowGroup")).booleanValue();
/*     */       
/* 101 */       String wxGroupImageUrl = configMap.get("wxGroupImageUrl");
/*     */       
/* 103 */       String showGroupTitle = configMap.get("showGroupTitle");
/*     */       
/* 105 */       String gitUrl = configMap.get("gitUrl");
/*     */       
/* 107 */       boolean isShowAppTip = Boolean.valueOf(configMap.get("isShowAppTip")).booleanValue();
/*     */       
/* 109 */       String appTip = configMap.get("appTip");
/*     */       
/* 111 */       if (version != null && wxGroupImageUrl != null && appTip != null && gitUrl != null) {
/*     */         
/* 113 */         if (functions.stringToint(version.replace(".", "")) > functions.stringToint("4.01".replace(".", ""))) {
/* 114 */           GOptionPane.showMessageDialog(null, EasyI18N.getI18nString("?????????????????????\n????????????:%s\n????????????:%s", new Object[] { "4.01", version }), "message", 2);
/* 115 */           functions.openBrowseUrl(gitUrl);
/*     */         } 
/*     */         
/* 118 */         if (isShowAppTip) {
/* 119 */           JOptionPane.showMessageDialog(null, appTip, "message", 1);
/*     */         }
/*     */         
/* 122 */         if (isShowGroup) {
/*     */           try {
/* 124 */             ImageIcon imageIcon = new ImageIcon(ImageIO.read(new ByteArrayInputStream(functions.httpReqest(wxGroupImageUrl, "GET", headers, null))));
/* 125 */             ImageShowDialog.showImageDiaolog(imageIcon, showGroupTitle);
/* 126 */           } catch (IOException e) {
/* 127 */             Log.error(e);
/* 128 */             Log.error("showGroup fail!");
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static HashMap<String, String> getAppConfig(String configUrl) throws Exception {
/*     */     String configString;
/* 139 */     byte[] result = functions.httpReqest(configUrl, "GET", headers, null);
/* 140 */     if (result == null) {
/* 141 */       throw new Exception("readApplication Fail!");
/*     */     }
/*     */     
/*     */     try {
/* 145 */       configString = new String(result, "utf-8");
/* 146 */     } catch (UnsupportedEncodingException e) {
/* 147 */       configString = new String(result);
/*     */     } 
/* 149 */     HashMap<String, String> hashMap = new HashMap<>();
/* 150 */     String[] lines = configString.split("\n");
/* 151 */     for (String line : lines) {
/* 152 */       int index = line.indexOf(':');
/* 153 */       if (index != -1) {
/* 154 */         hashMap.put(line.substring(0, index).trim(), line.substring(index + 1).trim());
/*     */       }
/*     */     } 
/* 157 */     return hashMap;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\ApplicationConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */