#!/bin/bash
{ time g++ "$1" -o "$2" 2> "$3" } 2> "$4"
