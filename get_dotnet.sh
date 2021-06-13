#!/usr/bin/env sh

# downloads and extracts dotnet sdk for macos or linux;;
# the archive is not saved locally but is piped to tar;
# (c) Sergejs Kozlovics, 2021
# license: CC BY 4.0

# to find newer releases, visit https://dotnet.microsoft.com/download/dotnet

DEST_DIR=dotnet

# We rely on these variables:

# $CPUTYPE in macOS can be "x86_64" or "arm64"
# $OSTYPE can be, e.g., "darwin20.0" in macOS

# $HOSTTYPE in Linux can be "x86_64" or "aarch64"
# $OSTYPE=linux-gnu in Linux

if case $OSTYPE in darwin*) true;; *) false;; esac; then
  # macOS
  SRC_URL="https://download.visualstudio.microsoft.com/download/pr/f4c0e2ca-6329-4138-a966-69db07876d93/f30fa96952d41bf4e661c0e404a288db/dotnet-sdk-5.0.301-osx-x64.tar.gz"
else
  # not macOS
  # using $SHELL to get variables (since we can be now in a dummy shell))
  OSTYPE=$(echo "echo \$OSTYPE" | $SHELL)
  HOSTTYPE=$(echo "echo \$HOSTTYPE" | $SHELL)

  if [ $OSTYPE = "linux-gnu" ]; then
    # GNU/Linux
    if [ $HOSTTYPE = "x86_64" ]; then
      SRC_URL="https://download.visualstudio.microsoft.com/download/pr/e1c236ec-c392-4eaa-a846-c600c82bb7f6/b13bd8b69f875f87cf83fc6f5457bcdf/dotnet-sdk-5.0.301-linux-x64.tar.gz"
    elif [ $HOSTTYPE = "aarch64" ]; then
      SRC_URL="https://download.visualstudio.microsoft.com/download/pr/574ddb7e-5fbc-4b28-ae76-2bb9c0d3f163/04d9d954b7d40c8d46b7c9067f421e03/dotnet-sdk-5.0.301-linux-arm64.tar.gz"
    else
      echo Unsupported Linux processor architecture $HOSTTYPE.
    fi
  else
    echo I do not know how to install GraalVM for your OS $OSTYPE
    exit
  fi
fi

mkdir -p $DEST_DIR
# wget -q -O - $SRC_URL | tar --directory $DEST_DIR -xvz
curl $SRC_URL | tar --directory $DEST_DIR -xvz

