/*    */ package org.springframework.core.env;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ 
/*    */ class CommandLineArgs
/*    */ {
/* 38 */   private final Map<String, List<String>> optionArgs = new HashMap<>();
/* 39 */   private final List<String> nonOptionArgs = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addOptionArg(String optionName, @Nullable String optionValue) {
/* 48 */     if (!this.optionArgs.containsKey(optionName)) {
/* 49 */       this.optionArgs.put(optionName, new ArrayList<>());
/*    */     }
/* 51 */     if (optionValue != null) {
/* 52 */       ((List<String>)this.optionArgs.get(optionName)).add(optionValue);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<String> getOptionNames() {
/* 60 */     return Collections.unmodifiableSet(this.optionArgs.keySet());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean containsOption(String optionName) {
/* 67 */     return this.optionArgs.containsKey(optionName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public List<String> getOptionValues(String optionName) {
/* 77 */     return this.optionArgs.get(optionName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addNonOptionArg(String value) {
/* 84 */     this.nonOptionArgs.add(value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> getNonOptionArgs() {
/* 91 */     return Collections.unmodifiableList(this.nonOptionArgs);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\CommandLineArgs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */