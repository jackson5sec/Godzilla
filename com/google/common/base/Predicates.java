/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Predicates
/*     */ {
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> alwaysTrue() {
/*  51 */     return ObjectPredicate.ALWAYS_TRUE.withNarrowedType();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> alwaysFalse() {
/*  57 */     return ObjectPredicate.ALWAYS_FALSE.withNarrowedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> isNull() {
/*  66 */     return ObjectPredicate.IS_NULL.withNarrowedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> notNull() {
/*  75 */     return ObjectPredicate.NOT_NULL.withNarrowedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> not(Predicate<T> predicate) {
/*  83 */     return new NotPredicate<>(predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> components) {
/*  94 */     return new AndPredicate<>(defensiveCopy(components));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public static <T> Predicate<T> and(Predicate<? super T>... components) {
/* 106 */     return new AndPredicate<>(defensiveCopy(components));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> and(Predicate<? super T> first, Predicate<? super T> second) {
/* 115 */     return new AndPredicate<>(asList(Preconditions.<Predicate>checkNotNull(first), Preconditions.<Predicate>checkNotNull(second)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> or(Iterable<? extends Predicate<? super T>> components) {
/* 126 */     return new OrPredicate<>(defensiveCopy(components));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public static <T> Predicate<T> or(Predicate<? super T>... components) {
/* 138 */     return new OrPredicate<>(defensiveCopy(components));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> or(Predicate<? super T> first, Predicate<? super T> second) {
/* 147 */     return new OrPredicate<>(asList(Preconditions.<Predicate>checkNotNull(first), Preconditions.<Predicate>checkNotNull(second)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> equalTo(T target) {
/* 155 */     return (target == null) ? isNull() : new IsEqualToPredicate<>(target);
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
/*     */   @GwtIncompatible
/*     */   public static Predicate<Object> instanceOf(Class<?> clazz) {
/* 173 */     return new InstanceOfPredicate(clazz);
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
/*     */   @GwtIncompatible
/*     */   @Beta
/*     */   public static Predicate<Class<?>> subtypeOf(Class<?> clazz) {
/* 193 */     return new SubtypeOfPredicate(clazz);
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
/*     */   public static <T> Predicate<T> in(Collection<? extends T> target) {
/* 208 */     return new InPredicate<>(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A, B> Predicate<A> compose(Predicate<B> predicate, Function<A, ? extends B> function) {
/* 219 */     return new CompositionPredicate<>(predicate, function);
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
/*     */   @GwtIncompatible
/*     */   public static Predicate<CharSequence> containsPattern(String pattern) {
/* 232 */     return new ContainsPatternFromStringPredicate(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.util.regex.Pattern")
/*     */   public static Predicate<CharSequence> contains(Pattern pattern) {
/* 244 */     return new ContainsPatternPredicate(new JdkPattern(pattern));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   enum ObjectPredicate
/*     */     implements Predicate<Object>
/*     */   {
/* 252 */     ALWAYS_TRUE
/*     */     {
/*     */       public boolean apply(Object o) {
/* 255 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 260 */         return "Predicates.alwaysTrue()";
/*     */       }
/*     */     },
/*     */     
/* 264 */     ALWAYS_FALSE
/*     */     {
/*     */       public boolean apply(Object o) {
/* 267 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 272 */         return "Predicates.alwaysFalse()";
/*     */       }
/*     */     },
/*     */     
/* 276 */     IS_NULL
/*     */     {
/*     */       public boolean apply(Object o) {
/* 279 */         return (o == null);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 284 */         return "Predicates.isNull()";
/*     */       }
/*     */     },
/*     */     
/* 288 */     NOT_NULL
/*     */     {
/*     */       public boolean apply(Object o) {
/* 291 */         return (o != null);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 296 */         return "Predicates.notNull()";
/*     */       }
/*     */     };
/*     */ 
/*     */     
/*     */     <T> Predicate<T> withNarrowedType() {
/* 302 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class NotPredicate<T> implements Predicate<T>, Serializable {
/*     */     final Predicate<T> predicate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     NotPredicate(Predicate<T> predicate) {
/* 311 */       this.predicate = Preconditions.<Predicate<T>>checkNotNull(predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(T t) {
/* 316 */       return !this.predicate.apply(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 321 */       return this.predicate.hashCode() ^ 0xFFFFFFFF;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 326 */       if (obj instanceof NotPredicate) {
/* 327 */         NotPredicate<?> that = (NotPredicate)obj;
/* 328 */         return this.predicate.equals(that.predicate);
/*     */       } 
/* 330 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 335 */       return "Predicates.not(" + this.predicate + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class AndPredicate<T>
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final List<? extends Predicate<? super T>> components;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private AndPredicate(List<? extends Predicate<? super T>> components) {
/* 346 */       this.components = components;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean apply(T t) {
/* 352 */       for (int i = 0; i < this.components.size(); i++) {
/* 353 */         if (!((Predicate<T>)this.components.get(i)).apply(t)) {
/* 354 */           return false;
/*     */         }
/*     */       } 
/* 357 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 363 */       return this.components.hashCode() + 306654252;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 368 */       if (obj instanceof AndPredicate) {
/* 369 */         AndPredicate<?> that = (AndPredicate)obj;
/* 370 */         return this.components.equals(that.components);
/*     */       } 
/* 372 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 377 */       return Predicates.toStringHelper("and", this.components);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class OrPredicate<T>
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final List<? extends Predicate<? super T>> components;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private OrPredicate(List<? extends Predicate<? super T>> components) {
/* 388 */       this.components = components;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean apply(T t) {
/* 394 */       for (int i = 0; i < this.components.size(); i++) {
/* 395 */         if (((Predicate<T>)this.components.get(i)).apply(t)) {
/* 396 */           return true;
/*     */         }
/*     */       } 
/* 399 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 405 */       return this.components.hashCode() + 87855567;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 410 */       if (obj instanceof OrPredicate) {
/* 411 */         OrPredicate<?> that = (OrPredicate)obj;
/* 412 */         return this.components.equals(that.components);
/*     */       } 
/* 414 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 419 */       return Predicates.toStringHelper("or", this.components);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String toStringHelper(String methodName, Iterable<?> components) {
/* 426 */     StringBuilder builder = (new StringBuilder("Predicates.")).append(methodName).append('(');
/* 427 */     boolean first = true;
/* 428 */     for (Object o : components) {
/* 429 */       if (!first) {
/* 430 */         builder.append(',');
/*     */       }
/* 432 */       builder.append(o);
/* 433 */       first = false;
/*     */     } 
/* 435 */     return builder.append(')').toString();
/*     */   }
/*     */   
/*     */   private static class IsEqualToPredicate<T> implements Predicate<T>, Serializable {
/*     */     private final T target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private IsEqualToPredicate(T target) {
/* 443 */       this.target = target;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(T t) {
/* 448 */       return this.target.equals(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 453 */       return this.target.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 458 */       if (obj instanceof IsEqualToPredicate) {
/* 459 */         IsEqualToPredicate<?> that = (IsEqualToPredicate)obj;
/* 460 */         return this.target.equals(that.target);
/*     */       } 
/* 462 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 467 */       return "Predicates.equalTo(" + this.target + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class InstanceOfPredicate
/*     */     implements Predicate<Object>, Serializable
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private InstanceOfPredicate(Class<?> clazz) {
/* 479 */       this.clazz = Preconditions.<Class<?>>checkNotNull(clazz);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(Object o) {
/* 484 */       return this.clazz.isInstance(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 489 */       return this.clazz.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 494 */       if (obj instanceof InstanceOfPredicate) {
/* 495 */         InstanceOfPredicate that = (InstanceOfPredicate)obj;
/* 496 */         return (this.clazz == that.clazz);
/*     */       } 
/* 498 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 503 */       return "Predicates.instanceOf(" + this.clazz.getName() + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class SubtypeOfPredicate
/*     */     implements Predicate<Class<?>>, Serializable
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private SubtypeOfPredicate(Class<?> clazz) {
/* 515 */       this.clazz = Preconditions.<Class<?>>checkNotNull(clazz);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(Class<?> input) {
/* 520 */       return this.clazz.isAssignableFrom(input);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 525 */       return this.clazz.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 530 */       if (obj instanceof SubtypeOfPredicate) {
/* 531 */         SubtypeOfPredicate that = (SubtypeOfPredicate)obj;
/* 532 */         return (this.clazz == that.clazz);
/*     */       } 
/* 534 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 539 */       return "Predicates.subtypeOf(" + this.clazz.getName() + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class InPredicate<T>
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final Collection<?> target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private InPredicate(Collection<?> target) {
/* 550 */       this.target = Preconditions.<Collection>checkNotNull(target);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(T t) {
/*     */       try {
/* 556 */         return this.target.contains(t);
/* 557 */       } catch (NullPointerException|ClassCastException e) {
/* 558 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 564 */       if (obj instanceof InPredicate) {
/* 565 */         InPredicate<?> that = (InPredicate)obj;
/* 566 */         return this.target.equals(that.target);
/*     */       } 
/* 568 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 573 */       return this.target.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 578 */       return "Predicates.in(" + this.target + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CompositionPredicate<A, B>
/*     */     implements Predicate<A>, Serializable
/*     */   {
/*     */     final Predicate<B> p;
/*     */     final Function<A, ? extends B> f;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private CompositionPredicate(Predicate<B> p, Function<A, ? extends B> f) {
/* 590 */       this.p = Preconditions.<Predicate<B>>checkNotNull(p);
/* 591 */       this.f = Preconditions.<Function<A, ? extends B>>checkNotNull(f);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(A a) {
/* 596 */       return this.p.apply(this.f.apply(a));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 601 */       if (obj instanceof CompositionPredicate) {
/* 602 */         CompositionPredicate<?, ?> that = (CompositionPredicate<?, ?>)obj;
/* 603 */         return (this.f.equals(that.f) && this.p.equals(that.p));
/*     */       } 
/* 605 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 610 */       return this.f.hashCode() ^ this.p.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 616 */       return this.p + "(" + this.f + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class ContainsPatternPredicate
/*     */     implements Predicate<CharSequence>, Serializable
/*     */   {
/*     */     final CommonPattern pattern;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ContainsPatternPredicate(CommonPattern pattern) {
/* 628 */       this.pattern = Preconditions.<CommonPattern>checkNotNull(pattern);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(CharSequence t) {
/* 633 */       return this.pattern.matcher(t).find();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 641 */       return Objects.hashCode(new Object[] { this.pattern.pattern(), Integer.valueOf(this.pattern.flags()) });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 646 */       if (obj instanceof ContainsPatternPredicate) {
/* 647 */         ContainsPatternPredicate that = (ContainsPatternPredicate)obj;
/*     */ 
/*     */ 
/*     */         
/* 651 */         return (Objects.equal(this.pattern.pattern(), that.pattern.pattern()) && this.pattern
/* 652 */           .flags() == that.pattern.flags());
/*     */       } 
/* 654 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 663 */       String patternString = MoreObjects.toStringHelper(this.pattern).add("pattern", this.pattern.pattern()).add("pattern.flags", this.pattern.flags()).toString();
/* 664 */       return "Predicates.contains(" + patternString + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class ContainsPatternFromStringPredicate
/*     */     extends ContainsPatternPredicate
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ContainsPatternFromStringPredicate(String string) {
/* 675 */       super(Platform.compilePattern(string));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 680 */       return "Predicates.containsPattern(" + this.pattern.pattern() + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> List<Predicate<? super T>> asList(Predicate<? super T> first, Predicate<? super T> second) {
/* 689 */     return Arrays.asList((Predicate<? super T>[])new Predicate[] { first, second });
/*     */   }
/*     */   
/*     */   private static <T> List<T> defensiveCopy(T... array) {
/* 693 */     return defensiveCopy(Arrays.asList(array));
/*     */   }
/*     */   
/*     */   static <T> List<T> defensiveCopy(Iterable<T> iterable) {
/* 697 */     ArrayList<T> list = new ArrayList<>();
/* 698 */     for (T element : iterable) {
/* 699 */       list.add(Preconditions.checkNotNull(element));
/*     */     }
/* 701 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Predicates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */