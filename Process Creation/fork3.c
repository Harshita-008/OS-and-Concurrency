#include <stdio.h>
#include <unistd.h>

int main() {
    fork();
    for (int i = 0; i < 5; i++) {
        printf("PID: %d, i = %d\n", getpid(), i);
        sleep(1);
    }
    return 0;
}