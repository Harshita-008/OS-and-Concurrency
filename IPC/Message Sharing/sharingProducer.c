#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <unistd.h>

int main() {
    const char *name = "/my_shm";   // name of shared memory
    const int SIZE = 4096;
    const char *message = "Hello from POSIX Shared Memory! , hi balle balle";

    // Create shared memory object
    int shm_fd = shm_open(name, O_CREAT | O_RDWR, 0666);
    if (shm_fd == -1) {
        perror("shm_open");
        exit(1);
    }

    // Set size
    ftruncate(shm_fd, SIZE);

    // Memory map
    void *ptr = mmap(0, SIZE, PROT_WRITE, MAP_SHARED, shm_fd, 0);  //maps shared memory into virtual space
    if (ptr == MAP_FAILED) {
        perror("mmap");
        exit(1);
    }

    // Write to shared memory
    sprintf((char *)ptr, "%s", message);
    printf("👨 Writer wrote: %s\n", message);

    close(shm_fd);
    return 0;
}