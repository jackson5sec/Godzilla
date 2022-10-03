/*    */ package com.formdev.flatlaf;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FlatDarkLaf
/*    */   extends FlatLaf
/*    */ {
/*    */   public static final String NAME = "FlatLaf Dark";
/*    */   
/*    */   public static boolean install() {
/* 38 */     return install(new FlatDarkLaf());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void installLafInfo() {
/* 48 */     installLafInfo("FlatLaf Dark", (Class)FlatDarkLaf.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 53 */     return "FlatLaf Dark";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 58 */     return "FlatLaf Dark Look and Feel";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDark() {
/* 63 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\FlatDarkLaf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */