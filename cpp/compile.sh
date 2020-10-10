#!/bin/bash
# shopt -s nullglob

if [ $# -ne 1 ]; then
    printf "Wrong number of arguments.\n"
    return 1
fi

if [ ! -f "$1" ]; then
    printf "File doesn't exist, are you in the right directory?\n"
    return 1
fi

g++ "$1" -o "$1.out" -DLOCAL

if [ $? -ne 0 ]; then
    # if compiling failed
    return 1
fi
