/*    */ package com.formdev.flatlaf.demo.intellijthemes;
/*    */ 
/*    */ import com.formdev.flatlaf.json.Json;
/*    */ import com.formdev.flatlaf.util.StringUtils;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.Reader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ class IJThemesManager
/*    */ {
/* 36 */   final List<IJThemeInfo> bundledThemes = new ArrayList<>();
/* 37 */   final List<IJThemeInfo> moreThemes = new ArrayList<>();
/* 38 */   private final Map<File, Long> lastModifiedMap = new HashMap<>();
/*    */   
/*    */   void loadBundledThemes() {
/*    */     Map<String, Object> json;
/* 42 */     this.bundledThemes.clear();
/*    */ 
/*    */ 
/*    */     
/* 46 */     try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("themes.json"), StandardCharsets.UTF_8)) {
/* 47 */       json = (Map<String, Object>)Json.parse(reader);
/* 48 */     } catch (IOException ex) {
/* 49 */       ex.printStackTrace();
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 54 */     for (Map.Entry<String, Object> e : json.entrySet()) {
/* 55 */       String resourceName = e.getKey();
/* 56 */       Map<String, String> value = (Map<String, String>)e.getValue();
/* 57 */       String name = value.get("name");
/* 58 */       boolean dark = Boolean.parseBoolean(value.get("dark"));
/* 59 */       String license = value.get("license");
/* 60 */       String licenseFile = value.get("licenseFile");
/* 61 */       String sourceCodeUrl = value.get("sourceCodeUrl");
/* 62 */       String sourceCodePath = value.get("sourceCodePath");
/*    */       
/* 64 */       this.bundledThemes.add(new IJThemeInfo(name, resourceName, dark, license, licenseFile, sourceCodeUrl, sourceCodePath, null, null));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   void loadThemesFromDirectory() {
/* 70 */     File directory = (new File("")).getAbsoluteFile();
/*    */     
/* 72 */     File[] themeFiles = directory.listFiles((dir, name) -> 
/* 73 */         (name.endsWith(".theme.json") || name.endsWith(".properties")));
/*    */     
/* 75 */     if (themeFiles == null) {
/*    */       return;
/*    */     }
/* 78 */     this.lastModifiedMap.clear();
/* 79 */     this.lastModifiedMap.put(directory, Long.valueOf(directory.lastModified()));
/*    */     
/* 81 */     this.moreThemes.clear();
/* 82 */     for (File f : themeFiles) {
/* 83 */       String fname = f.getName();
/*    */ 
/*    */       
/* 86 */       String name = fname.endsWith(".properties") ? StringUtils.removeTrailing(fname, ".properties") : StringUtils.removeTrailing(fname, ".theme.json");
/* 87 */       this.moreThemes.add(new IJThemeInfo(name, null, false, null, null, null, null, f, null));
/* 88 */       this.lastModifiedMap.put(f, Long.valueOf(f.lastModified()));
/*    */     } 
/*    */   }
/*    */   
/*    */   boolean hasThemesFromDirectoryChanged() {
/* 93 */     for (Map.Entry<File, Long> e : this.lastModifiedMap.entrySet()) {
/* 94 */       if (((File)e.getKey()).lastModified() != ((Long)e.getValue()).longValue())
/* 95 */         return true; 
/*    */     } 
/* 97 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\intellijthemes\IJThemesManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */