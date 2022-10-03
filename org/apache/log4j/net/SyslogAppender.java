/*     */ package org.apache.log4j.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.Writer;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import org.apache.log4j.AppenderSkeleton;
/*     */ import org.apache.log4j.Layout;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.apache.log4j.helpers.SyslogQuietWriter;
/*     */ import org.apache.log4j.helpers.SyslogWriter;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SyslogAppender
/*     */   extends AppenderSkeleton
/*     */ {
/*     */   public static final int LOG_KERN = 0;
/*     */   public static final int LOG_USER = 8;
/*     */   public static final int LOG_MAIL = 16;
/*     */   public static final int LOG_DAEMON = 24;
/*     */   public static final int LOG_AUTH = 32;
/*     */   public static final int LOG_SYSLOG = 40;
/*     */   public static final int LOG_LPR = 48;
/*     */   public static final int LOG_NEWS = 56;
/*     */   public static final int LOG_UUCP = 64;
/*     */   public static final int LOG_CRON = 72;
/*     */   public static final int LOG_AUTHPRIV = 80;
/*     */   public static final int LOG_FTP = 88;
/*     */   public static final int LOG_LOCAL0 = 128;
/*     */   public static final int LOG_LOCAL1 = 136;
/*     */   public static final int LOG_LOCAL2 = 144;
/*     */   public static final int LOG_LOCAL3 = 152;
/*     */   public static final int LOG_LOCAL4 = 160;
/*     */   public static final int LOG_LOCAL5 = 168;
/*     */   public static final int LOG_LOCAL6 = 176;
/*     */   public static final int LOG_LOCAL7 = 184;
/*     */   protected static final int SYSLOG_HOST_OI = 0;
/*     */   protected static final int FACILITY_OI = 1;
/*     */   static final String TAB = "    ";
/*  97 */   int syslogFacility = 8;
/*     */ 
/*     */   
/*     */   String facilityStr;
/*     */ 
/*     */   
/*     */   boolean facilityPrinting = false;
/*     */ 
/*     */   
/*     */   SyslogQuietWriter sqw;
/*     */ 
/*     */   
/*     */   String syslogHost;
/*     */ 
/*     */   
/*     */   private boolean header = false;
/*     */ 
/*     */   
/* 115 */   private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss ", Locale.ENGLISH);
/*     */ 
/*     */ 
/*     */   
/*     */   private String localHostname;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean layoutHeaderChecked = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SyslogAppender() {
/* 129 */     initSyslogFacilityStr();
/*     */   }
/*     */ 
/*     */   
/*     */   public SyslogAppender(Layout layout, int syslogFacility) {
/* 134 */     this.layout = layout;
/* 135 */     this.syslogFacility = syslogFacility;
/* 136 */     initSyslogFacilityStr();
/*     */   }
/*     */ 
/*     */   
/*     */   public SyslogAppender(Layout layout, String syslogHost, int syslogFacility) {
/* 141 */     this(layout, syslogFacility);
/* 142 */     setSyslogHost(syslogHost);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void close() {
/* 153 */     this.closed = true;
/* 154 */     if (this.sqw != null) {
/*     */       try {
/* 156 */         if (this.layoutHeaderChecked && this.layout != null && this.layout.getFooter() != null) {
/* 157 */           sendLayoutMessage(this.layout.getFooter());
/*     */         }
/* 159 */         this.sqw.close();
/* 160 */         this.sqw = null;
/* 161 */       } catch (InterruptedIOException e) {
/* 162 */         Thread.currentThread().interrupt();
/* 163 */         this.sqw = null;
/* 164 */       } catch (IOException e) {
/* 165 */         this.sqw = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void initSyslogFacilityStr() {
/* 172 */     this.facilityStr = getFacilityString(this.syslogFacility);
/*     */     
/* 174 */     if (this.facilityStr == null) {
/* 175 */       System.err.println("\"" + this.syslogFacility + "\" is an unknown syslog facility. Defaulting to \"USER\".");
/*     */       
/* 177 */       this.syslogFacility = 8;
/* 178 */       this.facilityStr = "user:";
/*     */     } else {
/* 180 */       this.facilityStr += ":";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getFacilityString(int syslogFacility) {
/* 191 */     switch (syslogFacility) { case 0:
/* 192 */         return "kern";
/* 193 */       case 8: return "user";
/* 194 */       case 16: return "mail";
/* 195 */       case 24: return "daemon";
/* 196 */       case 32: return "auth";
/* 197 */       case 40: return "syslog";
/* 198 */       case 48: return "lpr";
/* 199 */       case 56: return "news";
/* 200 */       case 64: return "uucp";
/* 201 */       case 72: return "cron";
/* 202 */       case 80: return "authpriv";
/* 203 */       case 88: return "ftp";
/* 204 */       case 128: return "local0";
/* 205 */       case 136: return "local1";
/* 206 */       case 144: return "local2";
/* 207 */       case 152: return "local3";
/* 208 */       case 160: return "local4";
/* 209 */       case 168: return "local5";
/* 210 */       case 176: return "local6";
/* 211 */       case 184: return "local7"; }
/* 212 */      return null;
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
/*     */   public static int getFacility(String facilityName) {
/* 230 */     if (facilityName != null) {
/* 231 */       facilityName = facilityName.trim();
/*     */     }
/* 233 */     if ("KERN".equalsIgnoreCase(facilityName))
/* 234 */       return 0; 
/* 235 */     if ("USER".equalsIgnoreCase(facilityName))
/* 236 */       return 8; 
/* 237 */     if ("MAIL".equalsIgnoreCase(facilityName))
/* 238 */       return 16; 
/* 239 */     if ("DAEMON".equalsIgnoreCase(facilityName))
/* 240 */       return 24; 
/* 241 */     if ("AUTH".equalsIgnoreCase(facilityName))
/* 242 */       return 32; 
/* 243 */     if ("SYSLOG".equalsIgnoreCase(facilityName))
/* 244 */       return 40; 
/* 245 */     if ("LPR".equalsIgnoreCase(facilityName))
/* 246 */       return 48; 
/* 247 */     if ("NEWS".equalsIgnoreCase(facilityName))
/* 248 */       return 56; 
/* 249 */     if ("UUCP".equalsIgnoreCase(facilityName))
/* 250 */       return 64; 
/* 251 */     if ("CRON".equalsIgnoreCase(facilityName))
/* 252 */       return 72; 
/* 253 */     if ("AUTHPRIV".equalsIgnoreCase(facilityName))
/* 254 */       return 80; 
/* 255 */     if ("FTP".equalsIgnoreCase(facilityName))
/* 256 */       return 88; 
/* 257 */     if ("LOCAL0".equalsIgnoreCase(facilityName))
/* 258 */       return 128; 
/* 259 */     if ("LOCAL1".equalsIgnoreCase(facilityName))
/* 260 */       return 136; 
/* 261 */     if ("LOCAL2".equalsIgnoreCase(facilityName))
/* 262 */       return 144; 
/* 263 */     if ("LOCAL3".equalsIgnoreCase(facilityName))
/* 264 */       return 152; 
/* 265 */     if ("LOCAL4".equalsIgnoreCase(facilityName))
/* 266 */       return 160; 
/* 267 */     if ("LOCAL5".equalsIgnoreCase(facilityName))
/* 268 */       return 168; 
/* 269 */     if ("LOCAL6".equalsIgnoreCase(facilityName))
/* 270 */       return 176; 
/* 271 */     if ("LOCAL7".equalsIgnoreCase(facilityName)) {
/* 272 */       return 184;
/*     */     }
/* 274 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void splitPacket(String header, String packet) {
/* 280 */     int byteCount = (packet.getBytes()).length;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 286 */     if (byteCount <= 1019) {
/* 287 */       this.sqw.write(packet);
/*     */     } else {
/* 289 */       int split = header.length() + (packet.length() - header.length()) / 2;
/* 290 */       splitPacket(header, packet.substring(0, split) + "...");
/* 291 */       splitPacket(header, header + "..." + packet.substring(split));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void append(LoggingEvent event) {
/*     */     String packet;
/* 298 */     if (!isAsSevereAsThreshold((Priority)event.getLevel())) {
/*     */       return;
/*     */     }
/*     */     
/* 302 */     if (this.sqw == null) {
/* 303 */       this.errorHandler.error("No syslog host is set for SyslogAppedender named \"" + this.name + "\".");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 308 */     if (!this.layoutHeaderChecked) {
/* 309 */       if (this.layout != null && this.layout.getHeader() != null) {
/* 310 */         sendLayoutMessage(this.layout.getHeader());
/*     */       }
/* 312 */       this.layoutHeaderChecked = true;
/*     */     } 
/*     */     
/* 315 */     String hdr = getPacketHeader(event.timeStamp);
/*     */     
/* 317 */     if (this.layout == null) {
/* 318 */       packet = String.valueOf(event.getMessage());
/*     */     } else {
/* 320 */       packet = this.layout.format(event);
/*     */     } 
/* 322 */     if (this.facilityPrinting || hdr.length() > 0) {
/* 323 */       StringBuffer buf = new StringBuffer(hdr);
/* 324 */       if (this.facilityPrinting) {
/* 325 */         buf.append(this.facilityStr);
/*     */       }
/* 327 */       buf.append(packet);
/* 328 */       packet = buf.toString();
/*     */     } 
/*     */     
/* 331 */     this.sqw.setLevel(event.getLevel().getSyslogEquivalent());
/*     */ 
/*     */ 
/*     */     
/* 335 */     if (packet.length() > 256) {
/* 336 */       splitPacket(hdr, packet);
/*     */     } else {
/* 338 */       this.sqw.write(packet);
/*     */     } 
/*     */     
/* 341 */     if (this.layout == null || this.layout.ignoresThrowable()) {
/* 342 */       String[] s = event.getThrowableStrRep();
/* 343 */       if (s != null) {
/* 344 */         for (int i = 0; i < s.length; i++) {
/* 345 */           if (s[i].startsWith("\t")) {
/* 346 */             this.sqw.write(hdr + "    " + s[i].substring(1));
/*     */           } else {
/* 348 */             this.sqw.write(hdr + s[i]);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/* 361 */     if (this.header) {
/* 362 */       getLocalHostname();
/*     */     }
/* 364 */     if (this.layout != null && this.layout.getHeader() != null) {
/* 365 */       sendLayoutMessage(this.layout.getHeader());
/*     */     }
/* 367 */     this.layoutHeaderChecked = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresLayout() {
/* 377 */     return true;
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
/*     */   public void setSyslogHost(String syslogHost) {
/* 391 */     this.sqw = new SyslogQuietWriter((Writer)new SyslogWriter(syslogHost), this.syslogFacility, this.errorHandler);
/*     */ 
/*     */     
/* 394 */     this.syslogHost = syslogHost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSyslogHost() {
/* 402 */     return this.syslogHost;
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
/*     */   public void setFacility(String facilityName) {
/* 416 */     if (facilityName == null) {
/*     */       return;
/*     */     }
/* 419 */     this.syslogFacility = getFacility(facilityName);
/* 420 */     if (this.syslogFacility == -1) {
/* 421 */       System.err.println("[" + facilityName + "] is an unknown syslog facility. Defaulting to [USER].");
/*     */       
/* 423 */       this.syslogFacility = 8;
/*     */     } 
/*     */     
/* 426 */     initSyslogFacilityStr();
/*     */ 
/*     */     
/* 429 */     if (this.sqw != null) {
/* 430 */       this.sqw.setSyslogFacility(this.syslogFacility);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFacility() {
/* 439 */     return getFacilityString(this.syslogFacility);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFacilityPrinting(boolean on) {
/* 449 */     this.facilityPrinting = on;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFacilityPrinting() {
/* 457 */     return this.facilityPrinting;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean getHeader() {
/* 467 */     return this.header;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setHeader(boolean val) {
/* 476 */     this.header = val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getLocalHostname() {
/* 485 */     if (this.localHostname == null) {
/*     */       try {
/* 487 */         InetAddress addr = InetAddress.getLocalHost();
/* 488 */         this.localHostname = addr.getHostName();
/* 489 */       } catch (UnknownHostException uhe) {
/* 490 */         this.localHostname = "UNKNOWN_HOST";
/*     */       } 
/*     */     }
/* 493 */     return this.localHostname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getPacketHeader(long timeStamp) {
/* 503 */     if (this.header) {
/* 504 */       StringBuffer buf = new StringBuffer(this.dateFormat.format(new Date(timeStamp)));
/*     */       
/* 506 */       if (buf.charAt(4) == '0') {
/* 507 */         buf.setCharAt(4, ' ');
/*     */       }
/* 509 */       buf.append(getLocalHostname());
/* 510 */       buf.append(' ');
/* 511 */       return buf.toString();
/*     */     } 
/* 513 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendLayoutMessage(String msg) {
/* 521 */     if (this.sqw != null) {
/* 522 */       String packet = msg;
/* 523 */       String hdr = getPacketHeader((new Date()).getTime());
/* 524 */       if (this.facilityPrinting || hdr.length() > 0) {
/* 525 */         StringBuffer buf = new StringBuffer(hdr);
/* 526 */         if (this.facilityPrinting) {
/* 527 */           buf.append(this.facilityStr);
/*     */         }
/* 529 */         buf.append(msg);
/* 530 */         packet = buf.toString();
/*     */       } 
/* 532 */       this.sqw.setLevel(6);
/* 533 */       this.sqw.write(packet);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\net\SyslogAppender.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */