/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.google.common.io.ByteStreams;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class InetAddresses
/*     */ {
/*     */   private static final int IPV4_PART_COUNT = 4;
/*     */   private static final int IPV6_PART_COUNT = 8;
/* 104 */   private static final Splitter IPV4_SPLITTER = Splitter.on('.').limit(4);
/* 105 */   private static final Splitter IPV6_SPLITTER = Splitter.on(':').limit(10);
/* 106 */   private static final Inet4Address LOOPBACK4 = (Inet4Address)forString("127.0.0.1");
/* 107 */   private static final Inet4Address ANY4 = (Inet4Address)forString("0.0.0.0");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Inet4Address getInet4Address(byte[] bytes) {
/* 119 */     Preconditions.checkArgument((bytes.length == 4), "Byte array has invalid length for an IPv4 address: %s != 4.", bytes.length);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     return (Inet4Address)bytesToInetAddress(bytes);
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
/*     */   public static InetAddress forString(String ipString) {
/* 139 */     byte[] addr = ipStringToBytes(ipString);
/*     */ 
/*     */     
/* 142 */     if (addr == null) {
/* 143 */       throw formatIllegalArgumentException("'%s' is not an IP string literal.", new Object[] { ipString });
/*     */     }
/*     */     
/* 146 */     return bytesToInetAddress(addr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isInetAddress(String ipString) {
/* 157 */     return (ipStringToBytes(ipString) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] ipStringToBytes(String ipString) {
/* 162 */     boolean hasColon = false;
/* 163 */     boolean hasDot = false;
/* 164 */     for (int i = 0; i < ipString.length(); i++) {
/* 165 */       char c = ipString.charAt(i);
/* 166 */       if (c == '.') {
/* 167 */         hasDot = true;
/* 168 */       } else if (c == ':') {
/* 169 */         if (hasDot) {
/* 170 */           return null;
/*     */         }
/* 172 */         hasColon = true;
/* 173 */       } else if (Character.digit(c, 16) == -1) {
/* 174 */         return null;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 179 */     if (hasColon) {
/* 180 */       if (hasDot) {
/* 181 */         ipString = convertDottedQuadToHex(ipString);
/* 182 */         if (ipString == null) {
/* 183 */           return null;
/*     */         }
/*     */       } 
/* 186 */       return textToNumericFormatV6(ipString);
/* 187 */     }  if (hasDot) {
/* 188 */       return textToNumericFormatV4(ipString);
/*     */     }
/* 190 */     return null;
/*     */   }
/*     */   
/*     */   private static byte[] textToNumericFormatV4(String ipString) {
/* 194 */     byte[] bytes = new byte[4];
/* 195 */     int i = 0;
/*     */     try {
/* 197 */       for (String octet : IPV4_SPLITTER.split(ipString)) {
/* 198 */         bytes[i++] = parseOctet(octet);
/*     */       }
/* 200 */     } catch (NumberFormatException ex) {
/* 201 */       return null;
/*     */     } 
/*     */     
/* 204 */     return (i == 4) ? bytes : null;
/*     */   }
/*     */   
/*     */   private static byte[] textToNumericFormatV6(String ipString) {
/*     */     int partsHi, partsLo;
/* 209 */     List<String> parts = IPV6_SPLITTER.splitToList(ipString);
/* 210 */     if (parts.size() < 3 || parts.size() > 9) {
/* 211 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 216 */     int skipIndex = -1;
/* 217 */     for (int i = 1; i < parts.size() - 1; i++) {
/* 218 */       if (((String)parts.get(i)).length() == 0) {
/* 219 */         if (skipIndex >= 0) {
/* 220 */           return null;
/*     */         }
/* 222 */         skipIndex = i;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 228 */     if (skipIndex >= 0) {
/*     */       
/* 230 */       partsHi = skipIndex;
/* 231 */       partsLo = parts.size() - skipIndex - 1;
/* 232 */       if (((String)parts.get(0)).length() == 0 && --partsHi != 0) {
/* 233 */         return null;
/*     */       }
/* 235 */       if (((String)Iterables.getLast(parts)).length() == 0 && --partsLo != 0) {
/* 236 */         return null;
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 241 */       partsHi = parts.size();
/* 242 */       partsLo = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 247 */     int partsSkipped = 8 - partsHi + partsLo;
/* 248 */     if ((skipIndex >= 0) ? (partsSkipped >= 1) : (partsSkipped == 0)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 253 */       ByteBuffer rawBytes = ByteBuffer.allocate(16); try {
/*     */         int j;
/* 255 */         for (j = 0; j < partsHi; j++) {
/* 256 */           rawBytes.putShort(parseHextet(parts.get(j)));
/*     */         }
/* 258 */         for (j = 0; j < partsSkipped; j++) {
/* 259 */           rawBytes.putShort((short)0);
/*     */         }
/* 261 */         for (j = partsLo; j > 0; j--) {
/* 262 */           rawBytes.putShort(parseHextet(parts.get(parts.size() - j)));
/*     */         }
/* 264 */       } catch (NumberFormatException ex) {
/* 265 */         return null;
/*     */       } 
/* 267 */       return rawBytes.array();
/*     */     } 
/*     */     return null;
/*     */   } private static String convertDottedQuadToHex(String ipString) {
/* 271 */     int lastColon = ipString.lastIndexOf(':');
/* 272 */     String initialPart = ipString.substring(0, lastColon + 1);
/* 273 */     String dottedQuad = ipString.substring(lastColon + 1);
/* 274 */     byte[] quad = textToNumericFormatV4(dottedQuad);
/* 275 */     if (quad == null) {
/* 276 */       return null;
/*     */     }
/* 278 */     String penultimate = Integer.toHexString((quad[0] & 0xFF) << 8 | quad[1] & 0xFF);
/* 279 */     String ultimate = Integer.toHexString((quad[2] & 0xFF) << 8 | quad[3] & 0xFF);
/* 280 */     return initialPart + penultimate + ":" + ultimate;
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte parseOctet(String ipPart) {
/* 285 */     int octet = Integer.parseInt(ipPart);
/*     */ 
/*     */     
/* 288 */     if (octet > 255 || (ipPart.startsWith("0") && ipPart.length() > 1)) {
/* 289 */       throw new NumberFormatException();
/*     */     }
/* 291 */     return (byte)octet;
/*     */   }
/*     */ 
/*     */   
/*     */   private static short parseHextet(String ipPart) {
/* 296 */     int hextet = Integer.parseInt(ipPart, 16);
/* 297 */     if (hextet > 65535) {
/* 298 */       throw new NumberFormatException();
/*     */     }
/* 300 */     return (short)hextet;
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
/*     */   private static InetAddress bytesToInetAddress(byte[] addr) {
/*     */     try {
/* 315 */       return InetAddress.getByAddress(addr);
/* 316 */     } catch (UnknownHostException e) {
/* 317 */       throw new AssertionError(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toAddrString(InetAddress ip) {
/* 337 */     Preconditions.checkNotNull(ip);
/* 338 */     if (ip instanceof Inet4Address)
/*     */     {
/* 340 */       return ip.getHostAddress();
/*     */     }
/* 342 */     Preconditions.checkArgument(ip instanceof Inet6Address);
/* 343 */     byte[] bytes = ip.getAddress();
/* 344 */     int[] hextets = new int[8];
/* 345 */     for (int i = 0; i < hextets.length; i++) {
/* 346 */       hextets[i] = Ints.fromBytes((byte)0, (byte)0, bytes[2 * i], bytes[2 * i + 1]);
/*     */     }
/* 348 */     compressLongestRunOfZeroes(hextets);
/* 349 */     return hextetsToIPv6String(hextets);
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
/*     */   private static void compressLongestRunOfZeroes(int[] hextets) {
/* 361 */     int bestRunStart = -1;
/* 362 */     int bestRunLength = -1;
/* 363 */     int runStart = -1;
/* 364 */     for (int i = 0; i < hextets.length + 1; i++) {
/* 365 */       if (i < hextets.length && hextets[i] == 0) {
/* 366 */         if (runStart < 0) {
/* 367 */           runStart = i;
/*     */         }
/* 369 */       } else if (runStart >= 0) {
/* 370 */         int runLength = i - runStart;
/* 371 */         if (runLength > bestRunLength) {
/* 372 */           bestRunStart = runStart;
/* 373 */           bestRunLength = runLength;
/*     */         } 
/* 375 */         runStart = -1;
/*     */       } 
/*     */     } 
/* 378 */     if (bestRunLength >= 2) {
/* 379 */       Arrays.fill(hextets, bestRunStart, bestRunStart + bestRunLength, -1);
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
/*     */ 
/*     */   
/*     */   private static String hextetsToIPv6String(int[] hextets) {
/* 396 */     StringBuilder buf = new StringBuilder(39);
/* 397 */     boolean lastWasNumber = false;
/* 398 */     for (int i = 0; i < hextets.length; i++) {
/* 399 */       boolean thisIsNumber = (hextets[i] >= 0);
/* 400 */       if (thisIsNumber) {
/* 401 */         if (lastWasNumber) {
/* 402 */           buf.append(':');
/*     */         }
/* 404 */         buf.append(Integer.toHexString(hextets[i]));
/*     */       }
/* 406 */       else if (i == 0 || lastWasNumber) {
/* 407 */         buf.append("::");
/*     */       } 
/*     */       
/* 410 */       lastWasNumber = thisIsNumber;
/*     */     } 
/* 412 */     return buf.toString();
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
/*     */   public static String toUriString(InetAddress ip) {
/* 435 */     if (ip instanceof Inet6Address) {
/* 436 */       return "[" + toAddrString(ip) + "]";
/*     */     }
/* 438 */     return toAddrString(ip);
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
/*     */   public static InetAddress forUriString(String hostAddr) {
/* 456 */     InetAddress addr = forUriStringNoThrow(hostAddr);
/* 457 */     if (addr == null) {
/* 458 */       throw formatIllegalArgumentException("Not a valid URI IP literal: '%s'", new Object[] { hostAddr });
/*     */     }
/*     */     
/* 461 */     return addr;
/*     */   } private static InetAddress forUriStringNoThrow(String hostAddr) {
/*     */     String ipString;
/*     */     int expectBytes;
/* 465 */     Preconditions.checkNotNull(hostAddr);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 470 */     if (hostAddr.startsWith("[") && hostAddr.endsWith("]")) {
/* 471 */       ipString = hostAddr.substring(1, hostAddr.length() - 1);
/* 472 */       expectBytes = 16;
/*     */     } else {
/* 474 */       ipString = hostAddr;
/* 475 */       expectBytes = 4;
/*     */     } 
/*     */ 
/*     */     
/* 479 */     byte[] addr = ipStringToBytes(ipString);
/* 480 */     if (addr == null || addr.length != expectBytes) {
/* 481 */       return null;
/*     */     }
/*     */     
/* 484 */     return bytesToInetAddress(addr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isUriInetAddress(String ipString) {
/* 495 */     return (forUriStringNoThrow(ipString) != null);
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
/*     */   public static boolean isCompatIPv4Address(Inet6Address ip) {
/* 517 */     if (!ip.isIPv4CompatibleAddress()) {
/* 518 */       return false;
/*     */     }
/*     */     
/* 521 */     byte[] bytes = ip.getAddress();
/* 522 */     if (bytes[12] == 0 && bytes[13] == 0 && bytes[14] == 0 && (bytes[15] == 0 || bytes[15] == 1))
/*     */     {
/*     */ 
/*     */       
/* 526 */       return false;
/*     */     }
/*     */     
/* 529 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Inet4Address getCompatIPv4Address(Inet6Address ip) {
/* 540 */     Preconditions.checkArgument(
/* 541 */         isCompatIPv4Address(ip), "Address '%s' is not IPv4-compatible.", toAddrString(ip));
/*     */     
/* 543 */     return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 12, 16));
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
/*     */   public static boolean is6to4Address(Inet6Address ip) {
/* 559 */     byte[] bytes = ip.getAddress();
/* 560 */     return (bytes[0] == 32 && bytes[1] == 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Inet4Address get6to4IPv4Address(Inet6Address ip) {
/* 571 */     Preconditions.checkArgument(is6to4Address(ip), "Address '%s' is not a 6to4 address.", toAddrString(ip));
/*     */     
/* 573 */     return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 2, 6));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static final class TeredoInfo
/*     */   {
/*     */     private final Inet4Address server;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Inet4Address client;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int port;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int flags;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TeredoInfo(Inet4Address server, Inet4Address client, int port, int flags) {
/* 608 */       Preconditions.checkArgument((port >= 0 && port <= 65535), "port '%s' is out of range (0 <= port <= 0xffff)", port);
/*     */       
/* 610 */       Preconditions.checkArgument((flags >= 0 && flags <= 65535), "flags '%s' is out of range (0 <= flags <= 0xffff)", flags);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 615 */       this.server = (Inet4Address)MoreObjects.firstNonNull(server, InetAddresses.ANY4);
/* 616 */       this.client = (Inet4Address)MoreObjects.firstNonNull(client, InetAddresses.ANY4);
/* 617 */       this.port = port;
/* 618 */       this.flags = flags;
/*     */     }
/*     */     
/*     */     public Inet4Address getServer() {
/* 622 */       return this.server;
/*     */     }
/*     */     
/*     */     public Inet4Address getClient() {
/* 626 */       return this.client;
/*     */     }
/*     */     
/*     */     public int getPort() {
/* 630 */       return this.port;
/*     */     }
/*     */     
/*     */     public int getFlags() {
/* 634 */       return this.flags;
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
/*     */   public static boolean isTeredoAddress(Inet6Address ip) {
/* 647 */     byte[] bytes = ip.getAddress();
/* 648 */     return (bytes[0] == 32 && bytes[1] == 1 && bytes[2] == 0 && bytes[3] == 0);
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
/*     */   public static TeredoInfo getTeredoInfo(Inet6Address ip) {
/* 662 */     Preconditions.checkArgument(isTeredoAddress(ip), "Address '%s' is not a Teredo address.", toAddrString(ip));
/*     */     
/* 664 */     byte[] bytes = ip.getAddress();
/* 665 */     Inet4Address server = getInet4Address(Arrays.copyOfRange(bytes, 4, 8));
/*     */     
/* 667 */     int flags = ByteStreams.newDataInput(bytes, 8).readShort() & 0xFFFF;
/*     */ 
/*     */     
/* 670 */     int port = (ByteStreams.newDataInput(bytes, 10).readShort() ^ 0xFFFFFFFF) & 0xFFFF;
/*     */     
/* 672 */     byte[] clientBytes = Arrays.copyOfRange(bytes, 12, 16);
/* 673 */     for (int i = 0; i < clientBytes.length; i++)
/*     */     {
/* 675 */       clientBytes[i] = (byte)(clientBytes[i] ^ 0xFFFFFFFF);
/*     */     }
/* 677 */     Inet4Address client = getInet4Address(clientBytes);
/*     */     
/* 679 */     return new TeredoInfo(server, client, port, flags);
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
/*     */   public static boolean isIsatapAddress(Inet6Address ip) {
/* 699 */     if (isTeredoAddress(ip)) {
/* 700 */       return false;
/*     */     }
/*     */     
/* 703 */     byte[] bytes = ip.getAddress();
/*     */     
/* 705 */     if ((bytes[8] | 0x3) != 3)
/*     */     {
/*     */ 
/*     */       
/* 709 */       return false;
/*     */     }
/*     */     
/* 712 */     return (bytes[9] == 0 && bytes[10] == 94 && bytes[11] == -2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Inet4Address getIsatapIPv4Address(Inet6Address ip) {
/* 723 */     Preconditions.checkArgument(isIsatapAddress(ip), "Address '%s' is not an ISATAP address.", toAddrString(ip));
/*     */     
/* 725 */     return getInet4Address(Arrays.copyOfRange(ip.getAddress(), 12, 16));
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
/*     */   public static boolean hasEmbeddedIPv4ClientAddress(Inet6Address ip) {
/* 741 */     return (isCompatIPv4Address(ip) || is6to4Address(ip) || isTeredoAddress(ip));
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
/*     */   public static Inet4Address getEmbeddedIPv4ClientAddress(Inet6Address ip) {
/* 757 */     if (isCompatIPv4Address(ip)) {
/* 758 */       return getCompatIPv4Address(ip);
/*     */     }
/*     */     
/* 761 */     if (is6to4Address(ip)) {
/* 762 */       return get6to4IPv4Address(ip);
/*     */     }
/*     */     
/* 765 */     if (isTeredoAddress(ip)) {
/* 766 */       return getTeredoInfo(ip).getClient();
/*     */     }
/*     */     
/* 769 */     throw formatIllegalArgumentException("'%s' has no embedded IPv4 address.", new Object[] { toAddrString(ip) });
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
/*     */   public static boolean isMappedIPv4Address(String ipString) {
/* 791 */     byte[] bytes = ipStringToBytes(ipString);
/* 792 */     if (bytes != null && bytes.length == 16) {
/* 793 */       int i; for (i = 0; i < 10; i++) {
/* 794 */         if (bytes[i] != 0) {
/* 795 */           return false;
/*     */         }
/*     */       } 
/* 798 */       for (i = 10; i < 12; i++) {
/* 799 */         if (bytes[i] != -1) {
/* 800 */           return false;
/*     */         }
/*     */       } 
/* 803 */       return true;
/*     */     } 
/* 805 */     return false;
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
/*     */   public static Inet4Address getCoercedIPv4Address(InetAddress ip) {
/* 827 */     if (ip instanceof Inet4Address) {
/* 828 */       return (Inet4Address)ip;
/*     */     }
/*     */ 
/*     */     
/* 832 */     byte[] bytes = ip.getAddress();
/* 833 */     boolean leadingBytesOfZero = true;
/* 834 */     for (int i = 0; i < 15; i++) {
/* 835 */       if (bytes[i] != 0) {
/* 836 */         leadingBytesOfZero = false;
/*     */         break;
/*     */       } 
/*     */     } 
/* 840 */     if (leadingBytesOfZero && bytes[15] == 1)
/* 841 */       return LOOPBACK4; 
/* 842 */     if (leadingBytesOfZero && bytes[15] == 0) {
/* 843 */       return ANY4;
/*     */     }
/*     */     
/* 846 */     Inet6Address ip6 = (Inet6Address)ip;
/* 847 */     long addressAsLong = 0L;
/* 848 */     if (hasEmbeddedIPv4ClientAddress(ip6)) {
/* 849 */       addressAsLong = getEmbeddedIPv4ClientAddress(ip6).hashCode();
/*     */     }
/*     */     else {
/*     */       
/* 853 */       addressAsLong = ByteBuffer.wrap(ip6.getAddress(), 0, 8).getLong();
/*     */     } 
/*     */ 
/*     */     
/* 857 */     int coercedHash = Hashing.murmur3_32().hashLong(addressAsLong).asInt();
/*     */ 
/*     */     
/* 860 */     coercedHash |= 0xE0000000;
/*     */ 
/*     */ 
/*     */     
/* 864 */     if (coercedHash == -1) {
/* 865 */       coercedHash = -2;
/*     */     }
/*     */     
/* 868 */     return getInet4Address(Ints.toByteArray(coercedHash));
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
/*     */   public static int coerceToInteger(InetAddress ip) {
/* 890 */     return ByteStreams.newDataInput(getCoercedIPv4Address(ip).getAddress()).readInt();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Inet4Address fromInteger(int address) {
/* 900 */     return getInet4Address(Ints.toByteArray(address));
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
/*     */   public static InetAddress fromLittleEndianByteArray(byte[] addr) throws UnknownHostException {
/* 914 */     byte[] reversed = new byte[addr.length];
/* 915 */     for (int i = 0; i < addr.length; i++) {
/* 916 */       reversed[i] = addr[addr.length - i - 1];
/*     */     }
/* 918 */     return InetAddress.getByAddress(reversed);
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
/*     */   public static InetAddress decrement(InetAddress address) {
/* 931 */     byte[] addr = address.getAddress();
/* 932 */     int i = addr.length - 1;
/* 933 */     while (i >= 0 && addr[i] == 0) {
/* 934 */       addr[i] = -1;
/* 935 */       i--;
/*     */     } 
/*     */     
/* 938 */     Preconditions.checkArgument((i >= 0), "Decrementing %s would wrap.", address);
/*     */     
/* 940 */     addr[i] = (byte)(addr[i] - 1);
/* 941 */     return bytesToInetAddress(addr);
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
/*     */   public static InetAddress increment(InetAddress address) {
/* 954 */     byte[] addr = address.getAddress();
/* 955 */     int i = addr.length - 1;
/* 956 */     while (i >= 0 && addr[i] == -1) {
/* 957 */       addr[i] = 0;
/* 958 */       i--;
/*     */     } 
/*     */     
/* 961 */     Preconditions.checkArgument((i >= 0), "Incrementing %s would wrap.", address);
/*     */     
/* 963 */     addr[i] = (byte)(addr[i] + 1);
/* 964 */     return bytesToInetAddress(addr);
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
/*     */   public static boolean isMaximum(InetAddress address) {
/* 976 */     byte[] addr = address.getAddress();
/* 977 */     for (int i = 0; i < addr.length; i++) {
/* 978 */       if (addr[i] != -1) {
/* 979 */         return false;
/*     */       }
/*     */     } 
/* 982 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static IllegalArgumentException formatIllegalArgumentException(String format, Object... args) {
/* 987 */     return new IllegalArgumentException(String.format(Locale.ROOT, format, args));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\net\InetAddresses.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */