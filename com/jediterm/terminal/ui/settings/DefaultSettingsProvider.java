/*     */ package com.jediterm.terminal.ui.settings;
/*     */ 
/*     */ import com.jediterm.terminal.HyperlinkStyle;
/*     */ import com.jediterm.terminal.TerminalColor;
/*     */ import com.jediterm.terminal.TextStyle;
/*     */ import com.jediterm.terminal.emulator.ColorPalette;
/*     */ import com.jediterm.terminal.emulator.ColorPaletteImpl;
/*     */ import com.jediterm.terminal.ui.TerminalActionPresentation;
/*     */ import com.jediterm.terminal.ui.UIUtil;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.util.Collections;
/*     */ import javax.swing.KeyStroke;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ public class DefaultSettingsProvider
/*     */   implements SettingsProvider
/*     */ {
/*     */   @NotNull
/*     */   public TerminalActionPresentation getNewSessionActionPresentation() {
/*  22 */     return new TerminalActionPresentation("New Session", UIUtil.isMac ? 
/*  23 */         KeyStroke.getKeyStroke(84, 256) : 
/*  24 */         KeyStroke.getKeyStroke(84, 192));
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TerminalActionPresentation getOpenUrlActionPresentation() {
/*  29 */     return new TerminalActionPresentation("Open as URL", Collections.emptyList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public TerminalActionPresentation getCopyActionPresentation() {
/*  37 */     KeyStroke keyStroke = UIUtil.isMac ? KeyStroke.getKeyStroke(67, 256) : KeyStroke.getKeyStroke(67, 192);
/*  38 */     return new TerminalActionPresentation("Copy", keyStroke);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public TerminalActionPresentation getPasteActionPresentation() {
/*  46 */     KeyStroke keyStroke = UIUtil.isMac ? KeyStroke.getKeyStroke(86, 256) : KeyStroke.getKeyStroke(86, 192);
/*  47 */     return new TerminalActionPresentation("Paste", keyStroke);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TerminalActionPresentation getClearBufferActionPresentation() {
/*  52 */     return new TerminalActionPresentation("Clear Buffer", UIUtil.isMac ? 
/*  53 */         KeyStroke.getKeyStroke(75, 256) : 
/*  54 */         KeyStroke.getKeyStroke(76, 128));
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TerminalActionPresentation getPageUpActionPresentation() {
/*  59 */     return new TerminalActionPresentation("Page Up", 
/*  60 */         KeyStroke.getKeyStroke(33, 64));
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TerminalActionPresentation getPageDownActionPresentation() {
/*  65 */     return new TerminalActionPresentation("Page Down", 
/*  66 */         KeyStroke.getKeyStroke(34, 64));
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TerminalActionPresentation getLineUpActionPresentation() {
/*  71 */     return new TerminalActionPresentation("Line Up", UIUtil.isMac ? 
/*  72 */         KeyStroke.getKeyStroke(38, 256) : 
/*  73 */         KeyStroke.getKeyStroke(38, 128));
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TerminalActionPresentation getLineDownActionPresentation() {
/*  78 */     return new TerminalActionPresentation("Line Down", UIUtil.isMac ? 
/*  79 */         KeyStroke.getKeyStroke(40, 256) : 
/*  80 */         KeyStroke.getKeyStroke(40, 128));
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TerminalActionPresentation getCloseSessionActionPresentation() {
/*  85 */     return new TerminalActionPresentation("Close Session", UIUtil.isMac ? 
/*  86 */         KeyStroke.getKeyStroke(87, 256) : 
/*  87 */         KeyStroke.getKeyStroke(87, 192));
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TerminalActionPresentation getFindActionPresentation() {
/*  92 */     return new TerminalActionPresentation("Find", UIUtil.isMac ? 
/*  93 */         KeyStroke.getKeyStroke(70, 256) : 
/*  94 */         KeyStroke.getKeyStroke(70, 128));
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TerminalActionPresentation getSelectAllActionPresentation() {
/*  99 */     return new TerminalActionPresentation("Select All", Collections.emptyList());
/*     */   }
/*     */ 
/*     */   
/*     */   public ColorPalette getTerminalColorPalette() {
/* 104 */     return UIUtil.isWindows ? ColorPaletteImpl.WINDOWS_PALETTE : ColorPaletteImpl.XTERM_PALETTE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Font getTerminalFont() {
/*     */     String fontName;
/* 110 */     if (UIUtil.isWindows) {
/* 111 */       fontName = "Consolas";
/* 112 */     } else if (UIUtil.isMac) {
/* 113 */       fontName = "Menlo";
/*     */     } else {
/* 115 */       fontName = "Monospaced";
/*     */     } 
/* 117 */     return new Font(fontName, 0, (int)getTerminalFontSize());
/*     */   }
/*     */ 
/*     */   
/*     */   public float getTerminalFontSize() {
/* 122 */     return 14.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextStyle getDefaultStyle() {
/* 127 */     return new TextStyle(TerminalColor.BLACK, TerminalColor.WHITE);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TextStyle getSelectionColor() {
/* 133 */     return new TextStyle(TerminalColor.WHITE, TerminalColor.rgb(82, 109, 165));
/*     */   }
/*     */ 
/*     */   
/*     */   public TextStyle getFoundPatternColor() {
/* 138 */     return new TextStyle(TerminalColor.BLACK, TerminalColor.rgb(255, 255, 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public TextStyle getHyperlinkColor() {
/* 143 */     return new TextStyle(TerminalColor.awt(Color.BLUE), TerminalColor.WHITE);
/*     */   }
/*     */ 
/*     */   
/*     */   public HyperlinkStyle.HighlightMode getHyperlinkHighlightingMode() {
/* 148 */     return HyperlinkStyle.HighlightMode.HOVER;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useInverseSelectionColor() {
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean copyOnSelect() {
/* 158 */     return emulateX11CopyPaste();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean pasteOnMiddleMouseClick() {
/* 163 */     return emulateX11CopyPaste();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean emulateX11CopyPaste() {
/* 168 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useAntialiasing() {
/* 173 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int maxRefreshRate() {
/* 178 */     return 50;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean audibleBell() {
/* 183 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean enableMouseReporting() {
/* 188 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int caretBlinkingMs() {
/* 193 */     return 505;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean scrollToBottomOnTyping() {
/* 198 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean DECCompatibilityMode() {
/* 203 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean forceActionOnMouseReporting() {
/* 208 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBufferMaxLinesCount() {
/* 213 */     return 5000;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean altSendsEscape() {
/* 218 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ambiguousCharsAreDoubleWidth() {
/* 223 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\settings\DefaultSettingsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */