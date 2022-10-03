/*     */ package org.fife.rsta.ac.php;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.fife.rsta.ac.html.HtmlCompletionProvider;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.CompletionXMLParser;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PhpCompletionProvider
/*     */   extends HtmlCompletionProvider
/*     */ {
/*     */   private boolean phpCompletion;
/*     */   private List<Completion> phpCompletions;
/*     */   
/*     */   public PhpCompletionProvider() {
/*  70 */     ClassLoader cl = getClass().getClassLoader();
/*  71 */     InputStream in = cl.getResourceAsStream("data/php.xml");
/*     */     try {
/*  73 */       if (in == null) {
/*  74 */         in = new FileInputStream("data/php.xml");
/*     */       }
/*  76 */       loadPhpCompletionsFromXML(in);
/*  77 */     } catch (IOException ioe) {
/*  78 */       ioe.printStackTrace();
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
/*     */   public void loadPhpCompletionsFromXML(InputStream in) throws IOException {
/*  93 */     long start = System.currentTimeMillis();
/*     */     
/*  95 */     SAXParserFactory factory = SAXParserFactory.newInstance();
/*  96 */     CompletionXMLParser handler = new CompletionXMLParser((CompletionProvider)this);
/*  97 */     BufferedInputStream bin = new BufferedInputStream(in);
/*     */     try {
/*  99 */       SAXParser saxParser = factory.newSAXParser();
/* 100 */       saxParser.parse(bin, (DefaultHandler)handler);
/* 101 */       this.phpCompletions = handler.getCompletions();
/* 102 */       char startChar = handler.getParamStartChar();
/* 103 */       if (startChar != '\000') {
/* 104 */         char endChar = handler.getParamEndChar();
/* 105 */         String sep = handler.getParamSeparator();
/* 106 */         if (endChar != '\000' && sep != null && sep.length() > 0) {
/* 107 */           setParameterizedCompletionParams(startChar, sep, endChar);
/*     */         }
/*     */       } 
/* 110 */     } catch (SAXException|javax.xml.parsers.ParserConfigurationException e) {
/* 111 */       throw new IOException(e.toString());
/*     */     } finally {
/* 113 */       long time = System.currentTimeMillis() - start;
/* 114 */       System.out.println("XML loaded in: " + time + "ms");
/* 115 */       bin.close();
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
/*     */   public String getAlreadyEnteredText(JTextComponent comp) {
/* 127 */     this.phpCompletion = false;
/*     */     
/* 129 */     String text = super.getAlreadyEnteredText(comp);
/* 130 */     if (text == null && 
/* 131 */       inPhpBlock(comp)) {
/* 132 */       text = defaultGetAlreadyEnteredText(comp);
/* 133 */       this.phpCompletion = true;
/*     */     } 
/*     */ 
/*     */     
/* 137 */     return text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Completion> getCompletionsImpl(JTextComponent comp) {
/*     */     List<Completion> list;
/* 149 */     String text = getAlreadyEnteredText(comp);
/*     */     
/* 151 */     if (this.phpCompletion) {
/*     */       
/* 153 */       if (text == null) {
/* 154 */         list = new ArrayList<>(0);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 159 */         list = new ArrayList<>();
/*     */ 
/*     */         
/* 162 */         int index = Collections.binarySearch(this.phpCompletions, text, (Comparator<?>)this.comparator);
/* 163 */         if (index < 0) {
/* 164 */           index = -index - 1;
/*     */         }
/*     */         
/* 167 */         while (index < this.phpCompletions.size()) {
/* 168 */           Completion c = this.phpCompletions.get(index);
/* 169 */           if (Util.startsWithIgnoreCase(c.getInputText(), text)) {
/* 170 */             list.add(c);
/* 171 */             index++;
/*     */           
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 182 */       list = super.getCompletionsImpl(comp);
/*     */     } 
/*     */     
/* 185 */     return list;
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
/*     */   private boolean inPhpBlock(JTextComponent comp) {
/*     */     int line;
/* 199 */     RSyntaxTextArea textArea = (RSyntaxTextArea)comp;
/* 200 */     int dot = comp.getCaretPosition();
/* 201 */     RSyntaxDocument doc = (RSyntaxDocument)comp.getDocument();
/*     */     
/*     */     try {
/* 204 */       line = textArea.getLineOfOffset(dot);
/* 205 */     } catch (BadLocationException ble) {
/* 206 */       ble.printStackTrace();
/* 207 */       return false;
/*     */     } 
/* 209 */     Token token = doc.getTokenListForLine(line);
/*     */     
/* 211 */     boolean inPhp = false;
/*     */ 
/*     */ 
/*     */     
/* 215 */     while (token != null && token.isPaintable() && token.getOffset() <= dot) {
/* 216 */       if (token.getType() == 22 && token.length() >= 2) {
/* 217 */         char ch1 = token.charAt(0);
/* 218 */         char ch2 = token.charAt(1);
/* 219 */         if (ch1 == '<' && ch2 == '?') {
/* 220 */           inPhp = true;
/*     */         }
/* 222 */         else if (ch1 == '?' && ch2 == '>') {
/* 223 */           inPhp = false;
/*     */         } 
/*     */       } 
/* 226 */       token = token.getNextToken();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 232 */     if (!inPhp && line > 0) {
/* 233 */       int prevLineEndType = doc.getLastTokenTypeOnLine(line - 1);
/* 234 */       if (prevLineEndType <= -8192) {
/* 235 */         inPhp = true;
/*     */       }
/*     */     } 
/*     */     
/* 239 */     return inPhp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoActivateOkay(JTextComponent tc) {
/* 249 */     return (inPhpBlock(tc) || super.isAutoActivateOkay(tc));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\php\PhpCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */