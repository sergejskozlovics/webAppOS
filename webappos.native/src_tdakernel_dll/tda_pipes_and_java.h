/** @defgroup tdakernelpipes TDA Kernel DLL Functions For Accessing Java VM and Piped Processes	
 *  TDA Kernel DLL Functions For Accessing Java VM and Piped Processes	
 *  @{
 */
#ifndef TDA_PIPES_AND_JAVA_H_
#define TDA_PIPES_AND_JAVA_H_

#include "tdakernel.h"

TDAEXTERN void* TDACALL TDA_LaunchPipedProcess(IN const char* program, IN const char** args);
  // Launches an external native program.
  // The process will be started from the directory where the TDA Kernel library resides.
  //   program  - the full path to the program to execute in UTF-8 encoding;
  //   args     - an array of command-line arguments NOT including the program name as the first argument;
  //              args have to be NULL-terminated, i.e, the last argument must be NULL;
  //   Returns:   a libtdakernel-specific handle for the launched process (or NULL on error).

TDAEXTERN void* TDACALL TDA_GetParentPipedProcess();

TDAEXTERN bool TDACALL TDA_ReadProcessOutputStream(void* hProcess, void* buffer, IN unsigned int size, OUT unsigned int &read);
TDAEXTERN bool TDACALL TDA_WriteProcessInputStream(void* hProcess, void* buffer, IN unsigned int size, OUT unsigned int &written);
TDAEXTERN bool TDACALL TDA_IsPipedProcessTerminated(void* hProcess);
TDAEXTERN void TDACALL TDA_ReleasePipedProcess(void* hProcess, bool terminate);
  // Closes the process handle and input/output pipe handles (if any).
  //   terminate - close the handles and terminate the process, or simply close the handles.

TDAEXTERN const char* TDACALL TDA_GetJavaHomeAnyBits();
  // Returns: the Java  (either 32, or 64 bit --- which is found first)
  // home directory in standard UTF-8 encoding or NULL, if Java not found.
TDAEXTERN const char* TDACALL TDA_GetJavaHomeSameBits();
  // Returns: the N-bit Java home directory in standard UTF-8 encoding or NULL, if Java not found.
  // N is 32 or 64 depending on the target platform of this DLL.

TDAEXTERN char** TDACALL TDA_UpdateJVMOptions(IN const char** options);
  // The "-Djava.class.path=..." and "-Djava.library.path=..." JVM options
  // are appended by TDA-specific paths.
  // Options is a NULL-terminated array.
  // The result is NULL in case of error.
  // The result (if not NULL) must be freed by calling TDA_FreeUpdatedJVMOptions().
  // The result contains an array of options, where the last option is NULL to specify the end-of-options.
TDAEXTERN void TDACALL TDA_FreeUpdatedJVMOptions(IN char** options);

TDAEXTERN void* TDACALL TDA_LaunchPipedJavaProcess(IN const char** jvmOptions, IN const char* mainClassName, IN const char** mainArgs);
  // Launches a Java class in a separate Java VM. Implemented using TDA_GetJavaHome and TDA_LaunchProcess.
  // The process will be started from the directory where the TDA Kernel library resides.
  //   jvmOptions    -  A NULL-terminated array of Java VM arguments.
  //                    The "-Djava.class.path=..." and "-Djava.library.path=..." arguments
  //                    are appended by TDA-specific paths.
  //   mainClassName - the full class name with the main() method to execute;
  //   mainArgs      - Arguments for the main() method (NOT including the program name as the first argument).
  //   Returns:        a libtdakernel-specific handle for the launched process (or NULL on error).

TDAEXTERN void** TDACALL TDA_GetExistingJavaVMs();
  // returns a NULL-terminated array of existing Java VMs, or NULL on error (e.g., if jvm library is not loaded yet);
  // The result (if not NULL) must be freed by calling TDA_FreeArrayOfExistingJavaVMs().
TDAEXTERN void TDACALL TDA_FreeArrayOfExistingJavaVMs(IN void** array);

TDAEXTERN bool TDACALL TDA_CreateNewJavaVM(IN const char** jvmOptions, OUT void** jvm, OUT void** jvmLibHandle);
TDAEXTERN void TDACALL TDA_DestroyJavaVM(IN void* jvm, IN void* jvmLibHandle);
  // must be called from the same thread, from which TDA_CreateNewJavaVM was called;
  // must not be called at all, if TDA_NewCreateJavaVM returned false;

TDAEXTERN bool TDACALL TDA_LaunchJavaClass(IN void* jvm, IN const char* mainClassName, IN const char** mainArgs);
  // Launches (in the same process and thread) the static main method of the given Java class.
  // This is useful for native launchers.
  // The main class name may be in the form of package1.package2.ClassName or package1/package2/ClassName
  // TDA_CreateNewJavaVM should be called first to obtain a pointer to a JVM.

TDAEXTERN const char* TDACALL TDA_LaunchJavaStringToStringClassMethod(IN void* jvm, IN const char* className, IN const char* methodName, IN const char* arg);
  // Launches (in the same process and thread) the static method with the given name of the given Java class.
  // The Java method must take exactly one String argument and must return a String value.
  // The class name may be in the form of package1.package2.ClassName or package1/package2/ClassName
  // TDA_CreateNewJavaVM should be called first to obtain a pointer to a JVM.
  // The return value is NULL, if either the call was not successful, or the Java method returned null.

TDAEXTERN void* TDACALL TDA_CreateSharedMemory(IN const char* memoryName, IN const unsigned int size);
  // Creates/opens shared memory with the given memoryName and size.
  // Returns a TDA-specific handle to that memory to be used in the following two functions, or NULL on error.
  // Currently, implemented for the Windows platform only.
TDAEXTERN unsigned char* TDACALL TDA_GetSharedMemoryByteArray(void* sharedMemory);
  // Returns a pointer to the byte array of the given shared memory, or NULL on error.
  // Currently, implemented for the Windows platform only.
TDAEXTERN void TDACALL TDA_CloseSharedMemory(void* sharedMemory);
  // Closes the given shared memory.

TDAEXTERN void TDACALL TDA_Sleep(IN const unsigned int ms);
  // Sleeps at least ms milliseconds. If ms==0, gives the remaining time of the current context switch to other threads/processes.

#endif /* TDA_PIPES_AND_JAVA_H_ */
/**
 *  @}
 */
