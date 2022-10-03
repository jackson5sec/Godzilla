/*     */ package com.jgoodies.common.swing;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import com.jgoodies.common.base.Strings;
/*     */ import java.text.CharacterIterator;
/*     */ import java.text.StringCharacterIterator;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.JLabel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MnemonicUtils
/*     */ {
/*     */   static final char MNEMONIC_MARKER = '&';
/*     */   
/*     */   public static void configure(AbstractButton target, String markedText) {
/* 176 */     Preconditions.checkNotNull(target, "The %1$s must not be null.", new Object[] { "target" });
/* 177 */     configure0(target, new MnemonicText(markedText, '&'));
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
/*     */   public static void configure(Action target, String markedText) {
/* 193 */     Preconditions.checkNotNull(target, "The %1$s must not be null.", new Object[] { "target" });
/* 194 */     configure0(target, new MnemonicText(markedText, '&'));
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
/*     */   public static void configure(JLabel target, String markedText) {
/* 210 */     Preconditions.checkNotNull(target, "The %1$s must not be null.", new Object[] { "target" });
/* 211 */     configure0(target, new MnemonicText(markedText, '&'));
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
/*     */   
/*     */   public static String plainText(String markedText) {
/* 238 */     return (new MnemonicText(markedText, '&')).text;
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
/*     */   static int mnemonic(String markedText) {
/* 251 */     return (new MnemonicText(markedText, '&')).key;
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
/*     */   static int mnemonicIndex(String markedText) {
/* 263 */     return (new MnemonicText(markedText, '&')).index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void configure0(AbstractButton button, MnemonicText mnemonicText) {
/* 270 */     button.setText(mnemonicText.text);
/* 271 */     button.setMnemonic(mnemonicText.key);
/* 272 */     button.setDisplayedMnemonicIndex(mnemonicText.index);
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
/*     */   private static void configure0(Action action, MnemonicText mnemonicText) {
/* 285 */     Integer keyValue = Integer.valueOf(mnemonicText.key);
/* 286 */     Integer indexValue = (mnemonicText.index == -1) ? null : Integer.valueOf(mnemonicText.index);
/*     */ 
/*     */     
/* 289 */     action.putValue("Name", mnemonicText.text);
/* 290 */     action.putValue("MnemonicKey", keyValue);
/* 291 */     action.putValue("SwingDisplayedMnemonicIndexKey", indexValue);
/*     */   }
/*     */   
/*     */   private static void configure0(JLabel label, MnemonicText mnemonicText) {
/* 295 */     label.setText(mnemonicText.text);
/* 296 */     label.setDisplayedMnemonic(mnemonicText.key);
/* 297 */     label.setDisplayedMnemonicIndex(mnemonicText.index);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class MnemonicText
/*     */   {
/*     */     String text;
/*     */     
/*     */     int key;
/*     */     
/*     */     int index;
/*     */     
/*     */     private MnemonicText(String markedText, char marker) {
/*     */       int i;
/* 311 */       if (markedText == null || markedText.length() <= 1 || (i = markedText.indexOf(marker)) == -1) {
/*     */ 
/*     */         
/* 314 */         this.text = markedText;
/* 315 */         this.key = 0;
/* 316 */         this.index = -1;
/*     */         return;
/*     */       } 
/* 319 */       boolean html = Strings.startsWithIgnoreCase(markedText, "<html>");
/* 320 */       StringBuilder builder = new StringBuilder();
/* 321 */       int begin = 0;
/* 322 */       int quotedMarkers = 0;
/* 323 */       int markerIndex = -1;
/* 324 */       boolean marked = false;
/* 325 */       char markedChar = Character.MIN_VALUE;
/* 326 */       CharacterIterator sci = new StringCharacterIterator(markedText);
/*     */       do {
/* 328 */         builder.append(markedText.substring(begin, i));
/* 329 */         char current = sci.setIndex(i);
/* 330 */         char next = sci.next();
/* 331 */         if (html) {
/* 332 */           int entityEnd = indexOfEntityEnd(markedText, i);
/* 333 */           if (entityEnd == -1) {
/* 334 */             marked = true;
/* 335 */             builder.append("<u>").append(next).append("</u>");
/* 336 */             begin = i + 2;
/* 337 */             markedChar = next;
/*     */           } else {
/* 339 */             builder.append(markedText.substring(i, entityEnd));
/* 340 */             begin = entityEnd;
/*     */           }
/*     */         
/* 343 */         } else if (next == marker) {
/* 344 */           builder.append(next);
/* 345 */           begin = i + 2;
/* 346 */           quotedMarkers++;
/* 347 */         } else if (Character.isWhitespace(next)) {
/* 348 */           builder.append(current).append(next);
/* 349 */           begin = i + 2;
/* 350 */         } else if (next == Character.MAX_VALUE) {
/* 351 */           builder.append(current);
/* 352 */           begin = i + 2;
/*     */         } else {
/* 354 */           builder.append(next);
/* 355 */           begin = i + 2;
/* 356 */           markerIndex = i - quotedMarkers;
/* 357 */           marked = true;
/* 358 */           markedChar = next;
/*     */         } 
/*     */         
/* 361 */         i = markedText.indexOf(marker, begin);
/* 362 */       } while (i != -1 && !marked);
/* 363 */       if (begin < markedText.length()) {
/* 364 */         builder.append(markedText.substring(begin));
/*     */       }
/* 366 */       this.text = builder.toString();
/* 367 */       this.index = markerIndex;
/* 368 */       if (marked) {
/* 369 */         this.key = mnemonicKey(markedChar);
/*     */       } else {
/* 371 */         this.key = 0;
/*     */       } 
/*     */     }
/*     */     
/*     */     private static int indexOfEntityEnd(String htmlText, int start) {
/* 376 */       CharacterIterator sci = new StringCharacterIterator(htmlText, start);
/*     */       
/*     */       while (true) {
/* 379 */         char c = sci.next();
/* 380 */         if (c == ';') {
/* 381 */           return sci.getIndex();
/*     */         }
/* 383 */         if (!Character.isLetterOrDigit(c)) {
/* 384 */           return -1;
/*     */         }
/* 386 */         if (c == Character.MAX_VALUE) {
/* 387 */           return -1;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static int mnemonicKey(char c) {
/* 396 */       int vk = c;
/* 397 */       if (vk >= 97 && vk <= 122) {
/* 398 */         vk -= 32;
/*     */       }
/* 400 */       return vk;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\common\swing\MnemonicUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */