/*    */ package org.springframework.util.xml;
/*    */ 
/*    */ import java.util.NoSuchElementException;
/*    */ import javax.xml.stream.XMLEventReader;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import org.springframework.util.ClassUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractXMLEventReader
/*    */   implements XMLEventReader
/*    */ {
/*    */   private boolean closed;
/*    */   
/*    */   public Object next() {
/*    */     try {
/* 41 */       return nextEvent();
/*    */     }
/* 43 */     catch (XMLStreamException ex) {
/* 44 */       throw new NoSuchElementException(ex.getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove() {
/* 50 */     throw new UnsupportedOperationException("remove not supported on " + 
/* 51 */         ClassUtils.getShortName(getClass()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getProperty(String name) throws IllegalArgumentException {
/* 60 */     throw new IllegalArgumentException("Property not supported: [" + name + "]");
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 65 */     this.closed = true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void checkIfClosed() throws XMLStreamException {
/* 74 */     if (this.closed)
/* 75 */       throw new XMLStreamException("XMLEventReader has been closed"); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\AbstractXMLEventReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */