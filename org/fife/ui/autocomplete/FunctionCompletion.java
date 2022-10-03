/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Position;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FunctionCompletion
/*     */   extends VariableCompletion
/*     */   implements ParameterizedCompletion
/*     */ {
/*     */   private List<ParameterizedCompletion.Parameter> params;
/*     */   private String returnValDesc;
/*     */   private String compareString;
/*     */   
/*     */   public FunctionCompletion(CompletionProvider provider, String name, String returnType) {
/*  57 */     super(provider, name, returnType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addDefinitionString(StringBuilder sb) {
/*  63 */     sb.append("<html><b>");
/*  64 */     sb.append(getDefinitionString());
/*  65 */     sb.append("</b>");
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
/*     */   protected void addParameters(StringBuilder sb) {
/*  78 */     int paramCount = getParamCount();
/*  79 */     if (paramCount > 0) {
/*  80 */       sb.append("<b>Parameters:</b><br>");
/*  81 */       sb.append("<center><table width='90%'><tr><td>");
/*  82 */       for (int i = 0; i < paramCount; i++) {
/*  83 */         ParameterizedCompletion.Parameter param = getParam(i);
/*  84 */         sb.append("<b>");
/*  85 */         sb.append((param.getName() != null) ? param.getName() : param
/*  86 */             .getType());
/*  87 */         sb.append("</b>&nbsp;");
/*  88 */         String desc = param.getDescription();
/*  89 */         if (desc != null) {
/*  90 */           sb.append(desc);
/*     */         }
/*  92 */         sb.append("<br>");
/*     */       } 
/*  94 */       sb.append("</td></tr></table></center><br><br>");
/*     */     } 
/*     */     
/*  97 */     if (this.returnValDesc != null) {
/*  98 */       sb.append("<b>Returns:</b><br><center><table width='90%'><tr><td>");
/*  99 */       sb.append(this.returnValDesc);
/* 100 */       sb.append("</td></tr></table></center><br><br>");
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
/*     */   public int compareTo(Completion c2) {
/*     */     int rc;
/* 117 */     if (c2 == this) {
/* 118 */       rc = 0;
/*     */     
/*     */     }
/* 121 */     else if (c2 instanceof FunctionCompletion) {
/* 122 */       rc = getCompareString().compareTo(((FunctionCompletion)c2)
/* 123 */           .getCompareString());
/*     */     }
/*     */     else {
/*     */       
/* 127 */       rc = super.compareTo(c2);
/*     */     } 
/*     */     
/* 130 */     return rc;
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
/*     */   public boolean equals(Object other) {
/* 143 */     return (other instanceof Completion && compareTo((Completion)other) == 0);
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
/*     */   private String getCompareString() {
/* 162 */     if (this.compareString == null) {
/* 163 */       StringBuilder sb = new StringBuilder(getName());
/*     */       
/* 165 */       int paramCount = getParamCount();
/* 166 */       if (paramCount < 10) {
/* 167 */         sb.append('0');
/*     */       }
/* 169 */       sb.append(paramCount);
/* 170 */       for (int i = 0; i < paramCount; i++) {
/* 171 */         String type = getParam(i).getType();
/* 172 */         sb.append(type);
/* 173 */         if (i < paramCount - 1) {
/* 174 */           sb.append(',');
/*     */         }
/*     */       } 
/* 177 */       this.compareString = sb.toString();
/*     */     } 
/*     */     
/* 180 */     return this.compareString;
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
/*     */   public String getDefinitionString() {
/* 195 */     StringBuilder sb = new StringBuilder();
/*     */ 
/*     */     
/* 198 */     String type = getType();
/* 199 */     if (type != null) {
/* 200 */       sb.append(type).append(' ');
/*     */     }
/*     */ 
/*     */     
/* 204 */     sb.append(getName());
/*     */ 
/*     */     
/* 207 */     CompletionProvider provider = getProvider();
/* 208 */     char start = provider.getParameterListStart();
/* 209 */     if (start != '\000') {
/* 210 */       sb.append(start);
/*     */     }
/* 212 */     for (int i = 0; i < getParamCount(); i++) {
/* 213 */       ParameterizedCompletion.Parameter param = getParam(i);
/* 214 */       type = param.getType();
/* 215 */       String name = param.getName();
/* 216 */       if (type != null) {
/* 217 */         sb.append(type);
/* 218 */         if (name != null) {
/* 219 */           sb.append(' ');
/*     */         }
/*     */       } 
/* 222 */       if (name != null) {
/* 223 */         sb.append(name);
/*     */       }
/* 225 */       if (i < this.params.size() - 1) {
/* 226 */         sb.append(provider.getParameterListSeparator());
/*     */       }
/*     */     } 
/* 229 */     char end = provider.getParameterListEnd();
/* 230 */     if (end != '\000') {
/* 231 */       sb.append(end);
/*     */     }
/*     */     
/* 234 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterizedCompletionInsertionInfo getInsertionInfo(JTextComponent tc, boolean replaceTabsWithSpaces) {
/* 243 */     ParameterizedCompletionInsertionInfo info = new ParameterizedCompletionInsertionInfo();
/*     */ 
/*     */     
/* 246 */     StringBuilder sb = new StringBuilder();
/* 247 */     char paramListStart = getProvider().getParameterListStart();
/* 248 */     if (paramListStart != '\000') {
/* 249 */       sb.append(paramListStart);
/*     */     }
/* 251 */     int dot = tc.getCaretPosition() + sb.length();
/* 252 */     int paramCount = getParamCount();
/*     */ 
/*     */ 
/*     */     
/* 256 */     int minPos = dot;
/* 257 */     Position maxPos = null;
/*     */     try {
/* 259 */       maxPos = tc.getDocument().createPosition(dot - sb.length() + 1);
/* 260 */     } catch (BadLocationException ble) {
/* 261 */       ble.printStackTrace();
/*     */     } 
/* 263 */     info.setCaretRange(minPos, maxPos);
/* 264 */     int firstParamLen = 0;
/*     */ 
/*     */ 
/*     */     
/* 268 */     int start = dot;
/* 269 */     for (int i = 0; i < paramCount; i++) {
/* 270 */       ParameterizedCompletion.Parameter param = getParam(i);
/* 271 */       String paramText = getParamText(param);
/* 272 */       if (i == 0) {
/* 273 */         firstParamLen = paramText.length();
/*     */       }
/* 275 */       sb.append(paramText);
/* 276 */       int end = start + paramText.length();
/* 277 */       info.addReplacementLocation(start, end);
/*     */ 
/*     */       
/* 280 */       String sep = getProvider().getParameterListSeparator();
/* 281 */       if (i < paramCount - 1 && sep != null) {
/* 282 */         sb.append(sep);
/* 283 */         start = end + sep.length();
/*     */       } 
/*     */     } 
/* 286 */     sb.append(getProvider().getParameterListEnd());
/* 287 */     int endOffs = dot + sb.length();
/* 288 */     endOffs--;
/* 289 */     info.addReplacementLocation(endOffs, endOffs);
/* 290 */     info.setDefaultEndOffs(endOffs);
/*     */     
/* 292 */     int selectionEnd = (paramCount > 0) ? (dot + firstParamLen) : dot;
/* 293 */     info.setInitialSelection(dot, selectionEnd);
/* 294 */     info.setTextToInsert(sb.toString());
/* 295 */     return info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterizedCompletion.Parameter getParam(int index) {
/* 305 */     return this.params.get(index);
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
/*     */   public int getParamCount() {
/* 317 */     return (this.params == null) ? 0 : this.params.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getShowParameterToolTip() {
/* 326 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getParamText(ParameterizedCompletion.Parameter param) {
/* 337 */     String text = param.getName();
/* 338 */     if (text == null) {
/* 339 */       text = param.getType();
/* 340 */       if (text == null) {
/* 341 */         text = "arg";
/*     */       }
/*     */     } 
/* 344 */     return text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReturnValueDescription() {
/* 355 */     return this.returnValDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSummary() {
/* 364 */     StringBuilder sb = new StringBuilder();
/* 365 */     addDefinitionString(sb);
/* 366 */     if (!possiblyAddDescription(sb)) {
/* 367 */       sb.append("<br><br><br>");
/*     */     }
/* 369 */     addParameters(sb);
/* 370 */     possiblyAddDefinedIn(sb);
/* 371 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SuppressFBWarnings(value = {"RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE"}, justification = "Subclasses could return null")
/*     */   public String getToolTipText() {
/* 382 */     String text = getSummary();
/* 383 */     if (text == null) {
/* 384 */       text = getDefinitionString();
/*     */     }
/* 386 */     return text;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 392 */     int hashCode = super.hashCode();
/*     */     
/* 394 */     for (int i = 0; i < getParamCount(); i++) {
/* 395 */       hashCode ^= getParam(i).hashCode();
/*     */     }
/*     */     
/* 398 */     hashCode ^= (this.returnValDesc != null) ? this.returnValDesc.hashCode() : 0;
/* 399 */     hashCode ^= (this.compareString != null) ? this.compareString.hashCode() : 0;
/*     */     
/* 401 */     return hashCode;
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
/*     */   public void setParams(List<ParameterizedCompletion.Parameter> params) {
/* 414 */     if (params != null)
/*     */     {
/* 416 */       this.params = new ArrayList<>(params);
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
/*     */   public void setReturnValueDescription(String desc) {
/* 428 */     this.returnValDesc = desc;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\FunctionCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */