Accessing a Repository via TDA Kernel
=====================================
When accessing a model repository via TDA Kernel, you need to specify
the adapter name for that repository, e.g., "ar:".
(The name of the adapter is the name of the corresponding sub-package
where the RepositoryAdapter class resides:
lv.lumii.tda.kernel.adapters.repository.<adapter-name>.RepositoryAdapter)

Example (Java):
  import lv.lumii.tda.kernel.TDAKernel;
  ...

  TDAKernel k = new TDAKernel();
  k.open("ar:/tmp/repo_dir");

Example (C/C++):
  #include "tdakernel.h"
  ...

  void* tdaKernel = TDA_GetTDAKernelReference("jni");
  TDA_Open(tdaKernel, "ar:/tmp/repo_dir");

Dependencies:
  Both Java and C/C++ code require TDA Kernel (packaged in a .jar or folder)
  as well as slf4j-api-*.jar (logging facade library). Java runtime must
  also be installed (at least version 11, works with both Oracle Java and OpenJDK;
  Java 8 can also be used, but then .class files have to be recompiled and re-packaged into
  jars).

  In addition, C/C++ requires the TDA Kernel library (.dll, .dylib, or .so)
  compiled for the desired platform.

Accessing a Repository via its Adapter (without TDA Kernel)
===========================================================
When a repository is accessed via repository adapter
(without TDA Kernel), you do not need to specify the adapter name.
However, you should call the static create() function of the
adapter class, since it can initialize necessary wrappers
if the adapter does not implement certain functions.

Example (Java):
  import lv.lumii.tda.kernel.adapters.repository.ar.RepositoryAdapter;
  ...

  IRepository repo = RepositoryAdapter.create();
    // ^^^ static call instead of: new RepositoryAdapter();
  repo.open("/tmp/repo_dir");
    // ^^^ no "ar:" prefix
