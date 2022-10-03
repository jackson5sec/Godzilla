/*    */ package com.kichik.pecoff4j.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FilenameFilter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PEFilenameFilter
/*    */   implements FilenameFilter
/*    */ {
/*    */   public boolean accept(File dir, String name) {
/* 18 */     return (name.toLowerCase().endsWith(".exe") || name
/* 19 */       .toLowerCase().endsWith(".dll"));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4\\util\PEFilenameFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */