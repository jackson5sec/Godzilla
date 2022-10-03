/*      */ package com.jediterm.terminal.model;
/*      */ import com.jediterm.terminal.CursorShape;
/*      */ import com.jediterm.terminal.HyperlinkStyle;
/*      */ import com.jediterm.terminal.RequestOrigin;
/*      */ import com.jediterm.terminal.StyledTextConsumer;
/*      */ import com.jediterm.terminal.SubstringFinder;
/*      */ import com.jediterm.terminal.TerminalDisplay;
/*      */ import com.jediterm.terminal.TerminalKeyEncoder;
/*      */ import com.jediterm.terminal.TerminalMode;
/*      */ import com.jediterm.terminal.TerminalOutputStream;
/*      */ import com.jediterm.terminal.TextStyle;
/*      */ import com.jediterm.terminal.emulator.charset.CharacterSet;
/*      */ import com.jediterm.terminal.emulator.charset.GraphicSet;
/*      */ import com.jediterm.terminal.emulator.charset.GraphicSetState;
/*      */ import com.jediterm.terminal.emulator.mouse.MouseFormat;
/*      */ import com.jediterm.terminal.emulator.mouse.MouseMode;
/*      */ import com.jediterm.terminal.util.CharUtils;
/*      */ import java.awt.Desktop;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Point;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseWheelEvent;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.Arrays;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.SortedSet;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import javax.swing.SwingUtilities;
/*      */ import org.jetbrains.annotations.NotNull;
/*      */ 
/*      */ public class JediTerminal implements Terminal, TerminalMouseListener, TerminalCoordinates {
/*   33 */   private static final Logger LOG = Logger.getLogger(JediTerminal.class.getName());
/*      */   
/*      */   private static final int MIN_WIDTH = 5;
/*      */   
/*      */   private static final int MIN_HEIGHT = 2;
/*      */   private int myScrollRegionTop;
/*      */   private int myScrollRegionBottom;
/*   40 */   private volatile int myCursorX = 0;
/*   41 */   private volatile int myCursorY = 1;
/*      */   
/*      */   private int myTerminalWidth;
/*      */   
/*      */   private int myTerminalHeight;
/*      */   
/*      */   private final TerminalDisplay myDisplay;
/*      */   
/*      */   private final TerminalTextBuffer myTerminalTextBuffer;
/*      */   private final StyleState myStyleState;
/*   51 */   private StoredCursor myStoredCursor = null;
/*      */   
/*   53 */   private final EnumSet<TerminalMode> myModes = EnumSet.noneOf(TerminalMode.class);
/*      */   
/*   55 */   private final TerminalKeyEncoder myTerminalKeyEncoder = new TerminalKeyEncoder();
/*      */   
/*      */   private final Tabulator myTabulator;
/*      */   
/*      */   private final GraphicSetState myGraphicSetState;
/*      */   
/*   61 */   private MouseFormat myMouseFormat = MouseFormat.MOUSE_FORMAT_XTERM;
/*      */   @Nullable
/*   63 */   private TerminalOutputStream myTerminalOutput = null;
/*      */ 
/*      */   
/*   66 */   private MouseMode myMouseMode = MouseMode.MOUSE_REPORTING_NONE;
/*   67 */   private Point myLastMotionReport = null;
/*      */   private boolean myCursorYChanged;
/*      */   
/*      */   public JediTerminal(TerminalDisplay display, TerminalTextBuffer buf, StyleState initialStyleState) {
/*   71 */     this.myDisplay = display;
/*   72 */     this.myTerminalTextBuffer = buf;
/*   73 */     this.myStyleState = initialStyleState;
/*      */     
/*   75 */     this.myTerminalWidth = display.getColumnCount();
/*   76 */     this.myTerminalHeight = display.getRowCount();
/*      */     
/*   78 */     this.myScrollRegionTop = 1;
/*   79 */     this.myScrollRegionBottom = this.myTerminalHeight;
/*      */     
/*   81 */     this.myTabulator = new DefaultTabulator(this.myTerminalWidth);
/*      */     
/*   83 */     this.myGraphicSetState = new GraphicSetState();
/*      */     
/*   85 */     reset();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModeEnabled(TerminalMode mode, boolean enabled) {
/*   91 */     if (enabled) {
/*   92 */       this.myModes.add(mode);
/*      */     } else {
/*   94 */       this.myModes.remove(mode);
/*      */     } 
/*      */     
/*   97 */     mode.setEnabled(this, enabled);
/*      */   }
/*      */ 
/*      */   
/*      */   public void disconnected() {
/*  102 */     this.myDisplay.setCursorVisible(false);
/*      */   }
/*      */   
/*      */   private void wrapLines() {
/*  106 */     if (this.myCursorX >= this.myTerminalWidth) {
/*  107 */       this.myCursorX = 0;
/*      */       
/*  109 */       this.myTerminalTextBuffer.getLine(this.myCursorY - 1).deleteCharacters(this.myTerminalWidth);
/*  110 */       if (isAutoWrap()) {
/*  111 */         this.myTerminalTextBuffer.getLine(this.myCursorY - 1).setWrapped(true);
/*  112 */         this.myCursorY++;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void finishText() {
/*  118 */     this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*  119 */     scrollY();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeCharacters(String string) {
/*  124 */     writeDecodedCharacters(decodeUsingGraphicalState(string));
/*      */   }
/*      */   
/*      */   private void writeDecodedCharacters(char[] string) {
/*  128 */     this.myTerminalTextBuffer.lock();
/*      */     try {
/*  130 */       if (this.myCursorYChanged && string.length > 0) {
/*  131 */         this.myCursorYChanged = false;
/*  132 */         if (this.myCursorY > 1) {
/*  133 */           this.myTerminalTextBuffer.getLine(this.myCursorY - 2).setWrapped(false);
/*      */         }
/*      */       } 
/*  136 */       wrapLines();
/*  137 */       scrollY();
/*      */       
/*  139 */       if (string.length != 0) {
/*  140 */         CharBuffer characters = new CharBuffer(string, 0, string.length);
/*      */         
/*  142 */         this.myTerminalTextBuffer.writeString(this.myCursorX, this.myCursorY, characters);
/*  143 */         this.myCursorX += characters.length();
/*      */       } 
/*      */       
/*  146 */       finishText();
/*      */     } finally {
/*  148 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeDoubleByte(char[] bytesOfChar) throws UnsupportedEncodingException {
/*  154 */     writeCharacters(new String(bytesOfChar, 0, 2));
/*      */   }
/*      */ 
/*      */   
/*      */   private char[] decodeUsingGraphicalState(String string) {
/*  159 */     StringBuilder result = new StringBuilder();
/*  160 */     for (char c : string.toCharArray()) {
/*  161 */       result.append(this.myGraphicSetState.map(c));
/*      */     }
/*      */     
/*  164 */     return result.toString().toCharArray();
/*      */   }
/*      */   
/*      */   public void writeUnwrappedString(String string) {
/*  168 */     int length = string.length();
/*  169 */     int off = 0;
/*  170 */     while (off < length) {
/*  171 */       int amountInLine = Math.min(distanceToLineEnd(), length - off);
/*  172 */       writeCharacters(string.substring(off, off + amountInLine));
/*  173 */       wrapLines();
/*  174 */       scrollY();
/*  175 */       off += amountInLine;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void scrollY() {
/*  181 */     this.myTerminalTextBuffer.lock();
/*      */     try {
/*  183 */       if (this.myCursorY > this.myScrollRegionBottom) {
/*  184 */         int dy = this.myScrollRegionBottom - this.myCursorY;
/*  185 */         this.myCursorY = this.myScrollRegionBottom;
/*  186 */         scrollArea(this.myScrollRegionTop, scrollingRegionSize(), dy);
/*  187 */         this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */       } 
/*  189 */       if (this.myCursorY < this.myScrollRegionTop) {
/*  190 */         this.myCursorY = this.myScrollRegionTop;
/*      */       }
/*      */     } finally {
/*  193 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void crnl() {
/*  198 */     carriageReturn();
/*  199 */     newLine();
/*      */   }
/*      */ 
/*      */   
/*      */   public void newLine() {
/*  204 */     this.myCursorYChanged = true;
/*  205 */     this.myCursorY++;
/*      */     
/*  207 */     scrollY();
/*      */     
/*  209 */     if (isAutoNewLine()) {
/*  210 */       carriageReturn();
/*      */     }
/*      */     
/*  213 */     this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */   }
/*      */ 
/*      */   
/*      */   public void mapCharsetToGL(int num) {
/*  218 */     this.myGraphicSetState.setGL(num);
/*      */   }
/*      */ 
/*      */   
/*      */   public void mapCharsetToGR(int num) {
/*  223 */     this.myGraphicSetState.setGR(num);
/*      */   }
/*      */ 
/*      */   
/*      */   public void designateCharacterSet(int tableNumber, char charset) {
/*  228 */     GraphicSet gs = this.myGraphicSetState.getGraphicSet(tableNumber);
/*  229 */     this.myGraphicSetState.designateGraphicSet(gs, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public void singleShiftSelect(int num) {
/*  234 */     this.myGraphicSetState.overrideGL(num);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAnsiConformanceLevel(int level) {
/*  239 */     if (level == 1 || level == 2) {
/*  240 */       this.myGraphicSetState.designateGraphicSet(0, CharacterSet.ASCII);
/*  241 */       this.myGraphicSetState
/*  242 */         .designateGraphicSet(1, CharacterSet.DEC_SUPPLEMENTAL);
/*  243 */       mapCharsetToGL(0);
/*  244 */       mapCharsetToGR(1);
/*  245 */     } else if (level == 3) {
/*  246 */       designateCharacterSet(0, 'B');
/*  247 */       mapCharsetToGL(0);
/*      */     } else {
/*  249 */       throw new IllegalArgumentException();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWindowTitle(String name) {
/*  255 */     this.myDisplay.setWindowTitle(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCurrentPath(String path) {
/*  260 */     this.myDisplay.setCurrentPath(path);
/*      */   }
/*      */ 
/*      */   
/*      */   public void backspace() {
/*  265 */     this.myCursorX--;
/*  266 */     if (this.myCursorX < 0) {
/*  267 */       this.myCursorY--;
/*  268 */       this.myCursorX = this.myTerminalWidth - 1;
/*      */     } 
/*  270 */     adjustXY(-1);
/*  271 */     this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */   }
/*      */ 
/*      */   
/*      */   public void carriageReturn() {
/*  276 */     this.myCursorX = 0;
/*  277 */     this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */   }
/*      */ 
/*      */   
/*      */   public void horizontalTab() {
/*  282 */     if (this.myCursorX >= this.myTerminalWidth) {
/*      */       return;
/*      */     }
/*  285 */     int length = this.myTerminalTextBuffer.getLine(this.myCursorY - 1).getText().length();
/*  286 */     int stop = this.myTabulator.nextTab(this.myCursorX);
/*  287 */     this.myCursorX = Math.max(this.myCursorX, length);
/*  288 */     if (this.myCursorX < stop) {
/*  289 */       char[] chars = new char[stop - this.myCursorX];
/*  290 */       Arrays.fill(chars, ' ');
/*  291 */       writeDecodedCharacters(chars);
/*      */     } else {
/*  293 */       this.myCursorX = stop;
/*      */     } 
/*  295 */     adjustXY(1);
/*  296 */     this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */   }
/*      */ 
/*      */   
/*      */   public void eraseInDisplay(int arg) {
/*  301 */     this.myTerminalTextBuffer.lock();
/*      */     
/*      */     try {
/*      */       int beginY;
/*      */       int endY;
/*  306 */       switch (arg) {
/*      */         
/*      */         case 0:
/*  309 */           if (this.myCursorX < this.myTerminalWidth) {
/*  310 */             this.myTerminalTextBuffer.eraseCharacters(this.myCursorX, -1, this.myCursorY - 1);
/*      */           }
/*      */           
/*  313 */           beginY = this.myCursorY;
/*  314 */           endY = this.myTerminalHeight;
/*      */           break;
/*      */ 
/*      */         
/*      */         case 1:
/*  319 */           this.myTerminalTextBuffer.eraseCharacters(0, this.myCursorX + 1, this.myCursorY - 1);
/*      */           
/*  321 */           beginY = 0;
/*  322 */           endY = this.myCursorY - 1;
/*      */           break;
/*      */         case 2:
/*  325 */           beginY = 0;
/*  326 */           endY = this.myTerminalHeight - 1;
/*  327 */           this.myTerminalTextBuffer.moveScreenLinesToHistory();
/*      */           break;
/*      */         default:
/*  330 */           LOG.error("Unsupported erase in display mode:" + arg);
/*  331 */           beginY = 1;
/*  332 */           endY = 1;
/*      */           break;
/*      */       } 
/*      */       
/*  336 */       if (beginY != endY) {
/*  337 */         clearLines(beginY, endY);
/*      */       }
/*      */     } finally {
/*  340 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void clearLines(int beginY, int endY) {
/*  345 */     this.myTerminalTextBuffer.lock();
/*      */     try {
/*  347 */       this.myTerminalTextBuffer.clearLines(beginY, endY);
/*      */     } finally {
/*  349 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearScreen() {
/*  355 */     clearLines(0, this.myTerminalHeight - 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCursorVisible(boolean visible) {
/*  360 */     this.myDisplay.setCursorVisible(visible);
/*      */   }
/*      */ 
/*      */   
/*      */   public void useAlternateBuffer(boolean enabled) {
/*  365 */     this.myTerminalTextBuffer.useAlternateBuffer(enabled);
/*  366 */     this.myDisplay.setScrollingEnabled(!enabled);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] getCodeForKey(int key, int modifiers) {
/*  371 */     return this.myTerminalKeyEncoder.getCode(key, modifiers);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setApplicationArrowKeys(boolean enabled) {
/*  376 */     if (enabled) {
/*  377 */       this.myTerminalKeyEncoder.arrowKeysApplicationSequences();
/*      */     } else {
/*  379 */       this.myTerminalKeyEncoder.arrowKeysAnsiCursorSequences();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setApplicationKeypad(boolean enabled) {
/*  385 */     if (enabled) {
/*  386 */       this.myTerminalKeyEncoder.keypadApplicationSequences();
/*      */     } else {
/*  388 */       this.myTerminalKeyEncoder.keypadAnsiSequences();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAutoNewLine(boolean enabled) {
/*  394 */     this.myTerminalKeyEncoder.setAutoNewLine(enabled);
/*      */   }
/*      */   
/*      */   public void eraseInLine(int arg) {
/*  398 */     this.myTerminalTextBuffer.lock(); try {
/*      */       int extent;
/*  400 */       switch (arg) {
/*      */         case 0:
/*  402 */           if (this.myCursorX < this.myTerminalWidth) {
/*  403 */             this.myTerminalTextBuffer.eraseCharacters(this.myCursorX, -1, this.myCursorY - 1);
/*      */           }
/*      */           
/*  406 */           this.myTerminalTextBuffer.getLine(this.myCursorY - 1).setWrapped(false);
/*      */           break;
/*      */         case 1:
/*  409 */           extent = Math.min(this.myCursorX + 1, this.myTerminalWidth);
/*  410 */           this.myTerminalTextBuffer.eraseCharacters(0, extent, this.myCursorY - 1);
/*      */           break;
/*      */         case 2:
/*  413 */           this.myTerminalTextBuffer.eraseCharacters(0, -1, this.myCursorY - 1);
/*      */           break;
/*      */         default:
/*  416 */           LOG.error("Unsupported erase in line mode:" + arg);
/*      */           break;
/*      */       } 
/*      */     } finally {
/*  420 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void deleteCharacters(int count) {
/*  426 */     this.myTerminalTextBuffer.lock();
/*      */     try {
/*  428 */       this.myTerminalTextBuffer.deleteCharacters(this.myCursorX, this.myCursorY - 1, count);
/*      */     } finally {
/*  430 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void insertBlankCharacters(int count) {
/*  436 */     this.myTerminalTextBuffer.lock();
/*      */     try {
/*  438 */       int extent = Math.min(count, this.myTerminalWidth - this.myCursorX);
/*  439 */       this.myTerminalTextBuffer.insertBlankCharacters(this.myCursorX, this.myCursorY - 1, extent);
/*      */     } finally {
/*  441 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void eraseCharacters(int count) {
/*  449 */     this.myTerminalTextBuffer.lock();
/*      */     try {
/*  451 */       this.myTerminalTextBuffer.eraseCharacters(this.myCursorX, this.myCursorX + count, this.myCursorY - 1);
/*      */     } finally {
/*  453 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearTabStopAtCursor() {
/*  459 */     this.myTabulator.clearTabStop(this.myCursorX);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearAllTabStops() {
/*  464 */     this.myTabulator.clearAllTabStops();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTabStopAtCursor() {
/*  469 */     this.myTabulator.setTabStop(this.myCursorX);
/*      */   }
/*      */ 
/*      */   
/*      */   public void insertLines(int count) {
/*  474 */     this.myTerminalTextBuffer.lock();
/*      */     try {
/*  476 */       this.myTerminalTextBuffer.insertLines(this.myCursorY - 1, count, this.myScrollRegionBottom);
/*      */     } finally {
/*  478 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void deleteLines(int count) {
/*  484 */     this.myTerminalTextBuffer.lock();
/*      */     try {
/*  486 */       this.myTerminalTextBuffer.deleteLines(this.myCursorY - 1, count, this.myScrollRegionBottom);
/*      */     } finally {
/*  488 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlinkingCursor(boolean enabled) {
/*  494 */     this.myDisplay.setBlinkingCursor(enabled);
/*      */   }
/*      */ 
/*      */   
/*      */   public void cursorUp(int countY) {
/*  499 */     this.myTerminalTextBuffer.lock();
/*      */     try {
/*  501 */       this.myCursorYChanged = true;
/*  502 */       this.myCursorY -= countY;
/*  503 */       this.myCursorY = Math.max(this.myCursorY, scrollingRegionTop());
/*  504 */       adjustXY(-1);
/*  505 */       this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */     } finally {
/*  507 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void cursorDown(int dY) {
/*  513 */     this.myTerminalTextBuffer.lock();
/*      */     try {
/*  515 */       this.myCursorYChanged = true;
/*  516 */       this.myCursorY += dY;
/*  517 */       this.myCursorY = Math.min(this.myCursorY, scrollingRegionBottom());
/*  518 */       adjustXY(-1);
/*  519 */       this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */     } finally {
/*  521 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void index() {
/*  530 */     this.myTerminalTextBuffer.lock();
/*      */     try {
/*  532 */       if (this.myCursorY == this.myScrollRegionBottom) {
/*  533 */         scrollArea(this.myScrollRegionTop, scrollingRegionSize(), -1);
/*      */       } else {
/*  535 */         this.myCursorY++;
/*  536 */         adjustXY(-1);
/*  537 */         this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */       } 
/*      */     } finally {
/*  540 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void scrollArea(int scrollRegionTop, int scrollRegionSize, int dy) {
/*  545 */     this.myDisplay.scrollArea(scrollRegionTop, scrollRegionSize, dy);
/*  546 */     this.myTerminalTextBuffer.scrollArea(scrollRegionTop, dy, scrollRegionTop + scrollRegionSize - 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void nextLine() {
/*  551 */     this.myTerminalTextBuffer.lock();
/*      */     try {
/*  553 */       this.myCursorX = 0;
/*  554 */       if (this.myCursorY == this.myScrollRegionBottom) {
/*  555 */         scrollArea(this.myScrollRegionTop, scrollingRegionSize(), -1);
/*      */       } else {
/*  557 */         this.myCursorY++;
/*      */       } 
/*  559 */       this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */     } finally {
/*  561 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */   
/*      */   private int scrollingRegionSize() {
/*  566 */     return this.myScrollRegionBottom - this.myScrollRegionTop + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void reverseIndex() {
/*  574 */     this.myTerminalTextBuffer.lock();
/*      */     try {
/*  576 */       if (this.myCursorY == this.myScrollRegionTop) {
/*  577 */         scrollArea(this.myScrollRegionTop, scrollingRegionSize(), 1);
/*      */       } else {
/*  579 */         this.myCursorY--;
/*  580 */         this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */       } 
/*      */     } finally {
/*  583 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */   
/*      */   private int scrollingRegionTop() {
/*  588 */     return isOriginMode() ? this.myScrollRegionTop : 1;
/*      */   }
/*      */   
/*      */   private int scrollingRegionBottom() {
/*  592 */     return isOriginMode() ? this.myScrollRegionBottom : this.myTerminalHeight;
/*      */   }
/*      */ 
/*      */   
/*      */   public void cursorForward(int dX) {
/*  597 */     this.myCursorX += dX;
/*  598 */     this.myCursorX = Math.min(this.myCursorX, this.myTerminalWidth - 1);
/*  599 */     adjustXY(1);
/*  600 */     this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */   }
/*      */ 
/*      */   
/*      */   public void cursorBackward(int dX) {
/*  605 */     this.myCursorX -= dX;
/*  606 */     this.myCursorX = Math.max(this.myCursorX, 0);
/*  607 */     adjustXY(-1);
/*  608 */     this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */   }
/*      */ 
/*      */   
/*      */   public void cursorShape(CursorShape shape) {
/*  613 */     this.myDisplay.setCursorShape(shape);
/*      */   }
/*      */ 
/*      */   
/*      */   public void cursorHorizontalAbsolute(int x) {
/*  618 */     cursorPosition(x, this.myCursorY);
/*      */   }
/*      */ 
/*      */   
/*      */   public void linePositionAbsolute(int y) {
/*  623 */     this.myCursorY = y;
/*  624 */     adjustXY(-1);
/*  625 */     this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */   }
/*      */ 
/*      */   
/*      */   public void cursorPosition(int x, int y) {
/*  630 */     if (isOriginMode()) {
/*  631 */       this.myCursorY = y + scrollingRegionTop() - 1;
/*      */     } else {
/*  633 */       this.myCursorY = y;
/*      */     } 
/*      */     
/*  636 */     if (this.myCursorY > scrollingRegionBottom()) {
/*  637 */       this.myCursorY = scrollingRegionBottom();
/*      */     }
/*      */ 
/*      */     
/*  641 */     this.myCursorX = Math.max(0, x - 1);
/*  642 */     this.myCursorX = Math.min(this.myCursorX, this.myTerminalWidth - 1);
/*      */     
/*  644 */     this.myCursorY = Math.max(0, this.myCursorY);
/*      */     
/*  646 */     adjustXY(-1);
/*      */     
/*  648 */     this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setScrollingRegion(int top, int bottom) {
/*  653 */     if (top > bottom) {
/*  654 */       LOG.error("Top margin of scroll region can't be greater then bottom: " + top + ">" + bottom);
/*      */     }
/*  656 */     this.myScrollRegionTop = Math.max(1, top);
/*  657 */     this.myScrollRegionBottom = Math.min(this.myTerminalHeight, bottom);
/*      */ 
/*      */     
/*  660 */     cursorPosition(1, 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void scrollUp(int count) {
/*  665 */     scrollDown(-count);
/*      */   }
/*      */ 
/*      */   
/*      */   public void scrollDown(int count) {
/*  670 */     this.myTerminalTextBuffer.lock();
/*      */     try {
/*  672 */       scrollArea(this.myScrollRegionTop, scrollingRegionSize(), count);
/*      */     } finally {
/*  674 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void resetScrollRegions() {
/*  680 */     setScrollingRegion(1, this.myTerminalHeight);
/*      */   }
/*      */ 
/*      */   
/*      */   public void characterAttributes(TextStyle textStyle) {
/*  685 */     this.myStyleState.setCurrent(textStyle);
/*      */   }
/*      */ 
/*      */   
/*      */   public void beep() {
/*  690 */     this.myDisplay.beep();
/*      */   }
/*      */ 
/*      */   
/*      */   public int distanceToLineEnd() {
/*  695 */     return this.myTerminalWidth - this.myCursorX;
/*      */   }
/*      */ 
/*      */   
/*      */   public void saveCursor() {
/*  700 */     this.myStoredCursor = createCursorState();
/*      */   }
/*      */   
/*      */   private StoredCursor createCursorState() {
/*  704 */     return new StoredCursor(this.myCursorX, this.myCursorY, this.myStyleState.getCurrent(), 
/*  705 */         isAutoWrap(), isOriginMode(), this.myGraphicSetState);
/*      */   }
/*      */ 
/*      */   
/*      */   public void restoreCursor() {
/*  710 */     if (this.myStoredCursor != null) {
/*  711 */       restoreCursor(this.myStoredCursor);
/*      */     } else {
/*  713 */       setModeEnabled(TerminalMode.OriginMode, false);
/*  714 */       cursorPosition(1, 1);
/*  715 */       this.myStyleState.reset();
/*      */       
/*  717 */       this.myGraphicSetState.resetState();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  723 */     this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */   }
/*      */   
/*      */   public void restoreCursor(@NotNull StoredCursor storedCursor) {
/*  727 */     if (storedCursor == null) $$$reportNull$$$0(0);  this.myCursorX = storedCursor.getCursorX();
/*  728 */     this.myCursorY = storedCursor.getCursorY();
/*      */     
/*  730 */     adjustXY(-1);
/*      */     
/*  732 */     this.myStyleState.setCurrent(storedCursor.getTextStyle());
/*      */     
/*  734 */     setModeEnabled(TerminalMode.AutoWrap, storedCursor.isAutoWrap());
/*  735 */     setModeEnabled(TerminalMode.OriginMode, storedCursor.isOriginMode());
/*      */     
/*  737 */     CharacterSet[] designations = storedCursor.getDesignations();
/*  738 */     for (int i = 0; i < designations.length; i++) {
/*  739 */       this.myGraphicSetState.designateGraphicSet(i, designations[i]);
/*      */     }
/*  741 */     this.myGraphicSetState.setGL(storedCursor.getGLMapping());
/*  742 */     this.myGraphicSetState.setGR(storedCursor.getGRMapping());
/*      */     
/*  744 */     if (storedCursor.getGLOverride() >= 0) {
/*  745 */       this.myGraphicSetState.overrideGL(storedCursor.getGLOverride());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void reset() {
/*  751 */     this.myGraphicSetState.resetState();
/*      */     
/*  753 */     this.myStyleState.reset();
/*      */     
/*  755 */     this.myTerminalTextBuffer.clearAll();
/*      */     
/*  757 */     this.myDisplay.setScrollingEnabled(true);
/*      */     
/*  759 */     initModes();
/*      */     
/*  761 */     initMouseModes();
/*      */     
/*  763 */     cursorPosition(1, 1);
/*      */   }
/*      */   
/*      */   private void initMouseModes() {
/*  767 */     setMouseMode(MouseMode.MOUSE_REPORTING_NONE);
/*  768 */     setMouseFormat(MouseFormat.MOUSE_FORMAT_XTERM);
/*      */   }
/*      */   
/*      */   private void initModes() {
/*  772 */     this.myModes.clear();
/*  773 */     setModeEnabled(TerminalMode.AutoWrap, true);
/*  774 */     setModeEnabled(TerminalMode.AutoNewLine, false);
/*  775 */     setModeEnabled(TerminalMode.CursorVisible, true);
/*  776 */     setModeEnabled(TerminalMode.CursorBlinking, true);
/*      */   }
/*      */   
/*      */   public boolean isAutoNewLine() {
/*  780 */     return this.myModes.contains(TerminalMode.AutoNewLine);
/*      */   }
/*      */   
/*      */   public boolean isOriginMode() {
/*  784 */     return this.myModes.contains(TerminalMode.OriginMode);
/*      */   }
/*      */   
/*      */   public boolean isAutoWrap() {
/*  788 */     return this.myModes.contains(TerminalMode.AutoWrap);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int createButtonCode(MouseEvent event) {
/*  793 */     if (SwingUtilities.isLeftMouseButton(event))
/*  794 */       return 0; 
/*  795 */     if (SwingUtilities.isMiddleMouseButton(event))
/*  796 */       return 1; 
/*  797 */     if (SwingUtilities.isRightMouseButton(event))
/*  798 */       return -1; 
/*  799 */     if (event instanceof MouseWheelEvent) {
/*  800 */       if (((MouseWheelEvent)event).getWheelRotation() > 0) {
/*  801 */         return 5;
/*      */       }
/*  803 */       return 4;
/*      */     } 
/*      */     
/*  806 */     return -1;
/*      */   }
/*      */   
/*      */   private byte[] mouseReport(int button, int x, int y) {
/*  810 */     StringBuilder sb = new StringBuilder();
/*  811 */     String charset = "UTF-8";
/*  812 */     switch (this.myMouseFormat)
/*      */     { case MOUSE_FORMAT_XTERM_EXT:
/*  814 */         sb.append(String.format("\033[M%c%c%c", new Object[] {
/*  815 */                 Character.valueOf((char)(32 + button)), 
/*  816 */                 Character.valueOf((char)(32 + x)), 
/*  817 */                 Character.valueOf((char)(32 + y))
/*      */               }));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  843 */         LOG.debug(this.myMouseFormat + " (" + charset + ") report : " + button + ", " + x + "x" + y + " = " + sb);
/*  844 */         return sb.toString().getBytes(Charset.forName(charset));case MOUSE_FORMAT_URXVT: sb.append(String.format("\033[%d;%d;%dM", new Object[] { Integer.valueOf(32 + button), Integer.valueOf(x), Integer.valueOf(y) })); LOG.debug(this.myMouseFormat + " (" + charset + ") report : " + button + ", " + x + "x" + y + " = " + sb); return sb.toString().getBytes(Charset.forName(charset));case MOUSE_FORMAT_SGR: if ((button & 0x80) != 0) { sb.append(String.format("\033[<%d;%d;%dm", new Object[] { Integer.valueOf(button ^ 0x80), Integer.valueOf(x), Integer.valueOf(y) })); } else { sb.append(String.format("\033[<%d;%d;%dM", new Object[] { Integer.valueOf(button), Integer.valueOf(x), Integer.valueOf(y) })); }  LOG.debug(this.myMouseFormat + " (" + charset + ") report : " + button + ", " + x + "x" + y + " = " + sb); return sb.toString().getBytes(Charset.forName(charset)); }  charset = "ISO-8859-1"; sb.append(String.format("\033[M%c%c%c", new Object[] { Character.valueOf((char)(32 + button)), Character.valueOf((char)(32 + x)), Character.valueOf((char)(32 + y)) })); LOG.debug(this.myMouseFormat + " (" + charset + ") report : " + button + ", " + x + "x" + y + " = " + sb); return sb.toString().getBytes(Charset.forName(charset));
/*      */   }
/*      */   
/*      */   private boolean shouldSendMouseData(MouseMode... eligibleModes) {
/*  848 */     if (this.myMouseMode == MouseMode.MOUSE_REPORTING_NONE || this.myTerminalOutput == null) {
/*  849 */       return false;
/*      */     }
/*  851 */     if (this.myMouseMode == MouseMode.MOUSE_REPORTING_ALL_MOTION) {
/*  852 */       return true;
/*      */     }
/*  854 */     for (MouseMode m : eligibleModes) {
/*  855 */       if (this.myMouseMode == m) {
/*  856 */         return true;
/*      */       }
/*      */     } 
/*  859 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void mousePressed(int x, int y, MouseEvent event) {
/*  864 */     if (shouldSendMouseData(new MouseMode[] { MouseMode.MOUSE_REPORTING_NORMAL, MouseMode.MOUSE_REPORTING_BUTTON_MOTION })) {
/*  865 */       int cb = createButtonCode(event);
/*      */       
/*  867 */       if (cb != -1) {
/*  868 */         if (cb == 4 || cb == 5) {
/*      */           
/*  870 */           int offset = 4;
/*  871 */           cb -= offset;
/*  872 */           cb |= 0x40;
/*      */         } 
/*      */         
/*  875 */         cb = applyModifierKeys(event, cb);
/*      */         
/*  877 */         if (this.myTerminalOutput != null) {
/*  878 */           this.myTerminalOutput.sendBytes(mouseReport(cb, x + 1, y + 1));
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void mouseReleased(int x, int y, MouseEvent event) {
/*  886 */     if (shouldSendMouseData(new MouseMode[] { MouseMode.MOUSE_REPORTING_NORMAL, MouseMode.MOUSE_REPORTING_BUTTON_MOTION })) {
/*  887 */       int cb = createButtonCode(event);
/*      */       
/*  889 */       if (cb != -1) {
/*      */         
/*  891 */         if (this.myMouseFormat == MouseFormat.MOUSE_FORMAT_SGR) {
/*      */           
/*  893 */           cb |= 0x80;
/*      */         } else {
/*      */           
/*  896 */           cb = 3;
/*      */         } 
/*      */         
/*  899 */         cb = applyModifierKeys(event, cb);
/*      */         
/*  901 */         if (this.myTerminalOutput != null) {
/*  902 */           this.myTerminalOutput.sendBytes(mouseReport(cb, x + 1, y + 1));
/*      */         }
/*      */       } 
/*      */     } 
/*  906 */     this.myLastMotionReport = null;
/*      */   }
/*      */   
/*      */   public void mouseMoved(int x, int y, MouseEvent event) {
/*  910 */     if (this.myLastMotionReport != null && this.myLastMotionReport.equals(new Point(x, y))) {
/*      */       return;
/*      */     }
/*  913 */     if (shouldSendMouseData(new MouseMode[] { MouseMode.MOUSE_REPORTING_ALL_MOTION
/*  914 */         }) && this.myTerminalOutput != null) {
/*  915 */       this.myTerminalOutput.sendBytes(mouseReport(3, x + 1, y + 1));
/*      */     }
/*      */     
/*  918 */     this.myLastMotionReport = new Point(x, y);
/*      */   }
/*      */ 
/*      */   
/*      */   public void mouseDragged(int x, int y, MouseEvent event) {
/*  923 */     if (this.myLastMotionReport != null && this.myLastMotionReport.equals(new Point(x, y))) {
/*      */       return;
/*      */     }
/*  926 */     if (shouldSendMouseData(new MouseMode[] { MouseMode.MOUSE_REPORTING_BUTTON_MOTION })) {
/*      */       
/*  928 */       int cb = createButtonCode(event);
/*      */       
/*  930 */       if (cb != -1) {
/*  931 */         cb |= 0x20;
/*  932 */         cb = applyModifierKeys(event, cb);
/*  933 */         if (this.myTerminalOutput != null) {
/*  934 */           this.myTerminalOutput.sendBytes(mouseReport(cb, x + 1, y + 1));
/*      */         }
/*      */       } 
/*      */     } 
/*  938 */     this.myLastMotionReport = new Point(x, y);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void mouseWheelMoved(int x, int y, MouseWheelEvent event) {
/*  944 */     mousePressed(x, y, event);
/*      */   }
/*      */   
/*      */   private static int applyModifierKeys(MouseEvent event, int cb) {
/*  948 */     if (event.isControlDown()) {
/*  949 */       cb |= 0x10;
/*      */     }
/*  951 */     if (event.isShiftDown()) {
/*  952 */       cb |= 0x4;
/*      */     }
/*  954 */     if ((event.getModifiersEx() & 0x4) != 0) {
/*  955 */       cb |= 0x8;
/*      */     }
/*  957 */     return cb;
/*      */   }
/*      */   
/*      */   public void setTerminalOutput(TerminalOutputStream terminalOutput) {
/*  961 */     this.myTerminalOutput = terminalOutput;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMouseMode(@NotNull MouseMode mode) {
/*  966 */     if (mode == null) $$$reportNull$$$0(1);  this.myMouseMode = mode;
/*  967 */     this.myDisplay.terminalMouseModeSet(mode);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAltSendsEscape(boolean enabled) {
/*  972 */     this.myTerminalKeyEncoder.setAltSendsEscape(enabled);
/*      */   }
/*      */ 
/*      */   
/*      */   public void deviceStatusReport(String str) {
/*  977 */     if (this.myTerminalOutput != null) {
/*  978 */       this.myTerminalOutput.sendString(str);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void deviceAttributes(byte[] response) {
/*  984 */     if (this.myTerminalOutput != null) {
/*  985 */       this.myTerminalOutput.sendBytes(response);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLinkUriStarted(@NotNull String uri) {
/*  991 */     if (uri == null) $$$reportNull$$$0(2);  TextStyle style = this.myStyleState.getCurrent();
/*  992 */     this.myStyleState.setCurrent((TextStyle)new HyperlinkStyle(style, new LinkInfo(() -> {
/*      */               try {
/*      */                 Desktop.getDesktop().browse(new URI(uri));
/*  995 */               } catch (Exception exception) {}
/*      */             })));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLinkUriFinished() {
/* 1002 */     TextStyle current = this.myStyleState.getCurrent();
/* 1003 */     if (current instanceof HyperlinkStyle) {
/* 1004 */       TextStyle prevTextStyle = ((HyperlinkStyle)current).getPrevTextStyle();
/* 1005 */       if (prevTextStyle != null) {
/* 1006 */         this.myStyleState.setCurrent(prevTextStyle);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMouseFormat(MouseFormat mouseFormat) {
/* 1013 */     this.myMouseFormat = mouseFormat;
/*      */   }
/*      */   
/*      */   private void adjustXY(int dirX) {
/* 1017 */     if (this.myCursorY > -this.myTerminalTextBuffer.getHistoryLinesCount() && 
/* 1018 */       Character.isLowSurrogate(this.myTerminalTextBuffer.getCharAt(this.myCursorX, this.myCursorY - 1)))
/*      */     {
/* 1020 */       if (dirX > 0) {
/* 1021 */         if (this.myCursorX == this.myTerminalWidth) {
/* 1022 */           this.myCursorX--;
/*      */         } else {
/* 1024 */           this.myCursorX++;
/*      */         } 
/*      */       } else {
/* 1027 */         this.myCursorX--;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public int getX() {
/* 1034 */     return this.myCursorX;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setX(int x) {
/* 1039 */     this.myCursorX = x;
/* 1040 */     adjustXY(-1);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getY() {
/* 1045 */     return this.myCursorY;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setY(int y) {
/* 1050 */     this.myCursorY = y;
/* 1051 */     adjustXY(-1);
/*      */   }
/*      */   
/*      */   public void writeString(String s) {
/* 1055 */     writeCharacters(s);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resize(@NotNull Dimension newTermSize, @NotNull RequestOrigin origin) {
/* 1064 */     if (newTermSize == null) $$$reportNull$$$0(3);  if (origin == null) $$$reportNull$$$0(4);  resize(newTermSize, origin, CompletableFuture.completedFuture(null));
/*      */   }
/*      */ 
/*      */   
/*      */   public void resize(@NotNull Dimension newTermSize, @NotNull RequestOrigin origin, @NotNull CompletableFuture<?> promptUpdated) {
/* 1069 */     if (newTermSize == null) $$$reportNull$$$0(5);  if (origin == null) $$$reportNull$$$0(6);  if (promptUpdated == null) $$$reportNull$$$0(7);  int oldHeight = this.myTerminalHeight;
/* 1070 */     ensureTermMinimumSize(newTermSize);
/* 1071 */     if (newTermSize.width == this.myTerminalWidth && newTermSize.height == this.myTerminalHeight) {
/*      */       return;
/*      */     }
/* 1074 */     if (newTermSize.width == this.myTerminalWidth) {
/* 1075 */       doResize(newTermSize, origin, oldHeight);
/*      */     } else {
/*      */       
/* 1078 */       this.myTerminalWidth = newTermSize.width;
/* 1079 */       this.myTerminalHeight = newTermSize.height;
/* 1080 */       promptUpdated.thenRun(() -> doResize(newTermSize, origin, oldHeight));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void doResize(@NotNull Dimension newTermSize, @NotNull RequestOrigin origin, int oldHeight) {
/* 1087 */     if (newTermSize == null) $$$reportNull$$$0(8);  if (origin == null) $$$reportNull$$$0(9);  this.myDisplay.requestResize(newTermSize, origin, this.myCursorX, this.myCursorY, (termWidth, termHeight, cursorX, cursorY) -> {
/*      */           this.myTerminalWidth = termWidth;
/*      */           
/*      */           this.myTerminalHeight = termHeight;
/*      */           this.myCursorY = cursorY;
/*      */           this.myCursorX = Math.min(cursorX, this.myTerminalWidth - 1);
/*      */           this.myDisplay.setCursor(this.myCursorX, this.myCursorY);
/*      */           this.myTabulator.resize(this.myTerminalWidth);
/*      */         });
/* 1096 */     this.myScrollRegionBottom += this.myTerminalHeight - oldHeight;
/*      */   }
/*      */   
/*      */   public static void ensureTermMinimumSize(@NotNull Dimension termSize) {
/* 1100 */     if (termSize == null) $$$reportNull$$$0(10);  termSize.setSize(Math.max(5, termSize.width), Math.max(2, termSize.height));
/*      */   }
/*      */ 
/*      */   
/*      */   public void fillScreen(char c) {
/* 1105 */     this.myTerminalTextBuffer.lock();
/*      */     try {
/* 1107 */       char[] chars = new char[this.myTerminalWidth];
/* 1108 */       Arrays.fill(chars, c);
/*      */       
/* 1110 */       for (int row = 1; row <= this.myTerminalHeight; row++) {
/* 1111 */         this.myTerminalTextBuffer.writeString(0, row, newCharBuf(chars));
/*      */       }
/*      */     } finally {
/* 1114 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */   }
/*      */   @NotNull
/*      */   private CharBuffer newCharBuf(char[] str) {
/*      */     char[] buf;
/* 1120 */     int dwcCount = CharUtils.countDoubleWidthCharacters(str, 0, str.length, this.myDisplay.ambiguousCharsAreDoubleWidth());
/*      */ 
/*      */ 
/*      */     
/* 1124 */     if (dwcCount > 0) {
/*      */       
/* 1126 */       buf = new char[str.length + dwcCount];
/*      */       
/* 1128 */       int j = 0;
/* 1129 */       for (int i = 0; i < str.length; i++) {
/* 1130 */         buf[j] = str[i];
/* 1131 */         int codePoint = Character.codePointAt(str, i);
/* 1132 */         boolean doubleWidthCharacter = CharUtils.isDoubleWidthCharacter(codePoint, this.myDisplay.ambiguousCharsAreDoubleWidth());
/* 1133 */         if (doubleWidthCharacter) {
/* 1134 */           j++;
/* 1135 */           buf[j] = '';
/*      */         } 
/* 1137 */         j++;
/*      */       } 
/*      */     } else {
/* 1140 */       buf = str;
/*      */     } 
/* 1142 */     return new CharBuffer(buf, 0, buf.length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTerminalWidth() {
/* 1147 */     return this.myTerminalWidth;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTerminalHeight() {
/* 1152 */     return this.myTerminalHeight;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getCursorX() {
/* 1157 */     return this.myCursorX + 1;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getCursorY() {
/* 1162 */     return this.myCursorY;
/*      */   }
/*      */ 
/*      */   
/*      */   public StyleState getStyleState() {
/* 1167 */     return this.myStyleState;
/*      */   }
/*      */   
/*      */   public SubstringFinder.FindResult searchInTerminalTextBuffer(String pattern, boolean ignoreCase) {
/* 1171 */     if (pattern.length() == 0) {
/* 1172 */       return null;
/*      */     }
/*      */     
/* 1175 */     final SubstringFinder finder = new SubstringFinder(pattern, ignoreCase);
/*      */     
/* 1177 */     this.myTerminalTextBuffer.processHistoryAndScreenLines(-this.myTerminalTextBuffer.getHistoryLinesCount(), -1, new StyledTextConsumer()
/*      */         {
/*      */           public void consume(int x, int y, @NotNull TextStyle style, @NotNull CharBuffer characters, int startRow) {
/* 1180 */             if (style == null) $$$reportNull$$$0(0);  if (characters == null) $$$reportNull$$$0(1);  int offset = 0;
/* 1181 */             int length = characters.length();
/* 1182 */             if (characters instanceof SubCharBuffer) {
/* 1183 */               SubCharBuffer subCharBuffer = (SubCharBuffer)characters;
/* 1184 */               characters = subCharBuffer.getParent();
/* 1185 */               offset = subCharBuffer.getOffset();
/*      */             } 
/* 1187 */             for (int i = offset; i < offset + length; i++) {
/* 1188 */               finder.nextChar(x, y - startRow, characters, i);
/*      */             }
/*      */           }
/*      */ 
/*      */           
/*      */           public void consumeNul(int x, int y, int nulIndex, @NotNull TextStyle style, @NotNull CharBuffer characters, int startRow) {
/* 1194 */             if (style == null) $$$reportNull$$$0(2);  if (characters == null) $$$reportNull$$$0(3);
/*      */           
/*      */           }
/*      */ 
/*      */           
/*      */           public void consumeQueue(int x, int y, int nulIndex, int startRow) {}
/*      */         });
/* 1201 */     return finder.getResult();
/*      */   }
/*      */ 
/*      */   
/*      */   private static class DefaultTabulator
/*      */     implements Tabulator
/*      */   {
/*      */     private static final int TAB_LENGTH = 8;
/*      */     private final SortedSet<Integer> myTabStops;
/*      */     private int myWidth;
/*      */     private int myTabLength;
/*      */     
/*      */     public DefaultTabulator(int width) {
/* 1214 */       this(width, 8);
/*      */     }
/*      */     
/*      */     public DefaultTabulator(int width, int tabLength) {
/* 1218 */       this.myTabStops = new TreeSet<>();
/*      */       
/* 1220 */       this.myWidth = width;
/* 1221 */       this.myTabLength = tabLength;
/*      */       
/* 1223 */       initTabStops(width, tabLength);
/*      */     }
/*      */     
/*      */     private void initTabStops(int columns, int tabLength) {
/* 1227 */       for (int i = tabLength; i < columns; i += tabLength) {
/* 1228 */         this.myTabStops.add(Integer.valueOf(i));
/*      */       }
/*      */     }
/*      */     
/*      */     public void resize(int columns) {
/* 1233 */       if (columns > this.myWidth) {
/* 1234 */         for (int i = this.myTabLength * this.myWidth / this.myTabLength; i < columns; i += this.myTabLength) {
/* 1235 */           if (i >= this.myWidth) {
/* 1236 */             this.myTabStops.add(Integer.valueOf(i));
/*      */           }
/*      */         } 
/*      */       } else {
/* 1240 */         Iterator<Integer> it = this.myTabStops.iterator();
/* 1241 */         while (it.hasNext()) {
/* 1242 */           int i = ((Integer)it.next()).intValue();
/* 1243 */           if (i > columns) {
/* 1244 */             it.remove();
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/* 1249 */       this.myWidth = columns;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clearTabStop(int position) {
/* 1254 */       this.myTabStops.remove(Integer.valueOf(position));
/*      */     }
/*      */ 
/*      */     
/*      */     public void clearAllTabStops() {
/* 1259 */       this.myTabStops.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getNextTabWidth(int position) {
/* 1264 */       return nextTab(position) - position;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getPreviousTabWidth(int position) {
/* 1269 */       return position - previousTab(position);
/*      */     }
/*      */ 
/*      */     
/*      */     public int nextTab(int position) {
/* 1274 */       int tabStop = Integer.MAX_VALUE;
/*      */ 
/*      */       
/* 1277 */       SortedSet<Integer> tailSet = this.myTabStops.tailSet(Integer.valueOf(position + 1));
/* 1278 */       if (!tailSet.isEmpty()) {
/* 1279 */         tabStop = ((Integer)tailSet.first()).intValue();
/*      */       }
/*      */ 
/*      */       
/* 1283 */       return Math.min(tabStop, this.myWidth - 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public int previousTab(int position) {
/* 1288 */       int tabStop = 0;
/*      */ 
/*      */       
/* 1291 */       SortedSet<Integer> headSet = this.myTabStops.headSet(Integer.valueOf(position));
/* 1292 */       if (!headSet.isEmpty()) {
/* 1293 */         tabStop = ((Integer)headSet.last()).intValue();
/*      */       }
/*      */ 
/*      */       
/* 1297 */       return Math.max(0, tabStop);
/*      */     }
/*      */ 
/*      */     
/*      */     public void setTabStop(int position) {
/* 1302 */       this.myTabStops.add(Integer.valueOf(position));
/*      */     }
/*      */   }
/*      */   
/*      */   public static interface ResizeHandler {
/*      */     void sizeUpdated(int param1Int1, int param1Int2, int param1Int3, int param1Int4);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\JediTerminal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */