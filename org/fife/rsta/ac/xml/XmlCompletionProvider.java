/*     */ package org.fife.rsta.ac.xml;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.html.AttributeCompletion;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.DefaultCompletionProvider;
/*     */ import org.fife.ui.autocomplete.MarkupTagCompletion;
/*     */ import org.fife.ui.autocomplete.ParameterizedCompletion;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*     */ import org.fife.ui.rsyntaxtextarea.Token;
/*     */ import org.fife.ui.rsyntaxtextarea.TokenImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class XmlCompletionProvider
/*     */   extends DefaultCompletionProvider
/*     */ {
/*  53 */   private static final char[] TAG_SELF_CLOSE = new char[] { '/', '>' };
/*     */ 
/*     */   
/*     */   public XmlCompletionProvider() {
/*  57 */     setAutoActivationRules(false, "<");
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
/*     */   private void addCompletionImpl(String word, int desiredType) {
/*     */     AttributeCompletion attributeCompletion;
/*  70 */     if (desiredType == 26) {
/*  71 */       MarkupTagCompletion markupTagCompletion = new MarkupTagCompletion((CompletionProvider)this, word);
/*     */     } else {
/*     */       
/*  74 */       ParameterizedCompletion.Parameter param = new ParameterizedCompletion.Parameter(null, word);
/*     */       
/*  76 */       attributeCompletion = new AttributeCompletion((CompletionProvider)this, param);
/*     */     } 
/*  78 */     this.completions.add(attributeCompletion);
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
/*     */   private Set<String> collectCompletionWordsAttribute(RSyntaxDocument doc, Token inTag, int currentWordStart) {
/*  95 */     Set<String> possibleAttrs = new HashSet<>();
/*  96 */     Set<String> attrs = new HashSet<>();
/*  97 */     Set<String> attrsAlreadySpecified = new HashSet<>();
/*  98 */     String desiredTagName = inTag.getLexeme();
/*  99 */     boolean collectAttrs = false;
/* 100 */     boolean inCurTag = false;
/*     */     
/* 102 */     for (Token t2 : doc) {
/* 103 */       int type = t2.getType();
/* 104 */       if (type == 26) {
/* 105 */         collectAttrs = desiredTagName.equals(t2.getLexeme());
/* 106 */         inCurTag = (t2.getOffset() == inTag.getOffset());
/* 107 */         if (!attrs.isEmpty()) {
/* 108 */           possibleAttrs.addAll(attrs);
/* 109 */           attrs.clear();
/*     */         }  continue;
/*     */       } 
/* 112 */       if (type == 27 && collectAttrs && 
/* 113 */         t2.getOffset() != currentWordStart) {
/* 114 */         String word = t2.getLexeme();
/* 115 */         if (inCurTag) {
/* 116 */           if (word.indexOf('<') > -1) {
/* 117 */             collectAttrs = false;
/* 118 */             attrs.clear();
/*     */ 
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 124 */           attrsAlreadySpecified.add(word);
/*     */ 
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 133 */         if (word.indexOf('<') > -1) {
/*     */           
/* 135 */           collectAttrs = false;
/* 136 */           attrs.clear();
/* 137 */           attrsAlreadySpecified.clear();
/*     */           continue;
/*     */         } 
/* 140 */         attrs.add(word);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     if (!attrs.isEmpty()) {
/* 148 */       possibleAttrs.addAll(attrs);
/*     */     }
/* 150 */     possibleAttrs.removeAll(attrsAlreadySpecified);
/* 151 */     return possibleAttrs;
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
/*     */   private Set<String> collectCompletionWordsTag(RSyntaxDocument doc, int currentWordStart) {
/* 166 */     Set<String> words = new HashSet<>();
/* 167 */     for (Token t2 : doc) {
/* 168 */       if (t2.getType() == 26 && t2
/* 169 */         .getOffset() != currentWordStart) {
/* 170 */         words.add(t2.getLexeme());
/*     */       }
/*     */     } 
/* 173 */     return words;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Completion> getCompletionsImpl(JTextComponent comp) {
/*     */     Set<String> words;
/* 180 */     this.completions.clear();
/*     */     
/* 182 */     String text = getAlreadyEnteredText(comp);
/* 183 */     if (text == null) {
/* 184 */       return this.completions;
/*     */     }
/*     */     
/* 187 */     RSyntaxTextArea textArea = (RSyntaxTextArea)comp;
/* 188 */     int dot = textArea.getCaretPosition();
/* 189 */     RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/* 190 */     Token t = RSyntaxUtilities.getPreviousImportantTokenFromOffs(doc, dot);
/* 191 */     if (t == null) {
/* 192 */       UIManager.getLookAndFeel().provideErrorFeedback((Component)textArea);
/* 193 */       return this.completions;
/*     */     } 
/*     */     
/* 196 */     int desiredType = getDesiredTokenType(t, dot);
/* 197 */     if (desiredType == 0) {
/* 198 */       UIManager.getLookAndFeel().provideErrorFeedback((Component)textArea);
/* 199 */       return this.completions;
/*     */     } 
/*     */     
/* 202 */     int currentWordStart = dot - text.length();
/*     */     
/* 204 */     if (desiredType == 26) {
/* 205 */       words = collectCompletionWordsTag(doc, currentWordStart);
/*     */     } else {
/*     */       
/* 208 */       Token tagNameToken = getTagNameTokenForCaretOffset(textArea);
/* 209 */       if (tagNameToken != null) {
/* 210 */         TokenImpl tokenImpl = new TokenImpl(tagNameToken);
/* 211 */         words = collectCompletionWordsAttribute(doc, (Token)tokenImpl, currentWordStart);
/*     */       }
/*     */       else {
/*     */         
/* 215 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)textArea);
/* 216 */         return this.completions;
/*     */       } 
/*     */     } 
/*     */     
/* 220 */     for (String word : words) {
/* 221 */       addCompletionImpl(word, desiredType);
/*     */     }
/* 223 */     Collections.sort(this.completions);
/*     */     
/* 225 */     return super.getCompletionsImpl(comp);
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
/*     */   private static final int getDesiredTokenType(Token t, int dot) {
/* 240 */     switch (t.getType()) {
/*     */       case 26:
/* 242 */         if (t.containsPosition(dot - 1)) {
/* 243 */           return t.getType();
/*     */         }
/* 245 */         return 27;
/*     */       case 27:
/* 247 */         return t.getType();
/*     */       case 28:
/* 249 */         if (t.containsPosition(dot)) {
/* 250 */           return 0;
/*     */         }
/* 252 */         return 27;
/*     */       case 25:
/* 254 */         if (t.isSingleChar('<')) {
/* 255 */           return 26;
/*     */         }
/* 257 */         return 0;
/*     */     } 
/* 259 */     return 0;
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
/*     */   public static final Token getTagNameTokenForCaretOffset(RSyntaxTextArea textArea) {
/* 275 */     int dot = textArea.getCaretPosition();
/* 276 */     int line = textArea.getCaretLineNumber();
/* 277 */     Token toMark = null;
/*     */ 
/*     */     
/*     */     do {
/* 281 */       Token t = textArea.getTokenListForLine(line);
/*     */       
/* 283 */       while (t != null && t.isPaintable()) {
/* 284 */         if (t.getType() == 26) {
/* 285 */           toMark = t;
/*     */         }
/* 287 */         if (t.getEndOffset() == dot || t.containsPosition(dot)) {
/*     */           break;
/*     */         }
/* 290 */         if (t.getType() == 25 && (
/* 291 */           t.isSingleChar('>') || t.is(TAG_SELF_CLOSE))) {
/* 292 */           toMark = null;
/*     */         }
/*     */         
/* 295 */         t = t.getNextToken();
/*     */       }
/*     */     
/* 298 */     } while (toMark == null && --line >= 0);
/*     */     
/* 300 */     return toMark;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\xml\XmlCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */