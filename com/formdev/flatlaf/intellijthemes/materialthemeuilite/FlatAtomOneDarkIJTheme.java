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
/*    */ public class FlatAtomOneDarkIJTheme
/*    */   extends IntelliJTheme.ThemeLaf
/*    */ {
/*    */   public static final String NAME = "Atom One Dark (Material)";
/*    */   
/*    */   public static boolean install() {
/*    */     try {
/* 36 */       return install((LookAndFeel)new FlatAtomOneDarkIJTheme());
/* 37 */     } catch (RuntimeException ex) {
/* 38 */       return false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void installLafInfo() {
/* 43 */     installLafInfo("Atom One Dark (Material)", FlatAtomOneDarkIJTheme.class);
/*    */   }
/*    */   
/*    */   public FlatAtomOneDarkIJTheme() {
/* 47 */     super(Utils.loadTheme("Atom One Dark.theme.json"));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 52 */     return "Atom One Dark (Material)";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\intellijthemes\materialthemeuilite\FlatAtomOneDarkIJTheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */