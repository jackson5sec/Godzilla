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
/*    */ public class FlatArcOrangeIJTheme
/*    */   extends IntelliJTheme.ThemeLaf
/*    */ {
/*    */   public static final String NAME = "Arc - Orange";
/*    */   
/*    */   public static boolean install() {
/*    */     try {
/* 36 */       return install((LookAndFeel)new FlatArcOrangeIJTheme());
/* 37 */     } catch (RuntimeException ex) {
/* 38 */       return false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void installLafInfo() {
/* 43 */     installLafInfo("Arc - Orange", FlatArcOrangeIJTheme.class);
/*    */   }
/*    */   
/*    */   public FlatArcOrangeIJTheme() {
/* 47 */     super(Utils.loadTheme("arc-theme-orange.theme.json"));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 52 */     return "Arc - Orange";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\intellijthemes\FlatArcOrangeIJTheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */