/*    */ package org.fife.rsta.ac.css;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.regex.Pattern;
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
/*    */ class TimeCompletionGenerator
/*    */   implements CompletionGenerator
/*    */ {
/*    */   private static final String ICON_KEY = "css_propertyvalue_identifier";
/* 31 */   private static final Pattern DIGITS = Pattern.compile("\\d*");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Completion> generate(CompletionProvider provider, String input) {
/* 40 */     List<Completion> completions = new ArrayList<>();
/*    */     
/* 42 */     if (DIGITS.matcher(input).matches()) {
/* 43 */       completions.add(new TimeCompletion(provider, input + "s"));
/* 44 */       completions.add(new TimeCompletion(provider, input + "ms"));
/*    */     } 
/*    */     
/* 47 */     return completions;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static class TimeCompletion
/*    */     extends BasicCssCompletion
/*    */   {
/*    */     public TimeCompletion(CompletionProvider provider, String value) {
/* 58 */       super(provider, value, "css_propertyvalue_identifier");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\css\TimeCompletionGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */