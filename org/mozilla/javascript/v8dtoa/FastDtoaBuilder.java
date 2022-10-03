/*     */ package org.mozilla.javascript.v8dtoa;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FastDtoaBuilder
/*     */ {
/*  14 */   final char[] chars = new char[25];
/*  15 */   int end = 0;
/*     */   int point;
/*     */   boolean formatted = false;
/*     */   
/*     */   void append(char c) {
/*  20 */     this.chars[this.end++] = c;
/*     */   }
/*     */   
/*     */   void decreaseLast() {
/*  24 */     this.chars[this.end - 1] = (char)(this.chars[this.end - 1] - 1);
/*     */   }
/*     */   
/*     */   public void reset() {
/*  28 */     this.end = 0;
/*  29 */     this.formatted = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  34 */     return "[chars:" + new String(this.chars, 0, this.end) + ", point:" + this.point + "]";
/*     */   }
/*     */   
/*     */   public String format() {
/*  38 */     if (!this.formatted) {
/*     */       
/*  40 */       int firstDigit = (this.chars[0] == '-') ? 1 : 0;
/*  41 */       int decPoint = this.point - firstDigit;
/*  42 */       if (decPoint < -5 || decPoint > 21) {
/*  43 */         toExponentialFormat(firstDigit, decPoint);
/*     */       } else {
/*  45 */         toFixedFormat(firstDigit, decPoint);
/*     */       } 
/*  47 */       this.formatted = true;
/*     */     } 
/*  49 */     return new String(this.chars, 0, this.end);
/*     */   }
/*     */ 
/*     */   
/*     */   private void toFixedFormat(int firstDigit, int decPoint) {
/*  54 */     if (this.point < this.end) {
/*     */       
/*  56 */       if (decPoint > 0) {
/*     */         
/*  58 */         System.arraycopy(this.chars, this.point, this.chars, this.point + 1, this.end - this.point);
/*  59 */         this.chars[this.point] = '.';
/*  60 */         this.end++;
/*     */       } else {
/*     */         
/*  63 */         int target = firstDigit + 2 - decPoint;
/*  64 */         System.arraycopy(this.chars, firstDigit, this.chars, target, this.end - firstDigit);
/*  65 */         this.chars[firstDigit] = '0';
/*  66 */         this.chars[firstDigit + 1] = '.';
/*  67 */         if (decPoint < 0) {
/*  68 */           Arrays.fill(this.chars, firstDigit + 2, target, '0');
/*     */         }
/*  70 */         this.end += 2 - decPoint;
/*     */       } 
/*  72 */     } else if (this.point > this.end) {
/*     */       
/*  74 */       Arrays.fill(this.chars, this.end, this.point, '0');
/*  75 */       this.end += this.point - this.end;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void toExponentialFormat(int firstDigit, int decPoint) {
/*  80 */     if (this.end - firstDigit > 1) {
/*     */       
/*  82 */       int dot = firstDigit + 1;
/*  83 */       System.arraycopy(this.chars, dot, this.chars, dot + 1, this.end - dot);
/*  84 */       this.chars[dot] = '.';
/*  85 */       this.end++;
/*     */     } 
/*  87 */     this.chars[this.end++] = 'e';
/*  88 */     char sign = '+';
/*  89 */     int exp = decPoint - 1;
/*  90 */     if (exp < 0) {
/*  91 */       sign = '-';
/*  92 */       exp = -exp;
/*     */     } 
/*  94 */     this.chars[this.end++] = sign;
/*     */     
/*  96 */     int charPos = (exp > 99) ? (this.end + 2) : ((exp > 9) ? (this.end + 1) : this.end);
/*  97 */     this.end = charPos + 1;
/*     */ 
/*     */     
/*     */     do {
/* 101 */       int r = exp % 10;
/* 102 */       this.chars[charPos--] = digits[r];
/* 103 */       exp /= 10;
/* 104 */     } while (exp != 0);
/*     */   }
/*     */ 
/*     */   
/* 108 */   static final char[] digits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\v8dtoa\FastDtoaBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */