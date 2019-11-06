#ifndef TDAKERNEL_JNI_LAUNCHER_H
#define TDAKERNEL_JNI_LAUNCHER_H

#include <jni.h>

jobject createJniTDAKernel(JavaVM** jvm);
jobject findJniTDAKernelByUUID(const char *uuid, JavaVM** jvm);

#endif // TDAKERNEL_JNI_LAUNCHER_H
