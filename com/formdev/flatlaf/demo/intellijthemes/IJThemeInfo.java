/*    */ package com.formdev.flatlaf.demo.intellijthemes;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ public class IJThemeInfo
/*    */   implements Serializable
/*    */ {
/*    */   protected String name;
/*    */   protected String resourceName;
/*    */   protected boolean dark;
/*    */   protected String license;
/*    */   protected String licenseFile;
/*    */   protected String sourceCodeUrl;
/*    */   protected String sourceCodePath;
/*    */   protected File themeFile;
/*    */   protected String lafClassName;
/*    */   
/*    */   public IJThemeInfo(String name, String resourceName, boolean dark, String license, String licenseFile, String sourceCodeUrl, String sourceCodePath, File themeFile, String lafClassName) {
/* 42 */     this.name = name;
/* 43 */     this.resourceName = resourceName;
/* 44 */     this.dark = dark;
/* 45 */     this.license = license;
/* 46 */     this.licenseFile = licenseFile;
/* 47 */     this.sourceCodeUrl = sourceCodeUrl;
/* 48 */     this.sourceCodePath = sourceCodePath;
/* 49 */     this.themeFile = themeFile;
/* 50 */     this.lafClassName = lafClassName;
/*    */   }
/*    */   
/*    */   public IJThemeInfo(String resourceName, String lafClassName) {
/* 54 */     this.resourceName = resourceName;
/* 55 */     this.lafClassName = lafClassName;
/*    */   }
/*    */   
/*    */   public String getResourceName() {
/* 59 */     return this.resourceName;
/*    */   }
/*    */   
/*    */   public void setResourceName(String resourceName) {
/* 63 */     this.resourceName = resourceName;
/*    */   }
/*    */   
/*    */   public String getLafClassName() {
/* 67 */     return this.lafClassName;
/*    */   }
/*    */   
/*    */   public void setLafClassName(String lafClassName) {
/* 71 */     this.lafClassName = lafClassName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 76 */     return "IJThemeInfo{name='" + this.name + '\'' + ", resourceName='" + this.resourceName + '\'' + ", dark=" + this.dark + ", license='" + this.license + '\'' + ", licenseFile='" + this.licenseFile + '\'' + ", sourceCodeUrl='" + this.sourceCodeUrl + '\'' + ", sourceCodePath='" + this.sourceCodePath + '\'' + ", themeFile=" + this.themeFile + ", lafClassName='" + this.lafClassName + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\intellijthemes\IJThemeInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */