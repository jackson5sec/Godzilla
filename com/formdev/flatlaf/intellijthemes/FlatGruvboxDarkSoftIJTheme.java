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
/*    */ public class FlatGruvboxDarkSoftIJTheme
/*    */   extends IntelliJTheme.ThemeLaf
/*    */ {
/*    */   public static final String NAME = "Gruvbox Dark Soft";
/*    */   
/*    */   public static boolean install() {
/*    */     try {
/* 36 */       return install((LookAndFeel)new FlatGruvboxDarkSoftIJTheme());
/* 37 */     } catch (RuntimeException ex) {
/* 38 */       return false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void installLafInfo() {
/* 43 */     installLafInfo("Gruvbox Dark Soft", FlatGruvboxDarkSoftIJTheme.class);
/*    */   }
/*    */   
/*    */   public FlatGruvboxDarkSoftIJTheme() {
/* 47 */     super(Utils.loadTheme("gruvbox_dark_soft.theme.json"));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 52 */     return "Gruvbox Dark Soft";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\intellijthemes\FlatGruvboxDarkSoftIJTheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */