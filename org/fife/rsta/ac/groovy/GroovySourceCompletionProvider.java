/*     */ package org.fife.rsta.ac.groovy;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ac.common.CodeBlock;
/*     */ import org.fife.rsta.ac.common.TokenScanner;
/*     */ import org.fife.rsta.ac.common.VariableDeclaration;
/*     */ import org.fife.rsta.ac.java.JarManager;
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
/*     */ public class GroovySourceCompletionProvider
/*     */   extends DefaultCompletionProvider
/*     */ {
/*  38 */   private static final char[] KEYWORD_DEF = new char[] { 'd', 'e', 'f' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroovySourceCompletionProvider() {
/*  45 */     this((JarManager)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GroovySourceCompletionProvider(JarManager jarManager) {
/*  55 */     if (jarManager == null) {
/*  56 */       jarManager = new JarManager();
/*     */     }
/*     */     
/*  59 */     setParameterizedCompletionParams('(', ", ", ')');
/*  60 */     setAutoActivationRules(false, ".");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CodeBlock createAst(JTextComponent comp) {
/*  67 */     CodeBlock ast = new CodeBlock(0);
/*     */     
/*  69 */     RSyntaxTextArea textArea = (RSyntaxTextArea)comp;
/*  70 */     TokenScanner scanner = new TokenScanner(textArea);
/*  71 */     parseCodeBlock(scanner, ast);
/*     */     
/*  73 */     return ast;
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
/*  85 */     this.completions.clear();
/*     */     
/*  87 */     CodeBlock ast = createAst(comp);
/*     */     
/*  89 */     int dot = comp.getCaretPosition();
/*  90 */     recursivelyAddLocalVars(this.completions, ast, dot);
/*     */     
/*  92 */     Collections.sort(this.completions);
/*     */ 
/*     */     
/*  95 */     String text = getAlreadyEnteredText(comp);
/*     */     
/*  97 */     int start = Collections.binarySearch(this.completions, text, (Comparator<? super String>)this.comparator);
/*  98 */     if (start < 0) {
/*  99 */       start = -(start + 1);
/*     */     }
/*     */     else {
/*     */       
/* 103 */       while (start > 0 && this.comparator
/* 104 */         .compare(this.completions.get(start - 1), text) == 0) {
/* 105 */         start--;
/*     */       }
/*     */     } 
/*     */     
/* 109 */     int end = Collections.binarySearch(this.completions, text + '{', (Comparator<? super String>)this.comparator);
/* 110 */     end = -(end + 1);
/*     */     
/* 112 */     return this.completions.subList(start, end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidChar(char ch) {
/* 122 */     return (Character.isJavaIdentifierPart(ch) || ch == '.');
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseCodeBlock(TokenScanner scanner, CodeBlock block) {
/* 128 */     Token t = scanner.next();
/* 129 */     while (t != null) {
/* 130 */       if (t.isRightCurly()) {
/* 131 */         block.setEndOffset(t.getOffset()); return;
/*     */       } 
/* 133 */       if (t.isLeftCurly()) {
/* 134 */         CodeBlock child = block.addChildCodeBlock(t.getOffset());
/* 135 */         parseCodeBlock(scanner, child);
/* 136 */       } else if (t.is(6, KEYWORD_DEF)) {
/* 137 */         t = scanner.next();
/* 138 */         if (t != null) {
/*     */           
/* 140 */           VariableDeclaration varDec = new VariableDeclaration(t.getLexeme(), t.getOffset());
/* 141 */           block.addVariable(varDec);
/*     */         } 
/*     */       } 
/* 144 */       t = scanner.next();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void recursivelyAddLocalVars(List<Completion> completions, CodeBlock block, int dot) {
/* 153 */     if (!block.contains(dot)) {
/*     */       return;
/*     */     }
/*     */     
/*     */     int i;
/* 158 */     for (i = 0; i < block.getVariableDeclarationCount(); ) {
/* 159 */       VariableDeclaration dec = block.getVariableDeclaration(i);
/* 160 */       int decOffs = dec.getOffset();
/* 161 */       if (decOffs < dot) {
/* 162 */         BasicCompletion c = new BasicCompletion((CompletionProvider)this, dec.getName());
/* 163 */         completions.add(c);
/*     */ 
/*     */         
/*     */         i++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 171 */     for (i = 0; i < block.getChildCodeBlockCount(); i++) {
/* 172 */       CodeBlock child = block.getChildCodeBlock(i);
/* 173 */       if (child.contains(dot)) {
/* 174 */         recursivelyAddLocalVars(completions, child, dot);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\groovy\GroovySourceCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */