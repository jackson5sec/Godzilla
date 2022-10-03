/*    */ package com.kichik.pecoff4j;
/*    */ 
/*    */ import com.kichik.pecoff4j.util.DataObject;
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
/*    */ public class AttributeCertificateTable
/*    */   extends DataObject
/*    */ {
/*    */   private int length;
/*    */   private int revision;
/*    */   private int certificateType;
/*    */   private byte[] certificate;
/*    */   
/*    */   public int getLength() {
/* 25 */     return this.length;
/*    */   }
/*    */   
/*    */   public void setLength(int length) {
/* 29 */     this.length = length;
/*    */   }
/*    */   
/*    */   public int getRevision() {
/* 33 */     return this.revision;
/*    */   }
/*    */   
/*    */   public void setRevision(int revision) {
/* 37 */     this.revision = revision;
/*    */   }
/*    */   
/*    */   public int getCertificateType() {
/* 41 */     return this.certificateType;
/*    */   }
/*    */   
/*    */   public void setCertificateType(int certificateType) {
/* 45 */     this.certificateType = certificateType;
/*    */   }
/*    */   
/*    */   public byte[] getCertificate() {
/* 49 */     return this.certificate;
/*    */   }
/*    */   
/*    */   public void setCertificate(byte[] certificate) {
/* 53 */     this.certificate = certificate;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\AttributeCertificateTable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */