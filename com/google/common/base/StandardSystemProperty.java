/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ public enum StandardSystemProperty
/*     */ {
/*  30 */   JAVA_VERSION("java.version"),
/*     */ 
/*     */   
/*  33 */   JAVA_VENDOR("java.vendor"),
/*     */ 
/*     */   
/*  36 */   JAVA_VENDOR_URL("java.vendor.url"),
/*     */ 
/*     */   
/*  39 */   JAVA_HOME("java.home"),
/*     */ 
/*     */   
/*  42 */   JAVA_VM_SPECIFICATION_VERSION("java.vm.specification.version"),
/*     */ 
/*     */   
/*  45 */   JAVA_VM_SPECIFICATION_VENDOR("java.vm.specification.vendor"),
/*     */ 
/*     */   
/*  48 */   JAVA_VM_SPECIFICATION_NAME("java.vm.specification.name"),
/*     */ 
/*     */   
/*  51 */   JAVA_VM_VERSION("java.vm.version"),
/*     */ 
/*     */   
/*  54 */   JAVA_VM_VENDOR("java.vm.vendor"),
/*     */ 
/*     */   
/*  57 */   JAVA_VM_NAME("java.vm.name"),
/*     */ 
/*     */   
/*  60 */   JAVA_SPECIFICATION_VERSION("java.specification.version"),
/*     */ 
/*     */   
/*  63 */   JAVA_SPECIFICATION_VENDOR("java.specification.vendor"),
/*     */ 
/*     */   
/*  66 */   JAVA_SPECIFICATION_NAME("java.specification.name"),
/*     */ 
/*     */   
/*  69 */   JAVA_CLASS_VERSION("java.class.version"),
/*     */ 
/*     */   
/*  72 */   JAVA_CLASS_PATH("java.class.path"),
/*     */ 
/*     */   
/*  75 */   JAVA_LIBRARY_PATH("java.library.path"),
/*     */ 
/*     */   
/*  78 */   JAVA_IO_TMPDIR("java.io.tmpdir"),
/*     */ 
/*     */   
/*  81 */   JAVA_COMPILER("java.compiler"),
/*     */ 
/*     */   
/*  84 */   JAVA_EXT_DIRS("java.ext.dirs"),
/*     */ 
/*     */   
/*  87 */   OS_NAME("os.name"),
/*     */ 
/*     */   
/*  90 */   OS_ARCH("os.arch"),
/*     */ 
/*     */   
/*  93 */   OS_VERSION("os.version"),
/*     */ 
/*     */   
/*  96 */   FILE_SEPARATOR("file.separator"),
/*     */ 
/*     */   
/*  99 */   PATH_SEPARATOR("path.separator"),
/*     */ 
/*     */   
/* 102 */   LINE_SEPARATOR("line.separator"),
/*     */ 
/*     */   
/* 105 */   USER_NAME("user.name"),
/*     */ 
/*     */   
/* 108 */   USER_HOME("user.home"),
/*     */ 
/*     */   
/* 111 */   USER_DIR("user.dir");
/*     */   
/*     */   private final String key;
/*     */   
/*     */   StandardSystemProperty(String key) {
/* 116 */     this.key = key;
/*     */   }
/*     */ 
/*     */   
/*     */   public String key() {
/* 121 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String value() {
/* 129 */     return System.getProperty(this.key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 135 */     return key() + "=" + value();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\StandardSystemProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */