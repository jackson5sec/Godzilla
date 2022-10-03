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
/*    */ 
/*    */ class CommonFontCompletionGenerator
/*    */   implements CompletionGenerator
/*    */ {
/*    */   private static final String ICON_KEY = "css_propertyvalue_identifier";
/*    */   
/*    */   public List<Completion> generate(CompletionProvider provider, String input) {
/* 38 */     List<Completion> completions = new ArrayList<>();
/*    */     
/* 40 */     completions.add(new FontFamilyCompletion(provider, "Georgia"));
/* 41 */     completions.add(new FontFamilyCompletion(provider, "\"Times New Roman\""));
/* 42 */     completions.add(new FontFamilyCompletion(provider, "Arial"));
/* 43 */     completions.add(new FontFamilyCompletion(provider, "Helvetica"));
/* 44 */     completions.add(new FontFamilyCompletion(provider, "Impact"));
/* 45 */     completions.add(new FontFamilyCompletion(provider, "\"Lucida Sans Unicode\""));
/* 46 */     completions.add(new FontFamilyCompletion(provider, "Tahoma"));
/* 47 */     completions.add(new FontFamilyCompletion(provider, "Verdana"));
/* 48 */     completions.add(new FontFamilyCompletion(provider, "Geneva"));
/* 49 */     completions.add(new FontFamilyCompletion(provider, "\"Courier New\""));
/* 50 */     completions.add(new FontFamilyCompletion(provider, "Courier"));
/* 51 */     completions.add(new FontFamilyCompletion(provider, "\"Lucida Console\""));
/* 52 */     completions.add(new FontFamilyCompletion(provider, "Menlo"));
/* 53 */     completions.add(new FontFamilyCompletion(provider, "Monaco"));
/* 54 */     completions.add(new FontFamilyCompletion(provider, "Consolas"));
/*    */     
/* 56 */     return completions;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static class FontFamilyCompletion
/*    */     extends BasicCssCompletion
/*    */   {
/*    */     public FontFamilyCompletion(CompletionProvider provider, String value) {
/* 67 */       super(provider, value, "css_propertyvalue_identifier");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\css\CommonFontCompletionGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */