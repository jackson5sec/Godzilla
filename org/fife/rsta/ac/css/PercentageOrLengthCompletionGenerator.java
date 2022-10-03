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
/*    */ 
/*    */ class PercentageOrLengthCompletionGenerator
/*    */   implements CompletionGenerator
/*    */ {
/*    */   private boolean includePercentage;
/*    */   private static final String ICON_KEY = "css_propertyvalue_unit";
/* 33 */   private static final Pattern DIGITS = Pattern.compile("\\d*");
/*    */ 
/*    */   
/*    */   public PercentageOrLengthCompletionGenerator(boolean includePercentage) {
/* 37 */     this.includePercentage = includePercentage;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Completion> generate(CompletionProvider provider, String input) {
/* 47 */     List<Completion> completions = new ArrayList<>();
/*    */     
/* 49 */     if (DIGITS.matcher(input).matches()) {
/*    */ 
/*    */       
/* 52 */       completions.add(new POrLCompletion(provider, input + "em"));
/* 53 */       completions.add(new POrLCompletion(provider, input + "ex"));
/* 54 */       completions.add(new POrLCompletion(provider, input + "ch"));
/* 55 */       completions.add(new POrLCompletion(provider, input + "rem"));
/*    */ 
/*    */       
/* 58 */       completions.add(new POrLCompletion(provider, input + "vh"));
/* 59 */       completions.add(new POrLCompletion(provider, input + "vw"));
/* 60 */       completions.add(new POrLCompletion(provider, input + "vmin"));
/* 61 */       completions.add(new POrLCompletion(provider, input + "vmax"));
/*    */ 
/*    */       
/* 64 */       completions.add(new POrLCompletion(provider, input + "px"));
/* 65 */       completions.add(new POrLCompletion(provider, input + "in"));
/* 66 */       completions.add(new POrLCompletion(provider, input + "cm"));
/* 67 */       completions.add(new POrLCompletion(provider, input + "mm"));
/* 68 */       completions.add(new POrLCompletion(provider, input + "pt"));
/* 69 */       completions.add(new POrLCompletion(provider, input + "pc"));
/*    */       
/* 71 */       if (this.includePercentage) {
/* 72 */         completions.add(new POrLCompletion(provider, input + "%"));
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 77 */     return completions;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static class POrLCompletion
/*    */     extends BasicCssCompletion
/*    */   {
/*    */     public POrLCompletion(CompletionProvider provider, String value) {
/* 87 */       super(provider, value, "css_propertyvalue_unit");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\css\PercentageOrLengthCompletionGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */