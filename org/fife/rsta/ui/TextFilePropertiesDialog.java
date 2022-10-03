/*     */ package org.fife.rsta.ui;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.File;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.BreakIterator;
/*     */ import java.text.CharacterIterator;
/*     */ import java.text.MessageFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Segment;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.TextEditorPane;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextFilePropertiesDialog
/*     */   extends EscapableDialog
/*     */   implements ActionListener
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private JTextField filePathField;
/*     */   private JComboBox<String> terminatorCombo;
/*     */   private JComboBox<String> encodingCombo;
/*     */   private JButton okButton;
/*     */   private TextEditorPane textArea;
/*  67 */   private static final ResourceBundle MSG = ResourceBundle.getBundle("org.fife.rsta.ui.TextFilePropertiesDialog");
/*     */ 
/*     */   
/*  70 */   private static final String[] LINE_TERMINATOR_LABELS = new String[] { MSG
/*  71 */       .getString("SysDef"), MSG
/*  72 */       .getString("CR"), MSG
/*  73 */       .getString("LF"), MSG
/*  74 */       .getString("CRLF") };
/*     */ 
/*     */   
/*  77 */   private static final String[] LINE_TERMINATORS = new String[] {
/*  78 */       System.getProperty("line.separator"), "\r", "\n", "\r\n"
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextFilePropertiesDialog(Dialog parent, TextEditorPane textArea) {
/*  88 */     super(parent);
/*  89 */     init(textArea);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextFilePropertiesDialog(Frame parent, TextEditorPane textArea) {
/* 100 */     super(parent);
/* 101 */     init(textArea);
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
/*     */   public void actionPerformed(ActionEvent e) {
/* 113 */     String command = e.getActionCommand();
/*     */     
/* 115 */     if ("TerminatorComboBox".equals(command)) {
/* 116 */       this.okButton.setEnabled(true);
/*     */     
/*     */     }
/* 119 */     else if ("encodingCombo".equals(command)) {
/* 120 */       this.okButton.setEnabled(true);
/*     */     
/*     */     }
/* 123 */     else if ("OKButton".equals(command)) {
/* 124 */       String terminator = getSelectedLineTerminator();
/* 125 */       if (terminator != null) {
/* 126 */         String old = (String)this.textArea.getLineSeparator();
/* 127 */         if (!terminator.equals(old)) {
/* 128 */           this.textArea.setLineSeparator(terminator);
/*     */         }
/*     */       } 
/* 131 */       String encoding = (String)this.encodingCombo.getSelectedItem();
/* 132 */       if (encoding != null) {
/* 133 */         this.textArea.setEncoding(encoding);
/*     */       }
/* 135 */       setVisible(false);
/*     */     
/*     */     }
/* 138 */     else if ("CancelButton".equals(command)) {
/* 139 */       escapePressed();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int calculateWordCount(TextEditorPane textArea) {
/* 147 */     int wordCount = 0;
/* 148 */     RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/*     */     
/* 150 */     BreakIterator bi = BreakIterator.getWordInstance();
/* 151 */     bi.setText(new DocumentCharIterator(textArea.getDocument())); int nextBoundary;
/* 152 */     for (nextBoundary = bi.first(); nextBoundary != -1; 
/* 153 */       nextBoundary = bi.next()) {
/*     */ 
/*     */       
/*     */       try {
/* 157 */         char ch = doc.charAt(nextBoundary);
/* 158 */         if (Character.isLetterOrDigit(ch)) {
/* 159 */           wordCount++;
/*     */         }
/* 161 */       } catch (BadLocationException ble) {
/* 162 */         ble.printStackTrace();
/*     */       } 
/*     */     } 
/*     */     
/* 166 */     return wordCount;
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
/*     */   protected Container createButtonFooter(JButton ok, JButton cancel) {
/* 180 */     JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
/* 181 */     buttonPanel.add(ok);
/* 182 */     buttonPanel.add(cancel);
/*     */     
/* 184 */     JPanel panel = new JPanel(new BorderLayout());
/* 185 */     ComponentOrientation o = getComponentOrientation();
/* 186 */     int padding = 8;
/* 187 */     int left = o.isLeftToRight() ? 0 : 8;
/* 188 */     int right = o.isLeftToRight() ? 8 : 0;
/* 189 */     panel.setBorder(BorderFactory.createEmptyBorder(10, left, 0, right));
/* 190 */     panel.add(buttonPanel, "After");
/* 191 */     return panel;
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
/*     */   protected String createTitle(String fileName) {
/* 203 */     return MessageFormat.format(MSG
/* 204 */         .getString("Title"), new Object[] { this.textArea.getFileName() });
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
/*     */   private static String getFileSizeStringFor(File file) {
/* 217 */     int count = 0;
/* 218 */     double tempSize = file.length();
/* 219 */     double prevSize = tempSize;
/*     */ 
/*     */ 
/*     */     
/* 223 */     while (count < 4 && (tempSize = prevSize / 1024.0D) >= 1.0D) {
/* 224 */       prevSize = tempSize;
/* 225 */       count++;
/*     */     } 
/*     */ 
/*     */     
/* 229 */     switch (count)
/*     */     { case 0:
/* 231 */         suffix = "bytes";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 248 */         fileSizeFormat = NumberFormat.getNumberInstance();
/* 249 */         fileSizeFormat.setGroupingUsed(true);
/* 250 */         fileSizeFormat.setMinimumFractionDigits(0);
/* 251 */         fileSizeFormat.setMaximumFractionDigits(1);
/* 252 */         return fileSizeFormat.format(prevSize) + " " + suffix;case 1: suffix = "KB"; fileSizeFormat = NumberFormat.getNumberInstance(); fileSizeFormat.setGroupingUsed(true); fileSizeFormat.setMinimumFractionDigits(0); fileSizeFormat.setMaximumFractionDigits(1); return fileSizeFormat.format(prevSize) + " " + suffix;case 2: suffix = "MB"; fileSizeFormat = NumberFormat.getNumberInstance(); fileSizeFormat.setGroupingUsed(true); fileSizeFormat.setMinimumFractionDigits(0); fileSizeFormat.setMaximumFractionDigits(1); return fileSizeFormat.format(prevSize) + " " + suffix;case 3: suffix = "GB"; fileSizeFormat = NumberFormat.getNumberInstance(); fileSizeFormat.setGroupingUsed(true); fileSizeFormat.setMinimumFractionDigits(0); fileSizeFormat.setMaximumFractionDigits(1); return fileSizeFormat.format(prevSize) + " " + suffix; }  String suffix = "TB"; NumberFormat fileSizeFormat = NumberFormat.getNumberInstance(); fileSizeFormat.setGroupingUsed(true); fileSizeFormat.setMinimumFractionDigits(0); fileSizeFormat.setMaximumFractionDigits(1); return fileSizeFormat.format(prevSize) + " " + suffix;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String getSelectedLineTerminator() {
/* 258 */     return LINE_TERMINATORS[this.terminatorCombo.getSelectedIndex()];
/*     */   }
/*     */ 
/*     */   
/*     */   private void init(TextEditorPane textArea) {
/*     */     String modifiedString;
/* 264 */     this.textArea = textArea;
/* 265 */     setTitle(createTitle(textArea.getFileName()));
/*     */ 
/*     */     
/* 268 */     ComponentOrientation o = ComponentOrientation.getOrientation(getLocale());
/*     */     
/* 270 */     JPanel contentPane = new ResizableFrameContentPane(new BorderLayout());
/* 271 */     contentPane.setBorder(UIUtil.getEmpty5Border());
/*     */ 
/*     */     
/* 274 */     JPanel content2 = new JPanel();
/* 275 */     content2.setLayout(new SpringLayout());
/* 276 */     contentPane.add(content2, "North");
/*     */     
/* 278 */     this.filePathField = new JTextField(40);
/* 279 */     this.filePathField.setText(textArea.getFileFullPath());
/* 280 */     this.filePathField.setEditable(false);
/* 281 */     JLabel filePathLabel = UIUtil.newLabel(MSG, "Path", this.filePathField);
/*     */     
/* 283 */     JLabel linesLabel = new JLabel(MSG.getString("Lines"));
/*     */     
/* 285 */     JLabel linesCountLabel = new JLabel(Integer.toString(textArea.getLineCount()));
/*     */     
/* 287 */     JLabel charsLabel = new JLabel(MSG.getString("Characters"));
/*     */     
/* 289 */     JLabel charsCountLabel = new JLabel(Integer.toString(textArea.getDocument().getLength()));
/*     */     
/* 291 */     JLabel wordsLabel = new JLabel(MSG.getString("Words"));
/*     */     
/* 293 */     JLabel wordsCountLabel = new JLabel(Integer.toString(calculateWordCount(textArea)));
/*     */     
/* 295 */     this.terminatorCombo = new JComboBox<>(LINE_TERMINATOR_LABELS);
/* 296 */     if (textArea.isReadOnly()) {
/* 297 */       this.terminatorCombo.setEnabled(false);
/*     */     }
/* 299 */     UIUtil.fixComboOrientation(this.terminatorCombo);
/* 300 */     setSelectedLineTerminator((String)textArea.getLineSeparator());
/* 301 */     this.terminatorCombo.setActionCommand("TerminatorComboBox");
/* 302 */     this.terminatorCombo.addActionListener(this);
/* 303 */     JLabel terminatorLabel = UIUtil.newLabel(MSG, "LineTerminator", this.terminatorCombo);
/*     */ 
/*     */     
/* 306 */     this.encodingCombo = new JComboBox<>();
/* 307 */     if (textArea.isReadOnly()) {
/* 308 */       this.encodingCombo.setEnabled(false);
/*     */     }
/* 310 */     UIUtil.fixComboOrientation(this.encodingCombo);
/*     */ 
/*     */     
/* 313 */     Map<String, Charset> availableCharsets = Charset.availableCharsets();
/* 314 */     Set<String> charsetNames = availableCharsets.keySet();
/* 315 */     for (String charsetName : charsetNames) {
/* 316 */       this.encodingCombo.addItem(charsetName);
/*     */     }
/* 318 */     setEncoding(textArea.getEncoding());
/* 319 */     this.encodingCombo.setActionCommand("encodingCombo");
/* 320 */     this.encodingCombo.addActionListener(this);
/* 321 */     JLabel encodingLabel = UIUtil.newLabel(MSG, "Encoding", this.encodingCombo);
/*     */     
/* 323 */     JLabel sizeLabel = new JLabel(MSG.getString("FileSize"));
/* 324 */     File file = new File(textArea.getFileFullPath());
/* 325 */     String size = "";
/* 326 */     if (file.exists() && !file.isDirectory()) {
/* 327 */       size = getFileSizeStringFor(file);
/*     */     }
/* 329 */     JLabel sizeLabel2 = new JLabel(size);
/*     */     
/* 331 */     long temp = textArea.getLastSaveOrLoadTime();
/*     */     
/* 333 */     if (temp <= 0L) {
/* 334 */       modifiedString = "";
/*     */     } else {
/*     */       
/* 337 */       Date modifiedDate = new Date(temp);
/* 338 */       SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a  EEE, MMM d, yyyy");
/*     */       
/* 340 */       modifiedString = sdf.format(modifiedDate);
/*     */     } 
/* 342 */     JLabel modifiedLabel = new JLabel(MSG.getString("LastModified"));
/* 343 */     JLabel modified = new JLabel(modifiedString);
/*     */     
/* 345 */     if (o.isLeftToRight()) {
/* 346 */       content2.add(filePathLabel); content2.add(this.filePathField);
/* 347 */       content2.add(linesLabel); content2.add(linesCountLabel);
/* 348 */       content2.add(charsLabel); content2.add(charsCountLabel);
/* 349 */       content2.add(wordsLabel); content2.add(wordsCountLabel);
/* 350 */       content2.add(terminatorLabel); content2.add(this.terminatorCombo);
/* 351 */       content2.add(encodingLabel); content2.add(this.encodingCombo);
/* 352 */       content2.add(sizeLabel); content2.add(sizeLabel2);
/* 353 */       content2.add(modifiedLabel); content2.add(modified);
/*     */     } else {
/*     */       
/* 356 */       content2.add(this.filePathField); content2.add(filePathLabel);
/* 357 */       content2.add(linesCountLabel); content2.add(linesLabel);
/* 358 */       content2.add(charsCountLabel); content2.add(charsLabel);
/* 359 */       content2.add(wordsCountLabel); content2.add(wordsLabel);
/* 360 */       content2.add(this.terminatorCombo); content2.add(terminatorLabel);
/* 361 */       content2.add(this.encodingCombo); content2.add(encodingLabel);
/* 362 */       content2.add(sizeLabel2); content2.add(sizeLabel);
/* 363 */       content2.add(modified); content2.add(modifiedLabel);
/*     */     } 
/*     */     
/* 366 */     UIUtil.makeSpringCompactGrid(content2, 8, 2, 0, 0, 5, 5);
/*     */ 
/*     */     
/* 369 */     this.okButton = UIUtil.newButton(MSG, "OK");
/* 370 */     this.okButton.setActionCommand("OKButton");
/* 371 */     this.okButton.addActionListener(this);
/* 372 */     this.okButton.setEnabled(false);
/* 373 */     JButton cancelButton = UIUtil.newButton(MSG, "Cancel");
/* 374 */     cancelButton.setActionCommand("CancelButton");
/* 375 */     cancelButton.addActionListener(this);
/* 376 */     Container buttons = createButtonFooter(this.okButton, cancelButton);
/* 377 */     contentPane.add(buttons, "South");
/*     */     
/* 379 */     setContentPane(contentPane);
/* 380 */     setModal(true);
/* 381 */     applyComponentOrientation(o);
/* 382 */     pack();
/* 383 */     setLocationRelativeTo(getParent());
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
/*     */   private void setEncoding(String encoding) {
/* 396 */     Charset cs1 = Charset.forName(encoding);
/*     */     
/* 398 */     int count = this.encodingCombo.getItemCount(); int i;
/* 399 */     for (i = 0; i < count; i++) {
/* 400 */       String item = this.encodingCombo.getItemAt(i);
/* 401 */       Charset cs2 = Charset.forName(item);
/* 402 */       if (cs1.equals(cs2)) {
/* 403 */         this.encodingCombo.setSelectedIndex(i);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 409 */     cs1 = StandardCharsets.US_ASCII;
/* 410 */     for (i = 0; i < count; i++) {
/* 411 */       String item = this.encodingCombo.getItemAt(i);
/* 412 */       Charset cs2 = Charset.forName(item);
/* 413 */       if (cs1.equals(cs2)) {
/* 414 */         this.encodingCombo.setSelectedIndex(i);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setSelectedLineTerminator(String terminator) {
/* 423 */     for (int i = 0; i < LINE_TERMINATORS.length; i++) {
/* 424 */       if (LINE_TERMINATORS[i].equals(terminator)) {
/* 425 */         this.terminatorCombo.setSelectedIndex(i);
/*     */         break;
/*     */       } 
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
/*     */   public void setVisible(boolean visible) {
/* 440 */     if (visible) {
/* 441 */       SwingUtilities.invokeLater(() -> {
/*     */             this.filePathField.requestFocusInWindow();
/*     */             this.filePathField.selectAll();
/*     */           });
/*     */     }
/* 446 */     super.setVisible(visible);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class DocumentCharIterator
/*     */     implements CharacterIterator
/*     */   {
/*     */     private Document doc;
/*     */     
/*     */     private int index;
/*     */     
/*     */     private Segment s;
/*     */     
/*     */     DocumentCharIterator(Document doc) {
/* 460 */       this.doc = doc;
/* 461 */       this.index = 0;
/* 462 */       this.s = new Segment();
/*     */     }
/*     */ 
/*     */     
/*     */     public Object clone() {
/*     */       try {
/* 468 */         return super.clone();
/* 469 */       } catch (CloneNotSupportedException cnse) {
/* 470 */         throw new InternalError("Clone not supported???");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public char current() {
/* 476 */       if (this.index >= getEndIndex()) {
/* 477 */         return Character.MAX_VALUE;
/*     */       }
/*     */       try {
/* 480 */         this.doc.getText(this.index, 1, this.s);
/* 481 */         return this.s.first();
/* 482 */       } catch (BadLocationException ble) {
/* 483 */         return Character.MAX_VALUE;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public char first() {
/* 489 */       this.index = getBeginIndex();
/* 490 */       return current();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getBeginIndex() {
/* 495 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getEndIndex() {
/* 500 */       return this.doc.getLength();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getIndex() {
/* 505 */       return this.index;
/*     */     }
/*     */ 
/*     */     
/*     */     public char last() {
/* 510 */       this.index = Math.max(0, getEndIndex() - 1);
/* 511 */       return current();
/*     */     }
/*     */ 
/*     */     
/*     */     public char next() {
/* 516 */       this.index = Math.min(this.index + 1, getEndIndex());
/* 517 */       return current();
/*     */     }
/*     */ 
/*     */     
/*     */     public char previous() {
/* 522 */       this.index = Math.max(this.index - 1, getBeginIndex());
/* 523 */       return current();
/*     */     }
/*     */ 
/*     */     
/*     */     public char setIndex(int pos) {
/* 528 */       if (pos < getBeginIndex() || pos > getEndIndex()) {
/* 529 */         throw new IllegalArgumentException("Illegal index: " + this.index);
/*     */       }
/* 531 */       this.index = pos;
/* 532 */       return current();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\TextFilePropertiesDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */