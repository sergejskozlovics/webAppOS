#include "tda_unicode.h"

#include <stdio.h>
#include <conio.h>
#include <string.h>

// Example form http://msdn.microsoft.com/en-us/library/system.text.encoding.utf8(v=vs.80).aspx
// The characters to encode:
//    Latin Small Letter Z (U+007A)
//    Latin Small Letter A (U+0061)
//    Combining Breve (U+0306)
//    Latin Small Letter AE With Acute (U+01FD)
//    Greek Small Letter Beta (U+03B2)
//    a high-surrogate value (U+D8FF)
//    a low-surrogate value (U+DCFF)
WCHAR wchars[] = { L'z', L'a', 0x0306, 0x01FD, 0x03B2, 0xD8FF, 0xDCFF, 0x0000 };
CHAR utf8chars[] = { 0x7A, 0x61, 0xCC, 0x86, 0xC7, 0xBD, 0xCE, 0xB2, 0xF1, 0x8F, 0xB3, 0xBF, 0x00 };
CHAR mutf8chars[] = {0x7A, 0x61, 0xCC, 0x86, 0xC7, 0xBD, 0xCE, 0xB2, 0xED, 0xA3, 0xBF, 0xED, 0xB3, 0xBF, 0x00 };
LCHAR utf32chars[] = { 0x0000007A, 0x00000061, 0x00000306, 0x000001FD, 0x000003B2, 0x0004FCFF, 0x00000000 };

int main(int argc, char* argv[])
{
  size_t n;
  int i;
  CHAR s[1024];
  WCHAR w[1024];
  LCHAR l[1024];

  printf("testing wchars length:\t\t\t%d", UTF16Length(wchars)==7);
  printf("\t(errno=%d)\n", errno);

  printf("testing utf8 length:\t\t\t%d", UTF8Length(utf8chars)==12);
  printf("\t(errno=%d)\n", errno);

  printf("testing modif. utf8 length:\t\t%d", UTF8Length(mutf8chars)==14);
  printf("\t(errno=%d)\n", errno);

  printf("testing utf32 length:\t\t%d", UTF32Length(utf32chars)==6);
  printf("\t(errno=%d)\n", errno);

  n = UTF16ToStandardUTF8(wchars, 0, NULL, TDA_UNICODE_MAX_SIZE, 0);
  printf("testing wchars length in std UTF-8:\t%d", n==12);
  printf("\t(errno=%d)\n", errno);
  UTF16ToStandardUTF8(wchars, 0, s, 1024, 1);
  printf("testing wchars convert to std UTF-8:\t%d", strcmp(s, utf8chars)==0);
  printf("\t(errno=%d)\n", errno);
  for (i=0; i<n; i++) {
    printf(" %02X", s[i]);
  }
  printf("\n\n");

  n = UTF32ToStandardUTF8(utf32chars, 0, NULL, TDA_UNICODE_MAX_SIZE, 0);
  printf("testing utf32 length in std UTF-8:\t%d", n==12);
  printf("\t(errno=%d)\n", errno);
  UTF32ToStandardUTF8(utf32chars, 0, s, 1024, 1);
  printf("testing utf32chars convert to std UTF-8:\t%d", strcmp(s, utf8chars)==0);
  printf("\t(errno=%d)\n", errno);
  for (i=0; i<n; i++) {
    printf(" %02X", s[i]);
  }
  printf("\n\n");

  n = UTF16ToModifiedUTF8(wchars, 0, NULL, TDA_UNICODE_MAX_SIZE, 0);
  printf("testing wchars length in modif. UTF-8:\t%d", n==14);
  printf("\t(errno=%d)\n", errno);
  UTF16ToModifiedUTF8(wchars, 0, s, 1024, 1);
  printf("testing wchars convert to modif. UTF-8:\t%d", strcmp(s, mutf8chars)==0);
  printf("\t(errno=%d)\n", errno);
  for (i=0; i<n; i++) {
    printf(" %02X", s[i]);
  }
  printf("\n\n");

  n = UTF32ToModifiedUTF8(utf32chars, 0, NULL, TDA_UNICODE_MAX_SIZE, 0);
  printf("testing utf32chars length in modif. UTF-8:\t%d", n==14);
  printf("\t(errno=%d)\n", errno);
  UTF32ToModifiedUTF8(utf32chars, 0, s, 1024, 1);
  printf("testing utf32chars convert to modif. UTF-8:\t%d", strcmp(s, mutf8chars)==0);
  printf("\t(errno=%d)\n", errno);
  for (i=0; i<n; i++) {
    printf(" %02X", s[i]);
  }
  printf("\n\n");

  n = UTF16ToUTF32(wchars, 0, NULL, TDA_UNICODE_MAX_SIZE, 0);
  printf("testing wchars length in UTF-32:\t%d", n==6*4);
  printf("\t(errno=%d)\n", errno);
  UTF16ToUTF32(wchars, 0, l, 1024, 0);
  printf("testing wchars convert to UTF-32:\t%d", strcmp(l, utf32chars)==0);
  printf("\t(errno=%d)\n", errno);
  n/=4;
  for (i=0; i<n; i++) {
    printf(" %08X", l[i]);
  }
  printf("\n\n");

  n = UTF32ToUTF16(utf32chars, 0, NULL, TDA_UNICODE_MAX_SIZE, 0);
  printf("testing utf32chars length in UTF-16:\t%d", n==7*2);
  printf("\t(errno=%d)\n", errno);
  UTF32ToUTF16(utf32chars, 0, w, 1024, 1);
  printf("testing utf32chars convert to UTF-16:\t%d", wcscmp(w, wchars)==0);
  printf("\t(errno=%d)\n", errno);
  n/=2;
  for (i=0; i<n; i++) {
    printf(" %04X", w[i]);
  }
  printf("\n\n");

  n = UTF16ToUTF32(wchars, 0, l, 8, 1);
  printf("testing wchars convert to UTF-32 with max=8, with \'\\0\':\t%d", n==8);
  printf("\t(errno=%d)\n", errno);
  n/=4;
  for (i=0; i<n; i++) {
    printf(" %08X", l[i]);
  }
  printf("\n\n");

  n = UTF16ToUTF32(wchars, 0, l, 9, 0);
  printf("testing wchars convert to UTF-32 with max=9, no \'\\0\':\t%d", n==8);
  printf("\t(errno=%d)\n", errno);
  n/=4;
  for (i=0; i<n; i++) {
    printf(" %08X", l[i]);
  }
  printf("\n\n");

  n = UTF8ToModifiedUTF8(utf8chars, 0, NULL, TDA_UNICODE_MAX_SIZE, 0);
  printf("testing UTF-8 length in modif. UTF-8:\t%d", n==14);
  printf("\t(errno=%d)\n", errno);
  UTF8ToModifiedUTF8(utf8chars, 0, s, 1024, 1);
  printf("testing wchars convert to modif. UTF-8:\t%d", strcmp(s, mutf8chars)==0);
  printf("\t(errno=%d)\n", errno);
  for (i=0; i<n; i++) {
    printf(" %02X", s[i]);
  }
  printf("\n\n");

  n = UTF8ToModifiedUTF8(utf8chars, 13, NULL, TDA_UNICODE_MAX_SIZE, 0);
  printf("testing UTF-8\' length in modif. UTF-8:\t%d", n==14+2);
  printf("\t(errno=%d)\n", errno);
  UTF8ToModifiedUTF8(utf8chars, 13, s, 1024, 0);
  printf("testing wchars convert to modif. UTF-8:\t%d", (strncmp(s, mutf8chars, 14)==0) && (s[14]==0xC0) && (s[15]==0x80) );
  printf("\t(errno=%d)\n", errno);
  for (i=0; i<n; i++) {
    printf(" %02X", s[i]);
  }
  printf("\n\n");

  n = UTF8ToUTF16(s, 16, NULL, TDA_UNICODE_MAX_SIZE, 0);
  printf("testing m. UTF-8\' length in UTF-16:\t%d", n==(7+1)*2);
  printf("\t(errno=%d)\n", errno);
  UTF8ToUTF16(s, 16, w, 1024, 0);
  printf("testing m. UTF-8\' convert to UTF-16:\t%d", (wcsncmp(w, wchars, 7)==0) && (w[7]==0x0000) );
  printf("\t(errno=%d)\n", errno);
  n/=2;
  for (i=0; i<n; i++) {
    printf(" %04X", w[i]);
  }
  printf("\n\n");

  n = UTF8ToStandardUTF8(s, 16, NULL, TDA_UNICODE_MAX_SIZE, 0);
  printf("testing m. UTF-8\' length in std. UTF-8:\t%d", n==12+1);
  printf("\t(errno=%d)\n", errno);
  UTF8ToStandardUTF8(s, 16, s, 1024, 0);
  printf("testing m. UTF-8 convert to std. UTF-8:\t%d", (strncmp(s, utf8chars, 12)==0) && (s[12]==0x00) );
  printf("\t(errno=%d)\n", errno);
  for (i=0; i<n; i++) {
    printf(" %02X", s[i]);
  }
  printf("\n\n");

  n = UTF8ToStandardUTF8(utf8chars, 16, NULL, 3, 0);
  printf("testing m. UTF-8\' length in std. UTF-8 with max=3, no \'\\0\':\t%d", n==2);
  printf("\t(errno=%d)\n", errno);
  UTF8ToStandardUTF8(utf8chars, 16, s, 3, 0);
  printf("testing m. UTF-8 convert to std. UTF-8:\t%d", (strncmp(s, utf8chars, 2)==0) );
  printf("\t(errno=%d)\n", errno);
  for (i=0; i<n; i++) {
    printf(" %02X", s[i]);
  }
  printf("\n\n");

  n = UTF8ToStandardUTF8(utf8chars, 12, NULL, 4, 1);
  printf("testing m. UTF-8\' length in std. UTF-8 with max=4, with \'\\0\':\t%d", n==3);
  printf("\t(errno=%d)\n", errno);
  UTF8ToStandardUTF8(utf8chars, 12, s, 4, 1);
  printf("testing m. UTF-8 convert to std. UTF-8:\t%d", (strncmp(s, utf8chars, 2)==0) && (s[2]=='\0'));
  printf("\t(errno=%d)\n", errno);
  for (i=0; i<n; i++) {
    printf(" %02X", s[i]);
  }
  printf("\n\n");

  getch();
  return 0;
}

