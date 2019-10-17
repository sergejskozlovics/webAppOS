
#include "tdakernel_jni_launcher.h"

#define TDA_EXPORT
#include "tdakernel.h"

#include <jni.h>
#include <assert.h>

#include <string>
using namespace std;

#if defined(_WIN32) || defined(_WIN64)
#include <windows.h>
#endif



///// FUNCTIONS FOR ACTIVATING NEW TDA KERNELS FROM C/C++ /////

jobject Internal_CreateTDAKernelAndGetReference(JavaVM* jvm)
{
	assert(jvm != NULL);

	JNIEnv *env = NULL;
	jint result = jvm->AttachCurrentThread((void**)&env, NULL);

	if ((result != 0) || (env == NULL))
		return NULL;

	jclass clsTDAKernel = env->FindClass("lv/lumii/tda/kernel/TDAKernel");

	if (clsTDAKernel == NULL)
		return NULL;
	jclass clsClass = env->FindClass("java/lang/Class");
	if (clsClass == NULL) {
		env->DeleteLocalRef(clsTDAKernel);
		return NULL;
	}

	jmethodID mid = env->GetMethodID(clsClass, "newInstance", "()Ljava/lang/Object;");
	if (mid == NULL) {
		env->DeleteLocalRef(clsTDAKernel);
		env->DeleteLocalRef(clsClass);
		return NULL;
	}

	jobject tdaKernel = env->CallObjectMethod(clsTDAKernel, mid);

	env->DeleteLocalRef(clsTDAKernel);
	env->DeleteLocalRef(clsClass);

	return tdaKernel;

}

// Searches for some Java VM (see the search algorithm below)
// and uses it to initialize TDA.
// Returns the newly created TDA kernel, or NULL on error.
//
// If (jvm==NULL) then we assume that the caller is not interested in
// what Java VM was used to initialize TDA.
// If jvm is not NULL, but *jvm == NULL, then the the value *jvm
// is being replaced with the Java VM, which was used to create TDA.
// if *jvm is not NULL, then the value *jvm is used as a pointer to JavaVM
// (as JavaVM*).

// The Java VM search algorithm:
// 1. Uses the last created existing Java VM.
// 2. If no existing Java VM is found, creates a new Java VM. If
//    the caller is not interested in it, this VM will remain active
//    and the jvm library will remain loaded
//    until the caller process terminates.
//    Otherwise, the caller may manually destroy that VM
//    by calling functions from <jni.h>, when needed. The jvm library
//    may also be freed (with dlclose or FreeLibrary).
//
// The jvm.dll/libjvm.so/libjvm.dylib is being searched in this way:
// 1. If it is already loaded, use it.
// 2. Otherwise, it is being searched in directories from the PATH variable
//    (you should check the PATH variable if you have both 32-bit and 64-bit
//    Java runtimes; the desired Java runtime must go earlier in the PATH).


jobject createJniTDAKernel(JavaVM** jvm)
{
  // This function will initialize Java, if needed, and
  // then will create and activate a new TDA kernel.

  if ((jvm == NULL) || (*jvm == NULL)) { // no Java specified
    JavaVM** virtualMachinesArr = (JavaVM**)TDA_GetExistingJavaVMs();

    if ((virtualMachinesArr != NULL) && (*virtualMachinesArr != NULL)) {
      // there exists at least one JVM...

      // moving to the last one...
      JavaVM** ptr = virtualMachinesArr;
      while (*(ptr+1) != NULL)
    	  ptr++;
      // now: *(ptr+1) == NULL, but *ptr != NULL, i.e., ptr points to the last Java VM

      // activating the kernel in the last VM
      jobject retVal = Internal_CreateTDAKernelAndGetReference(*ptr);

      if (retVal != NULL) {
        if (jvm != NULL)
          *jvm = *ptr;
      }

      TDA_FreeArrayOfExistingJavaVMs((void**)virtualMachinesArr);
      return retVal;
    }
    else { // no VM found

      TDA_FreeArrayOfExistingJavaVMs((void**)virtualMachinesArr); // the array is not needed any more, since it is either NULL, or empty

      JavaVM* newjvm;
      void* hLibJVM;
      bool created = TDA_CreateNewJavaVM(NULL, (void**)&newjvm, (void**)&hLibJVM);

      if (!created)
    	  return NULL;

      jobject retVal = Internal_CreateTDAKernelAndGetReference((JavaVM*)(newjvm));
      if (retVal == NULL)
    	  TDA_DestroyJavaVM(newjvm, hLibJVM);

      if (retVal != NULL) {
        if (jvm != NULL)
          *jvm = newjvm;
      }

      // do not freeing the jni.dll library (hLibJVM); keeping it until this process terminates...
      return retVal;
    }

  }
  else {
    return Internal_CreateTDAKernelAndGetReference((JavaVM*)(*jvm));
  }
}

jobject findJniTDAKernelByUUID(const char *uuid, JavaVM** _jvm)
{
	JavaVM** virtualMachinesArr = (JavaVM**)TDA_GetExistingJavaVMs();

	JavaVM** ptr = virtualMachinesArr;
	if (ptr == NULL)
		return NULL;

	if (_jvm != NULL)
		*_jvm = NULL;

	for (; *ptr != NULL; ptr++) {

		JavaVM *jvm = *ptr;
		JNIEnv *env = NULL;
		if (jvm->AttachCurrentThread((void**)&env, NULL) != 0)
			continue;

		jstring jUUID = env->NewStringUTF(uuid); // TODO modified utf
		if (jUUID == NULL)
			continue;

		jclass clsTDAKernel = env->FindClass("lv/lumii/tda/kernel/TDAKernel");
		if (clsTDAKernel == NULL) {
			env->DeleteLocalRef(jUUID);
			continue;
		}

		jmethodID mid = env->GetStaticMethodID(clsTDAKernel, "findTDAKernelByUUID", "(Ljava/lang/String;)Llv/lumii/tda/kernel/TDAKernel;");

		if (mid == NULL) {
			env->DeleteLocalRef(jUUID);
			env->DeleteLocalRef(clsTDAKernel);
			continue;
		}

		jobject retVal = env->CallStaticObjectMethod(clsTDAKernel, mid, jUUID);

		env->DeleteLocalRef(jUUID);
		env->DeleteLocalRef(clsTDAKernel);

		if (retVal) {
			if (_jvm != NULL)
				*_jvm = jvm;
			TDA_FreeArrayOfExistingJavaVMs((void**)virtualMachinesArr);
			return retVal;
		}
	}

    TDA_FreeArrayOfExistingJavaVMs((void**)virtualMachinesArr);
	return NULL;
}

