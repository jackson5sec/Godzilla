/*     */ package org.hamcrest.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.hamcrest.Description;
/*     */ import org.hamcrest.Factory;
/*     */ import org.hamcrest.Matcher;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnyOf<T>
/*     */   extends ShortcutCombination<T>
/*     */ {
/*     */   public AnyOf(Iterable<Matcher<? super T>> matchers) {
/*  18 */     super(matchers);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(Object o) {
/*  23 */     return matches(o, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void describeTo(Description description) {
/*  28 */     describeTo(description, "or");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Factory
/*     */   public static <T> AnyOf<T> anyOf(Iterable<Matcher<? super T>> matchers) {
/*  39 */     return new AnyOf<T>(matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Factory
/*     */   public static <T> AnyOf<T> anyOf(Matcher<? super T>... matchers) {
/*  50 */     return anyOf(Arrays.asList(matchers));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Factory
/*     */   public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second) {
/*  61 */     List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();
/*  62 */     matchers.add(first);
/*  63 */     matchers.add(second);
/*  64 */     return anyOf(matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Factory
/*     */   public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third) {
/*  75 */     List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();
/*  76 */     matchers.add(first);
/*  77 */     matchers.add(second);
/*  78 */     matchers.add(third);
/*  79 */     return anyOf(matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Factory
/*     */   public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth) {
/*  90 */     List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();
/*  91 */     matchers.add(first);
/*  92 */     matchers.add(second);
/*  93 */     matchers.add(third);
/*  94 */     matchers.add(fourth);
/*  95 */     return anyOf(matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Factory
/*     */   public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth) {
/* 106 */     List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();
/* 107 */     matchers.add(first);
/* 108 */     matchers.add(second);
/* 109 */     matchers.add(third);
/* 110 */     matchers.add(fourth);
/* 111 */     matchers.add(fifth);
/* 112 */     return anyOf(matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Factory
/*     */   public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth, Matcher<? super T> sixth) {
/* 123 */     List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();
/* 124 */     matchers.add(first);
/* 125 */     matchers.add(second);
/* 126 */     matchers.add(third);
/* 127 */     matchers.add(fourth);
/* 128 */     matchers.add(fifth);
/* 129 */     matchers.add(sixth);
/* 130 */     return anyOf(matchers);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\core\AnyOf.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */