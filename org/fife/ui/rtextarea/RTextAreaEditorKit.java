/*      */ package org.fife.ui.rtextarea;
/*      */ 
/*      */ import java.awt.Container;
/*      */ import java.awt.Font;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.io.IOException;
/*      */ import java.io.Reader;
/*      */ import java.text.DateFormat;
/*      */ import java.util.Date;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JViewport;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Caret;
/*      */ import javax.swing.text.DefaultEditorKit;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import javax.swing.text.NavigationFilter;
/*      */ import javax.swing.text.Position;
/*      */ import javax.swing.text.Segment;
/*      */ import javax.swing.text.Utilities;
/*      */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*      */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
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
/*      */ public class RTextAreaEditorKit
/*      */   extends DefaultEditorKit
/*      */ {
/*      */   public static final String rtaBeginRecordingMacroAction = "RTA.BeginRecordingMacroAction";
/*      */   public static final String rtaDecreaseFontSizeAction = "RTA.DecreaseFontSizeAction";
/*      */   public static final String rtaDeleteLineAction = "RTA.DeleteLineAction";
/*      */   public static final String rtaDeletePrevWordAction = "RTA.DeletePrevWordAction";
/*      */   public static final String rtaDeleteRestOfLineAction = "RTA.DeleteRestOfLineAction";
/*      */   public static final String rtaDumbCompleteWordAction = "RTA.DumbCompleteWordAction";
/*      */   public static final String rtaEndRecordingMacroAction = "RTA.EndRecordingMacroAction";
/*      */   public static final String rtaIncreaseFontSizeAction = "RTA.IncreaseFontSizeAction";
/*      */   public static final String rtaInvertSelectionCaseAction = "RTA.InvertCaseAction";
/*      */   public static final String rtaJoinLinesAction = "RTA.JoinLinesAction";
/*      */   public static final String rtaLineDownAction = "RTA.LineDownAction";
/*      */   public static final String rtaLineUpAction = "RTA.LineUpAction";
/*      */   public static final String rtaLowerSelectionCaseAction = "RTA.LowerCaseAction";
/*      */   public static final String rtaNextOccurrenceAction = "RTA.NextOccurrenceAction";
/*      */   public static final String rtaPrevOccurrenceAction = "RTA.PrevOccurrenceAction";
/*      */   public static final String rtaNextBookmarkAction = "RTA.NextBookmarkAction";
/*      */   public static final String clipboardHistoryAction = "RTA.PasteHistoryAction";
/*      */   public static final String rtaPrevBookmarkAction = "RTA.PrevBookmarkAction";
/*      */   public static final String rtaPlaybackLastMacroAction = "RTA.PlaybackLastMacroAction";
/*      */   public static final String rtaRedoAction = "RTA.RedoAction";
/*      */   public static final String rtaScrollDownAction = "RTA.ScrollDownAction";
/*      */   public static final String rtaScrollUpAction = "RTA.ScrollUpAction";
/*      */   public static final String rtaSelectionPageUpAction = "RTA.SelectionPageUpAction";
/*      */   public static final String rtaSelectionPageDownAction = "RTA.SelectionPageDownAction";
/*      */   public static final String rtaSelectionPageLeftAction = "RTA.SelectionPageLeftAction";
/*      */   public static final String rtaSelectionPageRightAction = "RTA.SelectionPageRightAction";
/*      */   public static final String rtaTimeDateAction = "RTA.TimeDateAction";
/*      */   public static final String rtaToggleBookmarkAction = "RTA.ToggleBookmarkAction";
/*      */   public static final String rtaToggleTextModeAction = "RTA.ToggleTextModeAction";
/*      */   public static final String rtaUndoAction = "RTA.UndoAction";
/*      */   public static final String rtaUnselectAction = "RTA.UnselectAction";
/*      */   public static final String rtaUpperSelectionCaseAction = "RTA.UpperCaseAction";
/*  230 */   private static final RecordableTextAction[] defaultActions = new RecordableTextAction[] { new BeginAction("caret-begin", false), new BeginAction("selection-begin", true), new BeginLineAction("caret-begin-line", false), new BeginLineAction("selection-begin-line", true), new BeginRecordingMacroAction(), new BeginWordAction("caret-begin-word", false), new BeginWordAction("selection-begin-word", true), new ClipboardHistoryAction(), new CopyAction(), new CutAction(), new DefaultKeyTypedAction(), new DeleteLineAction(), new DeleteNextCharAction(), new DeletePrevCharAction(), new DeletePrevWordAction(), new DeleteRestOfLineAction(), new DumbCompleteWordAction(), new EndAction("caret-end", false), new EndAction("selection-end", true), new EndLineAction("caret-end-line", false), new EndLineAction("selection-end-line", true), new EndRecordingMacroAction(), new EndWordAction("caret-end-word", false), new EndWordAction("caret-end-word", true), new InsertBreakAction(), new InsertContentAction(), new InsertTabAction(), new InvertSelectionCaseAction(), new JoinLinesAction(), new LowerSelectionCaseAction(), new LineMoveAction("RTA.LineUpAction", -1), new LineMoveAction("RTA.LineDownAction", 1), new NextBookmarkAction("RTA.NextBookmarkAction", true), new NextBookmarkAction("RTA.PrevBookmarkAction", false), new NextVisualPositionAction("caret-forward", false, 3), new NextVisualPositionAction("caret-backward", false, 7), new NextVisualPositionAction("selection-forward", true, 3), new NextVisualPositionAction("selection-backward", true, 7), new NextVisualPositionAction("caret-up", false, 1), new NextVisualPositionAction("caret-down", false, 5), new NextVisualPositionAction("selection-up", true, 1), new NextVisualPositionAction("selection-down", true, 5), new NextOccurrenceAction("RTA.NextOccurrenceAction"), new PreviousOccurrenceAction("RTA.PrevOccurrenceAction"), new NextWordAction("caret-next-word", false), new NextWordAction("selection-next-word", true), new PageAction("RTA.SelectionPageLeftAction", true, true), new PageAction("RTA.SelectionPageRightAction", false, true), new PasteAction(), new PlaybackLastMacroAction(), new PreviousWordAction("caret-previous-word", false), new PreviousWordAction("selection-previous-word", true), new RedoAction(), new ScrollAction("RTA.ScrollUpAction", -1), new ScrollAction("RTA.ScrollDownAction", 1), new SelectAllAction(), new SelectLineAction(), new SelectWordAction(), new SetReadOnlyAction(), new SetWritableAction(), new ToggleBookmarkAction(), new ToggleTextModeAction(), new UndoAction(), new UnselectAction(), new UpperSelectionCaseAction(), new VerticalPageAction("page-up", -1, false), new VerticalPageAction("page-down", 1, false), new VerticalPageAction("RTA.SelectionPageUpAction", -1, true), new VerticalPageAction("RTA.SelectionPageDownAction", 1, true) };
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
/*      */   private static final int READBUFFER_SIZE = 32768;
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
/*      */   public IconRowHeader createIconRowHeader(RTextArea textArea) {
/*  323 */     return new IconRowHeader(textArea);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LineNumberList createLineNumberList(RTextArea textArea) {
/*  334 */     return new LineNumberList(textArea);
/*      */   }
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
/*      */   public Action[] getActions() {
/*  347 */     return (Action[])defaultActions;
/*      */   }
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
/*      */   public void read(Reader in, Document doc, int pos) throws IOException, BadLocationException {
/*  368 */     char[] buff = new char[32768];
/*      */     
/*  370 */     boolean lastWasCR = false;
/*  371 */     boolean isCRLF = false;
/*  372 */     boolean isCR = false;
/*      */     
/*  374 */     boolean wasEmpty = (doc.getLength() == 0);
/*      */ 
/*      */ 
/*      */     
/*      */     int nch;
/*      */ 
/*      */     
/*  381 */     while ((nch = in.read(buff, 0, buff.length)) != -1) {
/*  382 */       int last = 0;
/*  383 */       for (int counter = 0; counter < nch; counter++) {
/*  384 */         switch (buff[counter]) {
/*      */           case '\r':
/*  386 */             if (lastWasCR) {
/*  387 */               isCR = true;
/*  388 */               if (counter == 0) {
/*  389 */                 doc.insertString(pos, "\n", null);
/*  390 */                 pos++;
/*      */                 break;
/*      */               } 
/*  393 */               buff[counter - 1] = '\n';
/*      */               
/*      */               break;
/*      */             } 
/*  397 */             lastWasCR = true;
/*      */             break;
/*      */           
/*      */           case '\n':
/*  401 */             if (lastWasCR) {
/*  402 */               if (counter > last + 1) {
/*  403 */                 doc.insertString(pos, new String(buff, last, counter - last - 1), null);
/*      */                 
/*  405 */                 pos += counter - last - 1;
/*      */               } 
/*      */ 
/*      */               
/*  409 */               lastWasCR = false;
/*  410 */               last = counter;
/*  411 */               isCRLF = true;
/*      */             } 
/*      */             break;
/*      */           default:
/*  415 */             if (lastWasCR) {
/*  416 */               isCR = true;
/*  417 */               if (counter == 0) {
/*  418 */                 doc.insertString(pos, "\n", null);
/*  419 */                 pos++;
/*      */               } else {
/*      */                 
/*  422 */                 buff[counter - 1] = '\n';
/*      */               } 
/*  424 */               lastWasCR = false;
/*      */             } 
/*      */             break;
/*      */         } 
/*      */       
/*      */       } 
/*  430 */       if (last < nch) {
/*  431 */         if (lastWasCR) {
/*  432 */           if (last < nch - 1) {
/*  433 */             doc.insertString(pos, new String(buff, last, nch - last - 1), null);
/*      */             
/*  435 */             pos += nch - last - 1;
/*      */           } 
/*      */           continue;
/*      */         } 
/*  439 */         doc.insertString(pos, new String(buff, last, nch - last), null);
/*      */         
/*  441 */         pos += nch - last;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  447 */     if (lastWasCR) {
/*  448 */       doc.insertString(pos, "\n", null);
/*  449 */       isCR = true;
/*      */     } 
/*      */     
/*  452 */     if (wasEmpty) {
/*  453 */       if (isCRLF) {
/*  454 */         doc.putProperty("__EndOfLine__", "\r\n");
/*      */       }
/*  456 */       else if (isCR) {
/*  457 */         doc.putProperty("__EndOfLine__", "\r");
/*      */       } else {
/*      */         
/*  460 */         doc.putProperty("__EndOfLine__", "\n");
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class BeepAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public BeepAction() {
/*  473 */       super("beep");
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  478 */       UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  483 */       return "beep";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class BeginAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */ 
/*      */     
/*      */     public BeginAction(String name, boolean select) {
/*  497 */       super(name);
/*  498 */       this.select = select;
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  503 */       if (this.select) {
/*  504 */         textArea.moveCaretPosition(0);
/*      */       } else {
/*      */         
/*  507 */         textArea.setCaretPosition(0);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  513 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class BeginLineAction
/*      */     extends RecordableTextAction
/*      */   {
/*  525 */     private Segment currentLine = new Segment();
/*      */     private boolean select;
/*      */     
/*      */     public BeginLineAction(String name, boolean select) {
/*  529 */       super(name);
/*  530 */       this.select = select;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  536 */       int newPos = 0;
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  541 */         if (textArea.getLineWrap()) {
/*  542 */           int offs = textArea.getCaretPosition();
/*      */ 
/*      */ 
/*      */           
/*  546 */           int begOffs = Utilities.getRowStart(textArea, offs);
/*      */ 
/*      */ 
/*      */           
/*  550 */           newPos = begOffs;
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */ 
/*      */           
/*  559 */           int caretPosition = textArea.getCaretPosition();
/*  560 */           Document document = textArea.getDocument();
/*  561 */           Element map = document.getDefaultRootElement();
/*  562 */           int currentLineNum = map.getElementIndex(caretPosition);
/*  563 */           Element currentLineElement = map.getElement(currentLineNum);
/*  564 */           int currentLineStart = currentLineElement.getStartOffset();
/*  565 */           int currentLineEnd = currentLineElement.getEndOffset();
/*  566 */           int count = currentLineEnd - currentLineStart;
/*  567 */           if (count > 0) {
/*  568 */             document.getText(currentLineStart, count, this.currentLine);
/*  569 */             int firstNonWhitespace = getFirstNonWhitespacePos();
/*  570 */             firstNonWhitespace = currentLineStart + firstNonWhitespace - this.currentLine.offset;
/*      */             
/*  572 */             if (caretPosition != firstNonWhitespace) {
/*  573 */               newPos = firstNonWhitespace;
/*      */             } else {
/*      */               
/*  576 */               newPos = currentLineStart;
/*      */             } 
/*      */           } else {
/*      */             
/*  580 */             newPos = currentLineStart;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  585 */         if (this.select) {
/*  586 */           textArea.moveCaretPosition(newPos);
/*      */         } else {
/*      */           
/*  589 */           textArea.setCaretPosition(newPos);
/*      */         }
/*      */       
/*      */       }
/*  593 */       catch (BadLocationException ble) {
/*      */         
/*  595 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*  596 */         ble.printStackTrace();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private int getFirstNonWhitespacePos() {
/*  602 */       int offset = this.currentLine.offset;
/*  603 */       int end = offset + this.currentLine.count - 1;
/*  604 */       int pos = offset;
/*  605 */       char[] array = this.currentLine.array;
/*  606 */       char currentChar = array[pos];
/*  607 */       while ((currentChar == '\t' || currentChar == ' ') && ++pos < end) {
/*  608 */         currentChar = array[pos];
/*      */       }
/*  610 */       return pos;
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  615 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class BeginRecordingMacroAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public BeginRecordingMacroAction() {
/*  627 */       super("RTA.BeginRecordingMacroAction");
/*      */     }
/*      */ 
/*      */     
/*      */     public BeginRecordingMacroAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/*  632 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  637 */       RTextArea.beginRecordingMacro();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isRecordable() {
/*  642 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  647 */       return "RTA.BeginRecordingMacroAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class BeginWordAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */ 
/*      */     
/*      */     protected BeginWordAction(String name, boolean select) {
/*  661 */       super(name);
/*  662 */       this.select = select;
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*      */       try {
/*  668 */         int offs = textArea.getCaretPosition();
/*  669 */         int begOffs = getWordStart(textArea, offs);
/*  670 */         if (this.select) {
/*  671 */           textArea.moveCaretPosition(begOffs);
/*      */         } else {
/*      */           
/*  674 */           textArea.setCaretPosition(begOffs);
/*      */         } 
/*  676 */       } catch (BadLocationException ble) {
/*  677 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  683 */       return getName();
/*      */     }
/*      */ 
/*      */     
/*      */     protected int getWordStart(RTextArea textArea, int offs) throws BadLocationException {
/*  688 */       return Utilities.getWordStart(textArea, offs);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ClipboardHistoryAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private ClipboardHistory clipboardHistory;
/*      */ 
/*      */ 
/*      */     
/*      */     public ClipboardHistoryAction() {
/*  703 */       super("RTA.PasteHistoryAction");
/*  704 */       this.clipboardHistory = ClipboardHistory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     public ClipboardHistoryAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/*  709 */       super(name, icon, desc, mnemonic, accelerator);
/*  710 */       this.clipboardHistory = ClipboardHistory.get();
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  715 */       Window owner = SwingUtilities.getWindowAncestor(textArea);
/*  716 */       ClipboardHistoryPopup popup = new ClipboardHistoryPopup(owner, textArea);
/*  717 */       popup.setContents(this.clipboardHistory.getHistory());
/*  718 */       popup.setVisible(true);
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  723 */       return "RTA.PasteHistoryAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class CopyAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public CopyAction() {
/*  735 */       super("copy-to-clipboard");
/*      */     }
/*      */ 
/*      */     
/*      */     public CopyAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/*  740 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  745 */       textArea.copy();
/*  746 */       textArea.requestFocusInWindow();
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  751 */       return "copy-to-clipboard";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class CutAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public CutAction() {
/*  763 */       super("cut-to-clipboard");
/*      */     }
/*      */ 
/*      */     
/*      */     public CutAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/*  768 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  773 */       textArea.cut();
/*  774 */       textArea.requestFocusInWindow();
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  779 */       return "cut-to-clipboard";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class DecreaseFontSizeAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     protected float decreaseAmount;
/*      */ 
/*      */     
/*      */     protected static final float MINIMUM_SIZE = 2.0F;
/*      */ 
/*      */     
/*      */     public DecreaseFontSizeAction() {
/*  795 */       super("RTA.DecreaseFontSizeAction");
/*  796 */       initialize();
/*      */     }
/*      */ 
/*      */     
/*      */     public DecreaseFontSizeAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/*  801 */       super(name, icon, desc, mnemonic, accelerator);
/*  802 */       initialize();
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  807 */       Font font = textArea.getFont();
/*  808 */       float oldSize = font.getSize2D();
/*  809 */       float newSize = oldSize - this.decreaseAmount;
/*  810 */       if (newSize >= 2.0F) {
/*      */         
/*  812 */         font = font.deriveFont(newSize);
/*  813 */         textArea.setFont(font);
/*      */       }
/*  815 */       else if (oldSize > 2.0F) {
/*      */ 
/*      */         
/*  818 */         font = font.deriveFont(2.0F);
/*  819 */         textArea.setFont(font);
/*      */       }
/*      */       else {
/*      */         
/*  823 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */       } 
/*  825 */       textArea.requestFocusInWindow();
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  830 */       return "RTA.DecreaseFontSizeAction";
/*      */     }
/*      */     
/*      */     protected void initialize() {
/*  834 */       this.decreaseAmount = 1.0F;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class DefaultKeyTypedAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private Action delegate;
/*      */ 
/*      */ 
/*      */     
/*      */     public DefaultKeyTypedAction() {
/*  849 */       super("default-typed", (Icon)null, (String)null, (Integer)null, (KeyStroke)null);
/*      */       
/*  851 */       this.delegate = new DefaultEditorKit.DefaultKeyTypedAction();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  862 */       this.delegate.actionPerformed(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  867 */       return "default-typed";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class DeleteLineAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public DeleteLineAction() {
/*  879 */       super("RTA.DeleteLineAction", (Icon)null, (String)null, (Integer)null, (KeyStroke)null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  886 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/*  887 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */         
/*      */         return;
/*      */       } 
/*  891 */       int selStart = textArea.getSelectionStart();
/*  892 */       int selEnd = textArea.getSelectionEnd();
/*      */ 
/*      */       
/*      */       try {
/*  896 */         int line1 = textArea.getLineOfOffset(selStart);
/*  897 */         int startOffs = textArea.getLineStartOffset(line1);
/*  898 */         int line2 = textArea.getLineOfOffset(selEnd);
/*  899 */         int endOffs = textArea.getLineEndOffset(line2);
/*      */ 
/*      */         
/*  902 */         if (line2 > line1 && 
/*  903 */           selEnd == textArea.getLineStartOffset(line2)) {
/*  904 */           endOffs = selEnd;
/*      */         }
/*      */ 
/*      */         
/*  908 */         textArea.replaceRange((String)null, startOffs, endOffs);
/*      */       }
/*  910 */       catch (BadLocationException ble) {
/*  911 */         ble.printStackTrace();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  918 */       return "RTA.DeleteLineAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class DeleteNextCharAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public DeleteNextCharAction() {
/*  931 */       super("delete-next", (Icon)null, (String)null, (Integer)null, (KeyStroke)null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public DeleteNextCharAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/*  937 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  943 */       boolean beep = true;
/*  944 */       if (textArea != null && textArea.isEditable()) {
/*      */         try {
/*  946 */           Document doc = textArea.getDocument();
/*  947 */           Caret caret = textArea.getCaret();
/*  948 */           int dot = caret.getDot();
/*  949 */           int mark = caret.getMark();
/*  950 */           if (dot != mark) {
/*  951 */             doc.remove(Math.min(dot, mark), Math.abs(dot - mark));
/*  952 */             beep = false;
/*      */           }
/*  954 */           else if (dot < doc.getLength()) {
/*  955 */             int delChars = 1;
/*  956 */             if (dot < doc.getLength() - 1) {
/*  957 */               String dotChars = doc.getText(dot, 2);
/*  958 */               char c0 = dotChars.charAt(0);
/*  959 */               char c1 = dotChars.charAt(1);
/*  960 */               if (c0 >= '?' && c0 <= '?' && c1 >= '?' && c1 <= '?')
/*      */               {
/*  962 */                 delChars = 2;
/*      */               }
/*      */             } 
/*  965 */             doc.remove(dot, delChars);
/*  966 */             beep = false;
/*      */           } 
/*  968 */         } catch (BadLocationException badLocationException) {}
/*      */       }
/*      */ 
/*      */       
/*  972 */       if (beep) {
/*  973 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */       }
/*  975 */       if (textArea != null) {
/*  976 */         textArea.requestFocusInWindow();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  983 */       return "delete-next";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class DeletePrevCharAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public DeletePrevCharAction() {
/*  996 */       super("delete-previous");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1002 */       boolean beep = true;
/* 1003 */       if (textArea != null && textArea.isEditable()) {
/*      */         try {
/* 1005 */           Document doc = textArea.getDocument();
/* 1006 */           Caret caret = textArea.getCaret();
/* 1007 */           int dot = caret.getDot();
/* 1008 */           int mark = caret.getMark();
/* 1009 */           if (dot != mark) {
/* 1010 */             doc.remove(Math.min(dot, mark), Math.abs(dot - mark));
/* 1011 */             beep = false;
/*      */           }
/* 1013 */           else if (dot > 0) {
/* 1014 */             int delChars = 1;
/* 1015 */             if (dot > 1) {
/* 1016 */               String dotChars = doc.getText(dot - 2, 2);
/* 1017 */               char c0 = dotChars.charAt(0);
/* 1018 */               char c1 = dotChars.charAt(1);
/* 1019 */               if (c0 >= '?' && c0 <= '?' && c1 >= '?' && c1 <= '?')
/*      */               {
/* 1021 */                 delChars = 2;
/*      */               }
/*      */             } 
/* 1024 */             doc.remove(dot - delChars, delChars);
/* 1025 */             beep = false;
/*      */           } 
/* 1027 */         } catch (BadLocationException badLocationException) {}
/*      */       }
/*      */ 
/*      */       
/* 1031 */       if (beep) {
/* 1032 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1039 */       return "delete-previous";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class DeletePrevWordAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public DeletePrevWordAction() {
/* 1051 */       super("RTA.DeletePrevWordAction");
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1056 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/* 1057 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */         return;
/*      */       } 
/*      */       try {
/* 1061 */         int end = textArea.getSelectionStart();
/* 1062 */         int start = getPreviousWordStart(textArea, end);
/* 1063 */         if (end > start) {
/* 1064 */           textArea.getDocument().remove(start, end - start);
/*      */         }
/* 1066 */       } catch (BadLocationException ex) {
/* 1067 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public String getMacroID() {
/* 1073 */       return "RTA.DeletePrevWordAction";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int getPreviousWordStart(RTextArea textArea, int end) throws BadLocationException {
/* 1082 */       return Utilities.getPreviousWord(textArea, end);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class DeleteRestOfLineAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public DeleteRestOfLineAction() {
/* 1095 */       super("RTA.DeleteRestOfLineAction");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1101 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/* 1102 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/* 1110 */         Document document = textArea.getDocument();
/* 1111 */         int caretPosition = textArea.getCaretPosition();
/* 1112 */         Element map = document.getDefaultRootElement();
/* 1113 */         int currentLineNum = map.getElementIndex(caretPosition);
/* 1114 */         Element currentLineElement = map.getElement(currentLineNum);
/*      */         
/* 1116 */         int currentLineEnd = currentLineElement.getEndOffset() - 1;
/* 1117 */         if (caretPosition < currentLineEnd) {
/* 1118 */           document.remove(caretPosition, currentLineEnd - caretPosition);
/*      */         
/*      */         }
/*      */       }
/* 1122 */       catch (BadLocationException ble) {
/* 1123 */         ble.printStackTrace();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1130 */       return "RTA.DeleteRestOfLineAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class DumbCompleteWordAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private int lastWordStart;
/*      */ 
/*      */     
/*      */     private int lastDot;
/*      */     
/*      */     private int searchOffs;
/*      */     
/*      */     private String lastPrefix;
/*      */ 
/*      */     
/*      */     public DumbCompleteWordAction() {
/* 1150 */       super("RTA.DumbCompleteWordAction");
/* 1151 */       this.lastWordStart = this.searchOffs = this.lastDot = -1;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1157 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/* 1163 */         int dot = textArea.getCaretPosition();
/* 1164 */         if (dot == 0) {
/*      */           return;
/*      */         }
/*      */         
/* 1168 */         int curWordStart = getWordStart(textArea, dot);
/*      */         
/* 1170 */         if (this.lastWordStart != curWordStart || dot != this.lastDot) {
/* 1171 */           this.lastPrefix = textArea.getText(curWordStart, dot - curWordStart);
/*      */ 
/*      */           
/* 1174 */           if (!isAcceptablePrefix(this.lastPrefix)) {
/* 1175 */             UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */             return;
/*      */           } 
/* 1178 */           this.lastWordStart = dot - this.lastPrefix.length();
/*      */ 
/*      */           
/* 1181 */           this.searchOffs = Math.max(this.lastWordStart - 1, 0);
/*      */         } 
/*      */         
/* 1184 */         while (this.searchOffs > 0) {
/*      */           int wordStart;
/*      */           try {
/* 1187 */             wordStart = getPreviousWord(textArea, this.searchOffs);
/* 1188 */           } catch (BadLocationException ble) {
/*      */ 
/*      */ 
/*      */             
/* 1192 */             wordStart = -1;
/*      */           } 
/* 1194 */           if (wordStart == -1) {
/* 1195 */             UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */             
/*      */             break;
/*      */           } 
/* 1199 */           int end = getWordEnd(textArea, wordStart);
/* 1200 */           String word = textArea.getText(wordStart, end - wordStart);
/* 1201 */           this.searchOffs = wordStart;
/* 1202 */           if (word.startsWith(this.lastPrefix)) {
/* 1203 */             textArea.replaceRange(word, this.lastWordStart, dot);
/* 1204 */             this.lastDot = textArea.getCaretPosition();
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/* 1209 */       } catch (BadLocationException ble) {
/* 1210 */         ble.printStackTrace();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1217 */       return getName();
/*      */     }
/*      */ 
/*      */     
/*      */     protected int getPreviousWord(RTextArea textArea, int offs) throws BadLocationException {
/* 1222 */       return Utilities.getPreviousWord(textArea, offs);
/*      */     }
/*      */ 
/*      */     
/*      */     protected int getWordEnd(RTextArea textArea, int offs) throws BadLocationException {
/* 1227 */       return Utilities.getWordEnd(textArea, offs);
/*      */     }
/*      */ 
/*      */     
/*      */     protected int getWordStart(RTextArea textArea, int offs) throws BadLocationException {
/* 1232 */       return Utilities.getWordStart(textArea, offs);
/*      */     }
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
/*      */     protected boolean isAcceptablePrefix(String prefix) {
/* 1246 */       return (prefix.length() > 0 && 
/* 1247 */         Character.isLetter(prefix.charAt(prefix.length() - 1)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class EndAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */ 
/*      */     
/*      */     public EndAction(String name, boolean select) {
/* 1261 */       super(name);
/* 1262 */       this.select = select;
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1267 */       int dot = getVisibleEnd(textArea);
/* 1268 */       if (this.select) {
/* 1269 */         textArea.moveCaretPosition(dot);
/*      */       } else {
/*      */         
/* 1272 */         textArea.setCaretPosition(dot);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1278 */       return getName();
/*      */     }
/*      */     
/*      */     protected int getVisibleEnd(RTextArea textArea) {
/* 1282 */       return textArea.getDocument().getLength();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class EndLineAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */ 
/*      */     
/*      */     public EndLineAction(String name, boolean select) {
/* 1296 */       super(name);
/* 1297 */       this.select = select;
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1302 */       int offs = textArea.getCaretPosition();
/* 1303 */       int endOffs = 0;
/*      */       try {
/* 1305 */         if (textArea.getLineWrap()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1311 */           endOffs = Utilities.getRowEnd(textArea, offs);
/*      */         } else {
/*      */           
/* 1314 */           Element root = textArea.getDocument().getDefaultRootElement();
/* 1315 */           int line = root.getElementIndex(offs);
/* 1316 */           endOffs = root.getElement(line).getEndOffset() - 1;
/*      */         } 
/* 1318 */         if (this.select) {
/* 1319 */           textArea.moveCaretPosition(endOffs);
/*      */         } else {
/*      */           
/* 1322 */           textArea.setCaretPosition(endOffs);
/*      */         } 
/* 1324 */       } catch (Exception ex) {
/* 1325 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1331 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class EndRecordingMacroAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public EndRecordingMacroAction() {
/* 1343 */       super("RTA.EndRecordingMacroAction");
/*      */     }
/*      */ 
/*      */     
/*      */     public EndRecordingMacroAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/* 1348 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1353 */       RTextArea.endRecordingMacro();
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1358 */       return "RTA.EndRecordingMacroAction";
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isRecordable() {
/* 1363 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class EndWordAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */ 
/*      */     
/*      */     protected EndWordAction(String name, boolean select) {
/* 1377 */       super(name);
/* 1378 */       this.select = select;
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*      */       try {
/* 1384 */         int offs = textArea.getCaretPosition();
/* 1385 */         int endOffs = getWordEnd(textArea, offs);
/* 1386 */         if (this.select) {
/* 1387 */           textArea.moveCaretPosition(endOffs);
/*      */         } else {
/*      */           
/* 1390 */           textArea.setCaretPosition(endOffs);
/*      */         } 
/* 1392 */       } catch (BadLocationException ble) {
/* 1393 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1399 */       return getName();
/*      */     }
/*      */ 
/*      */     
/*      */     protected int getWordEnd(RTextArea textArea, int offs) throws BadLocationException {
/* 1404 */       return Utilities.getWordEnd(textArea, offs);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class IncreaseFontSizeAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     protected float increaseAmount;
/*      */ 
/*      */     
/*      */     protected static final float MAXIMUM_SIZE = 40.0F;
/*      */ 
/*      */     
/*      */     public IncreaseFontSizeAction() {
/* 1420 */       super("RTA.IncreaseFontSizeAction");
/* 1421 */       initialize();
/*      */     }
/*      */ 
/*      */     
/*      */     public IncreaseFontSizeAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/* 1426 */       super(name, icon, desc, mnemonic, accelerator);
/* 1427 */       initialize();
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1432 */       Font font = textArea.getFont();
/* 1433 */       float oldSize = font.getSize2D();
/* 1434 */       float newSize = oldSize + this.increaseAmount;
/* 1435 */       if (newSize <= 40.0F) {
/*      */         
/* 1437 */         font = font.deriveFont(newSize);
/* 1438 */         textArea.setFont(font);
/*      */       }
/* 1440 */       else if (oldSize < 40.0F) {
/*      */ 
/*      */         
/* 1443 */         font = font.deriveFont(40.0F);
/* 1444 */         textArea.setFont(font);
/*      */       }
/*      */       else {
/*      */         
/* 1448 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */       } 
/* 1450 */       textArea.requestFocusInWindow();
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1455 */       return "RTA.IncreaseFontSizeAction";
/*      */     }
/*      */     
/*      */     protected void initialize() {
/* 1459 */       this.increaseAmount = 1.0F;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class InsertBreakAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public InsertBreakAction() {
/* 1471 */       super("insert-break");
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1476 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/* 1477 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */         return;
/*      */       } 
/* 1480 */       textArea.replaceSelection("\n");
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1485 */       return "insert-break";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isEnabled() {
/* 1495 */       JTextComponent tc = getTextComponent((ActionEvent)null);
/* 1496 */       return (tc == null || tc.isEditable()) ? super.isEnabled() : false;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class InsertContentAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public InsertContentAction() {
/* 1508 */       super("insert-content", (Icon)null, (String)null, (Integer)null, (KeyStroke)null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1514 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/* 1515 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */         return;
/*      */       } 
/* 1518 */       String content = e.getActionCommand();
/* 1519 */       if (content != null) {
/* 1520 */         textArea.replaceSelection(content);
/*      */       } else {
/*      */         
/* 1523 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1529 */       return "insert-content";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class InsertTabAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public InsertTabAction() {
/* 1542 */       super("insert-tab");
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1547 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/* 1548 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */         return;
/*      */       } 
/* 1551 */       textArea.replaceSelection("\t");
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1556 */       return "insert-tab";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class InvertSelectionCaseAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public InvertSelectionCaseAction() {
/* 1568 */       super("RTA.InvertCaseAction");
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1573 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/* 1574 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */         return;
/*      */       } 
/* 1577 */       String selection = textArea.getSelectedText();
/* 1578 */       if (selection != null) {
/* 1579 */         StringBuilder buffer = new StringBuilder(selection);
/* 1580 */         int length = buffer.length();
/* 1581 */         for (int i = 0; i < length; i++) {
/* 1582 */           char c = buffer.charAt(i);
/* 1583 */           if (Character.isUpperCase(c)) {
/* 1584 */             buffer.setCharAt(i, Character.toLowerCase(c));
/*      */           }
/* 1586 */           else if (Character.isLowerCase(c)) {
/* 1587 */             buffer.setCharAt(i, Character.toUpperCase(c));
/*      */           } 
/*      */         } 
/* 1590 */         textArea.replaceSelection(buffer.toString());
/*      */       } 
/* 1592 */       textArea.requestFocusInWindow();
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1597 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class JoinLinesAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public JoinLinesAction() {
/* 1609 */       super("RTA.JoinLinesAction");
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1614 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/* 1615 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */         return;
/*      */       } 
/*      */       try {
/* 1619 */         Caret c = textArea.getCaret();
/* 1620 */         int caretPos = c.getDot();
/* 1621 */         Document doc = textArea.getDocument();
/* 1622 */         Element map = doc.getDefaultRootElement();
/* 1623 */         int lineCount = map.getElementCount();
/* 1624 */         int line = map.getElementIndex(caretPos);
/* 1625 */         if (line == lineCount - 1) {
/* 1626 */           UIManager.getLookAndFeel()
/* 1627 */             .provideErrorFeedback(textArea);
/*      */           return;
/*      */         } 
/* 1630 */         Element lineElem = map.getElement(line);
/* 1631 */         caretPos = lineElem.getEndOffset() - 1;
/* 1632 */         c.setDot(caretPos);
/* 1633 */         doc.remove(caretPos, 1);
/* 1634 */       } catch (BadLocationException ble) {
/*      */         
/* 1636 */         ble.printStackTrace();
/*      */       } 
/* 1638 */       textArea.requestFocusInWindow();
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1643 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class LineMoveAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private int moveAmt;
/*      */ 
/*      */ 
/*      */     
/*      */     public LineMoveAction(String name, int moveAmt) {
/* 1657 */       super(name);
/* 1658 */       this.moveAmt = moveAmt;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1664 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/* 1665 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*      */       try {
/* 1671 */         int dot = textArea.getCaretPosition();
/* 1672 */         int mark = textArea.getCaret().getMark();
/* 1673 */         Document doc = textArea.getDocument();
/* 1674 */         Element root = doc.getDefaultRootElement();
/* 1675 */         int startLine = root.getElementIndex(Math.min(dot, mark));
/* 1676 */         int endLine = root.getElementIndex(Math.max(dot, mark));
/*      */ 
/*      */ 
/*      */         
/* 1680 */         int moveCount = endLine - startLine + 1;
/* 1681 */         if (moveCount > 1) {
/* 1682 */           Element elem = root.getElement(endLine);
/* 1683 */           if (dot == elem.getStartOffset() || mark == elem.getStartOffset()) {
/* 1684 */             moveCount--;
/*      */           }
/*      */         } 
/*      */         
/* 1688 */         if (this.moveAmt == -1 && startLine > 0) {
/* 1689 */           moveLineUp(textArea, startLine, moveCount);
/*      */         }
/* 1691 */         else if (this.moveAmt == 1 && endLine < root.getElementCount() - 1) {
/* 1692 */           moveLineDown(textArea, startLine, moveCount);
/*      */         } else {
/*      */           
/* 1695 */           UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */           return;
/*      */         } 
/* 1698 */       } catch (BadLocationException ble) {
/*      */         
/* 1700 */         ble.printStackTrace();
/* 1701 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */         return;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1708 */       return getName();
/*      */     }
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
/*      */     private void moveLineDown(RTextArea textArea, int line, int lineCount) throws BadLocationException {
/* 1722 */       Document doc = textArea.getDocument();
/* 1723 */       Element root = doc.getDefaultRootElement();
/* 1724 */       Element elem = root.getElement(line);
/* 1725 */       int start = elem.getStartOffset();
/*      */       
/* 1727 */       int endLine = line + lineCount - 1;
/* 1728 */       elem = root.getElement(endLine);
/* 1729 */       int end = elem.getEndOffset();
/*      */       
/* 1731 */       textArea.beginAtomicEdit();
/*      */       
/*      */       try {
/* 1734 */         String text = doc.getText(start, end - start);
/* 1735 */         doc.remove(start, end - start);
/*      */         
/* 1737 */         int insertLine = Math.min(line + 1, textArea.getLineCount());
/* 1738 */         boolean newlineInserted = false;
/* 1739 */         if (insertLine == textArea.getLineCount()) {
/* 1740 */           textArea.append("\n");
/* 1741 */           newlineInserted = true;
/*      */         } 
/*      */         
/* 1744 */         int insertOffs = textArea.getLineStartOffset(insertLine);
/* 1745 */         doc.insertString(insertOffs, text, null);
/* 1746 */         textArea.setSelectionStart(insertOffs);
/* 1747 */         textArea.setSelectionEnd(insertOffs + text.length() - 1);
/*      */         
/* 1749 */         if (newlineInserted) {
/* 1750 */           doc.remove(doc.getLength() - 1, 1);
/*      */         }
/*      */       } finally {
/*      */         
/* 1754 */         textArea.endAtomicEdit();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void moveLineUp(RTextArea textArea, int line, int moveCount) throws BadLocationException {
/* 1762 */       Document doc = textArea.getDocument();
/* 1763 */       Element root = doc.getDefaultRootElement();
/* 1764 */       Element elem = root.getElement(line);
/* 1765 */       int start = elem.getStartOffset();
/*      */       
/* 1767 */       int endLine = line + moveCount - 1;
/* 1768 */       elem = root.getElement(endLine);
/* 1769 */       int end = elem.getEndOffset();
/* 1770 */       int lineCount = textArea.getLineCount();
/* 1771 */       boolean movingLastLine = false;
/* 1772 */       if (endLine == lineCount - 1) {
/* 1773 */         movingLastLine = true;
/* 1774 */         end--;
/*      */       } 
/*      */       
/* 1777 */       int insertLine = Math.max(line - 1, 0);
/*      */       
/* 1779 */       textArea.beginAtomicEdit();
/*      */       
/*      */       try {
/* 1782 */         String text = doc.getText(start, end - start);
/* 1783 */         if (movingLastLine) {
/* 1784 */           text = text + '\n';
/*      */         }
/* 1786 */         doc.remove(start, end - start);
/*      */         
/* 1788 */         int insertOffs = textArea.getLineStartOffset(insertLine);
/* 1789 */         doc.insertString(insertOffs, text, null);
/* 1790 */         textArea.setSelectionStart(insertOffs);
/* 1791 */         int selEnd = insertOffs + text.length() - 1;
/* 1792 */         textArea.setSelectionEnd(selEnd);
/* 1793 */         if (movingLastLine) {
/* 1794 */           doc.remove(doc.getLength() - 1, 1);
/*      */         }
/*      */       } finally {
/*      */         
/* 1798 */         textArea.endAtomicEdit();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class LowerSelectionCaseAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public LowerSelectionCaseAction() {
/* 1812 */       super("RTA.LowerCaseAction");
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1817 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/* 1818 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */         return;
/*      */       } 
/* 1821 */       String selection = textArea.getSelectedText();
/* 1822 */       if (selection != null) {
/* 1823 */         textArea.replaceSelection(selection.toLowerCase());
/*      */       }
/* 1825 */       textArea.requestFocusInWindow();
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1830 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class NextBookmarkAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private boolean forward;
/*      */ 
/*      */ 
/*      */     
/*      */     public NextBookmarkAction(String name, boolean forward) {
/* 1844 */       super(name);
/* 1845 */       this.forward = forward;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1851 */       Gutter gutter = RSyntaxUtilities.getGutter(textArea);
/* 1852 */       if (gutter != null) {
/*      */         
/*      */         try {
/*      */           
/* 1856 */           GutterIconInfo[] bookmarks = gutter.getBookmarks();
/* 1857 */           if (bookmarks.length == 0) {
/* 1858 */             UIManager.getLookAndFeel()
/* 1859 */               .provideErrorFeedback(textArea);
/*      */             
/*      */             return;
/*      */           } 
/* 1863 */           GutterIconInfo moveTo = null;
/* 1864 */           int curLine = textArea.getCaretLineNumber();
/*      */           
/* 1866 */           if (this.forward) {
/* 1867 */             for (GutterIconInfo bookmark : bookmarks) {
/* 1868 */               int i = bookmark.getMarkedOffset();
/* 1869 */               int j = textArea.getLineOfOffset(i);
/* 1870 */               if (j > curLine) {
/* 1871 */                 moveTo = bookmark;
/*      */                 break;
/*      */               } 
/*      */             } 
/* 1875 */             if (moveTo == null) {
/* 1876 */               moveTo = bookmarks[0];
/*      */             }
/*      */           } else {
/*      */             
/* 1880 */             for (int i = bookmarks.length - 1; i >= 0; i--) {
/* 1881 */               GutterIconInfo bookmark = bookmarks[i];
/* 1882 */               int j = bookmark.getMarkedOffset();
/* 1883 */               int k = textArea.getLineOfOffset(j);
/* 1884 */               if (k < curLine) {
/* 1885 */                 moveTo = bookmark;
/*      */                 break;
/*      */               } 
/*      */             } 
/* 1889 */             if (moveTo == null) {
/* 1890 */               moveTo = bookmarks[bookmarks.length - 1];
/*      */             }
/*      */           } 
/*      */           
/* 1894 */           int offs = moveTo.getMarkedOffset();
/* 1895 */           if (textArea instanceof RSyntaxTextArea) {
/* 1896 */             RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/* 1897 */             if (rsta.isCodeFoldingEnabled()) {
/* 1898 */               rsta.getFoldManager()
/* 1899 */                 .ensureOffsetNotInClosedFold(offs);
/*      */             }
/*      */           } 
/* 1902 */           int line = textArea.getLineOfOffset(offs);
/* 1903 */           offs = textArea.getLineStartOffset(line);
/* 1904 */           textArea.setCaretPosition(offs);
/*      */         }
/* 1906 */         catch (BadLocationException ble) {
/* 1907 */           UIManager.getLookAndFeel()
/* 1908 */             .provideErrorFeedback(textArea);
/* 1909 */           ble.printStackTrace();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1917 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class NextOccurrenceAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public NextOccurrenceAction(String name) {
/* 1929 */       super(name);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1934 */       String selectedText = textArea.getSelectedText();
/* 1935 */       if (selectedText == null || selectedText.length() == 0) {
/* 1936 */         selectedText = RTextArea.getSelectedOccurrenceText();
/* 1937 */         if (selectedText == null || selectedText.length() == 0) {
/* 1938 */           UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */           return;
/*      */         } 
/*      */       } 
/* 1942 */       SearchContext context = new SearchContext(selectedText);
/* 1943 */       if (!textArea.getMarkAllOnOccurrenceSearches()) {
/* 1944 */         context.setMarkAll(false);
/*      */       }
/* 1946 */       if (!SearchEngine.find(textArea, context).wasFound()) {
/* 1947 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */       }
/* 1949 */       RTextArea.setSelectedOccurrenceText(selectedText);
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1954 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class NextVisualPositionAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */     
/*      */     private int direction;
/*      */ 
/*      */     
/*      */     public NextVisualPositionAction(String nm, boolean select, int dir) {
/* 1970 */       super(nm);
/* 1971 */       this.select = select;
/* 1972 */       this.direction = dir;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1978 */       Caret caret = textArea.getCaret();
/* 1979 */       int dot = caret.getDot();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1987 */       if (!this.select) {
/* 1988 */         int mark; switch (this.direction) {
/*      */           case 3:
/* 1990 */             mark = caret.getMark();
/* 1991 */             if (dot != mark) {
/* 1992 */               caret.setDot(Math.max(dot, mark));
/*      */               return;
/*      */             } 
/*      */             break;
/*      */           case 7:
/* 1997 */             mark = caret.getMark();
/* 1998 */             if (dot != mark) {
/* 1999 */               caret.setDot(Math.min(dot, mark));
/*      */               return;
/*      */             } 
/*      */             break;
/*      */         } 
/*      */ 
/*      */       
/*      */       } 
/* 2007 */       Position.Bias[] bias = new Position.Bias[1];
/* 2008 */       Point magicPosition = caret.getMagicCaretPosition();
/*      */ 
/*      */       
/*      */       try {
/* 2012 */         if (magicPosition == null && (this.direction == 1 || this.direction == 5)) {
/*      */ 
/*      */           
/* 2015 */           Rectangle r = textArea.modelToView(dot);
/* 2016 */           magicPosition = new Point(r.x, r.y);
/*      */         } 
/*      */         
/* 2019 */         NavigationFilter filter = textArea.getNavigationFilter();
/*      */         
/* 2021 */         if (filter != null) {
/* 2022 */           dot = filter.getNextVisualPositionFrom(textArea, dot, Position.Bias.Forward, this.direction, bias);
/*      */         }
/*      */         else {
/*      */           
/* 2026 */           dot = textArea.getUI().getNextVisualPositionFrom(textArea, dot, Position.Bias.Forward, this.direction, bias);
/*      */         } 
/*      */ 
/*      */         
/* 2030 */         if (this.select) {
/* 2031 */           caret.moveDot(dot);
/*      */         } else {
/*      */           
/* 2034 */           caret.setDot(dot);
/*      */         } 
/*      */         
/* 2037 */         if (magicPosition != null && (this.direction == 1 || this.direction == 5))
/*      */         {
/*      */           
/* 2040 */           caret.setMagicCaretPosition(magicPosition);
/*      */         }
/*      */       }
/* 2043 */       catch (BadLocationException ble) {
/* 2044 */         ble.printStackTrace();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2051 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class NextWordAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */ 
/*      */     
/*      */     public NextWordAction(String name, boolean select) {
/* 2065 */       super(name);
/* 2066 */       this.select = select;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2072 */       int offs = textArea.getCaretPosition();
/* 2073 */       int oldOffs = offs;
/* 2074 */       Element curPara = Utilities.getParagraphElement(textArea, offs);
/*      */       
/*      */       try {
/* 2077 */         offs = getNextWord(textArea, offs);
/* 2078 */         if (offs >= curPara.getEndOffset() && oldOffs != curPara
/* 2079 */           .getEndOffset() - 1)
/*      */         {
/*      */           
/* 2082 */           offs = curPara.getEndOffset() - 1;
/*      */         }
/* 2084 */       } catch (BadLocationException ble) {
/* 2085 */         int end = textArea.getDocument().getLength();
/* 2086 */         if (offs != end) {
/* 2087 */           if (oldOffs != curPara.getEndOffset() - 1) {
/* 2088 */             offs = curPara.getEndOffset() - 1;
/*      */           } else {
/*      */             
/* 2091 */             offs = end;
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/* 2096 */       if (this.select) {
/* 2097 */         textArea.moveCaretPosition(offs);
/*      */       } else {
/*      */         
/* 2100 */         textArea.setCaretPosition(offs);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2107 */       return getName();
/*      */     }
/*      */ 
/*      */     
/*      */     protected int getNextWord(RTextArea textArea, int offs) throws BadLocationException {
/* 2112 */       return Utilities.getNextWord(textArea, offs);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class PageAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private boolean select;
/*      */     
/*      */     private boolean left;
/*      */ 
/*      */     
/*      */     PageAction(String name, boolean left, boolean select) {
/* 2127 */       super(name);
/* 2128 */       this.select = select;
/* 2129 */       this.left = left;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2136 */       Rectangle visible = new Rectangle();
/* 2137 */       textArea.computeVisibleRect(visible);
/* 2138 */       if (this.left) {
/* 2139 */         visible.x = Math.max(0, visible.x - visible.width);
/*      */       } else {
/*      */         
/* 2142 */         visible.x += visible.width;
/*      */       } 
/*      */       
/* 2145 */       int selectedIndex = textArea.getCaretPosition();
/* 2146 */       if (selectedIndex != -1) {
/* 2147 */         if (this.left) {
/* 2148 */           selectedIndex = textArea.viewToModel(new Point(visible.x, visible.y));
/*      */         }
/*      */         else {
/*      */           
/* 2152 */           selectedIndex = textArea.viewToModel(new Point(visible.x + visible.width - 1, visible.y + visible.height - 1));
/*      */         } 
/*      */ 
/*      */         
/* 2156 */         Document doc = textArea.getDocument();
/* 2157 */         if (selectedIndex != 0 && selectedIndex > doc
/* 2158 */           .getLength() - 1) {
/* 2159 */           selectedIndex = doc.getLength() - 1;
/*      */         }
/* 2161 */         else if (selectedIndex < 0) {
/* 2162 */           selectedIndex = 0;
/*      */         } 
/* 2164 */         if (this.select) {
/* 2165 */           textArea.moveCaretPosition(selectedIndex);
/*      */         } else {
/*      */           
/* 2168 */           textArea.setCaretPosition(selectedIndex);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2176 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class PasteAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public PasteAction() {
/* 2188 */       super("paste-from-clipboard");
/*      */     }
/*      */ 
/*      */     
/*      */     public PasteAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/* 2193 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2198 */       textArea.paste();
/* 2199 */       textArea.requestFocusInWindow();
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2204 */       return "paste-from-clipboard";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class PlaybackLastMacroAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public PlaybackLastMacroAction() {
/* 2216 */       super("RTA.PlaybackLastMacroAction");
/*      */     }
/*      */ 
/*      */     
/*      */     public PlaybackLastMacroAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/* 2221 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2226 */       textArea.playbackLastMacro();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isRecordable() {
/* 2231 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2236 */       return "RTA.PlaybackLastMacroAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class PreviousOccurrenceAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public PreviousOccurrenceAction(String name) {
/* 2248 */       super(name);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2253 */       String selectedText = textArea.getSelectedText();
/* 2254 */       if (selectedText == null || selectedText.length() == 0) {
/* 2255 */         selectedText = RTextArea.getSelectedOccurrenceText();
/* 2256 */         if (selectedText == null || selectedText.length() == 0) {
/* 2257 */           UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */           return;
/*      */         } 
/*      */       } 
/* 2261 */       SearchContext context = new SearchContext(selectedText);
/* 2262 */       if (!textArea.getMarkAllOnOccurrenceSearches()) {
/* 2263 */         context.setMarkAll(false);
/*      */       }
/* 2265 */       context.setSearchForward(false);
/* 2266 */       if (!SearchEngine.find(textArea, context).wasFound()) {
/* 2267 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */       }
/* 2269 */       RTextArea.setSelectedOccurrenceText(selectedText);
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2274 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class PreviousWordAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */ 
/*      */     
/*      */     public PreviousWordAction(String name, boolean select) {
/* 2288 */       super(name);
/* 2289 */       this.select = select;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2295 */       int offs = textArea.getCaretPosition();
/* 2296 */       boolean failed = false;
/*      */       
/*      */       try {
/* 2299 */         Element curPara = Utilities.getParagraphElement(textArea, offs);
/* 2300 */         offs = getPreviousWord(textArea, offs);
/* 2301 */         if (offs < curPara.getStartOffset())
/*      */         {
/* 2303 */           offs = Utilities.getParagraphElement(textArea, offs).getEndOffset() - 1;
/*      */         }
/*      */       }
/* 2306 */       catch (BadLocationException bl) {
/* 2307 */         if (offs != 0) {
/* 2308 */           offs = 0;
/*      */         } else {
/*      */           
/* 2311 */           failed = true;
/*      */         } 
/*      */       } 
/*      */       
/* 2315 */       if (!failed) {
/* 2316 */         if (this.select) {
/* 2317 */           textArea.moveCaretPosition(offs);
/*      */         } else {
/*      */           
/* 2320 */           textArea.setCaretPosition(offs);
/*      */         } 
/*      */       } else {
/*      */         
/* 2324 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2331 */       return getName();
/*      */     }
/*      */ 
/*      */     
/*      */     protected int getPreviousWord(RTextArea textArea, int offs) throws BadLocationException {
/* 2336 */       return Utilities.getPreviousWord(textArea, offs);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class RedoAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public RedoAction() {
/* 2348 */       super("RTA.RedoAction");
/*      */     }
/*      */ 
/*      */     
/*      */     public RedoAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/* 2353 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2358 */       if (textArea.isEnabled() && textArea.isEditable()) {
/* 2359 */         textArea.redoLastAction();
/* 2360 */         textArea.requestFocusInWindow();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2366 */       return "RTA.RedoAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ScrollAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private int delta;
/*      */ 
/*      */ 
/*      */     
/*      */     public ScrollAction(String name, int delta) {
/* 2381 */       super(name);
/* 2382 */       this.delta = delta;
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2387 */       Container parent = textArea.getParent();
/* 2388 */       if (parent instanceof JViewport) {
/* 2389 */         JViewport viewport = (JViewport)parent;
/* 2390 */         Point p = viewport.getViewPosition();
/* 2391 */         p.y += this.delta * textArea.getLineHeight();
/* 2392 */         if (p.y < 0) {
/* 2393 */           p.y = 0;
/*      */         } else {
/*      */           
/* 2396 */           Rectangle viewRect = viewport.getViewRect();
/* 2397 */           int visibleEnd = p.y + viewRect.height;
/* 2398 */           if (visibleEnd >= textArea.getHeight()) {
/* 2399 */             p.y = textArea.getHeight() - viewRect.height;
/*      */           }
/*      */         } 
/* 2402 */         viewport.setViewPosition(p);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2408 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class SelectAllAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public SelectAllAction() {
/* 2420 */       super("select-all");
/*      */     }
/*      */ 
/*      */     
/*      */     public SelectAllAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/* 2425 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2430 */       Document doc = textArea.getDocument();
/* 2431 */       textArea.setCaretPosition(0);
/* 2432 */       textArea.moveCaretPosition(doc.getLength());
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2437 */       return "select-all";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class SelectLineAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private Action start;
/*      */     
/*      */     private Action end;
/*      */ 
/*      */     
/*      */     public SelectLineAction() {
/* 2452 */       super("select-line");
/* 2453 */       this.start = new RTextAreaEditorKit.BeginLineAction("pigdog", false);
/* 2454 */       this.end = new RTextAreaEditorKit.EndLineAction("pigdog", true);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2459 */       this.start.actionPerformed(e);
/* 2460 */       this.end.actionPerformed(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2465 */       return "select-line";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class SelectWordAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     protected Action start;
/*      */     
/*      */     protected Action end;
/*      */ 
/*      */     
/*      */     public SelectWordAction() {
/* 2480 */       super("select-word");
/* 2481 */       createActions();
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2486 */       this.start.actionPerformed(e);
/* 2487 */       this.end.actionPerformed(e);
/*      */     }
/*      */     
/*      */     protected void createActions() {
/* 2491 */       this.start = new RTextAreaEditorKit.BeginWordAction("pigdog", false);
/* 2492 */       this.end = new RTextAreaEditorKit.EndWordAction("pigdog", true);
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2497 */       return "select-word";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class SetReadOnlyAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public SetReadOnlyAction() {
/* 2510 */       super("set-read-only");
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2515 */       textArea.setEditable(false);
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2520 */       return "set-read-only";
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isRecordable() {
/* 2525 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class SetWritableAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public SetWritableAction() {
/* 2537 */       super("set-writable");
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2542 */       textArea.setEditable(true);
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2547 */       return "set-writable";
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isRecordable() {
/* 2552 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class TimeDateAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public TimeDateAction() {
/* 2565 */       super("RTA.TimeDateAction");
/*      */     }
/*      */ 
/*      */     
/*      */     public TimeDateAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/* 2570 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2575 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/* 2576 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */         return;
/*      */       } 
/* 2579 */       Date today = new Date();
/* 2580 */       DateFormat timeDateStamp = DateFormat.getDateTimeInstance();
/* 2581 */       String dateString = timeDateStamp.format(today);
/* 2582 */       textArea.replaceSelection(dateString);
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2587 */       return "RTA.TimeDateAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ToggleBookmarkAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public ToggleBookmarkAction() {
/* 2599 */       super("RTA.ToggleBookmarkAction");
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2604 */       Gutter gutter = RSyntaxUtilities.getGutter(textArea);
/* 2605 */       if (gutter != null) {
/* 2606 */         int line = textArea.getCaretLineNumber();
/*      */         try {
/* 2608 */           gutter.toggleBookmark(line);
/* 2609 */         } catch (BadLocationException ble) {
/* 2610 */           UIManager.getLookAndFeel()
/* 2611 */             .provideErrorFeedback(textArea);
/* 2612 */           ble.printStackTrace();
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2619 */       return "RTA.ToggleBookmarkAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ToggleTextModeAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public ToggleTextModeAction() {
/* 2631 */       super("RTA.ToggleTextModeAction");
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2636 */       int textMode = textArea.getTextMode();
/* 2637 */       if (textMode == 0) {
/* 2638 */         textArea.setTextMode(1);
/*      */       } else {
/*      */         
/* 2641 */         textArea.setTextMode(0);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2647 */       return "RTA.ToggleTextModeAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class UndoAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public UndoAction() {
/* 2659 */       super("RTA.UndoAction");
/*      */     }
/*      */ 
/*      */     
/*      */     public UndoAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/* 2664 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2669 */       if (textArea.isEnabled() && textArea.isEditable()) {
/* 2670 */         textArea.undoLastAction();
/* 2671 */         textArea.requestFocusInWindow();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2677 */       return "RTA.UndoAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class UnselectAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public UnselectAction() {
/* 2689 */       super("RTA.UnselectAction");
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2694 */       textArea.setCaretPosition(textArea.getCaretPosition());
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2699 */       return "RTA.UnselectAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class UpperSelectionCaseAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public UpperSelectionCaseAction() {
/* 2711 */       super("RTA.UpperCaseAction");
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2716 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/* 2717 */         UIManager.getLookAndFeel().provideErrorFeedback(textArea);
/*      */         return;
/*      */       } 
/* 2720 */       String selection = textArea.getSelectedText();
/* 2721 */       if (selection != null) {
/* 2722 */         textArea.replaceSelection(selection.toUpperCase());
/*      */       }
/* 2724 */       textArea.requestFocusInWindow();
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2729 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class VerticalPageAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private boolean select;
/*      */ 
/*      */     
/*      */     private int direction;
/*      */ 
/*      */     
/*      */     public VerticalPageAction(String name, int direction, boolean select) {
/* 2745 */       super(name);
/* 2746 */       this.select = select;
/* 2747 */       this.direction = direction;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2753 */       Rectangle visible = textArea.getVisibleRect();
/* 2754 */       Rectangle newVis = new Rectangle(visible);
/* 2755 */       int selectedIndex = textArea.getCaretPosition();
/* 2756 */       int scrollAmount = textArea.getScrollableBlockIncrement(visible, 1, this.direction);
/*      */       
/* 2758 */       int initialY = visible.y;
/* 2759 */       Caret caret = textArea.getCaret();
/* 2760 */       Point magicPosition = caret.getMagicCaretPosition();
/*      */ 
/*      */       
/* 2763 */       if (selectedIndex != -1) {
/*      */ 
/*      */         
/*      */         try {
/* 2767 */           Rectangle dotBounds = textArea.modelToView(selectedIndex);
/* 2768 */           int x = (magicPosition != null) ? magicPosition.x : dotBounds.x;
/*      */           
/* 2770 */           int h = dotBounds.height;
/*      */           
/* 2772 */           int yOffset = this.direction * ((int)Math.ceil(scrollAmount / h) - 1) * h;
/* 2773 */           newVis.y = constrainY(textArea, initialY + yOffset, yOffset, visible.height);
/*      */ 
/*      */           
/* 2776 */           if (visible.contains(dotBounds.x, dotBounds.y)) {
/*      */ 
/*      */             
/* 2779 */             newIndex = textArea.viewToModel(new Point(x, 
/* 2780 */                   constrainY(textArea, dotBounds.y + yOffset, 0, 0)));
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/* 2786 */           else if (this.direction == -1) {
/* 2787 */             newIndex = textArea.viewToModel(new Point(x, newVis.y));
/*      */           }
/*      */           else {
/*      */             
/* 2791 */             newIndex = textArea.viewToModel(new Point(x, newVis.y + visible.height));
/*      */           } 
/*      */ 
/*      */           
/* 2795 */           int newIndex = constrainOffset(textArea, newIndex);
/* 2796 */           if (newIndex != selectedIndex) {
/*      */ 
/*      */ 
/*      */             
/* 2800 */             adjustScrollIfNecessary(textArea, newVis, initialY, newIndex);
/*      */             
/* 2802 */             if (this.select) {
/* 2803 */               textArea.moveCaretPosition(newIndex);
/*      */             } else {
/*      */               
/* 2806 */               textArea.setCaretPosition(newIndex);
/*      */             }
/*      */           
/*      */           } 
/* 2810 */         } catch (BadLocationException badLocationException) {}
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 2815 */         int yOffset = this.direction * scrollAmount;
/* 2816 */         newVis.y = constrainY(textArea, initialY + yOffset, yOffset, visible.height);
/*      */       } 
/*      */       
/* 2819 */       if (magicPosition != null) {
/* 2820 */         caret.setMagicCaretPosition(magicPosition);
/*      */       }
/*      */       
/* 2823 */       textArea.scrollRectToVisible(newVis);
/*      */     }
/*      */     
/*      */     private int constrainY(JTextComponent textArea, int y, int vis, int screenHeight) {
/* 2827 */       if (y < 0) {
/* 2828 */         y = 0;
/*      */       }
/* 2830 */       else if (y + vis > textArea.getHeight()) {
/*      */         
/* 2832 */         y = Math.max(0, textArea.getHeight() - screenHeight);
/*      */       } 
/* 2834 */       return y;
/*      */     }
/*      */     
/*      */     private int constrainOffset(JTextComponent text, int offset) {
/* 2838 */       Document doc = text.getDocument();
/* 2839 */       if (offset != 0 && offset > doc.getLength()) {
/* 2840 */         offset = doc.getLength();
/*      */       }
/* 2842 */       if (offset < 0) {
/* 2843 */         offset = 0;
/*      */       }
/* 2845 */       return offset;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void adjustScrollIfNecessary(JTextComponent text, Rectangle visible, int initialY, int index) {
/*      */       try {
/* 2852 */         Rectangle dotBounds = text.modelToView(index);
/* 2853 */         if (dotBounds.y < visible.y || dotBounds.y > visible.y + visible.height || dotBounds.y + dotBounds.height > visible.y + visible.height) {
/*      */           int y;
/*      */ 
/*      */ 
/*      */           
/* 2858 */           if (dotBounds.y < visible.y) {
/* 2859 */             y = dotBounds.y;
/*      */           } else {
/*      */             
/* 2862 */             y = dotBounds.y + dotBounds.height - visible.height;
/*      */           } 
/* 2864 */           if ((this.direction == -1 && y < initialY) || (this.direction == 1 && y > initialY))
/*      */           {
/*      */             
/* 2867 */             visible.y = y;
/*      */           }
/*      */         } 
/* 2870 */       } catch (BadLocationException badLocationException) {}
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2875 */       return getName();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\RTextAreaEditorKit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */