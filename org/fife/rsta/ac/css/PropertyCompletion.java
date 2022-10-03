/*    */ package org.fife.rsta.ac.css;
/*    */ 
/*    */ import javax.swing.Icon;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
/*    */ import org.fife.ui.autocomplete.ShorthandCompletion;
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
/*    */ class PropertyCompletion
/*    */   extends ShorthandCompletion
/*    */ {
/*    */   private String iconKey;
/*    */   
/*    */   public PropertyCompletion(CompletionProvider provider, String property, String iconKey) {
/* 32 */     super(provider, property, property + ": ");
/* 33 */     this.iconKey = iconKey;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Icon getIcon() {
/* 39 */     return IconFactory.get().getIcon(this.iconKey);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\css\PropertyCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */