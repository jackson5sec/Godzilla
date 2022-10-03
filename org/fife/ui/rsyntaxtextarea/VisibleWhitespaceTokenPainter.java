/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics2D;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class VisibleWhitespaceTokenPainter
/*     */   extends DefaultTokenPainter
/*     */ {
/*     */   protected float paintImpl(Token token, Graphics2D g, float x, float y, RSyntaxTextArea host, TabExpander e, float clipStart, boolean selected, boolean useSTC) {
/*  56 */     int origX = (int)x;
/*  57 */     int textOffs = token.getTextOffset();
/*  58 */     char[] text = token.getTextArray();
/*  59 */     int end = textOffs + token.length();
/*  60 */     float nextX = x;
/*  61 */     int flushLen = 0;
/*  62 */     int flushIndex = textOffs;
/*     */     
/*  64 */     Color fg = useSTC ? host.getSelectedTextColor() : host.getForegroundForToken(token);
/*  65 */     Color bg = selected ? null : host.getBackgroundForToken(token);
/*  66 */     g.setFont(host.getFontForTokenType(token.getType()));
/*  67 */     FontMetrics fm = host.getFontMetricsForTokenType(token.getType());
/*     */     
/*  69 */     int ascent = fm.getAscent();
/*  70 */     int height = fm.getHeight();
/*     */     
/*  72 */     for (int i = textOffs; i < end; i++) {
/*     */       float nextNextX; int halfHeight; int quarterHeight; int ymid; int width; int dotX; int dotY;
/*  74 */       switch (text[i]) {
/*     */ 
/*     */ 
/*     */         
/*     */         case '\t':
/*  79 */           nextX = x + fm.charsWidth(text, flushIndex, flushLen);
/*  80 */           nextNextX = e.nextTabStop(nextX, 0);
/*  81 */           if (bg != null) {
/*  82 */             paintBackground(x, y, nextNextX - x, height, g, ascent, host, bg);
/*     */           }
/*     */           
/*  85 */           g.setColor(fg);
/*     */ 
/*     */           
/*  88 */           if (flushLen > 0) {
/*  89 */             g.drawChars(text, flushIndex, flushLen, (int)x, (int)y);
/*  90 */             flushLen = 0;
/*     */           } 
/*  92 */           flushIndex = i + 1;
/*     */ 
/*     */           
/*  95 */           halfHeight = height / 2;
/*  96 */           quarterHeight = halfHeight / 2;
/*  97 */           ymid = (int)y - ascent + halfHeight;
/*  98 */           g.drawLine((int)nextX, ymid, (int)nextNextX, ymid);
/*  99 */           g.drawLine((int)nextNextX, ymid, (int)nextNextX - 4, ymid - quarterHeight);
/* 100 */           g.drawLine((int)nextNextX, ymid, (int)nextNextX - 4, ymid + quarterHeight);
/*     */           
/* 102 */           x = nextNextX;
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case ' ':
/* 117 */           nextX = x + fm.charsWidth(text, flushIndex, flushLen + 1);
/* 118 */           width = fm.charWidth(' ');
/*     */ 
/*     */           
/* 121 */           if (bg != null) {
/* 122 */             paintBackground(x, y, nextX - x, height, g, ascent, host, bg);
/*     */           }
/*     */           
/* 125 */           g.setColor(fg);
/*     */ 
/*     */           
/* 128 */           if (flushLen > 0) {
/* 129 */             g.drawChars(text, flushIndex, flushLen, (int)x, (int)y);
/* 130 */             flushLen = 0;
/*     */           } 
/*     */ 
/*     */           
/* 134 */           dotX = (int)(nextX - width / 2.0F);
/* 135 */           dotY = (int)(y - ascent + height / 2.0F);
/* 136 */           g.drawLine(dotX, dotY, dotX, dotY);
/* 137 */           flushIndex = i + 1;
/* 138 */           x = nextX;
/*     */           break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         default:
/* 147 */           flushLen++;
/*     */           break;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 153 */     nextX = x + fm.charsWidth(text, flushIndex, flushLen);
/*     */     
/* 155 */     if (flushLen > 0 && nextX >= clipStart) {
/* 156 */       if (bg != null) {
/* 157 */         paintBackground(x, y, nextX - x, height, g, ascent, host, bg);
/*     */       }
/*     */       
/* 160 */       g.setColor(fg);
/* 161 */       g.drawChars(text, flushIndex, flushLen, (int)x, (int)y);
/*     */     } 
/*     */     
/* 164 */     if (host.getUnderlineForToken(token)) {
/* 165 */       g.setColor(fg);
/* 166 */       int y2 = (int)(y + 1.0F);
/* 167 */       g.drawLine(origX, y2, (int)nextX, y2);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     if (host.getPaintTabLines() && origX == (host.getMargin()).left) {
/* 174 */       paintTabLines(token, origX, (int)y, (int)nextX, g, e, host);
/*     */     }
/*     */     
/* 177 */     return nextX;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\VisibleWhitespaceTokenPainter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */