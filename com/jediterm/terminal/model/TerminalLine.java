/*     */ package com.jediterm.terminal.model;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.jediterm.terminal.StyledTextConsumer;
/*     */ import com.jediterm.terminal.TextStyle;
/*     */ import com.jediterm.terminal.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TerminalLine
/*     */ {
/*  23 */   private static final Logger LOG = Logger.getLogger(TerminalLine.class);
/*     */   
/*  25 */   private TextEntries myTextEntries = new TextEntries();
/*     */   private boolean myWrapped = false;
/*  27 */   private final List<TerminalLineIntervalHighlighting> myCustomHighlightings = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TerminalLine(@NotNull TextEntry entry) {
/*  33 */     this.myTextEntries.add(entry);
/*     */   }
/*     */   
/*     */   public static TerminalLine createEmpty() {
/*  37 */     return new TerminalLine();
/*     */   }
/*     */   
/*     */   public synchronized String getText() {
/*  41 */     StringBuilder sb = new StringBuilder();
/*     */     
/*  43 */     for (TextEntry textEntry : Lists.newArrayList(this.myTextEntries)) {
/*     */       
/*  45 */       if (textEntry.getText().isNul()) {
/*     */         break;
/*     */       }
/*  48 */       sb.append(textEntry.getText());
/*     */     } 
/*     */ 
/*     */     
/*  52 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public char charAt(int x) {
/*  56 */     String text = getText();
/*  57 */     return (x < text.length()) ? text.charAt(x) : ' ';
/*     */   }
/*     */   
/*     */   public boolean isWrapped() {
/*  61 */     return this.myWrapped;
/*     */   }
/*     */   
/*     */   public void setWrapped(boolean wrapped) {
/*  65 */     this.myWrapped = wrapped;
/*     */   }
/*     */   
/*     */   public synchronized void clear(@NotNull TextEntry filler) {
/*  69 */     if (filler == null) $$$reportNull$$$0(1);  this.myTextEntries.clear();
/*  70 */     this.myTextEntries.add(filler);
/*  71 */     setWrapped(false);
/*     */   }
/*     */   
/*     */   public void writeString(int x, @NotNull CharBuffer str, @NotNull TextStyle style) {
/*  75 */     if (str == null) $$$reportNull$$$0(2);  if (style == null) $$$reportNull$$$0(3);  writeCharacters(x, style, str);
/*     */   }
/*     */   
/*     */   private synchronized void writeCharacters(int x, @NotNull TextStyle style, @NotNull CharBuffer characters) {
/*  79 */     if (style == null) $$$reportNull$$$0(4);  if (characters == null) $$$reportNull$$$0(5);  int len = this.myTextEntries.length();
/*     */     
/*  81 */     if (x >= len) {
/*     */       
/*  83 */       if (x - len > 0) {
/*  84 */         this.myTextEntries.add(new TextEntry(TextStyle.EMPTY, new CharBuffer(false, x - len)));
/*     */       }
/*  86 */       this.myTextEntries.add(new TextEntry(style, characters));
/*     */     } else {
/*  88 */       len = Math.max(len, x + characters.length());
/*  89 */       this.myTextEntries = merge(x, characters, style, this.myTextEntries, len);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static TextEntries merge(int x, @NotNull CharBuffer str, @NotNull TextStyle style, @NotNull TextEntries entries, int lineLength) {
/*  94 */     if (str == null) $$$reportNull$$$0(6);  if (style == null) $$$reportNull$$$0(7);  if (entries == null) $$$reportNull$$$0(8);  Pair<char[], TextStyle[]> pair = toBuf(entries, lineLength);
/*     */     
/*  96 */     for (int i = 0; i < str.length(); i++) {
/*  97 */       ((char[])pair.first)[i + x] = str.charAt(i);
/*  98 */       ((TextStyle[])pair.second)[i + x] = style;
/*     */     } 
/*     */     
/* 101 */     return collectFromBuffer((char[])pair.first, (TextStyle[])pair.second);
/*     */   }
/*     */   
/*     */   private static Pair<char[], TextStyle[]> toBuf(TextEntries entries, int lineLength) {
/* 105 */     Pair<char[], TextStyle[]> pair = Pair.create(new char[lineLength], new TextStyle[lineLength]);
/*     */ 
/*     */     
/* 108 */     int p = 0;
/* 109 */     for (TextEntry entry : entries) {
/* 110 */       for (int i = 0; i < entry.getLength(); i++) {
/* 111 */         ((char[])pair.first)[p + i] = entry.getText().charAt(i);
/* 112 */         ((TextStyle[])pair.second)[p + i] = entry.getStyle();
/*     */       } 
/* 114 */       p += entry.getLength();
/*     */     } 
/* 116 */     return pair;
/*     */   }
/*     */   
/*     */   private static TextEntries collectFromBuffer(char[] buf, @NotNull TextStyle[] styles) {
/* 120 */     if (styles == null) $$$reportNull$$$0(9);  TextEntries result = new TextEntries();
/*     */     
/* 122 */     TextStyle curStyle = styles[0];
/* 123 */     int start = 0;
/*     */     
/* 125 */     for (int i = 1; i < buf.length; i++) {
/* 126 */       if (styles[i] != curStyle) {
/* 127 */         result.add(new TextEntry(curStyle, new CharBuffer(buf, start, i - start)));
/* 128 */         curStyle = styles[i];
/* 129 */         start = i;
/*     */       } 
/*     */     } 
/*     */     
/* 133 */     result.add(new TextEntry(curStyle, new CharBuffer(buf, start, buf.length - start)));
/*     */     
/* 135 */     return result;
/*     */   }
/*     */   
/*     */   public synchronized void deleteCharacters(int x) {
/* 139 */     deleteCharacters(x, TextStyle.EMPTY);
/*     */   }
/*     */   
/*     */   public synchronized void deleteCharacters(int x, @NotNull TextStyle style) {
/* 143 */     if (style == null) $$$reportNull$$$0(10);  deleteCharacters(x, this.myTextEntries.length() - x, style);
/*     */     
/* 145 */     setWrapped(false);
/*     */   }
/*     */   
/*     */   public synchronized void deleteCharacters(int x, int count, @NotNull TextStyle style) {
/* 149 */     if (style == null) $$$reportNull$$$0(11);  int p = 0;
/* 150 */     TextEntries newEntries = new TextEntries();
/*     */     
/* 152 */     int remaining = count;
/*     */     
/* 154 */     for (TextEntry entry : this.myTextEntries) {
/* 155 */       if (remaining == 0) {
/* 156 */         newEntries.add(entry);
/*     */         continue;
/*     */       } 
/* 159 */       int len = entry.getLength();
/* 160 */       if (p + len <= x) {
/* 161 */         p += len;
/* 162 */         newEntries.add(entry);
/*     */         continue;
/*     */       } 
/* 165 */       int dx = x - p;
/* 166 */       if (dx > 0) {
/*     */         
/* 168 */         newEntries.add(new TextEntry(entry.getStyle(), entry.getText().subBuffer(0, dx)));
/* 169 */         p = x;
/*     */       } 
/* 171 */       if (dx + remaining < len) {
/*     */         
/* 173 */         newEntries.add(new TextEntry(entry.getStyle(), entry.getText().subBuffer(dx + remaining, len - dx + remaining)));
/* 174 */         remaining = 0; continue;
/*     */       } 
/* 176 */       remaining -= len - dx;
/* 177 */       p = x;
/*     */     } 
/*     */     
/* 180 */     if (count > 0 && style != TextStyle.EMPTY) {
/* 181 */       newEntries.add(new TextEntry(style, new CharBuffer(false, count)));
/*     */     }
/*     */     
/* 184 */     this.myTextEntries = newEntries;
/*     */   }
/*     */   
/*     */   public synchronized void insertBlankCharacters(int x, int count, int maxLen, @NotNull TextStyle style) {
/* 188 */     if (style == null) $$$reportNull$$$0(12);  int len = this.myTextEntries.length();
/* 189 */     len = Math.min(len + count, maxLen);
/*     */     
/* 191 */     char[] buf = new char[len];
/* 192 */     TextStyle[] styles = new TextStyle[len];
/*     */     
/* 194 */     int p = 0;
/* 195 */     for (TextEntry entry : this.myTextEntries) {
/* 196 */       for (int i = 0; i < entry.getLength() && p < len; i++) {
/* 197 */         if (p == x) {
/* 198 */           for (int j = 0; j < count && p < len; j++) {
/* 199 */             buf[p] = ' ';
/* 200 */             styles[p] = style;
/* 201 */             p++;
/*     */           } 
/*     */         }
/* 204 */         if (p < len) {
/* 205 */           buf[p] = entry.getText().charAt(i);
/* 206 */           styles[p] = entry.getStyle();
/* 207 */           p++;
/*     */         } 
/*     */       } 
/* 210 */       if (p >= len) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 216 */     for (; p < x && p < len; p++) {
/* 217 */       buf[p] = ' ';
/* 218 */       styles[p] = TextStyle.EMPTY;
/* 219 */       p++;
/*     */     } 
/* 221 */     for (; p < x + count && p < len; p++) {
/* 222 */       buf[p] = ' ';
/* 223 */       styles[p] = style;
/* 224 */       p++;
/*     */     } 
/*     */     
/* 227 */     this.myTextEntries = collectFromBuffer(buf, styles);
/*     */   }
/*     */   
/*     */   public synchronized void clearArea(int leftX, int rightX, @NotNull TextStyle style) {
/* 231 */     if (style == null) $$$reportNull$$$0(13);  if (rightX == -1) {
/* 232 */       rightX = Math.max(this.myTextEntries.length(), leftX);
/*     */     }
/* 234 */     writeCharacters(leftX, style, new CharBuffer(
/* 235 */           (rightX >= this.myTextEntries.length()) ? Character.MIN_VALUE : 32, rightX - leftX));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public synchronized TextStyle getStyleAt(int x) {
/* 241 */     int i = 0;
/*     */     
/* 243 */     for (TextEntry te : this.myTextEntries) {
/* 244 */       if (x >= i && x < i + te.getLength()) {
/* 245 */         return te.getStyle();
/*     */       }
/* 247 */       i += te.getLength();
/*     */     } 
/*     */     
/* 250 */     return null;
/*     */   }
/*     */   
/*     */   public synchronized void process(int y, StyledTextConsumer consumer, int startRow) {
/* 254 */     int x = 0;
/* 255 */     int nulIndex = -1;
/* 256 */     TerminalLineIntervalHighlighting highlighting = this.myCustomHighlightings.stream().findFirst().orElse(null);
/* 257 */     for (TextEntry te : this.myTextEntries) {
/* 258 */       if (te.getText().isNul()) {
/* 259 */         if (nulIndex < 0) {
/* 260 */           nulIndex = x;
/*     */         }
/* 262 */         consumer.consumeNul(x, y, nulIndex, te.getStyle(), te.getText(), startRow);
/*     */       }
/* 264 */       else if (highlighting != null && te.getLength() > 0 && highlighting.intersectsWith(x, x + te.getLength())) {
/* 265 */         processIntersection(x, y, te, consumer, startRow, highlighting);
/*     */       } else {
/*     */         
/* 268 */         consumer.consume(x, y, te.getStyle(), te.getText(), startRow);
/*     */       } 
/*     */       
/* 271 */       x += te.getLength();
/*     */     } 
/* 273 */     consumer.consumeQueue(x, y, (nulIndex < 0) ? x : nulIndex, startRow);
/*     */   }
/*     */ 
/*     */   
/*     */   private void processIntersection(int startTextOffset, int y, @NotNull TextEntry te, @NotNull StyledTextConsumer consumer, int startRow, @NotNull TerminalLineIntervalHighlighting highlighting) {
/* 278 */     if (te == null) $$$reportNull$$$0(14);  if (consumer == null) $$$reportNull$$$0(15);  if (highlighting == null) $$$reportNull$$$0(16);  CharBuffer text = te.getText();
/* 279 */     int endTextOffset = startTextOffset + text.length();
/* 280 */     int[] offsets = { startTextOffset, endTextOffset, highlighting.getStartOffset(), highlighting.getEndOffset() };
/* 281 */     Arrays.sort(offsets);
/* 282 */     int startTextOffsetInd = Arrays.binarySearch(offsets, startTextOffset);
/* 283 */     int endTextOffsetInd = Arrays.binarySearch(offsets, endTextOffset);
/* 284 */     if (startTextOffsetInd < 0 || endTextOffsetInd < 0) {
/* 285 */       LOG.error("Cannot find " + Arrays.toString(new int[] { startTextOffset, endTextOffset }) + " in " + 
/* 286 */           Arrays.toString(offsets) + ": " + Arrays.toString(new int[] { startTextOffsetInd, endTextOffsetInd }));
/* 287 */       consumer.consume(startTextOffset, y, te.getStyle(), text, startRow);
/*     */       return;
/*     */     } 
/* 290 */     for (int i = startTextOffsetInd; i < endTextOffsetInd; i++) {
/* 291 */       int length = offsets[i + 1] - offsets[i];
/* 292 */       if (length != 0) {
/* 293 */         CharBuffer subText = new SubCharBuffer(text, offsets[i] - startTextOffset, length);
/* 294 */         if (highlighting.intersectsWith(offsets[i], offsets[i + 1])) {
/* 295 */           consumer.consume(offsets[i], y, highlighting.mergeWith(te.getStyle()), subText, startRow);
/*     */         } else {
/*     */           
/* 298 */           consumer.consume(offsets[i], y, te.getStyle(), subText, startRow);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public synchronized boolean isNul() {
/* 304 */     for (TextEntry e : this.myTextEntries.entries()) {
/* 305 */       if (!e.isNul()) {
/* 306 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 310 */     return true;
/*     */   }
/*     */   
/*     */   void forEachEntry(@NotNull Consumer<TextEntry> action) {
/* 314 */     if (action == null) $$$reportNull$$$0(17);  this.myTextEntries.forEach(action);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<TextEntry> getEntries() {
/* 319 */     return this.myTextEntries.entries();
/*     */   }
/*     */   
/*     */   void appendEntry(@NotNull TextEntry entry) {
/* 323 */     if (entry == null) $$$reportNull$$$0(18);  this.myTextEntries.add(entry);
/*     */   }
/*     */   @NotNull
/*     */   public synchronized TerminalLineIntervalHighlighting addCustomHighlighting(int startOffset, int length, @NotNull TextStyle textStyle) {
/* 327 */     if (textStyle == null) $$$reportNull$$$0(19);  TerminalLineIntervalHighlighting highlighting = new TerminalLineIntervalHighlighting(this, startOffset, length, textStyle)
/*     */       {
/*     */         protected void doDispose() {
/* 330 */           synchronized (TerminalLine.this) {
/* 331 */             TerminalLine.this.myCustomHighlightings.remove(this);
/*     */           } 
/*     */         }
/*     */       };
/* 335 */     this.myCustomHighlightings.add(highlighting);
/* 336 */     if (highlighting == null) $$$reportNull$$$0(20);  return highlighting;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 341 */     return this.myTextEntries.length() + " chars, " + (this.myWrapped ? "wrapped, " : "") + this.myTextEntries
/*     */       
/* 343 */       .myTextEntries.size() + " entries: " + 
/* 344 */       Joiner.on("|").join((Iterable)this.myTextEntries.myTextEntries.stream().map(entry -> entry.getText().toString())
/* 345 */         .collect(Collectors.toList()));
/*     */   }
/*     */   
/*     */   public TerminalLine() {}
/*     */   
/*     */   public static class TextEntry { private final TextStyle myStyle;
/*     */     private final CharBuffer myText;
/*     */     
/*     */     public TextEntry(@NotNull TextStyle style, @NotNull CharBuffer text) {
/* 354 */       this.myStyle = style;
/* 355 */       this.myText = text.clone();
/*     */     }
/*     */     
/*     */     public TextStyle getStyle() {
/* 359 */       return this.myStyle;
/*     */     }
/*     */     
/*     */     public CharBuffer getText() {
/* 363 */       return this.myText;
/*     */     }
/*     */     
/*     */     public int getLength() {
/* 367 */       return this.myText.length();
/*     */     }
/*     */     
/*     */     public boolean isNul() {
/* 371 */       return this.myText.isNul();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 376 */       return this.myText.length() + " chars, style: " + this.myStyle + ", text: " + this.myText;
/*     */     } }
/*     */ 
/*     */   
/*     */   private static class TextEntries implements Iterable<TextEntry> {
/* 381 */     private final List<TerminalLine.TextEntry> myTextEntries = new ArrayList<>();
/*     */     
/* 383 */     private int myLength = 0;
/*     */ 
/*     */     
/*     */     public void add(TerminalLine.TextEntry entry) {
/* 387 */       if (!entry.getText().isNul()) {
/* 388 */         for (TerminalLine.TextEntry t : this.myTextEntries) {
/* 389 */           if (t.getText().isNul()) {
/* 390 */             t.getText().unNullify();
/*     */           }
/*     */         } 
/*     */       }
/* 394 */       this.myTextEntries.add(entry);
/* 395 */       this.myLength += entry.getLength();
/*     */     }
/*     */     
/*     */     private List<TerminalLine.TextEntry> entries() {
/* 399 */       return Collections.unmodifiableList(this.myTextEntries);
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public Iterator<TerminalLine.TextEntry> iterator() {
/* 404 */       if (entries().iterator() == null) $$$reportNull$$$0(0);  return entries().iterator();
/*     */     }
/*     */     
/*     */     public int length() {
/* 408 */       return this.myLength;
/*     */     }
/*     */     
/*     */     public void clear() {
/* 412 */       this.myTextEntries.clear();
/* 413 */       this.myLength = 0;
/*     */     }
/*     */     
/*     */     private TextEntries() {}
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\TerminalLine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */