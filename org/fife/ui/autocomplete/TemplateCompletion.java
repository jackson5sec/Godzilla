/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Position;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TemplateCompletion
/*     */   extends AbstractCompletion
/*     */   implements ParameterizedCompletion
/*     */ {
/*     */   private List<TemplatePiece> pieces;
/*     */   private String inputText;
/*     */   private String definitionString;
/*     */   private String shortDescription;
/*     */   private String summary;
/*     */   private List<ParameterizedCompletion.Parameter> params;
/*     */   
/*     */   public TemplateCompletion(CompletionProvider provider, String inputText, String definitionString, String template) {
/*  86 */     this(provider, inputText, definitionString, template, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TemplateCompletion(CompletionProvider provider, String inputText, String definitionString, String template, String shortDescription, String summary) {
/*  93 */     super(provider);
/*  94 */     this.inputText = inputText;
/*  95 */     this.definitionString = definitionString;
/*  96 */     this.shortDescription = shortDescription;
/*  97 */     this.summary = summary;
/*  98 */     this.pieces = new ArrayList<>(3);
/*  99 */     this.params = new ArrayList<>(3);
/* 100 */     parse(template);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addTemplatePiece(TemplatePiece piece) {
/* 105 */     this.pieces.add(piece);
/* 106 */     if (piece instanceof TemplatePiece.Param && !"cursor".equals(piece.getText())) {
/* 107 */       String type = null;
/* 108 */       ParameterizedCompletion.Parameter param = new ParameterizedCompletion.Parameter(type, piece.getText());
/* 109 */       this.params.add(param);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInputText() {
/* 116 */     return this.inputText;
/*     */   }
/*     */ 
/*     */   
/*     */   private String getPieceText(int index, String leadingWS) {
/* 121 */     TemplatePiece piece = this.pieces.get(index);
/* 122 */     String text = piece.getText();
/* 123 */     if (text.indexOf('\n') > -1) {
/* 124 */       text = text.replaceAll("\n", "\n" + leadingWS);
/*     */     }
/* 126 */     return text;
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
/*     */   public String getReplacementText() {
/* 138 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/* 144 */     return this.summary;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefinitionString() {
/* 150 */     return this.definitionString;
/*     */   }
/*     */   
/*     */   public String getShortDescription() {
/* 154 */     return this.shortDescription;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getShowParameterToolTip() {
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterizedCompletionInsertionInfo getInsertionInfo(JTextComponent tc, boolean replaceTabsWithSpaces) {
/*     */     String leadingWS;
/* 171 */     ParameterizedCompletionInsertionInfo info = new ParameterizedCompletionInsertionInfo();
/*     */ 
/*     */     
/* 174 */     StringBuilder sb = new StringBuilder();
/* 175 */     int dot = tc.getCaretPosition();
/*     */ 
/*     */ 
/*     */     
/* 179 */     int minPos = dot;
/* 180 */     Position maxPos = null;
/* 181 */     int defaultEndOffs = -1;
/*     */     try {
/* 183 */       maxPos = tc.getDocument().createPosition(dot);
/* 184 */     } catch (BadLocationException ble) {
/* 185 */       ble.printStackTrace();
/*     */     } 
/* 187 */     info.setCaretRange(minPos, maxPos);
/* 188 */     int selStart = dot;
/* 189 */     int selEnd = selStart;
/*     */     
/* 191 */     Document doc = tc.getDocument();
/*     */     
/*     */     try {
/* 194 */       leadingWS = RSyntaxUtilities.getLeadingWhitespace(doc, dot);
/* 195 */     } catch (BadLocationException ble) {
/* 196 */       ble.printStackTrace();
/* 197 */       leadingWS = "";
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 202 */     int start = dot;
/* 203 */     for (int i = 0; i < this.pieces.size(); i++) {
/* 204 */       TemplatePiece piece = this.pieces.get(i);
/* 205 */       String text = getPieceText(i, leadingWS);
/* 206 */       if (piece instanceof TemplatePiece.Text) {
/* 207 */         if (replaceTabsWithSpaces) {
/* 208 */           start = possiblyReplaceTabsWithSpaces(sb, text, tc, start);
/*     */         } else {
/*     */           
/* 211 */           sb.append(text);
/* 212 */           start += text.length();
/*     */         }
/*     */       
/* 215 */       } else if (piece instanceof TemplatePiece.Param && "cursor".equals(text)) {
/* 216 */         defaultEndOffs = start;
/*     */       } else {
/*     */         
/* 219 */         int end = start + text.length();
/* 220 */         sb.append(text);
/* 221 */         if (piece instanceof TemplatePiece.Param) {
/* 222 */           info.addReplacementLocation(start, end);
/* 223 */           if (selStart == dot) {
/* 224 */             selStart = start;
/* 225 */             selEnd = selStart + text.length();
/*     */           }
/*     */         
/* 228 */         } else if (piece instanceof TemplatePiece.ParamCopy) {
/* 229 */           info.addReplacementCopy(piece.getText(), start, end);
/*     */         } 
/* 231 */         start = end;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 237 */     if (selStart == minPos && selStart == selEnd && getParamCount() == 0 && 
/* 238 */       defaultEndOffs > -1) {
/* 239 */       selStart = selEnd = defaultEndOffs;
/*     */     }
/*     */     
/* 242 */     info.setInitialSelection(selStart, selEnd);
/*     */     
/* 244 */     if (defaultEndOffs > -1)
/*     */     {
/* 246 */       info.addReplacementLocation(defaultEndOffs, defaultEndOffs);
/*     */     }
/* 248 */     info.setDefaultEndOffs(defaultEndOffs);
/* 249 */     info.setTextToInsert(sb.toString());
/*     */     
/* 251 */     return info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterizedCompletion.Parameter getParam(int index) {
/* 261 */     return this.params.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getParamCount() {
/* 270 */     return (this.params == null) ? 0 : this.params.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isParamDefined(String name) {
/* 281 */     for (int i = 0; i < getParamCount(); i++) {
/* 282 */       ParameterizedCompletion.Parameter param = getParam(i);
/* 283 */       if (name.equals(param.getName())) {
/* 284 */         return true;
/*     */       }
/*     */     } 
/* 287 */     return false;
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
/*     */   private void parse(String template) {
/* 299 */     int lastOffs = 0;
/*     */     int offs;
/* 301 */     while ((offs = template.indexOf('$', lastOffs)) > -1 && offs < template.length() - 1) {
/*     */       int closingCurly;
/* 303 */       char next = template.charAt(offs + 1);
/* 304 */       switch (next) {
/*     */         case '$':
/* 306 */           addTemplatePiece(new TemplatePiece.Text(template
/* 307 */                 .substring(lastOffs, offs + 1)));
/* 308 */           lastOffs = offs + 2;
/*     */         
/*     */         case '{':
/* 311 */           closingCurly = template.indexOf('}', offs + 2);
/* 312 */           if (closingCurly > -1) {
/* 313 */             addTemplatePiece(new TemplatePiece.Text(template
/* 314 */                   .substring(lastOffs, offs)));
/* 315 */             String varName = template.substring(offs + 2, closingCurly);
/* 316 */             if (!"cursor".equals(varName) && isParamDefined(varName)) {
/* 317 */               addTemplatePiece(new TemplatePiece.ParamCopy(varName));
/*     */             } else {
/*     */               
/* 320 */               addTemplatePiece(new TemplatePiece.Param(varName));
/*     */             } 
/* 322 */             lastOffs = closingCurly + 1;
/*     */           } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 331 */     if (lastOffs < template.length()) {
/* 332 */       String text = template.substring(lastOffs);
/* 333 */       addTemplatePiece(new TemplatePiece.Text(text));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int possiblyReplaceTabsWithSpaces(StringBuilder sb, String text, JTextComponent tc, int start) {
/* 342 */     int tab = text.indexOf('\t');
/* 343 */     if (tab > -1) {
/*     */       
/* 345 */       int startLen = sb.length();
/*     */       
/* 347 */       int size = 4;
/* 348 */       Document doc = tc.getDocument();
/* 349 */       if (doc != null) {
/* 350 */         Integer integer = (Integer)doc.getProperty("tabSize");
/* 351 */         if (integer != null) {
/* 352 */           size = integer.intValue();
/*     */         }
/*     */       } 
/*     */       
/* 356 */       StringBuilder sb2 = new StringBuilder();
/* 357 */       for (int i = 0; i < size; i++) {
/* 358 */         sb2.append(' ');
/*     */       }
/* 360 */       String tabStr = sb2.toString();
/*     */       
/* 362 */       int lastOffs = 0;
/*     */       do {
/* 364 */         sb.append(text, lastOffs, tab);
/* 365 */         sb.append(tabStr);
/* 366 */         lastOffs = tab + 1;
/* 367 */       } while ((tab = text.indexOf('\t', lastOffs)) > -1);
/* 368 */       sb.append(text.substring(lastOffs));
/*     */       
/* 370 */       start += sb.length() - startLen;
/*     */     }
/*     */     else {
/*     */       
/* 374 */       sb.append(text);
/* 375 */       start += text.length();
/*     */     } 
/*     */     
/* 378 */     return start;
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
/*     */   public void setShortDescription(String shortDesc) {
/* 390 */     this.shortDescription = shortDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 396 */     return getDefinitionString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\TemplateCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */