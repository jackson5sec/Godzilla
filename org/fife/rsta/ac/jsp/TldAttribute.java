/*    */ package org.fife.rsta.ac.jsp;
/*    */ 
/*    */ import org.fife.rsta.ac.html.AttributeCompletion;
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
/*    */ class TldAttribute
/*    */   extends AttributeCompletion
/*    */ {
/*    */   public boolean required;
/*    */   public boolean rtexprvalue;
/*    */   
/*    */   public TldAttribute(JspCompletionProvider provider, TldAttributeParam param) {
/* 31 */     super((CompletionProvider)provider, param);
/*    */   }
/*    */ 
/*    */   
/*    */   public static class TldAttributeParam
/*    */     extends ParameterizedCompletion.Parameter
/*    */   {
/*    */     private boolean required;
/*    */     private boolean rtextprvalue;
/*    */     
/*    */     public TldAttributeParam(Object type, String name, boolean required, boolean rtextprvalue) {
/* 42 */       super(type, name);
/* 43 */       this.required = required;
/* 44 */       this.rtextprvalue = rtextprvalue;
/*    */     }
/*    */     
/*    */     public boolean isRequired() {
/* 48 */       return this.required;
/*    */     }
/*    */     
/*    */     public boolean getRtextprvalue() {
/* 52 */       return this.rtextprvalue;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\jsp\TldAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */