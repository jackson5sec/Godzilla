/*     */ package org.springframework.core.log;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class LogMessage
/*     */   implements CharSequence
/*     */ {
/*     */   @Nullable
/*     */   private String result;
/*     */   
/*     */   public int length() {
/*  50 */     return toString().length();
/*     */   }
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/*  55 */     return toString().charAt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/*  60 */     return toString().subSequence(start, end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  69 */     if (this.result == null) {
/*  70 */       this.result = buildString();
/*     */     }
/*  72 */     return this.result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract String buildString();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LogMessage of(Supplier<? extends CharSequence> supplier) {
/*  84 */     return new SupplierMessage(supplier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LogMessage format(String format, Object arg1) {
/*  94 */     return new FormatMessage1(format, arg1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LogMessage format(String format, Object arg1, Object arg2) {
/* 105 */     return new FormatMessage2(format, arg1, arg2);
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
/*     */   public static LogMessage format(String format, Object arg1, Object arg2, Object arg3) {
/* 117 */     return new FormatMessage3(format, arg1, arg2, arg3);
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
/*     */   public static LogMessage format(String format, Object arg1, Object arg2, Object arg3, Object arg4) {
/* 130 */     return new FormatMessage4(format, arg1, arg2, arg3, arg4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LogMessage format(String format, Object... args) {
/* 140 */     return new FormatMessageX(format, args);
/*     */   }
/*     */   
/*     */   private static final class SupplierMessage
/*     */     extends LogMessage
/*     */   {
/*     */     private Supplier<? extends CharSequence> supplier;
/*     */     
/*     */     SupplierMessage(Supplier<? extends CharSequence> supplier) {
/* 149 */       Assert.notNull(supplier, "Supplier must not be null");
/* 150 */       this.supplier = supplier;
/*     */     }
/*     */ 
/*     */     
/*     */     String buildString() {
/* 155 */       return ((CharSequence)this.supplier.get()).toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class FormatMessage
/*     */     extends LogMessage
/*     */   {
/*     */     protected final String format;
/*     */     
/*     */     FormatMessage(String format) {
/* 165 */       Assert.notNull(format, "Format must not be null");
/* 166 */       this.format = format;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class FormatMessage1
/*     */     extends FormatMessage
/*     */   {
/*     */     private final Object arg1;
/*     */     
/*     */     FormatMessage1(String format, Object arg1) {
/* 176 */       super(format);
/* 177 */       this.arg1 = arg1;
/*     */     }
/*     */ 
/*     */     
/*     */     protected String buildString() {
/* 182 */       return String.format(this.format, new Object[] { this.arg1 });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class FormatMessage2
/*     */     extends FormatMessage
/*     */   {
/*     */     private final Object arg1;
/*     */     private final Object arg2;
/*     */     
/*     */     FormatMessage2(String format, Object arg1, Object arg2) {
/* 194 */       super(format);
/* 195 */       this.arg1 = arg1;
/* 196 */       this.arg2 = arg2;
/*     */     }
/*     */ 
/*     */     
/*     */     String buildString() {
/* 201 */       return String.format(this.format, new Object[] { this.arg1, this.arg2 });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class FormatMessage3
/*     */     extends FormatMessage
/*     */   {
/*     */     private final Object arg1;
/*     */     
/*     */     private final Object arg2;
/*     */     private final Object arg3;
/*     */     
/*     */     FormatMessage3(String format, Object arg1, Object arg2, Object arg3) {
/* 215 */       super(format);
/* 216 */       this.arg1 = arg1;
/* 217 */       this.arg2 = arg2;
/* 218 */       this.arg3 = arg3;
/*     */     }
/*     */ 
/*     */     
/*     */     String buildString() {
/* 223 */       return String.format(this.format, new Object[] { this.arg1, this.arg2, this.arg3 });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class FormatMessage4
/*     */     extends FormatMessage
/*     */   {
/*     */     private final Object arg1;
/*     */     
/*     */     private final Object arg2;
/*     */     
/*     */     private final Object arg3;
/*     */     private final Object arg4;
/*     */     
/*     */     FormatMessage4(String format, Object arg1, Object arg2, Object arg3, Object arg4) {
/* 239 */       super(format);
/* 240 */       this.arg1 = arg1;
/* 241 */       this.arg2 = arg2;
/* 242 */       this.arg3 = arg3;
/* 243 */       this.arg4 = arg4;
/*     */     }
/*     */ 
/*     */     
/*     */     String buildString() {
/* 248 */       return String.format(this.format, new Object[] { this.arg1, this.arg2, this.arg3, this.arg4 });
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class FormatMessageX
/*     */     extends FormatMessage
/*     */   {
/*     */     private final Object[] args;
/*     */     
/*     */     FormatMessageX(String format, Object... args) {
/* 258 */       super(format);
/* 259 */       this.args = args;
/*     */     }
/*     */ 
/*     */     
/*     */     String buildString() {
/* 264 */       return String.format(this.format, this.args);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\log\LogMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */