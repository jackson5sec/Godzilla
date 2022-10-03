/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class JOptCommandLinePropertySource
/*     */   extends CommandLinePropertySource<OptionSet>
/*     */ {
/*     */   public JOptCommandLinePropertySource(OptionSet options) {
/*  70 */     super(options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JOptCommandLinePropertySource(String name, OptionSet options) {
/*  78 */     super(name, options);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean containsOption(String name) {
/*  84 */     return this.source.has(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getPropertyNames() {
/*  89 */     List<String> names = new ArrayList<>();
/*  90 */     for (OptionSpec<?> spec : (Iterable<OptionSpec<?>>)this.source.specs()) {
/*  91 */       String lastOption = (String)CollectionUtils.lastElement(spec.options());
/*  92 */       if (lastOption != null)
/*     */       {
/*  94 */         names.add(lastOption);
/*     */       }
/*     */     } 
/*  97 */     return StringUtils.toStringArray(names);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> getOptionValues(String name) {
/* 103 */     List<?> argValues = this.source.valuesOf(name);
/* 104 */     List<String> stringArgValues = new ArrayList<>();
/* 105 */     for (Object argValue : argValues) {
/* 106 */       stringArgValues.add(argValue.toString());
/*     */     }
/* 108 */     if (stringArgValues.isEmpty()) {
/* 109 */       return this.source.has(name) ? Collections.<String>emptyList() : null;
/*     */     }
/* 111 */     return Collections.unmodifiableList(stringArgValues);
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<String> getNonOptionArgs() {
/* 116 */     List<?> argValues = this.source.nonOptionArguments();
/* 117 */     List<String> stringArgValues = new ArrayList<>();
/* 118 */     for (Object argValue : argValues) {
/* 119 */       stringArgValues.add(argValue.toString());
/*     */     }
/* 121 */     return stringArgValues.isEmpty() ? Collections.<String>emptyList() : 
/* 122 */       Collections.<String>unmodifiableList(stringArgValues);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\JOptCommandLinePropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */