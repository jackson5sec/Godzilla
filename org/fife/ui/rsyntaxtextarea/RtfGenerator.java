/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Toolkit;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.fife.ui.rtextarea.RTextArea;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RtfGenerator
/*     */ {
/*     */   private Color mainBG;
/*     */   private List<Font> fontList;
/*     */   private List<Color> colorList;
/*     */   private StringBuilder document;
/*     */   private boolean lastWasControlWord;
/*     */   private int lastFontIndex;
/*     */   private int lastFGIndex;
/*     */   private boolean lastBold;
/*     */   private boolean lastItalic;
/*     */   private int lastFontSize;
/*     */   private int screenRes;
/*     */   private static final int DEFAULT_FONT_SIZE = 12;
/*     */   
/*     */   public RtfGenerator(Color mainBG) {
/*  76 */     this.mainBG = mainBG;
/*  77 */     this.fontList = new ArrayList<>(1);
/*  78 */     this.colorList = new ArrayList<>(1);
/*  79 */     this.document = new StringBuilder();
/*  80 */     reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendNewline() {
/*  90 */     this.document.append("\\line");
/*  91 */     this.document.append('\n');
/*  92 */     this.lastWasControlWord = false;
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
/*     */   public void appendToDoc(String text, Font f, Color fg, Color bg) {
/* 109 */     appendToDoc(text, f, fg, bg, false);
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
/*     */   public void appendToDocNoFG(String text, Font f, Color bg, boolean underline) {
/* 126 */     appendToDoc(text, f, null, bg, underline, false);
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
/*     */   public void appendToDoc(String text, Font f, Color fg, Color bg, boolean underline) {
/* 145 */     appendToDoc(text, f, fg, bg, underline, true);
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
/*     */   public void appendToDoc(String text, Font f, Color fg, Color bg, boolean underline, boolean setFG) {
/* 167 */     if (text != null) {
/*     */ 
/*     */       
/* 170 */       int fontIndex = (f == null) ? 0 : (getFontIndex(this.fontList, f) + 1);
/* 171 */       if (fontIndex != this.lastFontIndex) {
/* 172 */         this.document.append("\\f").append(fontIndex);
/* 173 */         this.lastFontIndex = fontIndex;
/* 174 */         this.lastWasControlWord = true;
/*     */       } 
/*     */ 
/*     */       
/* 178 */       if (f != null) {
/* 179 */         int fontSize = fixFontSize(f.getSize2D() * 2.0F);
/* 180 */         if (fontSize != this.lastFontSize) {
/* 181 */           this.document.append("\\fs").append(fontSize);
/* 182 */           this.lastFontSize = fontSize;
/* 183 */           this.lastWasControlWord = true;
/*     */         } 
/* 185 */         if (f.isBold() != this.lastBold) {
/* 186 */           this.document.append(this.lastBold ? "\\b0" : "\\b");
/* 187 */           this.lastBold = !this.lastBold;
/* 188 */           this.lastWasControlWord = true;
/*     */         } 
/* 190 */         if (f.isItalic() != this.lastItalic) {
/* 191 */           this.document.append(this.lastItalic ? "\\i0" : "\\i");
/* 192 */           this.lastItalic = !this.lastItalic;
/* 193 */           this.lastWasControlWord = true;
/*     */         } 
/*     */       } else {
/*     */         
/* 197 */         if (this.lastFontSize != 12) {
/* 198 */           this.document.append("\\fs").append(12);
/* 199 */           this.lastFontSize = 12;
/* 200 */           this.lastWasControlWord = true;
/*     */         } 
/* 202 */         if (this.lastBold) {
/* 203 */           this.document.append("\\b0");
/* 204 */           this.lastBold = false;
/* 205 */           this.lastWasControlWord = true;
/*     */         } 
/* 207 */         if (this.lastItalic) {
/* 208 */           this.document.append("\\i0");
/* 209 */           this.lastItalic = false;
/* 210 */           this.lastWasControlWord = true;
/*     */         } 
/*     */       } 
/* 213 */       if (underline) {
/* 214 */         this.document.append("\\ul");
/* 215 */         this.lastWasControlWord = true;
/*     */       } 
/*     */ 
/*     */       
/* 219 */       if (setFG) {
/* 220 */         int fgIndex = 0;
/* 221 */         if (fg != null) {
/* 222 */           fgIndex = getColorIndex(this.colorList, fg) + 1;
/*     */         }
/* 224 */         if (fgIndex != this.lastFGIndex) {
/* 225 */           this.document.append("\\cf").append(fgIndex);
/* 226 */           this.lastFGIndex = fgIndex;
/* 227 */           this.lastWasControlWord = true;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 232 */       if (bg != null) {
/* 233 */         int pos = getColorIndex(this.colorList, bg);
/* 234 */         this.document.append("\\highlight").append(pos + 1);
/* 235 */         this.lastWasControlWord = true;
/*     */       } 
/*     */       
/* 238 */       if (this.lastWasControlWord) {
/* 239 */         this.document.append(' ');
/* 240 */         this.lastWasControlWord = false;
/*     */       } 
/* 242 */       escapeAndAdd(this.document, text);
/*     */ 
/*     */       
/* 245 */       if (bg != null) {
/* 246 */         this.document.append("\\highlight0");
/* 247 */         this.lastWasControlWord = true;
/*     */       } 
/* 249 */       if (underline) {
/* 250 */         this.document.append("\\ul0");
/* 251 */         this.lastWasControlWord = true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void escapeAndAdd(StringBuilder sb, String text) {
/* 273 */     int count = text.length();
/* 274 */     for (int i = 0; i < count; i++) {
/* 275 */       char ch = text.charAt(i);
/* 276 */       switch (ch) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case '\t':
/* 282 */           sb.append("\\tab");
/* 283 */           while (++i < count && text.charAt(i) == '\t') {
/* 284 */             sb.append("\\tab");
/*     */           }
/* 286 */           sb.append(' ');
/* 287 */           i--;
/*     */           break;
/*     */         case '\\':
/*     */         case '{':
/*     */         case '}':
/* 292 */           sb.append('\\').append(ch);
/*     */           break;
/*     */         default:
/* 295 */           if (ch <= '') {
/* 296 */             sb.append(ch);
/*     */             break;
/*     */           } 
/* 299 */           sb.append("\\u").append(ch).append(' ');
/*     */           break;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int fixFontSize(float pointSize) {
/* 322 */     if (this.screenRes != 72) {
/* 323 */       pointSize = Math.round(pointSize * 72.0F / this.screenRes);
/*     */     }
/* 325 */     return (int)pointSize;
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
/*     */   private static int getColorIndex(List<Color> list, Color item) {
/* 338 */     int pos = list.indexOf(item);
/* 339 */     if (pos == -1) {
/* 340 */       list.add(item);
/* 341 */       pos = list.size() - 1;
/*     */     } 
/* 343 */     return pos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getColorTableRtf() {
/* 352 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 354 */     sb.append("{\\colortbl ;");
/* 355 */     for (Color c : this.colorList) {
/* 356 */       sb.append("\\red").append(c.getRed());
/* 357 */       sb.append("\\green").append(c.getGreen());
/* 358 */       sb.append("\\blue").append(c.getBlue());
/* 359 */       sb.append(';');
/*     */     } 
/* 361 */     sb.append("}");
/*     */     
/* 363 */     return sb.toString();
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
/*     */   private static int getFontIndex(List<Font> list, Font font) {
/* 381 */     String fontName = font.getFamily();
/* 382 */     for (int i = 0; i < list.size(); i++) {
/* 383 */       Font font2 = list.get(i);
/* 384 */       if (font2.getFamily().equals(fontName)) {
/* 385 */         return i;
/*     */       }
/*     */     } 
/* 388 */     list.add(font);
/* 389 */     return list.size() - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getFontTableRtf() {
/* 398 */     StringBuilder sb = new StringBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 403 */     String monoFamilyName = getMonospacedFontFamily();
/*     */     
/* 405 */     sb.append("{\\fonttbl{\\f0\\fnil\\fcharset0 ").append(monoFamilyName).append(";}");
/* 406 */     for (int i = 0; i < this.fontList.size(); i++) {
/* 407 */       Font f = this.fontList.get(i);
/* 408 */       String familyName = f.getFamily();
/* 409 */       if (familyName.equals("Monospaced")) {
/* 410 */         familyName = monoFamilyName;
/*     */       }
/* 412 */       sb.append("{\\f").append(i + 1).append("\\fnil\\fcharset0 ");
/* 413 */       sb.append(familyName).append(";}");
/*     */     } 
/* 415 */     sb.append('}');
/*     */     
/* 417 */     return sb.toString();
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
/*     */   private static String getMonospacedFontFamily() {
/* 429 */     String family = RTextArea.getDefaultFont().getFamily();
/* 430 */     if ("Monospaced".equals(family)) {
/* 431 */       family = "Courier";
/*     */     }
/* 433 */     return family;
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
/*     */   public String getRtf() {
/* 445 */     int mainBGIndex = getColorIndex(this.colorList, this.mainBG);
/*     */     
/* 447 */     StringBuilder sb = new StringBuilder();
/* 448 */     sb.append("{");
/*     */ 
/*     */     
/* 451 */     sb.append("\\rtf1\\ansi\\ansicpg1252");
/* 452 */     sb.append("\\deff0");
/* 453 */     sb.append("\\deflang1033");
/* 454 */     sb.append("\\viewkind4");
/* 455 */     sb.append("\\uc\\pard\\f0");
/* 456 */     sb.append("\\fs20");
/* 457 */     sb.append(getFontTableRtf()).append('\n');
/* 458 */     sb.append(getColorTableRtf()).append('\n');
/*     */ 
/*     */     
/* 461 */     int bgIndex = mainBGIndex + 1;
/* 462 */     sb.append("\\cb").append(bgIndex).append(' ');
/* 463 */     this.lastWasControlWord = true;
/* 464 */     if (this.document.length() > 0) {
/* 465 */       this.document.append("\\line");
/*     */     }
/* 467 */     sb.append(this.document);
/*     */     
/* 469 */     sb.append("}");
/*     */ 
/*     */     
/* 472 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 482 */     this.fontList.clear();
/* 483 */     this.colorList.clear();
/* 484 */     this.document.setLength(0);
/* 485 */     this.lastWasControlWord = false;
/* 486 */     this.lastFontIndex = 0;
/* 487 */     this.lastFGIndex = 0;
/* 488 */     this.lastBold = false;
/* 489 */     this.lastItalic = false;
/* 490 */     this.lastFontSize = 12;
/* 491 */     this.screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\RtfGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */