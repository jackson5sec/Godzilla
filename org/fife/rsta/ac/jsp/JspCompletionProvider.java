/*     */ package org.fife.rsta.ac.jsp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.fife.rsta.ac.html.AttributeCompletion;
/*     */ import org.fife.rsta.ac.html.HtmlCompletionProvider;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.MarkupTagCompletion;
/*     */ import org.fife.ui.autocomplete.ParameterizedCompletion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JspCompletionProvider
/*     */   extends HtmlCompletionProvider
/*     */ {
/*     */   private Map<String, TldFile> prefixToTld;
/*     */   
/*     */   public JspCompletionProvider() {
/*  43 */     this.prefixToTld = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     setAutoActivationRules(false, "<:");
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
/*     */   protected List<AttributeCompletion> getAttributeCompletionsForTag(String tagName) {
/*  72 */     List<AttributeCompletion> list = super.getAttributeCompletionsForTag(tagName);
/*     */     
/*  74 */     if (list == null) {
/*     */       
/*  76 */       int colon = tagName.indexOf(':');
/*  77 */       if (colon > -1) {
/*     */         
/*  79 */         String prefix = tagName.substring(0, colon);
/*  80 */         tagName = tagName.substring(colon + 1);
/*     */         
/*  82 */         TldFile tldFile = this.prefixToTld.get(prefix);
/*  83 */         if (tldFile != null) {
/*  84 */           List<ParameterizedCompletion.Parameter> attrs = tldFile.getAttributesForTag(tagName);
/*  85 */           if (attrs != null && attrs.size() > -1) {
/*  86 */             list = new ArrayList<>();
/*  87 */             for (ParameterizedCompletion.Parameter param : attrs) {
/*  88 */               list.add(new AttributeCompletion((CompletionProvider)this, param));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  97 */     return list;
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
/*     */   protected List<Completion> getTagCompletions() {
/* 112 */     List<Completion> completions = new ArrayList<>(super.getTagCompletions());
/*     */     
/* 114 */     for (Map.Entry<String, TldFile> entry : this.prefixToTld.entrySet()) {
/* 115 */       String prefix = entry.getKey();
/* 116 */       TldFile tld = entry.getValue();
/* 117 */       for (int j = 0; j < tld.getElementCount(); j++) {
/* 118 */         TldElement elem = tld.getElement(j);
/*     */         
/* 120 */         MarkupTagCompletion mtc = new MarkupTagCompletion((CompletionProvider)this, prefix + ":" + elem.getName());
/* 121 */         mtc.setDescription(elem.getDescription());
/* 122 */         completions.add(mtc);
/*     */       } 
/*     */     } 
/*     */     
/* 126 */     Collections.sort(completions);
/* 127 */     return completions;
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
/*     */   protected void initCompletions() {
/* 139 */     super.initCompletions();
/*     */ 
/*     */     
/*     */     try {
/* 143 */       loadFromXML("data/jsp.xml");
/* 144 */     } catch (IOException ioe) {
/* 145 */       ioe.printStackTrace();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 150 */     this.completions.sort((Comparator)this.comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidChar(char ch) {
/* 157 */     return (super.isValidChar(ch) || ch == ':');
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\jsp\JspCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */