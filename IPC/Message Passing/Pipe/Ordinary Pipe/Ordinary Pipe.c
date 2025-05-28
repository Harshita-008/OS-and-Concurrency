#include <stdio.h>
#include <unistd.h>
#include <string.h>

int main() {
    int fd[2];
    char message[] = "Hello from parent!";
    char buffer[100];

    pipe(fd); // fd[0] - read, fd[1] - write
    pid_t pid = fork();

    if (pid == 0) {
        close(fd[1]); // Close write in child
        read(fd[0], buffer, sizeof(buffer));
        printf("Child received: %s\n", buffer);
    } else {
        close(fd[0]); // Close read in parent
        write(fd[1], message, strlen(message)+1);
    }

    return 0;
}