/*    */ package org.hamcrest.core;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.hamcrest.BaseMatcher;
/*    */ import org.hamcrest.Description;
/*    */ import org.hamcrest.Factory;
/*    */ import org.hamcrest.Matcher;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DescribedAs<T>
/*    */   extends BaseMatcher<T>
/*    */ {
/*    */   private final String descriptionTemplate;
/*    */   private final Matcher<T> matcher;
/*    */   private final Object[] values;
/* 22 */   private static final Pattern ARG_PATTERN = Pattern.compile("%([0-9]+)");
/*    */   
/*    */   public DescribedAs(String descriptionTemplate, Matcher<T> matcher, Object[] values) {
/* 25 */     this.descriptionTemplate = descriptionTemplate;
/* 26 */     this.matcher = matcher;
/* 27 */     this.values = (Object[])values.clone();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(Object o) {
/* 32 */     return this.matcher.matches(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public void describeTo(Description description) {
/* 37 */     Matcher arg = ARG_PATTERN.matcher(this.descriptionTemplate);
/*    */     
/* 39 */     int textStart = 0;
/* 40 */     while (arg.find()) {
/* 41 */       description.appendText(this.descriptionTemplate.substring(textStart, arg.start()));
/* 42 */       description.appendValue(this.values[Integer.parseInt(arg.group(1))]);
/* 43 */       textStart = arg.end();
/*    */     } 
/*    */     
/* 46 */     if (textStart < this.descriptionTemplate.length()) {
/* 47 */       description.appendText(this.descriptionTemplate.substring(textStart));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void describeMismatch(Object item, Description description) {
/* 53 */     this.matcher.describeMismatch(item, description);
/*    */   }
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
/*    */   @Factory
/*    */   public static <T> Matcher<T> describedAs(String description, Matcher<T> matcher, Object... values) {
/* 72 */     return (Matcher<T>)new DescribedAs<T>(description, matcher, values);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\core\DescribedAs.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */