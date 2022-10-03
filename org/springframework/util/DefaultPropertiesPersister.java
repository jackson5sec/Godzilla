/*    */ package org.springframework.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Reader;
/*    */ import java.io.Writer;
/*    */ import java.util.Properties;
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
/*    */ public class DefaultPropertiesPersister
/*    */   implements PropertiesPersister
/*    */ {
/*    */   public void load(Properties props, InputStream is) throws IOException {
/* 59 */     props.load(is);
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(Properties props, Reader reader) throws IOException {
/* 64 */     props.load(reader);
/*    */   }
/*    */ 
/*    */   
/*    */   public void store(Properties props, OutputStream os, String header) throws IOException {
/* 69 */     props.store(os, header);
/*    */   }
/*    */ 
/*    */   
/*    */   public void store(Properties props, Writer writer, String header) throws IOException {
/* 74 */     props.store(writer, header);
/*    */   }
/*    */ 
/*    */   
/*    */   public void loadFromXml(Properties props, InputStream is) throws IOException {
/* 79 */     props.loadFromXML(is);
/*    */   }
/*    */ 
/*    */   
/*    */   public void storeToXml(Properties props, OutputStream os, String header) throws IOException {
/* 84 */     props.storeToXML(os, header);
/*    */   }
/*    */ 
/*    */   
/*    */   public void storeToXml(Properties props, OutputStream os, String header, String encoding) throws IOException {
/* 89 */     props.storeToXML(os, header, encoding);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\DefaultPropertiesPersister.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */