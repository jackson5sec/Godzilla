/*     */ package org.mozilla.javascript.tools.shell;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PipedInputStream;
/*     */ import java.io.PipedOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Segment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConsoleTextArea
/*     */   extends JTextArea
/*     */   implements KeyListener, DocumentListener
/*     */ {
/*     */   static final long serialVersionUID = 8557083244830872961L;
/*     */   private ConsoleWriter console1;
/*     */   private ConsoleWriter console2;
/*     */   private PrintStream out;
/*     */   private PrintStream err;
/*     */   private PrintWriter inPipe;
/*     */   private PipedInputStream in;
/*     */   private List<String> history;
/*  87 */   private int historyIndex = -1;
/*  88 */   private int outputMark = 0;
/*     */ 
/*     */   
/*     */   public void select(int start, int end) {
/*  92 */     requestFocus();
/*  93 */     super.select(start, end);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConsoleTextArea(String[] argv) {
/*  98 */     this.history = new ArrayList<String>();
/*  99 */     this.console1 = new ConsoleWriter(this);
/* 100 */     this.console2 = new ConsoleWriter(this);
/* 101 */     this.out = new PrintStream(this.console1, true);
/* 102 */     this.err = new PrintStream(this.console2, true);
/* 103 */     PipedOutputStream outPipe = new PipedOutputStream();
/* 104 */     this.inPipe = new PrintWriter(outPipe);
/* 105 */     this.in = new PipedInputStream();
/*     */     try {
/* 107 */       outPipe.connect(this.in);
/* 108 */     } catch (IOException exc) {
/* 109 */       exc.printStackTrace();
/*     */     } 
/* 111 */     getDocument().addDocumentListener(this);
/* 112 */     addKeyListener(this);
/* 113 */     setLineWrap(true);
/* 114 */     setFont(new Font("Monospaced", 0, 12));
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void returnPressed() {
/* 119 */     Document doc = getDocument();
/* 120 */     int len = doc.getLength();
/* 121 */     Segment segment = new Segment();
/*     */     try {
/* 123 */       doc.getText(this.outputMark, len - this.outputMark, segment);
/* 124 */     } catch (BadLocationException ignored) {
/* 125 */       ignored.printStackTrace();
/*     */     } 
/* 127 */     if (segment.count > 0) {
/* 128 */       this.history.add(segment.toString());
/*     */     }
/* 130 */     this.historyIndex = this.history.size();
/* 131 */     this.inPipe.write(segment.array, segment.offset, segment.count);
/* 132 */     append("\n");
/* 133 */     this.outputMark = doc.getLength();
/* 134 */     this.inPipe.write("\n");
/* 135 */     this.inPipe.flush();
/* 136 */     this.console1.flush();
/*     */   }
/*     */   
/*     */   public void eval(String str) {
/* 140 */     this.inPipe.write(str);
/* 141 */     this.inPipe.write("\n");
/* 142 */     this.inPipe.flush();
/* 143 */     this.console1.flush();
/*     */   }
/*     */   
/*     */   public void keyPressed(KeyEvent e) {
/* 147 */     int code = e.getKeyCode();
/* 148 */     if (code == 8 || code == 37) {
/* 149 */       if (this.outputMark == getCaretPosition()) {
/* 150 */         e.consume();
/*     */       }
/* 152 */     } else if (code == 36) {
/* 153 */       int caretPos = getCaretPosition();
/* 154 */       if (caretPos == this.outputMark) {
/* 155 */         e.consume();
/* 156 */       } else if (caretPos > this.outputMark && 
/* 157 */         !e.isControlDown()) {
/* 158 */         if (e.isShiftDown()) {
/* 159 */           moveCaretPosition(this.outputMark);
/*     */         } else {
/* 161 */           setCaretPosition(this.outputMark);
/*     */         } 
/* 163 */         e.consume();
/*     */       }
/*     */     
/* 166 */     } else if (code == 10) {
/* 167 */       returnPressed();
/* 168 */       e.consume();
/* 169 */     } else if (code == 38) {
/* 170 */       this.historyIndex--;
/* 171 */       if (this.historyIndex >= 0) {
/* 172 */         if (this.historyIndex >= this.history.size()) {
/* 173 */           this.historyIndex = this.history.size() - 1;
/*     */         }
/* 175 */         if (this.historyIndex >= 0) {
/* 176 */           String str = this.history.get(this.historyIndex);
/* 177 */           int len = getDocument().getLength();
/* 178 */           replaceRange(str, this.outputMark, len);
/* 179 */           int caretPos = this.outputMark + str.length();
/* 180 */           select(caretPos, caretPos);
/*     */         } else {
/* 182 */           this.historyIndex++;
/*     */         } 
/*     */       } else {
/* 185 */         this.historyIndex++;
/*     */       } 
/* 187 */       e.consume();
/* 188 */     } else if (code == 40) {
/* 189 */       int caretPos = this.outputMark;
/* 190 */       if (this.history.size() > 0) {
/* 191 */         this.historyIndex++;
/* 192 */         if (this.historyIndex < 0) this.historyIndex = 0; 
/* 193 */         int len = getDocument().getLength();
/* 194 */         if (this.historyIndex < this.history.size()) {
/* 195 */           String str = this.history.get(this.historyIndex);
/* 196 */           replaceRange(str, this.outputMark, len);
/* 197 */           caretPos = this.outputMark + str.length();
/*     */         } else {
/* 199 */           this.historyIndex = this.history.size();
/* 200 */           replaceRange("", this.outputMark, len);
/*     */         } 
/*     */       } 
/* 203 */       select(caretPos, caretPos);
/* 204 */       e.consume();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void keyTyped(KeyEvent e) {
/* 209 */     int keyChar = e.getKeyChar();
/* 210 */     if (keyChar == 8) {
/* 211 */       if (this.outputMark == getCaretPosition()) {
/* 212 */         e.consume();
/*     */       }
/* 214 */     } else if (getCaretPosition() < this.outputMark) {
/* 215 */       setCaretPosition(this.outputMark);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void keyReleased(KeyEvent e) {}
/*     */   
/*     */   public synchronized void write(String str) {
/* 223 */     insert(str, this.outputMark);
/* 224 */     int len = str.length();
/* 225 */     this.outputMark += len;
/* 226 */     select(this.outputMark, this.outputMark);
/*     */   }
/*     */   
/*     */   public synchronized void insertUpdate(DocumentEvent e) {
/* 230 */     int len = e.getLength();
/* 231 */     int off = e.getOffset();
/* 232 */     if (this.outputMark > off) {
/* 233 */       this.outputMark += len;
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void removeUpdate(DocumentEvent e) {
/* 238 */     int len = e.getLength();
/* 239 */     int off = e.getOffset();
/* 240 */     if (this.outputMark > off) {
/* 241 */       if (this.outputMark >= off + len) {
/* 242 */         this.outputMark -= len;
/*     */       } else {
/* 244 */         this.outputMark = off;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void postUpdateUI() {
/* 251 */     requestFocus();
/* 252 */     setCaret(getCaret());
/* 253 */     select(this.outputMark, this.outputMark);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void changedUpdate(DocumentEvent e) {}
/*     */ 
/*     */   
/*     */   public InputStream getIn() {
/* 261 */     return this.in;
/*     */   }
/*     */   
/*     */   public PrintStream getOut() {
/* 265 */     return this.out;
/*     */   }
/*     */   
/*     */   public PrintStream getErr() {
/* 269 */     return this.err;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\shell\ConsoleTextArea.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */