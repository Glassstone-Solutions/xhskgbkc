#!/bin/sh
cd /c/Users/Thompson/AndroidStudioProjects/TheDiaryMagazine
git status
echo -n "Enter branch commit message: "
read -en 200 message
# git checkout dev
git add .
git commit -am "$message"
# git push
gradle :app:assembleRelease
echo Press Enter...
read