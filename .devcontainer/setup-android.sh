#!/bin/bash

set -euo pipefail

echo "Installing Android SDK and tools..."

# Download and install Android command line tools
cd /tmp
wget -q https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
mkdir -p "$HOME/android-sdk/cmdline-tools"
unzip -q -o commandlinetools-linux-9477386_latest.zip -d "$HOME/android-sdk/cmdline-tools"
rm -rf "$HOME/android-sdk/cmdline-tools/latest"
mv "$HOME/android-sdk/cmdline-tools/cmdline-tools" "$HOME/android-sdk/cmdline-tools/latest"

# Set environment variables early so subsequent commands see them
export ANDROID_HOME="$HOME/android-sdk"
export PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools"

# Persist environment variables for future shells
{
  echo "export ANDROID_HOME=$HOME/android-sdk"
  echo "export PATH=\$PATH:\$ANDROID_HOME/cmdline-tools/latest/bin:\$ANDROID_HOME/platform-tools"
} >> "$HOME/.bashrc"

# Accept licenses and install required packages; tolerate sdkmanager's occasional non-zero exits
yes | sdkmanager --licenses || true
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0" || true

echo "Android SDK installation complete!"
