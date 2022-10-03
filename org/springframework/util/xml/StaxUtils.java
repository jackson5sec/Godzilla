/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import javax.xml.stream.XMLEventFactory;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLInputFactory;
/*     */ import javax.xml.stream.XMLResolver;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stax.StAXResult;
/*     */ import javax.xml.transform.stax.StAXSource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StreamUtils;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class StaxUtils
/*     */ {
/*     */   private static final XMLResolver NO_OP_XML_RESOLVER = (publicID, systemID, base, ns) -> StreamUtils.emptyInput();
/*     */   
/*     */   public static XMLInputFactory createDefensiveInputFactory() {
/*  67 */     return createDefensiveInputFactory(XMLInputFactory::newInstance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends XMLInputFactory> T createDefensiveInputFactory(Supplier<T> instanceSupplier) {
/*  77 */     XMLInputFactory xMLInputFactory = (XMLInputFactory)instanceSupplier.get();
/*  78 */     xMLInputFactory.setProperty("javax.xml.stream.supportDTD", Boolean.valueOf(false));
/*  79 */     xMLInputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", Boolean.valueOf(false));
/*  80 */     xMLInputFactory.setXMLResolver(NO_OP_XML_RESOLVER);
/*  81 */     return (T)xMLInputFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Source createStaxSource(XMLStreamReader streamReader) {
/*  90 */     return new StAXSource(streamReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Source createStaxSource(XMLEventReader eventReader) throws XMLStreamException {
/*  99 */     return new StAXSource(eventReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Source createCustomStaxSource(XMLStreamReader streamReader) {
/* 108 */     return new StaxSource(streamReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Source createCustomStaxSource(XMLEventReader eventReader) {
/* 117 */     return new StaxSource(eventReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isStaxSource(Source source) {
/* 127 */     return (source instanceof StAXSource || source instanceof StaxSource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static XMLStreamReader getXMLStreamReader(Source source) {
/* 139 */     if (source instanceof StAXSource) {
/* 140 */       return ((StAXSource)source).getXMLStreamReader();
/*     */     }
/* 142 */     if (source instanceof StaxSource) {
/* 143 */       return ((StaxSource)source).getXMLStreamReader();
/*     */     }
/*     */     
/* 146 */     throw new IllegalArgumentException("Source '" + source + "' is neither StaxSource nor StAXSource");
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
/*     */   @Nullable
/*     */   public static XMLEventReader getXMLEventReader(Source source) {
/* 159 */     if (source instanceof StAXSource) {
/* 160 */       return ((StAXSource)source).getXMLEventReader();
/*     */     }
/* 162 */     if (source instanceof StaxSource) {
/* 163 */       return ((StaxSource)source).getXMLEventReader();
/*     */     }
/*     */     
/* 166 */     throw new IllegalArgumentException("Source '" + source + "' is neither StaxSource nor StAXSource");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Result createStaxResult(XMLStreamWriter streamWriter) {
/* 176 */     return new StAXResult(streamWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Result createStaxResult(XMLEventWriter eventWriter) {
/* 185 */     return new StAXResult(eventWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Result createCustomStaxResult(XMLStreamWriter streamWriter) {
/* 194 */     return new StaxResult(streamWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Result createCustomStaxResult(XMLEventWriter eventWriter) {
/* 203 */     return new StaxResult(eventWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isStaxResult(Result result) {
/* 213 */     return (result instanceof StAXResult || result instanceof StaxResult);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static XMLStreamWriter getXMLStreamWriter(Result result) {
/* 225 */     if (result instanceof StAXResult) {
/* 226 */       return ((StAXResult)result).getXMLStreamWriter();
/*     */     }
/* 228 */     if (result instanceof StaxResult) {
/* 229 */       return ((StaxResult)result).getXMLStreamWriter();
/*     */     }
/*     */     
/* 232 */     throw new IllegalArgumentException("Result '" + result + "' is neither StaxResult nor StAXResult");
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
/*     */   @Nullable
/*     */   public static XMLEventWriter getXMLEventWriter(Result result) {
/* 245 */     if (result instanceof StAXResult) {
/* 246 */       return ((StAXResult)result).getXMLEventWriter();
/*     */     }
/* 248 */     if (result instanceof StaxResult) {
/* 249 */       return ((StaxResult)result).getXMLEventWriter();
/*     */     }
/*     */     
/* 252 */     throw new IllegalArgumentException("Result '" + result + "' is neither StaxResult nor StAXResult");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLEventReader createXMLEventReader(List<XMLEvent> events) {
/* 263 */     return new ListBasedXMLEventReader(events);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContentHandler createContentHandler(XMLStreamWriter streamWriter) {
/* 272 */     return new StaxStreamHandler(streamWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContentHandler createContentHandler(XMLEventWriter eventWriter) {
/* 281 */     return new StaxEventHandler(eventWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLReader createXMLReader(XMLStreamReader streamReader) {
/* 290 */     return new StaxStreamXMLReader(streamReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLReader createXMLReader(XMLEventReader eventReader) {
/* 299 */     return new StaxEventXMLReader(eventReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLStreamReader createEventStreamReader(XMLEventReader eventReader) throws XMLStreamException {
/* 309 */     return new XMLEventStreamReader(eventReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLStreamWriter createEventStreamWriter(XMLEventWriter eventWriter) {
/* 318 */     return new XMLEventStreamWriter(eventWriter, XMLEventFactory.newFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLStreamWriter createEventStreamWriter(XMLEventWriter eventWriter, XMLEventFactory eventFactory) {
/* 327 */     return new XMLEventStreamWriter(eventWriter, eventFactory);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\StaxUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */