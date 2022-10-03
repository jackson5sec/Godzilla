/*     */ package org.fife.rsta.ac.perl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.c.CCompletionProvider;
/*     */ import org.fife.rsta.ac.common.CodeBlock;
/*     */ import org.fife.rsta.ac.common.TokenScanner;
/*     */ import org.fife.rsta.ac.common.VariableDeclaration;
/*     */ import org.fife.ui.autocomplete.BasicCompletion;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.DefaultCompletionProvider;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PerlCompletionProvider
/*     */   extends CCompletionProvider
/*     */ {
/*     */   private boolean useParensWithFunctions;
/*     */   
/*     */   protected void addShorthandCompletions(DefaultCompletionProvider codeCP) {}
/*     */   
/*     */   private CodeBlock createAst(RSyntaxTextArea textArea) {
/*  72 */     CodeBlock ast = new CodeBlock(0);
/*  73 */     TokenScanner scanner = new TokenScanner(textArea);
/*  74 */     parseCodeBlock(scanner, ast);
/*  75 */     return ast;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CompletionProvider createCodeCompletionProvider() {
/*  84 */     DefaultCompletionProvider cp = new PerlCodeCompletionProvider(this);
/*  85 */     loadCodeCompletionsFromXml(cp);
/*  86 */     addShorthandCompletions(cp);
/*  87 */     cp.setAutoActivationRules(true, null);
/*  88 */     return (CompletionProvider)cp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CompletionProvider createStringCompletionProvider() {
/*  98 */     DefaultCompletionProvider cp = new DefaultCompletionProvider();
/*  99 */     cp.setAutoActivationRules(true, null);
/* 100 */     return (CompletionProvider)cp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Completion> getCompletionsImpl(JTextComponent comp) {
/* 110 */     List<Completion> completions = super.getCompletionsImpl(comp);
/*     */     
/* 112 */     SortedSet<Completion> varCompletions = getVariableCompletions(comp);
/* 113 */     if (varCompletions != null) {
/* 114 */       completions.addAll(varCompletions);
/* 115 */       Collections.sort(completions);
/*     */     } 
/*     */     
/* 118 */     return completions;
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
/*     */   public char getParameterListEnd() {
/* 133 */     return getUseParensWithFunctions() ? ')' : Character.MIN_VALUE;
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
/*     */   public char getParameterListStart() {
/* 147 */     return getUseParensWithFunctions() ? '(' : ' ';
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
/*     */   public boolean getUseParensWithFunctions() {
/* 159 */     return this.useParensWithFunctions;
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
/*     */   private SortedSet<Completion> getVariableCompletions(JTextComponent comp) {
/* 174 */     RSyntaxTextArea textArea = (RSyntaxTextArea)comp;
/* 175 */     int dot = textArea.getCaretPosition();
/* 176 */     SortedSet<Completion> varCompletions = new TreeSet<>(this.comparator);
/*     */     
/* 178 */     CompletionProvider p = getDefaultCompletionProvider();
/* 179 */     String text = p.getAlreadyEnteredText(comp);
/* 180 */     char firstChar = (text.length() == 0) ? Character.MIN_VALUE : text.charAt(0);
/* 181 */     if (firstChar != '$' && firstChar != '@' && firstChar != '%') {
/* 182 */       System.out.println("DEBUG: No use matching variables, exiting");
/* 183 */       return null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 188 */     CodeBlock block = createAst(textArea);
/* 189 */     recursivelyAddLocalVars(varCompletions, block, dot, firstChar);
/*     */ 
/*     */     
/* 192 */     if (varCompletions.size() > 0) {
/* 193 */       BasicCompletion basicCompletion1 = new BasicCompletion(p, text);
/* 194 */       BasicCompletion basicCompletion2 = new BasicCompletion(p, text + '{');
/* 195 */       varCompletions = (SortedSet)varCompletions.subSet(basicCompletion1, basicCompletion2);
/*     */     } 
/*     */     
/* 198 */     return varCompletions;
/*     */   }
/*     */ 
/*     */   
/* 202 */   private CaseInsensitiveComparator comparator = new CaseInsensitiveComparator();
/*     */ 
/*     */   
/*     */   private static class CaseInsensitiveComparator
/*     */     implements Comparator<Completion>, Serializable
/*     */   {
/*     */     private CaseInsensitiveComparator() {}
/*     */ 
/*     */     
/*     */     public int compare(Completion c1, Completion c2) {
/* 212 */       String s1 = c1.getInputText();
/* 213 */       String s2 = c2.getInputText();
/* 214 */       return String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
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
/*     */   protected String getXmlResource() {
/* 226 */     return "data/perl5.xml";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseCodeBlock(TokenScanner scanner, CodeBlock block) {
/* 237 */     Token t = scanner.next();
/* 238 */     while (t != null) {
/* 239 */       if (t.isRightCurly()) {
/* 240 */         block.setEndOffset(t.getOffset());
/*     */         return;
/*     */       } 
/* 243 */       if (t.isLeftCurly()) {
/* 244 */         CodeBlock child = block.addChildCodeBlock(t.getOffset());
/* 245 */         parseCodeBlock(scanner, child);
/*     */       }
/* 247 */       else if (t.getType() == 17) {
/*     */         
/* 249 */         VariableDeclaration varDec = new VariableDeclaration(t.getLexeme(), t.getOffset());
/* 250 */         block.addVariable(varDec);
/*     */       } 
/* 252 */       t = scanner.next();
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
/*     */   private void recursivelyAddLocalVars(SortedSet<Completion> completions, CodeBlock block, int dot, int firstChar) {
/* 269 */     if (!block.contains(dot)) {
/*     */       return;
/*     */     }
/*     */     
/*     */     int i;
/* 274 */     for (i = 0; i < block.getVariableDeclarationCount(); ) {
/* 275 */       VariableDeclaration dec = block.getVariableDeclaration(i);
/* 276 */       int decOffs = dec.getOffset();
/* 277 */       if (decOffs < dot) {
/* 278 */         String name = dec.getName();
/* 279 */         char ch = name.charAt(0);
/* 280 */         if (firstChar <= ch) {
/* 281 */           if (firstChar < ch) {
/* 282 */             name = firstChar + name.substring(1);
/*     */           }
/* 284 */           BasicCompletion c = new BasicCompletion((CompletionProvider)this, name);
/* 285 */           completions.add(c);
/*     */         } 
/*     */ 
/*     */         
/*     */         i++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 294 */     for (i = 0; i < block.getChildCodeBlockCount(); i++) {
/* 295 */       CodeBlock child = block.getChildCodeBlock(i);
/* 296 */       if (child.contains(dot)) {
/* 297 */         recursivelyAddLocalVars(completions, child, dot, firstChar);
/*     */         return;
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
/*     */   public void setUseParensWithFunctions(boolean use) {
/* 312 */     this.useParensWithFunctions = use;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\perl\PerlCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */