/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SearchContext
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   public static final String PROPERTY_SEARCH_FOR = "Search.searchFor";
/*     */   public static final String PROPERTY_REPLACE_WITH = "Search.replaceWith";
/*     */   public static final String PROPERTY_MATCH_CASE = "Search.MatchCase";
/*     */   public static final String PROPERTY_MATCH_WHOLE_WORD = "Search.MatchWholeWord";
/*     */   public static final String PROPERTY_SEARCH_FORWARD = "Search.Forward";
/*     */   public static final String PROPERTY_SEARCH_WRAP = "Search.Wrap";
/*     */   public static final String PROPERTY_SELECTION_ONLY = "Search.SelectionOnly";
/*     */   public static final String PROPERTY_USE_REGEX = "Search.UseRegex";
/*     */   public static final String PROPERTY_MARK_ALL = "Search.MarkAll";
/*     */   private String searchFor;
/*     */   private String replaceWith;
/*     */   private boolean forward;
/*     */   private boolean wrap;
/*     */   private boolean matchCase;
/*     */   private boolean wholeWord;
/*     */   private boolean regex;
/*     */   private boolean selectionOnly;
/*     */   private boolean markAll;
/*     */   private transient PropertyChangeSupport support;
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public SearchContext() {
/*  76 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SearchContext(String searchFor) {
/*  87 */     this(searchFor, false);
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
/*     */   public SearchContext(String searchFor, boolean matchCase) {
/*  99 */     this.support = new PropertyChangeSupport(this);
/* 100 */     this.searchFor = searchFor;
/* 101 */     this.matchCase = matchCase;
/* 102 */     this.markAll = true;
/* 103 */     this.forward = true;
/* 104 */     this.wrap = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPropertyChangeListener(PropertyChangeListener l) {
/* 115 */     this.support.addPropertyChangeListener(l);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SearchContext clone() {
/*     */     try {
/* 122 */       SearchContext context = null;
/* 123 */       context = (SearchContext)super.clone();
/*     */       
/* 125 */       context.support = new PropertyChangeSupport(context);
/* 126 */       return context;
/* 127 */     } catch (CloneNotSupportedException cnse) {
/* 128 */       throw new RuntimeException("Should never happen", cnse);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void firePropertyChange(String property, boolean oldValue, boolean newValue) {
/* 135 */     this.support.firePropertyChange(property, oldValue, newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void firePropertyChange(String property, String oldValue, String newValue) {
/* 141 */     this.support.firePropertyChange(property, oldValue, newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getMarkAll() {
/* 152 */     return this.markAll;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getMatchCase() {
/* 163 */     return this.matchCase;
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
/*     */   public String getReplaceWith() {
/* 175 */     return this.replaceWith;
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
/*     */   public String getSearchFor() {
/* 187 */     return this.searchFor;
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
/*     */   public boolean getSearchForward() {
/* 199 */     return this.forward;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getSearchWrap() {
/* 210 */     return this.wrap;
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
/*     */   public boolean getSearchSelectionOnly() {
/* 222 */     return this.selectionOnly;
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
/*     */   public boolean getWholeWord() {
/* 237 */     return this.wholeWord;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRegularExpression() {
/* 248 */     return this.regex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePropertyChangeListener(PropertyChangeListener l) {
/* 259 */     this.support.removePropertyChangeListener(l);
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
/*     */   public void setMarkAll(boolean markAll) {
/* 271 */     if (markAll != this.markAll) {
/* 272 */       this.markAll = markAll;
/* 273 */       firePropertyChange("Search.MarkAll", !markAll, markAll);
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
/*     */   public void setMatchCase(boolean matchCase) {
/* 286 */     if (matchCase != this.matchCase) {
/* 287 */       this.matchCase = matchCase;
/* 288 */       firePropertyChange("Search.MatchCase", !matchCase, matchCase);
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
/*     */   public void setRegularExpression(boolean regex) {
/* 301 */     if (regex != this.regex) {
/* 302 */       this.regex = regex;
/* 303 */       firePropertyChange("Search.UseRegex", !regex, regex);
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
/*     */   public void setReplaceWith(String replaceWith) {
/* 318 */     if ((replaceWith == null && this.replaceWith != null) || (replaceWith != null && 
/* 319 */       !replaceWith.equals(this.replaceWith))) {
/* 320 */       String old = this.replaceWith;
/* 321 */       this.replaceWith = replaceWith;
/* 322 */       firePropertyChange("Search.replaceWith", old, replaceWith);
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
/*     */   public void setSearchFor(String searchFor) {
/* 336 */     if ((searchFor == null && this.searchFor != null) || (searchFor != null && 
/* 337 */       !searchFor.equals(this.searchFor))) {
/* 338 */       String old = this.searchFor;
/* 339 */       this.searchFor = searchFor;
/* 340 */       firePropertyChange("Search.searchFor", old, searchFor);
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
/*     */   public void setSearchForward(boolean forward) {
/* 354 */     if (forward != this.forward) {
/* 355 */       this.forward = forward;
/* 356 */       firePropertyChange("Search.Forward", !forward, forward);
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
/*     */   public void setSearchWrap(boolean wrap) {
/* 370 */     if (wrap != this.wrap) {
/* 371 */       this.wrap = wrap;
/* 372 */       firePropertyChange("Search.Wrap", !wrap, wrap);
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
/*     */   public void setSearchSelectionOnly(boolean selectionOnly) {
/* 389 */     if (selectionOnly != this.selectionOnly) {
/* 390 */       this.selectionOnly = selectionOnly;
/* 391 */       firePropertyChange("Search.SelectionOnly", !selectionOnly, selectionOnly);
/*     */       
/* 393 */       if (selectionOnly) {
/* 394 */         throw new UnsupportedOperationException("Searching in selection is not currently supported");
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
/*     */   public void setWholeWord(boolean wholeWord) {
/* 413 */     if (wholeWord != this.wholeWord) {
/* 414 */       this.wholeWord = wholeWord;
/* 415 */       firePropertyChange("Search.MatchWholeWord", !wholeWord, wholeWord);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 422 */     return "[SearchContext: searchFor=" + 
/* 423 */       getSearchFor() + ", replaceWith=" + 
/* 424 */       getReplaceWith() + ", matchCase=" + 
/* 425 */       getMatchCase() + ", wholeWord=" + 
/* 426 */       getWholeWord() + ", regex=" + 
/* 427 */       isRegularExpression() + ", markAll=" + 
/* 428 */       getMarkAll() + "]";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\SearchContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */