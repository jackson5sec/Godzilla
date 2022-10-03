/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.beans.XMLDecoder;
/*     */ import java.beans.XMLEncoder;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Segment;
/*     */ import org.fife.ui.rsyntaxtextarea.templates.CodeTemplate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CodeTemplateManager
/*     */ {
/*  66 */   private Segment s = new Segment();
/*  67 */   private TemplateComparator comparator = new TemplateComparator();
/*  68 */   private List<CodeTemplate> templates = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private int maxTemplateIDLength;
/*     */ 
/*     */ 
/*     */   
/*     */   private File directory;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addTemplate(CodeTemplate template) {
/*  82 */     if (template == null) {
/*  83 */       throw new IllegalArgumentException("template cannot be null");
/*     */     }
/*  85 */     this.templates.add(template);
/*  86 */     sortTemplates();
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
/*     */   public synchronized CodeTemplate getTemplate(RSyntaxTextArea textArea) {
/*  99 */     int caretPos = textArea.getCaretPosition();
/* 100 */     int charsToGet = Math.min(caretPos, this.maxTemplateIDLength);
/*     */     try {
/* 102 */       Document doc = textArea.getDocument();
/* 103 */       doc.getText(caretPos - charsToGet, charsToGet, this.s);
/* 104 */       int index = Collections.binarySearch(this.templates, this.s, this.comparator);
/* 105 */       return (index >= 0) ? this.templates.get(index) : null;
/* 106 */     } catch (BadLocationException ble) {
/* 107 */       ble.printStackTrace();
/* 108 */       throw new InternalError("Error in CodeTemplateManager");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized int getTemplateCount() {
/* 119 */     return this.templates.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized CodeTemplate[] getTemplates() {
/* 129 */     CodeTemplate[] temp = new CodeTemplate[this.templates.size()];
/* 130 */     return this.templates.<CodeTemplate>toArray(temp);
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
/*     */   private static boolean isValidChar(char ch) {
/* 142 */     return (RSyntaxUtilities.isLetterOrDigit(ch) || ch == '_');
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
/*     */   public synchronized boolean removeTemplate(CodeTemplate template) {
/* 159 */     if (template == null) {
/* 160 */       throw new IllegalArgumentException("template cannot be null");
/*     */     }
/*     */ 
/*     */     
/* 164 */     return this.templates.remove(template);
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
/*     */   public synchronized CodeTemplate removeTemplate(String id) {
/* 181 */     if (id == null) {
/* 182 */       throw new IllegalArgumentException("id cannot be null");
/*     */     }
/*     */ 
/*     */     
/* 186 */     for (Iterator<CodeTemplate> i = this.templates.iterator(); i.hasNext(); ) {
/* 187 */       CodeTemplate template = i.next();
/* 188 */       if (id.equals(template.getID())) {
/* 189 */         i.remove();
/* 190 */         return template;
/*     */       } 
/*     */     } 
/*     */     
/* 194 */     return null;
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
/*     */   public synchronized void replaceTemplates(CodeTemplate[] newTemplates) {
/* 207 */     this.templates.clear();
/* 208 */     if (newTemplates != null) {
/* 209 */       Collections.addAll(this.templates, newTemplates);
/*     */     }
/* 211 */     sortTemplates();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean saveTemplates() {
/* 222 */     if (this.templates == null) {
/* 223 */       return true;
/*     */     }
/* 225 */     if (this.directory == null || !this.directory.isDirectory()) {
/* 226 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 231 */     File[] oldXMLFiles = this.directory.listFiles(new XMLFileFilter());
/* 232 */     if (oldXMLFiles == null) {
/* 233 */       return false;
/*     */     }
/* 235 */     int count = oldXMLFiles.length;
/* 236 */     for (File oldXMLFile : oldXMLFiles)
/*     */     {
/* 238 */       oldXMLFile.delete();
/*     */     }
/*     */ 
/*     */     
/* 242 */     boolean wasSuccessful = true;
/* 243 */     for (CodeTemplate template : this.templates) {
/* 244 */       File xmlFile = new File(this.directory, template.getID() + ".xml");
/*     */       try {
/* 246 */         XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(xmlFile)));
/*     */         
/* 248 */         e.writeObject(template);
/* 249 */         e.close();
/* 250 */       } catch (IOException ioe) {
/* 251 */         ioe.printStackTrace();
/* 252 */         wasSuccessful = false;
/*     */       } 
/*     */     } 
/*     */     
/* 256 */     return wasSuccessful;
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
/*     */   public synchronized int setTemplateDirectory(File dir) {
/* 272 */     if (dir != null && dir.isDirectory()) {
/*     */       
/* 274 */       this.directory = dir;
/*     */       
/* 276 */       File[] files = dir.listFiles(new XMLFileFilter());
/* 277 */       int newCount = (files == null) ? 0 : files.length;
/* 278 */       int oldCount = this.templates.size();
/*     */       
/* 280 */       List<CodeTemplate> temp = new ArrayList<>(oldCount + newCount);
/*     */       
/* 282 */       temp.addAll(this.templates);
/*     */       
/* 284 */       for (int i = 0; i < newCount; i++) {
/*     */         try {
/* 286 */           XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream(files[i])));
/*     */           
/* 288 */           Object obj = d.readObject();
/* 289 */           if (!(obj instanceof CodeTemplate)) {
/* 290 */             d.close();
/* 291 */             throw new IOException("Not a CodeTemplate: " + files[i]
/* 292 */                 .getAbsolutePath());
/*     */           } 
/* 294 */           temp.add((CodeTemplate)obj);
/* 295 */           d.close();
/* 296 */         } catch (Exception e) {
/*     */ 
/*     */ 
/*     */           
/* 300 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/* 303 */       this.templates = temp;
/* 304 */       sortTemplates();
/*     */       
/* 306 */       return getTemplateCount();
/*     */     } 
/*     */ 
/*     */     
/* 310 */     return -1;
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
/*     */   private synchronized void sortTemplates() {
/* 323 */     this.maxTemplateIDLength = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 328 */     for (Iterator<CodeTemplate> i = this.templates.iterator(); i.hasNext(); ) {
/* 329 */       CodeTemplate temp = i.next();
/* 330 */       if (temp == null || temp.getID() == null) {
/* 331 */         i.remove();
/*     */         continue;
/*     */       } 
/* 334 */       this.maxTemplateIDLength = Math.max(this.maxTemplateIDLength, temp
/* 335 */           .getID().length());
/*     */     } 
/*     */ 
/*     */     
/* 339 */     Collections.sort(this.templates);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class TemplateComparator
/*     */     implements Comparator<Object>, Serializable
/*     */   {
/*     */     private TemplateComparator() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int compare(Object template, Object segment) {
/* 356 */       CodeTemplate t = (CodeTemplate)template;
/* 357 */       char[] templateArray = t.getID().toCharArray();
/* 358 */       int i = 0;
/* 359 */       int len1 = templateArray.length;
/*     */ 
/*     */       
/* 362 */       Segment s = (Segment)segment;
/* 363 */       char[] segArray = s.array;
/* 364 */       int len2 = s.count;
/* 365 */       int j = s.offset + len2 - 1;
/* 366 */       while (j >= s.offset && CodeTemplateManager.isValidChar(segArray[j])) {
/* 367 */         j--;
/*     */       }
/* 369 */       j++;
/* 370 */       int segShift = j - s.offset;
/* 371 */       len2 -= segShift;
/*     */       
/* 373 */       int n = Math.min(len1, len2);
/* 374 */       while (n-- != 0) {
/* 375 */         char c1 = templateArray[i++];
/* 376 */         char c2 = segArray[j++];
/* 377 */         if (c1 != c2) {
/* 378 */           return c1 - c2;
/*     */         }
/*     */       } 
/* 381 */       return len1 - len2;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class XMLFileFilter
/*     */     implements FileFilter
/*     */   {
/*     */     private XMLFileFilter() {}
/*     */ 
/*     */     
/*     */     public boolean accept(File f) {
/* 394 */       return f.getName().toLowerCase().endsWith(".xml");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\CodeTemplateManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */