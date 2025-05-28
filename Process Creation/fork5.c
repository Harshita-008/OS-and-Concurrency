#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>

int main() {
    int pid = fork();

    if (pid == 0) {
        execlp("ls", "ls", "/Users/yogeshkumar/Desktop", NULL);
        perror("execlp failed");
    } else {
        wait(NULL);
        printf("Done listing files.\n");
    }

    return 0;
}