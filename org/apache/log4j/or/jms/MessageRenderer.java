/*    */ package org.apache.log4j.or.jms;
/*    */ 
/*    */ import javax.jms.JMSException;
/*    */ import javax.jms.Message;
/*    */ import org.apache.log4j.helpers.LogLog;
/*    */ import org.apache.log4j.or.ObjectRenderer;
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
/*    */ public class MessageRenderer
/*    */   implements ObjectRenderer
/*    */ {
/*    */   public String doRender(Object o) {
/* 44 */     if (o instanceof Message) {
/* 45 */       StringBuffer sbuf = new StringBuffer();
/* 46 */       Message m = (Message)o;
/*    */       try {
/* 48 */         sbuf.append("DeliveryMode=");
/* 49 */         switch (m.getJMSDeliveryMode()) {
/*    */           case 1:
/* 51 */             sbuf.append("NON_PERSISTENT");
/*    */             break;
/*    */           case 2:
/* 54 */             sbuf.append("PERSISTENT"); break;
/*    */           default:
/* 56 */             sbuf.append("UNKNOWN"); break;
/*    */         } 
/* 58 */         sbuf.append(", CorrelationID=");
/* 59 */         sbuf.append(m.getJMSCorrelationID());
/*    */         
/* 61 */         sbuf.append(", Destination=");
/* 62 */         sbuf.append(m.getJMSDestination());
/*    */         
/* 64 */         sbuf.append(", Expiration=");
/* 65 */         sbuf.append(m.getJMSExpiration());
/*    */         
/* 67 */         sbuf.append(", MessageID=");
/* 68 */         sbuf.append(m.getJMSMessageID());
/*    */         
/* 70 */         sbuf.append(", Priority=");
/* 71 */         sbuf.append(m.getJMSPriority());
/*    */         
/* 73 */         sbuf.append(", Redelivered=");
/* 74 */         sbuf.append(m.getJMSRedelivered());
/*    */         
/* 76 */         sbuf.append(", ReplyTo=");
/* 77 */         sbuf.append(m.getJMSReplyTo());
/*    */         
/* 79 */         sbuf.append(", Timestamp=");
/* 80 */         sbuf.append(m.getJMSTimestamp());
/*    */         
/* 82 */         sbuf.append(", Type=");
/* 83 */         sbuf.append(m.getJMSType());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       }
/* 92 */       catch (JMSException e) {
/* 93 */         LogLog.error("Could not parse Message.", (Throwable)e);
/*    */       } 
/* 95 */       return sbuf.toString();
/*    */     } 
/* 97 */     return o.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\or\jms\MessageRenderer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */