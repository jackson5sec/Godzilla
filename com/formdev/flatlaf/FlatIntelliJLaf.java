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
/*    */ public class FlatIntelliJLaf
/*    */   extends FlatLightLaf
/*    */ {
/*    */   public static final String NAME = "FlatLaf IntelliJ";
/*    */   
/*    */   public static boolean install() {
/* 33 */     return install(new FlatIntelliJLaf());
/*    */   }
/*    */   
/*    */   public static void installLafInfo() {
/* 37 */     installLafInfo("FlatLaf IntelliJ", (Class)FlatIntelliJLaf.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 42 */     return "FlatLaf IntelliJ";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 47 */     return "FlatLaf IntelliJ Look and Feel";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\FlatIntelliJLaf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */