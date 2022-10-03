/*     */ package org.fife.rsta.ac.html;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.DefaultCompletionProvider;
/*     */ import org.fife.ui.autocomplete.MarkupTagCompletion;
/*     */ import org.fife.ui.autocomplete.ParameterizedCompletion;
/*     */ import org.fife.ui.autocomplete.Util;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*     */ import org.fife.ui.rsyntaxtextarea.Token;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HtmlCompletionProvider
/*     */   extends DefaultCompletionProvider
/*     */ {
/*     */   private Map<String, List<AttributeCompletion>> tagToAttrs;
/*     */   private boolean isTagName;
/*     */   private String lastTagName;
/*     */   
/*     */   public HtmlCompletionProvider() {
/*  68 */     initCompletions();
/*     */     
/*  70 */     this.tagToAttrs = new HashMap<>();
/*  71 */     for (Completion comp : this.completions) {
/*  72 */       MarkupTagCompletion c = (MarkupTagCompletion)comp;
/*  73 */       String tag = c.getName();
/*  74 */       List<AttributeCompletion> attrs = new ArrayList<>();
/*  75 */       this.tagToAttrs.put(tag.toLowerCase(), attrs);
/*  76 */       for (int j = 0; j < c.getAttributeCount(); j++) {
/*  77 */         ParameterizedCompletion.Parameter param = c.getAttribute(j);
/*  78 */         attrs.add(new AttributeCompletion((CompletionProvider)this, param));
/*     */       } 
/*     */     } 
/*     */     
/*  82 */     setAutoActivationRules(false, "<");
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
/*     */   protected String defaultGetAlreadyEnteredText(JTextComponent comp) {
/*  96 */     return super.getAlreadyEnteredText(comp);
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
/*     */   private boolean findLastTagNameBefore(RSyntaxDocument doc, Token tokenList, int offs) {
/* 113 */     this.lastTagName = null;
/* 114 */     boolean foundOpenTag = false;
/*     */     
/* 116 */     for (Token t = tokenList; t != null && 
/* 117 */       !t.containsPosition(offs); t = t.getNextToken()) {
/*     */ 
/*     */       
/* 120 */       if (t.getType() == 26) {
/* 121 */         this.lastTagName = t.getLexeme();
/*     */       }
/* 123 */       else if (t.getType() == 25) {
/* 124 */         this.lastTagName = null;
/* 125 */         foundOpenTag = t.isSingleChar('<');
/* 126 */         t = t.getNextToken();
/*     */ 
/*     */         
/* 129 */         if (t != null && !t.isWhitespace()) {
/* 130 */           this.lastTagName = t.getLexeme();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 135 */     if (this.lastTagName == null && !foundOpenTag) {
/*     */       
/* 137 */       Element root = doc.getDefaultRootElement();
/* 138 */       int prevLine = root.getElementIndex(offs) - 1;
/* 139 */       while (prevLine >= 0) {
/* 140 */         tokenList = doc.getTokenListForLine(prevLine);
/* 141 */         for (Token token = tokenList; token != null; token = token.getNextToken()) {
/* 142 */           if (token.getType() == 26) {
/* 143 */             this.lastTagName = token.getLexeme();
/*     */           }
/* 145 */           else if (token.getType() == 25) {
/* 146 */             this.lastTagName = null;
/* 147 */             foundOpenTag = token.isSingleChar('<');
/* 148 */             token = token.getNextToken();
/*     */ 
/*     */             
/* 151 */             if (token != null && !token.isWhitespace()) {
/* 152 */               this.lastTagName = token.getLexeme();
/*     */             }
/*     */           } 
/*     */         } 
/* 156 */         if (this.lastTagName != null || foundOpenTag) {
/*     */           break;
/*     */         }
/* 159 */         prevLine--;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 164 */     return (this.lastTagName != null);
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
/* 175 */     this.isTagName = true;
/* 176 */     this.lastTagName = null;
/*     */     
/* 178 */     String text = super.getAlreadyEnteredText(comp);
/* 179 */     if (text != null) {
/*     */ 
/*     */       
/* 182 */       int dot = comp.getCaretPosition();
/* 183 */       if (dot > 0) {
/*     */         
/* 185 */         RSyntaxTextArea textArea = (RSyntaxTextArea)comp;
/*     */ 
/*     */         
/*     */         try {
/* 189 */           int line = textArea.getLineOfOffset(dot - 1);
/* 190 */           Token list = textArea.getTokenListForLine(line);
/*     */           
/* 192 */           if (list != null) {
/*     */             
/* 194 */             Token t = RSyntaxUtilities.getTokenAtOffset(list, dot - 1);
/*     */             
/* 196 */             if (t == null) {
/* 197 */               text = null;
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             }
/* 203 */             else if (t.getType() == 25) {
/* 204 */               if (!isTagOpeningToken(t)) {
/* 205 */                 text = null;
/*     */ 
/*     */               
/*     */               }
/*     */             
/*     */             }
/* 211 */             else if (t.getType() == 21) {
/* 212 */               if (!insideMarkupTag(textArea, list, line, dot)) {
/* 213 */                 text = null;
/*     */ 
/*     */               
/*     */               }
/*     */ 
/*     */             
/*     */             }
/* 220 */             else if (t.getType() != 27 && t
/* 221 */               .getType() != 26) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 229 */               if (t.getType() > -1 || t.getType() < -9) {
/* 230 */                 text = null;
/*     */               }
/*     */             } 
/*     */ 
/*     */             
/* 235 */             if (text != null) {
/* 236 */               t = getTokenBeforeOffset(list, dot - text.length());
/* 237 */               this.isTagName = (t != null && isTagOpeningToken(t));
/* 238 */               if (!this.isTagName) {
/* 239 */                 RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/* 240 */                 findLastTagNameBefore(doc, list, dot);
/*     */               }
/*     */             
/*     */             }
/*     */           
/*     */           } 
/* 246 */         } catch (BadLocationException ble) {
/* 247 */           ble.printStackTrace();
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 253 */         text = null;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 258 */     return text;
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
/*     */   protected List<AttributeCompletion> getAttributeCompletionsForTag(String tagName) {
/* 274 */     return this.tagToAttrs.get(this.lastTagName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Completion> getCompletionsImpl(JTextComponent comp) {
/* 284 */     List<Completion> retVal = new ArrayList<>();
/* 285 */     String text = getAlreadyEnteredText(comp);
/* 286 */     List<? extends Completion> completions = getTagCompletions();
/* 287 */     if (this.lastTagName != null) {
/* 288 */       this.lastTagName = this.lastTagName.toLowerCase();
/* 289 */       completions = (List)getAttributeCompletionsForTag(this.lastTagName);
/*     */     } 
/*     */ 
/*     */     
/* 293 */     if (text != null && completions != null) {
/*     */ 
/*     */       
/* 296 */       int index = Collections.binarySearch(completions, text, (Comparator<?>)this.comparator);
/* 297 */       if (index < 0) {
/* 298 */         index = -index - 1;
/*     */       }
/*     */       
/* 301 */       while (index < completions.size()) {
/* 302 */         Completion c = completions.get(index);
/* 303 */         if (Util.startsWithIgnoreCase(c.getInputText(), text)) {
/* 304 */           retVal.add(c);
/* 305 */           index++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 314 */     return retVal;
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
/*     */   protected List<Completion> getTagCompletions() {
/* 326 */     return this.completions;
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
/*     */   private static Token getTokenBeforeOffset(Token tokenList, int offs) {
/* 340 */     if (tokenList != null) {
/* 341 */       Token prev = tokenList;
/* 342 */       for (Token t = tokenList.getNextToken(); t != null; t = t.getNextToken()) {
/* 343 */         if (t.containsPosition(offs)) {
/* 344 */           return prev;
/*     */         }
/* 346 */         prev = t;
/*     */       } 
/*     */     } 
/* 349 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initCompletions() {
/*     */     try {
/* 360 */       loadFromXML("data/html.xml");
/* 361 */     } catch (IOException ioe) {
/* 362 */       ioe.printStackTrace();
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
/*     */   private static boolean insideMarkupTag(RSyntaxTextArea textArea, Token list, int line, int offs) {
/* 381 */     int inside = -1;
/*     */     
/* 383 */     for (Token t = list; t != null && 
/* 384 */       !t.containsPosition(offs); t = t.getNextToken()) {
/*     */ 
/*     */       
/* 387 */       switch (t.getType()) {
/*     */         case 26:
/*     */         case 27:
/* 390 */           inside = 1;
/*     */           break;
/*     */         case 25:
/* 393 */           inside = t.isSingleChar('>') ? 0 : 1;
/*     */           break;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 399 */     if (inside == -1) {
/* 400 */       if (line == 0) {
/* 401 */         inside = 0;
/*     */       } else {
/*     */         
/* 404 */         RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/* 405 */         int prevLastToken = doc.getLastTokenTypeOnLine(line - 1);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 410 */         if (prevLastToken <= -1 && prevLastToken >= -9) {
/* 411 */           inside = 1;
/*     */         } else {
/*     */           
/* 414 */           inside = 0;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 419 */     return (inside == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoActivateOkay(JTextComponent tc) {
/* 430 */     boolean okay = super.isAutoActivateOkay(tc);
/*     */     
/* 432 */     if (okay) {
/*     */       
/* 434 */       RSyntaxTextArea textArea = (RSyntaxTextArea)tc;
/* 435 */       int dot = textArea.getCaretPosition();
/*     */ 
/*     */       
/*     */       try {
/* 439 */         int line = textArea.getLineOfOffset(dot);
/* 440 */         Token list = textArea.getTokenListForLine(line);
/*     */         
/* 442 */         if (list != null) {
/* 443 */           return !insideMarkupTag(textArea, list, line, dot);
/*     */         }
/*     */       }
/* 446 */       catch (BadLocationException ble) {
/* 447 */         ble.printStackTrace();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 452 */     return okay;
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
/*     */   private static boolean isTagOpeningToken(Token t) {
/* 464 */     return (t.isSingleChar('<') || (t
/* 465 */       .length() == 2 && t.charAt(0) == '<' && t
/* 466 */       .charAt(1) == '/'));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\html\HtmlCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */