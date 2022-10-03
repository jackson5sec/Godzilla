/*     */ package org.fife.rsta.ac.js;
/*     */ 
/*     */ import javax.swing.text.BadLocationException;
/*     */ import org.fife.rsta.ac.js.ast.JavaScriptDeclaration;
/*     */ import org.fife.rsta.ac.js.ast.JavaScriptFunctionDeclaration;
/*     */ import org.fife.rsta.ac.js.ast.JavaScriptVariableDeclaration;
/*     */ import org.fife.rsta.ac.js.ast.VariableResolver;
/*     */ import org.fife.ui.rsyntaxtextarea.LinkGenerator;
/*     */ import org.fife.ui.rsyntaxtextarea.LinkGeneratorResult;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*     */ import org.fife.ui.rsyntaxtextarea.SelectRegionLinkGeneratorResult;
/*     */ import org.fife.ui.rsyntaxtextarea.Token;
/*     */ import org.fife.ui.rsyntaxtextarea.TokenImpl;
/*     */ 
/*     */ public class JavaScriptLinkGenerator
/*     */   implements LinkGenerator
/*     */ {
/*     */   private JavaScriptLanguageSupport language;
/*     */   private boolean findLocal;
/*     */   private boolean findPreprocessed;
/*     */   private boolean findSystem;
/*     */   
/*     */   public JavaScriptLinkGenerator(JavaScriptLanguageSupport language) {
/*  25 */     this.language = language;
/*  26 */     this.findLocal = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkGeneratorResult isLinkAtOffset(RSyntaxTextArea textArea, int offs) {
/*  35 */     JavaScriptDeclaration dec = null;
/*  36 */     IsLinkableCheckResult result = checkForLinkableToken(textArea, offs);
/*  37 */     if (result != null) {
/*     */       JavaScriptFunctionDeclaration javaScriptFunctionDeclaration;
/*  39 */       Token t = result.token;
/*  40 */       boolean function = result.function;
/*  41 */       String name = t.getLexeme();
/*  42 */       if (name != null && name.length() > 0)
/*     */       {
/*     */         
/*  45 */         if (name.length() > 1 || (name.length() == 1 && Character.isJavaIdentifierPart(name.charAt(0))))
/*     */         {
/*  47 */           this.language.reparseDocument(offs);
/*     */         }
/*     */       }
/*  50 */       JavaScriptParser parser = this.language.getJavaScriptParser();
/*  51 */       VariableResolver variableResolver = parser.getVariablesAndFunctions();
/*     */       
/*  53 */       if (variableResolver != null)
/*     */       {
/*  55 */         if (!function) {
/*  56 */           JavaScriptVariableDeclaration javaScriptVariableDeclaration = variableResolver.findDeclaration(name, offs, this.findLocal, this.findPreprocessed, this.findSystem);
/*     */         } else {
/*     */           
/*  59 */           String lookup = getLookupNameForFunction(textArea, offs, name);
/*     */           
/*  61 */           javaScriptFunctionDeclaration = variableResolver.findFunctionDeclaration(lookup, this.findLocal, this.findPreprocessed);
/*  62 */           if (javaScriptFunctionDeclaration == null) {
/*  63 */             javaScriptFunctionDeclaration = variableResolver.findFunctionDeclarationByFunctionName(name, this.findLocal, this.findPreprocessed);
/*     */           }
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*  69 */       if (javaScriptFunctionDeclaration != null) {
/*  70 */         return createSelectedRegionResult(textArea, t, (JavaScriptDeclaration)javaScriptFunctionDeclaration);
/*     */       }
/*     */     } 
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LinkGeneratorResult createSelectedRegionResult(RSyntaxTextArea textArea, Token t, JavaScriptDeclaration dec) {
/*  80 */     if (dec.getTypeDeclarationOptions() != null && !dec.getTypeDeclarationOptions().isSupportsLinks()) {
/*  81 */       return null;
/*     */     }
/*  83 */     return (LinkGeneratorResult)new SelectRegionLinkGeneratorResult(textArea, t.getOffset(), dec.getStartOffSet(), dec.getEndOffset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFindLocal(boolean find) {
/*  90 */     this.findLocal = find;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFindPreprocessed(boolean find) {
/*  97 */     this.findPreprocessed = find;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFindSystem(boolean find) {
/* 104 */     this.findSystem = find;
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
/*     */   private String getLookupNameForFunction(RSyntaxTextArea textArea, int offs, String name) {
/* 121 */     StringBuilder temp = new StringBuilder();
/* 122 */     if (offs >= 0) {
/*     */       
/*     */       try {
/* 125 */         int line = textArea.getLineOfOffset(offs);
/*     */         
/* 127 */         Token first = wrapToken(textArea.getTokenListForLine(line));
/* 128 */         for (Token t = first; t != null && t.isPaintable(); t = wrapToken(t.getNextToken())) {
/* 129 */           if (t.containsPosition(offs)) {
/* 130 */             for (Token tt = t; tt != null && tt.isPaintable(); tt = wrapToken(tt.getNextToken())) {
/* 131 */               temp.append(tt.getLexeme());
/* 132 */               if (tt.isSingleChar(22, ')')) {
/*     */                 break;
/*     */               }
/*     */             } 
/*     */           }
/*     */         } 
/* 138 */       } catch (BadLocationException ble) {
/* 139 */         ble.printStackTrace();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 144 */     String function = temp.toString().replaceAll("\\s", "");
/* 145 */     boolean params = false;
/* 146 */     int count = 0;
/* 147 */     StringBuilder sb = new StringBuilder();
/* 148 */     for (int i = 0; i < function.length(); i++) {
/* 149 */       char ch = function.charAt(i);
/*     */       
/* 151 */       if (ch == '(') {
/* 152 */         params = true;
/* 153 */         count = 0;
/* 154 */         sb.append(ch);
/*     */       } else {
/*     */         
/* 157 */         if (ch == ')') {
/* 158 */           sb.append(ch);
/*     */           
/*     */           break;
/*     */         } 
/* 162 */         if (ch == ',') {
/* 163 */           count = 0;
/* 164 */           sb.append(ch);
/*     */ 
/*     */         
/*     */         }
/* 168 */         else if (params && count == 0) {
/* 169 */           sb.append('p');
/* 170 */           count++;
/*     */         
/*     */         }
/* 173 */         else if (!params) {
/* 174 */           sb.append(ch);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 180 */     return sb.toString();
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
/*     */   private IsLinkableCheckResult checkForLinkableToken(RSyntaxTextArea textArea, int offs) {
/* 196 */     IsLinkableCheckResult result = null;
/*     */     
/* 198 */     if (offs >= 0) {
/*     */       
/*     */       try {
/*     */         
/* 202 */         int line = textArea.getLineOfOffset(offs);
/* 203 */         Token first = wrapToken(textArea.getTokenListForLine(line));
/* 204 */         Token prev = null;
/*     */         
/* 206 */         for (Token t = first; t != null && t.isPaintable(); t = wrapToken(t
/* 207 */             .getNextToken())) {
/* 208 */           if (t.containsPosition(offs)) {
/*     */ 
/*     */ 
/*     */             
/* 212 */             Token token = wrapToken(t);
/*     */             
/* 214 */             boolean isFunction = false;
/*     */             
/* 216 */             if (prev != null && prev.isSingleChar('.')) {
/*     */               break;
/*     */             }
/*     */ 
/*     */             
/* 221 */             Token next = wrapToken(
/* 222 */                 RSyntaxUtilities.getNextImportantToken(t.getNextToken(), textArea, line));
/*     */             
/* 224 */             if (next != null && next
/* 225 */               .isSingleChar(22, '(')) {
/* 226 */               isFunction = true;
/*     */             }
/*     */             
/* 229 */             result = new IsLinkableCheckResult(token, isFunction);
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 234 */           if (!t.isCommentOrWhitespace()) {
/* 235 */             prev = t;
/*     */           }
/*     */         }
/*     */       
/*     */       }
/* 240 */       catch (BadLocationException ble) {
/* 241 */         ble.printStackTrace();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 246 */     return result;
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
/*     */   private Token wrapToken(Token token) {
/* 259 */     if (token != null)
/* 260 */       return (Token)new TokenImpl(token); 
/* 261 */     return token;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaScriptLanguageSupport getLanguage() {
/* 268 */     return this.language;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class IsLinkableCheckResult
/*     */   {
/*     */     private Token token;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean function;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private IsLinkableCheckResult(Token token, boolean function) {
/* 291 */       this.token = token;
/* 292 */       this.function = function;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\JavaScriptLinkGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */