/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import javax.swing.text.JTextComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCompletionProvider
/*     */   extends CompletionProviderBase
/*     */ {
/*     */   protected List<Completion> completions;
/*     */   protected CaseInsensitiveComparator comparator;
/*     */   
/*     */   public AbstractCompletionProvider() {
/*  48 */     this.comparator = new CaseInsensitiveComparator();
/*  49 */     clearParameterizedCompletionParams();
/*  50 */     this.completions = new ArrayList<>();
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
/*     */   public void addCompletion(Completion c) {
/*  67 */     checkProviderAndAdd(c);
/*  68 */     Collections.sort(this.completions);
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
/*     */   public void addCompletions(List<Completion> completions) {
/*  85 */     for (Completion c : completions) {
/*  86 */       checkProviderAndAdd(c);
/*     */     }
/*  88 */     Collections.sort(this.completions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addWordCompletions(String[] words) {
/*  99 */     int count = (words == null) ? 0 : words.length;
/* 100 */     for (int i = 0; i < count; i++) {
/* 101 */       this.completions.add(new BasicCompletion(this, words[i]));
/*     */     }
/* 103 */     Collections.sort(this.completions);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkProviderAndAdd(Completion c) {
/* 108 */     if (c.getProvider() != this) {
/* 109 */       throw new IllegalArgumentException("Invalid CompletionProvider");
/*     */     }
/* 111 */     this.completions.add(c);
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
/*     */   public void clear() {
/* 124 */     this.completions.clear();
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
/*     */   public List<Completion> getCompletionByInputText(String inputText) {
/* 140 */     int end = Collections.binarySearch(this.completions, inputText, this.comparator);
/* 141 */     if (end < 0) {
/* 142 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 146 */     int start = end;
/* 147 */     while (start > 0 && this.comparator
/* 148 */       .compare(this.completions.get(start - 1), inputText) == 0) {
/* 149 */       start--;
/*     */     }
/* 151 */     int count = this.completions.size();
/* 152 */     while (++end < count && this.comparator
/* 153 */       .compare(this.completions.get(end), inputText) == 0);
/*     */     
/* 155 */     return this.completions.subList(start, end);
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
/*     */   protected List<Completion> getCompletionsImpl(JTextComponent comp) {
/* 167 */     List<Completion> retVal = new ArrayList<>();
/* 168 */     String text = getAlreadyEnteredText(comp);
/*     */     
/* 170 */     if (text != null) {
/*     */       
/* 172 */       int index = Collections.binarySearch(this.completions, text, this.comparator);
/* 173 */       if (index < 0) {
/* 174 */         index = -index - 1;
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 181 */         int pos = index - 1;
/* 182 */         while (pos > 0 && this.comparator
/* 183 */           .compare(this.completions.get(pos), text) == 0) {
/* 184 */           retVal.add(this.completions.get(pos));
/* 185 */           pos--;
/*     */         } 
/*     */       } 
/*     */       
/* 189 */       while (index < this.completions.size()) {
/* 190 */         Completion c = this.completions.get(index);
/* 191 */         if (Util.startsWithIgnoreCase(c.getInputText(), text)) {
/* 192 */           retVal.add(c);
/* 193 */           index++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 202 */     return retVal;
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
/*     */   public boolean removeCompletion(Completion c) {
/* 220 */     int index = Collections.binarySearch((List)this.completions, c);
/* 221 */     if (index < 0) {
/* 222 */       return false;
/*     */     }
/* 224 */     this.completions.remove(index);
/* 225 */     return true;
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
/*     */   public static class CaseInsensitiveComparator
/*     */     implements Comparator, Serializable
/*     */   {
/*     */     public int compare(Object o1, Object o2) {
/* 240 */       String s1 = (o1 instanceof String) ? (String)o1 : ((Completion)o1).getInputText();
/*     */       
/* 242 */       String s2 = (o2 instanceof String) ? (String)o2 : ((Completion)o2).getInputText();
/* 243 */       return String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\AbstractCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */