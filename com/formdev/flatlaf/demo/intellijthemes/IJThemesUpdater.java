/*    */ package com.formdev.flatlaf.demo.intellijthemes;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import java.nio.file.CopyOption;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.StandardCopyOption;
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
/*    */ 
/*    */ public class IJThemesUpdater
/*    */ {
/*    */   public static void main(String[] args) {
/* 36 */     IJThemesManager themesManager = new IJThemesManager();
/* 37 */     themesManager.loadBundledThemes();
/*    */     
/* 39 */     for (IJThemeInfo ti : themesManager.bundledThemes) {
/* 40 */       if (ti.sourceCodeUrl == null || ti.sourceCodePath == null) {
/*    */         continue;
/*    */       }
/* 43 */       String fromUrl = ti.sourceCodeUrl + "/" + ti.sourceCodePath;
/* 44 */       if (fromUrl.contains("github.com")) {
/* 45 */         fromUrl = fromUrl + "?raw=true";
/* 46 */       } else if (fromUrl.contains("gitlab.com")) {
/* 47 */         fromUrl = fromUrl.replace("/blob/", "/raw/");
/*    */       } 
/* 49 */       String toPath = "../flatlaf-intellij-themes/src/main/resources/com/formdev/flatlaf/intellijthemes/themes/" + ti.resourceName;
/*    */       
/* 51 */       download(fromUrl, toPath);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void download(String fromUrl, String toPath) {
/* 56 */     System.out.println("Download " + fromUrl);
/*    */     
/* 58 */     Path out = (new File(toPath)).toPath();
/*    */     try {
/* 60 */       URL url = new URL(fromUrl.replace(" ", "%20"));
/* 61 */       URLConnection con = url.openConnection();
/* 62 */       Files.copy(con.getInputStream(), out, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 63 */     } catch (IOException ex) {
/* 64 */       ex.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\intellijthemes\IJThemesUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */