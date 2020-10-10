#!/bin/bash
# shopt -s nullglob

if [ $# -ne 3 ]; then
    printf "Wrong number of arguments.\n"
    exit 1
fi

if [ ! -f "$1" ]; then
    printf "File doesn't exist, are you in the right directory?\n"
    exit 1
fi

g++ "$1" -o "$2" 2> "$3"

if [ $? -ne 0 ]; then
    # if compiling failed
    exit 1
fi
