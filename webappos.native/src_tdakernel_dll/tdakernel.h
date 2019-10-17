/** @defgroup tdakerneldll TDA Kernel DLL Additional Functions
 *  TDA Kernel DLL Additional Functions
 *  @{
 */

#ifndef TDAKERNEL_H
#define TDAKERNEL_H

#if defined(_WIN32) || defined(_WIN64)
  #ifdef TDA_EXPORT
    #ifdef __cplusplus
      #define TDAEXTERN extern "C" __declspec(dllexport)
    #else
      #define TDAEXTERN __declspec(dllexport)
    #endif // __cplusplus
  #else
    #ifdef __cplusplus
      #define TDAEXTERN extern "C" __declspec(dllimport)
    #else
      #define TDAEXTERN __declspec(dllimport)
    #endif // __cplusplus
  #endif // EXPORT
  #define TDACALL __stdcall
#else
  #ifdef TDA_EXPORT
    #ifdef __cplusplus
      #define TDAEXTERN extern "C"
    #else
      #define TDAEXTERN extern
    #endif
  #else
    #ifdef __cplusplus
      #define TDAEXTERN extern "C"
    #else
      #define TDAEXTERN extern
    #endif
  #endif // TDA_EXPORT
  #define TDACALL //__cdecl
#endif

#define IN
#define INOUT
#define OUT

#define TDA_SHARED_MEMORY_SIZE 131072 // 128 KiB

#ifdef TDA_EXPORT
const char *TDA_CreateReturnString(const char *s); // for used only inside TDA Kernel library
unsigned int TDA_GetCurrentProcessID();
unsigned int TDA_GetParentProcessID();
#endif

#ifdef __GNUC__
  #include <stdint.h>
  #ifndef __int64
  #define __int64 int64_t // long long int
  #endif
#endif

TDAEXTERN const char* TDACALL TDA_GetTDAKernelLibraryPath(); // returns a UTF-8 string
#if defined(_WIN32) || defined(_WIN64)
TDAEXTERN const wchar_t* TDACALL TDA_GetTDAKernelLibraryPathW(); // returns a UTF-16 string (Windows-only)
#endif

// FUNCTIONS FOR CREATING NEW TDA KERNELS AND ACCESSING EXISTING ONES //

/**
 *  Either creates a new TDA Kernel instance and returns its reference (if just the name of a communication protocol is specified), or obtains a reference to an existing TDA Kernel (if an URI is specified).
 *  @param protocolOrURI a string denoting either a protocol name ("jni", "pipe", "shared_memory", "corba"),
 *  which specifies the communication mechanism for a TDA Kernel instance to be created, or
 *  an URI ("jni:UUID", "shared_memory:MEMORY_NAME", "corba:IOR") for obtaining a reference to an
 *  existing TDA Kernel.
 *  @return a reference to a TDA Kernel, or null on error.
 */
TDAEXTERN void* TDACALL TDA_GetTDAKernelReference(const char *protocolOrUri);

/*
 *  Frees the given TDA Kernel reference obtained earlier by calling TDA_GetTDAKernelReference.
 *  @param tdaKernel a reference to a TDA Kernel returned by TDA_GetTDAKernelReference
 *  @see TDA_GetTDAKernelReference
 */
TDAEXTERN bool TDACALL TDA_FreeTDAKernelReference(void *tdaKernel);

// FUNCTIONS FROM THE ITDAKernel INTERFACE IN C STYLE //
#include "tdakernel_stub_c2base.h"

// BONUS FUNCTIONS //
#include "tda_pipes_and_java.h"

#endif // TDAKERNEL_H
/**
 *  @}
 */
