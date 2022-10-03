/*     */ package org.fife.rsta.ac.css;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Segment;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.fife.ui.autocomplete.AbstractCompletionProvider;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.CompletionProviderBase;
/*     */ import org.fife.ui.autocomplete.CompletionXMLParser;
/*     */ import org.fife.ui.autocomplete.ParameterizedCompletion;
/*     */ import org.fife.ui.autocomplete.Util;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.Token;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public class PropertyValueCompletionProvider
/*     */   extends CompletionProviderBase
/*     */ {
/*     */   private List<Completion> htmlTagCompletions;
/*     */   private List<Completion> propertyCompletions;
/*     */   private Map<String, List<Completion>> valueCompletions;
/*     */   private Map<String, List<CompletionGenerator>> valueCompletionGenerators;
/*  64 */   private Segment seg = new Segment();
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractCompletionProvider.CaseInsensitiveComparator comparator;
/*     */ 
/*     */ 
/*     */   
/*     */   private String currentProperty;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isLess;
/*     */ 
/*     */ 
/*     */   
/*  80 */   private static final Pattern VENDOR_PREFIXES = Pattern.compile("^\\-(?:ms|moz|o|xv|webkit|khtml|apple)\\-");
/*     */   
/*  82 */   private final Completion INHERIT_COMPLETION = (Completion)new BasicCssCompletion((CompletionProvider)this, "inherit", "css_propertyvalue_identifier");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyValueCompletionProvider(boolean isLess) {
/*  88 */     setAutoActivationRules(true, "@: ");
/*     */     
/*  90 */     setParameterizedCompletionParams('(', ", ", ')');
/*  91 */     this.isLess = isLess;
/*     */     
/*     */     try {
/*  94 */       this.valueCompletions = new HashMap<>();
/*  95 */       this.valueCompletionGenerators = new HashMap<>();
/*     */       
/*  97 */       loadPropertyCompletions();
/*  98 */       this.htmlTagCompletions = loadHtmlTagCompletions();
/*  99 */     } catch (IOException ioe) {
/* 100 */       throw new RuntimeException(ioe);
/*     */     } 
/*     */     
/* 103 */     this.comparator = new AbstractCompletionProvider.CaseInsensitiveComparator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addAtRuleCompletions(List<Completion> completions) {
/* 109 */     completions.add(new BasicCssCompletion((CompletionProvider)this, "@charset", "charset_rule"));
/* 110 */     completions.add(new BasicCssCompletion((CompletionProvider)this, "@import", "link_rule"));
/* 111 */     completions.add(new BasicCssCompletion((CompletionProvider)this, "@namespace", "charset_rule"));
/* 112 */     completions.add(new BasicCssCompletion((CompletionProvider)this, "@media", "media_rule"));
/* 113 */     completions.add(new BasicCssCompletion((CompletionProvider)this, "@page", "page_rule"));
/* 114 */     completions.add(new BasicCssCompletion((CompletionProvider)this, "@font-face", "fontface_rule"));
/* 115 */     completions.add(new BasicCssCompletion((CompletionProvider)this, "@keyframes", "charset_rule"));
/* 116 */     completions.add(new BasicCssCompletion((CompletionProvider)this, "@supports", "charset_rule"));
/* 117 */     completions.add(new BasicCssCompletion((CompletionProvider)this, "@document", "charset_rule"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlreadyEnteredText(JTextComponent comp) {
/* 124 */     Document doc = comp.getDocument();
/*     */     
/* 126 */     int dot = comp.getCaretPosition();
/* 127 */     Element root = doc.getDefaultRootElement();
/* 128 */     int index = root.getElementIndex(dot);
/* 129 */     Element elem = root.getElement(index);
/* 130 */     int start = elem.getStartOffset();
/* 131 */     int len = dot - start;
/*     */     try {
/* 133 */       doc.getText(start, len, this.seg);
/* 134 */     } catch (BadLocationException ble) {
/* 135 */       ble.printStackTrace();
/* 136 */       return "";
/*     */     } 
/*     */     
/* 139 */     int segEnd = this.seg.offset + len;
/* 140 */     start = segEnd - 1;
/* 141 */     while (start >= this.seg.offset && isValidChar(this.seg.array[start])) {
/* 142 */       start--;
/*     */     }
/* 144 */     start++;
/*     */     
/* 146 */     len = segEnd - start;
/* 147 */     if (len == 0) {
/* 148 */       return "";
/*     */     }
/*     */     
/* 151 */     String text = new String(this.seg.array, start, len);
/* 152 */     return removeVendorPrefix(text);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final String removeVendorPrefix(String text) {
/* 157 */     if (text.length() > 0 && text.charAt(0) == '-') {
/* 158 */       Matcher m = VENDOR_PREFIXES.matcher(text);
/* 159 */       if (m.find()) {
/* 160 */         text = text.substring(m.group().length());
/*     */       }
/*     */     } 
/* 163 */     return text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Completion> getCompletionsAt(JTextComponent comp, Point p) {
/* 170 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ParameterizedCompletion> getParameterizedCompletions(JTextComponent tc) {
/* 177 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private LexerState getLexerState(RSyntaxTextArea textArea, int line) {
/* 183 */     int dot = textArea.getCaretPosition();
/* 184 */     LexerState state = LexerState.SELECTOR;
/* 185 */     boolean somethingFound = false;
/* 186 */     this.currentProperty = null;
/*     */     
/* 188 */     while (line >= 0 && !somethingFound) {
/* 189 */       Token t = textArea.getTokenListForLine(line--);
/* 190 */       while (t != null && t.isPaintable() && !t.containsPosition(dot)) {
/* 191 */         if (t.getType() == 6) {
/* 192 */           state = LexerState.PROPERTY;
/* 193 */           this.currentProperty = removeVendorPrefix(t.getLexeme());
/* 194 */           somethingFound = true;
/*     */         }
/* 196 */         else if (!this.isLess && t.getType() == 17) {
/*     */           
/* 198 */           state = LexerState.SELECTOR;
/* 199 */           this.currentProperty = null;
/* 200 */           somethingFound = true;
/*     */         }
/* 202 */         else if (t.getType() == 24 || t
/* 203 */           .getType() == 8 || t
/* 204 */           .getType() == 10) {
/* 205 */           state = LexerState.VALUE;
/* 206 */           somethingFound = true;
/*     */         }
/* 208 */         else if (t.isLeftCurly()) {
/* 209 */           state = LexerState.PROPERTY;
/* 210 */           somethingFound = true;
/*     */         }
/* 212 */         else if (t.isRightCurly()) {
/* 213 */           state = LexerState.SELECTOR;
/* 214 */           this.currentProperty = null;
/* 215 */           somethingFound = true;
/*     */         }
/* 217 */         else if (t.isSingleChar(23, ':')) {
/* 218 */           state = LexerState.VALUE;
/* 219 */           somethingFound = true;
/*     */         }
/* 221 */         else if (t.isSingleChar(23, ';')) {
/* 222 */           state = LexerState.PROPERTY;
/* 223 */           this.currentProperty = null;
/* 224 */           somethingFound = true;
/*     */         } 
/* 226 */         t = t.getNextToken();
/*     */       } 
/*     */     } 
/*     */     
/* 230 */     return state;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Completion> getCompletionsImpl(JTextComponent comp) {
/* 239 */     List<Completion> retVal = new ArrayList<>();
/* 240 */     String text = getAlreadyEnteredText(comp);
/*     */     
/* 242 */     if (text != null) {
/*     */       List<CompletionGenerator> generators;
/*     */       
/* 245 */       RSyntaxTextArea textArea = (RSyntaxTextArea)comp;
/* 246 */       LexerState lexerState = getLexerState(textArea, textArea
/* 247 */           .getCaretLineNumber());
/*     */       
/* 249 */       List<Completion> choices = new ArrayList<>();
/* 250 */       switch (lexerState) {
/*     */         case SELECTOR:
/* 252 */           choices = this.htmlTagCompletions;
/*     */           break;
/*     */         case PROPERTY:
/* 255 */           choices = this.propertyCompletions;
/*     */           break;
/*     */         case VALUE:
/* 258 */           choices = this.valueCompletions.get(this.currentProperty);
/*     */           
/* 260 */           generators = this.valueCompletionGenerators.get(this.currentProperty);
/* 261 */           if (generators != null) {
/* 262 */             for (CompletionGenerator generator : generators) {
/*     */               
/* 264 */               List<Completion> toMerge = generator.generate((CompletionProvider)this, text);
/* 265 */               if (toMerge != null) {
/* 266 */                 if (choices == null) {
/* 267 */                   choices = toMerge;
/*     */ 
/*     */                   
/*     */                   continue;
/*     */                 } 
/*     */                 
/* 273 */                 choices = new ArrayList<>(choices);
/* 274 */                 choices.addAll(toMerge);
/*     */               } 
/*     */             } 
/*     */           }
/*     */           
/* 279 */           if (choices == null) {
/* 280 */             choices = new ArrayList<>();
/*     */           }
/* 282 */           Collections.sort(choices);
/*     */           break;
/*     */       } 
/*     */       
/* 286 */       if (this.isLess && 
/* 287 */         addLessCompletions(choices, lexerState, comp, text)) {
/* 288 */         Collections.sort(choices);
/*     */       }
/*     */ 
/*     */       
/* 292 */       int index = Collections.binarySearch(choices, text, (Comparator<?>)this.comparator);
/* 293 */       if (index < 0) {
/* 294 */         index = -index - 1;
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 301 */         int pos = index - 1;
/* 302 */         while (pos > 0 && this.comparator
/* 303 */           .compare(choices.get(pos), text) == 0) {
/* 304 */           retVal.add(choices.get(pos));
/* 305 */           pos--;
/*     */         } 
/*     */       } 
/*     */       
/* 309 */       while (index < choices.size()) {
/* 310 */         Completion c = choices.get(index);
/* 311 */         if (Util.startsWithIgnoreCase(c.getInputText(), text)) {
/* 312 */           retVal.add(c);
/* 313 */           index++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 322 */     return retVal;
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
/*     */   protected boolean addLessCompletions(List<Completion> completions, LexerState state, JTextComponent comp, String alreadyEntered) {
/* 339 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoActivateOkay(JTextComponent tc) {
/* 349 */     boolean ok = super.isAutoActivateOkay(tc);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 355 */     if (ok) {
/* 356 */       RSyntaxDocument doc = (RSyntaxDocument)tc.getDocument();
/* 357 */       int dot = tc.getCaretPosition();
/*     */       try {
/* 359 */         if (dot > 1 && doc.charAt(dot) == ' ') {
/* 360 */           ok = (doc.charAt(dot - 1) == ':');
/*     */         }
/* 362 */       } catch (BadLocationException ble) {
/* 363 */         ble.printStackTrace();
/*     */       } 
/*     */     } 
/*     */     
/* 367 */     return ok;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValidChar(char ch) {
/* 373 */     switch (ch) {
/*     */       case '#':
/*     */       case '-':
/*     */       case '.':
/*     */       case '@':
/*     */       case '_':
/* 379 */         return true;
/*     */     } 
/* 381 */     return Character.isLetterOrDigit(ch);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Completion> loadHtmlTagCompletions() throws IOException {
/* 388 */     List<Completion> completions = loadFromXML("data/html.xml");
/*     */     
/* 390 */     addAtRuleCompletions(completions);
/*     */     
/* 392 */     Collections.sort(completions);
/* 393 */     return completions;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadPropertyCompletions() throws IOException {
/*     */     BufferedReader r;
/* 400 */     this.propertyCompletions = new ArrayList<>();
/*     */ 
/*     */     
/* 403 */     ClassLoader cl = getClass().getClassLoader();
/* 404 */     InputStream in = cl.getResourceAsStream("data/css_properties.txt");
/* 405 */     if (in != null) {
/* 406 */       r = new BufferedReader(new InputStreamReader(in));
/*     */     } else {
/*     */       
/* 409 */       r = new BufferedReader(new FileReader("data/css_properties.txt"));
/*     */     } 
/*     */     
/*     */     String line;
/* 413 */     while ((line = r.readLine()) != null) {
/* 414 */       if (line.length() > 0 && line.charAt(0) != '#') {
/* 415 */         parsePropertyValueCompletionLine(line);
/*     */       }
/*     */     } 
/*     */     
/* 419 */     r.close();
/*     */ 
/*     */     
/* 422 */     Collections.sort(this.propertyCompletions);
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
/*     */   private List<Completion> loadFromXML(InputStream in, ClassLoader cl) throws IOException {
/*     */     List<Completion> completions;
/* 444 */     SAXParserFactory factory = SAXParserFactory.newInstance();
/* 445 */     factory.setValidating(true);
/* 446 */     CompletionXMLParser handler = new CompletionXMLParser((CompletionProvider)this, cl);
/* 447 */     BufferedInputStream bin = new BufferedInputStream(in);
/*     */     try {
/* 449 */       SAXParser saxParser = factory.newSAXParser();
/* 450 */       saxParser.parse(bin, (DefaultHandler)handler);
/* 451 */       completions = handler.getCompletions();
/*     */     }
/* 453 */     catch (SAXException|javax.xml.parsers.ParserConfigurationException e) {
/* 454 */       throw new IOException(e.toString());
/*     */     }
/*     */     finally {
/*     */       
/* 458 */       bin.close();
/*     */     } 
/*     */     
/* 461 */     return completions;
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
/*     */   protected List<Completion> loadFromXML(String resource) throws IOException {
/* 473 */     ClassLoader cl = getClass().getClassLoader();
/* 474 */     InputStream in = cl.getResourceAsStream(resource);
/* 475 */     if (in == null) {
/* 476 */       File file = new File(resource);
/* 477 */       if (file.isFile()) {
/* 478 */         in = new FileInputStream(file);
/*     */       } else {
/*     */         
/* 481 */         throw new IOException("No such resource: " + resource);
/*     */       } 
/*     */     } 
/* 484 */     try (BufferedInputStream bin = new BufferedInputStream(in)) {
/* 485 */       return loadFromXML(bin, (ClassLoader)null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void add(Map<String, List<CompletionGenerator>> generatorMap, String prop, CompletionGenerator generator) {
/* 496 */     List<CompletionGenerator> generators = generatorMap.get(prop);
/* 497 */     if (generators == null) {
/* 498 */       generators = new ArrayList<>();
/* 499 */       generatorMap.put(prop, generators);
/*     */     } 
/* 501 */     generators.add(generator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void parsePropertyValueCompletionLine(String line) {
/* 507 */     String[] tokens = line.split("\\s+");
/* 508 */     String prop = tokens[0];
/* 509 */     String icon = (tokens.length > 1) ? tokens[1] : null;
/* 510 */     this.propertyCompletions.add(new PropertyCompletion((CompletionProvider)this, prop, icon));
/*     */     
/* 512 */     if (tokens.length > 2) {
/*     */       
/* 514 */       List<Completion> completions = new ArrayList<>();
/* 515 */       completions.add(this.INHERIT_COMPLETION);
/*     */ 
/*     */       
/* 518 */       if (tokens[2].equals("[") && tokens[tokens.length - 1]
/* 519 */         .equals("]")) {
/* 520 */         for (int i = 3; i < tokens.length - 1; i++) {
/* 521 */           BasicCssCompletion basicCssCompletion; String token = tokens[i];
/* 522 */           Completion completion = null;
/* 523 */           if ("*length*".equals(token)) {
/* 524 */             add(this.valueCompletionGenerators, prop, new PercentageOrLengthCompletionGenerator(false));
/*     */           
/*     */           }
/* 527 */           else if ("*percentage-or-length*".equals(token)) {
/* 528 */             add(this.valueCompletionGenerators, prop, new PercentageOrLengthCompletionGenerator(true));
/*     */           
/*     */           }
/* 531 */           else if ("*color*".equals(token)) {
/* 532 */             add(this.valueCompletionGenerators, prop, new ColorCompletionGenerator((CompletionProvider)this));
/*     */           
/*     */           }
/* 535 */           else if ("*border-style*".equals(token)) {
/* 536 */             add(this.valueCompletionGenerators, prop, new BorderStyleCompletionGenerator());
/*     */           
/*     */           }
/* 539 */           else if ("*time*".equals(token)) {
/* 540 */             add(this.valueCompletionGenerators, prop, new TimeCompletionGenerator());
/*     */           
/*     */           }
/* 543 */           else if ("*common-fonts*".equals(token)) {
/* 544 */             add(this.valueCompletionGenerators, prop, new CommonFontCompletionGenerator());
/*     */           }
/*     */           else {
/*     */             
/* 548 */             basicCssCompletion = new BasicCssCompletion((CompletionProvider)this, tokens[i], "css_propertyvalue_identifier");
/*     */           } 
/*     */           
/* 551 */           if (basicCssCompletion != null) {
/* 552 */             completions.add(basicCssCompletion);
/*     */           }
/*     */         } 
/*     */       }
/*     */       
/* 557 */       this.valueCompletions.put(prop, completions);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected enum LexerState
/*     */   {
/* 568 */     SELECTOR, PROPERTY, VALUE;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\css\PropertyValueCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */