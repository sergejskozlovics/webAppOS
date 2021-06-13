:: downloads and extracts graalvm for windows;
:: the archive is first downloaded from github locally, then extracted, then removed;
:: we use wget.exe to download graalvm archive, since graalvm github URL points to a HTML redirect that curl.exe cannot process;
:: we use curl.exe (available in Windows 10) to download wget.exe;
:: (c) Sergejs Kozlovics, 2021
:: licence: CC BY 4.0

curl.exe -o wget.exe https://eternallybored.org/misc/wget/1.21.1/64/wget.exe
wget.exe -O graalvm.zip https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-21.1.0/graalvm-ce-java11-windows-amd64-21.1.0.zip
powershell.exe -NoP -NonI -Command "Expand-Archive '.\graalvm.zip' '.'"
del .wget-hsts
del wget.exe
del graalvm.zip
ren graalvm-ce-java11-21.1.0 graalvm
