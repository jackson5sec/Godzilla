/*     */ package org.hamcrest;
/*     */ import org.hamcrest.core.AllOf;
/*     */ import org.hamcrest.core.AnyOf;
/*     */ import org.hamcrest.core.CombinableMatcher;
/*     */ import org.hamcrest.core.Is;
/*     */ import org.hamcrest.core.IsCollectionContaining;
/*     */ import org.hamcrest.core.IsInstanceOf;
/*     */ import org.hamcrest.core.IsNot;
/*     */ import org.hamcrest.core.IsNull;
/*     */ 
/*     */ public class CoreMatchers {
/*     */   public static <T> Matcher<T> allOf(Iterable<Matcher<? super T>> matchers) {
/*  13 */     return AllOf.allOf(matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Matcher<T> allOf(Matcher<? super T>... matchers) {
/*  23 */     return AllOf.allOf((Matcher[])matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second) {
/*  33 */     return AllOf.allOf(first, second);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third) {
/*  43 */     return AllOf.allOf(first, second, third);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth) {
/*  53 */     return AllOf.allOf(first, second, third, fourth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth) {
/*  63 */     return AllOf.allOf(first, second, third, fourth, fifth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth, Matcher<? super T> sixth) {
/*  73 */     return AllOf.allOf(first, second, third, fourth, fifth, sixth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> AnyOf<T> anyOf(Iterable<Matcher<? super T>> matchers) {
/*  83 */     return AnyOf.anyOf(matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third) {
/*  93 */     return AnyOf.anyOf(first, second, third);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth) {
/* 103 */     return AnyOf.anyOf(first, second, third, fourth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth) {
/* 113 */     return AnyOf.anyOf(first, second, third, fourth, fifth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth, Matcher<? super T> sixth) {
/* 123 */     return AnyOf.anyOf(first, second, third, fourth, fifth, sixth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second) {
/* 133 */     return AnyOf.anyOf(first, second);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> AnyOf<T> anyOf(Matcher<? super T>... matchers) {
/* 143 */     return AnyOf.anyOf((Matcher[])matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <LHS> CombinableMatcher.CombinableBothMatcher<LHS> both(Matcher<? super LHS> matcher) {
/* 153 */     return CombinableMatcher.both(matcher);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <LHS> CombinableMatcher.CombinableEitherMatcher<LHS> either(Matcher<? super LHS> matcher) {
/* 163 */     return CombinableMatcher.either(matcher);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Matcher<T> describedAs(String description, Matcher<T> matcher, Object... values) {
/* 181 */     return DescribedAs.describedAs(description, matcher, values);
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
/*     */   
/*     */   public static <U> Matcher<Iterable<U>> everyItem(Matcher<U> itemMatcher) {
/* 196 */     return Every.everyItem(itemMatcher);
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
/*     */   public static <T> Matcher<T> is(T value) {
/* 208 */     return Is.is(value);
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
/*     */   public static <T> Matcher<T> is(Matcher<T> matcher) {
/* 221 */     return Is.is(matcher);
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
/*     */   public static <T> Matcher<T> is(Class<T> type) {
/* 235 */     return Is.is(type);
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
/*     */   public static <T> Matcher<T> isA(Class<T> type) {
/* 247 */     return Is.isA(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matcher<Object> anything() {
/* 254 */     return IsAnything.anything();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matcher<Object> anything(String description) {
/* 265 */     return IsAnything.anything(description);
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
/*     */ 
/*     */   
/*     */   public static <T> Matcher<Iterable<? super T>> hasItem(T item) {
/* 281 */     return IsCollectionContaining.hasItem(item);
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
/*     */ 
/*     */   
/*     */   public static <T> Matcher<Iterable<? super T>> hasItem(Matcher<? super T> itemMatcher) {
/* 297 */     return IsCollectionContaining.hasItem(itemMatcher);
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
/*     */ 
/*     */   
/*     */   public static <T> Matcher<Iterable<T>> hasItems(T... items) {
/* 313 */     return IsCollectionContaining.hasItems((Object[])items);
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
/*     */ 
/*     */   
/*     */   public static <T> Matcher<Iterable<T>> hasItems(Matcher<? super T>... itemMatchers) {
/* 329 */     return IsCollectionContaining.hasItems((Matcher[])itemMatchers);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Matcher<T> equalTo(T operand) {
/* 355 */     return IsEqual.equalTo(operand);
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
/*     */ 
/*     */   
/*     */   public static <T> Matcher<T> any(Class<T> type) {
/* 371 */     return IsInstanceOf.any(type);
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
/*     */   public static <T> Matcher<T> instanceOf(Class<?> type) {
/* 385 */     return IsInstanceOf.instanceOf(type);
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
/*     */   public static <T> Matcher<T> not(Matcher<T> matcher) {
/* 399 */     return IsNot.not(matcher);
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
/*     */   
/*     */   public static <T> Matcher<T> not(T value) {
/* 414 */     return IsNot.not(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Matcher<Object> nullValue() {
/* 424 */     return IsNull.nullValue();
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
/*     */   public static <T> Matcher<T> nullValue(Class<T> type) {
/* 438 */     return IsNull.nullValue(type);
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
/*     */   public static Matcher<Object> notNullValue() {
/* 450 */     return IsNull.notNullValue();
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
/*     */ 
/*     */   
/*     */   public static <T> Matcher<T> notNullValue(Class<T> type) {
/* 466 */     return IsNull.notNullValue(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Matcher<T> sameInstance(T target) {
/* 477 */     return IsSame.sameInstance(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Matcher<T> theInstance(T target) {
/* 488 */     return IsSame.theInstance(target);
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
/*     */   public static Matcher<String> containsString(String substring) {
/* 502 */     return StringContains.containsString(substring);
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
/*     */   public static Matcher<String> startsWith(String prefix) {
/* 516 */     return StringStartsWith.startsWith(prefix);
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
/*     */   public static Matcher<String> endsWith(String suffix) {
/* 530 */     return StringEndsWith.endsWith(suffix);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\CoreMatchers.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */