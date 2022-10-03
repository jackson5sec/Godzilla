/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public abstract class CommandLinePropertySource<T>
/*     */   extends EnumerablePropertySource<T>
/*     */ {
/*     */   public static final String COMMAND_LINE_PROPERTY_SOURCE_NAME = "commandLineArgs";
/*     */   public static final String DEFAULT_NON_OPTION_ARGS_PROPERTY_NAME = "nonOptionArgs";
/* 217 */   private String nonOptionArgsPropertyName = "nonOptionArgs";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandLinePropertySource(T source) {
/* 225 */     super("commandLineArgs", source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandLinePropertySource(String name, T source) {
/* 233 */     super(name, source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNonOptionArgsPropertyName(String nonOptionArgsPropertyName) {
/* 242 */     this.nonOptionArgsPropertyName = nonOptionArgsPropertyName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean containsProperty(String name) {
/* 254 */     if (this.nonOptionArgsPropertyName.equals(name)) {
/* 255 */       return !getNonOptionArgs().isEmpty();
/*     */     }
/* 257 */     return containsOption(name);
/*     */   }
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
/*     */   @Nullable
/*     */   public final String getProperty(String name) {
/* 272 */     if (this.nonOptionArgsPropertyName.equals(name)) {
/* 273 */       Collection<String> nonOptionArguments = getNonOptionArgs();
/* 274 */       if (nonOptionArguments.isEmpty()) {
/* 275 */         return null;
/*     */       }
/*     */       
/* 278 */       return StringUtils.collectionToCommaDelimitedString(nonOptionArguments);
/*     */     } 
/*     */     
/* 281 */     Collection<String> optionValues = getOptionValues(name);
/* 282 */     if (optionValues == null) {
/* 283 */       return null;
/*     */     }
/*     */     
/* 286 */     return StringUtils.collectionToCommaDelimitedString(optionValues);
/*     */   }
/*     */   
/*     */   protected abstract boolean containsOption(String paramString);
/*     */   
/*     */   @Nullable
/*     */   protected abstract List<String> getOptionValues(String paramString);
/*     */   
/*     */   protected abstract List<String> getNonOptionArgs();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\CommandLinePropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */