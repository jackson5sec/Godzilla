/*    */ package org.fife.rsta.ac.html;
/*    */ 
/*    */ import org.fife.ui.autocomplete.AbstractCompletion;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
/*    */ import org.fife.ui.autocomplete.ParameterizedCompletion;
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
/*    */ public class AttributeCompletion
/*    */   extends AbstractCompletion
/*    */ {
/*    */   private ParameterizedCompletion.Parameter param;
/*    */   
/*    */   public AttributeCompletion(CompletionProvider provider, ParameterizedCompletion.Parameter param) {
/* 30 */     super(provider);
/* 31 */     this.param = param;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSummary() {
/* 37 */     return this.param.getDescription();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getReplacementText() {
/* 43 */     return this.param.getName();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\html\AttributeCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */