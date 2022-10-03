package org.apache.log4j.rewrite;

import org.apache.log4j.spi.LoggingEvent;

public interface RewritePolicy {
  LoggingEvent rewrite(LoggingEvent paramLoggingEvent);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\rewrite\RewritePolicy.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */