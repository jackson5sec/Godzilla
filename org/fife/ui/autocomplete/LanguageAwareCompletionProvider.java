/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*     */ import org.fife.ui.rsyntaxtextarea.Token;
/*     */ import org.fife.ui.rtextarea.RTextArea;
/*     */ import org.fife.ui.rtextarea.ToolTipSupplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LanguageAwareCompletionProvider
/*     */   extends CompletionProviderBase
/*     */   implements ToolTipSupplier
/*     */ {
/*     */   private CompletionProvider defaultProvider;
/*     */   private CompletionProvider stringCompletionProvider;
/*     */   private CompletionProvider commentCompletionProvider;
/*     */   private CompletionProvider docCommentCompletionProvider;
/*     */   
/*     */   protected LanguageAwareCompletionProvider() {}
/*     */   
/*     */   public LanguageAwareCompletionProvider(CompletionProvider defaultProvider) {
/*  94 */     setDefaultCompletionProvider(defaultProvider);
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
/*     */   public void clearParameterizedCompletionParams() {
/* 109 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAlreadyEnteredText(JTextComponent comp) {
/* 118 */     if (!(comp instanceof RSyntaxTextArea)) {
/* 119 */       return "";
/*     */     }
/* 121 */     CompletionProvider provider = getProviderFor(comp);
/* 122 */     return (provider != null) ? provider.getAlreadyEnteredText(comp) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletionProvider getCommentCompletionProvider() {
/* 133 */     return this.commentCompletionProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Completion> getCompletionsAt(JTextComponent tc, Point p) {
/* 142 */     return (this.defaultProvider == null) ? null : this.defaultProvider
/* 143 */       .getCompletionsAt(tc, p);
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
/*     */   protected List<Completion> getCompletionsImpl(JTextComponent comp) {
/* 156 */     if (comp instanceof RSyntaxTextArea) {
/* 157 */       CompletionProvider provider = getProviderFor(comp);
/* 158 */       if (provider != null) {
/* 159 */         return provider.getCompletions(comp);
/*     */       }
/*     */     } 
/* 162 */     return Collections.emptyList();
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
/*     */   public CompletionProvider getDefaultCompletionProvider() {
/* 174 */     return this.defaultProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletionProvider getDocCommentCompletionProvider() {
/* 185 */     return this.docCommentCompletionProvider;
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
/*     */   public List<ParameterizedCompletion> getParameterizedCompletions(JTextComponent tc) {
/* 198 */     CompletionProvider provider = getProviderFor(tc);
/* 199 */     return (provider == this.defaultProvider) ? provider
/* 200 */       .getParameterizedCompletions(tc) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getParameterListEnd() {
/* 209 */     return this.defaultProvider.getParameterListEnd();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameterListSeparator() {
/* 218 */     return this.defaultProvider.getParameterListSeparator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getParameterListStart() {
/* 227 */     return this.defaultProvider.getParameterListStart();
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
/*     */   private CompletionProvider getProviderFor(JTextComponent comp) {
/* 240 */     RSyntaxTextArea rsta = (RSyntaxTextArea)comp;
/* 241 */     RSyntaxDocument doc = (RSyntaxDocument)rsta.getDocument();
/* 242 */     int line = rsta.getCaretLineNumber();
/* 243 */     Token t = doc.getTokenListForLine(line);
/* 244 */     if (t == null) {
/* 245 */       return getDefaultCompletionProvider();
/*     */     }
/*     */     
/* 248 */     int dot = rsta.getCaretPosition();
/* 249 */     Token curToken = RSyntaxUtilities.getTokenAtOffset(t, dot);
/*     */     
/* 251 */     if (curToken == null) {
/*     */       
/* 253 */       int type = doc.getLastTokenTypeOnLine(line);
/* 254 */       if (type == 0) {
/* 255 */         Token temp = t.getLastPaintableToken();
/* 256 */         if (temp == null) {
/* 257 */           return getDefaultCompletionProvider();
/*     */         }
/* 259 */         type = temp.getType();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 265 */       else if (type < 0) {
/* 266 */         type = doc.getClosestStandardTokenTypeForInternalType(type);
/*     */       } 
/*     */       
/* 269 */       switch (type) {
/*     */         case 37:
/* 271 */           return getStringCompletionProvider();
/*     */         case 1:
/*     */         case 2:
/* 274 */           return getCommentCompletionProvider();
/*     */         case 3:
/* 276 */           return getDocCommentCompletionProvider();
/*     */       } 
/* 278 */       return getDefaultCompletionProvider();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 284 */     if (dot == curToken.getOffset())
/*     */     {
/*     */       
/* 287 */       return getDefaultCompletionProvider();
/*     */     }
/*     */     
/* 290 */     switch (curToken.getType()) {
/*     */       case 13:
/*     */       case 37:
/* 293 */         return getStringCompletionProvider();
/*     */       case 1:
/*     */       case 2:
/* 296 */         return getCommentCompletionProvider();
/*     */       case 3:
/* 298 */         return getDocCommentCompletionProvider();
/*     */       case 0:
/*     */       case 8:
/*     */       case 16:
/*     */       case 17:
/*     */       case 20:
/*     */       case 21:
/*     */       case 23:
/*     */       case 24:
/* 307 */         return getDefaultCompletionProvider();
/*     */     } 
/*     */     
/* 310 */     return null;
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
/*     */   public CompletionProvider getStringCompletionProvider() {
/* 322 */     return this.stringCompletionProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoActivateOkay(JTextComponent tc) {
/* 331 */     CompletionProvider provider = getProviderFor(tc);
/* 332 */     return (provider != null) ? provider.isAutoActivateOkay(tc) : false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommentCompletionProvider(CompletionProvider provider) {
/* 343 */     this.commentCompletionProvider = provider;
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
/*     */   public void setDefaultCompletionProvider(CompletionProvider provider) {
/* 355 */     if (provider == null) {
/* 356 */       throw new IllegalArgumentException("provider cannot be null");
/*     */     }
/* 358 */     this.defaultProvider = provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocCommentCompletionProvider(CompletionProvider provider) {
/* 369 */     this.docCommentCompletionProvider = provider;
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
/*     */   public void setParameterizedCompletionParams(char listStart, String separator, char listEnd) {
/* 385 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStringCompletionProvider(CompletionProvider provider) {
/* 396 */     this.stringCompletionProvider = provider;
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
/*     */   public String getToolTipText(RTextArea textArea, MouseEvent e) {
/* 417 */     String tip = null;
/*     */     
/* 419 */     List<Completion> completions = getCompletionsAt((JTextComponent)textArea, e.getPoint());
/* 420 */     if (completions != null && completions.size() > 0) {
/*     */       
/* 422 */       Completion c = completions.get(0);
/* 423 */       tip = c.getToolTipText();
/*     */     } 
/*     */     
/* 426 */     return tip;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\LanguageAwareCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */