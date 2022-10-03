/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Segment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CompletionProviderBase
/*     */   implements CompletionProvider
/*     */ {
/*     */   private CompletionProvider parent;
/*     */   private ListCellRenderer<Object> listCellRenderer;
/*     */   private char paramListStart;
/*     */   private char paramListEnd;
/*     */   private String paramListSeparator;
/*     */   private boolean autoActivateAfterLetters;
/*     */   private String autoActivateChars;
/*     */   private ParameterChoicesProvider paramChoicesProvider;
/*  76 */   private Segment s = new Segment();
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String EMPTY_STRING = "";
/*     */ 
/*     */ 
/*     */   
/*  84 */   private static final Comparator<Completion> SORT_BY_RELEVANCE_COMPARATOR = new SortByRelevanceComparator();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearParameterizedCompletionParams() {
/*  90 */     this.paramListEnd = this.paramListStart = Character.MIN_VALUE;
/*  91 */     this.paramListSeparator = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Completion> getCompletions(JTextComponent comp) {
/*  98 */     List<Completion> completions = getCompletionsImpl(comp);
/*  99 */     if (this.parent != null) {
/* 100 */       List<Completion> parentCompletions = this.parent.getCompletions(comp);
/* 101 */       if (parentCompletions != null) {
/* 102 */         completions.addAll(parentCompletions);
/* 103 */         Collections.sort(completions);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     completions.sort(SORT_BY_RELEVANCE_COMPARATOR);
/*     */ 
/*     */     
/* 114 */     return completions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract List<Completion> getCompletionsImpl(JTextComponent paramJTextComponent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListCellRenderer<Object> getListCellRenderer() {
/* 131 */     return this.listCellRenderer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterChoicesProvider getParameterChoicesProvider() {
/* 137 */     return this.paramChoicesProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public char getParameterListEnd() {
/* 143 */     return this.paramListEnd;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameterListSeparator() {
/* 149 */     return this.paramListSeparator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public char getParameterListStart() {
/* 155 */     return this.paramListStart;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletionProvider getParent() {
/* 161 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoActivateOkay(JTextComponent tc) {
/* 167 */     Document doc = tc.getDocument();
/* 168 */     char ch = Character.MIN_VALUE;
/*     */     try {
/* 170 */       doc.getText(tc.getCaretPosition(), 1, this.s);
/* 171 */       ch = this.s.first();
/* 172 */     } catch (BadLocationException ble) {
/* 173 */       ble.printStackTrace();
/*     */     } 
/* 175 */     return ((this.autoActivateAfterLetters && Character.isLetter(ch)) || (this.autoActivateChars != null && this.autoActivateChars
/* 176 */       .indexOf(ch) > -1));
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
/*     */   public void setAutoActivationRules(boolean letters, String others) {
/* 190 */     this.autoActivateAfterLetters = letters;
/* 191 */     this.autoActivateChars = others;
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
/*     */   public void setParameterChoicesProvider(ParameterChoicesProvider pcp) {
/* 206 */     this.paramChoicesProvider = pcp;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setListCellRenderer(ListCellRenderer<Object> r) {
/* 212 */     this.listCellRenderer = r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterizedCompletionParams(char listStart, String separator, char listEnd) {
/* 219 */     if (listStart < ' ' || listStart == '') {
/* 220 */       throw new IllegalArgumentException("Invalid listStart");
/*     */     }
/* 222 */     if (listEnd < ' ' || listEnd == '') {
/* 223 */       throw new IllegalArgumentException("Invalid listEnd");
/*     */     }
/* 225 */     if (separator == null || separator.length() == 0) {
/* 226 */       throw new IllegalArgumentException("Invalid separator");
/*     */     }
/* 228 */     this.paramListStart = listStart;
/* 229 */     this.paramListSeparator = separator;
/* 230 */     this.paramListEnd = listEnd;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(CompletionProvider parent) {
/* 236 */     this.parent = parent;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\CompletionProviderBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */