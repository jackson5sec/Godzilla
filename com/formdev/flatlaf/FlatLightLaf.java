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
/*    */ public class FlatLightLaf
/*    */   extends FlatLaf
/*    */ {
/*    */   public static final String NAME = "FlatLaf Light";
/*    */   
/*    */   public static boolean install() {
/* 38 */     return install(new FlatLightLaf());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void installLafInfo() {
/* 48 */     installLafInfo("FlatLaf Light", (Class)FlatLightLaf.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 53 */     return "FlatLaf Light";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 58 */     return "FlatLaf Light Look and Feel";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDark() {
/* 63 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\FlatLightLaf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */