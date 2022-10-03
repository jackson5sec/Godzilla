/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.text.Document;
/*     */ import org.fife.io.UnicodeReader;
/*     */ import org.fife.io.UnicodeWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextEditorPane
/*     */   extends RSyntaxTextArea
/*     */   implements DocumentListener
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final String FULL_PATH_PROPERTY = "TextEditorPane.fileFullPath";
/*     */   public static final String DIRTY_PROPERTY = "TextEditorPane.dirty";
/*     */   public static final String READ_ONLY_PROPERTY = "TextEditorPane.readOnly";
/*     */   public static final String ENCODING_PROPERTY = "TextEditorPane.encoding";
/*     */   private FileLocation loc;
/*     */   private String charSet;
/*     */   private boolean readOnly;
/*     */   private boolean dirty;
/*     */   private long lastSaveOrLoadTime;
/*     */   public static final long LAST_MODIFIED_UNKNOWN = 0L;
/*     */   private static final String DEFAULT_FILE_NAME = "Untitled.txt";
/*     */   
/*     */   public TextEditorPane() {
/* 133 */     this(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextEditorPane(int textMode) {
/* 144 */     this(textMode, false);
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
/*     */   public TextEditorPane(int textMode, boolean wordWrapEnabled) {
/* 157 */     super(textMode);
/* 158 */     setLineWrap(wordWrapEnabled);
/*     */     try {
/* 160 */       init((FileLocation)null, (String)null);
/* 161 */     } catch (IOException ioe) {
/* 162 */       ioe.printStackTrace();
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
/*     */ 
/*     */   
/*     */   public TextEditorPane(int textMode, boolean wordWrapEnabled, FileLocation loc) throws IOException {
/* 182 */     this(textMode, wordWrapEnabled, loc, (String)null);
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
/*     */   public TextEditorPane(int textMode, boolean wordWrapEnabled, FileLocation loc, String defaultEnc) throws IOException {
/* 205 */     super(textMode);
/* 206 */     setLineWrap(wordWrapEnabled);
/* 207 */     init(loc, defaultEnc);
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
/*     */   public void changedUpdate(DocumentEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getDefaultEncoding() {
/* 231 */     return Charset.defaultCharset().name();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 242 */     return this.charSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileFullPath() {
/* 252 */     return (this.loc == null) ? null : this.loc.getFileFullPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 262 */     return (this.loc == null) ? null : this.loc.getFileName();
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
/*     */   public long getLastSaveOrLoadTime() {
/* 281 */     return this.lastSaveOrLoadTime;
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
/*     */   public Object getLineSeparator() {
/* 302 */     return getDocument().getProperty("__EndOfLine__");
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
/*     */   private void init(FileLocation loc, String defaultEnc) throws IOException {
/* 320 */     if (loc == null) {
/*     */ 
/*     */ 
/*     */       
/* 324 */       this.loc = FileLocation.create("Untitled.txt");
/* 325 */       this.charSet = (defaultEnc == null) ? getDefaultEncoding() : defaultEnc;
/*     */ 
/*     */ 
/*     */       
/* 329 */       setLineSeparator(System.getProperty("line.separator"));
/*     */     } else {
/*     */       
/* 332 */       load(loc, defaultEnc);
/*     */     } 
/*     */     
/* 335 */     if (this.loc.isLocalAndExists()) {
/* 336 */       File file = new File(this.loc.getFileFullPath());
/* 337 */       this.lastSaveOrLoadTime = file.lastModified();
/* 338 */       setReadOnly(!file.canWrite());
/*     */     } else {
/*     */       
/* 341 */       this.lastSaveOrLoadTime = 0L;
/* 342 */       setReadOnly(false);
/*     */     } 
/*     */     
/* 345 */     setDirty(false);
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
/*     */   public void insertUpdate(DocumentEvent e) {
/* 357 */     if (!this.dirty) {
/* 358 */       setDirty(true);
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
/*     */   public boolean isDirty() {
/* 370 */     return this.dirty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLocal() {
/* 380 */     return this.loc.isLocal();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLocalAndExists() {
/* 390 */     return this.loc.isLocalAndExists();
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
/*     */   public boolean isModifiedOutsideEditor() {
/* 408 */     return (this.loc.getActualLastModified() > getLastSaveOrLoadTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadOnly() {
/* 419 */     return this.readOnly;
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
/*     */   public void load(FileLocation loc) throws IOException {
/* 439 */     load(loc, (String)null);
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
/*     */   public void load(FileLocation loc, Charset defaultEnc) throws IOException {
/* 460 */     load(loc, (defaultEnc == null) ? null : defaultEnc.name());
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
/*     */   public void load(FileLocation loc, String defaultEnc) throws IOException {
/* 483 */     if (loc.isLocal() && !loc.isLocalAndExists()) {
/* 484 */       this.charSet = (defaultEnc != null) ? defaultEnc : getDefaultEncoding();
/* 485 */       this.loc = loc;
/* 486 */       setText(null);
/* 487 */       discardAllEdits();
/* 488 */       setDirty(false);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 495 */     UnicodeReader ur = new UnicodeReader(loc.getInputStream(), defaultEnc);
/*     */ 
/*     */     
/* 498 */     Document doc = getDocument();
/* 499 */     doc.removeDocumentListener(this);
/* 500 */     try (BufferedReader r = new BufferedReader((Reader)ur)) {
/* 501 */       read(r, null);
/*     */     } finally {
/* 503 */       doc.addDocumentListener(this);
/*     */     } 
/*     */ 
/*     */     
/* 507 */     this.charSet = ur.getEncoding();
/* 508 */     String old = getFileFullPath();
/* 509 */     this.loc = loc;
/* 510 */     setDirty(false);
/* 511 */     setCaretPosition(0);
/* 512 */     discardAllEdits();
/* 513 */     firePropertyChange("TextEditorPane.fileFullPath", old, getFileFullPath());
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
/*     */   public void reload() throws IOException {
/* 536 */     String oldEncoding = getEncoding();
/* 537 */     UnicodeReader ur = new UnicodeReader(this.loc.getInputStream(), oldEncoding);
/* 538 */     String encoding = ur.getEncoding();
/* 539 */     try (BufferedReader r = new BufferedReader((Reader)ur)) {
/* 540 */       read(r, null);
/*     */     } 
/* 542 */     setEncoding(encoding);
/* 543 */     setDirty(false);
/* 544 */     syncLastSaveOrLoadTimeToActualFile();
/* 545 */     discardAllEdits();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeUpdate(DocumentEvent e) {
/* 556 */     if (!this.dirty) {
/* 557 */       setDirty(true);
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
/*     */   public void save() throws IOException {
/* 573 */     saveImpl(this.loc);
/* 574 */     setDirty(false);
/* 575 */     syncLastSaveOrLoadTimeToActualFile();
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
/*     */   public void saveAs(FileLocation loc) throws IOException {
/* 589 */     saveImpl(loc);
/*     */     
/* 591 */     String old = getFileFullPath();
/* 592 */     this.loc = loc;
/* 593 */     setDirty(false);
/* 594 */     this.lastSaveOrLoadTime = loc.getActualLastModified();
/* 595 */     firePropertyChange("TextEditorPane.fileFullPath", old, getFileFullPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void saveImpl(FileLocation loc) throws IOException {
/* 606 */     OutputStream out = loc.getOutputStream();
/* 607 */     try (BufferedWriter w = new BufferedWriter((Writer)new UnicodeWriter(out, 
/* 608 */             getEncoding()))) {
/* 609 */       write(w);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDirty(boolean dirty) {
/* 631 */     if (this.dirty != dirty) {
/* 632 */       this.dirty = dirty;
/* 633 */       firePropertyChange("TextEditorPane.dirty", !dirty, dirty);
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
/*     */   public void setDocument(Document doc) {
/* 645 */     Document old = getDocument();
/* 646 */     if (old != null) {
/* 647 */       old.removeDocumentListener(this);
/*     */     }
/* 649 */     super.setDocument(doc);
/* 650 */     doc.addDocumentListener(this);
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
/*     */   public void setEncoding(String encoding) {
/* 666 */     if (encoding == null) {
/* 667 */       throw new NullPointerException("encoding cannot be null");
/*     */     }
/* 669 */     if (!Charset.isSupported(encoding)) {
/* 670 */       throw new UnsupportedCharsetException(encoding);
/*     */     }
/* 672 */     if (this.charSet == null || !this.charSet.equals(encoding)) {
/* 673 */       String oldEncoding = this.charSet;
/* 674 */       this.charSet = encoding;
/* 675 */       firePropertyChange("TextEditorPane.encoding", oldEncoding, this.charSet);
/* 676 */       setDirty(true);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineSeparator(String separator) {
/* 697 */     setLineSeparator(separator, true);
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
/*     */   public void setLineSeparator(String separator, boolean setDirty) {
/* 719 */     if (separator == null) {
/* 720 */       throw new NullPointerException("terminator cannot be null");
/*     */     }
/* 722 */     if (!"\r\n".equals(separator) && !"\n".equals(separator) && 
/* 723 */       !"\r".equals(separator)) {
/* 724 */       throw new IllegalArgumentException("Invalid line terminator");
/*     */     }
/* 726 */     Document doc = getDocument();
/* 727 */     Object old = doc.getProperty("__EndOfLine__");
/*     */     
/* 729 */     if (!separator.equals(old)) {
/* 730 */       doc.putProperty("__EndOfLine__", separator);
/*     */       
/* 732 */       if (setDirty) {
/* 733 */         setDirty(true);
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
/*     */   public void setReadOnly(boolean readOnly) {
/* 747 */     if (this.readOnly != readOnly) {
/* 748 */       this.readOnly = readOnly;
/* 749 */       firePropertyChange("TextEditorPane.readOnly", !readOnly, readOnly);
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
/*     */   public void syncLastSaveOrLoadTimeToActualFile() {
/* 767 */     if (this.loc.isLocalAndExists())
/* 768 */       this.lastSaveOrLoadTime = this.loc.getActualLastModified(); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\TextEditorPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */