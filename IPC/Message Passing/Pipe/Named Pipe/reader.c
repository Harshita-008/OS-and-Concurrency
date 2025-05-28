#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>

int main() {
    int fd = open("mypipe", O_RDONLY);
    char buffer[100];
    read(fd, buffer, sizeof(buffer));
    printf("Reader got: %s\n", buffer);
    close(fd);
    return 0;
}