/*    */ package org.fife.rsta.ac.java;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.Icon;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
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
/*    */ class PackageNameCompletion
/*    */   extends AbstractJavaSourceCompletion
/*    */ {
/*    */   public PackageNameCompletion(CompletionProvider provider, String text, String alreadyEntered) {
/* 30 */     super(provider, text.substring(text.lastIndexOf('.') + 1));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 36 */     return (obj instanceof PackageNameCompletion && ((PackageNameCompletion)obj)
/* 37 */       .getReplacementText().equals(getReplacementText()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Icon getIcon() {
/* 43 */     return IconFactory.get().getIcon("packageIcon");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 49 */     return getReplacementText().hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void rendererText(Graphics g, int x, int y, boolean selected) {
/* 55 */     g.drawString(getInputText(), x, y);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\PackageNameCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */