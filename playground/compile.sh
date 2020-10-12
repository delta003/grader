#!/bin/bash
# shopt -s nullglob

if [ $# -ne 1 ]; then
    printf "Wrong number of arguments.\n"
    exit 1
fi

if [ ! -f "$1" ]; then
    printf "File doesn't exist, are you in the right directory?\n"
    exit 1
fi

g++ "$1" -o "$1.out" 2> "$1.error"

if [ $? -ne 0 ]; then
    # if compiling failed
    exit 1
fi
