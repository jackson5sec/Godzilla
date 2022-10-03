/*    */ package org.fife.rsta.ac.demo;
/*    */ 
/*    */ import java.io.File;
/*    */ import javax.swing.filechooser.FileFilter;
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
/*    */ 
/*    */ class ExtensionFileFilter
/*    */   extends FileFilter
/*    */ {
/*    */   private String desc;
/*    */   private String ext;
/*    */   
/*    */   public ExtensionFileFilter(String desc, String ext) {
/* 36 */     this.desc = desc;
/* 37 */     this.ext = ext;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean accept(File f) {
/* 46 */     return (f.isDirectory() || f.getName().endsWith(this.ext));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 55 */     return this.desc + " (*." + this.ext + ")";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\demo\ExtensionFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */