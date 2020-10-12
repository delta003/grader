#!/bin/bash
# shopt -s nullglob

if [ $# -gt 2 ]; then
    printf "Wrong number of arguments.\n"
    return 1
fi

if [ ! -f "$1" ]; then
    printf "File doesn't exist, have you been playing with the script?\n"
    return 1
fi

run_command="./$1.out"
if [ $# -eq 2 ]; then
    run_command="${run_command} < $2"
elif [ $# -eq 3 ]; then
    run_command="${run_command} < $2 > $3"
fi

eval $run_command
