/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Assert
/*     */ {
/*     */   public static void state(boolean expression, String message) {
/*  75 */     if (!expression) {
/*  76 */       throw new IllegalStateException(message);
/*     */     }
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
/*     */   public static void state(boolean expression, Supplier<String> messageSupplier) {
/*  96 */     if (!expression) {
/*  97 */       throw new IllegalStateException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void state(boolean expression) {
/* 108 */     state(expression, "[Assertion failed] - this state invariant must be true");
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
/*     */   public static void isTrue(boolean expression, String message) {
/* 120 */     if (!expression) {
/* 121 */       throw new IllegalArgumentException(message);
/*     */     }
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
/*     */   public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
/* 138 */     if (!expression) {
/* 139 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void isTrue(boolean expression) {
/* 150 */     isTrue(expression, "[Assertion failed] - this expression must be true");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void isNull(@Nullable Object object, String message) {
/* 161 */     if (object != null) {
/* 162 */       throw new IllegalArgumentException(message);
/*     */     }
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
/*     */   public static void isNull(@Nullable Object object, Supplier<String> messageSupplier) {
/* 178 */     if (object != null) {
/* 179 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void isNull(@Nullable Object object) {
/* 189 */     isNull(object, "[Assertion failed] - the object argument must be null");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void notNull(@Nullable Object object, String message) {
/* 200 */     if (object == null) {
/* 201 */       throw new IllegalArgumentException(message);
/*     */     }
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
/*     */   public static void notNull(@Nullable Object object, Supplier<String> messageSupplier) {
/* 218 */     if (object == null) {
/* 219 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void notNull(@Nullable Object object) {
/* 229 */     notNull(object, "[Assertion failed] - this argument is required; it must not be null");
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
/*     */   public static void hasLength(@Nullable String text, String message) {
/* 242 */     if (!StringUtils.hasLength(text)) {
/* 243 */       throw new IllegalArgumentException(message);
/*     */     }
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
/*     */   public static void hasLength(@Nullable String text, Supplier<String> messageSupplier) {
/* 262 */     if (!StringUtils.hasLength(text)) {
/* 263 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void hasLength(@Nullable String text) {
/* 274 */     hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
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
/*     */   public static void hasText(@Nullable String text, String message) {
/* 288 */     if (!StringUtils.hasText(text)) {
/* 289 */       throw new IllegalArgumentException(message);
/*     */     }
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
/*     */   public static void hasText(@Nullable String text, Supplier<String> messageSupplier) {
/* 308 */     if (!StringUtils.hasText(text)) {
/* 309 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void hasText(@Nullable String text) {
/* 320 */     hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
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
/*     */   public static void doesNotContain(@Nullable String textToSearch, String substring, String message) {
/* 333 */     if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch
/* 334 */       .contains(substring)) {
/* 335 */       throw new IllegalArgumentException(message);
/*     */     }
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
/*     */   public static void doesNotContain(@Nullable String textToSearch, String substring, Supplier<String> messageSupplier) {
/* 352 */     if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch
/* 353 */       .contains(substring)) {
/* 354 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void doesNotContain(@Nullable String textToSearch, String substring) {
/* 364 */     doesNotContain(textToSearch, substring, () -> "[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
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
/*     */   public static void notEmpty(@Nullable Object[] array, String message) {
/* 377 */     if (ObjectUtils.isEmpty(array)) {
/* 378 */       throw new IllegalArgumentException(message);
/*     */     }
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
/*     */   public static void notEmpty(@Nullable Object[] array, Supplier<String> messageSupplier) {
/* 395 */     if (ObjectUtils.isEmpty(array)) {
/* 396 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void notEmpty(@Nullable Object[] array) {
/* 407 */     notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
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
/*     */   public static void noNullElements(@Nullable Object[] array, String message) {
/* 419 */     if (array != null) {
/* 420 */       for (Object element : array) {
/* 421 */         if (element == null) {
/* 422 */           throw new IllegalArgumentException(message);
/*     */         }
/*     */       } 
/*     */     }
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
/*     */   public static void noNullElements(@Nullable Object[] array, Supplier<String> messageSupplier) {
/* 441 */     if (array != null) {
/* 442 */       for (Object element : array) {
/* 443 */         if (element == null) {
/* 444 */           throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void noNullElements(@Nullable Object[] array) {
/* 456 */     noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
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
/*     */   public static void notEmpty(@Nullable Collection<?> collection, String message) {
/* 469 */     if (CollectionUtils.isEmpty(collection)) {
/* 470 */       throw new IllegalArgumentException(message);
/*     */     }
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
/*     */   public static void notEmpty(@Nullable Collection<?> collection, Supplier<String> messageSupplier) {
/* 488 */     if (CollectionUtils.isEmpty(collection)) {
/* 489 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void notEmpty(@Nullable Collection<?> collection) {
/* 500 */     notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
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
/*     */   public static void noNullElements(@Nullable Collection<?> collection, String message) {
/* 514 */     if (collection != null) {
/* 515 */       for (Object element : collection) {
/* 516 */         if (element == null) {
/* 517 */           throw new IllegalArgumentException(message);
/*     */         }
/*     */       } 
/*     */     }
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
/*     */   public static void noNullElements(@Nullable Collection<?> collection, Supplier<String> messageSupplier) {
/* 536 */     if (collection != null) {
/* 537 */       for (Object element : collection) {
/* 538 */         if (element == null) {
/* 539 */           throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */         }
/*     */       } 
/*     */     }
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
/*     */   public static void notEmpty(@Nullable Map<?, ?> map, String message) {
/* 554 */     if (CollectionUtils.isEmpty(map)) {
/* 555 */       throw new IllegalArgumentException(message);
/*     */     }
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
/*     */   public static void notEmpty(@Nullable Map<?, ?> map, Supplier<String> messageSupplier) {
/* 572 */     if (CollectionUtils.isEmpty(map)) {
/* 573 */       throw new IllegalArgumentException(nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void notEmpty(@Nullable Map<?, ?> map) {
/* 584 */     notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
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
/*     */   public static void isInstanceOf(Class<?> type, @Nullable Object obj, String message) {
/* 600 */     notNull(type, "Type to check against must not be null");
/* 601 */     if (!type.isInstance(obj)) {
/* 602 */       instanceCheckFailed(type, obj, message);
/*     */     }
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
/*     */   public static void isInstanceOf(Class<?> type, @Nullable Object obj, Supplier<String> messageSupplier) {
/* 619 */     notNull(type, "Type to check against must not be null");
/* 620 */     if (!type.isInstance(obj)) {
/* 621 */       instanceCheckFailed(type, obj, nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void isInstanceOf(Class<?> type, @Nullable Object obj) {
/* 633 */     isInstanceOf(type, obj, "");
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
/*     */   public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, String message) {
/* 649 */     notNull(superType, "Super type to check against must not be null");
/* 650 */     if (subType == null || !superType.isAssignableFrom(subType)) {
/* 651 */       assignableCheckFailed(superType, subType, message);
/*     */     }
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
/*     */   public static void isAssignable(Class<?> superType, @Nullable Class<?> subType, Supplier<String> messageSupplier) {
/* 668 */     notNull(superType, "Super type to check against must not be null");
/* 669 */     if (subType == null || !superType.isAssignableFrom(subType)) {
/* 670 */       assignableCheckFailed(superType, subType, nullSafeGet(messageSupplier));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void isAssignable(Class<?> superType, Class<?> subType) {
/* 682 */     isAssignable(superType, subType, "");
/*     */   }
/*     */ 
/*     */   
/*     */   private static void instanceCheckFailed(Class<?> type, @Nullable Object obj, @Nullable String msg) {
/* 687 */     String className = (obj != null) ? obj.getClass().getName() : "null";
/* 688 */     String result = "";
/* 689 */     boolean defaultMessage = true;
/* 690 */     if (StringUtils.hasLength(msg)) {
/* 691 */       if (endsWithSeparator(msg)) {
/* 692 */         result = msg + " ";
/*     */       } else {
/*     */         
/* 695 */         result = messageWithTypeName(msg, className);
/* 696 */         defaultMessage = false;
/*     */       } 
/*     */     }
/* 699 */     if (defaultMessage) {
/* 700 */       result = result + "Object of class [" + className + "] must be an instance of " + type;
/*     */     }
/* 702 */     throw new IllegalArgumentException(result);
/*     */   }
/*     */   
/*     */   private static void assignableCheckFailed(Class<?> superType, @Nullable Class<?> subType, @Nullable String msg) {
/* 706 */     String result = "";
/* 707 */     boolean defaultMessage = true;
/* 708 */     if (StringUtils.hasLength(msg)) {
/* 709 */       if (endsWithSeparator(msg)) {
/* 710 */         result = msg + " ";
/*     */       } else {
/*     */         
/* 713 */         result = messageWithTypeName(msg, subType);
/* 714 */         defaultMessage = false;
/*     */       } 
/*     */     }
/* 717 */     if (defaultMessage) {
/* 718 */       result = result + subType + " is not assignable to " + superType;
/*     */     }
/* 720 */     throw new IllegalArgumentException(result);
/*     */   }
/*     */   
/*     */   private static boolean endsWithSeparator(String msg) {
/* 724 */     return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith("."));
/*     */   }
/*     */   
/*     */   private static String messageWithTypeName(String msg, @Nullable Object typeName) {
/* 728 */     return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
/* 733 */     return (messageSupplier != null) ? messageSupplier.get() : null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\Assert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */