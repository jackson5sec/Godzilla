/*     */ package org.springframework.core.env;
/*     */ 
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
/*     */ public class SimpleCommandLinePropertySource
/*     */   extends CommandLinePropertySource<CommandLineArgs>
/*     */ {
/*     */   public SimpleCommandLinePropertySource(String... args) {
/*  96 */     super((new SimpleCommandLineArgsParser()).parse(args));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleCommandLinePropertySource(String name, String[] args) {
/* 104 */     super(name, (new SimpleCommandLineArgsParser()).parse(args));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getPropertyNames() {
/* 112 */     return StringUtils.toStringArray(this.source.getOptionNames());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean containsOption(String name) {
/* 117 */     return this.source.containsOption(name);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected List<String> getOptionValues(String name) {
/* 123 */     return this.source.getOptionValues(name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<String> getNonOptionArgs() {
/* 128 */     return this.source.getNonOptionArgs();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\SimpleCommandLinePropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */