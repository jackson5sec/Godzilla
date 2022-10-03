/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.fife.io.UnicodeReader;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Macro
/*     */ {
/*     */   private String name;
/*     */   private ArrayList<MacroRecord> macroRecords;
/*     */   private static final String ROOT_ELEMENT = "macro";
/*     */   private static final String MACRO_NAME = "macroName";
/*     */   private static final String ACTION = "action";
/*     */   private static final String ID = "id";
/*     */   private static final String UNTITLED_MACRO_NAME = "<Untitled>";
/*     */   private static final String FILE_ENCODING = "UTF-8";
/*     */   
/*     */   public Macro() {
/*  84 */     this("<Untitled>");
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
/*     */   public Macro(File file) throws IOException {
/*     */     Document doc;
/*  99 */     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*     */ 
/*     */     
/*     */     try {
/* 103 */       DocumentBuilder db = dbf.newDocumentBuilder();
/*     */       
/* 105 */       InputSource is = new InputSource((Reader)new UnicodeReader(new FileInputStream(file), "UTF-8"));
/*     */       
/* 107 */       is.setEncoding("UTF-8");
/* 108 */       doc = db.parse(is);
/* 109 */     } catch (Exception e) {
/* 110 */       e.printStackTrace();
/* 111 */       String desc = e.getMessage();
/* 112 */       if (desc == null) {
/* 113 */         desc = e.toString();
/*     */       }
/* 115 */       throw new IOException("Error parsing XML: " + desc);
/*     */     } 
/*     */     
/* 118 */     this.macroRecords = new ArrayList<>();
/*     */ 
/*     */     
/* 121 */     boolean parsedOK = initializeFromXMLFile(doc.getDocumentElement());
/* 122 */     if (!parsedOK) {
/* 123 */       this.name = null;
/* 124 */       this.macroRecords.clear();
/* 125 */       this.macroRecords = null;
/* 126 */       throw new IOException("Error parsing XML!");
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
/*     */   public Macro(String name) {
/* 138 */     this(name, null);
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
/*     */   public Macro(String name, List<MacroRecord> records) {
/* 150 */     this.name = name;
/*     */     
/* 152 */     if (records != null) {
/* 153 */       this.macroRecords = new ArrayList<>(records.size());
/* 154 */       this.macroRecords.addAll(records);
/*     */     } else {
/*     */       
/* 157 */       this.macroRecords = new ArrayList<>(10);
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
/*     */   public void addMacroRecord(MacroRecord record) {
/* 170 */     if (record != null) {
/* 171 */       this.macroRecords.add(record);
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
/*     */   public List<MacroRecord> getMacroRecords() {
/* 183 */     return this.macroRecords;
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
/*     */   public String getName() {
/* 196 */     return this.name;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean initializeFromXMLFile(Element root) {
/* 223 */     NodeList childNodes = root.getChildNodes();
/* 224 */     int count = childNodes.getLength();
/*     */     
/* 226 */     for (int i = 0; i < count; i++) {
/*     */       String nodeName;
/* 228 */       Node node = childNodes.item(i);
/* 229 */       int type = node.getNodeType();
/* 230 */       switch (type) {
/*     */ 
/*     */ 
/*     */         
/*     */         case 1:
/* 235 */           nodeName = node.getNodeName();
/*     */           
/* 237 */           if (nodeName.equals("macroName")) {
/* 238 */             NodeList childNodes2 = node.getChildNodes();
/* 239 */             this.name = "<Untitled>";
/* 240 */             if (childNodes2.getLength() > 0) {
/* 241 */               node = childNodes2.item(0);
/* 242 */               int type2 = node.getNodeType();
/* 243 */               if (type2 != 4 && type2 != 3)
/*     */               {
/* 245 */                 return false;
/*     */               }
/* 247 */               this.name = node.getNodeValue().trim();
/*     */             } 
/*     */             
/*     */             break;
/*     */           } 
/* 252 */           if (nodeName.equals("action")) {
/* 253 */             NamedNodeMap attributes = node.getAttributes();
/* 254 */             if (attributes == null || attributes.getLength() != 1) {
/* 255 */               return false;
/*     */             }
/* 257 */             Node node2 = attributes.item(0);
/* 258 */             MacroRecord macroRecord = new MacroRecord();
/* 259 */             if (!node2.getNodeName().equals("id")) {
/* 260 */               return false;
/*     */             }
/* 262 */             macroRecord.id = node2.getNodeValue();
/* 263 */             NodeList childNodes2 = node.getChildNodes();
/* 264 */             int length = childNodes2.getLength();
/* 265 */             if (length == 0) {
/*     */               
/* 267 */               macroRecord.actionCommand = "";
/*     */               
/* 269 */               this.macroRecords.add(macroRecord);
/*     */               
/*     */               break;
/*     */             } 
/* 273 */             node = childNodes2.item(0);
/* 274 */             int type2 = node.getNodeType();
/* 275 */             if (type2 != 4 && type2 != 3)
/*     */             {
/* 277 */               return false;
/*     */             }
/* 279 */             macroRecord.actionCommand = node.getNodeValue();
/* 280 */             this.macroRecords.add(macroRecord);
/*     */           } 
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 294 */     return true;
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
/*     */   public void saveToFile(File file) throws IOException {
/* 310 */     saveToFile(file.getAbsolutePath());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveToFile(String fileName) throws IOException {
/*     */     try {
/* 342 */       DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
/* 343 */       DOMImplementation impl = db.getDOMImplementation();
/*     */       
/* 345 */       Document doc = impl.createDocument(null, "macro", null);
/* 346 */       Element rootElement = doc.getDocumentElement();
/*     */ 
/*     */       
/* 349 */       Element nameElement = doc.createElement("macroName");
/* 350 */       nameElement.appendChild(doc.createCDATASection(this.name));
/* 351 */       rootElement.appendChild(nameElement);
/*     */ 
/*     */       
/* 354 */       for (MacroRecord record : this.macroRecords) {
/* 355 */         Element actionElement = doc.createElement("action");
/* 356 */         actionElement.setAttribute("id", record.id);
/* 357 */         if (record.actionCommand != null && record.actionCommand
/* 358 */           .length() > 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 366 */           String command = record.actionCommand;
/* 367 */           for (int j = 0; j < command.length(); j++) {
/* 368 */             if (command.charAt(j) < ' ') {
/* 369 */               command = command.substring(0, j);
/* 370 */               if (j < command.length() - 1) {
/* 371 */                 command = command + command.substring(j + 1);
/*     */               }
/*     */             } 
/*     */           } 
/* 375 */           Node n = doc.createCDATASection(command);
/* 376 */           actionElement.appendChild(n);
/*     */         } 
/* 378 */         rootElement.appendChild(actionElement);
/*     */       } 
/*     */ 
/*     */       
/* 382 */       StreamResult result = new StreamResult(new File(fileName));
/* 383 */       DOMSource source = new DOMSource(doc);
/* 384 */       TransformerFactory transFac = TransformerFactory.newInstance();
/* 385 */       Transformer transformer = transFac.newTransformer();
/* 386 */       transformer.setOutputProperty("indent", "yes");
/* 387 */       transformer.setOutputProperty("encoding", "UTF-8");
/* 388 */       transformer.transform(source, result);
/*     */     }
/* 390 */     catch (RuntimeException re) {
/* 391 */       throw re;
/* 392 */     } catch (Exception e) {
/* 393 */       throw new IOException("Error generating XML!");
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
/*     */   public void setName(String name) {
/* 408 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class MacroRecord
/*     */   {
/*     */     String id;
/*     */ 
/*     */     
/*     */     String actionCommand;
/*     */ 
/*     */ 
/*     */     
/*     */     MacroRecord() {
/* 424 */       this(null, null);
/*     */     }
/*     */     
/*     */     MacroRecord(String id, String actionCommand) {
/* 428 */       this.id = id;
/* 429 */       this.actionCommand = actionCommand;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\Macro.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */