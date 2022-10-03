/*     */ package org.hamcrest.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.hamcrest.Description;
/*     */ import org.hamcrest.DiagnosingMatcher;
/*     */ import org.hamcrest.Factory;
/*     */ import org.hamcrest.Matcher;
/*     */ import org.hamcrest.SelfDescribing;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AllOf<T>
/*     */   extends DiagnosingMatcher<T>
/*     */ {
/*     */   private final Iterable<Matcher<? super T>> matchers;
/*     */   
/*     */   public AllOf(Iterable<Matcher<? super T>> matchers) {
/*  21 */     this.matchers = matchers;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(Object o, Description mismatch) {
/*  26 */     for (Matcher<? super T> matcher : this.matchers) {
/*  27 */       if (!matcher.matches(o)) {
/*  28 */         mismatch.appendDescriptionOf((SelfDescribing)matcher).appendText(" ");
/*  29 */         matcher.describeMismatch(o, mismatch);
/*  30 */         return false;
/*     */       } 
/*     */     } 
/*  33 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void describeTo(Description description) {
/*  38 */     description.appendList("(", " and ", ")", this.matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Factory
/*     */   public static <T> Matcher<T> allOf(Iterable<Matcher<? super T>> matchers) {
/*  49 */     return (Matcher<T>)new AllOf<T>(matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Factory
/*     */   public static <T> Matcher<T> allOf(Matcher<? super T>... matchers) {
/*  60 */     return allOf(Arrays.asList(matchers));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Factory
/*     */   public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second) {
/*  71 */     List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>(2);
/*  72 */     matchers.add(first);
/*  73 */     matchers.add(second);
/*  74 */     return allOf(matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Factory
/*     */   public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third) {
/*  85 */     List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>(3);
/*  86 */     matchers.add(first);
/*  87 */     matchers.add(second);
/*  88 */     matchers.add(third);
/*  89 */     return allOf(matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Factory
/*     */   public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth) {
/* 100 */     List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>(4);
/* 101 */     matchers.add(first);
/* 102 */     matchers.add(second);
/* 103 */     matchers.add(third);
/* 104 */     matchers.add(fourth);
/* 105 */     return allOf(matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Factory
/*     */   public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth) {
/* 116 */     List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>(5);
/* 117 */     matchers.add(first);
/* 118 */     matchers.add(second);
/* 119 */     matchers.add(third);
/* 120 */     matchers.add(fourth);
/* 121 */     matchers.add(fifth);
/* 122 */     return allOf(matchers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Factory
/*     */   public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth, Matcher<? super T> sixth) {
/* 133 */     List<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>(6);
/* 134 */     matchers.add(first);
/* 135 */     matchers.add(second);
/* 136 */     matchers.add(third);
/* 137 */     matchers.add(fourth);
/* 138 */     matchers.add(fifth);
/* 139 */     matchers.add(sixth);
/* 140 */     return allOf(matchers);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\core\AllOf.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */