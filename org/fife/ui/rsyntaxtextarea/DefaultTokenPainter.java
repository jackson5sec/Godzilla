/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import javax.swing.text.TabExpander;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DefaultTokenPainter
/*     */   implements TokenPainter
/*     */ {
/*  41 */   private Rectangle2D.Float bgRect = new Rectangle2D.Float();
/*     */ 
/*     */   
/*     */   private static char[] tabBuf;
/*     */ 
/*     */   
/*     */   public final float paint(Token token, Graphics2D g, float x, float y, RSyntaxTextArea host, TabExpander e) {
/*  48 */     return paint(token, g, x, y, host, e, 0.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float paint(Token token, Graphics2D g, float x, float y, RSyntaxTextArea host, TabExpander e, float clipStart) {
/*  55 */     return paintImpl(token, g, x, y, host, e, clipStart, false, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float paint(Token token, Graphics2D g, float x, float y, RSyntaxTextArea host, TabExpander e, float clipStart, boolean paintBG) {
/*  63 */     return paintImpl(token, g, x, y, host, e, clipStart, !paintBG, false);
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
/*     */   protected void paintBackground(float x, float y, float width, float height, Graphics2D g, int fontAscent, RSyntaxTextArea host, Color color) {
/*  83 */     g.setColor(color);
/*  84 */     this.bgRect.setRect(x, y - fontAscent, width, height);
/*     */     
/*  86 */     g.fillRect((int)x, (int)(y - fontAscent), (int)width, (int)height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected float paintImpl(Token token, Graphics2D g, float x, float y, RSyntaxTextArea host, TabExpander e, float clipStart, boolean selected, boolean useSTC) {
/*  97 */     int origX = (int)x;
/*  98 */     int textOffs = token.getTextOffset();
/*  99 */     char[] text = token.getTextArray();
/* 100 */     int end = textOffs + token.length();
/* 101 */     float nextX = x;
/* 102 */     int flushLen = 0;
/* 103 */     int flushIndex = textOffs;
/*     */     
/* 105 */     Color fg = useSTC ? host.getSelectedTextColor() : host.getForegroundForToken(token);
/* 106 */     Color bg = selected ? null : host.getBackgroundForToken(token);
/* 107 */     g.setFont(host.getFontForTokenType(token.getType()));
/* 108 */     FontMetrics fm = host.getFontMetricsForTokenType(token.getType());
/*     */     
/* 110 */     for (int i = textOffs; i < end; i++) {
/* 111 */       switch (text[i]) {
/*     */         case '\t':
/* 113 */           nextX = e.nextTabStop(x + fm
/* 114 */               .charsWidth(text, flushIndex, flushLen), 0);
/* 115 */           if (bg != null) {
/* 116 */             paintBackground(x, y, nextX - x, fm.getHeight(), g, fm
/* 117 */                 .getAscent(), host, bg);
/*     */           }
/* 119 */           if (flushLen > 0) {
/* 120 */             g.setColor(fg);
/* 121 */             g.drawChars(text, flushIndex, flushLen, (int)x, (int)y);
/* 122 */             flushLen = 0;
/*     */           } 
/* 124 */           flushIndex = i + 1;
/* 125 */           x = nextX;
/*     */           break;
/*     */         default:
/* 128 */           flushLen++;
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 133 */     nextX = x + fm.charsWidth(text, flushIndex, flushLen);
/* 134 */     Rectangle r = host.getMatchRectangle();
/*     */     
/* 136 */     if (flushLen > 0 && nextX >= clipStart) {
/* 137 */       if (bg != null) {
/* 138 */         paintBackground(x, y, nextX - x, fm.getHeight(), g, fm
/* 139 */             .getAscent(), host, bg);
/* 140 */         if (token.length() == 1 && r != null && r.x == x) {
/* 141 */           ((RSyntaxTextAreaUI)host.getUI()).paintMatchedBracketImpl(g, host, r);
/*     */         }
/*     */       } 
/*     */       
/* 145 */       g.setColor(fg);
/* 146 */       g.drawChars(text, flushIndex, flushLen, (int)x, (int)y);
/*     */     } 
/*     */     
/* 149 */     if (host.getUnderlineForToken(token)) {
/* 150 */       g.setColor(fg);
/* 151 */       int y2 = (int)(y + 1.0F);
/* 152 */       g.drawLine(origX, y2, (int)nextX, y2);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     if (host.getPaintTabLines() && origX == (host.getMargin()).left) {
/* 159 */       paintTabLines(token, origX, (int)y, (int)nextX, g, e, host);
/*     */     }
/*     */     
/* 162 */     return nextX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float paintSelected(Token token, Graphics2D g, float x, float y, RSyntaxTextArea host, TabExpander e, boolean useSTC) {
/* 170 */     return paintSelected(token, g, x, y, host, e, 0.0F, useSTC);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float paintSelected(Token token, Graphics2D g, float x, float y, RSyntaxTextArea host, TabExpander e, float clipStart, boolean useSTC) {
/* 178 */     return paintImpl(token, g, x, y, host, e, clipStart, true, useSTC);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintTabLines(Token token, int x, int y, int endX, Graphics2D g, TabExpander e, RSyntaxTextArea host) {
/* 205 */     if (token.getType() != 21) {
/* 206 */       int offs = 0;
/* 207 */       for (; offs < token.length() && 
/* 208 */         RSyntaxUtilities.isWhitespace(token.charAt(offs)); offs++);
/*     */ 
/*     */ 
/*     */       
/* 212 */       if (offs < 2) {
/*     */         return;
/*     */       }
/*     */       
/* 216 */       endX = (int)token.getWidthUpTo(offs, host, e, 0.0F);
/*     */     } 
/*     */ 
/*     */     
/* 220 */     FontMetrics fm = host.getFontMetricsForTokenType(token.getType());
/* 221 */     int tabSize = host.getTabSize();
/* 222 */     if (tabBuf == null || tabBuf.length < tabSize) {
/* 223 */       tabBuf = new char[tabSize];
/* 224 */       for (int i = 0; i < tabSize; i++) {
/* 225 */         tabBuf[i] = ' ';
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     int tabW = fm.charsWidth(tabBuf, 0, tabSize);
/*     */ 
/*     */ 
/*     */     
/* 237 */     g.setColor(host.getTabLineColor());
/* 238 */     int x0 = x + tabW;
/* 239 */     int y0 = y - fm.getAscent();
/* 240 */     if ((y0 & 0x1) > 0)
/*     */     {
/* 242 */       y0++;
/*     */     }
/*     */ 
/*     */     
/* 246 */     Token next = token.getNextToken();
/* 247 */     if (next == null || !next.isPaintable()) {
/* 248 */       endX++;
/*     */     }
/* 250 */     while (x0 < endX) {
/* 251 */       int y1 = y0;
/* 252 */       int y2 = y0 + host.getLineHeight();
/* 253 */       while (y1 < y2) {
/* 254 */         g.drawLine(x0, y1, x0, y1);
/* 255 */         y1 += 2;
/*     */       } 
/*     */       
/* 258 */       x0 += tabW;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\DefaultTokenPainter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */