
#if (defined(_WIN32) || defined(_WIN64)) && !defined(__WINE__)
#include <dir.h>
#include <dirent.h>
#include <direct.h>
#include <mem.h>
#else
#include <sys/stat.h>
#include <sys/types.h>
#include <dirent.h>
#include <unistd.h>
#include <string.h>
#endif
#include "tda_unicode.h"


// the following #defines will allow us to write: 6 CHARs or 2 WCHARs
#define CHARs *sizeof(CHAR)
#define WCHARs *sizeof(WCHAR)
#define LCHARs *sizeof(LCHAR)

#define ENCODING_KEEP_CURRENT 0
#define ENCODING_STANDARD_UTF8 1
#define ENCODING_MODIFIED_UTF8 2
#define ENCODING_UTF16 3
#define ENCODING_UTF32 4

/* UTF8Processor.
   Parameters:
     source - a pointer to a standard or modified UTF-8 string (null-terminated or not)
     utf8lengthInUnits - 0 to use a terminating '\0' to determine the length of the source string,
              or non-zero value to use it as a length (in UTF-8 units = bytes) despite of a possible
              presence of '\0' in the string
     target - NULL, if only the size of the result is needed;
              not-NULL - a buffer for the result
     targetSizeInBytes - the size of the target buffer in bytes or an upper bound
              for the target size if target == NULL;
              we will return the result, which is <= targetSizeInBytes
              (errno may be set to ERANGE, if targetSizeInBytes is not sufficient);
              if (target != NULL) we will write no more than targetSizeInBytes bytes into
              the target buffer;
     targetEncoding - one of the ENCODING_XXX constants from above
     writeTerminatingNull - a boolean (0 or 1) specifying whether we need to write
              a terminating '\0' unit into the target buffer
              (only when target != NULL and targetSizeInBytes
              is not less than a space needed for one target unit)

   Returns: the size of the target result in bytes
 */
size_t UTF8Processor(const void* source, size_t utf8lengthInUnits, void* target, size_t targetSizeInBytes, int targetEncoding, int writeTerminatingNull)
{
  size_t sourceUnitsDone = 0;
  size_t targetBytesDone = 0;

  CHAR* s = (CHAR*)source;
  errno = 0;

  if (s == NULL) {
    errno = EDOM;
    return 0;
  }

  if ((targetEncoding < 0) || (targetEncoding > ENCODING_UTF32)) {
    errno = EDOM;
    return 0;
  }

  if (writeTerminatingNull) {
    size_t bytesPerUnit;
    switch (targetEncoding) {
    case ENCODING_KEEP_CURRENT:
    case ENCODING_STANDARD_UTF8:
    case ENCODING_MODIFIED_UTF8:
      bytesPerUnit = sizeof(CHAR);
      break;
    case ENCODING_UTF16:
      bytesPerUnit = sizeof(WCHAR);
      break;
    case ENCODING_UTF32:
      bytesPerUnit = sizeof(LCHAR);
      break;
    }

    if (targetSizeInBytes >= bytesPerUnit)
      targetSizeInBytes -= bytesPerUnit; // reserving space for the terminating '\0' unit
    else {
      errno = ERANGE;
      targetSizeInBytes = 0;
      writeTerminatingNull = 0;
    }
  }

  for (;;) {
    // checking when to stop...
    if (utf8lengthInUnits == 0) {
      if (*s == '\0')
        break; // stop, if the source length is not given, but '\0' reached
    }
    else {
      if (sourceUnitsDone >= utf8lengthInUnits)
        break; // stop, if the source length is reached
    }

    if ( ((*s) & 0xF8) == 0xF9 ) { // [1111][1???] --- start of a 5 or more-byte UTF-8 sequence...
      errno = EILSEQ; // we do not support this...
      // go to the default action...
    }
    else
    if (*s == (CHAR)0xED) { // --- start of a 6 byte modified UTF-8 sequence
                            // in the form: ED A? [10??][????] ED B? [10??][????]
      if ( ((utf8lengthInUnits==0)||(utf8lengthInUnits-sourceUnitsDone>=6))
        // such kind of ">=" should not cause overflow since
        // we preserve the invariant: utf8lengthInUnits >=sourceUnitsDone (if utf8lengthInUnits was not 0)
        && ((s[1] & (CHAR)0xF0)==(CHAR)0xA0)
        && ((s[2] & (CHAR)0xC0)==(CHAR)0x80)
        && ( s[3]              ==(CHAR)0xED)
        && ((s[4] & (CHAR)0xF0)==(CHAR)0xB0)
        && ((s[5] & (CHAR)0xC0)==(CHAR)0x80)
      ) {
        switch (targetEncoding) {
        case ENCODING_KEEP_CURRENT:
        case ENCODING_MODIFIED_UTF8:
          if (targetSizeInBytes-targetBytesDone < 6 CHARs) {
          // such kind of "<" should not cause overflow, since we preserve
          // the invariant: targetSizeInBytes >= targetBytesDone
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            memcpy(target, s, 6 CHARs);
            target = ((CHAR*)target)+6;
          }
          targetBytesDone += 6 CHARs;
          break;
        case ENCODING_STANDARD_UTF8:
          if (targetSizeInBytes-targetBytesDone < 4 CHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            LCHAR codePoint =
              ((LCHAR)(s[1] & 0x0F) << 16) |
              ((LCHAR)(s[2] & 0x3F) << 10) |
              ((LCHAR)(s[4] & 0x0F) << 6) |
               (LCHAR)(s[5] & 0x3F);
            codePoint += (LCHAR)0x10000;
                 // 21 bits total
            if (codePoint > 0x10FFFF)
              errno = EILSEQ;
            ((CHAR*)target)[0] = 0xF0 | (CHAR)(codePoint>>18);
            ((CHAR*)target)[1] = 0x80 | (CHAR)( (codePoint>>12)&0x3F );
            ((CHAR*)target)[2] = 0x80 | (CHAR)( (codePoint>>6)&0x3F );
            ((CHAR*)target)[3] = 0x80 | (CHAR)( codePoint&0x3F );
            target = ((CHAR*)target)+4;
          }
          targetBytesDone += 4 CHARs;
          break;
        case ENCODING_UTF16:
          if (targetSizeInBytes-targetBytesDone < 2 WCHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            LCHAR codePoint = // without adding 0x10000
              ((LCHAR)(s[1] & 0x0F) << 16) |
              ((LCHAR)(s[2] & 0x3F) << 10) |
              ((LCHAR)(s[4] & 0x0F) << 6) |
               (LCHAR)(s[5] & 0x3F);
                 // 20 (!) bits total
            ((WCHAR*)target)[0] = 0xD800 | (codePoint >> 10);
            ((WCHAR*)target)[1] = 0xDC00 | (codePoint & 0x3FF);
            target = ((WCHAR*)target)+2;
          }
          targetBytesDone += 2 WCHARs;
          break;
        case ENCODING_UTF32:
          if (targetSizeInBytes-targetBytesDone < 1 LCHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            LCHAR codePoint =
              ((LCHAR)(s[1] & 0x0F) << 16) |
              ((LCHAR)(s[2] & 0x3F) << 10) |
              ((LCHAR)(s[4] & 0x0F) << 6) |
               (LCHAR)(s[5] & 0x3F);
            codePoint += (LCHAR)0x10000;
                 // 21 bits total
            if (codePoint > 0x10FFFF)
              errno = EILSEQ;
            *((LCHAR*)target) = codePoint;
            target = ((LCHAR*)target)+1;
          }
          targetBytesDone += 1 LCHARs;
          break;
        }

        sourceUnitsDone+=6;
        s+=6;
        continue; // do not go to the default action...
      }
      else {
        errno = EILSEQ;
        // go to the default action...
      }
    }
    else
    if ( (s[0] & 0xF8) == 0xF0 ) { // [1111][0???] --- start of a 4-byte UTF-8 sequence...
      if ( ((utf8lengthInUnits==0)||(utf8lengthInUnits-sourceUnitsDone>=4))
        && ((s[1] & 0xC0) == 0x80 )
        && ((s[2] & 0xC0) == 0x80 )
        && ((s[3] & 0xC0) == 0x80 )
      ) {
        switch (targetEncoding) {
        case ENCODING_KEEP_CURRENT:
        case ENCODING_STANDARD_UTF8:
          if (targetSizeInBytes-targetBytesDone < 4 CHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            memcpy(target, s, 4 CHARs);
            target = ((CHAR*)target)+4;
          }
          targetBytesDone += 4 CHARs;
          break;
        case ENCODING_MODIFIED_UTF8:
          if (targetSizeInBytes-targetBytesDone < 6 CHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            LCHAR codePoint =
              ((LCHAR)(s[0] & 0x07) << 18) |
              ((LCHAR)(s[1] & 0x3F) << 12) |
              ((LCHAR)(s[2] & 0x3F) << 6) |
               (LCHAR)(s[3] & 0x3F);
                 // 21 bits total
            if ((codePoint < 0x10000) || (codePoint > 0x10FFFF)) {
              errno = EILSEQ;
              goto DEFAULT_ACTION;
            }

            codePoint -= 0x10000; // subtracting 0x10000; 20 bits remaining
            ((CHAR*)target)[0] = (CHAR)0xED;
            ((CHAR*)target)[1] = (CHAR)0xA0 | (CHAR)( (codePoint>>16)&0x0F );
            ((CHAR*)target)[2] = (CHAR)0x80 | (CHAR)( (codePoint>>10)&0x3F );
            ((CHAR*)target)[3] = (CHAR)0xED;
            ((CHAR*)target)[4] = (CHAR)0xB0 | (CHAR)( (codePoint>>6)&0x0F );
            ((CHAR*)target)[5] = (CHAR)0x80 | (CHAR)( codePoint&0x3F );
            target = ((CHAR*)target)+6;
          }
          targetBytesDone += 6 CHARs;
          break;
        case ENCODING_UTF16:
          if (targetSizeInBytes-targetBytesDone < 2 WCHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            LCHAR codePoint =
              ((LCHAR)(s[0] & 0x07) << 18) |
              ((LCHAR)(s[1] & 0x3F) << 12) |
              ((LCHAR)(s[2] & 0x3F) << 6) |
               (LCHAR)(s[3] & 0x3F);
                 // 21 bits total
            if ((codePoint < 0x10000) || (codePoint > 0x10FFFF)) {
              errno = EILSEQ;
              goto DEFAULT_ACTION;
            }

            codePoint -= 0x10000; // subtracting 0x10000; 20 bits remaining
            ((WCHAR*)target)[0] = 0xD800 | (codePoint >> 10);
            ((WCHAR*)target)[1] = 0xDC00 | (codePoint & 0x3FF);
            target = ((WCHAR*)target)+2;
          }
          targetBytesDone += 2 WCHARs;
          break;
        case ENCODING_UTF32:
          if (targetSizeInBytes-targetBytesDone < 1 LCHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            LCHAR codePoint =
              ((LCHAR)(s[0] & 0x07) << 18) |
              ((LCHAR)(s[1] & 0x3F) << 12) |
              ((LCHAR)(s[2] & 0x3F) << 6) |
               (LCHAR)(s[3] & 0x3F);
                 // 21 bits total
            if (codePoint > 0x10FFFF) 
              errno = EILSEQ;
            *((LCHAR*)target) = codePoint;
            target = ((LCHAR*)target)+1;
          }
          targetBytesDone += 1 LCHARs;
          break;
        }

        sourceUnitsDone+=4;
        s+=4;
        continue; // do not go to the default action...
      }
      else {
        errno = EILSEQ;
        // go to the default action...
      }
    }
    else
    if ( (s[0] & 0xF0) == 0xE0 ) { // [1110][????] --- start of a 3-byte UTF-8 sequence...
      if ( ((utf8lengthInUnits==0)||(utf8lengthInUnits-sourceUnitsDone>=3))
        && ((s[1] & 0xC0) == 0x80 )
        && ((s[2] & 0xC0) == 0x80 )
      ) {
        switch (targetEncoding) {
        case ENCODING_KEEP_CURRENT:
        case ENCODING_STANDARD_UTF8:
        case ENCODING_MODIFIED_UTF8:
          if (targetSizeInBytes-targetBytesDone < 3 CHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            memcpy(target, s, 3 CHARs);
            target = ((CHAR*)target)+3;
          }
          targetBytesDone += 3 CHARs;
          break;
        case ENCODING_UTF16:
          if (targetSizeInBytes-targetBytesDone < 1 WCHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            WCHAR codePoint =
              ((WCHAR)(s[0] & 0x0F) << 12) |
              ((WCHAR)(s[1] & 0x3F) << 6) |
               (WCHAR)(s[2] & 0x3F);
                 // 16 bits total
            *((WCHAR*)target) = codePoint;
            target = ((WCHAR*)target)+1;
          }
          targetBytesDone += 1 WCHARs;
          break;
        case ENCODING_UTF32:
          if (targetSizeInBytes-targetBytesDone < 1 LCHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            LCHAR codePoint =
              ((LCHAR)(s[0] & 0x0F) << 12) |
              ((LCHAR)(s[1] & 0x3F) << 6) |
               (LCHAR)(s[2] & 0x3F);
                 // 16 bits total
            *((LCHAR*)target) = codePoint;
            target = ((LCHAR*)target)+1;
          }
          targetBytesDone += 1 LCHARs;
          break;
        }

        sourceUnitsDone+=3;
        s+=3;
        continue; // do not go to the default action...
      }
      else {
        errno = EILSEQ;
        // go to the default action...
      }
    }
    else
    if ( (s[0] & 0xE0) == 0xC0 ) { // [110?][????] --- start of a 2-byte UTF-8 sequence...
      if ( ((utf8lengthInUnits==0)||(utf8lengthInUnits-sourceUnitsDone>=2))
        && ((s[1] & 0xC0) == 0x80)
      ) {
        switch (targetEncoding) {
        case ENCODING_STANDARD_UTF8:
          if ((s[0] == 0xC0) && (s[1] == 0x80)) {
            if (targetSizeInBytes-targetBytesDone < 1 CHARs) {
              errno = ERANGE;
              goto FINALIZE;
            }
            // '\0' in standard UTF-8 will take one byte...
            if (target) {
              *((CHAR*)target) = '\0';
              target = ((CHAR*)target)+1;
            }
            targetBytesDone += 1 CHARs;
          }
          else {
            if (targetSizeInBytes-targetBytesDone < 2 CHARs) {
              errno = ERANGE;
              goto FINALIZE;
            }
            if (target) {
              memcpy(target, s, 2 CHARs);
              target = ((CHAR*)target)+2;
            }
            targetBytesDone += 2 CHARs;
          }
          break;
        case ENCODING_MODIFIED_UTF8:
        case ENCODING_KEEP_CURRENT:
          if (targetSizeInBytes-targetBytesDone < 2 CHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            memcpy(target, s, 2 CHARs);
            target = ((CHAR*)target)+2;
          }
          targetBytesDone += 2 CHARs;
          break;
        case ENCODING_UTF16:
          if (targetSizeInBytes-targetBytesDone < 1 WCHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            WCHAR codePoint =
              ((WCHAR)(s[0] & 0x1F) << 6) |
               (WCHAR)(s[1] & 0x3F);
                 // 11 bits total
            *((WCHAR*)target) = codePoint;
            target = ((WCHAR*)target)+1;
          }
          targetBytesDone += 1 WCHARs;
          break;
        case ENCODING_UTF32:
          if (targetSizeInBytes-targetBytesDone < 1 LCHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            LCHAR codePoint =
              ((LCHAR)(s[0] & 0x1F) << 6) |
               (LCHAR)(s[1] & 0x3F);
                 // 11 bits total
            *((LCHAR*)target) = codePoint;
            target = ((LCHAR*)target)+1;
          }
          targetBytesDone += 1 LCHARs;
          break;
        }

        sourceUnitsDone+=2;
        s+=2;
        continue; // do not go to the default action...
      }
      else {
        errno = EILSEQ;
        // go to the default action...
      }
    }

    // the default action:
    DEFAULT_ACTION:
    switch (targetEncoding) {
    case ENCODING_KEEP_CURRENT:
    case ENCODING_STANDARD_UTF8:
      if (targetSizeInBytes-targetBytesDone < 1 CHARs) {
        errno = ERANGE;
        goto FINALIZE;
      }
      if (target) {
        *((CHAR*)target) = *s;
        target = ((CHAR*)target)+1;
      }
      targetBytesDone += 1 CHARs;
      break;
    case ENCODING_MODIFIED_UTF8:
      if (*s == '\0') { // null unit
        if (targetSizeInBytes-targetBytesDone < 2 CHARs) {
          errno = ERANGE;
          goto FINALIZE;
        }
        if (target) {
          // representing the null in UTF-8 unit as two units in modified UTF-8...
          ((CHAR*)target)[0] = 0xC0;
          ((CHAR*)target)[1] = 0x80;
          target = ((CHAR*)target)+2;
        }
        targetBytesDone += 2 CHARs;
      }
      else { // not-null unit
        if (targetSizeInBytes-targetBytesDone < 1 CHARs) {
          errno = ERANGE;
          goto FINALIZE;
        }
        if (target) {
          *((CHAR*)target) = *s;
          target = ((CHAR*)target)+1;
        }
        targetBytesDone += 1 CHARs;
      }
      break;
    case ENCODING_UTF16:
      if (targetSizeInBytes-targetBytesDone < 1 WCHARs) {
        errno = ERANGE;
        goto FINALIZE;
      }
      if (target) {
        *((WCHAR*)target) = *s;
        target = ((WCHAR*)target)+1;
      }
      targetBytesDone += 1 WCHARs;
      break;
    case ENCODING_UTF32:
      if (targetSizeInBytes-targetBytesDone < 1 LCHARs) {
        errno = ERANGE;
        goto FINALIZE;
      }
      if (target) {
        *((LCHAR*)target) = *s;
        target = ((LCHAR*)target)+1;
      }
      targetBytesDone += 1 LCHARs;
      break;
    }

    sourceUnitsDone++;
    s++;

  }

FINALIZE:

  if (writeTerminatingNull) {
    switch (targetEncoding)
    {
    case ENCODING_KEEP_CURRENT:
    case ENCODING_STANDARD_UTF8:
    case ENCODING_MODIFIED_UTF8:
      if (target)
        *((CHAR*)target) = '\0'; // we have reserved the space;
                                 // no need to move target
      targetBytesDone += 1 CHARs;
      break;
    case ENCODING_UTF16:
      if (target)
        *((WCHAR*)target) = 0x0000; // we have reserved the space;
                                  // no need to move target
      targetBytesDone += 1 WCHARs;
      break;
    case ENCODING_UTF32:
      if (target)
        *((LCHAR*)target) = 0x00000000; // we have reserved the space;
                                  // no need to move target
      targetBytesDone += 1 LCHARs;
      break;
    }
  }

  return targetBytesDone;
}

size_t UTF16Processor(const void* source, size_t utf16lengthInUnits, void* target, size_t targetSizeInBytes, int targetEncoding, int writeTerminatingNull)
{
  size_t sourceUnitsDone = 0;
  size_t targetBytesDone = 0;

  WCHAR* w = (WCHAR*)source;
  errno = 0;

  if (w == NULL) {
    errno = EDOM;
    return 0;
  }

  if ((targetEncoding < 0) || (targetEncoding > ENCODING_UTF32)) {
    errno = EDOM;
    return 0;
  }

  if (writeTerminatingNull) {
    size_t bytesPerUnit;
    switch (targetEncoding) {
    case ENCODING_KEEP_CURRENT:
    case ENCODING_STANDARD_UTF8:
    case ENCODING_MODIFIED_UTF8:
      bytesPerUnit = sizeof(CHAR);
      break;
    case ENCODING_UTF16:
      bytesPerUnit = sizeof(WCHAR);
      break;
    case ENCODING_UTF32:
      bytesPerUnit = sizeof(LCHAR);
      break;
    }

    if (targetSizeInBytes >= bytesPerUnit)
      targetSizeInBytes -= bytesPerUnit; // reserving space for the terminating '\0' unit
    else {
      errno = ERANGE;
      targetSizeInBytes = 0;
      writeTerminatingNull = 0;
    }
  }

  for (;;) {
    // checking when to stop...
    if (utf16lengthInUnits == 0) {
      if (*w == 0x0000)
        break; // stop, if the source length is not given, but '\0' reached
    }
    else {
      if (sourceUnitsDone >= utf16lengthInUnits)
        break; // stop, if the source length is reached
    }

    if ( (w[0] & 0xFC00) == 0xD800 ) { // [1101][10??] --- start of a surrogate pair...
      if ( ((utf16lengthInUnits==0)||(utf16lengthInUnits-sourceUnitsDone>=2))
         &&((w[1] & 0xFC00) == 0xDC00 ) )
      {
        switch (targetEncoding) {
        case ENCODING_KEEP_CURRENT:
        case ENCODING_UTF16:
          if (targetSizeInBytes-targetBytesDone < 2 WCHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            memcpy(target, w, 2 WCHARs);
            target = ((WCHAR*)target)+2;
          }
          targetBytesDone += 2 WCHARs;
          break;
        case ENCODING_MODIFIED_UTF8:
          if (targetSizeInBytes-targetBytesDone < 6 CHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            LCHAR codePoint =
              ((LCHAR)(w[0] & 0x03FF) << 10) |
               (LCHAR)(w[1] & 0x03FF);
                   // 20 bits total
            ((CHAR*)target)[0] = (CHAR)0xED;
            ((CHAR*)target)[1] = (CHAR)0xA0 | (CHAR)( (codePoint>>16)&0x0F );
            ((CHAR*)target)[2] = (CHAR)0x80 | (CHAR)( (codePoint>>10)&0x3F );
            ((CHAR*)target)[3] = (CHAR)0xED;
            ((CHAR*)target)[4] = (CHAR)0xB0 | (CHAR)( (codePoint>>6)&0x0F );
            ((CHAR*)target)[5] = (CHAR)0x80 | (CHAR)( codePoint&0x3F );
            target = ((CHAR*)target)+6;
          }
          targetBytesDone += 6 CHARs;
          break;
        case ENCODING_STANDARD_UTF8:
          if (targetSizeInBytes-targetBytesDone < 4 CHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            LCHAR codePoint =
              ((LCHAR)(w[0] & 0x03FF) << 10) |
               (LCHAR)(w[1] & 0x03FF);
                   // 20 bits total
            codePoint += 0x10000;
              // now: 21 bits total
            ((CHAR*)target)[0] = (CHAR)0xF0 | (CHAR)( (codePoint >> 18)&0x07 );
            ((CHAR*)target)[1] = (CHAR)0x80 | (CHAR)( (codePoint >> 12)&0x3F );
            ((CHAR*)target)[2] = (CHAR)0x80 | (CHAR)( (codePoint >>  6)&0x3F );
            ((CHAR*)target)[3] = (CHAR)0x80 | (CHAR)(  codePoint       &0x3F );
            target = ((CHAR*)target)+4;
          }
          targetBytesDone += 4 CHARs;
          break;
        case ENCODING_UTF32:
          if (targetSizeInBytes-targetBytesDone < 1 LCHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            LCHAR codePoint =
              ((LCHAR)(w[0] & 0x03FF) << 10) |
               (LCHAR)(w[1] & 0x03FF);
                   // 20 bits total
            codePoint += 0x10000;
              // now: 21 bits total
            *((LCHAR*)target) = codePoint;
            target = ((LCHAR*)target)+1;
          }
          targetBytesDone += 1 LCHARs;
          break;
        }

        sourceUnitsDone+=2;
        w+=2;
        continue; // do not go to the default action...
      }
      else {
        errno = EILSEQ; // not a correct UTF-16 surrogate pair
        // go to the default action...
      }
    }
    // else: going to the default action

    // the default action:

    switch (targetEncoding) {
    case ENCODING_KEEP_CURRENT:
    case ENCODING_UTF16:
      if (targetSizeInBytes-targetBytesDone < 1 WCHARs) {
        errno = ERANGE;
        goto FINALIZE;
      }
      if (target) {
        memcpy(target, w, 1 WCHARs);
        target = ((WCHAR*)target)+1;
      }
      targetBytesDone += 1 WCHARs;
      break;
    case ENCODING_MODIFIED_UTF8:
    case ENCODING_STANDARD_UTF8:
      if ((w[0] & 0xF800) != 0x0000) {
        // 3 target bytes
        if (targetSizeInBytes-targetBytesDone < 3 CHARs) {
          errno = ERANGE;
          goto FINALIZE;
        }
        if (target) {
          ((CHAR*)target)[0] = (CHAR)0xE0 | (CHAR)(  w[0]>>12 );
          ((CHAR*)target)[1] = (CHAR)0x80 | (CHAR)( (w[0]>> 6)&0x3F );
          ((CHAR*)target)[2] = (CHAR)0x80 | (CHAR)(  w[0]     &0x3F );
          target = ((CHAR*)target)+3;
        }
        targetBytesDone += 3 CHARs;
      }
      else
      if ( ((w[0] & 0x0780) != 0x0000)
        || ((w[0] == 0x0000)&&(targetEncoding==ENCODING_MODIFIED_UTF8))/*'\0' in modified UTF-8 as 2 bytes*/ ) {
        // 2 target bytes
        if (targetSizeInBytes-targetBytesDone < 2 CHARs) {
          errno = ERANGE;
          goto FINALIZE;
        }
        if (target) {
          ((CHAR*)target)[0] = (CHAR)0xC0 | (CHAR)( (w[0]>>6)&0x1F );
          ((CHAR*)target)[1] = (CHAR)0x80 | (CHAR)(  w[0]    &0x3F );
          target = ((CHAR*)target)+2;
        }
        targetBytesDone += 2 CHARs;
      }
      else {
        // 1 target byte
        if (targetSizeInBytes-targetBytesDone < 1 CHARs) {
          errno = ERANGE;
          goto FINALIZE;
        }
        if (target) {
          *((CHAR*)target) = (CHAR)(w[0]&0x7F);
          target = ((CHAR*)target)+1;
        }
        targetBytesDone += 1 CHARs;
      }
      break;
    case ENCODING_UTF32:
      if (targetSizeInBytes-targetBytesDone < 1 LCHARs) {
        errno = ERANGE;
        goto FINALIZE;
      }
      if (target) {
        *((LCHAR*)target) = w[0];
        target = ((LCHAR*)target)+1;
      }
      targetBytesDone += 1 LCHARs;
      break;
    }

    sourceUnitsDone+=1;
    w+=1;

  } // for

FINALIZE:

  if (writeTerminatingNull) {
    switch (targetEncoding)
    {
    case ENCODING_KEEP_CURRENT:
    case ENCODING_STANDARD_UTF8:
    case ENCODING_MODIFIED_UTF8:
      if (target)
        *((CHAR*)target) = '\0'; // we have reserved the space;
                                 // no need to move target
      targetBytesDone += 1 CHARs;
      break;
    case ENCODING_UTF16:
      if (target)
        *((WCHAR*)target) = 0x0000; // we have reserved the space;
                                  // no need to move target
      targetBytesDone += 1 WCHARs;
      break;
    case ENCODING_UTF32:
      if (target)
        *((LCHAR*)target) = 0x00000000; // we have reserved the space;
                                  // no need to move target
      targetBytesDone += 1 LCHARs;
      break;
    }
  }

  return targetBytesDone;
}

size_t UTF32Processor(const void* source, size_t utf32lengthInUnits, void* target, size_t targetSizeInBytes, int targetEncoding, int writeTerminatingNull)
{
  size_t sourceUnitsDone = 0;
  size_t targetBytesDone = 0;

  LCHAR* l = (LCHAR*)source;
  errno = 0;

  if (l == NULL) {
    errno = EDOM;
    return 0;
  }

  if ((targetEncoding < 0) || (targetEncoding > ENCODING_UTF32)) {
    errno = EDOM;
    return 0;
  }

  if (writeTerminatingNull) {
    size_t bytesPerUnit;
    switch (targetEncoding) {
    case ENCODING_KEEP_CURRENT:
    case ENCODING_STANDARD_UTF8:
    case ENCODING_MODIFIED_UTF8:
      bytesPerUnit = sizeof(CHAR);
      break;
    case ENCODING_UTF16:
      bytesPerUnit = sizeof(WCHAR);
      break;
    case ENCODING_UTF32:
      bytesPerUnit = sizeof(LCHAR);
      break;
    }

    if (targetSizeInBytes >= bytesPerUnit)
      targetSizeInBytes -= bytesPerUnit; // reserving space for the terminating '\0' unit
    else {
      errno = ERANGE;
      targetSizeInBytes = 0;
      writeTerminatingNull = 0;
    }
  }

  for (;;) {
    // checking when to stop...
    if (utf32lengthInUnits == 0) {
      if (*l == 0x00000000)
        break; // stop, if the source length is not given, but '\0' reached
    }
    else {
      if (sourceUnitsDone >= utf32lengthInUnits)
        break; // stop, if the source length is reached
    }

    // the default action (no surrogate pairs in UTF-32):

    switch (targetEncoding) {
    case ENCODING_KEEP_CURRENT:
    case ENCODING_UTF32:
      if (*l > 0x10FFFF)
        errno = EILSEQ;
      if (targetSizeInBytes-targetBytesDone < 1 LCHARs) {
        errno = ERANGE;
        goto FINALIZE;
      }
      if (target) {
        *((LCHAR*)target) = *l;
        target = ((LCHAR*)target)+1;
      }
      targetBytesDone += 1 LCHARs;
      break;
    case ENCODING_UTF16:
      {
        LCHAR codePoint = *l;
        if (codePoint > 0x10FFFF) {
          errno = EILSEQ;
          codePoint = 0x10FFFF;
        }

        if (codePoint > 0xFFFF) { // 2 WCHARs in target...
          if (targetSizeInBytes-targetBytesDone < 2 WCHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            codePoint -= 0x10000;
            ((WCHAR*)target)[0] = 0xD800 | (codePoint >> 10);
            ((WCHAR*)target)[1] = 0xDC00 | (codePoint & 0x3FF);
            target = ((WCHAR*)target)+2;
          }
          targetBytesDone += 2 WCHARs;
        }
        else {
          if (targetSizeInBytes-targetBytesDone < 1 WCHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            ((WCHAR*)target)[0] = (WCHAR)codePoint;
            target = ((WCHAR*)target)+1;
          }
          targetBytesDone += 1 WCHARs;
        }
        break;
      }
    case ENCODING_MODIFIED_UTF8:
    case ENCODING_STANDARD_UTF8:
      {
        LCHAR codePoint = *l;
        if (codePoint > 0x10FFFF) {
          errno = EILSEQ;
          codePoint = 0x10FFFF;
        }

        if (codePoint > 0xFFFF) { // 6 or 4 CHARs in target...
          if (targetEncoding == ENCODING_MODIFIED_UTF8) {
            if (targetSizeInBytes-targetBytesDone < 6 CHARs) {
              errno = ERANGE;
              goto FINALIZE;
            }
            if (target) {
              codePoint -= 0x10000;
              ((CHAR*)target)[0] = (CHAR)0xED;
              ((CHAR*)target)[1] = (CHAR)0xA0 | (CHAR)( (codePoint>>16)&0x0F );
              ((CHAR*)target)[2] = (CHAR)0x80 | (CHAR)( (codePoint>>10)&0x3F );
              ((CHAR*)target)[3] = (CHAR)0xED;
              ((CHAR*)target)[4] = (CHAR)0xB0 | (CHAR)( (codePoint>>6)&0x0F );
              ((CHAR*)target)[5] = (CHAR)0x80 | (CHAR)( codePoint&0x3F );
              target = ((CHAR*)target)+6;
            }
            targetBytesDone += 6 CHARs;
          }
          else { // ENCODING_STANDARD_UTF8
            if (targetSizeInBytes-targetBytesDone < 4 CHARs) {
              errno = ERANGE;
              goto FINALIZE;
            }
            if (target) {
              ((CHAR*)target)[0] = 0xF0 | (CHAR)(codePoint>>18);
              ((CHAR*)target)[1] = 0x80 | (CHAR)( (codePoint>>12)&0x3F );
              ((CHAR*)target)[2] = 0x80 | (CHAR)( (codePoint>>6)&0x3F );
              ((CHAR*)target)[3] = 0x80 | (CHAR)( codePoint&0x3F );
              target = ((CHAR*)target)+4;
            }
            targetBytesDone += 4 CHARs;
          }
        }
        else
        if ((codePoint & 0xF800) != 0x0000) {
          // 3 target bytes
          if (targetSizeInBytes-targetBytesDone < 3 CHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            ((CHAR*)target)[0] = (CHAR)0xE0 | (CHAR)(  codePoint>>12 );
            ((CHAR*)target)[1] = (CHAR)0x80 | (CHAR)( (codePoint>> 6)&0x3F );
            ((CHAR*)target)[2] = (CHAR)0x80 | (CHAR)(  codePoint     &0x3F );
            target = ((CHAR*)target)+3;
          }
          targetBytesDone += 3 CHARs;
        }
        else
        if ( ((codePoint & 0x0780) != 0x0000)
          || ((codePoint == 0x0000)&&(targetEncoding==ENCODING_MODIFIED_UTF8))/*'\0' in modified UTF-8 as 2 bytes*/ ) {
          // 2 target bytes
          if (targetSizeInBytes-targetBytesDone < 2 CHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            ((CHAR*)target)[0] = (CHAR)0xC0 | (CHAR)( (codePoint>>6)&0x1F );
            ((CHAR*)target)[1] = (CHAR)0x80 | (CHAR)(  codePoint    &0x3F );
            target = ((CHAR*)target)+2;
          }
          targetBytesDone += 2 CHARs;
        }
        else {
          // 1 target byte
          if (targetSizeInBytes-targetBytesDone < 1 CHARs) {
            errno = ERANGE;
            goto FINALIZE;
          }
          if (target) {
            *((CHAR*)target) = (CHAR)(codePoint&0x7F);
            target = ((CHAR*)target)+1;
          }
          targetBytesDone += 1 CHARs;
        }
        break;
      } // case ENCODING_MODIFIED_UTF8, ENCODING_STANDARD_UTF8
    } // switch
    sourceUnitsDone+=1;
    l+=1;

  } // for

FINALIZE:

  if (writeTerminatingNull) {
    switch (targetEncoding)
    {
    case ENCODING_KEEP_CURRENT:
    case ENCODING_STANDARD_UTF8:
    case ENCODING_MODIFIED_UTF8:
      if (target)
        *((CHAR*)target) = '\0'; // we have reserved the space;
                                 // no need to move target
      targetBytesDone += 1 CHARs;
      break;
    case ENCODING_UTF16:
      if (target)
        *((WCHAR*)target) = 0x0000; // we have reserved the space;
                                  // no need to move target
      targetBytesDone += 1 WCHARs;
      break;
    case ENCODING_UTF32:
      if (target)
        *((LCHAR*)target) = 0x00000000; // we have reserved the space;
                                  // no need to move target
      targetBytesDone += 1 LCHARs;
      break;
    }
  }

  return targetBytesDone;
}

size_t UTF8Length(const void* source)
{
  return UTF8Processor(source, 0, NULL, TDA_UNICODE_MAX_SIZE, ENCODING_KEEP_CURRENT, 0);
}

size_t UTF8ToStandardUTF8(const void* source, size_t utf8lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull)
{
  return UTF8Processor(source, utf8lengthInUnits, target, targetSizeInBytes, ENCODING_STANDARD_UTF8, writeTerminatingNull);
}

size_t UTF8ToModifiedUTF8(const void* source, size_t utf8lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull)
{
  return UTF8Processor(source, utf8lengthInUnits, target, targetSizeInBytes, ENCODING_MODIFIED_UTF8, writeTerminatingNull);
}

size_t UTF8ToUTF16(const void* source, size_t utf8lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull)
{
  return UTF8Processor(source, utf8lengthInUnits, target, targetSizeInBytes, ENCODING_UTF16, writeTerminatingNull);
}

size_t UTF8ToUTF32(const void* source, size_t utf8lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull)
{
  return UTF8Processor(source, utf8lengthInUnits, target, targetSizeInBytes, ENCODING_UTF32, writeTerminatingNull);
}

size_t UTF16Length(const void* source)
{
  return UTF16Processor(source, 0, NULL, TDA_UNICODE_MAX_SIZE, ENCODING_KEEP_CURRENT, 0)/sizeof(WCHAR);
}

size_t UTF16ToStandardUTF8(const void* source, size_t utf16lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull)
{
  return UTF16Processor(source, utf16lengthInUnits, target, targetSizeInBytes, ENCODING_STANDARD_UTF8, writeTerminatingNull);
}

size_t UTF16ToModifiedUTF8(const void* source, size_t utf16lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull)
{
  return UTF16Processor(source, utf16lengthInUnits, target, targetSizeInBytes, ENCODING_MODIFIED_UTF8, writeTerminatingNull);
}

size_t UTF16ToUTF32(const void* source, size_t utf16lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull)
{
  return UTF16Processor(source, utf16lengthInUnits, target, targetSizeInBytes, ENCODING_UTF32, writeTerminatingNull);
}

size_t UTF32Length(const void* source)
{
  return UTF32Processor(source, 0, NULL, TDA_UNICODE_MAX_SIZE, ENCODING_KEEP_CURRENT, 0)/sizeof(LCHAR);
}

size_t UTF32ToStandardUTF8(const void* source, size_t utf32lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull)
{
  return UTF32Processor(source, utf32lengthInUnits, target, targetSizeInBytes, ENCODING_STANDARD_UTF8, writeTerminatingNull);
}

size_t UTF32ToModifiedUTF8(const void* source, size_t utf32lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull)
{
  return UTF32Processor(source, utf32lengthInUnits, target, targetSizeInBytes, ENCODING_MODIFIED_UTF8, writeTerminatingNull);
}

size_t UTF32ToUTF16(const void* source, size_t utf32lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull)
{
  return UTF32Processor(source, utf32lengthInUnits, target, targetSizeInBytes, ENCODING_UTF16, writeTerminatingNull);
}

FILE *fopen_utf8(const char *utf8FileName, const char *mode)
{
#if (defined(_WIN32) || defined(_WIN64)) && !defined(__WINE__)
  size_t n1, n2;
  wchar_t *w1 = NULL;
  wchar_t *w2 = NULL;
  FILE *retVal = NULL;

  n1 = UTF8ToUTF16(utf8FileName, 0, NULL, TDA_UNICODE_MAX_SIZE, 1);
  if (errno != 0)
    return NULL;
  n2 = UTF8ToUTF16(mode, 0, NULL, TDA_UNICODE_MAX_SIZE, 1);
  if (errno != 0)
    return NULL;
  w1 = malloc(n1);
  w2 = malloc(n2);
  if ((w1 != NULL) && (w2 != NULL)) {
    UTF8ToUTF16(utf8FileName, 0, w1, n1, 1);
    UTF8ToUTF16(mode, 0, w2, n2, 1);
    retVal = _wfopen(w1, w2);
  }
  if (w1)
    free(w1);
  if (w2)
    free(w2);
  return retVal;
#else
  return fopen(utf8FileName, mode);
#endif
}

int mkdir_utf8(const char *utf8Path)
{
#if (defined(_WIN32) || defined(_WIN64)) && !defined(__WINE__)
  size_t n;
  wchar_t *w = NULL;
  int retVal = 0;
  errno = 0;
  n = UTF8ToUTF16(utf8Path, 0, NULL, TDA_UNICODE_MAX_SIZE, 1);
  if (errno != 0)
    return -1;
  w = malloc(n);
  if (w == NULL) {
    errno = ENOMEM;
    return -1;
  }
  UTF8ToUTF16(utf8Path, 0, w, n, 1);
  retVal = _wmkdir(w);
  free(w);
  return retVal;
#else
  errno = 0;
  return mkdir(utf8Path, S_IRWXU);
#endif
}

int chdir_utf8(const char* utf8Path)
{
#if (defined(_WIN32) || defined(_WIN64)) && !defined(__WINE__)
  size_t n;
  wchar_t *w = NULL;
  int retVal = 0;
  errno = 0;
  n = UTF8ToUTF16(utf8Path, 0, NULL, TDA_UNICODE_MAX_SIZE, 1);
  if (errno != 0)
    return -1;
  w = malloc(n);
  if (w == NULL) {
    errno = ENOMEM;
    return -1;
  }
  UTF8ToUTF16(utf8Path, 0, w, n, 1);
  retVal = _wchdir(w);
  free(w);
  return retVal;
#else
  errno = 0;
  return chdir(utf8Path);
#endif
}

char *getcwd_utf8(char *buf, size_t buflen)
{
#if (defined(_WIN32) || defined(_WIN64)) && !defined(__WINE__)
  wchar_t *wbuf;
  size_t n;
  errno = 0;
  wbuf = _wgetcwd(NULL, buflen*2);
  if ((wbuf==NULL) || (errno != 0)) {
    if (wbuf != NULL)
      free(wbuf);
    return NULL;
  }
  n = UTF16ToStandardUTF8(wbuf, 0, NULL, TDA_UNICODE_MAX_SIZE, 1);
  if (errno != 0) {
    free(wbuf);
    return NULL;
  }

  if (buflen < n) {
    errno = ERANGE;
    free(wbuf);
    return NULL;
  }

  if (buf == NULL) {
    buf = malloc(n);
    if (buf == NULL) {
      errno = ENOMEM;
      return NULL;
    }
  }

  // now buf is allocated and buflen is sufficient...
  UTF16ToStandardUTF8(wbuf, 0, buf, buflen, 1);
  return buf;
#else
  return getcwd(buf, buflen);
#endif
}

struct OpenDirHandle {
#if (defined(_WIN32) || defined(_WIN64)) && !defined(__WINE__)
  #ifdef __GNUC__
  _WDIR *d;
  struct _wdirent *e;
  #else
  wDIR *d;
  struct wdirent *e;
  #endif
  char* s; // a string returned by get_d_name_utf8;
           // kept until the next call to get_d_name_utf8
           // or closedir_ut8
#else
  DIR *d;
  struct dirent *e;
#endif
};

void *opendir_utf8(const char *utf8DirName)
{
  struct OpenDirHandle* h = NULL;

#if (defined(_WIN32) || defined(_WIN64)) && !defined(__WINE__)
  size_t n;
  wchar_t *w = NULL;

  n = UTF8ToUTF16(utf8DirName, 0, NULL, TDA_UNICODE_MAX_SIZE, 1);
  if (errno != 0)
    return NULL;

  w = malloc(n);
  if (w == NULL)
    return NULL;
  UTF8ToUTF16(utf8DirName, 0, w, n, 1);

  h = malloc(sizeof(struct OpenDirHandle));
  if (h == NULL) {
    free(w);
    return NULL;
  }

  #ifdef __GNUC__
  h->d = _wopendir(w);
  #else
  h->d = wopendir(w);
  #endif
  if (h->d == NULL) {
    free(h);
    free(w);
    return NULL;
  }
  h->e = NULL;
  h->s = NULL;
  return h;
#else
  h = (struct OpenDirHandle*)malloc(sizeof(struct OpenDirHandle));
  if (h == NULL)
    return NULL;

  h->d = opendir(utf8DirName);
  if (h->d == NULL) {
    free(h);
    return NULL;
  }
  h->e = NULL;
  return h;
#endif
}

int readdir_utf8(void *hOpenDir) // returns 1 (true) or 0 (false)
{
  struct OpenDirHandle* h = (struct OpenDirHandle*)hOpenDir;
  if (h == NULL)
    return 0;
#if (defined(_WIN32) || defined(_WIN64)) && !defined(__WINE__)
  #ifdef __GNUC__
  h->e = _wreaddir(h->d);
  #else
  h->e = wreaddir(h->d);
  #endif
#else
  h->e = readdir(h->d);
#endif
  return (h->e != NULL);
}

char *get_d_name_utf8(void *hOpenDir)
{
  struct OpenDirHandle* h = (struct OpenDirHandle*)hOpenDir;
#if (defined(_WIN32) || defined(_WIN64)) && !defined(__WINE__)
  size_t n;
#endif
  if (h == NULL)
    return NULL;
  if (h->e == NULL)
    return NULL;
#if (defined(_WIN32) || defined(_WIN64)) && !defined(__WINE__)
  if (h->s != NULL) { // freing previously returned string...
    free(h->s);
    h->s = NULL;
  }
  n = UTF16ToStandardUTF8(h->e->d_name, 0, NULL, TDA_UNICODE_MAX_SIZE, 1);
  if (errno != 0)
    return NULL;
  h->s = malloc(n);
  if (h->s == NULL)
    return NULL;
  UTF16ToStandardUTF8(h->e->d_name, 0, h->s, n, 1);
  return h->s;
#else
  return h->e->d_name;
#endif
}

void closedir_utf8(void *hOpenDir)
{
  struct OpenDirHandle* h = (struct OpenDirHandle*)hOpenDir;
  if (h == NULL)
    return;
#if (defined(_WIN32) || defined(_WIN64)) && !defined(__WINE__)
  if (h->s != NULL) { // freing the last returned string...
    free(h->s);
    h->s = NULL;
  }
  #ifdef __GNUC__
  if (h->d != NULL)
    _wclosedir(h->d);
  #else
  if (h->d != NULL)
    wclosedir(h->d);
  #endif
#else
  if (h->d != NULL)
    closedir(h->d);
#endif
  free(h);
}

char *getenv_utf8_malloc(const char *utf8name) // the result (if not NULL) must be freed by free()
{
#if (defined(_WIN32) || defined(_WIN64)) && !defined(__WINE__)
  size_t n;
  wchar_t *w = NULL;
  wchar_t *value = NULL;
  char* retVal = NULL;
  n = UTF8ToUTF16(utf8name, 0, NULL, TDA_UNICODE_MAX_SIZE, 1);
  if (errno != 0)
    return NULL;
  w = malloc(n);
  if (w == NULL) {
    errno = ENOMEM;
    return NULL;
  }
  UTF8ToUTF16(utf8name, 0, w, n, 1);

  value = _wgetenv(w);
  free(w);

  if (value == NULL)
    return NULL;
  n = UTF16ToStandardUTF8(value, 0, NULL, TDA_UNICODE_MAX_SIZE, 1);
  if (errno != 0)
    return NULL;
  retVal = malloc(n);
  if (retVal == NULL)
    return NULL;
  UTF16ToStandardUTF8(value, 0, retVal, n, 1);
  return retVal;
#else
  char *value;
  value = getenv(utf8name);
  if (value == NULL)
    return NULL;
  return strdup(value); // calls malloc()
#endif
}

