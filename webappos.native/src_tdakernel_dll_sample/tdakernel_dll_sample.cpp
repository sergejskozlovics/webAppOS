//============================================================================
// Name        : tdakernel_dll_sample.cpp
// Author      : Sergejs Kozlovics
// Version     : 2018-04-11
// Description : Demonstrating the usage of TDA Kernel as a means for
//               accessing a model repository from C++ (without loading TDA
//               engines and model transformations).
//============================================================================

/*
By kind permission of University of Latvia this file
is distributed under the following MIT (X11) style license terms:

Copyright (C) 2010-2011 by University of Latvia
Copyright (C) 2013-2018 by Institute of Mathematics and Computer Science, University of Latvia

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

#include <iostream>
#include <string>
using namespace std;

#include "tdakernel.h"

int main() {
	cout << "Before creating a TDA kernel." << endl;

	void* tdaKernel = TDA_GetTDAKernelReference("jni");
	// The following values can be passed to TDA_GetTDAKernelReference:
	// "jni"  - create a TDA Kernel via JNI; load Java VM in the current process, if necessary
	// "pipe" - create a TDA Kernel by launching java in another process; the communication
	//          with that process is performed via a pipe (RAAPI calls are encoded specifically)
	// "shared_memory" - create a TDA Kernel by launching java in another process; the communication
	//          with that process is performed via shared memory (RAAPI calls are encoded specifically)
	// "jni:UUID" - find an existing TDA Kernel with the given UUID (encoded as a string)
	//              in the Java VM associated with the current process;
	//              UUID can be obtained from Java by calling TDA Kernel function getUUID()
	//              and by transferring this UUID to us somehow
	// "shared_memory:memory_name" - find an existing TDA Kernel, which is listenning to the
	//              shared memory with the given name; the existing TDA Kernel can be in this or another process;
	//              Shared memory name can be any string that is not used as a shared memory name yet.
	//              This name is set to a TDA Kernel from java by calling attachSharedMemory,
	//              or by launching TDAKernelSharedMemoryServer and specifying the memory name
	//              as the first command-line argument.

	if (tdaKernel == NULL) {
		cout << "Error creating a TDA kernel." << endl;
		return 1;
	}

	#if defined(_WIN32) || defined(_WIN64)
	string defRepLocation = "ar:c:\\repo_test";
	#else
	string defRepLocation = "ar:/tmp/repo_test";
	#endif
	cout << "Binding TDA Kernel to the location `" << defRepLocation.c_str() << "'." << endl;

	bool b;

		if (TDA_Exists(tdaKernel, defRepLocation.c_str()))
			cout << "The repository already exists at `" << defRepLocation.c_str() << "'." << endl;
		else
			cout << "The repository does not exist at `" << defRepLocation.c_str() << "'." << endl;

		b = TDA_Open(tdaKernel, defRepLocation.c_str());
		cout << "open() returned " << b << "." << endl;
		if (b) {
			__int64 rClass1 = TDA_CreateClass(tdaKernel, "ABC");
			cout << "createClass(ABC) returned " << rClass1 << endl;
			__int64 rClass2 = TDA_FindClass(tdaKernel, "ABC");
			cout << "findClass(ABC) returned " << rClass2 << endl;

			__int64 rClass3 = TDA_CreateClass(tdaKernel, "DEF");
			cout << "createClass(DEF) returned " << rClass3 << endl;

			TDA_FreeReference(tdaKernel, rClass1);
			TDA_FreeReference(tdaKernel, rClass2);
			TDA_FreeReference(tdaKernel, rClass3);

			b = TDA_StartSave(tdaKernel);
			if (!b) {
				cout << "startSave() failed." << endl;
				b = false;

				// no need to call cancelSave() here, since
				// cancelSave() is intended to be called after
				// successful startSave()
			}
			else {
				cout << "startSave() succeeded." << endl;
				b = TDA_FinishSave(tdaKernel);
				cout << "finishSave() returned " << b << "." << endl;
			}

			cout << "Closing TDA kernel..." << endl;
			TDA_Close(tdaKernel);
			cout << "TDA Kernel closed." << endl;

			if (b) { // save was successful
				cout << "Re-opening the saved repository." << endl;
				b = TDA_Open(tdaKernel, defRepLocation.c_str());
				cout << "open() returned: " << b << endl;
				if (b) {
					__int64 rClass1 = TDA_FindClass(tdaKernel, "ABC");
					cout << "findClass(ABC) returned " << rClass1 << endl;
					__int64 rClass2 = TDA_FindClass(tdaKernel, "DEF");
					cout << "findClass(DEF) returned " << rClass2 << endl;
					cout << "Freeing references " << rClass1 << " " << rClass2 << "..." << endl;
					TDA_FreeReference(tdaKernel, rClass1);
					TDA_FreeReference(tdaKernel, rClass2);
					rClass1 = TDA_FindClass(tdaKernel, "ABC");
					cout << "findClass(ABC) returned " << rClass1 << endl;
					cout << "Freeing reference " << rClass1 << "..." << endl;
					TDA_FreeReference(tdaKernel, rClass1);
					rClass2 = TDA_FindClass(tdaKernel, "DEF");
					cout << "findClass(DEF) returned " << rClass2 << endl
						 << endl;
					cout << "We will not free the reference for DEF now. We'll traverse the classes..." << endl;

					__int64 it = TDA_GetIteratorForClasses(tdaKernel);
					__int64 rCls = TDA_ResolveIteratorFirst(tdaKernel, it);
					while (rCls != 0) {
						cout << "  Found class: reference=" << rCls << ", name=" << TDA_GetClassName(tdaKernel, rCls) << endl;
							// NOTE: Do not free the returned char* value! It will be freed automatically.
							// It is guaranteed that the returned char* value will be valid in this
							// thread until the next call of some TDA_* function.
							// Other parallel threads may continue to call TDA_* functions ---
							// they will have their own space for char* return values.
						__int64 rPrevCls = rCls;
						rCls = TDA_ResolveIteratorNext(tdaKernel, it);
						TDA_FreeReference(tdaKernel, rPrevCls);
						/* variant:
						 * TDA_FreeReference(tdaKernel, rCls);
						 * rCls = TDA_ResolveIteratorNext(tdaKernel, it);
						 */
					}
					TDA_FreeIterator(tdaKernel, it);


					cout << "OK, now we can free the reference for DEF." << endl;
					TDA_FreeReference(tdaKernel, rClass2);
					cout << "Closing TDA kernel..." << endl;
					TDA_Close(tdaKernel);
					cout << "TDA Kernel closed." << endl;
				}
			}
		} // if open was ok


	TDA_FreeTDAKernelReference(tdaKernel);

	return 0;
}
