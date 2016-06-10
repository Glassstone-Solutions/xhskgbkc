#!/bin/sh
cd /c/Users/Thompson/AndroidStudioProjects/TheDiaryMagazine
git status
echo -n "Enter branch commit message: "
read message
# git checkout dev
git add .
git commit -am "$message"
# git push
echo Press Enter...
read