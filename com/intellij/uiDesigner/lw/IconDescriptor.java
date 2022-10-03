/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
/*    */ import javax.swing.Icon;
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
/*    */ public class IconDescriptor
/*    */ {
/*    */   private String myIconPath;
/*    */   private Icon myIcon;
/*    */   
/*    */   public IconDescriptor(String iconPath) {
/* 28 */     this.myIconPath = iconPath;
/*    */   }
/*    */   
/*    */   public String getIconPath() {
/* 32 */     return this.myIconPath;
/*    */   }
/*    */   
/*    */   public Icon getIcon() {
/* 36 */     return this.myIcon;
/*    */   }
/*    */   
/*    */   public void setIcon(Icon icon) {
/* 40 */     this.myIcon = icon;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\IconDescriptor.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */