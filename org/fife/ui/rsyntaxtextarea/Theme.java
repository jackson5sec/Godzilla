/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.SystemColor;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.Field;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.text.StyleContext;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.fife.io.UnicodeWriter;
/*     */ import org.fife.ui.rtextarea.Gutter;
/*     */ import org.fife.ui.rtextarea.RTextArea;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Theme
/*     */ {
/*     */   public Font baseFont;
/*     */   public Color bgColor;
/*     */   public Color caretColor;
/*     */   public boolean useSelectionFG;
/*     */   public Color selectionFG;
/*     */   public Color selectionBG;
/*     */   public boolean selectionRoundedEdges;
/*     */   public Color currentLineHighlight;
/*     */   public boolean fadeCurrentLineHighlight;
/*     */   public Color tabLineColor;
/*     */   public Color marginLineColor;
/*     */   public Color markAllHighlightColor;
/*     */   public Color markOccurrencesColor;
/*     */   public boolean markOccurrencesBorder;
/*     */   public Color matchedBracketFG;
/*     */   public Color matchedBracketBG;
/*     */   public boolean matchedBracketHighlightBoth;
/*     */   public boolean matchedBracketAnimate;
/*     */   public Color hyperlinkFG;
/*     */   public Color[] secondaryLanguages;
/*     */   public SyntaxScheme scheme;
/*     */   public Color gutterBackgroundColor;
/*     */   public Color gutterBorderColor;
/*     */   public Color activeLineRangeColor;
/*     */   public boolean iconRowHeaderInheritsGutterBG;
/*     */   public Color lineNumberColor;
/*     */   public String lineNumberFont;
/*     */   public int lineNumberFontSize;
/*     */   public Color foldIndicatorFG;
/*     */   public Color foldBG;
/*     */   public Color armedFoldBG;
/*     */   
/*     */   private Theme(Font baseFont) {
/* 120 */     this.baseFont = (baseFont != null) ? baseFont : RTextArea.getDefaultFont();
/* 121 */     this.secondaryLanguages = new Color[3];
/* 122 */     this.activeLineRangeColor = Gutter.DEFAULT_ACTIVE_LINE_RANGE_COLOR;
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
/*     */   public Theme(RSyntaxTextArea textArea) {
/* 134 */     this.baseFont = textArea.getFont();
/* 135 */     this.bgColor = textArea.getBackground();
/* 136 */     this.caretColor = textArea.getCaretColor();
/* 137 */     this.useSelectionFG = textArea.getUseSelectedTextColor();
/* 138 */     this.selectionFG = textArea.getSelectedTextColor();
/* 139 */     this.selectionBG = textArea.getSelectionColor();
/* 140 */     this.selectionRoundedEdges = textArea.getRoundedSelectionEdges();
/* 141 */     this.currentLineHighlight = textArea.getCurrentLineHighlightColor();
/* 142 */     this.fadeCurrentLineHighlight = textArea.getFadeCurrentLineHighlight();
/* 143 */     this.tabLineColor = textArea.getTabLineColor();
/* 144 */     this.marginLineColor = textArea.getMarginLineColor();
/* 145 */     this.markAllHighlightColor = textArea.getMarkAllHighlightColor();
/* 146 */     this.markOccurrencesColor = textArea.getMarkOccurrencesColor();
/* 147 */     this.markOccurrencesBorder = textArea.getPaintMarkOccurrencesBorder();
/* 148 */     this.matchedBracketBG = textArea.getMatchedBracketBGColor();
/* 149 */     this.matchedBracketFG = textArea.getMatchedBracketBorderColor();
/* 150 */     this.matchedBracketHighlightBoth = textArea.getPaintMatchedBracketPair();
/* 151 */     this.matchedBracketAnimate = textArea.getAnimateBracketMatching();
/* 152 */     this.hyperlinkFG = textArea.getHyperlinkForeground();
/*     */     
/* 154 */     int count = textArea.getSecondaryLanguageCount();
/* 155 */     this.secondaryLanguages = new Color[count];
/* 156 */     for (int i = 0; i < count; i++) {
/* 157 */       this.secondaryLanguages[i] = textArea.getSecondaryLanguageBackground(i + 1);
/*     */     }
/*     */     
/* 160 */     this.scheme = textArea.getSyntaxScheme();
/*     */     
/* 162 */     Gutter gutter = RSyntaxUtilities.getGutter(textArea);
/* 163 */     if (gutter != null) {
/* 164 */       this.gutterBackgroundColor = gutter.getBackground();
/* 165 */       this.gutterBorderColor = gutter.getBorderColor();
/* 166 */       this.activeLineRangeColor = gutter.getActiveLineRangeColor();
/* 167 */       this.iconRowHeaderInheritsGutterBG = gutter.getIconRowHeaderInheritsGutterBackground();
/* 168 */       this.lineNumberColor = gutter.getLineNumberColor();
/* 169 */       this.lineNumberFont = gutter.getLineNumberFont().getFamily();
/* 170 */       this.lineNumberFontSize = gutter.getLineNumberFont().getSize();
/* 171 */       this.foldIndicatorFG = gutter.getFoldIndicatorForeground();
/* 172 */       this.foldBG = gutter.getFoldBackground();
/* 173 */       this.armedFoldBG = gutter.getArmedFoldBackground();
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
/*     */   public void apply(RSyntaxTextArea textArea) {
/* 186 */     textArea.setFont(this.baseFont);
/* 187 */     textArea.setBackground(this.bgColor);
/* 188 */     textArea.setCaretColor(this.caretColor);
/* 189 */     textArea.setUseSelectedTextColor(this.useSelectionFG);
/* 190 */     textArea.setSelectedTextColor(this.selectionFG);
/* 191 */     textArea.setSelectionColor(this.selectionBG);
/* 192 */     textArea.setRoundedSelectionEdges(this.selectionRoundedEdges);
/* 193 */     textArea.setCurrentLineHighlightColor(this.currentLineHighlight);
/* 194 */     textArea.setFadeCurrentLineHighlight(this.fadeCurrentLineHighlight);
/* 195 */     textArea.setTabLineColor(this.tabLineColor);
/* 196 */     textArea.setMarginLineColor(this.marginLineColor);
/* 197 */     textArea.setMarkAllHighlightColor(this.markAllHighlightColor);
/* 198 */     textArea.setMarkOccurrencesColor(this.markOccurrencesColor);
/* 199 */     textArea.setPaintMarkOccurrencesBorder(this.markOccurrencesBorder);
/* 200 */     textArea.setMatchedBracketBGColor(this.matchedBracketBG);
/* 201 */     textArea.setMatchedBracketBorderColor(this.matchedBracketFG);
/* 202 */     textArea.setPaintMatchedBracketPair(this.matchedBracketHighlightBoth);
/* 203 */     textArea.setAnimateBracketMatching(this.matchedBracketAnimate);
/* 204 */     textArea.setHyperlinkForeground(this.hyperlinkFG);
/*     */     
/* 206 */     int count = this.secondaryLanguages.length;
/* 207 */     for (int i = 0; i < count; i++) {
/* 208 */       textArea.setSecondaryLanguageBackground(i + 1, this.secondaryLanguages[i]);
/*     */     }
/*     */     
/* 211 */     textArea.setSyntaxScheme(this.scheme);
/*     */     
/* 213 */     Gutter gutter = RSyntaxUtilities.getGutter(textArea);
/* 214 */     if (gutter != null) {
/* 215 */       gutter.setBackground(this.gutterBackgroundColor);
/* 216 */       gutter.setBorderColor(this.gutterBorderColor);
/* 217 */       gutter.setActiveLineRangeColor(this.activeLineRangeColor);
/* 218 */       gutter.setIconRowHeaderInheritsGutterBackground(this.iconRowHeaderInheritsGutterBG);
/* 219 */       gutter.setLineNumberColor(this.lineNumberColor);
/*     */       
/* 221 */       String fontName = (this.lineNumberFont != null) ? this.lineNumberFont : this.baseFont.getFamily();
/*     */       
/* 223 */       int fontSize = (this.lineNumberFontSize > 0) ? this.lineNumberFontSize : this.baseFont.getSize();
/* 224 */       Font font = getFont(fontName, 0, fontSize);
/* 225 */       gutter.setLineNumberFont(font);
/* 226 */       gutter.setFoldIndicatorForeground(this.foldIndicatorFG);
/* 227 */       gutter.setFoldBackground(this.foldBG);
/* 228 */       gutter.setArmedFoldBackground(this.armedFoldBG);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String colorToString(Color c) {
/* 235 */     int color = c.getRGB() & 0xFFFFFF;
/* 236 */     StringBuilder stringBuilder = new StringBuilder(Integer.toHexString(color));
/* 237 */     while (stringBuilder.length() < 6) {
/* 238 */       stringBuilder.insert(0, "0");
/*     */     }
/* 240 */     return stringBuilder.toString();
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
/*     */   private static Color getDefaultBG() {
/* 252 */     Color c = UIManager.getColor("nimbusLightBackground");
/* 253 */     if (c == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 258 */       c = UIManager.getColor("TextArea.background");
/* 259 */       if (c == null) {
/* 260 */         c = new ColorUIResource(SystemColor.text);
/*     */       }
/*     */     } 
/* 263 */     return c;
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
/*     */   private static Color getDefaultSelectionBG() {
/* 275 */     Color c = UIManager.getColor("TextArea.selectionBackground");
/* 276 */     if (c == null) {
/* 277 */       c = UIManager.getColor("textHighlight");
/* 278 */       if (c == null) {
/* 279 */         c = UIManager.getColor("nimbusSelectionBackground");
/* 280 */         if (c == null) {
/* 281 */           c = new ColorUIResource(SystemColor.textHighlight);
/*     */         }
/*     */       } 
/*     */     } 
/* 285 */     return c;
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
/*     */   private static Color getDefaultSelectionFG() {
/* 297 */     Color c = UIManager.getColor("TextArea.selectionForeground");
/* 298 */     if (c == null) {
/* 299 */       c = UIManager.getColor("textHighlightText");
/* 300 */       if (c == null) {
/* 301 */         c = UIManager.getColor("nimbusSelectedText");
/* 302 */         if (c == null) {
/* 303 */           c = new ColorUIResource(SystemColor.textHighlightText);
/*     */         }
/*     */       } 
/*     */     } 
/* 307 */     return c;
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
/*     */   private static Font getFont(String family, int style, int size) {
/* 321 */     StyleContext sc = StyleContext.getDefaultStyleContext();
/* 322 */     return sc.getFont(family, style, size);
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
/*     */   public static Theme load(InputStream in) throws IOException {
/* 336 */     return load(in, null);
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
/*     */   public static Theme load(InputStream in, Font baseFont) throws IOException {
/* 354 */     Theme theme = new Theme(baseFont);
/*     */     
/* 356 */     try (BufferedInputStream bin = new BufferedInputStream(in)) {
/* 357 */       XmlHandler.load(theme, bin);
/*     */     } 
/*     */     
/* 360 */     return theme;
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
/*     */   public void save(OutputStream out) throws IOException {
/* 373 */     try (BufferedOutputStream bout = new BufferedOutputStream(out)) {
/*     */ 
/*     */       
/* 376 */       DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
/* 377 */       DOMImplementation impl = db.getDOMImplementation();
/*     */       
/* 379 */       Document doc = impl.createDocument(null, "RSyntaxTheme", null);
/* 380 */       Element root = doc.getDocumentElement();
/* 381 */       root.setAttribute("version", "1.0");
/*     */       
/* 383 */       Element elem = doc.createElement("baseFont");
/* 384 */       if (!this.baseFont.getFamily().equals(
/* 385 */           RSyntaxTextArea.getDefaultFont().getFamily())) {
/* 386 */         elem.setAttribute("family", this.baseFont.getFamily());
/*     */       }
/* 388 */       elem.setAttribute("size", Integer.toString(this.baseFont.getSize()));
/* 389 */       root.appendChild(elem);
/*     */       
/* 391 */       elem = doc.createElement("background");
/* 392 */       elem.setAttribute("color", colorToString(this.bgColor));
/* 393 */       root.appendChild(elem);
/*     */       
/* 395 */       elem = doc.createElement("caret");
/* 396 */       elem.setAttribute("color", colorToString(this.caretColor));
/* 397 */       root.appendChild(elem);
/*     */       
/* 399 */       elem = doc.createElement("selection");
/* 400 */       elem.setAttribute("useFG", Boolean.toString(this.useSelectionFG));
/* 401 */       elem.setAttribute("fg", colorToString(this.selectionFG));
/* 402 */       elem.setAttribute("bg", colorToString(this.selectionBG));
/* 403 */       elem.setAttribute("roundedEdges", Boolean.toString(this.selectionRoundedEdges));
/* 404 */       root.appendChild(elem);
/*     */       
/* 406 */       elem = doc.createElement("currentLineHighlight");
/* 407 */       elem.setAttribute("color", colorToString(this.currentLineHighlight));
/* 408 */       elem.setAttribute("fade", Boolean.toString(this.fadeCurrentLineHighlight));
/* 409 */       root.appendChild(elem);
/*     */       
/* 411 */       elem = doc.createElement("tabLine");
/* 412 */       elem.setAttribute("color", colorToString(this.tabLineColor));
/* 413 */       root.appendChild(elem);
/*     */       
/* 415 */       elem = doc.createElement("marginLine");
/* 416 */       elem.setAttribute("fg", colorToString(this.marginLineColor));
/* 417 */       root.appendChild(elem);
/*     */       
/* 419 */       elem = doc.createElement("markAllHighlight");
/* 420 */       elem.setAttribute("color", colorToString(this.markAllHighlightColor));
/* 421 */       root.appendChild(elem);
/*     */       
/* 423 */       elem = doc.createElement("markOccurrencesHighlight");
/* 424 */       elem.setAttribute("color", colorToString(this.markOccurrencesColor));
/* 425 */       elem.setAttribute("border", Boolean.toString(this.markOccurrencesBorder));
/* 426 */       root.appendChild(elem);
/*     */       
/* 428 */       elem = doc.createElement("matchedBracket");
/* 429 */       elem.setAttribute("fg", colorToString(this.matchedBracketFG));
/* 430 */       elem.setAttribute("bg", colorToString(this.matchedBracketBG));
/* 431 */       elem.setAttribute("highlightBoth", Boolean.toString(this.matchedBracketHighlightBoth));
/* 432 */       elem.setAttribute("animate", Boolean.toString(this.matchedBracketAnimate));
/* 433 */       root.appendChild(elem);
/*     */       
/* 435 */       elem = doc.createElement("hyperlinks");
/* 436 */       elem.setAttribute("fg", colorToString(this.hyperlinkFG));
/* 437 */       root.appendChild(elem);
/*     */       
/* 439 */       elem = doc.createElement("secondaryLanguages");
/* 440 */       for (int i = 0; i < this.secondaryLanguages.length; i++) {
/* 441 */         Color color = this.secondaryLanguages[i];
/* 442 */         Element elem2 = doc.createElement("language");
/* 443 */         elem2.setAttribute("index", Integer.toString(i + 1));
/* 444 */         elem2.setAttribute("bg", (color == null) ? "" : colorToString(color));
/* 445 */         elem.appendChild(elem2);
/*     */       } 
/* 447 */       root.appendChild(elem);
/*     */       
/* 449 */       elem = doc.createElement("gutterBackground");
/* 450 */       elem.setAttribute("color", colorToString(this.gutterBackgroundColor));
/* 451 */       root.appendChild(elem);
/*     */       
/* 453 */       elem = doc.createElement("gutterBorder");
/* 454 */       elem.setAttribute("color", colorToString(this.gutterBorderColor));
/* 455 */       root.appendChild(elem);
/*     */       
/* 457 */       elem = doc.createElement("lineNumbers");
/* 458 */       elem.setAttribute("fg", colorToString(this.lineNumberColor));
/* 459 */       if (this.lineNumberFont != null) {
/* 460 */         elem.setAttribute("fontFamily", this.lineNumberFont);
/*     */       }
/* 462 */       if (this.lineNumberFontSize > 0) {
/* 463 */         elem.setAttribute("fontSize", 
/* 464 */             Integer.toString(this.lineNumberFontSize));
/*     */       }
/* 466 */       root.appendChild(elem);
/*     */       
/* 468 */       elem = doc.createElement("foldIndicator");
/* 469 */       elem.setAttribute("fg", colorToString(this.foldIndicatorFG));
/* 470 */       elem.setAttribute("iconBg", colorToString(this.foldBG));
/* 471 */       elem.setAttribute("iconArmedBg", colorToString(this.armedFoldBG));
/* 472 */       root.appendChild(elem);
/*     */       
/* 474 */       elem = doc.createElement("iconRowHeader");
/* 475 */       elem.setAttribute("activeLineRange", colorToString(this.activeLineRangeColor));
/* 476 */       elem.setAttribute("inheritsGutterBG", Boolean.toString(this.iconRowHeaderInheritsGutterBG));
/* 477 */       root.appendChild(elem);
/*     */       
/* 479 */       elem = doc.createElement("tokenStyles");
/* 480 */       Field[] fields = TokenTypes.class.getFields();
/* 481 */       for (Field field : fields) {
/* 482 */         int value = field.getInt(null);
/* 483 */         if (value != 39) {
/* 484 */           Style style = this.scheme.getStyle(value);
/* 485 */           if (style != null) {
/* 486 */             Element elem2 = doc.createElement("style");
/* 487 */             elem2.setAttribute("token", field.getName());
/* 488 */             Color fg = style.foreground;
/* 489 */             if (fg != null) {
/* 490 */               elem2.setAttribute("fg", colorToString(fg));
/*     */             }
/* 492 */             Color bg = style.background;
/* 493 */             if (bg != null) {
/* 494 */               elem2.setAttribute("bg", colorToString(bg));
/*     */             }
/* 496 */             Font font = style.font;
/* 497 */             if (font != null) {
/* 498 */               if (!font.getFamily().equals(this.baseFont
/* 499 */                   .getFamily())) {
/* 500 */                 elem2.setAttribute("fontFamily", font.getFamily());
/*     */               }
/* 502 */               if (font.getSize() != this.baseFont.getSize()) {
/* 503 */                 elem2.setAttribute("fontSize", Integer.toString(font.getSize()));
/*     */               }
/* 505 */               if (font.isBold()) {
/* 506 */                 elem2.setAttribute("bold", "true");
/*     */               }
/* 508 */               if (font.isItalic()) {
/* 509 */                 elem2.setAttribute("italic", "true");
/*     */               }
/*     */             } 
/* 512 */             if (style.underline) {
/* 513 */               elem2.setAttribute("underline", "true");
/*     */             }
/* 515 */             elem.appendChild(elem2);
/*     */           } 
/*     */         } 
/*     */       } 
/* 519 */       root.appendChild(elem);
/*     */       
/* 521 */       DOMSource source = new DOMSource(doc);
/*     */ 
/*     */       
/* 524 */       StreamResult result = new StreamResult(new PrintWriter((Writer)new UnicodeWriter(bout, "UTF-8")));
/*     */       
/* 526 */       TransformerFactory transFac = TransformerFactory.newInstance();
/* 527 */       Transformer transformer = transFac.newTransformer();
/* 528 */       transformer.setOutputProperty("indent", "yes");
/* 529 */       transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
/* 530 */       transformer.setOutputProperty("encoding", "UTF-8");
/* 531 */       transformer.setOutputProperty("doctype-system", "theme.dtd");
/* 532 */       transformer.transform(source, result);
/*     */     }
/* 534 */     catch (RuntimeException re) {
/* 535 */       throw re;
/* 536 */     } catch (Exception e) {
/* 537 */       throw new IOException("Error generating XML: " + e.getMessage(), e);
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
/*     */   private static Color stringToColor(String s) {
/* 557 */     return stringToColor(s, null);
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
/*     */   private static Color stringToColor(String s, Color defaultVal) {
/* 577 */     if (s == null || "default".equalsIgnoreCase(s)) {
/* 578 */       return defaultVal;
/*     */     }
/* 580 */     if (s.length() == 6 || s.length() == 7) {
/* 581 */       if (s.charAt(0) == '$') {
/* 582 */         s = s.substring(1);
/*     */       }
/* 584 */       return new Color(Integer.parseInt(s, 16));
/*     */     } 
/* 586 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class XmlHandler
/*     */     extends DefaultHandler
/*     */   {
/*     */     private Theme theme;
/*     */ 
/*     */ 
/*     */     
/*     */     public void error(SAXParseException e) throws SAXException {
/* 599 */       throw e;
/*     */     }
/*     */ 
/*     */     
/*     */     public void fatalError(SAXParseException e) throws SAXException {
/* 604 */       throw e;
/*     */     }
/*     */     
/*     */     public static void load(Theme theme, InputStream in) throws IOException {
/* 608 */       SAXParserFactory spf = SAXParserFactory.newInstance();
/* 609 */       spf.setValidating(true);
/*     */       try {
/* 611 */         SAXParser parser = spf.newSAXParser();
/* 612 */         XMLReader reader = parser.getXMLReader();
/* 613 */         XmlHandler handler = new XmlHandler();
/* 614 */         handler.theme = theme;
/* 615 */         reader.setEntityResolver(handler);
/* 616 */         reader.setContentHandler(handler);
/* 617 */         reader.setDTDHandler(handler);
/* 618 */         reader.setErrorHandler(handler);
/* 619 */         InputSource is = new InputSource(in);
/* 620 */         is.setEncoding("UTF-8");
/* 621 */         reader.parse(is);
/* 622 */       } catch (Exception se) {
/* 623 */         throw new IOException(se.toString());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private static int parseInt(Attributes attrs, String attr, int def) {
/* 629 */       int value = def;
/* 630 */       String temp = attrs.getValue(attr);
/* 631 */       if (temp != null) {
/*     */         try {
/* 633 */           value = Integer.parseInt(temp);
/* 634 */         } catch (NumberFormatException nfe) {
/* 635 */           nfe.printStackTrace();
/*     */         } 
/*     */       }
/* 638 */       return value;
/*     */     }
/*     */ 
/*     */     
/*     */     public InputSource resolveEntity(String publicID, String systemID) {
/* 643 */       return new InputSource(getClass()
/* 644 */           .getResourceAsStream("themes/theme.dtd"));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void startElement(String uri, String localName, String qName, Attributes attrs) {
/* 651 */       if ("background".equals(qName)) {
/*     */         
/* 653 */         String color = attrs.getValue("color");
/* 654 */         if (color != null) {
/* 655 */           this.theme.bgColor = Theme.stringToColor(color, Theme.getDefaultBG());
/* 656 */           this.theme.gutterBackgroundColor = this.theme.bgColor;
/*     */         } else {
/*     */           
/* 659 */           String img = attrs.getValue("image");
/* 660 */           if (img != null) {
/* 661 */             throw new IllegalArgumentException("Not yet implemented");
/*     */           
/*     */           }
/*     */         }
/*     */       
/*     */       }
/* 667 */       else if ("baseFont".equals(qName)) {
/* 668 */         int size = this.theme.baseFont.getSize();
/* 669 */         String sizeStr = attrs.getValue("size");
/* 670 */         if (sizeStr != null) {
/* 671 */           size = Integer.parseInt(sizeStr);
/*     */         }
/* 673 */         String family = attrs.getValue("family");
/* 674 */         if (family != null) {
/* 675 */           this.theme.baseFont = Theme.getFont(family, 0, size);
/*     */         }
/* 677 */         else if (sizeStr != null) {
/*     */           
/* 679 */           this.theme.baseFont = this.theme.baseFont.deriveFont(size * 1.0F);
/*     */         }
/*     */       
/*     */       }
/* 683 */       else if ("caret".equals(qName)) {
/* 684 */         String color = attrs.getValue("color");
/* 685 */         this.theme.caretColor = Theme.stringToColor(color);
/*     */       
/*     */       }
/* 688 */       else if ("currentLineHighlight".equals(qName)) {
/* 689 */         String color = attrs.getValue("color");
/* 690 */         this.theme.currentLineHighlight = Theme.stringToColor(color);
/* 691 */         String fadeStr = attrs.getValue("fade");
/* 692 */         boolean fade = Boolean.parseBoolean(fadeStr);
/* 693 */         this.theme.fadeCurrentLineHighlight = fade;
/*     */       
/*     */       }
/* 696 */       else if ("tabLine".equals(qName)) {
/* 697 */         String color = attrs.getValue("color");
/* 698 */         this.theme.tabLineColor = Theme.stringToColor(color);
/*     */       
/*     */       }
/* 701 */       else if ("foldIndicator".equals(qName)) {
/* 702 */         String color = attrs.getValue("fg");
/* 703 */         this.theme.foldIndicatorFG = Theme.stringToColor(color);
/* 704 */         color = attrs.getValue("iconBg");
/* 705 */         this.theme.foldBG = Theme.stringToColor(color);
/* 706 */         color = attrs.getValue("iconArmedBg");
/*     */ 
/*     */ 
/*     */         
/* 710 */         this.theme.armedFoldBG = Theme.stringToColor(color, this.theme.foldBG);
/*     */       
/*     */       }
/* 713 */       else if ("gutterBackground".equals(qName)) {
/* 714 */         String color = attrs.getValue("color");
/* 715 */         if (color != null) {
/* 716 */           this.theme.gutterBackgroundColor = Theme.stringToColor(color);
/*     */         
/*     */         }
/*     */       }
/* 720 */       else if ("gutterBorder".equals(qName)) {
/* 721 */         String color = attrs.getValue("color");
/* 722 */         this.theme.gutterBorderColor = Theme.stringToColor(color);
/*     */       
/*     */       }
/* 725 */       else if ("iconRowHeader".equals(qName)) {
/* 726 */         String color = attrs.getValue("activeLineRange");
/* 727 */         this.theme.activeLineRangeColor = Theme.stringToColor(color);
/* 728 */         String inheritBGStr = attrs.getValue("inheritsGutterBG");
/* 729 */         this.theme
/* 730 */           .iconRowHeaderInheritsGutterBG = Boolean.parseBoolean(inheritBGStr);
/*     */       
/*     */       }
/* 733 */       else if ("lineNumbers".equals(qName)) {
/* 734 */         String color = attrs.getValue("fg");
/* 735 */         this.theme.lineNumberColor = Theme.stringToColor(color);
/* 736 */         this.theme.lineNumberFont = attrs.getValue("fontFamily");
/* 737 */         this.theme.lineNumberFontSize = parseInt(attrs, "fontSize", -1);
/*     */       
/*     */       }
/* 740 */       else if ("marginLine".equals(qName)) {
/* 741 */         String color = attrs.getValue("fg");
/* 742 */         this.theme.marginLineColor = Theme.stringToColor(color);
/*     */       
/*     */       }
/* 745 */       else if ("markAllHighlight".equals(qName)) {
/* 746 */         String color = attrs.getValue("color");
/* 747 */         this.theme.markAllHighlightColor = Theme.stringToColor(color);
/*     */       
/*     */       }
/* 750 */       else if ("markOccurrencesHighlight".equals(qName)) {
/* 751 */         String color = attrs.getValue("color");
/* 752 */         this.theme.markOccurrencesColor = Theme.stringToColor(color);
/* 753 */         String border = attrs.getValue("border");
/* 754 */         this.theme.markOccurrencesBorder = Boolean.parseBoolean(border);
/*     */       
/*     */       }
/* 757 */       else if ("matchedBracket".equals(qName)) {
/* 758 */         String fg = attrs.getValue("fg");
/* 759 */         this.theme.matchedBracketFG = Theme.stringToColor(fg);
/* 760 */         String bg = attrs.getValue("bg");
/* 761 */         this.theme.matchedBracketBG = Theme.stringToColor(bg);
/* 762 */         String highlightBoth = attrs.getValue("highlightBoth");
/* 763 */         this.theme.matchedBracketHighlightBoth = Boolean.parseBoolean(highlightBoth);
/* 764 */         String animate = attrs.getValue("animate");
/* 765 */         this.theme.matchedBracketAnimate = Boolean.parseBoolean(animate);
/*     */       
/*     */       }
/* 768 */       else if ("hyperlinks".equals(qName)) {
/* 769 */         String fg = attrs.getValue("fg");
/* 770 */         this.theme.hyperlinkFG = Theme.stringToColor(fg);
/*     */       
/*     */       }
/* 773 */       else if ("language".equals(qName)) {
/* 774 */         String indexStr = attrs.getValue("index");
/* 775 */         int index = Integer.parseInt(indexStr) - 1;
/* 776 */         if (this.theme.secondaryLanguages.length > index) {
/* 777 */           Color bg = Theme.stringToColor(attrs.getValue("bg"));
/* 778 */           this.theme.secondaryLanguages[index] = bg;
/*     */         }
/*     */       
/*     */       }
/* 782 */       else if ("selection".equals(qName)) {
/* 783 */         String useStr = attrs.getValue("useFG");
/* 784 */         this.theme.useSelectionFG = Boolean.parseBoolean(useStr);
/* 785 */         String color = attrs.getValue("fg");
/* 786 */         this.theme.selectionFG = Theme.stringToColor(color, Theme
/* 787 */             .getDefaultSelectionFG());
/*     */         
/* 789 */         color = attrs.getValue("bg");
/* 790 */         this.theme.selectionBG = Theme.stringToColor(color, Theme
/* 791 */             .getDefaultSelectionBG());
/* 792 */         String roundedStr = attrs.getValue("roundedEdges");
/* 793 */         this.theme.selectionRoundedEdges = Boolean.parseBoolean(roundedStr);
/*     */ 
/*     */       
/*     */       }
/* 797 */       else if ("tokenStyles".equals(qName)) {
/* 798 */         this.theme.scheme = new SyntaxScheme(this.theme.baseFont, false);
/*     */ 
/*     */       
/*     */       }
/* 802 */       else if ("style".equals(qName)) {
/*     */         
/* 804 */         String type = attrs.getValue("token");
/* 805 */         Field field = null;
/*     */         try {
/* 807 */           field = Token.class.getField(type);
/* 808 */         } catch (RuntimeException re) {
/* 809 */           throw re;
/* 810 */         } catch (Exception e) {
/* 811 */           System.err.println("Invalid token type: " + type);
/*     */           
/*     */           return;
/*     */         } 
/* 815 */         if (field.getType() == int.class) {
/*     */           
/* 817 */           int index = 0;
/*     */           try {
/* 819 */             index = field.getInt(this.theme.scheme);
/* 820 */           } catch (IllegalArgumentException|IllegalAccessException e) {
/* 821 */             e.printStackTrace();
/*     */             
/*     */             return;
/*     */           } 
/* 825 */           String fgStr = attrs.getValue("fg");
/* 826 */           Color fg = Theme.stringToColor(fgStr);
/* 827 */           (this.theme.scheme.getStyle(index)).foreground = fg;
/*     */           
/* 829 */           String bgStr = attrs.getValue("bg");
/* 830 */           Color bg = Theme.stringToColor(bgStr);
/* 831 */           (this.theme.scheme.getStyle(index)).background = bg;
/*     */           
/* 833 */           Font font = this.theme.baseFont;
/* 834 */           String familyName = attrs.getValue("fontFamily");
/* 835 */           if (familyName != null) {
/* 836 */             font = Theme.getFont(familyName, font.getStyle(), font
/* 837 */                 .getSize());
/*     */           }
/* 839 */           String sizeStr = attrs.getValue("fontSize");
/* 840 */           if (sizeStr != null) {
/*     */             try {
/* 842 */               float size = Float.parseFloat(sizeStr);
/* 843 */               size = Math.max(size, 1.0F);
/* 844 */               font = font.deriveFont(size);
/* 845 */             } catch (NumberFormatException nfe) {
/* 846 */               nfe.printStackTrace();
/*     */             } 
/*     */           }
/* 849 */           (this.theme.scheme.getStyle(index)).font = font;
/*     */           
/* 851 */           boolean styleSpecified = false;
/* 852 */           boolean bold = false;
/* 853 */           boolean italic = false;
/* 854 */           String boldStr = attrs.getValue("bold");
/* 855 */           if (boldStr != null) {
/* 856 */             bold = Boolean.parseBoolean(boldStr);
/* 857 */             styleSpecified = true;
/*     */           } 
/* 859 */           String italicStr = attrs.getValue("italic");
/* 860 */           if (italicStr != null) {
/* 861 */             italic = Boolean.parseBoolean(italicStr);
/* 862 */             styleSpecified = true;
/*     */           } 
/* 864 */           if (styleSpecified) {
/* 865 */             int style = 0;
/* 866 */             if (bold) {
/* 867 */               style |= 0x1;
/*     */             }
/* 869 */             if (italic) {
/* 870 */               style |= 0x2;
/*     */             }
/* 872 */             Font orig = (this.theme.scheme.getStyle(index)).font;
/* 873 */             (this.theme.scheme.getStyle(index))
/* 874 */               .font = orig.deriveFont(style);
/*     */           } 
/*     */           
/* 877 */           String ulineStr = attrs.getValue("underline");
/* 878 */           if (ulineStr != null) {
/* 879 */             boolean uline = Boolean.parseBoolean(ulineStr);
/* 880 */             (this.theme.scheme.getStyle(index)).underline = uline;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void warning(SAXParseException e) throws SAXException {
/* 891 */       throw e;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\Theme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */