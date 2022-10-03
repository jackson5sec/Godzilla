/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.text.StringCharacterIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.fife.rsta.ac.java.buildpath.SourceLocation;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Util
/*     */ {
/*  41 */   static final Pattern DOC_COMMENT_LINE_HEADER = Pattern.compile("\\s*\\n\\s*\\*");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   static final Pattern LINK_TAG_MEMBER_PATTERN = Pattern.compile("(?:\\w+\\.)*\\w+(?:#\\w+(?:\\([^\\)]*\\))?)?|#\\w+(?:\\([^\\)]*\\))?");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static CompilationUnit lastCUFromDisk;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static SourceLocation lastCUFileParam;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ClassFile lastCUClassFileParam;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void appendDocCommentTail(StringBuilder sb, StringBuilder tail) {
/*  88 */     StringBuilder params = null;
/*  89 */     StringBuilder returns = null;
/*  90 */     StringBuilder throwsItems = null;
/*  91 */     StringBuilder see = null;
/*  92 */     StringBuilder seeTemp = null;
/*  93 */     StringBuilder since = null;
/*  94 */     StringBuilder author = null;
/*  95 */     StringBuilder version = null;
/*  96 */     StringBuilder unknowns = null;
/*  97 */     boolean inParams = false, inThrows = false;
/*  98 */     boolean inReturns = false, inSeeAlso = false;
/*  99 */     boolean inSince = false, inAuthor = false;
/* 100 */     boolean inVersion = false, inUnknowns = false;
/*     */     
/* 102 */     String[] st = tail.toString().split("[ \t\r\n\f]+");
/*     */ 
/*     */     
/* 105 */     int i = 0; String token;
/* 106 */     while (i < st.length && (token = st[i++]) != null) {
/* 107 */       if ("@param".equals(token) && i < st.length) {
/* 108 */         token = st[i++];
/* 109 */         if (params == null) {
/* 110 */           params = new StringBuilder("<b>Parameters:</b><p class='indented'>");
/*     */         } else {
/*     */           
/* 113 */           params.append("<br>");
/*     */         } 
/* 115 */         params.append("<b>").append(token).append("</b> ");
/* 116 */         inSeeAlso = false;
/* 117 */         inParams = true;
/* 118 */         inReturns = false;
/* 119 */         inThrows = false;
/* 120 */         inSince = false;
/* 121 */         inAuthor = false;
/* 122 */         inVersion = false;
/* 123 */         inUnknowns = false; continue;
/*     */       } 
/* 125 */       if ("@return".equals(token) && i < st.length) {
/* 126 */         if (returns == null) {
/* 127 */           returns = new StringBuilder("<b>Returns:</b><p class='indented'>");
/*     */         }
/* 129 */         inSeeAlso = false;
/* 130 */         inReturns = true;
/* 131 */         inParams = false;
/* 132 */         inThrows = false;
/* 133 */         inSince = false;
/* 134 */         inAuthor = false;
/* 135 */         inVersion = false;
/* 136 */         inUnknowns = false; continue;
/*     */       } 
/* 138 */       if ("@see".equals(token) && i < st.length) {
/* 139 */         if (see == null) {
/* 140 */           see = new StringBuilder("<b>See Also:</b><p class='indented'>");
/* 141 */           seeTemp = new StringBuilder();
/*     */         } else {
/*     */           
/* 144 */           if (seeTemp.length() > 0) {
/* 145 */             String temp = seeTemp.substring(0, seeTemp.length() - 1);
/*     */             
/* 147 */             appendLinkTagText(see, temp);
/*     */           } 
/* 149 */           see.append("<br>");
/* 150 */           seeTemp.setLength(0);
/*     */         } 
/*     */         
/* 153 */         inSeeAlso = true;
/* 154 */         inReturns = false;
/* 155 */         inParams = false;
/* 156 */         inThrows = false;
/* 157 */         inSince = false;
/* 158 */         inAuthor = false;
/* 159 */         inVersion = false;
/* 160 */         inUnknowns = false; continue;
/*     */       } 
/* 162 */       if ("@throws".equals(token) || ("@exception"
/* 163 */         .equals(token) && i < st.length)) {
/* 164 */         token = st[i++];
/* 165 */         if (throwsItems == null) {
/* 166 */           throwsItems = new StringBuilder("<b>Throws:</b><p class='indented'>");
/*     */         } else {
/*     */           
/* 169 */           throwsItems.append("<br>");
/*     */         } 
/* 171 */         throwsItems.append("<b>").append(token).append("</b> ");
/* 172 */         inSeeAlso = false;
/* 173 */         inParams = false;
/* 174 */         inReturns = false;
/* 175 */         inThrows = true;
/* 176 */         inSince = false;
/* 177 */         inAuthor = false;
/* 178 */         inVersion = false;
/* 179 */         inUnknowns = false; continue;
/*     */       } 
/* 181 */       if ("@since".equals(token) && i < st.length) {
/* 182 */         if (since == null) {
/* 183 */           since = new StringBuilder("<b>Since:</b><p class='indented'>");
/*     */         }
/* 185 */         inSeeAlso = false;
/* 186 */         inReturns = false;
/* 187 */         inParams = false;
/* 188 */         inThrows = false;
/* 189 */         inSince = true;
/* 190 */         inAuthor = false;
/* 191 */         inVersion = false;
/* 192 */         inUnknowns = false; continue;
/*     */       } 
/* 194 */       if ("@author".equals(token) && i < st.length) {
/* 195 */         if (author == null) {
/* 196 */           author = new StringBuilder("<b>Author:</b><p class='indented'>");
/*     */         } else {
/*     */           
/* 199 */           author.append("<br>");
/*     */         } 
/* 201 */         inSeeAlso = false;
/* 202 */         inReturns = false;
/* 203 */         inParams = false;
/* 204 */         inThrows = false;
/* 205 */         inSince = false;
/* 206 */         inAuthor = true;
/* 207 */         inVersion = false;
/* 208 */         inUnknowns = false; continue;
/*     */       } 
/* 210 */       if ("@version".equals(token) && i < st.length) {
/* 211 */         if (version == null) {
/* 212 */           version = new StringBuilder("<b>Version:</b><p class='indented'>");
/*     */         } else {
/*     */           
/* 215 */           version.append("<br>");
/*     */         } 
/* 217 */         inSeeAlso = false;
/* 218 */         inReturns = false;
/* 219 */         inParams = false;
/* 220 */         inThrows = false;
/* 221 */         inSince = false;
/* 222 */         inAuthor = false;
/* 223 */         inVersion = true;
/* 224 */         inUnknowns = false; continue;
/*     */       } 
/* 226 */       if (token.startsWith("@") && token.length() > 1) {
/* 227 */         if (unknowns == null) {
/* 228 */           unknowns = new StringBuilder();
/*     */         } else {
/*     */           
/* 231 */           unknowns.append("</p>");
/*     */         } 
/* 233 */         unknowns.append("<b>").append(token).append("</b><p class='indented'>");
/*     */         
/* 235 */         inSeeAlso = false;
/* 236 */         inParams = false;
/* 237 */         inReturns = false;
/* 238 */         inThrows = false;
/* 239 */         inSince = false;
/* 240 */         inAuthor = false;
/* 241 */         inVersion = false;
/* 242 */         inUnknowns = true; continue;
/*     */       } 
/* 244 */       if (inParams) {
/* 245 */         params.append(token).append(' '); continue;
/*     */       } 
/* 247 */       if (inReturns) {
/* 248 */         returns.append(token).append(' '); continue;
/*     */       } 
/* 250 */       if (inSeeAlso) {
/*     */         
/* 252 */         seeTemp.append(token).append(' '); continue;
/*     */       } 
/* 254 */       if (inThrows) {
/* 255 */         throwsItems.append(token).append(' '); continue;
/*     */       } 
/* 257 */       if (inSince) {
/* 258 */         since.append(token).append(' '); continue;
/*     */       } 
/* 260 */       if (inAuthor) {
/* 261 */         author.append(token).append(' '); continue;
/*     */       } 
/* 263 */       if (inVersion) {
/* 264 */         version.append(token).append(' '); continue;
/*     */       } 
/* 266 */       if (inUnknowns) {
/* 267 */         unknowns.append(token).append(' ');
/*     */       }
/*     */     } 
/*     */     
/* 271 */     sb.append("<p>");
/*     */     
/* 273 */     if (params != null) {
/* 274 */       sb.append(params).append("</p>");
/*     */     }
/* 276 */     if (returns != null) {
/* 277 */       sb.append(returns).append("</p>");
/*     */     }
/* 279 */     if (throwsItems != null) {
/* 280 */       sb.append(throwsItems).append("</p>");
/*     */     }
/* 282 */     if (see != null) {
/* 283 */       if (seeTemp.length() > 0) {
/* 284 */         String temp = seeTemp.substring(0, seeTemp.length() - 1);
/*     */         
/* 286 */         appendLinkTagText(see, temp);
/*     */       } 
/* 288 */       see.append("<br>");
/* 289 */       sb.append(see).append("</p>");
/*     */     } 
/* 291 */     if (author != null) {
/* 292 */       sb.append(author).append("</p>");
/*     */     }
/* 294 */     if (version != null) {
/* 295 */       sb.append(version).append("</p>");
/*     */     }
/* 297 */     if (since != null) {
/* 298 */       sb.append(since).append("</p>");
/*     */     }
/* 300 */     if (unknowns != null) {
/* 301 */       sb.append(unknowns).append("</p>");
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
/*     */   private static void appendLinkTagText(StringBuilder appendTo, String linkContent) {
/* 319 */     linkContent = linkContent.trim();
/* 320 */     Matcher m = LINK_TAG_MEMBER_PATTERN.matcher(linkContent);
/*     */     
/* 322 */     if (m.find() && m.start() == 0) {
/*     */       
/* 324 */       appendTo.append("<a href='");
/*     */ 
/*     */       
/* 327 */       String match = m.group(0);
/* 328 */       String link = match;
/*     */ 
/*     */ 
/*     */       
/* 332 */       String text = null;
/*     */       
/* 334 */       if (match.length() == linkContent.length()) {
/* 335 */         int pound = match.indexOf('#');
/* 336 */         if (pound == 0) {
/* 337 */           text = match.substring(1);
/*     */         }
/* 339 */         else if (pound > 0) {
/* 340 */           String prefix = match.substring(0, pound);
/* 341 */           if ("java.lang.Object".equals(prefix)) {
/* 342 */             text = match.substring(pound + 1);
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 347 */           text = match;
/*     */         } 
/*     */       } else {
/*     */         
/* 351 */         int offs = match.length();
/*     */         
/* 353 */         while (offs < linkContent.length() && 
/* 354 */           Character.isWhitespace(linkContent.charAt(offs))) {
/* 355 */           offs++;
/*     */         }
/* 357 */         if (offs < linkContent.length()) {
/* 358 */           text = linkContent.substring(offs);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 363 */       if (text == null) {
/* 364 */         text = linkContent;
/*     */       }
/*     */ 
/*     */       
/* 368 */       text = fixLinkText(text);
/*     */       
/* 370 */       appendTo.append(link).append("'>").append(text);
/* 371 */       appendTo.append("</a>");
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 376 */     else if (linkContent.startsWith("<a")) {
/* 377 */       appendTo.append(linkContent);
/*     */     }
/*     */     else {
/*     */       
/* 381 */       System.out.println("Unmatched linkContent: " + linkContent);
/* 382 */       appendTo.append(linkContent);
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
/*     */   public static String docCommentToHtml(String dc) {
/* 398 */     if (dc == null) {
/* 399 */       return null;
/*     */     }
/* 401 */     if (dc.endsWith("*/")) {
/* 402 */       dc = dc.substring(0, dc.length() - 2);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 407 */     Matcher m = DOC_COMMENT_LINE_HEADER.matcher(dc);
/* 408 */     dc = m.replaceAll("\n");
/*     */     
/* 410 */     StringBuilder html = new StringBuilder("<html><style> .indented { margin-top: 0px; padding-left: 30pt; } </style><body>");
/*     */     
/* 412 */     StringBuilder tailBuf = null;
/*     */     
/* 414 */     BufferedReader r = new BufferedReader(new StringReader(dc));
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 419 */       String line = r.readLine().substring(3);
/* 420 */       line = possiblyStripDocCommentTail(line);
/* 421 */       int offs = 0;
/* 422 */       while (offs < line.length() && Character.isWhitespace(line.charAt(offs))) {
/* 423 */         offs++;
/*     */       }
/* 425 */       if (offs < line.length()) {
/* 426 */         html.append(line.substring(offs));
/*     */       }
/* 428 */       boolean inPreBlock = isInPreBlock(line, false);
/* 429 */       html.append(inPreBlock ? 10 : 32);
/*     */ 
/*     */       
/* 432 */       while ((line = r.readLine()) != null) {
/* 433 */         line = possiblyStripDocCommentTail(line);
/* 434 */         if (tailBuf != null) {
/* 435 */           tailBuf.append(line).append(' '); continue;
/*     */         } 
/* 437 */         if (line.trim().startsWith("@")) {
/* 438 */           tailBuf = new StringBuilder();
/* 439 */           tailBuf.append(line).append(' ');
/*     */           continue;
/*     */         } 
/* 442 */         html.append(line);
/* 443 */         inPreBlock = isInPreBlock(line, inPreBlock);
/* 444 */         html.append(inPreBlock ? 10 : 32);
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 449 */     catch (IOException ioe) {
/* 450 */       ioe.printStackTrace();
/*     */     } 
/*     */     
/* 453 */     html = fixDocComment(html);
/* 454 */     if (tailBuf != null) {
/* 455 */       appendDocCommentTail(html, fixDocComment(tailBuf));
/*     */     }
/*     */     
/* 458 */     return html.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String forXML(String aText) {
/* 463 */     StringBuilder result = new StringBuilder();
/* 464 */     StringCharacterIterator iterator = new StringCharacterIterator(aText);
/* 465 */     char character = iterator.current();
/* 466 */     while (character != Character.MAX_VALUE) {
/* 467 */       if (character == '<') {
/* 468 */         result.append("&lt;");
/*     */       }
/* 470 */       else if (character == '>') {
/* 471 */         result.append("&gt;");
/*     */       }
/* 473 */       else if (character == '"') {
/* 474 */         result.append("&quot;");
/*     */       }
/* 476 */       else if (character == '\'') {
/* 477 */         result.append("&#039;");
/*     */       }
/* 479 */       else if (character == '&') {
/* 480 */         result.append("&amp;");
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 485 */         result.append(character);
/*     */       } 
/* 487 */       character = iterator.next();
/*     */     } 
/* 489 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder fixDocComment(StringBuilder text) {
/* 496 */     int index = text.indexOf("{@");
/* 497 */     if (index == -1) {
/* 498 */       return text;
/*     */     }
/*     */     
/* 501 */     StringBuilder sb = new StringBuilder();
/* 502 */     int textOffs = 0;
/*     */ 
/*     */     
/*     */     while (true) {
/* 506 */       int closingBrace = indexOf('}', text, index + 2);
/* 507 */       if (closingBrace > -1)
/*     */       
/* 509 */       { sb.append(text, textOffs, index);
/* 510 */         String content = text.substring(index + 2, closingBrace);
/* 511 */         index = textOffs = closingBrace + 1;
/*     */         
/* 513 */         if (content.startsWith("code ")) {
/* 514 */           sb.append("<code>")
/* 515 */             .append(forXML(content.substring(5)))
/* 516 */             .append("</code>");
/*     */         
/*     */         }
/* 519 */         else if (content.startsWith("link ")) {
/* 520 */           sb.append("<code>");
/* 521 */           appendLinkTagText(sb, content.substring(5));
/* 522 */           sb.append("</code>");
/*     */         
/*     */         }
/* 525 */         else if (content.startsWith("linkplain ")) {
/* 526 */           appendLinkTagText(sb, content.substring(10));
/*     */         
/*     */         }
/* 529 */         else if (content.startsWith("literal ")) {
/*     */           
/* 531 */           sb.append(content.substring(8));
/*     */         }
/*     */         else {
/*     */           
/* 535 */           sb.append("<code>").append(content).append("</code>");
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 543 */         if ((index = text.indexOf("{@", index)) <= -1)
/*     */           break;  continue; }  break;
/* 545 */     }  if (textOffs < text.length()) {
/* 546 */       sb.append(text.substring(textOffs));
/*     */     }
/*     */     
/* 549 */     return sb;
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
/*     */   private static String fixLinkText(String text) {
/* 561 */     if (text.startsWith("#")) {
/* 562 */       return text.substring(1);
/*     */     }
/* 564 */     return text.replace('#', '.');
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
/*     */   
/*     */   public static CompilationUnit getCompilationUnitFromDisk(SourceLocation loc, ClassFile cf) {
/* 584 */     if (loc == lastCUFileParam && cf == lastCUClassFileParam)
/*     */     {
/* 586 */       return lastCUFromDisk;
/*     */     }
/*     */     
/* 589 */     lastCUFileParam = loc;
/* 590 */     lastCUClassFileParam = cf;
/* 591 */     CompilationUnit cu = null;
/*     */     
/* 593 */     if (loc != null) {
/*     */       try {
/* 595 */         cu = loc.getCompilationUnit(cf);
/* 596 */       } catch (IOException ioe) {
/* 597 */         ioe.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 601 */     lastCUFromDisk = cu;
/* 602 */     return cu;
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
/*     */   public static final String getUnqualified(String clazz) {
/* 615 */     int dot = clazz.lastIndexOf('.');
/* 616 */     if (dot > -1) {
/* 617 */       clazz = clazz.substring(dot + 1);
/*     */     }
/* 619 */     return clazz;
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
/*     */   private static int indexOf(char ch, CharSequence sb, int offs) {
/* 635 */     while (offs < sb.length()) {
/* 636 */       if (ch == sb.charAt(offs)) {
/* 637 */         return offs;
/*     */       }
/* 639 */       offs++;
/*     */     } 
/* 641 */     return -1;
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
/*     */   public static final boolean isFullyQualified(String str) {
/* 654 */     return (str.indexOf('.') > -1);
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
/*     */   private static boolean isInPreBlock(String line, boolean prevValue) {
/* 666 */     int lastPre = line.lastIndexOf("pre>");
/* 667 */     if (lastPre <= 0) {
/* 668 */       return prevValue;
/*     */     }
/* 670 */     char prevChar = line.charAt(lastPre - 1);
/* 671 */     if (prevChar == '<') {
/* 672 */       return true;
/*     */     }
/* 674 */     if (prevChar == '/' && lastPre >= 2 && 
/* 675 */       line.charAt(lastPre - 2) == '<') {
/* 676 */       return false;
/*     */     }
/*     */     
/* 679 */     return prevValue;
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
/*     */   private static String possiblyStripDocCommentTail(String str) {
/* 692 */     if (str.endsWith("*/")) {
/* 693 */       str = str.substring(0, str.length() - 2);
/*     */     }
/* 695 */     return str;
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
/*     */   public static final String[] splitOnChar(String str, int ch) {
/* 713 */     List<String> list = new ArrayList<>(3);
/*     */     
/* 715 */     int old = 0; int pos;
/* 716 */     while ((pos = str.indexOf(ch, old)) > -1) {
/* 717 */       list.add(str.substring(old, pos));
/* 718 */       old = pos + 1;
/*     */     } 
/*     */ 
/*     */     
/* 722 */     list.add(str.substring(old));
/* 723 */     String[] array = new String[list.size()];
/* 724 */     return list.<String>toArray(array);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */