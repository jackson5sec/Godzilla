/*    */ package org.springframework.core.type.filter;
/*    */ 
/*    */ import java.util.regex.Pattern;
/*    */ import org.springframework.core.type.ClassMetadata;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class RegexPatternTypeFilter
/*    */   extends AbstractClassTestingTypeFilter
/*    */ {
/*    */   private final Pattern pattern;
/*    */   
/*    */   public RegexPatternTypeFilter(Pattern pattern) {
/* 37 */     Assert.notNull(pattern, "Pattern must not be null");
/* 38 */     this.pattern = pattern;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean match(ClassMetadata metadata) {
/* 44 */     return this.pattern.matcher(metadata.getClassName()).matches();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\filter\RegexPatternTypeFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */