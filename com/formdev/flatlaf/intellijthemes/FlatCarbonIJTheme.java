/*    */ package com.formdev.flatlaf.intellijthemes;
/*    */ 
/*    */ import com.formdev.flatlaf.IntelliJTheme;
/*    */ import javax.swing.LookAndFeel;
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
/*    */ public class FlatCarbonIJTheme
/*    */   extends IntelliJTheme.ThemeLaf
/*    */ {
/*    */   public static final String NAME = "Carbon";
/*    */   
/*    */   public static boolean install() {
/*    */     try {
/* 36 */       return install((LookAndFeel)new FlatCarbonIJTheme());
/* 37 */     } catch (RuntimeException ex) {
/* 38 */       return false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void installLafInfo() {
/* 43 */     installLafInfo("Carbon", FlatCarbonIJTheme.class);
/*    */   }
/*    */   
/*    */   public FlatCarbonIJTheme() {
/* 47 */     super(Utils.loadTheme("Carbon.theme.json"));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 52 */     return "Carbon";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\intellijthemes\FlatCarbonIJTheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */