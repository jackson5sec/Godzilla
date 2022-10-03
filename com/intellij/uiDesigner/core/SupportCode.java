/*    */ package com.intellij.uiDesigner.core;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import javax.swing.JComponent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SupportCode
/*    */ {
/*    */   public static TextWithMnemonic parseText(String textWithMnemonic) {
/* 16 */     if (textWithMnemonic == null) {
/* 17 */       throw new IllegalArgumentException("textWithMnemonic cannot be null");
/*    */     }
/*    */ 
/*    */     
/* 21 */     int index = -1;
/* 22 */     StringBuilder plainText = new StringBuilder();
/* 23 */     for (int i = 0; i < textWithMnemonic.length(); i++) {
/* 24 */       char ch = textWithMnemonic.charAt(i);
/* 25 */       if (ch == '&') {
/* 26 */         i++;
/* 27 */         if (i >= textWithMnemonic.length()) {
/*    */           break;
/*    */         }
/* 30 */         ch = textWithMnemonic.charAt(i);
/* 31 */         if (ch != '&') {
/* 32 */           index = plainText.length();
/*    */         }
/*    */       } 
/* 35 */       plainText.append(ch);
/*    */     } 
/*    */     
/* 38 */     return new TextWithMnemonic(plainText.toString(), index);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final class TextWithMnemonic
/*    */   {
/*    */     public final String myText;
/*    */ 
/*    */     
/*    */     public final int myMnemonicIndex;
/*    */ 
/*    */     
/*    */     private TextWithMnemonic(String text, int index) {
/* 52 */       if (text == null) {
/* 53 */         throw new IllegalArgumentException("text cannot be null");
/*    */       }
/* 55 */       if (index != -1 && (index < 0 || index >= text.length())) {
/* 56 */         throw new IllegalArgumentException("wrong index: " + index + "; text = '" + text + "'");
/*    */       }
/* 58 */       this.myText = text;
/* 59 */       this.myMnemonicIndex = index;
/*    */     }
/*    */     
/*    */     public char getMnemonicChar() {
/* 63 */       if (this.myMnemonicIndex == -1) {
/* 64 */         throw new IllegalStateException("text doesn't contain mnemonic");
/*    */       }
/* 66 */       return Character.toUpperCase(this.myText.charAt(this.myMnemonicIndex));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setDisplayedMnemonicIndex(JComponent component, int index) {
/*    */     try {
/* 76 */       Method method = component.getClass().getMethod("setDisplayedMnemonicIndex", new Class[] { int.class });
/* 77 */       method.setAccessible(true);
/* 78 */       method.invoke(component, new Object[] { Integer.valueOf(index) });
/*    */     }
/* 80 */     catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\core\SupportCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */