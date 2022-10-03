/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Utf8
/*     */ {
/*     */   public static int encodedLength(CharSequence sequence) {
/*  52 */     int utf16Length = sequence.length();
/*  53 */     int utf8Length = utf16Length;
/*  54 */     int i = 0;
/*     */ 
/*     */     
/*  57 */     while (i < utf16Length && sequence.charAt(i) < '') {
/*  58 */       i++;
/*     */     }
/*     */ 
/*     */     
/*  62 */     for (; i < utf16Length; i++) {
/*  63 */       char c = sequence.charAt(i);
/*  64 */       if (c < 'ࠀ') {
/*  65 */         utf8Length += 127 - c >>> 31;
/*     */       } else {
/*  67 */         utf8Length += encodedLengthGeneral(sequence, i);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  72 */     if (utf8Length < utf16Length)
/*     */     {
/*  74 */       throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (utf8Length + 4294967296L));
/*     */     }
/*     */     
/*  77 */     return utf8Length;
/*     */   }
/*     */   
/*     */   private static int encodedLengthGeneral(CharSequence sequence, int start) {
/*  81 */     int utf16Length = sequence.length();
/*  82 */     int utf8Length = 0;
/*  83 */     for (int i = start; i < utf16Length; i++) {
/*  84 */       char c = sequence.charAt(i);
/*  85 */       if (c < 'ࠀ') {
/*  86 */         utf8Length += 127 - c >>> 31;
/*     */       } else {
/*  88 */         utf8Length += 2;
/*     */         
/*  90 */         if ('?' <= c && c <= '?') {
/*     */           
/*  92 */           if (Character.codePointAt(sequence, i) == c) {
/*  93 */             throw new IllegalArgumentException(unpairedSurrogateMsg(i));
/*     */           }
/*  95 */           i++;
/*     */         } 
/*     */       } 
/*     */     } 
/*  99 */     return utf8Length;
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
/*     */   public static boolean isWellFormed(byte[] bytes) {
/* 113 */     return isWellFormed(bytes, 0, bytes.length);
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
/*     */   public static boolean isWellFormed(byte[] bytes, int off, int len) {
/* 126 */     int end = off + len;
/* 127 */     Preconditions.checkPositionIndexes(off, end, bytes.length);
/*     */     
/* 129 */     for (int i = off; i < end; i++) {
/* 130 */       if (bytes[i] < 0) {
/* 131 */         return isWellFormedSlowPath(bytes, i, end);
/*     */       }
/*     */     } 
/* 134 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isWellFormedSlowPath(byte[] bytes, int off, int end) {
/* 138 */     int index = off;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 144 */       if (index >= end)
/* 145 */         return true; 
/*     */       int byte1;
/* 147 */       if ((byte1 = bytes[index++]) < 0) {
/*     */         
/* 149 */         if (byte1 < -32) {
/*     */           
/* 151 */           if (index == end) {
/* 152 */             return false;
/*     */           }
/*     */ 
/*     */           
/* 156 */           if (byte1 < -62 || bytes[index++] > -65)
/* 157 */             return false;  continue;
/*     */         } 
/* 159 */         if (byte1 < -16) {
/*     */           
/* 161 */           if (index + 1 >= end) {
/* 162 */             return false;
/*     */           }
/* 164 */           int i = bytes[index++];
/* 165 */           if (i > -65 || (byte1 == -32 && i < -96) || (byte1 == -19 && -96 <= i) || bytes[index++] > -65)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 172 */             return false;
/*     */           }
/*     */           continue;
/*     */         } 
/* 176 */         if (index + 2 >= end) {
/* 177 */           return false;
/*     */         }
/* 179 */         int byte2 = bytes[index++];
/* 180 */         if (byte2 > -65 || (byte1 << 28) + byte2 - -112 >> 30 != 0 || bytes[index++] > -65 || bytes[index++] > -65) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String unpairedSurrogateMsg(int i) {
/* 197 */     return "Unpaired surrogate at index " + i;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Utf8.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */