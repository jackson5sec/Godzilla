/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class HostAndPort
/*     */   implements Serializable
/*     */ {
/*     */   private static final int NO_PORT = -1;
/*     */   private final String host;
/*     */   private final int port;
/*     */   private final boolean hasBracketlessColons;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private HostAndPort(String host, int port, boolean hasBracketlessColons) {
/*  79 */     this.host = host;
/*  80 */     this.port = port;
/*  81 */     this.hasBracketlessColons = hasBracketlessColons;
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
/*     */   public String getHost() {
/*  94 */     return this.host;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasPort() {
/*  99 */     return (this.port >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 110 */     Preconditions.checkState(hasPort());
/* 111 */     return this.port;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPortOrDefault(int defaultPort) {
/* 116 */     return hasPort() ? this.port : defaultPort;
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
/*     */   public static HostAndPort fromParts(String host, int port) {
/* 132 */     Preconditions.checkArgument(isValidPort(port), "Port out of range: %s", port);
/* 133 */     HostAndPort parsedHost = fromString(host);
/* 134 */     Preconditions.checkArgument(!parsedHost.hasPort(), "Host has a port: %s", host);
/* 135 */     return new HostAndPort(parsedHost.host, port, parsedHost.hasBracketlessColons);
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
/*     */   public static HostAndPort fromHost(String host) {
/* 150 */     HostAndPort parsedHost = fromString(host);
/* 151 */     Preconditions.checkArgument(!parsedHost.hasPort(), "Host has a port: %s", host);
/* 152 */     return parsedHost;
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
/*     */   public static HostAndPort fromString(String hostPortString) {
/*     */     String host;
/* 166 */     Preconditions.checkNotNull(hostPortString);
/*     */     
/* 168 */     String portString = null;
/* 169 */     boolean hasBracketlessColons = false;
/*     */     
/* 171 */     if (hostPortString.startsWith("[")) {
/* 172 */       String[] hostAndPort = getHostAndPortFromBracketedHost(hostPortString);
/* 173 */       host = hostAndPort[0];
/* 174 */       portString = hostAndPort[1];
/*     */     } else {
/* 176 */       int colonPos = hostPortString.indexOf(':');
/* 177 */       if (colonPos >= 0 && hostPortString.indexOf(':', colonPos + 1) == -1) {
/*     */         
/* 179 */         host = hostPortString.substring(0, colonPos);
/* 180 */         portString = hostPortString.substring(colonPos + 1);
/*     */       } else {
/*     */         
/* 183 */         host = hostPortString;
/* 184 */         hasBracketlessColons = (colonPos >= 0);
/*     */       } 
/*     */     } 
/*     */     
/* 188 */     int port = -1;
/* 189 */     if (!Strings.isNullOrEmpty(portString)) {
/*     */ 
/*     */       
/* 192 */       Preconditions.checkArgument(!portString.startsWith("+"), "Unparseable port number: %s", hostPortString);
/*     */       try {
/* 194 */         port = Integer.parseInt(portString);
/* 195 */       } catch (NumberFormatException e) {
/* 196 */         throw new IllegalArgumentException("Unparseable port number: " + hostPortString);
/*     */       } 
/* 198 */       Preconditions.checkArgument(isValidPort(port), "Port number out of range: %s", hostPortString);
/*     */     } 
/*     */     
/* 201 */     return new HostAndPort(host, port, hasBracketlessColons);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] getHostAndPortFromBracketedHost(String hostPortString) {
/* 212 */     int colonIndex = 0;
/* 213 */     int closeBracketIndex = 0;
/* 214 */     Preconditions.checkArgument(
/* 215 */         (hostPortString.charAt(0) == '['), "Bracketed host-port string must start with a bracket: %s", hostPortString);
/*     */ 
/*     */     
/* 218 */     colonIndex = hostPortString.indexOf(':');
/* 219 */     closeBracketIndex = hostPortString.lastIndexOf(']');
/* 220 */     Preconditions.checkArgument((colonIndex > -1 && closeBracketIndex > colonIndex), "Invalid bracketed host/port: %s", hostPortString);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     String host = hostPortString.substring(1, closeBracketIndex);
/* 226 */     if (closeBracketIndex + 1 == hostPortString.length()) {
/* 227 */       return new String[] { host, "" };
/*     */     }
/* 229 */     Preconditions.checkArgument(
/* 230 */         (hostPortString.charAt(closeBracketIndex + 1) == ':'), "Only a colon may follow a close bracket: %s", hostPortString);
/*     */ 
/*     */     
/* 233 */     for (int i = closeBracketIndex + 2; i < hostPortString.length(); i++) {
/* 234 */       Preconditions.checkArgument(
/* 235 */           Character.isDigit(hostPortString.charAt(i)), "Port must be numeric: %s", hostPortString);
/*     */     }
/*     */ 
/*     */     
/* 239 */     return new String[] { host, hostPortString.substring(closeBracketIndex + 2) };
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
/*     */   public HostAndPort withDefaultPort(int defaultPort) {
/* 253 */     Preconditions.checkArgument(isValidPort(defaultPort));
/* 254 */     if (hasPort()) {
/* 255 */       return this;
/*     */     }
/* 257 */     return new HostAndPort(this.host, defaultPort, this.hasBracketlessColons);
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
/*     */   public HostAndPort requireBracketsForIPv6() {
/* 275 */     Preconditions.checkArgument(!this.hasBracketlessColons, "Possible bracketless IPv6 literal: %s", this.host);
/* 276 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 281 */     if (this == other) {
/* 282 */       return true;
/*     */     }
/* 284 */     if (other instanceof HostAndPort) {
/* 285 */       HostAndPort that = (HostAndPort)other;
/* 286 */       return (Objects.equal(this.host, that.host) && this.port == that.port);
/*     */     } 
/* 288 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 293 */     return Objects.hashCode(new Object[] { this.host, Integer.valueOf(this.port) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 300 */     StringBuilder builder = new StringBuilder(this.host.length() + 8);
/* 301 */     if (this.host.indexOf(':') >= 0) {
/* 302 */       builder.append('[').append(this.host).append(']');
/*     */     } else {
/* 304 */       builder.append(this.host);
/*     */     } 
/* 306 */     if (hasPort()) {
/* 307 */       builder.append(':').append(this.port);
/*     */     }
/* 309 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isValidPort(int port) {
/* 314 */     return (port >= 0 && port <= 65535);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\net\HostAndPort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */