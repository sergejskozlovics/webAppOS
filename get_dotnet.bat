:: downloads and extracts graalvm for windows;
:: the archive is first downloaded from github locally, then extracted, then removed;
:: we use wget.exe to download graalvm archive, since graalvm github URL points to a HTML redirect that curl.exe cannot process;
:: we use curl.exe (available in Windows 10) to download wget.exe;
:: (c) Sergejs Kozlovics, 2021
:: licence: CC BY 4.0

if .%PROCESSOR_ARCHITECTURE%==.AMD64 goto amd64
if .%PROCESSOR_ARCHITECTURE%==.ARM64 goto arm64
if .%PROCESSOR_ARCHITECTURE%==.X86 goto x86

echo Unknown processor architecture.
goto end
:amd64
curl.exe -o dotnet.zip https://download.visualstudio.microsoft.com/download/pr/af1d2dd6-4c53-4595-9437-fe923fe0d7f1/bbbf3d8f43bfdb35e84f2aa867290547/dotnet-sdk-5.0.301-win-x64.zip
goto extract

:arm64
curl.exe -o dotnet.zip https://download.visualstudio.microsoft.com/download/pr/d10d6804-70a7-4acc-93e3-79ce6fecc51d/b6ec394c56b43fcca1e332903d1fc1e8/dotnet-sdk-5.0.301-win-arm64.zip
goto extract

:x86
curl.exe -o dotnet.zip https://download.visualstudio.microsoft.com/download/pr/63764a19-21d6-4e7b-b0b1-dddc215143ee/0e7fddbacf4fd9c42e8dd7718e88cd4c/dotnet-sdk-5.0.301-win-x86.zip
goto extract

:extract
powershell.exe -NoP -NonI -Command "Expand-Archive '.\dotnet.zip' '.\dotnet'"
del dotnet.zip
goto end

:end
