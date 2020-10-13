# Grader

## Executor

Safely executes commands such as compile or run.

## Execution time and memory

Requires gnu-time `brew install gnu-time`: `gtime --format="%S-%U-%e-%P-%M-%K" --output="output.txt" --append ./compiled-code.o`

- `%S` Total number of CPU-seconds that the process spent in kernel mode.
- `%U` Total number of CPU-seconds that the process spent in user mode.
- `%e` Elapsed real time (in seconds). - for debugging
- `%P` Percentage of the CPU that this job got, computed as (%U + %S) / %E. - for debugging
- `%M` Maximum resident set size of the process during its lifetime, in Kbytes.

We use `%S + %U` for precise execution time assuming process completed within 2x time limit. We use `%M` for precise
execution memory assuming process completed within 2x memory limit. 2x time and memory limit is enforced on a Docker
container running complied code.

- https://man7.org/linux/man-pages/man1/time.1.html
- https://docs.docker.com/config/containers/resource_constraints/
