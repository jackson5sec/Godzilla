/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import org.fife.rsta.ac.java.rjc.ast.CodeBlock;
/*     */ import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;
/*     */ import org.fife.rsta.ac.java.rjc.ast.FormalParameter;
/*     */ import org.fife.rsta.ac.java.rjc.ast.LocalVariable;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Member;
/*     */ import org.fife.rsta.ac.java.rjc.ast.Method;
/*     */ import org.fife.rsta.ac.java.rjc.ast.TypeDeclaration;
/*     */ import org.fife.ui.rsyntaxtextarea.LinkGenerator;
/*     */ import org.fife.ui.rsyntaxtextarea.LinkGeneratorResult;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*     */ import org.fife.ui.rsyntaxtextarea.SelectRegionLinkGeneratorResult;
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
/*     */ class JavaLinkGenerator
/*     */   implements LinkGenerator
/*     */ {
/*     */   private JavaLanguageSupport jls;
/*     */   
/*     */   JavaLinkGenerator(JavaLanguageSupport jls) {
/*  53 */     this.jls = jls;
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
/*     */   private IsLinkableCheckResult checkForLinkableToken(RSyntaxTextArea textArea, int offs) {
/*  68 */     IsLinkableCheckResult result = null;
/*     */     
/*  70 */     if (offs >= 0) {
/*     */       
/*     */       try {
/*     */         
/*  74 */         int line = textArea.getLineOfOffset(offs);
/*  75 */         Token first = textArea.getTokenListForLine(line);
/*  76 */         RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/*  77 */         Token prev = null;
/*     */         
/*  79 */         for (Token t = first; t != null && t.isPaintable(); t = t.getNextToken())
/*     */         {
/*  81 */           if (t.containsPosition(offs)) {
/*     */ 
/*     */ 
/*     */             
/*  85 */             TokenImpl tokenImpl = new TokenImpl(t);
/*  86 */             boolean isMethod = false;
/*     */             
/*  88 */             if (prev == null) {
/*  89 */               prev = RSyntaxUtilities.getPreviousImportantToken(doc, line - 1);
/*     */             }
/*     */             
/*  92 */             if (prev != null && prev.isSingleChar('.')) {
/*     */               break;
/*     */             }
/*     */ 
/*     */             
/*  97 */             Token next = RSyntaxUtilities.getNextImportantToken(t
/*  98 */                 .getNextToken(), textArea, line);
/*  99 */             if (next != null && next.isSingleChar(22, '(')) {
/* 100 */               isMethod = true;
/*     */             }
/*     */             
/* 103 */             result = new IsLinkableCheckResult((Token)tokenImpl, isMethod);
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 108 */           if (!t.isCommentOrWhitespace()) {
/* 109 */             prev = t;
/*     */           }
/*     */         }
/*     */       
/*     */       }
/* 114 */       catch (BadLocationException ble) {
/* 115 */         ble.printStackTrace();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 120 */     return result;
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
/*     */   public LinkGeneratorResult isLinkAtOffset(RSyntaxTextArea textArea, int offs) {
/* 132 */     int start = -1;
/* 133 */     int end = -1;
/*     */     
/* 135 */     IsLinkableCheckResult result = checkForLinkableToken(textArea, offs);
/* 136 */     if (result != null) {
/*     */       
/* 138 */       JavaParser parser = this.jls.getParser(textArea);
/* 139 */       CompilationUnit cu = parser.getCompilationUnit();
/* 140 */       Token t = result.token;
/* 141 */       boolean method = result.method;
/*     */       
/* 143 */       if (cu != null) {
/*     */         
/* 145 */         TypeDeclaration td = cu.getDeepestTypeDeclarationAtOffset(offs);
/* 146 */         boolean staticFieldsOnly = false;
/* 147 */         boolean deepestTypeDec = true;
/* 148 */         boolean deepestContainingMemberStatic = false;
/* 149 */         while (td != null && start == -1) {
/*     */ 
/*     */           
/* 152 */           if (!method && deepestTypeDec) {
/*     */             
/* 154 */             Iterator<Member> i = td.getMemberIterator();
/* 155 */             while (i.hasNext()) {
/*     */               
/* 157 */               Method m = null;
/* 158 */               Member member = i.next();
/* 159 */               CodeBlock block = null;
/*     */ 
/*     */               
/* 162 */               if (member instanceof Method) {
/* 163 */                 m = (Method)member;
/* 164 */                 if (m.getBodyContainsOffset(offs) && m.getBody() != null) {
/* 165 */                   deepestContainingMemberStatic = m.isStatic();
/* 166 */                   block = m.getBody().getDeepestCodeBlockContaining(offs);
/*     */                 }
/*     */               
/* 169 */               } else if (member instanceof CodeBlock) {
/* 170 */                 block = (CodeBlock)member;
/* 171 */                 deepestContainingMemberStatic = block.isStatic();
/* 172 */                 block = block.getDeepestCodeBlockContaining(offs);
/*     */               } 
/*     */ 
/*     */               
/* 176 */               if (block != null) {
/* 177 */                 String varName = t.getLexeme();
/*     */                 
/* 179 */                 List<LocalVariable> locals = block.getLocalVarsBefore(offs);
/* 180 */                 Collections.reverse(locals);
/* 181 */                 for (LocalVariable local : locals) {
/* 182 */                   if (varName.equals(local.getName())) {
/* 183 */                     start = local.getNameStartOffset();
/* 184 */                     end = local.getNameEndOffset();
/*     */                   } 
/*     */                 } 
/*     */                 
/* 188 */                 if (start == -1 && m != null) {
/* 189 */                   for (int j = 0; j < m.getParameterCount(); j++) {
/* 190 */                     FormalParameter p = m.getParameter(j);
/* 191 */                     if (varName.equals(p.getName())) {
/* 192 */                       start = p.getNameStartOffset();
/* 193 */                       end = p.getNameEndOffset();
/*     */                     } 
/*     */                   } 
/*     */                 }
/*     */ 
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           
/* 204 */           if (start == -1) {
/* 205 */             String varName = t.getLexeme();
/*     */             
/* 207 */             Iterator<? extends Member> i = method ? td.getMethodIterator() : td.getFieldIterator();
/* 208 */             while (i.hasNext()) {
/* 209 */               Member member = i.next();
/* 210 */               if (((!deepestContainingMemberStatic && !staticFieldsOnly) || member.isStatic()) && varName
/* 211 */                 .equals(member.getName())) {
/* 212 */                 start = member.getNameStartOffset();
/* 213 */                 end = member.getNameEndOffset();
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           
/* 220 */           if (start == -1) {
/* 221 */             staticFieldsOnly |= td.isStatic();
/*     */             
/* 223 */             td = td.getParentType();
/*     */             
/* 225 */             deepestTypeDec = false;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 232 */       if (start > -1) {
/* 233 */         return (LinkGeneratorResult)new SelectRegionLinkGeneratorResult(textArea, t.getOffset(), start, end);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 239 */     return null;
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
/*     */     private boolean method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private IsLinkableCheckResult(Token token, boolean method) {
/* 262 */       this.token = token;
/* 263 */       this.method = method;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\JavaLinkGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */