#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>

int main() {
    int fd = open("mypipe", O_WRONLY);
    char msg[] = "Hello from writer!, vbballleee balllee";
    write(fd, msg, strlen(msg) + 1);
    close(fd);
    return 0;
}