/*    */ package org.springframework.core.convert.converter;
/*    */ 
/*    */ import org.springframework.lang.Nullable;
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
/*    */ @FunctionalInterface
/*    */ public interface Converter<S, T>
/*    */ {
/*    */   @Nullable
/*    */   T convert(S paramS);
/*    */   
/*    */   default <U> Converter<S, U> andThen(Converter<? super T, ? extends U> after) {
/* 60 */     Assert.notNull(after, "After Converter must not be null");
/* 61 */     return s -> {
/*    */         T initialResult = convert((S)s);
/*    */         return (initialResult != null) ? after.convert(initialResult) : null;
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\converter\Converter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */