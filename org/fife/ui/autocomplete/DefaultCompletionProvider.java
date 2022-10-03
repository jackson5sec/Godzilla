/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Segment;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultCompletionProvider
/*     */   extends AbstractCompletionProvider
/*     */ {
/*     */   protected Segment seg;
/*     */   private String lastCompletionsAtText;
/*     */   private List<Completion> lastParameterizedCompletionsAt;
/*     */   
/*     */   public DefaultCompletionProvider() {
/*  63 */     init();
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
/*     */   public DefaultCompletionProvider(String[] words) {
/*  76 */     init();
/*  77 */     addWordCompletions(words);
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
/*     */   public String getAlreadyEnteredText(JTextComponent comp) {
/*  93 */     Document doc = comp.getDocument();
/*     */     
/*  95 */     int dot = comp.getCaretPosition();
/*  96 */     Element root = doc.getDefaultRootElement();
/*  97 */     int index = root.getElementIndex(dot);
/*  98 */     Element elem = root.getElement(index);
/*  99 */     int start = elem.getStartOffset();
/* 100 */     int len = dot - start;
/*     */     try {
/* 102 */       doc.getText(start, len, this.seg);
/* 103 */     } catch (BadLocationException ble) {
/* 104 */       ble.printStackTrace();
/* 105 */       return "";
/*     */     } 
/*     */     
/* 108 */     int segEnd = this.seg.offset + len;
/* 109 */     start = segEnd - 1;
/* 110 */     while (start >= this.seg.offset && isValidChar(this.seg.array[start])) {
/* 111 */       start--;
/*     */     }
/* 113 */     start++;
/*     */     
/* 115 */     len = segEnd - start;
/* 116 */     return (len == 0) ? "" : new String(this.seg.array, start, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Completion> getCompletionsAt(JTextComponent tc, Point p) {
/* 127 */     int offset = tc.viewToModel(p);
/* 128 */     if (offset < 0 || offset >= tc.getDocument().getLength()) {
/* 129 */       this.lastCompletionsAtText = null;
/* 130 */       return this.lastParameterizedCompletionsAt = null;
/*     */     } 
/*     */     
/* 133 */     Segment s = new Segment();
/* 134 */     Document doc = tc.getDocument();
/* 135 */     Element root = doc.getDefaultRootElement();
/* 136 */     int line = root.getElementIndex(offset);
/* 137 */     Element elem = root.getElement(line);
/* 138 */     int start = elem.getStartOffset();
/* 139 */     int end = elem.getEndOffset() - 1;
/*     */ 
/*     */     
/*     */     try {
/* 143 */       doc.getText(start, end - start, s);
/*     */ 
/*     */       
/* 146 */       int startOffs = s.offset + offset - start - 1;
/* 147 */       while (startOffs >= s.offset && isValidChar(s.array[startOffs])) {
/* 148 */         startOffs--;
/*     */       }
/*     */ 
/*     */       
/* 152 */       int endOffs = s.offset + offset - start;
/* 153 */       while (endOffs < s.offset + s.count && isValidChar(s.array[endOffs])) {
/* 154 */         endOffs++;
/*     */       }
/*     */       
/* 157 */       int len = endOffs - startOffs - 1;
/* 158 */       if (len <= 0) {
/* 159 */         return this.lastParameterizedCompletionsAt = null;
/*     */       }
/* 161 */       String text = new String(s.array, startOffs + 1, len);
/*     */       
/* 163 */       if (text.equals(this.lastCompletionsAtText)) {
/* 164 */         return this.lastParameterizedCompletionsAt;
/*     */       }
/*     */ 
/*     */       
/* 168 */       List<Completion> list = getCompletionByInputText(text);
/* 169 */       this.lastCompletionsAtText = text;
/* 170 */       return this.lastParameterizedCompletionsAt = list;
/*     */     }
/* 172 */     catch (BadLocationException ble) {
/* 173 */       ble.printStackTrace();
/*     */ 
/*     */       
/* 176 */       this.lastCompletionsAtText = null;
/* 177 */       return this.lastParameterizedCompletionsAt = null;
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
/*     */   public List<ParameterizedCompletion> getParameterizedCompletions(JTextComponent tc) {
/* 189 */     List<ParameterizedCompletion> list = null;
/*     */ 
/*     */ 
/*     */     
/* 193 */     char paramListStart = getParameterListStart();
/* 194 */     if (paramListStart == '\000') {
/* 195 */       return list;
/*     */     }
/*     */     
/* 198 */     int dot = tc.getCaretPosition();
/* 199 */     Segment s = new Segment();
/* 200 */     Document doc = tc.getDocument();
/* 201 */     Element root = doc.getDefaultRootElement();
/* 202 */     int line = root.getElementIndex(dot);
/* 203 */     Element elem = root.getElement(line);
/* 204 */     int offs = elem.getStartOffset();
/* 205 */     int len = dot - offs - 1;
/* 206 */     if (len <= 0) {
/* 207 */       return list;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 212 */       doc.getText(offs, len, s);
/*     */ 
/*     */ 
/*     */       
/* 216 */       offs = s.offset + len - 1;
/* 217 */       while (offs >= s.offset && Character.isWhitespace(s.array[offs])) {
/* 218 */         offs--;
/*     */       }
/* 220 */       int end = offs;
/* 221 */       while (offs >= s.offset && isValidChar(s.array[offs])) {
/* 222 */         offs--;
/*     */       }
/*     */       
/* 225 */       String text = new String(s.array, offs + 1, end - offs);
/*     */ 
/*     */ 
/*     */       
/* 229 */       List<Completion> l = getCompletionByInputText(text);
/* 230 */       if (l != null && !l.isEmpty()) {
/* 231 */         for (Completion o : l) {
/* 232 */           if (o instanceof ParameterizedCompletion) {
/* 233 */             if (list == null) {
/* 234 */               list = new ArrayList<>(1);
/*     */             }
/* 236 */             list.add((ParameterizedCompletion)o);
/*     */           }
/*     */         
/*     */         } 
/*     */       }
/* 241 */     } catch (BadLocationException ble) {
/* 242 */       ble.printStackTrace();
/*     */     } 
/*     */     
/* 245 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {
/* 254 */     this.seg = new Segment();
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
/*     */   protected boolean isValidChar(char ch) {
/* 268 */     return (Character.isLetterOrDigit(ch) || ch == '_');
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
/*     */   public void loadFromXML(File file) throws IOException {
/* 280 */     try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file))) {
/*     */       
/* 282 */       loadFromXML(bin);
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
/*     */   public void loadFromXML(InputStream in) throws IOException {
/* 295 */     loadFromXML(in, (ClassLoader)null);
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
/*     */   public void loadFromXML(InputStream in, ClassLoader cl) throws IOException {
/* 314 */     SAXParserFactory factory = SAXParserFactory.newInstance();
/* 315 */     factory.setValidating(true);
/* 316 */     CompletionXMLParser handler = new CompletionXMLParser(this, cl);
/* 317 */     try (BufferedInputStream bin = new BufferedInputStream(in)) {
/* 318 */       SAXParser saxParser = factory.newSAXParser();
/* 319 */       saxParser.parse(bin, handler);
/* 320 */       List<Completion> completions = handler.getCompletions();
/* 321 */       addCompletions(completions);
/* 322 */       char startChar = handler.getParamStartChar();
/* 323 */       if (startChar != '\000') {
/* 324 */         char endChar = handler.getParamEndChar();
/* 325 */         String sep = handler.getParamSeparator();
/* 326 */         if (endChar != '\000' && sep != null && sep.length() > 0) {
/* 327 */           setParameterizedCompletionParams(startChar, sep, endChar);
/*     */         }
/*     */       } 
/* 330 */     } catch (SAXException|javax.xml.parsers.ParserConfigurationException e) {
/* 331 */       throw new IOException(e.toString());
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
/*     */   public void loadFromXML(String resource) throws IOException {
/* 347 */     ClassLoader cl = getClass().getClassLoader();
/* 348 */     InputStream in = cl.getResourceAsStream(resource);
/* 349 */     if (in == null) {
/* 350 */       File file = new File(resource);
/* 351 */       if (file.isFile()) {
/* 352 */         in = new FileInputStream(file);
/*     */       } else {
/*     */         
/* 355 */         throw new IOException("No such resource: " + resource);
/*     */       } 
/*     */     } 
/* 358 */     try (BufferedInputStream bin = new BufferedInputStream(in)) {
/* 359 */       loadFromXML(bin);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\DefaultCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */