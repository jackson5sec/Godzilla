/*     */ package org.fife.rsta.ac.css;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.TemplateCompletion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ColorCompletionGenerator
/*     */   implements CompletionGenerator
/*     */ {
/*     */   private List<Completion> defaults;
/*     */   private static final String FUNC_ICON_KEY = "css_propertyvalue_function";
/*     */   private static final String ICON_KEY = "css_propertyvalue_identifier";
/*  35 */   private static final Pattern DIGITS = Pattern.compile("\\d*");
/*     */   
/*     */   public ColorCompletionGenerator(CompletionProvider provider) {
/*  38 */     this.defaults = createDefaults(provider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<Completion> createDefaults(CompletionProvider provider) {
/*  45 */     List<Completion> completions = new ArrayList<>();
/*     */ 
/*     */     
/*  48 */     completions.add(new ColorCompletion(provider, "black"));
/*  49 */     completions.add(new ColorCompletion(provider, "silver"));
/*  50 */     completions.add(new ColorCompletion(provider, "gray"));
/*  51 */     completions.add(new ColorCompletion(provider, "white"));
/*  52 */     completions.add(new ColorCompletion(provider, "maroon"));
/*  53 */     completions.add(new ColorCompletion(provider, "red"));
/*  54 */     completions.add(new ColorCompletion(provider, "purple"));
/*  55 */     completions.add(new ColorCompletion(provider, "fuchsia"));
/*  56 */     completions.add(new ColorCompletion(provider, "green"));
/*  57 */     completions.add(new ColorCompletion(provider, "lime"));
/*  58 */     completions.add(new ColorCompletion(provider, "olive"));
/*  59 */     completions.add(new ColorCompletion(provider, "yellow"));
/*  60 */     completions.add(new ColorCompletion(provider, "navy"));
/*  61 */     completions.add(new ColorCompletion(provider, "blue"));
/*  62 */     completions.add(new ColorCompletion(provider, "teal"));
/*  63 */     completions.add(new ColorCompletion(provider, "aqua"));
/*  64 */     completions.add(new ColorCompletion(provider, "orange"));
/*     */     
/*  66 */     completions.add(new ColorCompletion(provider, "currentColor"));
/*  67 */     completions.add(new ColorCompletion(provider, "transparent"));
/*  68 */     completions.add(new ColorTemplateCompletion(provider, "#", "#${rgb}${cursor}", "#RGB"));
/*     */     
/*  70 */     completions.add(new ColorTemplateCompletion(provider, "#", "#${rrggbb}${cursor}", "#RRGGBB"));
/*     */     
/*  72 */     completions.add(new ColorTemplateCompletion(provider, "rgb", "rgb(${red}, ${green}, ${blue})${cursor}", "rgb(r, g, b)"));
/*     */     
/*  74 */     completions.add(new ColorTemplateCompletion(provider, "rgba", "rgba(${red}, ${green}, ${blue}, ${alpha})${cursor}", "rgba(r, g, b, a)"));
/*     */     
/*  76 */     completions.add(new ColorTemplateCompletion(provider, "hsl", "hsl(${hue}, ${saturation}, ${brightness})${cursor}", "hsl(h, s, b)"));
/*     */     
/*  78 */     completions.add(new ColorTemplateCompletion(provider, "hsla", "hsla(${hue}, ${saturation}, ${brightness}, ${alpha})${cursor}", "hsla(h, s, b, a)"));
/*     */ 
/*     */     
/*  81 */     return completions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Completion> generate(CompletionProvider provider, String input) {
/*  92 */     List<Completion> completions = new ArrayList<>(this.defaults);
/*     */     
/*  94 */     if (DIGITS.matcher(input).matches()) {
/*  95 */       completions.add(new ColorCompletion(provider, input + "s"));
/*  96 */       completions.add(new ColorCompletion(provider, input + "ms"));
/*     */     } 
/*     */     
/*  99 */     return completions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ColorTemplateCompletion
/*     */     extends TemplateCompletion
/*     */   {
/*     */     public ColorTemplateCompletion(CompletionProvider provider, String input, String template, String desc) {
/* 110 */       super(provider, input, desc, template, desc, null);
/* 111 */       boolean function = (template.indexOf('(') > -1);
/* 112 */       setIcon(IconFactory.get().getIcon(function ? "css_propertyvalue_function" : "css_propertyvalue_identifier"));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ColorCompletion
/*     */     extends BasicCssCompletion
/*     */   {
/*     */     public ColorCompletion(CompletionProvider provider, String value) {
/* 125 */       super(provider, value, "css_propertyvalue_identifier");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\css\ColorCompletionGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */