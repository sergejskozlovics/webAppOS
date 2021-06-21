#!/usr/bin/env sh

# downloads and extracts the necessary subfolder from the graalvm macos archive stored at github;
# the archive is not saved locally but is piped to tar;
# we use wget (and not curl), since graalvm github URL points to a HTML redirect that curl cannot process
# (c) Sergejs Kozlovics, 2021
# license: CC BY 4.0

# to find newer releases, visit https://github.com/graalvm/graalvm-ce-builds/releases/


DEST_DIR=graalvm

# We rely on these variables:

# $CPUTYPE in macOS can be "x86_64" or "arm64"
# $OSTYPE can be, e.g., "darwin20.0" in macOS

# $HOSTTYPE in Linux can be "x86_64" or "aarch64"
# $OSTYPE=linux-gnu in Linux

if case $OSTYPE in darwin*) true;; *) false;; esac; then
  # macOS
  SRC_URL=https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-21.1.0/graalvm-ce-java16-darwin-amd64-21.1.0.tar.gz
  PATH_PREFIX=graalvm-ce-java16-21.1.0/Contents/Home
  PATH_PREFIX_LEN=3
else
  # not macOS
  # using $SHELL to get variables (since we can be now in a dummy shell))
  OSTYPE=$(echo "echo \$OSTYPE" | $SHELL)
  HOSTTYPE=$(echo "echo \$HOSTTYPE" | $SHELL)

  if [ $OSTYPE = "linux-gnu" ]; then
    # GNU/Linux
    if [ $HOSTTYPE = "x86_64" ]; then
      SRC_URL="https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-21.1.0/graalvm-ce-java16-linux-amd64-21.1.0.tar.gz"
    elif [ $HOSTTYPE = "aarch64" ]; then
      SRC_URL="https\://github.com/graalvm/graalvm-ce-builds/releases/download/vm-21.1.0/graalvm-ce-java16-linux-aarch64-21.1.0.tar.gz"
    else
      echo Unsupported Linux processor architecture $HOSTTYPE.
    fi
    PATH_PREFIX=graalvm-ce-java16-21.1.0
    PATH_PREFIX_LEN=1
  else
    echo I do not know how to install GraalVM for your OS $OSTYPE
    exit
  fi
fi

mkdir -p $DEST_DIR
wget -q -O - $SRC_URL | tar --directory $DEST_DIR --strip-components=$PATH_PREFIX_LEN -xvz $PATH_PREFIX
