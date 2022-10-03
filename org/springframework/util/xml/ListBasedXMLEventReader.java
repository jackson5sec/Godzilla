/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ListBasedXMLEventReader
/*     */   extends AbstractXMLEventReader
/*     */ {
/*     */   private final List<XMLEvent> events;
/*     */   @Nullable
/*     */   private XMLEvent currentEvent;
/*  46 */   private int cursor = 0;
/*     */ 
/*     */   
/*     */   public ListBasedXMLEventReader(List<XMLEvent> events) {
/*  50 */     Assert.notNull(events, "XMLEvent List must not be null");
/*  51 */     this.events = new ArrayList<>(events);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/*  57 */     return (this.cursor < this.events.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public XMLEvent nextEvent() {
/*  62 */     if (hasNext()) {
/*  63 */       this.currentEvent = this.events.get(this.cursor);
/*  64 */       this.cursor++;
/*  65 */       return this.currentEvent;
/*     */     } 
/*     */     
/*  68 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public XMLEvent peek() {
/*  75 */     if (hasNext()) {
/*  76 */       return this.events.get(this.cursor);
/*     */     }
/*     */     
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getElementText() throws XMLStreamException {
/*  85 */     checkIfClosed();
/*  86 */     if (this.currentEvent == null || !this.currentEvent.isStartElement()) {
/*  87 */       throw new XMLStreamException("Not at START_ELEMENT: " + this.currentEvent);
/*     */     }
/*     */     
/*  90 */     StringBuilder builder = new StringBuilder();
/*     */     while (true) {
/*  92 */       XMLEvent event = nextEvent();
/*  93 */       if (event.isEndElement()) {
/*     */         break;
/*     */       }
/*  96 */       if (!event.isCharacters()) {
/*  97 */         throw new XMLStreamException("Unexpected non-text event: " + event);
/*     */       }
/*  99 */       Characters characters = event.asCharacters();
/* 100 */       if (!characters.isIgnorableWhiteSpace()) {
/* 101 */         builder.append(event.asCharacters().getData());
/*     */       }
/*     */     } 
/* 104 */     return builder.toString();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public XMLEvent nextTag() throws XMLStreamException {
/*     */     XMLEvent event;
/* 110 */     checkIfClosed();
/*     */     
/*     */     while (true) {
/* 113 */       event = nextEvent();
/* 114 */       switch (event.getEventType()) {
/*     */         case 1:
/*     */         case 2:
/* 117 */           return event;
/*     */         case 8:
/* 119 */           return null;
/*     */         case 3:
/*     */         case 5:
/*     */         case 6:
/*     */           continue;
/*     */         case 4:
/*     */         case 12:
/* 126 */           if (!event.asCharacters().isWhiteSpace())
/* 127 */             throw new XMLStreamException("Non-ignorable whitespace CDATA or CHARACTERS event: " + event); 
/*     */           continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 132 */     throw new XMLStreamException("Expected START_ELEMENT or END_ELEMENT: " + event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 139 */     super.close();
/* 140 */     this.events.clear();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\ListBasedXMLEventReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */