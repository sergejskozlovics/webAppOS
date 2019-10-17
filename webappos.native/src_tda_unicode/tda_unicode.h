//============================================================================
// Name        : tda_unicode.h and tda_unicode.c
// Author      : Sergejs Kozlovics
// Version     : 2012-01-18
// Copyright   : University of Latvia, 2012
// Description : A C library for converting between UTF-8,
//               modified UTF-8, UTF-16 and UTF-32 encodings.
//               This library is a part of TDA Kernel, but may be used
//               by its own.
//               See comments below in this file for the API description.
//============================================================================

/*
By kind permission of University of Latvia this file
is distributed under the following MIT (X11) style license terms:

Copyright (C) 2010-2011 by University of Latvia

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/


#ifndef TDA_UNICODE_H
#define TDA_UNICODE_H

#include <errno.h>
#include <stdlib.h>
#include <stdio.h>

#ifdef __WINE__
#include <windows.h>
#endif

#ifndef EILSEQ
#define EILSEQ EINVDAT
#endif

#ifdef __GNUC__

#include <stdint.h>
#ifndef CHAR
#define CHAR uint8_t
#endif
#ifndef WCHAR
#define WCHAR uint16_t
#endif
#ifndef LCHAR
#define LCHAR uint32_t
#endif

#else // not __GNUC__

#ifndef CHAR
#define CHAR unsigned char
#endif
#if (sizeof(CHAR)!=1)
Error: size of CHAR is not 1. Please, redefine CHAR above for your target.
#endif
#ifndef WCHAR
#define WCHAR unsigned short
#endif
#if (sizeof(WCHAR)!=2)
Error: size of WCHAR is not 2. Please, redefine WCHAR above for your target.
#endif
#ifndef LCHAR
#define LCHAR unsigned int
#endif
#if (sizeof(LCHAR)!=4)
Error: size of LCHAR is not 4. Please, redefine LCHAR above for your target.
#endif

#endif // ifdef __GNUC__

#define TDA_UNICODE_MAX_SIZE ((size_t)(-1))

/*

  API DESCRIPTION

   The library works with the following encodings:
     UTF-8 (standard)
     modified UTF-8 (used by Java virtual machine)
     UTF-16 (standard)
     UTF-32 (standard).
   Code points may be in the range U+000000 to U+10FFFF.
   In case of an illegal sequence, errno is set to EILSEQ,
   but we will still try to preserve all the characters,
   although the target sequence might also become illegal.

  All the functions XXXToYYY take similar parameters:
     source - a pointer to a source string;
              for UTF8ToXXX functions - either standard, or modified UTF-8 string (null-terminated or not);
              for UTF16ToXXX functions - a UTF-16 (little-endian or big-endian according to the
                                                   current compiler target),
                                         null-terminated or not;
              for UTF32ToXXX functions - a UTF-32 (little-endian or big-endian according to the
                                                   current compiler target),
                                         null-terminated or not;
     utfXXlengthInUnits - 0 to use a terminating '\0' to determine the length of the source string
              (the source must be null-terminated),
              or non-zero value to use it as a length (UTF-8 units = bytes, UTF-16 units = double bytes,
              UTF-32 units = quadrabytes) despite of a possible
              presence of '\0' in the middle of the source string (the source may be null-terminated or not);
     target - NULL, if only the size of the result is needed;
              not-NULL - a buffer for the result
     targetSizeInBytes - the size of the target buffer in bytes or an upper bound
              for the target size if target == NULL;
              we will return the result, which is <= targetSizeInBytes
              (errno may be set to ERANGE, if targetSizeInBytes is not sufficient);
              if (target != NULL) we will write no more than targetSizeInBytes bytes into
              the target buffer;
     writeTerminatingNull - a boolean (0 or 1) specifying whether we need to write
              a terminating '\0' unit into the target buffer
              (only when target != NULL and targetSizeInBytes
              is not less than a space needed for one target unit)

   Returns: the size of the target result in bytes
*/

#ifdef __cplusplus
extern "C" {
#endif

size_t UTF8Length(const void* source);
size_t UTF8ToStandardUTF8(const void* source, size_t utf8lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull);
size_t UTF8ToModifiedUTF8(const void* source, size_t utf8lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull);
size_t UTF8ToUTF16(const void* source, size_t utf8lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull);
size_t UTF8ToUTF32(const void* source, size_t utf8lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull);

size_t UTF16Length(const void* source);
size_t UTF16ToStandardUTF8(const void* source, size_t utf16lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull);
size_t UTF16ToModifiedUTF8(const void* source, size_t utf16lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull);
size_t UTF16ToUTF32(const void* source, size_t utf16lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull);

size_t UTF32Length(const void* source);
size_t UTF32ToStandardUTF8(const void* source, size_t utf32lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull);
size_t UTF32ToModifiedUTF8(const void* source, size_t utf32lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull);
size_t UTF32ToUTF16(const void* source, size_t utf32lengthInUnits, void* target, size_t targetSizeInBytes, int writeTerminatingNull);


/* API for accessing files and folders in UTF-8 */
FILE *fopen_utf8(const char *utf8FileName, const char *mode);
int mkdir_utf8(const char *utf8Path);
int chdir_utf8(const char* utf8Path);
char *getcwd_utf8(char *buf, size_t buflen); // on error returns NULL

void *opendir_utf8(const char *utf8DirName);
int readdir_utf8(void *hOpenDir); // returns 1 (true) or 0 (false)
char *get_d_name_utf8(void *hOpenDir);
void closedir_utf8(void *hOpenDir);

char *getenv_utf8_malloc(const char *utf8name); // the result (if not NULL) must be freed by free()


#ifdef __cplusplus
}
#endif
                                        
#endif // TDA_UNICODE_H
