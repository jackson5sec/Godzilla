/*    */ package org.fife.rsta.ac.css;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.fife.ui.autocomplete.Completion;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ class BorderStyleCompletionGenerator
/*    */   implements CompletionGenerator
/*    */ {
/*    */   private static final String ICON_KEY = "css_propertyvalue_identifier";
/*    */   
/*    */   public List<Completion> generate(CompletionProvider provider, String input) {
/* 37 */     List<Completion> completions = new ArrayList<>();
/*    */     
/* 39 */     completions.add(new BorderStyleCompletion(provider, "none"));
/* 40 */     completions.add(new BorderStyleCompletion(provider, "hidden"));
/* 41 */     completions.add(new BorderStyleCompletion(provider, "dotted"));
/* 42 */     completions.add(new BorderStyleCompletion(provider, "dashed"));
/* 43 */     completions.add(new BorderStyleCompletion(provider, "solid"));
/* 44 */     completions.add(new BorderStyleCompletion(provider, "double"));
/* 45 */     completions.add(new BorderStyleCompletion(provider, "groove"));
/* 46 */     completions.add(new BorderStyleCompletion(provider, "ridge"));
/* 47 */     completions.add(new BorderStyleCompletion(provider, "inset"));
/* 48 */     completions.add(new BorderStyleCompletion(provider, "outset"));
/*    */     
/* 50 */     return completions;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static class BorderStyleCompletion
/*    */     extends BasicCssCompletion
/*    */   {
/*    */     public BorderStyleCompletion(CompletionProvider provider, String value) {
/* 60 */       super(provider, value, "css_propertyvalue_identifier");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\css\BorderStyleCompletionGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */