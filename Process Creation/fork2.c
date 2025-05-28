#include <stdio.h>
#include <unistd.h>

int main() {
    int pid = fork();

    if (pid > 0) {
        printf("👨 Parent Process, PID: %d\n", getpid());
    } else if (pid == 0) {
        printf("🧒 Child Process, PID: %d, Parent PID: %d\n", getpid(), getppid());
    } else {
        printf("❌ Fork failed!\n");
    }

    return 0;
}