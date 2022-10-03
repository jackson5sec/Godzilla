/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.awt.im.InputContext;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringBufferInputStream;
/*     */ import java.io.StringReader;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.TransferHandler;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Position;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RTATextTransferHandler
/*     */   extends TransferHandler
/*     */ {
/*     */   private JTextComponent exportComp;
/*     */   private boolean shouldRemove;
/*     */   private int p0;
/*     */   private int p1;
/*     */   private boolean withinSameComponent;
/*     */   
/*     */   protected DataFlavor getImportFlavor(DataFlavor[] flavors, JTextComponent c) {
/*  72 */     DataFlavor refFlavor = null;
/*  73 */     DataFlavor stringFlavor = null;
/*     */     
/*  75 */     for (DataFlavor flavor : flavors) {
/*     */       
/*  77 */       String mime = flavor.getMimeType();
/*  78 */       if (mime.startsWith("text/plain")) {
/*  79 */         return flavor;
/*     */       }
/*  81 */       if (refFlavor == null && mime
/*  82 */         .startsWith("application/x-java-jvm-local-objectref") && flavor
/*  83 */         .getRepresentationClass() == String.class) {
/*  84 */         refFlavor = flavor;
/*     */       }
/*  86 */       else if (stringFlavor == null && flavor
/*  87 */         .equals(DataFlavor.stringFlavor)) {
/*  88 */         stringFlavor = flavor;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  93 */     if (refFlavor != null) {
/*  94 */       return refFlavor;
/*     */     }
/*  96 */     if (stringFlavor != null) {
/*  97 */       return stringFlavor;
/*     */     }
/*     */     
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleReaderImport(Reader in, JTextComponent c) throws IOException {
/* 111 */     char[] buff = new char[1024];
/*     */     
/* 113 */     boolean lastWasCR = false;
/*     */     
/* 115 */     StringBuilder sbuff = null;
/*     */     
/*     */     int nch;
/*     */     
/* 119 */     while ((nch = in.read(buff, 0, buff.length)) != -1) {
/*     */       
/* 121 */       if (sbuff == null) {
/* 122 */         sbuff = new StringBuilder(nch);
/*     */       }
/* 124 */       int last = 0;
/*     */       
/* 126 */       for (int counter = 0; counter < nch; counter++) {
/*     */         
/* 128 */         switch (buff[counter]) {
/*     */           case '\r':
/* 130 */             if (lastWasCR) {
/* 131 */               if (counter == 0) {
/* 132 */                 sbuff.append('\n');
/*     */                 break;
/*     */               } 
/* 135 */               buff[counter - 1] = '\n';
/*     */               
/*     */               break;
/*     */             } 
/* 139 */             lastWasCR = true;
/*     */             break;
/*     */           
/*     */           case '\n':
/* 143 */             if (lastWasCR) {
/* 144 */               if (counter > last + 1) {
/* 145 */                 sbuff.append(buff, last, counter - last - 1);
/*     */               }
/*     */ 
/*     */               
/* 149 */               lastWasCR = false;
/* 150 */               last = counter;
/*     */             } 
/*     */             break;
/*     */           default:
/* 154 */             if (lastWasCR) {
/* 155 */               if (counter == 0) {
/* 156 */                 sbuff.append('\n');
/*     */               } else {
/*     */                 
/* 159 */                 buff[counter - 1] = '\n';
/*     */               } 
/* 161 */               lastWasCR = false;
/*     */             } 
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/*     */       } 
/* 169 */       if (last < nch) {
/* 170 */         if (lastWasCR) {
/* 171 */           if (last < nch - 1) {
/* 172 */             sbuff.append(buff, last, nch - last - 1);
/*     */           }
/*     */           continue;
/*     */         } 
/* 176 */         sbuff.append(buff, last, nch - last);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 182 */     if (this.withinSameComponent) {
/* 183 */       ((RTextArea)c).beginAtomicEdit();
/*     */     }
/*     */     
/* 186 */     if (lastWasCR) {
/* 187 */       sbuff.append('\n');
/*     */     }
/* 189 */     c.replaceSelection((sbuff != null) ? sbuff.toString() : "");
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
/*     */   public int getSourceActions(JComponent c) {
/* 207 */     if (((JTextComponent)c).isEditable()) {
/* 208 */       return 3;
/*     */     }
/*     */     
/* 211 */     return 1;
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
/*     */   protected Transferable createTransferable(JComponent comp) {
/* 227 */     this.exportComp = (JTextComponent)comp;
/* 228 */     this.shouldRemove = true;
/* 229 */     this.p0 = this.exportComp.getSelectionStart();
/* 230 */     this.p1 = this.exportComp.getSelectionEnd();
/* 231 */     return (this.p0 != this.p1) ? new TextTransferable(this.exportComp, this.p0, this.p1) : null;
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
/*     */   protected void exportDone(JComponent source, Transferable data, int action) {
/* 248 */     if (this.shouldRemove && action == 2) {
/* 249 */       TextTransferable t = (TextTransferable)data;
/* 250 */       t.removeText();
/* 251 */       if (this.withinSameComponent) {
/* 252 */         ((RTextArea)source).endAtomicEdit();
/* 253 */         this.withinSameComponent = false;
/*     */       } 
/*     */     } 
/* 256 */     this.exportComp = null;
/* 257 */     if (data instanceof TextTransferable) {
/* 258 */       ClipboardHistory.get().add(((TextTransferable)data).getPlainData());
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
/*     */   public boolean importData(JComponent comp, Transferable t) {
/* 277 */     JTextComponent c = (JTextComponent)comp;
/* 278 */     this.withinSameComponent = (c == this.exportComp);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 284 */     if (this.withinSameComponent && c.getCaretPosition() >= this.p0 && c.getCaretPosition() <= this.p1) {
/* 285 */       this.shouldRemove = false;
/* 286 */       return true;
/*     */     } 
/*     */     
/* 289 */     boolean imported = false;
/* 290 */     DataFlavor importFlavor = getImportFlavor(t.getTransferDataFlavors(), c);
/* 291 */     if (importFlavor != null) {
/*     */       try {
/* 293 */         InputContext ic = c.getInputContext();
/* 294 */         if (ic != null) {
/* 295 */           ic.endComposition();
/*     */         }
/* 297 */         Reader r = importFlavor.getReaderForText(t);
/* 298 */         handleReaderImport(r, c);
/* 299 */         imported = true;
/* 300 */       } catch (UnsupportedFlavorException|IOException e) {
/* 301 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 305 */     return imported;
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
/*     */   public boolean canImport(JComponent comp, DataFlavor[] flavors) {
/* 321 */     JTextComponent c = (JTextComponent)comp;
/* 322 */     if (!c.isEditable() || !c.isEnabled()) {
/* 323 */       return false;
/*     */     }
/* 325 */     return (getImportFlavor(flavors, c) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   static class TextTransferable
/*     */     implements Transferable
/*     */   {
/*     */     private Position p0;
/*     */     
/*     */     private Position p1;
/*     */     
/*     */     private JTextComponent c;
/*     */     
/*     */     protected String plainData;
/*     */     
/*     */     private static DataFlavor[] stringFlavors;
/*     */     private static DataFlavor[] plainFlavors;
/*     */     
/*     */     TextTransferable(JTextComponent c, int start, int end) {
/* 344 */       this.c = c;
/* 345 */       Document doc = c.getDocument();
/*     */       try {
/* 347 */         this.p0 = doc.createPosition(start);
/* 348 */         this.p1 = doc.createPosition(end);
/* 349 */         this.plainData = c.getSelectedText();
/* 350 */       } catch (BadLocationException badLocationException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected String getPlainData() {
/* 358 */       return this.plainData;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
/* 374 */       if (isPlainFlavor(flavor)) {
/* 375 */         String data = getPlainData();
/* 376 */         data = (data == null) ? "" : data;
/* 377 */         if (String.class.equals(flavor.getRepresentationClass()))
/* 378 */           return data; 
/* 379 */         if (Reader.class.equals(flavor.getRepresentationClass()))
/* 380 */           return new StringReader(data); 
/* 381 */         if (InputStream.class.equals(flavor.getRepresentationClass())) {
/* 382 */           return new StringBufferInputStream(data);
/*     */         }
/*     */       }
/* 385 */       else if (isStringFlavor(flavor)) {
/* 386 */         String data = getPlainData();
/* 387 */         data = (data == null) ? "" : data;
/* 388 */         return data;
/*     */       } 
/* 390 */       throw new UnsupportedFlavorException(flavor);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DataFlavor[] getTransferDataFlavors() {
/* 403 */       int plainCount = isPlainSupported() ? plainFlavors.length : 0;
/* 404 */       int stringCount = isPlainSupported() ? stringFlavors.length : 0;
/* 405 */       int totalCount = plainCount + stringCount;
/* 406 */       DataFlavor[] flavors = new DataFlavor[totalCount];
/*     */ 
/*     */       
/* 409 */       int pos = 0;
/* 410 */       if (plainCount > 0) {
/* 411 */         System.arraycopy(plainFlavors, 0, flavors, pos, plainCount);
/* 412 */         pos += plainCount;
/*     */       } 
/* 414 */       if (stringCount > 0) {
/* 415 */         System.arraycopy(stringFlavors, 0, flavors, pos, stringCount);
/*     */       }
/*     */ 
/*     */       
/* 419 */       return flavors;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isDataFlavorSupported(DataFlavor flavor) {
/* 431 */       DataFlavor[] flavors = getTransferDataFlavors();
/* 432 */       for (DataFlavor dataFlavor : flavors) {
/* 433 */         if (dataFlavor.equals(flavor)) {
/* 434 */           return true;
/*     */         }
/*     */       } 
/* 437 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean isPlainFlavor(DataFlavor flavor) {
/* 447 */       DataFlavor[] flavors = plainFlavors;
/* 448 */       for (DataFlavor dataFlavor : flavors) {
/* 449 */         if (dataFlavor.equals(flavor)) {
/* 450 */           return true;
/*     */         }
/*     */       } 
/* 453 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean isPlainSupported() {
/* 461 */       return (this.plainData != null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean isStringFlavor(DataFlavor flavor) {
/* 471 */       DataFlavor[] flavors = stringFlavors;
/* 472 */       for (DataFlavor dataFlavor : flavors) {
/* 473 */         if (dataFlavor.equals(flavor)) {
/* 474 */           return true;
/*     */         }
/*     */       } 
/* 477 */       return false;
/*     */     }
/*     */     
/*     */     void removeText() {
/* 481 */       if (this.p0 != null && this.p1 != null && this.p0.getOffset() != this.p1.getOffset()) {
/*     */         try {
/* 483 */           Document doc = this.c.getDocument();
/* 484 */           doc.remove(this.p0.getOffset(), this.p1.getOffset() - this.p0.getOffset());
/* 485 */         } catch (BadLocationException badLocationException) {}
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*     */       try {
/* 494 */         plainFlavors = new DataFlavor[3];
/* 495 */         plainFlavors[0] = new DataFlavor("text/plain;class=java.lang.String");
/* 496 */         plainFlavors[1] = new DataFlavor("text/plain;class=java.io.Reader");
/* 497 */         plainFlavors[2] = new DataFlavor("text/plain;charset=unicode;class=java.io.InputStream");
/*     */         
/* 499 */         stringFlavors = new DataFlavor[2];
/* 500 */         stringFlavors[0] = new DataFlavor("application/x-java-jvm-local-objectref;class=java.lang.String");
/* 501 */         stringFlavors[1] = DataFlavor.stringFlavor;
/*     */       }
/* 503 */       catch (ClassNotFoundException cle) {
/* 504 */         System.err.println("Error initializing org.fife.ui.RTATextTransferHandler");
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\RTATextTransferHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */