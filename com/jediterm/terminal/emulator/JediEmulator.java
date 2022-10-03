/*      */ package com.jediterm.terminal.emulator;
/*      */ 
/*      */ import com.jediterm.terminal.CursorShape;
/*      */ import com.jediterm.terminal.DataStreamIteratingEmulator;
/*      */ import com.jediterm.terminal.RequestOrigin;
/*      */ import com.jediterm.terminal.Terminal;
/*      */ import com.jediterm.terminal.TerminalColor;
/*      */ import com.jediterm.terminal.TerminalDataStream;
/*      */ import com.jediterm.terminal.TerminalMode;
/*      */ import com.jediterm.terminal.TextStyle;
/*      */ import com.jediterm.terminal.emulator.mouse.MouseFormat;
/*      */ import com.jediterm.terminal.emulator.mouse.MouseMode;
/*      */ import com.jediterm.terminal.util.CharUtils;
/*      */ import java.awt.Dimension;
/*      */ import java.io.IOException;
/*      */ import java.util.concurrent.BlockingQueue;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.LinkedBlockingQueue;
/*      */ import java.util.function.BiConsumer;
/*      */ import org.apache.log4j.Logger;
/*      */ import org.jetbrains.annotations.NotNull;
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JediEmulator
/*      */   extends DataStreamIteratingEmulator
/*      */ {
/*   28 */   private static final Logger LOG = Logger.getLogger(JediEmulator.class);
/*      */   
/*   30 */   private static int logThrottlerCounter = 0;
/*   31 */   private static int logThrottlerRatio = 100;
/*   32 */   private static int logThrottlerLimit = logThrottlerRatio;
/*   33 */   private final BlockingQueue<CompletableFuture<Void>> myResizeFutureQueue = new LinkedBlockingQueue<>();
/*      */   
/*      */   public JediEmulator(TerminalDataStream dataStream, Terminal terminal) {
/*   36 */     super(dataStream, terminal);
/*      */   }
/*      */   
/*      */   public void processChar(char ch, Terminal terminal) throws IOException {
/*      */     String nonControlCharacters;
/*   41 */     switch (ch) {
/*      */       case '\000':
/*      */         break;
/*      */       case '\007':
/*   45 */         terminal.beep();
/*      */         break;
/*      */       case '\b':
/*   48 */         terminal.backspace();
/*      */         break;
/*      */       case '\r':
/*   51 */         terminal.carriageReturn();
/*      */         break;
/*      */       case '\005':
/*   54 */         unsupported("Terminal status:" + escapeSequenceToString(new char[] { ch }));
/*      */         break;
/*      */       
/*      */       case '\n':
/*      */       case '\013':
/*      */       case '\f':
/*   60 */         terminal.newLine();
/*      */         break;
/*      */ 
/*      */       
/*      */       case '\017':
/*   65 */         terminal.mapCharsetToGL(0);
/*      */         break;
/*      */ 
/*      */       
/*      */       case '\016':
/*   70 */         if (Boolean.getBoolean("jediterm.enable.shift_out.character.support")) {
/*   71 */           terminal.mapCharsetToGL(1);
/*      */         }
/*      */         break;
/*      */       case '\t':
/*   75 */         terminal.horizontalTab();
/*      */         break;
/*      */       case '\033':
/*   78 */         processEscapeSequence(this.myDataStream.getChar(), this.myTerminal);
/*      */         break;
/*      */       default:
/*   81 */         if (ch <= '\037') {
/*   82 */           StringBuilder sb = new StringBuilder("Unhandled control character:");
/*   83 */           CharUtils.appendChar(sb, CharUtils.CharacterType.NONE, ch);
/*   84 */           unhandledLogThrottler(sb.toString()); break;
/*      */         } 
/*   86 */         this.myDataStream.pushChar(ch);
/*   87 */         nonControlCharacters = this.myDataStream.readNonControlCharacters(terminal.distanceToLineEnd());
/*      */         
/*   89 */         terminal.writeCharacters(nonControlCharacters);
/*      */         break;
/*      */     } 
/*      */     
/*   93 */     if (this.myDataStream.isEmpty())
/*   94 */       completeResize(); 
/*      */   }
/*      */   private void processEscapeSequence(char ch, Terminal terminal) throws IOException {
/*      */     ControlSequence args;
/*      */     SystemCommandSequence command;
/*   99 */     switch (ch) {
/*      */       case '[':
/*  101 */         args = new ControlSequence(this.myDataStream);
/*      */         
/*  103 */         if (LOG.isDebugEnabled()) {
/*  104 */           LOG.debug(args.appendTo("Control sequence\nparsed                        :"));
/*      */         }
/*  106 */         if (!args.pushBackReordered(this.myDataStream)) {
/*  107 */           boolean result = processControlSequence(args);
/*      */           
/*  109 */           if (!result) {
/*  110 */             StringBuilder sb = new StringBuilder();
/*  111 */             sb.append("Unhandled Control sequence\n");
/*  112 */             sb.append("parsed                        :");
/*  113 */             args.appendToBuffer(sb);
/*  114 */             sb.append('\n');
/*  115 */             sb.append("bytes read                    :ESC[");
/*  116 */             LOG.error(sb.toString());
/*      */           } 
/*      */         } 
/*      */         return;
/*      */       case 'D':
/*  121 */         terminal.index();
/*      */         return;
/*      */       case 'E':
/*  124 */         terminal.nextLine();
/*      */         return;
/*      */       case 'H':
/*  127 */         terminal.setTabStopAtCursor();
/*      */         return;
/*      */       case 'M':
/*  130 */         terminal.reverseIndex();
/*      */         return;
/*      */       case 'N':
/*  133 */         terminal.singleShiftSelect(2);
/*      */         return;
/*      */       case 'O':
/*  136 */         terminal.singleShiftSelect(3);
/*      */         return;
/*      */       
/*      */       case ']':
/*  140 */         command = new SystemCommandSequence(this.myDataStream);
/*      */         
/*  142 */         if (!operatingSystemCommand(command)) {
/*  143 */           LOG.error("Error processing OSC " + command.getSequenceString());
/*      */         }
/*      */         return;
/*      */       case '6':
/*  147 */         unsupported("Back Index (DECBI), VT420 and up");
/*      */         return;
/*      */       case '7':
/*  150 */         terminal.saveCursor();
/*      */         return;
/*      */       case '8':
/*  153 */         terminal.restoreCursor();
/*      */         return;
/*      */       case '9':
/*  156 */         unsupported("Forward Index (DECFI), VT420 and up");
/*      */         return;
/*      */       case '=':
/*  159 */         setModeEnabled(TerminalMode.Keypad, true);
/*      */         return;
/*      */       case '>':
/*  162 */         setModeEnabled(TerminalMode.Keypad, false);
/*      */         return;
/*      */       case 'F':
/*  165 */         terminal.cursorPosition(1, terminal.getTerminalHeight());
/*      */         return;
/*      */       case 'c':
/*  168 */         terminal.reset();
/*      */         return;
/*      */       case 'n':
/*  171 */         this.myTerminal.mapCharsetToGL(2);
/*      */         return;
/*      */       case 'o':
/*  174 */         this.myTerminal.mapCharsetToGL(3);
/*      */         return;
/*      */       case '|':
/*  177 */         this.myTerminal.mapCharsetToGR(3);
/*      */         return;
/*      */       case '}':
/*  180 */         this.myTerminal.mapCharsetToGR(2);
/*      */         return;
/*      */       case '~':
/*  183 */         this.myTerminal.mapCharsetToGR(1);
/*      */         return;
/*      */       case ' ':
/*      */       case '#':
/*      */       case '$':
/*      */       case '%':
/*      */       case '(':
/*      */       case ')':
/*      */       case '*':
/*      */       case '+':
/*      */       case '.':
/*      */       case '/':
/*      */       case '@':
/*  196 */         processTwoCharSequence(ch, terminal);
/*      */         return;
/*      */     } 
/*  199 */     unsupported(new char[] { ch });
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean operatingSystemCommand(SystemCommandSequence args) {
/*  204 */     Integer i = args.getIntAt(0);
/*      */     
/*  206 */     if (i != null) {
/*  207 */       String name; String path; String uri; switch (i.intValue()) {
/*      */         case 0:
/*      */         case 2:
/*  210 */           name = args.getStringAt(1);
/*  211 */           if (name != null) {
/*  212 */             this.myTerminal.setWindowTitle(name);
/*  213 */             return true;
/*      */           } 
/*      */           break;
/*      */         case 7:
/*  217 */           path = args.getStringAt(1);
/*  218 */           if (path != null) {
/*  219 */             this.myTerminal.setCurrentPath(path);
/*  220 */             return true;
/*      */           } 
/*      */           break;
/*      */         case 8:
/*  224 */           uri = args.getStringAt(2);
/*  225 */           if (uri != null) {
/*  226 */             if (!uri.isEmpty()) {
/*  227 */               this.myTerminal.setLinkUriStarted(uri);
/*      */             } else {
/*      */               
/*  230 */               this.myTerminal.setLinkUriFinished();
/*      */             } 
/*  232 */             return true;
/*      */           } 
/*      */           break;
/*      */       } 
/*      */     
/*      */     } 
/*  238 */     return false;
/*      */   }
/*      */   
/*      */   private void processTwoCharSequence(char ch, Terminal terminal) throws IOException {
/*  242 */     char secondCh = this.myDataStream.getChar();
/*  243 */     switch (ch) {
/*      */       case ' ':
/*  245 */         switch (secondCh) {
/*      */           
/*      */           case 'F':
/*  248 */             unsupported("Switching ot 7-bit");
/*      */             break;
/*      */           case 'G':
/*  251 */             unsupported("Switching ot 8-bit");
/*      */             break;
/*      */           
/*      */           case 'L':
/*  255 */             terminal.setAnsiConformanceLevel(1);
/*      */             break;
/*      */           case 'M':
/*  258 */             terminal.setAnsiConformanceLevel(2);
/*      */             break;
/*      */           case 'N':
/*  261 */             terminal.setAnsiConformanceLevel(3);
/*      */             break;
/*      */         } 
/*      */         
/*  265 */         unsupported(new char[] { ch, secondCh });
/*      */         break;
/*      */       
/*      */       case '#':
/*  269 */         switch (secondCh) {
/*      */           case '8':
/*  271 */             terminal.fillScreen('E');
/*      */             break;
/*      */         } 
/*  274 */         unsupported(new char[] { ch, secondCh });
/*      */         break;
/*      */       
/*      */       case '%':
/*  278 */         switch (secondCh) {
/*      */           case '@':
/*      */           case 'G':
/*  281 */             unsupported("Selecting charset is unsupported: " + escapeSequenceToString(new char[] { ch, secondCh }));
/*      */             break;
/*      */         } 
/*  284 */         unsupported(new char[] { ch, secondCh });
/*      */         break;
/*      */       
/*      */       case '(':
/*  288 */         terminal.designateCharacterSet(0, secondCh);
/*      */         break;
/*      */       case ')':
/*  291 */         terminal.designateCharacterSet(1, secondCh);
/*      */         break;
/*      */       case '*':
/*  294 */         terminal.designateCharacterSet(2, secondCh);
/*      */         break;
/*      */       case '+':
/*  297 */         terminal.designateCharacterSet(3, secondCh);
/*      */         break;
/*      */       case '-':
/*  300 */         terminal.designateCharacterSet(1, secondCh);
/*      */         break;
/*      */       case '.':
/*  303 */         terminal.designateCharacterSet(2, secondCh);
/*      */         break;
/*      */       case '/':
/*  306 */         terminal.designateCharacterSet(3, secondCh);
/*      */         break;
/*      */       case '$':
/*      */       case '@':
/*  310 */         unsupported(new char[] { ch, secondCh });
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void unsupported(char... sequenceChars) {
/*  321 */     unsupported(escapeSequenceToString(sequenceChars));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void unsupported(String msg) {
/*  330 */     unhandledLogThrottler("Unsupported control characters: " + msg);
/*      */   }
/*      */   
/*      */   private static void unhandledLogThrottler(String msg) {
/*  334 */     logThrottlerCounter++;
/*  335 */     if (logThrottlerCounter < logThrottlerLimit) {
/*  336 */       if (logThrottlerCounter % logThrottlerLimit / logThrottlerRatio == 0) {
/*  337 */         if (logThrottlerLimit / logThrottlerRatio > 1) {
/*  338 */           msg = msg + " and " + (logThrottlerLimit / logThrottlerRatio) + " more...";
/*      */         }
/*  340 */         LOG.error(msg);
/*      */       } 
/*      */     } else {
/*  343 */       logThrottlerLimit *= 10;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static String escapeSequenceToString(char... b) {
/*  348 */     StringBuilder sb = new StringBuilder("ESC ");
/*      */     
/*  350 */     for (char c : b) {
/*  351 */       sb.append(' ');
/*  352 */       sb.append(c);
/*      */     } 
/*  354 */     return sb.toString();
/*      */   }
/*      */   
/*      */   private boolean processControlSequence(ControlSequence args) {
/*  358 */     switch (args.getFinalChar()) {
/*      */       case '@':
/*  360 */         return insertBlankCharacters(args);
/*      */       case 'A':
/*  362 */         return cursorUp(args);
/*      */       case 'B':
/*  364 */         return cursorDown(args);
/*      */       case 'C':
/*  366 */         return cursorForward(args);
/*      */       case 'D':
/*  368 */         return cursorBackward(args);
/*      */       case 'E':
/*  370 */         return cursorNextLine(args);
/*      */       case 'F':
/*  372 */         return cursorPrecedingLine(args);
/*      */       case 'G':
/*      */       case '`':
/*  375 */         return cursorHorizontalAbsolute(args);
/*      */       case 'H':
/*      */       case 'f':
/*  378 */         return cursorPosition(args);
/*      */       case 'J':
/*  380 */         return eraseInDisplay(args);
/*      */       case 'K':
/*  382 */         return eraseInLine(args);
/*      */       case 'L':
/*  384 */         return insertLines(args);
/*      */       case 'M':
/*  386 */         return deleteLines(args);
/*      */       case 'X':
/*  388 */         return eraseCharacters(args);
/*      */       case 'P':
/*  390 */         return deleteCharacters(args);
/*      */       case 'S':
/*  392 */         return scrollUp(args);
/*      */       case 'T':
/*  394 */         return scrollDown(args);
/*      */       case 'c':
/*  396 */         if (args.startsWithMoreMark()) {
/*  397 */           if (args.getArg(0, 0) == 0) {
/*  398 */             sendDeviceAttributes();
/*  399 */             return true;
/*      */           } 
/*  401 */           return false;
/*      */         } 
/*  403 */         return sendDeviceAttributes();
/*      */       case 'd':
/*  405 */         return linePositionAbsolute(args);
/*      */       case 'g':
/*  407 */         return tabClear(args.getArg(0, 0));
/*      */       case 'h':
/*  409 */         return setModeOrPrivateMode(args, true);
/*      */       case 'l':
/*  411 */         return setModeOrPrivateMode(args, false);
/*      */       case 'm':
/*  413 */         if (args.startsWithMoreMark())
/*      */         {
/*      */           
/*  416 */           return false;
/*      */         }
/*  418 */         return characterAttributes(args);
/*      */       case 'n':
/*  420 */         return deviceStatusReport(args);
/*      */       case 'q':
/*  422 */         return cursorShape(args);
/*      */       case 'r':
/*  424 */         if (args.startsWithQuestionMark()) {
/*  425 */           return restoreDecPrivateModeValues(args);
/*      */         }
/*      */         
/*  428 */         return setScrollingRegion(args);
/*      */       
/*      */       case 't':
/*  431 */         return windowManipulation(args);
/*      */     } 
/*  433 */     return false;
/*      */   }
/*      */   
/*      */   private boolean windowManipulation(ControlSequence args) {
/*      */     int width;
/*      */     int height;
/*  439 */     switch (args.getArg(0, -1)) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 8:
/*  445 */         width = args.getArg(2, 0);
/*  446 */         height = args.getArg(1, 0);
/*  447 */         if (width == 0) {
/*  448 */           width = this.myTerminal.getTerminalWidth();
/*      */         }
/*  450 */         if (height == 0) {
/*  451 */           height = this.myTerminal.getTerminalHeight();
/*      */         }
/*  453 */         this.myTerminal.resize(new Dimension(width, height), RequestOrigin.Remote);
/*  454 */         return true;
/*      */     } 
/*  456 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean tabClear(int mode) {
/*  461 */     if (mode == 0) {
/*  462 */       this.myTerminal.clearTabStopAtCursor();
/*  463 */       return true;
/*  464 */     }  if (mode == 3) {
/*  465 */       this.myTerminal.clearAllTabStops();
/*  466 */       return true;
/*      */     } 
/*  468 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean eraseCharacters(ControlSequence args) {
/*  473 */     this.myTerminal.eraseCharacters(args.getArg(0, 1));
/*  474 */     return true;
/*      */   }
/*      */   
/*      */   private boolean setModeOrPrivateMode(ControlSequence args, boolean enabled) {
/*  478 */     if (args.startsWithQuestionMark()) {
/*  479 */       switch (args.getArg(0, -1)) {
/*      */         case 1:
/*  481 */           setModeEnabled(TerminalMode.CursorKey, enabled);
/*  482 */           return true;
/*      */         case 3:
/*  484 */           setModeEnabled(TerminalMode.WideColumn, enabled);
/*  485 */           return true;
/*      */         case 4:
/*  487 */           setModeEnabled(TerminalMode.SmoothScroll, enabled);
/*  488 */           return true;
/*      */         case 5:
/*  490 */           setModeEnabled(TerminalMode.ReverseVideo, enabled);
/*  491 */           return true;
/*      */         case 6:
/*  493 */           setModeEnabled(TerminalMode.OriginMode, enabled);
/*  494 */           return true;
/*      */         case 7:
/*  496 */           setModeEnabled(TerminalMode.AutoWrap, enabled);
/*  497 */           return true;
/*      */         case 8:
/*  499 */           setModeEnabled(TerminalMode.AutoRepeatKeys, enabled);
/*  500 */           return true;
/*      */ 
/*      */         
/*      */         case 12:
/*  504 */           return true;
/*      */         case 25:
/*  506 */           setModeEnabled(TerminalMode.CursorVisible, enabled);
/*  507 */           return true;
/*      */         case 40:
/*  509 */           setModeEnabled(TerminalMode.AllowWideColumn, enabled);
/*  510 */           return true;
/*      */         case 45:
/*  512 */           setModeEnabled(TerminalMode.ReverseWrapAround, enabled);
/*  513 */           return true;
/*      */         case 47:
/*      */         case 1047:
/*  516 */           setModeEnabled(TerminalMode.AlternateBuffer, enabled);
/*  517 */           return true;
/*      */         case 1048:
/*  519 */           setModeEnabled(TerminalMode.StoreCursor, enabled);
/*  520 */           return true;
/*      */         case 1049:
/*  522 */           setModeEnabled(TerminalMode.StoreCursor, enabled);
/*  523 */           setModeEnabled(TerminalMode.AlternateBuffer, enabled);
/*  524 */           return true;
/*      */         case 1000:
/*  526 */           if (enabled) {
/*  527 */             setMouseMode(MouseMode.MOUSE_REPORTING_NORMAL);
/*      */           } else {
/*  529 */             setMouseMode(MouseMode.MOUSE_REPORTING_NONE);
/*      */           } 
/*  531 */           return true;
/*      */         case 1001:
/*  533 */           if (enabled) {
/*  534 */             setMouseMode(MouseMode.MOUSE_REPORTING_HILITE);
/*      */           } else {
/*  536 */             setMouseMode(MouseMode.MOUSE_REPORTING_NONE);
/*      */           } 
/*  538 */           return true;
/*      */         case 1002:
/*  540 */           if (enabled) {
/*  541 */             setMouseMode(MouseMode.MOUSE_REPORTING_BUTTON_MOTION);
/*      */           } else {
/*  543 */             setMouseMode(MouseMode.MOUSE_REPORTING_NONE);
/*      */           } 
/*  545 */           return true;
/*      */         case 1003:
/*  547 */           if (enabled) {
/*  548 */             setMouseMode(MouseMode.MOUSE_REPORTING_ALL_MOTION);
/*      */           } else {
/*  550 */             setMouseMode(MouseMode.MOUSE_REPORTING_NONE);
/*      */           } 
/*  552 */           return true;
/*      */         case 1005:
/*  554 */           if (enabled) {
/*  555 */             this.myTerminal.setMouseFormat(MouseFormat.MOUSE_FORMAT_XTERM_EXT);
/*      */           } else {
/*  557 */             this.myTerminal.setMouseFormat(MouseFormat.MOUSE_FORMAT_XTERM);
/*      */           } 
/*  559 */           return true;
/*      */         case 1006:
/*  561 */           if (enabled) {
/*  562 */             this.myTerminal.setMouseFormat(MouseFormat.MOUSE_FORMAT_SGR);
/*      */           } else {
/*  564 */             this.myTerminal.setMouseFormat(MouseFormat.MOUSE_FORMAT_XTERM);
/*      */           } 
/*  566 */           return true;
/*      */         case 1015:
/*  568 */           if (enabled) {
/*  569 */             this.myTerminal.setMouseFormat(MouseFormat.MOUSE_FORMAT_URXVT);
/*      */           } else {
/*  571 */             this.myTerminal.setMouseFormat(MouseFormat.MOUSE_FORMAT_XTERM);
/*      */           } 
/*  573 */           return true;
/*      */         case 1034:
/*  575 */           setModeEnabled(TerminalMode.EightBitInput, enabled);
/*  576 */           return true;
/*      */         case 1039:
/*  578 */           setModeEnabled(TerminalMode.AltSendsEscape, enabled);
/*  579 */           return true;
/*      */       } 
/*  581 */       return false;
/*      */     } 
/*      */     
/*  584 */     switch (args.getArg(0, -1)) {
/*      */       case 2:
/*  586 */         setModeEnabled(TerminalMode.KeyboardAction, enabled);
/*  587 */         return true;
/*      */       case 4:
/*  589 */         setModeEnabled(TerminalMode.InsertMode, enabled);
/*  590 */         return true;
/*      */       case 12:
/*  592 */         setModeEnabled(TerminalMode.SendReceive, enabled);
/*  593 */         return true;
/*      */       case 20:
/*  595 */         setModeEnabled(TerminalMode.AutoNewLine, enabled);
/*  596 */         return true;
/*      */     } 
/*  598 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean linePositionAbsolute(ControlSequence args) {
/*  604 */     int y = args.getArg(0, 1);
/*  605 */     this.myTerminal.linePositionAbsolute(y);
/*      */     
/*  607 */     return true;
/*      */   }
/*      */   
/*      */   private boolean restoreDecPrivateModeValues(ControlSequence args) {
/*  611 */     LOG.error("Unsupported: " + args.toString());
/*      */     
/*  613 */     return false;
/*      */   }
/*      */   
/*      */   private boolean deviceStatusReport(ControlSequence args) {
/*  617 */     if (args.startsWithQuestionMark()) {
/*  618 */       LOG.error("Don't support DEC-specific Device Report Status");
/*  619 */       return false;
/*      */     } 
/*  621 */     int c = args.getArg(0, 0);
/*  622 */     if (c == 5) {
/*  623 */       String str = "\033[0n";
/*  624 */       LOG.debug("Sending Device Report Status : " + str);
/*  625 */       this.myTerminal.deviceStatusReport(str);
/*  626 */       return true;
/*  627 */     }  if (c == 6) {
/*  628 */       int row = this.myTerminal.getCursorY();
/*  629 */       int column = this.myTerminal.getCursorX();
/*  630 */       String str = "\033[" + row + ";" + column + "R";
/*      */       
/*  632 */       LOG.debug("Sending Device Report Status : " + str);
/*  633 */       this.myTerminal.deviceStatusReport(str);
/*  634 */       return true;
/*      */     } 
/*  636 */     LOG.error("Sending Device Report Status : unsupported parameter: " + args.toString());
/*  637 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean cursorShape(ControlSequence args) {
/*  642 */     this.myTerminal.cursorBackward(1);
/*  643 */     switch (args.getArg(0, 0)) {
/*      */       case 0:
/*      */       case 1:
/*  646 */         this.myTerminal.cursorShape(CursorShape.BLINK_BLOCK);
/*  647 */         return true;
/*      */       case 2:
/*  649 */         this.myTerminal.cursorShape(CursorShape.STEADY_BLOCK);
/*  650 */         return true;
/*      */       case 3:
/*  652 */         this.myTerminal.cursorShape(CursorShape.BLINK_UNDERLINE);
/*  653 */         return true;
/*      */       case 4:
/*  655 */         this.myTerminal.cursorShape(CursorShape.STEADY_UNDERLINE);
/*  656 */         return true;
/*      */       case 5:
/*  658 */         this.myTerminal.cursorShape(CursorShape.BLINK_VERTICAL_BAR);
/*  659 */         return true;
/*      */       case 6:
/*  661 */         this.myTerminal.cursorShape(CursorShape.STEADY_VERTICAL_BAR);
/*  662 */         return true;
/*      */     } 
/*  664 */     LOG.error("Setting cursor shape : unsupported parameter " + args.toString());
/*  665 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean insertLines(ControlSequence args) {
/*  670 */     this.myTerminal.insertLines(args.getArg(0, 1));
/*  671 */     return true;
/*      */   }
/*      */   
/*      */   private boolean sendDeviceAttributes() {
/*  675 */     if (LOG.isDebugEnabled()) {
/*  676 */       LOG.debug("Identifying to remote system as VT102");
/*      */     }
/*  678 */     this.myTerminal.deviceAttributes(CharUtils.VT102_RESPONSE);
/*      */     
/*  680 */     return true;
/*      */   }
/*      */   
/*      */   private boolean cursorHorizontalAbsolute(ControlSequence args) {
/*  684 */     int x = args.getArg(0, 1);
/*      */     
/*  686 */     this.myTerminal.cursorHorizontalAbsolute(x);
/*      */     
/*  688 */     return true;
/*      */   }
/*      */   
/*      */   private boolean cursorNextLine(ControlSequence args) {
/*  692 */     int dx = args.getArg(0, 1);
/*  693 */     dx = (dx == 0) ? 1 : dx;
/*  694 */     this.myTerminal.cursorDown(dx);
/*  695 */     this.myTerminal.cursorHorizontalAbsolute(1);
/*      */     
/*  697 */     return true;
/*      */   }
/*      */   
/*      */   private boolean cursorPrecedingLine(ControlSequence args) {
/*  701 */     int dx = args.getArg(0, 1);
/*  702 */     dx = (dx == 0) ? 1 : dx;
/*  703 */     this.myTerminal.cursorUp(dx);
/*      */     
/*  705 */     this.myTerminal.cursorHorizontalAbsolute(1);
/*      */     
/*  707 */     return true;
/*      */   }
/*      */   
/*      */   private boolean insertBlankCharacters(ControlSequence args) {
/*  711 */     int count = args.getArg(0, 1);
/*      */     
/*  713 */     this.myTerminal.insertBlankCharacters(count);
/*      */     
/*  715 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean eraseInDisplay(ControlSequence args) {
/*  720 */     int arg = args.getArg(0, 0);
/*      */     
/*  722 */     if (args.startsWithQuestionMark())
/*      */     {
/*  724 */       return false;
/*      */     }
/*      */     
/*  727 */     this.myTerminal.eraseInDisplay(arg);
/*      */     
/*  729 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean eraseInLine(ControlSequence args) {
/*  734 */     int arg = args.getArg(0, 0);
/*      */     
/*  736 */     if (args.startsWithQuestionMark())
/*      */     {
/*  738 */       return false;
/*      */     }
/*      */     
/*  741 */     this.myTerminal.eraseInLine(arg);
/*      */     
/*  743 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean deleteLines(ControlSequence args) {
/*  748 */     this.myTerminal.deleteLines(args.getArg(0, 1));
/*  749 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean deleteCharacters(ControlSequence args) {
/*  754 */     int arg = args.getArg(0, 1);
/*      */     
/*  756 */     this.myTerminal.deleteCharacters(arg);
/*      */     
/*  758 */     return true;
/*      */   }
/*      */   
/*      */   private boolean cursorBackward(ControlSequence args) {
/*  762 */     int dx = args.getArg(0, 1);
/*  763 */     dx = (dx == 0) ? 1 : dx;
/*      */     
/*  765 */     this.myTerminal.cursorBackward(dx);
/*      */     
/*  767 */     return true;
/*      */   }
/*      */   
/*      */   private boolean setScrollingRegion(ControlSequence args) {
/*  771 */     int top = args.getArg(0, 1);
/*  772 */     int bottom = args.getArg(1, this.myTerminal.getTerminalHeight());
/*      */     
/*  774 */     this.myTerminal.setScrollingRegion(top, bottom);
/*      */     
/*  776 */     return true;
/*      */   }
/*      */   
/*      */   private boolean scrollUp(ControlSequence args) {
/*  780 */     int count = args.getArg(0, 1);
/*  781 */     this.myTerminal.scrollUp(count);
/*  782 */     return true;
/*      */   }
/*      */   
/*      */   private boolean scrollDown(ControlSequence args) {
/*  786 */     int count = args.getArg(0, 1);
/*  787 */     this.myTerminal.scrollDown(count);
/*  788 */     return true;
/*      */   }
/*      */   
/*      */   private boolean cursorForward(ControlSequence args) {
/*  792 */     int countX = args.getArg(0, 1);
/*  793 */     countX = (countX == 0) ? 1 : countX;
/*      */     
/*  795 */     this.myTerminal.cursorForward(countX);
/*      */     
/*  797 */     return true;
/*      */   }
/*      */   
/*      */   private boolean cursorDown(ControlSequence cs) {
/*  801 */     int countY = cs.getArg(0, 0);
/*  802 */     countY = (countY == 0) ? 1 : countY;
/*  803 */     this.myTerminal.cursorDown(countY);
/*  804 */     return true;
/*      */   }
/*      */   
/*      */   private boolean cursorPosition(ControlSequence cs) {
/*  808 */     int argy = cs.getArg(0, 1);
/*  809 */     int argx = cs.getArg(1, 1);
/*      */     
/*  811 */     this.myTerminal.cursorPosition(argx, argy);
/*      */     
/*  813 */     return true;
/*      */   }
/*      */   
/*      */   private boolean characterAttributes(ControlSequence args) {
/*  817 */     TextStyle styleState = createStyleState(this.myTerminal.getStyleState().getCurrent(), args);
/*      */     
/*  819 */     this.myTerminal.characterAttributes(styleState);
/*      */     
/*  821 */     return true;
/*      */   }
/*      */   
/*      */   @NotNull
/*      */   private static TextStyle createStyleState(@NotNull TextStyle textStyle, ControlSequence args) {
/*  826 */     if (textStyle == null) $$$reportNull$$$0(0);  TextStyle.Builder builder = textStyle.toBuilder();
/*  827 */     int argCount = args.getCount();
/*  828 */     if (argCount == 0) {
/*  829 */       builder = new TextStyle.Builder();
/*      */     }
/*      */     
/*  832 */     int i = 0;
/*  833 */     while (i < argCount) {
/*  834 */       TerminalColor color256, bgColor256; int step = 1;
/*      */       
/*  836 */       int arg = args.getArg(i, -1);
/*  837 */       if (arg == -1) {
/*  838 */         LOG.error("Error in processing char attributes, arg " + i);
/*  839 */         i++;
/*      */         
/*      */         continue;
/*      */       } 
/*  843 */       switch (arg) {
/*      */         case 0:
/*  845 */           builder = new TextStyle.Builder();
/*      */           break;
/*      */         case 1:
/*  848 */           builder.setOption(TextStyle.Option.BOLD, true);
/*      */           break;
/*      */         case 2:
/*  851 */           builder.setOption(TextStyle.Option.DIM, true);
/*      */           break;
/*      */         case 3:
/*  854 */           builder.setOption(TextStyle.Option.ITALIC, true);
/*      */           break;
/*      */         case 4:
/*  857 */           builder.setOption(TextStyle.Option.UNDERLINED, true);
/*      */           break;
/*      */         case 5:
/*  860 */           builder.setOption(TextStyle.Option.BLINK, true);
/*      */           break;
/*      */         case 7:
/*  863 */           builder.setOption(TextStyle.Option.INVERSE, true);
/*      */           break;
/*      */         case 8:
/*  866 */           builder.setOption(TextStyle.Option.HIDDEN, true);
/*      */           break;
/*      */         case 22:
/*  869 */           builder.setOption(TextStyle.Option.BOLD, false);
/*  870 */           builder.setOption(TextStyle.Option.DIM, false);
/*      */           break;
/*      */         case 23:
/*  873 */           builder.setOption(TextStyle.Option.ITALIC, false);
/*      */           break;
/*      */         case 24:
/*  876 */           builder.setOption(TextStyle.Option.UNDERLINED, false);
/*      */           break;
/*      */         case 25:
/*  879 */           builder.setOption(TextStyle.Option.BLINK, false);
/*      */           break;
/*      */         case 27:
/*  882 */           builder.setOption(TextStyle.Option.INVERSE, false);
/*      */           break;
/*      */         case 28:
/*  885 */           builder.setOption(TextStyle.Option.HIDDEN, false);
/*      */           break;
/*      */         case 30:
/*      */         case 31:
/*      */         case 32:
/*      */         case 33:
/*      */         case 34:
/*      */         case 35:
/*      */         case 36:
/*      */         case 37:
/*  895 */           builder.setForeground(TerminalColor.index(arg - 30));
/*      */           break;
/*      */         case 38:
/*  898 */           color256 = getColor256(args, i);
/*  899 */           if (color256 != null) {
/*  900 */             builder.setForeground(color256);
/*  901 */             step = getColor256Step(args, i);
/*      */           } 
/*      */           break;
/*      */         case 39:
/*  905 */           builder.setForeground(null);
/*      */           break;
/*      */         case 40:
/*      */         case 41:
/*      */         case 42:
/*      */         case 43:
/*      */         case 44:
/*      */         case 45:
/*      */         case 46:
/*      */         case 47:
/*  915 */           builder.setBackground(TerminalColor.index(arg - 40));
/*      */           break;
/*      */         case 48:
/*  918 */           bgColor256 = getColor256(args, i);
/*  919 */           if (bgColor256 != null) {
/*  920 */             builder.setBackground(bgColor256);
/*  921 */             step = getColor256Step(args, i);
/*      */           } 
/*      */           break;
/*      */         case 49:
/*  925 */           builder.setBackground(null);
/*      */           break;
/*      */         
/*      */         case 90:
/*      */         case 91:
/*      */         case 92:
/*      */         case 93:
/*      */         case 94:
/*      */         case 95:
/*      */         case 96:
/*      */         case 97:
/*  936 */           builder.setForeground(ColorPalette.getIndexedTerminalColor(arg - 82));
/*      */           break;
/*      */         
/*      */         case 100:
/*      */         case 101:
/*      */         case 102:
/*      */         case 103:
/*      */         case 104:
/*      */         case 105:
/*      */         case 106:
/*      */         case 107:
/*  947 */           builder.setBackground(ColorPalette.getIndexedTerminalColor(arg - 92));
/*      */           break;
/*      */         default:
/*  950 */           LOG.error("Unknown character attribute:" + arg); break;
/*      */       } 
/*  952 */       i += step;
/*      */     } 
/*  954 */     if (builder.build() == null) $$$reportNull$$$0(1);  return builder.build();
/*      */   }
/*      */   
/*      */   private static TerminalColor getColor256(ControlSequence args, int index) {
/*  958 */     int code = args.getArg(index + 1, 0);
/*      */     
/*  960 */     if (code == 2) {
/*      */       
/*  962 */       int val0 = args.getArg(index + 2, -1);
/*  963 */       int val1 = args.getArg(index + 3, -1);
/*  964 */       int val2 = args.getArg(index + 4, -1);
/*  965 */       if (val0 >= 0 && val0 < 256 && val1 >= 0 && val1 < 256 && val2 >= 0 && val2 < 256)
/*      */       {
/*      */         
/*  968 */         return new TerminalColor(val0, val1, val2);
/*      */       }
/*  970 */       LOG.error("Bogus color setting " + args.toString());
/*  971 */       return null;
/*      */     } 
/*  973 */     if (code == 5)
/*      */     {
/*  975 */       return ColorPalette.getIndexedTerminalColor(args.getArg(index + 2, 0));
/*      */     }
/*  977 */     LOG.error("Unsupported code for color attribute " + args.toString());
/*  978 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getColor256Step(ControlSequence args, int i) {
/*  983 */     int code = args.getArg(i + 1, 0);
/*  984 */     if (code == 2)
/*  985 */       return 5; 
/*  986 */     if (code == 5) {
/*  987 */       return 3;
/*      */     }
/*  989 */     return 1;
/*      */   }
/*      */   
/*      */   private boolean cursorUp(ControlSequence cs) {
/*  993 */     int arg = cs.getArg(0, 0);
/*  994 */     arg = (arg == 0) ? 1 : arg;
/*  995 */     this.myTerminal.cursorUp(arg);
/*  996 */     return true;
/*      */   }
/*      */   
/*      */   private void setModeEnabled(TerminalMode mode, boolean enabled) {
/* 1000 */     if (LOG.isDebugEnabled()) {
/* 1001 */       LOG.info("Setting mode " + mode + " enabled = " + enabled);
/*      */     }
/* 1003 */     this.myTerminal.setModeEnabled(mode, enabled);
/*      */   }
/*      */   
/*      */   public void setMouseMode(MouseMode mouseMode) {
/* 1007 */     this.myTerminal.setMouseMode(mouseMode);
/*      */   }
/*      */   @NotNull
/*      */   public CompletableFuture<?> getPromptUpdatedAfterResizeFuture(@NotNull BiConsumer<Long, Runnable> taskScheduler) {
/* 1011 */     if (taskScheduler == null) $$$reportNull$$$0(2);  CompletableFuture<Void> resizeFuture = new CompletableFuture<>();
/* 1012 */     taskScheduler.accept(Long.valueOf(100L), this::completeResize);
/* 1013 */     this.myResizeFutureQueue.add(resizeFuture);
/* 1014 */     if (resizeFuture == null) $$$reportNull$$$0(3);  return resizeFuture;
/*      */   }
/*      */   
/*      */   private void completeResize() {
/*      */     CompletableFuture<Void> resizeFuture;
/* 1019 */     while ((resizeFuture = this.myResizeFutureQueue.poll()) != null)
/* 1020 */       resizeFuture.complete(null); 
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\emulator\JediEmulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */