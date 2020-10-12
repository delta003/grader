#  (c) Copyright 2020 Petlja. All rights reserved.
import subprocess

for i in range(0, 10):
    subprocess.run(['time', './benchmark-2.o'])
