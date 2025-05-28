#include <stdio.h>
#include <unistd.h>

int main() {
    fork();
    fork();
    printf("Hello world: %d\n", getpid());
    // printf("PID: %d\n", getpid());
    return 0;
}

// p
// first fork -> spawn a child process
// p    c1
// next fork commands comes in 
// c2   c3