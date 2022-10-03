/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics2D;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Field;
/*     */ import javax.swing.text.StyleContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ import org.xml.sax.helpers.XMLReaderFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SyntaxScheme
/*     */   implements Cloneable, TokenTypes
/*     */ {
/*     */   private Style[] styles;
/*     */   private static final String VERSION = "*ver1";
/*     */   
/*     */   public SyntaxScheme(boolean useDefaults) {
/*  57 */     this.styles = new Style[39];
/*  58 */     if (useDefaults) {
/*  59 */       restoreDefaults(null);
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
/*     */   public SyntaxScheme(Font baseFont) {
/*  72 */     this(baseFont, true);
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
/*     */   public SyntaxScheme(Font baseFont, boolean fontStyles) {
/*  86 */     this.styles = new Style[39];
/*  87 */     restoreDefaults(baseFont, fontStyles);
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
/*     */   void changeBaseFont(Font old, Font font) {
/* 104 */     for (Style style : this.styles) {
/* 105 */       if (style != null && style.font != null && 
/* 106 */         style.font.getFamily().equals(old.getFamily()) && style.font
/* 107 */         .getSize() == old.getSize()) {
/* 108 */         int s = style.font.getStyle();
/* 109 */         StyleContext sc = StyleContext.getDefaultStyleContext();
/* 110 */         style.font = sc.getFont(font.getFamily(), s, font.getSize());
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
/*     */   public Object clone() {
/*     */     SyntaxScheme shcs;
/*     */     try {
/* 126 */       shcs = (SyntaxScheme)super.clone();
/* 127 */     } catch (CloneNotSupportedException cnse) {
/* 128 */       cnse.printStackTrace();
/* 129 */       return null;
/*     */     } 
/* 131 */     shcs.styles = new Style[this.styles.length];
/* 132 */     for (int i = 0; i < this.styles.length; i++) {
/* 133 */       Style s = this.styles[i];
/* 134 */       if (s != null) {
/* 135 */         shcs.styles[i] = (Style)s.clone();
/*     */       }
/*     */     } 
/* 138 */     return shcs;
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
/*     */   public boolean equals(Object otherScheme) {
/* 155 */     if (!(otherScheme instanceof SyntaxScheme)) {
/* 156 */       return false;
/*     */     }
/*     */     
/* 159 */     Style[] otherSchemes = ((SyntaxScheme)otherScheme).styles;
/*     */     
/* 161 */     int length = this.styles.length;
/* 162 */     for (int i = 0; i < length; i++) {
/* 163 */       if (this.styles[i] == null) {
/* 164 */         if (otherSchemes[i] != null) {
/* 165 */           return false;
/*     */         }
/*     */       }
/* 168 */       else if (!this.styles[i].equals(otherSchemes[i])) {
/* 169 */         return false;
/*     */       } 
/*     */     } 
/* 172 */     return true;
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
/*     */   private static String getHexString(Color c) {
/* 185 */     return "$" + Integer.toHexString((c.getRGB() & 0xFFFFFF) + 16777216)
/* 186 */       .substring(1);
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
/*     */   public Style getStyle(int index) {
/* 199 */     return this.styles[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStyleCount() {
/* 210 */     return this.styles.length;
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
/*     */   public Style[] getStyles() {
/* 226 */     return this.styles;
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
/*     */   public int hashCode() {
/* 240 */     int hashCode = 0;
/* 241 */     int count = this.styles.length;
/* 242 */     for (Style style : this.styles) {
/* 243 */       if (style != null) {
/* 244 */         hashCode ^= style.hashCode();
/*     */         break;
/*     */       } 
/*     */     } 
/* 248 */     return hashCode;
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
/*     */   public static SyntaxScheme load(Font baseFont, InputStream in) throws IOException {
/* 267 */     if (baseFont == null) {
/* 268 */       baseFont = RSyntaxTextArea.getDefaultFont();
/*     */     }
/* 270 */     return SyntaxSchemeLoader.load(baseFont, in);
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
/*     */   public static SyntaxScheme loadFromString(String string) {
/* 287 */     return loadFromString(string, 39);
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
/*     */   public static SyntaxScheme loadFromString(String string, int tokenTypeCount) {
/* 312 */     SyntaxScheme scheme = new SyntaxScheme(true);
/*     */ 
/*     */     
/*     */     try {
/* 316 */       if (string != null)
/*     */       {
/* 318 */         String[] tokens = string.split(",", -1);
/*     */ 
/*     */         
/* 321 */         if (tokens.length == 0 || !"*ver1".equals(tokens[0])) {
/* 322 */           return scheme;
/*     */         }
/*     */         
/* 325 */         int tokenCount = tokenTypeCount * 7 + 1;
/* 326 */         if (tokens.length != tokenCount) {
/* 327 */           throw new Exception("Not enough tokens in packed color scheme: expected " + tokenCount + ", found " + tokens.length);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 334 */         StyleContext sc = StyleContext.getDefaultStyleContext();
/*     */ 
/*     */ 
/*     */         
/* 338 */         for (int i = 0; i < tokenTypeCount; i++)
/*     */         {
/* 340 */           int pos = i * 7 + 1;
/* 341 */           int integer = Integer.parseInt(tokens[pos]);
/* 342 */           if (integer != i) {
/* 343 */             throw new Exception("Expected " + i + ", found " + integer);
/*     */           }
/*     */ 
/*     */           
/* 347 */           Color fg = null; String temp = tokens[pos + 1];
/* 348 */           if (!"-".equals(temp)) {
/* 349 */             fg = stringToColor(temp);
/*     */           }
/* 351 */           Color bg = null; temp = tokens[pos + 2];
/* 352 */           if (!"-".equals(temp)) {
/* 353 */             bg = stringToColor(temp);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 359 */           temp = tokens[pos + 3];
/* 360 */           if (!"t".equals(temp) && !"f".equals(temp)) {
/* 361 */             throw new Exception("Expected 't' or 'f', found " + temp);
/*     */           }
/* 363 */           boolean underline = "t".equals(temp);
/*     */           
/* 365 */           Font font = null;
/* 366 */           String family = tokens[pos + 4];
/* 367 */           if (!"-".equals(family)) {
/* 368 */             font = sc.getFont(family, 
/* 369 */                 Integer.parseInt(tokens[pos + 5]), 
/* 370 */                 Integer.parseInt(tokens[pos + 6]));
/*     */           }
/* 372 */           scheme.styles[i] = new Style(fg, bg, font, underline);
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 378 */     catch (Exception e) {
/* 379 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 382 */     return scheme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void refreshFontMetrics(Graphics2D g2d) {
/* 389 */     for (Style s : this.styles) {
/* 390 */       if (s != null) {
/* 391 */         s
/* 392 */           .fontMetrics = (s.font == null) ? null : g2d.getFontMetrics(s.font);
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
/*     */   public void restoreDefaults(Font baseFont) {
/* 406 */     restoreDefaults(baseFont, true);
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
/*     */   public void restoreDefaults(Font baseFont, boolean fontStyles) {
/* 422 */     Color comment = new Color(0, 128, 0);
/* 423 */     Color docComment = new Color(164, 0, 0);
/* 424 */     Color markupComment = new Color(0, 96, 0);
/* 425 */     Color keyword = Color.BLUE;
/* 426 */     Color dataType = new Color(0, 128, 128);
/* 427 */     Color function = new Color(173, 128, 0);
/* 428 */     Color preprocessor = new Color(128, 128, 128);
/* 429 */     Color operator = new Color(128, 64, 64);
/* 430 */     Color regex = new Color(0, 128, 164);
/* 431 */     Color variable = new Color(255, 153, 0);
/* 432 */     Color literalNumber = new Color(100, 0, 200);
/* 433 */     Color literalString = new Color(220, 0, 156);
/* 434 */     Color error = new Color(148, 148, 0);
/*     */ 
/*     */     
/* 437 */     if (baseFont == null) {
/* 438 */       baseFont = RSyntaxTextArea.getDefaultFont();
/*     */     }
/* 440 */     Font commentFont = baseFont;
/* 441 */     Font keywordFont = baseFont;
/* 442 */     if (fontStyles) {
/*     */ 
/*     */       
/* 445 */       StyleContext sc = StyleContext.getDefaultStyleContext();
/* 446 */       Font boldFont = sc.getFont(baseFont.getFamily(), 1, baseFont
/* 447 */           .getSize());
/* 448 */       Font italicFont = sc.getFont(baseFont.getFamily(), 2, baseFont
/* 449 */           .getSize());
/* 450 */       commentFont = italicFont;
/* 451 */       keywordFont = boldFont;
/*     */     } 
/*     */     
/* 454 */     this.styles[1] = new Style(comment, null, commentFont);
/* 455 */     this.styles[2] = new Style(comment, null, commentFont);
/* 456 */     this.styles[3] = new Style(docComment, null, commentFont);
/* 457 */     this.styles[4] = new Style(new Color(255, 152, 0), null, commentFont);
/* 458 */     this.styles[5] = new Style(Color.gray, null, commentFont);
/* 459 */     this.styles[6] = new Style(keyword, null, keywordFont);
/* 460 */     this.styles[7] = new Style(keyword, null, keywordFont);
/* 461 */     this.styles[8] = new Style(function);
/* 462 */     this.styles[9] = new Style(literalNumber);
/* 463 */     this.styles[10] = new Style(literalNumber);
/* 464 */     this.styles[11] = new Style(literalNumber);
/* 465 */     this.styles[12] = new Style(literalNumber);
/* 466 */     this.styles[13] = new Style(literalString);
/* 467 */     this.styles[14] = new Style(literalString);
/* 468 */     this.styles[15] = new Style(literalString);
/* 469 */     this.styles[16] = new Style(dataType, null, keywordFont);
/* 470 */     this.styles[17] = new Style(variable);
/* 471 */     this.styles[18] = new Style(regex);
/* 472 */     this.styles[19] = new Style(Color.gray);
/* 473 */     this.styles[20] = new Style(null);
/* 474 */     this.styles[21] = new Style(Color.gray);
/* 475 */     this.styles[22] = new Style(Color.RED);
/* 476 */     this.styles[23] = new Style(operator);
/* 477 */     this.styles[24] = new Style(preprocessor);
/* 478 */     this.styles[25] = new Style(Color.RED);
/* 479 */     this.styles[26] = new Style(Color.BLUE);
/* 480 */     this.styles[27] = new Style(new Color(63, 127, 127));
/* 481 */     this.styles[28] = new Style(literalString);
/* 482 */     this.styles[29] = new Style(markupComment, null, commentFont);
/* 483 */     this.styles[30] = new Style(function);
/* 484 */     this.styles[31] = new Style(preprocessor);
/* 485 */     this.styles[33] = new Style(new Color(13395456));
/* 486 */     this.styles[32] = new Style(new Color(32896));
/* 487 */     this.styles[34] = new Style(dataType);
/* 488 */     this.styles[35] = new Style(error);
/* 489 */     this.styles[36] = new Style(error);
/* 490 */     this.styles[37] = new Style(error);
/* 491 */     this.styles[38] = new Style(error);
/*     */ 
/*     */ 
/*     */     
/* 495 */     for (int i = 0; i < this.styles.length; i++) {
/* 496 */       if (this.styles[i] == null) {
/* 497 */         this.styles[i] = new Style();
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
/*     */   public void setStyle(int type, Style style) {
/* 512 */     this.styles[type] = style;
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
/*     */   public void setStyles(Style[] styles) {
/* 528 */     this.styles = styles;
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
/*     */   private static Color stringToColor(String s) {
/* 548 */     char ch = s.charAt(0);
/* 549 */     return new Color((ch == '$' || ch == '#') ? 
/* 550 */         Integer.parseInt(s.substring(1), 16) : 
/* 551 */         Integer.parseInt(s));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toCommaSeparatedString() {
/* 584 */     StringBuilder sb = new StringBuilder("*ver1");
/* 585 */     sb.append(',');
/*     */     
/* 587 */     for (int i = 0; i < this.styles.length; i++) {
/*     */       
/* 589 */       sb.append(i).append(',');
/*     */       
/* 591 */       Style ss = this.styles[i];
/* 592 */       if (ss == null) {
/* 593 */         sb.append("-,-,f,-,,,");
/*     */       }
/*     */       else {
/*     */         
/* 597 */         Color c = ss.foreground;
/* 598 */         sb.append((c != null) ? (getHexString(c) + ",") : "-,");
/* 599 */         c = ss.background;
/* 600 */         sb.append((c != null) ? (getHexString(c) + ",") : "-,");
/*     */         
/* 602 */         sb.append(ss.underline ? "t," : "f,");
/*     */         
/* 604 */         Font font = ss.font;
/* 605 */         if (font != null) {
/* 606 */           sb.append(font.getFamily()).append(',')
/* 607 */             .append(font.getStyle()).append(',')
/* 608 */             .append(font.getSize()).append(',');
/*     */         } else {
/*     */           
/* 611 */           sb.append("-,,,");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 616 */     return sb.substring(0, sb.length() - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class SyntaxSchemeLoader
/*     */     extends DefaultHandler
/*     */   {
/*     */     private Font baseFont;
/*     */     
/*     */     private SyntaxScheme scheme;
/*     */ 
/*     */     
/*     */     SyntaxSchemeLoader(Font baseFont) {
/* 629 */       this.scheme = new SyntaxScheme(baseFont);
/*     */     }
/*     */ 
/*     */     
/*     */     public static SyntaxScheme load(Font baseFont, InputStream in) throws IOException {
/*     */       SyntaxSchemeLoader parser;
/*     */       try {
/* 636 */         XMLReader reader = XMLReaderFactory.createXMLReader();
/* 637 */         parser = new SyntaxSchemeLoader(baseFont);
/* 638 */         parser.baseFont = baseFont;
/* 639 */         reader.setContentHandler(parser);
/* 640 */         InputSource is = new InputSource(in);
/* 641 */         is.setEncoding("UTF-8");
/* 642 */         reader.parse(is);
/* 643 */       } catch (SAXException se) {
/* 644 */         throw new IOException(se.toString());
/*     */       } 
/* 646 */       return parser.scheme;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void startElement(String uri, String localName, String qName, Attributes attrs) {
/* 653 */       if ("style".equals(qName)) {
/*     */         Field field;
/* 655 */         String type = attrs.getValue("token");
/*     */ 
/*     */         
/*     */         try {
/* 659 */           field = SyntaxScheme.class.getField(type);
/* 660 */         } catch (RuntimeException re) {
/* 661 */           throw re;
/* 662 */         } catch (Exception e) {
/* 663 */           System.err.println("Error fetching 'getType' method for Token class");
/*     */           
/*     */           return;
/*     */         } 
/* 667 */         if (field.getType() == int.class) {
/*     */           
/* 669 */           int index = 0;
/*     */           try {
/* 671 */             index = field.getInt(this.scheme);
/* 672 */           } catch (IllegalArgumentException|IllegalAccessException e) {
/* 673 */             e.printStackTrace();
/*     */             
/*     */             return;
/*     */           } 
/* 677 */           String fgStr = attrs.getValue("fg");
/* 678 */           if (fgStr != null) {
/* 679 */             Color fg = SyntaxScheme.stringToColor(fgStr);
/* 680 */             (this.scheme.styles[index]).foreground = fg;
/*     */           } 
/*     */           
/* 683 */           String bgStr = attrs.getValue("bg");
/* 684 */           if (bgStr != null) {
/* 685 */             Color bg = SyntaxScheme.stringToColor(bgStr);
/* 686 */             (this.scheme.styles[index]).background = bg;
/*     */           } 
/*     */           
/* 689 */           boolean styleSpecified = false;
/* 690 */           boolean bold = false;
/* 691 */           boolean italic = false;
/* 692 */           String boldStr = attrs.getValue("bold");
/* 693 */           if (boldStr != null) {
/* 694 */             bold = Boolean.parseBoolean(boldStr);
/* 695 */             styleSpecified = true;
/*     */           } 
/* 697 */           String italicStr = attrs.getValue("italic");
/* 698 */           if (italicStr != null) {
/* 699 */             italic = Boolean.parseBoolean(italicStr);
/* 700 */             styleSpecified = true;
/*     */           } 
/* 702 */           if (styleSpecified) {
/* 703 */             int style = 0;
/* 704 */             if (bold) {
/* 705 */               style |= 0x1;
/*     */             }
/* 707 */             if (italic) {
/* 708 */               style |= 0x2;
/*     */             }
/* 710 */             (this.scheme.styles[index]).font = this.baseFont.deriveFont(style);
/*     */           } 
/*     */           
/* 713 */           String ulineStr = attrs.getValue("underline");
/* 714 */           if (ulineStr != null) {
/* 715 */             boolean uline = Boolean.parseBoolean(ulineStr);
/* 716 */             (this.scheme.styles[index]).underline = uline;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\SyntaxScheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */