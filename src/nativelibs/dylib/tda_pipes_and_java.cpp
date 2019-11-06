// new-safe

#if defined(_WIN32) || defined(_WIN64)
#include <windows.h>
#else
#include <dlfcn.h> // Linux, MacOS for loading dynamic libraries
#include <sys/types.h>
#include <sys/wait.h>
#include <signal.h> // for terminating a piped process
#include <unistd.h> // for pipe(), usleep()...
#endif

#include <stdio.h>
#include <string.h>

#include <string>
#include <vector>
using namespace std;

#include <jni.h>
#include "tda_unicode.h"

#define TDA_EXPORT
#include "tdakernel.h"

#if defined(_WIN32) || defined(_WIN64)
class WindowsProcessHandle {
public:
  HANDLE hProcess;
  HANDLE hChildInputStream;
  HANDLE hChildOutputStream;
};

DWORD WINAPI StdErrThreadFunction( LPVOID hStdErrRead )
{
//	AllocConsole();

	HANDLE h = GetStdHandle(STD_ERROR_HANDLE);

	DWORD read = 0, written = 0;

	char buffer[1025];

	while (ReadFile((HANDLE)hStdErrRead, buffer, 1024, &read, NULL)) {
		WriteConsole(h, (LPVOID)buffer, read, &written, NULL);
	}

	CloseHandle((HANDLE)hStdErrRead);
	return 0;
}

TDAEXTERN void* TDACALL TDA_LaunchPipedProcess(const char* program, const char** args) // Windows-version
  // Launches an external native program.
  //   program  - the full path to the program to execute in UTF-8 encoding;
  //   args     - an array of command-line arguments NOT including the program name as the first argument;
  //              args have to be NULL-terminated, i.e, the last argument must be NULL;
  //   Returns:   a libtdakernel-specific handle for the launched process (or NULL on error).
{
  HANDLE child_stdin_read, child_stdin_write;
  HANDLE child_stdout_read, child_stdout_write;
  HANDLE child_stderr_read, child_stderr_write;

  SECURITY_ATTRIBUTES sa = { 0 };
  sa.nLength = sizeof(sa);
  sa.lpSecurityDescriptor = NULL;
  sa.bInheritHandle = true;

  if (!CreatePipe(&child_stdin_read, &child_stdin_write, &sa, 0))
    return NULL;

  if (!CreatePipe(&child_stdout_read, &child_stdout_write, &sa, 0)) {
    CloseHandle(child_stdin_read);
    CloseHandle(child_stdin_write);
    return NULL;
  }

  if (!CreatePipe(&child_stderr_read, &child_stderr_write, &sa, 0)) {
    CloseHandle(child_stdin_read);
    CloseHandle(child_stdin_write);
    CloseHandle(child_stdout_read);
    CloseHandle(child_stdout_write);
    return NULL;
  }

  if ((!SetHandleInformation(child_stdin_write, HANDLE_FLAG_INHERIT, 0))
    ||(!SetHandleInformation(child_stdout_read, HANDLE_FLAG_INHERIT, 0))
    ||(!SetHandleInformation(child_stderr_read, HANDLE_FLAG_INHERIT, 0))) {
      CloseHandle(child_stdin_read);
      CloseHandle(child_stdin_write);
      CloseHandle(child_stdout_read);
      CloseHandle(child_stdout_write);
      CloseHandle(child_stderr_read);
      CloseHandle(child_stderr_write);
      return NULL;
  }

  STARTUPINFOW si = { 0 };
  si.cb = sizeof( si );
  si.hStdInput = child_stdin_read;
  si.hStdOutput = child_stdout_write;
  si.hStdError = child_stderr_write;
  si.dwFlags = STARTF_USESTDHANDLES; // | STARTF_USESHOWWINDOW;
  //si.wShowWindow = SW_HIDE;

  PROCESS_INFORMATION pi = { 0 };
  
  wchar_t wCmdProg[8192];
  wchar_t wCmdLine[8192];
  
  size_t remainingN = 8192*sizeof(WCHAR);
  size_t n = UTF8ToUTF16(program, 0, wCmdProg, remainingN, true);
  if (errno != 0)
    return NULL;
  
  wchar_t *wTailPtr = wCmdLine;
  
  if ((wcsstr(wCmdProg, L" ")!=NULL) || (wcsstr(wCmdProg, L"\t")!=NULL)) {
    // adding quotes...
    if (remainingN < n + 2*sizeof(char))
        return NULL; // no space to add quotes...  
  
    wTailPtr[0] = L'\"';
    wTailPtr++;
    remainingN -= sizeof(wchar_t);    
    wcscpy(wTailPtr, wCmdProg);  
    remainingN -= n; // remaining space (in bytes) in wCmdLine
    wTailPtr += (n/2)-1; // a pointer to the terminating L'\0';
    *wTailPtr = L'\"';
    wTailPtr++;
    remainingN -= sizeof(wchar_t);
    *wTailPtr = 0x0000;        
  }
  else {
    wcscpy(wTailPtr, wCmdProg);  
    remainingN -= n; // remaining space (in bytes) in wCmdLine
    wTailPtr += (n/2)-1; // a pointer to the terminating L'\0';
                         // we will overwrite the last L'\0' when appending an argument;
                         // then we will add another L'\0'
  }
  
  // appending arguments to wCmdLine...
  // invariant: wTailPtr points to the last L'\0' and remainingN is the size of the remaining buffer in BYTES, NOT INCLUDING the last L'\0' 
  while ((args!=NULL) && (*args!=NULL)) {
    *wTailPtr = (WCHAR)' '; // overwriting the last L'\0'...
    wTailPtr++;
    
    if ((strstr(*args, " ")!=NULL) || (strstr(*args, "\t")!=NULL)) {
      // appending the next argument embedded in quotes...
      if (remainingN < sizeof(WCHAR))
        return NULL; // could not add the first quote sign...
      *wTailPtr = (WCHAR)'\"'; // the first quote sign
      wTailPtr++;
      remainingN-=sizeof(WCHAR);
      
      n = UTF8ToUTF16(*args, 0, wTailPtr, remainingN, true);
      if (errno != 0)
        return NULL;
      remainingN-=n;
      wTailPtr+=(n/2)-1; // pointer to the last L'\0'
      
      *wTailPtr = (WCHAR)'\"'; // the second quote sign --- overwriting the last '\0' 
      wTailPtr++;
      
      if (remainingN < sizeof(WCHAR))
        return NULL; // could not add the new L'\0'
        
      *wTailPtr = 0x0000; // adding the new L'\0', and keeping the pointer on it... 
      remainingN-=sizeof(WCHAR);
            
    }
    else {
      // appending the argument without adding the quotes...
      n = UTF8ToUTF16(*args, 0, wTailPtr, remainingN, true);
      if (errno != 0)
        return NULL;
      remainingN-=n;
      wTailPtr+=(n/2)-1; // pointing to the new last L'\0'
    }
    args++;
  }
  
  /*FILE *f = fopen("D:\\cmdline.txt", "wt");
  fwprintf(f, L"%s\n", wCmdLine);
  fclose(f);*/
  //SetCurrentDirectoryW(thisLibraryFullPathW.c_str());
  chdir_utf8(TDA_GetTDAKernelLibraryPath());

  WindowsProcessHandle *retVal;
  try {
    retVal = new WindowsProcessHandle();
  }
  catch(...) {
    retVal = NULL;
  }

  if (!CreateProcessW(wCmdProg, wCmdLine, &sa, NULL, true, 0, NULL, NULL, &si, &pi) || (retVal==NULL)) {
	fprintf(stderr, "TDA Kernel library error: Could not launch the piped process %s.\n", program);
    CloseHandle(child_stdin_read);
    CloseHandle(child_stdin_write);
    CloseHandle(child_stdout_read);
    CloseHandle(child_stdout_write);
    CloseHandle(child_stderr_read);
    CloseHandle(child_stderr_write);

    return NULL;
  }

  CloseHandle(child_stdin_read);
  CloseHandle(child_stdout_write);
  CloseHandle(child_stderr_write);

  //CloseHandle(child_stderr_read);
  DWORD dwThreadId;
  HANDLE hThread = CreateThread(
              NULL,                   // default security attributes
              0,                      // use default stack size
              StdErrThreadFunction,   // thread function name
              child_stderr_read,      // argument to thread function
              0,                      // use default creation flags
              &dwThreadId);
  if (hThread == NULL)
	  CloseHandle(child_stderr_read); // we are not able to redirect child's stderr

  // freeing thread handle...
  CloseHandle(pi.hThread);
  

  // keeping only child_stdin_write and child_stdout_read...
  retVal->hChildInputStream = child_stdin_write;
  retVal->hChildOutputStream = child_stdout_read;
  // ... and also the process handle
  retVal->hProcess = pi.hProcess;


  return retVal; 
}
#else
class UnixProcessHandle {
public:
  int pid;
  FILE *fChildInput;
  FILE *fChildOutput;
};

TDAEXTERN void* TDACALL TDA_LaunchPipedProcess(const char* program, const char** args)
  // Launches an external native program.
  //   program  - the full path to the program to execute in UTF-8 encoding;
  //   args     - an array of command-line arguments NOT including the program name as the first argument;
  //              args have to be NULL-terminated, i.e, the last argument must be NULL;
  //   Returns:   a libtdakernel-specific handle for the launched process (or NULL on error).
{
  // creating retVal...
  UnixProcessHandle *retVal;
  try {
    retVal = new UnixProcessHandle();
  }
  catch(...)
  {
    return NULL;
  }
  
  // updating args...
  int n=0;  
  if (args!=NULL) {
    const char** ptr = args;
    while (*ptr) {
      n++;
      ptr++;
    }
  }
  const char** argsWithProgramName = (const char**)malloc(sizeof(char*)*(n+2));
  if (argsWithProgramName == NULL) {
    delete retVal;
    return NULL;
  }
  
  // program name without a path
  argsWithProgramName[0] = program;
  while (strstr(argsWithProgramName[0], "/") != NULL)
    argsWithProgramName[0] = strstr(argsWithProgramName[0], "/")+1;
  
  // arguments...
  for (int i=0; i<n; i++)
    argsWithProgramName[i+1] = args[i];
    
  // terminating NULL...
  argsWithProgramName[n+1] = NULL;
    

  int childInputPipe[2], childOutputPipe[2];
  if (pipe(childInputPipe)<0)
    return NULL;
  if (pipe(childOutputPipe)<0) {
    close(childInputPipe[0]);
    close(childInputPipe[1]);
    delete retVal;
    free(argsWithProgramName);
    return NULL;
  }
  
   
  
  int pid = (int)fork();
  if (pid < 0) {  // fork error
	fprintf(stderr, "TDA Kernel library error: Could not fork for the piped process %s.\n", program);
    close(childInputPipe[0]);
    close(childInputPipe[1]);
    close(childOutputPipe[0]);
    close(childOutputPipe[1]);
    delete retVal;
    free(argsWithProgramName);
    return NULL;
  }
  if (pid == 0) {
    // !!!CHILD!!!
    if (STDOUT_FILENO!=childOutputPipe[1]) {
      dup2(childOutputPipe[1],STDOUT_FILENO);
      close(childOutputPipe[1]);
    }
    close(childOutputPipe[0]); // the child does not need the read end of its output pipe
    if (STDIN_FILENO!=childInputPipe[0]) {
      dup2(childInputPipe[0],STDIN_FILENO);
      close(childInputPipe[0]);
    }
    close(childInputPipe[1]); // the child does not need the write end of its input pipe
    execv(program, (char* const*)argsWithProgramName);
	fprintf(stderr, "TDA Kernel library error: execv failed for the piped process %s.\n", program);
    _exit(-1); // execv failed    
  }

  // !!!PARENT CONTINUES!!!
  free(argsWithProgramName);
   
  close(childOutputPipe[1]); // the parent does not need the write end of child's output pipe
  close(childInputPipe[0]); // the parent does not need the read end of child's input pipe    
  
  retVal->pid = pid;
  retVal->fChildInput = fdopen(childInputPipe[1], "w");
  retVal->fChildOutput = fdopen(childOutputPipe[0], "r");
  
  if ( (retVal->fChildInput==NULL) || (retVal->fChildOutput==NULL) ) {
    if (retVal->fChildInput==NULL)
      close(childInputPipe[1]);
    else
      fclose(retVal->fChildInput);
    if (retVal->fChildOutput==NULL)
      close(childOutputPipe[0]);
    else
      fclose(retVal->fChildOutput);
    delete retVal;
    return NULL;
  }
  
  return retVal;
}
#endif 

/*void* TDACALL TDA_LaunchPipedProcessViaCommandProcessor(const char* program, const char** args)
{
	int n = 0;
	if (args != 0) {
		for (const char** p=args; *p!=NULL; p++) {
			n++;
		}
	}
	const char** newArgs = new const char*[n+2];
	newArgs[0] = "/k";
	newArgs[1] = program;
	for (int i=0; i<n; i++)
		newArgs[i+2] = args[i];


	void* retVal = NULL;

	#if defined(_WIN32) || defined(_WIN64)
	retVal = TDA_LaunchPipedProcess(getenv("COMSPEC"), newArgs);
	#else
	not implemented
	#endif

	delete[] newArgs;
	return retVal;
}*/

TDAEXTERN void* TDACALL TDA_GetParentPipedProcess()
{
  #if defined(_WIN32) || defined(_WIN64)
  WindowsProcessHandle *retVal;
  try {
    retVal = new WindowsProcessHandle();
  }
  catch(...) {
    return NULL;
  }
  DWORD ppid = TDA_GetParentProcessID();
  retVal->hProcess = OpenProcess(SYNCHRONIZE, FALSE/*do not inherit handles*/, ppid);
  retVal->hChildInputStream = GetStdHandle(STD_OUTPUT_HANDLE);
  retVal->hChildOutputStream = GetStdHandle(STD_INPUT_HANDLE);
  return retVal;
  #else
  UnixProcessHandle *retVal;
  try {
    retVal = new UnixProcessHandle();
  }
  catch(...)
  {
    return NULL;
  }
  retVal->pid = TDA_GetParentProcessID();
  retVal->fChildInput = stdout;
  retVal->fChildInput = stdin;
  return retVal;   
  #endif
}

TDAEXTERN bool TDACALL TDA_ReadProcessOutputStream(void* hProcess, void* buffer, unsigned int size, unsigned int &read)
{
  if (hProcess == NULL)
    return 0;
  #if defined(_WIN32) || defined(_WIN64)
  WindowsProcessHandle *h = (WindowsProcessHandle*)hProcess;
  
  DWORD _read;
  bool retVal = ReadFile(h->hChildOutputStream, buffer, size, &_read, NULL);
  read = _read;
  return retVal;
  #else
  UnixProcessHandle *h = (UnixProcessHandle*)hProcess;
  read = fread(buffer, 1, size, h->fChildOutput);
  if (read == size)
    return true;
  else
    return (ferror(h->fChildOutput)==0);
  #endif
}

TDAEXTERN bool TDACALL TDA_WriteProcessInputStream(void* hProcess, void* buffer, unsigned int size, unsigned int &written)
{
  if (hProcess == NULL)
    return 0;
  #if defined(_WIN32) || defined(_WIN64)
  WindowsProcessHandle *h = (WindowsProcessHandle*)hProcess;  
  DWORD _written;
  bool retVal = WriteFile(h->hChildInputStream, buffer, size, &_written, NULL);
  written = _written;
  return retVal;
  #else
  UnixProcessHandle *h = (UnixProcessHandle*)hProcess;
  written = fwrite(buffer, 1, size, h->fChildInput);
  if (written == size)
    return true;
  else
    return (ferror(h->fChildInput)==0);
  #endif
}

TDAEXTERN bool TDACALL TDA_IsPipedProcessTerminated(void* hProcess)
{
  if (hProcess == NULL)
    return false;
#if defined(_WIN32) || defined(_WIN64)
  WindowsProcessHandle *h = (WindowsProcessHandle*)hProcess;
  return (WaitForSingleObject(h->hProcess, 0) == WAIT_OBJECT_0);
  // variant: via GetExitCodeProcess
#else
  UnixProcessHandle *h = (UnixProcessHandle*)hProcess;
  int status;
  waitpid(h->pid, &status, WNOHANG);
  return (WIFEXITED(status)) || (WIFSIGNALED(status));
#endif
}

TDAEXTERN void TDACALL TDA_ReleasePipedProcess(void* hProcess, bool terminate)
  // Closes the process handle and input/output pipe handles (if any).
  //   terminate - close the handles and terminate the process, or simply close the handles.
{
  if (hProcess == NULL)
    return;
  #if defined(_WIN32) || defined(_WIN64)
  WindowsProcessHandle *h = (WindowsProcessHandle*)hProcess;
  
  CloseHandle(h->hChildInputStream);
  CloseHandle(h->hChildOutputStream);
  
  if (terminate)
    TerminateProcess(h->hProcess, 0);
  CloseHandle(h->hProcess);  
  
  delete h;
  #else
  UnixProcessHandle *h = (UnixProcessHandle*)hProcess;
  fclose(h->fChildInput);
  fclose(h->fChildOutput);
  if (terminate)
    kill(h->pid, SIGKILL);
  delete h;
  #endif
}

#if defined(_WIN32) || defined(_WIN64)
extern string _thisLibraryFullPath_8_3;
#else
extern string _thisLibraryFullPath_utf8;
#endif

const char* Internal_GetJavaHome(bool sameBits); // see below

TDAEXTERN const char* TDACALL TDA_GetJavaHomeAnyBits()
{
	return Internal_GetJavaHome(false);
}

TDAEXTERN const char* TDACALL TDA_GetJavaHomeSameBits()
{
	return Internal_GetJavaHome(true);
}

const char* Internal_GetJavaHome(bool sameBits)
  // Returns: the Java home directory in standard UTF-8 encoding or NULL, if Java not found.
{
  char *PATH = getenv_utf8_malloc("PATH");
  if (PATH == NULL)
    return NULL;

  // searching, where java.exe resides...
  #if defined(_WIN32) || defined(_WIN64)
  char *curPath = strtok(PATH, ";");
  #else
  char *curPath = strtok(PATH, ":"); // another path delimiter
  #endif
  while (curPath != NULL) {
    string fileName = curPath;
    #if defined(_WIN32) || defined(_WIN64)
    fileName += "\\javaw.exe";
    #else
    fileName += "/java";
    #endif
    FILE *f = fopen_utf8(fileName.c_str(), "rb");

	#if defined(_WIN32) || defined(_WIN64)
    if (f == NULL) { // javaw.exe not found; trying java.exe
        fileName = curPath;
        fileName += "\\java.exe";
        f = fopen_utf8(fileName.c_str(), "rb");
    }
	#endif

    if (f != NULL) {
        // OK, java.exe found
        fclose(f);
      
        // trying to launch PrintJavaHomeUTF Java class...
      
		#if defined(_WIN32) || defined(_WIN64)
        string arg0 = "-Djava.class.path="+
        		_thisLibraryFullPath_8_3 + ";" +
        		_thisLibraryFullPath_8_3 + "\\src_tdakernel;" +
        		_thisLibraryFullPath_8_3 + "\\tdakernel.jar;" +
        		_thisLibraryFullPath_8_3 + "\\..\\src_tdakernel;" +
        		_thisLibraryFullPath_8_3 + "\\..\\tdakernel.jar";
		#else
        //string arg0 = "-Djava.class.path=./:src_tdakernel:tdakernel.jar:../src_tdakernel:../tdakernel.jar";
        string arg0 = "-Djava.class.path="+
        		_thisLibraryFullPath_utf8 + ":" +
        		_thisLibraryFullPath_utf8 + "/src_tdakernel:" +
        		_thisLibraryFullPath_utf8 + "/tdakernel.jar:" +
        		_thisLibraryFullPath_utf8 + "/../src_tdakernel:" +
        		_thisLibraryFullPath_utf8 + "/../tdakernel.jar";
		#endif
        char qqq[16];
        sprintf(qqq, "%d", (int)(sizeof(void*)*8));
        string argBits = "-d"; argBits = argBits + qqq; // -d32 or -d64 depending on current bits
        string argLast = "PrintJavaHomeUTF";
                
        const char** args = NULL;
        try {
          args = new const char*[4];
        }
        catch(...) {
          args = NULL;
        }
        sameBits = false; // !!! Java11 does not support -d32/-d64 option
        if (args) {
	        args[0] = arg0.c_str();
	        if (sameBits) {
	        	args[1] = argBits.c_str();
	        	args[2] = argLast.c_str();
	        	args[3] = NULL;
	        }
	        else {
	        	args[1] = argLast.c_str();
	        	args[2] = NULL;
	        }
	        
	        const char* retVal = NULL;
	        
	        void* h = TDA_LaunchPipedProcess(fileName.c_str(), args);
	        if (h != NULL) {
	          unsigned int read;
	          unsigned char a, b;
	          unsigned int length;
	          if (TDA_ReadProcessOutputStream(h, &a, 1, read) && (read==1) && TDA_ReadProcessOutputStream(h, &b, 1, read) && (read==1)) {
	            length = (((unsigned int)a) << 8) | (unsigned int)b;
	            char q[10];
	            sprintf(q, "%d", length);
	            if (length > 0) {
	              char *modifiedUTF8=NULL, *standardUTF8=NULL;
	              try {
	                modifiedUTF8 = new char[length];
	                standardUTF8 = new char[length+1];
	              }
	              catch(...) {
	              }
	              if (modifiedUTF8 && standardUTF8) {
	                if (TDA_ReadProcessOutputStream(h, modifiedUTF8, length, read) && (read==length)) {
	                  UTF8ToStandardUTF8(modifiedUTF8, length, standardUTF8, length+1, true);
	                  if (errno == 0) {
	                    retVal = TDA_CreateReturnString(standardUTF8);
	                  }
	                }
	              }
                  if (standardUTF8)
	                delete[] standardUTF8;
	              if (modifiedUTF8)
	                delete[] modifiedUTF8;
	            }
	          }
	        } 
	        delete[] args;
	        
            if (retVal != NULL) {
      		  free(PATH);
              return retVal;
            }
            else
            	fprintf(stderr, "Java at \"%s\" was not suitable. We will try to find another Java.\n", fileName.c_str());
	    }
        
    } // if (f != NULL)

    #if defined(_WIN32) || defined(_WIN64)
    curPath = strtok(NULL, ";"); // continue search...
    #else
    curPath = strtok(NULL, ":"); // continue search...
    #endif
  }

  // not found
  free(PATH);
  return NULL;
}

void pathTo8_3(string &curPath)
{
  #if defined(_WIN32) || defined(_WIN64) // windows-only
	size_t wCurPathSize = UTF8ToUTF16(curPath.c_str(), 0, NULL, TDA_UNICODE_MAX_SIZE, 1);
	if (errno == 0) {
	  wchar_t *wCurPath = (wchar_t*)malloc(wCurPathSize);
	  if (wCurPath) {
	    UTF8ToUTF16(curPath.c_str(), 0, wCurPath, wCurPathSize, 1);
	    DWORD wShortPathLengthPlus1 = GetShortPathNameW(wCurPath, NULL, 0);
	    if (wShortPathLengthPlus1 > 0) {				      
	      wchar_t *wShortPath = (wchar_t*)malloc(wShortPathLengthPlus1*sizeof(wchar_t));
	      if (wShortPath) {
	        if (GetShortPathNameW(wCurPath, wShortPath, wShortPathLengthPlus1) == wShortPathLengthPlus1-1) {
	          size_t n = UTF16ToStandardUTF8(wShortPath, 0, NULL, TDA_UNICODE_MAX_SIZE, 1);
	          if (errno == 0) {
	            char* s = (char*)malloc(n);
	            if (s != NULL) {
	              UTF16ToStandardUTF8(wShortPath, 0, s, n, 1);
	              curPath = s; // OK!!!
	              free(s);
	            }
	          }
	        }
	        free(wShortPath);
	      }
	    }
	    free(wCurPath);
	  } 				
	}
  #endif
}

TDAEXTERN char** TDACALL TDA_UpdateJVMOptions(const char** options)
  // The "-Djava.class.path=..." and "-Djava.library.path=..." JVM options
  // are appended by TDA-specific paths.
  // The result is NULL in case of error.
  // The result (if not NULL) must be freed by calling TDA_FreeUpdatedJVMOptions().
  // The result contains an array of options, where the last opotion is NULL to specify the end-of-options.
{
	// Calculating n...
	
	int n = 0;
	
    const char** ptr = options;
    if (ptr) {
      while (*ptr) {
        n++;
        ptr++;
      }
    }
    
	///// Preparing options for the JVM to be created...

	int classPathIndex = -1;
	int libraryPathIndex = -1;
	//int checkJNIIndex = -1;

	vector<string> v;

	int i;
	for (i=0; i<n; i++) {
		if (options[i] != NULL) {
			string s = options[i];
			if (s.substr(0, 18) == "-Djava.class.path=")
				classPathIndex = i;
			else
			if ((s == string("-classpath")) || (s == string("-cp")))
				classPathIndex = i+1;
			else
			if (s.substr(0, 20) == "-Djava.library.path=")
				libraryPathIndex = i;
			//else
			//if (s == "-Xcheck:jni")
			//	checkJNIIndex = i;
			v.push_back(s);
			if (classPathIndex == i+1) {
			  if ((i+1 < n) && (options[i+1] != NULL))
			    v.push_back(options[i+1]);
			  else
			    v.push_back("");
			  i++;
			}
		}
	}

	#if defined(_WIN32) || defined(_WIN64) // Windows-specific
	string pathDelimiter = ";";
	string fileDelimiter = "\\";
	#else
	string pathDelimiter = ":";
	string fileDelimiter = "/";
	#endif

	// Searching for files *.jar and folders src_* to append to the java.class.path...
	string appendToClassPath;

	void* h = opendir_utf8(TDA_GetTDAKernelLibraryPath());
	if (h != NULL) {
		while (readdir_utf8(h)) {
			char* d_name = get_d_name_utf8(h);
			if ((strncmp(d_name, "src", 3) == 0)
			  ||((strlen(d_name)>=4)&&(strncmp(d_name+strlen(d_name)-4, ".jar", 4) == 0))
			) {
				string curPath = TDA_GetTDAKernelLibraryPath();
				curPath += fileDelimiter + d_name;
				
				// transforming curPath to the short (8.3-style) path name due to a bug in java.exe (for Windows),
				// which does not support UNICODE arguments...
				pathTo8_3(curPath);
				
				appendToClassPath += pathDelimiter + curPath; 
			}
		}
		closedir_utf8(h);
	}

	h = opendir_utf8( (string(TDA_GetTDAKernelLibraryPath())+fileDelimiter+"..").c_str() );
	if (h != NULL) {
		while (readdir_utf8(h)) {
			char* d_name = get_d_name_utf8(h);
			if ((strncmp(d_name, "src", 3) == 0)
			  ||((strlen(d_name)>=4)&&(strncmp(d_name+strlen(d_name)-4, ".jar", 4) == 0))
			) {
				string curPath = TDA_GetTDAKernelLibraryPath();
				curPath += fileDelimiter + ".." + fileDelimiter + d_name;
				
				// transforming curPath to the short (8.3-style) path name due to a bug in java.exe (for Windows),
				// which does not support UNICODE arguments...
				pathTo8_3(curPath);
				
				appendToClassPath += pathDelimiter + curPath; 
			}
		}
		closedir_utf8(h);
	}
	
	string thisLibraryPath = TDA_GetTDAKernelLibraryPath();
	pathTo8_3(thisLibraryPath);

	if (classPathIndex >= 0)
		v[classPathIndex] += pathDelimiter+thisLibraryPath+/*pathDelimiter+thisLibraryPath+fileDelimiter+"*"+*/appendToClassPath;
	else
		v.push_back("-Djava.class.path="+thisLibraryPath+/*pathDelimiter+thisLibraryPath+fileDelimiter+"*"+*/appendToClassPath);

	if (libraryPathIndex >= 0)
		v[libraryPathIndex] += pathDelimiter+thisLibraryPath;
	else
		v.push_back("-Djava.library.path="+thisLibraryPath);

	/*if (checkJNIIndex == -1)
		v.push_back("-Xcheck:jni");*/  // --- -Xcheck:jni not good for pipes, since it will print out messages to the pipe


	char* debugPort = getenv("TDA_KERNEL_DEBUG_PORT");
	if ((debugPort != NULL) && (debugPort[0]!='\0')) {
		v.push_back("-Xdebug");
		string arg = "-Xrunjdwp:transport=dt_socket,address=";
		arg += debugPort;
		arg += ",server=y";

		char* suspend = getenv("TDA_KERNEL_DEBUG_SUSPEND");
		if ((suspend != NULL) && (suspend[0]!='\0')) {
			arg += ",suspend=y";
			//MessageBox(NULL, arg.c_str(), "TDA Kernel debug", MB_OK);
		}
		else {
			arg += ",suspend=n";
			//MessageBox(NULL, arg.c_str(), "TDA Kernel debug", MB_OK);
		}
		v.push_back(arg);

	}
		
	char** retVal = (char**)malloc( sizeof(char*) * (v.size()+1) );
	if (retVal == NULL)
	  return retVal;
	  
	retVal[v.size()] = NULL; // the last end-of-options value
	  
	bool ok = true;
	for (unsigned int i=0; i<v.size(); i++) {
	  retVal[i] = strdup(v[i].c_str());
	  if (retVal[i] == NULL)
	    ok = false;
	}
	
	if (ok)
	  return retVal;
	else {
	  TDA_FreeUpdatedJVMOptions(retVal);
	  return NULL;
	}
}
  
TDAEXTERN void TDACALL TDA_FreeUpdatedJVMOptions(char** options)
{
  char** ptr = options;
  if (ptr == NULL)
    return;
  while (*ptr != NULL) {
    if (*ptr != NULL)
      free(*ptr); // freeing the current string (option)
    ptr++;
  }
  free(options); // freeing the whole array of pointers
}

TDAEXTERN void* TDACALL TDA_LaunchPipedJavaProcess(IN const char** jvmOptions, IN const char* mainClassName, IN const char** mainArgs)
{
  const char* _javaHome = TDA_GetJavaHomeAnyBits();
  if (_javaHome == NULL)
    return NULL;
    
  string javaHome = _javaHome;  
  
  char** updatedJvmOptions = TDA_UpdateJVMOptions(jvmOptions);  
  
  if (updatedJvmOptions == NULL)
    return NULL;
    
  int n1 = 0;
  char** ptr1 = updatedJvmOptions;
  while (ptr1 && (*ptr1 != NULL)) {
    n1++;
    ptr1++;
  }
  
  int n2 = 0;
  const char** ptr2 = mainArgs;
  while (ptr2 && (*ptr2 != NULL)) {
    n2++;
    ptr2++;
  }
  
  const char** totalArgs = (const char**) malloc( sizeof(char*)*(n1+1+n2+1) ); // +1 for class name and +1 for terminating NULL
  if (totalArgs == NULL) {
    TDA_FreeUpdatedJVMOptions(updatedJvmOptions);
    return NULL;
  }
    
  for (int i=0; i<n1; i++) {
    totalArgs[i] = updatedJvmOptions[i];
  }
  totalArgs[n1] = mainClassName;
  for (int i=0; i<n2; i++)
    totalArgs[n1+1+i] = mainArgs[i];
  totalArgs[n1+1+n2] = NULL;
  
  
  #if defined(_WIN32) || defined(_WIN64)
  void* retVal = TDA_LaunchPipedProcess( (javaHome + "\\bin\\javaw.exe").c_str(), (const char**)totalArgs); // javaw since we do not want a console to appear
  #else
  void* retVal = TDA_LaunchPipedProcess( (javaHome + "/bin/java").c_str(), (const char**)totalArgs);
  #endif  
  free(totalArgs);
  TDA_FreeUpdatedJVMOptions(updatedJvmOptions);    
  return retVal;
}

TDAEXTERN void** TDACALL TDA_GetExistingJavaVMs()
  // returns a NULL-terminated array of existing Java VMs, or NULL on error (e.g., if jvm library is not loaded yet);
  // The result (if not NULL) must be freed by calling TDA_FreeArrayOfExistingJavaVMs().
{
  jint JNICALL (*myJNI_GetCreatedJavaVMs)(JavaVM **, jsize , jsize *);
  #if defined(_WIN32) || defined(_WIN64)
  HINSTANCE hLibJVM = GetModuleHandle("jvm.dll");
  if (hLibJVM == NULL)
    return NULL;
  myJNI_GetCreatedJavaVMs = (jint JNICALL (*)(JavaVM **, jsize , jsize *))GetProcAddress(hLibJVM, "JNI_GetCreatedJavaVMs");
  if (myJNI_GetCreatedJavaVMs == NULL)
    return NULL;
  #else  
  void *h = dlopen("libjvm.so", RTLD_NOW); // Linux 32-bit or x64
  if (!h)
    h = dlopen("libjvm.dylib", RTLD_NOW); // MacOS
  if (!h)
    h = dlopen("libclient.dylib", RTLD_NOW); // MacOS
  if (!h)
    h = dlopen("libclient64.dylib", RTLD_NOW); // MacOS
  if (!h)
    h = dlopen("libserver.dylib", RTLD_NOW); // MacOS
  if (!h)
    return NULL;      
  
  myJNI_GetCreatedJavaVMs = (jint JNICALL (*)(JavaVM **, jsize , jsize *))dlsym(h, "JNI_GetCreatedJavaVMs"); // Linux-style
  if (dlerror()) {
    myJNI_GetCreatedJavaVMs = (jint JNICALL (*)(JavaVM **, jsize , jsize *))dlsym(h, "JNI_GetCreatedJavaVMs_Impl"); // MacOS-style
    if (dlerror()) {
      dlclose(h);
      return NULL; // not found 
    }
  }  
  #endif
  
  // Library loaded and myJNI_GetCreatedJavaVMs found...
    
  jsize virtualMachinesCount = 0;
  jint result = myJNI_GetCreatedJavaVMs(NULL, 0, &virtualMachinesCount);
  if (result != 0) {
      #if !defined(_WIN32) && !defined(_WIN64)
      dlclose(h);
      #endif
      return NULL;
  }

  typedef JavaVM* PJavaVM;

  JavaVM** virtualMachinesArr = (JavaVM**)malloc(sizeof(JavaVM*)*(virtualMachinesCount+1));
  if (virtualMachinesArr == NULL) {
    #if !defined(_WIN32) && !defined(_WIN64)
    dlclose(h);
    #endif
    return NULL;
  }

  jsize temp;
  result = myJNI_GetCreatedJavaVMs(virtualMachinesArr, virtualMachinesCount, &temp);
  if ((result != 0) || (temp != virtualMachinesCount)) {
    free(virtualMachinesArr);
    #if !defined(_WIN32) && !defined(_WIN64)
    dlclose(h);
    #endif
    return NULL;
  }
  
  #if !defined(_WIN32) && !defined(_WIN64)
  dlclose(h);
  #endif
    
  virtualMachinesArr[virtualMachinesCount] = NULL; // terminating NULL
  return (void**)virtualMachinesArr;
}

TDAEXTERN void TDACALL TDA_FreeArrayOfExistingJavaVMs(void** array)
{
  if (array)
    free(array);
}

typedef jint JNICALL (*JNI_CreateJavaVM_Function)(JavaVM **pvm, void **penv, void *args);

/*bool _INTERNAL_LoadJVMLibrary(
  void* &libHandle,
  JNI_CreateJavaVM_Function &myJNI_CreateJavaVM)
{
#if defined(_WIN32) || defined(_WIN64)
	// loading jvm.dll...
	// (however, it might already been loaded)
	libHandle = LoadLibraryW(L"jvm.dll");
	if (libHandle == NULL) { // not loaded, or not found
		// obtaining PATH...
		wchar_t *PATH = _wgetenv(L"PATH");
		if (PATH == NULL)
			return false;
		// duplicating...
		PATH = wcsdup(PATH);
		if (PATH == NULL)
			return false;
		// searching, where java.exe resides...
		wchar_t *curPath = wcstok(PATH, L";");
		while (curPath != NULL) {
			FILE *f = _wfopen((wstring(curPath) + L"\\client\\jvm.dll").c_str(), L"rb");
			if (f != NULL) {
				fclose(f);

				// OK, jvm.dll exists. Trying to load it...
				libHandle = LoadLibraryW( (wstring(curPath)+L"\\client\\jvm.dll").c_str() );
				if (libHandle != NULL) {
					// OK, loaded.
					free(PATH);
					break;
				}
				// else: could not load this jvm.dll
				//       (e.g., that jvm.dll was 32-bit, while this TDA Kernel 64-bit, or vice versa);
				//       continuing search in PATH...
			}

			f = _wfopen((wstring(curPath) + L"\\server\\jvm.dll").c_str(), L"rb");
			if (f != NULL) {
				fclose(f);

				// OK, jvm.dll exists. Trying to load it...
				libHandle = LoadLibraryW( (wstring(curPath)+L"\\server\\jvm.dll").c_str() );
				if (libHandle != NULL) {
					// OK, loaded.
					free(PATH);
					break;
				}
				// else: could not load this jvm.dll
				//       (e.g., that jvm.dll was 32-bit, while this TDA Kernel 64-bit, or vice versa);
				//       continuing search in PATH...
			}

			curPath = wcstok(NULL, L";");
		}

		if (curPath == NULL) { // not found
			free(PATH);
			return false;
		}

	}
	// jvm.dll loaded

	myJNI_CreateJavaVM = (jint JNICALL (*)(JavaVM **pvm, void **penv, void *args))GetProcAddress((HINSTANCE)libHandle, "JNI_CreateJavaVM");
	if (myJNI_CreateJavaVM == NULL) {
		FreeLibrary((HINSTANCE)libHandle);
		libHandle = NULL;
		myJNI_CreateJavaVM = NULL;
		return false;
	}

	return true;
#else // not Windows
    thisLibraryPath
    pathfind (getenv ("PATH"), "ls", "rx")
#endif
}*/

bool INTERNAL_LoadJVMLibrary(
  void* &libHandle,
  JNI_CreateJavaVM_Function &myJNI_CreateJavaVM)
{
    libHandle = NULL;
    myJNI_CreateJavaVM = NULL;
    
    const char* _javaHome = TDA_GetJavaHomeSameBits();

    if (_javaHome == NULL) {
  	  fprintf(stderr, "TDA Kernel library error: Java not found! "
    			      "You must have a %d-bit Java installed, and the \"java\" command has to use this Java version "
    			  	  "(you can check that by executing \"java -version\").\n", (int)sizeof(void*)*8);
      return false;
    }
    string javaHome = _javaHome;
    
    vector<string> libsToTry;
    
    #if defined(_WIN32) || defined(_WIN64)
    size_t n = UTF8ToUTF16(javaHome.c_str(), 0, NULL, TDA_UNICODE_MAX_SIZE, 1);
    if (errno != 0) {
      fprintf(stderr, "TDA Kernel library error: UTF8ToUTF16 error when converting Java home directory name.\n");
      return false;
    }
    wchar_t *w = (wchar_t *)malloc(n);
    if (w == NULL) {
      fprintf(stderr, "TDA Kernel library error: no memory when converting Java home directory name.\n");
      return false;
    }
    UTF8ToUTF16(javaHome.c_str(), 0, w, n, 1);
    wstring wjavaHome = w;
    free(w);
    
    // updating PATH to add Java's bin, from where the DLLs used by jvm.dll can be loaded
    wstring oldPath = _wgetenv(L"PATH");
    wstring newPath;
    newPath += wjavaHome;
    newPath += L"\\bin;";
    newPath += wjavaHome;
    newPath += L"\\jre\\bin;";
    newPath += oldPath;
    SetEnvironmentVariableW(L"PATH", newPath.c_str());

    HINSTANCE h = LoadLibraryW( (wjavaHome + L"\\bin\\server\\jvm.dll").c_str() );
    if (h == NULL)
      h = LoadLibraryW( (wjavaHome + L"\\bin\\client\\jvm.dll").c_str() );

    SetEnvironmentVariableW(L"PATH", oldPath.c_str());
      
    if (h == NULL) {
  	  fprintf(stderr, "TDA Kernel library error: Could not load Java VM library (however, Java was found at %s). "
  			          "You must have a %d-bit Java installed, and the \"java\" command has to use this Java version "
  			  	  	  "(you can check that by executing \"java -version\").\n", javaHome.c_str(), (int)sizeof(void*)*8);
      return false;
    }
      
    myJNI_CreateJavaVM = (JNI_CreateJavaVM_Function)GetProcAddress((HINSTANCE)h, "JNI_CreateJavaVM");
	if (myJNI_CreateJavaVM == NULL) {
        fprintf(stderr, "TDA Kernel library error: JNI_CreateJavaVM not found in Java VM library (however, Java was found at %s, and its JVM library was loaded).\n",
        		         javaHome.c_str());
		FreeLibrary(h);
		return false;
	}
	else {
		libHandle = h;
		return true;
	}        
    #else
    void* h = dlopen(  (javaHome+"/../Libraries/libjvm.dylib").c_str(), RTLD_NOW); // for MacOS    
    if (!h) {
      h = dlopen(  (javaHome+"/../Libraries/libclient.dylib").c_str(), RTLD_NOW); // for MacOS 32-bit
    }                
    if (!h) {
      h = dlopen(  (javaHome+"/../Libraries/libclient64.dylib").c_str(), RTLD_NOW); // for MacOS 64-bit
    }
    if (!h) {
      h = dlopen(  (javaHome+"/../Libraries/libserver.dylib").c_str(), RTLD_NOW); // for MacOS 64-bit
    }                
    if (!h) {
      h = dlopen(  (javaHome+"/lib/amd64/server/libjvm.so").c_str(), RTLD_NOW); // for Linux x64
    }
    if (!h) {
      h = dlopen(  (javaHome+"/lib/amd64/client/libjvm.so").c_str(), RTLD_NOW); // for Linux x64; // do we need this case ???
    }
    if (!h) {
      h = dlopen(  (javaHome+"/lib/i386/client/libjvm.so").c_str(), RTLD_NOW); // for Linux 32-bit;
    }
    if (!h) {
      h = dlopen(  (javaHome+"/lib/i386/server/libjvm.so").c_str(), RTLD_NOW); // for Linux 32-bit; // do we need this case ???
    }
    if (!h) {
   	  fprintf(stderr, "TDA Kernel library error: Could not load Java VM library (however, Java was found at %s). "
   			          "You must have a %d-bit Java installed, and the \"java\" command has to use this Java version "
   			  	  	  "(you can check that by executing \"java -version\").\n", javaHome.c_str(), (int)sizeof(void*)*8);
      return false; // could not load the library
    }
    
    dlerror(); // clearing dlerror
    void* faddr = dlsym(h, "JNI_CreateJavaVM");
    char* errmsg = dlerror();
    if (errmsg) { // we check for dlerror() since faddr may legally be 0 (NULL)
      faddr = dlsym(h, "JNI_CreateJavaVM_Impl"); // MacOS-style
      errmsg = dlerror();
    }
    
    if (errmsg) {
      fprintf(stderr, "TDA Kernel library error: neither JNI_CreateJavaVM, nor JNI_CreateJavaVM_Impl was found in Java VM library (however, Java was found at %s, and its JVM library was loaded).\n",
       		         javaHome.c_str());
      dlclose(h);
      return false; // could not find the function
    }
    
    libHandle = h;
    myJNI_CreateJavaVM = (JNI_CreateJavaVM_Function)faddr;
    return true;
    
    #endif  
}


void INTERNAL_FreeJVMLibrary(void* libHandle)
{
#if defined(_WIN32) || defined(_WIN64)
	if (libHandle != NULL)
		FreeLibrary((HINSTANCE)libHandle);
#else
    dlclose(libHandle);
#endif
}


TDAEXTERN bool TDACALL TDA_CreateNewJavaVM(const char** jvmOptions, void** jvm, void** jvmLibHandle)
{
	*jvm = NULL;
	*jvmLibHandle = NULL;

	///// Loading JVM library (jvm.dll)...
	void* libHandle;
	JNI_CreateJavaVM_Function myJNI_CreateJavaVM = NULL;

	bool ok = INTERNAL_LoadJVMLibrary(
				libHandle,
				myJNI_CreateJavaVM);

	if (!ok)
		return false;


	///// Preparing options for the JVM to be created...
	char** updatedOptions = TDA_UpdateJVMOptions(jvmOptions);

	vector<string> v;

	if (updatedOptions) {
		char** ptr = updatedOptions;
		while (*ptr) {
			v.push_back(*ptr);
			ptr++;
		}
	}
	TDA_FreeUpdatedJVMOptions(updatedOptions);

	///// Creating JVM...
    JavaVM* newjvm;
    JNIEnv* env;

    JavaVMInitArgs args;
    JavaVMOption opts[v.size()];
    args.version = JNI_VERSION_1_6;
    args.nOptions = v.size();
    args.options = opts;

    for (unsigned int i=0; i<v.size(); i++) {
    	opts[i].optionString = (char*)v[i].c_str();
    }

    jint result = myJNI_CreateJavaVM(&newjvm, (void**)&env, &args);
    if (result != 0) {
    	fprintf(stderr, "TDA Kernel library error: Error creating Java HotSpot virtual machine.\n");
    	INTERNAL_FreeJVMLibrary(libHandle);
    	return false;
    }

    *jvm = newjvm;
    *jvmLibHandle = libHandle;
    return true;
}

TDAEXTERN void TDACALL TDA_DestroyJavaVM(void* jvm, void* jvmLibHandle)
{
	if (jvm != NULL) {
		((JavaVM*)jvm)->DestroyJavaVM();
	}
	INTERNAL_FreeJVMLibrary(jvmLibHandle);
}

TDAEXTERN bool TDACALL TDA_LaunchJavaClass(IN void* jvm, IN const char* _mainClassName, IN const char** mainArgs)
{
  if ((jvm == NULL) || (_mainClassName == NULL))
    return false;
    
  char *mainClassName = strdup(_mainClassName);
  if (!mainClassName)
    return false;
    
  char *ptr = mainClassName;
  while (*ptr) {
    if (*ptr == '.')
      *ptr = '/';
    ptr++;
  }
  
  JNIEnv *env = NULL;
  ((JavaVM*)jvm)->AttachCurrentThread((void**)&env, NULL);
  
  if (env == NULL) {
    free(mainClassName);
    return false;
  }
      
  jclass jcls = env->FindClass(mainClassName);
  free(mainClassName);
  
  if (jcls == NULL)
    return false;
  
  // class found.
  
  jmethodID jmthd = env->GetStaticMethodID(jcls, "main", "([Ljava/lang/String;)V");
  if (jmthd == NULL) {
    env->DeleteLocalRef(jcls);
    return false;
  }
    
  // main() method found.
  
  // calculating the number of arguments...
  int n = 0;
  const char **mptr = mainArgs;
  while (mptr && (*mptr)) {
    n++;
    mptr++;
  }
  
  jobjectArray jargs = env->NewObjectArray(n, env->FindClass("java/lang/String"), NULL);
  if (jargs == NULL) {
    env->DeleteLocalRef(jcls);
    return false;
  }
    
  for (int i=0; i<n; i++) {   
    size_t len = UTF8ToModifiedUTF8(mainArgs[i], 0, NULL, TDA_UNICODE_MAX_SIZE, 1);
    if (errno) {
      env->DeleteLocalRef(jcls);
      env->DeleteLocalRef(jargs);
      return false;
    }

    char* s = (char*)malloc(len);
    if (s == NULL) {
      env->DeleteLocalRef(jcls);
      env->DeleteLocalRef(jargs);
      return false;
    }
      
    UTF8ToModifiedUTF8(mainArgs[i], 0, s, len, 1);
    jstring jstr = env->NewStringUTF(s);
    if (jstr == NULL) {
      env->DeleteLocalRef(jcls);
      env->DeleteLocalRef(jargs);
      return false;
    }
    env->SetObjectArrayElement(jargs, i, jstr);
    env->DeleteLocalRef(jstr);  
  }
  
  env->CallStaticVoidMethod(jcls, jmthd, jargs);  
  return true;  
}

TDAEXTERN const char* TDACALL TDA_LaunchJavaStringToStringClassMethod(IN void* jvm, IN const char* className, IN const char* methodName, IN const char* arg)
{
  if ((jvm == NULL) || (className == NULL))
    return NULL;

  char *mainClassName = strdup(className);
  if (!mainClassName)
    return NULL;

  char *ptr = mainClassName;
  while (*ptr) {
    if (*ptr == '.')
      *ptr = '/';
    ptr++;
  }

  JNIEnv *env = NULL;
  ((JavaVM*)jvm)->AttachCurrentThread((void**)&env, NULL);

  if (env == NULL) {
    free(mainClassName);
    return NULL;
  }

  jclass jcls = env->FindClass(mainClassName);
  free(mainClassName);

  if (jcls == NULL)
    return NULL;

  // class found.

  jmethodID jmthd = env->GetStaticMethodID(jcls, methodName, "(Ljava/lang/String;)Ljava/lang/String;");
  if (jmthd == NULL) {
    env->DeleteLocalRef(jcls);
    return NULL;
  }

  // method found.

    size_t len = UTF8ToModifiedUTF8(arg, 0, NULL, TDA_UNICODE_MAX_SIZE, 1);
    if (errno) {
      env->DeleteLocalRef(jcls);
      return NULL;
    }

    char* s = (char*)malloc(len);
    if (s == NULL) {
      env->DeleteLocalRef(jcls);
      return NULL;
    }

    UTF8ToModifiedUTF8(arg, 0, s, len, 1);
    jstring jstr = env->NewStringUTF(s);
    if (jstr == NULL) {
      env->DeleteLocalRef(jcls);
      return NULL;
    }

  jstring jRetStr = (jstring)env->CallStaticObjectMethod(jcls, jmthd, jstr);
  env->DeleteLocalRef(jcls);
  env->DeleteLocalRef(jstr);

  const char* modifiedUTF = env->GetStringUTFChars(jRetStr, NULL);
  const char* retVal = TDA_CreateReturnString(modifiedUTF);
  env->ReleaseStringUTFChars(jRetStr, modifiedUTF);
  env->DeleteLocalRef(jRetStr);

  return retVal;
}

#if defined(_WIN32) || defined(_WIN64)
class SharedMemoryHandle {
public:
  HANDLE hMapFile;
  LPTSTR pBuf;
};
#endif

TDAEXTERN void* TDACALL TDA_CreateSharedMemory(IN const char* memoryName, IN const unsigned int size)
{
#if defined(_WIN32) || defined(_WIN64)
	SharedMemoryHandle *h = new SharedMemoryHandle();
	h->hMapFile = CreateFileMapping(
	                 INVALID_HANDLE_VALUE,    // use paging file
	                 NULL,                    // default security
	                 PAGE_READWRITE,          // read/write access
	                 0,                       // maximum object size (high-order DWORD)
	                 size,                    // maximum object size (low-order DWORD)
	                 memoryName);             // name of mapping object
	if (h->hMapFile == NULL) {
		delete h;
		return NULL;
	}
	h->pBuf = (LPTSTR) MapViewOfFile(h->hMapFile,   // handle to map object
	                        FILE_MAP_ALL_ACCESS, // read/write permission
	                        0,
	                        0,
	                        size);
	if (h->pBuf == NULL) {
		CloseHandle(h->hMapFile);
		delete h;
		return NULL;
	}

	memset(h->pBuf, 0, size);

	return h;
#else
	return NULL; // not implemented for non-windows
#endif
}

TDAEXTERN unsigned char* TDACALL TDA_GetSharedMemoryByteArray(void* sharedMemory)
{
#if defined(_WIN32) || defined(_WIN64)
	if (sharedMemory == NULL)
		return NULL;
	SharedMemoryHandle *h = (SharedMemoryHandle *)sharedMemory;
	return (unsigned char*)(h->pBuf);
#else
	return NULL; // not implemented for non-windows
#endif
}

TDAEXTERN void TDACALL TDA_CloseSharedMemory(void* sharedMemory)
{
#if defined(_WIN32) || defined(_WIN64)
	if (sharedMemory == NULL)
		return;
	SharedMemoryHandle *h = (SharedMemoryHandle *)sharedMemory;
	UnmapViewOfFile(h->pBuf);
	CloseHandle(h->hMapFile);
	delete h;
#else
	return; // not implemented for non-windows
#endif
}

#if defined(_WIN32) || defined(_WIN64)

TDAEXTERN void TDACALL TDA_Sleep(IN const unsigned int ms)
{
	Sleep(ms);
}

#else

#include <sched.h>
TDAEXTERN void TDACALL TDA_Sleep(IN const unsigned int ms)
{
	if (ms == 0)
		sched_yield();
	else
		usleep(ms*1000);
}

#endif


