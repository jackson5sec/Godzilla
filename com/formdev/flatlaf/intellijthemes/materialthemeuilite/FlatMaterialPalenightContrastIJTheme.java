/*    */ package com.formdev.flatlaf.intellijthemes.materialthemeuilite;
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
/*    */ public class FlatMaterialPalenightContrastIJTheme
/*    */   extends IntelliJTheme.ThemeLaf
/*    */ {
/*    */   public static final String NAME = "Material Palenight Contrast (Material)";
/*    */   
/*    */   public static boolean install() {
/*    */     try {
/* 36 */       return install((LookAndFeel)new FlatMaterialPalenightContrastIJTheme());
/* 37 */     } catch (RuntimeException ex) {
/* 38 */       return false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void installLafInfo() {
/* 43 */     installLafInfo("Material Palenight Contrast (Material)", FlatMaterialPalenightContrastIJTheme.class);
/*    */   }
/*    */   
/*    */   public FlatMaterialPalenightContrastIJTheme() {
/* 47 */     super(Utils.loadTheme("Material Palenight Contrast.theme.json"));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 52 */     return "Material Palenight Contrast (Material)";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\intellijthemes\materialthemeuilite\FlatMaterialPalenightContrastIJTheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */