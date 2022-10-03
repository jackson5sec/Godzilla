/*    */ package org.fife.rsta.ac.css;
/*    */ 
/*    */ import javax.swing.Icon;
/*    */ import org.fife.ui.autocomplete.BasicCompletion;
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
/*    */ 
/*    */ class BasicCssCompletion
/*    */   extends BasicCompletion
/*    */ {
/*    */   private String iconKey;
/*    */   
/*    */   public BasicCssCompletion(CompletionProvider provider, String value, String iconKey) {
/* 33 */     super(provider, value);
/* 34 */     this.iconKey = iconKey;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Icon getIcon() {
/* 40 */     return IconFactory.get().getIcon(this.iconKey);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\css\BasicCssCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */