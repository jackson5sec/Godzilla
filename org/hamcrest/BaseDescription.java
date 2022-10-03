/*     */ package org.hamcrest;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import org.hamcrest.internal.ArrayIterator;
/*     */ import org.hamcrest.internal.SelfDescribingValueIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseDescription
/*     */   implements Description
/*     */ {
/*     */   public Description appendText(String text) {
/*  18 */     append(text);
/*  19 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Description appendDescriptionOf(SelfDescribing value) {
/*  24 */     value.describeTo(this);
/*  25 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Description appendValue(Object value) {
/*  30 */     if (value == null) {
/*  31 */       append("null");
/*  32 */     } else if (value instanceof String) {
/*  33 */       toJavaSyntax((String)value);
/*  34 */     } else if (value instanceof Character) {
/*  35 */       append('"');
/*  36 */       toJavaSyntax(((Character)value).charValue());
/*  37 */       append('"');
/*  38 */     } else if (value instanceof Short) {
/*  39 */       append('<');
/*  40 */       append(descriptionOf(value));
/*  41 */       append("s>");
/*  42 */     } else if (value instanceof Long) {
/*  43 */       append('<');
/*  44 */       append(descriptionOf(value));
/*  45 */       append("L>");
/*  46 */     } else if (value instanceof Float) {
/*  47 */       append('<');
/*  48 */       append(descriptionOf(value));
/*  49 */       append("F>");
/*  50 */     } else if (value.getClass().isArray()) {
/*  51 */       appendValueList("[", ", ", "]", (Iterator<?>)new ArrayIterator(value));
/*     */     } else {
/*  53 */       append('<');
/*  54 */       append(descriptionOf(value));
/*  55 */       append('>');
/*     */     } 
/*  57 */     return this;
/*     */   }
/*     */   
/*     */   private String descriptionOf(Object value) {
/*     */     try {
/*  62 */       return String.valueOf(value);
/*     */     }
/*  64 */     catch (Exception e) {
/*  65 */       return value.getClass().getName() + "@" + Integer.toHexString(value.hashCode());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Description appendValueList(String start, String separator, String end, T... values) {
/*  71 */     return appendValueList(start, separator, end, Arrays.asList((Object[])values));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Description appendValueList(String start, String separator, String end, Iterable<T> values) {
/*  76 */     return appendValueList(start, separator, end, values.iterator());
/*     */   }
/*     */   
/*     */   private <T> Description appendValueList(String start, String separator, String end, Iterator<T> values) {
/*  80 */     return appendList(start, separator, end, (Iterator<? extends SelfDescribing>)new SelfDescribingValueIterator(values));
/*     */   }
/*     */ 
/*     */   
/*     */   public Description appendList(String start, String separator, String end, Iterable<? extends SelfDescribing> values) {
/*  85 */     return appendList(start, separator, end, values.iterator());
/*     */   }
/*     */   
/*     */   private Description appendList(String start, String separator, String end, Iterator<? extends SelfDescribing> i) {
/*  89 */     boolean separate = false;
/*     */     
/*  91 */     append(start);
/*  92 */     while (i.hasNext()) {
/*  93 */       if (separate) append(separator); 
/*  94 */       appendDescriptionOf(i.next());
/*  95 */       separate = true;
/*     */     } 
/*  97 */     append(end);
/*     */     
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void append(String str) {
/* 108 */     for (int i = 0; i < str.length(); i++) {
/* 109 */       append(str.charAt(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void append(char paramChar);
/*     */ 
/*     */   
/*     */   private void toJavaSyntax(String unformatted) {
/* 119 */     append('"');
/* 120 */     for (int i = 0; i < unformatted.length(); i++) {
/* 121 */       toJavaSyntax(unformatted.charAt(i));
/*     */     }
/* 123 */     append('"');
/*     */   }
/*     */   
/*     */   private void toJavaSyntax(char ch) {
/* 127 */     switch (ch) {
/*     */       case '"':
/* 129 */         append("\\\"");
/*     */         return;
/*     */       case '\n':
/* 132 */         append("\\n");
/*     */         return;
/*     */       case '\r':
/* 135 */         append("\\r");
/*     */         return;
/*     */       case '\t':
/* 138 */         append("\\t");
/*     */         return;
/*     */     } 
/* 141 */     append(ch);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\hamcrest\BaseDescription.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */